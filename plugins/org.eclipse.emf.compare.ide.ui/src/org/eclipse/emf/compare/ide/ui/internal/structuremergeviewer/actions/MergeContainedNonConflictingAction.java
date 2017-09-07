/*******************************************************************************
 * Copyright (c) 2015, 2017 EclipseSource Munich and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation, bug 521948
 *     Martin Fleck - bug 514415
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import static org.eclipse.emf.compare.ConflictKind.REAL;

import com.google.common.base.Predicate;

import org.eclipse.compare.INavigatable;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.domain.IMergeRunnable;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IDiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Action that manages a merge of a contained non-conflicting difference in case the selection is a resource
 * match or a model element match.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 * @since 4.1
 */
public class MergeContainedNonConflictingAction extends AbstractMergeContainedAction {

	@SuppressWarnings("unchecked")
	private static final Predicate<Diff> NON_CONFLICTING_DIFFS = (Predicate<Diff>)EMFComparePredicates
			.hasNoDirectOrIndirectConflict(REAL);

	/**
	 * {@inheritDoc}
	 * 
	 * @param isFiltered
	 *            The predicate to use for determining whether a {@link TreeNode} is filtered.
	 */
	public MergeContainedNonConflictingAction(IEMFCompareConfiguration compareConfiguration,
			Registry mergerRegistry, MergeMode mode, INavigatable navigatable, IStructuredSelection selection,
			Predicate<TreeNode> isFiltered) {
		super(compareConfiguration, mergerRegistry, mode, navigatable);
		this.isFiltered = isFiltered;
		setEnabled(updateSelection(selection));
	}

	@Override
	protected void initToolTipAndImage(MergeMode mode) {
		switch (mode) {
			case LEFT_TO_RIGHT:
				setText(EMFCompareIDEUIMessages.getString("merged.contained.to.right.tooltip")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("merged.contained.to.right.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/etoolb16/merge_contained_non_conflicting_diffs_to_right.png")); //$NON-NLS-1$
				break;
			case RIGHT_TO_LEFT:
				setText(EMFCompareIDEUIMessages.getString("merged.contained.to.left.tooltip")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("merged.contained.to.left.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/etoolb16/merge_contained_non_conflicting_diffs_to_left.png")); //$NON-NLS-1$
				break;
			case ACCEPT:
				setText(EMFCompareIDEUIMessages.getString("accept.contained.changes.tooltip")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("accept.contained.changes.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/etoolb16/accept_contained_non_conflicting_diffs.png")); //$NON-NLS-1$
				break;
			case REJECT:
				setText(EMFCompareIDEUIMessages.getString("reject.contained.changes.tooltip")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("reject.contained.changes.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/etoolb16/reject_contained_non_conflicting_diffs.png")); //$NON-NLS-1$
				break;
			default:
				throw new IllegalStateException();
		}
	}

	@Override
	protected IMergeRunnable createMergeRunnable(MergeMode mode, boolean isLeftEditable,
			boolean isRightEditable, IDiffRelationshipComputer relationshipComputer) {
		return new MergeNonConflictingRunnable(isLeftEditable, isRightEditable, mode, relationshipComputer);
	}

	@Override
	protected Predicate<Diff> getDiffPredicate() {
		return NON_CONFLICTING_DIFFS;
	}
}
