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
package org.eclipse.emf.compare.ui.viewer.content.part.property;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.diff.metamodel.AttributeChange;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.UnMatchElement;
import org.eclipse.emf.compare.ui.EMFCompareUIMessages;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.emf.compare.ui.util.EMFCompareEObjectUtils;
import org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer;
import org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab;
import org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabFolder;
import org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabItem;
import org.eclipse.emf.compare.util.AdapterUtils;
import org.eclipse.emf.compare.util.EMFCompareMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

/**
 * Represents the property view under a {@link ModelContentMergeTabFolder}'s property tab.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class ModelContentMergePropertyTab extends TableViewer implements IModelContentMergeViewerTab {
	/** <code>int</code> representing this viewer part side. */
	protected final int partSide;

	/** Maps a TreeItem to its data. The key used is the URI of the data. */
	private final Map<String, ModelContentMergeTabItem> dataToItem = new EMFCompareMap<String, ModelContentMergeTabItem>();

	/**
	 * Keeps a reference to all the differences detected by the comparison. This list will be cleared as soon
	 * as {@link #dataToItem} will be populated.
	 */
	private final List<DiffElement> differences = new ArrayList<DiffElement>();

	/** Keeps a reference to the containing tab folder. */
	private final ModelContentMergeTabFolder parent;

	/**
	 * Creates a table viewer on a newly-created table control under the given parent. The table control is
	 * created using the given style bits.
	 * 
	 * @param parentComposite
	 *            the parent control.
	 * @param side
	 *            Side of this viewer part.
	 * @param parentFolder
	 *            Parent folder of this tab.
	 */
	public ModelContentMergePropertyTab(Composite parentComposite, int side,
			ModelContentMergeTabFolder parentFolder) {
		super(parentComposite, SWT.NONE);
		partSide = side;
		parent = parentFolder;

		setContentProvider(new PropertyContentProvider());
		setLabelProvider(new PropertyLabelProvider(AdapterUtils.getAdapterFactory()));
		getTable().addPaintListener(new PropertyPaintListener());

		setUseHashlookup(true);
		getTable().setLinesVisible(true);
		getTable().setHeaderVisible(true);

		final GC gc = new GC(getTable());
		gc.setFont(getTable().getFont());
		final FontMetrics metrics = gc.getFontMetrics();
		gc.dispose();

		final TableColumn nameColumn = new TableColumn(getTable(), SWT.LEFT);
		nameColumn.setText(EMFCompareUIMessages.getString("ModelContentMergePropertyPart.column1.name")); //$NON-NLS-1$
		nameColumn.setWidth(Dialog.convertWidthInCharsToPixels(metrics, nameColumn.getText().length() * 3));
		final TableColumn weightsColumn = new TableColumn(getTable(), SWT.RIGHT);
		weightsColumn.setText(EMFCompareUIMessages.getString("ModelContentMergePropertyPart.column2.name")); //$NON-NLS-1$
		weightsColumn.setWidth(Dialog.convertWidthInCharsToPixels(metrics,
				weightsColumn.getText().length() * 3));

		mapDifferences();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab#dispose()
	 */
	public void dispose() {
		dataToItem.clear();
		differences.clear();
		getTable().dispose();
	}

	/**
	 * Returns the widget representing the given {@link DiffElement} in the table.
	 * 
	 * @param diff
	 *            {@link DiffElement} to seek in the table.
	 * @return The widget representing the given {@link DiffElement}.
	 * @see org.eclipse.jface.viewers.StructuredViewer#findItem(Object)
	 */
	public TableItem find(DiffElement diff) {
		final EObject inputEObject = ((PropertyContentProvider)getContentProvider()).getInputEObject();
		TableItem item = null;
		if (diff instanceof AttributeChange) {
			final AttributeChange theDiff = (AttributeChange)diff;
			if (theDiff.getLeftElement() == inputEObject || theDiff.getRightElement() == inputEObject)
				item = (TableItem)findItem(theDiff.getAttribute());
		} else if (diff instanceof ReferenceChange) {
			final ReferenceChange theDiff = (ReferenceChange)diff;
			if (theDiff.getLeftElement() == inputEObject || theDiff.getRightElement() == inputEObject)
				item = (TableItem)findItem(theDiff.getReference());
		}
		return item;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab#getSelectedElements()
	 */
	public List<? extends Item> getSelectedElements() {
		final List<TableItem> result = new ArrayList<TableItem>();
		final Control control = getControl();
		if (control != null && !control.isDisposed()) {
			final List<?> list = getSelectionFromWidget();
			for (Object o : list) {
				final Widget w = findItem(o);
				if (w instanceof TableItem)
					result.add((TableItem)w);
			}
		}
		return result;
	}

	/**
	 * Returns the side of this viewer part.
	 * 
	 * @return The side of this viewer part.
	 */
	public int getSide() {
		return partSide;
	}

	/**
	 * Returns the width of the columns displayed.
	 * 
	 * @return The width of the columns displayed.
	 */
	public int getTotalColumnsWidth() {
		int width = 0;
		for (final TableColumn col : getTable().getColumns())
			width += col.getWidth();
		return width;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab#getUIItem(org.eclipse.emf.ecore.EObject)
	 */
	public ModelContentMergeTabItem getUIItem(EObject data) {
		final EObject element;
		if (getInput() instanceof UnMatchElement)
			element = ((UnMatchElement)getInput()).getElement();
		else if (partSide == EMFCompareConstants.LEFT)
			element = ((Match2Elements)getInput()).getLeftElement();
		else
			element = ((Match2Elements)getInput()).getRightElement();

		String key = EcoreUtil.getURI(element).fragment();
		final String[] fragments = EcoreUtil.getURI(data).fragment().split("/"); //$NON-NLS-1$
		if (fragments.length > 0)
			key += '/' + fragments[fragments.length - 1];
		final ModelContentMergeTabItem item = dataToItem.get(key);
		if (item != null) {
			if (getSelectedElements().contains(item.getActualItem()))
				item.setCurveSize(2);
			else
				item.setCurveSize(1);
			item.setCurveY(((TableItem)item.getActualItem()).getBounds().y
					+ ((TableItem)item.getActualItem()).getBounds().height / 2);
		}
		return item;
	}

	/**
	 * Returns a list of this tab's visible elements.
	 * <p>
	 * For the property tab, we won't try and find the "visible" elements and this will return the whole
	 * table's content.
	 * </p>
	 * 
	 * @return List of this tab's elements.
	 */
	public List<ModelContentMergeTabItem> getVisibleElements() {
		if (dataToItem.size() > 0 && dataToItem.values().iterator().next().getActualItem().isDisposed())
			mapTableItems();

		final List<ModelContentMergeTabItem> result = new ArrayList<ModelContentMergeTabItem>();
		for (String data : dataToItem.keySet()) {
			final EObject element;
			if (getInput() instanceof UnMatchElement)
				element = ((UnMatchElement)getInput()).getElement();
			else if (partSide == EMFCompareConstants.LEFT)
				element = ((Match2Elements)getInput()).getLeftElement();
			else
				element = ((Match2Elements)getInput()).getRightElement();
			if (data.startsWith(EcoreUtil.getURI(element).fragment())) {
				final ModelContentMergeTabItem next = dataToItem.get(data);
				final TableItem nextTableItem = (TableItem)next.getActualItem();
				if (nextTableItem.getBounds().y >= getTable().getClientArea().y
						&& nextTableItem.getBounds().y <= getTable().getClientArea().y
								+ getTable().getClientArea().height) {
					if (getSelectedElements().contains(nextTableItem))
						next.setCurveSize(2);
					else
						next.setCurveSize(1);
					next.setCurveY(nextTableItem.getBounds().y + nextTableItem.getBounds().height / 2);
					next.setHeaderHeight(getTable().getHeaderHeight());
					result.add(next);
				}
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab#redraw()
	 */
	public void redraw() {
		getTable().redraw();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ColumnViewer#refresh(java.lang.Object, boolean)
	 */
	@Override
	public void refresh(Object element, boolean updateLabels) {
		super.refresh(element, updateLabels);
		mapDifferences();
		mapTableItems();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab#setReflectiveInput(org.eclipse.emf.ecore.EObject)
	 */
	public void setReflectiveInput(Object input) {
		setInput(input);
		mapDifferences();
		mapTableItems();
	}

	/**
	 * Ensures that the given diff is visible in the table.
	 * 
	 * @param diff
	 *            {@link DiffElement} to make visible.
	 */
	public void showItem(DiffElement diff) {
		final TableItem elementItem = find(diff);
		if (elementItem != null) {
			getTable().setSelection(elementItem);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab#showItems(java.util.List)
	 */
	public void showItems(List<DiffElement> items) {
		if (items.size() > 0)
			showItem(items.get(0));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#associate(Object, Item)
	 */
	@Override
	protected void associate(Object element, Item item) {
		if (element instanceof Object[]) {
			final Object data = item.getData();
			if (data != ((Object[])element)[0]) {
				if (data != null) {
					disassociate(item);
				}
				item.setData(((Object[])element)[0]);
			}
			// Always map the element, even if data == ((Object[])element)[0],
			// since unmapAllElements() can leave the map inconsistent
			// See bug 2741 for details.
			mapElement(((Object[])element)[0], item);
		} else {
			if (element instanceof List) {
				final Object data = item.getData();
				if (data != ((List<?>)element).get(0)) {
					if (data != null) {
						disassociate(item);
					}
					item.setData(((List<?>)element).get(0));
				}
				// Always map the element, even if data == ((List)element).get(0),
				// since unmapAllElements() can leave the map inconsistent
				// See bug 2741 for details.
				mapElement(((List<?>)element).get(0), item);
			} else {
				super.associate(element, item);
			}
		}
	}

	/**
	 * Maps the input's differences if any.
	 */
	private void mapDifferences() {
		differences.clear();
		final Iterator<DiffElement> diffIterator = parent.getDiffAsList().iterator();
		while (diffIterator.hasNext()) {
			final DiffElement diff = diffIterator.next();
			if (diff instanceof ReferenceChange || diff instanceof AttributeChange) {
				differences.add(diff);
			}
		}
	}

	/**
	 * This will map all the TableItems in this TreeViewer that need be taken into account when drawing diff
	 * markers to a corresponding ModelContentMergeTabItem. This will allow us to browse everything once and
	 * for all.
	 */
	private void mapTableItems() {
		dataToItem.clear();
		for (int i = 0; i < differences.size(); i++) {
			final DiffElement diff = differences.get(i);
			// Defines the TableItem corresponding to this difference
			final EObject data;
			if (diff instanceof ReferenceChange)
				data = ((ReferenceChange)diff).getReference();
			else
				data = ((AttributeChange)diff).getAttribute();
			final TableItem item = (TableItem)findItem(data);

			if (item != null) {
				// and now the color which should be used for this kind of differences
				final String color = EMFCompareConstants.PREFERENCES_KEY_CHANGED_COLOR;

				final ModelContentMergeTabItem wrappedItem = new ModelContentMergeTabItem(diff, item, color);
				String key = ""; //$NON-NLS-1$
				if (partSide == EMFCompareConstants.LEFT)
					key = EcoreUtil.getURI(EMFCompareEObjectUtils.getLeftElement(diff)).fragment();
				else if (partSide == EMFCompareConstants.RIGHT)
					key = EcoreUtil.getURI(EMFCompareEObjectUtils.getRightElement(diff)).fragment();
				final String[] fragments = EcoreUtil.getURI(data).fragment().split("/"); //$NON-NLS-1$
				key += '/' + fragments[fragments.length - 1];
				dataToItem.put(key, wrappedItem);
			}
		}
	}

	/**
	 * This implementation of {@link PaintListener} handles the drawing of blocks around modified members in
	 * the properties tab.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	class PropertyPaintListener implements PaintListener {
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
		 */
		public void paintControl(PaintEvent event) {
			if (ModelContentMergeViewer.shouldDrawDiffMarkers()) {
				for (ModelContentMergeTabItem item : getVisibleElements()) {
					drawLine(event, item);
				}
			}
		}

		/**
		 * Handles the drawing itself.
		 * 
		 * @param event
		 *            {@link PaintEvent} that triggered this operation.
		 * @param item
		 *            Item we want connected to the center part.
		 */
		private void drawLine(PaintEvent event, ModelContentMergeTabItem item) {
			final Rectangle tableBounds = getTable().getBounds();

			// properties that present differences should be highlighted
			((TableItem)item.getActualItem()).setBackground(new Color(getControl().getDisplay(),
					ModelContentMergeViewer.getColor(EMFCompareConstants.PREFERENCES_KEY_HIGHLIGHT_COLOR)));

			event.gc.setLineWidth(2);
			event.gc.setForeground(new Color(item.getActualItem().getDisplay(), ModelContentMergeViewer
					.getColor(item.getCurveColor())));
			if (partSide == EMFCompareConstants.LEFT)
				event.gc.drawLine(getTotalColumnsWidth(), item.getCurveY(), tableBounds.width, item
						.getCurveY());
		}
	}

	/**
	 * Label provider used by the table control of this part.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private class PropertyLabelProvider extends AdapterFactoryLabelProvider {
		/**
		 * Instantiates this label provider given its {@link AdapterFactory}.
		 * 
		 * @param theAdapterFactory
		 *            Adapter factory providing this {@link LabelProvider}'s text and images.
		 */
		public PropertyLabelProvider(AdapterFactory theAdapterFactory) {
			super(theAdapterFactory);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see AdapterFactoryLabelProvider#getColumnImage(Object, int)
		 */
		@Override
		public Image getColumnImage(Object object, int columnIndex) {
			Image image = super.getColumnImage(object, columnIndex);
			if (object instanceof List) {
				image = super.getColumnImage(((List<?>)object).get(columnIndex), columnIndex);

			}
			return image;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see AdapterFactoryLabelProvider#getColumnText(Object, int)
		 */
		@Override
		public String getColumnText(Object object, int columnIndex) {
			String text = super.getColumnText(object, columnIndex);
			if (object instanceof List) {
				text = super.getColumnText(((List<?>)object).get(columnIndex), columnIndex);
			}
			return text;
		}
	}
}
