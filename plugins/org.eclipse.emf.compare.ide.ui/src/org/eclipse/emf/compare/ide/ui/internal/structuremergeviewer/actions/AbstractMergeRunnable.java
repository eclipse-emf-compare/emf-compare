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

import java.util.Collection;
import java.util.Set;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.internal.merge.IMergeData;
import org.eclipse.emf.compare.internal.merge.MergeDataImpl;
import org.eclipse.emf.compare.internal.merge.MergeDependenciesUtil;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.ecore.util.EcoreUtil;

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
	 * Marks a single diff as merged, keeping track of the merged mode used for the operation.
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
	private void markAsMerged(Diff diff, MergeMode mode, boolean mergeRightToLeft, Registry mergerRegistry) {
		if (diff.getState() == DifferenceState.MERGED) {
			return;
		}

		final Set<Diff> implied = MergeDependenciesUtil.getAllResultingMerges(diff, mergerRegistry,
				mergeRightToLeft);
		for (Diff impliedDiff : implied) {
			impliedDiff.setState(DifferenceState.MERGED);
			addOrUpdateMergeData(impliedDiff, mode);
		}

		final Set<Diff> rejections = MergeDependenciesUtil.getAllResultingRejections(diff, mergerRegistry,
				mergeRightToLeft);
		for (Diff impliedRejection : rejections) {
			impliedRejection.setState(DifferenceState.MERGED);
			if (mergeMode == MergeMode.LEFT_TO_RIGHT || mergeMode == MergeMode.RIGHT_TO_LEFT) {
				addOrUpdateMergeData(impliedRejection, mode);
			} else {
				addOrUpdateMergeData(impliedRejection, mode.inverse());
			}
		}
	}

	/**
	 * Updates the IMergeData adapter for all of the given diffs.
	 * 
	 * @param differences
	 *            The differences for which to set or update the IMergeData adapter.
	 * @param mode
	 *            Merge mode we wish to keep track of.
	 */
	protected void addOrUpdateMergeData(Collection<Diff> differences, MergeMode mode) {
		for (Diff difference : differences) {
			addOrUpdateMergeData(difference, mode);
		}
	}

	/**
	 * Updates the IMergeData adapter for the given diff.
	 * 
	 * @param diff
	 *            The difference for which to set or update the IMergeData adapter.
	 * @param mode
	 *            Merge mode we wish to keep track of.
	 */
	private void addOrUpdateMergeData(Diff diff, MergeMode mode) {
		IMergeData mergeData = (IMergeData)EcoreUtil.getExistingAdapter(diff, IMergeData.class);
		if (mergeData != null) {
			mergeData.setMergeMode(mode);
			mergeData.setLeftEditable(isLeftEditable);
			mergeData.setRightEditable(isRightEditable);
		} else {
			mergeData = new MergeDataImpl(mode, isLeftEditable, isRightEditable);
			diff.eAdapters().add(mergeData);
		}
	}
}
