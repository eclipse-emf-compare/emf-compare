/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;

import java.util.List;
import java.util.ListIterator;

import org.eclipse.compare.INavigatable;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.DynamicObject;
import org.eclipse.emf.compare.ide.ui.internal.util.JFaceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class Navigatable implements INavigatable {
	/**
	 * 
	 */
	private final AdapterFactory adapterFactory;

	private final StructuredViewer viewer;

	private final DynamicObject dynamicObject;

	/**
	 * @param adapterFactory
	 */
	public Navigatable(AdapterFactory adapterFactory, StructuredViewer viewer) {
		this.adapterFactory = adapterFactory;
		this.viewer = viewer;
		this.dynamicObject = new DynamicObject(viewer);
	}

	public boolean selectChange(int flag) {
		TreeNode nextOrPrev = null;
		Object firstElement = ((IStructuredSelection)viewer.getSelection()).getFirstElement();
		if (firstElement instanceof Adapter) {
			Notifier target = ((Adapter)firstElement).getTarget();
			if (target instanceof TreeNode) {
				TreeNode treeNode = (TreeNode)target;
				if (flag == INavigatable.NEXT_CHANGE) {
					nextOrPrev = getNextDiffNode(treeNode);
				} else if (flag == INavigatable.PREVIOUS_CHANGE) {
					nextOrPrev = getPrevDiffNode(treeNode);
				} else if (flag == INavigatable.FIRST_CHANGE || flag == INavigatable.LAST_CHANGE) {
					return true;
				}
				if (nextOrPrev != null) {
					StructuredSelection newSelection = new StructuredSelection(adapterFactory.adapt(
							nextOrPrev, ICompareInput.class));
					viewer.setSelection(newSelection);
					dynamicObject
							.invoke("fireOpen", new Class<?>[] {OpenEvent.class, }, new OpenEvent(viewer, newSelection)); //$NON-NLS-1$
				}
			}
		}
		return nextOrPrev == null;
	}

	public Object getInput() {
		return viewer.getInput();
	}

	public boolean openSelectedChange() {
		return true;
	}

	public boolean hasChange(int changeFlag) {
		if (changeFlag == INavigatable.NEXT_CHANGE || changeFlag == INavigatable.PREVIOUS_CHANGE) {
			return true;
		}
		return false;
	}

	/**
	 * Returns, from the given TreeNode, the previous TreeNode that contains a diff.
	 * 
	 * @param treeNode
	 *            the given TreeNode for which we want to find the previous.
	 * @return the previous TreeNode that contains a diff.
	 */
	private TreeNode getPrevDiffNode(TreeNode treeNode) {
		TreeNode previousNode = null;
		TreeNode parentNode = treeNode.getParent();
		if (parentNode != null) {
			List<TreeNode> children = getSortedTreeNodeChildren(
					adapterFactory.adapt(parentNode, ICompareInput.class)).reverse();
			int indexOfTreeNode = children.indexOf(treeNode);
			if (indexOfTreeNode == children.size() - 1) {
				if (EMFCompareDiffTreeViewer.IS_DIFF_TREE_NODE.apply(parentNode)) {
					previousNode = parentNode;
				} else {
					previousNode = getPrevDiffNode(parentNode);
				}
			} else {
				boolean stop = false;
				while (!stop) {
					if (children.size() > indexOfTreeNode + 1) {
						TreeNode prevSibling = children.get(indexOfTreeNode + 1);
						previousNode = getLastChildDiffNode(prevSibling);
						if (previousNode != null) {
							stop = true;
						} else if (EMFCompareDiffTreeViewer.IS_DIFF_TREE_NODE.apply(prevSibling)) {
							previousNode = prevSibling;
							stop = true;
						}
						indexOfTreeNode++;
					} else {
						previousNode = getPrevDiffNode(parentNode);
						stop = true;
					}
				}
			}
		}
		return previousNode;
	}

	/**
	 * Returns, from the given TreeNode, the next TreeNode that contains a diff.
	 * 
	 * @param treeNode
	 *            the given TreeNode for which we want to find the next.
	 * @return the next TreeNode that contains a diff.
	 */
	private TreeNode getNextDiffNode(TreeNode treeNode) {
		TreeNode next = getFirstChildDiffNode(treeNode);
		if (next == null) {
			TreeNode currentNode = treeNode;
			boolean stop = false;
			while (!stop && currentNode != null) {
				next = getNextSiblingDiffNode(currentNode);
				if (next == null) {
					currentNode = currentNode.getParent();
				} else {
					stop = true;
				}
			}
		}
		return next;
	}

	/**
	 * Returns, from the given TreeNode, the next sibling TreeNode that contains a diff.
	 * 
	 * @param treeNode
	 *            the given TreeNode for which we want to find the next sibling.
	 * @return the next sibling TreeNode that contains a diff.
	 */
	private TreeNode getNextSiblingDiffNode(TreeNode treeNode) {
		TreeNode next = null;
		TreeNode parent = treeNode.getParent();
		if (parent != null) {
			List<TreeNode> children = getSortedTreeNodeChildren(adapterFactory.adapt(parent,
					ICompareInput.class));
			int indexOfTreeNode = children.indexOf(treeNode);
			boolean stop = false;
			while (!stop) {
				if (children.size() > indexOfTreeNode + 1) {
					TreeNode nextSibling = children.get(indexOfTreeNode + 1);
					if (EMFCompareDiffTreeViewer.IS_DIFF_TREE_NODE.apply(nextSibling)) {
						next = nextSibling;
					} else {
						next = getFirstChildDiffNode(nextSibling);
					}
					if (next != null) {
						stop = true;
					}
					indexOfTreeNode++;
				} else {
					stop = true;
				}
			}
		}
		return next;
	}

	/**
	 * Returns, from the given TreeNode, the first TreeNode child that contains a diff.
	 * 
	 * @param treeNode
	 *            the given TreeNode for which we want to find the first child.
	 * @return the first TreeNode child that contains a diff.
	 */
	private TreeNode getFirstChildDiffNode(TreeNode treeNode) {
		UnmodifiableIterator<EObject> diffChildren = Iterators.filter(treeNode.eAllContents(),
				EMFCompareDiffTreeViewer.IS_DIFF_TREE_NODE);
		while (diffChildren.hasNext()) {
			TreeNode next = (TreeNode)diffChildren.next();
			if (!JFaceUtil.isFiltered(viewer, adapterFactory.adapt(next, ICompareInput.class), adapterFactory
					.adapt(next.getParent(), ICompareInput.class))) {
				return next;
			}
		}
		return null;
	}

	/**
	 * Returns, from the given TreeNode, the last TreeNode child that contains a diff.
	 * 
	 * @param treeNode
	 *            the given TreeNode for which we want to find the last child.
	 * @return the last TreeNode child that contains a diff.
	 */
	private TreeNode getLastChildDiffNode(TreeNode treeNode) {
		UnmodifiableIterator<EObject> diffChildren = Iterators.filter(treeNode.eAllContents(),
				EMFCompareDiffTreeViewer.IS_DIFF_TREE_NODE);
		List<EObject> l = Lists.newArrayList(diffChildren);
		ListIterator<EObject> li = l.listIterator(l.size());
		while (li.hasPrevious()) {
			TreeNode prev = (TreeNode)li.previous();
			if (!JFaceUtil.isFiltered(viewer, adapterFactory.adapt(prev, ICompareInput.class), adapterFactory
					.adapt(prev.getParent(), ICompareInput.class))) {
				return prev;
			}
		}
		return null;
	}

	/**
	 * Returns the sorted and filtered set of TreeNode children of the given element.
	 * 
	 * @param treeNodeAdapter
	 *            the given TreeNode adapter.
	 * @return the sorted and filtered set of TreeNode children of the given element.
	 */
	private ImmutableList<TreeNode> getSortedTreeNodeChildren(Adapter treeNodeAdapter) {
		List<TreeNode> treeNodeChildren = Lists.newArrayList();
		Object[] sortedChildren = (Object[])dynamicObject.invoke("getSortedChildren", //$NON-NLS-1$
				new Class<?>[] {Object.class, }, treeNodeAdapter);
		for (Object object : sortedChildren) {
			if (object instanceof Adapter) {

				Notifier target = ((Adapter)object).getTarget();
				if (target instanceof TreeNode) {
					treeNodeChildren.add((TreeNode)target);
				}
			}
		}
		return ImmutableList.copyOf(treeNodeChildren);

	}
}
