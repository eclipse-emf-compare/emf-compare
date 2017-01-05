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
package org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.merge;

import static org.eclipse.emf.compare.DifferenceKind.DELETE;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceState.MERGED;

import java.util.Set;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.merge.AbstractMerger;
import org.eclipse.emf.compare.merge.AdditiveMergeCriterion;
import org.eclipse.emf.compare.merge.IMergeCriterion;

/**
 * This specific implementation of {@link AbstractMerger} will be used to merge papyrus resource attachment
 * changes in an additive merge context.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 * @since 3.3
 */
public class AdditivePapyrusResourceAttachmentChangeMerger extends PapyrusResourceAttachmentChangeMerger {

	/**
	 * The constructor specify the context where this merger can be used.
	 */
	public AdditivePapyrusResourceAttachmentChangeMerger() {
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

		if (target.getKind() == DELETE) {
			if (target.getSource() == LEFT) {
				super.copyRightToLeft(target, monitor);
			} else {
				target.setState(MERGED);
				for (Diff refiningDiff : target.getRefinedBy()) {
					refiningDiff.setState(MERGED);
				}
			}
		} else {
			super.copyRightToLeft(target, monitor);
		}
	}

	@Override
	public Set<Diff> getDirectMergeDependencies(Diff diff, boolean mergeRightToLeft) {
		if (diff.getKind() == DELETE) {
			if (diff.getSource() == LEFT) {
				return super.getDirectMergeDependencies(diff, mergeRightToLeft);
			} else {
				return super.getDirectMergeDependencies(diff, !mergeRightToLeft);
			}
		} else {
			return super.getDirectMergeDependencies(diff, mergeRightToLeft);
		}
	}

	@Override
	public Set<Diff> getDirectResultingMerges(Diff diff, boolean mergeRightToLeft) {
		if (diff.getKind() == DELETE) {
			if (diff.getSource() == LEFT) {
				return super.getDirectResultingMerges(diff, mergeRightToLeft);
			} else {
				return super.getDirectResultingMerges(diff, !mergeRightToLeft);
			}
		} else {
			return super.getDirectResultingMerges(diff, mergeRightToLeft);
		}
	}
}
