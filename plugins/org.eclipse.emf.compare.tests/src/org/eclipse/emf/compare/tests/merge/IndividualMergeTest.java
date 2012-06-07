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
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.moved;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.tests.merge.data.MergeInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

@SuppressWarnings("nls")
public class IndividualMergeTest {
	private MergeInputData input = new MergeInputData();

	@Test
	public void testComplexUseCaseLtoR1() throws IOException {
		final Resource left = input.getComplexLeft();
		final Resource origin = input.getComplexOrigin();
		final Resource right = input.getComplexRight();

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

		// FIXME !!! There is actually nothing to do here : the conflict *should* now be a pseudo conflict.
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
		assertValueIndexIs(diff5, false, 6);
		diff5.copyLeftToRight();
		assertValueIndexIs(diff5, false, -1);

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
		assertValueIndexIs(diff6, false, -1);

		// Left and Right should now be equal
		final EObject leftContainer = diff7.getMatch().getLeft();
		final EObject rightContainer = diff7.getMatch().getRight();
		final List<EObject> leftContents = getAsList(leftContainer, diff7.getReference());
		final List<EObject> rightContents = getAsList(rightContainer, diff7.getReference());
		assertEqualContents(comparison, leftContents, rightContents);
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
