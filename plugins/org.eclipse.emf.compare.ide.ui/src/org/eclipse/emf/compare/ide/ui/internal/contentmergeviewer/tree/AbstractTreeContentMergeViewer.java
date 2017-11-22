/*******************************************************************************
 * Copyright (c) 2012, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - bugs 487595, 510442
 *     Martin Fleck - bug 483798
 *     Philip Langer - bug 527567
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree;

import static com.google.common.base.Predicates.equalTo;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.TreeMergeViewer;
import org.eclipse.emf.compare.rcp.ui.internal.util.MergeViewerUtil;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Specialized {@link org.eclipse.compare.contentmergeviewer.ContentMergeViewer} that uses
 * {@link org.eclipse.jface.viewers.TreeViewer} to display left, right and ancestor {@link EObject}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractTreeContentMergeViewer extends EMFCompareContentMergeViewer {

	private double[] fBasicCenterCurve;

	/**
	 * Creates a new {@link AbstractTreeContentMergeViewer} by calling the super constructor with the given
	 * parameters.
	 * <p>
	 * It calls {@link #buildControl(Composite)} as stated in its javadoc.
	 * <p>
	 * It sets a {@link TreeContentMergeViewerContentProvider specific}
	 * {@link #setContentProvider(org.eclipse.jface.viewers.IContentProvider) content provider} to properly
	 * display ancestor, left and right parts.
	 * 
	 * @param style
	 *            the style indicator for the parent
	 * @param bundle
	 *            the {@link ResourceBundle} for localization
	 * @param parent
	 *            the parent composite to build the UI in
	 * @param config
	 *            the {@link CompareConfiguration}
	 */
	public AbstractTreeContentMergeViewer(int style, ResourceBundle bundle, EMFCompareConfiguration config) {
		super(style, bundle, config);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getAncestorMergeViewer()
	 */
	// see createMergeViewer() to see it is safe
	@Override
	protected TreeMergeViewer getAncestorMergeViewer() {
		return (TreeMergeViewer)super.getAncestorMergeViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getLeftMergeViewer()
	 */
	// see createMergeViewer() to see it is safe
	@Override
	protected TreeMergeViewer getLeftMergeViewer() {
		return (TreeMergeViewer)super.getLeftMergeViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getRightMergeViewer()
	 */
	// see createMergeViewer() to see it is safe
	@Override
	protected TreeMergeViewer getRightMergeViewer() {
		return (TreeMergeViewer)super.getRightMergeViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#getContents(boolean)
	 */
	@Override
	protected byte[] getContents(boolean left) {
		return null;
	}

	/**
	 * Adds all required listeners to the given {@link TreeMergeViewer}.
	 * 
	 * @param treeMergeViewer
	 *            the {@link TreeMergeViewer}.
	 */
	protected void hookListeners(TreeMergeViewer treeMergeViewer) {

		treeMergeViewer.getStructuredViewer().getTree().getVerticalBar().addListener(SWT.Selection,
				new Listener() {
					public void handleEvent(Event event) {
						redrawCenterControl();
					}
				});

		treeMergeViewer.getStructuredViewer().getTree().addMouseWheelListener(new MouseWheelListener() {
			public void mouseScrolled(MouseEvent e) {
				redrawCenterControl();
			}
		});
		treeMergeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				redrawCenterControl();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#paintCenter(org.eclipse.swt.widgets.Canvas,
	 *      org.eclipse.swt.graphics.GC)
	 */
	@Override
	protected void paintCenter(GC g) {
		TreeMergeViewer leftMergeViewer = getLeftMergeViewer();
		TreeMergeViewer rightMergeViewer = getRightMergeViewer();

		Tree leftTree = leftMergeViewer.getStructuredViewer().getTree();
		Tree rightTree = rightMergeViewer.getStructuredViewer().getTree();

		Rectangle leftClientArea = leftTree.getClientArea();
		Rectangle rightClientArea = rightTree.getClientArea();

		final List<TreeItem> leftItems = getExpandedTreeItems(leftTree);
		final List<TreeItem> rightItems = getExpandedTreeItems(rightTree);

		final ImmutableSet<TreeItem> selection = ImmutableSet.copyOf(leftTree.getSelection());

		for (TreeItem leftItem : leftItems) {
			final boolean selected = Iterables.any(selection, equalTo(leftItem));
			IMergeViewerItem leftData = (IMergeViewerItem)leftItem.getData();
			final Diff leftDiff = leftData.getDiff();
			if (leftDiff != null) {
				if (!MergeViewerUtil.isMarkAsMerged(leftDiff, leftData, getCompareConfiguration())) {
					TreeItem rightItem = findRightTreeItemFromLeftDiff(rightItems, leftDiff, leftData);

					if (rightItem != null) {
						final Color strokeColor = getCompareColor().getStrokeColor(leftDiff, isThreeWay(),
								false, selected);
						g.setForeground(strokeColor);
						drawCenterLine(g, leftClientArea, rightClientArea, leftItem, rightItem);
					}
				}
			}
		}
	}

	private List<TreeItem> getExpandedTreeItems(Tree tree) {
		return getExpandedTreeItems(tree.getItems());
	}

	/**
	 * @param items
	 * @return
	 */
	private List<TreeItem> getExpandedTreeItems(TreeItem[] items) {
		List<TreeItem> ret = newArrayList();
		for (TreeItem item : items) {
			ret.add(item);
			if (!item.getExpanded()) {
				continue;
			}
			ret.addAll(getExpandedTreeItems(item.getItems()));
		}
		return ret;
	}

	private void drawCenterLine(GC g, Rectangle leftClientArea, Rectangle rightClientArea, TreeItem leftItem,
			TreeItem rightItem) {
		Control control = getCenterControl();
		Point from = new Point(0, 0);
		Point to = new Point(0, 0);

		Rectangle leftBounds = leftItem.getBounds();
		Rectangle rightBounds = rightItem.getBounds();

		from.y = leftBounds.y + (leftBounds.height / 2) - leftClientArea.y;
		if ("gtk".equals(SWT.getPlatform())) { //$NON-NLS-1$
			from.y -= 1;
		} else if ("win32".equals(SWT.getPlatform())) { //$NON-NLS-1$
			from.y += 1;
		}

		to.x = control.getBounds().width;
		to.y = rightBounds.y + (rightBounds.height / 2) - rightClientArea.y;
		if ("gtk".equals(SWT.getPlatform())) { //$NON-NLS-1$
			to.y -= 1;
		} else if ("win32".equals(SWT.getPlatform())) { //$NON-NLS-1$
			to.y += 1;
		}

		int[] points = getCenterCurvePoints(from, to);
		for (int i = 1; i < points.length; i++) {
			g.drawLine(from.x + i - 1, points[i - 1], i, points[i]);
		}
	}

	private TreeItem findRightTreeItemFromLeftDiff(List<TreeItem> rightItems, Diff leftDiff,
			IMergeViewerItem leftData) {
		TreeItem ret = null;
		for (TreeItem rightItem : rightItems) {
			IMergeViewerItem rightData = (IMergeViewerItem)rightItem.getData();
			final Diff rightDiff = rightData.getDiff();
			if (leftDiff == rightDiff) {
				return rightItem;
			} else if (rightData.getAncestor() == leftData.getAncestor()
					&& rightData.getRight() == leftData.getRight()
					&& rightData.getLeft() == leftData.getLeft()) {
				ret = rightItem;
			}
		}
		return ret;
	}

	private int[] getCenterCurvePoints(Point from, Point to) {
		int startx = from.x;
		int starty = from.y;
		int endx = to.x;
		int endy = to.y;
		if (fBasicCenterCurve == null) {
			buildBaseCenterCurve(endx - startx);
		}
		double height = endy - starty;
		height = height / 2;
		int width = endx - startx;
		int[] points = new int[width];
		for (int i = 0; i < width; i++) {
			points[i] = (int)(-height * fBasicCenterCurve[i] + height + starty);
		}
		return points;
	}

	private void buildBaseCenterCurve(int w) {
		double width = w;
		fBasicCenterCurve = new double[getCenterWidth()];
		for (int i = 0; i < getCenterWidth(); i++) {
			double r = i / width;
			fBasicCenterCurve[i] = Math.cos(Math.PI * r);
		}
	}
}
