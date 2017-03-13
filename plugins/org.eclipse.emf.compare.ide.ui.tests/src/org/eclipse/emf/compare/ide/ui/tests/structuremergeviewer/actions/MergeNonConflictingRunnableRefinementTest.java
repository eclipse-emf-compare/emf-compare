/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions;

import static java.util.Arrays.asList;
import static org.eclipse.emf.compare.ConflictKind.REAL;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;
import static org.eclipse.emf.compare.DifferenceState.UNRESOLVED;
import static org.eclipse.emf.compare.internal.merge.MergeMode.LEFT_TO_RIGHT;

import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeNonConflictingRunnable;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.internal.spec.ComparisonSpec;
import org.eclipse.emf.compare.internal.spec.ConflictSpec;
import org.eclipse.emf.compare.internal.spec.DiffSpec;
import org.eclipse.emf.compare.internal.spec.MatchSpec;
import org.eclipse.emf.compare.merge.AbstractMerger;
import org.eclipse.emf.compare.merge.DiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IMerger;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for ensuring that refinement diffs are only merged together as a whole or not at all when merging
 * non-conflicting changes.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
@SuppressWarnings({"restriction", "nls" })
public class MergeNonConflictingRunnableRefinementTest {
	private static final IMerger.Registry MERGER_REGISTRY = IMerger.RegistryImpl.createStandaloneInstance();
	static {
		MERGER_REGISTRY.add(new AbstractMerger() {
			public boolean isMergerFor(Diff target) {
				return true;
			}
		});
	}

	/**
	 * Creates a new difference.
	 * 
	 * @param name
	 *            name used in {@link #toString()} for convenience.
	 * @param source
	 *            difference source.
	 * @return newly created difference
	 */
	protected Diff createDiff(final String name, DifferenceSource source) {
		Diff diff = new DiffSpec() {
			@Override
			public String toString() {
				return name;
			}
		};
		diff.setSource(source);
		return diff;
	}

	/**
	 * Creates a new conflict.
	 * 
	 * @param kind
	 *            conflict kind.
	 * @param diffs
	 *            differences that participate in the conflict.
	 * @return newly created conflict
	 */
	protected Conflict createConflict(ConflictKind kind, Diff... diffs) {
		Conflict conflict = new ConflictSpec();
		conflict.setKind(kind);
		conflict.getDifferences().addAll(asList(diffs));
		return conflict;
	}

	/**
	 * Creates a new comparison with a single match that holds all differences.
	 * 
	 * @param differences
	 *            differences in the comparison
	 * @param conflicts
	 *            conflicts in the comparison
	 * @return newly created comparison
	 */
	protected Comparison createComparison(List<Diff> differences, List<Conflict> conflicts) {
		Comparison comparison = new ComparisonSpec();
		Match match = new MatchSpec();
		match.getDifferences().addAll(differences);
		comparison.getMatches().add(match);
		comparison.getConflicts().addAll(conflicts);
		comparison.setThreeWay(false);
		return comparison;
	}

	/**
	 * With the following refinement graph. Upper layers are refined by lower layers:
	 * 
	 * <pre>
	 *   Left   |  Right
	 *  --------+---------
	 *     A    |    A     
	 *    / \   |   / \
	 *   B   C  |  B   C
	 * 
	 * Merge:         Left To Right
	 * Real Conflict: Left.B, Right.B
	 * Expected:      No Diffs merged
	 * Start Diff:    Left.B
	 * </pre>
	 */
	@Test
	public void testMergeNonConflictingWithBStartB() {
		final boolean isLeftToRight = true;
		final MergeMode mergeMode = LEFT_TO_RIGHT;

		// Create diffs
		Diff leftA = createDiff("LeftA", LEFT);
		Diff leftB = createDiff("LeftB", LEFT);
		Diff leftC = createDiff("LeftC", LEFT);
		leftA.getRefines().add(leftB);
		leftA.getRefines().add(leftC);

		// Create diffs
		Diff rightA = createDiff("RightA", RIGHT);
		Diff rightB = createDiff("RightB", RIGHT);
		Diff rightC = createDiff("RightC", RIGHT);
		rightA.getRefines().add(rightB);
		rightA.getRefines().add(rightC);

		// Add conflicts
		Conflict conflict = createConflict(REAL, leftB, rightB);

		Comparison comparison = createComparison(asList(leftB, leftC, leftA, rightB, rightC, rightA),
				asList(conflict));

		mergeNonConflictingChanges(comparison, mergeMode, isLeftToRight);
		verifyAllUnresolved(comparison.getDifferences());
	}

