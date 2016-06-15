/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - [446947, 458147] Support for three-way text merging 
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import static org.eclipse.emf.compare.utils.ReferenceUtil.safeEGet;
import static org.eclipse.emf.compare.utils.ReferenceUtil.safeEIsSet;
import static org.eclipse.emf.compare.utils.ReferenceUtil.safeESet;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.internal.ThreeWayTextDiff;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;

/**
 * This specific implementation of {@link AbstractMerger} will be used to merge attribute changes.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class AttributeChangeMerger extends AbstractMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#isMergerFor(org.eclipse.emf.compare.Diff)
	 */
	public boolean isMergerFor(Diff target) {
		return target instanceof AttributeChange;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.AbstractMerger#accept(org.eclipse.emf.compare.Diff, boolean)
	 */
	@Override
	protected void accept(final Diff diff, boolean rightToLeft) {
		AttributeChange attributeChange = (AttributeChange)diff;
		switch (diff.getKind()) {
			case ADD:
				// Create the same element in right
				addInTarget(attributeChange, rightToLeft);
				break;
			case DELETE:
				// Delete that same element from right
				removeFromTarget(attributeChange, rightToLeft);
				break;
			case MOVE:
				moveElement(attributeChange, rightToLeft);
				break;
			case CHANGE:
				changeValue(attributeChange, rightToLeft);
				break;
			default:
				break;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.AbstractMerger#reject(org.eclipse.emf.compare.Diff, boolean)
	 */
	@Override
	protected void reject(Diff diff, boolean rightToLeft) {
		AttributeChange attributeChange = (AttributeChange)diff;
		switch (diff.getKind()) {
			case ADD:
				// We have a ADD on right. we need to revert this addition
				removeFromTarget(attributeChange, rightToLeft);
				break;
			case DELETE:
				// DELETE in the right. We need to re-create this element
				addInTarget(attributeChange, rightToLeft);
				break;
			case MOVE:
				moveElement(attributeChange, rightToLeft);
				break;
			case CHANGE:
				changeValue(attributeChange, rightToLeft);
				break;
			default:
				break;
		}
	}

	/**
	 * This will be called when we need to create an element in the target side.
	 * <p>
	 * All necessary sanity checks have been made to ensure that the current operation is one that should
	 * create an object in its side or add an objet to an attribute. In other words, either :
	 * <ul>
	 * <li>We are copying from right to left and
	 * <ul>
	 * <li>we are copying an addition to the right side (we need to create the same object in the left), or
	 * </li>
	 * <li>we are copying a deletion from the left side (we need to revert the deletion).</li>
	 * </ul>
	 * </li>
	 * <li>We are copying from left to right and
	 * <ul>
	 * <li>we are copying a deletion from the right side (we need to revert the deletion), or</li>
	 * <li>we are copying an addition to the left side (we need to create the same object in the right).</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * </p>
	 * 
	 * @param diff
	 *            The diff we are currently merging.
	 * @param rightToLeft
	 *            Tells us whether we are to add an object on the left or right side.
	 */
	@SuppressWarnings("unchecked")
	protected void addInTarget(AttributeChange diff, boolean rightToLeft) {
		final Match match = diff.getMatch();
		final EObject targetContainer = getTargetContainer(diff, rightToLeft);

		if (targetContainer == null) {
			// FIXME throw exception? log? re-try to merge our requirements?
			// one of the "required" diffs should have created our container.
		} else {
			final Comparison comparison = match.getComparison();
			final EStructuralFeature attribute = diff.getAttribute();
			final Object expectedValue = diff.getValue();
			// We have the container, attribute and value. We need to know the insertion index.
			if (attribute.isMany()) {
				final int insertionIndex = findInsertionIndex(comparison, diff, rightToLeft);

				final List<Object> targetList = (List<Object>)safeEGet(targetContainer, attribute);
				addAt(targetList, expectedValue, insertionIndex);
			} else {
				safeESet(targetContainer, attribute, expectedValue);
			}
		}
	}

	/**
	 * This will be called when we need to remove an element from the target side.
	 * <p>
	 * All necessary sanity checks have been made to ensure that the current operation is one that should
	 * delete an object. In other words, we are :
	 * <ul>
	 * <li>Copying from right to left and either
	 * <ul>
	 * <li>we are copying a deletion from the right side (we need to remove the same object in the left) or,
	 * </li>
	 * <li>we are copying an addition to the left side (we need to revert the addition).</li>
	 * </ul>
	 * </li>
	 * <li>Copying from left to right and either
	 * <ul>
	 * <li>we are copying an addition to the right side (we need to revert the addition), or.</li>
	 * <li>we are copying a deletion from the left side (we need to remove the same object in the right).</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * </p>
	 * 
	 * @param diff
	 *            The diff we are currently merging.
	 * @param rightToLeft
	 *            Tells us whether we are to add an object on the left or right side.
	 */
	@SuppressWarnings("unchecked")
	protected void removeFromTarget(AttributeChange diff, boolean rightToLeft) {
		final EObject currentContainer = getTargetContainer(diff, rightToLeft);

		if (currentContainer == null) {
			// FIXME throw exception? log? re-try to merge our requirements?
		} else {
			final Object expectedValue = diff.getValue();
			final EStructuralFeature attribute = diff.getAttribute();
			// We have the container, attribute and value to remove.
			if (attribute.isMany()) {
				/*
				 * TODO if the same value appears twice, should we try and find the one that has actually been
				 * deleted? Will it happen that often? For now, remove the first occurence we find.
				 */
				final List<Object> targetList = (List<Object>)safeEGet(currentContainer, attribute);
				targetList.remove(expectedValue);
			} else {
				currentContainer.eUnset(attribute);
			}
		}
	}

	/**
	 * This will be called when trying to copy a "MOVE" diff.
	 * 
	 * @param diff
	 *            The diff we are currently merging.
	 * @param rightToLeft
	 *            Whether we should move the value in the left or right side.
	 */
	protected void moveElement(AttributeChange diff, boolean rightToLeft) {
		final EObject expectedContainer = getTargetContainer(diff, rightToLeft);

		if (expectedContainer == null) {
			// TODO throws exception?
		} else {
			final Comparison comparison = diff.getMatch().getComparison();
			final Object expectedValue = diff.getValue();

			// We now know the target container, target attribute and target value.
			doMove(diff, comparison, expectedContainer, expectedValue, rightToLeft);
		}
	}

	/**
	 * This will do the actual work of moving the element into its attribute. All sanity checks were made in
	 * {@link #moveElement(boolean)} and no more verification will be made here.
	 * 
	 * @param diff
	 *            The diff we are currently merging.
	 * @param comparison
	 *            Comparison holding this Diff.
	 * @param expectedContainer
	 *            The container in which we are reorganizing an attribute.
	 * @param expectedValue
	 *            The value that is to be moved within its attribute.
	 * @param rightToLeft
	 *            Whether we should move the value in the left or right side.
	 */
	@SuppressWarnings("unchecked")
	protected void doMove(AttributeChange diff, Comparison comparison, EObject expectedContainer,
			Object expectedValue, boolean rightToLeft) {
		final EStructuralFeature attribute = diff.getAttribute();
		if (attribute.isMany()) {
			// Element to move cannot be part of the LCS... or there would not be a MOVE diff
			int insertionIndex = findInsertionIndex(comparison, diff, rightToLeft);
			/*
			 * However, it could still have been located "before" its new index, in which case we need to take
			 * it into account.
			 */
			final List<Object> targetList = (List<Object>)safeEGet(expectedContainer, attribute);
			final int currentIndex = targetList.indexOf(expectedValue);
			if (insertionIndex > currentIndex) {
				insertionIndex--;
			}

			if (targetList instanceof EList<?>) {
				if (insertionIndex < 0 || insertionIndex > targetList.size()) {
					((EList<Object>)targetList).move(targetList.size() - 1, expectedValue);
				} else {
					((EList<Object>)targetList).move(insertionIndex, expectedValue);
				}
			} else {
				targetList.remove(expectedValue);
				if (insertionIndex < 0 || insertionIndex > targetList.size()) {
					targetList.add(expectedValue);
				} else {
					targetList.add(insertionIndex, expectedValue);
				}
			}
		} else {
			// This will never happen with the default diff engine, but may still be done from extenders
			safeESet(expectedContainer, attribute, expectedValue);
		}
	}

	/**
	 * This will be called by the merge operations in order to reset an attribute to its original value, be
	 * that the left or right side.
	 * <p>
	 * Should never be called on multi-valued attributes.
	 * </p>
	 * 
	 * @param diff
	 *            The diff we are currently merging.
	 * @param rightToLeft
	 *            Tells us the direction of this merge operation.
	 * @deprecated this has been refactored into {@link #changeValue(AttributeChange, boolean)}.
	 */
	@Deprecated
	protected void resetInTarget(AttributeChange diff, boolean rightToLeft) {
		final EStructuralFeature attribute = diff.getAttribute();
		final EObject targetContainer = getTargetContainer(diff, rightToLeft);
		final EObject sourceContainer = getSourceContainer(diff, rightToLeft);

		if (sourceContainer == null || !safeEIsSet(targetContainer, attribute)
				|| !safeEIsSet(sourceContainer, attribute)) {
			targetContainer.eUnset(attribute);
		} else {
			final Object expectedValue = safeEGet(sourceContainer, attribute);
			safeESet(targetContainer, attribute, expectedValue);
		}
	}

	/**
	 * This will be called by the merge operations in order to change single-valued attributes.
	 * 
	 * @param diff
	 *            The diff we are currently merging.
	 * @param rightToLeft
	 *            Direction of the merge.
	 */
	protected void changeValue(AttributeChange diff, boolean rightToLeft) {
		final EStructuralFeature attribute = diff.getAttribute();
		final Object targetValue = getTargetValue(diff, rightToLeft);
		final EObject targetContainer = getTargetContainer(diff, rightToLeft);

		if (isUnset(diff, targetValue)) {
			targetContainer.eUnset(attribute);
		} else {
			safeESet(targetContainer, attribute, targetValue);
		}
	}

	/**
	 * Returns the target value, that is, the value to be set when merging the given {@code diff} in the
	 * direction indicated by {@code rightToLeft}.
	 * 
	 * @param diff
	 *            The diff we are currently merging.
	 * @param rightToLeft
	 *            Direction of the merge.
	 * @return The target value to be set when merging.
	 */
	private Object getTargetValue(AttributeChange diff, boolean rightToLeft) {
		final EStructuralFeature attribute = diff.getAttribute();
		final EObject targetContainer = getTargetContainer(diff, rightToLeft);
		final EObject sourceContainer = getSourceContainer(diff, rightToLeft);
		final Object sourceValue = safeEGet(sourceContainer, attribute);

		final Object targetValue;
		if (isEnumChangeOfDynamicEObject(diff, targetContainer)) {
			final EEnum eEnum = getAttributeTypeEnumFromDynamicObject(targetContainer, attribute);
			targetValue = eEnum.getEEnumLiteral(((ENamedElement)sourceValue).getName());
		} else if (requireThreeWayTextMerge(diff)) {
			targetValue = performThreeWayTextMerge(diff, rightToLeft);
		} else {
			targetValue = sourceValue;
		}

		return targetValue;
	}

	/**
	 * Specifies whether the given {@code diff} concerns a change of a dynamic EObject at an attribute with an
	 * EEnum type.
	 * 
	 * @param diff
	 *            The diff to check.
	 * @param targetContainer
	 *            The target container.
	 * @return <code>true</code> if is a change of a dynamic EObject with an EEnum-typed attribute,
	 *         <code>false</code> otherwise.
	 */
	private boolean isEnumChangeOfDynamicEObject(AttributeChange diff, EObject targetContainer) {
		return diff.getAttribute().getEType() instanceof EEnum
				&& targetContainer instanceof DynamicEObjectImpl;
	}

	/**
	 * Returns the EEnum that is the attribute type of the given {@code attribute} used in the given container
	 * object {@code dynamicObject}.
	 * 
	 * @param dynamicObject
	 *            The container object.
	 * @param attribute
	 *            The attribute.
	 * @return The EEnum acting as type of {@code attribute}.
	 */
	private EEnum getAttributeTypeEnumFromDynamicObject(EObject dynamicObject, EStructuralFeature attribute) {
		return (EEnum)((EEnumLiteral)safeEGet(dynamicObject, attribute)).eContainer();
	}

	/**
	 * Returns the source container, that is the original container holding the original value before the
	 * given {@code diff} is applied. This is different depending on direction indicated by
	 * {@code rightToLeft}.
	 * 
	 * @param diff
	 *            The diff we are currently merging.
	 * @param rightToLeft
	 *            Direction of the merge.
	 * @return The source container.
	 */
	private EObject getSourceContainer(AttributeChange diff, boolean rightToLeft) {
		final EObject sourceContainer;
		final Match match = diff.getMatch();
		if (match.getComparison().isThreeWay() && isRejectingChange(diff, rightToLeft)) {
			sourceContainer = match.getOrigin();
		} else if (rightToLeft) {
			sourceContainer = match.getRight();
		} else {
			sourceContainer = match.getLeft();
		}
		return sourceContainer;
	}

	/**
	 * Returns the target container, that is the container holding the value to be updated when merging the
	 * given {@code diff} in the direction indicated by {@code rightToLeft}.
	 * 
	 * @param diff
	 *            The diff we are currently merging.
	 * @param rightToLeft
	 *            Direction of the merge.
	 * @return The target container to be updated when merging.
	 */
	private EObject getTargetContainer(AttributeChange diff, boolean rightToLeft) {
		final Match match = diff.getMatch();
		if (rightToLeft) {
			return match.getLeft();
		} else {
			return match.getRight();
		}
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
	 * Specifies whether a three-way text merge is required for applying the given {@code diff} in the
	 * direction indicated in {@code rightToLeft}.
	 * <p>
	 * Three-way text merging is required when accepting or rejecting the changes of a
	 * {@link #isStringAttribute(EAttribute) String attributes} in a three-way merge scenario.
	 * </p>
	 * 
	 * @param diff
	 *            The diff to be applied.
	 * @return <code>true</code> if three-way text merging is required, <code>false</code> otherwise.
	 */
	private boolean requireThreeWayTextMerge(AttributeChange diff) {
		return diff.getMatch().getComparison().isThreeWay() && isStringAttribute(diff.getAttribute());
	}

	/**
	 * Specifies whether the given {@code attribute} is an attribute of type String.
	 * 
	 * @param attribute
	 *            The attribute to be checked.
	 * @return <code>true</code> if it is a String attribute, <code>false</code> otherwise.
	 */
	private boolean isStringAttribute(EAttribute attribute) {
		return attribute.getEAttributeType().getInstanceClass() == String.class;
	}

	/**
	 * Specifies whether applying the given {@code diff} in the direction indicated in {@code rightToLeft}
	 * means accepting the change as opposed to rejecting the change.
	 * 
	 * @param diff
	 *            The diff to be checked.
	 * @param rightToLeft
	 *            The direction of applying {@code diff}.
	 * @return <code>true</code> if it means accepting the change, <code>false</code> otherwise.
	 */
	private boolean isAcceptingChange(AttributeChange diff, boolean rightToLeft) {
		return (DifferenceSource.LEFT.equals(diff.getSource()) && !rightToLeft)
				|| (DifferenceSource.RIGHT.equals(diff.getSource()) && rightToLeft);
	}

	/**
	 * Specifies whether applying the given {@code diff} in the direction indicated in {@code rightToLeft}
	 * means rejecting the change as opposed to accepting the change.
	 * 
	 * @param diff
	 *            The diff to be checked.
	 * @param rightToLeft
	 *            The direction of applying {@code diff}.
	 * @return <code>true</code> if it means rejecting the change, <code>false</code> otherwise.
	 */
	private boolean isRejectingChange(AttributeChange diff, boolean rightToLeft) {
		return !isAcceptingChange(diff, rightToLeft);
	}

	/**
	 * Performs a three-way text merge for the given {@code diff} and returns the merged text.
	 * <p>
	 * Depending on whether the given {@code diff} is an accept or reject in the context of the merge
	 * direction indicated by {@code rightToLeft}, this method will perform different strategies of merging.
	 * </p>
	 * 
	 * @param diff
	 *            The diff for which a three-way text diff is to be performed.
	 * @param rightToLeft
	 *            The direction of applying the {@code diff}.
	 * @return The merged text.
	 */
	private String performThreeWayTextMerge(AttributeChange diff, boolean rightToLeft) {
		if (isAcceptingChange(diff, rightToLeft)) {
			return performAcceptingThreeWayTextMerge(diff);
		} else {
			return performRejectingThreeWayTextMerge(diff, rightToLeft);
		}
	}

	/**
	 * Performs a three-way text merge accepting the given {@code diff} and returns the merged text.
	 * <p>
	 * This method must only be called for {@link #isStringAttribute(EAttribute) String attributes}. As the
	 * three-way text merging is symmetric, the result is equal irrespectively of the direction of merging as
	 * long as applying the change {@link #isAcceptingChange(AttributeChange, boolean) means accepting it} as
	 * opposed to rejecting it.
	 * </p>
	 * 
	 * @param diff
	 *            The diff for which a three-way text diff is to be performed.
	 * @return The merged text.
	 */
	private String performAcceptingThreeWayTextMerge(AttributeChange diff) {
		final Match match = diff.getMatch();
		final EAttribute attribute = diff.getAttribute();
		final String originValue = (String)safeEGet(match.getOrigin(), attribute);
		final String leftValue = (String)safeEGet(match.getLeft(), attribute);
		final String rightValue = (String)safeEGet(match.getRight(), attribute);

		return performThreeWayTextMerge(leftValue, rightValue, originValue);
	}

	/**
	 * Performs a three-way text merge rejecting the given {@code diff} and returns the merged text.
	 * <p>
	 * When rejecting an attribute change, we need to undo its effects on the attribute value, which is in
	 * most of the cases done by setting the value to the original value. However, if there is a concurrent
	 * attribute change of the same attribute value at the opposite side, it might have been merged to the
	 * current side already. Therefore, we need to undo only those differences in the attribute value that
	 * come from the current to-be-rejected diff.
	 * </p>
	 * <p>
	 * This is done by applying a normal three-way merge, but instead of the origin value, left value, and
	 * right value, we compute the three-way merge as follows: as origin value, we use the value of the
	 * {@code diff}, which is the value as it was set on the respective side. As a left value, the value of
	 * the current side as it is currently stored in the model; thus, a potential merging of opposite diffs
	 * may have changed this value already. And as a right value, we use the actual origin value from the
	 * origin model.
	 * </p>
	 * <p>
	 * Since we consider the current value as it is in the model right now (including potential previous
	 * merges) and the origin value as the two changed sides, a three-way merge will apply the changes we did
	 * through merging and reset all other to the origin value.
	 * </p>
	 * <p>
	 * Note that, if {@code diff} is an unset (that is, the current value is empty or null or default), we
	 * just use the original value.
	 * </p>
	 * 
	 * @param diff
	 *            The diff for which a three-way text diff is to be performed.
	 * @param rightToLeft
	 *            The direction of applying the {@code diff}.
	 * @return The merged text.
	 */
	private String performRejectingThreeWayTextMerge(AttributeChange diff, boolean rightToLeft) {
		final EAttribute attribute = diff.getAttribute();
		final EObject originContainer = diff.getMatch().getOrigin();
		final String originValue = (String)safeEGet(originContainer, attribute);
		final String changedValueFromModel = getChangedValueFromModel(diff);
		final String changedValue = (String)diff.getValue();

		if (isUnset(diff, changedValueFromModel)) {
			return originValue;
		} else {
			return performThreeWayTextMerge(changedValueFromModel, originValue, changedValue);
		}
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
	 * Performs a three-way text merge for the given {@code origin}, {@code left}, and {@code right} text
	 * versions.
	 * 
	 * @param left
	 *            The left version of the String.
	 * @param right
	 *            The right version of the String.
	 * @param origin
	 *            The original version of the String.
	 * @return The merged version.
	 * @since 3.2
	 */
	protected String performThreeWayTextMerge(final String left, final String right, final String origin) {
		ThreeWayTextDiff textDiff = new ThreeWayTextDiff(origin, left, right);
		return textDiff.getMerged();
	}

	/**
	 * This will be used by the distinct merge actions in order to find the index at which a value should be
	 * inserted in its target list. See {@link DiffUtil#findInsertionIndex(Comparison, Diff, boolean)} for
	 * more on this.
	 * <p>
	 * Sub-classes can override this if the insertion order is irrelevant. A return value of {@code -1} will
	 * be considered as "no index" and the value will be inserted at the end of its target list.
	 * </p>
	 * 
	 * @param comparison
	 *            This will be used in order to retrieve the Match for EObjects when comparing them.
	 * @param diff
	 *            The diff which merging will trigger the need for an insertion index in its target list.
	 * @param rightToLeft
	 *            {@code true} if the merging will be done into the left list, so that we should consider the
	 *            right model as the source and the left as the target.
	 * @return The index at which this {@code diff}'s value should be inserted into the 'target' list, as
	 *         inferred from {@code rightToLeft}. {@code -1} if the value should be inserted at the end of its
	 *         target list.
	 * @see DiffUtil#findInsertionIndex(Comparison, Diff, boolean)
	 */
	protected int findInsertionIndex(Comparison comparison, Diff diff, boolean rightToLeft) {
		return DiffUtil.findInsertionIndex(comparison, diff, rightToLeft);
	}
}
