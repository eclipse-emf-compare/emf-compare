/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.match;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.framework.EMFCompareTestBase;
import org.eclipse.emf.compare.tests.framework.IdentifierMatchValidator;
import org.eclipse.emf.compare.tests.framework.NotifierTuple;
import org.eclipse.emf.compare.tests.framework.junit.EMFCompareTestRunner;
import org.eclipse.emf.compare.tests.framework.junit.annotation.BeforeMatch;
import org.eclipse.emf.compare.tests.framework.junit.annotation.MatchTest;
import org.eclipse.emf.compare.tests.framework.junit.annotation.UseCase;
import org.eclipse.emf.compare.tests.match.data.identifier.IdentifierMatchInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.runner.RunWith;

@RunWith(EMFCompareTestRunner.class)
public class IdentifierMatchTest extends EMFCompareTestBase {
	private IdentifierMatchInputData inputData = new IdentifierMatchInputData();

	@UseCase("Extended library three-way")
	public NotifierTuple extlibrary3WayTuple() throws IOException {
		final Resource left = inputData.getExtlibraryLeft();
		final Resource right = inputData.getExtlibraryRight();
		final Resource origin = inputData.getExtlibraryOrigin();

		return new NotifierTuple(left, right, origin);
	}

	@UseCase("Extended library two-way")
	public NotifierTuple extlibrary2WayTuple() throws IOException {
		final Resource left = inputData.getExtlibraryLeft();
		final Resource right = inputData.getExtlibraryRight();

		return new NotifierTuple(left, right, null);
	}

	@BeforeMatch
	public void beforeMatch(NotifierTuple tuple) {
		assertNotNull(tuple);
		assertTrue(tuple.getLeft() instanceof Resource);
		assertTrue(tuple.getRight() instanceof Resource);
		// We have both two-way and three-way use cases here
	}

	@MatchTest
	public void testIdentifierMatch(IComparisonScope scope, Comparison comparison) {
		final Resource left = (Resource)scope.getLeft();
		final Resource right = (Resource)scope.getRight();
		final Resource origin = (Resource)scope.getOrigin();
		assertSame(Boolean.valueOf(origin != null), Boolean.valueOf(comparison.isThreeWay()));

		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getMatchedResources().size()));
		final MatchResource matchedResource = comparison.getMatchedResources().get(0);
		assertEquals(left.getURI().toString(), matchedResource.getLeftURI());
		assertEquals(right.getURI().toString(), matchedResource.getRightURI());
		if (origin != null) {
			assertEquals(origin.getURI().toString(), matchedResource.getOriginURI());
		}

		final IdentifierMatchValidator validator = new IdentifierMatchValidator();
		validator.validate(comparison);

		// Make sure that we have a Match for all EObjects
		final List<EObject> leftChildren = getAllProperContent(left);
		final List<EObject> rightChildren = getAllProperContent(right);
		final List<EObject> originChildren = getAllProperContent(origin);

		assertAllMatched(leftChildren, comparison, scope);
		assertAllMatched(rightChildren, comparison, scope);
		assertAllMatched(originChildren, comparison, scope);
	}
}
