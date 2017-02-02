/*******************************************************************************
 * Copyright (c) 2014, 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import static org.eclipse.emf.compare.DifferenceState.DISCARDED;
import static org.eclipse.emf.compare.DifferenceState.MERGED;
import static org.eclipse.emf.compare.merge.AbstractMerger.isAccepting;
import static org.eclipse.emf.compare.merge.AbstractMerger.isInTerminalState;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.internal.merge.MergeDependenciesUtil;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger.Registry;

/**
 * Provides inheritable default behavior for the merge runnables.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public abstract class AbstractMergeRunnable {
	/** Tells us whether the left side of the comparison we're operating on is editable. */
	private final boolean isLeftEditable;

	/** Tells us whether the right side of the comparison we're operating on is editable. */
	private final boolean isRightEditable;

	/** Current merging mode. */
	private final MergeMode mergeMode;

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
	public AbstractMergeRunnable(boolean isLeftEditable, boolean isRightEditable, MergeMode mergeMode) {
		this.isLeftEditable = isLeftEditable;
		this.isRightEditable = isRightEditable;
		this.mergeMode = mergeMode;
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
			boolean isLeftToRight = mode.isLeftToRight(diff, isLeftEditable, isRightEditable);
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
		if (isAccepting(diff, mergeRightToLeft)) {
			final Set<Diff> implied = MergeDependenciesUtil.getAllResultingMerges(diff, mergerRegistry,
					mergeRightToLeft);
			final Set<Diff> rejections = MergeDependenciesUtil.getAllResultingRejections(diff, mergerRegistry,
					mergeRightToLeft);
			for (Diff impliedDiff : Sets.difference(implied, rejections)) {
				impliedDiff.setState(MERGED);
			}
			for (Diff impliedRejection : rejections) {
				impliedRejection.setState(DISCARDED);
			}
		} else {
			final Set<Diff> implied = MergeDependenciesUtil.getAllResultingMerges(diff, mergerRegistry,
					mergeRightToLeft);
			for (Diff impliedDiff : implied) {
				impliedDiff.setState(DISCARDED);
			}
		}
	}
}
