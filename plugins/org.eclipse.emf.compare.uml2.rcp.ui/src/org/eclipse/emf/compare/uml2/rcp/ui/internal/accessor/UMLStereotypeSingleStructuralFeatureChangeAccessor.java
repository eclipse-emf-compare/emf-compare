/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.SingleStructuralFeatureAccessorImpl;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Case of stereotype single structural feature (attribute/reference) changes.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class UMLStereotypeSingleStructuralFeatureChangeAccessor extends SingleStructuralFeatureAccessorImpl {

	/**
	 * Creates a specialized accessor for the stereotype application differences.
	 * 
	 * @param adapterFactory
	 *            The adapter factory used by the accessor.
	 * @param diff
	 *            The diff for which we need an accessor.
	 * @param side
	 *            The side on which this accessor will be used.
	 */
	public UMLStereotypeSingleStructuralFeatureChangeAccessor(AdapterFactory adapterFactory, UMLDiff diff,
			MergeViewerSide side) {
		super(adapterFactory, diff, side);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.AbstractStructuralFeatureAccessor#getAffectedFeature(org.eclipse.emf.compare.Diff)
	 */
	@Override
	protected EStructuralFeature getAffectedFeature(Diff diff) {
		return (EStructuralFeature)((UMLDiff)diff).getDiscriminant();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor.UMLStereotypeSingleStructuralFeatureChangeAccessor#getEObject(org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer.MergeViewerSide)
	 */
	@Override
	public EObject getEObject(MergeViewerSide side) {
		Diff refined = getInitialDiff().getRefinedBy().get(0);
		final EObject eObject;
		switch (side) {
			case ANCESTOR:
				eObject = refined.getMatch().getOrigin();
				break;
			case LEFT:
				eObject = refined.getMatch().getLeft();
				break;
			case RIGHT:
				eObject = refined.getMatch().getRight();
				break;
			default:
				throw new IllegalStateException();
		}
		return eObject;
	}
}
