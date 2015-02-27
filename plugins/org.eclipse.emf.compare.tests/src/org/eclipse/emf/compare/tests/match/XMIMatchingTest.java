/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.match;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.match.data.MatchInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.junit.Test;

/**
 * Tests the matching behavior when combining xmi:ids and id-attributes.
 *
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public class XMIMatchingTest {

	private MatchInputData input = new MatchInputData();

	/**
	 * Tests a scenario in which the elements are identified via xmi:ids and an additional id-attribute is set
	 * on one side.
	 *
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testSetIDAttribute() throws IOException {
		final Resource left = input.getSetIDAttributeLeft();
		final Resource right = input.getSetIDAttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Match> matches = comparison.getMatches();

		// There should be one root match
		assertEquals(1, matches.size());

		// The root match should have two submatches.
		final Match rootMatch = matches.get(0);
		final List<Match> subMatches = rootMatch.getSubmatches();
		assertEquals(2, subMatches.size());

		// matches should have same xmi:id
		for (Match match : subMatches) {
			EObject leftObject = match.getLeft();
			EObject rightObject = match.getRight();

			Resource leftResource = leftObject.eResource();
			Resource rightResource = rightObject.eResource();

			String leftID = ((XMIResource)leftResource).getID(leftObject);
			String rightID = ((XMIResource)rightResource).getID(rightObject);
			assertEquals(leftID, rightID);
		}

		// Based on the matches only one difference should be determined
		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());
	}

	/**
	 * Tests a scenario in which the elements should be matched according to their xmi:ids although
	 * contradicting attribute ids are set.
	 *
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testXMIIDPriorityA1() throws IOException {
		final Resource left = input.getXMIIDPriorityA1Left();
		final Resource right = input.getXMIIDPriorityA1Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Match> matches = comparison.getMatches();

		// There should be one root match
		assertEquals(1, matches.size());

		// The root match should have two submatches.
		final Match rootMatch = matches.get(0);
		final List<Match> subMatches = rootMatch.getSubmatches();
		assertEquals(2, subMatches.size());

		// matches should have same xmi:id since they should not be matched via id-attribute
		for (Match match : subMatches) {
			EObject leftObject = match.getLeft();
			EObject rightObject = match.getRight();

			Resource leftResource = leftObject.eResource();
			Resource rightResource = rightObject.eResource();

			String leftID = ((XMIResource)leftResource).getID(leftObject);
			String rightID = ((XMIResource)rightResource).getID(rightObject);
			assertEquals(leftID, rightID);
		}
	}

	/**
	 * Tests a scenario without xmi:ids in the model. The elements should not match since new xmi:ids are
	 * generated on the fly.
	 *
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testXMIIDPriorityA2() throws IOException {
		final Resource left = input.getXMIIDPriorityA2Left();
		final Resource right = input.getXMIIDPriorityA2Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Match> matches = comparison.getMatches();

		// There should be one root match
		assertEquals(1, matches.size());

		final Match rootMatch = matches.get(0);
		final List<Match> subMatches = rootMatch.getSubmatches();

		// Should have 4 matches since the elements can not be matched
		assertEquals(4, subMatches.size());

		for (Match match : subMatches) {
			EObject leftObject = match.getLeft();
			EObject rightObject = match.getRight();

			final boolean leftIsNull = leftObject == null && rightObject != null;
			final boolean rightIsNull = rightObject == null && leftObject != null;

			assertTrue(leftIsNull || rightIsNull);
		}
	}
}
