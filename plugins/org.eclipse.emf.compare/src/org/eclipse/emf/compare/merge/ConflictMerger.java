/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import java.util.Iterator;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompareMessages;

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
		return target.getConflict() != null && target.getConflict().getKind() == ConflictKind.REAL;
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
		if (target.getState() != DifferenceState.UNRESOLVED) {
			return;
		}

		if (target.getSource() == DifferenceSource.LEFT && target.getKind() != DifferenceKind.MOVE) {
			// Call the appropriate merger for each conflicted diff
			Conflict conflict = target.getConflict();
			for (Diff conflictedDiff : conflict.getDifferences()) {
				if (target != conflictedDiff) {
					if (conflictedDiff.getSource() == DifferenceSource.RIGHT) {
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
		if (target.getState() != DifferenceState.UNRESOLVED) {
			return;
		}

		if (target.getSource() == DifferenceSource.RIGHT && target.getKind() != DifferenceKind.MOVE) {
			// Call the appropriate merger for each conflicted diff
			Conflict conflict = target.getConflict();
			for (Diff conflictedDiff : conflict.getDifferences()) {
				if (target != conflictedDiff) {
					if (conflictedDiff.getSource() == DifferenceSource.LEFT) {
						mergeConflictedDiff(conflictedDiff, false, monitor);
					}
				}
			}
		}

		// Call the appropriate merger for the current diff
		getHighestRankingMerger(target).copyRightToLeft(target, monitor);
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
		if (conflictedDiff.getKind() != DifferenceKind.MOVE) {
			IMerger highestRankingMerger = getHighestRankingMerger(conflictedDiff);
			if (leftToRight) {
				highestRankingMerger.copyLeftToRight(conflictedDiff, monitor);
			} else {
				highestRankingMerger.copyRightToLeft(conflictedDiff, monitor);
			}
		} else {
			conflictedDiff.setState(DifferenceState.MERGED);
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
