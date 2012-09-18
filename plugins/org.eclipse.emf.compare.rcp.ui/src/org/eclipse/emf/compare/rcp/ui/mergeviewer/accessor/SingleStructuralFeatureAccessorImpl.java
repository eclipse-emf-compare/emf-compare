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
package org.eclipse.emf.compare.rcp.ui.mergeviewer.accessor;

import static com.google.common.collect.Iterables.getFirst;

import com.google.common.collect.ImmutableList;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.InsertionPoint;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.MatchedObject;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.MergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class SingleStructuralFeatureAccessorImpl extends BasicStructuralFeatureAccessorImpl {

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
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.BasicStructuralFeatureAccessorImpl#getItems()
	 */
	public ImmutableList<? extends IMergeViewerItem> getItems() {
		Object thisSideValue = getValue(getSide());

		Object leftValue = getValue(MergeViewerSide.LEFT);
		Object rightValue = getValue(MergeViewerSide.RIGHT);
		Object ancestorValue = getValue(MergeViewerSide.ANCESTOR);

		// there can be only one diff on !many structural feature.
		Diff diff = getFirst(getDifferences(), null);
		final ImmutableList<? extends IMergeViewerItem> ret;
		if (thisSideValue == null) {
			if (getSide() != MergeViewerSide.ANCESTOR) {
				InsertionPoint insertionPoint = new InsertionPoint(diff, leftValue, rightValue, ancestorValue);
				ret = ImmutableList.of(insertionPoint);
			} else {
				ret = ImmutableList.of();
			}
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

}
