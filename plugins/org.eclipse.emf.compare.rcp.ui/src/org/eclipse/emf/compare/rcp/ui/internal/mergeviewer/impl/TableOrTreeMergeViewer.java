/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl;

import com.google.common.base.Objects;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.ICompareColor;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scrollable;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class TableOrTreeMergeViewer extends StructuredMergeViewer {

	private final ICompareColor.Provider fColorProvider;

	private Listener fEraseItemListener;

	private MesureItemListener fMesureItemListener;

	private Listener fPaintItemListener;

	public TableOrTreeMergeViewer(Composite parent, MergeViewerSide side, ICompareColor.Provider colorProvider) {
		super(parent, side);

		getStructuredViewer().setUseHashlookup(true);
		getStructuredViewer().setComparer(new ElementComparer());

		fColorProvider = colorProvider;
		fEraseItemListener = new Listener() {
			public void handleEvent(Event event) {
				TableOrTreeMergeViewer.this.handleEraseItemEvent(event);
			}
		};
		getStructuredViewer().getControl().addListener(SWT.EraseItem, fEraseItemListener);
		fPaintItemListener = new Listener() {
			public void handleEvent(Event event) {
				TableOrTreeMergeViewer.this.handlePaintItemEvent(event);
			}
		};
		getStructuredViewer().getControl().addListener(SWT.PaintItem, fPaintItemListener);

		fMesureItemListener = new MesureItemListener();
		getStructuredViewer().getControl().addListener(SWT.MeasureItem, fMesureItemListener);
	}

	/**
	 * @param event
	 */
	protected void handlePaintItemEvent(Event event) {
		TableOrTreeItemWrapper itemWrapper = TableOrTreeItemWrapper.create((Item)event.item);
		String text = itemWrapper.getText(event.index);
		Image image = itemWrapper.getImage(event.index);
		/* center column 1 vertically */

		Point size = event.gc.textExtent(text);
		int yOffset = Math.max(0, (event.height - size.y) / 2);
		int xOffset = event.x;
		if (image != null) {
			int imageYOffset = Math.max(0, (event.height - image.getBounds().height) / 2);
			event.gc.drawImage(image, event.x + IMAGE_GAP, event.y + imageYOffset);
			xOffset += IMAGE_GAP + image.getBounds().width;
		}
		event.gc.drawText(text, xOffset + TEXT_GAP, event.y + yOffset, true);
		event.width += 2;
	}

	static final int IMAGE_GAP = 5;

	static final int DELTA_IMAGE_GAP = 2;

	static final int TEXT_GAP = 2;

	static final int CELL_GAP = 1;

	/**
	 * @param event
	 */
	protected void handleEraseItemEvent(Event event) {
		// let the #handlePaintItem handle the painting of foreground
		event.detail &= ~SWT.FOREGROUND;

		Item item = (Item)event.item;
		TableOrTreeItemWrapper itemWrapper = TableOrTreeItemWrapper.create(item);
		Object data = itemWrapper.getData();

		if (data instanceof IMergeViewerItem) {
			IMergeViewerItem mergeViewerItem = ((IMergeViewerItem)data);
			boolean doPaint = true;
			Diff diff = mergeViewerItem.getDiff();
			for (IDifferenceFilter filter : getSelectedFilters()) {
				if (filter.getPredicateWhenSelected().apply(diff)) {
					doPaint = false;
					break;
				}
			}
			if (doPaint) {
				if (mergeViewerItem.isInsertionPoint()) {
					paintItemDiffBox(event, itemWrapper, diff, getBoundsForInsertionPoint(event, itemWrapper));
				} else {
					if (diff != null) {
						paintItemDiffBox(event, itemWrapper, diff, getBounds(event, itemWrapper));
					}
				}
			}
		}
	}

	private void paintItemDiffBox(Event event, TableOrTreeItemWrapper itemWrapper, Diff diff, Rectangle bounds) {
		event.detail &= ~SWT.HOT;

		GC g = event.gc;
		Color oldBackground = g.getBackground();
		Color oldForeground = g.getForeground();

		setGCStyleForDiff(g, diff, isSelected(event));
		g.fillRectangle(bounds);
		g.drawRectangle(bounds);

		if (diff.getKind() == DifferenceKind.MOVE) {
			g.setLineStyle(SWT.LINE_SOLID);
		}

		switch (getSide()) {
			case LEFT:
				drawLineFromBoxToCenter(itemWrapper, bounds, g);
				break;
			case RIGHT:
				drawLineFromCenterToBox(itemWrapper, bounds, g);
				break;
		}

		if (isSelected(event)) {
			g.setForeground(event.display.getSystemColor(SWT.COLOR_LIST_FOREGROUND));
			g.setBackground(event.display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
			event.detail &= ~SWT.SELECTED;
		} else {
			g.setBackground(oldBackground);
			g.setForeground(oldForeground);
		}
	}

	private void drawLineFromCenterToBox(TableOrTreeItemWrapper itemWrapper, Rectangle boxBounds, GC g) {
		TableOrTreeItemWrapper parent = itemWrapper.getParentItem();
		final int xOffset;
		if (getContentProvider() instanceof ITreeContentProvider) {
			final boolean hasChildren = ((ITreeContentProvider)getContentProvider()).hasChildren(itemWrapper
					.getData());
			if (hasChildren) {
				if (parent != null) {
					xOffset = parent.getImageBounds(0).x;
				} else {
					xOffset = 0;
				}
			} else {
				xOffset = boxBounds.x;
			}
		} else {
			xOffset = boxBounds.x;
		}

		Rectangle itemBounds = itemWrapper.getBounds();
		Point from = new Point(0, 0);
		from.y = itemBounds.y + (itemBounds.height / 2);
		Point to = new Point(xOffset, from.y);
		g.drawLine(from.x, from.y, to.x, to.y);
	}

	private void drawLineFromBoxToCenter(TableOrTreeItemWrapper itemWrapper, Rectangle boxBounds, GC g) {
		Rectangle itemBounds = itemWrapper.getBounds();
		Rectangle clientArea = itemWrapper.getParent().getClientArea();
		Point from = new Point(0, 0);
		from.x = boxBounds.x + boxBounds.width;
		from.y = itemBounds.y + (itemBounds.height / 2);
		Point to = new Point(0, from.y);
		to.x = clientArea.x + clientArea.width;
		g.drawLine(from.x, from.y, to.x, to.y);
	}

	private static Rectangle getBoundsForInsertionPoint(Event event, TableOrTreeItemWrapper itemWrapper) {
		Rectangle fill = getBounds(event, itemWrapper);
		Rectangle treeBounds = itemWrapper.getParent().getClientArea();
		Rectangle itemBounds = itemWrapper.getBounds();
		fill.x = itemWrapper.getImageBounds(0).x + 2;
		fill.y = fill.y + (itemBounds.height / 3);
		fill.width = treeBounds.width / 4;
		fill.height = itemBounds.height / 3;

		return fill;
	}

	private void setGCStyleForDiff(GC g, Diff diff, boolean selected) {
		final Comparison comparison = diff.getMatch().getComparison();
		final boolean isThreeWay = comparison.isThreeWay();

		if (diff.getKind() == DifferenceKind.MOVE) {
			g.setLineStyle(SWT.LINE_DOT);
		}

		g.setForeground(fColorProvider.getCompareColor().getStrokeColor(diff, isThreeWay, false, selected));
		g.setBackground(fColorProvider.getCompareColor().getFillColor(diff, isThreeWay, false, selected));
	}

	private static Rectangle getBounds(Event event, TableOrTreeItemWrapper itemWrapper) {
		Scrollable tree = itemWrapper.getParent();
		Rectangle treeBounds = tree.getClientArea();
		Rectangle itemBounds = itemWrapper.getBounds();
		Rectangle imageBounds = itemWrapper.getImageBounds(0);

		Rectangle fill = new Rectangle(0, 0, 0, 0);
		fill.x = itemBounds.x - imageBounds.width;
		fill.y = itemBounds.y;
		if (!"cocoa".equals(SWT.getPlatform())) { //$NON-NLS-1$
			fill.y += 1;
		}
		// +x to add the icon and the expand "+" if we are in a tree
		fill.width = itemBounds.width + imageBounds.width + DELTA_IMAGE_GAP;
		fill.height = itemBounds.height - 1;
		if (!"cocoa".equals(SWT.getPlatform())) { //$NON-NLS-1$
			fill.height -= 3;
		}

		final GC g = event.gc;
		// If you wish to paint the selection beyond the end of last column, you must change the clipping
		// region.
		int columnCount = itemWrapper.getParentColumnCount();
		if (event.index == columnCount - 1 || columnCount == 0) {
			int width = treeBounds.x + treeBounds.width - event.x;
			if (width > 0) {
				Region region = new Region();
				g.getClipping(region);
				region.add(event.x, event.y, width, event.height);
				g.setClipping(region);
				region.dispose();
			}
		}
		g.setAdvanced(true);

		return fill;
	}

	private static boolean isSelected(Event event) {
		return (event.detail & SWT.SELECTED) != 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.impl.StructuredMergeViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		getStructuredViewer().getControl().removeListener(SWT.MeasureItem, fMesureItemListener);
		getStructuredViewer().getControl().removeListener(SWT.EraseItem, fEraseItemListener);
		getStructuredViewer().getControl().removeListener(SWT.PaintItem, fPaintItemListener);
		super.handleDispose(event);
	}

	/**
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	public static final class ElementComparer implements IElementComparer {
		public int hashCode(Object element) {
			final int hashCode;
			if (element instanceof IMergeViewerItem) {
				IMergeViewerItem item = (IMergeViewerItem)element;
				Diff diff = item.getDiff();
				if (diff != null && diff.getConflict() != null
						&& diff.getConflict().getKind() == ConflictKind.PSEUDO) {
					// we do not create only one item per diff in pseudo conflict, so we hash the conflict and
					// not the diff
					hashCode = Objects.hashCode(item.getAncestor(), diff.getConflict());
				} else if (diff != null && item.getLeft() == null && item.getRight() == null) {
					hashCode = Objects.hashCode(item.getAncestor(), diff);
				} else {
					hashCode = Objects.hashCode(item.getLeft(), item.getRight(), item.getAncestor(), diff);
				}
			} else {
				hashCode = element.hashCode();
			}
			return hashCode;
		}

		public boolean equals(Object a, Object b) {
			final boolean ret;
			if (a != b && a instanceof IMergeViewerItem && b instanceof IMergeViewerItem) {
				IMergeViewerItem itemA = (IMergeViewerItem)a;
				IMergeViewerItem itemB = (IMergeViewerItem)b;
				Diff diffA = itemA.getDiff();
				Diff diffB = itemB.getDiff();
				if (diffA != null && diffA.getConflict() != null
						&& diffA.getConflict().getKind() == ConflictKind.PSEUDO && diffB != null
						&& diffB.getConflict() != null
						&& diffB.getConflict().getKind() == ConflictKind.PSEUDO) {
					// pseudo conflict
					ret = Objects.equal(itemA.getAncestor(), itemB.getAncestor())
							&& Objects.equal(diffA.getConflict(), diffB.getConflict());
				} else if (diffA != null && diffB != null && itemA.getLeft() == null
						&& itemA.getRight() == null && itemB.getLeft() == null && itemB.getRight() == null) {
					ret = Objects.equal(diffA, diffB);
				} else {
					ret = Objects.equal(itemA.getLeft(), itemB.getLeft())
							&& Objects.equal(itemA.getRight(), itemB.getRight())
							&& Objects.equal(itemA.getAncestor(), itemB.getAncestor())
							&& Objects.equal(diffA, diffB);
				}
			} else {
				ret = Objects.equal(a, b);
			}
			return ret;
		}
	}

	/**
	 * This will be used in order to resize the table items to an even height. Otherwise the lines we draw
	 * around it look somewhat flattened.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	public static class MesureItemListener implements Listener {
		/**
		 * The height to which we will resize items.
		 */
		private int fHeight = Integer.MIN_VALUE;

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
		 */
		public void handleEvent(Event event) {
			if (fHeight == Integer.MIN_VALUE) {
				TableOrTreeItemWrapper itemWrapper = TableOrTreeItemWrapper.create(((Item)event.item));
				Rectangle imageBounds = itemWrapper.getImageBounds(0);
				fHeight = imageBounds.height + 3;
			}
			event.height = fHeight;
		}
	}
}
