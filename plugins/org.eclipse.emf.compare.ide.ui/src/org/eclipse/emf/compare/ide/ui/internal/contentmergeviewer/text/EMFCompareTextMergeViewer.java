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

import java.util.EventObject;
import java.util.ResourceBundle;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.TextMergeViewer;
import org.eclipse.compare.internal.CompareHandlerService;
import org.eclipse.compare.internal.MergeSourceViewer;
import org.eclipse.compare.internal.Utilities;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareConstants;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.DynamicObject;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.RedoAction;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.UndoAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.AttributeChangeNode;
import org.eclipse.emf.compare.ide.ui.internal.util.EMFCompareEditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.actions.ActionFactory;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareTextMergeViewer extends TextMergeViewer implements CommandStackListener {

	private static final String BUNDLE_NAME = EMFCompareTextMergeViewer.class.getName();

	private final EMFCompareEditingDomain fEditingDomain;

	private DynamicObject fDynamicObject;

	private ActionContributionItem fCopyDiffLeftToRightItem;

	private ActionContributionItem fCopyDiffRightToLeftItem;

	/**
	 * @param parent
	 * @param configuration
	 */
	public EMFCompareTextMergeViewer(Composite parent, CompareConfiguration configuration) {
		super(parent, configuration);
		fEditingDomain = (EMFCompareEditingDomain)getCompareConfiguration().getProperty(
				EMFCompareConstants.EDITING_DOMAIN);
		fEditingDomain.getCommandStack().addCommandStackListener(this);
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
					.getDifferences(), leftToRight);
			fEditingDomain.getCommandStack().execute(copyCommand);

			if (leftToRight) {
				setRightDirty(true);
			} else {
				setLeftDirty(true);
			}

			refresh();
		}
	}

	protected void copyDiff(boolean leftToRight) {
		Object input = getInput();
		if (input instanceof AttributeChangeNode) {
			final AttributeChange attributeChange = ((AttributeChangeNode)input).getTarget();

			final Command copyCommand = fEditingDomain.createCopyCommand(attributeChange, leftToRight);
			fEditingDomain.getCommandStack().execute(copyCommand);

			if (leftToRight) {
				setRightDirty(true);
			} else {
				setLeftDirty(true);
			}

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
	 * @see org.eclipse.compare.contentmergeviewer.TextMergeViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		fEditingDomain.getCommandStack().removeCommandStackListener(this);
		super.handleDispose(event);
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
		// TODO copied from EMFCompareContentMergeViewer ... externalize
		Action a;

		// avoid super to avoid NPE in org.eclipse.compare.internal.ViewerDescriptor.createViewer
		CompareHandlerService handlerService = CompareHandlerService.createFor(getCompareConfiguration()
				.getContainer(), getLeftSourceViewer().getSourceViewer().getControl().getShell());
		setHandlerService(handlerService);

		CompareConfiguration cc = getCompareConfiguration();
		if (cc.isRightEditable()) {
			a = new Action() {
				@Override
				public void run() {
					copyDiff(true);
				}
			};
			Utilities.initAction(a, getResourceBundle(), "action.CopyDiffLeftToRight."); //$NON-NLS-1$
			fCopyDiffLeftToRightItem = new ActionContributionItem(a);
			fCopyDiffLeftToRightItem.setVisible(true);
			toolBarManager.appendToGroup("merge", fCopyDiffLeftToRightItem); //$NON-NLS-1$
			handlerService.registerAction(a, "org.eclipse.compare.copyLeftToRight"); //$NON-NLS-1$
		}

		if (cc.isLeftEditable()) {
			a = new Action() {
				@Override
				public void run() {
					copyDiff(false);
				}
			};
			Utilities.initAction(a, getResourceBundle(), "action.CopyDiffRightToLeft."); //$NON-NLS-1$
			fCopyDiffRightToLeftItem = new ActionContributionItem(a);
			fCopyDiffRightToLeftItem.setVisible(true);
			toolBarManager.appendToGroup("merge", fCopyDiffRightToLeftItem); //$NON-NLS-1$
			handlerService.registerAction(a, "org.eclipse.compare.copyRightToLeft"); //$NON-NLS-1$
		}

		// This is called from the super-constructor, fEditingDomain is not set yet.
		final EMFCompareEditingDomain domain = (EMFCompareEditingDomain)getCompareConfiguration()
				.getProperty(EMFCompareConstants.EDITING_DOMAIN);

		final UndoAction undoAction = new UndoAction(domain);
		final RedoAction redoAction = new RedoAction(domain);

		domain.getCommandStack().addCommandStackListener(new CommandStackListener() {
			public void commandStackChanged(EventObject event) {
				undoAction.update();
				redoAction.update();
				refresh();
			}
		});

		handlerService.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);
		handlerService.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.TextMergeViewer#setEditable(org.eclipse.jface.text.source.ISourceViewer,
	 *      boolean)
	 */
	@Override
	protected void setEditable(ISourceViewer sourceViewer, boolean state) {
		sourceViewer.setEditable(false);
	}

	@Override
	protected ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle(BUNDLE_NAME);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStackListener#commandStackChanged(java.util.EventObject)
	 */
	public void commandStackChanged(EventObject event) {
		refresh();
	}
}
