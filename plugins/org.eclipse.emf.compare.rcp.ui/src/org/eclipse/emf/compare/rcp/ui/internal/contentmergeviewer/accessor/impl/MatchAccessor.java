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
package org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.getFirst;

import com.google.common.collect.ImmutableList;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.ICompareAccessor;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.impl.AbstractTypedElementAdapter;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.impl.TypeConstants;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.MergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.MergeViewerItem.Container;
import org.eclipse.emf.compare.rcp.ui.internal.util.MergeViewerUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchAccessor extends AbstractTypedElementAdapter implements ICompareAccessor {

	private final Match fMatch;

	private final MergeViewerSide fSide;

	/**
	 * Creates a new object wrapping the given <code>eObject</code>.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to get the image from.
	 * @param eObject
	 *            the {@link EObject} to wrap.
	 */
	public MatchAccessor(AdapterFactory adapterFactory, Match match, MergeViewerSide side) {
		super(adapterFactory);
		fMatch = match;
		fSide = side;
	}

	/**
	 * @return the fSide
	 */
	protected final MergeViewerSide getSide() {
		return fSide;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.wrapper.accessor.compare.ITypedElement#getName()
	 */
	public String getName() {
		final EObject eObject = MergeViewerUtil.getBestSideEObject(fMatch, fSide);
		if (eObject != null) {
			return getItemDelegator().getText(eObject);
		} else {
			return getItemDelegator().getText(fMatch);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.wrapper.accessor.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		final EObject eObject = MergeViewerUtil.getBestSideEObject(fMatch, fSide);
		final Object image;
		if (eObject != null) {
			image = getItemDelegator().getImage(eObject);
		} else {
			image = getItemDelegator().getImage(fMatch);
		}
		return ExtendedImageRegistry.getInstance().getImage(image);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.ITypedElement#getType()
	 */
	public String getType() {
		return TypeConstants.TYPE__EMATCH;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.ICompareAccessor#getComparison()
	 */
	public Comparison getComparison() {
		return fMatch.getComparison();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.ICompareAccessor#getInitialItem()
	 */
	public IMergeViewerItem getInitialItem() {
		return new MergeViewerItem.Container(fMatch.getComparison(), null, fMatch, fSide,
				getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.ICompareAccessor#getItems()
	 */
	public ImmutableList<? extends IMergeViewerItem> getItems() {
		final ImmutableList.Builder<IMergeViewerItem> ret = ImmutableList.builder();

		EList<Match> matches = getComparison().getMatches();
		for (Match match : matches) {
			ResourceAttachmentChange diff = getFirst(filter(match.getDifferences(),
					ResourceAttachmentChange.class), null);
			Container container = null;
			if (getSide() != MergeViewerSide.ANCESTOR
					|| (getSide() == MergeViewerSide.ANCESTOR && match.getOrigin() != null)) {
				container = new MergeViewerItem.Container(getComparison(), diff, match.getLeft(), match
						.getRight(), match.getOrigin(), getSide(), getRootAdapterFactory());
				ret.add(container);
			}
		}

		return ret.build();
	}
}
