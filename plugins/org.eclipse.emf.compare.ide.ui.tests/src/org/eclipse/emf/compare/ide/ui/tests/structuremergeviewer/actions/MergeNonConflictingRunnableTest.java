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

import static java.util.Arrays.asList;
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
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeNonConflictingRunnable;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link MergeNonConflictingRunnable} according to the specification given in <a
 * href="https://wiki.eclipse.org/EMF_Compare/Specifications/AllNonConflictingActions">the wiki</a> and
 * according to its capabilities to only merge a given collection of differences.
 * <p>
 * The goal of this test is to have a more unit-level test of the {@link MergeNonConflictingRunnable} (as
 * opposed to o.e.e.compare.ide.ui.tests.command.MergeAllCommandTests, which is more an integration test).
 * </p>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
@SuppressWarnings({"restriction", "nls" })
public class MergeNonConflictingRunnableTest {

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

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedLeftToRightOnly(leftAdd);
		verifyHasNotBeenMerged(leftDelete, rightDelete, rightAdd);
		verifyStateIsUnchanged(leftDelete, rightDelete, rightAdd);
	}

	@Test
	public void testMergeLeftToRightWithConflicts() {
		final boolean isLeftToRight = true;
		final MergeMode mergeMode = MergeMode.LEFT_TO_RIGHT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		addConflictsToMockComparison(newConflict(leftDelete, rightDelete));

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		// leftAdd is in the list and the only non-conflicting
		sut.merge(asList(leftDelete, rightDelete, leftAdd), isLeftToRight, mergerRegistry);

		// so it should be the only diff that has been merged
		verifyHasBeenMergedLeftToRightOnly(leftAdd);
		verifyHasNotBeenMerged(leftDelete, rightDelete, rightAdd);
		verifyStateIsUnchanged(leftDelete, rightDelete, rightAdd);
	}

	@Test
	public void testMergeLeftToRightWithConflictsAndLimitedSetOfDifferences() {
		final boolean isLeftToRight = true;
		final MergeMode mergeMode = MergeMode.LEFT_TO_RIGHT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		addConflictsToMockComparison(newConflict(leftDelete, rightDelete));

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		// leftAdd is not in the list, but the only non-conflicting on the left
		sut.merge(asList(leftDelete, rightDelete), isLeftToRight, mergerRegistry);

		// so no merge should be performed
		verifyHasNotBeenMerged(leftAdd, leftDelete, rightDelete, rightAdd);
		verifyStateIsUnchanged(leftAdd, leftDelete, rightDelete, rightAdd);
	}

	@Test
	public void testMergeAllLeftToRightWithoutConflicts() {
		final boolean isLeftToRight = true;
		final MergeMode mergeMode = MergeMode.LEFT_TO_RIGHT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		setNoConflictsInMockComparison();

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedLeftToRightOnly(leftDelete, leftAdd);
		verifyHasNotBeenMerged(rightDelete, rightAdd);
		verifyStateIsUnchanged(rightDelete, rightAdd);
	}

	@Test
	public void testMergeLeftToRightWithoutConflicts() {
		final boolean isLeftToRight = true;
		final MergeMode mergeMode = MergeMode.LEFT_TO_RIGHT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		setNoConflictsInMockComparison();

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		// leftDelete is in the list, but we do not include leftAdd
		sut.merge(asList(leftDelete, rightDelete), isLeftToRight, mergerRegistry);

		// so only leftDelete should be merged
		verifyHasBeenMergedLeftToRightOnly(leftDelete);
		verifyHasNotBeenMerged(leftAdd, rightDelete, rightAdd);
		verifyStateIsUnchanged(leftAdd, rightDelete, rightAdd);
	}

	@Test
	public void testMergeAllRightToLeftWithConflicts() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.RIGHT_TO_LEFT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		addConflictsToMockComparison(newConflict(leftDelete, rightDelete));

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedRightToLeftOnly(rightAdd);
		verifyHasNotBeenMerged(rightDelete, leftDelete, leftAdd);
		verifyStateIsUnchanged(rightDelete, leftDelete, leftAdd);
	}

	@Test
	public void testMergeRightToLeftWithConflicts() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.RIGHT_TO_LEFT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		addConflictsToMockComparison(newConflict(leftDelete, rightDelete));

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		// rightAdd is in the list and the only non-conflicting on the right
		sut.merge(asList(leftDelete, rightDelete, rightAdd), isLeftToRight, mergerRegistry);

		// so it should be the only diff that has been merged
		verifyHasBeenMergedRightToLeftOnly(rightAdd);
		verifyHasNotBeenMerged(leftDelete, rightDelete, leftAdd);
		verifyStateIsUnchanged(leftDelete, rightDelete, leftAdd);
	}

	@Test
	public void testMergeRightToLeftWithConflictsAndLimitedSetOfDifferences() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.RIGHT_TO_LEFT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		addConflictsToMockComparison(newConflict(leftDelete, rightDelete));

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		// rightAdd is not in the list, but the only non-conflicting
		sut.merge(asList(leftDelete, rightDelete), isLeftToRight, mergerRegistry);

		// so no merge should be performed
		verifyHasNotBeenMerged(leftAdd, leftDelete, rightDelete, rightAdd);
		verifyStateIsUnchanged(leftAdd, leftDelete, rightDelete, rightAdd);
	}

	@Test
	public void testMergeAllRightToLeftWithoutConflicts() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.RIGHT_TO_LEFT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		setNoConflictsInMockComparison();

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedRightToLeftOnly(rightDelete, rightAdd);
		verifyHasNotBeenMerged(leftDelete, leftAdd);
		verifyStateIsUnchanged(leftDelete, leftAdd);
	}

	@Test
	public void testMergeRightToLeftWithoutConflicts() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.RIGHT_TO_LEFT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		setNoConflictsInMockComparison();

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		// we do not include rightAdd, so it should not be merged
		// and leftDelete should not be merged, because we merge left to right
		sut.merge(asList(leftDelete, rightDelete), isLeftToRight, mergerRegistry);

		verifyHasBeenMergedRightToLeftOnly(rightDelete);
		verifyHasNotBeenMerged(leftAdd, leftDelete, rightAdd);
		verifyStateIsUnchanged(leftAdd, leftDelete, rightAdd);
	}

	@Test
	public void testAcceptAllWithoutConflicts() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.ACCEPT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		setNoConflictsInMockComparison();

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedRightToLeftOnly(rightDelete, rightAdd);
		verifyHasNotBeenMerged(leftDelete, leftAdd);
		verifyHasBeenMarkedAsMerged(leftAdd, leftDelete);
	}

	@Test
	public void testAcceptWithoutConflicts() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.ACCEPT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		setNoConflictsInMockComparison();

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		// rightDelete is in the list, but we do not include rightAdd
		sut.merge(asList(leftAdd, leftDelete, rightDelete), isLeftToRight, mergerRegistry);

		// so only rightDelete should be merged
		verifyHasBeenMergedRightToLeftOnly(rightDelete);
		verifyHasNotBeenMerged(leftDelete, leftAdd);
		verifyHasBeenMarkedAsMerged(leftAdd, leftDelete);
		verifyStateIsUnchanged(rightAdd);
	}

	@Test
	public void testRejectAllWithoutConflicts() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.REJECT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		setNoConflictsInMockComparison();

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedRightToLeftOnly(leftDelete, leftAdd);
		verifyHasNotBeenMerged(rightDelete, rightAdd);
		verifyHasBeenMarkedAsMerged(rightAdd, rightDelete);
	}

	@Test
	public void testRejectWithoutConflicts() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.REJECT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		setNoConflictsInMockComparison();

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		// leftDelete is in the list, but we do not include leftAdd
		sut.merge(asList(leftDelete, rightDelete, rightAdd), isLeftToRight, mergerRegistry);

		// so only leftDelete should be merged
		verifyHasBeenMergedRightToLeftOnly(leftDelete);
		verifyHasNotBeenMerged(rightDelete, rightAdd);
		verifyHasBeenMarkedAsMerged(rightAdd, rightDelete);
		verifyStateIsUnchanged(leftAdd);
	}

	@Test
	public void testAcceptAllWithConflicts() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.ACCEPT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		addConflictsToMockComparison(newConflict(leftDelete, rightDelete));

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
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

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
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

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedLeftToRightOnly(leftDelete, leftAdd);
	}

	@Test
	public void testTwoWayMergeLeftToRight() {
		final boolean isLeftToRight = true;
		final MergeMode mergeMode = MergeMode.LEFT_TO_RIGHT;

		setUpTwoWayComparisonWithOneAdditionAndOneDeletion();

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(asList(leftDelete), isLeftToRight, mergerRegistry);

		verifyHasBeenMergedLeftToRightOnly(leftDelete);
		verifyHasNotBeenMerged(leftAdd);
		verifyStateIsUnchanged(leftAdd);
	}

	@Test
	public void testTwoWayMergeAllRightToLeft() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.RIGHT_TO_LEFT;

		setUpTwoWayComparisonWithOneAdditionAndOneDeletion();

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedRightToLeftOnly(leftDelete, leftAdd);
	}

	@Test
	public void testTwoWayMergeRightToLeft() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.RIGHT_TO_LEFT;

		setUpTwoWayComparisonWithOneAdditionAndOneDeletion();

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(asList(leftDelete), isLeftToRight, mergerRegistry);

		verifyHasBeenMergedRightToLeftOnly(leftDelete);
		verifyHasNotBeenMerged(leftAdd);
		verifyStateIsUnchanged(leftAdd);
	}

	@Test
	public void testTwoWayAcceptAll() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.ACCEPT;

		setUpTwoWayComparisonWithOneAdditionAndOneDeletion();

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasNotBeenMerged(leftDelete, leftAdd);
		verifyHasBeenMarkedAsMerged(leftDelete, leftAdd);
	}

	@Test
	public void testTwoWayAccept() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.ACCEPT;

		setUpTwoWayComparisonWithOneAdditionAndOneDeletion();

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(asList(leftDelete), isLeftToRight, mergerRegistry);

		verifyHasNotBeenMerged(leftDelete);
		verifyHasBeenMarkedAsMerged(leftDelete);
		verifyStateIsUnchanged(leftAdd);
	}

	@Test
	public void testTwoWayRejectAll() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.REJECT;

		setUpTwoWayComparisonWithOneAdditionAndOneDeletion();

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(comparison, isLeftToRight, mergerRegistry);

		verifyHasBeenMergedRightToLeftOnly(leftDelete, leftAdd);
	}

	@Test
	public void testTwoWayReject() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.REJECT;

		setUpTwoWayComparisonWithOneAdditionAndOneDeletion();

		final MergeNonConflictingRunnable sut = newMergeAllNonConflictingRunnable(mergeMode);
		sut.merge(asList(leftAdd), isLeftToRight, mergerRegistry);

		verifyHasBeenMergedRightToLeftOnly(leftAdd);
		verifyHasNotBeenMerged(leftDelete);
	}

	private void addConflictsToMockComparison(Conflict... conflicts) {
		when(comparison.getConflicts()).thenReturn(newEList(conflicts));
	}

	private void addDifferencesToMockComparison(Diff... diffs) {
		when(comparison.getDifferences()).thenReturn(newEList(diffs));
		for (Diff diff : diffs) {
			final Match match = mock(Match.class);
			when(match.getComparison()).thenReturn(comparison);
			when(diff.getMatch()).thenReturn(match);
		}
	}

	private ReferenceChange mockReferenceChange(DifferenceSource side, DifferenceKind kind, String name) {
		final ReferenceChange diff = mock(ReferenceChange.class, name);
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
		when(Boolean.valueOf(comparison.isThreeWay())).thenReturn(Boolean.TRUE);
	}

	private void setTwoWayComparison() {
		when(Boolean.valueOf(comparison.isThreeWay())).thenReturn(Boolean.FALSE);
	}

	private void setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide() {
		leftDelete = mockReferenceChange(LEFT, DELETE, "leftDelete");
		leftAdd = mockReferenceChange(LEFT, ADD, "leftAdd");
		rightDelete = mockReferenceChange(RIGHT, DELETE, "rightDelete");
		rightAdd = mockReferenceChange(RIGHT, ADD, "rightAdd");

		addDifferencesToMockComparison(leftDelete, leftAdd, rightDelete, rightAdd);
		setThreeWayComparison();
	}

	private void setUpTwoWayComparisonWithOneAdditionAndOneDeletion() {
		leftDelete = mockReferenceChange(LEFT, DELETE, "leftDelete");
		leftAdd = mockReferenceChange(LEFT, ADD, "leftAdd");

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

	private MergeNonConflictingRunnable newMergeAllNonConflictingRunnable(MergeMode mergeMode) {
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
		return new MergeNonConflictingRunnable(isLeftEditable, isRightEditable, mergeMode) {
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
