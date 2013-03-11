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

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.ICompareAccessor;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.IStructuralFeatureAccessor;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.ResourceContentsAccessorImpl;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.ICompareColorProvider;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.InsertionPoint;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TableMergeViewer extends StructuredMergeViewer {

	private final ICompareColorProvider fColorProvider;

	private TableViewer fTableViewer;

	private InfoViewer fInfoViewer;

	public TableMergeViewer(Composite parent, MergeViewerSide side, ICompareColorProvider colorProvider) {
		super(parent, side);
		fColorProvider = colorProvider;

		getStructuredViewer().getTable().addListener(SWT.EraseItem, new Listener() {
			public void handleEvent(Event event) {
				TableMergeViewer.this.handleEraseItemEvent(event);
			}
		});

		getStructuredViewer().getTable().addListener(SWT.MeasureItem, new ItemResizeListener());

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.AbstractMergeViewer#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginLeft = -1;
		layout.marginRight = -1;
		layout.marginTop = -1;
		layout.marginBottom = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);

		fInfoViewer = new InfoViewer(composite, getSide());
		fInfoViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		fTableViewer = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.FULL_SELECTION);
		fTableViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		return composite;
	}

	public final int getVerticalOffset() {
		return fInfoViewer.getControl().getSize().y - 2;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.AbstractMergeViewer.ui.internal.contentmergeviewer.AbstractMergeViewer#getStructuredViewer()
	 */
	@Override
	public final TableViewer getStructuredViewer() {
		return fTableViewer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.AbstractMergeViewer#setContentProvider(org.eclipse.jface.viewers.IContentProvider)
	 */
	@Override
	public void setContentProvider(IContentProvider contentProvider) {
		super.setContentProvider(contentProvider);
		fInfoViewer.setContentProvider(contentProvider);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.AbstractMergeViewer#setLabelProvider(org.eclipse.jface.viewers.IBaseLabelProvider)
	 */
	@Override
	public void setLabelProvider(IBaseLabelProvider labelProvider) {
		super.setLabelProvider(labelProvider);
		fInfoViewer.setLabelProvider(labelProvider);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#inputChanged(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void inputChanged(Object input, Object oldInput) {
		fTableViewer.setInput(input);
		fInfoViewer.setInput(input);
		((Composite)getControl()).layout(true);
	}

	private void handleEraseItemEvent(Event event) {
		TableItem tableItem = (TableItem)event.item;
		Object data = tableItem.getData();

		if (data instanceof InsertionPoint) {
			InsertionPoint insertionPoint = (InsertionPoint)data;
			paintItemDiffBox(event, insertionPoint.getDiff(), getBoundsForInsertionPoint(event));
		} else if (data instanceof IMergeViewerItem) {
			Diff diff = ((IMergeViewerItem)data).getDiff();
			if (diff != null) {
				paintItemDiffBox(event, diff, getBounds(event));
			}
		}
	}

	private void paintItemDiffBox(Event event, Diff diff, Rectangle bounds) {
		event.detail &= ~SWT.HOT;

		if (diff != null
				&& (diff.getState() == DifferenceState.DISCARDED || diff.getState() == DifferenceState.MERGED)) {
			return;
		}

		GC g = event.gc;
		Color oldBackground = g.getBackground();
		Color oldForeground = g.getForeground();

		setDiffColorsToGC(g, diff, isSelected(event));
		g.fillRectangle(bounds);
		g.drawRectangle(bounds);

		if (getSide() == MergeViewerSide.LEFT) {
			drawLineFromBoxToCenter(event, bounds, g);
		} else {
			drawLineFromCenterToBox(event, bounds, g);
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

	private void drawLineFromCenterToBox(Event event, Rectangle boxBounds, GC g) {
		TableItem tableItem = (TableItem)event.item;
		Rectangle itemBounds = tableItem.getBounds();
		Point from = new Point(0, 0);
		from.y = itemBounds.y + (itemBounds.height / 2);
		Point to = new Point(boxBounds.x, from.y);
		g.drawLine(from.x, from.y, to.x, to.y);
	}

	private void drawLineFromBoxToCenter(Event event, Rectangle boxBounds, GC g) {
		TableItem tableItem = (TableItem)event.item;
		Rectangle itemBounds = tableItem.getBounds();
		Rectangle clientArea = tableItem.getParent().getClientArea();
		Point from = new Point(0, 0);
		from.x = boxBounds.x + boxBounds.width;
		from.y = itemBounds.y + (itemBounds.height / 2);
		Point to = new Point(0, from.y);
		to.x = clientArea.x + clientArea.width;
		g.drawLine(from.x, from.y, to.x, to.y);
	}

	static boolean isSelected(Event event) {
		return (event.detail & SWT.SELECTED) != 0;
	}

	private static Rectangle getBounds(Event event) {
		TableItem tableItem = (TableItem)event.item;
		Table table = tableItem.getParent();
		Rectangle tableBounds = table.getClientArea();
		Rectangle itemBounds = tableItem.getBounds();

		Rectangle fill = new Rectangle(0, 0, 0, 0);
		fill.x = 2;
		fill.y = itemBounds.y + 2;
		// +x to add the icon and the expand "+" if we are in a tree
		fill.width = itemBounds.width + itemBounds.x;
		fill.height = itemBounds.height - 3;

		final GC g = event.gc;
		// If you wish to paint the selection beyond the end of last column, you must change the clipping
		// region.
		int columnCount = table.getColumnCount();
		if (event.index == columnCount - 1 || columnCount == 0) {
			int width = tableBounds.x + tableBounds.width - event.x;
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

	private static Rectangle getBoundsForInsertionPoint(Event event) {
		Rectangle fill = getBounds(event);
		TableItem tableItem = (TableItem)event.item;
		Rectangle tableBounds = tableItem.getParent().getClientArea();
		fill.y = fill.y + 6;
		fill.width = tableBounds.width / 4;
		fill.height = fill.height - 12;

		return fill;
	}

	private void setDiffColorsToGC(GC g, Diff diff, boolean selected) {
		boolean isThreeWay = false;
		if (getInput() instanceof ICompareAccessor) {
			Comparison comparison = ((ICompareAccessor)getInput()).getComparison();
			isThreeWay = comparison.isThreeWay();
		}
		g.setForeground(fColorProvider.getCompareColor().getStrokeColor(diff, isThreeWay, false, selected));
		g.setBackground(fColorProvider.getCompareColor().getFillColor(diff, isThreeWay, false, selected));
	}

	/**
	 * This will be used in order to resize the table items to an even height. Otherwise the lines we draw
	 * around it look somewhat flattened.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private static class ItemResizeListener implements Listener {
		/** The last item we resized. Will be used to workaround a windows-specific bug. */
		private Widget fLastWidget;

		/**
		 * The height to which we've resized the last item. Will be used to workaround a windows-specific bug.
		 */
		private int fLastHeight;

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
		 */
		public void handleEvent(Event event) {
			// Windows bug: prevents StackOverflow
			if (event.item == fLastWidget && event.height == fLastHeight) {
				return;
			}

			fLastWidget = event.item;
			fLastHeight = event.height;
			int newHeight = (int)(event.gc.getFontMetrics().getHeight() * 1.33d);
			// If odd, make even
			if ((newHeight & 1) == 1) {
				newHeight += 1;
			}
			event.height = newHeight;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#refresh()
	 */
	@Override
	public void refresh() {
		fInfoViewer.refresh();
		fTableViewer.refresh();
	}

	private static class InfoViewer extends ContentViewer {

		private final MergeViewerSide fSide;

		private final Composite fControl;

		private final Label fEObjectIcon;

		private final Label fEObjectLabel;

		private final Label fFeatureIcon;

		private final Label fFeatureLabel;

		private ISelection fSelection;

		private Object fInput;

		/**
		 * 
		 */
		public InfoViewer(Composite parent, MergeViewerSide side) {
			this.fControl = new Composite(parent, SWT.BORDER);
			this.fSide = side;

			fControl.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
			GridLayout layout = new GridLayout(3, false);
			layout.verticalSpacing = 0;
			layout.horizontalSpacing = 0;
			layout.marginLeft = 1;
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			layout.marginBottom = 0;
			fControl.setLayout(layout);

			Composite eObjectComposite = new Composite(fControl, SWT.NONE);
			GridLayout eObjectCompositelayout = new GridLayout(2, false);
			eObjectCompositelayout.verticalSpacing = 0;
			eObjectCompositelayout.horizontalSpacing = 0;
			eObjectCompositelayout.marginLeft = 0;
			eObjectCompositelayout.marginHeight = 0;
			eObjectCompositelayout.marginWidth = 0;
			eObjectCompositelayout.marginBottom = 0;
			eObjectComposite.setLayout(eObjectCompositelayout);
			fEObjectIcon = new Label(eObjectComposite, SWT.NONE);
			fEObjectIcon.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
			fEObjectLabel = new Label(eObjectComposite, SWT.NONE);
			fEObjectLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			eObjectComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true, 3, 1));

			Label lblIn = new Label(fControl, SWT.NONE);
			lblIn.setText("    "); //$NON-NLS-1$

			fFeatureIcon = new Label(fControl, SWT.NONE);
			fFeatureIcon.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
			fFeatureLabel = new Label(fControl, SWT.NONE);
			fFeatureLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

			hookControl(fControl);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.Viewer#getControl()
		 */
		@Override
		public Control getControl() {
			return fControl;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.Viewer#inputChanged(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected void inputChanged(Object input, Object oldInput) {
			fInput = input;
			fControl.setRedraw(false);
			try {
				refresh();
			} finally {
				fControl.setRedraw(true);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.Viewer#getSelection()
		 */
		@Override
		public ISelection getSelection() {
			return fSelection;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.Viewer#refresh()
		 */
		@Override
		public void refresh() {
			if (fInput instanceof IStructuralFeatureAccessor) {
				IStructuralFeatureAccessor featureAccessor = (IStructuralFeatureAccessor)fInput;

				EObject eObject = featureAccessor.getEObject(fSide);
				if (eObject == null) {
					if (fSide != MergeViewerSide.ANCESTOR) {
						eObject = featureAccessor.getEObject(MergeViewerSide.ANCESTOR);
						if (eObject == null) {
							eObject = featureAccessor.getEObject(fSide.opposite());
						}
					} else {
						eObject = featureAccessor.getEObject(MergeViewerSide.LEFT);
						if (eObject == null) {
							eObject = featureAccessor.getEObject(MergeViewerSide.RIGHT);
						}
					}
				}
				EStructuralFeature structuralFeature = featureAccessor.getStructuralFeature();

				if (getLabelProvider() instanceof ILabelProvider) {
					ILabelProvider labelProvider = (ILabelProvider)getLabelProvider();

					fFeatureIcon.setImage(labelProvider.getImage(structuralFeature));
					fFeatureLabel.setText(labelProvider.getText(structuralFeature));

					fEObjectIcon.setImage(labelProvider.getImage(eObject));
					fEObjectLabel.setText(labelProvider.getText(eObject));
				}

				fControl.layout(true);
			} else if (fInput instanceof ResourceContentsAccessorImpl) {
				final ResourceContentsAccessorImpl resourceContentAccessor = (ResourceContentsAccessorImpl)fInput;
				final Resource resource = resourceContentAccessor.getResource(fSide);

				if (getLabelProvider() instanceof ILabelProvider) {
					ILabelProvider labelProvider = (ILabelProvider)getLabelProvider();
					fFeatureLabel.setText("resource contents");

					fEObjectIcon.setImage(labelProvider.getImage(resource));
					fEObjectLabel.setText(labelProvider.getText(resource));
				}

				fControl.layout(true);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.Viewer#setSelection(org.eclipse.jface.viewers.ISelection, boolean)
		 */
		@Override
		public void setSelection(ISelection selection, boolean reveal) {
			fSelection = selection;
		}

	}
}
