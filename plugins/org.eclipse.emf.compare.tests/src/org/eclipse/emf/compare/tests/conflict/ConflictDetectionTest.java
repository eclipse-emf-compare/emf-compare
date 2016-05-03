/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 479449
 *******************************************************************************/
package org.eclipse.emf.compare.tests.conflict;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;
import static org.eclipse.emf.compare.ConflictKind.PSEUDO;
import static org.eclipse.emf.compare.ConflictKind.REAL;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.moved;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.movedInAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.movedInReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onEObject;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.referenceValueMatch;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.conflict.data.ConflictInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.Test;

@SuppressWarnings("nls")
public class ConflictDetectionTest {

	/**
	 * This predicate can be used to check whether a given Diff represents the setting of a value in a
	 * single-valued reference going by {@code referenceName} on an EObject which name matches
	 * {@code qualifiedName}.
	 * <p>
	 * Note that in order for this to work, we expect the EObjects to have a "name" feature returning a String
	 * for us to compare it with the given qualified name.
	 * </p>
	 * <p>
	 * TODO this is only here to allow us to avoid changing API; should be moved to EMFComparePredicates
	 * </p>
	 * 
	 * @param qualifiedName
	 *            Qualified name of the EObject which we expect to present a ReferenceChange.
	 * @param referenceName
	 *            Name of the multi-valued reference on which we expect a change.
	 * @param addedQualifiedName
	 *            Qualified name of the EObject which we expect to have been added to this reference.
	 * @return The created predicate.
	 */
	@SuppressWarnings("unchecked")
	private static Predicate<? super Diff> setOfReference(final String qualifiedName,
			final String referenceName, final String addedQualifiedName) {
		// This is only meant for multi-valued references
		return and(ofKind(DifferenceKind.ADD), onEObject(qualifiedName), referenceValueMatch(referenceName,
				addedQualifiedName, false));
	}

	private ConflictInputData input = new ConflictInputData();

