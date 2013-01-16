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
package org.eclipse.emf.compare.match.eobject;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;

/**
 * Default implementation which is parameterized to set weights based on features, to ignore features and
 * consider "name" features as more important.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class ReflectiveWeightProvider implements WeightProvider {

	/**
	 * The list of specific weight to apply on specific Features.
	 */
	private Map<EStructuralFeature, Integer> weights;

	/**
	 * The list of features to ignore during the distance computation.
	 */
	private Set<EStructuralFeature> toBeIgnored;

	/**
	 * Weight coefficient of a change on a reference.
	 */
	private int referenceChangeCoef = 2;

	/**
	 * Weight coefficient of a change on an attribute.
	 */
	private int attributeChangeCoef = 5;

	/**
	 * Create the weight provider.
	 */
	public ReflectiveWeightProvider() {
		weights = Maps.newHashMap();
		toBeIgnored = Sets.newLinkedHashSet();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getWeight(EStructuralFeature feature) {

		if (irrelevant(feature) || toBeIgnored.contains(feature)) {
			return 0;
		}

		Integer found = weights.get(feature);
		if (found == null) {
			found = Integer.valueOf(1);
			/*
			 * This is worst than empirical but it works in many cases, if your feature is a "name" its likely
			 * that it's important for matching the element. At some point I'll have to come up with something
			 * which is more extensible..
			 */
			if ("name".equals(feature.getName()) || "id".equals(feature.getName())) { //$NON-NLS-1$
				found = Integer.valueOf(4);
			}
			if (feature instanceof EReference && ((EReference)feature).isContainment()) {
				found = Integer.valueOf(2);
			}
		}
		if (feature instanceof EReference) {
			found = referenceChangeCoef * found.intValue();
		} else {
			found = attributeChangeCoef * found.intValue();
		}
		return found;
	}

	/**
	 * return true i the feature is irrelevant for the comparison.
	 * 
	 * @param feat
	 *            any feature.
	 * @return true i the feature is irrelevant for the comparison.
	 */
	protected boolean irrelevant(EStructuralFeature feat) {
		boolean irrelevantFeature = feat.isDerived() || feat.isTransient();
		if (!irrelevantFeature && feat instanceof EReference) {
			EReference ref = (EReference)feat;
			irrelevantFeature = ref.isContainment() || ref.isContainer();
		}
		return irrelevantFeature;
	}

	/**
	 * {@inheritDoc}
	 */
	// CHECKSTYLE:OFF
	public int getParentWeight(EObject a) {
		/*
		 * these should belong to an Ecore specific class
		 */
		if (a instanceof EStructuralFeature) {
			return 34;
		} else if (a instanceof EAnnotation) {
			return 34;
		} else if (a instanceof EOperation) {
			return 20;
		} else if (a instanceof EParameter) {
			return 30;
		} else if (a instanceof EStringToStringMapEntryImpl) {
			return 30;
		}
		return 1;
	}

	// CHECKSTYLE:ON

	/**
	 * {@inheritDoc}
	 */
	public int getContainingFeatureWeight(EObject a) {
		/*
		 * these should belong to an ECore specific class
		 */
		// CHECKSTYLE:OFF
		if (a instanceof EStructuralFeature || a instanceof EAnnotation || a instanceof EOperation) {
			return 20;
		}
		// CHECKSTYLE:ON
		return 1;
	}

}
