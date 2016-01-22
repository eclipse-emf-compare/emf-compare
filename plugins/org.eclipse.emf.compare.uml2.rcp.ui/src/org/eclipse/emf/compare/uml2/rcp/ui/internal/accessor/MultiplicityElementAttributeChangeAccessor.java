/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexandra Buzila - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.SingleStructuralFeatureAccessorImpl;
import org.eclipse.emf.compare.rcp.ui.internal.util.MergeViewerUtil;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.uml2.internal.MultiplicityElementChange;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Change accessor for {@link MultiplicityElementChange MultiplicityElementChanges} whose prime refining is an
 * {@link AttributeChange}.
 * 
 * @author Alexandra Buzila
 */
@SuppressWarnings("restriction")
public class MultiplicityElementAttributeChangeAccessor extends SingleStructuralFeatureAccessorImpl {

	/**
	 * A specific {@link SingleStructuralFeatureAccessorImpl} for {@link MultiplicityElementChange
	 * MultiplicityElementChanges}.
	 * 
	 * @param adapterFactory
	 *            the adapter factory used to create the accessor
	 * @param diff
	 *            the diff associated with this accessor
	 * @param side
	 *            the side of the accessor
	 */
	public MultiplicityElementAttributeChangeAccessor(AdapterFactory adapterFactory, Diff diff,
			MergeViewerSide side) {
		super(adapterFactory, diff, side);
	}

	@Override
	protected EStructuralFeature getAffectedFeature(Diff diff) {
		Diff primeRefining = diff.getPrimeRefining();
		return MergeViewerUtil.getAffectedFeature(primeRefining);
	}

	@Override
	public EObject getEObject(MergeViewerSide side) {
		Match match = getInitialDiff().getMatch();
		EObject multiplicityElement = MergeViewerUtil.getEObject(match, side);
		if (multiplicityElement == null) {
			return null;
		}
		MultiplicityElementChange change = (MultiplicityElementChange)getInitialDiff();
		// the lowerValue/upperValue feature affected by the diff
		EReference multiplicityElementFeature = change.getDiscriminant().eContainmentFeature();
		return (EObject)ReferenceUtil.safeEGet(multiplicityElement, multiplicityElementFeature);
	}

}
