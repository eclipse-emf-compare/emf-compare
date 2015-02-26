/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - bugs 441172, 452147, 460902 and 460923
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.moved;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.FeatureMapChange;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.conflict.data.ConflictInputData;
import org.eclipse.emf.compare.tests.equi.data.EquiInputData;
import org.eclipse.emf.compare.tests.fullcomparison.data.identifier.IdentifierMatchInputData;
import org.eclipse.emf.compare.tests.merge.data.TwoWayMergeInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Test;

@SuppressWarnings("nls")
public class MultipleMergeTest {
	// We'll use input from various other tests
	private ConflictInputData conflictInput = new ConflictInputData();

	private EquiInputData equivalenceInput = new EquiInputData();

	private TwoWayMergeInputData twoWayInput = new TwoWayMergeInputData();

	private IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	@Test
	public void testComplexUseCaseLtoR1() throws IOException {
		final Resource left = conflictInput.getComplexLeft();
		final Resource origin = conflictInput.getComplexOrigin();
		final Resource right = conflictInput.getComplexRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

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
		mergerRegistry.getHighestRankingMerger(diff1).copyLeftToRight(diff1, new BasicMonitor());
		assertValueIndexIs(diff1, false, 1);

		// merge 2 (add Node9). Since there is a conflict, merge 9 right beforehand
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node9")));
		final ReferenceChange diff9 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), added("Root.Node9")));
		// Revert addition of Node9 in right
		mergerRegistry.getHighestRankingMerger(diff9).copyLeftToRight(diff9, new BasicMonitor());
		// LCS is now {8, 2, 3, 4}. Insertion should be right after 8
		mergerRegistry.getHighestRankingMerger(diff2).copyLeftToRight(diff2, new BasicMonitor());
		assertValueIndexIs(diff2, false, 2);

		// merge 3 (move Node1). Since there is a conflict, merge 11 beforehand
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), moved("Root.Node1", "containmentRef1")));
		final ReferenceChange diff11 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), moved("Root.Node1", "containmentRef1")));
		// revert move of Node 1 in right. It should be re-positioned right before 2
		mergerRegistry.getHighestRankingMerger(diff11).copyLeftToRight(diff11, new BasicMonitor());
		assertValueIndexIs(diff11, false, 3);
		// LCS is {8, 9, 2, 3, 4}. 1 should be moved right after 4.
		mergerRegistry.getHighestRankingMerger(diff3).copyLeftToRight(diff3, new BasicMonitor());
		assertValueIndexIs(diff3, false, 7);

		// merge 4 (add Node0). There is a conflict. Merge 10 beforehand.
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node0")));
		final ReferenceChange diff10 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), added("Root.Node0")));
		// revert addition of 0 in right
		mergerRegistry.getHighestRankingMerger(diff10).copyLeftToRight(diff10, new BasicMonitor());
		// LCS is now {8, 9, 2, 3, 4, 1}. 0 should be added right after 1
		mergerRegistry.getHighestRankingMerger(diff4).copyLeftToRight(diff4, new BasicMonitor());
		assertValueIndexIs(diff4, false, 7);

		// merge 5 (remove Node5). There is a conflict, but it is a pseudo-conflict.
		// These diffs won't even be presented to the user, but let's merge them nonetheless.
		final ReferenceChange diff5 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node5")));
		final ReferenceChange diff12 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), removed("Root.Node5")));
		mergerRegistry.getHighestRankingMerger(diff12).copyLeftToRight(diff12, new BasicMonitor());
		assertValueIndexIs(diff12, false, -1);
		mergerRegistry.getHighestRankingMerger(diff5).copyLeftToRight(diff5, new BasicMonitor());
		assertValueIndexIs(diff5, false, -1);

		// merge 6 (remove Node6). There is a conflict. Merge 8 beforehand.
		final ReferenceChange diff6 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node6")));
		final ReferenceChange diff8 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), moved("Root.Node6", "containmentRef1")));
		// Revert move of 6 in right.
		mergerRegistry.getHighestRankingMerger(diff8).copyLeftToRight(diff8, new BasicMonitor());
		assertValueIndexIs(diff8, false, 5);
		mergerRegistry.getHighestRankingMerger(diff6).copyLeftToRight(diff6, new BasicMonitor());
		assertValueIndexIs(diff6, false, -1);

		// merge 7 (remove Node7)
		final ReferenceChange diff7 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node7")));
		mergerRegistry.getHighestRankingMerger(diff7).copyLeftToRight(diff7, new BasicMonitor());
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

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		// See description of the changes in #testComplexUseCaseLtoR1
		// Merge all, left to right, in arbitrary order. Resolve conflicts by taking left side.

		// merge 3 (move Node1). Since there is a conflict, merge 11 beforehand
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), moved("Root.Node1", "containmentRef1")));
		final ReferenceChange diff11 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), moved("Root.Node1", "containmentRef1")));
		// revert move of Node 1 in right. It should be re-positioned right before 2
		mergerRegistry.getHighestRankingMerger(diff11).copyLeftToRight(diff11, new BasicMonitor());
		assertValueIndexIs(diff11, false, 1);
		// Merge move of 1. Should be moved right after 4.
		mergerRegistry.getHighestRankingMerger(diff3).copyLeftToRight(diff3, new BasicMonitor());
		assertValueIndexIs(diff3, false, 6);

		// merge 6 (add Node6). There is a conflict. Merge 8 beforehand.
		final ReferenceChange diff6 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node6")));
		final ReferenceChange diff8 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), moved("Root.Node6", "containmentRef1")));
		// Revert move of 6 in right.
		mergerRegistry.getHighestRankingMerger(diff8).copyLeftToRight(diff8, new BasicMonitor());
		assertValueIndexIs(diff8, false, 5);
		mergerRegistry.getHighestRankingMerger(diff6).copyLeftToRight(diff6, new BasicMonitor());
		assertValueIndexIs(diff6, false, -1);

		// merge 7 (remove Node7)
		final ReferenceChange diff7 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node7")));
		mergerRegistry.getHighestRankingMerger(diff7).copyLeftToRight(diff7, new BasicMonitor());
		assertValueIndexIs(diff7, false, -1);

		// merge 4 (add Node0). There is a conflict. Merge 10 beforehand.
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node0")));
		final ReferenceChange diff10 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), added("Root.Node0")));
		// revert addition of 0 in right
		mergerRegistry.getHighestRankingMerger(diff10).copyLeftToRight(diff10, new BasicMonitor());
		assertValueIndexIs(diff10, false, -1);
		mergerRegistry.getHighestRankingMerger(diff4).copyLeftToRight(diff4, new BasicMonitor());
		assertValueIndexIs(diff4, false, 5);

		// merge 1 (add Node8)
		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node8")));
		mergerRegistry.getHighestRankingMerger(diff1).copyLeftToRight(diff1, new BasicMonitor());
		assertValueIndexIs(diff1, false, 0);

		// merge 2 (add Node9). Since there is a conflict, merge 9 right beforehand
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node9")));
		final ReferenceChange diff9 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), added("Root.Node9")));
		// Revert addition of Node9 in right
		mergerRegistry.getHighestRankingMerger(diff9).copyLeftToRight(diff9, new BasicMonitor());
		assertValueIndexIs(diff9, false, -1);
		mergerRegistry.getHighestRankingMerger(diff2).copyLeftToRight(diff2, new BasicMonitor());
		assertValueIndexIs(diff2, false, 1);

		// merge 5 (remove Node5). There is a conflict, but it is a pseudo-conflict.
		final ReferenceChange diff5 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node5")));
		final ReferenceChange diff12 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), removed("Root.Node5")));
		// revert remove
		mergerRegistry.getHighestRankingMerger(diff12).copyLeftToRight(diff12, new BasicMonitor());
		assertValueIndexIs(diff12, false, -1);
		// apply remove
		mergerRegistry.getHighestRankingMerger(diff5).copyLeftToRight(diff5, new BasicMonitor());
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

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		// See description of the changes in #testComplexUseCaseLtoR1
		// Merge all, right to left, in order. Resolve conflicts by taking right side.

		// merge 8 (move Node6). There is a conflict. Merge 6 beforehand.
		final ReferenceChange diff6 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node6")));
		final ReferenceChange diff8 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), moved("Root.Node6", "containmentRef1")));
		// Revert remove of 6 in left.
		mergerRegistry.getHighestRankingMerger(diff6).copyRightToLeft(diff6, new BasicMonitor());
		assertValueIndexIs(diff6, true, 5);
		// apply the move in left
		mergerRegistry.getHighestRankingMerger(diff8).copyRightToLeft(diff8, new BasicMonitor());
		assertValueIndexIs(diff8, true, 2);

		// merge 9 (add Node9). Since there is a conflict, merge 2 right beforehand
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node9")));
		final ReferenceChange diff9 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), added("Root.Node9")));
		// Revert addition in left
		mergerRegistry.getHighestRankingMerger(diff2).copyRightToLeft(diff2, new BasicMonitor());
		assertValueIndexIs(diff2, true, -1);
		mergerRegistry.getHighestRankingMerger(diff9).copyRightToLeft(diff9, new BasicMonitor());
		assertValueIndexIs(diff9, true, 3);

		// merge 10 (add Node0). There is a conflict. Merge 4 beforehand.
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node0")));
		final ReferenceChange diff10 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), added("Root.Node0")));
		// Revert addition in left
		mergerRegistry.getHighestRankingMerger(diff4).copyRightToLeft(diff4, new BasicMonitor());
		assertValueIndexIs(diff4, true, -1);
		mergerRegistry.getHighestRankingMerger(diff10).copyRightToLeft(diff10, new BasicMonitor());
		assertValueIndexIs(diff10, true, 5);

		// merge 11 (move Node1). Since there is a conflict, merge 3 beforehand
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), moved("Root.Node1", "containmentRef1")));
		final ReferenceChange diff11 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), moved("Root.Node1", "containmentRef1")));
		// Revert move of 1 in left
		mergerRegistry.getHighestRankingMerger(diff3).copyRightToLeft(diff3, new BasicMonitor());
		assertValueIndexIs(diff3, true, 2);
		mergerRegistry.getHighestRankingMerger(diff11).copyRightToLeft(diff11, new BasicMonitor());
		assertValueIndexIs(diff11, true, 6);

		// merge 12 (remove Node5). Merge 5 beforehand.
		final ReferenceChange diff5 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node5")));
		final ReferenceChange diff12 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), removed("Root.Node5")));
		// revert remove in left
		mergerRegistry.getHighestRankingMerger(diff5).copyRightToLeft(diff5, new BasicMonitor());
		assertValueIndexIs(diff5, true, -1);
		mergerRegistry.getHighestRankingMerger(diff12).copyRightToLeft(diff12, new BasicMonitor());
		assertValueIndexIs(diff12, true, -1);

		// merge 1 (add Node8). This will remove Node8
		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node8")));
		mergerRegistry.getHighestRankingMerger(diff1).copyRightToLeft(diff1, new BasicMonitor());
		assertValueIndexIs(diff1, false, -1);

		// merge 7 (remove Node7). This will re-add Node7
		final ReferenceChange diff7 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node7")));
		mergerRegistry.getHighestRankingMerger(diff7).copyRightToLeft(diff7, new BasicMonitor());
		assertValueIndexIs(diff7, false, 7);

		// Left and Right should now be equal
		final EObject leftContainer = diff7.getMatch().getLeft();
		final EObject rightContainer = diff7.getMatch().getRight();
		final List<EObject> leftContents = getAsList(leftContainer, diff7.getReference());
		final List<EObject> rightContents = getAsList(rightContainer, diff7.getReference());
		assertEqualContents(comparison, leftContents, rightContents);
	}

	@Test
	public void testComplexUseCaseRtoL2() throws IOException {
		final Resource left = conflictInput.getComplexLeft();
		final Resource origin = conflictInput.getComplexOrigin();
		final Resource right = conflictInput.getComplexRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

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
		mergerRegistry.getHighestRankingMerger(diff5).copyRightToLeft(diff5, new BasicMonitor());
		assertValueIndexIs(diff5, true, -1);
		mergerRegistry.getHighestRankingMerger(diff12).copyRightToLeft(diff12, new BasicMonitor());
		assertValueIndexIs(diff12, true, -1);

		// merge 10 (add Node0). There is a conflict. Merge 4 beforehand.
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node0")));
		final ReferenceChange diff10 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), added("Root.Node0")));
		// Revert addition in left
		mergerRegistry.getHighestRankingMerger(diff4).copyRightToLeft(diff4, new BasicMonitor());
		assertValueIndexIs(diff4, true, -1);
		mergerRegistry.getHighestRankingMerger(diff10).copyRightToLeft(diff10, new BasicMonitor());
		assertValueIndexIs(diff10, true, 4);

		// merge 7 (remove Node7). This will re-add Node7
		final ReferenceChange diff7 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node7")));
		mergerRegistry.getHighestRankingMerger(diff7).copyRightToLeft(diff7, new BasicMonitor());
		assertValueIndexIs(diff7, false, 7);

		// merge 9 (add Node9). Since there is a conflict, merge 2 right beforehand
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node9")));
		final ReferenceChange diff9 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), added("Root.Node9")));
		// Revert addition in left
		mergerRegistry.getHighestRankingMerger(diff2).copyRightToLeft(diff2, new BasicMonitor());
		assertValueIndexIs(diff2, true, -1);
		mergerRegistry.getHighestRankingMerger(diff9).copyRightToLeft(diff9, new BasicMonitor());
		assertValueIndexIs(diff9, true, 2);

		// merge 1 (add Node8). This will remove Node8
		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), added("Root.Node8")));
		mergerRegistry.getHighestRankingMerger(diff1).copyRightToLeft(diff1, new BasicMonitor());
		assertValueIndexIs(diff1, false, -1);

		// merge 8 (move Node6). There is a conflict. Merge 6 beforehand.
		final ReferenceChange diff6 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), removed("Root.Node6")));
		final ReferenceChange diff8 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), moved("Root.Node6", "containmentRef1")));
		// Revert remove of 6 in left.
		mergerRegistry.getHighestRankingMerger(diff6).copyRightToLeft(diff6, new BasicMonitor());
		assertValueIndexIs(diff6, true, 5);
		// apply the move in left
		mergerRegistry.getHighestRankingMerger(diff8).copyRightToLeft(diff8, new BasicMonitor());
		assertValueIndexIs(diff8, true, 0);

		// merge 11 (move Node1). Since there is a conflict, merge 3 beforehand
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), moved("Root.Node1", "containmentRef1")));
		final ReferenceChange diff11 = (ReferenceChange)Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), moved("Root.Node1", "containmentRef1")));
		// Revert move of 1 in left
		mergerRegistry.getHighestRankingMerger(diff3).copyRightToLeft(diff3, new BasicMonitor());
		assertValueIndexIs(diff3, true, 1);
		mergerRegistry.getHighestRankingMerger(diff11).copyRightToLeft(diff11, new BasicMonitor());
		assertValueIndexIs(diff11, true, 5);

		// Left and Right should now be equal
		final EObject leftContainer = diff7.getMatch().getLeft();
		final EObject rightContainer = diff7.getMatch().getRight();
		final List<EObject> leftContents = getAsList(leftContainer, diff7.getReference());
		final List<EObject> rightContents = getAsList(rightContainer, diff7.getReference());
		assertEqualContents(comparison, leftContents, rightContents);
	}

	@Test
	public void testEquivalenceA1LtoR() throws IOException {
		final Resource left = equivalenceInput.getA1Left();
		final Resource right = equivalenceInput.getA1Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 6 differences, equivalent by pairs
		assertEquals(6, differences.size());

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

		mergerRegistry.getHighestRankingMerger(diff1).copyLeftToRight(diff1, new BasicMonitor());
		// Check that diff1 got properly merged
		assertMerged(comparison, diff1, false, false);
		// And validate that diff2 got merged as an equivalent diff
		assertMerged(comparison, diff2, false, false);

		mergerRegistry.getHighestRankingMerger(diff3).copyLeftToRight(diff3, new BasicMonitor());
		// Check that diff3 got properly merged
		assertMerged(comparison, diff3, false, false);
		// And validate that diff4 got merged as an equivalent diff
		assertMerged(comparison, diff4, false, false);

		mergerRegistry.getHighestRankingMerger(diff5).copyLeftToRight(diff5, new BasicMonitor());
		// Check that diff5 got properly merged
		assertMerged(comparison, diff5, false, false);
		// And validate that diff6 got merged as an equivalent diff
		assertMerged(comparison, diff6, false, false);

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testEquivalenceA1RtoL() throws IOException {
		final Resource left = equivalenceInput.getA1Left();
		final Resource right = equivalenceInput.getA1Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 6 differences, equivalent by pairs
		assertEquals(6, differences.size());

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

		mergerRegistry.getHighestRankingMerger(diff1).copyRightToLeft(diff1, new BasicMonitor());
		// Check that diff1 got properly merged (we're unsetting values)
		assertMerged(comparison, diff1, true, true);
		// And validate that diff2 got merged as an equivalent diff
		assertMerged(comparison, diff2, true, true);

		mergerRegistry.getHighestRankingMerger(diff3).copyRightToLeft(diff3, new BasicMonitor());
		assertMerged(comparison, diff3, true, true);
		assertMerged(comparison, diff4, true, true);

		mergerRegistry.getHighestRankingMerger(diff5).copyRightToLeft(diff5, new BasicMonitor());
		assertMerged(comparison, diff5, true, true);
		assertMerged(comparison, diff6, true, true);

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testEquivalenceA4LtoR() throws IOException {
		final Resource left = equivalenceInput.getA4Left();
		final Resource right = equivalenceInput.getA4Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 4 differences, equivalent by pairs
		assertEquals(4, differences.size());

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

		mergerRegistry.getHighestRankingMerger(diff1).copyLeftToRight(diff1, new BasicMonitor());
		assertMerged(comparison, diff1, false, false);
		assertMerged(comparison, diff2, false, false);

		mergerRegistry.getHighestRankingMerger(diff3).copyLeftToRight(diff3, new BasicMonitor());
		assertMerged(comparison, diff3, false, false);
		assertMerged(comparison, diff4, false, false);

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testEquivalenceA4RtoL() throws IOException {
		final Resource left = equivalenceInput.getA4Left();
		final Resource right = equivalenceInput.getA4Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 4 differences, equivalent by pairs
		assertEquals(4, differences.size());

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

		mergerRegistry.getHighestRankingMerger(diff1).copyRightToLeft(diff1, new BasicMonitor());
		assertMerged(comparison, diff1, true, true);
		assertMerged(comparison, diff2, true, true);

		mergerRegistry.getHighestRankingMerger(diff3).copyRightToLeft(diff3, new BasicMonitor());
		assertMerged(comparison, diff3, true, true);
		assertMerged(comparison, diff4, true, true);

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testEquivalenceA5LtoR() throws IOException {
		final Resource left = equivalenceInput.getA5Left();
		final Resource right = equivalenceInput.getA5Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 12 differences, 3 equivalent pairs, some dependencies
		assertEquals(12, differences.size());

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

		mergerRegistry.getHighestRankingMerger(diff1).copyLeftToRight(diff1, new BasicMonitor());
		assertMerged(comparison, diff1, false, false);
		assertMerged(comparison, diff2, false, false);
		assertSame(DifferenceState.MERGED, diff7.getState());
		assertSame(DifferenceState.MERGED, diff8.getState());

		mergerRegistry.getHighestRankingMerger(diff3).copyLeftToRight(diff3, new BasicMonitor());
		assertMerged(comparison, diff3, false, false);
		assertMerged(comparison, diff4, false, false);
		assertSame(DifferenceState.MERGED, diff9.getState());
		assertSame(DifferenceState.MERGED, diff10.getState());

		mergerRegistry.getHighestRankingMerger(diff5).copyLeftToRight(diff5, new BasicMonitor());
		assertMerged(comparison, diff5, false, false);
		assertMerged(comparison, diff6, false, false);
		assertSame(DifferenceState.MERGED, diff11.getState());
		assertSame(DifferenceState.MERGED, diff12.getState());

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testEquivalenceA5RtoL() throws IOException {
		final Resource left = equivalenceInput.getA5Left();
		final Resource right = equivalenceInput.getA5Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 12 differences, 3 equivalent pairs, some dependencies
		assertEquals(12, differences.size());

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
		mergerRegistry.getHighestRankingMerger(diff1).copyRightToLeft(diff1, new BasicMonitor());
		assertMerged(comparison, diff1, true, true);
		assertMerged(comparison, diff2, true, true);
		assertSame(DifferenceState.UNRESOLVED, diff7.getState());
		assertSame(DifferenceState.UNRESOLVED, diff8.getState());

		mergerRegistry.getHighestRankingMerger(diff3).copyRightToLeft(diff3, new BasicMonitor());
		assertMerged(comparison, diff3, true, true);
		assertMerged(comparison, diff4, true, true);
		assertSame(DifferenceState.UNRESOLVED, diff9.getState());
		assertSame(DifferenceState.UNRESOLVED, diff10.getState());

		mergerRegistry.getHighestRankingMerger(diff5).copyRightToLeft(diff5, new BasicMonitor());
		assertMerged(comparison, diff5, true, true);
		assertMerged(comparison, diff6, true, true);
		assertSame(DifferenceState.UNRESOLVED, diff11.getState());
		assertSame(DifferenceState.UNRESOLVED, diff12.getState());

		// Merge the 6 remaining diffs
		mergerRegistry.getHighestRankingMerger(diff7).copyRightToLeft(diff7, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diff8).copyRightToLeft(diff8, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diff9).copyRightToLeft(diff9, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diff10).copyRightToLeft(diff10, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diff11).copyRightToLeft(diff11, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diff12).copyRightToLeft(diff12, new BasicMonitor());

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testEquivalenceC2LtoR1() throws IOException {
		final Resource left = equivalenceInput.getC2Left();
		final Resource right = equivalenceInput.getC2Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 4 differences
		// 1 : added C
		// 2 : changed reference "destination" of A to C
		// 3 : unset reference "source" of B
		// 4 : changed reference "source" of C to A
		// 2, 3 and 4 are equivalent
		// 2 and 4 require 1
		assertEquals(4, differences.size());

		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.C"));
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.A", "destination", "Requirements.B", "Requirements.C"));
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.B", "source", "Requirements.A", null));
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.C", "source", null, "Requirements.A"));

		mergerRegistry.getHighestRankingMerger(diff2).copyLeftToRight(diff2, new BasicMonitor());
		assertSame(DifferenceState.MERGED, diff1.getState());
		assertMerged(comparison, diff2, false, false);
		assertMerged(comparison, diff3, true, false);
		assertMerged(comparison, diff4, false, false);

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testEquivalenceC2LtoR2() throws IOException {
		final Resource left = equivalenceInput.getC2Left();
		final Resource right = equivalenceInput.getC2Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 4 differences
		// 1 : added C
		// 2 : changed reference "destination" of A to C
		// 3 : unset reference "source" of B
		// 4 : changed reference "source" of C to A
		// 2, 3 and 4 are equivalent
		// 2 and 4 require 1
		assertEquals(4, differences.size());

		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.C"));
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.A", "destination", "Requirements.B", "Requirements.C"));
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.B", "source", "Requirements.A", null));
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.C", "source", null, "Requirements.A"));

		mergerRegistry.getHighestRankingMerger(diff1).copyLeftToRight(diff1, new BasicMonitor());
		assertSame(DifferenceState.MERGED, diff1.getState());
		assertSame(DifferenceState.UNRESOLVED, diff2.getState());
		assertSame(DifferenceState.UNRESOLVED, diff3.getState());
		assertSame(DifferenceState.UNRESOLVED, diff4.getState());

		mergerRegistry.getHighestRankingMerger(diff2).copyLeftToRight(diff2, new BasicMonitor());
		assertMerged(comparison, diff2, false, false);
		assertMerged(comparison, diff3, true, false);
		assertMerged(comparison, diff4, false, false);

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testEquivalenceC2RtoL() throws IOException {
		final Resource left = equivalenceInput.getC2Left();
		final Resource right = equivalenceInput.getC2Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 4 differences
		// 1 : added C
		// 2 : changed reference "destination" of A to C
		// 3 : unset reference "source" of B
		// 4 : changed reference "source" of C to A
		// 2, 3 and 4 are equivalent
		// 2 and 4 require 1
		assertEquals(4, differences.size());

		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Requirements", "containmentRef1", "Requirements.C"));
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.A", "destination", "Requirements.B", "Requirements.C"));
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.B", "source", "Requirements.A", null));
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Requirements.C", "source", null, "Requirements.A"));

		mergerRegistry.getHighestRankingMerger(diff2).copyRightToLeft(diff2, new BasicMonitor());
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

		mergerRegistry.getHighestRankingMerger(diff1).copyRightToLeft(diff1, new BasicMonitor());

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testEquivalenceC4LtoR() throws IOException {
		final Resource left = equivalenceInput.getC4Left();
		final Resource right = equivalenceInput.getC4Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

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
		assertEquals(5, differences.size());

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

		mergerRegistry.getHighestRankingMerger(diff1).copyLeftToRight(diff1, new BasicMonitor());
		assertMerged(comparison, diff1, false, false);
		assertSame(DifferenceState.UNRESOLVED, diff2.getState());
		assertSame(DifferenceState.UNRESOLVED, diff3.getState());
		assertSame(DifferenceState.UNRESOLVED, diff4.getState());
		assertSame(DifferenceState.UNRESOLVED, diff5.getState());

		mergerRegistry.getHighestRankingMerger(diff2).copyLeftToRight(diff2, new BasicMonitor());
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

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testEquivalenceC4RtoL() throws IOException {
		final Resource left = equivalenceInput.getC4Left();
		final Resource right = equivalenceInput.getC4Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

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
		assertEquals(5, differences.size());

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
		mergerRegistry.getHighestRankingMerger(diff1).copyRightToLeft(diff1, new BasicMonitor());
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

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testEquivalenceC5LtoR() throws IOException {
		final Resource left = equivalenceInput.getC5Left();
		final Resource right = equivalenceInput.getC5Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		// Initially 5 differences
		// 1 : Change: Set Node1.source to Node3
		// 2 : Change: Set Node3.destination to Node1
		// 3 : Change: Unset Node4.destination
		// 4 : Add: Node3
		// 5 : Delete Node4
		// 1-2-3 are equivalent

		assertEquals(5, differences.size());

		final ReferenceChange diff1 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Root.Node1", "source", "Root.Node4", "Root.Node3"));
		final ReferenceChange diff2 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Root.Node3", "destination", null, "Root.Node1"));
		final ReferenceChange diff3 = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Root.Node4", "destination", "Root.Node1", null));
		final ReferenceChange diff4 = (ReferenceChange)Iterators.find(differences.iterator(),
				added("Root.Node3"));
		final ReferenceChange diff5 = (ReferenceChange)Iterators.find(differences.iterator(),
				removed("Root.Node4"));

		/*
		 * Merge diff3 first. If the merger blindly merges diff3 without proper looking at the equivalences,
		 * diff1 and diff2 will also be set to status "merged" although the reference to be set is still
		 * missing.
		 */
		mergerRegistry.getHighestRankingMerger(diff3).copyLeftToRight(diff3, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diff1).copyLeftToRight(diff1, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diff2).copyLeftToRight(diff2, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diff4).copyLeftToRight(diff4, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diff5).copyLeftToRight(diff5, new BasicMonitor());

		// check if no differences between models are left
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testEquivalenceC5RtoL() throws IOException {
		final Resource left = equivalenceInput.getC5Left();
		final Resource right = equivalenceInput.getC5Right();

		// test the other way by reusing the models from C5LtoR
		final IComparisonScope scope = new DefaultComparisonScope(right, left, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		final ReferenceChange unsetDiff = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Root.Node4", "destination", null, "Root.Node1"));

		/*
		 * Merge the unsetDiff first. If the merger blindly merges the unsetDiff without proper looking at its
		 * equivalences, the equivalences will also be set to status "merged" although the reference to be set
		 * is still missing.
		 */
		mergerRegistry.getHighestRankingMerger(unsetDiff).copyRightToLeft(unsetDiff, new BasicMonitor());

		// merge the remaining diffs
		for (Diff diff : differences) {
			if (diff != unsetDiff) {
				mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
			}
		}

		// check if no differences between models are left
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testEquivalenceC6LtoR() throws IOException {
		final Resource left = equivalenceInput.getC6Left();
		final Resource right = equivalenceInput.getC6Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		// Initially 3 differences
		// 1 : Change: Set Node1.source to Node3
		// 2 : Change: Set Node3.destination to Node1
		// 3 : Change: Unset Node4.destination
		// 1-2-3 are equivalent

		final ReferenceChange unsetDiff = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Root.Node4", "destination", "Root.Node1", null));

		/*
		 * Merge the unsetDiff first. If the merger blindly merges the unsetDiff without proper looking at its
		 * equivalences, the equivalences will also be set to status "merged" although the reference to be set
		 * is still missing.
		 */
		mergerRegistry.getHighestRankingMerger(unsetDiff).copyLeftToRight(unsetDiff, new BasicMonitor());

		// merge the remaining differences
		for (Diff diff : differences) {
			if (diff != unsetDiff) {
				mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
			}
		}

		// check if no differences between models are left
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testEquivalenceC6RtoL() throws IOException {
		final Resource left = equivalenceInput.getC6Left();
		final Resource right = equivalenceInput.getC6Right();

		// test the other way by reusing the models from C6LtoR
		final IComparisonScope scope = new DefaultComparisonScope(right, left, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		final ReferenceChange unsetDiff = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Root.Node4", "destination", null, "Root.Node1"));

		/*
		 * Merge the unsetDiff first. If the merger blindly merges the unsetDiff without proper looking at its
		 * equivalences, the equivalences will also be set to status "merged" although the reference to be set
		 * is still missing.
		 */
		mergerRegistry.getHighestRankingMerger(unsetDiff).copyRightToLeft(unsetDiff, new BasicMonitor());

		// merge the remaining differences
		for (Diff diff : differences) {
			if (diff != unsetDiff) {
				mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
			}
		}

		// check if no differences between models are left
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	/**
	 * Tests a scenario in which two nodes contain interlocked one-to-one references. The merger must handle
	 * these cases with care since diffs can become redundant during merging.
	 */
	@Test
	public void testOneToOneRefMergeL2R() throws IOException {
		final Resource left = twoWayInput.getOneToOneMergeL2RLeft();
		final Resource right = twoWayInput.getOneToOneRefMergeL2RRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);
		final List<Diff> differences = comparison.getDifferences();

		// differences:
		// 1. Change c.source to c
		// 2. Change c.destination to c
		// 3. Change d.source to null
		// 4. Change d.destination to null
		// 1,4 and 2,3 are equivalent

		final ReferenceChange setCSourceDiff = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Root.c", "source", "Root.d", "Root.c"));

		final ReferenceChange setDSourceDiff = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Root.d", "source", "Root.c", null));

		// By merging diff1 (setCSourceDiff) the model will be in a state where the remaining diffs
		// describe actions which already occurred.
		mergerRegistry.getHighestRankingMerger(setCSourceDiff).copyLeftToRight(setCSourceDiff,
				new BasicMonitor());

		// Check if the non-equivalent diff is also set to merged
		assertEquals(DifferenceState.MERGED, setDSourceDiff.getState());

		// Check if no differences between models are left
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	/**
	 * Tests a scenario in which two nodes contain interlocked one-to-one references. The merger must handle
	 * these cases with care since diffs can become redundant during merging.
	 */
	@Test
	public void testOneToOneRefMergeR2L() throws IOException {
		final Resource left = twoWayInput.getOneToOneMergeR2LLeft();
		final Resource right = twoWayInput.getOneToOneRefMergeR2LRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);
		final List<Diff> differences = comparison.getDifferences();

		// differences:
		// 1. Change c.source to d
		// 2. Change c.destination to d
		// 3. Change d.source to c
		// 4. Change d.destination to c
		// 1,4 and 2,3 are equivalent

		final ReferenceChange setCSourceDiff = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Root.c", "source", "Root.c", "Root.d"));

		final ReferenceChange setDSourceDiff = (ReferenceChange)Iterators.find(differences.iterator(),
				changedReference("Root.d", "source", null, "Root.c"));

		// By merging diff1 (setCSourceDiff) R2L the model will be in a state where the remaining diffs
		// describe actions which already occurred.
		mergerRegistry.getHighestRankingMerger(setCSourceDiff).copyRightToLeft(setCSourceDiff,
				new BasicMonitor());

		// Check if the non-equivalent diff is marked as merged
		assertEquals(DifferenceState.MERGED, setDSourceDiff.getState());

		// Check if no differences between models are left
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testEquivalenceC7LtoR() throws IOException {
		final Resource left = equivalenceInput.getC7Left();
		final Resource right = equivalenceInput.getC7Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final ReferenceChange deleteDiff = (ReferenceChange)Iterators.find(differences.iterator(),
				removedFromReference("Root.a", "destination", "Root.a"));

		/*
		 * Merge the deleteDiff first. If the merger blindly merges the deleteDiff without proper looking at
		 * its equivalences, the equivalences will also be set to status "merged" although the reference to be
		 * added is still missing.
		 */
		mergerRegistry.getHighestRankingMerger(deleteDiff).copyLeftToRight(deleteDiff, new BasicMonitor());

		// merge the remaining differences
		for (Diff diff : differences) {
			if (diff != deleteDiff) {
				mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
			}
		}

		// check if no differences between models are left
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testEquivalenceC7RtoL() throws IOException {
		final Resource left = equivalenceInput.getC7Left();
		final Resource right = equivalenceInput.getC7Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final ReferenceChange deleteDiff = (ReferenceChange)Iterators.find(differences.iterator(),
				addedToReference("Root.b", "destination", "Root.a"));

		/*
		 * Merge the diff resulting in a delete first. If the merger blindly merges the deleteDiff without
		 * proper looking at its equivalences, the equivalences will also be set to status "merged" although
		 * the reference to be added is still missing.
		 */
		mergerRegistry.getHighestRankingMerger(deleteDiff).copyRightToLeft(deleteDiff, new BasicMonitor());

		// merge the remaining differences
		for (Diff diff : differences) {
			if (diff != deleteDiff) {
				mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
			}
		}

		// check if no differences between models are left
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testFeatureMapDependencyL2R() throws IOException {
		ResourceSetImpl resourceSet = new ResourceSetImpl();

		final Resource left = twoWayInput.getFeatureMapDependencyL2RLeft(resourceSet);
		final Resource right = twoWayInput.getFeatureMapDependencyL2RRight(resourceSet);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		// There should be 3 differences
		// 1. Add first key (ReferenceChange)
		// 2. Add first key (FeatureMapChange)
		// 3. Add NodeFeatureMapContainment (ReferenceChange)
		assertEquals(3, comparison.getDifferences().size());

		FeatureMapChange addFirstKey = (FeatureMapChange)Iterators.find(differences.iterator(),
				instanceOf(FeatureMapChange.class));
		assertNotNull(addFirstKey);

		// Execute FeatureMapChange to test if it properly resolves its dependencies
		mergerRegistry.getHighestRankingMerger(addFirstKey).copyLeftToRight(addFirstKey, new BasicMonitor());

		// Execute the remaining differences
		for (Diff diff : differences) {
			if (diff != addFirstKey) {
				mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
			}
		}

		// Check if no differences are left
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testRemoveFeatureMapR2L() throws IOException {
		ResourceSetImpl resourceSet = new ResourceSetImpl();

		final Resource left = twoWayInput.getRemoveFeatureMapR2LLeft(resourceSet);
		final Resource right = twoWayInput.getRemoveFeatureMapR2LRight(resourceSet);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		// There should be 5 differences
		// 1. Remove first key (ReferenceChange)
		// 2. Remove first key (FeatureMapChange)
		// 3. Remove second key (ReferenceChange)
		// 4. Remove second key (FeatureMapChange)
		// 5. Remove NodeFeatureMapContainment (ReferenceChange)
		assertEquals(5, comparison.getDifferences().size());

		// Also test dependencies by merging the FeatureMapChanges first
		Iterator<Diff> featureMapChangesIt = Iterators.filter(differences.iterator(),
				instanceOf(FeatureMapChange.class));
		List<Diff> featureMapChanges = Lists.newArrayList(featureMapChangesIt);
		assertEquals(2, featureMapChanges.size());

		// Execute FeatureMapChanges first to test if they properly resolve their dependencies
		for (Diff diff : featureMapChanges) {
			mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		}

		// Execute the remaining differences
		for (Diff diff : differences) {
			if (!featureMapChanges.contains(diff)) {
				mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
			}
		}

		// Check if no differences are left
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testMergeAllDiffsTwice() throws IOException {
		final IdentifierMatchInputData inputData = new IdentifierMatchInputData();
		final Resource left = inputData.getExtlibraryLeft();
		final Resource origin = inputData.getExtlibraryOrigin();
		final Resource right = inputData.getExtlibraryRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		final List<Diff> differences = comparison.getDifferences();

		assertFalse(differences.isEmpty());

		// Just test no NPE is raising
		for (Diff diff : differences) {
			ComparisonUtil.getSubDiffs(true).apply(diff);
			mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
			ComparisonUtil.getSubDiffs(true).apply(diff);
			mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		}
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
		assertEquals(size, list2.size());

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
		if (unset && ref.isContainment()) {
			assertNull(valueMatch);
		} else if (ref.isMany()) {
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
				assertEquals(expectedIndex, values.indexOf(addedToLeft));
			} else {
				assertTrue(valueMatch == null || valueMatch.getLeft() == null);
			}
		} else {
			if (expectedIndex != -1) {
				assertNotNull(valueMatch.getRight());
				assertSame(containerMatch.getRight().eResource(), valueMatch.getRight().eResource());
				final EObject addedToRight = valueMatch.getRight();
				final List<EObject> values = getAsList(containerMatch.getRight(), diff.getReference());
				assertEquals(expectedIndex, values.indexOf(addedToRight));
			} else {
				assertTrue(valueMatch == null || valueMatch.getRight() == null);
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
