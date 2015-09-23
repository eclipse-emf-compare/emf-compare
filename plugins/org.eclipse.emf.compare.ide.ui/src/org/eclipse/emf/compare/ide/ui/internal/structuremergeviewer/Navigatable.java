/*******************************************************************************
 * Copyright (c) 2013, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Michael Borkowski - bug 462237, refactoring
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import static org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.EMFCompareStructureMergeViewerContentProvider.CallbackType.IN_UI_ASYNC;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import java.util.Arrays;
import java.util.List;

import org.eclipse.compare.INavigatable;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.EMFCompareStructureMergeViewerContentProvider.CallbackType;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class Navigatable implements INavigatable {

	public static final int NEXT_UNRESOLVED_CHANGE = 80;

	private final WrappableTreeViewer viewer;

	private final EMFCompareStructureMergeViewerContentProvider contentProvider;

	protected CallbackType uiSyncCallbackType = IN_UI_ASYNC;

	public Navigatable(WrappableTreeViewer viewer,
			EMFCompareStructureMergeViewerContentProvider contentProvider) {
		this.viewer = viewer;
		this.contentProvider = contentProvider;
	}

	public boolean selectChange(final int flag) {
		contentProvider.runWhenReady(uiSyncCallbackType, new Runnable() {
			public void run() {
				TreeItem[] selection = viewer.getTree().getSelection();
				TreeItem firstSelectedItem = firstOrNull(selection);
				Object newSelection = calculateNextSelection(firstSelectedItem, flag);
				if (newSelection != null) {
					fireOpen(newSelection);
				}
			}
		});
		return false;
	}

	private static TreeItem firstOrNull(TreeItem[] items) {
		if (items.length > 0) {
			return items[0];
		}
		return null;
	}

	private Object calculateNextSelection(TreeItem currentSelection, int flag) {
		switch (flag) {
			case NEXT_CHANGE:
				return getNextDiff(thisOrFirstItem(currentSelection));
			case PREVIOUS_CHANGE:
				return getPreviousDiff(thisOrFirstItem(currentSelection));
			case FIRST_CHANGE:
				return getNextDiff(getFirstItemInTree());
			case LAST_CHANGE:
				return getPreviousDiff(getFirstItemInTree());
			case NEXT_UNRESOLVED_CHANGE:
				return getNextUnresolvedDiff(thisOrFirstItem(currentSelection));
			default:
				throw new IllegalStateException();
		}
	}

	private TreeItem thisOrFirstItem(TreeItem item) {
		if (item != null) {
			return item;
		}
		return getFirstItemInTree();
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
		TreeItem[] selection = viewer.getTree().getItems();
		TreeItem firstSelectedItem = selection.length > 0 ? selection[0] : null;
		switch (changeFlag) {
			case NEXT_CHANGE:
				return getNextDiff(thisOrFirstItem(firstSelectedItem)) != null;
			case PREVIOUS_CHANGE:
				return getPreviousDiff(thisOrFirstItem(firstSelectedItem)) != null;
			case NEXT_UNRESOLVED_CHANGE:
				return getNextUnresolvedDiff(thisOrFirstItem(firstSelectedItem)) != null;
			case FIRST_CHANGE:
				TreeItem firstItemInTree = getFirstItemInTree();
				return firstItemInTree != null && getNextDiff(firstItemInTree) != null;
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
	private Object getNextDiff(TreeItem start) {
		return getNextData(start, Predicates.alwaysTrue());
	}

	/**
	 * Returns, from the given TreeNode, the previous TreeNode that contains a diff.
	 * 
	 * @param treeNode
	 *            the given TreeNode for which we want to find the previous.
	 * @return the previous TreeNode that contains a diff.
	 */
	private Object getPreviousDiff(TreeItem item) {
		return getPreviousData(item, Predicates.alwaysTrue());
	}

	/**
	 * Returns, from the given TreeNode, the next TreeNode that contains an unresolved diff.
	 * 
	 * @param treeNode
	 *            the given TreeNode for which we want to find the next unresolvable diff.
	 * @return the previous TreeNode that contains an unresolvable diff.
	 */
	private Object getNextUnresolvedDiff(TreeItem start) {
		return getNextData(start, EMFComparePredicates.hasState(DifferenceState.UNRESOLVED));
	}

	/**
	 * Returns the data of the next tree item meeting the supplied predicate
	 * 
	 * @param start
	 *            the TreeNode at which to start searching
	 * @param predicate
	 *            the predicate to match
	 * @return the next matching TreeNode
	 */
	private Object getNextData(TreeItem start, Predicate<? super Diff> predicate) {
		TreeItem current = getNextItem(start);
		while (current != null) {
			EObject data = EMFCompareStructureMergeViewer.getDataOfTreeNodeOfAdapter(current.getData());
			if (data instanceof Diff && predicate.apply((Diff)data)) {
				return current.getData();
			}
			current = getNextItem(current);
		}
		return null;
	}

	/**
	 * Returns the data of the previous tree item meeting the supplied predicate
	 * 
	 * @param start
	 *            the TreeNode at which to start searching
	 * @param predicate
	 *            the predicate to match
	 * @return the previous matching TreeNode
	 */
	private Object getPreviousData(TreeItem start, Predicate<? super Diff> predicate) {
		TreeItem current = getPreviousItem(start);
		while (current != null) {
			EObject data = EMFCompareStructureMergeViewer.getDataOfTreeNodeOfAdapter(current.getData());
			if (data instanceof Diff && predicate.apply((Diff)data)) {
				return current.getData();
			}
			current = getPreviousItem(current);
		}
		return null;
	}

	/**
	 * Returns the first TreeItem
	 * 
	 * @return the first TreeItem
	 */
	private TreeItem getFirstItemInTree() {
		final TreeItem startingItem;
		TreeItem[] roots = viewer.getTree().getItems();
		if (roots != null && roots.length > 0) {
			startingItem = roots[0];
		} else {
			startingItem = null;
		}
		return startingItem;
	}

	/**
	 * Starting at the given TreeItem, returns the next item in the tree.
	 * 
	 * @param start
	 *            the item for which to find the next one
	 * @return the next TreeItem
	 */
	// Protected for testing purposes
	protected TreeItem getNextItem(TreeItem start) {
		viewer.createChildren(start);
		if (hasChildren(start)) {
			return start.getItem(0);
		} else if (hasNextSibling(start)) {
			return getNextSibling(start);
		} else {
			return getNextAncestor(start);
		}
	}

	/**
	 * Checks whether the given item has children.
	 * 
	 * @param item
	 *            the item to check
	 * @return whether it has children
	 */
	private boolean hasChildren(TreeItem item) {
		return item.getItems().length != 0;
	}

	/**
	 * Checks whether the given item has a next sibling.
	 * 
	 * @param item
	 *            the item to check
	 * @return whether it has a next sibling
	 */
	private boolean hasNextSibling(TreeItem item) {
		return getNextSibling(item) != null;
	}

	/**
	 * Returns the next ancestor of the given TreeItem. "Next" means that it is the closest available sibling
	 * for one of the given TreeNode's ancestors.
	 * 
	 * @param item
	 *            the item for which to get the next ancestor
	 * @return the next ancestor
	 */
	private TreeItem getNextAncestor(TreeItem item) {
		TreeItem nextAncestor = null;
		TreeItem ancestor = item.getParentItem();
		while (ancestor != null && nextAncestor == null) {
			nextAncestor = getNextSibling(ancestor);
			ancestor = ancestor.getParentItem();
		}
		return nextAncestor;
	}

	/**
	 * Starting at the given TreeItem, returns the previous item in the tree.
	 * 
	 * @param start
	 *            the item for which to find the previous one
	 * @return the previous TreeItem
	 */
	// Protected for testing purposes
	protected TreeItem getPreviousItem(TreeItem start) {
		if (hasPreviousSibling(start)) {
			return getLastDescendant(getPreviousSibling(start));
		} else {
			return start.getParentItem();
		}
	}

	/**
	 * Checks whether the given item has a previous sibling.
	 * 
	 * @param item
	 *            the item to check
	 * @return whether it has a previous sibling
	 */
	private boolean hasPreviousSibling(TreeItem item) {
		return getPreviousSibling(item) != null;
	}

	/**
	 * Returns the last item of a depth-first search starting at the given TreeItem
	 * 
	 * @param input
	 *            the starting item
	 * @return the resulting item
	 */
	private TreeItem getLastDescendant(TreeItem input) {
		TreeItem[] children = input.getItems();
		TreeItem deepestChild = input;
		while (children.length > 0) {
			deepestChild = children[children.length - 1];
			children = deepestChild.getItems();
		}
		return deepestChild;
	}

	/**
	 * Returns the previous sibling of the given TreeItem.
	 * 
	 * @param item
	 *            the item to get the sibling for
	 * @return the sibling
	 */
	private TreeItem getPreviousSibling(TreeItem item) {
		List<TreeItem> siblings = Arrays.asList(getSiblings(item));
		int indexOfCurrent = siblings.indexOf(item);
		if (indexOfCurrent > 0) {
			return siblings.get(indexOfCurrent - 1);
		}
		return null;
	}

	/**
	 * Returns the next sibling of the given TreeItem.
	 * 
	 * @param item
	 *            the item to get the sibling for
	 * @return the sibling
	 */
	private TreeItem getNextSibling(TreeItem item) {
		List<TreeItem> siblings = Arrays.asList(getSiblings(item));
		int indexOfItem = siblings.indexOf(item);
		if (indexOfItem >= 0 && indexOfItem < siblings.size() - 1) {
			return siblings.get(indexOfItem + 1);
		}
		return null;
	}

	/**
	 * Returns the siblings for a given TreeItem (including the item itself).
	 * 
	 * @param item
	 *            the item to get the siblings for
	 * @return the siblings
	 */
	private TreeItem[] getSiblings(TreeItem item) {
		if (item.getParentItem() == null) {
			return viewer.getTree().getItems();
		} else {
			return item.getParentItem().getItems();
		}
	}
}
