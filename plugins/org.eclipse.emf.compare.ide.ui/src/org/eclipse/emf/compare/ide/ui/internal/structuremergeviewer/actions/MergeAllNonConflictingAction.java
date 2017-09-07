/*******************************************************************************
 * Copyright (c) 2013, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Martin Fleck - bug 514079
 *     Martin Fleck - bug 514415
 *     Philip Langer - bug 521948
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import static com.google.common.collect.Iterables.addAll;
import static com.google.common.collect.Iterables.filter;
import static org.eclipse.emf.compare.ConflictKind.REAL;

import com.google.common.base.Predicate;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.command.ICompareCommandStack;
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IDiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Action that manages a merge of a all non-conflicting difference.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class MergeAllNonConflictingAction extends MergeAction {

	@SuppressWarnings("unchecked")
	private static final Predicate<Diff> NON_CONFLICTING_DIFFS = (Predicate<Diff>)EMFComparePredicates
			.hasNoDirectOrIndirectConflict(REAL);

	private Comparison comparison;

	private List<Diff> differences;

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The compare configuration object.
	 */
	public MergeAllNonConflictingAction(IEMFCompareConfiguration compareConfiguration, Comparison comparison,
			IMerger.Registry mergerRegistry, MergeMode mode) {
		super(compareConfiguration, mergerRegistry, mode, null);
		setComparison(comparison);
	}

	@Override
	protected MergeNonConflictingRunnable createMergeRunnable(MergeMode mode, boolean isLeftEditable,
			boolean isRightEditable, IDiffRelationshipComputer relationshipComputer) {
		return new MergeNonConflictingRunnable(isLeftEditable, isRightEditable, mode, relationshipComputer);
	}

	@Override
	protected void initToolTipAndImage(MergeMode mode) {
		switch (mode) {
			case LEFT_TO_RIGHT:
				setToolTipText(EMFCompareIDEUIMessages.getString("merged.all.to.right.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/toolb16/merge_all_to_right.gif")); //$NON-NLS-1$
				break;
			case RIGHT_TO_LEFT:
				setToolTipText(EMFCompareIDEUIMessages.getString("merged.all.to.left.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/toolb16/merge_all_to_left.gif")); //$NON-NLS-1$
				break;
			case ACCEPT:
				setToolTipText(EMFCompareIDEUIMessages.getString("accept.all.changes.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/toolb16/accept_all_changes.gif")); //$NON-NLS-1$
				break;
			case REJECT:
				setToolTipText(EMFCompareIDEUIMessages.getString("reject.all.changes.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/toolb16/reject_all_changes.gif")); //$NON-NLS-1$
				break;
			default:
				throw new IllegalStateException();
		}
	}

	public void setComparison(Comparison comparison) {
		this.comparison = comparison;
		if (comparison != null) {
			differences = comparison.getDifferences();
		} else {
			differences = Collections.emptyList();
		}
		clearCache();
		// update the enablement of this action by simulating a selection change.
		setEnabled(comparison != null);
	}

	@Override
	protected void execute(ICompareCommandStack commandStack, MergeMode mode, List<Diff> diffs) {
		if (editingDomain instanceof EMFCompareEditingDomain) {
			ICompareCopyCommand mergeCommand = ((EMFCompareEditingDomain)editingDomain)
					.createCopyAllNonConflictingCommand(comparison, isLeftToRight(), mergerRegistry,
							createMergeRunnable(mode, isLeftEditable(), isRightEditable(),
									getDiffRelationshipComputer()));
			editingDomain.getCommandStack().execute(mergeCommand);
		} else {
			// FIXME remove this once we have pulled "createCopyAllNonConflictingCommand" up as API.
			EMFCompareIDEUIPlugin.getDefault().getLog().log(new Status(IStatus.ERROR,
					EMFCompareIDEUIPlugin.PLUGIN_ID, "Couldn't create the copy all command.")); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeAction#updateSelection(org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	protected boolean updateSelection(IStructuredSelection selection) {
		addAll(getSelectedDifferences(), getSelectedDifferences(differences));
		// The action is enabled only there are any selected differences that will change state when this
		// action is applied.
		return !getSelectedDifferences().isEmpty();
	}

	@Override
	protected Iterable<Diff> getSelectedDifferences(Iterable<Diff> diffs) {
		return filter(super.getSelectedDifferences(diffs), NON_CONFLICTING_DIFFS);
	}

}
