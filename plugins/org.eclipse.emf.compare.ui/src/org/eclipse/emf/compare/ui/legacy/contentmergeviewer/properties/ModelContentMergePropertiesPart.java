/*******************************************************************************
 * Copyright (c) 2006, Intalio Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Intalio Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.legacy.contentmergeviewer.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.compare.diff.AttributeChange;
import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

/**
 * 
 * @author <a href="http://www.intalio.com">&copy; Intalio, Inc.</a>
 * 
 */
public class ModelContentMergePropertiesPart extends TableViewer {
	private class PropertiesLabelProvider extends AdapterFactoryLabelProvider {

		public PropertiesLabelProvider(final AdapterFactory adapterFactory) {
			super(adapterFactory);
		}

		public String getColumnText(final Object object, final int columnIndex) {
			if (object instanceof List) {
				return super.getColumnText(((List) object).get(columnIndex),
						columnIndex);

			}
			return super.getColumnText(object, columnIndex);
		}

		public Image getColumnImage(final Object object, final int columnIndex) {
			if (object instanceof List) {
				return super.getColumnImage(((List) object).get(columnIndex),
						columnIndex);

			}
			return super.getColumnImage(object, columnIndex);
		}
	}

	private int side;

	/**
	 * @param parent
	 * @param style
	 */
	public ModelContentMergePropertiesPart(final Composite parent, final int style, final int side) {
		super(parent, style);
		final List<AdapterFactoryImpl> factories = new ArrayList<AdapterFactoryImpl>();
		/*
		 * factories.add(new UMLItemProviderAdapterFactory()); factories.add(new
		 * EcoreItemProviderAdapterFactory()); factories.add(new
		 * UMLReflectiveItemProviderAdapterFactory()); factories.add(new
		 * ResourceItemProviderAdapterFactory()); factories.add(new
		 * ReflectiveItemProviderAdapterFactory());
		 */
		factories.add(new ResourceItemProviderAdapterFactory());
		factories.add(new EcoreItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
				factories);
		setLabelProvider(new PropertiesLabelProvider(adapterFactory));
		this.side = side;
		final GC gc = new GC(parent);
		gc.setFont(parent.getFont());
		final FontMetrics fontMetrics = gc.getFontMetrics();
		gc.dispose();

		setUseHashlookup(true);
		getTable().setLinesVisible(true);
		getTable().setHeaderVisible(true);

		final TableColumn nameColumn = new TableColumn(getTable(), SWT.LEFT);
		nameColumn.setText("Attribute Name");
		nameColumn
				.setWidth(Dialog.convertWidthInCharsToPixels(fontMetrics, 30));
		final TableColumn weightsColumn = new TableColumn(getTable(), SWT.RIGHT);
		weightsColumn.setText("Value");
		weightsColumn.setWidth(Dialog.convertWidthInCharsToPixels(fontMetrics,
				8));
	}

	public int getSide() {
		return this.side;
	}

	/**
	 * @param diff
	 * @return
	 */
	public TableItem find(final DiffElement diff) {
		if (diff instanceof AttributeChange) {
			return (TableItem) findItem(((AttributeChange) diff).getAttribute());
		}
		return null;

	}

	public TableItem find(final EAttribute attr) {

		return (TableItem) findItem(attr);

	}

	/**
	 * @param diff
	 */
	public void showItem(final DiffElement diff) {
		if (diff instanceof AttributeChange) {
			final Widget result = findItem(((AttributeChange) diff).getAttribute());
			if ((result != null) && (result instanceof TableItem)) {
				getTable().setSelection((TableItem) result);
			}
		}
	}

	/**
	 * Associates the given element with the given widget. Sets the given item's
	 * data to be the element, and maps the element to the item in the element
	 * map (if enabled).
	 * 
	 * @param element
	 *            the element
	 * @param item
	 *            the widget
	 */
	@Override
	protected void associate(final Object element, final Item item) {
		if (element instanceof Object[]) {
			final Object data = item.getData();
			if (data != ((Object[]) element)[0]) {
				if (data != null) {
					disassociate(item);
				}
				item.setData(((Object[]) element)[0]);
			}
			// Always map the element, even if data == element,
			// since unmapAllElements() can leave the map inconsistent
			// See bug 2741 for details.
			mapElement(((Object[]) element)[0], item);
		} else {
			if (element instanceof List) {
				final Object data = item.getData();
				if (data != ((List) element).get(0)) {
					if (data != null) {
						disassociate(item);
					}
					item.setData(((List) element).get(0));
				}
				// Always map the element, even if data == element,
				// since unmapAllElements() can leave the map inconsistent
				// See bug 2741 for details.
				mapElement(((List) element).get(0), item);
			} else {
				super.associate(element, item);
			}
		}

	}

	/**
	 * @param att
	 */
	public void showItem(final EAttribute att) {
		final Widget result = findItem(att);
		if ((result != null) && (result instanceof TableItem)) {
			getTable().setSelection((TableItem) result);
		}
	}
}
