/*******************************************************************************
 * Copyright (c) 2013, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import static com.google.common.base.Predicates.instanceOf;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.internal.util.JFaceUtil;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.ICompareColor;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * A specific canvas that must be presented next to a TreeViewer. It shows consequences of a Diff (required
 * and unmergeable differences), as in the TreeViewer, but in a compact format (small colored rectangles) and
 * with links to respectives Treeitems.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class EMFCompareDiffTreeRuler extends Canvas {

	/** The vertical offset for an annotation. */
	private static final int Y_OFFSET = 6;

	/** The height of an annotation. */
	private static final int ANNOTATION_HEIGHT = 5;

	/** The TreeViewer associated with this Treeruler. */
	private final WrappableTreeViewer fTreeViewer;

	/**
	 * A map that links a rectangle with a tree item and tree node couple. <b>Note</b> that the TreeNode might
	 * not be the actual node represented by the TreeItem of its {@link TreeNodeToVisibleTreeItem} couple: it
	 * could be a child if the TreeItem is not expanded (hence the node's real TreeItem is hidden from view,
	 * or might not even exist yet in the Tree).
	 */
	private Map<Rectangle, TreeNodeToVisibleTreeItem> annotationsData;

	/** The paint listener. */
	private PaintListener paintListener;

	/** The mouse click listener. */
	private MouseListener mouseClickListener;

	/** The mouse move listener. */
	private MouseMoveListener mouseMoveListener;

	/** The mouse track listener. */
	private MouseTrackListener mouseTrackListener;

	/** The last cursor used. */
	private Cursor lastCursor;

	private final DependencyData dependencyData;

	private ICompareColor compareColor;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            the control's parent.
	 * @param style
	 *            the style of the control to construct.
	 * @param width
	 *            the control's width.
	 * @param treeViewer
	 *            the TreeViewer associated with this control.
	 * @param config
	 *            the configuration for this control.
	 */
	EMFCompareDiffTreeRuler(Composite parent, int style, WrappableTreeViewer treeViewer,
			DependencyData dependencyData, ICompareColor compareColor) {
		super(parent, style);
		fTreeViewer = treeViewer;
		this.dependencyData = dependencyData;
		this.compareColor = compareColor;

		annotationsData = Maps.newHashMap();

		paintListener = new PaintListener() {
			public void paintControl(PaintEvent e) {
				handlePaintEvent(e);
			}
		};
		addPaintListener(paintListener);

		mouseClickListener = new MouseListener() {

			public void mouseUp(MouseEvent e) {
				handleMouseClickEvent(e);
			}

			public void mouseDown(MouseEvent e) {
				// Do nothing.
			}

			public void mouseDoubleClick(MouseEvent e) {
				// Do nothing.
			}
		};
		addMouseListener(mouseClickListener);

		mouseMoveListener = new MouseMoveListener() {

			public void mouseMove(MouseEvent e) {
				handleMouveMoveEvent(e);
			}
		};
		addMouseMoveListener(mouseMoveListener);

		mouseTrackListener = new MouseTrackListener() {

			public void mouseHover(MouseEvent e) {
				handleMouseHoverEvent(e);
			}

			public void mouseExit(MouseEvent e) {
				// Do nothing.
			}

			public void mouseEnter(MouseEvent e) {
				// Do nothing.
			}
		};
		addMouseTrackListener(mouseTrackListener);

	}

	/**
	 * Handles the dispose event on this control.
	 */
	public void handleDispose() {
		removeMouseTrackListener(mouseTrackListener);
		removeMouseMoveListener(mouseMoveListener);
		removeMouseListener(mouseClickListener);
		removePaintListener(paintListener);
	}

	/**
	 * Handles the paint event.
	 * 
	 * @param e
	 *            the paint event.
	 */
	private void handlePaintEvent(PaintEvent e) {
		annotationsData.clear();
		for (Diff diff : dependencyData.getRequires()) {
			final Collection<TreeNode> treeNodes = dependencyData.getTreeNodes(diff);
			for (TreeNodeToVisibleTreeItem treeNodeToVisibleTreeItem : findTreeItems(treeNodes)) {
				if (!JFaceUtil.isFiltered(fTreeViewer, treeNodeToVisibleTreeItem.getTreeNode(), null)) {
					createAnnotation(e, treeNodeToVisibleTreeItem, compareColor.getRequiredFillColor(),
							compareColor.getRequiredStrokeColor());
				}
			}
		}
		for (Diff diff : dependencyData.getRejections()) {
			final Collection<TreeNode> treeNodes = dependencyData.getTreeNodes(diff);
			for (TreeNodeToVisibleTreeItem treeNodeToVisibleTreeItem : findTreeItems(treeNodes)) {
				if (!JFaceUtil.isFiltered(fTreeViewer, treeNodeToVisibleTreeItem.getTreeNode(), null)) {
					createAnnotation(e, treeNodeToVisibleTreeItem, compareColor.getUnmergeableFillColor(),
							compareColor.getUnmergeableStrokeColor());
				}
			}
		}
	}

	public Collection<TreeNodeToVisibleTreeItem> findTreeItems(Collection<TreeNode> treeNodes) {
		final Set<TreeNodeToVisibleTreeItem> result = new LinkedHashSet<TreeNodeToVisibleTreeItem>();
		for (TreeNode node : treeNodes) {
			final TreeItem item = findFirstAncestorTreeItem(node);
			if (item != null) {
				result.add(new TreeNodeToVisibleTreeItem(node, item));
			}
		}
		return result;
	}

	private TreeItem findFirstAncestorTreeItem(TreeNode node) {
		CompareInputAdapter cia = findFirstAncestorCompareInputAdapter(node);
		Object item;
		if (cia != null) {
			item = fTreeViewer.testFindItem(cia);
		} else {
			item = null;
		}
		TreeNode parent = node;
		while (!(item instanceof TreeItem) && (parent = parent.getParent()) != null) {
			cia = findFirstAncestorCompareInputAdapter(parent);
			if (cia != null) {
				item = fTreeViewer.testFindItem(cia);
			} else {
				item = null;
			}
		}
		if (item instanceof TreeItem) {
			return (TreeItem)item;
		} else {
			return null;
		}
	}

	private CompareInputAdapter findFirstAncestorCompareInputAdapter(TreeNode node) {
		CompareInputAdapter cia = getCompareInputAdapter(node);
		if (cia != null) {
			return cia;
		}

		TreeNode parent = node;
		while (cia == null && (parent = parent.getParent()) != null) {
			cia = getCompareInputAdapter(parent);
		}
		return cia;
	}

	private CompareInputAdapter getCompareInputAdapter(TreeNode node) {
		return (CompareInputAdapter)Iterators.tryFind(node.eAdapters().iterator(),
				instanceOf(CompareInputAdapter.class)).orNull();
	}

	/**
	 * Handles the mouse click event.
	 * 
	 * @param e
	 *            the mouse click event.
	 */
	private void handleMouseClickEvent(MouseEvent e) {
		for (Map.Entry<Rectangle, TreeNodeToVisibleTreeItem> entry : annotationsData.entrySet()) {
			if (e.y >= entry.getKey().y && e.y <= entry.getKey().y + ANNOTATION_HEIGHT) {
				TreeItem targetItem = createChildrenDownTo(entry.getValue().getTreeItem(), entry.getValue()
						.getTreeNode());
				TreePath treePath = getTreePathFromItem(targetItem);
				fTreeViewer.expandToLevel(treePath, 0);
				fTreeViewer.reveal(treePath);
				if (isVerticalScrollBarEnabled()) {
					TreeItem previousItem = getPreviousItem(targetItem, 2);
					fTreeViewer.getTree().setTopItem(previousItem);
				}
				redraw();
				return;
			}
		}
	}

	private TreeItem createChildrenDownTo(TreeItem from, TreeNode to) {
		TreeItem currentItem = from;
		TreeNode currentNode = getTreeNodeFromAdapter(currentItem.getData());
		final Iterator<TreeNode> pathToTarget = getPath(currentNode, to).iterator();
		while (currentNode != to && pathToTarget.hasNext()) {
			// pathToTarget excludes "from" so we can safely iterate it after expanding "currentItem"
			TreeItem[] children = currentItem.getItems();
			if (isDummyChild(children)) {
				fTreeViewer.createChildren(currentItem);
				children = currentItem.getItems();
			}

			currentNode = pathToTarget.next();
			for (TreeItem child : children) {
				TreeNode childNode = getTreeNodeFromAdapter(child.getData());
				if (childNode == currentNode) {
					currentItem = child;
					break;
				}
			}
		}
		return currentItem;
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

	/* excludes "from" */
	private Iterable<TreeNode> getPath(TreeNode from, TreeNode to) {
		if (to == from) {
			return Collections.emptyList();
		}

		final List<TreeNode> path = new ArrayList<TreeNode>();
		path.add(to);
		TreeNode parent = to.getParent();
		while (parent != null && parent != from) {
			path.add(parent);
			parent = parent.getParent();
		}
		return Lists.reverse(path);
	}

	private boolean isDummyChild(TreeItem[] items) {
		// item with non created children has a fake child item with null data.
		return items.length == 1 && items[0].getData() == null;
	}

	/**
	 * Handles the mouse move event.
	 * 
	 * @param e
	 *            the mouse move event.
	 */
	private void handleMouveMoveEvent(MouseEvent e) {
		Cursor cursor = null;
		for (Rectangle rect : annotationsData.keySet()) {
			if (e.y >= rect.y && e.y <= rect.y + ANNOTATION_HEIGHT) {
				cursor = e.display.getSystemCursor(SWT.CURSOR_HAND);
				break;
			}
		}
		if (cursor != lastCursor) {
			setCursor(cursor);
			lastCursor = cursor;
		}
	}

	/**
	 * Handles the mouse hover event.
	 * 
	 * @param e
	 *            the mouve hover event.
	 */
	private void handleMouseHoverEvent(MouseEvent e) {
		String overview = ""; //$NON-NLS-1$
		for (Map.Entry<Rectangle, TreeNodeToVisibleTreeItem> entry : annotationsData.entrySet()) {
			if (e.y >= entry.getKey().y && e.y <= entry.getKey().y + ANNOTATION_HEIGHT) {
				TreeNode node = entry.getValue().getTreeNode();
				// TODO is there a chance the label provider won't be an instance of that low-level one?
				if (fTreeViewer.getLabelProvider() instanceof DelegatingStyledCellLabelProvider) {
					overview = ((DelegatingStyledCellLabelProvider)fTreeViewer.getLabelProvider())
							.getStyledStringProvider().getStyledText(node).toString();
				}
				break;
			}
		}
		setToolTipText(overview);
	}

	/**
	 * Create an annotation in the tree ruler.
	 * 
	 * @param e
	 *            the PaintEvent.
	 * @param nodeToItem
	 *            the visible item on which to add this annotation.
	 * @param fill
	 *            the annotation's fill color.
	 * @param border
	 *            the annotation's border color.
	 */
	private void createAnnotation(PaintEvent e, TreeNodeToVisibleTreeItem treeNodeToVisibleTreeItem,
			Color fill, Color border) {
		TreeItem item = getDeepestVisibleTreeItem(treeNodeToVisibleTreeItem.getTreeItem(),
				treeNodeToVisibleTreeItem.getTreeItem());
		if (item != null) {
			int y = item.getBounds().y;
			int yRuler = getSize().y;
			if (isVerticalScrollBarEnabled()) {
				int yMin = Math.abs(item.getParent().getItems()[0].getBounds().y);
				int yMax = getLastVisibleItem().getBounds().y;
				int realYMax = yMax + yMin;
				if (realYMax > 0) {
					y = (y + yMin) * yRuler / realYMax;
				} else {
					y = (y + yMin) * yRuler;
				}
				if (y + Y_OFFSET + ANNOTATION_HEIGHT > yRuler) {
					y = yRuler - Y_OFFSET - ANNOTATION_HEIGHT;
				}
			}
			Rectangle rect = drawAnnotation(e.gc, 2, y + Y_OFFSET, getBounds().width - 5, ANNOTATION_HEIGHT,
					fill, border);
			annotationsData.put(rect, treeNodeToVisibleTreeItem);
		}
	}

	/**
	 * Returns the full tree path of the given tree item.
	 * 
	 * @param item
	 *            the given tree item.
	 * @return the full tree path of the given tree item.
	 */
	private TreePath getTreePathFromItem(TreeItem item) {
		LinkedList<Object> segments = Lists.newLinkedList();
		TreeItem parent = item;
		while (parent != null) {
			Object segment = parent.getData();
			Assert.isNotNull(segment);
			segments.addFirst(segment);
			parent = parent.getParentItem();
		}
		return new TreePath(segments.toArray());
	}

	/**
	 * Checks if the vertical scroll bar of the tree viewer associated with this tree ruler is activated and
	 * enabled.
	 * 
	 * @return true if the vertical scroll bar is activated and enabled, false otherwise.
	 */
	private boolean isVerticalScrollBarEnabled() {
		ScrollBar verticalBar = fTreeViewer.getTree().getVerticalBar();
		if (verticalBar != null) {
			return verticalBar.isVisible() && verticalBar.isEnabled();
		}
		return false;
	}

	/**
	 * Draw an annotation (a Rectangle) on this tree ruler.
	 * 
	 * @param gc
	 *            the swt GC.
	 * @param x
	 *            the x coordinate of the origin of the annotation.
	 * @param y
	 *            the y coordinate of the origin of the annotation.
	 * @param w
	 *            the width of the annotation.
	 * @param h
	 *            the height of the annotation.
	 * @param fill
	 *            the annotation's fill color.
	 * @param border
	 *            the annotation's border color.
	 * @return the annotation (a Rectangle).
	 */
	private Rectangle drawAnnotation(GC gc, int x, int y, int w, int h, Color fill, Color border) {
		Rectangle rect = new Rectangle(x, y, w, h);
		gc.setBackground(fill);
		gc.fillRectangle(rect);

		gc.setForeground(border);
		gc.drawRectangle(x, y, w, h);
		return rect;
	}

	/**
	 * Returns, for the given tree item, the deepest visible {@link TreeItem} in the Treeviewer associated
	 * with this TreeRuler.
	 * 
	 * @param currentItem
	 *            the given tree item.
	 * @param deepestVisibleItem
	 *            the deepest visible tree item (a parent or the item itself) of the given item.
	 * @return the deepest visible tree item (a parent or the item itself).
	 */
	private TreeItem getDeepestVisibleTreeItem(final TreeItem currentItem, final TreeItem deepestVisibleItem) {
		TreeItem item = null;
		if (!currentItem.isDisposed()) {
			TreeItem parent = currentItem.getParentItem();
			if (parent == null) {
				item = deepestVisibleItem;
			} else if (parent.getExpanded()) {
				item = getDeepestVisibleTreeItem(parent, deepestVisibleItem);
			} else {
				item = getDeepestVisibleTreeItem(parent, parent);
			}
		}
		return item;
	}

	/**
	 * Get the previous item of the given {@link TreeItem}.
	 * 
	 * @param treeItem
	 *            the given {@link TreeItem}.
	 * @param index
	 *            the index of the previous item.
	 * @return the previous item of the given {@link TreeItem}.
	 */
	private TreeItem getPreviousItem(TreeItem treeItem, int index) {
		TreeItem previousItem = treeItem;
		if (index > 0) {
			TreeItem parentItem = treeItem.getParentItem();
			if (parentItem != null) {
				int treeItemIndex = 0;
				for (TreeItem siblingItem : parentItem.getItems()) {
					if (siblingItem.equals(treeItem)) {
						break;
					}
					treeItemIndex++;
				}
				if (treeItemIndex == 0) {
					previousItem = getPreviousItem(parentItem, index - 1);
				} else if (treeItemIndex == 1) {
					TreeItem firstChild = parentItem.getItem(0);
					previousItem = getLastVisibleItem(firstChild);
					previousItem = getPreviousItem(previousItem, index - 1);
				} else {
					previousItem = getPreviousItem(getLastVisibleItem(parentItem.getItem(treeItemIndex - 1)),
							index - 1);
				}
			} else {
				// It is a root item. May be there are some previous root items.
				Tree tree = treeItem.getParent();
				int treeItemIndex = 0;
				for (TreeItem siblingItem : tree.getItems()) {
					if (siblingItem.equals(treeItem)) {
						break;
					}
					treeItemIndex++;
				}
				if (treeItemIndex == 0) {
					previousItem = treeItem;
				} else if (treeItemIndex == 1) {
					TreeItem firstRoot = tree.getItem(0);
					if (firstRoot.getExpanded()) {
						previousItem = getLastVisibleItem(firstRoot);
					} else {
						previousItem = firstRoot;
					}
					previousItem = getPreviousItem(previousItem, index - 1);
				} else {
					previousItem = tree.getItem(treeItemIndex - index);
				}
			}
		}
		return previousItem;
	}

	/**
	 * Returns the last visible {@link TreeItem} in the Treeviewer associated with this TreeRuler.
	 * 
	 * @return the last visible TreeItem in the Treeviewer associated with this TreeRuler.
	 */
	private TreeItem getLastVisibleItem() {
		int rootChildren = fTreeViewer.getTree().getItemCount();
		return getLastVisibleItem(fTreeViewer.getTree().getItem(rootChildren - 1));
	}

	/**
	 * Returns the last visible child of the given {@link TreeItem} in the Treeviewer associated with this
	 * TreeRuler.
	 * 
	 * @param item
	 *            teh given TreeItem.
	 * @return the last visible child of the given TreeItem in the Treeviewer associated with this TreeRuler.
	 */
	private TreeItem getLastVisibleItem(TreeItem item) {
		TreeItem lastVisibleItem = null;
		int directChildren = item.getItemCount();
		if (directChildren == 0 || !item.getExpanded()) {
			lastVisibleItem = item;
		} else {
			TreeItem lastDirectChildren = item.getItem(directChildren - 1);
			if (lastDirectChildren.getData() == null) {
				lastVisibleItem = item;
			} else if (lastDirectChildren.getExpanded()) {
				lastVisibleItem = getLastVisibleItem(lastDirectChildren);
			} else {
				lastVisibleItem = lastDirectChildren;
			}
		}
		return lastVisibleItem;
	}

	static class TreeNodeToVisibleTreeItem {
		private final TreeNode treeNode;

		private final TreeItem treeItem;

		public TreeNodeToVisibleTreeItem(TreeNode treeNode, TreeItem treeItem) {
			this.treeNode = treeNode;
			this.treeItem = treeItem;
		}

		public TreeNode getTreeNode() {
			return treeNode;
		}

		public TreeItem getTreeItem() {
			return treeItem;
		}
	}
}
