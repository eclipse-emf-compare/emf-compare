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
package org.eclipse.emf.compare.tooltip;

import static org.eclipse.emf.compare.internal.EMFCompareEditMessages.getString;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.ResourceLocationChange;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.provider.ISemanticObjectLabelProvider;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

/**
 * This class defines usual methods for contextual tooltips.
 *
 * @param <T>
 *            The parametric type of the class
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public abstract class AbstractTooltipProvider<T extends Diff> extends AdapterImpl {

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
	 * Create the final tooltip for an accepted change which lead to a modification of the left side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @return the complete tooltip
	 */
	protected String acceptAndChanged(String value) {
		String accept = getString("ContextualTooltip.acceptChange"); //$NON-NLS-1$
		String modify = getString("ContextualTooltip.readonly.leftChanged"); //$NON-NLS-1$

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
	 * Create the final tooltip for an accepted change which don't lead to a modification of the left side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @return the complete tooltip
	 */
	protected String acceptAndUnchanged(String value) {
		String accept = getString("ContextualTooltip.acceptChange"); //$NON-NLS-1$
		String modify = getString("ContextualTooltip.readonly.leftUnchanged"); //$NON-NLS-1$

		StringBuilder builder = new StringBuilder();
		builder.append(accept).append(LINE_SEPARATOR).append(value).append(LINE_SEPARATOR).append(modify);
		return builder.toString();
	}

	/**
	 * Create the final tooltip for a rejected change which lead to a modification of the left side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @return the complete tooltip
	 */
	protected String rejectAndChanged(String value) {
		String accept = getString("ContextualTooltip.rejectChange"); //$NON-NLS-1$
		String modify = getString("ContextualTooltip.readonly.leftChanged"); //$NON-NLS-1$

		StringBuilder builder = new StringBuilder();
		builder.append(accept).append(LINE_SEPARATOR).append(value).append(LINE_SEPARATOR).append(modify);
		return builder.toString();
	}

	/**
	 * Create the final tooltip for a rejected change which don't lead to a modification of the left side.
	 *
	 * @param value
	 *            The body of the tooltip
	 * @return the complete tooltip
	 */
	protected String rejectAndUnchanged(String value) {
		String accept = getString("ContextualTooltip.rejectChange"); //$NON-NLS-1$
		String modify = getString("ContextualTooltip.readonly.leftUnchanged"); //$NON-NLS-1$

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
			ancestor = eObject.eGet(eStructuralFeature);
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
	 * @return the label of the object
	 */
	protected String getLabelFromObject(EObject eObject) {
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

		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.delete.nonContainment.left.leftToRight", value); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.delete.nonContainment.right.leftToRight", value); //$NON-NLS-1$
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.delete.nonContainment.left.rightToLeft", value); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.delete.nonContainment.right.rightToLeft", value); //$NON-NLS-1$
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.delete.nonContainment.left.accept", value); //$NON-NLS-1$
					tooltip = acceptAndUnchanged(body);
				} else {
					body = getString("ContextualTooltip.delete.nonContainment.right.accept", value); //$NON-NLS-1$
					tooltip = acceptAndChanged(body);
				}
				break;
			case REJECT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.delete.nonContainment.left.reject", value); //$NON-NLS-1$
					tooltip = rejectAndChanged(body);
				} else {
					body = getString("ContextualTooltip.delete.nonContainment.right.reject", value); //$NON-NLS-1$
					tooltip = rejectAndUnchanged(body);
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

		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.nonContainment.left.leftToRight", value); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.add.nonContainment.right.leftToRight", value); //$NON-NLS-1$
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.nonContainment.left.rightToLeft", value); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.add.nonContainment.right.rightToLeft", value); //$NON-NLS-1$
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.nonContainment.left.accept", value); //$NON-NLS-1$
					tooltip = acceptAndUnchanged(body);
				} else {
					body = getString("ContextualTooltip.add.nonContainment.right.accept", value); //$NON-NLS-1$
					tooltip = acceptAndChanged(body);
				}
				break;
			case REJECT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.add.nonContainment.left.reject", value); //$NON-NLS-1$
					tooltip = rejectAndChanged(body);
				} else {
					body = getString("ContextualTooltip.add.nonContainment.right.reject", value); //$NON-NLS-1$
					tooltip = rejectAndUnchanged(body);
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
						body = getString("ContextualTooltip.move.position.left.container.accept", value, //$NON-NLS-1$
								containerValue);
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
	protected String setSetTooltip(MergeMode mode, T diff, boolean isFromLeft) {
		// compute the name of the structural feature modified
		EStructuralFeature eStructuralFeature = MatchUtil.getStructuralFeature(diff);

		// The name of the container of the structural feature
		EObject left = diff.getMatch().getLeft();
		EObject right = diff.getMatch().getRight();
		String containerName = ""; //$NON-NLS-1$
		String rightValue = ""; //$NON-NLS-1$
		String leftValue = ""; //$NON-NLS-1$

		if (isFromLeft && left != null) {
			containerName = left.eClass().getName();
		} else if (!isFromLeft && right != null) {
			containerName = right.eClass().getName();
		}

		if (left != null) {
			leftValue = getPreviousValue(eStructuralFeature, left);
		}
		if (right != null) {
			rightValue = getPreviousValue(eStructuralFeature, right);
		}

		return doSetTooltip(mode, isFromLeft, rightValue, leftValue, containerName, eStructuralFeature, diff);
	}

	/**
	 * This method compute the tooltip with the given parameters.
	 * 
	 * @param mode
	 *            The merge mode
	 * @param isFromLeft
	 *            True if the change comes from left
	 * @param rightValue
	 *            The value of the right element
	 * @param leftValue
	 *            The value of the left element
	 * @param containerName
	 *            The name of the container of the Set diff
	 * @param eStructuralFeature
	 *            The structural feature set
	 * @param diff
	 *            The diff
	 * @return the tooltip
	 */
	private String doSetTooltip(MergeMode mode, boolean isFromLeft, String rightValue, String leftValue,
			String containerName, EStructuralFeature eStructuralFeature, T diff) {
		String tooltip;
		String body;
		String structuralFeatureName = eStructuralFeature.getName();
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
	protected String setUnsetTooltip(MergeMode mode, T diff, boolean isFromLeft) {
		String value = getLabel(diff);

		// compute the name of the structural feature modified
		EStructuralFeature structuralFeature = MatchUtil.getStructuralFeature(diff);
		String structuralFeatureName = structuralFeature.getName();

		// The name of the container of the structural feature
		String containerName = ""; //$NON-NLS-1$
		if (isFromLeft && diff.getMatch().getLeft() != null) {
			containerName = diff.getMatch().getLeft().eClass().getName();
		} else if (!isFromLeft && diff.getMatch().getRight() != null) {
			containerName = diff.getMatch().getRight().eClass().getName();
		}

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
		switch (mode) {
			case LEFT_TO_RIGHT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.rlc.left.leftToRight", newValue); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.rlc.right.leftToRight", oldValue); //$NON-NLS-1$
				}
				tooltip = rightChanged(body);
				break;
			case RIGHT_TO_LEFT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.rlc.left.rightToLeft", oldValue); //$NON-NLS-1$
				} else {
					body = getString("ContextualTooltip.rlc.right.rightToLeft", newValue); //$NON-NLS-1$
				}
				tooltip = rightUnchanged(body);
				break;
			case ACCEPT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.rlc.left.accept", newValue); //$NON-NLS-1$
					tooltip = acceptAndUnchanged(body);
				} else {
					body = getString("ContextualTooltip.rlc.right.accept", newValue); //$NON-NLS-1$
					tooltip = acceptAndChanged(body);
				}
				break;
			case REJECT:
				if (isFromLeft) {
					body = getString("ContextualTooltip.rlc.left.reject", oldValue); //$NON-NLS-1$
					tooltip = rejectAndChanged(body);
				} else {
					body = getString("ContextualTooltip.rlc.right.reject", oldValue); //$NON-NLS-1$
					tooltip = rejectAndUnchanged(body);
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return tooltip;
	}

}
