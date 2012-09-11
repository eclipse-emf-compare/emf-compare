/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.spec;

import com.google.common.base.Objects;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.impl.AttributeChangeImpl;
import org.eclipse.emf.compare.utils.DiffUtil;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * This specialization of the {@link AttributeChangeImpl} class allows us to define the derived features and
 * operations implementations.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class AttributeChangeSpec extends AttributeChangeImpl {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#copyLeftToRight()
	 */
	@Override
	public void copyLeftToRight() {
		// Don't merge an already merged (or discarded) diff
		if (getState() != DifferenceState.UNRESOLVED) {
			return;
		}

		// Change the diff's state before we actually merge it : this allows us to avoid requirement cycles.
		setState(DifferenceState.MERGED);
		if (getEquivalence() != null) {
			for (Diff equivalent : getEquivalence().getDifferences()) {
				equivalent.setState(DifferenceState.MERGED);
			}
		}

		if (getSource() == DifferenceSource.LEFT) {
			// merge all "requires" diffs
			mergeRequires(false);

			switch (getKind()) {
				case ADD:
					// Create the same element in right
					addInTarget(false);
					break;
				case DELETE:
					// Delete that same element from right
					removeFromTarget(false);
					break;
				case MOVE:
					moveElement(false);
					break;
				case CHANGE:
					if (isUnset()) {
						removeFromTarget(false);
					} else {
						addInTarget(false);
					}
					break;
				default:
					break;
			}
		} else {
			// merge all "required by" diffs
			mergeRequiredBy(false);

			switch (getKind()) {
				case ADD:
					// We have a ADD on right. we need to revert this addition
					removeFromTarget(false);
					break;
				case DELETE:
					// DELETE in the right. We need to re-create this element
					addInTarget(false);
					break;
				case MOVE:
					moveElement(false);
					break;
				case CHANGE:
					if (isUnset()) {
						// Value has been unset in the right, and we are merging towards right.
						// We need to re-add this element
						addInTarget(false);
					} else {
						// We'll actually need to "reset" this reference to its original value
						resetInTarget(false);
					}
					break;
				default:
					break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#copyRightToLeft()
	 */
	@Override
	public void copyRightToLeft() {
		// Don't merge an already merged (or discarded) diff
		if (getState() != DifferenceState.UNRESOLVED) {
			return;
		}

		// Change the diff's state before we actually merge it : this allows us to avoid requirement cycles.
		setState(DifferenceState.MERGED);
		if (getEquivalence() != null) {
			for (Diff equivalent : getEquivalence().getDifferences()) {
				equivalent.setState(DifferenceState.MERGED);
			}
		}

		if (getSource() == DifferenceSource.LEFT) {
			// merge all "required by" diffs
			mergeRequiredBy(true);

			switch (getKind()) {
				case ADD:
					// We have a ADD on left, thus nothing in right. We need to revert the addition
					removeFromTarget(true);
					break;
				case DELETE:
					// DELETE in the left, thus an element in right. We need to re-create that element
					addInTarget(true);
					break;
				case MOVE:
					moveElement(true);
					break;
				case CHANGE:
					if (isUnset()) {
						// Value has been unset in the left, and we're copying towards the left.
						// We need to re-create the element.
						addInTarget(true);
					} else {
						// We'll actually need to "reset" this reference to its original value
						resetInTarget(true);
					}
					break;
				default:
					break;
			}
		} else {
			// merge all "requires" diffs
			mergeRequires(true);

			switch (getKind()) {
				case ADD:
					addInTarget(true);
					break;
				case DELETE:
					removeFromTarget(true);
					break;
				case MOVE:
					moveElement(true);
					break;
				case CHANGE:
					if (isUnset()) {
						removeFromTarget(true);
					} else {
						addInTarget(true);
					}
					break;
				default:
					break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#discard()
	 */
	@Override
	public void discard() {
		setState(DifferenceState.DISCARDED);
		// Should we also discard equivalent diffs? And diffs that require this one?
	}

	/**
	 * Checks whether this Diff represents the unsetting of an attribute value. Only meant for mono-valued
	 * attributes.
	 * 
	 * @return {@code true} if this Diff represents the unsetting of an attribute.
	 */
	protected boolean isUnset() {
		final EObject expectedContainer;
		if (getSource() == DifferenceSource.LEFT) {
			expectedContainer = getMatch().getLeft();
		} else {
			expectedContainer = getMatch().getRight();
		}

		final Object currentValue = expectedContainer.eGet(getAttribute());
		// Though not the "default value", we consider that an empty string is an unset attribute.
		final Object defaultValue = getAttribute().getDefaultValue();
		return currentValue == null || currentValue.equals(defaultValue)
				|| (defaultValue == null && "".equals(currentValue)); //$NON-NLS-1$
	}

	/**
	 * This will merge all {@link #getRequiredBy() differences that require us} in the given direction.
	 * 
	 * @param rightToLeft
	 *            If {@code true}, {@link #copyRightToLeft() apply} all {@link #getRequiredBy() differences
	 *            that require us}. Otherwise, {@link #copyLeftToRight() revert} them.
	 */
	protected void mergeRequiredBy(boolean rightToLeft) {
		// TODO log back to the user what we will merge along?
		for (Diff dependency : getRequiredBy()) {
			if (rightToLeft) {
				dependency.copyRightToLeft();
			} else {
				dependency.copyLeftToRight();
			}
		}
	}

	/**
	 * This will merge all {@link #getRequires() required differences} in the given direction.
	 * 
	 * @param rightToLeft
	 *            If {@code true}, {@link #copyRightToLeft() apply} all {@link #getRequires() required
	 *            differences}. Otherwise, {@link #copyLeftToRight() revert} them.
	 */
	protected void mergeRequires(boolean rightToLeft) {
		// TODO log back to the user what we will merge along?
		for (Diff dependency : getRequires()) {
			if (rightToLeft) {
				dependency.copyRightToLeft();
			} else {
				dependency.copyLeftToRight();
			}
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
	 * @param rightToLeft
	 *            Tells us whether we are to add an object on the left or right side.
	 */
	@SuppressWarnings("unchecked")
	protected void addInTarget(boolean rightToLeft) {
		final EObject expectedContainer;
		if (rightToLeft) {
			expectedContainer = getMatch().getLeft();
		} else {
			expectedContainer = getMatch().getRight();
		}

		if (expectedContainer == null) {
			// FIXME throw exception? log? re-try to merge our requirements?
			// one of the "required" diffs should have created our container.
		} else {
			final Comparison comparison = getMatch().getComparison();
			final Object expectedValue = getValue();
			// We have the container, attribute and value. We need to know the insertion index.
			if (getAttribute().isMany()) {
				final int insertionIndex = DiffUtil.findInsertionIndex(comparison, this, rightToLeft);

				final List<Object> targetList = (List<Object>)expectedContainer.eGet(getAttribute());
				if (targetList instanceof InternalEList<?>) {
					((InternalEList<Object>)targetList).addUnique(insertionIndex, expectedValue);
				} else {
					targetList.add(insertionIndex, expectedValue);
				}
			} else {
				expectedContainer.eSet(getAttribute(), expectedValue);
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
	 * @param rightToLeft
	 *            Tells us whether we are to add an object on the left or right side.
	 */
	@SuppressWarnings("unchecked")
	protected void removeFromTarget(boolean rightToLeft) {
		final EObject currentContainer;
		if (rightToLeft) {
			currentContainer = getMatch().getLeft();
		} else {
			currentContainer = getMatch().getRight();
		}

		if (currentContainer == null) {
			// FIXME throw exception? log? re-try to merge our requirements?
		} else {
			final Object expectedValue = getValue();
			// We have the container, attribute and value to remove.
			if (getAttribute().isMany()) {
				/*
				 * TODO if the same value appears twice, should we try and find the one that has actually been
				 * deleted? Will it happen that often? For now, remove the first occurence we find.
				 */
				final List<Object> targetList = (List<Object>)currentContainer.eGet(getAttribute());
				targetList.remove(expectedValue);
			} else {
				currentContainer.eUnset(getAttribute());
			}
		}
	}

	/**
	 * This will be called when trying to copy a "MOVE" diff.
	 * 
	 * @param rightToLeft
	 *            Whether we should move the value in the left or right side.
	 */
	protected void moveElement(boolean rightToLeft) {
		final EObject expectedContainer;
		if (rightToLeft) {
			expectedContainer = getMatch().getLeft();
		} else {
			expectedContainer = getMatch().getRight();
		}

		if (expectedContainer == null) {
			// TODO throws exception?
		} else {
			final Comparison comparison = getMatch().getComparison();
			final Object expectedValue = getValue();

			// We now know the target container, target attribute and target value.
			doMove(comparison, expectedContainer, expectedValue, rightToLeft);
		}
	}

	/**
	 * This will do the actual work of moving the element into its attribute. All sanity checks were made in
	 * {@link #moveElement(boolean)} and no more verification will be made here.
	 * 
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
	protected void doMove(Comparison comparison, EObject expectedContainer, Object expectedValue,
			boolean rightToLeft) {
		if (getAttribute().isMany()) {
			int insertionIndex = DiffUtil.findInsertionIndex(comparison, this, rightToLeft);
			/*
			 * However, it could still have been located "before" its new index, in which case we need to take
			 * it into account.
			 */
			final List<Object> targetList = (List<Object>)expectedContainer.eGet(getAttribute());
			if (insertionIndex > targetList.indexOf(expectedValue)) {
				insertionIndex--;
			}

			if (targetList instanceof EList<?>) {
				((EList<Object>)targetList).move(insertionIndex, expectedValue);
			} else {
				targetList.remove(expectedValue);
				targetList.add(insertionIndex, expectedValue);
			}
		} else {
			// This will never happen with the default diff engine, but may still be done from extenders
			expectedContainer.eSet(getAttribute(), expectedValue);
		}
	}

	/**
	 * This will be called by the merge operations in order to reset an attribute to its original value, be
	 * that the left or right side.
	 * <p>
	 * Should never be called on multi-valued attributes.
	 * </p>
	 * 
	 * @param rightToLeft
	 *            Tells us the direction of this merge operation.
	 */
	protected void resetInTarget(boolean rightToLeft) {
		final EObject targetContainer;
		if (rightToLeft) {
			targetContainer = getMatch().getLeft();
		} else {
			targetContainer = getMatch().getRight();
		}

		final EObject originContainer;
		if (getMatch().getComparison().isThreeWay()) {
			originContainer = getMatch().getOrigin();
		} else if (rightToLeft) {
			originContainer = getMatch().getRight();
		} else {
			originContainer = getMatch().getLeft();
		}

		if (originContainer == null || !targetContainer.eIsSet(getAttribute())
				|| !originContainer.eIsSet(getAttribute())) {
			targetContainer.eUnset(getAttribute());
		} else {
			final Object expectedValue = originContainer.eGet(getAttribute());
			targetContainer.eSet(getAttribute(), expectedValue);
		}
	}

	@Override
	public String toString() {
		EDataType eAttributeType = getAttribute().getEAttributeType();
		final String valueString;
		if (!FeatureMapUtil.isFeatureMap(getAttribute())) {
			valueString = EcoreUtil.convertToString(eAttributeType, getValue());
		} else {
			valueString = getValue().toString();
		}

		return Objects.toStringHelper(this).add("attribute", //$NON-NLS-1$
				getAttribute().getEContainingClass().getName() + "." + getAttribute().getName()).add("value", //$NON-NLS-1$ //$NON-NLS-2$
				valueString)
				.add("kind", getKind()).add("source", getSource()).add("state", getState()).toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
