/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EReference;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class PseudoConflictMerger extends AbstractMerger {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#isMergerFor(org.eclipse.emf.compare.Diff)
	 */
	public boolean isMergerFor(Diff target) {
		return target.getConflict() != null && target.getConflict().getKind() == ConflictKind.PSEUDO;
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
		final ReferenceChange diff = (ReferenceChange)target;

		// Change the diff's state before we actually merge it : this allows us to avoid requirement cycles.
		diff.setState(DifferenceState.MERGED);
		if (diff.getEquivalence() != null) {
			boolean continueMerge = handleEquivalences(diff, false, monitor);
			if (!continueMerge) {
				return;
			}
		}

		if (diff.getSource() == DifferenceSource.LEFT) {
			// merge all "requires" diffs
			mergeRequires(diff, false, monitor);
		} else {
			// merge all "required by" diffs
			mergeRequiredBy(diff, false, monitor);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#copyRightToLeft(org.eclipse.emf.compare.Diff,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void copyRightToLeft(Diff target, Monitor monitor) {
		// Don't merge an already merged (or discarded) diff
		if (target.getState() != DifferenceState.UNRESOLVED) {
			return;
		}
		final ReferenceChange diff = (ReferenceChange)target;

		// Change the diff's state before we actually merge it : this allows us to avoid requirement cycles.
		diff.setState(DifferenceState.MERGED);
		if (diff.getEquivalence() != null) {
			boolean continueMerge = handleEquivalences(diff, true, monitor);
			if (!continueMerge) {
				return;
			}
		}

		if (diff.getSource() == DifferenceSource.LEFT) {
			// merge all "required by" diffs
			mergeRequiredBy(diff, true, monitor);
		} else {
			// merge all "requires" diffs
			mergeRequires(diff, true, monitor);
		}
	}

	/**
	 * Handles the equivalences of this difference.
	 * <p>
	 * Note that in certain cases, we'll merge our opposite instead of merging this diff. Specifically, we'll
	 * do that for one-to-many eOpposites : we'll merge the 'many' side instead of the 'unique' one. This
	 * allows us not to worry about the order of the references on that 'many' side.
	 * </p>
	 * <p>
	 * This is called before the merge of <code>this</code>. In short, if this returns <code>false</code>, we
	 * won't carry on merging <code>this</code> after returning.
	 * </p>
	 * 
	 * @param diff
	 *            The diff we are currently merging.
	 * @param rightToLeft
	 *            Direction of the merge.
	 * @param monitor
	 *            The monitor to use in order to report progress information.
	 * @return <code>true</code> if the current difference should still be merged after handling its
	 *         equivalences, <code>false</code> if it should be considered "already merged".
	 */
	protected boolean handleEquivalences(ReferenceChange diff, boolean rightToLeft, Monitor monitor) {
		final EReference reference = diff.getReference();
		boolean continueMerge = true;
		for (Diff equivalent : diff.getEquivalence().getDifferences()) {
			if (equivalent instanceof ReferenceChange
					&& reference.getEOpposite() == ((ReferenceChange)equivalent).getReference()
					&& equivalent.getState() == DifferenceState.UNRESOLVED) {
				// This equivalence is on our eOpposite. Should we merge it instead of 'this'?
				final boolean mergeEquivalence = !reference.isMany()
						&& ((ReferenceChange)equivalent).getReference().isMany();
				if (mergeEquivalence) {
					mergeDiff(equivalent, rightToLeft, monitor);
					continueMerge = false;
				}
			} else if (diff.getSource() == DifferenceSource.LEFT) {
				// This can happen when merging subset/supersets... see AddInterfaceTest#testA50UseCase
				/*
				 * This should be removed (or we should make sure that we can never be here) when bug 398402
				 * is fixed.
				 */
				if (rightToLeft && diff.getRequiredBy().contains(equivalent)) {
					mergeDiff(equivalent, rightToLeft, monitor);
					continueMerge = false;
				} else if (!rightToLeft && diff.getRequires().contains(equivalent)) {
					mergeDiff(equivalent, rightToLeft, monitor);
					continueMerge = false;
				}
			} else if (diff.getSource() == DifferenceSource.RIGHT) {
				// This can happen when merging subset/supersets... see AddInterfaceTest#testA50UseCase
				/*
				 * This should be removed (or we should make sure that we can never be here) when bug 398402
				 * is fixed.
				 */
				if (rightToLeft && diff.getRequires().contains(equivalent)) {
					mergeDiff(equivalent, rightToLeft, monitor);
					continueMerge = false;
				} else if (!rightToLeft && diff.getRequiredBy().contains(equivalent)) {
					mergeDiff(equivalent, rightToLeft, monitor);
					continueMerge = false;
				}
			}
			equivalent.setState(DifferenceState.MERGED);
		}
		return continueMerge;
	}
}
