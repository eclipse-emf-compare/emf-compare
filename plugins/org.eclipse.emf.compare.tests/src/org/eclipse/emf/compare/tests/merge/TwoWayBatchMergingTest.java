/*******************************************************************************
 * Copyright (c) EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.compare.merge.AbstractMerger;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.ReferenceChangeMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.merge.data.TwoWayMergeInputData;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodeMultipleContainment;
import org.eclipse.emf.compare.tests.nodes.NodeOppositeRefOneToMany;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

/**
 * Tests two-way comparison of {@link NodesPackage nodes models} with XMI IDs and subsequent merging using the
 * {@link BatchMerger}.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class TwoWayBatchMergingTest {

	private enum Direction {
		LEFT_TO_RIGHT, RIGHT_TO_LEFT;
	}

	private TwoWayMergeInputData input = new TwoWayMergeInputData();

	private IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	/**
	 * Tests a scenario in which an element is moved from one container to another, whereas the containment
	 * reference in the original container (left) is not available in the target container (right). This lead
	 * to a NPE in {@link DiffUtil#findInsertionIndex(Comparison, org.eclipse.emf.compare.Diff, boolean)} (cf.
	 * Bug #440679).
	 * <p>
	 * In this test case we have two differences: (1) Deletion of {@link NodeMultipleContainment} "A" and (2)
	 * Move of {@link Node} "B" from {@link NodeMultipleContainment} "A" (reference "containmentRef2") to
	 * {@link Node} "Root" into reference "containmentRef1". As a result, we move node "B" originally
	 * contained through "containmentRef2" into a {@link Node}, which does not have the feature
	 * "containmentRef2".
	 * </p>
	 * 
	 * @throws IOException
	 *             if {@link TwoWayMergeInputData} fails to load the test models.
	 */
	@Test
	public void mergingMoveToDifferentContainmentFeatureR2L() throws IOException {
		final Resource left = input.getMoveToDifferentContainmentFeatureRTLLeft();
		final Resource right = input.getMoveToDifferentContainmentFeatureRTLRight();
		batchMergeAndAssertEquality(left, right, Direction.RIGHT_TO_LEFT);
	}

	/**
	 * This test is the reverse of the test ({@link #mergingMoveToDifferentContainmentFeatureR2L() above} to
	 * make sure, this issue is not appearing in the other direction as well.
	 * 
	 * @throws IOException
	 *             if {@link TwoWayMergeInputData} fails to load the test models.
	 */
	@Test
	public void mergingMoveToDifferentContainmentFeatureL2R() throws IOException {
		final Resource left = input.getMoveToDifferentContainmentFeatureL2RLeft();
		final Resource right = input.getMoveToDifferentContainmentFeatureL2RRight();
		batchMergeAndAssertEquality(left, right, Direction.LEFT_TO_RIGHT);
	}

	/**
	 * Tests a scenario in which an opposite one-to-many reference is changed, whereas the original object on
	 * the single-valued side of the one-to-many reference has no match. This lead to an
	 * {@link IndexOutOfBoundsException} (cf. Bug #413520), because in the
	 * {@link ReferenceChangeMerger#internalCheckOrdering(ReferenceChange, boolean) ordering check of the
	 * equivalent changes}, the source container is <code>null</code> leading to an empty source list. This
	 * leads to an invocation of {@link EList#move(int, EObject)} on an empty list.
	 * <p>
	 * In this test case, we have the following differences: (1) the deletion of {@link Node} "C" (only
	 * available on the right-hand side), as a result, (2) the deletion of the reference to {@link Node} "C"
	 * from {@link NodeOppositeRefOneToMany} "A" at its feature
	 * {@link NodeOppositeRefOneToMany#getDestination() destination}, and (3 & 4) the change of the opposite
	 * references {@link NodeOppositeRefOneToMany#getSource() source} and
	 * {@link NodeOppositeRefOneToMany#getDestination() destination} between {@link NodeOppositeRefOneToMany
	 * nodes} "A" and "B" (both have a match in the left and right model version).
	 * </p>
	 * 
	 * @throws IOException
	 *             if {@link TwoWayMergeInputData} fails to load the test models.
	 */
	@Test
	public void mergingOppositeReferenceChangeWithoutMatchingOriginalL2R() throws IOException {
		final Resource left = input.getOppositeReferenceChangeWithoutMatchingOrignalContainerL2RLeft();
		final Resource right = input.getOppositeReferenceChangeWithoutMatchingOrignalContainerL2RRight();
		batchMergeAndAssertEquality(left, right, Direction.LEFT_TO_RIGHT);
	}

	/**
	 * Tests a scenario in which an opposite one-to-many reference is changed, whereas there is one deletion
	 * and one addition on the multi-valued side of the opposite references. This correctly leads to three
	 * differences: (1) a change of the single-valued reference, (2) an addition of a value in the
	 * multi-valued opposite reference, and (3) a deletion of a value in the multi-valued opposite reference.
	 * However, all three of them end up in the same {@link Equivalence object}, which caused the
	 * {@link AbstractMerger} to merge only one of them; that is, in this scenario, the deletion. This
	 * ultimately lead to unmerged differences (cf. Bug #441172).
	 * 
	 * @throws IOException
	 *             if {@link TwoWayMergeInputData} fails to load the test models.
	 */
	@Test
	public void mergingOppositeReferenceChangeWithAddAndDeleteOnMultivaluedSideR2L() throws IOException {
		final Resource left = input.getOppositeReferenceChangeWithAddAndDeleteOnMultivaluedSideLeft();
		final Resource right = input.getOppositeReferenceChangeWithAddAndDeleteOnMultivaluedSideRight();
		batchMergeAndAssertEquality(left, right, Direction.RIGHT_TO_LEFT);
	}

	/**
	 * Merges the given resources {@code left} and {@code right} using the {@link BatchMerger} in the
	 * specified {@code direction}, re-compares left and right, and asserts their equality in the end.
	 * 
	 * @param left
	 *            left resource.
	 * @param right
	 *            right resource.
	 */
	private void batchMergeAndAssertEquality(Resource left, Resource right, Direction direction) {
		// perform comparison
		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);
		final EList<Diff> differences = comparison.getDifferences();

		// batch merging of all detected differences:
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		switch (direction) {
			case LEFT_TO_RIGHT:
				merger.copyAllLeftToRight(differences, new BasicMonitor());
			case RIGHT_TO_LEFT:
				merger.copyAllRightToLeft(differences, new BasicMonitor());
		}

		// check that models are equal after batch merging
		Comparison assertionComparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, assertionComparison.getDifferences().size());
	}

}
