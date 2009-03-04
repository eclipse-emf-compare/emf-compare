/*******************************************************************************
 * Copyright (c) 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.internal.statistic;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This class will hold utility methods used by the match service to match resources.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class ResourceSimilarity {
	/**
	 * Utility classes don't need to be instantiated.
	 */
	private ResourceSimilarity() {
		// hides default construtor
	}

	/**
	 * This will try and find a resource in <code>candidates</code> similar to <code>resource</code>.
	 * 
	 * @param resource
	 *            The resource we seek a similar to in the given resourceSet.
	 * @param candidates
	 *            candidate resources.
	 * @return The most similar resource to <code>resource</code> we could find in <code>resourceSet</code>.
	 */
	public static Resource findMatchingResource(Resource resource, List<Resource> candidates) {
		final double resourceSimilarityThreshold = 0.7d;
		final URI referenceURI = resource.getURI();
		if (candidates.size() == 1)
			return candidates.get(0);

		Resource mostSimilar = null;
		double highestSimilarity = -1;
		for (final Resource candidate : candidates) {
			final URI candidateURI = candidate.getURI();
			if (referenceURI.fileExtension().equals(candidateURI.fileExtension())) {
				final String[] referenceSegments = referenceURI.trimFileExtension().segments();
				final String[] candidateSegments = candidateURI.trimFileExtension().segments();
				final double similarity = resourceURISimilarity(referenceSegments, candidateSegments);
				if (similarity > highestSimilarity) {
					highestSimilarity = similarity;
					mostSimilar = candidate;
				}
			}
		}

		// Consider dissimilar
		if (highestSimilarity < resourceSimilarityThreshold) {
			mostSimilar = null;
		}
		return mostSimilar;
	}

	/**
	 * This will compute the similarity of two URIs based on their segments (minus file extension).
	 * 
	 * @param reference
	 *            The reference URI.
	 * @param candidate
	 *            Candidate for which the similarity to <code>reference</code> is to be computed.
	 * @return A double comprised between <code>0</code> and <code>1</code> included, <code>1</code> being
	 *         equal and <code>0</code> different.
	 */
	private static double resourceURISimilarity(String[] reference, String[] candidate) {
		final double nameWeight = 0.6;
		final double equalSegmentWeight = 0.4;

		final String referenceName = reference[reference.length - 1];
		final String candidateName = candidate[candidate.length - 1];
		final double nameSimilarity = NameSimilarity.nameSimilarityMetric(referenceName, candidateName);

		double equalSegments = 0d;
		int referenceIndex = reference.length - 2;
		int candidateIndex = candidate.length - 2;
		while (referenceIndex >= 0 && candidateIndex >= 0) {
			if (reference[referenceIndex].equals(candidate[candidateIndex])) {
				equalSegments++;
			}
			referenceIndex--;
			candidateIndex--;
		}
		if (reference.length == 1 || candidate.length == 1)
			return nameSimilarity;
		return nameSimilarity * nameWeight + (equalSegments * 2 / (reference.length + candidate.length - 2))
				* equalSegmentWeight;
	}
}
