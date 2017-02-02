/*******************************************************************************
 * Copyright (c) 2013, 2017 Obeo.
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
import org.eclipse.emf.compare.domain.IMergeRunnable;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.internal.merge.MergeOperation;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @noreference
 * @noextend
 */
// Visible for testing
public final class MergeRunnableImpl extends AbstractMergeRunnable implements IMergeRunnable {
	public MergeRunnableImpl(boolean isLeftEditable, boolean isRightEditable, MergeMode mergeMode) {
		super(isLeftEditable, isRightEditable, mergeMode);
	}

	public void merge(List<? extends Diff> differences, boolean leftToRight, Registry mergerRegistry) {
		Preconditions
				.checkState(getMergeMode().isLeftToRight(isLeftEditable(), isRightEditable()) == leftToRight);
		// Execute merge
		if (getMergeMode() == MergeMode.LEFT_TO_RIGHT || getMergeMode() == MergeMode.RIGHT_TO_LEFT) {
			mergeAll(differences, leftToRight, mergerRegistry);
		} else if (getMergeMode() == MergeMode.ACCEPT || getMergeMode() == MergeMode.REJECT) {
			List<Diff> diffToMarkAsMerged = newArrayList();
			List<Diff> diffToCopyFromLeftToRight = newArrayList();
			List<Diff> diffToCopyFromRightToLeft = newArrayList();
			for (Diff diff : differences) {
				MergeOperation mergeAction = getMergeMode().getMergeAction(diff, isLeftEditable(),
						isRightEditable());
				if (mergeAction == MergeOperation.MARK_AS_MERGE) {
					diffToMarkAsMerged.add(diff);
				} else {
					if (isLeftEditable() && leftToRight) {
						diffToCopyFromRightToLeft.add(diff);
					} else {
						diffToCopyFromLeftToRight.add(diff);
					}
				}
			}
			mergeAll(diffToCopyFromLeftToRight, leftToRight, mergerRegistry);
			mergeAll(diffToCopyFromRightToLeft, !leftToRight, mergerRegistry);
			markAllAsMerged(diffToMarkAsMerged, getMergeMode(), mergerRegistry);
		} else {
			throw new IllegalStateException();
		}
	}

	private void mergeAll(Collection<? extends Diff> differences, boolean leftToRight,
			Registry mergerRegistry) {
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		if (leftToRight) {
			merger.copyAllLeftToRight(differences, new BasicMonitor());
		} else {
			merger.copyAllRightToLeft(differences, new BasicMonitor());
		}
	}
}
