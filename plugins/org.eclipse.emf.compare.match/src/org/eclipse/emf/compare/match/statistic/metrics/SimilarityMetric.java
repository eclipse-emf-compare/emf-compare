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

import org.eclipse.emf.ecore.EObject;

/**
 * Common interface for similarity metrics used by the generic match engine.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 * 
 */
public interface SimilarityMetric {
	/**
	 * Compute the similarity metric and return the computation result.
	 * 
	 * @param left :
	 *            left element to compare.
	 * @param right :
	 *            right element to compare.
	 * @return a double between 0.0 - elements not matching at all, and 1.0 :
	 *         identical elements.
	 */
	 double computeSimilarity(EObject left, EObject right);
	 	 
}
