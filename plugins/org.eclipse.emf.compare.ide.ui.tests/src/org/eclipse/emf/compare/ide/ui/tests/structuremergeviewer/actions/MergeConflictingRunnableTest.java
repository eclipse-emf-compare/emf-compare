/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions;

import static com.google.common.collect.Lists.newArrayList;
import static org.eclipse.emf.compare.DifferenceKind.ADD;
import static org.eclipse.emf.compare.DifferenceKind.DELETE;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeConflictingRunnable;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.DiffRelationshipComputer;
import org.junit.Test;

/**
 * Tests the {@link MergeConflictingRunnable}.
 * <p>
 * The following table gives an overview of the different scenarios regarding merge direction and mode, as
 * well as the expected result:
 * </p>
 * <table>
 * <tr>
 * <th>ID</th>
 * <th>Direction</th>
 * <th>Mode</th>
 * <th>Expected for left diff</th>
 * <th>Expected for right diff</th>
 * </tr>
 * <tr>
 * <td>#1</td>
 * <td>right-to-left</td>
 * <td>ACCEPT</td>
 * <td>DISCARDED</td>
 * <td>MERGED</td>
 * </tr>
 * <tr>
 * <td>#2</td>
 * <td>right-to-left</td>
 * <td>REJECT</td>
 * <td>MARK AS ACCEPTED</td>
 * <td>MARK AS REJECTED</td>
 * </tr>
 * <tr>
 * <td>#3</td>
 * <td>left-to-right</td>
 * <td>LEFT-TO-RIGHT</td>
 * <td>MERGED</td>
 * <td>DISCARDED</td>
 * </tr>
 * <tr>
 * <td>#4</td>
 * <td>right-to-left</td>
 * <td>RIGHT-TO-LEFT</td>
 * <td>DISCARDED</td>
 * <td>MERGED</td>
 * </tr>
 * <tr>
 * <td>#5</td>
 * <td>right-to-left</td>
 * <td>LEFT-TO-RIGHT</td>
 * <td>ILLEGAL</td>
 * <td>ILLEGAL</td>
 * </tr>
 * <tr>
 * <td>#6</td>
 * <td>left-to-right</td>
 * <td>RIGHT-TO-LEFT</td>
 * <td>ILLEGAL</td>
 * <td>ILLEGAL</td>
 * </tr>
 * <tr>
 * <td>#7</td>
 * <td>left-to-right</td>
 * <td>REJECT</td>
 * <td>ILLEGAL</td>
 * <td>ILLEGAL</td>
 * </tr>
 * <tr>
 * <td>#8</td>
 * <td>left-to-right</td>
 * <td>ACCEPT</td>
 * <td>ILLEGAL</td>
 * <td>ILLEGAL</td>
 * </tr>
 * </table>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
@SuppressWarnings({"restriction", "nls" })
public class MergeConflictingRunnableTest extends AbstractMergeRunnableTest {

	private Diff leftDelete;

	private Diff leftAdd;

	private Diff rightDelete;

	private Diff rightAdd;

	/**
	 * Tests whether no conflicts have been merged, if we call {@link MergeConflictingRunnable} with
	 * non-conflicting diffs only in a right-to-left accept merge.
	 */
	@Test
	public void testThatNoDiffIsMergedInRightToLeftAcceptIfNotConflicting() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.ACCEPT;
		setupAndAssertThatNoDiffIsMergedIfNotConflicting(isLeftToRight, mergeMode);
	}

	/**
	 * Tests whether no conflicts have been merged, if we call {@link MergeConflictingRunnable} with
	 * non-conflicting diffs only in a right-to-left reject merge.
	 */
	@Test
	public void testThatNoDiffIsMergedInRightToLeftRejectIfNotConflicting() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.REJECT;
		setupAndAssertThatNoDiffIsMergedIfNotConflicting(isLeftToRight, mergeMode);
	}

	/**
	 * Tests whether no conflicts have been merged, if we call {@link MergeConflictingRunnable} with
	 * non-conflicting diffs only in a right-to-left merge.
	 */
	@Test
	public void testThatNoDiffIsMergedInRightToLeftIfNotConflicting() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.RIGHT_TO_LEFT;
		setupAndAssertThatNoDiffIsMergedIfNotConflicting(isLeftToRight, mergeMode);
	}

	/**
	 * Tests whether no conflicts have been merged, if we call {@link MergeConflictingRunnable} with
	 * non-conflicting diffs only in a left-to-right merge.
	 */
	@Test
	public void testThatNoDiffIsMergedInLeftToRightIfNotConflicting() {
		final boolean isLeftToRight = true;
		final MergeMode mergeMode = MergeMode.LEFT_TO_RIGHT;
		setupAndAssertThatNoDiffIsMergedIfNotConflicting(isLeftToRight, mergeMode);
	}

	private void setupAndAssertThatNoDiffIsMergedIfNotConflicting(boolean isLeftToRight,
			MergeMode mergeMode) {
		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		// no conflicts

		final MergeConflictingRunnable sut = newMergeConflictingRunnable(mergeMode);

		for (Diff diff : newArrayList(leftAdd, leftDelete, rightDelete, rightAdd)) {
			sut.merge(newArrayList(diff), isLeftToRight, mergerRegistry);
			verifyHasNotBeenMerged(leftDelete, leftAdd, rightDelete, rightAdd);
			verifyStateIsUnchanged(leftDelete, rightDelete, rightAdd);
		}
	}

	/**
	 * Tests scenario #1 (see javadoc of this class).
	 * <p>
	 * Tests whether invoking the runnable under test by accepting the right-hand side diff of a two-diff
	 * conflict, will merge the left-hand side conflicting diff as well and the right-hand side conflicting
	 * diff right-to-left.
	 * </p>
	 */
	@Test
	public void testMergeRightToLeftAcceptWithConflict() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.ACCEPT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		addConflictsToMockComparison(newConflict(leftDelete, rightDelete));

		final MergeConflictingRunnable sut = newMergeConflictingRunnable(mergeMode);
		sut.merge(newArrayList(rightDelete), isLeftToRight, mergerRegistry);

		verifyHasBeenMergedRightToLeftOnly(leftDelete, rightDelete);
		verifyHasNotBeenMerged(leftAdd, rightAdd);
		verifyStateIsUnchanged(leftAdd, rightAdd);
	}

	/**
	 * Tests scenario #2 (see javadoc of this class).
	 * <p>
	 * Tests whether invoking the runnable under test by rejecting the right-hand side diff of a two-diff
	 * conflict, will accept the left-hand side conflicting diff (that is, the left-hand side diff is marked
	 * as merged) but that actually no diff has been really merged at all.
	 * </p>
	 */
	@Test
	public void testMergeRightToLeftRejectWithConflict() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.REJECT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		addConflictsToMockComparison(newConflict(leftDelete, rightDelete));

		final MergeConflictingRunnable sut = newMergeConflictingRunnable(mergeMode);
		sut.merge(newArrayList(rightDelete), isLeftToRight, mergerRegistry);

		verifyHasBeenMarkedAsMerged(leftDelete);
		verifyHasBeenMarkedAsDiscarded(rightDelete);
		verifyHasNotBeenMerged(leftDelete, rightDelete, leftAdd, rightAdd);
		verifyStateIsUnchanged(leftAdd, rightAdd);
	}

	/**
	 * Tests scenario #3 (see javadoc of this class).
	 * <p>
	 * Tests whether invoking the runnable under test by merging the right-hand side diff of a two-diff
	 * conflict left-to-right, will merge the left-hand side conflicting diff as well and the right-hand side
	 * conflicting diff left-to-right.
	 * </p>
	 */
	@Test
	public void testMergeLeftToRightWithConflict() {
		final boolean isLeftToRight = true;
		final MergeMode mergeMode = MergeMode.LEFT_TO_RIGHT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		addConflictsToMockComparison(newConflict(leftDelete, rightDelete));

		final MergeConflictingRunnable sut = newMergeConflictingRunnable(mergeMode);
		sut.merge(newArrayList(rightDelete), isLeftToRight, mergerRegistry);

		verifyHasBeenMergedLeftToRightOnly(leftDelete, rightDelete);
		verifyHasNotBeenMerged(leftAdd, rightAdd);
		verifyStateIsUnchanged(leftAdd, rightAdd);
	}

	/**
	 * Tests scenario #4 (see javadoc of this class).
	 * <p>
	 * Tests whether invoking the runnable under test by merging the right-hand side diff of a two-diff
	 * conflict right-to-left, will merge the left-hand side conflicting diff as well and the right-hand side
	 * conflicting diff right-to-left.
	 * </p>
	 */
	@Test
	public void testMergeRightToLeftWithConflict() {
		final boolean isLeftToRight = false;
		final MergeMode mergeMode = MergeMode.RIGHT_TO_LEFT;

		setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide();
		addConflictsToMockComparison(newConflict(leftDelete, rightDelete));

		final MergeConflictingRunnable sut = newMergeConflictingRunnable(mergeMode);
		sut.merge(newArrayList(rightDelete), isLeftToRight, mergerRegistry);

		verifyHasBeenMergedRightToLeftOnly(leftDelete, rightDelete);
		verifyHasNotBeenMerged(leftAdd, rightAdd);
		verifyStateIsUnchanged(leftAdd, rightAdd);
	}

	private void setUpThreeWayComparisonWithOneAdditionAndDeletionOnEachSide() {
		leftDelete = mockReferenceChange(LEFT, DELETE, "leftDelete");
		leftAdd = mockReferenceChange(LEFT, ADD, "leftAdd");
		rightDelete = mockReferenceChange(RIGHT, DELETE, "rightDelete");
		rightAdd = mockReferenceChange(RIGHT, ADD, "rightAdd");

		addDifferencesToMockComparison(leftDelete, leftAdd, rightDelete, rightAdd);
		setThreeWayComparison();
	}

	private MergeConflictingRunnable newMergeConflictingRunnable(MergeMode mergeMode) {
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
		return new MergeConflictingRunnable(isLeftEditable, isRightEditable, mergeMode,
				new DiffRelationshipComputer(mergerRegistry));
	}

}
