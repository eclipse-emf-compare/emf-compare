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

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.papyrus.emf.facet.custom.metamodel.v0_2_0.internal.treeproxy.EObjectTreeElement;
import org.eclipse.papyrus.emf.facet.custom.metamodel.v0_2_0.internal.treeproxy.TreeproxyFactory;

/**
 * Converts MergeViewerItems to PapyrusMergeViewerItems.
 * 
 * @author Stefan Dirix
 */
public class MergeViewerItemConverter {

	/**
	 * The {@link AdapterFactory} which is used when creating new {@link IMergeViewerItem}s.
	 */
	private AdapterFactory adapterFactory;

	/**
	 * The {@link Comparison} which is used when creating new {@link IMergeViewerItem}s.
	 */
	private Comparison comparison;

	/**
	 * Constructor.
	 * 
	 * @param factory
	 *            the {@link AdapterFactory} which is used when creating new {@link IMergeViewerItem}s.
	 * @param comparison
	 *            the {@link Comparison} which is used when creating new {@link IMergeViewerItem}s.
	 */
	public MergeViewerItemConverter(AdapterFactory factory, Comparison comparison) {
		this.adapterFactory = factory;
		this.comparison = comparison;
	}

	/**
	 * Checks whether the given {@code item} needs conversion and executes the conversion if needed.
	 * 
	 * @param item
	 *            the {@link IMergeViewerItem} to convert.
	 * @return The converted {@link IMergeViewerItem} if the conversion was needed and allowed, the original
	 *         {@link IMergeViewerItem} otherwise.
	 */
	public IMergeViewerItem convert(IMergeViewerItem item) {
		if (isConversionNeeded(item) && isConversionAllowed(item)) {
			return doConvert(item);
		}
		return item;
	}

	/**
	 * Determines whether the conversion is allowed for this {@code item}.
	 * 
	 * @param item
	 *            the {@link IMergeViewerItem}.
	 * @return {@code true} if conversion is allowed, {@code false} otherwise.
	 */
	protected boolean isConversionAllowed(IMergeViewerItem item) {
		// Do not convert items regarding resources
		return !isResourceItem(item);
	}

	/**
	 * Checks whether any side of the {@link IMergeViewerItem} is of kind '{@link Resource}'.
	 * 
	 * @param item
	 *            the {@link IMergeViewerItem} to check.
	 * @return {@code true} if any side is of kind '{@link Resource}', {@code false} otherwise.
	 */
	private boolean isResourceItem(IMergeViewerItem item) {
		return Resource.class.isInstance(item.getLeft()) || Resource.class.isInstance(item.getRight())
				|| Resource.class.isInstance(item.getAncestor());
	}

	/**
	 * Determines whether a conversion is needed for this {@code item}.
	 * 
	 * @param item
	 *            the {@link IMergeViewerItem}.
	 * @return {@code true} if a conversion is needed, {@code false} otherwise.
	 */
	protected boolean isConversionNeeded(IMergeViewerItem item) {
		return !ContentProviderMergeViewerItem.class.isInstance(item);
	}

	/**
	 * Converts the given {@code item}.
	 * 
	 * @param item
	 *            The {@link IMergeViewerItem}.
	 * @return The converted {@link IMergeViewerItem}.
	 */
	protected IMergeViewerItem doConvert(IMergeViewerItem item) {
		final Object value = item.getSideValue(item.getSide());
		if (EObject.class.isInstance(value)) {
			final EObjectTreeElement treeElement = TreeproxyFactory.eINSTANCE.createEObjectTreeElement();
			treeElement.setEObject(EObject.class.cast(value));
			return new PapyrusContentProviderMergeViewerItem(comparison, item.getDiff(), item.getLeft(),
					item.getRight(), item.getAncestor(), item.getSide(), adapterFactory, treeElement, null);
		}

		return new ContentProviderMergeViewerItem.Container(comparison, item.getDiff(), item.getLeft(),
				item.getRight(), item.getAncestor(), item.getSide(), adapterFactory);
	}
}
