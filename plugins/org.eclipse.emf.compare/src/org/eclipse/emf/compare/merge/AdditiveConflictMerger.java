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

import java.util.Set;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Diff;

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
		if (isInTerminalState(target)) {
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

	@Override
	public Set<Diff> getDirectMergeDependencies(Diff target, boolean mergeRightToLeft) {
		if (target.getSource() == RIGHT && target.getKind() != DELETE) {
			return super.getDirectMergeDependencies(target, mergeRightToLeft);
		} else if (target.getSource() == LEFT && target.getKind() == DELETE) {
			return super.getDirectMergeDependencies(target, mergeRightToLeft);
		} else {
			return super.getDirectMergeDependencies(target, !mergeRightToLeft);
		}
	}

	@Override
	public Set<Diff> getDirectResultingMerges(Diff target, boolean mergeRightToLeft) {
		if (target.getSource() == RIGHT && target.getKind() != DELETE) {
			return super.getDirectResultingMerges(target, mergeRightToLeft);
		} else if (target.getSource() == LEFT && target.getKind() == DELETE) {
			return super.getDirectResultingMerges(target, mergeRightToLeft);
		} else {
			return super.getDirectResultingMerges(target, !mergeRightToLeft);
		}
	}
}
