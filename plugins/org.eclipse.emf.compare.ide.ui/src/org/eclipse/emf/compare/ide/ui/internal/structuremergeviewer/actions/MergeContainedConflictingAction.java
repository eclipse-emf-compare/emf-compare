/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import static org.eclipse.emf.compare.ConflictKind.REAL;

import com.google.common.base.Predicate;

import org.eclipse.compare.INavigatable;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
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
 * Action that manages a merge of a contained conflicting differences in case the selection is a resource
 * match or a model element match.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 * @since 4.5
 */
public class MergeContainedConflictingAction extends AbstractMergeContainedAction {

	@SuppressWarnings("unchecked")
	private static final Predicate<Diff> CONFLICTING_DIFFS = (Predicate<Diff>)EMFComparePredicates
			.hasDirectOrIndirectConflict(REAL);

	/**
	 * {@inheritDoc}
	 * 
	 * @param isFiltered
	 *            The predicate to use for determining whether a {@link TreeNode} is filtered.
	 */
	public MergeContainedConflictingAction(IEMFCompareConfiguration compareConfiguration,
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
				setText(EMFCompareIDEUIMessages.getString("merged.contained.conflicting.to.right.tooltip")); //$NON-NLS-1$
				setToolTipText(
						EMFCompareIDEUIMessages.getString("merged.contained.conflicting.to.right.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/etoolb16/merge_contained_conflicting_diffs_to_right.png")); //$NON-NLS-1$
				break;
			case RIGHT_TO_LEFT:
				setText(EMFCompareIDEUIMessages.getString("merged.contained.conflicting.to.left.tooltip")); //$NON-NLS-1$
				setToolTipText(
						EMFCompareIDEUIMessages.getString("merged.contained.conflicting.to.left.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/etoolb16/merge_contained_conflicting_diffs_to_left.png")); //$NON-NLS-1$
				break;
			case ACCEPT:
				setText(EMFCompareIDEUIMessages.getString("accept.contained.conflicting.changes.tooltip")); //$NON-NLS-1$
				setToolTipText(
						EMFCompareIDEUIMessages.getString("accept.contained.conflicting.changes.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/etoolb16/accept_contained_conflicting_diffs.png")); //$NON-NLS-1$
				break;
			case REJECT:
				setText(EMFCompareIDEUIMessages.getString("reject.contained.conflicting.changes.tooltip")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("reject.contained.changes.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
						"icons/full/etoolb16/reject_contained_conflicting_diffs.png")); //$NON-NLS-1$
				break;
			default:
				throw new IllegalStateException();
		}
	}

	@Override
	protected IMergeRunnable createMergeRunnable(MergeMode mode, boolean isLeftEditable,
			boolean isRightEditable, IDiffRelationshipComputer relationshipComputer) {
		return new MergeConflictingRunnable(isLeftEditable, isRightEditable, mode, relationshipComputer);
	}

	@Override
	protected Predicate<Diff> getDiffPredicate() {
		return CONFLICTING_DIFFS;
	}

	@Override
	protected Predicate<Diff> getStatePredicate() {
		return new Predicate<Diff>() {
			public boolean apply(Diff diff) {
				switch (diff.getState()) {
					case DISCARDED:
						switch (getSelectedMode()) {
							case REJECT:
							case RIGHT_TO_LEFT:
								return diff.getSource() == DifferenceSource.LEFT;
							case ACCEPT:
							case LEFT_TO_RIGHT:
							default:
								return diff.getSource() == DifferenceSource.RIGHT;
						}
					case MERGED:
						switch (getSelectedMode()) {
							case REJECT:
							case RIGHT_TO_LEFT:
								return diff.getSource() == DifferenceSource.RIGHT;
							case ACCEPT:
							case LEFT_TO_RIGHT:
							default:
								return diff.getSource() == DifferenceSource.LEFT;
						}
					case UNRESOLVED:
					default:
						return true;
				}
			}
		};
	}
}
