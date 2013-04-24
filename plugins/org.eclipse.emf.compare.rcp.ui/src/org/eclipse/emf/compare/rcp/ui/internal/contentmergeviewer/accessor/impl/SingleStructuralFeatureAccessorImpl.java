/*******************************************************************************
 * Copyright (c) 2012 Obeo.
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
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.MergeViewerItem;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class SingleStructuralFeatureAccessorImpl extends AbstractStructuralFeatureAccessor {

	/**
	 * @param diff
	 * @param side
	 */
	public SingleStructuralFeatureAccessorImpl(AdapterFactory adapterFactory, Diff diff, MergeViewerSide side) {
		super(adapterFactory, diff, side);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.AbstractStructuralFeatureAccessor.ui.internal.contentmergeviewer.provider.BasicStructuralFeatureAccessorImpl#getItems()
	 */
	public ImmutableList<? extends IMergeViewerItem> getItems() {
		Object thisSideValue = getValue(getSide());
		if (thisSideValue == null && getSide() == MergeViewerSide.ANCESTOR) {
			// No use retrieving all sides ...
			return ImmutableList.of();
		}

		Object leftValue = getValue(MergeViewerSide.LEFT);
		Object rightValue = getValue(MergeViewerSide.RIGHT);
		Object ancestorValue = getValue(MergeViewerSide.ANCESTOR);

		// there can be only one diff on !many structural feature.
		Diff diff = getInitialDiff();

		final ImmutableList<? extends IMergeViewerItem> ret;
		if (thisSideValue == null) {
			IMergeViewerItem insertionPoint = new MergeViewerItem(getComparison(), diff, leftValue,
					rightValue, ancestorValue, getSide(), getAdapterFactory());
			ret = ImmutableList.of(insertionPoint);
		} else {
			IMergeViewerItem matchedObject = new MergeViewerItem(getComparison(), diff, leftValue,
					rightValue, ancestorValue, getSide(), getAdapterFactory());
			ret = ImmutableList.of(matchedObject);
		}

		return ret;
	}

	private Object getValue(MergeViewerSide side) {
		Object value = null;
		EObject eObject = getEObject(side);
		if (eObject != null) {
			value = ReferenceUtil.safeEGet(eObject, getStructuralFeature());
		}
		return value;
	}
}
