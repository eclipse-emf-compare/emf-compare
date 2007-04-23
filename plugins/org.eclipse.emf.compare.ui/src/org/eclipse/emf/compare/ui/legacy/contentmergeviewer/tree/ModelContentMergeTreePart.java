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
package org.eclipse.emf.compare.ui.legacy.contentmergeviewer.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.compare.diff.AddModelElement;
import org.eclipse.emf.compare.diff.ModelElementChange;
import org.eclipse.emf.compare.diff.RemoveModelElement;
import org.eclipse.emf.compare.ui.legacy.AdapterUtils;
import org.eclipse.emf.compare.ui.legacy.DiffConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

/**
 * 
 * @author <a href="http://www.intalio.com">&copy; Intalio, Inc.</a>
 * 
 */
public class ModelContentMergeTreePart extends TreeViewer {
	private int total;

	private final int side;

	public ModelContentMergeTreePart(final Composite parent, final int side) {
		super(new Tree(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL));
		setUseHashlookup(true);
		this.side = side;
		final List<AdapterFactoryImpl> factories = new ArrayList<AdapterFactoryImpl>();
//		factories.add(new UMLResourceItemProviderAdapterFactory());
//		factories.add(new UMLItemProviderAdapterFactory());
//		factories.add(new EcoreItemProviderAdapterFactory());
//		factories.add(new UMLReflectiveItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());		
		// factories.add(new UMLResourceItemProviderAdapterFactory());
		// factories.add(new UMLItemProviderAdapterFactory());
		// factories.add(new UMLReflectiveItemProviderAdapterFactory());
		// factories.add(new EcoreItemProviderAdapterFactory());
		// //teststart
		// factories.add(new ReflectiveItemProviderAdapterFactory());
		// factories.add(new ResourceItemProviderAdapterFactory());
		// //testend
		adapterFactory = new ComposedAdapterFactory(
				factories);
		setLabelProvider(new DeltaLabelProvider(adapterFactory));
	}
	
	ComposedAdapterFactory adapterFactory;

	private class DeltaLabelProvider extends AdapterFactoryLabelProvider {
		/**
		 * @param adapterFactory
		 */
		public DeltaLabelProvider(final AdapterFactory adapterFactory) {
			super(adapterFactory);
		}

		/**
		 * The <code>LabelProvider</code> implementation of this
		 * <code>ILabelProvider</code> method returns the element's
		 * <code>toString</code> string. Subclasses may override.
		 */
		public String getText(final Object element) {
			if (element instanceof ModelElementChange) {
				switch (ModelContentMergeTreePart.this.side) {
				case DiffConstants.LEFT:
					return super.getText(((RemoveModelElement) element)
							.getLeftElement());
				case DiffConstants.RIGHT:
					return super.getText(((AddModelElement) element)
							.getRightElement());
					// TODOCBR handle 3way diff
					// case DiffConstants.ANCESTOR :
					// return super.getText(((Delta)
					// element).getAncestorElt());//$NON-NLS-1$
				}

			}
			return super.getText(element);
		}

		public Image getImage(final Object element) {
			if (element instanceof ModelElementChange) {
				switch (ModelContentMergeTreePart.this.side) {
				case DiffConstants.LEFT:
					return super.getImage(((RemoveModelElement) element)
							.getLeftElement());
				case DiffConstants.RIGHT:
					return super.getImage(((AddModelElement) element)
							.getRightElement());
					// TODOCBR handle 3way diff
					// case DiffConstants.ANCESTOR :
					// return super.getImage(((Delta)
					// object).getAncestorElt());//$NON-NLS-1$
				}

			}
			return super.getImage(element);
		}
	}

	/*
	 * Scroll the tree to the given item.
	 */
	public void vscroll(final TreeItem item) {

		final Object[] expandedElements = getVisibleExpandedElements();
		for (final Object elt : expandedElements) {
			if (elt.equals(item)) {
				return;
			}
		}

		getTree().setTopItem(item);
	}

	public int getVisibleElementsCount() {
		this.total = 0;
		countVisibleChildren(getTree().getItems());
		return this.total;
	}

	private void countVisibleChildren(final TreeItem[] items) {
		for (final TreeItem item : items) {
			this.total++;
			if (item.getExpanded()) {
				countVisibleChildren(item.getItems());
			}
		}
	}

