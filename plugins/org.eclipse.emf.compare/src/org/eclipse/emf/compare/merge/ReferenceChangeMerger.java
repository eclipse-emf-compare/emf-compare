/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import static org.eclipse.emf.compare.utils.ReferenceUtil.safeEIsSet;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.utils.DiffUtil;
import org.eclipse.emf.compare.utils.IEqualityHelper;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * This specific implementation of {@link AbstractMerger} will be used to merge reference changes.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ReferenceChangeMerger extends AbstractMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#isMergerFor(org.eclipse.emf.compare.Diff)
	 */
	public boolean isMergerFor(Diff target) {
		return target instanceof ReferenceChange;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#copyLeftToRight(org.eclipse.emf.compare.Diff,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void copyLeftToRight(Diff target, Monitor monitor) {
		// Don't merge an already merged (or discarded) diff
		if (target.getState() != DifferenceState.UNRESOLVED) {
			return;
		}
		final ReferenceChange diff = (ReferenceChange)target;

		// Change the diff's state before we actually merge it : this allows us to avoid requirement cycles.
		diff.setState(DifferenceState.MERGED);
		if (diff.getEquivalence() != null) {
			boolean continueMerge = handleEquivalences(diff, false, monitor);
			if (!continueMerge) {
				return;
			}
		}

		if (diff.getSource() == DifferenceSource.LEFT) {
			// merge all "requires" diffs
			mergeRequires(diff, false, monitor);

			switch (diff.getKind()) {
				case ADD:
					// Create the same element in right
					addInTarget(diff, false);
					break;
				case DELETE:
					// Delete that same element from right
					removeFromTarget(diff, false);
					break;
				case MOVE:
					moveElement(diff, false);
					break;
				case CHANGE:
					// Is it an unset?
					if (diff.getMatch().getLeft() != null) {
						final EObject leftValue = (EObject)diff.getMatch().getLeft().eGet(
								diff.getReference(), false);
						if (leftValue == null) {
							removeFromTarget(diff, false);
						} else {
							addInTarget(diff, false);
						}
					} else {
						// we have no left, and the source is on the left. Can only be an unset
						removeFromTarget(diff, false);
					}
					break;
				default:
					break;
			}
		} else {
			// merge all "required by" diffs
			mergeRequiredBy(diff, false, monitor);

			switch (diff.getKind()) {
				case ADD:
					// We have a ADD on right. we need to revert this addition
					removeFromTarget(diff, false);
					break;
				case DELETE:
					// DELETE in the right. We need to re-create this element
					addInTarget(diff, false);
					break;
				case MOVE:
					moveElement(diff, false);
					break;
				case CHANGE:
					// Is it an unset?
					if (diff.getMatch().getRight() != null) {
						final EObject rightValue = (EObject)diff.getMatch().getRight().eGet(
								diff.getReference(), false);
						if (rightValue == null) {
							// Value has been unset in the right, and we are merging towards right.
							// We need to re-add this element
							addInTarget(diff, false);
						} else {
							// We'll actually need to "reset" this reference to its original value
							resetInTarget(diff, false);
						}
					} else {
						// we have no right, and the source is on the right. Can only be an unset
						addInTarget(diff, false);
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
	 * @see org.eclipse.emf.compare.merge.IMerger#copyRightToLeft(org.eclipse.emf.compare.Diff,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void copyRightToLeft(Diff target, Monitor monitor) {
		// Don't merge an already merged (or discarded) diff
		if (target.getState() != DifferenceState.UNRESOLVED) {
			return;
		}
		final ReferenceChange diff = (ReferenceChange)target;

		// Change the diff's state before we actually merge it : this allows us to avoid requirement cycles.
		diff.setState(DifferenceState.MERGED);
		if (diff.getEquivalence() != null) {
			boolean continueMerge = handleEquivalences(diff, true, monitor);
			if (!continueMerge) {
				return;
			}
		}

		if (diff.getSource() == DifferenceSource.LEFT) {
			// merge all "required by" diffs
			mergeRequiredBy(diff, true, monitor);

			switch (diff.getKind()) {
				case ADD:
					// We have a ADD on left, thus nothing in right. We need to revert the addition
					removeFromTarget(diff, true);
					break;
				case DELETE:
					// DELETE in the left, thus an element in right. We need to re-create that element
					addInTarget(diff, true);
					break;
				case MOVE:
					moveElement(diff, true);
					break;
				case CHANGE:
					// Is it an unset?
					if (diff.getMatch().getLeft() != null) {
						final EObject leftValue = (EObject)diff.getMatch().getLeft().eGet(
								diff.getReference(), false);
						if (leftValue == null) {
							// Value has been unset in the right, and we are merging towards right.
							// We need to re-add this element
							addInTarget(diff, true);
						} else {
							// We'll actually need to "reset" this reference to its original value
							resetInTarget(diff, true);
						}
					} else {
						// we have no left, and the source is on the left. Can only be an unset
						addInTarget(diff, true);
					}
					break;
				default:
					break;
			}
		} else {
			// merge all "requires" diffs
			mergeRequires(diff, true, monitor);

			switch (diff.getKind()) {
				case ADD:
					addInTarget(diff, true);
					break;
				case DELETE:
					removeFromTarget(diff, true);
					break;
				case MOVE:
					moveElement(diff, true);
					break;
				case CHANGE:
					// Is it an unset?
					if (diff.getMatch().getRight() != null) {
						final EObject rightValue = (EObject)diff.getMatch().getRight().eGet(
								diff.getReference(), false);
						if (rightValue == null) {
							removeFromTarget(diff, true);
						} else {
							addInTarget(diff, true);
						}
					} else {
						// we have no right, and the source is on the right. Can only be an unset
						removeFromTarget(diff, true);
					}
					break;
				default:
					break;
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
	protected void moveElement(ReferenceChange diff, boolean rightToLeft) {
		final Comparison comparison = diff.getMatch().getComparison();
		final Match valueMatch = comparison.getMatch(diff.getValue());
		final EReference reference = diff.getReference();

		final EObject expectedContainer;
		if (reference.isContainment()) {
			/*
			 * We cannot "trust" the holding match (getMatch) in this case. However, "valueMatch" cannot be
			 * null : we cannot have detected a move if the moved element is not matched on both sides. Use
			 * that information to retrieve the proper "target" container.
			 */
			final Match targetContainerMatch;
			// If it exists, use the source side's container as reference
			if (rightToLeft && valueMatch.getRight() != null) {
				targetContainerMatch = comparison.getMatch(valueMatch.getRight().eContainer());
			} else if (!rightToLeft && valueMatch.getLeft() != null) {
				targetContainerMatch = comparison.getMatch(valueMatch.getLeft().eContainer());
			} else {
				// Otherwise, the value we're moving on one side has been removed from its source side.
				targetContainerMatch = comparison.getMatch(valueMatch.getOrigin().eContainer());
			}
			if (rightToLeft) {
				expectedContainer = targetContainerMatch.getLeft();
			} else {
				expectedContainer = targetContainerMatch.getRight();
			}
		} else if (rightToLeft) {
			expectedContainer = diff.getMatch().getLeft();
		} else {
			expectedContainer = diff.getMatch().getRight();
		}
		if (expectedContainer == null) {
			// FIXME throw exception? log? re-try to merge our requirements?
			// one of the "required" diffs should have created our container.
			return;
		}

		final EObject expectedValue;
		if (valueMatch == null) {
			// The value being moved is out of the scope
			/*
			 * Note : there should not be a way to end up with a "move" for an out of scope value : a move can
			 * only be detected if the object is matched on both sides, otherwise all we can see is "add" and
			 * "delete"... Is this "fallback" code even reachable? If so, how?
			 */
			// We need to look it up
			if (reference.isMany()) {
				@SuppressWarnings("unchecked")
				final List<EObject> targetList = (List<EObject>)expectedContainer.eGet(reference);
				expectedValue = findMatchIn(comparison, targetList, diff.getValue());
			} else {
				expectedValue = (EObject)expectedContainer.eGet(reference);
			}
		} else {
			if (rightToLeft) {
				expectedValue = valueMatch.getLeft();
			} else {
				expectedValue = valueMatch.getRight();
			}
		}
		// We now know the target container, target reference and target value.
		doMove(diff, comparison, expectedContainer, expectedValue, rightToLeft);
	}

	/**
	 * This will do the actual work of moving the element into its reference. All sanity checks were made in
	 * {@link #moveElement(boolean)} and no more verification will be made here.
	 * 
	 * @param diff
	 *            The diff we are currently merging.
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
	protected void doMove(ReferenceChange diff, Comparison comparison, EObject expectedContainer,
			EObject expectedValue, boolean rightToLeft) {
		final EReference reference = diff.getReference();
		if (reference.isMany()) {
			// Element to move cannot be part of the LCS... or there would not be a MOVE diff
			int insertionIndex = findInsertionIndex(comparison, diff, rightToLeft);

			/*
			 * However, it could still have been located "before" its new index, in which case we need to take
			 * it into account.
			 */
			final List<EObject> targetList = (List<EObject>)expectedContainer.eGet(reference);
			final int currentIndex = targetList.indexOf(expectedValue);
			if (insertionIndex > currentIndex && currentIndex >= 0) {
				insertionIndex--;
			}

			if (currentIndex == -1) {
				// happens for container changes for example.
				if (!reference.isContainment()) {
					targetList.remove(expectedValue);
				}
				if (insertionIndex < 0 && insertionIndex > targetList.size()) {
					targetList.add(expectedValue);
				} else {
					targetList.add(insertionIndex, expectedValue);
				}
			} else if (targetList instanceof EList<?>) {
				if (insertionIndex < 0 && insertionIndex > targetList.size()) {
					((EList<EObject>)targetList).move(targetList.size() - 1, expectedValue);
				} else {
					((EList<EObject>)targetList).move(insertionIndex, expectedValue);
				}
			} else {
				targetList.remove(expectedValue);
				if (insertionIndex < 0 && insertionIndex > targetList.size()) {
					targetList.add(expectedValue);
				} else {
					targetList.add(insertionIndex, expectedValue);
				}
			}
		} else {
			expectedContainer.eSet(reference, expectedValue);
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
	 * @param diff
	 *            The diff we are currently merging.
	 * @param rightToLeft
	 *            Tells us whether we are to add an object on the left or right side.
	 */
	@SuppressWarnings("unchecked")
	protected void addInTarget(ReferenceChange diff, boolean rightToLeft) {
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
			return;
		}

		final Comparison comparison = match.getComparison();
		final EReference reference = diff.getReference();
		final EObject expectedValue;
		final Match valueMatch = comparison.getMatch(diff.getValue());
		if (valueMatch == null) {
			// This is an out of scope value.
			if (diff.getValue().eIsProxy()) {
				// Copy the proxy
				expectedValue = EcoreUtil.copy(diff.getValue());
			} else {
				// Use the same value.
				expectedValue = diff.getValue();
			}
		} else if (rightToLeft) {
			if (reference.isContainment()) {
				expectedValue = createCopy(diff.getValue());
				valueMatch.setLeft(expectedValue);
			} else {
				expectedValue = valueMatch.getLeft();
			}
		} else {
			if (reference.isContainment()) {
				expectedValue = createCopy(diff.getValue());
				valueMatch.setRight(expectedValue);
			} else {
				expectedValue = valueMatch.getRight();
			}
		}

		// We have the container, reference and value. We need to know the insertion index.
		if (reference.isMany()) {
			final int insertionIndex = findInsertionIndex(comparison, diff, rightToLeft);

			final List<EObject> targetList = (List<EObject>)expectedContainer.eGet(reference);
			addAt(targetList, expectedValue, insertionIndex);
		} else {
			expectedContainer.eSet(reference, expectedValue);
		}

		if (reference.isContainment()) {
			// Copy XMI ID when applicable.
			final Resource initialResource = diff.getValue().eResource();
			final Resource targetResource = expectedValue.eResource();
			if (initialResource instanceof XMIResource && targetResource instanceof XMIResource) {
				((XMIResource)targetResource).setID(expectedValue, ((XMIResource)initialResource).getID(diff
						.getValue()));
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
	protected void removeFromTarget(ReferenceChange diff, boolean rightToLeft) {
		final Match match = diff.getMatch();
		final EReference reference = diff.getReference();
		final EObject currentContainer;
		if (rightToLeft) {
			currentContainer = match.getLeft();
		} else {
			currentContainer = match.getRight();
		}
		final Comparison comparison = match.getComparison();
		final Match valueMatch = comparison.getMatch(diff.getValue());

		if (currentContainer == null) {
			// FIXME throw exception? log? re-try to merge our requirements?
			// one of the "required" diffs should have created our container.
			return;
		}

		final EObject expectedValue;
		if (valueMatch == null) {
			// value is out of the scope... we need to look it up
			if (reference.isMany()) {
				final List<EObject> targetList = (List<EObject>)currentContainer.eGet(reference);
				expectedValue = findMatchIn(comparison, targetList, diff.getValue());
			} else {
				// the value will not be needed anyway
				expectedValue = null;
			}
		} else if (rightToLeft) {
			expectedValue = valueMatch.getLeft();
		} else {
			expectedValue = valueMatch.getRight();
		}

		// We have the container, reference and value to remove. Expected value can be null when the
		// deletion was made on both side (i.e. a pseudo delete)
		if (reference.isContainment() && expectedValue != null) {
			EcoreUtil.remove(expectedValue);
			if (rightToLeft && valueMatch != null) {
				valueMatch.setLeft(null);
			} else if (valueMatch != null) {
				valueMatch.setRight(null);
			}
			// TODO remove dangling? remove empty Match?
		} else if (reference.isMany()) {
			/*
			 * TODO if the same value appears twice, should we try and find the one that has actually been
			 * deleted? Can it happen? For now, remove the first occurence we find.
			 */
			final List<EObject> targetList = (List<EObject>)currentContainer.eGet(reference);
			targetList.remove(expectedValue);
		} else {
			currentContainer.eUnset(reference);
		}
	}

	/**
	 * This will be called by the merge operations in order to reset a reference to its original value, be
	 * that the left or right side.
	 * <p>
	 * Should never be called on multi-valued references.
	 * </p>
	 * 
	 * @param diff
	 *            The diff we are currently merging.
	 * @param rightToLeft
	 *            Tells us the direction of this merge operation.
	 */
	protected void resetInTarget(ReferenceChange diff, boolean rightToLeft) {
		final Match match = diff.getMatch();
		final EReference reference = diff.getReference();
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

		if (originContainer == null || !safeEIsSet(targetContainer, reference)
				|| !safeEIsSet(originContainer, reference)) {
			targetContainer.eUnset(reference);
		} else {
			final EObject originalValue = (EObject)originContainer.eGet(reference);
			final Match valueMatch = match.getComparison().getMatch(originalValue);
			final EObject expectedValue;
			if (valueMatch == null) {
				// Value is out of the scope, use it as-is
				expectedValue = originalValue;
			} else if (rightToLeft) {
				expectedValue = valueMatch.getLeft();
			} else {
				expectedValue = valueMatch.getRight();
			}
			targetContainer.eSet(reference, expectedValue);
		}
	}

	/**
	 * Handles the equivalences of this difference.
	 * <p>
	 * Note that in certain cases, we'll merge our opposite instead of merging this diff. Specifically, we'll
	 * do that for one-to-many eOpposites : we'll merge the 'many' side instead of the 'unique' one. This
	 * allows us not to worry about the order of the references on that 'many' side.
	 * </p>
	 * <p>
	 * This is called before the merge of <code>this</code>. In short, if this returns <code>false</code>, we
	 * won't carry on merging <code>this</code> after returning.
	 * </p>
	 * 
	 * @param diff
	 *            The diff we are currently merging.
	 * @param rightToLeft
	 *            Direction of the merge.
	 * @param monitor
	 *            The monitor to use in order to report progress information.
	 * @return <code>true</code> if the current difference should still be merged after handling its
	 *         equivalences, <code>false</code> if it should be considered "already merged".
	 */
	protected boolean handleEquivalences(ReferenceChange diff, boolean rightToLeft, Monitor monitor) {
		final EReference reference = diff.getReference();
		boolean continueMerge = true;
		for (Diff equivalent : diff.getEquivalence().getDifferences()) {
			if (equivalent instanceof ReferenceChange
					&& reference.getEOpposite() == ((ReferenceChange)equivalent).getReference()
					&& equivalent.getState() == DifferenceState.UNRESOLVED) {
				// This equivalence is on our eOpposite. Should we merge it instead of 'this'?
				final boolean mergeEquivalence = !reference.isMany()
						&& ((ReferenceChange)equivalent).getReference().isMany();
				if (mergeEquivalence) {
					mergeDiff(equivalent, rightToLeft, monitor);
					continueMerge = false;
				}
			} else if (diff.getSource() == DifferenceSource.LEFT) {
				// This can happen when merging subset/supersets... see AddInterfaceTest#testA50UseCase
				/*
				 * This should be removed (or we should make sure that we can never be here) when bug 398402
				 * is fixed.
				 */
				if (rightToLeft && diff.getRequiredBy().contains(equivalent)) {
					mergeDiff(equivalent, rightToLeft, monitor);
					continueMerge = false;
				} else if (!rightToLeft && diff.getRequires().contains(equivalent)) {
					mergeDiff(equivalent, rightToLeft, monitor);
					continueMerge = false;
				}
			} else if (diff.getSource() == DifferenceSource.RIGHT) {
				// This can happen when merging subset/supersets... see AddInterfaceTest#testA50UseCase
				/*
				 * This should be removed (or we should make sure that we can never be here) when bug 398402
				 * is fixed.
				 */
				if (rightToLeft && diff.getRequires().contains(equivalent)) {
					mergeDiff(equivalent, rightToLeft, monitor);
					continueMerge = false;
				} else if (!rightToLeft && diff.getRequiredBy().contains(equivalent)) {
					mergeDiff(equivalent, rightToLeft, monitor);
					continueMerge = false;
				}
			}
			equivalent.setState(DifferenceState.MERGED);
		}
		return continueMerge;
	}

	/**
	 * Seeks a match of the given {@code element} in the given list, using the equality helper to find it.
	 * This is only used when moving or deleting proxies for now.
	 * 
	 * @param comparison
	 *            The comparison which Diff we are currently merging.
	 * @param list
	 *            The list from which we seek a value.
	 * @param element
	 *            The value for which we need a match in {@code list}.
	 * @return The match of {@code element} in {@code list}, {@code null} if none.
	 */
	protected EObject findMatchIn(Comparison comparison, List<EObject> list, EObject element) {
		final IEqualityHelper helper = comparison.getEqualityHelper();
		final Iterator<EObject> it = list.iterator();
		while (it.hasNext()) {
			final EObject next = it.next();
			if (helper.matchingValues(next, element)) {
				return next;
			}
		}
		return null;
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
