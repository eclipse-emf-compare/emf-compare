/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
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
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

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
	 * <li>we are copying an addition to the right side (we need to create the same object in the left), or</li>
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
		final EObject expectedContainer;
		if (rightToLeft) {
			expectedContainer = match.getLeft();
		} else {
			expectedContainer = match.getRight();
		}

		if (expectedContainer == null) {
			// FIXME throw exception? log? re-try to merge our requirements?
			// one of the "required" diffs should have created our container.
		} else {
			final Comparison comparison = match.getComparison();
			final EStructuralFeature attribute = diff.getAttribute();
			final Object expectedValue = diff.getValue();
			// We have the container, attribute and value. We need to know the insertion index.
			if (attribute.isMany()) {
				final int insertionIndex = findInsertionIndex(comparison, diff, rightToLeft);

				final List<Object> targetList = (List<Object>)safeEGet(expectedContainer, attribute);
				addAt(targetList, expectedValue, insertionIndex);
			} else {
				safeESet(expectedContainer, attribute, expectedValue);
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
	 * <li>we are copying a deletion from the right side (we need to remove the same object in the left) or,</li>
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
		final EObject currentContainer;
		if (rightToLeft) {
			currentContainer = diff.getMatch().getLeft();
		} else {
			currentContainer = diff.getMatch().getRight();
		}

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
		final EObject expectedContainer;
		if (rightToLeft) {
			expectedContainer = diff.getMatch().getLeft();
		} else {
			expectedContainer = diff.getMatch().getRight();
		}

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
		final Match match = diff.getMatch();
		final EStructuralFeature attribute = diff.getAttribute();
		final EObject targetContainer;
		if (rightToLeft) {
			targetContainer = match.getLeft();
		} else {
			targetContainer = match.getRight();
		}

		final EObject originContainer;
		if (match.getComparison().isThreeWay()) {
			originContainer = match.getOrigin();
		} else if (rightToLeft) {
			originContainer = match.getRight();
		} else {
			originContainer = match.getLeft();
		}

		if (originContainer == null || !safeEIsSet(targetContainer, attribute)
				|| !safeEIsSet(originContainer, attribute)) {
			targetContainer.eUnset(attribute);
		} else {
			final Object expectedValue = safeEGet(originContainer, attribute);
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
		final Match match = diff.getMatch();
		final EStructuralFeature attribute = diff.getAttribute();
		final EObject expectedContainer;
		if (rightToLeft) {
			expectedContainer = match.getLeft();
		} else {
			expectedContainer = match.getRight();
		}

		final EObject originContainer;
		final boolean resetToOrigin = diff.getSource() == DifferenceSource.LEFT && rightToLeft
				|| diff.getSource() == DifferenceSource.RIGHT && !rightToLeft;
		if (resetToOrigin && match.getComparison().isThreeWay()) {
			originContainer = match.getOrigin();
		} else if (rightToLeft) {
			originContainer = match.getRight();
		} else {
			originContainer = match.getLeft();
		}

		final Object targetValue = safeEGet(originContainer, attribute);

		// Though not the "default value", we consider that an empty string is an unset attribute.
		final Object defaultValue = attribute.getDefaultValue();
		boolean isUnset = targetValue == null || targetValue.equals(defaultValue)
				|| (defaultValue == null && "".equals(targetValue)); //$NON-NLS-1$

		if (isUnset) {
			expectedContainer.eUnset(attribute);
		} else {
			safeESet(expectedContainer, attribute, targetValue);
		}
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