	/**
	 * With the following refinement graph. Upper layers are refined by lower layers:
	 * 
	 * <pre>
	 *   Left   |  Right
	 *  --------+---------
	 *     A    |    A     
	 *    / \   |   / \
	 *   B   C  |  B   C
	 * 
	 * Merge:         Left To Right
	 * Real Conflict: Left.B, Right.B
	 * Expected:      No Diffs merged
	 * Start Diff:    Left.C
	 * </pre>
	 */
	@Test
	public void testMergeNonConflictingWithBStartC() {
		final boolean isLeftToRight = true;
		final MergeMode mergeMode = LEFT_TO_RIGHT;

		// Create diffs
		Diff leftA = createDiff("LeftA", LEFT);
		Diff leftB = createDiff("LeftB", LEFT);
		Diff leftC = createDiff("LeftC", LEFT);
		leftA.getRefines().add(leftB);
		leftA.getRefines().add(leftC);

		// Create diffs
		Diff rightA = createDiff("RightA", RIGHT);
		Diff rightB = createDiff("RightB", RIGHT);
		Diff rightC = createDiff("RightC", RIGHT);
		rightA.getRefines().add(rightB);
		rightA.getRefines().add(rightC);

		// Add conflicts
		Conflict conflict = createConflict(REAL, leftB, rightB);

		Comparison comparison = createComparison(asList(leftC, leftB, leftA, rightB, rightC, rightA),
				asList(conflict));

		mergeNonConflictingChanges(comparison, mergeMode, isLeftToRight);
		verifyAllUnresolved(comparison.getDifferences());
	}

	/**
	 * With the following refinement graph. Upper layers are refined by lower layers:
	 * 
	 * <pre>
	 *   Left   |  Right
	 *  --------+---------
	 *     A    |    A     
	 *    / \   |   / \
	 *   B   C  |  B   C
	 * 
	 * Merge:         Left To Right
	 * Real Conflict: Left.B, Right.B
	 * Expected:      No Diffs merged
	 * Start Diff:    Left.A
	 * </pre>
	 */
	@Test
	public void testMergeNonConflictingWithBStartA() {
		final boolean isLeftToRight = true;
		final MergeMode mergeMode = LEFT_TO_RIGHT;

		// Create diffs
		Diff leftA = createDiff("LeftA", LEFT);
		Diff leftB = createDiff("LeftB", LEFT);
		Diff leftC = createDiff("LeftC", LEFT);
		leftA.getRefines().add(leftB);
		leftA.getRefines().add(leftC);

		// Create diffs
		Diff rightA = createDiff("RightA", RIGHT);
		Diff rightB = createDiff("RightB", RIGHT);
		Diff rightC = createDiff("RightC", RIGHT);
		rightA.getRefines().add(rightB);
		rightA.getRefines().add(rightC);

		// Add conflicts
		Conflict conflict = createConflict(REAL, leftB, rightB);

		Comparison comparison = createComparison(asList(leftA, leftC, leftB, rightB, rightC, rightA),
				asList(conflict));

		mergeNonConflictingChanges(comparison, mergeMode, isLeftToRight);
		verifyAllUnresolved(comparison.getDifferences());
	}

