/*******************************************************************************
 * Copyright (c) 2014 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.merge.data.ThreeWayMergeInputData;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

/**
 * Tests three-way comparison of {@link NodesPackage nodes models} and subsequent merging using the
 * {@link BatchMerger}.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class ThreeWayBatchMergingTest {

	final private ThreeWayMergeInputData input = new ThreeWayMergeInputData();

	final private IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	final private IBatchMerger merger = new BatchMerger(mergerRegistry);

	/**
	 * Tests a scenario in which a model element is moved from one container into another, whereas the
	 * original container's type does not provide the containment reference that is used as the target of the
	 * move. This lead to an NPE (cf. bug #446739). In this test, the move is applied on the left-hand side.
	 * 
	 * @throws IOException
	 *             if {@link ThreeWayMergeInputData} fails to load the test models.
	 */
	@Test
	public void mergingMoveToDifferentContainmentFeatureLeft() throws IOException {
		final Resource origin = input.getMoveToDifferentContainmentFeatureOrigin();
		final Resource left = input.getMoveToDifferentContainmentFeatureMove();
		final Resource right = input.getMoveToDifferentContainmentFeatureUnchanged();
		assertUnchanged(new DefaultComparisonScope(left, right, origin), RIGHT);
		batchMergeAndAssertEquality(new DefaultComparisonScope(left, right, origin), LEFT);
	}

	/**
	 * Tests a scenario in which a model element is moved from one container into another, whereas the
	 * original container's type does not provide the containment reference that is used as the target of the
	 * move. This lead to an NPE (cf. bug #446739). In this test, the move is applied on the right-hand side.
	 * 
	 * @throws IOException
	 *             if {@link ThreeWayMergeInputData} fails to load the test models.
	 */
	@Test
	public void mergingMoveToDifferentContainmentFeatureRight() throws IOException {
		final Resource origin = input.getMoveToDifferentContainmentFeatureOrigin();
		final Resource left = input.getMoveToDifferentContainmentFeatureUnchanged();
		final Resource right = input.getMoveToDifferentContainmentFeatureMove();
		assertUnchanged(new DefaultComparisonScope(left, right, origin), LEFT);
		batchMergeAndAssertEquality(new DefaultComparisonScope(left, right, origin), RIGHT);
	}

	private void assertUnchanged(IComparisonScope scope, DifferenceSource side) {
		final Notifier supposeUnchanged;
		if (LEFT.equals(side)) {
			supposeUnchanged = scope.getLeft();
		} else {
			supposeUnchanged = scope.getRight();
		}
		final IComparisonScope scopeForAssert = createTwoWayScopeWithOrigin(scope, supposeUnchanged);
		EList<Diff> diffsForAssertion = compare(scopeForAssert).getDifferences();
		assertEquals(0, diffsForAssertion.size());
	}

	private IComparisonScope createTwoWayScopeWithOrigin(IComparisonScope scope, final Notifier notifier) {
		return new DefaultComparisonScope(scope.getOrigin(), notifier, null);
	}

	private void batchMergeAndAssertEquality(IComparisonScope scope, DifferenceSource side) {
		batchMerge(scope, side);
		assertEqualityOfLeftAndRight(scope);
	}

	private void batchMerge(IComparisonScope scope, DifferenceSource side) {
		Comparison comparison = compare(scope);
		final EList<Diff> differences = comparison.getDifferences();

		if (LEFT.equals(side)) {
			final List<Diff> leftDiffs = filterDiffs(differences, LEFT, false);
			merger.copyAllLeftToRight(leftDiffs, new BasicMonitor());
		} else {
			final List<Diff> rightDiffs = filterDiffs(differences, RIGHT, false);
			merger.copyAllRightToLeft(rightDiffs, new BasicMonitor());
		}
	}

	private Comparison compare(IComparisonScope scope) {
		return EMFCompare.builder().build().compare(scope);
	}

	private List<Diff> filterDiffs(List<Diff> diffs, DifferenceSource source, boolean filterConflicting) {
		List<Diff> filteredDiffs = new ArrayList<Diff>();
		for (Diff diff : diffs) {
			if (source.equals(diff.getSource()) && (!isConflicting(diff) || !filterConflicting)) {
				filteredDiffs.add(diff);
			}
		}
		return filteredDiffs;
	}

	private boolean isConflicting(Diff diff) {
		return diff.getConflict() != null && ConflictKind.REAL.equals(diff.getConflict().getKind());
	}

	private void assertEqualityOfLeftAndRight(IComparisonScope scope) {
		final Notifier left = scope.getLeft();
		final Notifier right = scope.getRight();
		final IComparisonScope assertScope = new DefaultComparisonScope(left, right, null);
		final Comparison assertComparison = compare(assertScope);
		final EList<Diff> assertDiffs = assertComparison.getDifferences();

		assertTrue(assertDiffs.isEmpty());
	}

}
