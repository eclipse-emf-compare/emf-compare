/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.content.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * We want to avoid flickering as much as possible for our draw operations on the center part, yet we can't
 * use double buffering to draw on it. We will then draw on a {@link Canvas} moved above that center part.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public abstract class AbstractCenterPart extends Canvas {
	/** Buffer used by this {@link Canvas} to smoothly paint its content. */
	protected Image buffer;

	/**
	 * This array is used to compute the curve to draw between left and right elements.
	 */
	private double[] baseCenterCurve;

	/** Keeps track of the last "left visible" items list. */
	private List<ModelContentMergeTabItem> lastLeftVisible;

	/** Keeps track of the last "right visible" items list. */
	private List<ModelContentMergeTabItem> lastRightVisible;

	/** Keeps track of the last visible diffs. */
	private List<DiffElement> lastVisibleDiffs;

	/**
	 * Default constructor, instantiates the canvas given its parent.
	 * 
	 * @param parent
	 *            Parent of the canvas.
	 */
	public AbstractCenterPart(Composite parent) {
		super(parent, SWT.NO_BACKGROUND | SWT.NO_MERGE_PAINTS | SWT.NO_REDRAW_RESIZE);

		final PaintListener paintListener = new PaintListener() {
			public void paintControl(PaintEvent event) {
				doubleBufferedPaint(event.gc);
			}
		};
		addPaintListener(paintListener);

		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if (buffer != null) {
					buffer.dispose();
					buffer = null;
				}
				removePaintListener(paintListener);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		if (buffer != null) {
			buffer.dispose();
		}
		baseCenterCurve = null;
	}

	/**
	 * Clients should implement this method for the actual drawing.
	 * 
	 * @param gc
	 *            {@link GC} used for the painting.
	 */
	public abstract void doPaint(GC gc);

	/**
	 * Draws a line connecting the given right and left items.
	 * 
	 * @param gc
	 *            {@link GC graphics configuration} on which to actually draw.
	 * @param leftItem
	 *            Left of the two items to connect.
	 * @param rightItem
	 *            Right of the items to connect.
	 */
	protected void drawLine(GC gc, ModelContentMergeTabItem leftItem, ModelContentMergeTabItem rightItem) {
		if (leftItem == null || rightItem == null)
			return;

		final Rectangle drawingBounds = getBounds();
		RGB color = ModelContentMergeViewer.getColor(leftItem.getCurveColor());
		if (rightItem.getCurveColor() != leftItem.getCurveColor()
				&& rightItem.getCurveColor() != EMFCompareConstants.PREFERENCES_KEY_CHANGED_COLOR)
			color = ModelContentMergeViewer.getColor(rightItem.getCurveColor());

		// Defines all variables needed for drawing the line.
		final int treeTabBorder = 5;
		final int leftX = 0;
		final int rightX = drawingBounds.width;
		final int leftY;
		final int rightY;
		if (System.getProperty("os.name").contains("Windows")) { //$NON-NLS-1$ //$NON-NLS-2$
			leftY = leftItem.getCurveY() + treeTabBorder;
			rightY = rightItem.getCurveY() + treeTabBorder;
		} else {
			leftY = leftItem.getCurveY() + leftItem.getHeaderHeight() + treeTabBorder;
			rightY = rightItem.getCurveY() + rightItem.getHeaderHeight() + treeTabBorder;
		}
		final int lineWidth = leftItem.getCurveSize();

		// Performs the actual drawing
		gc.setForeground(new Color(getDisplay(), color));
		gc.setLineWidth(lineWidth);
		gc.setLineStyle(SWT.LINE_SOLID);
		final int[] points = getCenterCurvePoints(leftX, leftY, rightX, rightY);
		for (int i = 1; i < points.length; i++) {
			gc.drawLine(leftX + i - 1, points[i - 1], leftX + i, points[i]);
		}
	}

	/**
	 * This will retain the visible differences in the given list.
	 * 
	 * @param leftVisible
	 *            Items visible on the left part.
	 * @param rightVisible
	 *            Items visible on the right part.
	 * @return Filtered list of DiffElements.
	 */
	protected List<DiffElement> retainVisibleDiffs(List<ModelContentMergeTabItem> leftVisible,
			List<ModelContentMergeTabItem> rightVisible) {
		if (!leftVisible.equals(lastLeftVisible) || !rightVisible.equals(lastRightVisible)) {
			lastVisibleDiffs = new ArrayList<DiffElement>(leftVisible.size() + rightVisible.size());
			for (ModelContentMergeTabItem left : leftVisible) {
				lastVisibleDiffs.add(left.getDiff());
			}
			for (ModelContentMergeTabItem right : rightVisible) {
				lastVisibleDiffs.add(right.getDiff());
			}
		}
		return lastVisibleDiffs;
	}

	/**
	 * Paints this component using double-buffering.
	 * 
	 * @param dest
	 *            Destination {@link GC graphics}.
	 */
	void doubleBufferedPaint(GC dest) {
		final Point size = getSize();

		if (size.x <= 0 || size.y <= 0)
			return;

		if (buffer != null) {
			final Rectangle bufferBounds = buffer.getBounds();
			if (bufferBounds.width != size.x || bufferBounds.height != size.y) {
				buffer.dispose();
				buffer = null;
			}
		}

		if (buffer == null)
			buffer = new Image(getDisplay(), size.x, size.y);

		final GC gc = new GC(buffer);
		// Draw lines on the left and right edges
		gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		gc.drawLine(0, 0, 0, getBounds().height);
		gc.drawLine(getBounds().width - 1, 0, getBounds().width - 1, getBounds().height);
		try {
			gc.setBackground(getBackground());
			gc.fillRectangle(0, 0, size.x, size.y);
			doPaint(gc);
		} finally {
			gc.dispose();
		}

		dest.drawImage(buffer, 0, 0);
	}

	/**
	 * Computes the points needed to draw a curve of the given width.
	 * 
	 * @param width
	 *            This is the width of the curve to build.
	 */
	private void buildBaseCenterCurve(int width) {
		final double doubleWidth = width;
		baseCenterCurve = new double[ModelContentMergeViewer.CENTER_WIDTH];
		for (int i = 0; i < ModelContentMergeViewer.CENTER_WIDTH; i++) {
			final double r = i / doubleWidth;
			baseCenterCurve[i] = Math.cos(Math.PI * r);
		}
	}

	/**
	 * Computes the points to connect for the curve between the two items to connect.
	 * 
	 * @param startx
	 *            X coordinate of the starting point.
	 * @param starty
	 *            Y coordinate of the starting point.
	 * @param endx
	 *            X coordinate of the ending point.
	 * @param endy
	 *            Y coordinate of the ending point.
	 * @return The points to connect to draw the curve between the two items to connect.
	 */
	private int[] getCenterCurvePoints(int startx, int starty, int endx, int endy) {
		if (baseCenterCurve == null) {
			buildBaseCenterCurve(endx - startx);
		}
		double height = endy - starty;
		height = height / 2;
		final int width = endx - startx;
		final int[] points = new int[width];
		for (int i = 0; i < width; i++) {
			points[i] = (int)(-height * baseCenterCurve[i] + height + starty);
		}
		return points;
	}
}
