/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.content.part.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.ui.util.EMFAdapterFactoryProvider;
import org.eclipse.emf.compare.util.AdapterUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

/**
 * Represents the tree view under a {@link ModelContentMergeViewerPart}'s Tree tab.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class ModelContentMergeTreePart extends TreeViewer {
	/**
	 * Creates a tree viewer under the given parent control.
	 * 
	 * @param parent
	 *            The parent {@link Composite} for this tree viewer.
	 */
	public ModelContentMergeTreePart(Composite parent) {
		super(new Tree(parent, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL));

		setUseHashlookup(true);
		setLabelProvider(new TreeLabelProvider(EMFAdapterFactoryProvider.getAdapterFactory()));
	}

	/**
	 * Returns the widget representing the given {@link Object} in the tree.
	 * 
	 * @param element
	 *            {@link Object} to seek in the table.
	 * @return The widget representing the given {@link Object}.
	 * @see org.eclipse.jface.viewers.StructuredViewer#findItem(Object)
	 */
	public Widget find(Object element) {
		Widget res = super.findItem(element);
		if (res == null && element instanceof EObject) {
			if (((EObject)element).eContainer() != null) {
				res = find(((EObject)element).eContainer());
			} else if (getTree().getItemCount() > 0)
				res = getTree().getItem(0);
		}
		if (res instanceof Tree) {
			return getTree().getItem(0);
		}
		return res;
	}

	/**
	 * Returns a {@link List} of the selected elements.
	 * 
	 * @return {@link List} of the selected elements.
	 */
	public List<TreeItem> getSelectedElements() {
		return Arrays.asList(getTree().getSelection());
	}

	/**
	 * Returns a list of all the {@link Tree tree}'s visible elements.
	 * 
	 * @return List containing all the {@link Tree tree}'s visible elements.
	 */
	public List<TreeItem> getVisibleElements() {
		final TreeItem topItem = getTree().getTopItem();
		// We won't go further down that the visible height of the tree, 
		// yet we will take the whole width into account when searching for elements.
		final int treeHeight = getTree().getClientArea().height;
		final int treeWidth = getTree().getBounds().width;
		final int itemHeight = topItem.getBounds().height;
		final int itemWidth = topItem.getBounds().width;
		final List<TreeItem> visibleItems = new ArrayList<TreeItem>(treeHeight / itemHeight + 1);

		visibleItems.add(topItem);
		
		// The loop will start at the element directly following the "top" one
		final int loopStart = topItem.getBounds().y + itemHeight;
		final int loopEnd = treeHeight;
		for (int i = loopStart; i <= loopEnd; i += itemHeight) {
			TreeItem next = null;
			// we'll try and seek on all the line, thus increasing x on each iteration
			for (int xCoord = topItem.getBounds().x; xCoord < treeWidth; xCoord += itemWidth >> 1) {
				next = getTree().getItem(new Point(xCoord, i));
				// We found the item, it is unnecessary to probe any further on the line
				if (next != null)
					break;
			}
			if (next != null) {
				visibleItems.add(next);
			} else {
				// We did not found an item, it is thus useless to probe any further down
				break;
			}
		}

		return visibleItems;
	}

	/**
	 * Populates the given list with all the visible children of the given {@link TreeItem}.
	 * 
	 * @param item
	 *            The {@link TreeItem}.
	 * @param list
	 *            List where we need to add the tree's elements.
	 */
	public void getVisibleElements(TreeItem item, List<TreeItem> list) {
		list.add(item);
		if (item.getExpanded()) {
			for (TreeItem child : item.getItems()) {
				getVisibleElements(child, list);
			}
		}
	}

	/**
	 * Modifies the input of this viewer. Sets a new label provider adapted to the given {@link EObject}.
	 * 
	 * @param eObject
	 *            New input of this viewer.
	 */
	public void setReflectiveInput(EObject eObject) {
		final ComposedAdapterFactory adapterFactory = EMFAdapterFactoryProvider.getAdapterFactory();
		final AdapterFactory best = AdapterUtils.findAdapterFactory(eObject);
		if (best != null)
			adapterFactory.addAdapterFactory(best);
		setLabelProvider(new TreeLabelProvider(adapterFactory));
		setInput(eObject.eResource());
	}

	/**
	 * Ensures that the given item is visible in the tree.
	 * 
	 * @param item
	 *            {@link Object} to make visible.
	 */
	public void showItem(Object item) {
		if (item != null) {
			if (item instanceof TreeItem) {
				super.showItem((TreeItem)item);
			}
			setSelection(new StructuredSelection(item), true);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see StructuredViewer#setSelectionToWidget(List, boolean)
	 */
	@Override
	protected void setSelectionToWidget(List l, boolean reveal) {
		// Will expand the treeItem to one level below the current if needed
		for (Object data : l) {
			final Widget widget = find(data);
			if (widget != null && widget instanceof TreeItem && ((TreeItem)widget).getExpanded()
					&& getChildren(widget).length > 0) {
				expandToLevel(data, 1);
			}
		}
		super.setSelectionToWidget(l, reveal);
	}

	/**
	 * Label provider used by the tree control of this part.
	 * 
	 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
	 */
	private class TreeLabelProvider extends AdapterFactoryLabelProvider {
		/**
		 * Instantiates this label provider given its {@link AdapterFactory}.
		 * 
		 * @param theAdapterFactory
		 *            Adapter factory providing this {@link LabelProvider}'s text and images.
		 */
		public TreeLabelProvider(AdapterFactory theAdapterFactory) {
			super(theAdapterFactory);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see AdapterFactoryLabelProvider#getImage(Object)
		 */
		@Override
		public Image getImage(Object object) {
			Image image = super.getImage(object);
			if (image == null && object instanceof EObject) {
				EMFAdapterFactoryProvider.addAdapterFactoryFor((EObject)object);
				image = super.getImage(object);
			}
			return image;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see AdapterFactoryLabelProvider#getText(Object)
		 */
		@Override
		public String getText(Object object) {
			String text = super.getText(object);
			if (text == null && object instanceof EObject) {
				EMFAdapterFactoryProvider.addAdapterFactoryFor((EObject)object);
				text = super.getText(object);
			}
			return text;
		}
	}
}
