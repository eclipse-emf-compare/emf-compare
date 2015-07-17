/*******************************************************************************
 * Copyright (c) 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import static org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages.getString;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

/**
 * This class is used to handle creation tooltips for MOVE modifications.
 *
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class MoveTooltipManager extends AbstractTooltipManager {

	/**
	 * The constructor.
	 *
	 * @param adapterFactory
	 *            The given adapter factory.
	 */
	public MoveTooltipManager(AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
		this.labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
	}

	/**
	 * Entry point for the computation of MOVE tooltips.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @return the tooltip
	 */
	public String setMoveTooltip(MergeMode mode, Diff diff, boolean isFromLeft) {
		EObject right = null;
		EObject left = null;
		EObject ancestor = null;
		String tooltip;

		boolean isContainmentReference = isContainmentReferenceChange(diff);
		if (isContainmentReference) {
			Match valueMatch = diff.getMatch().getComparison().getMatch(((ReferenceChange)diff).getValue());
			left = valueMatch.getLeft();
			right = valueMatch.getRight();
			ancestor = valueMatch.getOrigin();
		}

		// Two case are handled : move of an element in another container, position move of an element inside
		// the same container
		if (isContainmentReference && isContainerMove(isFromLeft, ancestor, right, left)) {
			tooltip = setMoveContainerTooltip(mode, diff, isFromLeft, left, right, ancestor);
		} else {
			tooltip = setMovePositionTooltip(mode, diff, isFromLeft);
		}
		return tooltip;
	}

	/**
	 * This method verify if the container (left and right) of an element are the same in order to detect move
	 * from a container to another.
	 *
	 * @param isFromLeft
	 *            True if the change comes from the left side, false otherwise
	 * @param origin
	 *            The origin container
	 * @param right
	 *            The right container
	 * @param left
	 *            The left container
	 * @return true if the two containers are different
	 */
	private boolean isContainerMove(boolean isFromLeft, EObject origin, EObject right, EObject left) {
		boolean isContainerMove = false;
		if (isFromLeft) {
			if (left != null && origin != null) {
				// if the label of the container of an element is different of the label of it ancestor
				// or if the containing feature between an element and its container are different
				// We consider that it is a container move
				if (!getLabelFromObject(left.eContainer()).equals(getLabelFromObject(origin.eContainer()))
						|| left.eContainingFeature() != origin.eContainingFeature()) {
					isContainerMove = true;
				}
			}
		} else {
			if (right != null && origin != null) {
				if (!getLabelFromObject(right.eContainer()).equals(getLabelFromObject(origin.eContainer()))
						|| right.eContainingFeature() != origin.eContainingFeature()) {
					isContainerMove = true;
				}
			}
		}
		return isContainerMove;
	}

	/**
	 * Compute the tooltip for the move (position) of an element inside the same container.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @return the tooltip
	 */
	private String setMovePositionTooltip(MergeMode mode, Diff diff, boolean isFromLeft) {
		String value = getLabel(diff);
		String containerValue = getLabel(diff.getMatch());
		String tooltip;
		String body;

		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.move.position.left.leftToRight", value); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.move.position.right.leftToRight", value); //$NON-NLS-1$
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.move.position.left.rightToLeft", value); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.move.position.right.rightToLeft", value); //$NON-NLS-1$
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft) {
					// display container label only if the element is inside a container
					if (diff instanceof ReferenceChange
							&& ((ReferenceChange)diff).getReference().isContainment()) {
						body = getString(
								"ContextualTooltip.move.position.left.container.accept", value, containerValue); //$NON-NLS-1$
					} else {
						body = getString("ContextualTooltip.move.position.left.accept", value); //$NON-NLS-1$
					}
					tooltip = acceptAndUnchanged(body);
				} else {
					if (diff instanceof ReferenceChange
							&& ((ReferenceChange)diff).getReference().isContainment()) {
						body = getString(
								"ContextualTooltip.move.position.right.container.accept", value, containerValue); //$NON-NLS-1$
					} else {
						body = getString("ContextualTooltip.move.position.right.accept", value); //$NON-NLS-1$
					}
					tooltip = acceptAndChanged(body);
				}
				break;
			case REJECT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.move.position.left.reject", value); //$NON-NLS-1$
					tooltip = rejectAndChanged(body);
				} else {
					// display container label only if the element is inside a container
					if (diff instanceof ReferenceChange
							&& ((ReferenceChange)diff).getReference().isContainment()) {
						body = getString(
								"ContextualTooltip.move.position.right.container.reject", value, containerValue); //$NON-NLS-1$
					} else {
						body = getString("ContextualTooltip.move.position.right.reject", value); //$NON-NLS-1$
					}
					tooltip = rejectAndUnchanged(body);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
	}

	/**
	 * Compute the tooltip for the move of an element from a container to another.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @param left
	 *            The modified element in the left model
	 * @param right
	 *            The modified element in the right model
	 * @param ancestor
	 *            The modified element in the left model
	 * @return the tooltip
	 */
	private String setMoveContainerTooltip(MergeMode mode, Diff diff, boolean isFromLeft, EObject left,
			EObject right, EObject ancestor) {
		String value = getLabel(diff);
		String leftContainerValue = getLabelFromObject(left.eContainer());
		String rightContainerValue = getLabelFromObject(right.eContainer());
		String ancestorContainerValue;
		if (diff.getMatch().getComparison().isThreeWay() && ancestor != null) {
			ancestorContainerValue = getLabelFromObject(ancestor.eContainer());
		} else {
			ancestorContainerValue = rightContainerValue;
		}
		String tooltip;
		String body;

		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.move.container.left.leftToRight", value, //$NON-NLS-1$
							leftContainerValue, rightContainerValue);
				} else {
					body = getString("ContextualTooltip.move.container.right.leftToRight", value, //$NON-NLS-1$
							leftContainerValue, rightContainerValue);
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.move.container.left.rightToLeft", value, //$NON-NLS-1$
							rightContainerValue, leftContainerValue);
				} else {
					body = getString("ContextualTooltip.move.container.right.rightToLeft", value, //$NON-NLS-1$
							rightContainerValue, leftContainerValue);
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.move.container.left.accept", value, //$NON-NLS-1$
							leftContainerValue);
					tooltip = acceptAndUnchanged(body);
				} else {
					body = getString("ContextualTooltip.move.container.right.accept", value, //$NON-NLS-1$
							rightContainerValue, leftContainerValue);
					tooltip = acceptAndChanged(body);
				}
				break;
			case REJECT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.move.container.left.reject", value, //$NON-NLS-1$
							ancestorContainerValue, leftContainerValue);
					tooltip = rejectAndChanged(body);
				} else {
					body = getString("ContextualTooltip.move.container.right.reject", value, //$NON-NLS-1$
							leftContainerValue);
					tooltip = rejectAndUnchanged(body);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
	}

}
