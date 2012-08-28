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
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.impl.ReferenceChangeImpl;
import org.eclipse.emf.compare.utils.DiffUtil;
import org.eclipse.emf.compare.utils.EMFCompareCopier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * This specialization of the {@link referenceChangeImpl} class allows us to define the derived features and
 * operations implementations.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ReferenceChangeSpec extends ReferenceChangeImpl {
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
					// Is it an unset?
					if (getMatch().getLeft() != null) {
						final EObject leftValue = (EObject)getMatch().getLeft().eGet(getReference(), false);
						if (leftValue == null) {
							removeFromTarget(false);
						} else {
							addInTarget(false);
						}
					} else {
						// we have no left, and the source is on the left. Can only be an unset
						removeFromTarget(false);
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
					// Is it an unset?
					if (getMatch().getRight() != null) {
						final EObject rightValue = (EObject)getMatch().getRight().eGet(getReference(), false);
						if (rightValue == null) {
							// Value has been unset in the right, and we are merging towards right.
							// We need to re-add this element
							addInTarget(false);
						} else {
							// We'll actually need to "reset" this reference to its original value
							resetInTarget(false);
						}
					} else {
						// we have no right, and the source is on the right. Can only be an unset
						addInTarget(false);
					}
					break;
				default:
					break;
			}
		}

		setState(DifferenceState.MERGED);
		if (getEquivalence() != null) {
			for (Diff equivalent : getEquivalence().getDifferences()) {
				equivalent.setState(DifferenceState.MERGED);
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
					// Is it an unset?
					if (getMatch().getLeft() != null) {
						final EObject leftValue = (EObject)getMatch().getLeft().eGet(getReference(), false);
						if (leftValue == null) {
							// Value has been unset in the right, and we are merging towards right.
							// We need to re-add this element
							addInTarget(true);
						} else {
							// We'll actually need to "reset" this reference to its original value
							resetInTarget(true);
						}
					} else {
						// we have no left, and the source is on the left. Can only be an unset
						addInTarget(true);
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
					// Is it an unset?
					if (getMatch().getRight() != null) {
						final EObject rightValue = (EObject)getMatch().getRight().eGet(getReference(), false);
						if (rightValue == null) {
							removeFromTarget(true);
						} else {
							addInTarget(true);
						}
					} else {
						// we have no right, and the source is on the right. Can only be an unset
						removeFromTarget(true);
					}
					break;
				default:
					break;
			}
		}

		setState(DifferenceState.MERGED);
		if (getEquivalence() != null) {
			for (Diff equivalent : getEquivalence().getDifferences()) {
				equivalent.setState(DifferenceState.MERGED);
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
			// TODO: what to do when state = Discarded but is required?
			if (dependency.getState() != DifferenceState.MERGED) {
				if (rightToLeft) {
					dependency.copyRightToLeft();
				} else {
					dependency.copyLeftToRight();
				}
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
			// TODO: what to do when state = Discarded but is required?
			if (dependency.getState() != DifferenceState.MERGED) {
				if (rightToLeft) {
					dependency.copyRightToLeft();
				} else {
					dependency.copyLeftToRight();
				}
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
		final Comparison comparison = getMatch().getComparison();
		final Match valueMatch = comparison.getMatch(getValue());

		if (expectedContainer == null) {
			// FIXME throw exception? log? re-try to merge our requirements?
			// one of the "required" diffs should have created our container.
			return;
		}

		final EObject expectedValue;
		if (valueMatch == null) {
			// The value being moved is out of the scope
			// Whether it is a proxy or not, move the value itself.
			expectedValue = getValue();
		} else {
			if (rightToLeft) {
				expectedValue = valueMatch.getLeft();
			} else {
				expectedValue = valueMatch.getRight();
			}
		}
		// We now know the target container, target reference and target value.
		doMove(comparison, expectedContainer, expectedValue, rightToLeft);

		// TODO check that XMI IDs were preserved
	}

	/**
	 * This will do the actual work of moving the element into its reference. All sanity checks were made in
	 * {@link #moveElement(boolean)} and no more verification will be made here.
	 * 
	 * @param comparison
	 *            Comparison holding this Diff.
	 * @param expectedContainer
	 *            The container in which we are reorganizing a reference.
	 * @param expectedValue
	 *            The value that is to be moved within its reference.
	 * @param rightToLeft
	 *            Whether we should move the value in the left or right side.
	 */
	@SuppressWarnings("unchecked")
	protected void doMove(Comparison comparison, EObject expectedContainer, EObject expectedValue,
			boolean rightToLeft) {
		if (getReference().isMany()) {
			// Element to move cannot be part of the LCS... or there would not be a MOVE diff
			int insertionIndex = DiffUtil.findInsertionIndex(comparison, this, rightToLeft);

			/*
			 * However, it could still have been located "before" its new index, in which case we need to take
			 * it into account.
			 */
			final List<EObject> targetList = (List<EObject>)expectedContainer.eGet(getReference());
			if (insertionIndex > targetList.indexOf(expectedValue)) {
				insertionIndex--;
			}

			if (targetList instanceof EList<?>) {
				((EList<EObject>)targetList).move(insertionIndex, expectedValue);
			} else {
				targetList.remove(expectedValue);
				targetList.add(insertionIndex, expectedValue);
			}
		} else {
			expectedContainer.eSet(getReference(), expectedValue);
		}
	}

	/**
	 * This will be called when we need to create an element in the target side.
	 * <p>
	 * All necessary sanity checks have been made to ensure that the current operation is one that should
	 * create an object in its side or add an objet to a reference. In other words, either :
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
		final Comparison comparison = getMatch().getComparison();

		if (expectedContainer == null) {
			// FIXME throw exception? log? re-try to merge our requirements?
			// one of the "required" diffs should have created our container.
			return;
		}

		final EObject expectedValue;
		final Match valueMatch = comparison.getMatch(getValue());
		if (valueMatch == null) {
			// This is an out of scope value.
			if (getValue().eIsProxy()) {
				// Copy the proxy
				expectedValue = EcoreUtil.copy(getValue());
			} else {
				// Use the same value.
				expectedValue = getValue();
			}
		} else if (rightToLeft) {
			if (getReference().isContainment()) {
				expectedValue = createTarget(getValue());
				valueMatch.setLeft(expectedValue);
			} else {
				expectedValue = valueMatch.getLeft();
			}
		} else {
			if (getReference().isContainment()) {
				expectedValue = createTarget(getValue());
				valueMatch.setRight(expectedValue);
			} else {
				expectedValue = valueMatch.getRight();
			}
		}

		// We have the container, reference and value. We need to know the insertion index.
		if (getReference().isMany()) {
			final int insertionIndex = DiffUtil.findInsertionIndex(comparison, this, rightToLeft);

			final List<EObject> targetList = (List<EObject>)expectedContainer.eGet(getReference());

			if (targetList instanceof InternalEList<?>) {
				((InternalEList<EObject>)targetList).addUnique(insertionIndex, expectedValue);
			} else {
				targetList.add(insertionIndex, expectedValue);
			}
		} else {
			expectedContainer.eSet(getReference(), expectedValue);
		}

		if (getReference().isContainment()) {
			// Copy XMI ID when applicable.
			final Resource initialResource = getValue().eResource();
			final Resource targetResource = expectedValue.eResource();
			if (initialResource instanceof XMIResource && targetResource instanceof XMIResource) {
				((XMIResource)targetResource).setID(expectedValue, ((XMIResource)initialResource)
						.getID(getValue()));
			}
		}
	}

	/**
	 * This will create a copy of the given EObject that can be used as the target of an addition (or the
	 * reverting of a deletion).
	 * <p>
	 * The target will be self-contained and will have no reference towards any other EObject set (neither
	 * containment nor "classic" references). All of its attributes' values will match the given
	 * {@code referenceObject}'s.
	 * </p>
	 * 
	 * @param referenceObject
	 *            The EObject for which we'll create a copy.
	 * @return A self-contained copy of {@code referenceObject}.
	 * @see EMFCompareCopier#copy(EObject)
	 */
	protected EObject createTarget(EObject referenceObject) {
		/*
		 * We can't simply use EcoreUtil.copy. References will have their own diffs and will thus be merged
		 * later on.
		 */
		final EcoreUtil.Copier copier = new EMFCompareCopier();
		return copier.copy(referenceObject);
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
		final Comparison comparison = getMatch().getComparison();
		final Match valueMatch = comparison.getMatch(getValue());

		if (currentContainer == null) {
			// FIXME throw exception? log? re-try to merge our requirements?
			// one of the "required" diffs should have created our container.
			return;
		}

		final EObject expectedValue;
		if (valueMatch == null) {
			// value is out of the scope, use it as-is
			expectedValue = getValue();
		} else if (rightToLeft) {
			expectedValue = valueMatch.getLeft();
		} else {
			expectedValue = valueMatch.getRight();
		}

		// We have the container, reference and value to remove. Expected value can be null when the
		// deletion was made on both side (i.e. a pseudo delete)
		if (getReference().isContainment() && expectedValue != null) {
			EcoreUtil.remove(expectedValue);
			if (rightToLeft && valueMatch != null) {
				valueMatch.setLeft(null);
			} else if (valueMatch != null) {
				valueMatch.setRight(null);
			}
			// TODO remove dangling? remove empty Match?
		} else if (getReference().isMany()) {
			/*
			 * TODO if the same value appears twice, should we try and find the one that has actually been
			 * deleted? Can it happen? For now, remove the first occurence we find.
			 */
			final List<EObject> targetList = (List<EObject>)currentContainer.eGet(getReference());
			targetList.remove(expectedValue);
		} else {
			currentContainer.eUnset(getReference());
		}
	}

	/**
	 * This will be called by the merge operations in order to reset a reference to its original value, be
	 * that the left or right side.
	 * <p>
	 * Should never be called on multi-valued references.
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

		if (originContainer == null || !targetContainer.eIsSet(getReference())
				|| !originContainer.eIsSet(getReference())) {
			targetContainer.eUnset(getReference());
		} else {
			final EObject originalValue = (EObject)originContainer.eGet(getReference());
			final Match valueMatch = getMatch().getComparison().getMatch(originalValue);
			final EObject expectedValue;
			if (valueMatch == null) {
				// Value is out of the scope, use it as-is
				expectedValue = originalValue;
			} else if (rightToLeft) {
				expectedValue = valueMatch.getLeft();
			} else {
				expectedValue = valueMatch.getRight();
			}
			targetContainer.eSet(getReference(), expectedValue);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#toString()
	 */
	@Override
	public String toString() {
		// @formatter:off
		// Formatting these would make them unreadable
		return Objects.toStringHelper(this)
					.add("reference", getReference().getEContainingClass().getName() + "." + getReference().getName()) //$NON-NLS-1$ //$NON-NLS-2$
					.add("value", EObjectUtil.getLabel(getValue())) //$NON-NLS-1$
					.add("parentMatch", getMatch().toString()) //$NON-NLS-1$
					.add("match of value", getMatch().getComparison().getMatch(getValue())) //$NON-NLS-1$
					.add("kind", getKind()) //$NON-NLS-1$
					.add("source", getSource()) //$NON-NLS-1$
					.add("state", getState()).toString(); //$NON-NLS-1$
		// @formatter:on
	}
}
