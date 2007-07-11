/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.statistic.metrics;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.match.statistic.similarity.NameSimilarity;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EObject;

/**
 * Compute the name similarity between two elements.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class NameSimilarityMetric implements SimilarityMetric {
	/**
	 * Create a name similarity metric.
	 */
	public NameSimilarityMetric() {
		super();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.match.statistic.metrics.SimilarityMetric#computeSimilarity(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
	 */
	public double computeSimilarity(EObject left, EObject right) {
		try {
			return NameSimilarity.nameSimilarityMetric(NameSimilarity.findName(left), NameSimilarity
					.findName(right));
		} catch (FactoryException e) {
			EMFComparePlugin.log(e, false);
		}
		return 1 / 2;
	}
}
