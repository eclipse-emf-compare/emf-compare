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

import static com.google.common.base.Predicates.and;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.tests.framework.predicates.EMFComparePredicates.added;
import static org.eclipse.emf.compare.tests.framework.predicates.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.tests.framework.predicates.EMFComparePredicates.removed;

import com.google.common.base.Predicate;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.framework.EMFCompareTestBase;
import org.eclipse.emf.compare.tests.framework.IdentifierMatchValidator;
import org.eclipse.emf.compare.tests.framework.NotifierTuple;
import org.eclipse.emf.compare.tests.framework.junit.EMFCompareTestRunner;
import org.eclipse.emf.compare.tests.framework.junit.annotation.BeforeMatch;
import org.eclipse.emf.compare.tests.framework.junit.annotation.DiffTest;
import org.eclipse.emf.compare.tests.framework.junit.annotation.MatchTest;
import org.eclipse.emf.compare.tests.framework.junit.annotation.UseCase;
import org.eclipse.emf.compare.tests.match.data.identifier.IdentifierMatchInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.runner.RunWith;

@RunWith(EMFCompareTestRunner.class)
@SuppressWarnings("nls")
public class IdentifierComparisonTest extends EMFCompareTestBase {
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

		// Validate that all matches point to sides that have the same IDs, and that there is only 1 Match for
		// one "ID" (if two EObjects on two different sides have the same ID, they share the same Match).
		final IdentifierMatchValidator validator = new IdentifierMatchValidator();
		validator.validate(comparison);

		// Make sure that we have a Match for all EObjects of this scope
		final List<EObject> leftChildren = getAllProperContent(left);
		final List<EObject> rightChildren = getAllProperContent(right);
		final List<EObject> originChildren = getAllProperContent(origin);

		assertAllMatched(leftChildren, comparison, scope);
		assertAllMatched(rightChildren, comparison, scope);
		assertAllMatched(originChildren, comparison, scope);
	}

	@DiffTest
	public void testIdentifierDiffTest(IComparisonScope scope, Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		assertAdded(differences, "extlibrary.BookCategory.Encyclopedia", DifferenceSource.LEFT);
		assertAdded(differences, "extlibrary.BookCategory.Dictionary", DifferenceSource.LEFT);
		assertAdded(differences, "extlibrary.Magazine", DifferenceSource.LEFT);
		assertAdded(differences, "extlibrary.Person.fullName", DifferenceSource.LEFT);

		assertRemoved(differences, "extlibrary.Periodical", DifferenceSource.LEFT);
		assertRemoved(differences, "extlibrary.BookOnTape.reader", DifferenceSource.LEFT);
		assertRemoved(differences, "extlibrary.Person.firstName", DifferenceSource.LEFT);
		assertRemoved(differences, "extlibrary.Person.lastName", DifferenceSource.LEFT);
	}

	private static void assertChanged(List<Diff> differences, String qualifiedName,
			EStructuralFeature feature, Object oldValue, Object newValue, DifferenceSource side) {
		// onE
	}

	private static void assertAdded(List<Diff> differences, String qualifiedName, DifferenceSource side) {
		final Predicate<? super Diff> predicate = and(fromSide(side), added(qualifiedName));
		assertTrue(removeFirst(differences.iterator(), predicate) != null);
	}

	private static void assertRemoved(List<Diff> differences, String qualifiedName, DifferenceSource side) {
		final Predicate<? super Diff> predicate = and(fromSide(side), removed(qualifiedName));
		assertTrue(removeFirst(differences.iterator(), predicate) != null);
	}
}
