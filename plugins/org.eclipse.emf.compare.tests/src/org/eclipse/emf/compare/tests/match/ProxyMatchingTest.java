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
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.match.data.MatchInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

/**
 * Tests the matching behavior when unresolvable proxies exist.
 *
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public class ProxyMatchingTest {

	private MatchInputData input = new MatchInputData();

	/**
	 * Tests a scenario in which the elements are identified via xmi:ids and the left side uses an
	 * unresolvable proxy.
	 * 
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testProxyA1() throws IOException {
		final Resource left = input.getProxyMatchingA1Left();
		final Resource right = input.getProxyMatchingA1Right();

		final ResourceSet leftResourceSet = left.getResourceSet();
		final ResourceSet rightResourceSet = right.getResourceSet();

		EcoreUtil.resolveAll(leftResourceSet);
		EcoreUtil.resolveAll(rightResourceSet);

		final IComparisonScope scope = new DefaultComparisonScope(leftResourceSet, rightResourceSet, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Match> matches = comparison.getMatches();

		// There should be one root match
		assertEquals(1, matches.size());

		// The root match should have two submatches since the proxy can not be matched to the "real" object
		final Match rootMatch = matches.get(0);
		final List<Match> subMatches = rootMatch.getSubmatches();
		assertEquals(2, subMatches.size());

		// Based on the matches two differences should be determined (add/delete)
		final List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());
	}

	/**
	 * Tests a scenario in which the elements are identified via xmi:ids and the left side uses a resolvable
	 * proxy.
	 * 
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testProxyA2() throws IOException {
		final Resource left = input.getProxyMatchingA2Left();
		final Resource right = input.getProxyMatchingA2Right();

		final ResourceSet leftResourceSet = left.getResourceSet();
		final ResourceSet rightResourceSet = right.getResourceSet();

		EcoreUtil.resolveAll(leftResourceSet);
		EcoreUtil.resolveAll(rightResourceSet);

		final IComparisonScope scope = new DefaultComparisonScope(leftResourceSet, rightResourceSet, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Match> matches = comparison.getMatches();

		// There should be one root match
		assertEquals(1, matches.size());

		// The root match should have one submatch
		final Match rootMatch = matches.get(0);
		final List<Match> subMatches = rootMatch.getSubmatches();
		assertEquals(1, subMatches.size());

		// There should be one ResourceAttachmentChange
		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());
		assertTrue(differences.get(0) instanceof ResourceAttachmentChange);
	}
}
