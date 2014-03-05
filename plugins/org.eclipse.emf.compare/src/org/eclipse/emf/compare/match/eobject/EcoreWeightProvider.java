/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.eobject;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;

/**
 * The implementation of {@link org.eclipse.emf.compare.match.eobject.WeightProvider} applicable to all ecore
 * objects.
 * 
 * @since 3.1.0
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class EcoreWeightProvider extends DefaultWeightProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getWeight(EStructuralFeature feature) {

		if (irrelevant(feature)) {
			return 0;
		}

		Integer found = weights.get(feature);
		if (found == null) {
			found = Integer.valueOf(SMALL);
			if (feature == EcorePackage.Literals.ENAMED_ELEMENT__NAME) {
				found = Integer.valueOf(SIGNIFICANT);
			}
			if (feature instanceof EReference) {
				found = Integer.valueOf(referenceChangeCoef * found.intValue());
			} else {
				found = Integer.valueOf(attributeChangeCoef * found.intValue());
			}
			weights.put(feature, found);
		}
		return found.intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getParentWeight(EObject a) {
		final int parentWeight;
		if (a instanceof EStructuralFeature) {
			parentWeight = MASSIVE;
		} else if (a instanceof EAnnotation) {
			parentWeight = UNLIKELY_TO_MATCH;
		} else if (a instanceof EOperation) {
			parentWeight = MAJOR;
		} else if (a instanceof EParameter) {
			parentWeight = UNLIKELY_TO_MATCH;
		} else if (a instanceof EStringToStringMapEntryImpl) {
			parentWeight = UNLIKELY_TO_MATCH;
		} else {
			parentWeight = SIGNIFICANT;
		}
		return parentWeight;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getContainingFeatureWeight(EObject a) {
		if (a instanceof EStructuralFeature || a instanceof EAnnotation || a instanceof EOperation) {
			return MAJOR;
		}
		return SIGNIFICANT;
	}

}
