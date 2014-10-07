/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.emf.compare.internal.utils.Graph;
import org.eclipse.emf.compare.internal.utils.PruningIterator;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry;

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

		final List<Diff> affectedDiffs;
		final Monitor emfMonitor = new BasicMonitor();
		if (hasRealConflict(comparison)) {
			// pre-merge what can be
			final Graph<Diff> differencesGraph = MergeDependenciesUtil.mapDifferences(comparison,
					mergerRegistry, !leftToRight);
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
						if (leftToRight) {
							merger.copyLeftToRight(next, emfMonitor);
						} else {
							merger.copyRightToLeft(next, emfMonitor);
						}
					}
				}
			}
		} else {
			final IBatchMerger merger = new BatchMerger(mergerRegistry);
			if (leftToRight) {
				affectedDiffs = Lists.newArrayList(Iterables.filter(comparison.getDifferences(),
						fromSide(DifferenceSource.LEFT)));
				merger.copyAllLeftToRight(affectedDiffs, emfMonitor);
			} else {
				affectedDiffs = Lists.newArrayList(Iterables.filter(comparison.getDifferences(),
						fromSide(DifferenceSource.RIGHT)));
				merger.copyAllRightToLeft(affectedDiffs, emfMonitor);
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
}
