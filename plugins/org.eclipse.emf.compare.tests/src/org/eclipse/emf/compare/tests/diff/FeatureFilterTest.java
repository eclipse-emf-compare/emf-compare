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
package org.eclipse.emf.compare.tests.diff;

import static org.junit.Assert.assertFalse;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diff.FeatureFilter;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.diff.data.featurefilter.featuremap.FeatureFilterFeatureMapsInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

/**
 * We will use this to test the methods exposed by the {@link FeatureFilter}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("nls")
public class FeatureFilterTest {
	@Test
	public void testOnFeatureMaps() throws IOException {
		// Test with models containing NodeFeatureMapContainment2 elements. A NodeFeatureMapContainment2 is an
		// element that contains a map with two types of entries: NodeMultipleContainment and
		// NodeSingleValueContainment. NodeMultipleContainment has containment references that don't exist in
		// NodeSingleValueContainment. The FeatureFilter needs to properly handle the case.
		FeatureFilterFeatureMapsInputData inputData = new FeatureFilterFeatureMapsInputData();

		final Resource left = inputData.getNodesLeft();
		final Resource right = inputData.getNodesRight();

		IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);
		FeatureFilter filter = new FeatureFilter();

		Collection<Match> allMatches = getAllMatches(comparison);
		assertFalse(allMatches.isEmpty());

		for (Match m : allMatches) {
			Iterator<EReference> referencesToCheck = filter.getReferencesToCheck(m);
			EObject rhsMatch = m.getRight();
			if (rhsMatch != null) {
				for (EStructuralFeature sf : rhsMatch.eClass().getEAllStructuralFeatures()) {
					// If the ref doesn't exists in the right hand side of the match, the filter must exclude
					// it.
					if (sf == null) {
						assertFalse(Iterators.contains(referencesToCheck, sf));
					}
				}
			}
		}
	}

	private Collection<Match> getAllMatches(Comparison comparison) {
		Collection<Match> matches = Sets.newLinkedHashSet();
		for (Match match : comparison.getMatches()) {
			matches.add(match);
			for (Match subMatch : match.getAllSubmatches()) {
				matches.add(subMatch);
			}
		}
		return matches;

	}
}
