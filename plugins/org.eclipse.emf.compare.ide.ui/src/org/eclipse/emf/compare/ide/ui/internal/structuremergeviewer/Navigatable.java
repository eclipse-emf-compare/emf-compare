/*******************************************************************************
 * Copyright (c) 2013, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Michael Borkowski - bug 462237, refactoring
 *     Simon Delisle - bug 511172
 *     Philip Langer - bug 509975
 *     Martin Fleck - bug 518572
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import static org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.EMFCompareStructureMergeViewerContentProvider.CallbackType.IN_UI_ASYNC;

import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.compare.INavigatable;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.EMFCompareStructureMergeViewerContentProvider.CallbackType;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.TreeNodeCompareInput;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class Navigatable implements INavigatable {

	public static final int NEXT_UNRESOLVED_CHANGE = 80;

	private final WrappableTreeViewer viewer;

	private final EMFCompareStructureMergeViewerContentProvider contentProvider;

	protected CallbackType uiSyncCallbackType = IN_UI_ASYNC;

	private Object cachedParent;

	private Object[] cachedChildren;

	public Navigatable(WrappableTreeViewer viewer,
			EMFCompareStructureMergeViewerContentProvider contentProvider) {
		this.viewer = viewer;
		this.contentProvider = contentProvider;
	}

	public boolean selectChange(final int flag) {
		contentProvider.runWhenReady(uiSyncCallbackType, new Runnable() {
			public void run() {
				Object firstSelectedElement = getFirstSelectedItem();
				Object newSelection = calculateNextSelection(firstSelectedElement, flag);
				if (newSelection != null) {
					fireOpen(newSelection);
				}
			}
		});
		return false;
	}

	private Object getFirstSelectedItem() {
		IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
		return selection.getFirstElement();
	}

	private Object calculateNextSelection(Object currentSelection, int flag) {
		switch (flag) {
			case NEXT_CHANGE:
				return getNextDiff(thisOrFirstItem(currentSelection));
			case PREVIOUS_CHANGE:
				return getPreviousDiff(thisOrFirstItem(currentSelection));
			case FIRST_CHANGE:
				return getNextDiff(getFirstItem());
			case LAST_CHANGE:
				return getPreviousDiff(getFirstItem());
			case NEXT_UNRESOLVED_CHANGE:
				return getNextUnresolvedDiff(thisOrFirstItem(currentSelection));
			default:
				throw new IllegalStateException();
		}
	}

	private Object thisOrFirstItem(Object object) {
		if (object != null) {
			return object;
		}
		return getFirstItem();
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
		Object firstSelectedItem = getFirstSelectedItem();
		switch (changeFlag) {
			case NEXT_CHANGE:
				return getNextDiff(thisOrFirstItem(firstSelectedItem)) != null;
			case PREVIOUS_CHANGE:
				return getPreviousDiff(thisOrFirstItem(firstSelectedItem)) != null;
			case NEXT_UNRESOLVED_CHANGE:
				return getNextUnresolvedDiff(thisOrFirstItem(firstSelectedItem)) != null;
			case FIRST_CHANGE:
				Object firstItemInTree = getFirstItem();
				return firstItemInTree != null && getNextDiff(firstItemInTree) != null;
			default:
				throw new IllegalStateException();
		}
	}

	/**
	 * Returns, from the given item, the next item that contains a diff.
	 * 
	 * @param start
	 *            the given item for which we want to find the next.
	 * @return the next item that contains a diff.
	 */
	private Object getNextDiff(Object start) {
		return getNextItemWithDiff(start, false);
	}

	/**
	 * Returns, from the given item, the previous item that contains a diff.
	 * 
	 * @param start
	 *            the given item for which we want to find the previous.
	 * @return the previous item that contains a diff.
	 */
	private Object getPreviousDiff(Object start) {
		return getPreviousItemWithDiff(start);
	}

	/**
	 * Returns, from the given item, the next item that contains an unresolved diff. This method supports
	 * round-trip behavior, i.e., we start at the top after reaching bottom.
	 * 
	 * @param start
	 *            the given item for which we want to find the next unresolved diff.
	 * @return the next item that contains an unresolved diff.
	 */
	private Object getNextUnresolvedDiff(Object start) {
		Object nextUnresolvedDiff = getNextItemWithDiff(start, true);
		if (nextUnresolvedDiff == null) {
			// we don't have an unresolved diff AFTER the the current one (TreeItem start)
			// thus, we look for the next unresolved diff starting from the beginning
			nextUnresolvedDiff = getNextItemWithDiff(getFirstItem(), true);
		}

		return nextUnresolvedDiff;
	}

	/**
	 * Returns, from the given item, the next item that contains a diff matching the supplied criteria.
	 * 
	 * @param start
	 *            the item at which to start searching.
	 * @param onlyUnresolvedDiff
	 *            if true only items with unresolved diffs are considered.
	 * @return the next matching item.
	 */
	private Object getNextItemWithDiff(Object start, boolean onlyUnresolvedDiff) {
		Object item = getNextItem(start);
		while (item != null) {
			if (hasDiff(item, onlyUnresolvedDiff)) {
				return item;
			}
			item = getNextItem(item);
		}
		return null;
	}

	/**
	 * Returns, from the given item, the previous item that contains a diff.
	 * 
	 * @param start
	 *            the item at which to start searching.
	 * @return the previous matching item.
	 */
	private Object getPreviousItemWithDiff(Object start) {
		Object item = getPreviousItem(start);
		while (item != null) {
			if (hasDiff(item, false)) {
				return item;
			}
			item = getPreviousItem(item);
		}
		return null;
	}

	/**
	 * Returns whether the given item has a matching diff.
	 * 
	 * @param item
	 *            the item to test.
	 * @param onlyUnresolved
	 *            if true only an unresolved diff is considered.
	 * @return true if the given item has a matching diff, false otherwise.
	 */
	private boolean hasDiff(Object item, boolean onlyUnresolved) {
		EObject data = EMFCompareStructureMergeViewer.getDataOfTreeNodeOfAdapter(item);
		if (data instanceof Diff) {
			Diff diff = (Diff)data;
			return !onlyUnresolved || diff.getState() == DifferenceState.UNRESOLVED;
		}
		return false;
	}

	/**
	 * Returns the first item in the viewer, i.e., it's input.
	 * 
	 * @return the first item in the viewer
	 */
	private Object getFirstItem() {
		return viewer.getInput();
	}

	/**
	 * Starting at the given item, returns the next item in the viewer.
	 * 
	 * @param start
	 *            the item for which to find the next one.
	 * @return the next item.
	 */
	// Protected for testing purposes
	protected Object getNextItem(Object start) {
		Object[] children = getChildren(start);
		if (children.length > 0) {
			return children[0];
		} else {
			Object nextSibling = getNextSibling(start);
			if (nextSibling != null) {
				return nextSibling;
			} else {
				return getNextAncestor(start);
			}
		}
	}

	/**
	 * Returns the next ancestor of the given item. "Next" means that it is the closest available sibling for
	 * one of the given item's ancestors.
	 * 
	 * @param item
	 *            the item for which to get the next ancestor.
	 * @return the next ancestor
	 */
	private Object getNextAncestor(Object item) {
		Object nextAncestor = null;
		Object ancestor = getAncestor(item);
		while (ancestor != null && nextAncestor == null) {
			nextAncestor = getNextSibling(ancestor);
			ancestor = getAncestor(ancestor);
		}
		return nextAncestor;
	}

	/**
	 * Returns the ancestor of the given item, i.e., its parent.
	 * 
	 * @param item
	 *            the item for which to get the ancestor.
	 * @return the ancestor item.
	 */
	private Object getAncestor(Object item) {
		Object ancestor = viewer.getParentElement(item);
		if (ancestor != null) {
			return ancestor;
		}
		// use difference group hidden by the content provider as parent, if available
		return getDifferenceGroup(item);
	}

	/**
	 * Starting at the given element, returns the previous item in the viewer.
	 * 
	 * @param start
	 *            the item for which to find the previous one.
	 * @return the previous item.
	 */
	// Protected for testing purposes
	protected Object getPreviousItem(Object start) {
		Object previousSibling = getPreviousSibling(start);
		if (previousSibling != null) {
			return getLastDescendant(previousSibling);
		} else {
			Object parent = getAncestor(start);
			if (parent != null) {
				return parent;
			} else {
				return null;
			}
		}
	}

	/**
	 * Returns the last item of a depth-first search starting at the given item
	 * 
	 * @param parent
	 *            the starting item.
	 * @return the last descendant of the parent.
	 */
	private Object getLastDescendant(Object parent) {
		Object[] children = getChildren(parent);
		Object deepestChild = parent;
		while (children.length > 0) {
			deepestChild = children[children.length - 1];
			children = getChildren(deepestChild);
		}
		return deepestChild;
	}

	/**
	 * Return a (possibly empty) array of items which are the children of the input item.
	 * 
	 * @param input
	 *            the item for which we to find the children.
	 * @return direct children of the input item.
	 */
	private Object[] getChildren(Object input) {
		if (input == cachedParent) {
			return cachedChildren;
		}

		cachedParent = input;
		if (input instanceof IDifferenceGroup) {
			cachedChildren = getChildren((IDifferenceGroup)input);
		} else {
			cachedChildren = viewer.getFilteredChildren(input);
		}

		return cachedChildren;
	}

	/**
	 * Returns the previous sibling of the given item.
	 * 
	 * @param item
	 *            the item for which to get the sibling.
	 * @return the previous sibling item.
	 */
	private Object getPreviousSibling(Object item) {
		Object[] siblings = getSiblings(item);
		for (int i = 0; i < siblings.length; ++i) {
			if (siblings[i] == item) {
				if (--i >= 0) {
					return siblings[i];
				} else {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the next sibling of the given TreeItem.
	 * 
	 * @param item
	 *            the item to get the sibling for
	 * @return the next sibling item.
	 */
	private Object getNextSibling(Object item) {
		Object[] siblings = getSiblings(item);
		for (int i = 0; i < siblings.length; ++i) {
			if (siblings[i] == item) {
				if (++i < siblings.length) {
					return siblings[i];
				} else {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the siblings for a given item (including the item itself).
	 * 
	 * @param item
	 *            the item for which to get the siblings.
	 * @return the sibling items.
	 */
	private Object[] getSiblings(Object item) {
		Object parent = getAncestor(item);
		if (parent == null) {
			return new Object[] {item };
		} else {
			return getChildren(parent);
		}
	}

	/**
	 * Returns the difference group associated with the given item.
	 * 
	 * @param item
	 *            the item for which we want to get the difference group.
	 * @return the associated difference group or null.
	 */
	private static IDifferenceGroup getDifferenceGroup(Object item) {
		if (item instanceof TreeNodeCompareInput) {
			TreeNodeCompareInput treeNodeCompareInput = (TreeNodeCompareInput)item;
			Notifier target = treeNodeCompareInput.getTarget();
			IDifferenceGroup differenceGroup = (IDifferenceGroup)EcoreUtil.getExistingAdapter(target,
					IDifferenceGroup.class);
			return differenceGroup;
		}
		return null;
	}

	/**
	 * Returns the children of the given difference group.
	 * 
	 * @param differenceGroup
	 *            the difference group for which we want to get the children.
	 * @return a non-null array of children elements.
	 */
	private static Object[] getChildren(IDifferenceGroup differenceGroup) {
		if (differenceGroup == null) {
			return new Object[0];
		}

		List<Object> children = Lists.newArrayList();
		for (TreeNode child : differenceGroup.getChildren()) {
			for (Adapter adapter : child.eAdapters()) {
				if (adapter instanceof TreeNodeCompareInput) {
					children.add(adapter);
				}
			}
		}
		return children.toArray();
	}
}
