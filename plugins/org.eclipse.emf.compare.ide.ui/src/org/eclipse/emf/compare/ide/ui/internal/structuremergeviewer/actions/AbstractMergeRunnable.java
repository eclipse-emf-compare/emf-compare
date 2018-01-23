/*******************************************************************************
 * Copyright (c) 2014, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Martin Fleck - bug 514415
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import static org.eclipse.emf.compare.DifferenceState.DISCARDED;
import static org.eclipse.emf.compare.DifferenceState.MERGED;
import static org.eclipse.emf.compare.merge.AbstractMerger.isAccepting;
import static org.eclipse.emf.compare.merge.AbstractMerger.isInTerminalState;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.DiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IDiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.utils.EMFComparePredicates;

/**
 * Provides inheritable default behavior for the merge runnables.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public abstract class AbstractMergeRunnable {

	private static final Predicate<? super Diff> HAS_UNRESOLVED_STATE = EMFComparePredicates
			.hasState(DifferenceState.UNRESOLVED);

	/** Tells us whether the left side of the comparison we're operating on is editable. */
	private final boolean isLeftEditable;

	/** Tells us whether the right side of the comparison we're operating on is editable. */
	private final boolean isRightEditable;

	/** Current merging mode. */
	private final MergeMode mergeMode;

	/** Computer to calculate the relationship between diffs. */
	private IDiffRelationshipComputer diffRelationshipComputer;

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
	public AbstractMergeRunnable(boolean isLeftEditable, boolean isRightEditable, MergeMode mergeMode,
			IDiffRelationshipComputer diffRelationshipComputer) {
		this.isLeftEditable = isLeftEditable;
		this.isRightEditable = isRightEditable;
		this.mergeMode = mergeMode;
		this.diffRelationshipComputer = diffRelationshipComputer;
	}

	protected boolean isLeftEditable() {
		return isLeftEditable;
	}

	protected boolean isRightEditable() {
		return isRightEditable;
	}

	protected MergeMode getMergeMode() {
		return mergeMode;
	}

	/**
	 * Returns the diff relationship computer instance from the compare configuration with the given merger
	 * registry. If no computer instance has been set, a default instance will be created.
	 * 
	 * @param mergerRegistry
	 *            merger registry used to compute diff relationships.
	 * @return a non-null diff relationship computer.
	 */
	protected IDiffRelationshipComputer getDiffRelationshipComputer(IMerger.Registry mergerRegistry) {
		if (diffRelationshipComputer == null) {
			diffRelationshipComputer = new DiffRelationshipComputer(mergerRegistry);
		}
		diffRelationshipComputer.setMergerRegistry(mergerRegistry);
		return diffRelationshipComputer;
	}

	/**
	 * Marks all of the given diffs as merged, keeping track of the merged mode used for the operation.
	 * 
	 * @param diffToMarkAsMerged
	 *            List of Diffs that are to be marked as merged.
	 * @param mode
	 *            Mode with which these diffs have been merged.
	 * @param mergerRegistry
	 *            Current registry of mergers.
	 */
	protected void markAllAsMerged(Collection<? extends Diff> diffToMarkAsMerged, MergeMode mode,
			Registry mergerRegistry) {
		for (Diff diff : diffToMarkAsMerged) {
			boolean isLeftToRight = mode.isLeftToRight(diff, isLeftEditable(), isRightEditable());
			markAsMerged(diff, mode, !isLeftToRight, mergerRegistry);
		}
	}

	/**
	 * Marks a single diff as merged, keeping track of the merge mode used for the operation.
	 * 
	 * @param diff
	 *            Diff to mark as merged.
	 * @param mode
	 *            Mode with which this Diff has been merged.
	 * @param mergeRightToLeft
	 *            Direction of the merge operation.
	 * @param mergerRegistry
	 *            Current registry of mergers.
	 */
	protected void markAsMerged(Diff diff, MergeMode mode, boolean mergeRightToLeft,
			Registry mergerRegistry) {
		if (isInTerminalState(diff)) {
			return;
		}
		IDiffRelationshipComputer computer = getDiffRelationshipComputer(mergerRegistry);
		if (isAccepting(diff, mergeRightToLeft)) {
			final Set<Diff> implied = computer.getAllResultingMerges(diff, mergeRightToLeft,
					HAS_UNRESOLVED_STATE);
			final Set<Diff> rejections = computer.getAllResultingRejections(diff, mergeRightToLeft,
					HAS_UNRESOLVED_STATE);
			for (Diff impliedDiff : Sets.difference(implied, rejections)) {
				setState(impliedDiff, MERGED);
			}
			for (Diff impliedRejection : rejections) {
				setState(impliedRejection, DISCARDED);
			}
		} else {
			final Set<Diff> implied = computer.getAllResultingMerges(diff, mergeRightToLeft,
					HAS_UNRESOLVED_STATE);
			for (Diff impliedDiff : implied) {
				setState(impliedDiff, DISCARDED);
			}
		}
	}

	private void setState(Diff diff, DifferenceState state) {
		// Check the current state first, because changing the state sends notifications and that's relatively
		// expensive when the state does not actually change.
		if (diff.getState() != state) {
			diff.setState(state);
		}
	}

	protected void mergeAll(Collection<? extends Diff> differences, boolean leftToRight,
			Registry mergerRegistry) {
		final IBatchMerger merger = new BatchMerger(getDiffRelationshipComputer(mergerRegistry));
		if (leftToRight) {
			merger.copyAllLeftToRight(differences, new BasicMonitor());
		} else {
			merger.copyAllRightToLeft(differences, new BasicMonitor());
		}
	}

}
