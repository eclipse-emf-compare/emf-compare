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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.domain.IMergeRunnable;
import org.eclipse.emf.compare.internal.merge.IMergeData;
import org.eclipse.emf.compare.internal.merge.MergeDataImpl;
import org.eclipse.emf.compare.internal.merge.MergeDependenciesUtil;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.internal.merge.MergeOperation;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.merge.IMerger2;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @noreference
 * @noextend
 */
// Visible for testing
public final class MergeRunnableImpl implements IMergeRunnable {

	private final boolean isLeftEditable;

	private final boolean isRightEditable;

	private final MergeMode mergeMode;

	public MergeRunnableImpl(boolean isLeftEditable, boolean isRightEditable, MergeMode mergeMode) {
		this.isLeftEditable = isLeftEditable;
		this.isRightEditable = isRightEditable;
		this.mergeMode = mergeMode;
	}

	public void merge(List<? extends Diff> differences, boolean leftToRight, Registry mergerRegistry) {
		Preconditions.checkState(mergeMode.isLeftToRight(isLeftEditable, isRightEditable) == leftToRight);

		// Execute merge
		if (mergeMode == MergeMode.LEFT_TO_RIGHT || mergeMode == MergeMode.RIGHT_TO_LEFT) {
			mergeAll(differences, leftToRight, mergerRegistry);
		} else if (mergeMode == MergeMode.ACCEPT || mergeMode == MergeMode.REJECT) {
			List<Diff> diffToMarkAsMerged = newArrayList();
			List<Diff> diffToCopyFromLeftToRight = newArrayList();
			List<Diff> diffToCopyFromRightToLeft = newArrayList();
			for (Diff diff : differences) {
				MergeOperation mergeAction = mergeMode.getMergeAction(diff, isLeftEditable, isRightEditable);
				if (mergeAction == MergeOperation.MARK_AS_MERGE) {
					diffToMarkAsMerged.add(diff);
				} else {
					if (isLeftEditable && leftToRight) {
						diffToCopyFromRightToLeft.add(diff);
					} else {
						diffToCopyFromLeftToRight.add(diff);
					}
				}
			}
			mergeAll(diffToCopyFromLeftToRight, leftToRight, mergerRegistry);
			mergeAll(diffToCopyFromRightToLeft, !leftToRight, mergerRegistry);
			markAllAsMerged(diffToMarkAsMerged, mergeMode, mergerRegistry);
		} else {
			throw new IllegalStateException();
		}
	}

	/**
	 * @param diffToMarkAsMerged
	 * @param mode
	 */
	private void markAllAsMerged(Collection<Diff> diffToMarkAsMerged, MergeMode mode, Registry mergerRegistry) {
		for (Diff diff : diffToMarkAsMerged) {
			boolean isLeftToRight = mode.isLeftToRight(diff, isLeftEditable, isRightEditable);
			markAsMerged(diff, mode, !isLeftToRight, mergerRegistry);
		}
	}

	private void markAsMerged(Diff diff, MergeMode mode, boolean mergeRightToLeft, Registry mergerRegistry) {
		if (diff.getState() == DifferenceState.MERGED) {
			return;
		}

		final Set<Diff> implied = MergeDependenciesUtil.getAllResultingMerges(diff, mergerRegistry,
				mergeRightToLeft);
		final Set<Diff> rejections = MergeDependenciesUtil.getAllResultingRejections(diff, mergerRegistry,
				mergeRightToLeft);
		for (Diff req : implied) {
			req.setState(DifferenceState.MERGED);
			addOrUpdateMergeData(req, mode);
		}
		for (Diff rejected : rejections) {
			rejected.setState(DifferenceState.MERGED);
			if (mergeMode == MergeMode.LEFT_TO_RIGHT || mergeMode == MergeMode.RIGHT_TO_LEFT) {
				addOrUpdateMergeData(rejected, mode);
			} else {
				addOrUpdateMergeData(rejected, mode.inverse());
			}
		}
	}

	private void addOrUpdateMergeData(Collection<Diff> differences, MergeMode mode) {
		for (Diff difference : differences) {
			addOrUpdateMergeData(difference, mode);
		}
	}

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

	private void mergeAll(Collection<? extends Diff> differences, boolean leftToRight, Registry mergerRegistry) {
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		if (leftToRight) {
			merger.copyAllLeftToRight(differences, new BasicMonitor());
		} else {
			merger.copyAllRightToLeft(differences, new BasicMonitor());
		}

		for (Diff difference : differences) {
			final IMerger diffMerger = mergerRegistry.getHighestRankingMerger(difference);
			if (diffMerger instanceof IMerger2) {
				final Set<Diff> resultingMerges = MergeDependenciesUtil.getAllResultingMerges(difference,
						mergerRegistry, !leftToRight);
				final Set<Diff> resultingRejections = MergeDependenciesUtil.getAllResultingRejections(
						difference, mergerRegistry, !leftToRight);
				addOrUpdateMergeData(resultingMerges, mergeMode);
				if (mergeMode == MergeMode.LEFT_TO_RIGHT || mergeMode == MergeMode.RIGHT_TO_LEFT) {
					addOrUpdateMergeData(resultingRejections, mergeMode);
				} else {
					addOrUpdateMergeData(resultingRejections, mergeMode.inverse());
				}
			} else {
				addOrUpdateMergeData(Collections.singleton(difference), mergeMode);
			}
		}
	}
}
