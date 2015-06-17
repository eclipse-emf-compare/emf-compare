/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 469355, bug 462884, refactorings
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Lists.newArrayList;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.containsConflictOfTypes;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.domain.IMergeRunnable;
import org.eclipse.emf.compare.internal.domain.IMergeAllNonConflictingRunnable;
import org.eclipse.emf.compare.internal.merge.MergeDependenciesUtil;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.internal.merge.MergeOperation;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.internal.utils.Graph;
import org.eclipse.emf.compare.internal.utils.PruningIterator;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.merge.IMerger2;

/**
 * Implements the "merge non-conflicting" and "merge all non-conflicting" action.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class MergeNonConflictingRunnable extends AbstractMergeRunnable implements IMergeAllNonConflictingRunnable, IMergeRunnable {
	/**
	 * Default constructor.
	 * 
	 * @param isLeftEditable
	 *            Whether the left side of the comparison we're operating on is editable.
	 * @param isRightEditable
	 *            Whether the right side of the comparison we're operating on is editable.
	 * @param mergeMode
	 *            Merge mode for this operation.
	 */
	public MergeNonConflictingRunnable(boolean isLeftEditable, boolean isRightEditable, MergeMode mergeMode) {
		super(isLeftEditable, isRightEditable, mergeMode);
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterable<Diff> merge(Comparison comparison, boolean leftToRight, Registry mergerRegistry) {
		checkState(getMergeMode().isLeftToRight(isLeftEditable(), isRightEditable()) == leftToRight);
		return doMergeNonConflicting(comparison.getDifferences(), comparison, leftToRight, mergerRegistry);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Differences that are conflicting or that depend on conflicting differences will be left out.
	 * Non-conflicting differences that are implied or required by the given differences will be merged, also
	 * if they are not explicitly included in the given list of {@code differences}.
	 * </p>
	 */
	@SuppressWarnings("unchecked")
	public void merge(List<? extends Diff> differences, boolean leftToRight, Registry mergerRegistry) {
		checkState(getMergeMode().isLeftToRight(isLeftEditable(), isRightEditable()) == leftToRight);
		checkState(!differences.isEmpty() && ComparisonUtil.getComparison(differences.get(0)) != null);
		final Comparison comparison = ComparisonUtil.getComparison(differences.get(0));
		doMergeNonConflicting((Collection<Diff>)differences, comparison, leftToRight, mergerRegistry);
	}

	/**
	 * Performs the merge of the non-conflicting differences in the given {@code differences}.
	 * 
	 * @param differences
	 *            The differences to be merged.
	 * @param comparison
	 *            The comparison containing the differences to decide on whether conflicts are in play or not
	 *            and to determine whether this is a three- or two-way comparison.
	 * @param leftToRight
	 *            The direction in which {@code differences} should be merged.
	 * @param mergerRegistry
	 *            The registry of mergers.
	 * @return an iterable over the differences that have actually been merged by this operation.
	 */
	private Iterable<Diff> doMergeNonConflicting(Collection<Diff> differences, Comparison comparison,
			boolean leftToRight, Registry mergerRegistry) {
		final Iterable<Diff> affectedChanges;
		if (hasRealConflict(comparison)) {
			// This is a 3-way comparison, pre-merge what can be.
			affectedChanges = mergeWithConflicts(differences, leftToRight, mergerRegistry);
		} else if (comparison.isThreeWay()) {
			// This is a 3-way comparison without conflicts
			affectedChanges = mergeThreeWayWithoutConflicts(differences, leftToRight, mergerRegistry);
		} else {
			// This is a 2-way comparison
			affectedChanges = mergeTwoWay(differences, leftToRight, mergerRegistry);
		}
		return affectedChanges;
	}

	/**
	 * Handles the merge of all non-conflicting differences in case of a three-way comparison without
	 * conflicts.
	 * 
	 * @param differences
	 *            The differences to be merged.
	 * @param leftToRight
	 *            The direction in which {@code differences} should be merged.
	 * @param mergerRegistry
	 *            The registry of mergers.
	 * @return an iterable over the differences that have actually been merged by this operation.
	 */
	private Iterable<Diff> mergeThreeWayWithoutConflicts(Collection<Diff> differences, boolean leftToRight,
			Registry mergerRegistry) {
		final List<Diff> affectedDiffs;
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		if (getMergeMode() == MergeMode.LEFT_TO_RIGHT) {
			affectedDiffs = Lists
					.newArrayList(Iterables.filter(differences, fromSide(DifferenceSource.LEFT)));
			merger.copyAllLeftToRight(affectedDiffs, new BasicMonitor());
			addOrUpdateMergeData(affectedDiffs, getMergeMode());
		} else if (getMergeMode() == MergeMode.RIGHT_TO_LEFT) {
			affectedDiffs = Lists.newArrayList(Iterables
					.filter(differences, fromSide(DifferenceSource.RIGHT)));
			merger.copyAllRightToLeft(affectedDiffs, new BasicMonitor());
			addOrUpdateMergeData(affectedDiffs, getMergeMode());
		} else if (getMergeMode() == MergeMode.ACCEPT || getMergeMode() == MergeMode.REJECT) {
			affectedDiffs = acceptOrRejectWithoutConflicts(differences, leftToRight, mergerRegistry, merger);
		} else {
			throw new IllegalStateException();
		}

		return affectedDiffs;
	}

	/**
	 * Returns the {@link MergeOperation} for the given {@code diff}.
	 * <p>
	 * The merge operation will be different depending on whether the left-hand side and right-hand side are
	 * editable in the current context (i.e., the {@link #getMergeMode() merge mode}.
	 * </p>
	 * 
	 * @param diff
	 *            The difference to get the merge operation for.
	 * @return The merge operation.
	 */
	private MergeOperation getMergeOperation(Diff diff) {
		return getMergeMode().getMergeAction(diff, isLeftEditable(), isRightEditable());
	}

	/**
	 * Handles the merge of all non-conflicting differences in case of a two-way comparison without conflicts.
	 * 
	 * @param differences
	 *            The differences to be merged.
	 * @param leftToRight
	 *            The direction in which {@code differences} should be merged.
	 * @param mergerRegistry
	 *            The registry of mergers.
	 * @return an iterable over the differences that have actually been merged by this operation.
	 */
	private Iterable<Diff> mergeTwoWay(Collection<Diff> differences, boolean leftToRight,
			Registry mergerRegistry) {
		final List<Diff> affectedDiffs;
		final IBatchMerger merger = new BatchMerger(mergerRegistry);

		// in two-way comparison, difference source is always LEFT
		if (getMergeMode() == MergeMode.LEFT_TO_RIGHT) {
			affectedDiffs = Lists
					.newArrayList(Iterables.filter(differences, fromSide(DifferenceSource.LEFT)));
			merger.copyAllLeftToRight(affectedDiffs, new BasicMonitor());
			addOrUpdateMergeData(affectedDiffs, getMergeMode());
		} else if (getMergeMode() == MergeMode.RIGHT_TO_LEFT) {
			affectedDiffs = Lists
					.newArrayList(Iterables.filter(differences, fromSide(DifferenceSource.LEFT)));
			merger.copyAllRightToLeft(affectedDiffs, new BasicMonitor());
			addOrUpdateMergeData(affectedDiffs, getMergeMode());
		} else if (getMergeMode() == MergeMode.ACCEPT || getMergeMode() == MergeMode.REJECT) {
			affectedDiffs = acceptOrRejectWithoutConflicts(differences, leftToRight, mergerRegistry, merger);
		} else {
			throw new IllegalStateException();
		}

		return affectedDiffs;
	}

	/**
	 * Handles the merge of all non-conflicting differences in case of a comparison with conflicts.
	 * 
	 * @param differences
	 *            The differences to be merged.
	 * @param leftToRight
	 *            The direction in which {@code differences} should be merged.
	 * @param mergerRegistry
	 *            The registry of mergers.
	 * @return an iterable over the differences that have actually been merged by this operation.
	 */
	private Iterable<Diff> mergeWithConflicts(Collection<Diff> differences, boolean leftToRight,
			Registry mergerRegistry) {
		final List<Diff> affectedDiffs = new ArrayList<Diff>();
		final Monitor emfMonitor = new BasicMonitor();
		final Graph<Diff> differencesGraph = MergeDependenciesUtil.mapDifferences(differences,
				mergerRegistry, !leftToRight, getMergeMode());
		final PruningIterator<Diff> iterator = differencesGraph.breadthFirstIterator();

		while (iterator.hasNext()) {
			final Diff next = iterator.next();
			if (hasConflict(ConflictKind.REAL).apply(next)) {
				iterator.prune();
			} else {
				if (next.getState() != DifferenceState.MERGED) {
					affectedDiffs.add(next);
					final IMerger merger = mergerRegistry.getHighestRankingMerger(next);
					if (getMergeMode() == MergeMode.LEFT_TO_RIGHT) {
						merger.copyLeftToRight(next, emfMonitor);
					} else if (getMergeMode() == MergeMode.RIGHT_TO_LEFT) {
						merger.copyRightToLeft(next, emfMonitor);
					} else if (getMergeMode() == MergeMode.ACCEPT || getMergeMode() == MergeMode.REJECT) {
						MergeOperation mergeAction = getMergeOperation(next);
						if (mergeAction == MergeOperation.MARK_AS_MERGE) {
							markAsMerged(next, getMergeMode(), leftToRight, mergerRegistry);
						} else {
							if (isLeftEditable() && !leftToRight) {
								merger.copyRightToLeft(next, emfMonitor);
							} else if (isRightEditable() && leftToRight) {
								merger.copyLeftToRight(next, emfMonitor);
							}
						}
					} else {
						throw new IllegalStateException();
					}
				}
			}
		}
		addOrUpdateMergeData(affectedDiffs, getMergeMode());
		return affectedDiffs;
	}

	/**
	 * Performs an accept or reject operation in a three-way merge without conflicts or in a two-way merge.
	 * 
	 * @param differences
	 *            The differences to be merged.
	 * @param leftToRight
	 *            The direction in which {@code differences} should be merged.
	 * @param mergerRegistry
	 *            The registry of mergers.
	 * @param merger
	 *            The merger to be used in this operation.
	 * @return an iterable over the differences that have actually been merged by this operation.
	 */
	private List<Diff> acceptOrRejectWithoutConflicts(Collection<Diff> differences, boolean leftToRight,
			Registry mergerRegistry, final IBatchMerger merger) {
		final List<Diff> diffsToMarkAsMerged = newArrayList();
		final List<Diff> diffsToAccept = newArrayList();
		final List<Diff> diffsToReject = newArrayList();

		for (Diff diff : differences) {
			final MergeOperation mergeAction = getMergeOperation(diff);
			if (mergeAction == MergeOperation.MARK_AS_MERGE) {
				diffsToMarkAsMerged.add(diff);
			} else {
				if (isLeftEditable() && leftToRight) {
					diffsToReject.add(diff);
				} else {
					diffsToAccept.add(diff);
				}
			}
		}

		final Monitor emfMonitor = new BasicMonitor();
		mergeAll(diffsToAccept, leftToRight, merger, mergerRegistry, emfMonitor);
		mergeAll(diffsToReject, !leftToRight, merger, mergerRegistry, emfMonitor);
		markAllAsMerged(diffsToMarkAsMerged, getMergeMode(), mergerRegistry);

		final List<Diff> affectedDiffs = Lists.newArrayList(diffsToAccept);
		affectedDiffs.addAll(diffsToReject);
		affectedDiffs.addAll(diffsToMarkAsMerged);
		return affectedDiffs;
	}

	/**
	 * Checks whether the given comparison presents a real conflict.
	 * 
	 * @param comparison
	 *            The comparison to check for conflicts.
	 * @return <code>true</code> if there's at least one {@link ConflictKind#REAL real conflict} within this
	 *         comparison.
	 */
	private boolean hasRealConflict(Comparison comparison) {
		return any(comparison.getConflicts(), containsConflictOfTypes(ConflictKind.REAL));
	}

	/**
	 * Merge all given differences in case of an ACCEPT or REJECT MergeMode.
	 * 
	 * @param differences
	 *            The differences to merge.
	 * @param leftToRight
	 *            The direction in which {@code differences} should be merged.
	 * @param merger
	 *            The current merger.
	 * @param mergerRegistry
	 *            The registry of mergers.
	 * @param emfMonitor
	 *            To monitor the process.
	 */
	private void mergeAll(Collection<? extends Diff> differences, boolean leftToRight, IBatchMerger merger,
			Registry mergerRegistry, Monitor emfMonitor) {
		if (leftToRight) {
			merger.copyAllLeftToRight(differences, emfMonitor);
		} else {
			merger.copyAllRightToLeft(differences, emfMonitor);
		}

		for (Diff difference : differences) {
			final IMerger diffMerger = mergerRegistry.getHighestRankingMerger(difference);
			if (diffMerger instanceof IMerger2) {
				final Set<Diff> resultingMerges = MergeDependenciesUtil.getAllResultingMerges(difference,
						mergerRegistry, !leftToRight);
				addOrUpdateMergeData(resultingMerges, getMergeMode());

				final Set<Diff> resultingRejections = MergeDependenciesUtil.getAllResultingRejections(
						difference, mergerRegistry, !leftToRight);
				addOrUpdateMergeData(resultingRejections, getMergeMode().inverse());
			} else {
				addOrUpdateMergeData(Collections.singleton(difference), getMergeMode());
			}
		}
	}
}
