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

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.compare.INavigatable;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.GroupItemProviderAdapter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * Abstract {@link MergeAction} for merging all diffs contained in the selection.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 * @since 4.5
 */
public abstract class AbstractMergeContainedAction extends MergeAction {

	/**
	 * Function for transforming a tree node into all non-filtered leaf differences.
	 */
	private static Function<TreeNode, Stream<Diff>> treeNodesToLeafDiffs(
			final Predicate<TreeNode> isFiltered) {
		return new Function<TreeNode, Stream<Diff>>() {
			public Stream<Diff> apply(TreeNode input) {
				final TreeIterator<EObject> allContents = input.eAllContents();
				final Stream.Builder<Diff> builder = Stream.builder();
				while (allContents.hasNext()) {
					final EObject eObject = allContents.next();
					if (eObject instanceof TreeNode && !isFiltered.test((TreeNode)eObject)) {
						final EObject data = ((TreeNode)eObject).getData();
						if (data instanceof Diff) {
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

	/**
	 * Return a predicate that filters the {{@link #getSelectedDifferences() selected differences} to just
	 * those differences this action will actually operate upon.
	 * 
	 * @return a predicate that filters the {{@link #getSelectedDifferences() selected differences}.
	 */
	protected abstract Predicate<Diff> getDiffPredicate();

	@Override
	protected boolean updateSelection(IStructuredSelection selection) {
		super.updateSelection(selection);
		// The action is enabled only there are any selected differences that will change state when this
		// action is applied.
		return !getSelectedDifferences().isEmpty();
	}

	@Override
	protected List<Diff> getSelectedDifferences(IStructuredSelection selection) {
		Object firstElement = selection.getFirstElement();
		if (firstElement instanceof GroupItemProviderAdapter) {
			Stream<Diff> diffs = ((GroupItemProviderAdapter)firstElement).getChildren(firstElement).stream()
					.filter(TreeNode.class::isInstance).map(TreeNode.class::cast)
					.flatMap(treeNodesToLeafDiffs(isFiltered));
			return getSelectedDifferences(diffs).stream().filter(getDiffPredicate())
					.collect(Collectors.toList());
		} else {
			final List<?> selectedObjects = selection.toList();
			Stream<Diff> diffs = selectedObjects.stream().filter(Adapter.class::isInstance)
					.map(adapter -> ((Adapter)adapter).getTarget()).filter(TreeNode.class::isInstance)
					.map(TreeNode.class::cast).flatMap(treeNodesToLeafDiffs(isFiltered));
			return getSelectedDifferences(diffs).stream().filter(getDiffPredicate())
					.collect(Collectors.toList());
		}
	}

}
