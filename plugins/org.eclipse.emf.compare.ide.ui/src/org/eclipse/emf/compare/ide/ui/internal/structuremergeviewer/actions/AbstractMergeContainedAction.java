/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
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
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * Abstract {@link MergeAction} for merging all diffs contained in the selection.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 * @since 4.5
 */
public class AbstractMergeContainedAction extends MergeAction {

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
	protected Predicate<TreeNode> isFiltered;

	public AbstractMergeContainedAction(IEMFCompareConfiguration compareConfiguration,
			Registry mergerRegistry, MergeMode mode, INavigatable navigatable) {
		super(compareConfiguration, mergerRegistry, mode, navigatable);
	}

	public AbstractMergeContainedAction(IEMFCompareConfiguration compareConfiguration,
			Registry mergerRegistry, MergeMode mode, INavigatable navigatable,
			IStructuredSelection selection) {
		super(compareConfiguration, mergerRegistry, mode, navigatable, selection);
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
