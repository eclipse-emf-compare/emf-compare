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
import static org.eclipse.emf.compare.utils.ReferenceUtil.safeEGet;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

/**
 * This class is used to handle creation tooltips for CHANGE (SET and UNSET) modifications.
 *
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class ChangeTooltipManager extends AbstractTooltipManager {

	/**
	 * The constructor.
	 *
	 * @param adapterFactory
	 *            The given adapter factory
	 */
	public ChangeTooltipManager(AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
		this.labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
	}

	/**
	 * Entry point for the computation of CHANGE tooltips.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @return the tooltip
	 */
	public String setChangeTooltip(MergeMode mode, Diff diff, boolean isFromLeft) {
		boolean isUnset = isUnset(diff);
		String tooltip;

		// Two case are handled : set and unset
		if (isUnset) {
			tooltip = setUnsetTooltip(mode, diff, isFromLeft);
		} else {
			tooltip = setSetTooltip(mode, diff, isFromLeft);
		}
		return tooltip;
	}

	/**
	 * Returns the changed value, as it is right now stored in the model, of the attribute that is affected by
	 * the given {@code diff}.
	 *
	 * @param diff
	 *            The diff to get the changed value for.
	 * @return The changed value.
	 */
	private String getChangedValueFromModel(AttributeChange diff) {
		final EAttribute attribute = diff.getAttribute();
		final EObject changedContainer;
		switch (diff.getSource()) {
			case LEFT:
				changedContainer = diff.getMatch().getLeft();
				break;
			case RIGHT:
				changedContainer = diff.getMatch().getRight();
				break;
			default:
				throw new IllegalArgumentException();
		}
		return (String)safeEGet(changedContainer, attribute);
	}

	/**
	 * Check if the change in a given diff is a set or an unset.
	 * 
	 * @param diff
	 *            The given diff
	 * @return <code>true</code> if the diff is an unset, <code>false</code> otherwise
	 */
	private boolean isUnset(Diff diff) {
		boolean isUnset = false;
		if (diff instanceof ReferenceChange) {
			if (isUnset((ReferenceChange)diff)) {
				isUnset = true;
			}
		} else if (diff instanceof AttributeChange) {
			AttributeChange change = (AttributeChange)diff;
			if (isUnset(change, getChangedValueFromModel(change))) {
				isUnset = true;
			}
		}
		return isUnset;
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
	 * Specifies whether the given {@code diff} unsets the attribute value when updating the attribute with
	 * the given {@code targetValue}.
	 *
	 * @param diff
	 *            The difference to check.
	 * @param targetValue
	 *            The value to be set.
	 * @return <code>true</code> if setting {@code targetValue} is an unset, <code>false</code> otherwise.
	 */
	private boolean isUnset(AttributeChange diff, Object targetValue) {
		final Object defaultValue = diff.getAttribute().getDefaultValue();
		return targetValue == null || targetValue.equals(defaultValue)
				|| (defaultValue == null && "".equals(targetValue)); //$NON-NLS-1$
	}

	/**
	 * Get the label for the structuralFeature of the given eObject.
	 * 
	 * @param eStructuralFeature
	 *            The eStructuralFeature
	 * @param eObject
	 *            The eObject
	 * @return the label of the eStructuralFeature
	 */
	private String getPreviousValue(EStructuralFeature eStructuralFeature, EObject eObject) {
		Object ancestor = eObject.eGet(eStructuralFeature);
		String value = ""; //$NON-NLS-1$
		if (ancestor instanceof EObject) {
			value = getLabelFromObject((EObject)ancestor);
		} else if (ancestor != null) {
			value = ancestor.toString();
		}
		return value;
	}

	/**
	 * Compute the tooltip for the set of a value.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @return the tooltip
	 */
	private String setSetTooltip(MergeMode mode, Diff diff, boolean isFromLeft) {
		// compute the name of the structural feature modified
		EStructuralFeature eStructuralFeature = MatchUtil.getStructuralFeature(diff);
		String structuralFeatureName = eStructuralFeature.getName();

		// The name of the container of the structural feature
		String containerName = diff.getMatch().getLeft().eClass().getName();

		String rightValue = getPreviousValue(eStructuralFeature, diff.getMatch().getRight());
		String leftValue = getPreviousValue(eStructuralFeature, diff.getMatch().getLeft());
		String tooltip;
		String body;
		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft) {
					if ("".equals(rightValue)) { //$NON-NLS-1$
						body = getString(
								"ContextualTooltip.set.left.leftToRight.empty", structuralFeatureName, //$NON-NLS-1$
								containerName, leftValue);

					} else {
						body = getString("ContextualTooltip.set.left.leftToRight", structuralFeatureName, //$NON-NLS-1$
								containerName, leftValue, rightValue);
					}
				} else {
					// if previous value cannot be displayed
					if ("".equals(leftValue)) { //$NON-NLS-1$
						body = getString("ContextualTooltip.set.right.leftToRight.empty", //$NON-NLS-1$
								structuralFeatureName, containerName, rightValue);
					} else {
						body = getString("ContextualTooltip.set.right.leftToRight", structuralFeatureName, //$NON-NLS-1$
								containerName, leftValue, rightValue);
					}
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft) {
					// if previous value cannot be displayed
					if ("".equals(rightValue)) { //$NON-NLS-1$
						body = getString("ContextualTooltip.set.left.rightToLeft.empty", //$NON-NLS-1$
								structuralFeatureName, containerName, leftValue);
					} else {
						body = getString("ContextualTooltip.set.left.rightToLeft", structuralFeatureName, //$NON-NLS-1$
								containerName, rightValue, leftValue);
					}
				} else {
					if ("".equals(leftValue)) { //$NON-NLS-1$
						body = getString(
								"ContextualTooltip.set.right.rightToLeft.empty", structuralFeatureName, //$NON-NLS-1$
								containerName, rightValue);
					} else {
						body = getString("ContextualTooltip.set.right.rightToLeft", structuralFeatureName, //$NON-NLS-1$
								containerName, rightValue, leftValue);
					}
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.set.left.accept", leftValue, structuralFeatureName, //$NON-NLS-1$
							containerName);
					tooltip = acceptAndUnchanged(body);
				} else {
					if ("".equals(leftValue)) { //$NON-NLS-1$
						body = getString("ContextualTooltip.set.right.accept.empty", structuralFeatureName, //$NON-NLS-1$
								containerName, rightValue);
					} else {
						body = getString("ContextualTooltip.set.right.accept", structuralFeatureName, //$NON-NLS-1$
								containerName, rightValue, leftValue);
					}
					tooltip = acceptAndChanged(body);
				}
				break;
			case REJECT:
				// get the value of the structural feature in the origin side if it is a three way comparison,
				// get the value of the left side otherwise
				String previousValue;
				if (diff.getMatch().getComparison().isThreeWay()) {
					previousValue = getPreviousValue(eStructuralFeature, diff.getMatch().getOrigin());
				} else {
					previousValue = rightValue;
				}

				if (isFromLeft) {
					// if previous value cannot be displayed
					if ("".equals(previousValue)) { //$NON-NLS-1$
						body = getString("ContextualTooltip.set.left.reject.empty", structuralFeatureName, //$NON-NLS-1$
								containerName, leftValue);
					} else {
						body = getString("ContextualTooltip.set.left.reject", structuralFeatureName, //$NON-NLS-1$
								containerName, previousValue, leftValue);
					}
					tooltip = rejectAndChanged(body);
				} else {
					if (diff.getMatch().getComparison().isThreeWay()) {
						previousValue = getPreviousValue(eStructuralFeature, diff.getMatch().getOrigin());
					} else {
						previousValue = leftValue;
					}
					// if previous value cannot be displayed
					if ("".equals(previousValue)) { //$NON-NLS-1$
						body = getString("ContextualTooltip.set.right.reject.empty", structuralFeatureName, //$NON-NLS-1$
								containerName);
					} else {
						body = getString(
								"ContextualTooltip.set.right.reject", previousValue, structuralFeatureName, //$NON-NLS-1$
								containerName);
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
	 * Compute the tooltip for the unset of a value.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @return the tooltip
	 */
	private String setUnsetTooltip(MergeMode mode, Diff diff, boolean isFromLeft) {
		String value = getLabel(diff);

		// compute the name of the structural feature modified
		EStructuralFeature structuralFeature = MatchUtil.getStructuralFeature(diff);
		String structuralFeatureName = structuralFeature.getName();

		// The name of the container of the structural feature
		String containerName = diff.getMatch().getLeft().eClass().getName();

		String tooltip;
		String body;
		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.unset.left.leftToRight", structuralFeatureName, //$NON-NLS-1$
							containerName, value);
				} else {
					body = getString("ContextualTooltip.unset.right.leftToRight", structuralFeatureName, //$NON-NLS-1$
							containerName, value);
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.unset.left.rightToLeft", structuralFeatureName, //$NON-NLS-1$
							containerName, value);
				} else {
					body = getString("ContextualTooltip.unset.right.rightToLeft", structuralFeatureName, //$NON-NLS-1$
							containerName, value);
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.unset.left.accept", structuralFeatureName, //$NON-NLS-1$
							containerName);
					tooltip = acceptAndUnchanged(body);
				} else {
					body = getString("ContextualTooltip.unset.right.accept", structuralFeatureName, //$NON-NLS-1$
							containerName, value);
					tooltip = acceptAndChanged(body);
				}
				break;
			case REJECT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.unset.left.reject", structuralFeatureName, //$NON-NLS-1$
							containerName, value);
					tooltip = rejectAndChanged(body);
				} else {
					body = getString("ContextualTooltip.unset.right.reject", structuralFeatureName, //$NON-NLS-1$
							containerName, value);
					tooltip = rejectAndUnchanged(body);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
	}

}
