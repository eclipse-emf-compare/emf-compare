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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Predicates.alwaysTrue;
import static org.eclipse.emf.compare.ConflictKind.REAL;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.merge.AbstractMerger.isInTerminalState;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.domain.IMergeRunnable;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.compare.merge.ComputeDiffsToMerge;
import org.eclipse.emf.compare.merge.IDiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.merge.IMerger.Registry2;

/**
 * Implements the "merge all contained conflicting" action.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class MergeConflictingRunnable extends AbstractMergeRunnable implements IMergeRunnable {

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(MergeConflictingRunnable.class);

	/**
	 * Default constructor.
	 * 
	 * @param isLeftEditable
	 *            Whether the left side of the comparison we're operating on is editable.
	 * @param isRightEditable
	 *            Whether the right side of the comparison we're operating on is editable.
	 * @param mergeMode
	 *            Merge mode for this operation.
	 * @param diffRelationshipComputer
	 *            The diff relationship computer used to find resulting merges and rejections.
	 */
	public MergeConflictingRunnable(boolean isLeftEditable, boolean isRightEditable, MergeMode mergeMode,
			IDiffRelationshipComputer diffRelationshipComputer) {
		super(isLeftEditable, isRightEditable, mergeMode, diffRelationshipComputer);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void merge(List<? extends Diff> differences, boolean leftToRight, Registry mergerRegistry) {
		checkState(getMergeMode().isLeftToRight(isLeftEditable(), isRightEditable()) == leftToRight);
		checkState(!differences.isEmpty() && ComparisonUtil.getComparison(differences.get(0)) != null);
		final Comparison comparison = ComparisonUtil.getComparison(differences.get(0));
		doMergeConflicting((Collection<Diff>)differences, comparison, leftToRight, (Registry2)mergerRegistry);
	}

	/**
	 * Performs the merge of the conflicting differences in the given {@code differences}.
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
	private Iterable<Diff> doMergeConflicting(Collection<Diff> differences, Comparison comparison,
			boolean leftToRight, Registry2 mergerRegistry) {
		final List<Diff> affectedDiffs = new ArrayList<Diff>();

		for (Diff diff : differences) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("doMergeConflicting(Collection<Diff>, Comparison, leftToRight, mergerRegistry)" //$NON-NLS-1$
						+ diff.hashCode());
			}
			affectedDiffs.addAll(mergeDirectAndIndirectConflicts(leftToRight, mergerRegistry, diff));
		}

		return affectedDiffs;
	}

	private List<Diff> mergeDirectAndIndirectConflicts(boolean leftToRight, Registry2 registry, Diff diff) {
		final Set<Diff> conflictingDiffs = getAllDirectlyAndIndirectlyConflictingDiffs(diff);
		if (conflictingDiffs.isEmpty()) {
			return Collections.emptyList();
		}

		final List<Diff> affectedDiffs = new ArrayList<Diff>();
		final ComputeDiffsToMerge computer = new ComputeDiffsToMerge(!leftToRight, registry);
		computer.failOnRealConflictUnless(alwaysTrue());

		conflictingDiffs.add(diff);
		for (Diff conflictingDiff : conflictingDiffs) {
			if (!isInTerminalState(conflictingDiff)) {
				for (Diff diffToMerge : computer.getAllDiffsToMerge(conflictingDiff)) {
					if (!leftToRight && isLeftEditable()) {
						// merging right to left
						if (LEFT.equals(getDiffSourceToMerge())) {
							// mark left as merged and reject right
							markAsMerged(diffToMerge, getMergeMode(), leftToRight, registry);
						} else {
							// merge right and discard left
							final IMerger merger = registry.getHighestRankingMerger(diffToMerge);
							merger.copyRightToLeft(diffToMerge, new BasicMonitor());
							affectedDiffs.add(diffToMerge);
						}
					} else if (leftToRight && isRightEditable()) {
						// merging left to right
						if (LEFT.equals(getDiffSourceToMerge())) {
							// merge left and discard right
							final IMerger merger = registry.getHighestRankingMerger(diffToMerge);
							merger.copyLeftToRight(diffToMerge, new BasicMonitor());
							affectedDiffs.add(diffToMerge);
						} else {
							// mark right as merged and reject left
							markAsMerged(diffToMerge, getMergeMode(), leftToRight, registry);
						}
					}
				}
			}
		}

		return affectedDiffs;
	}

	private Set<Diff> getAllDirectlyAndIndirectlyConflictingDiffs(Diff diff) {
		final Set<Diff> conflictingDiffs = Sets.newHashSet();
		if (hasConflict(REAL).apply(diff)) {
			conflictingDiffs.addAll(diff.getConflict().getDifferences());
		}
		final Set<Diff> allRefiningDiffs = DiffUtil.getAllRefiningDiffs(diff);
		for (Diff refiningDiff : allRefiningDiffs) {
			if (hasConflict(REAL).apply(refiningDiff)) {
				conflictingDiffs.addAll(refiningDiff.getConflict().getDifferences());
			}
		}
		return conflictingDiffs;
	}

	private DifferenceSource getDiffSourceToMerge() {
		switch (getMergeMode()) {
			case REJECT:
				// fall through
			case LEFT_TO_RIGHT:
				return DifferenceSource.LEFT;

			case ACCEPT:
				// fall through
			case RIGHT_TO_LEFT:
				// fall through
			default:
				return DifferenceSource.RIGHT;
		}
	}
}
