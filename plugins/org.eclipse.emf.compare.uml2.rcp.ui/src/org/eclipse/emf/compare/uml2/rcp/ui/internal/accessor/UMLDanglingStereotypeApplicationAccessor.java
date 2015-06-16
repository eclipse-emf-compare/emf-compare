/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor;

import com.google.common.collect.ImmutableList;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.ResourceContentsAccessorImpl;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.uml2.rcp.ui.internal.mergeviewer.item.impl.DanglingStereotypeApplicationMergeViewerItem;

/**
 * A specific {@link ResourceContentsAccessorImpl} for
 * {@link org.eclipse.emf.compare.uml2.internal.DanglingStereotypeApplication} objects.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 2.4
 */
@SuppressWarnings("restriction")
public class UMLDanglingStereotypeApplicationAccessor extends ResourceContentsAccessorImpl {

	/**
	 * Default constructor.
	 * 
	 * @param adapterFactory
	 *            the adapter factory used to create the accessor.
	 * @param diff
	 *            The difference performed.
	 * @param side
	 *            The side on which the difference is located.
	 */
	public UMLDanglingStereotypeApplicationAccessor(AdapterFactory adapterFactory, Diff diff,
			MergeViewerSide side) {
		super(adapterFactory, diff, side);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement#getName()
	 */
	@Override
	public String getName() {
		return UMLDanglingStereotypeApplicationAccessor.class.getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.IResourceContentsAccessor#getItems()
	 */
	@Override
	public ImmutableList<? extends IMergeViewerItem> getItems() {
		final ImmutableList<? extends IMergeViewerItem> ret = ImmutableList
				.of(new DanglingStereotypeApplicationMergeViewerItem(getComparison(), null,
						getResource(MergeViewerSide.LEFT), getResource(MergeViewerSide.RIGHT),
						getResource(MergeViewerSide.ANCESTOR), getSide(), getRootAdapterFactory()));
		return ret;
	}

}
