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
package org.eclipse.emf.compare.tests.merge;

import static com.google.common.base.Predicates.and;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.moved;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.tests.conflict.data.ConflictInputData;
import org.eclipse.emf.compare.tests.equi.data.EquiInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

@SuppressWarnings("nls")
public class IndividualMergeTest {
	// We'll use input from various other tests
	private ConflictInputData conflictInput = new ConflictInputData();

	private EquiInputData equivalenceInput = new EquiInputData();

	@Test
	public void testComplexUseCaseLtoR1() throws IOException {
		final Resource left = conflictInput.getComplexLeft();
		final Resource origin = conflictInput.getComplexOrigin();
		final Resource right = conflictInput.getComplexRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		/*
		 * This use case features 12 distinct differences of all types, adding up to 3 real conflict and 2
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

		// Pseudo conflict : 3 and 11 (Moving the same value to the same index on both sides)
		// Pseudo conflict : 5 and 12 (Removing the same value on both sides)

		// For reference
		// "original" is : {Node1, Node2, Node3, Node4, Node5, Node6, Node7}
		// "left" is : {Node8, Node9, Node2, Node3, Node4, Node1, Node0}
		// "right" is : {Node6, Node2, Node9, Node3, Node0, Node1, Node4, Node7}

		// Merge all, left to right, in order. Resolve conflicts by taking left side.

		// merge 1 (add Node8)
		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node8")));
		// LCS is currently {2, 3, 4}. Insertion index is right before 2.
		diff1.copyLeftToRight();
		assertValueIndexIs(diff1, false, 1);

		// merge 2 (add Node9). Since there is a conflict, merge 9 right beforehand
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node9")));
		final ReferenceChange diff9 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), added("Root.Node9")));
		// Revert addition of Node9 in right
		diff9.copyLeftToRight();
		// LCS is now {8, 2, 3, 4}. Insertion should be right after 8
		diff2.copyLeftToRight();
		assertValueIndexIs(diff2, false, 2);

		// merge 3 (move Node1). Since there is a conflict, merge 11 beforehand
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), moved("Root.Node1", "containmentRef1")));
		final ReferenceChange diff11 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), moved("Root.Node1", "containmentRef1")));
		// revert move of Node 1 in right. It should be re-positioned right before 2
		diff11.copyLeftToRight();
		assertValueIndexIs(diff11, false, 3);
		// LCS is {8, 9, 2, 3, 4}. 1 should be moved right after 4.
		diff3.copyLeftToRight();
		assertValueIndexIs(diff3, false, 7);

		// merge 4 (add Node0). There is a conflict. Merge 10 beforehand.
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node0")));
		final ReferenceChange diff10 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), added("Root.Node0")));
		// revert addition of 0 in right
		diff10.copyLeftToRight();
		// LCS is now {8, 9, 2, 3, 4, 1}. 0 should be added right after 1
		diff4.copyLeftToRight();
		assertValueIndexIs(diff4, false, 7);

		// merge 5 (remove Node5). There is a conflict, but it is a pseudo-conflict.
		// These diffs won't even be presented to the user, but let's merge them nonetheless.
		final ReferenceChange diff5 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node5")));
		final ReferenceChange diff12 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), removed("Root.Node5")));
		diff12.copyLeftToRight();
		assertValueIndexIs(diff12, false, 6);
		diff5.copyLeftToRight();
		assertValueIndexIs(diff5, false, -1);

		// merge 6 (remove Node6). There is a conflict. Merge 8 beforehand.
		final ReferenceChange diff6 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node6")));
		final ReferenceChange diff8 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), moved("Root.Node6", "containmentRef1")));
		// Revert move of 6 in right.
		diff8.copyLeftToRight();
		assertValueIndexIs(diff8, false, 5);
		diff6.copyLeftToRight();
		assertValueIndexIs(diff6, false, -1);

		// merge 7 (remove Node7)
		final ReferenceChange diff7 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node7")));
		diff7.copyLeftToRight();
		assertValueIndexIs(diff7, false, -1);

		// Left and Right should now be equal
		final EObject leftContainer = diff7.getMatch().getLeft();
		final EObject rightContainer = diff7.getMatch().getRight();
		final List<EObject> leftContents = getAsList(leftContainer, diff7.getReference());
		final List<EObject> rightContents = getAsList(rightContainer, diff7.getReference());
		assertEqualContents(comparison, leftContents, rightContents);
	}

	@Test
	public void testComplexUseCaseLtoR2() throws IOException {
		final Resource left = conflictInput.getComplexLeft();
		final Resource origin = conflictInput.getComplexOrigin();
		final Resource right = conflictInput.getComplexRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();

		// See description of the changes in #testComplexUseCaseLtoR1
		// Merge all, left to right, in arbitrary order. Resolve conflicts by taking left side.

		// merge 3 (move Node1). Since there is a conflict, merge 11 beforehand
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), moved("Root.Node1", "containmentRef1")));
		final ReferenceChange diff11 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), moved("Root.Node1", "containmentRef1")));
		// revert move of Node 1 in right. It should be re-positioned right before 2
		diff11.copyLeftToRight();
		assertValueIndexIs(diff11, false, 1);
		// Merge move of 1. Should be moved right after 4.
		diff3.copyLeftToRight();
		assertValueIndexIs(diff3, false, 6);

		// merge 6 (add Node6). There is a conflict. Merge 8 beforehand.
		final ReferenceChange diff6 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node6")));
		final ReferenceChange diff8 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), moved("Root.Node6", "containmentRef1")));
		// Revert move of 6 in right.
		diff8.copyLeftToRight();
		assertValueIndexIs(diff8, false, 5);
		diff6.copyLeftToRight();
		assertValueIndexIs(diff6, false, -1);

		// merge 7 (remove Node7)
		final ReferenceChange diff7 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node7")));
		diff7.copyLeftToRight();
		assertValueIndexIs(diff7, false, -1);

		// merge 4 (add Node0). There is a conflict. Merge 10 beforehand.
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node0")));
		final ReferenceChange diff10 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), added("Root.Node0")));
		// revert addition of 0 in right
		diff10.copyLeftToRight();
		assertValueIndexIs(diff10, false, -1);
		diff4.copyLeftToRight();
		assertValueIndexIs(diff4, false, 5);

		// merge 1 (add Node8)
		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node8")));
		diff1.copyLeftToRight();
		assertValueIndexIs(diff1, false, 0);

		// merge 2 (add Node9). Since there is a conflict, merge 9 right beforehand
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node9")));
		final ReferenceChange diff9 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), added("Root.Node9")));
		// Revert addition of Node9 in right
		diff9.copyLeftToRight();
		assertValueIndexIs(diff9, false, -1);
		diff2.copyLeftToRight();
		assertValueIndexIs(diff2, false, 1);

		// merge 5 (remove Node5). There is a conflict, but it is a pseudo-conflict.
		final ReferenceChange diff5 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node5")));
		final ReferenceChange diff12 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), removed("Root.Node5")));
		// revert remove
		diff12.copyLeftToRight();
		assertValueIndexIs(diff12, false, 5);
		// apply remove
		diff5.copyLeftToRight();
		assertValueIndexIs(diff5, false, -1);

		// Left and Right should now be equal
		final EObject leftContainer = diff7.getMatch().getLeft();
		final EObject rightContainer = diff7.getMatch().getRight();
		final List<EObject> leftContents = getAsList(leftContainer, diff7.getReference());
		final List<EObject> rightContents = getAsList(rightContainer, diff7.getReference());
		assertEqualContents(comparison, leftContents, rightContents);
	}

	@Test
	public void testComplexUseCaseRtoL1() throws IOException {
		final Resource left = conflictInput.getComplexLeft();
		final Resource origin = conflictInput.getComplexOrigin();
		final Resource right = conflictInput.getComplexRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();

		// See description of the changes in #testComplexUseCaseLtoR1
		// Merge all, right to left, in order. Resolve conflicts by taking right side.

		// merge 8 (move Node6). There is a conflict. Merge 6 beforehand.
		final ReferenceChange diff6 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node6")));
		final ReferenceChange diff8 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), moved("Root.Node6", "containmentRef1")));
		// Revert remove of 6 in left.
		diff6.copyRightToLeft();
		assertValueIndexIs(diff6, true, 5);
		// apply the move in left
		diff8.copyRightToLeft();
		assertValueIndexIs(diff8, true, 2);

		// merge 9 (add Node9). Since there is a conflict, merge 2 right beforehand
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node9")));
		final ReferenceChange diff9 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), added("Root.Node9")));
		// Revert addition in left
		diff2.copyRightToLeft();
		assertValueIndexIs(diff2, true, -1);
		diff9.copyRightToLeft();
		assertValueIndexIs(diff9, true, 3);

		// merge 10 (add Node0). There is a conflict. Merge 4 beforehand.
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node0")));
		final ReferenceChange diff10 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), added("Root.Node0")));
		// Revert addition in left
		diff4.copyRightToLeft();
		assertValueIndexIs(diff4, true, -1);
		diff10.copyRightToLeft();
		assertValueIndexIs(diff10, true, 5);

		// merge 11 (move Node1). Since there is a conflict, merge 3 beforehand
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), moved("Root.Node1", "containmentRef1")));
		final ReferenceChange diff11 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), moved("Root.Node1", "containmentRef1")));
		// Revert move of 1 in left
		diff3.copyRightToLeft();
		assertValueIndexIs(diff3, true, 2);
		diff11.copyRightToLeft();
		assertValueIndexIs(diff11, true, 6);

		// merge 12 (remove Node5). Merge 5 beforehand.
		final ReferenceChange diff5 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node5")));
		final ReferenceChange diff12 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), removed("Root.Node5")));
		// revert remove in left
		diff5.copyRightToLeft();
		assertValueIndexIs(diff5, true, 8);
		diff12.copyRightToLeft();
		assertValueIndexIs(diff12, true, -1);

		// merge 1 (add Node8). This will remove Node8
		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node8")));
		diff1.copyRightToLeft();
		assertValueIndexIs(diff1, false, -1);

		// merge 7 (remove Node7). This will re-add Node7
		final ReferenceChange diff7 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node7")));
		diff7.copyRightToLeft();
		assertValueIndexIs(diff7, false, 7);

		// Left and Right should now be equal
		final EObject leftContainer = diff7.getMatch().getLeft();
		final EObject rightContainer = diff7.getMatch().getRight();
		final List<EObject> leftContents = getAsList(leftContainer, diff7.getReference());
		final List<EObject> rightContents = getAsList(rightContainer, diff7.getReference());
		// TODO invert left and right : for now, the Map in ComparisonSpec is not updated when Match change
		assertEqualContents(comparison, rightContents, leftContents);
	}

	@Test
	public void testComplexUseCaseRtoL2() throws IOException {
		final Resource left = conflictInput.getComplexLeft();
		final Resource origin = conflictInput.getComplexOrigin();
		final Resource right = conflictInput.getComplexRight();

		final Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();

		// "original" is : {Node1, Node2, Node3, Node4, Node5, Node6, Node7}
		// "left" is : {Node8, Node9, Node2, Node3, Node4, Node1, Node0}
		// "right" is : {Node6, Node2, Node9, Node3, Node0, Node1, Node4, Node7}

		// See description of the changes in #testComplexUseCaseLtoR1
		// Merge all, right to left, in arbitrary order. Resolve conflicts by taking right side.

		// merge 12 (remove Node5). Merge 5 beforehand.
		final ReferenceChange diff5 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node5")));
		final ReferenceChange diff12 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), removed("Root.Node5")));
		// revert remove in left
		diff5.copyRightToLeft();
		assertValueIndexIs(diff5, true, 5);
		diff12.copyRightToLeft();
		assertValueIndexIs(diff12, true, -1);

		// merge 10 (add Node0). There is a conflict. Merge 4 beforehand.
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node0")));
		final ReferenceChange diff10 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), added("Root.Node0")));
		// Revert addition in left
		diff4.copyRightToLeft();
		assertValueIndexIs(diff4, true, -1);
		diff10.copyRightToLeft();
		assertValueIndexIs(diff10, true, 4);

		// merge 7 (remove Node7). This will re-add Node7
		final ReferenceChange diff7 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node7")));
		diff7.copyRightToLeft();
		assertValueIndexIs(diff7, false, 7);

		// merge 9 (add Node9). Since there is a conflict, merge 2 right beforehand
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node9")));
		final ReferenceChange diff9 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), added("Root.Node9")));
		// Revert addition in left
		diff2.copyRightToLeft();
		assertValueIndexIs(diff2, true, -1);
		diff9.copyRightToLeft();
		assertValueIndexIs(diff9, true, 2);

		// merge 1 (add Node8). This will remove Node8
		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node8")));
		diff1.copyRightToLeft();
		assertValueIndexIs(diff1, false, -1);

		// merge 8 (move Node6). There is a conflict. Merge 6 beforehand.
		final ReferenceChange diff6 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node6")));
		final ReferenceChange diff8 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), moved("Root.Node6", "containmentRef1")));
		// Revert remove of 6 in left.
		diff6.copyRightToLeft();
		assertValueIndexIs(diff6, true, 5);
		// apply the move in left
		diff8.copyRightToLeft();
		assertValueIndexIs(diff8, true, 0);

		// merge 11 (move Node1). Since there is a conflict, merge 3 beforehand
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), moved("Root.Node1", "containmentRef1")));
		final ReferenceChange diff11 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), moved("Root.Node1", "containmentRef1")));
		// Revert move of 1 in left
		diff3.copyRightToLeft();
		assertValueIndexIs(diff3, true, 1);
		diff11.copyRightToLeft();
		assertValueIndexIs(diff11, true, 5);

		// Left and Right should now be equal
		final EObject leftContainer = diff7.getMatch().getLeft();
		final EObject rightContainer = diff7.getMatch().getRight();
		final List<EObject> leftContents = getAsList(leftContainer, diff7.getReference());
		final List<EObject> rightContents = getAsList(rightContainer, diff7.getReference());
		// TODO invert left and right : for now, the Map in ComparisonSpec is not updated when Match change
		assertEqualContents(comparison, rightContents, leftContents);
	}

	@Test
	public void testEquivalenceA1LtoR() throws IOException {
		final Resource left = equivalenceInput.getA1Left();
		final Resource right = equivalenceInput.getA1Right();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 6 differences, equivalent by pairs
		assertSame(Integer.valueOf(6), Integer.valueOf(differences.size()));

		// diff1 is equivalent to diff2
		// diff3 is equivalent to diff4
		// diff5 is equivalent to diff6

		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.A", "destination", null, "Requirements.B"));
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.B", "source", null, "Requirements.A"));
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.C", "destination", "Requirements.D"));
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.D", "source", null, "Requirements.C"));
		final ReferenceChange diff5 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.E", "destination", "Requirements.F"));
		final ReferenceChange diff6 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.F", "source", "Requirements.E"));

		diff1.copyLeftToRight();
		// Check that diff1 got properly merged
		assertMerged(comparison, diff1, false, false);
		// And validate that diff2 got merged as an equivalent diff
		assertMerged(comparison, diff2, false, false);

		diff3.copyLeftToRight();
		// Check that diff3 got properly merged
		assertMerged(comparison, diff3, false, false);
		// And validate that diff4 got merged as an equivalent diff
		assertMerged(comparison, diff4, false, false);

		diff5.copyLeftToRight();
		// Check that diff5 got properly merged
		assertMerged(comparison, diff5, false, false);
		// And validate that diff6 got merged as an equivalent diff
		assertMerged(comparison, diff6, false, false);

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testEquivalenceA1RtoL() throws IOException {
		final Resource left = equivalenceInput.getA1Left();
		final Resource right = equivalenceInput.getA1Right();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 6 differences, equivalent by pairs
		assertSame(Integer.valueOf(6), Integer.valueOf(differences.size()));

		// diff1 is equivalent to diff2
		// diff3 is equivalent to diff4
		// diff5 is equivalent to diff6

		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.A", "destination", null, "Requirements.B"));
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.B", "source", null, "Requirements.A"));
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.C", "destination", "Requirements.D"));
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.D", "source", null, "Requirements.C"));
		final ReferenceChange diff5 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.E", "destination", "Requirements.F"));
		final ReferenceChange diff6 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.F", "source", "Requirements.E"));

		diff1.copyRightToLeft();
		// Check that diff1 got properly merged (we're unsetting values)
		assertMerged(comparison, diff1, true, true);
		// And validate that diff2 got merged as an equivalent diff
		assertMerged(comparison, diff2, true, true);

		diff3.copyRightToLeft();
		assertMerged(comparison, diff3, true, true);
		assertMerged(comparison, diff4, true, true);

		diff5.copyRightToLeft();
		assertMerged(comparison, diff5, true, true);
		assertMerged(comparison, diff6, true, true);

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testEquivalenceA4LtoR() throws IOException {
		final Resource left = equivalenceInput.getA4Left();
		final Resource right = equivalenceInput.getA4Right();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 4 differences, equivalent by pairs
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		// diff1 is equivalent to diff2
		// diff3 is equivalent to diff4

		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.A", "destination", "Requirements.B"));
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.B", "source", "Requirements.A"));
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.B", "destination", "Requirements.A"));
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.A", "source", "Requirements.B"));

		diff1.copyLeftToRight();
		assertMerged(comparison, diff1, false, false);
		assertMerged(comparison, diff2, false, false);

		diff3.copyLeftToRight();
		assertMerged(comparison, diff3, false, false);
		assertMerged(comparison, diff4, false, false);

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testEquivalenceA4RtoL() throws IOException {
		final Resource left = equivalenceInput.getA4Left();
		final Resource right = equivalenceInput.getA4Right();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 4 differences, equivalent by pairs
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		// diff1 is equivalent to diff2
		// diff3 is equivalent to diff4

		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.A", "destination", "Requirements.B"));
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.B", "source", "Requirements.A"));
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.B", "destination", "Requirements.A"));
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.A", "source", "Requirements.B"));

		diff1.copyRightToLeft();
		assertMerged(comparison, diff1, true, true);
		assertMerged(comparison, diff2, true, true);

		diff3.copyRightToLeft();
		assertMerged(comparison, diff3, true, true);
		assertMerged(comparison, diff4, true, true);

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testEquivalenceA5LtoR() throws IOException {
		final Resource left = equivalenceInput.getA5Left();
		final Resource right = equivalenceInput.getA5Right();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 12 differences, 3 equivalent pairs, some dependencies
		assertSame(Integer.valueOf(12), Integer.valueOf(differences.size()));

		// diff1 is equivalent to diff2
		// diff3 is equivalent to diff4
		// diff5 is equivalent to diff6

		// diff1 depends on diff7 and diff 8
		// diff2 also depends on 7 and 8 (equivalent diffs have the same requires)
		// 3 and 4 both depend on 9 and 10
		// 5 and 6 both depend on 11 and 12
		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.A", "destination", null, "Requirements.B"));
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.B", "source", null, "Requirements.A"));
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.C", "destination", "Requirements.D"));
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.D", "source", null, "Requirements.C"));
		final ReferenceChange diff5 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.E", "destination", "Requirements.F"));
		final ReferenceChange diff6 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.F", "source", "Requirements.E"));

		final ReferenceChange diff7 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.A"));
		final ReferenceChange diff8 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.B"));
		final ReferenceChange diff9 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.C"));
		final ReferenceChange diff10 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.D"));
		final ReferenceChange diff11 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.E"));
		final ReferenceChange diff12 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.F"));

		diff1.copyLeftToRight();
		assertMerged(comparison, diff1, false, false);
		assertMerged(comparison, diff2, false, false);
		assertSame(DifferenceState.MERGED, diff7.getState());
		assertSame(DifferenceState.MERGED, diff8.getState());

		diff3.copyLeftToRight();
		assertMerged(comparison, diff3, false, false);
		assertMerged(comparison, diff4, false, false);
		assertSame(DifferenceState.MERGED, diff9.getState());
		assertSame(DifferenceState.MERGED, diff10.getState());

		diff5.copyLeftToRight();
		assertMerged(comparison, diff5, false, false);
		assertMerged(comparison, diff6, false, false);
		assertSame(DifferenceState.MERGED, diff11.getState());
		assertSame(DifferenceState.MERGED, diff12.getState());

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testEquivalenceA5RtoL() throws IOException {
		final Resource left = equivalenceInput.getA5Left();
		final Resource right = equivalenceInput.getA5Right();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 12 differences, 3 equivalent pairs, some dependencies
		assertSame(Integer.valueOf(12), Integer.valueOf(differences.size()));

		// diff1 is equivalent to diff2
		// diff3 is equivalent to diff4
		// diff5 is equivalent to diff6

		// diff1 depends on diff7 and diff 8
		// diff2 also depends on 7 and 8 (equivalent diffs have the same requires)
		// 3 and 4 both depend on 9 and 10
		// 5 and 6 both depend on 11 and 12
		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.A", "destination", null, "Requirements.B"));
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.B", "source", null, "Requirements.A"));
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.C", "destination", "Requirements.D"));
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.D", "source", null, "Requirements.C"));
		final ReferenceChange diff5 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.E", "destination", "Requirements.F"));
		final ReferenceChange diff6 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements.F", "source", "Requirements.E"));

		final ReferenceChange diff7 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.A"));
		final ReferenceChange diff8 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.B"));
		final ReferenceChange diff9 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.C"));
		final ReferenceChange diff10 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.D"));
		final ReferenceChange diff11 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.E"));
		final ReferenceChange diff12 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.F"));

		// Removing the link between A and B does not necessarily means removing A and B
		// The "required" diffs will ne be merged
		diff1.copyRightToLeft();
		assertMerged(comparison, diff1, true, true);
		assertMerged(comparison, diff2, true, true);
		assertSame(DifferenceState.UNRESOLVED, diff7.getState());
		assertSame(DifferenceState.UNRESOLVED, diff8.getState());

		diff3.copyRightToLeft();
		assertMerged(comparison, diff3, true, true);
		assertMerged(comparison, diff4, true, true);
		assertSame(DifferenceState.UNRESOLVED, diff9.getState());
		assertSame(DifferenceState.UNRESOLVED, diff10.getState());

		diff5.copyRightToLeft();
		assertMerged(comparison, diff5, true, true);
		assertMerged(comparison, diff6, true, true);
		assertSame(DifferenceState.UNRESOLVED, diff11.getState());
		assertSame(DifferenceState.UNRESOLVED, diff12.getState());

		// Merge the 6 remaining diffs
		diff7.copyRightToLeft();
		diff8.copyRightToLeft();
		diff9.copyRightToLeft();
		diff10.copyRightToLeft();
		diff11.copyRightToLeft();
		diff12.copyRightToLeft();

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testEquivalenceC2LtoR1() throws IOException {
		final Resource left = equivalenceInput.getC2Left();
		final Resource right = equivalenceInput.getC2Right();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 4 differences
		// 1 : added C
		// 2 : changed reference "destination" of A to C
		// 3 : unset reference "source" of B
		// 4 : changed reference "source" of C to A
		// 2, 3 and 4 are equivalent
		// 2 and 4 require 1
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.C"));
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.A", "destination", "Requirements.B", "Requirements.C"));
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.B", "source", "Requirements.A", null));
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.C", "source", null, "Requirements.A"));

		diff2.copyLeftToRight();
		assertSame(DifferenceState.MERGED, diff1.getState());
		assertMerged(comparison, diff2, false, false);
		assertMerged(comparison, diff3, true, false);
		assertMerged(comparison, diff4, false, false);

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testEquivalenceC2LtoR2() throws IOException {
		final Resource left = equivalenceInput.getC2Left();
		final Resource right = equivalenceInput.getC2Right();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 4 differences
		// 1 : added C
		// 2 : changed reference "destination" of A to C
		// 3 : unset reference "source" of B
		// 4 : changed reference "source" of C to A
		// 2, 3 and 4 are equivalent
		// 2 and 4 require 1
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.C"));
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.A", "destination", "Requirements.B", "Requirements.C"));
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.B", "source", "Requirements.A", null));
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.C", "source", null, "Requirements.A"));

		diff1.copyLeftToRight();
		assertSame(DifferenceState.MERGED, diff1.getState());
		assertSame(DifferenceState.UNRESOLVED, diff2.getState());
		assertSame(DifferenceState.UNRESOLVED, diff3.getState());
		assertSame(DifferenceState.UNRESOLVED, diff4.getState());

		diff2.copyLeftToRight();
		assertMerged(comparison, diff2, false, false);
		assertMerged(comparison, diff3, true, false);
		assertMerged(comparison, diff4, false, false);

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testEquivalenceC2RtoL() throws IOException {
		final Resource left = equivalenceInput.getC2Left();
		final Resource right = equivalenceInput.getC2Right();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 4 differences
		// 1 : added C
		// 2 : changed reference "destination" of A to C
		// 3 : unset reference "source" of B
		// 4 : changed reference "source" of C to A
		// 2, 3 and 4 are equivalent
		// 2 and 4 require 1
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.C"));
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.A", "destination", "Requirements.B", "Requirements.C"));
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.B", "source", "Requirements.A", null));
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.C", "source", null, "Requirements.A"));

		diff2.copyRightToLeft();
		assertSame(DifferenceState.UNRESOLVED, diff1.getState());
		assertMerged(comparison, diff3, false, true);
		assertMerged(comparison, diff4, true, true);
		/*
		 * Diff 2 is a little more complicated than the usual. We are on a mono-valued refrence, and the merge
		 * operation is actually resetting the value to its original state. We will need to check that.
		 */
		final EObject nodeA = diff2.getMatch().getLeft();
		final EObject nodeB = diff3.getMatch().getLeft();
		assertEquals("A", nodeA.eGet(nodeA.eClass().getEStructuralFeature("name")));
		assertEquals("B", nodeB.eGet(nodeB.eClass().getEStructuralFeature("name")));
		assertSame(nodeB, nodeA.eGet(diff2.getReference()));

		diff1.copyRightToLeft();

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testEquivalenceC4LtoR() throws IOException {
		final Resource left = equivalenceInput.getC4Left();
		final Resource right = equivalenceInput.getC4Right();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 5 differences
		// 1 : added C
		// 2 : removed B
		// 3 : changed reference "destination" of A to C
		// 4 : unset reference "source" of B
		// 5 : changed reference "source" of C to A
		// 3, 4 and 5 are equivalent
		// 2 requires 3 and 4
		// 3 and 5 require 1
		assertSame(Integer.valueOf(5), Integer.valueOf(differences.size()));

		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.C"));
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(),
				removedFromReference("Requirements", "containmentRef1", "Requirements.B"));
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.A", "destination", "Requirements.B", "Requirements.C"));
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.B", "source", "Requirements.A", null));
		final ReferenceChange diff5 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.C", "source", null, "Requirements.A"));

		diff1.copyLeftToRight();
		assertMerged(comparison, diff1, false, false);
		assertSame(DifferenceState.UNRESOLVED, diff2.getState());
		assertSame(DifferenceState.UNRESOLVED, diff3.getState());
		assertSame(DifferenceState.UNRESOLVED, diff4.getState());
		assertSame(DifferenceState.UNRESOLVED, diff5.getState());

		diff2.copyLeftToRight();
		// B has been deleted : unset element in right
		assertMerged(comparison, diff2, true, false);
		// Change A.destination from "B" to "C"
		assertMerged(comparison, diff3, false, false);
		// Unsetting B.source. B is no longer in either right or left. We'll have to manually check.
		assertSame(DifferenceState.MERGED, diff4.getState());
		assertNull(diff4.getMatch().getLeft());
		assertNull(diff4.getMatch().getRight());
		// 2 required 3, which is equivalent to 5. 5 should thus have been merged too
		assertMerged(comparison, diff5, false, false);

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testEquivalenceC4RtoL() throws IOException {
		final Resource left = equivalenceInput.getC4Left();
		final Resource right = equivalenceInput.getC4Right();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 5 differences
		// 1 : added C
		// 2 : removed B
		// 3 : changed reference "destination" of A to C
		// 4 : unset reference "source" of B
		// 5 : changed reference "source" of C to A
		// 3, 4 and 5 are equivalent
		// 2 requires 3 and 4
		// 3 and 5 require 1
		assertSame(Integer.valueOf(5), Integer.valueOf(differences.size()));

		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.C"));
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(),
				removedFromReference("Requirements", "containmentRef1", "Requirements.B"));
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.A", "destination", "Requirements.B", "Requirements.C"));
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.B", "source", "Requirements.A", null));
		final ReferenceChange diff5 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.C", "source", null, "Requirements.A"));

		// 1 is required by 3, which is required by 2 and equivalent to 4 and 5.
		// Resetting 1 should thus reset all other diffs.
		diff1.copyRightToLeft();
		assertMerged(comparison, diff1, true, true);
		assertMerged(comparison, diff2, false, true);

		// C has been removed, thus the value match of diff3 has neither left nor right.
		assertSame(DifferenceState.MERGED, diff3.getState());
		final EObject nodeA = diff3.getMatch().getLeft();
		final EObject nodeB = diff4.getMatch().getLeft();
		assertSame(nodeB, nodeA.eGet(diff3.getReference()));

		assertMerged(comparison, diff4, false, true);

		assertSame(DifferenceState.MERGED, diff5.getState());
		assertNull(diff5.getMatch().getLeft());
		assertNull(diff5.getMatch().getRight());

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	/**
	 * Ensures that the two given lists contain the same elements in the same order. The kind of list does not
	 * matter.
	 * 
	 * @param list1
	 *            First of the two lists to compare.
	 * @param list2
	 *            Second of the two lists to compare.
	 */
	private static <T extends EObject> void assertEqualContents(Comparison comparison, List<T> list1,
			List<T> list2) {
		final int size = list1.size();
		assertSame(Integer.valueOf(size), Integer.valueOf(list2.size()));

		for (int i = 0; i < size; i++) {
			final EObject eObject1 = list1.get(i);
			final EObject eObject2 = list2.get(i);
			final Match match = comparison.getMatch(eObject1);
			if (match.getLeft() == eObject1) {
				assertEquals(match.getRight(), eObject2);
			} else {
				assertEquals(match.getRight(), eObject1);
				assertEquals(match.getLeft(), eObject2);
			}
		}
	}

	/* NOTE : not meant for containment changes */
	private static void assertMerged(Comparison comparison, ReferenceChange referenceChange, boolean unset,
			boolean rightToLeft) {
		assertSame(referenceChange.getState(), DifferenceState.MERGED);

		final EObject container;
		if (rightToLeft) {
			container = referenceChange.getMatch().getLeft();
		} else {
			container = referenceChange.getMatch().getRight();
		}

		final EReference ref = referenceChange.getReference();
		final Match valueMatch = comparison.getMatch(referenceChange.getValue());
		if (ref.isMany()) {
			@SuppressWarnings("unchecked")
			final List<EObject> refValue = (List<EObject>)container.eGet(ref);
			if (rightToLeft && unset) {
				assertTrue(!refValue.contains(valueMatch.getLeft()));
			} else if (rightToLeft) {
				assertTrue(refValue.contains(valueMatch.getLeft()));
			} else if (unset) {
				assertTrue(!refValue.contains(valueMatch.getRight()));
			} else {
				assertTrue(refValue.contains(valueMatch.getRight()));
			}
		} else {
			final EObject refValue = (EObject)container.eGet(ref);
			if (unset) {
				assertSame(null, refValue);
			} else if (rightToLeft) {
				assertSame(valueMatch.getLeft(), refValue);
			} else {
				assertSame(valueMatch.getRight(), refValue);
			}
		}
	}

	private static void assertValueIndexIs(ReferenceChange diff, boolean rightToLeft, int expectedIndex) {
		final Match containerMatch = diff.getMatch();
		final Match valueMatch = containerMatch.getComparison().getMatch(diff.getValue());

		if (rightToLeft) {
			if (expectedIndex != -1) {
				assertNotNull(valueMatch.getLeft());
				assertSame(containerMatch.getLeft().eResource(), valueMatch.getLeft().eResource());
				final EObject addedToLeft = valueMatch.getLeft();
				final List<EObject> values = getAsList(containerMatch.getLeft(), diff.getReference());
				assertSame(Integer.valueOf(expectedIndex), Integer.valueOf(values.indexOf(addedToLeft)));
			} else {
				assertNull(valueMatch.getLeft());
			}
		} else {
			if (expectedIndex != -1) {
				assertNotNull(valueMatch.getRight());
				assertSame(containerMatch.getRight().eResource(), valueMatch.getRight().eResource());
				final EObject addedToRight = valueMatch.getRight();
				final List<EObject> values = getAsList(containerMatch.getRight(), diff.getReference());
				assertSame(Integer.valueOf(expectedIndex), Integer.valueOf(values.indexOf(addedToRight)));
			} else {
				assertNull(valueMatch.getRight());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static List<EObject> getAsList(EObject object, EReference feature) {
		if (object != null) {
			Object value = object.eGet(feature, false);
			final List<EObject> asList;
			if (value instanceof List) {
				asList = (List<EObject>)value;
			} else if (value instanceof Iterable) {
				asList = ImmutableList.copyOf((Iterable<EObject>)value);
			} else if (value != null) {
				asList = ImmutableList.of((EObject)value);
			} else {
				asList = Collections.emptyList();
			}
			return asList;
		}
		return Collections.emptyList();
	}
}
