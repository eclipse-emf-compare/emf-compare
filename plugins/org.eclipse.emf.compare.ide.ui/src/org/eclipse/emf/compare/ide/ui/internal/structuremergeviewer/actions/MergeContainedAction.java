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

import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;
import static org.eclipse.emf.compare.internal.merge.MergeMode.ACCEPT;
import static org.eclipse.emf.compare.internal.merge.MergeMode.LEFT_TO_RIGHT;

import com.google.common.base.Predicate;

import org.eclipse.compare.INavigatable;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.domain.IMergeRunnable;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IDiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * This action will merge all differences contained in the selection, regardless of conflicts or originating
 * side.
 * <p>
 * This will allow the user to either get the right content or keep the left content for the given
 * differences.
 * </p>
 * 
 * @author Laurent Goubet <laurent.goubet@obeo.fr>
 */
public class MergeContainedAction extends AbstractMergeContainedAction {
	public MergeContainedAction(IEMFCompareConfiguration compareConfiguration, Registry mergerRegistry,
			MergeMode mode, INavigatable navigatable, IStructuredSelection selection,
			Predicate<TreeNode> isFiltered) {
		super(compareConfiguration, mergerRegistry, mode, navigatable);
		this.isFiltered = isFiltered;
		setEnabled(updateSelection(selection));
	}

	@Override
	protected IMergeRunnable createMergeRunnable(MergeMode mode, boolean isLeftEditable,
			boolean isRightEditable, IDiffRelationshipComputer relationshipComputer) {
		return new MergeContainedRunnable(isLeftEditable, isRightEditable, mode, relationshipComputer);
	}

	@Override
	protected void initToolTipAndImage(MergeMode mode) {
		switch (mode) {
			case LEFT_TO_RIGHT:
				// fall through
			case ACCEPT:
				setText(EMFCompareIDEUIMessages.getString("merge.all.contained.to.right.text")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("merge.all.contained.to.right.text")); //$NON-NLS-1$
				break;
			case RIGHT_TO_LEFT:
				// fall through
			case REJECT:
				setText(EMFCompareIDEUIMessages.getString("merge.all.contained.to.left.text")); //$NON-NLS-1$
				setToolTipText(EMFCompareIDEUIMessages.getString("merge.all.contained.to.left.text")); //$NON-NLS-1$
				break;
			default:
				throw new IllegalStateException();
		}
	}

	@Override
	protected Predicate<Diff> getDiffPredicate() {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				final DifferenceSource sourceSide;
				if (LEFT_TO_RIGHT.equals(getSelectedMode()) || ACCEPT.equals(getSelectedMode())) {
					sourceSide = LEFT;
				} else {
					sourceSide = RIGHT;
				}
				return EMFComparePredicates.fromSide(sourceSide).apply(input);
			}
		};
	}
}
