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
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.moved;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.movedInAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.movedInReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;

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
		assertSame(ConflictKind.PSEUDO, conflict.getKind());
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

	@Test
	public void testB5UseCaseForAttribute() throws IOException {
		final Resource left = input.getB5AttributeLeft();
		final Resource origin = input.getB5AttributeOrigin();
		final Resource right = input.getB5AttributeRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = changedAttribute("root.conflictHolder",
				"singleValuedAttribute", null, "leftAndRight");
		final Predicate<? super Diff> rightDiffDescription = changedAttribute("root.conflictHolder",
				"singleValuedAttribute", null, "leftAndRight");

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
		assertSame(ConflictKind.PSEUDO, conflict.getKind());
	}

	@Test
	public void testB5UseCaseForReference() throws IOException {
		final Resource left = input.getB5ReferenceLeft();
		final Resource origin = input.getB5ReferenceOrigin();
		final Resource right = input.getB5ReferenceRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = changedReference("root.conflictHolder",
				"singleValuedReference", null, "root.leftAndRight");
		final Predicate<? super Diff> rightDiffDescription = changedReference("root.conflictHolder",
				"singleValuedReference", null, "root.leftAndRight");

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
		assertSame(ConflictKind.PSEUDO, conflict.getKind());
	}

	@Test
	public void testC1UseCaseForAttribute() throws IOException {
		final Resource left = input.getC1AttributeLeft();
		final Resource origin = input.getC1AttributeOrigin();
		final Resource right = input.getC1AttributeRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = addedToAttribute("root.conflictHolder",
				"multiValuedAttribute", "left1");
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
	public void testC1UseCaseForReference() throws IOException {
		final Resource left = input.getC1ReferenceLeft();
		final Resource origin = input.getC1ReferenceOrigin();
		final Resource right = input.getC1ReferenceRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		/*
		 * We should have 5 differences here. An element has been deleted from the right side, all three of
		 * its reference's values have been deleted, and finally a value has been added into that same
		 * reference on the left side. The addition of a value into the reference conflicts with the removal
		 * of the container from the right. The three other diffs do not conflict.
		 */
		assertSame(Integer.valueOf(5), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftReferenceDiffDescription = addedToReference("root.conflictHolder",
				"multiValuedReference", "root.left1");
		final Predicate<? super Diff> rightDeleteDiffDescription = removed("root.conflictHolder");
		final Predicate<? super Diff> rightReferenceDiff1Description = removedFromReference(
				"root.conflictHolder", "multiValuedReference", "root.origin1");
		final Predicate<? super Diff> rightReferenceDiff2Description = removedFromReference(
				"root.conflictHolder", "multiValuedReference", "root.origin2");
		final Predicate<? super Diff> rightReferenceDiff3Description = removedFromReference(
				"root.conflictHolder", "multiValuedReference", "root.origin3");

		final Diff leftReferenceDiff = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), leftReferenceDiffDescription));
		final Diff rightDeleteDiff = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), rightDeleteDiffDescription));
		final Diff rightReferenceDiff1 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), rightReferenceDiff1Description));
		final Diff rightReferenceDiff2 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), rightReferenceDiff2Description));
		final Diff rightReferenceDiff3 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), rightReferenceDiff3Description));

		assertNotNull(leftReferenceDiff);
		assertNotNull(rightDeleteDiff);
		assertNotNull(rightReferenceDiff1);
		assertNotNull(rightReferenceDiff2);
		assertNotNull(rightReferenceDiff3);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(2), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftReferenceDiff));
		assertTrue(conflictDiff.contains(rightDeleteDiff));
		assertSame(ConflictKind.REAL, conflict.getKind());
	}

	@Test
	public void testC2UseCaseForAttribute() throws IOException {
		final Resource left = input.getC2AttributeLeft();
		final Resource origin = input.getC2AttributeOrigin();
		final Resource right = input.getC2AttributeRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = removedFromAttribute("root.conflictHolder",
				"multiValuedAttribute", "origin1");
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
		assertSame(ConflictKind.PSEUDO, conflict.getKind());
	}

	@Test
	public void testC2UseCaseForReference() throws IOException {
		final Resource left = input.getC2ReferenceLeft();
		final Resource origin = input.getC2ReferenceOrigin();
		final Resource right = input.getC2ReferenceRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		/*
		 * We should have 5 differences here. An element has been deleted from the right side, all three of
		 * its reference's values have been deleted, and finally a value has been removed from that same
		 * reference on the left side. The deletion of a value into the left reference conflicts with the
		 * removal of the container from the right. It is also in pseudo-conflict with the deletion of this
		 * same value on the right.
		 */
		assertSame(Integer.valueOf(5), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftReferenceDiffDescription = removedFromReference(
				"root.conflictHolder", "multiValuedReference", "root.origin1");
		final Predicate<? super Diff> rightDeleteDiffDescription = removed("root.conflictHolder");
		final Predicate<? super Diff> rightReferenceDiff1Description = removedFromReference(
				"root.conflictHolder", "multiValuedReference", "root.origin1");
		final Predicate<? super Diff> rightReferenceDiff2Description = removedFromReference(
				"root.conflictHolder", "multiValuedReference", "root.origin2");
		final Predicate<? super Diff> rightReferenceDiff3Description = removedFromReference(
				"root.conflictHolder", "multiValuedReference", "root.origin3");

		final Diff leftReferenceDiff = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), leftReferenceDiffDescription));
		final Diff rightDeleteDiff = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), rightDeleteDiffDescription));
		final Diff rightReferenceDiff1 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), rightReferenceDiff1Description));
		final Diff rightReferenceDiff2 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), rightReferenceDiff2Description));
		final Diff rightReferenceDiff3 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), rightReferenceDiff3Description));

		assertNotNull(leftReferenceDiff);
		assertNotNull(rightDeleteDiff);
		assertNotNull(rightReferenceDiff1);
		assertNotNull(rightReferenceDiff2);
		assertNotNull(rightReferenceDiff3);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(3), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftReferenceDiff));
		assertTrue(conflictDiff.contains(rightDeleteDiff));
		assertTrue(conflictDiff.contains(rightReferenceDiff1));
		assertSame(ConflictKind.PSEUDO, conflict.getKind());
	}

	@Test
	public void testC3UseCaseForAttribute() throws IOException {
		final Resource left = input.getC3AttributeLeft();
		final Resource origin = input.getC3AttributeOrigin();
		final Resource right = input.getC3AttributeRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = addedToAttribute("root.conflictHolder",
				"multiValuedAttribute", "left1");
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
	public void testC3UseCaseForReference() throws IOException {
		final Resource left = input.getC3ReferenceLeft();
		final Resource origin = input.getC3ReferenceOrigin();
		final Resource right = input.getC3ReferenceRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftReferenceDiffDescription = addedToReference("root.conflictHolder",
				"multiValuedReference", "root.left1");
		final Predicate<? super Diff> rightDeleteDiffDescription = removed("root.conflictHolder");

		final Diff leftReferenceDiff = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), leftReferenceDiffDescription));
		final Diff rightDeleteDiff = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), rightDeleteDiffDescription));

		assertNotNull(leftReferenceDiff);
		assertNotNull(rightDeleteDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(2), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftReferenceDiff));
		assertTrue(conflictDiff.contains(rightDeleteDiff));
		assertSame(ConflictKind.REAL, conflict.getKind());
	}

	@Test
	public void testC4UseCaseForAttribute() throws IOException {
		final Resource left = input.getC4AttributeLeft();
		final Resource origin = input.getC4AttributeOrigin();
		final Resource right = input.getC4AttributeRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		/*
		 * We expect 4 differences here. On the right side, an element has been deleted. On the left side, all
		 * three values of one of this element's features have been removed. All three diffs on the left are
		 * in conflict with the right diff.
		 */
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftAttributeDiff1Description = removedFromAttribute(
				"root.conflictHolder", "multiValuedAttribute", "origin1");
		final Predicate<? super Diff> leftAttributeDiff2Description = removedFromAttribute(
				"root.conflictHolder", "multiValuedAttribute", "origin2");
		final Predicate<? super Diff> leftAttributeDiff3Description = removedFromAttribute(
				"root.conflictHolder", "multiValuedAttribute", "origin3");
		final Predicate<? super Diff> rightDiffDescription = removed("root.conflictHolder");

		final Diff leftAttributeDiff1 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), leftAttributeDiff1Description));
		final Diff leftAttributeDiff2 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), leftAttributeDiff2Description));
		final Diff leftAttributeDiff3 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), leftAttributeDiff3Description));
		final Diff rightDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				rightDiffDescription));

		assertNotNull(leftAttributeDiff1);
		assertNotNull(leftAttributeDiff2);
		assertNotNull(leftAttributeDiff3);
		assertNotNull(rightDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftAttributeDiff1));
		assertTrue(conflictDiff.contains(leftAttributeDiff2));
		assertTrue(conflictDiff.contains(leftAttributeDiff3));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(ConflictKind.PSEUDO, conflict.getKind());
	}

	@Test
	public void testC4UseCaseForReference() throws IOException {
		final Resource left = input.getC4ReferenceLeft();
		final Resource origin = input.getC4ReferenceOrigin();
		final Resource right = input.getC4ReferenceRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		/*
		 * We expect 7 differences here. On the right, an element has been deleted. All three values of this
		 * element's reference have been removed. On the left, we've also removed all three values of that
		 * same reference. All 7 differences are in pseudo-conflict with each other.
		 */
		assertSame(Integer.valueOf(7), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> referenceDiff1Description = removedFromReference("root.conflictHolder",
				"multiValuedReference", "root.origin1");
		final Predicate<? super Diff> referenceDiff2Description = removedFromReference("root.conflictHolder",
				"multiValuedReference", "root.origin2");
		final Predicate<? super Diff> referenceDiff3Description = removedFromReference("root.conflictHolder",
				"multiValuedReference", "root.origin2");
		final Predicate<? super Diff> rightDeleteDiffDescription = removed("root.conflictHolder");

		final Diff leftReferenceDiff1 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), referenceDiff1Description));
		final Diff leftReferenceDiff2 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), referenceDiff2Description));
		final Diff leftReferenceDiff3 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), referenceDiff3Description));
		final Diff rightDeleteDiff = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), rightDeleteDiffDescription));
		final Diff rightReferenceDiff1 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), referenceDiff1Description));
		final Diff rightReferenceDiff2 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), referenceDiff2Description));
		final Diff rightReferenceDiff3 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), referenceDiff3Description));

		assertNotNull(leftReferenceDiff1);
		assertNotNull(leftReferenceDiff2);
		assertNotNull(leftReferenceDiff3);
		assertNotNull(rightDeleteDiff);
		assertNotNull(rightReferenceDiff1);
		assertNotNull(rightReferenceDiff2);
		assertNotNull(rightReferenceDiff3);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(7), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftReferenceDiff1));
		assertTrue(conflictDiff.contains(leftReferenceDiff2));
		assertTrue(conflictDiff.contains(leftReferenceDiff3));
		assertTrue(conflictDiff.contains(rightDeleteDiff));
		assertTrue(conflictDiff.contains(rightReferenceDiff1));
		assertTrue(conflictDiff.contains(rightReferenceDiff2));
		assertTrue(conflictDiff.contains(rightReferenceDiff3));
		assertSame(ConflictKind.PSEUDO, conflict.getKind());
	}

	@Test
	public void testC5UseCaseForAttribute() throws IOException {
		final Resource left = input.getC5AttributeLeft();
		final Resource origin = input.getC5AttributeOrigin();
		final Resource right = input.getC5AttributeRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = movedInAttribute("root.conflictHolder",
				"multiValuedAttribute", "origin1");
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
	public void testC5UseCaseForReference() throws IOException {
		final Resource left = input.getC5ReferenceLeft();
		final Resource origin = input.getC5ReferenceOrigin();
		final Resource right = input.getC5ReferenceRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		/*
		 * We expect five differences here. An element has been deleted from the right side, all three values
		 * of its reference have been deleted too. In the left, we've moved one of these values to another
		 * index. The MOVE conflicts with both the container deletion and the value's removal.
		 */
		assertSame(Integer.valueOf(5), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftReferenceDiffDescription = movedInReference("root.conflictHolder",
				"multiValuedReference", "root.origin1");
		final Predicate<? super Diff> rightDeleteDiffDescription = removed("root.conflictHolder");
		final Predicate<? super Diff> rightReferenceDiff1Description = removedFromReference(
				"root.conflictHolder", "multiValuedReference", "root.origin1");
		final Predicate<? super Diff> rightReferenceDiff2Description = removedFromReference(
				"root.conflictHolder", "multiValuedReference", "root.origin2");
		final Predicate<? super Diff> rightReferenceDiff3Description = removedFromReference(
				"root.conflictHolder", "multiValuedReference", "root.origin2");

		final Diff leftReferenceDiff = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), leftReferenceDiffDescription));
		final Diff rightDeleteDiff = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), rightDeleteDiffDescription));
		final Diff rightReferenceDiff1 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), rightReferenceDiff1Description));
		final Diff rightReferenceDiff2 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), rightReferenceDiff2Description));
		final Diff rightReferenceDiff3 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), rightReferenceDiff3Description));

		assertNotNull(leftReferenceDiff);
		assertNotNull(rightDeleteDiff);
		assertNotNull(rightReferenceDiff1);
		assertNotNull(rightReferenceDiff2);
		assertNotNull(rightReferenceDiff3);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertSame(Integer.valueOf(3), Integer.valueOf(conflictDiff.size()));
		assertTrue(conflictDiff.contains(leftReferenceDiff));
		assertTrue(conflictDiff.contains(rightDeleteDiff));
		assertTrue(conflictDiff.contains(rightReferenceDiff1));
		assertSame(ConflictKind.REAL, conflict.getKind());
	}

	@Test
	public void testD1UseCaseForAttribute() throws IOException {
		final Resource left = input.getD1AttributeLeft();
		final Resource origin = input.getD1AttributeOrigin();
		final Resource right = input.getD1AttributeRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = removedFromAttribute("root.conflictHolder",
				"multiValuedAttribute", "origin1");
		final Predicate<? super Diff> rightDiffDescription = movedInAttribute("root.conflictHolder",
				"multiValuedAttribute", "origin1");

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
	public void testD1UseCaseForReference() throws IOException {
		final Resource left = input.getD1ReferenceLeft();
		final Resource origin = input.getD1ReferenceOrigin();
		final Resource right = input.getD1ReferenceRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = removedFromReference("root.conflictHolder",
				"multiValuedReference", "root.origin1");
		final Predicate<? super Diff> rightDiffDescription = movedInReference("root.conflictHolder",
				"multiValuedReference", "root.origin1");

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
	public void testD2UseCaseForAttribute() throws IOException {
		final Resource left = input.getD2AttributeLeft();
		final Resource origin = input.getD2AttributeOrigin();
		final Resource right = input.getD2AttributeRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = movedInAttribute("root.conflictHolder",
				"multiValuedAttribute", "origin1");
		final Predicate<? super Diff> rightDiffDescription = removedFromAttribute("root.conflictHolder",
				"multiValuedAttribute", "origin1");

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
	public void testD2UseCaseForReference() throws IOException {
		final Resource left = input.getD2ReferenceLeft();
		final Resource origin = input.getD2ReferenceOrigin();
		final Resource right = input.getD2ReferenceRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = movedInReference("root.conflictHolder",
				"multiValuedReference", "root.origin1");
		final Predicate<? super Diff> rightDiffDescription = removedFromReference("root.conflictHolder",
				"multiValuedReference", "root.origin1");

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
	public void testD3UseCaseForAttribute() throws IOException {
		final Resource left = input.getD3AttributeLeft();
		final Resource origin = input.getD3AttributeOrigin();
		final Resource right = input.getD3AttributeRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = movedInAttribute("root.conflictHolder",
				"multiValuedAttribute", "origin1");
		final Predicate<? super Diff> rightDiffDescription = movedInAttribute("root.conflictHolder",
				"multiValuedAttribute", "origin1");

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
	public void testD3UseCaseForReference() throws IOException {
		final Resource left = input.getD3ReferenceLeft();
		final Resource origin = input.getD3ReferenceOrigin();
		final Resource right = input.getD3ReferenceRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = movedInReference("root.conflictHolder",
				"multiValuedReference", "root.origin1");
		final Predicate<? super Diff> rightDiffDescription = movedInReference("root.conflictHolder",
				"multiValuedReference", "root.origin1");

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
	public void testE1UseCase() throws IOException {
		final Resource left = input.getE1Left();
		final Resource origin = input.getE1Origin();
		final Resource right = input.getE1Right();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = changedReference("root.conflictHolder",
				"singleValuedReference", null, "root.origin");
		final Predicate<? super Diff> rightDiffDescription = removed("root.origin");

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
	public void testE2UseCase() throws IOException {
		final Resource left = input.getE2Left();
		final Resource origin = input.getE2Origin();
		final Resource right = input.getE2Right();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = addedToReference("root.conflictHolder",
				"multiValuedReference", "root.origin");
		final Predicate<? super Diff> rightDiffDescription = removed("root.origin");

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
	public void testFUseCase() throws IOException {
		final Resource left = input.getFLeft();
		final Resource origin = input.getFOrigin();
		final Resource right = input.getFRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = moved("root.conflictHolder.origin",
				"containmentRef2");
		final Predicate<? super Diff> rightDiffDescription = moved("root.conflictHolder.origin",
				"containmentRef3");

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
	public void testGUseCase() throws IOException {
		final Resource left = input.getGLeft();
		final Resource origin = input.getGOrigin();
		final Resource right = input.getGRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = moved("root.leftContainer.conflictNode",
				"containmentRef1");
		final Predicate<? super Diff> rightDiffDescription = moved("root.rightContainer.conflictNode",
				"containmentRef1");

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
	public void testH1UseCase() throws IOException {
		final Resource left = input.getH1Left();
		final Resource origin = input.getH1Origin();
		final Resource right = input.getH1Right();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = moved("root.left.conflictNode",
				"singleValueContainment");
		final Predicate<? super Diff> rightDiffDescription = removed("root.left");

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
	public void testH2UseCase() throws IOException {
		final Resource left = input.getH2Left();
		final Resource origin = input.getH2Origin();
		final Resource right = input.getH2Right();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = moved("root.leftContainer.conflictNode",
				"containmentRef1");
		final Predicate<? super Diff> rightDiffDescription = removed("root.leftContainer");

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
