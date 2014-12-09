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

import static org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.EMFCompareStructureMergeViewerContentProvider.CallbackType.IN_UI_ASYNC;

import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.compare.INavigatable;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class Navigatable implements INavigatable {

	private final WrappableTreeViewer viewer;

	private final EMFCompareStructureMergeViewerContentProvider contentProvider;

	/**
	 * @param viewer
	 * @param contentProvider
	 */
	public Navigatable(WrappableTreeViewer viewer,
			EMFCompareStructureMergeViewerContentProvider contentProvider) {
		this.viewer = viewer;
		this.contentProvider = contentProvider;
	}

	public boolean selectChange(final int flag) {
		contentProvider.runWhenReady(IN_UI_ASYNC, new Runnable() {

			public void run() {
				internalSelectChange(flag);
			}
		});
		return false;
	}

	private boolean internalSelectChange(int flag) {
		Object nextOrPrev = null;
		Item[] selection = viewer.getSelection(viewer.getTree());
		Item firstSelectedItem = selection.length > 0 ? selection[0] : null;
		switch (flag) {
			case NEXT_CHANGE:
				nextOrPrev = getNextDiff(firstSelectedItem);
				break;
			case PREVIOUS_CHANGE:
				nextOrPrev = getPreviousDiff(firstSelectedItem);
				break;
			case FIRST_CHANGE:
				nextOrPrev = getNextDiff(null);
				break;
			case LAST_CHANGE:
				nextOrPrev = getPreviousDiff(null);
				break;
			default:
				throw new IllegalStateException();
		}

		if (nextOrPrev != null) {
			fireOpen(nextOrPrev);
		}

		return nextOrPrev == null;

	}

	/**
	 * Execute the fireOpen method of the viewer associated to this navigatable.
	 * 
	 * @param element
	 *            the input of the selection of the open event fired by the fireOpen method.
	 */
	public void fireOpen(Object element) {
		StructuredSelection newSelection = new StructuredSelection(element);
		viewer.setSelection(newSelection);
		viewer.fireOpen(new OpenEvent(viewer, newSelection));
	}

	/**
	 * Return the viewer associated with this Navigatable.
	 * 
	 * @return the viewer associated with this Navigatable.
	 */
	public WrappableTreeViewer getViewer() {
		return viewer;
	}

	public Object getInput() {
		return viewer.getInput();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.INavigatable#openSelectedChange()
	 */
	public boolean openSelectedChange() {
		ISelection selection = viewer.getSelection();
		if (selection.isEmpty()) {
			return false;
		} else {
			viewer.fireOpen(new OpenEvent(viewer, selection));
			return true;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.INavigatable#hasChange(int)
	 */
	public boolean hasChange(int changeFlag) {
		Item[] selection = viewer.getSelection(viewer.getTree());
		Item firstSelectedItem = selection.length > 0 ? selection[0] : null;
		switch (changeFlag) {
			case NEXT_CHANGE:
				return getNextDiff(firstSelectedItem) != null;
			case PREVIOUS_CHANGE:
				return getPreviousDiff(firstSelectedItem) != null;
			case FIRST_CHANGE:
				return getNextDiff(null) != null;
			default:
				throw new IllegalStateException();
		}
	}

	/**
	 * Returns, from the given TreeNode, the next TreeNode that contains a diff.
	 * 
	 * @param treeNode
	 *            the given TreeNode for which we want to find the next.
	 * @return the next TreeNode that contains a diff.
	 */
	private Object getNextDiff(Item item) {
		final Item startingItem;
		if (item != null) {
			startingItem = item;
		} else {
			startingItem = getStartingItem();
		}

		if (startingItem == null) {
			return null;
		}

		Item nextItem = getNextItem(startingItem);
		Object result = null;
		while (nextItem != null && result == null) {
			EObject data = EMFCompareStructureMergeViewer.getDataOfTreeNodeOfAdapter(nextItem.getData());
			if (data instanceof Diff) {
				result = nextItem.getData();
			} else {
				nextItem = getNextItem(nextItem);
			}
		}
		return result;
	}

	/**
	 * Returns the first item of the tree.
	 * 
	 * @return
	 */
	private TreeItem getStartingItem() {
		final TreeItem startingItem;
		TreeItem[] roots = viewer.getTree().getItems();
		if (roots != null && roots.length > 0) {
			startingItem = roots[0];
		} else {
			startingItem = null;
		}
		return startingItem;
	}

	private Object getPreviousDiff(Item item) {
		final Item startingItem;
		if (item != null) {
			startingItem = item;
		} else {
			startingItem = getStartingItem();
		}

		if (startingItem == null) {
			return null;
		}

		Item previousItem = getPreviousItem(startingItem);
		Object result = null;
		while (previousItem != null && result == null) {
			EObject data = EMFCompareStructureMergeViewer.getDataOfTreeNodeOfAdapter(previousItem.getData());
			if (data instanceof Diff) {
				result = previousItem.getData();
			} else {
				previousItem = getPreviousItem(previousItem);
			}
		}
		return result;
	}

	// Protected for testing purpose
	protected Item getNextItem(Item previousItem) {
		final Item result;
		TreeItem firstChild = getFirstChild(previousItem);
		if (firstChild != null) {
			result = firstChild;
		} else {
			TreeItem sibling = getSibling(previousItem);
			if (sibling != null) {
				result = sibling;
			} else {
				result = getAncestorSibling(previousItem);
			}
		}
		if (result instanceof TreeItem && result.getData() == null) {
			// This is a dummy object for yet to be created children
			final TreeItem parentItem = ((TreeItem)result).getParentItem();
			if (parentItem != null) {
				viewer.createChildren(parentItem);
			} else {
				viewer.createChildren(viewer.getTree());
			}
			return getNextItem(previousItem);
		}
		return result;
	}

	// Protected for testing purpose
	protected Item getPreviousItem(Item previousItem) {
		final Item result;
		TreeItem previousSibling = getPreviousSibling(previousItem);
		if (previousSibling != null) {
			result = getDeepestChild(previousSibling);
		} else {
			Object parent = getParent(previousItem);
			if (parent instanceof TreeItem) {
				result = (TreeItem)parent;
			} else {
				result = null;
			}
		}
		if (result instanceof TreeItem && result.getData() == null) {
			// This is a dummy object for yet to be created children
			final TreeItem parentItem = ((TreeItem)result).getParentItem();
			if (parentItem != null) {
				viewer.createChildren(parentItem);
			} else {
				viewer.createChildren(viewer.getTree());
			}
			return getPreviousItem(previousItem);
		}
		return result;
	}

	/**
	 * Gets the previous sibling element.
	 * 
	 * @param item
	 *            input element.
	 * @return the first previous sibling of the input element.
	 */
	private TreeItem getPreviousSibling(Item item) {
		TreeItem previsousSibling = null;
		final Object parent;
		parent = getParent(item);
		if (parent != null) {
			List<TreeItem> sibling = getChildren(parent);
			int indexOfCurrent = sibling.indexOf(item);
			if (indexOfCurrent > 0) {
				previsousSibling = sibling.get(indexOfCurrent - 1);
			}
		}
		return previsousSibling;
	}

	/**
	 * Get the deepest child of this item in the tree. If this item has several deepest child then the first
	 * one is returned.
	 * 
	 * @param input
	 * @return
	 */
	private TreeItem getDeepestChild(TreeItem input) {
		List<TreeItem> children = getChildren(input);
		TreeItem deepestChild = input;
		while (!children.isEmpty()) {
			deepestChild = children.get(children.size() - 1);
			children = getChildren(deepestChild);
		}
		return deepestChild;
	}

	/**
	 * Gets the first child of the input item.
	 * 
	 * @return
	 */
	private TreeItem getFirstChild(Item item) {
		List<TreeItem> children = getChildren(item);
		if (!children.isEmpty()) {
			return children.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Gets the children of the input item.
	 * 
	 * @param item
	 *            can be a {@link TreeItem} or a {@link Tree}
	 * @return the children.
	 */
	private List<TreeItem> getChildren(Object item) {
		final TreeItem[] children;
		if (item instanceof TreeItem) {
			children = ((TreeItem)item).getItems();
		} else if (item instanceof Tree) {
			children = ((Tree)item).getItems();
		} else {
			children = new TreeItem[] {};
		}
		return Lists.newArrayList(children);
	}

	/**
	 * Gets the parent of the input item. The parent can either be a {@link TreeItem} or a {@link Tree}.
	 * 
	 * @param item
	 *            input item
	 * @return a {@link TreeItem} or a {@link Tree}.
	 */
	private Object getParent(Item item) {
		final Object parent;
		Item parentItem = viewer.getParentItem(item);
		if (parentItem != null) {
			parent = parentItem;
		} else {
			parent = viewer.getTree();
		}
		return parent;
	}

	/**
	 * Returns the first sibling of one of the ancestor of the input item.
	 * 
	 * @param inputItem
	 * @return the sibling of one ancestor of the input item or <code>null</code> otherwise.
	 */
	private TreeItem getAncestorSibling(Item inputItem) {
		Object parent = getParent(inputItem);
		TreeItem ancestorSibling = null;
		while (parent instanceof TreeItem && ancestorSibling == null) {
			ancestorSibling = getSibling((TreeItem)parent);
			parent = getParent((TreeItem)parent);
		}
		return ancestorSibling;
	}

	/**
	 * Gets the next sibling item of the input item.
	 * 
	 * @return
	 */
	private TreeItem getSibling(Item item) {
		Object parent = getParent(item);
		if (parent != null) {
			List<TreeItem> children = getChildren(parent);
			int indexOfCurrent = children.indexOf(item);
			if (indexOfCurrent != children.size() - 1) {
				return children.get(indexOfCurrent + 1);
			}
		}
		return null;
	}
}
