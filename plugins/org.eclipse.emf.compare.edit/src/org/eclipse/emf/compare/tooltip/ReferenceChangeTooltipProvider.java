/*******************************************************************************
 * Copyright (c) 2015, 2018 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 514079
 *******************************************************************************/
package org.eclipse.emf.compare.tooltip;

import static org.eclipse.emf.compare.internal.EMFCompareEditMessages.getString;

import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

/**
 * This class handle the tooltips computation for Reference changes.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class ReferenceChangeTooltipProvider extends AbstractTooltipProvider<ReferenceChange> {

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
		if (origin == null) {
			if (left != null && right != null) {
				if (left.eContainingFeature() != right.eContainingFeature()
						|| !getLabelFromObject(left.eContainer())
								.equals(getLabelFromObject(right.eContainer()))) {
					isContainerMove = true;
				}
			}
		} else if (isFromLeft) {
			if (left != null) {
				// if the label of the container of an element is different of the label of its ancestor
				// or if the containing feature between an element and its container are different
				// We consider that it is a container move
				if (left.eContainingFeature() != origin.eContainingFeature()
						|| !getLabelFromObject(left.eContainer())
								.equals(getLabelFromObject(origin.eContainer()))) {
					isContainerMove = true;
				}
			}
		} else if (right != null) {
			if (right.eContainingFeature() != origin.eContainingFeature()
					|| !getLabelFromObject(right.eContainer())
							.equals(getLabelFromObject(origin.eContainer()))) {
				isContainerMove = true;
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
	@SuppressWarnings("nls")
	private String setMoveContainerTooltip(MergeMode mode, ReferenceChange diff, boolean isFromLeft,
			EObject left, EObject right, EObject ancestor) {
		String value = getLabel(diff);
		String leftContainerValue = "";
		if (left != null) {
			leftContainerValue = getLabelFromObject(left.eContainer());
		}

		String rightContainerValue = "";
		if (right != null) {
			rightContainerValue = getLabelFromObject(right.eContainer());
		}

		String tooltip;
		String body;
		boolean mirrored = isMirrored(diff);
		boolean isLeftToRight = isLeftToRight(diff, mode);
		String effectiveLeft;
		String effectiveRight;

		switch (mode) {
			case LEFT_TO_RIGHT:
			case RIGHT_TO_LEFT:
				String key = "ContextualTooltip.move.container.";
				boolean isLeftToRightMode = mode == MergeMode.LEFT_TO_RIGHT;
				effectiveLeft = getDirectionalValue(mirrored == isLeftToRightMode, leftContainerValue,
						rightContainerValue);
				effectiveRight = getDirectionalValue(mirrored == isLeftToRightMode, rightContainerValue,
						leftContainerValue);
				if (isFromLeft != mirrored) {
					if (isLeftToRightMode) {
						key += "left.leftToRight";
					} else {
						key += "left.rightToLeft";
					}
				} else {
					if (isLeftToRightMode) {
						key += "right.leftToRight";
					} else {
						key += "right.rightToLeft";
					}
				}
				body = getString(key, value, effectiveLeft, effectiveRight);
				if (isLeftToRightMode) {
					tooltip = rightChanged(body);
				} else {
					tooltip = rightUnchanged(body);
				}
				break;
			case ACCEPT:
				effectiveLeft = getDirectionalValue(isLeftToRight == mirrored, leftContainerValue,
						rightContainerValue);
				effectiveRight = getDirectionalValue(isLeftToRight == mirrored, rightContainerValue,
						leftContainerValue);
				if (isFromLeft != isLeftToRight != mirrored) {
					body = getString(
							getDirectionalKey(isLeftToRight, "ContextualTooltip.move.container.left.accept"),
							value, effectiveRight);
					tooltip = acceptAndUnchanged(body, isLeftToRight);
				} else {
					body = getString(
							getDirectionalKey(isLeftToRight, "ContextualTooltip.move.container.right.accept"),
							value, effectiveLeft, effectiveRight);
					tooltip = acceptAndChanged(body, isLeftToRight);
				}
				break;
			case REJECT:
				effectiveLeft = getDirectionalValue(isLeftToRight == mirrored, leftContainerValue,
						rightContainerValue);
				effectiveRight = getDirectionalValue(isLeftToRight == mirrored, rightContainerValue,
						leftContainerValue);
				if (isFromLeft != isLeftToRight != mirrored) {
					String previousValue = null;
					if (diff.getMatch().getComparison().isThreeWay() && ancestor != null) {
						previousValue = getLabelFromObject(ancestor.eContainer());
					} else {
						previousValue = effectiveLeft;
					}

					body = getString(
							getDirectionalKey(isLeftToRight, "ContextualTooltip.move.container.left.reject"),
							value, previousValue, effectiveRight);
					tooltip = rejectAndChanged(body, isLeftToRight);
				} else {
					body = getString(
							getDirectionalKey(isLeftToRight, "ContextualTooltip.move.container.right.reject"),
							value, effectiveRight);
					tooltip = rejectAndUnchanged(body, isLeftToRight);
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

		boolean mirrored = isMirrored(diff);
		boolean isLeftToRight = isLeftToRight(diff, mode);
		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft != mirrored) {
					body = getString("ContextualTooltip.delete.containment.left.leftToRight", value, //$NON-NLS-1$
							containerValue);
				} else {
					body = getString("ContextualTooltip.delete.containment.right.leftToRight", value, //$NON-NLS-1$
							containerValue);
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft != mirrored) {
					body = getString("ContextualTooltip.delete.containment.left.rightToLeft", value, //$NON-NLS-1$
							containerValue);
				} else {
					body = getString("ContextualTooltip.delete.containment.right.rightToLeft", value, //$NON-NLS-1$
							containerValue);
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft != isLeftToRight != mirrored) {
					body = getString(
							getDirectionalKey(isLeftToRight,
									"ContextualTooltip.delete.containment.left.accept"), //$NON-NLS-1$
							value, containerValue);
					tooltip = acceptAndUnchanged(body, isLeftToRight);
				} else {
					body = getString(
							getDirectionalKey(isLeftToRight,
									"ContextualTooltip.delete.containment.right.accept"), //$NON-NLS-1$
							value, containerValue);
					tooltip = acceptAndChanged(body, isLeftToRight);
				}
				break;
			case REJECT:
				if (isFromLeft != isLeftToRight != mirrored) {
					body = getString(
							getDirectionalKey(isLeftToRight,
									"ContextualTooltip.delete.containment.left.reject"), //$NON-NLS-1$
							value, containerValue);
					tooltip = rejectAndChanged(body, isLeftToRight);
				} else {
					body = getString(
							getDirectionalKey(isLeftToRight,
									"ContextualTooltip.delete.containment.right.reject"), //$NON-NLS-1$
							value, containerValue);
					tooltip = rejectAndUnchanged(body, isLeftToRight);
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
		boolean mirrored = isMirrored(diff);
		boolean isLeftToRight = isLeftToRight(diff, mode);
		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft != mirrored) {
					body = getString("ContextualTooltip.add.containment.left.leftToRight", value, //$NON-NLS-1$
							containerValue);
				} else {
					body = getString("ContextualTooltip.add.containment.right.leftToRight", value, //$NON-NLS-1$
							containerValue);
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft != mirrored) {
					body = getString("ContextualTooltip.add.containment.left.rightToLeft", value, //$NON-NLS-1$
							containerValue);
				} else {
					body = getString("ContextualTooltip.add.containment.right.rightToLeft", value, //$NON-NLS-1$
							containerValue);
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft != isLeftToRight != mirrored) {
					body = getString(
							getDirectionalKey(isLeftToRight, "ContextualTooltip.add.containment.left.accept"), //$NON-NLS-1$
							value, containerValue);
					tooltip = acceptAndUnchanged(body, isLeftToRight);
				} else {
					body = getString(
							getDirectionalKey(isLeftToRight,
									"ContextualTooltip.add.containment.right.accept"), //$NON-NLS-1$
							value, containerValue);
					tooltip = acceptAndChanged(body, isLeftToRight);
				}
				break;
			case REJECT:
				if (isFromLeft != isLeftToRight != mirrored) {
					body = getString(
							getDirectionalKey(isLeftToRight, "ContextualTooltip.add.containment.left.reject"), //$NON-NLS-1$
							value, containerValue);
					tooltip = rejectAndChanged(body, isLeftToRight);
				} else {
					body = getString(
							getDirectionalKey(isLeftToRight,
									"ContextualTooltip.add.containment.right.reject"), //$NON-NLS-1$
							value, containerValue);
					tooltip = rejectAndUnchanged(body, isLeftToRight);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
	}

}