	/**
	 * With the following refinement graph. Upper layers are refined by lower layers:
	 * 
	 * <pre>
	 *   Left   |  Right
	 *  --------+---------
	 *     A    |    A     
	 *    / \   |   / \
	 *   B   C  |  B   C
	 * 
	 * Merge:         Left To Right
	 * Real Conflict: Left.A, Right.B
	 * Expected:      No Diffs merged
	 * Start Diff:    Left.B
	 * </pre>
	 */
	@Test
	public void testMergeNonConflictingWithAStartB() {
		final boolean isLeftToRight = true;
		final MergeMode mergeMode = LEFT_TO_RIGHT;

		// Create diffs
		Diff leftA = createDiff("LeftA", LEFT);
		Diff leftB = createDiff("LeftB", LEFT);
		Diff leftC = createDiff("LeftC", LEFT);
		leftA.getRefines().add(leftB);
		leftA.getRefines().add(leftC);

		// Create diffs
		Diff rightA = createDiff("RightA", RIGHT);
		Diff rightB = createDiff("RightB", RIGHT);
		Diff rightC = createDiff("RightC", RIGHT);
		rightA.getRefines().add(rightB);
		rightA.getRefines().add(rightC);

		// Add conflicts
		Conflict conflict = createConflict(REAL, leftA, rightB);

		Comparison comparison = createComparison(asList(leftB, leftC, leftA, rightB, rightC, rightA),
				asList(conflict));

		mergeNonConflictingChanges(comparison, mergeMode, isLeftToRight);
		verifyAllUnresolved(comparison.getDifferences());
	}

	/**
	 * With the following refinement graph. Upper layers are refined by lower layers:
	 * 
	 * <pre>
	 *   Left   |  Right
	 *  --------+---------
	 *     A    |    A     
	 *    / \   |   / \
	 *   B   C  |  B   C
	 * 
	 * Merge:         Left To Right
	 * Real Conflict: Left.A, Right.B
	 * Expected:      No Diffs merged
	 * Start Diff:    Left.C
	 * </pre>
	 */
	@Test
	public void testMergeNonConflictingWithAStartC() {
		final boolean isLeftToRight = true;
		final MergeMode mergeMode = LEFT_TO_RIGHT;

		// Create diffs
		Diff leftA = createDiff("LeftA", LEFT);
		Diff leftB = createDiff("LeftB", LEFT);
		Diff leftC = createDiff("LeftC", LEFT);
		leftA.getRefines().add(leftB);
		leftA.getRefines().add(leftC);

		// Create diffs
		Diff rightA = createDiff("RightA", RIGHT);
		Diff rightB = createDiff("RightB", RIGHT);
		Diff rightC = createDiff("RightC", RIGHT);
		rightA.getRefines().add(rightB);
		rightA.getRefines().add(rightC);

		// Add conflicts
		Conflict conflict = createConflict(REAL, leftA, rightB);

		Comparison comparison = createComparison(asList(leftC, leftB, leftA, rightB, rightC, rightA),
				asList(conflict));

		mergeNonConflictingChanges(comparison, mergeMode, isLeftToRight);
		verifyAllUnresolved(comparison.getDifferences());
	}

	/**
	 * With the following refinement graph. Upper layers are refined by lower layers:
	 * 
	 * <pre>
	 *   Left   |  Right
	 *  --------+---------
	 *     A    |    A     
	 *    / \   |   / \
	 *   B   C  |  B   C
	 * 
	 * Merge:         Left To Right
	 * Real Conflict: Left.A, Right.B
	 * Expected:      No Diffs merged
	 * Start Diff:    Left.A
	 * </pre>
	 */
	@Test
	public void testMergeNonConflictingWithAStartA() {
		final boolean isLeftToRight = true;
		final MergeMode mergeMode = LEFT_TO_RIGHT;

		// Create diffs
		Diff leftA = createDiff("LeftA", LEFT);
		Diff leftB = createDiff("LeftB", LEFT);
		Diff leftC = createDiff("LeftC", LEFT);
		leftA.getRefines().add(leftB);
		leftA.getRefines().add(leftC);

		// Create diffs
		Diff rightA = createDiff("RightA", RIGHT);
		Diff rightB = createDiff("RightB", RIGHT);
		Diff rightC = createDiff("RightC", RIGHT);
		rightA.getRefines().add(rightB);
		rightA.getRefines().add(rightC);

		// Add conflicts
		Conflict conflict = createConflict(REAL, leftA, rightB);

		Comparison comparison = createComparison(asList(leftA, leftC, leftB, rightB, rightC, rightA),
				asList(conflict));

		mergeNonConflictingChanges(comparison, mergeMode, isLeftToRight);
		verifyAllUnresolved(comparison.getDifferences());
	}

