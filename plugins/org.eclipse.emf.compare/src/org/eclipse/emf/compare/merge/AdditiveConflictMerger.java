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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EObject;

/**
 * This specific implementation of {@link AbstractMerger} will be used to merge conflicts in an additive merge
 * context.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 * @since 3.4
 */
public class AdditiveConflictMerger extends ConflictMerger {

	/**
	 * The constructor specify the context where this merger can be used.
	 */
	public AdditiveConflictMerger() {
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

		// We need to merge diffs in the conflict that are pseudo conflicting diffs. This behavior is required
		// to bypass some behavior in AbstractMerger which is not aware of additive related behavior.
		Comparison comparison = target.getMatch().getComparison();
		EList<Diff> rightDifferences = target.getConflict().getRightDifferences();
		for (Diff diff : rightDifferences) {
			EList<Diff> differences = diff.getMatch().getDifferences();
			for (Diff diff2 : differences) {
				if (isEquivalentFromDifferentSides(comparison, diff, diff2)) {
					diff.setState(MERGED);
					diff2.setState(MERGED);
				}
			}
		}

		// get rid of refined diffs.
		if (target.getSource() == RIGHT) {
			for (Diff diff : target.getRefines()) {
				getMergerDelegate(diff).copyRightToLeft(diff, monitor);
			}
		}

		if (target.getState() == MERGED) {
			return;
		}

		if (target.getSource() == RIGHT && target.getKind() != DELETE) {
			super.copyRightToLeft(target, monitor);
		} else if (target.getSource() == LEFT && target.getKind() == DELETE) {
			super.copyRightToLeft(target, monitor);
		} else {
			target.setState(MERGED);
			for (Diff refiningDiff : target.getRefinedBy()) {
				refiningDiff.setState(MERGED);
			}
		}
	}

	/**
	 * Test if two given diffs are equivalent, i.e. if they weren't in a real conflicts they where detected as
	 * pseudo conflicts.
	 * 
	 * @param comparison
	 *            The comparison
	 * @param diff
	 *            The first diff
	 * @param diff2
	 *            The second diff
	 * @return <code>true</code> if the diffs are equivalent
	 */
	private boolean isEquivalentFromDifferentSides(Comparison comparison, Diff diff, Diff diff2) {
		if (diff.eClass() == diff2.eClass() && diff.getSource() != diff2.getSource()) {
			if (diff.getKind() == diff2.getKind()) {
				if (diff instanceof ReferenceChange) {
					EObject value = ((ReferenceChange)diff).getValue();
					EObject value2 = ((ReferenceChange)diff2).getValue();
					if (value == null && value2 == null) {
						return true;
					}
					if (comparison.getMatch(value) == comparison.getMatch(value2)) {
						return true;
					}
				} else if (diff instanceof AttributeChange) {
					if (((AttributeChange)diff).getValue() == ((AttributeChange)diff2).getValue()) {
						return true;
					}
				}
			}
		}

		return false;
	}

}
