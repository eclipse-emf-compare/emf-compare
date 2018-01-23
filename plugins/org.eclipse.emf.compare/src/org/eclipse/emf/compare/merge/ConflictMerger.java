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
import static org.eclipse.emf.compare.DifferenceState.DISCARDED;
import static org.eclipse.emf.compare.merge.IMergeCriterion.NONE;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.utils.IEqualityHelper;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.InternalEList;

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
		final Conflict conflict = target.getConflict();
		return conflict != null && conflict.getKind() == REAL;
	}

	@Override
	public boolean apply(IMergeCriterion criterion) {
		return criterion == null || criterion == NONE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#copyLeftToRight(org.eclipse.emf.compare.Diff,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	@Override
	public void copyLeftToRight(Diff target, Monitor monitor) {
		if (isInTerminalState(target)) {
			return;
		}

		if (target.getSource() == LEFT) {
			// Call the appropriate merger for each conflicted diff
			Conflict conflict = target.getConflict();
			Predicate<Diff> conflictVsMoveAndDelete = isConflictVsMoveAndDelete(target, MOVE, DELETE);
			for (Diff conflictedDiff : getDifferences(conflict)) {
				if (conflictedDiff.getSource() == RIGHT) {
					if (conflictVsMoveAndDelete.apply(conflictedDiff)) {
						conflictedDiff.setState(DISCARDED);
					} else {
						mergeConflictedDiff(conflictedDiff, true, monitor);
					}
				}
			}
		}

		// Call the appropriate merger for the current diff
		getMergerDelegate(target).copyLeftToRight(target, monitor);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#copyRightToLeft(org.eclipse.emf.compare.Diff,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	@Override
	public void copyRightToLeft(Diff target, Monitor monitor) {
		if (isInTerminalState(target)) {
			return;
		}

		if (target.getSource() == RIGHT) {
			// Call the appropriate merger for each conflicted diff
			Conflict conflict = target.getConflict();
			Predicate<Diff> conflictVsMoveAndDelete = isConflictVsMoveAndDelete(target, MOVE, DELETE);
			for (Diff conflictedDiff : getDifferences(conflict)) {
				if (conflictedDiff.getSource() == LEFT) {
					if (conflictVsMoveAndDelete.apply(conflictedDiff)) {
						conflictedDiff.setState(DISCARDED);
					} else {
						mergeConflictedDiff(conflictedDiff, false, monitor);
					}
				}
			}
		}

		// Call the appropriate merger for the current diff
		getMergerDelegate(target).copyRightToLeft(target, monitor);
	}

	@Override
	public Set<Diff> getDirectMergeDependencies(Diff diff, boolean rightToLeft) {
		Set<Diff> result = super.getDirectMergeDependencies(diff, rightToLeft);
		if (AbstractMerger.isAccepting(diff, rightToLeft)) {
			Conflict conflict = diff.getConflict();
			DifferenceSource source = diff.getSource();
			Predicate<Diff> conflictVsMoveAndDelete = isConflictVsMoveAndDelete(diff, DELETE, MOVE);
			// Add each conflicting diff from the other side
			for (Diff conflictingDiff : getDifferences(conflict)) {
				if (conflictingDiff.getSource() != source && conflictingDiff.getKind() != MOVE
						&& !conflictVsMoveAndDelete.apply(conflictingDiff)) {
					result.add(conflictingDiff);
				}
			}
		}
		return result;
	}

	@Override
	public Set<Diff> getDirectResultingMerges(Diff diff, boolean rightToLeft) {
		Set<Diff> result = super.getDirectResultingMerges(diff, rightToLeft);

		if (AbstractMerger.isAccepting(diff, rightToLeft)) {
			Conflict conflict = diff.getConflict();
			DifferenceSource source = diff.getSource();
			Predicate<Diff> conflictVsMoveAndDelete = isConflictVsMoveAndDelete(diff, DELETE, MOVE);
			for (Diff conflictingDiff : getDifferences(conflict)) {
				if (conflictingDiff.getSource() != source && (conflictingDiff.getKind() != MOVE
						|| conflictVsMoveAndDelete.apply(conflictingDiff))) {
					result.add(conflictingDiff);
				}
			}
		}
		return result;
	}

	/**
	 * Returns the unresolving basic list for the {@link Conflict#getDifferences() conflict's differences}.
	 * 
	 * @param conflict
	 *            the conflict.
	 * @return the unresolving basic list for the conflict's differences.
	 */
	private List<Diff> getDifferences(Conflict conflict) {
		return ((InternalEList<Diff>)conflict.getDifferences()).basicList();
	}

	/**
	 * A predicate to test if the given diff and the predicate's diff are diffs on the same object with one
	 * move and one delete. The move diff must be the one selected by the user for merging.
	 * 
	 * @param diff
	 *            the given diff.
	 * @param diffKind
	 *            the kind of the given diff.
	 * @param otherDiffKind
	 *            the kind of the predicate's diff.
	 * @return A predicate to test if the given diff and the predicate's diff are diffs on the same object
	 *         with one move and one delete.
	 */
	private Predicate<Diff> isConflictVsMoveAndDelete(final Diff diff, DifferenceKind diffKind,
			final DifferenceKind otherDiffKind) {
		if (diff.getKind() == diffKind && diff instanceof ReferenceChange) {
			return new Predicate<Diff>() {
				private EObject targetValue;

				private IEqualityHelper equalityHelper;

				public boolean apply(Diff input) {
					if (input.getKind() == otherDiffKind && input instanceof ReferenceChange) {
						if (equalityHelper == null) {
							equalityHelper = diff.getMatch().getComparison().getEqualityHelper();
							targetValue = ((ReferenceChange)diff).getValue();
						}
						return equalityHelper.matchingValues(targetValue,
								((ReferenceChange)input).getValue());
					} else {
						return false;
					}
				}
			};
		} else {
			return Predicates.alwaysFalse();
		}
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
			DelegatingMerger delegate = getMergerDelegate(conflictedDiff);
			if (leftToRight) {
				delegate.copyLeftToRight(conflictedDiff, monitor);
			} else {
				delegate.copyRightToLeft(conflictedDiff, monitor);
			}
		} else {
			conflictedDiff.setState(DISCARDED);
		}
	}

	@Override
	protected DelegatingMerger getMergerDelegate(Diff diff) {
		IMergeCriterion criterion = (IMergeCriterion)getMergeOptions()
				.get(IMergeCriterion.OPTION_MERGE_CRITERION);
		Iterator<IMerger> it = ((Registry2)getRegistry()).getMergersByRankDescending(diff, criterion);
		IMerger merger = this;
		while (it.hasNext() && merger == this) {
			merger = it.next();
		}
		if (merger == null) {
			throw new IllegalStateException("No merger found for diff " + diff.getClass().getSimpleName()); //$NON-NLS-1$
		}
		return new DelegatingMerger(merger, criterion);
	}
}