	/**
	 * With the following refinement graph. Upper layers are refined by lower layers:
	 * 
	 * <pre>
	 *     Left   |    Right
	 *  ----------+-----------
	 *       A    |      A     
	 *      / \   |     / \
	 *     B   C  |    B   C
	 *    / \     |
	 *   D   E    |
	 * 
	 * Merge:         Left To Right
	 * Real Conflict: Left.C, Right.B
	 * Expected:      No Diffs merged
	 * Start Diff:    Left.E
	 * </pre>
	 */
	@Test
	public void testMergeNonConflictingTwoTiersWithCStartE() {
		final boolean isLeftToRight = true;
		final MergeMode mergeMode = LEFT_TO_RIGHT;

		// Create diffs
		Diff leftA = createDiff("LeftA", LEFT);
		Diff leftB = createDiff("LeftB", LEFT);
		Diff leftC = createDiff("LeftC", LEFT);
		Diff leftD = createDiff("LeftD", LEFT);
		Diff leftE = createDiff("LeftE", LEFT);
		leftA.getRefines().add(leftB);
		leftA.getRefines().add(leftC);
		leftB.getRefines().add(leftD);
		leftB.getRefines().add(leftE);

		// Create diffs
		Diff rightA = createDiff("RightA", RIGHT);
		Diff rightB = createDiff("RightB", RIGHT);
		Diff rightC = createDiff("RightC", RIGHT);
		rightA.getRefines().add(rightB);
		rightA.getRefines().add(rightC);

		// Add conflicts
		Conflict conflict = createConflict(REAL, leftC, rightB);

		Comparison comparison = createComparison(
				asList(leftE, leftC, leftB, leftA, leftD, rightB, rightC, rightA), asList(conflict));

		mergeNonConflictingChanges(comparison, mergeMode, isLeftToRight);
		verifyAllUnresolved(comparison.getDifferences());
	}

	/**
	 * With the following refinement graph. Upper layers are refined by lower layers:
	 * 
	 * <pre>
	 *        Left    |    Right
	 *  --------------+-----------
	 *       A   B    |      A     
	 *      / \ / \   |     / \
	 *     C   D   E  |    B   C
	 * 
	 * Merge:         Left To Right
	 * Real Conflict: Left.D, Right.B
	 * Expected:      No Diffs merged
	 * Start Diff:    Left.C
	 * </pre>
	 */
	@Test
	public void testMergeNonConflictingSharedRefiningWithCStartC() {
		final boolean isLeftToRight = true;
		final MergeMode mergeMode = LEFT_TO_RIGHT;

		// Create diffs
		Diff leftA = createDiff("LeftA", LEFT);
		Diff leftB = createDiff("LeftB", LEFT);
		Diff leftC = createDiff("LeftC", LEFT);
		Diff leftD = createDiff("LeftD", LEFT);
		Diff leftE = createDiff("LeftE", LEFT);
		leftA.getRefines().add(leftC);
		leftA.getRefines().add(leftD);
		leftB.getRefines().add(leftD);
		leftB.getRefines().add(leftE);

		// Create diffs
		Diff rightA = createDiff("RightA", RIGHT);
		Diff rightB = createDiff("RightB", RIGHT);
		Diff rightC = createDiff("RightC", RIGHT);
		rightA.getRefines().add(rightB);
		rightA.getRefines().add(rightC);

		// Add conflicts
		Conflict conflict = createConflict(REAL, leftD, rightB);

		Comparison comparison = createComparison(
				asList(leftC, leftE, leftB, leftA, leftD, rightB, rightC, rightA), asList(conflict));

		mergeNonConflictingChanges(comparison, mergeMode, isLeftToRight);
		verifyAllUnresolved(comparison.getDifferences());
	}

