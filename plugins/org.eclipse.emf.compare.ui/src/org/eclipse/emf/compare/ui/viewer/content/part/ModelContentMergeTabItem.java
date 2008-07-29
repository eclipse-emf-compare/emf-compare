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

import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.swt.widgets.Item;

// TODO Diff should be made optional. What if a tab doesn't display diff-related information?
/**
 * This class will be used to wrap {@link Item} subclasses such as {@link TreeItem} and {@link TableItem} to
 * allow us to call methods such as <tt>getBounds</tt> without explicitely casting each time we do so.
 * <p>
 * This wrapper will allow us to maintain a logical structure of the tree : which TreeItem corresponds to
 * which difference or visible parent ...
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class ModelContentMergeTabItem {
	/** The item this instance has actually been created for. */
	private final Item actualItem;

	/** Key of the color to use for UI components using this item. */
	private String curveColorID;

	/** Size of the center curve connecting for this item. */
	private int curveSize;

	/** Y coordinate at which the center curve should be connected for this item. */
	private int curveY;

	/** Holds a reference to the difference represented by the wrapped item. */
	private final DiffElement difference;

	/** Height of the item's control's header. */
	private int headerHeight;

	/** The visible item on which will be drawn UI marquees. */
	private Item visibleItem;

	/**
	 * Constructs a wrapper around the given item. This constructor specifies the color to use when drawing UI
	 * components with this item.
	 * 
	 * @param diff
	 *            Difference represented by this item.
	 * @param actual
	 *            The effective item this instance describes.
	 * @param visible
	 *            The visible item this instance holds UI information for. Defaults as <tt>actual</tt> if
	 *            <code>null</code>.
	 * @param drawingColor
	 *            Key of the color to use when drawing UI components for this item.
	 */
	public ModelContentMergeTabItem(DiffElement diff, Item actual, Item visible, String drawingColor) {
		this(diff, actual, visible, drawingColor, -1, -1);
	}

	/**
	 * Constructs a wrapper around the given item. This constructor specifies the color to use when drawing UI
	 * components with this item as well as the Y coordinate and size of the center curve connected to this
	 * item.
	 * 
	 * @param diff
	 *            Difference represented by this item.
	 * @param actual
	 *            The effective item this instance describes.
	 * @param visible
	 *            The visible item this instance holds UI information for. Defaults as <tt>actual</tt> if
	 *            <code>null</code>.
	 * @param drawingColor
	 *            Key of the color to use when drawing UI components for this item.
	 * @param curveExpectedY
	 *            Y coordinate of the center curve for this item.
	 * @param curveExpectedSize
	 *            Size of the center curve for this item.
	 */
	public ModelContentMergeTabItem(DiffElement diff, Item actual, Item visible, String drawingColor,
			int curveExpectedY, int curveExpectedSize) {
		difference = diff;
		actualItem = actual;
		visibleItem = visible;
		if (drawingColor == null)
			curveColorID = EMFCompareConstants.PREFERENCES_KEY_CHANGED_COLOR;
		else
			curveColorID = drawingColor;
		curveY = curveExpectedY;
		curveSize = curveExpectedSize;
	}

	/**
	 * Constructs a wrapper around the given item. This constructor specifies the color to use when drawing UI
	 * components with this item.
	 * 
	 * @param diff
	 *            Difference represented by this item.
	 * @param actual
	 *            The effective item this instance describes.
	 * @param drawingColor
	 *            Key of the color to use when drawing UI components for this item.
	 */
	public ModelContentMergeTabItem(DiffElement diff, Item actual, String drawingColor) {
		this(diff, actual, actual, drawingColor, -1, -1);
	}

	/**
	 * Returns the actual item this instance has been created for.
	 * 
	 * @return The actual item this instance has been created for.
	 */
	public Item getActualItem() {
		return actualItem;
	}

	/**
	 * Returns the color which should be used when drawing the center curve and other UI colored components
	 * using this item.
	 * <p>
	 * If no colors are specified for this item, {@link ModelContentMergeViewer#getChangedColor()} will be
	 * used as default.
	 * </p>
	 * 
	 * @return The color which should be used when drawing UI components.
	 */
	public String getCurveColor() {
		return curveColorID;
	}

	/**
	 * Returns the size of the center connecting curve.
	 * <p>
	 * If it is not specified or is negative, no curve will be drawn.
	 * </p>
	 * 
	 * @return The size of the center connecting curve.
	 */
	public int getCurveSize() {
		return curveSize;
	}

	/**
	 * Returns the Y coordinate at which the center connecting curve should be drawn on this item.
	 * <p>
	 * If it is not specified or is negative, no curve will be drawn.
	 * </p>
	 * 
	 * @return The Y coordinate of the center curve connected to this item.
	 */
	public int getCurveY() {
		return curveY;
	}

	/**
	 * Returns the represented difference.
	 * 
	 * @return The represented difference.
	 */
	public DiffElement getDiff() {
		return difference;
	}

	/**
	 * Returns the height of the item's control header.
	 * 
	 * @return The height of the item's control header.
	 */
	public int getHeaderHeight() {
		return headerHeight;
	}

	/**
	 * Returns the visible item on which UI marquees should be drawn.
	 * 
	 * @return The visible item on which UI marquees should be drawn.
	 */
	public Item getVisibleItem() {
		return visibleItem;
	}

	/**
	 * Sets a new value for this item's center curve size.
	 * 
	 * @param newCurveSize
	 *            New size to affect to the curve.
	 */
	public void setCurveSize(int newCurveSize) {
		curveSize = newCurveSize;
	}

	/**
	 * Sets a new value for this item's center curve Y ccordinate.
	 * 
	 * @param newCurveY
	 *            New Y coordinate of the center curve for this point.
	 */
	public void setCurveY(int newCurveY) {
		curveY = newCurveY;
	}

	/**
	 * Returns the value of the item's control header height.
	 * 
	 * @param newHeaderHeight
	 *            The value of the item's control header height.
	 */
	public void setHeaderHeight(int newHeaderHeight) {
		headerHeight = newHeaderHeight;
	}

	/**
	 * Sets the visible item for this instance.
	 * 
	 * @param newVisibleItem
	 *            New value of the visible Item reference.
	 */
	public void setVisibleItem(Item newVisibleItem) {
		visibleItem = newVisibleItem;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "actual item="; //$NON-NLS-1$
		result += actualItem;
		result += "\tvisible item="; //$NON-NLS-1$
		result += visibleItem;
		result += "\t(curveColor="; //$NON-NLS-1$
		result += curveColorID;
		result += ", curveSize="; //$NON-NLS-1$
		result += curveSize;
		result += ", curveY="; //$NON-NLS-1$
		result += curveY;
		result += ')';
		return result;
	}
}
