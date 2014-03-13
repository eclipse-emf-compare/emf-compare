/**
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.fullcomparison;

import static org.eclipse.emf.compare.tests.framework.EMFCompareAssert.assertAdded;
import static org.eclipse.emf.compare.tests.framework.EMFCompareAssert.assertAddedToReference;
import static org.eclipse.emf.compare.tests.framework.EMFCompareAssert.assertAllMatched;
import static org.eclipse.emf.compare.tests.framework.EMFCompareAssert.assertChangedAttribute;
import static org.eclipse.emf.compare.tests.framework.EMFCompareAssert.assertChangedReference;
import static org.eclipse.emf.compare.tests.framework.EMFCompareAssert.assertRemoved;
import static org.eclipse.emf.compare.tests.framework.EMFCompareAssert.assertRemovedFromReference;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.framework.EMFCompareTestBase;
import org.eclipse.emf.compare.tests.framework.IdentifierMatchValidator;
import org.eclipse.emf.compare.tests.framework.NotifierTuple;
import org.eclipse.emf.compare.tests.framework.junit.EMFCompareTestRunner;
import org.eclipse.emf.compare.tests.framework.junit.annotation.BeforeMatch;
import org.eclipse.emf.compare.tests.framework.junit.annotation.ConflictTest;
import org.eclipse.emf.compare.tests.framework.junit.annotation.DiffTest;
import org.eclipse.emf.compare.tests.framework.junit.annotation.MatchTest;
import org.eclipse.emf.compare.tests.framework.junit.annotation.UseCase;
import org.eclipse.emf.compare.tests.fullcomparison.data.identifier.IdentifierMatchInputData;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
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
	public void testIdentifierDiffTest(@SuppressWarnings("unused") IComparisonScope scope,
			Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// See file org.eclipse.emf.compare.tests.model.mock.CHANGES for the description of these

		assertAdded(differences, "extlibrary.BookCategory.Encyclopedia", DifferenceSource.LEFT);
		assertAdded(differences, "extlibrary.BookCategory.Dictionary", DifferenceSource.LEFT);
		assertAdded(differences, "extlibrary.Magazine", DifferenceSource.LEFT);
		assertAdded(differences, "extlibrary.Magazine.title", DifferenceSource.LEFT);
		assertAdded(differences, "extlibrary.Magazine.pages", DifferenceSource.LEFT);
		assertAdded(differences, "extlibrary.Person.fullName", DifferenceSource.LEFT);

		assertRemoved(differences, "extlibrary.Periodical", DifferenceSource.LEFT);
		assertRemoved(differences, "extlibrary.Periodical.issuesPerYear", DifferenceSource.LEFT);
		assertRemoved(differences, "extlibrary.Person.firstName", DifferenceSource.LEFT);

		assertChangedAttribute(differences, "extlibrary.Lendable", "name", "Lendable", "Borrowable",
				DifferenceSource.LEFT);

		assertAddedToReference(differences, "extlibrary.Magazine", "eSuperTypes",
				"extlibrary.CirculatingItem", DifferenceSource.LEFT);
		assertRemovedFromReference(differences, "extlibrary.Periodical", "eSuperTypes", "extlibrary.Item",
				DifferenceSource.LEFT);

		assertChangedReference(differences, "extlibrary.Magazine.title", "eType", null, "ecore.EString",
				DifferenceSource.LEFT);
		assertChangedReference(differences, "extlibrary.Magazine.pages", "eType", null, "ecore.EInt",
				DifferenceSource.LEFT);
		assertChangedReference(differences, "extlibrary.Person.fullName", "eType", null, "ecore.EString",
				DifferenceSource.LEFT);

		assertChangedReference(differences, "extlibrary.Periodical.issuesPerYear", "eType", "ecore.EInt",
				null, DifferenceSource.LEFT);
		assertChangedReference(differences, "extlibrary.Person.firstName", "eType", "ecore.EString", null,
				DifferenceSource.LEFT);

		// some diffs change according to the presence of an origin.
		if (comparison.isThreeWay()) {
			// This one will be detected differently in two-way
			assertRemoved(differences, "extlibrary.Person.lastName", DifferenceSource.LEFT);
			assertChangedReference(differences, "extlibrary.Person.lastName", "eType", "ecore.EString", null,
					DifferenceSource.LEFT);

			final DifferenceSource side = DifferenceSource.RIGHT;
			assertAdded(differences, "extlibrary.BookCategory.Manga", side);
			assertAdded(differences, "extlibrary.BookCategory.Manhwa", side);
			assertAdded(differences, "extlibrary.Book.subtitle", side);
			assertAdded(differences, "extlibrary.Magazine", side);
			assertAdded(differences, "extlibrary.TitledItem", side);
			assertAdded(differences, "extlibrary.TitledItem.title", side);

			assertRemoved(differences, "extlibrary.Book.title", side);
			assertRemoved(differences, "extlibrary.AudioVisualItem.title", side);

			assertAddedToReference(differences, "extlibrary.Book", "eSuperTypes", "extlibrary.TitledItem",
					side);
			assertAddedToReference(differences, "extlibrary.Periodical", "eSuperTypes",
					"extlibrary.TitledItem", side);
			assertAddedToReference(differences, "extlibrary.AudioVisualItem", "eSuperTypes",
					"extlibrary.TitledItem", side);
			assertAddedToReference(differences, "extlibrary.Magazine", "eSuperTypes",
					"extlibrary.Periodical", side);

			assertChangedReference(differences, "extlibrary.Book.subtitle", "eType", null, "ecore.EString",
					side);
			assertChangedReference(differences, "extlibrary.TitledItem.title", "eType", null,
					"ecore.EString", side);

			assertChangedReference(differences, "extlibrary.Book.title", "eType", "ecore.EString", null, side);
			assertChangedReference(differences, "extlibrary.AudioVisualItem.title", "eType", "ecore.EString",
					null, side);

			/*
			 * The following are actually conflicts, most changes according to whether we are in three-way or
			 * not.
			 */
			assertChangedAttribute(differences, "extlibrary.AudioVisualItem.length", "name", "minutesLength",
					"length", DifferenceSource.LEFT);
			// These are actually pseudo-conflicts : same diff on both sides
			assertRemoved(differences, "extlibrary.BookOnTape.reader", DifferenceSource.LEFT);
			assertRemoved(differences, "extlibrary.BookOnTape.reader", DifferenceSource.RIGHT);
			assertRemoved(differences, "extlibrary.Periodical.title", DifferenceSource.LEFT);
			assertRemoved(differences, "extlibrary.Periodical.title", DifferenceSource.RIGHT);
			assertChangedReference(differences, "extlibrary.BookOnTape.reader", "eType", "extlibrary.Person",
					null, DifferenceSource.LEFT);
			assertChangedReference(differences, "extlibrary.BookOnTape.reader", "eType", "extlibrary.Person",
					null, DifferenceSource.RIGHT);
			assertChangedReference(differences, "extlibrary.Periodical.title", "eType", "ecore.EString",
					null, DifferenceSource.LEFT);
			assertChangedReference(differences, "extlibrary.Periodical.title", "eType", "ecore.EString",
					null, DifferenceSource.RIGHT);
			/*
			 * These changes can only be detected with an origin : lastName has been removed in the left model
			 * and thus only the removal can be detected in two-way. Likewise, "minutesLength" has been
			 * renamed in both left and right, and thus no mention of that value can be found in two-way
			 */
			assertChangedAttribute(differences, "extlibrary.Person.familyName", "name", "lastName",
					"familyName", side);
			assertChangedAttribute(differences, "extlibrary.AudioVisualItem.length", "name", "minutesLength",
					"minutes", side);
		} else {
			assertRemoved(differences, "extlibrary.Person.familyName", DifferenceSource.LEFT);
			assertChangedReference(differences, "extlibrary.Person.familyName", "eType", "ecore.EString",
					null, DifferenceSource.LEFT);

			final DifferenceSource side = DifferenceSource.LEFT;
			assertRemoved(differences, "extlibrary.BookCategory.Manga", side);
			assertRemoved(differences, "extlibrary.BookCategory.Manhwa", side);
			assertRemoved(differences, "extlibrary.Book.subtitle", side);
			assertRemoved(differences, "extlibrary.Magazine", side);
			assertRemoved(differences, "extlibrary.TitledItem", side);
			assertRemoved(differences, "extlibrary.TitledItem.title", side);

			assertAdded(differences, "extlibrary.Book.title", side);
			assertAdded(differences, "extlibrary.AudioVisualItem.title", side);

			assertRemovedFromReference(differences, "extlibrary.Book", "eSuperTypes",
					"extlibrary.TitledItem", side);
			assertRemovedFromReference(differences, "extlibrary.Periodical", "eSuperTypes",
					"extlibrary.TitledItem", side);
			assertRemovedFromReference(differences, "extlibrary.AudioVisualItem", "eSuperTypes",
					"extlibrary.TitledItem", side);
			assertRemovedFromReference(differences, "extlibrary.Magazine", "eSuperTypes",
					"extlibrary.Periodical", side);

			assertChangedReference(differences, "extlibrary.Book.subtitle", "eType", "ecore.EString", null,
					side);
			assertChangedReference(differences, "extlibrary.TitledItem.title", "eType", "ecore.EString",
					null, side);

			assertChangedReference(differences, "extlibrary.Book.title", "eType", null, "ecore.EString", side);
			assertChangedReference(differences, "extlibrary.AudioVisualItem.title", "eType", null,
					"ecore.EString", side);

			// This is a conflict, the expected diff is not the same in two-way
			assertChangedAttribute(differences, "extlibrary.AudioVisualItem.length", "name", "minutes",
					"length", DifferenceSource.LEFT);
		}

		// We should have no more differences than those
		assertTrue(differences.isEmpty());
	}

	@ConflictTest
	public void testIdentifierConflictTest(@SuppressWarnings("unused") IComparisonScope scope,
			Comparison comparison) {
		final List<Conflict> conflicts = comparison.getConflicts();

		if (!comparison.isThreeWay()) {
			assertTrue(conflicts.isEmpty());
			return;
		}

		assertEquals(5, conflicts.size());

		Conflict periodicalConflict = null;
		Conflict titleConflict = null;
		Conflict lastNameConflict = null;
		Conflict minutesLengthConflict = null;
		Conflict readerConflict = null;

		for (Conflict conflict : conflicts) {
			for (Diff diff : conflict.getDifferences()) {
				boolean breakLoop = false;
				final Match match = diff.getMatch();
				if (isMatchOf(match, "Periodical") && conflict.getDifferences().size() == 3) {
					periodicalConflict = conflict;
					breakLoop = true;
				} else if (isMatchOf(match, "Periodical")) {
					titleConflict = conflict;
					breakLoop = true;
				} else if (isMatchOf(match, "Person")) {
					lastNameConflict = conflict;
					breakLoop = true;
				} else if (isMatchOf(match, "BookOnTape")) {
					readerConflict = conflict;
					breakLoop = true;
				} else if (isMatchOf(match, "minutesLength")) {
					minutesLengthConflict = conflict;
					breakLoop = true;
				}
				if (breakLoop) {
					break;
				}
			}
		}

		assertNotNull(periodicalConflict);
		assertNotNull(titleConflict);
		assertNotNull(lastNameConflict);
		assertNotNull(minutesLengthConflict);
		assertNotNull(readerConflict);

		// These classic asserts will make the compiler happy.
		assert periodicalConflict != null;
		assert titleConflict != null;
		assert lastNameConflict != null;
		assert minutesLengthConflict != null;
		assert readerConflict != null;

		final List<Diff> periodicalDiffs = Lists.newArrayList(periodicalConflict.getDifferences());
		assertSame(ConflictKind.REAL, periodicalConflict.getKind());
		assertRemoved(periodicalDiffs, "extlibrary.Periodical", DifferenceSource.LEFT);
		assertAddedToReference(periodicalDiffs, "extlibrary.Periodical", "eSuperTypes",
				"extlibrary.TitledItem", DifferenceSource.RIGHT);
		assertAddedToReference(periodicalDiffs, "extlibrary.Magazine", "eSuperTypes",
				"extlibrary.Periodical", DifferenceSource.RIGHT);
		assertTrue(periodicalDiffs.isEmpty());

		final List<Diff> nameDiffs = Lists.newArrayList(lastNameConflict.getDifferences());
		assertSame(ConflictKind.REAL, lastNameConflict.getKind());
		assertRemoved(nameDiffs, "extlibrary.Person.lastName", DifferenceSource.LEFT);
		assertChangedAttribute(nameDiffs, "extlibrary.Person.familyName", "name", "lastName", "familyName",
				DifferenceSource.RIGHT);
		assertTrue(nameDiffs.isEmpty());

		final List<Diff> lengthDiffs = Lists.newArrayList(minutesLengthConflict.getDifferences());
		assertSame(ConflictKind.REAL, minutesLengthConflict.getKind());
		assertChangedAttribute(lengthDiffs, "extlibrary.AudioVisualItem.length", "name", "minutesLength",
				"length", DifferenceSource.LEFT);
		assertChangedAttribute(lengthDiffs, "extlibrary.AudioVisualItem.length", "name", "minutesLength",
				"minutes", DifferenceSource.RIGHT);
		assertTrue(lengthDiffs.isEmpty());

		final List<Diff> titleDiffs = Lists.newArrayList(titleConflict.getDifferences());
		assertSame(ConflictKind.PSEUDO, titleConflict.getKind());
		assertRemoved(titleDiffs, "extlibrary.Periodical.title", DifferenceSource.LEFT);
		assertChangedReference(titleDiffs, "extlibrary.Periodical.title", "eType", "ecore.EString", null,
				DifferenceSource.LEFT);
		assertChangedReference(titleDiffs, "extlibrary.Periodical.title", "eType", "ecore.EString", null,
				DifferenceSource.RIGHT);
		assertRemoved(titleDiffs, "extlibrary.Periodical.title", DifferenceSource.RIGHT);
		assertTrue(titleDiffs.isEmpty());

		final List<Diff> readerDiffs = Lists.newArrayList(readerConflict.getDifferences());
		assertSame(ConflictKind.PSEUDO, readerConflict.getKind());
		assertRemoved(readerDiffs, "extlibrary.BookOnTape.reader", DifferenceSource.LEFT);
		assertRemoved(readerDiffs, "extlibrary.BookOnTape.reader", DifferenceSource.RIGHT);
		assertChangedReference(readerDiffs, "extlibrary.BookOnTape.reader", "eType", "extlibrary.Person",
				null, DifferenceSource.LEFT);
		assertChangedReference(readerDiffs, "extlibrary.BookOnTape.reader", "eType", "extlibrary.Person",
				null, DifferenceSource.RIGHT);
		assertTrue(readerDiffs.isEmpty());
	}

	private boolean isMatchOf(Match match, String name) {
		boolean isMatch = false;
		if (match.getLeft() instanceof ENamedElement) {
			if (((ENamedElement)match.getLeft()).getName().equals(name)) {
				isMatch = true;
			}
		}
		if (!isMatch && match.getRight() instanceof ENamedElement) {
			if (((ENamedElement)match.getRight()).getName().equals(name)) {
				isMatch = true;
			}
		}
		if (!isMatch && match.getOrigin() instanceof ENamedElement) {
			if (((ENamedElement)match.getOrigin()).getName().equals(name)) {
				isMatch = true;
			}
		}
		return isMatch;
	}
}
