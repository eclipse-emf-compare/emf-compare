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
package org.eclipse.emf.compare.uml2.rcp.ui.internal.mergeviewer.item.impl;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.getFirst;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.MergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.ResourceAttachmentChangeMergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.internal.util.MergeViewerUtil;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.uml2.internal.DanglingStereotypeApplication;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * A specific {@link MergeViewerItem} for {@link DanglingStereotypeApplication}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 2.4
 */
@SuppressWarnings("restriction")
public class DanglingStereotypeApplicationMergeViewerItem extends ResourceAttachmentChangeMergeViewerItem {

	/**
	 * {@inheritDoc}.
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.
	 *      ResourceAttachmentChangeMergeViewerItem .ResourceAttachmentChangeMergeViewerItem(Comparison
	 *      comparison, Diff diff, Object left, Object right, Object ancestor, MergeViewerSide side,
	 *      AdapterFactory adapterFactory)
	 */
	public DanglingStereotypeApplicationMergeViewerItem(Comparison comparison, Diff diff, Resource left,
			Resource right, Resource ancestor, IMergeViewer.MergeViewerSide side,
			AdapterFactory adapterFactory) {
		super(comparison, diff, left, right, ancestor, side, adapterFactory);
	}

	/**
	 * {@inheritDoc}.
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.
	 *      ResourceAttachmentChangeMergeViewerItem .ResourceAttachmentChangeMergeViewerItem(Comparison, Diff,
	 *      Match, MergeViewerSide, AdapterFactory)
	 */
	public DanglingStereotypeApplicationMergeViewerItem(Comparison comparison, Diff diff, Match match,
			IMergeViewer.MergeViewerSide side, AdapterFactory adapterFactory) {
		super(comparison, diff, match, side, adapterFactory);
	}

	/**
	 * Creates an IMergeViewerItem from an EObject.
	 * 
	 * @param eObject
	 *            the given eObject.
	 * @return an IMergeViewerItem.
	 */
	@Override
	protected IMergeViewerItem createMergeViewerItemFrom(EObject eObject) {

		Match match = getComparison().getMatch(eObject);

		if (match != null) {
			ResourceAttachmentChange rac = getFirst(
					filter(match.getDifferences(), DanglingStereotypeApplication.class), null);
			if (rac != null) {
				Object left = match.getLeft();
				Object right = match.getRight();
				Object ancestor = match.getOrigin();
				// Manage case where the resource attachment change is between an existing resource and an
				// unknown resource
				if (MergeViewerUtil.getResource(getComparison(), MergeViewerSide.LEFT, rac) == null) {
					left = null;
				}
				if (MergeViewerUtil.getResource(getComparison(), MergeViewerSide.RIGHT, rac) == null) {
					right = null;
				}
				if (MergeViewerUtil.getResource(getComparison(), MergeViewerSide.ANCESTOR, rac) == null) {
					ancestor = null;
				}

				return new MergeViewerItem.Container(getComparison(), rac, left, right, ancestor, getSide(),
						getAdapterFactory());
			}
		}
		return null;

	}

}
