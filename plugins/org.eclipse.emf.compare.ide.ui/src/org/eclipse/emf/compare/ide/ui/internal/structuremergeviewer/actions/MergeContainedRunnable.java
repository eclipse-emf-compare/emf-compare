/*******************************************************************************
 * Copyright (c) 2018 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.domain.IMergeRunnable;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IDiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry;

/**
 * The runnable for the {@link MergeContainedAction}. Will only change the left-hand side of the comparison.
 * 
 * @author Laurent Goubet <laurent.goubet@obeo.fr>
 */
public class MergeContainedRunnable extends AbstractMergeRunnable implements IMergeRunnable {
	/** The delegate merger for the conflicting differences. */
	private final MergeConflictingRunnable conflictsMergers;

	public MergeContainedRunnable(boolean isLeftEditable, boolean isRightEditable, MergeMode mergeMode,
			IDiffRelationshipComputer diffRelationshipComputer) {
		super(isLeftEditable, isRightEditable, mergeMode, diffRelationshipComputer);
		// Note that we're considering that "ACCEPT" means that we're keeping the left version, so we need to
		// inverse the mode for the delegate merger.
		conflictsMergers = new MergeConflictingRunnable(isLeftEditable, isRightEditable, mergeMode.inverse(),
				diffRelationshipComputer);
	}

	public void merge(List<? extends Diff> differences, boolean leftToRight, Registry mergerRegistry) {
		if (getMergeMode().isLeftToRight(isLeftEditable(), isRightEditable()) != leftToRight) {
			throw new IllegalStateException();
		}

		// Use our delegate to handle all conflicts
		conflictsMergers.merge(differences, leftToRight, mergerRegistry);

		// Then merge the remainder as needed
		final boolean mergeDiffs;
		switch (getMergeMode()) {
			case LEFT_TO_RIGHT:
				// fall through
			case ACCEPT:
				mergeDiffs = false;
				break;
			case RIGHT_TO_LEFT:
				// fall through
			case REJECT:
				// fall through
			default:
				mergeDiffs = true;
		}

		if (mergeDiffs) {
			mergeAll(differences, false, mergerRegistry);
		} else {
			// Mark all differences from the left as merged and ACCEPTED
			differences.stream().filter(d -> d.getSource() == DifferenceSource.LEFT)
					.forEach(d -> markAsMerged(d, MergeMode.ACCEPT, mergerRegistry));
			// Mark all differences from the right as merged and REJECTED
			differences.stream().filter(d -> d.getSource() == DifferenceSource.RIGHT)
					.forEach(d -> markAsMerged(d, MergeMode.REJECT, mergerRegistry));
		}

	}

	private void markAsMerged(Diff diff, MergeMode mode, IMerger.Registry mergerRegistry) {
		boolean isLeftToRight = mode.isLeftToRight(diff, isLeftEditable(), isRightEditable());
		markAsMerged(diff, mode, !isLeftToRight, mergerRegistry);
	}
}
