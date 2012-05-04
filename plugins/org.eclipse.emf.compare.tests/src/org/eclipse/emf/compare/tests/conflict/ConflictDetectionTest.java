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
	}

	@Test
	public void testA1UseCaseForReference() throws IOException {
		final Resource left = input.getA1ReferenceLeft();
		final Resource origin = input.getA1ReferenceOrigin();
		final Resource right = input.getA1ReferenceRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = changedReference("root.conflictHolder",
				"singleValuedReference", "root.origin", "root.left");
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
	}

	@Test
	public void testA3UseCaseForReference() throws IOException {
		final Resource left = input.getA3ReferenceLeft();
		final Resource origin = input.getA3ReferenceOrigin();
		final Resource right = input.getA3ReferenceRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(conflicts.size()));

		final Predicate<? super Diff> leftDiffDescription = changedReference("root.conflictHolder",
				"singleValuedReference", "root.origin", null);
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
	}
}
