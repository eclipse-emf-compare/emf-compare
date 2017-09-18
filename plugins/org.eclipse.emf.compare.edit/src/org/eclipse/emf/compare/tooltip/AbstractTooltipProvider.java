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

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.ResourceLocationChange;
import org.eclipse.emf.compare.internal.merge.IMergeData;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.provider.ISemanticObjectLabelProvider;
import org.eclipse.emf.compare.provider.ITooltipLabelProvider;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

/**
 * This class defines usual methods for contextual tooltips.
 *
 * @param <T>
 *            The parametric type of the class
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public abstract class AbstractTooltipProvider<T extends Diff> extends AdapterImpl implements ITooltipLabelProvider {

	/**
	 * The line separator used to compute tooltips.
	 */
	private static final String LINE_SEPARATOR = "\n"; //$NON-NLS-1$

	/**
	 * The adapter factory.
	 */
	protected ComposedAdapterFactory adapterFactory;

	/**
	 * The label provider.
	 */
	protected IItemLabelProvider labelProvider;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#isAdapterForType(Object)
	 */
	@Override
	public boolean isAdapterForType(Object type) {
		if (type == ITooltipLabelProvider.class) {
			return true;
		}
		return super.isAdapterForType(type);
	}

	/**
	 * Create the final tooltip for an accepted change which lead to a modification of the left side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @return the complete tooltip
	 * @deprecated Override or call {@link #acceptAndChanged(String, boolean)} instead.
	 */
	@Deprecated
	protected String acceptAndChanged(String value) {
		return acceptAndChanged(value, false);
	}

	/**
	 * Create the final tooltip for an accepted change which leads to a modification of the specified side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @param isLeftToRight
	 *            the direction of the change.
	 * @return the complete tooltip
	 */
	protected String acceptAndChanged(String value, boolean isLeftToRight) {
		String accept = getString("ContextualTooltip.acceptChange"); //$NON-NLS-1$
		String modify;
		if (isLeftToRight) {
			modify = getString("ContextualTooltip.readonly.rightChanged"); //$NON-NLS-1$
		} else {
			modify = getString("ContextualTooltip.readonly.leftChanged"); //$NON-NLS-1$
		}

		StringBuilder builder = new StringBuilder();
		builder.append(accept).append(LINE_SEPARATOR).append(value).append(LINE_SEPARATOR).append(modify);
		return builder.toString();
	}

	/**
	 * Used to verify if the origin of a diff come from the left side or not.
	 * 
	 * @param diff
	 *            The given diff
	 * @return <code>true</code> if the change come from left, <code>false</code> otherwise
	 */
	protected boolean isFromLeft(T diff) {
		return diff.getSource().equals(DifferenceSource.LEFT);
	}

	/**
	 * Returns whether the sides are logically mirrored when displayed.
	 * 
	 * @param diff
	 *            the diff to consider.
	 * @return whether the sides are logically mirrored when displayed.
	 */
	protected boolean isMirrored(T diff) {
		Comparison comparison = ComparisonUtil.getComparison(diff);
		if (comparison != null) {
			IMergeData mergeData = (IMergeData)EcoreUtil.getExistingAdapter(comparison, IMergeData.class);
			return mergeData != null && mergeData.isMirrored();
		}

		return false;
	}

	/**
	 * Returns whether the mode will lead to merge to left to right depending whether left and/or right are
	 * editable.
	 * 
	 * @param diff
	 *            the diff is used to determine the {@link ComparisonUtil#getComparison(Diff) comparison}.
	 * @param mode
	 *            the mode.
	 * @return if whether the mode will lead to merge to left to right depending whether left and/or right are
	 *         editable.
	 */
	protected boolean isLeftToRight(T diff, MergeMode mode) {
		Comparison comparison = ComparisonUtil.getComparison(diff);
		if (comparison != null) {
			IMergeData mergeData = (IMergeData)EcoreUtil.getExistingAdapter(comparison, IMergeData.class);
			if (mergeData != null) {
				if (mergeData.isMirrored()) {
					return mode.isLeftToRight(mergeData.isRightEditable(), mergeData.isLeftEditable());
				} else {
					return mode.isLeftToRight(mergeData.isLeftEditable(), mergeData.isRightEditable());
				}
			}
		}
		return mode.isLeftToRight(true, true);
	}

	/**
	 * Create the final tooltip for an accepted change which doesn't lead to a modification of the left side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @return the complete tooltip
	 * @deprecated Override or call {@link #acceptAndUnchanged(String, boolean)} instead.
	 */
	@Deprecated
	protected String acceptAndUnchanged(String value) {
		return acceptAndUnchanged(value, false);
	}

	/**
	 * Create the final tooltip for an accepted change which don't lead to a modification of the specified
	 * side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @param isLeftToRight
	 *            the direction of the change.
	 * @return the complete tooltip
	 */
	protected String acceptAndUnchanged(String value, boolean isLeftToRight) {
		String accept = getString("ContextualTooltip.acceptChange"); //$NON-NLS-1$
		String modify;
		if (isLeftToRight) {
			modify = getString("ContextualTooltip.readonly.rightUnchanged"); //$NON-NLS-1$
		} else {
			modify = getString("ContextualTooltip.readonly.leftUnchanged"); //$NON-NLS-1$
		}

		StringBuilder builder = new StringBuilder();
		builder.append(accept).append(LINE_SEPARATOR).append(value).append(LINE_SEPARATOR).append(modify);
		return builder.toString();
	}

	/**
	 * Create the final tooltip for a rejected change which leads to a modification of the left side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @return the complete tooltip
	 * @deprecated Override or call {@link #rejectAndChanged(String, boolean)} instead.
	 */
	@Deprecated
	protected String rejectAndChanged(String value) {
		return rejectAndChanged(value, false);
	}

	/**
	 * Create the final tooltip for a rejected change which leads to a modification of the specified side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @param isLeftToRight
	 *            the direction of the change.
	 * @return the complete tooltip
	 */
	protected String rejectAndChanged(String value, boolean isLeftToRight) {
		String accept = getString("ContextualTooltip.rejectChange"); //$NON-NLS-1$
		String modify;
		if (isLeftToRight) {
			modify = getString("ContextualTooltip.readonly.rightChanged"); //$NON-NLS-1$
		} else {
			modify = getString("ContextualTooltip.readonly.leftChanged"); //$NON-NLS-1$
		}

		StringBuilder builder = new StringBuilder();
		builder.append(accept).append(LINE_SEPARATOR).append(value).append(LINE_SEPARATOR).append(modify);
		return builder.toString();
	}

	/**
	 * Create the final tooltip for a rejected change which doesn't lead to a modification of the left side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @return the complete tooltip
	 * @deprecated Override or call {@link #rejectAndUnchanged(String, boolean)} instead.
	 */
	@Deprecated
	protected String rejectAndUnchanged(String value) {
		return rejectAndUnchanged(value, false);
	}

	/**
	 * Create the final tooltip for a rejected change which don't lead to a modification of the left side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @param isLeftToRight
	 *            the direction of the change.
	 * @return the complete tooltip
	 */
	protected String rejectAndUnchanged(String value, boolean isLeftToRight) {
		String accept = getString("ContextualTooltip.rejectChange"); //$NON-NLS-1$
		String modify;
		if (isLeftToRight) {
			modify = getString("ContextualTooltip.readonly.rightUnchanged"); //$NON-NLS-1$
		} else {
			modify = getString("ContextualTooltip.readonly.leftUnchanged"); //$NON-NLS-1$
		}

		StringBuilder builder = new StringBuilder();
		builder.append(accept).append(LINE_SEPARATOR).append(value).append(LINE_SEPARATOR).append(modify);
		return builder.toString();
	}

	/**
	 * Create the final tooltip for a change with two editable files which don't lead to a modification of the
	 * right side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @return the complete tooltip
	 */
	protected String rightUnchanged(String value) {
		String modify = getString("ContextualTooltip.editable.rightUnchanged"); //$NON-NLS-1$

		StringBuilder builder = new StringBuilder();
		builder.append(value).append(LINE_SEPARATOR).append(modify);
		return builder.toString();
	}

	/**
	 * Create the final tooltip for a change with two editable files which lead to a modification of the right
	 * side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @return the complete tooltip
	 */
	protected String rightChanged(String value) {
		String modify = getString("ContextualTooltip.editable.rightChanged"); //$NON-NLS-1$

		StringBuilder builder = new StringBuilder();
		builder.append(value).append(LINE_SEPARATOR).append(modify);
		return builder.toString();
	}

	/**
	 * Returns the label of the given <code>object</code> by adapting it to
	 * {@link ISemanticObjectLabelProvider} and asking for its
	 * {@link ISemanticObjectLabelProvider#getSemanticObjectLabel(Object) text}. Returns null if
	 * <code>object</code> is null.
	 *
	 * @param eObject
	 *            The object
	 * @return the label of the object
	 */
	protected String getLabel(EObject eObject) {
		if (eObject != null) {
			Object adapter = adapterFactory.getRootAdapterFactory().adapt(eObject,
					ISemanticObjectLabelProvider.class);
			if (adapter instanceof ISemanticObjectLabelProvider) {
				return ((ISemanticObjectLabelProvider)adapter).getSemanticObjectLabel(eObject);
			}
		}
		return null;
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
		Object ancestor = null;
		if (eObject != null) {
			ancestor = ReferenceUtil.safeEGet(eObject, eStructuralFeature);
		}
		String value = ""; //$NON-NLS-1$
		if (ancestor instanceof EObject) {
			value = getLabelFromObject((EObject)ancestor);
		} else if (ancestor != null) {
			value = ancestor.toString();
		}
		return value;
	}

	/**
	 * Returns the label of the given <code>object</code> by using the label provider.
	 *
	 * @param eObject
	 *            The object
	 * @return the label of the object, or an empty String if <code>eObject</code> is <code>null</code>
	 */
	protected String getLabelFromObject(EObject eObject) {
		if (eObject == null) {
			return ""; //$NON-NLS-1$
		}
		return this.labelProvider.getText(eObject);
	}

	/**
	 * Compute the tooltip for an deletion in a non-containment reference.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @return the tooltip
	 */
	protected String setDeleteNonContainmentTooltip(MergeMode mode, T diff, boolean isFromLeft) {
		String value = getLabel(diff);
		String tooltip;
		String body;
		boolean mirrored = isMirrored(diff);
		boolean isLeftToRight = isLeftToRight(diff, mode);

		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft != mirrored) {
					body = getString("ContextualTooltip.delete.nonContainment.left.leftToRight", value); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.delete.nonContainment.right.leftToRight", value); //$NON-NLS-1$
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft != mirrored) {
					body = getString("ContextualTooltip.delete.nonContainment.left.rightToLeft", value); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.delete.nonContainment.right.rightToLeft", value); //$NON-NLS-1$
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft != isLeftToRight != mirrored) {
					body = getString(getDirectionalKey(isLeftToRight,
							"ContextualTooltip.delete.nonContainment.left.accept"), value); //$NON-NLS-1$
					tooltip = acceptAndUnchanged(body, isLeftToRight);
				} else {
					body = getString(getDirectionalKey(isLeftToRight,
							"ContextualTooltip.delete.nonContainment.right.accept"), value); //$NON-NLS-1$
					tooltip = acceptAndChanged(body, isLeftToRight);
				}
				break;
			case REJECT:
				if (isFromLeft != isLeftToRight != mirrored) {
					body = getString("ContextualTooltip.delete.nonContainment.left.reject", value); //$NON-NLS-1$
					tooltip = rejectAndChanged(body, isLeftToRight);
				} else {
					body = getString(getDirectionalKey(isLeftToRight,
							"ContextualTooltip.delete.nonContainment.right.reject"), value); //$NON-NLS-1$
					tooltip = rejectAndUnchanged(body, isLeftToRight);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
	}

	/**
	 * Compute the tooltip for an addition in a non-containment reference.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @return the tooltip
	 */
	protected String setAddNonContainmentTooltip(MergeMode mode, T diff, boolean isFromLeft) {
		String value = getLabel(diff);
		String tooltip;
		String body;
		boolean mirrored = isMirrored(diff);
		boolean isLeftToRight = isLeftToRight(diff, mode);

		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft != mirrored) {
					body = getString("ContextualTooltip.add.nonContainment.left.leftToRight", value); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.add.nonContainment.right.leftToRight", value); //$NON-NLS-1$
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft != mirrored) {
					body = getString("ContextualTooltip.add.nonContainment.left.rightToLeft", value); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.add.nonContainment.right.rightToLeft", value); //$NON-NLS-1$
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft != isLeftToRight != mirrored) {
					body = getString(getDirectionalKey(isLeftToRight,
							"ContextualTooltip.add.nonContainment.left.accept"), value); //$NON-NLS-1$
					tooltip = acceptAndUnchanged(body, isLeftToRight);
				} else {
					body = getString(getDirectionalKey(isLeftToRight,
							"ContextualTooltip.add.nonContainment.right.accept"), value); //$NON-NLS-1$
					tooltip = acceptAndChanged(body, isLeftToRight);
				}
				break;
			case REJECT:
				if (isFromLeft != isLeftToRight != mirrored) {
					body = getString(getDirectionalKey(isLeftToRight,
							"ContextualTooltip.add.nonContainment.left.reject"), value); //$NON-NLS-1$
					tooltip = rejectAndChanged(body, isLeftToRight);
				} else {
					body = getString(getDirectionalKey(isLeftToRight,
							"ContextualTooltip.add.nonContainment.right.reject"), value); //$NON-NLS-1$
					tooltip = rejectAndUnchanged(body, isLeftToRight);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
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
	protected String setMovePositionTooltip(MergeMode mode, T diff, boolean isFromLeft) {
		String value = getLabel(diff);
		String containerValue = getLabel(diff.getMatch());
		String tooltip;
		String body;
		boolean mirrored = isMirrored(diff);
		boolean isLeftToRight = isLeftToRight(diff, mode);

		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft != mirrored) {
					body = getString("ContextualTooltip.move.position.left.leftToRight", value); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.move.position.right.leftToRight", value); //$NON-NLS-1$
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft != mirrored) {
					body = getString("ContextualTooltip.move.position.left.rightToLeft", value); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.move.position.right.rightToLeft", value); //$NON-NLS-1$
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft != isLeftToRight != mirrored) {
					// display container label only if the element is inside a container
					if (diff instanceof ReferenceChange
							&& ((ReferenceChange)diff).getReference().isContainment()) {
						body = getString(
								getDirectionalKey(isLeftToRight,
										"ContextualTooltip.move.position.left.container.accept"), //$NON-NLS-1$
								value, containerValue);
					} else {
						body = getString(getDirectionalKey(isLeftToRight,
								"ContextualTooltip.move.position.left.accept"), value); //$NON-NLS-1$
					}
					tooltip = acceptAndUnchanged(body, isLeftToRight);
				} else {
					if (diff instanceof ReferenceChange
							&& ((ReferenceChange)diff).getReference().isContainment()) {
						body = getString(
								getDirectionalKey(isLeftToRight,
										"ContextualTooltip.move.position.right.container.accept"), //$NON-NLS-1$
								value, containerValue);
					} else {
						body = getString(getDirectionalKey(isLeftToRight,
								"ContextualTooltip.move.position.right.accept"), value); //$NON-NLS-1$
					}
					tooltip = acceptAndChanged(body, isLeftToRight);
				}
				break;
			case REJECT:
				if (isFromLeft != isLeftToRight != mirrored) {
					body = getString(
							getDirectionalKey(isLeftToRight, "ContextualTooltip.move.position.left.reject"), //$NON-NLS-1$
							value);
					tooltip = rejectAndChanged(body, isLeftToRight);
				} else {
					// display container label only if the element is inside a container
					if (diff instanceof ReferenceChange
							&& ((ReferenceChange)diff).getReference().isContainment()) {
						body = getString(
								getDirectionalKey(isLeftToRight,
										"ContextualTooltip.move.position.right.container.reject"), //$NON-NLS-1$
								value, containerValue);
					} else {
						body = getString(getDirectionalKey(isLeftToRight,
								"ContextualTooltip.move.position.right.reject"), value); //$NON-NLS-1$
					}
					tooltip = rejectAndUnchanged(body, isLeftToRight);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
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
	// CHECKSTYLE:OFF
	@SuppressWarnings("nls")
	protected String setSetTooltip(MergeMode mode, T diff, boolean isFromLeft) {
		EStructuralFeature eStructuralFeature = MatchUtil.getStructuralFeature(diff);
		Match match = diff.getMatch();
		EObject left = match.getLeft();
		EObject right = match.getRight();
		String rightValue = getPreviousValue(eStructuralFeature, left);
		String leftValue = getPreviousValue(eStructuralFeature, right);

		String containerName = "";
		if (isFromLeft && left != null) {
			containerName = left.eClass().getName();
		} else if (!isFromLeft && right != null) {
			containerName = right.eClass().getName();
		}

		boolean mirrored = isMirrored(diff);
		boolean isLeftToRight = isLeftToRight(diff, mode);
		String tooltip;
		String body;
		String structuralFeatureName = eStructuralFeature.getName();
		String effectiveLeft;
		String effectiveRight;

		switch (mode) {
			case LEFT_TO_RIGHT:
			case RIGHT_TO_LEFT:
				String key = "ContextualTooltip.set.";
				boolean isLeftToRightMode = mode == MergeMode.LEFT_TO_RIGHT;
				String singleSideValue = null;
				effectiveLeft = getDirectionalValue(mirrored != isLeftToRightMode, leftValue, rightValue);
				effectiveRight = getDirectionalValue(mirrored != isLeftToRightMode, rightValue, leftValue);
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
				if (effectiveLeft.isEmpty()) {
					key += ".empty";
					singleSideValue = effectiveRight;
				} else if (effectiveRight.isEmpty()) {
					key += ".empty";
					singleSideValue = effectiveLeft;
				}
				if (singleSideValue != null) {
					body = getString(key, structuralFeatureName, containerName, singleSideValue);
				} else {
					body = getString(key, structuralFeatureName, containerName, effectiveLeft,
							effectiveRight);
				}
				if (isLeftToRightMode) {
					tooltip = rightChanged(body);
				} else {
					tooltip = rightUnchanged(body);
				}
				break;
			case ACCEPT:
				effectiveLeft = getDirectionalValue(isLeftToRight == mirrored, leftValue, rightValue);
				if (isFromLeft != isLeftToRight != mirrored) {
					body = getString(getDirectionalKey(isLeftToRight, "ContextualTooltip.set.left.accept"),
							effectiveLeft, structuralFeatureName, containerName);
					tooltip = acceptAndUnchanged(body, isLeftToRight);
				} else {
					effectiveRight = getDirectionalValue(isLeftToRight == mirrored, rightValue, leftValue);
					if (effectiveLeft.isEmpty()) {
						body = getString(
								getDirectionalKey(isLeftToRight, "ContextualTooltip.set.right.accept.empty"),
								structuralFeatureName, containerName, effectiveRight);
					} else {
						body = getString(
								getDirectionalKey(isLeftToRight, "ContextualTooltip.set.right.accept"),
								structuralFeatureName, containerName, effectiveRight, effectiveLeft);
					}
					tooltip = acceptAndChanged(body, isLeftToRight);
				}
				break;
			case REJECT:
				effectiveLeft = getDirectionalValue(isLeftToRight == mirrored, leftValue, rightValue);
				effectiveRight = getDirectionalValue(isLeftToRight == mirrored, rightValue, leftValue);
				String previousValue = null;
				if (match.getComparison().isThreeWay()) {
					previousValue = getPreviousValue(eStructuralFeature, match.getOrigin());
				}
				if (isFromLeft != isLeftToRight != mirrored) {
					if (previousValue == null) {
						previousValue = effectiveRight;
					}
					if (previousValue.isEmpty()) {
						body = getString(
								getDirectionalKey(isLeftToRight, "ContextualTooltip.set.left.reject.empty"),
								structuralFeatureName, containerName, effectiveLeft);
					} else {
						body = getString(
								getDirectionalKey(isLeftToRight, "ContextualTooltip.set.left.reject"),
								structuralFeatureName, containerName, previousValue, effectiveLeft);
					}
					tooltip = rejectAndChanged(body, isLeftToRight);
				} else {
					if (previousValue == null) {
						previousValue = effectiveLeft;
					}
					if (previousValue.isEmpty()) {
						body = getString(
								getDirectionalKey(isLeftToRight, "ContextualTooltip.set.right.reject.empty"),
								structuralFeatureName, containerName);
					} else {
						body = getString(
								getDirectionalKey(isLeftToRight, "ContextualTooltip.set.right.reject"),
								previousValue, structuralFeatureName, containerName);
					}
					tooltip = rejectAndUnchanged(body, isLeftToRight);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
	}
	// CHECKSTYLE:ON

	/**
	 * Augments the key with a directional indicator to look up the correct message.
	 * 
	 * @param isLeftToRight
	 *            the direction of the change.
	 * @param key
	 *            the key to augment.
	 * @return the appropriate key for the direction.
	 */
	protected String getDirectionalKey(boolean isLeftToRight, String key) {
		if (isLeftToRight) {
			return key + ".leftToRight"; //$NON-NLS-1$
		} else {
			return key;
		}
	}

	/**
	 * Returns either the left or right value, depending on the direction.
	 * 
	 * @param isLeftToRight
	 *            the direction.
	 * @param leftValue
	 *            the left value.
	 * @param rightValue
	 *            the right value.
	 * @return either the left or right value, depending on the direction.
	 */
	protected String getDirectionalValue(boolean isLeftToRight, String leftValue, String rightValue) {
		if (isLeftToRight) {
			return rightValue;
		} else {
			return leftValue;
		}
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
	protected String setUnsetTooltip(MergeMode mode, T diff, boolean isFromLeft) {
		String value = getLabel(diff);

		// compute the name of the structural feature modified
		EStructuralFeature structuralFeature = MatchUtil.getStructuralFeature(diff);
		String structuralFeatureName = structuralFeature.getName();

		// The name of the container of the structural feature
		String containerName = ""; //$NON-NLS-1$
		Match match = diff.getMatch();
		if (isFromLeft && match.getLeft() != null) {
			containerName = match.getLeft().eClass().getName();
		} else if (!isFromLeft && match.getRight() != null) {
			containerName = match.getRight().eClass().getName();
		}

		String tooltip;
		String body;
		boolean mirrored = isMirrored(diff);
		boolean isLeftToRight = isLeftToRight(diff, mode);
		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft != mirrored) {
					body = getString("ContextualTooltip.unset.left.leftToRight", structuralFeatureName, //$NON-NLS-1$
							containerName, value);
				} else {
					body = getString("ContextualTooltip.unset.right.leftToRight", structuralFeatureName, //$NON-NLS-1$
							containerName, value);
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft != mirrored) {
					body = getString("ContextualTooltip.unset.left.rightToLeft", structuralFeatureName, //$NON-NLS-1$
							containerName, value);
				} else {
					body = getString("ContextualTooltip.unset.right.rightToLeft", structuralFeatureName, //$NON-NLS-1$
							containerName, value);
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft != isLeftToRight != mirrored) {
					body = getString(getDirectionalKey(isLeftToRight, "ContextualTooltip.unset.left.accept"), //$NON-NLS-1$
							structuralFeatureName, containerName);
					tooltip = acceptAndUnchanged(body, isLeftToRight);
				} else {
					body = getString(getDirectionalKey(isLeftToRight, "ContextualTooltip.unset.right.accept"), //$NON-NLS-1$
							structuralFeatureName, containerName, value);
					tooltip = acceptAndChanged(body, isLeftToRight);
				}
				break;
			case REJECT:
				if (isFromLeft != isLeftToRight != mirrored) {
					body = getString(getDirectionalKey(isLeftToRight, "ContextualTooltip.unset.left.reject"), //$NON-NLS-1$
							structuralFeatureName, containerName, value);
					tooltip = rejectAndChanged(body, isLeftToRight);
				} else {
					body = getString(getDirectionalKey(isLeftToRight, "ContextualTooltip.unset.right.reject"), //$NON-NLS-1$
							structuralFeatureName, containerName, value);
					tooltip = rejectAndUnchanged(body, isLeftToRight);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
	}

	/**
	 * Compute the tooltip for a resource location change.
	 *
	 * @param mode
	 *            The comparison mode
	 * @param diff
	 *            The diff
	 * @param isFromLeft
	 *            True if the modification come from the left side
	 * @return the tooltip
	 */
	protected String setResourceLocationChangeTooltip(MergeMode mode, T diff, boolean isFromLeft) {
		String oldValue;
		String newValue;
		if (diff instanceof ResourceAttachmentChange) {
			// ResourceAttachmentChange change = (ResourceAttachmentChange)diff;
			// oldValue = change.getBaseLocation();
			// newValue = change.getChangedLocation();
			return "not supported yet"; //$NON-NLS-1$
		} else if (diff instanceof ResourceLocationChange) {
			ResourceLocationChange change = (ResourceLocationChange)diff;
			oldValue = change.getBaseLocation();
			newValue = change.getChangedLocation();
		} else {
			return ""; //$NON-NLS-1$
		}

		String body;
		String tooltip;
		boolean mirrored = isMirrored(diff);
		boolean isLeftToRight = isLeftToRight(diff, mode);
		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft != mirrored) {
					body = getString("ContextualTooltip.rlc.left.leftToRight", newValue); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.rlc.right.leftToRight", oldValue); //$NON-NLS-1$
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft != mirrored) {
					body = getString("ContextualTooltip.rlc.left.rightToLeft", oldValue); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.rlc.right.rightToLeft", newValue); //$NON-NLS-1$
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft != isLeftToRight != mirrored) {
					body = getString(getDirectionalKey(isLeftToRight, "ContextualTooltip.rlc.left.accept"), //$NON-NLS-1$
							newValue);
					tooltip = acceptAndUnchanged(body, isLeftToRight);
				} else {
					body = getString(getDirectionalKey(isLeftToRight, "ContextualTooltip.rlc.right.accept"), //$NON-NLS-1$
							newValue);
					tooltip = acceptAndChanged(body, isLeftToRight);
				}
				break;
			case REJECT:
				if (isFromLeft != isLeftToRight != mirrored) {
					body = getString(getDirectionalKey(isLeftToRight, "ContextualTooltip.rlc.left.reject"), //$NON-NLS-1$
							oldValue);
					tooltip = rejectAndChanged(body, isLeftToRight);
				} else {
					body = getString(getDirectionalKey(isLeftToRight, "ContextualTooltip.rlc.right.reject"), //$NON-NLS-1$
							oldValue);
					tooltip = rejectAndUnchanged(body, isLeftToRight);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
	}

}
