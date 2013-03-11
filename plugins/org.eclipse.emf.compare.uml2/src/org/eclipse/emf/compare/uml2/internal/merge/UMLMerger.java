/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.merge;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.merge.AbstractMerger;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;

/**
 * Specific implementation of {@link AbstractMerger} for UML differences.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class UMLMerger extends AbstractMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#isMergerFor(org.eclipse.emf.compare.Diff)
	 */
	public boolean isMergerFor(Diff target) {
		return target instanceof UMLDiff;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#copyLeftToRight(org.eclipse.emf.compare.Diff,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void copyLeftToRight(Diff target, Monitor monitor) {
		// Don't merge an already merged (or discarded) diff
		if (target.getState() != DifferenceState.UNRESOLVED) {
			return;
		}
		final UMLDiff diff = (UMLDiff)target;

		setEquivalentDiffAsMerged(diff);

		// Change the diff's state before we actually merge it : this allows us to avoid requirement cycles.
		diff.setState(DifferenceState.MERGED);
		for (Diff refining : diff.getRefinedBy()) {
			mergeDiff(refining, false, monitor);
		}

		if (diff.getSource() == DifferenceSource.LEFT) {
			// merge all "requires" diffs
			mergeRequires(diff, false, monitor);
		} else {
			// merge all "required by" diffs
			mergeRequiredBy(diff, false, monitor);
		}
	}

	public void copyRightToLeft(Diff target, Monitor monitor) {
		// Don't merge an already merged (or discarded) diff
		if (target.getState() != DifferenceState.UNRESOLVED) {
			return;
		}
		final UMLDiff diff = (UMLDiff)target;

		setEquivalentDiffAsMerged(diff);

		// Change the diff's state before we actually merge it : this allows us to avoid requirement cycles.
		diff.setState(DifferenceState.MERGED);
		for (Diff refining : diff.getRefinedBy()) {
			mergeDiff(refining, true, monitor);
		}

		if (diff.getSource() == DifferenceSource.LEFT) {
			// merge all "required by" diffs
			mergeRequiredBy(diff, true, monitor);
		} else {
			mergeRequires(diff, true, monitor);
		}
	}

	/**
	 * Iterates over the differences equivalent to {@code diff} and sets them as
	 * {@link DifferenceState#MERGED}.
	 * 
	 * @param diff
	 *            Diff which equivalences are to be considered merged.
	 */
	private void setEquivalentDiffAsMerged(UMLDiff diff) {
		if (diff.getEquivalence() != null) {
			for (Diff equivalent : diff.getEquivalence().getDifferences()) {
				equivalent.setState(DifferenceState.MERGED);
			}
		}
	}
}
