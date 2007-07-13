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
package org.eclipse.emf.compare.ui.viewerpart;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.match.statistic.MetamodelFilter;
import org.eclipse.emf.compare.match.statistic.similarity.NameSimilarity;
import org.eclipse.emf.compare.ui.util.EMFAdapterFactoryProvider;
import org.eclipse.emf.compare.util.AdapterUtils;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
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
			double maxSimilarity = 0d;
			Object mostSimilarData = null;
			for (TreeItem item : getVisibleElements()) {
				if (haveDistinctXMIID((EObject)element, (EObject)item.getData()))
					continue;
				final double similarity = contentSimilarity((EObject)element, (EObject)item.getData());
				if (similarity > maxSimilarity) {
					maxSimilarity = similarity;
					mostSimilarData = item.getData();
				}
			}
			if (mostSimilarData != null) {
				res = super.findItem(mostSimilarData);
			} else if (((EObject)element).eContainer() != null) {
				res = find(((EObject)element).eContainer());
			} else
				res = getTree().getItem(0);
		}
		if (res instanceof Tree) {
			return getTree().getItem(0);
		}
		return res;
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
	 * Returns a list of all the {@link Tree tree}'s visible elements.
	 * 
	 * @return List containing all the {@link Tree tree}'s visible elements.
	 */
	public List<TreeItem> getVisibleElements() {
		final List<TreeItem> result = new LinkedList<TreeItem>();
		if (getTree().getItemCount() != 0) {
			for (TreeItem item : getTree().getItems()) {
				getVisibleElements(item, result);
			}
		}
		return result;
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
	 * Returns a {@link List} of the selected elements.
	 * 
	 * @return {@link List} of the selected elements.
	 */
	public List<TreeItem> getSelectedElements() {
		return Arrays.asList(getTree().getSelection());
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
	 * {@inheritDoc}
	 * 
	 * @see StructuredViewer#setSelectionToWidget(List, boolean)
	 */
	@Override
	protected void setSelectionToWidget(List l, boolean reveal) {
		// Will expand the treeItem to one level below the current if needed
		for (Object data : l) {
			final Widget widget = find(data);
			if (widget != null && widget instanceof TreeItem && ((TreeItem)widget).getExpanded() && getChildren(widget).length > 0) {
				expandToLevel(data, 1);
			}
		}
		super.setSelectionToWidget(l, reveal);
	}

	/**
	 * This will compare two objects to see if they have ID and in that case, if these IDs are distinct.
	 * 
	 * @param left
	 *            Left of the two objects to compare.
	 * @param right
	 *            right of the two objects to compare.
	 * @return <code>True</code> if only one of the two objects has an ID or the two are distinct, <code>False</code> otherwise.
	 */
	private boolean haveDistinctXMIID(EObject left, EObject right) {
		boolean result = false;
		String item1ID = null;
		String item2ID = null;
		if (left.eResource() != null && left.eResource() instanceof XMIResource)
			item1ID = ((XMIResource)left.eResource()).getID(left);
		if (right.eResource() != null && right.eResource() instanceof XMIResource)
			item2ID = ((XMIResource)right.eResource()).getID(right);
		if (item1ID != null) {
			result = !item1ID.equals(item2ID);
		} else {
			result = item2ID != null;
		}
		return result;
	}

	private double contentSimilarity(EObject obj1, EObject obj2) {
		double similarity = 0d;
		try {
			similarity = NameSimilarity.nameSimilarityMetric(NameSimilarity.contentValue(obj1, new MetamodelFilter()), NameSimilarity.contentValue(obj2, new MetamodelFilter()));
		} catch (FactoryException e) {
			// fails silently, will return 0d
		}
		return similarity;
	}

	/**
	 * Label provider used by the tree control of this part.
	 */
	private class TreeLabelProvider extends AdapterFactoryLabelProvider {
		/**
		 * Instantiates this label provider given its {@link AdapterFactory}.
		 * 
		 * @param adapterFactory
		 *            Adapter factory providing this {@link LabelProvider}'s text and images.
		 */
		public TreeLabelProvider(AdapterFactory adapterFactory) {
			super(adapterFactory);
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
	}
}
