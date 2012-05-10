/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.conflict;

import static com.google.common.base.Predicates.and;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.tests.conflict.data.ConflictInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

@SuppressWarnings("nls")
public class ConflictDetectionTest {
	private ConflictInputData input = new ConflictInputData();

	@Test
	public void testA1UseCaseForAttribute() throws IOException {
		final Resource left = input.getA1AttributeLeft();
		final Resource origin = input.getA1AttributeOrigin();
		final Resource right = input.getA1AttributeRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = changedAttribute("root.conflictHolder",
				"singleValuedAttribute", "origin", "left");
		final Predicate<? super Diff> rightDiffDescription = removed("root.conflictHolder");

		final Diff leftDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiffDescription));
		final Diff rightDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				rightDiffDescription));

		assertNotNull(leftDiff);
		assertNotNull(rightDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(2), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(ConflictKind.REAL, conflict.getKind());
	}

	@Test
	public void testA1UseCaseForReference() throws IOException {
		final Resource left = input.getA1ReferenceLeft();
		final Resource origin = input.getA1ReferenceOrigin();
		final Resource right = input.getA1ReferenceRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		/*
		 * We have three diffs here : an object has been deleted on the right side, its reference has been
		 * unset, and that same reference has been changed on the left to a new value. The two diffs on the
		 * right are in conflict with the only diff on the left.
		 */
		assertSame(Integer.valueOf(3), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftReferenceDiffDescription = changedReference("root.conflictHolder",
				"singleValuedReference", "root.origin", "root.left");
		final Predicate<? super Diff> rightReferenceDiffDescription = changedReference("root.conflictHolder",
				"singleValuedReference", "root.origin", null);
		final Predicate<? super Diff> rightDeleteDiffDescription = removed("root.conflictHolder");

		final Diff leftReferenceDiff = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), leftReferenceDiffDescription));
		final Diff rightReferenceDiff = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), rightReferenceDiffDescription));
		final Diff rightDeleteDiff = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), rightDeleteDiffDescription));

		assertNotNull(leftReferenceDiff);
		assertNotNull(rightReferenceDiff);
		assertNotNull(rightDeleteDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(3), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftReferenceDiff));
		assertTrue(conflictDiff.contains(rightReferenceDiff));
		assertTrue(conflictDiff.contains(rightDeleteDiff));
		assertSame(ConflictKind.REAL, conflict.getKind());
	}

	@Test
	public void testA2UseCaseForAttribute() throws IOException {
		final Resource left = input.getA2AttributeLeft();
		final Resource origin = input.getA2AttributeOrigin();
		final Resource right = input.getA2AttributeRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = changedAttribute("root.conflictHolder",
				"singleValuedAttribute", null, "left");
		final Predicate<? super Diff> rightDiffDescription = removed("root.conflictHolder");

		final Diff leftDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiffDescription));
		final Diff rightDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				rightDiffDescription));

		assertNotNull(leftDiff);
		assertNotNull(rightDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(2), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(ConflictKind.REAL, conflict.getKind());
	}

	@Test
	public void testA2UseCaseForReference() throws IOException {
		final Resource left = input.getA2ReferenceLeft();
		final Resource origin = input.getA2ReferenceOrigin();
		final Resource right = input.getA2ReferenceRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = changedReference("root.conflictHolder",
				"singleValuedReference", null, "root.left");
		final Predicate<? super Diff> rightDiffDescription = removed("root.conflictHolder");

		final Diff leftDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiffDescription));
		final Diff rightDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				rightDiffDescription));

		assertNotNull(leftDiff);
		assertNotNull(rightDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(2), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(ConflictKind.REAL, conflict.getKind());
	}

	@Test
	public void testA3UseCaseForAttribute() throws IOException {
		final Resource left = input.getA3AttributeLeft();
		final Resource origin = input.getA3AttributeOrigin();
		final Resource right = input.getA3AttributeRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = changedAttribute("root.conflictHolder",
				"singleValuedAttribute", "origin", null);
		final Predicate<? super Diff> rightDiffDescription = removed("root.conflictHolder");

		final Diff leftDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiffDescription));
		final Diff rightDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				rightDiffDescription));

		assertNotNull(leftDiff);
		assertNotNull(rightDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(2), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(ConflictKind.REAL, conflict.getKind());
	}

	@Test
	public void testA3UseCaseForReference() throws IOException {
		final Resource left = input.getA3ReferenceLeft();
		final Resource origin = input.getA3ReferenceOrigin();
		final Resource right = input.getA3ReferenceRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		/*
		 * We expect three differences here : an object has been deleted on the right, its reference has been
		 * unset, and that same reference has been unset on the left. All three diffs are in pseudo-conflict
		 * with each other.
		 */
		assertSame(Integer.valueOf(3), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftReferenceDiffDescription = changedReference("root.conflictHolder",
				"singleValuedReference", "root.origin", null);
		final Predicate<? super Diff> rightReferenceDiffDescription = changedReference("root.conflictHolder",
				"singleValuedReference", "root.origin", null);
		final Predicate<? super Diff> rightDeleteDiffDescription = removed("root.conflictHolder");

		final Diff leftReferenceDiff = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), leftReferenceDiffDescription));
		final Diff rightReferenceDiff = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), rightReferenceDiffDescription));
		final Diff rightDeleteDiff = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), rightDeleteDiffDescription));

		assertNotNull(leftReferenceDiff);
		assertNotNull(rightReferenceDiff);
		assertNotNull(rightDeleteDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(3), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftReferenceDiff));
		assertTrue(conflictDiff.contains(rightReferenceDiff));
		assertTrue(conflictDiff.contains(rightDeleteDiff));
		assertSame(ConflictKind.PSEUDO, conflict.getKind());
	}

	@Test
	public void testB1UseCaseForAttribute() throws IOException {
		final Resource left = input.getB1AttributeLeft();
		final Resource origin = input.getB1AttributeOrigin();
		final Resource right = input.getB1AttributeRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = changedAttribute("root.conflictHolder",
				"singleValuedAttribute", "origin", "left");
		final Predicate<? super Diff> rightDiffDescription = changedAttribute("root.conflictHolder",
				"singleValuedAttribute", "origin", "right");

		final Diff leftDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiffDescription));
		final Diff rightDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				rightDiffDescription));

		assertNotNull(leftDiff);
		assertNotNull(rightDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(2), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(ConflictKind.REAL, conflict.getKind());
	}

	@Test
	public void testB1UseCaseForReference() throws IOException {
		final Resource left = input.getB1ReferenceLeft();
		final Resource origin = input.getB1ReferenceOrigin();
		final Resource right = input.getB1ReferenceRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = changedReference("root.conflictHolder",
				"singleValuedReference", "root.origin", "root.left");
		final Predicate<? super Diff> rightDiffDescription = changedReference("root.conflictHolder",
				"singleValuedReference", "root.origin", "root.right");

		final Diff leftDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiffDescription));
		final Diff rightDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				rightDiffDescription));

		assertNotNull(leftDiff);
		assertNotNull(rightDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(2), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(ConflictKind.REAL, conflict.getKind());
	}

	@Test
	public void testB2UseCaseForAttribute() throws IOException {
		final Resource left = input.getB2AttributeLeft();
		final Resource origin = input.getB2AttributeOrigin();
		final Resource right = input.getB2AttributeRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = changedAttribute("root.conflictHolder",
				"singleValuedAttribute", "origin", "left");
		final Predicate<? super Diff> rightDiffDescription = changedAttribute("root.conflictHolder",
				"singleValuedAttribute", "origin", null);

		final Diff leftDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiffDescription));
		final Diff rightDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				rightDiffDescription));

		assertNotNull(leftDiff);
		assertNotNull(rightDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(2), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(ConflictKind.REAL, conflict.getKind());
	}

	@Test
	public void testB2UseCaseForReference() throws IOException {
		final Resource left = input.getB2ReferenceLeft();
		final Resource origin = input.getB2ReferenceOrigin();
		final Resource right = input.getB2ReferenceRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = changedReference("root.conflictHolder",
				"singleValuedReference", "root.origin", "root.left");
		final Predicate<? super Diff> rightDiffDescription = changedReference("root.conflictHolder",
				"singleValuedReference", "root.origin", null);

		final Diff leftDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiffDescription));
		final Diff rightDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				rightDiffDescription));

		assertNotNull(leftDiff);
		assertNotNull(rightDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(2), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(ConflictKind.REAL, conflict.getKind());
	}

	@Test
	public void testB3UseCaseForAttribute() throws IOException {
		final Resource left = input.getB3AttributeLeft();
		final Resource origin = input.getB3AttributeOrigin();
		final Resource right = input.getB3AttributeRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = changedAttribute("root.conflictHolder",
				"singleValuedAttribute", null, "left");
		final Predicate<? super Diff> rightDiffDescription = changedAttribute("root.conflictHolder",
				"singleValuedAttribute", null, "right");

		final Diff leftDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiffDescription));
		final Diff rightDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				rightDiffDescription));

		assertNotNull(leftDiff);
		assertNotNull(rightDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(2), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(ConflictKind.REAL, conflict.getKind());
	}

	@Test
	public void testB3UseCaseForReference() throws IOException {
		final Resource left = input.getB3ReferenceLeft();
		final Resource origin = input.getB3ReferenceOrigin();
		final Resource right = input.getB3ReferenceRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = changedReference("root.conflictHolder",
				"singleValuedReference", null, "root.left");
		final Predicate<? super Diff> rightDiffDescription = changedReference("root.conflictHolder",
				"singleValuedReference", null, "root.right");

		final Diff leftDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiffDescription));
		final Diff rightDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				rightDiffDescription));

		assertNotNull(leftDiff);
		assertNotNull(rightDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(2), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(ConflictKind.REAL, conflict.getKind());
	}

	@Test
	public void testB4UseCaseForAttribute() throws IOException {
		final Resource left = input.getB4AttributeLeft();
		final Resource origin = input.getB4AttributeOrigin();
		final Resource right = input.getB4AttributeRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = changedAttribute("root.conflictHolder",
				"singleValuedAttribute", "origin", null);
		final Predicate<? super Diff> rightDiffDescription = changedAttribute("root.conflictHolder",
				"singleValuedAttribute", "origin", "right");

		final Diff leftDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiffDescription));
		final Diff rightDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				rightDiffDescription));

		assertNotNull(leftDiff);
		assertNotNull(rightDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(2), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(ConflictKind.REAL, conflict.getKind());
	}

	@Test
	public void testB4UseCaseForReference() throws IOException {
		final Resource left = input.getB4ReferenceLeft();
		final Resource origin = input.getB4ReferenceOrigin();
		final Resource right = input.getB4ReferenceRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = changedReference("root.conflictHolder",
				"singleValuedReference", "root.origin", null);
		final Predicate<? super Diff> rightDiffDescription = changedReference("root.conflictHolder",
				"singleValuedReference", "root.origin", "root.right");

		final Diff leftDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiffDescription));
		final Diff rightDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				rightDiffDescription));

		assertNotNull(leftDiff);
		assertNotNull(rightDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(2), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(ConflictKind.REAL, conflict.getKind());
	}
}
