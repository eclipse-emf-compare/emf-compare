/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import static com.google.common.collect.Lists.newArrayList;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;

import com.google.common.base.Preconditions;
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
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.internal.domain.IMergeAllNonConflictingRunnable;
import org.eclipse.emf.compare.internal.merge.MergeDependenciesUtil;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.internal.merge.MergeOperation;
import org.eclipse.emf.compare.internal.utils.Graph;
import org.eclipse.emf.compare.internal.utils.PruningIterator;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.merge.IMerger2;

/**
 * Implements the "merge all non-conflicting" action.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class MergeAllNonConflictingRunnable extends AbstractMergeRunnable implements IMergeAllNonConflictingRunnable {
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
	public MergeAllNonConflictingRunnable(boolean isLeftEditable, boolean isRightEditable, MergeMode mergeMode) {
		super(isLeftEditable, isRightEditable, mergeMode);
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterable<Diff> merge(Comparison comparison, boolean leftToRight, Registry mergerRegistry) {
		Preconditions
				.checkState(getMergeMode().isLeftToRight(isLeftEditable(), isRightEditable()) == leftToRight);
		Iterable<Diff> affectedChanges;
		if (hasRealConflict(comparison)) {
			// This is a 3-way comparison.
			// pre-merge what can be.
			affectedChanges = mergeWithConflicts(comparison, leftToRight, mergerRegistry);
		} else {
			// There are no conflicts here.
			affectedChanges = mergeWithoutConflicts(comparison, leftToRight, mergerRegistry);
		}
		return affectedChanges;
	}

	/**
	 * Handles the merge of all non-conflicting differences in case of a comparison without conflicts.
	 * 
	 * @param comparison
	 *            The comparison object.
	 * @param leftToRight
	 *            The direction in which {@code differences} should be merged.
	 * @param mergerRegistry
	 *            The registry of mergers.
	 * @return an iterable over the differences that have actually been merged by this operation.
	 */
	private Iterable<Diff> mergeWithoutConflicts(Comparison comparison, boolean leftToRight,
			Registry mergerRegistry) {
		final List<Diff> affectedDiffs;
		final Monitor emfMonitor = new BasicMonitor();
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		final boolean threeWay = comparison.isThreeWay();
		if (threeWay && getMergeMode() == MergeMode.LEFT_TO_RIGHT) {
			affectedDiffs = Lists.newArrayList(Iterables.filter(comparison.getDifferences(),
					fromSide(DifferenceSource.LEFT)));
			merger.copyAllLeftToRight(affectedDiffs, emfMonitor);
			addOrUpdateMergeData(affectedDiffs, getMergeMode());
		} else if (threeWay && getMergeMode() == MergeMode.RIGHT_TO_LEFT) {
			affectedDiffs = Lists.newArrayList(Iterables.filter(comparison.getDifferences(),
					fromSide(DifferenceSource.RIGHT)));
			merger.copyAllRightToLeft(affectedDiffs, emfMonitor);
			addOrUpdateMergeData(affectedDiffs, getMergeMode());
		} else if (getMergeMode() == MergeMode.ACCEPT || getMergeMode() == MergeMode.REJECT) {
			List<Diff> diffsToMarkAsMerged = newArrayList();
			List<Diff> diffsToAccept = newArrayList();
			List<Diff> diffsToReject = newArrayList();
			for (Diff diff : comparison.getDifferences()) {
				MergeOperation mergeAction = getMergeMode().getMergeAction(diff, isLeftEditable(),
						isRightEditable());
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
			mergeAll(diffsToAccept, leftToRight, merger, mergerRegistry, emfMonitor);
			mergeAll(diffsToReject, !leftToRight, merger, mergerRegistry, emfMonitor);
			markAllAsMerged(diffsToMarkAsMerged, getMergeMode(), mergerRegistry);
			affectedDiffs = Lists.newArrayList(diffsToAccept);
			affectedDiffs.addAll(diffsToReject);
			affectedDiffs.addAll(diffsToMarkAsMerged);
		} else if (getMergeMode() == MergeMode.LEFT_TO_RIGHT) {
			// We're in a 2way-comparison, so all differences come from left side.
			affectedDiffs = Lists.newArrayList(Iterables.filter(comparison.getDifferences(),
					fromSide(DifferenceSource.LEFT)));
			merger.copyAllLeftToRight(affectedDiffs, emfMonitor);
			addOrUpdateMergeData(affectedDiffs, getMergeMode());
		} else if (getMergeMode() == MergeMode.RIGHT_TO_LEFT) {
			// We're in a 2way-comparison, so all differences come from left side.
			affectedDiffs = Lists.newArrayList(Iterables.filter(comparison.getDifferences(),
					fromSide(DifferenceSource.LEFT)));
			merger.copyAllRightToLeft(affectedDiffs, emfMonitor);
			addOrUpdateMergeData(affectedDiffs, getMergeMode());
		} else {
			throw new IllegalStateException();
		}

		return affectedDiffs;
	}

	/**
	 * Handles the merge of all non-conflicting differences in case of a comparison with conflicts.
	 * 
	 * @param comparison
	 *            The comparison object.
	 * @param leftToRight
	 *            The direction in which {@code differences} should be merged.
	 * @param mergerRegistry
	 *            The registry of mergers.
	 * @return an iterable over the differences that have actually been merged by this operation.
	 */
	private Iterable<Diff> mergeWithConflicts(Comparison comparison, boolean leftToRight,
			Registry mergerRegistry) {
		final List<Diff> affectedDiffs;
		final Monitor emfMonitor = new BasicMonitor();
		final Graph<Diff> differencesGraph = MergeDependenciesUtil.mapDifferences(comparison, mergerRegistry,
				!leftToRight, getMergeMode());
		final PruningIterator<Diff> iterator = differencesGraph.breadthFirstIterator();

		affectedDiffs = new ArrayList<Diff>();
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
						MergeOperation mergeAction = getMergeMode().getMergeAction(next, isLeftEditable(),
								isRightEditable());
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
	 * Checks whether this comparison presents a real conflict.
	 * 
	 * @param comparison
	 *            The comparison to check for conflicts.
	 * @return <code>true</code> if there's at least one {@link ConflictKind#REAL real conflict} within this
	 *         comparison.
	 */
	private static boolean hasRealConflict(Comparison comparison) {
		for (Conflict conflict : comparison.getConflicts()) {
			if (conflict.getKind() == ConflictKind.REAL) {
				return true;
			}
		}
		return false;
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
