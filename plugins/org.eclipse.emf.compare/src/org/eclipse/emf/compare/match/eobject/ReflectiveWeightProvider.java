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

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

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
			/*
			 * This is worst than empirical but it works in many cases, if your feature is a "name" its likely
			 * that it's important for matching the element. At some point I'll have to come up with something
			 * which is more extensible..
			 */
			if ("name".equals(feature.getName())) { //$NON-NLS-1$
				found = Integer.valueOf(4);
			} else {
				found = Integer.valueOf(1);
			}
		}
		return found.intValue();
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
}
