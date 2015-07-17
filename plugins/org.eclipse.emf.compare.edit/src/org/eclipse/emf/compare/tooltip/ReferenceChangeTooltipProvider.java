/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tooltip;

import static org.eclipse.emf.compare.internal.EMFCompareEditMessages.getString;

import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.provider.ITooltipLabelProvider;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

/**
 * This class handle the tooltips computation for Reference changes.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class ReferenceChangeTooltipProvider extends AbstractTooltipProvider<ReferenceChange> implements ITooltipLabelProvider {

	/**
	 * The constructor.
	 * 
	 * @param adapterFactory
	 *            The composed adapter factory
	 */
	public ReferenceChangeTooltipProvider(ComposedAdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
		this.labelProvider = new AdapterFactoryItemDelegator(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ITooltipLabelProvider#getTooltip(MergeMode)
	 */
	public String getTooltip(MergeMode mode) throws IllegalArgumentException {
		ReferenceChange diff = (ReferenceChange)target;
		boolean isFromLeft = isFromLeft(diff);
		boolean isContainment = diff.getReference().isContainment();
		String tooltip;

		switch (diff.getKind()) {
			case DELETE:
				if (isContainment) {
					tooltip = setDeleteContainmentTooltip(mode, diff, isFromLeft);
				} else {
					tooltip = setDeleteNonContainmentTooltip(mode, diff, isFromLeft);
				}
				break;
			case ADD:
				if (isContainment) {
					tooltip = setAddContainmentTooltip(mode, diff, isFromLeft);
				} else {
					tooltip = setAddNonContainmentTooltip(mode, diff, isFromLeft);
				}
				break;
			case MOVE:
				if (isContainment) {
					Match valueMatch = diff.getMatch().getComparison().getMatch(diff.getValue());
					EObject left = valueMatch.getLeft();
					EObject right = valueMatch.getRight();
					EObject ancestor = valueMatch.getOrigin();
					if (isContainerMove(isFromLeft, ancestor, right, left)) {
						tooltip = setMoveContainerTooltip(mode, diff, isFromLeft, left, right, ancestor);
					} else {
						tooltip = setMovePositionTooltip(mode, diff, isFromLeft);
					}
				} else {
					tooltip = setMovePositionTooltip(mode, diff, isFromLeft);
				}
				break;
			case CHANGE:
				if (isUnset(diff)) {
					tooltip = setUnsetTooltip(mode, diff, isFromLeft);
				} else {
					tooltip = setSetTooltip(mode, diff, isFromLeft);
				}
				break;
			default:
				throw new IllegalArgumentException();
		}
		return tooltip;
	}

	/**
	 * Specifies whether the given {@code diff} unsets the reference value.
	 *
	 * @param diff
	 *            The difference to check
	 * @return <code>true</code> if setting {@code targetValue} is an unset, <code>false</code> otherwise.
	 */
	private boolean isUnset(ReferenceChange diff) {
		boolean isUnset = false;
		final Match match = diff.getMatch();
		final EObject container;
		if (diff.getSource() == DifferenceSource.LEFT) {
			container = match.getLeft();
		} else {
			container = match.getRight();
		}

		if (container == null) {
			isUnset = true;
		} else {
			if (!ReferenceUtil.safeEIsSet(container, diff.getReference())) {
				isUnset = true;
			}
		}

		return isUnset;
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
				// if the label of the container of an element is different of the label of its ancestor
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
	private String setMoveContainerTooltip(MergeMode mode, ReferenceChange diff, boolean isFromLeft,
			EObject left, EObject right, EObject ancestor) {
		String value = getLabel(diff);
		String leftContainerValue = ""; //$NON-NLS-1$
		if (left.eContainer() != null) {
			leftContainerValue = getLabelFromObject(left.eContainer());
		}

		String rightContainerValue = ""; //$NON-NLS-1$
		if (right.eContainer() != null) {
			rightContainerValue = getLabelFromObject(right.eContainer());
		}

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

	/**
	 * Compute the tooltip for an deletion in a containment reference.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @return the tooltip
	 */
	private String setDeleteContainmentTooltip(MergeMode mode, ReferenceChange diff, boolean isFromLeft) {
		String value = getLabel(diff);
		String containerValue = getLabel(diff.getMatch());
		String tooltip;
		String body;

		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.delete.containment.left.leftToRight", value, //$NON-NLS-1$
							containerValue);
				} else {
					body = getString("ContextualTooltip.delete.containment.right.leftToRight", value, //$NON-NLS-1$
							containerValue);
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.delete.containment.left.rightToLeft", value, //$NON-NLS-1$
							containerValue);
				} else {
					body = getString("ContextualTooltip.delete.containment.right.rightToLeft", value, //$NON-NLS-1$
							containerValue);
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.delete.containment.left.accept", value, //$NON-NLS-1$
							containerValue);
					tooltip = acceptAndUnchanged(body);
				} else {
					body = getString("ContextualTooltip.delete.containment.right.accept", value, //$NON-NLS-1$
							containerValue);
					tooltip = acceptAndChanged(body);
				}
				break;
			case REJECT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.delete.containment.left.reject", value, //$NON-NLS-1$
							containerValue);
					tooltip = rejectAndChanged(body);
				} else {
					body = getString("ContextualTooltip.delete.containment.right.reject", value, //$NON-NLS-1$
							containerValue);
					tooltip = rejectAndUnchanged(body);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
	}

	/**
	 * Compute the tooltip for an addition in a containment reference.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @return the tooltip
	 */
	private String setAddContainmentTooltip(MergeMode mode, ReferenceChange diff, boolean isFromLeft) {
		String value = getLabel(diff);
		String containerValue = getLabel(diff.getMatch());

		String tooltip;
		String body;
		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.containment.left.leftToRight", value, //$NON-NLS-1$
							containerValue);
				} else {
					body = getString("ContextualTooltip.add.containment.right.leftToRight", value, //$NON-NLS-1$
							containerValue);
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.containment.left.rightToLeft", value, //$NON-NLS-1$
							containerValue);
				} else {
					body = getString("ContextualTooltip.add.containment.right.rightToLeft", value, //$NON-NLS-1$
							containerValue);
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.containment.left.accept", value, //$NON-NLS-1$
							containerValue);
					tooltip = acceptAndUnchanged(body);
				} else {
					body = getString("ContextualTooltip.add.containment.right.accept", value, //$NON-NLS-1$
							containerValue);
					tooltip = acceptAndChanged(body);
				}
				break;
			case REJECT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.containment.left.reject", value, //$NON-NLS-1$
							containerValue);
					tooltip = rejectAndChanged(body);
				} else {
					body = getString("ContextualTooltip.add.containment.right.reject", value, //$NON-NLS-1$
							containerValue);
					tooltip = rejectAndUnchanged(body);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
	}

}
