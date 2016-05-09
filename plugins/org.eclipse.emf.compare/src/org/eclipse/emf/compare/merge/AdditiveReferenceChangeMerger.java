/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import static org.eclipse.emf.compare.DifferenceKind.DELETE;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;
import static org.eclipse.emf.compare.DifferenceState.MERGED;
import static org.eclipse.emf.compare.DifferenceState.UNRESOLVED;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * This specific implementation of {@link AbstractMerger} will be used to merge reference changes in an
 * additive merge context.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 * @since 3.4
 */
public class AdditiveReferenceChangeMerger extends ReferenceChangeMerger {

	/**
	 * The constructor specify the context where this merger can be used.
	 */
	public AdditiveReferenceChangeMerger() {
		super();
		mergeOptions.put(IMergeCriterion.OPTION_MERGE_CRITERION, AdditiveMergeCriterion.INSTANCE);
	}

	@Override
	public boolean apply(IMergeCriterion criterion) {
		return criterion == AdditiveMergeCriterion.INSTANCE;
	}

	@Override
	public void copyRightToLeft(Diff target, Monitor monitor) {
		if (target.getState() != UNRESOLVED) {
			return;
		}

		ReferenceChange rc = (ReferenceChange)target;
		if (rc.getReference().isContainment()) {
			if (rc.getSource() == RIGHT) {
				if (rc.getKind() == DELETE) {
					markAsMerged(rc);
				} else {
					super.copyRightToLeft(rc, monitor);
				}
			} else {
				if (rc.getKind() == DELETE) {
					super.copyRightToLeft(rc, monitor);
				} else {
					markAsMerged(rc);
				}
			}
		} else {
			if (rc.getSource() == RIGHT) {
				if (rc.getKind() == DELETE || isUnsetRelatedToDeletion(rc)) {
					markAsMerged(rc);
				} else {
					super.copyRightToLeft(rc, monitor);
				}
			} else {
				if (rc.getKind() == DELETE || isUnsetRelatedToDeletion(rc)) {
					super.copyRightToLeft(rc, monitor);
				} else {
					markAsMerged(rc);
				}
			}
		}
	}

	/**
	 * Determine if the given diff is an unset of a value which is related to the deletion of an object.
	 * 
	 * @param rc
	 *            The given ReferenceChange
	 * @return <code>true</code> if the diff is an unset related to a deleted object
	 */
	private boolean isUnsetRelatedToDeletion(ReferenceChange rc) {
		if (rc.getKind() == DifferenceKind.CHANGE && isUnsetOfValue(rc)) {
			for (Diff diff : rc.getRequiredBy()) {
				if (diff.getKind() == DELETE && diff instanceof ReferenceChange
						&& ((ReferenceChange)diff).getReference().isContainment()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Determine if the diff is an unset.
	 * 
	 * @param difference
	 *            The given difference
	 * @return <code>true</code> if the given diff is an unset
	 */
	private boolean isUnsetOfValue(ReferenceChange difference) {
		boolean isUnset = false;
		final EReference reference = difference.getReference();
		// FIXME bad smell -> what about multiple references?
		if (!reference.isMany()) {
			EObject referrer = null;
			if (difference.getSource() == LEFT) {
				referrer = difference.getMatch().getLeft();
			} else {
				referrer = difference.getMatch().getRight();
			}
			isUnset = referrer == null || !ReferenceUtil.safeEIsSet(referrer, reference);
		}
		return isUnset;
	}

	/**
	 * Mark a diff and its required diffs as merged.
	 * 
	 * @param diff
	 *            The diff to merge
	 */
	private void markAsMerged(Diff diff) {
		// diff.setState(MERGED);
		diff.setState(MERGED);
		for (Diff refiningDiff : diff.getRefinedBy()) {
			// markAsMerged(refiningDiff);
			refiningDiff.setState(MERGED);
		}
	}

}
