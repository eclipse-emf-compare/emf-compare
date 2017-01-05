/*******************************************************************************
 * Copyright (c) 2016 Obeo and others.
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
import static com.google.common.collect.Collections2.permutations;
import static com.google.common.collect.Iterables.find;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.moved;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.junit.Assert.assertEquals;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.conflict.data.ConflictInputData;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * This use case features 12 distinct differences of all types, with 4 real conflicts and 1 pseudo conflict.
 * The test makes sure that whatever the order in which the operations are performed, as long the user starts
 * by rejecting all 'remote' diffs before accepting all other diffs, the 2 sides of the comparison are equal.
 * The combinatorial number (34560 combinations) has been limited by dealing with pseudo-conflicting diff and
 * non-conflicting diffs after dealing with all conflicts and not only after rejecting the conflicting diffs
 * on the remote side.
 * <ol>
 * <li>Left : Node8 added</li>
 * <li>Left : Node9 added</li>
 * <li>Left : Node1 moved</li>
 * <li>Left : Node0 added</li>
 * <li>Left : Node5 removed</li>
 * <li>Left : Node6 removed</li>
 * <li>Left : Node7 removed</li>
 * <li>Right : Node6 moved</li>
 * <li>Right : Node9 added</li>
 * <li>Right : Node0 added</li>
 * <li>Right : Node1 moved</li>
 * <li>Right : Node5 removed</li>
 * </ol>
 * <ul>
 * <li>Real conflict : Node0 (Adding the same value at different indices)</li>
 * <li>Real conflict : Node1 (Moving the same value to different indices on both sides)</li>
 * <li>Real conflict : Node6 (Moving and deleting the same value)</li>
 * <li>Real conflict : Node9 (Adding the same value at different indices)</li>
 * <li>Pseudo conflict : Node5 (Removing the same value on both sides)</li>
 * </ul>
 * For reference,
 * <ul>
 * <li>"original" is : {Node1, Node2, Node3, Node4, Node5, Node6, Node7}</li>
 * <li>"left" is : {Node8, Node9, Node2, Node3, Node4, Node1, Node0}</li>
 * <li>"right" is : {Node6, Node2, Node9, Node3, Node0, Node1, Node4, Node7}</li>
 * </ul>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
@RunWith(Parameterized.class)
public class ComplexMergeTest {

	private ConflictInputData conflictInput = new ConflictInputData();

	private IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	private List<Predicate<? super Diff>> rightConflictPermutation;

	private List<Predicate<? super Diff>> leftConflictPermutation;

	private List<Predicate<? super Diff>> otherPermutation;

	@SuppressWarnings("unchecked")
	@Parameters
	public static Iterable<Object[]> data() {
		Collection<List<Predicate<? super Diff>>> rightConflictPermutations = permutations(
				Arrays.<Predicate<? super Diff>> asList(added("Root.Node0"), //$NON-NLS-1$
						moved("Root.Node1", "containmentRef1"), //$NON-NLS-1$ //$NON-NLS-2$
						moved("Root.Node6", //$NON-NLS-1$
								"containmentRef1"), //$NON-NLS-1$
						added("Root.Node9"))); //$NON-NLS-1$
		Collection<List<Predicate<? super Diff>>> leftConflictPermutations = permutations(
				Arrays.<Predicate<? super Diff>> asList(added("Root.Node0"), //$NON-NLS-1$
						moved("Root.Node1", "containmentRef1"), removed("Root.Node5"), removed("Root.Node6"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						added("Root.Node9"))); //$NON-NLS-1$
		Collection<List<Predicate<? super Diff>>> otherPermutations = permutations(
				Arrays.<Predicate<? super Diff>> asList(and(fromSide(LEFT), removed("Root.Node5")), //$NON-NLS-1$
						and(fromSide(LEFT), removed("Root.Node7")), and( //$NON-NLS-1$
								fromSide(LEFT), added("Root.Node8")))); //$NON-NLS-1$

		List<Object[]> data = new ArrayList<Object[]>();
		for (List<Predicate<? super Diff>> otherPermutation : otherPermutations) {
			for (List<Predicate<? super Diff>> rightConflictPermutation : rightConflictPermutations) {
				for (List<Predicate<? super Diff>> leftConflictPermutation : leftConflictPermutations) {
					data.add(new Object[] {leftConflictPermutation, rightConflictPermutation,
							otherPermutation });
				}
			}
		}
		return data;
	}

	public ComplexMergeTest(List<Predicate<? super Diff>> leftConflictpermutation,
			List<Predicate<? super Diff>> rightConflictPermutation,
			List<Predicate<? super Diff>> otherPermutation) {
		this.rightConflictPermutation = rightConflictPermutation;
		this.leftConflictPermutation = leftConflictpermutation;
		this.otherPermutation = otherPermutation;
	}

	/**
	 * make sure that on a complex case, it's possible to merge left to right all conflicting right diffs
	 * (i.e. reject them all) and then merge left to right all the other differences and always get 2
	 * identical models.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testLeftToRight() throws IOException {
		final Resource left = conflictInput.getComplexLeft();
		final Resource origin = conflictInput.getComplexOrigin();
		final Resource right = conflictInput.getComplexRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		EMFCompare emfc = EMFCompare.builder().build();

		Comparison comp = emfc.compare(scope);
		for (Predicate<? super Diff> conflictingNode : rightConflictPermutation) {
			Diff diff = getDiff(comp, RIGHT, conflictingNode);
			copyLeftToRight(diff);
		}
		for (Predicate<? super Diff> conflictingNode : leftConflictPermutation) {
			Diff diff = getDiff(comp, LEFT, conflictingNode);
			copyLeftToRight(diff);
		}
		for (Predicate<? super Diff> otherNode : otherPermutation) {
			Diff diff = getDiff(comp, otherNode);
			copyLeftToRight(diff);
		}

		// Left and Right should now be equal
		assertEqualContents(comp, getNodes(left), getNodes(right));
	}

	/**
	 * make sure that on a complex case, it's possible to merge left to right all conflicting right diffs
	 * (i.e. reject them all) and then merge left to right all the other differences and always get 2
	 * identical models.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testRightToLeft() throws IOException {
		final Resource left = conflictInput.getComplexLeft();
		final Resource origin = conflictInput.getComplexOrigin();
		final Resource right = conflictInput.getComplexRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		EMFCompare emfc = EMFCompare.builder().build();

		Comparison comp = emfc.compare(scope);
		for (Predicate<? super Diff> conflictingNode : leftConflictPermutation) {
			Diff diff = getDiff(comp, LEFT, conflictingNode);
			copyRightToLeft(diff);
		}
		for (Predicate<? super Diff> conflictingNode : rightConflictPermutation) {
			Diff diff = getDiff(comp, RIGHT, conflictingNode);
			copyRightToLeft(diff);
		}
		for (Predicate<? super Diff> otherNode : otherPermutation) {
			Diff diff = getDiff(comp, otherNode);
			copyRightToLeft(diff);
		}
		// Left and Right should now be equal
		assertEqualContents(comp, getNodes(left), getNodes(right));
	}

	private List<EObject> getNodes(Resource r) {
		EObject container = r.getContents().get(0);
		return getAsList(container, NodesPackage.eINSTANCE.getNode_ContainmentRef1());
	}

	private Diff getDiff(Comparison comp, DifferenceSource side, Predicate<? super Diff> predicate) {
		return find(comp.getDifferences(), and(fromSide(side), predicate));
	}

	private Diff getDiff(Comparison comp, Predicate<? super Diff> predicate) {
		return find(comp.getDifferences(), predicate);
	}

	private void copyRightToLeft(Diff diff) {
		new BatchMerger(mergerRegistry).copyAllRightToLeft(Arrays.asList(diff), new BasicMonitor());
	}

	private void copyLeftToRight(Diff diff) {
		new BatchMerger(mergerRegistry).copyAllLeftToRight(Arrays.asList(diff), new BasicMonitor());
	}

	/**
	 * Ensure that the two given lists contain the same elements in the same order. The kind of list does not
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
