/*******************************************************************************
 * Copyright (c) 2013, 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import static org.eclipse.emf.compare.ConflictKind.REAL;
import static org.eclipse.emf.compare.DifferenceKind.DELETE;
import static org.eclipse.emf.compare.DifferenceKind.MOVE;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;
import static org.eclipse.emf.compare.DifferenceState.MERGED;
import static org.eclipse.emf.compare.DifferenceState.UNRESOLVED;

import java.util.Iterator;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompareMessages;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.utils.IEqualityHelper;

/**
 * This specific implementation of {@link AbstractMerger} will be used to merge real conflicts.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.1
 */
public class ConflictMerger extends AbstractMerger {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#isMergerFor(org.eclipse.emf.compare.Diff)
	 */
	public boolean isMergerFor(Diff target) {
		return target.getConflict() != null && target.getConflict().getKind() == REAL;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#copyLeftToRight(org.eclipse.emf.compare.Diff,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	@Override
	public void copyLeftToRight(Diff target, Monitor monitor) {
		// Don't merge an already merged (or discarded) diff
		if (target.getState() != UNRESOLVED) {
			return;
		}

		if (target.getSource() == LEFT) {
			// Call the appropriate merger for each conflicted diff
			Conflict conflict = target.getConflict();
			for (Diff conflictedDiff : conflict.getDifferences()) {
				if (conflictedDiff.getSource() == RIGHT) {
					if (isConflictVsMoveAndDelete(target, conflictedDiff, true)) {
						conflictedDiff.setState(MERGED);
					} else {
						mergeConflictedDiff(conflictedDiff, true, monitor);
					}
				}
			}
		}

		// Call the appropriate merger for the current diff
		getHighestRankingMerger(target).copyLeftToRight(target, monitor);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#copyRightToLeft(org.eclipse.emf.compare.Diff,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	@Override
	public void copyRightToLeft(Diff target, Monitor monitor) {
		// Don't merge an already merged (or discarded) diff
		if (target.getState() != UNRESOLVED) {
			return;
		}

		if (target.getSource() == RIGHT) {
			// Call the appropriate merger for each conflicted diff
			Conflict conflict = target.getConflict();
			for (Diff conflictedDiff : conflict.getDifferences()) {
				if (conflictedDiff.getSource() == LEFT) {
					if (isConflictVsMoveAndDelete(target, conflictedDiff, false)) {
						conflictedDiff.setState(MERGED);
					} else {
						mergeConflictedDiff(conflictedDiff, false, monitor);
					}
				}
			}
		}

		// Call the appropriate merger for the current diff
		getHighestRankingMerger(target).copyRightToLeft(target, monitor);
	}

	/**
	 * Detect if the two given diff are diffs on the same object with one move and one delete. The move diff
	 * must be the one selected by the user for merging.
	 * 
	 * @param target
	 *            The diff selected for merge by the user
	 * @param conflictedDiff
	 *            Another diff of the conflict
	 * @param leftToRight
	 *            The direction of the merge
	 * @return <code>true</code> if the diff selected by the user is a move and is conflicting with a delete
	 *         of the same element
	 */
	private boolean isConflictVsMoveAndDelete(Diff target, Diff conflictedDiff, boolean leftToRight) {
		boolean result = false;
		if (target.getConflict() != null && target.getConflict().getKind() == REAL) {
			if (target instanceof ReferenceChange && target.getKind() == MOVE) {
				ReferenceChange moveDiff = (ReferenceChange)target;
				if (conflictedDiff instanceof ReferenceChange && conflictedDiff.getKind() == DELETE) {
					ReferenceChange deleteDiff = (ReferenceChange)conflictedDiff;
					IEqualityHelper equalityHelper = target.getMatch().getComparison().getEqualityHelper();
					result = equalityHelper.matchingAttributeValues(moveDiff.getValue(),
							deleteDiff.getValue());
				}
			}
		}

		return result;
	}

	/**
	 * Manages the merge of the given conflicted diff.
	 * 
	 * @param conflictedDiff
	 *            The given diff.
	 * @param leftToRight
	 *            The way of merge.
	 * @param monitor
	 *            Monitor.
	 */
	private void mergeConflictedDiff(Diff conflictedDiff, boolean leftToRight, Monitor monitor) {
		if (conflictedDiff.getKind() != MOVE) {
			IMerger highestRankingMerger = getHighestRankingMerger(conflictedDiff);
			if (leftToRight) {
				highestRankingMerger.copyLeftToRight(conflictedDiff, monitor);
			} else {
				highestRankingMerger.copyRightToLeft(conflictedDiff, monitor);
			}
		} else {
			conflictedDiff.setState(MERGED);
		}
	}

	/**
	 * Returns the highest ranking merger without taking into account this merger (Conflict Merger).
	 * 
	 * @param target
	 *            The given target difference.
	 * @return The found merger.
	 */
	private IMerger getHighestRankingMerger(Diff target) {
		Iterator<IMerger> mergers = getRegistry().getMergers(target).iterator();

		IMerger ret = null;

		if (mergers.hasNext()) {
			IMerger highestRanking = mergers.next();
			while (mergers.hasNext()) {
				IMerger merger = mergers.next();
				if (highestRanking == this
						|| (merger != this && (merger.getRanking() > highestRanking.getRanking()))) {
					highestRanking = merger;
				}
			}
			ret = highestRanking;
		}

		if (ret == null) {
			throw new IllegalStateException(EMFCompareMessages.getString("IMerger.MissingMerger", target //$NON-NLS-1$
					.getClass().getSimpleName()));
		}

		return ret;
	}
}
