/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.addAll;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.internal.AbstractCompareECrossReferencerAdapter;
import org.eclipse.emf.compare.internal.merge.MergeDependenciesUtil;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.emf.edit.tree.TreePackage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class DependencyData {

	private final IEMFCompareConfiguration compareConfiguration;

	private Set<Diff> requires;

	private Set<Diff> rejectedDiffs;

	private final WrappableTreeViewer treeViewer;

	private TreeNodeCrossReferencer treeNodeToDiffCrossReferencer;

	public DependencyData(IEMFCompareConfiguration compareConfiguration, WrappableTreeViewer treeViewer) {
		this.compareConfiguration = compareConfiguration;
		this.treeViewer = treeViewer;
		requires = newHashSet();
		rejectedDiffs = newHashSet();
	}

	/**
	 * @param selection
	 */
	public void updateDependencies(ISelection selection, IMerger.Registry mergerRegistry) {
		boolean leftEditable = compareConfiguration.isLeftEditable();
		boolean rightEditable = compareConfiguration.isRightEditable();
		if (leftEditable || rightEditable) {
			Iterable<Diff> selectedDiffs = filter(getSelectedComparisonObjects(selection), Diff.class);

			MergeMode mergePreviewMode = compareConfiguration.getMergePreviewMode();

			requires = newHashSet();
			rejectedDiffs = newHashSet();
			for (Diff diff : selectedDiffs) {
				boolean leftToRight = mergePreviewMode.isLeftToRight(diff, leftEditable, rightEditable);
				requires.addAll(MergeDependenciesUtil.getAllResultingMerges(diff, mergerRegistry,
						!leftToRight));
				requires.remove(diff);
				rejectedDiffs.addAll(MergeDependenciesUtil.getAllResultingRejections(diff, mergerRegistry,
						!leftToRight));
				rejectedDiffs.remove(diff);
			}
		}
	}

	public void updateTreeItemMappings() {
		Tree tree = treeViewer.getTree();

		// Create the first level of the tree (all roots) eagerly
		if (isDummyChild(tree.getItems())) {
			treeViewer.createChildren(tree);
		}

		final List<TreeNode> needCrossReferencer = new ArrayList<TreeNode>();
		TreeNodeCrossReferencer crossReferencer = null;
		for (TreeItem root : tree.getItems()) {
			final TreeNode rootNode = getTreeNodeFromAdapter(root.getData());
			final Adapter adapter = EcoreUtil.getExistingAdapter(rootNode, TreeNodeCrossReferencer.class);
			if (adapter instanceof TreeNodeCrossReferencer) {
				crossReferencer = (TreeNodeCrossReferencer)adapter;
			} else {
				needCrossReferencer.add(rootNode);
			}
		}
		if (crossReferencer == null) {
			crossReferencer = new TreeNodeCrossReferencer();
		}
		for (TreeNode needy : needCrossReferencer) {
			needy.eAdapters().add(crossReferencer);
		}
		treeNodeToDiffCrossReferencer = crossReferencer;
	}

	private TreeNode getTreeNodeFromAdapter(Object data) {
		if (data instanceof Adapter) {
			Notifier target = ((Adapter)data).getTarget();
			if (target instanceof TreeNode) {
				return (TreeNode)target;
			}
		}
		return null;
	}

	private static List<EObject> getSelectedComparisonObjects(ISelection selection) {
		List<EObject> ret = newArrayList();
		if (selection instanceof IStructuredSelection) {
			List<?> selectedObjects = ((IStructuredSelection)selection).toList();
			Iterable<EObject> data = transform(selectedObjects, ADAPTER__TARGET__DATA);
			Iterable<EObject> notNullData = Iterables.filter(data, notNull());
			addAll(ret, notNullData);
		}
		return ret;
	}

	private static final Function<Object, EObject> ADAPTER__TARGET__DATA = new Function<Object, EObject>() {
		public EObject apply(Object object) {
			return EMFCompareStructureMergeViewer.getDataOfTreeNodeOfAdapter(object);
		}
	};

	/**
	 * @return the requires
	 */
	public Set<Diff> getRequires() {
		return requires;
	}

	/**
	 * @return the unmergeables
	 */
	public Set<Diff> getRejections() {
		return rejectedDiffs;
	}

	public Collection<TreeNode> getTreeNodes(Diff diff) {
		final Collection<EStructuralFeature.Setting> settings = treeNodeToDiffCrossReferencer
				.getNonNavigableInverseReferences(diff, false);
		final List<TreeNode> nodes = new ArrayList<TreeNode>(settings.size());
		for (EStructuralFeature.Setting setting : settings) {
			final EObject referencing = setting.getEObject();
			if (referencing instanceof TreeNode) {
				nodes.add((TreeNode)referencing);
			}
		}
		return nodes;
	}

	private boolean isDummyChild(TreeItem[] items) {
		// item with non created children has a fake child item with null data.
		return items.length == 1 && items[0].getData() == null;
	}

	/**
	 * This implementation of an {@link org.eclipse.emf.ecore.util.ECrossReferenceAdapter} will allow us to
	 * map TreeNodes to their target Diff.
	 */
	private static class TreeNodeCrossReferencer extends AbstractCompareECrossReferencerAdapter {
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#isIncluded(org.eclipse.emf.ecore.EReference)
		 */
		@Override
		protected boolean isIncluded(EReference eReference) {
			return eReference == TreePackage.Literals.TREE_NODE__DATA;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#addAdapter(org.eclipse.emf.common.notify.Notifier)
		 */
		@Override
		protected void addAdapter(Notifier notifier) {
			if (notifier instanceof TreeNode) {
				super.addAdapter(notifier);
			}
		}

		/** {@inheritDoc} */
		@Override
		public boolean isAdapterForType(Object type) {
			return type == TreeNodeCrossReferencer.class;
		}
	}
}
