/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions;

import static org.eclipse.emf.compare.DifferenceKind.ADD;
import static org.eclipse.emf.compare.DifferenceKind.DELETE;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;
import static org.eclipse.emf.compare.DifferenceState.MERGED;
import static org.eclipse.emf.compare.DifferenceState.UNRESOLVED;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeAllNonConflictingRunnable;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link MergeAllNonConflictingRunnable} according to the specification given in <a
 * href="https://wiki.eclipse.org/EMF_Compare/Specifications/AllNonConflictingActions">the wiki</a>.
 * <p>
 * The goal of this test is to have a more unit-level test of the {@link MergeAllNonConflictingRunnable} (as
 * opposed to o.e.e.compare.ide.ui.tests.command.MergeAllCommandTests, which is more an integration test), as
 * well as to have a test in preparation of the refactorings panned for fixing bug 462884.
 * </p>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
@SuppressWarnings("restriction")
public class MergeAllNonConflictingRunnableTest {

	private Comparison comparison;

	private Registry mergerRegistry;

	private IMerger merger;

	private Diff leftDelete;

	private Diff leftAdd;

	private Diff rightDelete;

	private Diff rightAdd;

	@Before
	public void setupMocks() {
		comparison = mock(Comparison.class);
		mergerRegistry = mock(Registry.class);
		merger = mock(IMerger.class);
		when(mergerRegistry.getHighestRankingMerger(any(Diff.class))).thenReturn(merger);
	}

	@Test
	public void testMergeAllLeftToRightWithConflicts() {
		final boolean isLeftToRight = true;
		final MergeMode mergeMode = MergeMode.LEFT_TO_RIGHT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		addConflictsToMockComparison(newConflict(leftDelete, rightDelete));

		final MergeAllNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedLeftToRightOnly(leftAdd);
		verifyHasNotBeenMerged(leftDelete, rightDelete, rightAdd);
		verifyStateIsUnchanged(leftDelete, rightDelete, rightAdd);
	}

	@Test
	public void testMergeAllLeftToRightWithoutConflicts() {
		final boolean isLeftToRight = true;
		final MergeMode mergeMode = MergeMode.LEFT_TO_RIGHT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		setNoConflictsInMockComparison();

		final MergeAllNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedLeftToRightOnly(leftDelete, leftAdd);
		verifyHasNotBeenMerged(rightDelete, rightAdd);
		verifyStateIsUnchanged(rightDelete, rightAdd);
	}

	@Test
	public void testMergeAllRightToLeftWithConflicts() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.RIGHT_TO_LEFT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		addConflictsToMockComparison(newConflict(leftDelete, rightDelete));

