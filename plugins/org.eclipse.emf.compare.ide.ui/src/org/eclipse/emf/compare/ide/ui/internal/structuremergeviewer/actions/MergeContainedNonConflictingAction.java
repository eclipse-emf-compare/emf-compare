/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Munich and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import java.util.List;

import org.eclipse.compare.INavigatable;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.domain.IMergeRunnable;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.ecore.EObject;
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
public class MergeContainedNonConflictingAction extends MergeAction {

	/**
	 * Function for transforming a tree node into all non-filtered leaf differences.
	 */
	private static Function<TreeNode, Iterable<Diff>> treeNodesToLeafDiffs(
			final Predicate<TreeNode> isFiltered) {
		return new Function<TreeNode, Iterable<Diff>>() {
			public Iterable<Diff> apply(TreeNode input) {
				final TreeIterator<EObject> allContents = input.eAllContents();
				final Builder<Diff> builder = new ImmutableList.Builder<Diff>();
				while (allContents.hasNext()) {
					final EObject eObject = allContents.next();
					if (eObject instanceof TreeNode) {
						final TreeNode treeNode = (TreeNode)eObject;
						final EObject data = IDifferenceGroup.TREE_NODE_DATA.apply(treeNode);
						if (data instanceof Diff && !isFiltered.apply(treeNode)) {
							builder.add((Diff)data);
						}
					}
				}
				return builder.build();
			}
		};
	}

	/**
	 * The predicate to determine whether a tree node is filtered.
	 */
	private Predicate<TreeNode> isFiltered;

	/**
	 * {@inheritDoc}
	 * 
	 * @param isFiltered
	 *            The predicate to use for determining whether a {@link TreeNode} is filtered.
	 */
	public MergeContainedNonConflictingAction(ICompareEditingDomain editingDomain, Registry mergerRegistry,
			MergeMode mode, boolean isLeftEditable, boolean isRightEditable, INavigatable navigatable,
			IStructuredSelection selection, Predicate<TreeNode> isFiltered) {
		super(editingDomain, mergerRegistry, mode, isLeftEditable, isRightEditable, navigatable);
		this.isFiltered = isFiltered;
		updateSelection(selection);
	}

	@Override
	protected void initToolTipAndImage(MergeMode mode) {
		switch (mode) {
			case LEFT_TO_RIGHT:
				setText(EMFCompareIDEUIMessages.getString("merged.contained.to.right.tooltip")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("merged.contained.to.right.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/merge_all_to_right.gif")); //$NON-NLS-1$
				break;
			case RIGHT_TO_LEFT:
				setText(EMFCompareIDEUIMessages.getString("merged.contained.to.left.tooltip")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("merged.contained.to.left.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/merge_all_to_left.gif")); //$NON-NLS-1$
				break;
			case ACCEPT:
				setText(EMFCompareIDEUIMessages.getString("accept.contained.changes.tooltip")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("accept.contained.changes.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/accept_all_changes.gif")); //$NON-NLS-1$
				break;
			case REJECT:
				setText(EMFCompareIDEUIMessages.getString("reject.contained.changes.tooltip")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("reject.contained.changes.tooltip")); //$NON-NLS-1$
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						EMFCompareIDEUIPlugin.PLUGIN_ID, "icons/full/toolb16/reject_all_changes.gif")); //$NON-NLS-1$
				break;
			default:
				throw new IllegalStateException();
		}
	}

	@Override
	protected IMergeRunnable createMergeRunnable(MergeMode mode, boolean isLeftEditable,
			boolean isRightEditable) {
		return new MergeNonConflictingRunnable(isLeftEditable, isRightEditable, mode);
	}

	@Override
	protected Iterable<Diff> getSelectedDifferences(IStructuredSelection selection) {
		final List<?> selectedObjects = selection.toList();
		final Iterable<Adapter> selectedAdapters = filter(selectedObjects, Adapter.class);
		final Iterable<Notifier> selectedNotifiers = transform(selectedAdapters, ADAPTER__TARGET);
		final Iterable<TreeNode> selectedTreeNodes = filter(selectedNotifiers, TreeNode.class);
		return concat(transform(selectedTreeNodes, treeNodesToLeafDiffs(isFiltered)));
	}
}
