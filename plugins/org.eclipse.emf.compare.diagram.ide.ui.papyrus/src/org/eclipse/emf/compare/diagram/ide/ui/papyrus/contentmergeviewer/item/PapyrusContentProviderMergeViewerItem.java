/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.papyrus.contentmergeviewer.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.contentmergeviewer.facet.PapyrusFacetContentProviderWrapper;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.emf.facet.custom.metamodel.v0_2_0.internal.treeproxy.EObjectTreeElement;
import org.eclipse.papyrus.emf.facet.custom.metamodel.v0_2_0.internal.treeproxy.TreeElement;

/**
 * This class keeps track of the related {@link TreeElement}s and handles the raw ouput of the
 * {@link PapyrusFacetContentProviderWrapper}.
 * 
 * @author Stefan Dirix
 */
public class PapyrusContentProviderMergeViewerItem extends ContentProviderMergeViewerItem.Container {

	/**
	 * The linked {@link TreeElement}.
	 */
	protected EObjectTreeElement treeElement;

	/**
	 * The parent of this item.
	 */
	protected IMergeViewerItem.Container parent;

	/**
	 * Mapping between EObjects and their {@link TreeElement}s.
	 */
	private Map<EObject, EObjectTreeElement> childrenTreeElements;

	/**
	 * Factory responsible for creating new {@link PapyrusContentProviderMergeViewerItem}s.
	 */
	private ContentProviderMergeViewerItemFactory mergeViewerItemFactory;

	/**
	 * Constructor.
	 * 
	 * @param comparison
	 *            the {@link Comparison}.
	 * @param diff
	 *            the {@link Diff}.
	 * @param left
	 *            the left object
	 * @param right
	 *            the right object
	 * @param ancestor
	 *            the ancestor object
	 * @param side
	 *            the {@link MergeViewerSide}
	 * @param adapterFactory
	 *            the {@link adapterFactory}
	 * @param treeElement
	 *            the linked {@link EObjectTreeElement}.
	 */
	// CHECKSTYLE:OFF Many parameters needed because of base class
	public PapyrusContentProviderMergeViewerItem(Comparison comparison, Diff diff, Object left, Object right,
			Object ancestor, MergeViewerSide side, AdapterFactory adapterFactory,
			EObjectTreeElement treeElement, IMergeViewerItem.Container parent) {
		// CHECKSTYLE:ON
		super(comparison, diff, left, right, ancestor, side, adapterFactory);
		this.treeElement = treeElement;
		childrenTreeElements = new HashMap<EObject, EObjectTreeElement>();
		mergeViewerItemFactory = new PapyrusContentProviderMergeViewerItemFactory(this);
		this.parent = parent;
	}

	@Override
	public ContentProviderMergeViewerItemFactory getMergeViewerItemFactory() {
		return mergeViewerItemFactory;
	}

	@Override
	protected List<Object> getChildrenFromContentProvider(Object object) {
		final List<Object> children = super.getChildrenFromContentProvider(object);
		final boolean thisSideChildren = treeElement != null && treeElement.getEObject() == object;
		return unwrap(children, thisSideChildren);
	}

	@Override
	public IMergeViewerItem.Container getParent() {
		if (parent != null) {
			return parent;
		}
		return super.getParent();
	}

	/**
	 * The Papyrus ModelExplorer tree consists of special Papyrus objects. To use them in EMFCompare they have
	 * to be unwrapped.
	 * 
	 * @param children
	 *            The children to unwrap
	 * @param thisSideChildren
	 *            Indicates if these are children displayed as part of this side of the tree
	 * @return The collection of unwrapped objects.
	 */
	private List<Object> unwrap(List<Object> children, boolean thisSideChildren) {
		final List<Object> result = new ArrayList<Object>();
		for (Object object : children) {
			if (EObjectTreeElement.class.isInstance(object)) {
				final EObjectTreeElement childTreeElement = EObjectTreeElement.class.cast(object);
				result.add(childTreeElement.getEObject());
				if (thisSideChildren) {
					childTreeElement.setParent(treeElement);
				}
				childrenTreeElements.put(childTreeElement.getEObject(), childTreeElement);
			} else {
				result.add(object);
			}
		}
		return result;
	}

	/**
	 * Returns the {@link TreeElement} for the value side.
	 * 
	 * @return The {@link TreeElement} for the value side.
	 */
	public TreeElement getTreeElement() {
		return treeElement;
	}

	/**
	 * Factory for creating new {@link PapyrusContentProviderMergeViewerItem}s.
	 */
	private class PapyrusContentProviderMergeViewerItemFactory extends ContentProviderMergeViewerItemFactory {

		/**
		 * the parent.
		 */
		private IMergeViewerItem.Container factoryParent;

		/**
		 * Constructor.
		 * 
		 * @param parent
		 *            the parent.
		 */
		PapyrusContentProviderMergeViewerItemFactory(IMergeViewerItem.Container parent) {
			this.factoryParent = parent;
		}

		@Override
		public IMergeViewerItem.Container createContentProviderMergeViewerItem(Comparison comparison,
				Diff diff, Match match, MergeViewerSide side, AdapterFactory adapterFactory) {
			return createContentProviderMergeViewerItem(comparison, diff, match.getLeft(), match.getRight(),
					match.getOrigin(), side, adapterFactory);
		}

		@Override
		public IMergeViewerItem.Container createContentProviderMergeViewerItem(Comparison comparison,
				Diff diff, Object left, Object right, Object ancestor, MergeViewerSide side,
				AdapterFactory adapterFactory) {
			final EObjectTreeElement childTreeElement;
			switch (side) {
				case RIGHT:
					childTreeElement = childrenTreeElements.get(right);
					break;
				case ANCESTOR:
					childTreeElement = childrenTreeElements.get(ancestor);
					break;
				case LEFT:
				default:
					childTreeElement = childrenTreeElements.get(left);
			}
			return new PapyrusContentProviderMergeViewerItem(comparison, diff, left, right, ancestor, side,
					adapterFactory, childTreeElement, factoryParent);
		}
	}

}
