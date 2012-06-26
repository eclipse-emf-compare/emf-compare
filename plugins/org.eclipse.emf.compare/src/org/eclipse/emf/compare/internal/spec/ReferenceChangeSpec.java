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

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.impl.ReferenceChangeImpl;
import org.eclipse.emf.compare.utils.DiffUtil;
import org.eclipse.emf.compare.utils.EMFCompareCopier;
import org.eclipse.emf.compare.utils.EqualityHelper;
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
					final Match valueMatch = getMatch().getComparison().getMatch(getValue());
					if (valueMatch != null && getValue() != valueMatch.getLeft()) {
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
					// Is it an unset?
					final Match valueMatch = getMatch().getComparison().getMatch(getValue());
					if (valueMatch != null && getValue() != valueMatch.getRight()) {
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
					final Match valueMatch = getMatch().getComparison().getMatch(getValue());
					if (valueMatch != null && getValue() != valueMatch.getLeft()) {
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
					// Is it an unset?
					final Match valueMatch = getMatch().getComparison().getMatch(getValue());
					if (valueMatch != null && getValue() != valueMatch.getRight()) {
						removeFromTarget(true);
					} else {
						addInTarget(true);
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

		if (expectedContainer == null || valueMatch == null) {
			// TODO throws exception?
		} else if (rightToLeft && valueMatch.getLeft() == null || !rightToLeft
				&& valueMatch.getRight() == null) {
			// TODO should not happen : one of our requires should have created the value if needed
		} else {
			final EObject expectedValue;
			if (rightToLeft) {
				expectedValue = valueMatch.getLeft();
			} else {
				expectedValue = valueMatch.getRight();
			}

			// We now know the target container, target reference and target value.
			doMove(comparison, expectedContainer, expectedValue, rightToLeft);

			// TODO check that XMI IDs were preserved
		}
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
			// Determine the index to move the element to.
			final boolean undoingLeft = rightToLeft && getSource() == DifferenceSource.LEFT;
			final boolean undoingRight = !rightToLeft && getSource() == DifferenceSource.RIGHT;

			final List<EObject> sourceList;
			if ((undoingLeft || undoingRight) && getMatch().getOrigin() != null) {
				sourceList = (List<EObject>)getMatch().getOrigin().eGet(getReference());
			} else if (rightToLeft) {
				sourceList = (List<EObject>)getMatch().getRight().eGet(getReference());
			} else {
				sourceList = (List<EObject>)getMatch().getLeft().eGet(getReference());
			}

			final List<EObject> targetList = (List<EObject>)expectedContainer.eGet(getReference());

			Iterable<EObject> ignoredElements;
			if (undoingLeft || undoingRight) {
				// Undoing a change
				ignoredElements = null;
			} else {
				if (comparison.isThreeWay() && getMatch().getOrigin() != null) {
					ignoredElements = computeIgnoredElements(targetList);
					ignoredElements = Iterables.concat(ignoredElements, Collections.singleton(expectedValue));
				} else {
					ignoredElements = Collections.singleton(expectedValue);
				}
			}

			// Element to move cannot be part of the LCS... or there would not be a MOVE diff
			final EqualityHelper helper = new EqualityHelper();
			int insertionIndex = DiffUtil.findInsertionIndex(comparison, helper, ignoredElements, sourceList,
					targetList, expectedValue);
			/*
			 * However, it could still have been located "before" its new index, in which case we need to take
			 * it into account.
			 */
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
		final Match valueMatch = comparison.getMatch(getValue());

		if (expectedContainer == null || valueMatch == null) {
			// FIXME throw exception? log? re-try to merge our requirements?
			// one of the "required" diffs should have created our container.
		} else {
			final EObject expectedValue;
			if (rightToLeft) {
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
				// TODO extract this for reuse (the UI will need to compute insertion indices too)
				final List<EObject> sourceList;
				if (getValue() == valueMatch.getOrigin()) {
					sourceList = (List<EObject>)getMatch().getOrigin().eGet(getReference());
				} else if (rightToLeft) {
					sourceList = (List<EObject>)getMatch().getRight().eGet(getReference());
				} else {
					sourceList = (List<EObject>)getMatch().getLeft().eGet(getReference());
				}
				final List<EObject> targetList = (List<EObject>)expectedContainer.eGet(getReference());

				final Iterable<EObject> ignoredElements;
				if (comparison.isThreeWay() && getValue() != valueMatch.getOrigin()) {
					ignoredElements = computeIgnoredElements(targetList);
				} else {
					ignoredElements = null;
				}

				final int insertionIndex = DiffUtil.findInsertionIndex(comparison, new EqualityHelper(),
						ignoredElements, sourceList, targetList, expectedValue);

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

		if (currentContainer == null || valueMatch == null) {
			// FIXME throw exception? log? re-try to merge our requirements?
		} else {
			final EObject expectedValue;
			if (rightToLeft) {
				expectedValue = valueMatch.getLeft();
			} else {
				expectedValue = valueMatch.getRight();
			}
			// We have the container, reference and value to remove.
			if (getReference().isContainment()) {
				EcoreUtil.remove(expectedValue);
				if (rightToLeft) {
					valueMatch.setLeft(null);
				} else {
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
			final Match valueMatch = getMatch().getComparison().getMatch(
					(EObject)originContainer.eGet(getReference()));
			final EObject expectedValue;
			if (valueMatch == null) {
				expectedValue = null;
			} else if (rightToLeft) {
				expectedValue = valueMatch.getLeft();
			} else {
				expectedValue = valueMatch.getRight();
			}
			targetContainer.eSet(getReference(), expectedValue);
		}
	}

	/**
	 * When computing the insertion index of an element in a list, we need to ignore all elements present in
	 * that list that feature unresolved Diffs on the same reference.
	 * 
	 * @param candidates
	 *            The sequence in which we need to compute an insertion index.
	 * @return The list of elements that should be ignored when computing the insertion index for a new
	 *         element in {@code candidates}.
	 */
	protected Iterable<EObject> computeIgnoredElements(Iterable<EObject> candidates) {
		return Iterables.filter(candidates, new Predicate<EObject>() {
			public boolean apply(final EObject element) {
				final Match match = getMatch();
				final Iterable<ReferenceChange> filteredCandidates = Iterables.filter(match.getDifferences(),
						ReferenceChange.class);

				return Iterables.any(filteredCandidates, new Predicate<ReferenceChange>() {
					public boolean apply(ReferenceChange input) {
						return input.getState() == DifferenceState.UNRESOLVED
								&& input.getReference() == getReference() && input.getValue() == element;
					}
				});
			}
		});
	}
}