		final MergeAllNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedRightToLeftOnly(rightAdd);
		verifyHasNotBeenMerged(rightDelete, leftDelete, leftAdd);
		verifyStateIsUnchanged(rightDelete, leftDelete, leftAdd);
	}

	@Test
	public void testMergeAllRightToLeftWithoutConflicts() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.RIGHT_TO_LEFT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		setNoConflictsInMockComparison();

		final MergeAllNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedRightToLeftOnly(rightDelete, rightAdd);
		verifyHasNotBeenMerged(leftDelete, leftAdd);
		verifyStateIsUnchanged(leftDelete, leftAdd);
	}

	@Test
	public void testAcceptAllWithoutConflicts() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.ACCEPT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		setNoConflictsInMockComparison();

		final MergeAllNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedRightToLeftOnly(rightDelete, rightAdd);
		verifyHasNotBeenMerged(leftDelete, leftAdd);
		verifyHasBeenMarkedAsMerged(leftAdd, leftDelete);
	}

	@Test
	public void testRejectAllWithoutConflicts() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.REJECT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		setNoConflictsInMockComparison();

		final MergeAllNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedRightToLeftOnly(leftDelete, leftAdd);
		verifyHasNotBeenMerged(rightDelete, rightAdd);
		verifyHasBeenMarkedAsMerged(rightAdd, rightDelete);
	}

	@Test
	public void testAcceptAllWithConflicts() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.ACCEPT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		addConflictsToMockComparison(newConflict(leftDelete, rightDelete));

		final MergeAllNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedRightToLeftOnly(rightAdd);
		verifyHasNotBeenMerged(leftDelete, rightDelete, leftAdd);
		verifyHasBeenMarkedAsMerged(leftAdd);
		verifyStateIsUnchanged(leftDelete, rightDelete);
	}

	@Test
	public void testRejectAllWithConflicts() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.REJECT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		addConflictsToMockComparison(newConflict(leftDelete, rightDelete));

		final MergeAllNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedRightToLeftOnly(leftAdd);
		verifyHasNotBeenMerged(rightDelete, leftDelete, rightAdd);
		verifyHasBeenMarkedAsMerged(rightAdd);
		verifyStateIsUnchanged(leftDelete, rightDelete);
	}

	@Test
	public void testTwoWayMergeAllLeftToRight() {
		final boolean isLeftToRight = true;
		final MergeMode mergeMode = MergeMode.LEFT_TO_RIGHT;

		setUpTwoWayComparisonWithOneAdditionAndOneDeletion();

		final MergeAllNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedLeftToRightOnly(leftDelete, leftAdd);
	}

	@Test
	public void testTwoWayMergeAllRightToLeft() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.RIGHT_TO_LEFT;

		setUpTwoWayComparisonWithOneAdditionAndOneDeletion();

		final MergeAllNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedRightToLeftOnly(leftDelete, leftAdd);
	}

	@Test
	public void testTwoWayAcceptAll() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.ACCEPT;

		setUpTwoWayComparisonWithOneAdditionAndOneDeletion();

		final MergeAllNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasNotBeenMerged(leftDelete, leftAdd);
		verifyHasBeenMarkedAsMerged(leftDelete, leftAdd);
	}

	@Test
	public void testTwoWayRejectAll() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.REJECT;

		setUpTwoWayComparisonWithOneAdditionAndOneDeletion();

		final MergeAllNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedRightToLeftOnly(leftDelete, leftAdd);
	}

	private void addConflictsToMockComparison(Conflict... conflicts) {
		when(comparison.getConflicts()).thenReturn(newEList(conflicts));
	}

	private void addDifferencesToMockComparison(Diff... diffs) {
		when(comparison.getDifferences()).thenReturn(newEList(diffs));
	}

	private ReferenceChange mockReferenceChange(DifferenceSource side, DifferenceKind kind) {
		final ReferenceChange diff = mock(ReferenceChange.class);
		when(diff.getSource()).thenReturn(side);
		when(diff.getKind()).thenReturn(kind);
		when(diff.getState()).thenReturn(UNRESOLVED);
		return diff;
	}

	private Conflict newConflict(Diff... diffs) {
		Conflict conflict = mock(Conflict.class);
		when(conflict.getKind()).thenReturn(ConflictKind.REAL);
		when(conflict.getDifferences()).thenReturn(newEList(diffs));
		for (Diff diff : diffs) {
			when(diff.getConflict()).thenReturn(conflict);
		}
		return conflict;
	}

	private EList<Conflict> newEList(Conflict... diffs) {
		final EList<Conflict> list = new BasicEList<Conflict>();
		for (Conflict diff : diffs) {
			list.add(diff);
		}
		return list;
	}

	private EList<Diff> newEList(Diff... diffs) {
		final EList<Diff> list = new BasicEList<Diff>();
		for (Diff diff : diffs) {
			list.add(diff);
		}
		return list;
	}

	private void setNoConflictsInMockComparison() {
		addConflictsToMockComparison(new Conflict[0]);
	}

	private void setThreeWayComparison() {
		when(comparison.isThreeWay()).thenReturn(true);
	}

	private void setTwoWayComparison() {
		when(comparison.isThreeWay()).thenReturn(false);
	}

	private void setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide() {
		leftDelete = mockReferenceChange(LEFT, DELETE);
		leftAdd = mockReferenceChange(LEFT, ADD);
		rightDelete = mockReferenceChange(RIGHT, DELETE);
		rightAdd = mockReferenceChange(RIGHT, ADD);

		addDifferencesToMockComparison(leftDelete, leftAdd, rightDelete, rightAdd);
		setThreeWayComparison();
	}

	private void setUpTwoWayComparisonWithOneAdditionAndOneDeletion() {
		leftDelete = mockReferenceChange(LEFT, DELETE);
		leftAdd = mockReferenceChange(LEFT, ADD);

		addDifferencesToMockComparison(leftDelete, leftAdd);
		setNoConflictsInMockComparison();
		setTwoWayComparison();
	}

	private void verifyHasBeenMarkedAsMerged(Diff... diffs) {
		for (Diff diff : diffs) {
			verifyHasBeenMarkedAsMerged(diff);
		}
	}

	private void verifyHasBeenMarkedAsMerged(Diff diff) {
		verify(diff).setState(eq(MERGED));
	}

	private void verifyHasBeenMergedLeftToRightOnly(Diff diff) {
		verify(merger, atLeastOnce()).copyLeftToRight(same(diff), any(Monitor.class));
		verify(merger, never()).copyRightToLeft(same(diff), any(Monitor.class));
	}

	private void verifyHasBeenMergedLeftToRightOnly(Diff... diffs) {
		for (Diff diff : diffs) {
			verifyHasBeenMergedLeftToRightOnly(diff);
		}
	}

	private void verifyHasBeenMergedRightToLeftOnly(Diff... diffs) {
		for (Diff diff : diffs) {
			verifyHasBeenMergedRightToLeftOnly(diff);
		}
	}

	private void verifyHasBeenMergedRightToLeftOnly(Diff diff) {
		verify(merger, atLeastOnce()).copyRightToLeft(same(diff), any(Monitor.class));
		verify(merger, never()).copyLeftToRight(same(diff), any(Monitor.class));
	}

	private void verifyHasNotBeenMerged(Diff diff) {
		verify(merger, never()).copyLeftToRight(same(diff), any(Monitor.class));
		verify(merger, never()).copyRightToLeft(same(diff), any(Monitor.class));
	}

	private void verifyHasNotBeenMerged(Diff... diffs) {
		for (Diff diff : diffs) {
			verifyHasNotBeenMerged(diff);
		}
	}

	private void verifyStateIsUnchanged(Diff diff) {
		verify(diff, never()).setState(any(DifferenceState.class));
	}

	private void verifyStateIsUnchanged(Diff... diffs) {
		for (Diff diff : diffs) {
			verifyStateIsUnchanged(diff);
		}
	}

	private MergeAllNonConflictingRunnable newMergeAllNonConflictingRunnable(MergeMode mergeMode) {
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
		return new MergeAllNonConflictingRunnable(isLeftEditable, isRightEditable, mergeMode) {
			@Override
			protected void addOrUpdateMergeData(Collection<Diff> differences, MergeMode mode) {
				// do nothing to prevent call of EcoreUtil.getAdapter()
			}

			@Override
			protected void markAsMerged(Diff diff, MergeMode mode, boolean mergeRightToLeft, Registry registry) {
				// overwritten to prevent call of EcoreUtil.getAdapter()
				// note that we rely on setting diff state to merged in verifyHasBeenMarkedAsMerged(Diff)
				diff.setState(MERGED);
			}
		};
	}

}
