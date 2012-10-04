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
package org.eclipse.emf.compare.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * This implementation of an {@link ECrossReferenceAdapter} will allow us to only attach ourselves to the Diff
 * elements.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class DiffCrossReferencer extends ECrossReferenceAdapter {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#isIncluded(org.eclipse.emf.ecore.EReference)
	 */
	@Override
	protected boolean isIncluded(EReference eReference) {
		if (super.isIncluded(eReference)) {
			EClass eClass = eReference.getEContainingClass();
			return eClass.getEAllSuperTypes().contains(ComparePackage.Literals.DIFF);
		}
		return false;

	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Overridden to prevent potential NPEs because of UML. In short, some elements from UML do not respect
	 * the basic contract of EMF and we can have a state where <code>eObject.eContainer() != null</code> but
	 * <code>eObject.eContainmentFeature() == null</code>.
	 * </p>
	 * 
	 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#getInverseReferences(org.eclipse.emf.ecore.EObject,
	 *      boolean)
	 */
	// CHECKSTYLE:OFF this is an exact copy/paste of the super class, with only one additional condition
	@SuppressWarnings("unchecked")
	@Override
	public Collection<EStructuralFeature.Setting> getInverseReferences(EObject eObject, boolean resolve) {
		Collection<EStructuralFeature.Setting> result = new ArrayList<EStructuralFeature.Setting>();

		if (resolve) {
			resolveAll(eObject);
		}

		EObject eContainer = eObject.eContainer();
		if (eContainer != null && eObject.eContainingFeature() != null) {
			result.add(((InternalEObject)eContainer).eSetting(eObject.eContainmentFeature()));
		}

		Collection<EStructuralFeature.Setting> nonNavigableInverseReferences = inverseCrossReferencer
				.get(eObject);
		if (nonNavigableInverseReferences != null) {
			result.addAll(nonNavigableInverseReferences);
		}

		for (EReference eReference : eObject.eClass().getEAllReferences()) {
			EReference eOpposite = eReference.getEOpposite();
			if (eOpposite != null && !eReference.isContainer() && eObject.eIsSet(eReference)) {
				if (eReference.isMany()) {
					Object collection = eObject.eGet(eReference);
					for (Iterator<EObject> j = resolve() ? ((Collection<EObject>)collection).iterator()
							: ((InternalEList<EObject>)collection).basicIterator(); j.hasNext();) {
						InternalEObject referencingEObject = (InternalEObject)j.next();
						result.add(referencingEObject.eSetting(eOpposite));
					}
				} else {
					result.add(((InternalEObject)eObject.eGet(eReference, resolve())).eSetting(eOpposite));
				}
			}
		}

		return result;
	}

	// CHECKSTYLE:ON

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#addAdapter(org.eclipse.emf.common.notify.Notifier)
	 */
	@Override
	protected void addAdapter(Notifier notifier) {
		// We only need to install ourselves on Match elements (to listen to new Diffs) and the Diff
		// themselves as they're what we wish to cross reference.
		if (notifier instanceof Match || notifier instanceof Diff) {
			super.addAdapter(notifier);
		}
	}
}
