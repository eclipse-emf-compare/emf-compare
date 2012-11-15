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

import static org.eclipse.emf.compare.utils.ReferenceUtil.safeEIsSet;

import com.google.common.base.Objects;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.impl.ReferenceChangeImpl;
import org.eclipse.emf.compare.utils.DiffUtil;
import org.eclipse.emf.compare.utils.EMFCompareCopier;
import org.eclipse.emf.compare.utils.IEqualityHelper;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * This specialization of the {@link ReferenceChangeImpl} class allows us to define the derived features and
 * operations implementations.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ReferenceChangeSpec extends ReferenceChangeImpl {
	/**
	 * Set this to <code>true</code> to activate checking the merge state through
	 * {@link #checkMergeState(boolean)}.
	 */
	private static final boolean DEBUG_MERGE = false;

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
			boolean continueMerge = handleEquivalences(false);
			if (!continueMerge) {
				return;
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

		if (DEBUG_MERGE) {
			checkMergeState(false);
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
			boolean continueMerge = handleEquivalences(true);
			if (!continueMerge) {
				return;
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

		if (DEBUG_MERGE) {
			checkMergeState(true);
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
			// TODO: what to do when state = Discarded but is required?
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
		final Comparison comparison = getMatch().getComparison();
		final Match valueMatch = comparison.getMatch(getValue());

		final EObject expectedContainer;
		if (getReference().isContainment()) {
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
			expectedContainer = getMatch().getLeft();
		} else {
			expectedContainer = getMatch().getRight();
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
			if (getReference().isMany()) {
				@SuppressWarnings("unchecked")
				final List<EObject> targetList = (List<EObject>)expectedContainer.eGet(getReference());
				expectedValue = findMatchIn(targetList, getValue());
			} else {
				expectedValue = (EObject)expectedContainer.eGet(getReference());
			}
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
			final int currentIndex = targetList.indexOf(expectedValue);
			if (insertionIndex > currentIndex && currentIndex >= 0) {
				insertionIndex--;
			}

			if (currentIndex == -1) {
				// happens for container changes for example.
				if (!getReference().isContainment()) {
					targetList.remove(expectedValue);
				}
				if (insertionIndex >= 0) {
					targetList.add(insertionIndex, expectedValue);
				} else {
					targetList.add(expectedValue);
				}
			} else if (targetList instanceof EList<?>) {
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
			// value is out of the scope... we need to look it up
			if (getReference().isMany()) {
				final List<EObject> targetList = (List<EObject>)currentContainer.eGet(getReference());
				expectedValue = findMatchIn(targetList, getValue());
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

		if (originContainer == null || !safeEIsSet(targetContainer, getReference())
				|| !safeEIsSet(originContainer, getReference())) {
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
	 * @param rightToLeft
	 *            Direction of the merge.
	 * @return <code>true</code> if the current difference should still be merged after handling its
	 *         equivalences, <code>false</code> if it should be considered "already merged".
	 */
	protected boolean handleEquivalences(boolean rightToLeft) {
		boolean continueMerge = true;
		for (Diff equivalent : getEquivalence().getDifferences()) {
			if (equivalent instanceof ReferenceChange
					&& getReference().getEOpposite() == ((ReferenceChange)equivalent).getReference()) {
				// This equivalence is on our eOpposite. Should we merge it instead of 'this'?
				final boolean mergeEquivalence = !getReference().isMany()
						&& ((ReferenceChange)equivalent).getReference().isMany();
				if (mergeEquivalence && rightToLeft) {
					equivalent.copyRightToLeft();
					continueMerge = false;
				} else if (mergeEquivalence) {
					equivalent.copyLeftToRight();
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
	 * @param list
	 *            The list from which we seek a value.
	 * @param element
	 *            The value for which we need a match in {@code list}.
	 * @return The match of {@code element} in {@code list}, {@code null} if none.
	 */
	private EObject findMatchIn(List<EObject> list, EObject element) {
		final Comparison comparison = getMatch().getComparison();
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

	/**
	 * This should only be activated when debugging. Not only may this long list of assertions not cover every
	 * possible case, but it would impair performance. Even when debugging, take these with a grain of salt :
	 * we've covered the known cases, but the assertions here could still fail on some cases where the merge
	 * worked.
	 * <p>
	 * Set {@link #DEBUG_MERGE} to <code>true</code> to activate checking the merge state through here.
	 * </p>
	 * 
	 * @param rightToLeft
	 *            Direction of the merge.
	 */
	// This is a long list of assertions ... disable formatting and style checking.
	// @formatter:off
	// CHECKSTYLE:OFF
	@SuppressWarnings("nls")
	private void checkMergeState(boolean rightToLeft) {
		final Match containerMatch = getMatch();
		final Comparison comparison = containerMatch.getComparison();
		final Match valueMatch = comparison.getMatch(getValue());

		Object leftValue = null;
		if (containerMatch.getLeft() != null) {
			leftValue = containerMatch.getLeft().eGet(getReference());
		}
		Object rightValue = null;
		if (containerMatch.getRight() != null) {
			rightValue = containerMatch.getRight().eGet(getReference());
		}

		final boolean mergeTowardsSource = rightToLeft && getSource() == DifferenceSource.LEFT
				|| !rightToLeft && getSource() == DifferenceSource.RIGHT;
		final boolean wasDeleteMerge = getKind() == DifferenceKind.ADD && mergeTowardsSource
				|| getKind() == DifferenceKind.DELETE && !mergeTowardsSource;

		assert getState() == DifferenceState.MERGED : "Difference has not been set as MERGED.";
		if (mergeTowardsSource) {
			for (Diff diff : getRequiredBy()) {
				assert diff.getState() == DifferenceState.MERGED : "Requirement has not been merged.";
			}
		} else {
			for (Diff diff : getRequires()) {
				assert diff.getState() == DifferenceState.MERGED : "Requirement has not been merged.";
			}
		}
		if (getEquivalence() != null) {
			for (Diff diff : getEquivalence().getDifferences()) {
				assert diff.getState() == DifferenceState.MERGED : "Equivalence has not been set as MERGED.";
			}
		}
		
		assert getReference() != null : "Cannot have an empty reference";
		
		if (getReference().isContainment()) {
			if (valueMatch == null) {
				// Value was out of the scope. This could only be an "ADD" or "DELETE" diff. Whatever the direction, both sides now point towards the very same instance
				if (getReference().isMany()) {
					if (wasDeleteMerge) {
						if (leftValue != null) {
							assert leftValue instanceof List<?>;
							assert !((List<?>)leftValue).contains(getValue()) : "None of the sides should contain the value";
						}
						if (rightValue != null) {
							assert rightValue instanceof List<?>;
							assert !((List<?>)rightValue).contains(getValue()) : "None of the sides should contain the value";
						}
					} else {
						if (leftValue != null) {
							assert leftValue instanceof List<?>;
							assert ((List<?>)leftValue).contains(getValue()) : "Both sides shoulds point towards the same instance";
						}
						if (rightValue != null) {
							assert rightValue instanceof List<?>;
							assert ((List<?>)rightValue).contains(getValue()) : "Both sides shoulds point towards the same instance";
						}
					}
				} else {
					// EObject or null, but same instance anyway
					assert leftValue == rightValue : "Both sides should point towards the same instance";
				}
			} else {
				if (getKind() == DifferenceKind.MOVE && valueMatch.getLeft() != null && valueMatch.getRight() != null) {
					// We can only trust the value match here.
					final EObject leftContainer = valueMatch.getLeft().eContainer();
					final EObject rightContainer = valueMatch.getRight().eContainer();
					// The "values" we computed beforehand are thus wrong.
					leftValue = leftContainer.eGet(getReference());
					rightValue = rightContainer.eGet(getReference());
				}
				
				if (getReference().isMany()) {
					if (wasDeleteMerge) {
						if (leftValue != null) {
							assert leftValue instanceof List<?>;
							assert !((List<?>)leftValue).contains(valueMatch.getLeft()) : "None of the sides should contain the matched value";
						}
						if (rightValue != null) {
							assert rightValue instanceof List<?>;
							assert !((List<?>)rightValue).contains(valueMatch.getRight()) : "None of the sides should contain the matched value";
						}
					} else  {
						if (leftValue != null) {
							assert leftValue instanceof List<?>;
							assert ((List<?>)leftValue).contains(valueMatch.getLeft()) : "Both sides shoulds point towards the matched value";
						}
						if (rightValue != null) {
							assert rightValue instanceof List<?>;
							assert ((List<?>)rightValue).contains(valueMatch.getRight()) : "Both sides shoulds point towards the matchedValue";
						}
					}
				} else if (wasDeleteMerge) {
					assert leftValue == null && rightValue == null : "Both values should be 'null'.";
				} else {
					assert leftValue == valueMatch.getLeft() && rightValue == valueMatch.getRight() : "The value should be the matched one on both sides.";
				}
			}
		} else {
			if (leftValue instanceof EObject) {
				final Match leftValueMatch = comparison.getMatch((EObject)leftValue);
				if (leftValueMatch == null) {
					assert leftValue == rightValue : "Merging an out-of-scope element should leave both sides pointing towards the instance.";
				} else {
					assert leftValueMatch.getRight() == rightValue : "Merging should leave both sides pointing towards the same matched value";
				}
			} else {
				if (valueMatch == null) {
					if (wasDeleteMerge) {
						if (leftValue instanceof List<?>) {
							assert !((List<?>)leftValue).contains(getValue()) : "Value should have been deleted from both sides.";
						}
						if (rightValue instanceof List<?>) {
							assert !((List<?>)rightValue).contains(getValue()) : "Value should have been deleted from both sides.";
						}
					} else {
						if (leftValue instanceof List<?>) {
							assert ((List<?>)leftValue).contains(getValue()) : "Merging an out-of-scope element should leave both sides pointing towards the instance.";
						}
						if (rightValue instanceof List<?>) {
							assert ((List<?>)rightValue).contains(getValue()) : "Merging an out-of-scope element should leave both sides pointing towards the instance.";
						}
					}
				} else {
					if (wasDeleteMerge) {
						if (leftValue instanceof List<?>) {
							assert !((List<?>)leftValue).contains(valueMatch.getLeft()) : "None of the sides should contain the matched value";
						}
						if (rightValue instanceof List<?>) {
							assert !((List<?>)rightValue).contains(valueMatch.getRight()) : "None of the sides should contain the matched value";
						}
					} else  {
						if (leftValue instanceof List<?>) {
							assert ((List<?>)leftValue).contains(valueMatch.getLeft()) : "Both sides shoulds point towards the matched value";
						}
						if (rightValue instanceof List<?>) {
							assert ((List<?>)rightValue).contains(valueMatch.getRight()) : "Both sides shoulds point towards the matchedValue";
						}
					}
				}
			}
		}
	}
	// @formatter:on
	// CHECKSTYLE:ON
}
