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
package org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl;

import com.google.common.collect.ImmutableList;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.ResourceLocationChange;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.ICompareAccessor;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.IResourceContentsAccessor;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.impl.AbstractTypedElementAdapter;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.TypeConstants;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.MergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * A specific {@link IResourceContentsAccessor} for {@link ResourceLocationChange} objects.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 4.0
 * @deprecated {@link org.eclipse.emf.compare.ResourceLocationChange}s have been replaced by
 *             {@link ResourceAttachmentChange}s of kind Move.
 */
@Deprecated
public class ResourceLocationChangeAccessorImpl extends AbstractTypedElementAdapter implements ICompareAccessor {

	/** The difference performed. */
	private final ResourceLocationChange fDiff;

	/** The side on which the difference is located. */
	private final MergeViewerSide fSide;

	/** The match resource associated to the performed difference. */
	private final MatchResource fOwnerMatchResource;

	private final Comparison fComparison;

	public ResourceLocationChangeAccessorImpl(AdapterFactory adapterFactory, ResourceLocationChange diff,
			MergeViewerSide side) {
		super(adapterFactory);
		fDiff = diff;
		fSide = side;
		fOwnerMatchResource = (MatchResource)diff.eContainer();
		fComparison = fOwnerMatchResource.getComparison();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.ICompareAccessor#getComparison()
	 */
	public Comparison getComparison() {
		return fComparison;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.ICompareAccessor#getInitialItem()
	 */
	public IMergeViewerItem getInitialItem() {
		return new MergeViewerItem(fComparison, fDiff, fOwnerMatchResource.getLeft(), fOwnerMatchResource
				.getRight(), fOwnerMatchResource.getOrigin(), fSide, getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.ICompareAccessor#getItems()
	 */
	public ImmutableList<? extends IMergeViewerItem> getItems() {
		return ImmutableList.of(getInitialItem());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement#getName()
	 */
	public String getName() {
		return ResourceLocationChangeAccessorImpl.class.getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement#getImage()
	 */
	public Image getImage() {

		Object image = getItemDelegator().getImage(getResource());
		return ExtendedImageRegistry.getInstance().getImage(image);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement#getType()
	 */
	public String getType() {
		return TypeConstants.TYPE_ERESOURCE_DIFF;
	}

	/**
	 * Get the first resource found associated with the match resource. Search order: left, then right, then
	 * origin.
	 * 
	 * @return the first resource found associated with the match resource.
	 */
	private Resource getResource() {
		Resource resource = fOwnerMatchResource.getLeft();
		if (resource == null) {
			resource = fOwnerMatchResource.getRight();
		}
		if (resource == null) {
			resource = fOwnerMatchResource.getOrigin();
		}
		return resource;
	}
}