	public void showItem(final Object item) {
		if (item == null) {
			return;
		}
		if (item instanceof TreeItem) {
			super.showItem((Item) item);
		}
		setSelection(new StructuredSelection(item));
		if (item instanceof ModelElementChange) {
			final Object result = findItem(item);

			TreeItem resultItem = null;
			if (result instanceof Tree) // root
			{
				resultItem = getTree().getItem(0);
			} else {
				if (result instanceof TreeItem) {
					resultItem = (TreeItem) result;
				} else {
					// shouldn't be there
					resultItem = null;
					assert (false);
				}
			}
			if (resultItem == null) // we expand the tree to recuperate the
									// element.
			{
				expandAll(); // TODO change to recursive expand

				resultItem = (TreeItem) findItem(item);
				if (resultItem == null) {
					// present because the element or the
										// compared element is null
					return;
				}
			}
			assert (resultItem != null);
			for (final TreeItem selectedItem : getTree().getSelection()) {
				if (selectedItem.equals(resultItem)) {
					return;
				}
			}
			collapseToLevel(resultItem, 0);

			getTree().setSelection(resultItem);
			getTree().showSelection();
		}
	}

	public Widget find(final Object element) {
		final Widget res = super.findItem(element);
		if (res instanceof Tree) {
			return getTree().getItem(0);
		}
		return res;
	}

	public List<Object> getVisibleElements() {
		final List<Object> result = new LinkedList<Object>();
		if (getTree().getItemCount() == 0) {
			return result;
		}
		internalgetVisibleElements(getTree().getItem(0), result);
		return result;
	}

	private void internalgetVisibleElements(final TreeItem item, final List<Object> result) {
		result.add(item);
		if (item.getExpanded()) {
			for (final TreeItem child : item.getItems()) {
				internalgetVisibleElements(child, result);
			}
		}

	}

	private int limitedTotal;

	private boolean stop = false;

	public int getLimitedSelection() {
		this.limitedTotal = 0;
		if (getTree().getSelectionCount() == 0) {
			return 0;
		}
		this.stop = false;
		countLimitedSelection(getTree().getItems());

		return this.limitedTotal;
	}

	private void countLimitedSelection(final TreeItem[] items) {
		if (this.stop) {
			return;
		}
		for (final TreeItem item : items) {
			if (getTree().getSelection()[0].equals(item)) {
				this.stop = true;
				return;
			} else {
				this.limitedTotal++;
				if (item.getExpanded()) {
					countLimitedSelection(item.getItems());
				}
			}
		}
	}

	/**
	 * @param leftItem
	 * @return
	 */
	public TreeItem getVisibleParentElement(TreeItem item) {
		final List vItems = getVisibleElements();
		while ((!vItems.contains(item))) {
			if (item.getParentItem() == null) {
				return null;
			}
			item = item.getParentItem();
		}
		return item;
	}

	protected void createTreeItem(final Widget parent, final Object element, final int index) {
		if (element instanceof ModelElementChange) {
			boolean elementIsNull = false;
			switch (this.side) {
			case DiffConstants.LEFT:
				elementIsNull = ((RemoveModelElement) element).getLeftElement() == null;
				break;
			case DiffConstants.RIGHT:
				elementIsNull = ((AddModelElement) element).getRightElement() == null;
				break;
			case DiffConstants.ANCESTOR:
				// TODOCBR handle 3way diff
				// elementIsNull = ((Delta) element).getAncestorElt() == null;
				break;
			default:
				throw new IllegalStateException("Invalid side value");
			}
			if (elementIsNull) {
				return;
			}

		}
		super.createTreeItem(parent, element, index);
	}

	public void setReflectiveInput(EObject eobj) {
		final List<AdapterFactory> factories = new ArrayList<AdapterFactory>();
		AdapterFactory best = new AdapterUtils().findAdapterFactory(eobj);
		if (best != null)
			factories.add(best);
		factories.add(new ReflectiveItemProviderAdapterFactory());	
		adapterFactory = new ComposedAdapterFactory(
				factories);
		setLabelProvider(new DeltaLabelProvider(adapterFactory));		
		setInput(eobj.eResource());
	}
}
