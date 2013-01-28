/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.text;

import com.google.common.collect.ImmutableSet;

import java.util.ResourceBundle;
import java.util.Set;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareNavigator;
import org.eclipse.compare.ICompareNavigator;
import org.eclipse.compare.contentmergeviewer.TextMergeViewer;
import org.eclipse.compare.internal.CompareHandlerService;
import org.eclipse.compare.internal.MergeSourceViewer;
import org.eclipse.compare.internal.Utilities;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.ide.EMFCompareIDEPlugin;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareConstants;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.DynamicObject;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.AttributeChangeNode;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareTextMergeViewer extends TextMergeViewer {

	private static final String BUNDLE_NAME = EMFCompareTextMergeViewer.class.getName();

	private final ICompareEditingDomain fEditingDomain;

	private DynamicObject fDynamicObject;

	private ActionContributionItem fCopyDiffLeftToRightItem;

	private ActionContributionItem fCopyDiffRightToLeftItem;

	/**
	 * @param parent
	 * @param configuration
	 */
	public EMFCompareTextMergeViewer(Composite parent, CompareConfiguration configuration) {
		super(parent, configuration);
		fEditingDomain = (ICompareEditingDomain)getCompareConfiguration().getProperty(
				EMFCompareConstants.EDITING_DOMAIN);
		setContentProvider(new EMFCompareTextMergeViewerContentProvider(configuration));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.TextMergeViewer#copy(boolean)
	 */
	@Override
	protected void copy(boolean leftToRight) {
		Object input = getInput();
		if (input instanceof AttributeChangeNode) {
			final AttributeChange attributeChange = ((AttributeChangeNode)input).getTarget();
			final Comparison comparison = attributeChange.getMatch().getComparison();

			final Command copyCommand = fEditingDomain.createCopyAllNonConflictingCommand(comparison
					.getDifferences(), leftToRight, EMFCompareIDEPlugin.getDefault().getMergerRegistry());
			fEditingDomain.getCommandStack().execute(copyCommand);

			refresh();
		}
	}

	protected void copyDiff(boolean leftToRight) {
		Object input = getInput();
		if (input instanceof AttributeChangeNode) {
			final AttributeChange attributeChange = ((AttributeChangeNode)input).getTarget();

			final Command copyCommand = fEditingDomain.createCopyCommand(attributeChange, leftToRight,
					EMFCompareIDEPlugin.getDefault().getMergerRegistry());
			fEditingDomain.getCommandStack().execute(copyCommand);

			refresh();
		}
	}

	/**
	 * Inhibits this method to avoid asking to save on each input change!!
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#doSave(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	protected boolean doSave(Object newInput, Object oldInput) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ContentViewer#setInput(java.lang.Object)
	 */
	@Override
	public void setInput(Object newInput) {
		if (newInput == null) {
			// When we leave the current input
			Object oldInput = getInput();
			if (oldInput instanceof AttributeChangeNode) {
				final AttributeChange diff = ((AttributeChangeNode)oldInput).getTarget();
				final EAttribute eAttribute = diff.getAttribute();
				Match match = diff.getMatch();
				final EObject left = match.getLeft();
				final EObject right = match.getRight();
				Object oldLeftValue = left == null ? null : ReferenceUtil.safeEGet(left, eAttribute);
				Object oldRightValue = right == null ? null : ReferenceUtil.safeEGet(right, eAttribute);
				final String newLeftValue = new String(getContents(true));
				final Object newRightValue = new String(getContents(false));
				// TOOD: use equality helper to compare values && use EcoreUtil.convertToString to create the
				// sring rep of oldXXValue
				if (left != null && !newLeftValue.equals(oldLeftValue)
						&& getCompareConfiguration().isLeftEditable()) {
					// Save the change on left side
					fEditingDomain.getCommandStack().execute(
							new UpdateModelAndRejectDiffCommand(fEditingDomain.getChangeRecorder(), left,
									eAttribute, newLeftValue, diff, true));
				}
				if (right != null && !newRightValue.equals(oldRightValue)
						&& getCompareConfiguration().isRightEditable()) {
					// Save the change on right side
					fEditingDomain.getCommandStack().execute(
							new UpdateModelAndRejectDiffCommand(fEditingDomain.getChangeRecorder(), right,
									eAttribute, newRightValue, diff, false));

				}

			}
		}
		super.setInput(newInput);
	}

	/**
	 * @return the fDynamicObject
	 */
	public DynamicObject getDynamicObject() {
		if (fDynamicObject == null) {
			this.fDynamicObject = new DynamicObject(this);
		}
		return fDynamicObject;
	}

	@SuppressWarnings("restriction")
	protected final MergeSourceViewer getLeftSourceViewer() {
		return (MergeSourceViewer)getDynamicObject().get("fLeft"); //$NON-NLS-1$
	}

	@SuppressWarnings("restriction")
	protected final MergeSourceViewer getRightSourceViewer() {
		return (MergeSourceViewer)getDynamicObject().get("fRight"); //$NON-NLS-1$
	}

	protected final void setHandlerService(@SuppressWarnings("restriction") CompareHandlerService service) {
		getDynamicObject().set("fHandlerService", service); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.TextMergeViewer#createToolItems(org.eclipse.jface.action.ToolBarManager)
	 */
	@SuppressWarnings("restriction")
	@Override
	protected void createToolItems(ToolBarManager toolBarManager) {
		// avoid super to avoid NPE in org.eclipse.compare.internal.ViewerDescriptor.createViewer
		CompareHandlerService handlerService = CompareHandlerService.createFor(getCompareConfiguration()
				.getContainer(), getLeftSourceViewer().getSourceViewer().getControl().getShell());
		setHandlerService(handlerService);

		// Copy actions
		CompareConfiguration cc = getCompareConfiguration();
		if (cc.isRightEditable()) {
			Action copyLeftToRight = new Action() {
				@Override
				public void run() {
					copyDiff(true);
					navigate(true);
				}
			};
			Utilities.initAction(copyLeftToRight, getResourceBundle(), "action.CopyDiffLeftToRight."); //$NON-NLS-1$
			fCopyDiffLeftToRightItem = new ActionContributionItem(copyLeftToRight);
			fCopyDiffLeftToRightItem.setVisible(true);
			toolBarManager.appendToGroup("merge", fCopyDiffLeftToRightItem); //$NON-NLS-1$
			handlerService.registerAction(copyLeftToRight, "org.eclipse.compare.copyLeftToRight"); //$NON-NLS-1$
		}

		if (cc.isLeftEditable()) {
			Action copyRightToLeft = new Action() {
				@Override
				public void run() {
					copyDiff(false);
					navigate(true);
				}
			};
			Utilities.initAction(copyRightToLeft, getResourceBundle(), "action.CopyDiffRightToLeft."); //$NON-NLS-1$
			fCopyDiffRightToLeftItem = new ActionContributionItem(copyRightToLeft);
			fCopyDiffRightToLeftItem.setVisible(true);
			toolBarManager.appendToGroup("merge", fCopyDiffRightToLeftItem); //$NON-NLS-1$
			handlerService.registerAction(copyRightToLeft, "org.eclipse.compare.copyRightToLeft"); //$NON-NLS-1$
		}

		// Navigation
		final Action nextDiff = new Action() {
			@Override
			public void run() {
				endOfContentReached(true);
			}
		};
		Utilities.initAction(nextDiff, getResourceBundle(), "action.NextDiff.");
		ActionContributionItem contributionNextDiff = new ActionContributionItem(nextDiff);
		contributionNextDiff.setVisible(true);
		toolBarManager.appendToGroup("navigation", contributionNextDiff);

		final Action previousDiff = new Action() {
			@Override
			public void run() {
				endOfContentReached(false);
			}
		};
		Utilities.initAction(previousDiff, getResourceBundle(), "action.PrevDiff.");
		ActionContributionItem contributionPreviousDiff = new ActionContributionItem(previousDiff);
		contributionPreviousDiff.setVisible(true);
		toolBarManager.appendToGroup("navigation", contributionPreviousDiff);
	}

	/**
	 * Called by the framework when the last (or first) diff of the current content viewer has been reached.
	 * This will open the content viewer for the next (or previous) diff displayed in the structure viewer.
	 * 
	 * @param next
	 *            <code>true</code> if we are to open the next structure viewer's diff, <code>false</code> if
	 *            we should go to the previous instead.
	 */
	protected void endOfContentReached(boolean next) {
		final Control control = getControl();
		if (control != null && !control.isDisposed()) {
			final ICompareNavigator navigator = getCompareConfiguration().getContainer().getNavigator();
			if (navigator instanceof CompareNavigator && ((CompareNavigator)navigator).hasChange(next)) {
				navigator.selectChange(next);
			}
		}
	}

	/**
	 * Called by the framework to navigate to the next (or previous) difference. This will open the content
	 * viewer for the next (or previous) diff displayed in the structure viewer.
	 * 
	 * @param next
	 *            <code>true</code> if we are to open the next structure viewer's diff, <code>false</code> if
	 *            we should go to the previous instead.
	 */
	protected void navigate(boolean next) {
		final Control control = getControl();
		if (control != null && !control.isDisposed()) {
			final ICompareNavigator navigator = getCompareConfiguration().getContainer().getNavigator();
			if (navigator instanceof CompareNavigator && ((CompareNavigator)navigator).hasChange(next)) {
				navigator.selectChange(next);
			}
		}
	}

	@Override
	protected ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle(BUNDLE_NAME);
	}

	/**
	 * Command to directly modify the semantic model and reject the related difference.
	 * 
	 * @author cnotot
	 */
	private static class UpdateModelAndRejectDiffCommand extends ChangeCommand implements ICompareCopyCommand {

		private boolean isLeft;

		private Diff difference;

		private Object value;

		private EStructuralFeature feature;

		private EObject owner;

		public UpdateModelAndRejectDiffCommand(ChangeRecorder changeRecorder, EObject owner,
				EStructuralFeature feature, Object value, Diff difference, boolean isLeft) {
			super(changeRecorder, ImmutableSet.<Notifier> builder().add(owner).addAll(
					getAffectedDiff(difference)).build());
			this.owner = owner;
			this.feature = feature;
			this.value = value;
			this.difference = difference;
			this.isLeft = isLeft;
		}

		@Override
		public void doExecute() {
			owner.eSet(feature, value);
			for (Diff affectedDiff : getAffectedDiff(difference)) {
				affectedDiff.setState(DifferenceState.DISCARDED);
			}
		}

		private static Set<Diff> getAffectedDiff(Diff diff) {
			EList<Conflict> conflicts = diff.getMatch().getComparison().getConflicts();
			for (Conflict conflict : conflicts) {
				EList<Diff> conflictualDifferences = conflict.getDifferences();
				if (conflictualDifferences.contains(diff)) {
					return ImmutableSet.copyOf(conflictualDifferences);
				}
			}
			return ImmutableSet.of(diff);
		}

		public boolean isLeftToRight() {
			return !isLeft;
		}

	}

}
