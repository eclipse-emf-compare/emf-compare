/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

class MergeTreeViewer implements IMergeViewer<TreeViewer, Tree> {

	private final TreeViewer fViewer;

	private final MergeViewerSide fSide;

	/**
	 * @param parent
	 */
	public MergeTreeViewer(Composite parent, MergeViewerSide side, IContentProvider contentProvider,
			ILabelProvider labelProvider) {
		fViewer = new TreeViewer(parent);
		fSide = side;
		fViewer.setContentProvider(contentProvider);
		fViewer.setLabelProvider(labelProvider);
	}

	public Tree getControl() {
		return fViewer.getTree();
	}

	public TreeViewer getViewer() {
		return fViewer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer#getLineHeight()
	 */
	public int getLineHeight() {
		return fViewer.getTree().getItemHeight();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer#getViewportHeight()
	 */
	public int getViewportHeight() {
		Rectangle clientArea = fViewer.getTree().getClientArea();
		if (!clientArea.isEmpty()) {
			return clientArea.height;
		}
		return 0;
	}

	public List<TreeItem> getVisibleTreeItem() {
		return visibleTreeItems(getControl());
	}

	public static List<TreeItem> visibleTreeItems(Tree tree) {
		Rectangle treeBounds = new Rectangle(0, 0, 0, 0);
		Point treeSize = tree.getSize();
		TreeItem topItem = tree.getTopItem();
		if (topItem != null) {
			// Rectangle topItemBounds = topItem.getBounds();
			// treeBounds.y = topItemBounds.y;
		}
		treeBounds.width = treeSize.x;
		treeBounds.height = treeSize.y;
		TreeItem[] items = tree.getItems();

		return visibleTreeItems(items, treeBounds, false);
	}

	private static boolean isItemPartiallyVisible(Rectangle treeBounds, final TreeItem rootItem) {
		return treeBounds.intersects(rootItem.getBounds());
	}

	public static List<TreeItem> visibleTreeItems(TreeItem[] items, Rectangle treeBounds,
			boolean parentVisible) {
		List<TreeItem> ret = newArrayList();

		boolean previousItemWasVisible = false;
		for (TreeItem treeItem : items) {
			if (treeItem.getExpanded()) {
				boolean itemPartiallyVisible = isItemPartiallyVisible(treeBounds, treeItem);
				if (itemPartiallyVisible) {
					ret.add(treeItem);
					previousItemWasVisible = true;
				}
				if (previousItemWasVisible && !itemPartiallyVisible) {
					break; // quick loop exit
				}
			}
		}

		if (!ret.isEmpty()) { // some items are visible, so browse children to see if some are visible
			for (TreeItem treeItem : newArrayList(ret)) {
				if (treeItem.getExpanded()) { // only if the current visible item is expanded
					List<TreeItem> visibleChildren = visibleTreeItems(treeItem.getItems(), treeBounds, true);
					ret.addAll(visibleChildren);
				}
			}
		} else if (!parentVisible) { // the parent is not visible but expanded and then some children are
										// visible
			for (TreeItem treeItem : items) {
				if (treeItem.getExpanded()) { // only if the current item is expanded
					List<TreeItem> visibleChildren = visibleTreeItems(treeItem.getItems(), treeBounds, false);
					ret.addAll(visibleChildren);
				}
			}
		}

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer#getVerticalScrollOffset()
	 */
	public int getVerticalScrollOffset() {
		return -14;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer#getSide()
	 */
	public MergeViewerSide getSide() {
		return fSide;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer#setEnabled(boolean)
	 */
	public void setEnabled(boolean b) {
		// TODO Auto-generated method stub

	}
}