	@Test
	public void testA1UseCaseForAttribute() throws IOException {
		final Resource left = input.getA1AttributeLeft();
		final Resource origin = input.getA1AttributeOrigin();
		final Resource right = input.getA1AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testA1UseCaseForReference() throws IOException {
		final Resource left = input.getA1ReferenceLeft();
		final Resource origin = input.getA1ReferenceOrigin();
		final Resource right = input.getA1ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		/*
		 * We have three diffs here : an object has been deleted on the right side, its reference has been
		 * unset, and that same reference has been changed on the left to a new value. The two diffs on the
		 * right are in conflict with the only diff on the left.
		 */
		assertEquals(3, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(3, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftReferenceDiff));
		assertTrue(conflictDiff.contains(rightReferenceDiff));
		assertTrue(conflictDiff.contains(rightDeleteDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testA2UseCaseForAttribute() throws IOException {
		final Resource left = input.getA2AttributeLeft();
		final Resource origin = input.getA2AttributeOrigin();
		final Resource right = input.getA2AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testA2UseCaseForReference() throws IOException {
		final Resource left = input.getA2ReferenceLeft();
		final Resource origin = input.getA2ReferenceOrigin();
		final Resource right = input.getA2ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testA3UseCaseForAttribute() throws IOException {
		final Resource left = input.getA3AttributeLeft();
		final Resource origin = input.getA3AttributeOrigin();
		final Resource right = input.getA3AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(PSEUDO, conflict.getKind());
	}

	@Test
	public void testA3UseCaseForReference() throws IOException {
		final Resource left = input.getA3ReferenceLeft();
		final Resource origin = input.getA3ReferenceOrigin();
		final Resource right = input.getA3ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		/*
		 * We expect three differences here : an object has been deleted on the right, its reference has been
		 * unset, and that same reference has been unset on the left. All three diffs are in pseudo-conflict
		 * with each other.
		 */
		assertEquals(3, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(3, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftReferenceDiff));
		assertTrue(conflictDiff.contains(rightReferenceDiff));
		assertTrue(conflictDiff.contains(rightDeleteDiff));
		assertSame(PSEUDO, conflict.getKind());
	}

	@Test
	public void testB1UseCaseForAttribute() throws IOException {
		final Resource left = input.getB1AttributeLeft();
		final Resource origin = input.getB1AttributeOrigin();
		final Resource right = input.getB1AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testB1UseCaseForReference() throws IOException {
		final Resource left = input.getB1ReferenceLeft();
		final Resource origin = input.getB1ReferenceOrigin();
		final Resource right = input.getB1ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testB2UseCaseForAttribute() throws IOException {
		final Resource left = input.getB2AttributeLeft();
		final Resource origin = input.getB2AttributeOrigin();
		final Resource right = input.getB2AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testB2UseCaseForReference() throws IOException {
		final Resource left = input.getB2ReferenceLeft();
		final Resource origin = input.getB2ReferenceOrigin();
		final Resource right = input.getB2ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testB3UseCaseForAttribute() throws IOException {
		final Resource left = input.getB3AttributeLeft();
		final Resource origin = input.getB3AttributeOrigin();
		final Resource right = input.getB3AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testB3UseCaseForReference() throws IOException {
		final Resource left = input.getB3ReferenceLeft();
		final Resource origin = input.getB3ReferenceOrigin();
		final Resource right = input.getB3ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testB3UseCaseForContainmentReference() throws IOException {
		final Resource left = input.getB3ContainmentReferenceLeft();
		final Resource origin = input.getB3ContainmentReferenceOrigin();
		final Resource right = input.getB3ContainmentReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

		final Predicate<? super Diff> leftDiffDescription = setOfReference("root.conflictHolder",
				"singleValueContainment", "root.conflictHolder.newleft");
		final Predicate<? super Diff> rightDiffDescription = setOfReference("root.conflictHolder",
				"singleValueContainment", "root.conflictHolder.newright");

		final Diff leftDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiffDescription));
		final Diff rightDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				rightDiffDescription));

		assertNotNull(leftDiff);
		assertNotNull(rightDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testB4UseCaseForAttribute() throws IOException {
		final Resource left = input.getB4AttributeLeft();
		final Resource origin = input.getB4AttributeOrigin();
		final Resource right = input.getB4AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testB4UseCaseForReference() throws IOException {
		final Resource left = input.getB4ReferenceLeft();
		final Resource origin = input.getB4ReferenceOrigin();
		final Resource right = input.getB4ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testB5UseCaseForAttribute() throws IOException {
		final Resource left = input.getB5AttributeLeft();
		final Resource origin = input.getB5AttributeOrigin();
		final Resource right = input.getB5AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(PSEUDO, conflict.getKind());
	}

	@Test
	public void testB5UseCaseForReference() throws IOException {
		final Resource left = input.getB5ReferenceLeft();
		final Resource origin = input.getB5ReferenceOrigin();
		final Resource right = input.getB5ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(PSEUDO, conflict.getKind());
	}

	@Test
	public void testB6UseCaseForAttribute() throws IOException {
		final Resource left = input.getB6AttributeLeft();
		final Resource origin = input.getB6AttributeOrigin();
		final Resource right = input.getB6AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

		final Predicate<? super Diff> leftDiffDescription = changedAttribute("root.conflictHolder",
				"singleValuedAttribute", "origin", null);
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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(PSEUDO, conflict.getKind());
	}

	@Test
	public void testB6UseCaseForReference() throws IOException {
		final Resource left = input.getB6ReferenceLeft();
		final Resource origin = input.getB6ReferenceOrigin();
		final Resource right = input.getB6ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

		final Predicate<? super Diff> leftDiffDescription = changedReference("root.conflictHolder",
				"singleValuedReference", "root.origin", null);
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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(PSEUDO, conflict.getKind());
	}

	@Test
	public void testC1UseCaseForAttribute() throws IOException {
		final Resource left = input.getC1AttributeLeft();
		final Resource origin = input.getC1AttributeOrigin();
		final Resource right = input.getC1AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testC1UseCaseForReference() throws IOException {
		final Resource left = input.getC1ReferenceLeft();
		final Resource origin = input.getC1ReferenceOrigin();
		final Resource right = input.getC1ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		/*
		 * We should have 5 differences here. An element has been deleted from the right side, all three of
		 * its reference's values have been deleted, and finally a value has been added into that same
		 * reference on the left side. The addition of a value into the reference conflicts with the removal
		 * of the container from the right. The three other diffs do not conflict.
		 */
		assertEquals(5, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftReferenceDiff));
		assertTrue(conflictDiff.contains(rightDeleteDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testC2UseCaseForAttribute() throws IOException {
		final Resource left = input.getC2AttributeLeft();
		final Resource origin = input.getC2AttributeOrigin();
		final Resource right = input.getC2AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(PSEUDO, conflict.getKind());
	}

	@Test
	public void testC2UseCaseForReference() throws IOException {
		final Resource left = input.getC2ReferenceLeft();
		final Resource origin = input.getC2ReferenceOrigin();
		final Resource right = input.getC2ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		/*
		 * We should have 5 differences here. An element has been deleted from the right side, all three of
		 * its reference's values have been deleted, and finally a value has been removed from that same
		 * reference on the left side. The deletion of a value into the left reference conflicts with the
		 * removal of the container from the right. It is also in pseudo-conflict with the deletion of this
		 * same value on the right.
		 */
		assertEquals(5, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(3, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftReferenceDiff));
		assertTrue(conflictDiff.contains(rightDeleteDiff));
		assertTrue(conflictDiff.contains(rightReferenceDiff1));
		assertSame(PSEUDO, conflict.getKind());
	}

	@Test
	public void testC3UseCaseForAttribute() throws IOException {
		final Resource left = input.getC3AttributeLeft();
		final Resource origin = input.getC3AttributeOrigin();
		final Resource right = input.getC3AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testC3UseCaseForReference() throws IOException {
		final Resource left = input.getC3ReferenceLeft();
		final Resource origin = input.getC3ReferenceOrigin();
		final Resource right = input.getC3ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftReferenceDiff));
		assertTrue(conflictDiff.contains(rightDeleteDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testC4UseCaseForAttribute() throws IOException {
		final Resource left = input.getC4AttributeLeft();
		final Resource origin = input.getC4AttributeOrigin();
		final Resource right = input.getC4AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		/*
		 * We expect 4 differences here. On the right side, an element has been deleted. On the left side, all
		 * three values of one of this element's features have been removed. All three diffs on the left are
		 * in conflict with the right diff.
		 */
		assertEquals(4, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(4, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftAttributeDiff1));
		assertTrue(conflictDiff.contains(leftAttributeDiff2));
		assertTrue(conflictDiff.contains(leftAttributeDiff3));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(PSEUDO, conflict.getKind());
	}

	@Test
	public void testC4UseCaseForReference() throws IOException {
		final Resource left = input.getC4ReferenceLeft();
		final Resource origin = input.getC4ReferenceOrigin();
		final Resource right = input.getC4ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		/*
		 * We expect 7 differences here. On the right, an element has been deleted. All three values of this
		 * element's reference have been removed. On the left, we've also removed all three values of that
		 * same reference. All 7 differences are in pseudo-conflict with each other.
		 */
		assertEquals(7, differences.size());
		assertEquals(1, conflicts.size());

		final Predicate<? super Diff> referenceDiff1Description = removedFromReference("root.conflictHolder",
				"multiValuedReference", "root.origin1");
		final Predicate<? super Diff> referenceDiff2Description = removedFromReference("root.conflictHolder",
				"multiValuedReference", "root.origin2");
		final Predicate<? super Diff> referenceDiff3Description = removedFromReference("root.conflictHolder",
				"multiValuedReference", "root.origin3");
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
		assertEquals(7, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftReferenceDiff1));
		assertTrue(conflictDiff.contains(leftReferenceDiff2));
		assertTrue(conflictDiff.contains(leftReferenceDiff3));
		assertTrue(conflictDiff.contains(rightDeleteDiff));
		assertTrue(conflictDiff.contains(rightReferenceDiff1));
		assertTrue(conflictDiff.contains(rightReferenceDiff2));
		assertTrue(conflictDiff.contains(rightReferenceDiff3));
		assertSame(PSEUDO, conflict.getKind());
	}

	@Test
	public void testC5UseCaseForAttribute() throws IOException {
		final Resource left = input.getC5AttributeLeft();
		final Resource origin = input.getC5AttributeOrigin();
		final Resource right = input.getC5AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testC5UseCaseForReference() throws IOException {
		final Resource left = input.getC5ReferenceLeft();
		final Resource origin = input.getC5ReferenceOrigin();
		final Resource right = input.getC5ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		/*
		 * We expect five differences here. An element has been deleted from the right side, all three values
		 * of its reference have been deleted too. In the left, we've moved one of these values to another
		 * index. The MOVE conflicts with both the container deletion and the value's removal.
		 */
		assertEquals(5, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(3, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftReferenceDiff));
		assertTrue(conflictDiff.contains(rightDeleteDiff));
		assertTrue(conflictDiff.contains(rightReferenceDiff1));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testD1UseCaseForAttribute() throws IOException {
		final Resource left = input.getD1AttributeLeft();
		final Resource origin = input.getD1AttributeOrigin();
		final Resource right = input.getD1AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testD1UseCaseForReference() throws IOException {
		final Resource left = input.getD1ReferenceLeft();
		final Resource origin = input.getD1ReferenceOrigin();
		final Resource right = input.getD1ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testD2UseCaseForAttribute() throws IOException {
		final Resource left = input.getD2AttributeLeft();
		final Resource origin = input.getD2AttributeOrigin();
		final Resource right = input.getD2AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testD2UseCaseForReference() throws IOException {
		final Resource left = input.getD2ReferenceLeft();
		final Resource origin = input.getD2ReferenceOrigin();
		final Resource right = input.getD2ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testD3UseCaseForAttribute() throws IOException {
		final Resource left = input.getD3AttributeLeft();
		final Resource origin = input.getD3AttributeOrigin();
		final Resource right = input.getD3AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testD3UseCaseForReference() throws IOException {
		final Resource left = input.getD3ReferenceLeft();
		final Resource origin = input.getD3ReferenceOrigin();
		final Resource right = input.getD3ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testD4UseCaseForAttribute() throws IOException {
		final Resource left = input.getD4AttributeLeft();
		final Resource origin = input.getD4AttributeOrigin();
		final Resource right = input.getD4AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		/*
		 * We expect 6 differences here. On both right and left, we've emptied an attribute from its 3 values.
		 * This should give use 3 pseudo-conflict, each removal conflicting with its other side.
		 */
		assertEquals(6, differences.size());
		assertEquals(3, conflicts.size());

		final Predicate<? super Diff> AttributeDiff1Description = removedFromAttribute("root.conflictHolder",
				"multiValuedAttribute", "origin1");
		final Predicate<? super Diff> AttributeDiff2Description = removedFromAttribute("root.conflictHolder",
				"multiValuedAttribute", "origin2");
		final Predicate<? super Diff> AttributeDiff3Description = removedFromAttribute("root.conflictHolder",
				"multiValuedAttribute", "origin3");

		final Diff leftAttributeDiff1 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), AttributeDiff1Description));
		final Diff leftAttributeDiff2 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), AttributeDiff2Description));
		final Diff leftAttributeDiff3 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), AttributeDiff3Description));

		final Diff rightAttributeDiff1 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), AttributeDiff1Description));
		final Diff rightAttributeDiff2 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), AttributeDiff2Description));
		final Diff rightAttributeDiff3 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), AttributeDiff3Description));

		assertNotNull(leftAttributeDiff1);
		assertNotNull(leftAttributeDiff2);
		assertNotNull(leftAttributeDiff3);
		assertNotNull(rightAttributeDiff1);
		assertNotNull(rightAttributeDiff2);
		assertNotNull(rightAttributeDiff3);

		// We know we have three conflicts
		final Conflict conflict1 = conflicts.get(0);

		final List<Diff> conflictDiffs1 = conflict1.getDifferences();
		assertEquals(2, conflictDiffs1.size());
		assertTrue(conflictDiffs1.contains(leftAttributeDiff1));
		assertTrue(conflictDiffs1.contains(rightAttributeDiff1));
		assertSame(PSEUDO, conflict1.getKind());

		final Conflict conflict2 = conflicts.get(1);

		final List<Diff> conflictDiffs2 = conflict2.getDifferences();
		assertEquals(2, conflictDiffs2.size());
		assertTrue(conflictDiffs2.contains(leftAttributeDiff2));
		assertTrue(conflictDiffs2.contains(rightAttributeDiff2));
		assertSame(PSEUDO, conflict2.getKind());

		final Conflict conflict3 = conflicts.get(2);

		final List<Diff> conflictDiffs3 = conflict3.getDifferences();
		assertEquals(2, conflictDiffs3.size());
		assertTrue(conflictDiffs3.contains(leftAttributeDiff3));
		assertTrue(conflictDiffs3.contains(rightAttributeDiff3));
		assertSame(PSEUDO, conflict3.getKind());
	}

	@Test
	public void testD4UseCaseForReference() throws IOException {
		final Resource left = input.getD4ReferenceLeft();
		final Resource origin = input.getD4ReferenceOrigin();
		final Resource right = input.getD4ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		/*
		 * We expect 6 differences here. On both right and left, we've emptied a reference from its 3 values.
		 * This should give use 3 pseudo-conflict, each removal conflicting with its other side.
		 */
		assertEquals(6, differences.size());
		assertEquals(3, conflicts.size());

		final Predicate<? super Diff> referenceDiff1Description = removedFromReference("root.conflictHolder",
				"multiValuedReference", "root.origin1");
		final Predicate<? super Diff> referenceDiff2Description = removedFromReference("root.conflictHolder",
				"multiValuedReference", "root.origin2");
		final Predicate<? super Diff> referenceDiff3Description = removedFromReference("root.conflictHolder",
				"multiValuedReference", "root.origin3");

		final Diff leftReferenceDiff1 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), referenceDiff1Description));
		final Diff leftReferenceDiff2 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), referenceDiff2Description));
		final Diff leftReferenceDiff3 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), referenceDiff3Description));

		final Diff rightReferenceDiff1 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), referenceDiff1Description));
		final Diff rightReferenceDiff2 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), referenceDiff2Description));
		final Diff rightReferenceDiff3 = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), referenceDiff3Description));

		assertNotNull(leftReferenceDiff1);
		assertNotNull(leftReferenceDiff2);
		assertNotNull(leftReferenceDiff3);
		assertNotNull(rightReferenceDiff1);
		assertNotNull(rightReferenceDiff2);
		assertNotNull(rightReferenceDiff3);

		// We know we have three conflicts
		final Conflict conflict1 = conflicts.get(0);

		final List<Diff> conflictDiffs1 = conflict1.getDifferences();
		assertEquals(2, conflictDiffs1.size());
		assertTrue(conflictDiffs1.contains(leftReferenceDiff1));
		assertTrue(conflictDiffs1.contains(rightReferenceDiff1));
		assertSame(PSEUDO, conflict1.getKind());

		final Conflict conflict2 = conflicts.get(1);

		final List<Diff> conflictDiffs2 = conflict2.getDifferences();
		assertEquals(2, conflictDiffs2.size());
		assertTrue(conflictDiffs2.contains(leftReferenceDiff2));
		assertTrue(conflictDiffs2.contains(rightReferenceDiff2));
		assertSame(PSEUDO, conflict2.getKind());

		final Conflict conflict3 = conflicts.get(2);

		final List<Diff> conflictDiffs3 = conflict3.getDifferences();
		assertEquals(2, conflictDiffs3.size());
		assertTrue(conflictDiffs3.contains(leftReferenceDiff3));
		assertTrue(conflictDiffs3.contains(rightReferenceDiff3));
		assertSame(PSEUDO, conflict3.getKind());
	}

	@Test
	public void testD5UseCaseForAttribute() throws IOException {
		final Resource left = input.getD5AttributeLeft();
		final Resource origin = input.getD5AttributeOrigin();
		final Resource right = input.getD5AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

		final Predicate<? super Diff> diffDescription = removedFromAttribute("root.conflictHolder",
				"multiValuedAttribute", "origin1");

		final Diff leftDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				diffDescription));
		final Diff rightDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				diffDescription));

		assertNotNull(leftDiff);
		assertNotNull(rightDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(PSEUDO, conflict.getKind());
	}

	@Test
	public void testD5UseCaseForReference() throws IOException {
		final Resource left = input.getD5ReferenceLeft();
		final Resource origin = input.getD5ReferenceOrigin();
		final Resource right = input.getD5ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

		final Predicate<? super Diff> diffDescription = removedFromReference("root.conflictHolder",
				"multiValuedReference", "root.origin1");

		final Diff leftDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				diffDescription));
		final Diff rightDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				diffDescription));

		assertNotNull(leftDiff);
		assertNotNull(rightDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(PSEUDO, conflict.getKind());
	}

	@Test
	public void testD6UseCaseForAttribute() throws IOException {
		final Resource left = input.getD6AttributeLeft();
		final Resource origin = input.getD6AttributeOrigin();
		final Resource right = input.getD6AttributeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

		final Predicate<? super Diff> diffDescription = movedInAttribute("root.conflictHolder",
				"multiValuedAttribute", "origin1");

		final Diff leftDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				diffDescription));
		final Diff rightDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				diffDescription));

		assertNotNull(leftDiff);
		assertNotNull(rightDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(PSEUDO, conflict.getKind());
	}

	@Test
	public void testD6UseCaseForReference() throws IOException {
		final Resource left = input.getD6ReferenceLeft();
		final Resource origin = input.getD6ReferenceOrigin();
		final Resource right = input.getD6ReferenceRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

		final Predicate<? super Diff> diffDescription = movedInReference("root.conflictHolder",
				"multiValuedReference", "root.origin1");

		final Diff leftDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				diffDescription));
		final Diff rightDiff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				diffDescription));

		assertNotNull(leftDiff);
		assertNotNull(rightDiff);

		// We know there's only one conflict
		final Conflict conflict = conflicts.get(0);

		final List<Diff> conflictDiff = conflict.getDifferences();
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(PSEUDO, conflict.getKind());
	}

	@Test
	public void testE1UseCase() throws IOException {
		final Resource left = input.getE1Left();
		final Resource origin = input.getE1Origin();
		final Resource right = input.getE1Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testE2UseCase() throws IOException {
		final Resource left = input.getE2Left();
		final Resource origin = input.getE2Origin();
		final Resource right = input.getE2Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testFUseCase() throws IOException {
		final Resource left = input.getFLeft();
		final Resource origin = input.getFOrigin();
		final Resource right = input.getFRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testGUseCase() throws IOException {
		final Resource left = input.getGLeft();
		final Resource origin = input.getGOrigin();
		final Resource right = input.getGRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testH1UseCase() throws IOException {
		final Resource left = input.getH1Left();
		final Resource origin = input.getH1Origin();
		final Resource right = input.getH1Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testH2UseCase() throws IOException {
		final Resource left = input.getH2Left();
		final Resource origin = input.getH2Origin();
		final Resource right = input.getH2Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// We should have no less and no more than 2 differences, composing a single conflict
		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());

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
		assertEquals(2, conflictDiff.size());
		assertTrue(conflictDiff.contains(leftDiff));
		assertTrue(conflictDiff.contains(rightDiff));
		assertSame(REAL, conflict.getKind());
	}

	@Test
	public void testIUseCase() throws IOException {
		final Resource left = input.getILeft();
		final Resource origin = input.getIOrigin();
		final Resource right = input.getIRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		assertEquals(5, differences.size());
		assertEquals(1, conflicts.size());

		Conflict soleConflict = conflicts.get(0);
		assertSame(PSEUDO, soleConflict.getKind());
	}

	@Test
	public void testJUseCase() throws IOException {
		final Resource left = input.getJLeft();
		final Resource origin = input.getJOrigin();
		final Resource right = input.getJRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		assertEquals(5, differences.size());
		assertEquals(1, conflicts.size());

		Conflict soleConflict = conflicts.get(0);
		assertSame(PSEUDO, soleConflict.getKind());
	}

	@Test
	public void testK1UseCase() throws IOException {
		final Resource left = input.getK1Left();
		final Resource origin = input.getK1Origin();
		final Resource right = input.getK1Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		assertEquals(6, differences.size());
		assertEquals(2, conflicts.size());

		for (Conflict conflict : conflicts) {
			assertSame(REAL, conflict.getKind());
		}
	}

	@Test
	public void testK2UseCase() throws IOException {
		final Resource left = input.getK2Left();
		final Resource origin = input.getK2Origin();
		final Resource right = input.getK2Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		assertEquals(8, differences.size());
		assertEquals(3, conflicts.size());

		for (Conflict conflict : conflicts) {
			assertSame(REAL, conflict.getKind());
		}
	}

	@Test
	public void testK3UseCase() throws IOException {
		final Resource left = input.getK3Left();
		final Resource origin = input.getK3Origin();
		final Resource right = input.getK3Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		assertEquals(6, differences.size());
		assertEquals(3, conflicts.size());

		for (Conflict conflict : conflicts) {
			assertSame(REAL, conflict.getKind());
		}
	}

	@Test
	public void testK4UseCase() throws IOException {
		final Resource left = input.getK4Left();
		final Resource origin = input.getK4Origin();
		final Resource right = input.getK4Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		assertEquals(4, differences.size());
		assertEquals(2, conflicts.size());

		assertEquals(PSEUDO, conflicts.get(0).getKind());
		assertEquals(REAL, conflicts.get(1).getKind());

	}

	@Test
	public void testComplexUseCase() throws IOException {
		final Resource left = input.getComplexLeft();
		final Resource origin = input.getComplexOrigin();
		final Resource right = input.getComplexRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		/*
		 * This use case features 12 distinct differences of all types, adding up to 4 real conflict and 1
		 * pseudo conflicts.
		 */
		// 1 - Left : Node8 added
		// 2 - Left : Node9 added
		// 3 - Left : Node1 moved
		// 4 - Left : Node0 added
		// 5 - Left : Node5 removed
		// 6 - Left : Node6 removed
		// 7 - Left : Node7 removed

		// 8 - Right : Node6 moved
		// 9 - Right : Node9 added
		// 10 - Right : Node0 added
		// 11 - Right : Node1 moved
		// 12 - Right : Node5 removed

		// Real conflict : 6 and 8 (Moving and deleting the same value)
		// Real conflict : 2 and 9 (Adding the same value at different indices)
		// Real conflict : 4 and 10 (Adding the same value at different indices)
		// Real conflict : 3 and 11 (Moving the same value to different indices)

		// Pseudo conflict : 5 and 12 (Removing the same value on both sides)

		assertEquals(12, differences.size());
		assertEquals(5, conflicts.size());

		final Predicate<? super Diff> leftDiff1Description = added("Root.Node8");
		final Predicate<? super Diff> leftDiff2Description = added("Root.Node9");
		final Predicate<? super Diff> leftDiff3Description = moved("Root.Node1", "containmentRef1");
		final Predicate<? super Diff> leftDiff4Description = added("Root.Node0");
		final Predicate<? super Diff> leftDiff5Description = removed("Root.Node5");
		final Predicate<? super Diff> leftDiff6Description = removed("Root.Node6");
		final Predicate<? super Diff> leftDiff7Description = removed("Root.Node7");

		final Predicate<? super Diff> rightDiff8Description = moved("Root.Node6", "containmentRef1");
		final Predicate<? super Diff> rightDiff9Description = added("Root.Node9");
		final Predicate<? super Diff> rightDiff10Description = added("Root.Node0");
		final Predicate<? super Diff> rightDiff11Description = moved("Root.Node1", "containmentRef1");
		final Predicate<? super Diff> rightDiff12Description = removed("Root.Node5");

		final Diff leftDiff1 = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiff1Description));
		final Diff leftDiff2 = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiff2Description));
		final Diff leftDiff3 = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiff3Description));
		final Diff leftDiff4 = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiff4Description));
		final Diff leftDiff5 = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiff5Description));
		final Diff leftDiff6 = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiff6Description));
		final Diff leftDiff7 = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				leftDiff7Description));

		final Diff rightDiff8 = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				rightDiff8Description));
		final Diff rightDiff9 = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				rightDiff9Description));
		final Diff rightDiff10 = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				rightDiff10Description));
		final Diff rightDiff11 = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				rightDiff11Description));
		final Diff rightDiff12 = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				rightDiff12Description));

		assertNotNull(leftDiff1);
		assertNotNull(leftDiff2);
		assertNotNull(leftDiff3);
		assertNotNull(leftDiff4);
		assertNotNull(leftDiff5);
		assertNotNull(leftDiff6);
		assertNotNull(leftDiff7);

		assertNotNull(rightDiff8);
		assertNotNull(rightDiff9);
		assertNotNull(rightDiff10);
		assertNotNull(rightDiff11);
		assertNotNull(rightDiff12);

		// We know there are 5 conflicts here
		for (Conflict conflict : conflicts) {
			final List<Diff> conflictDiffs = conflict.getDifferences();
			assertEquals(2, conflictDiffs.size());
			if (conflictDiffs.contains(leftDiff6)) {
				assertTrue(conflictDiffs.contains(rightDiff8));
				assertSame(REAL, conflict.getKind());
			} else if (conflictDiffs.contains(leftDiff2)) {
				assertTrue(conflictDiffs.contains(rightDiff9));
				assertSame(REAL, conflict.getKind());
			} else if (conflictDiffs.contains(leftDiff4)) {
				assertTrue(conflictDiffs.contains(rightDiff10));
				assertSame(REAL, conflict.getKind());
			} else if (conflictDiffs.contains(leftDiff3)) {
				assertTrue(conflictDiffs.contains(rightDiff11));
				assertSame(REAL, conflict.getKind());
			} else if (conflictDiffs.contains(leftDiff5)) {
				assertTrue(conflictDiffs.contains(rightDiff12));
				assertSame(PSEUDO, conflict.getKind());
			} else {
				fail("unexpected conflict");
			}
		}
	}

	/**
	 * Make sure that and RAC:DELETE is not in conflict with a change on its EObject if this EObject had an
	 * eContainer in the ancestor (i.e. when there is a ReferenceChange:DLETE of a containment reference to
	 * conflict with)
	 */
	@Test
	public void testDanglingRACTest() throws Exception {
		ResourceSet rsAncestor = input.getRACDanglingConflictAncestorModel();
		ResourceSet rsLeft = input.getRACDanglingConflictLeftModel();
		ResourceSet rsRight = input.getRACDanglingConflictRightModel();

		IComparisonScope scope = new DefaultComparisonScope(rsLeft, rsRight, rsAncestor);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		List<Conflict> conflicts = comparison.getConflicts();

		assertEquals(5, differences.size());
		assertEquals(2, conflicts.size());
		assertEquals(4, Iterables.size(Iterables.filter(differences, hasConflict(REAL))));
		Iterable<Diff> nonConflictingDiffs = Iterables.filter(differences, not(hasConflict(REAL, PSEUDO)));
		assertEquals(1, Iterables.size(nonConflictingDiffs));
		Diff nonConflictingDiff = nonConflictingDiffs.iterator().next();
		assertTrue(nonConflictingDiff instanceof ResourceAttachmentChange);
		assertEquals(DifferenceSource.RIGHT, nonConflictingDiff.getSource());
		assertTrue(((ResourceAttachmentChange)nonConflictingDiff).getResourceURI().endsWith("fragment.nodes"));
	}
}