	/**
	 * With the following refinement graph. Upper layers refine lower layers:
	 * 
	 * <pre>
	 *        Left    |    Right
	 *  --------------+-----------
	 *       A   B    |      A     
	 *      / \ / \   |     / \
	 *     C   D   E  |    B   C
	 * 
	 * Merge:         Left To Right
	 * Real Conflict: Left.D, Right.B
	 * Expected:      No Diffs merged
	 * Start Diff:    Left.A
	 * </pre>
	 */
	@Test
	public void testMergeNonConflictingSharedRefiningWithCStartA() {
		final boolean isLeftToRight = true;
		final MergeMode mergeMode = LEFT_TO_RIGHT;

		// Create diffs
		Diff leftA = createDiff("LeftA", LEFT);
		Diff leftB = createDiff("LeftB", LEFT);
		Diff leftC = createDiff("LeftC", LEFT);
		Diff leftD = createDiff("LeftD", LEFT);
		Diff leftE = createDiff("LeftE", LEFT);
		leftA.getRefines().add(leftC);
		leftA.getRefines().add(leftD);
		leftB.getRefines().add(leftD);
		leftB.getRefines().add(leftE);

		// Create diffs
		Diff rightA = createDiff("RightA", RIGHT);
		Diff rightB = createDiff("RightB", RIGHT);
		Diff rightC = createDiff("RightC", RIGHT);
		rightA.getRefines().add(rightB);
		rightA.getRefines().add(rightC);

		// Add conflicts
		Conflict conflict = createConflict(REAL, leftD, rightB);

		Comparison comparison = createComparison(
				asList(leftA, leftB, leftE, leftC, leftD, rightB, rightC, rightA), asList(conflict));

		mergeNonConflictingChanges(comparison, mergeMode, isLeftToRight);
		verifyAllUnresolved(comparison.getDifferences());
	}

	/**
	 * Verifies that all differences are unresolved.
	 * 
	 * @param diffs
	 *            differences
	 */
	protected void verifyAllUnresolved(List<Diff> diffs) {
		for (Diff diff : diffs) {
			Assert.assertSame(UNRESOLVED, diff.getState());
		}
	}

	/**
	 * Accepts all non-conflicting changes. The left changes will be accepted and the right changes will be
	 * merged into the left-hand side.
	 * 
	 * @param comparison
	 *            comparison with differences
	 * @param leftToRight
	 *            direction of merge
	 * @return affected differences
	 */

	private void mergeNonConflictingChanges(Comparison comparison, MergeMode mergeMode, boolean leftToRight) {
		final boolean isLeftEditable;
		final boolean isRightEditable;
		switch (mergeMode) {
			case LEFT_TO_RIGHT:
				// fall through
			case RIGHT_TO_LEFT:
				isLeftEditable = true;
				isRightEditable = true;
				break;
			case ACCEPT:
				// fall through
			case REJECT:
				isLeftEditable = true;
				isRightEditable = false;
				break;
			default:
				throw new IllegalArgumentException();
		}
		MergeNonConflictingRunnable mergeNonConflicting = new MergeNonConflictingRunnable(isLeftEditable,
				isRightEditable, mergeMode, new DiffRelationshipComputer(MERGER_REGISTRY));
		mergeNonConflicting.merge(comparison, leftToRight, MERGER_REGISTRY);
	}
}
