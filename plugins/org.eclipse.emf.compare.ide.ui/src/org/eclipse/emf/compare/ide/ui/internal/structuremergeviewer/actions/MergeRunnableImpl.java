/*******************************************************************************
 * Copyright (c) 2013 Obeo.
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
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.domain.IMergeRunnable;
import org.eclipse.emf.compare.internal.merge.IMergeData;
import org.eclipse.emf.compare.internal.merge.MergeDataImpl;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.internal.merge.MergeOperation;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
final class MergeRunnableImpl implements IMergeRunnable {

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
			List<Diff> diffToCopy = newArrayList();
			for (Diff diff : differences) {
				MergeOperation mergeAction = mergeMode.getMergeAction(diff, isLeftEditable, isRightEditable);
				if (mergeAction == MergeOperation.MARK_AS_MERGE) {
					diffToMarkAsMerged.add(diff);
				} else {
					diffToCopy.add(diff);
				}
			}
			mergeAll(diffToCopy, leftToRight, mergerRegistry);
			markAllAsMerged(diffToMarkAsMerged, mergeMode);
		} else {
			throw new IllegalStateException();
		}
	}

	/**
	 * @param diffToMarkAsMerged
	 * @param leftToRight
	 * @param isLeftEditable
	 * @param isRightEditable
	 */
	private void markAllAsMerged(Collection<Diff> diffToMarkAsMerged, MergeMode mergeMode) {
		for (Diff diff : diffToMarkAsMerged) {
			markAsMerged(diff, mergeMode);
		}
	}

	private void markAsMerged(Diff diff, MergeMode mergeMode) {
		diff.setState(DifferenceState.MERGED);
		addOrUpdateMergeData(diff, mergeMode);
	}

	private void addOrUpdateMergeData(Collection<Diff> differences, MergeMode mergeMode) {
		for (Diff difference : differences) {
			addOrUpdateMergeData(difference, mergeMode);
		}
	}

	private void addOrUpdateMergeData(Diff diff, MergeMode mergeMode) {
		IMergeData mergeData = (IMergeData)EcoreUtil.getExistingAdapter(diff, IMergeData.class);
		if (mergeData != null) {
			mergeData.setMergeMode(mergeMode);
			mergeData.setLeftEditable(isLeftEditable);
			mergeData.setRightEditable(isRightEditable);
		} else {
			mergeData = new MergeDataImpl(mergeMode, isLeftEditable, isRightEditable);
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
			addOrUpdateMergeData(difference, mergeMode);
			addOrUpdateMergeData(DiffUtil.getRequires(difference, leftToRight), mergeMode);
			addOrUpdateMergeData(DiffUtil.getUnmergeables(difference, leftToRight), mergeMode);
		}
	}
}
