/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.utils;

import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.compare.Match;
import org.junit.Before;
import org.junit.Test;

public class ComparisonUtilDeleteTest {

	private static final CompareFactory COMPARE_FACTORY = CompareFactory.eINSTANCE;

	private Match match;

	private Diff diffToDelete;

	@Before
	public void setUp() {
		match = createMatch(COMPARE_FACTORY.createComparison());
		diffToDelete = createDiff(LEFT);
	}

	private Match createMatch(Comparison comparison) {
		Match newMatch = COMPARE_FACTORY.createMatch();
		comparison.getMatches().add(newMatch);
		return newMatch;
	}

	private Diff createDiff(DifferenceSource side) {
		Diff newDiff = COMPARE_FACTORY.createDiff();
		match.getDifferences().add(newDiff);
		newDiff.setSource(side);
		return newDiff;
	}

	private Conflict createConflict(Diff... diffs) {
		Conflict newConflict = COMPARE_FACTORY.createConflict();
		newConflict.getDifferences().addAll(Arrays.asList(diffs));
		match.getComparison().getConflicts().add(newConflict);
		return newConflict;
	}

	private Equivalence createEquivalence(Diff... diffs) {
		Equivalence newEquivalence = COMPARE_FACTORY.createEquivalence();
		newEquivalence.getDifferences().addAll(Arrays.asList(diffs));
		match.getComparison().getEquivalences().add(newEquivalence);
		return newEquivalence;
	}

	@Test
	public void testDeleteDiff() {
		ComparisonUtil.delete(diffToDelete);
		assertFalse(match.getDifferences().contains(diffToDelete));
	}

	@Test
	public void testDeleteDiff_WithConflictsToDelete() {
		final Diff conflictingDiff = createDiff(RIGHT);
		Conflict conflictToDelete = createConflict(diffToDelete, conflictingDiff);
		Conflict otherConflict = createConflict(createDiff(LEFT), createDiff(RIGHT));
		ComparisonUtil.delete(diffToDelete);

		assertFalse(match.getDifferences().contains(diffToDelete));
		assertNull(conflictingDiff.getConflict());
		assertFalse(match.getComparison().getConflicts().contains(conflictToDelete));
		assertTrue(match.getComparison().getConflicts().contains(otherConflict));
		assertEquals(2, otherConflict.getDifferences().size());
	}

	@Test
	public void testDeleteDiff_WithConflictToKeep() {
		Conflict conflictToKeep = createConflict(diffToDelete, createDiff(LEFT), createDiff(RIGHT));
		ComparisonUtil.delete(diffToDelete);

		assertFalse(match.getDifferences().contains(diffToDelete));
		assertFalse(conflictToKeep.getDifferences().contains(diffToDelete));

		assertTrue(match.getComparison().getConflicts().contains(conflictToKeep));
		assertEquals(1, conflictToKeep.getLeftDifferences().size());
		assertEquals(1, conflictToKeep.getRightDifferences().size());
	}

	@Test
	public void testDeleteDiff_WithEquivalencesToDelete() {
		Diff equivalentDiff = createDiff(RIGHT);
		Equivalence equivalenceToDelete = createEquivalence(diffToDelete, equivalentDiff);
		Equivalence otherEquivalence = createEquivalence(createDiff(LEFT), createDiff(RIGHT));
		ComparisonUtil.delete(diffToDelete);

		assertFalse(match.getDifferences().contains(diffToDelete));
		assertNull(equivalentDiff.getEquivalence());
		assertFalse(match.getComparison().getEquivalences().contains(equivalenceToDelete));
		assertTrue(match.getComparison().getEquivalences().contains(otherEquivalence));
		assertEquals(2, otherEquivalence.getDifferences().size());
	}

	@Test
	public void testDeleteDiff_WithEquivalenceToKeep() {
		Equivalence equivalenceToKeep = createEquivalence(diffToDelete, createDiff(LEFT), createDiff(RIGHT));
		ComparisonUtil.delete(diffToDelete);

		assertFalse(match.getDifferences().contains(diffToDelete));
		assertFalse(equivalenceToKeep.getDifferences().contains(diffToDelete));

		assertTrue(match.getComparison().getEquivalences().contains(equivalenceToKeep));
		assertEquals(2, equivalenceToKeep.getDifferences().size());
	}
}
