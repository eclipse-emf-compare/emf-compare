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

import static com.google.common.collect.Iterables.getFirst;

import com.google.common.collect.ImmutableList;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.impl.TypeConstants;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.InsertionPoint;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.MatchedObject;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.provider.EcoreEditPlugin;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class SingleStructuralFeatureAccessorImpl extends AbstractStructuralFeatureAccessor {

	/**
	 * @param diff
	 * @param side
	 */
	public SingleStructuralFeatureAccessorImpl(Diff diff, MergeViewerSide side) {
		super(diff, side);
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
		Diff diff = getFirst(getDifferences(), null);
		final ImmutableList<? extends IMergeViewerItem> ret;
		if (thisSideValue == null) {
			InsertionPoint insertionPoint = new InsertionPoint(diff, leftValue, rightValue, ancestorValue);
			ret = ImmutableList.of(insertionPoint);
		} else {
			MatchedObject matchedObject = new MatchedObject(diff, leftValue, rightValue, ancestorValue);
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.ITypedElement#getName()
	 */
	public String getName() {
		return SingleStructuralFeatureAccessorImpl.class.getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.ITypedElement#getImage()
	 */
	public Image getImage() {
		if (getStructuralFeature() instanceof EAttribute) {
			return ExtendedImageRegistry.getInstance().getImage(
					EcoreEditPlugin.getPlugin().getImage("full/obj16/EAttribute")); //$NON-NLS-1$
		} else {
			return ExtendedImageRegistry.getInstance().getImage(
					EcoreEditPlugin.getPlugin().getImage("full/obj16/EReference")); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.ITypedElement#getType()
	 */
	public String getType() {
		return TypeConstants.TYPE__EDIFF;
	}

}
