/*******************************************************************************
 * Copyright (c) 2014 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Alexandra Buzila - Fixes for bug 446252
 *     Stefan Dirix - Fixes for bug 453749
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isFeatureMapContainment;
import static org.eclipse.emf.compare.utils.ReferenceUtil.safeEGet;
import static org.eclipse.emf.compare.utils.ReferenceUtil.safeESet;

import com.google.common.collect.Iterables;

import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.compare.FeatureMapChange;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.compare.utils.IEqualityHelper;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * This specific implementation of {@link AbstractMerger} will be used to merge attribute changes.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class FeatureMapChangeMerger extends AbstractMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#isMergerFor(org.eclipse.emf.compare.Diff)
	 */
	public boolean isMergerFor(Diff target) {
		return target instanceof FeatureMapChange;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.AbstractMerger#accept(org.eclipse.emf.compare.Diff, boolean)
	 */
	@Override
	protected void accept(final Diff diff, boolean rightToLeft) {
		FeatureMapChange featureMapChange = (FeatureMapChange)diff;
		switch (diff.getKind()) {
			case ADD:
				// Create the same element in right
				addInTarget(featureMapChange, rightToLeft);
				break;
			case DELETE:
				// Delete that same element from right
				removeFromTarget(featureMapChange, rightToLeft);
				break;
			case MOVE:
				moveElement(featureMapChange, rightToLeft);
				break;
			case CHANGE:
				changeValue(featureMapChange, rightToLeft);
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
		FeatureMapChange featureMapChange = (FeatureMapChange)diff;
		switch (diff.getKind()) {
			case ADD:
				// We have a ADD on right. we need to revert this addition
				removeFromTarget(featureMapChange, rightToLeft);
				break;
			case DELETE:
				// DELETE in the right. We need to re-create this element
				addInTarget(featureMapChange, rightToLeft);
				break;
			case MOVE:
				moveElement(featureMapChange, rightToLeft);
				break;
			case CHANGE:
				changeValue(featureMapChange, rightToLeft);
				break;
			default:
				break;
		}
	}

	/**
	 * This will be called when we need to create an element in the target side.
	 * <p>
	 * All necessary sanity checks have been made to ensure that the current operation is one that should
	 * create an object in its side or add an object to an attribute. In other words, either :
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
	protected void addInTarget(FeatureMapChange diff, boolean rightToLeft) {
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
			final FeatureMap.Entry expectedValue = (FeatureMap.Entry)diff.getValue();
			// We have the container, attribute and value. We need to know the insertion index.
			final int insertionIndex = findInsertionIndex(comparison, diff, rightToLeft);

			final List<Object> targetList = (List<Object>)safeEGet(expectedContainer, attribute);
			addFeatureMapValueInTarget(comparison, rightToLeft, targetList, insertionIndex, expectedValue);
		}
	}

	/**
	 * Add the FeatueMapEntry value at the insertionIndex position in the list.
	 * 
	 * @param comparison
	 *            The comparison object.
	 * @param rightToLeft
	 *            The way of merge.
	 * @param list
	 *            The list into which {@code value} should be added.
	 * @param insertionIndex
	 *            The index at which {@code value} should be inserted into {@code list}. {@code -1} if it
	 *            should be appended at the end of the list.
	 * @param entry
	 *            The value we need to add to {@code list}.
	 */
	private void addFeatureMapValueInTarget(final Comparison comparison, final boolean rightToLeft,
			final List<Object> list, final int insertionIndex, final FeatureMap.Entry entry) {
		final Object value = entry.getValue();
		final EStructuralFeature key = entry.getEStructuralFeature();
		if (value instanceof EObject) {
			final EObject copy;
			// The value has its equivalent on the opposite side, or not
			final Match match = comparison.getMatch((EObject)value);
			final EObject left = match.getLeft();
			final EObject right = match.getRight();
			if (rightToLeft && left != null) {
				copy = left;
			} else if (!rightToLeft && right != null) {
				copy = right;
			} else {
				copy = createCopy((EObject)value);
			}

			((BasicFeatureMap)(Object)list).addUnique(insertionIndex, FeatureMapUtil.createEntry(key, copy));

			if (key instanceof EReference && ((EReference)key).isContainment()) {
				if (rightToLeft) {
					match.setLeft(copy);
				} else {
					match.setRight(copy);
				}
				// Copy XMI ID when applicable.
				final Resource initialResource = ((EObject)value).eResource();
				final Resource targetResource = copy.eResource();
				if (initialResource instanceof XMIResource && targetResource instanceof XMIResource) {
					((XMIResource)targetResource).setID(copy, ((XMIResource)initialResource)
							.getID((EObject)value));
				}
			}
		} else {
			((BasicFeatureMap)(Object)list).add(insertionIndex, FeatureMapUtil.createEntry(key, value));
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
	protected void removeFromTarget(FeatureMapChange diff, boolean rightToLeft) {
		final EObject currentContainer;
		if (rightToLeft) {
			currentContainer = diff.getMatch().getLeft();
		} else {
			currentContainer = diff.getMatch().getRight();
		}

		if (currentContainer == null) {
			// FIXME throw exception? log? re-try to merge our requirements?
		} else {
			FeatureMap.Entry expectedValue = (FeatureMap.Entry)diff.getValue();

			if (!isDiffSourceIsMergeTarget(diff, rightToLeft)) {
				final List<Object> targetList = (List<Object>)safeEGet(currentContainer, diff.getAttribute());
				for (Object object : targetList) {
					if (diff.getMatch().getComparison().getEqualityHelper().matchingValues(expectedValue,
							object)) {
						expectedValue = (FeatureMap.Entry)object;
						break;
					}
				}
			}

			final EStructuralFeature attribute = diff.getAttribute();
			// We have the container, attribute and value to remove.
			/*
			 * TODO if the same value appears twice, should we try and find the one that has actually been
			 * deleted? Will it happen that often? For now, remove the first occurence we find.
			 */
			final List<Object> targetList = (List<Object>)safeEGet(currentContainer, attribute);
			final Comparison comparison = diff.getMatch().getComparison();
			removeFeatureMapValueFromTarget(comparison, rightToLeft, targetList, expectedValue);
		}
	}

	/**
	 * Remove the FeatueMapEntry value from the list.
	 * 
	 * @param comparison
	 *            The comparison object.
	 * @param rightToLeft
	 *            The way of merge.
	 * @param list
	 *            The list from which {@code value} should be removed.
	 * @param entry
	 *            The value we need to remove from {@code list}.
	 */
	private void removeFeatureMapValueFromTarget(final Comparison comparison, final boolean rightToLeft,
			final List<Object> list, final FeatureMap.Entry entry) {
		final Object value = entry.getValue();
		final EStructuralFeature key = entry.getEStructuralFeature();
		final Match expectedContainerMatch = comparison.getMatch((EObject)value);
		((BasicFeatureMap)(Object)list).remove(key, value);

		if (((EReference)key).isContainment()) {
			if (rightToLeft) {
				expectedContainerMatch.setLeft(null);
			} else {
				expectedContainerMatch.setRight(null);
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
	protected void moveElement(final FeatureMapChange diff, final boolean rightToLeft) {
		final Match match = diff.getMatch();
		final Comparison comparison = match.getComparison();
		final EObject expectedContainer = ComparisonUtil.moveElementGetExpectedContainer(comparison, diff,
				rightToLeft);

		if (expectedContainer == null) {
			// TODO throws exception?
		} else {
			final FeatureMap.Entry expectedEntry = moveElementGetExpectedEntry(comparison, diff,
					expectedContainer, rightToLeft);
			// We now know the target container, target attribute and target entry.
			doMove(diff, comparison, expectedContainer, expectedEntry, rightToLeft);
		}
	}

	/**
	 * Get the expected FeatureMap.Entry to move.
	 * 
	 * @param comparison
	 *            The comparison object.
	 * @param diff
	 *            The diff we are currently merging.
	 * @param expectedContainer
	 *            The expected container that will contain the expected entry to move.
	 * @param rightToLeft
	 *            Whether we should move the value in the left or right side.
	 * @return The expected entry if found, <code>null</code> otherwise.
	 */
	private FeatureMap.Entry moveElementGetExpectedEntry(final Comparison comparison,
			final FeatureMapChange diff, final EObject expectedContainer, final boolean rightToLeft) {
		final FeatureMap.Entry expectedEntry;
		if (isDiffSourceIsMergeTarget(diff, rightToLeft)) {
			expectedEntry = getExpectedEntryWhenDiffSourceIsMergeTarget(comparison, diff);

		} else {
			expectedEntry = getExpectedEntryWhenDiffSourceIsNotMergeTarget(comparison, diff,
					expectedContainer, rightToLeft);
		}
		return expectedEntry;
	}

	/**
	 * Get the expected FeatureMap.Entry to move.
	 * 
	 * @param comparison
	 *            The comparison object.
	 * @param diff
	 *            The diff we are currently merging.
	 * @param expectedContainer
	 *            The expected container that will contain the expected entry to move.
	 * @param rightToLeft
	 *            Whether we should move the value in the left or right side.
	 * @return The expected entry if found, <code>null</code> otherwise.
	 */
	@SuppressWarnings("unchecked")
	private FeatureMap.Entry getExpectedEntryWhenDiffSourceIsNotMergeTarget(final Comparison comparison,
			final FeatureMapChange diff, final EObject expectedContainer, final boolean rightToLeft) {
		FeatureMap.Entry expectedEntry = null;
		final IEqualityHelper equalityHelper = comparison.getEqualityHelper();
		final FeatureMap.Entry diffEntry = (FeatureMap.Entry)diff.getValue();
		// It is a Move on containment entry and the diff has an equivalence.
		Equivalence equ = diff.getEquivalence();
		if (isFeatureMapContainment(diff) && equ != null) {
			// There is a ReferenceChange associated with the FeatureMapChange. This ReferenceChange
			// contains the expected value to move.
			for (ReferenceChange equivalence : Iterables.filter(equ.getDifferences(), ReferenceChange.class)) {
				final Match equivalenceMatchValue = comparison.getMatch(equivalence.getValue());
				final Object expectedEntryValue;
				if (rightToLeft) {
					expectedEntryValue = equivalenceMatchValue.getLeft();
				} else {
					expectedEntryValue = equivalenceMatchValue.getRight();
				}
				expectedEntry = FeatureMapUtil.createEntry(diffEntry.getEStructuralFeature(),
						expectedEntryValue);
				break;
			}
		} else {
			final List<Object> targetList = (List<Object>)safeEGet(expectedContainer, diff.getAttribute());
			for (Object object : targetList) {
				if (equalityHelper.matchingValues(diffEntry, object)) {
					expectedEntry = (FeatureMap.Entry)object;
					break;
				}
			}
		}
		return expectedEntry;
	}

	/**
	 * Get the expected FeatureMap.Entry to move.
	 * 
	 * @param comparison
	 *            The comparison object.
	 * @param diff
	 *            The diff we are currently merging.
	 * @return The expected entry if found, <code>null</code> otherwise.
	 */
	@SuppressWarnings("unchecked")
	private FeatureMap.Entry getExpectedEntryWhenDiffSourceIsMergeTarget(final Comparison comparison,
			final FeatureMapChange diff) {
		final FeatureMap.Entry expectedEntry;
		final IEqualityHelper equalityHelper = comparison.getEqualityHelper();
		final FeatureMap.Entry diffEntry = (FeatureMap.Entry)diff.getValue();
		if (comparison.isThreeWay() && isFeatureMapContainment(diff)) {
			final Match matchValue = comparison.getMatch((EObject)diffEntry.getValue());
			// search the origin key associated to the value
			EStructuralFeature originKey = null;
			final List<Object> originList = (List<Object>)safeEGet(matchValue.getOrigin().eContainer(), diff
					.getAttribute());
			for (Object object : originList) {
				if (object instanceof FeatureMap.Entry
						&& equalityHelper.matchingValues(diffEntry.getValue(), ((FeatureMap.Entry)object)
								.getValue())) {
					// same value, get the key
					originKey = ((FeatureMap.Entry)object).getEStructuralFeature();
					break;
				}
			}
			expectedEntry = FeatureMapUtil.createEntry(originKey, diffEntry.getValue());
		} else if (((EReference)diffEntry.getEStructuralFeature()).isContainment()) {
			EStructuralFeature targetReference = getTargetReference(comparison, diff);
			expectedEntry = FeatureMapUtil.createEntry(targetReference, diffEntry.getValue());
		} else {
			expectedEntry = diffEntry;
		}
		return expectedEntry;
	}

	/**
	 * Get the target EStructuralFeature when moving a FeatureMap.Entry.
	 * 
	 * @param comparison
	 *            The comparison object.
	 * @param diff
	 *            The diff we are currently merging.
	 * @return The target reference, i.e. the reference into which the value will be moved.
	 */
	private EStructuralFeature getTargetReference(final Comparison comparison, final FeatureMapChange diff) {
		final FeatureMap.Entry diffEntry = (FeatureMap.Entry)diff.getValue();
		final Match equivalenceMatchValue = comparison.getMatch((EObject)diffEntry.getValue());
		final EObject targetValue;
		if (diff.getSource() == DifferenceSource.LEFT) {
			targetValue = equivalenceMatchValue.getRight();
		} else {
			targetValue = equivalenceMatchValue.getLeft();
		}
		return targetValue.eContainingFeature();
	}

	/**
	 * Checks if the source of the given diff is the same as the target of the merge.
	 * 
	 * @param diff
	 *            The diff we are currently merging.
	 * @param rightToLeft
	 *            Whether we should merge the diff in the left or right side.
	 * @return true if the source of the given diff is the same as the target of the merge, false otherwise.
	 */
	private boolean isDiffSourceIsMergeTarget(final Diff diff, final boolean rightToLeft) {
		DifferenceSource source = diff.getSource();
		return source == DifferenceSource.LEFT && rightToLeft || source == DifferenceSource.RIGHT
				&& !rightToLeft;
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
	protected void doMove(FeatureMapChange diff, Comparison comparison, EObject expectedContainer,
			FeatureMap.Entry expectedValue, boolean rightToLeft) {
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
			if (insertionIndex > currentIndex && currentIndex >= 0) {
				insertionIndex--;
			}

			if (currentIndex == -1) {
				if (insertionIndex < 0 || insertionIndex > targetList.size()) {
					((BasicFeatureMap)(Object)targetList).addUnique(expectedValue);
				} else {
					((BasicFeatureMap)(Object)targetList).addUnique(insertionIndex, expectedValue);
				}
			} else {
				if (insertionIndex < 0 || insertionIndex > targetList.size()) {
					((BasicFeatureMap)(Object)targetList).move(targetList.size() - 1, expectedValue);
				} else {
					((BasicFeatureMap)(Object)targetList).move(insertionIndex, expectedValue);
				}
			}
		} else {
			// This will never happen with the default diff engine, but may still be done from extenders
			safeESet(expectedContainer, attribute, expectedValue);
		}
	}

	/**
	 * This will be called by the merge operations in order to change a key.
	 * 
	 * @param diff
	 *            The diff we are currently merging.
	 * @param rightToLeft
	 *            Direction of the merge.
	 */
	@SuppressWarnings("unchecked")
	protected void changeValue(FeatureMapChange diff, boolean rightToLeft) {
		final Match match = diff.getMatch();
		final IEqualityHelper equalityHelper = match.getComparison().getEqualityHelper();
		final EStructuralFeature attribute = diff.getAttribute();
		final FeatureMap.Entry entry = (FeatureMap.Entry)diff.getValue();
		// the value we're looking for in expected and origin container.
		final Object entryValue = entry.getValue();

		// Get the XMI ID
		final String originValueId;
		if (entryValue instanceof EObject) {
			final Resource initialResource = ((EObject)entryValue).eResource();
			if (initialResource instanceof XMIResource) {
				originValueId = ((XMIResource)initialResource).getID((EObject)entryValue);
			} else {
				originValueId = null;
			}
		} else {
			originValueId = null;
		}

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

		// search the origin key associated to the value
		EStructuralFeature originKey = null;
		final List<Object> originList = (List<Object>)safeEGet(originContainer, attribute);
		for (Object object : originList) {
			if (object instanceof FeatureMap.Entry) {
				// same value, get the key
				if (equalityHelper.matchingValues(entryValue, ((FeatureMap.Entry)object).getValue())) {
					originKey = ((FeatureMap.Entry)object).getEStructuralFeature();
					break;
				}
			}
		}

		if (originKey == null) {
			throw new RuntimeException("FeatureMapChangeMerger: Cannot find the key to change."); //$NON-NLS-1$
		}

		// search the value in expected container to change his key.
		final List<Object> targetList = (List<Object>)safeEGet(expectedContainer, attribute);
		int index = 0;
		for (Object object : targetList) {
			if (object instanceof FeatureMap.Entry) {
				// same value, now change the key
				Object targetValue = ((FeatureMap.Entry)object).getValue();
				if (equalityHelper.matchingValues(entryValue, targetValue)) {
					// forced to use setUnique(int, Entry) because if the originKey is not present in the
					// target map, the setUnique(EStructuralFeature, int, Object) will not validate the key
					((BasicFeatureMap)(Object)targetList).setUnique(index, FeatureMapUtil.createEntry(
							originKey, targetValue));

					// setUnique(int, Entry) doesn't keep ID, so copy XMI ID when applicable.
					final Resource targetResource;
					if (originKey instanceof EReference && ((EReference)originKey).isContainment()
							&& targetValue instanceof EObject && originValueId != null) {
						targetResource = ((EObject)targetValue).eResource();
					} else {
						targetResource = null;
					}
					if (targetResource instanceof XMIResource) {
						((XMIResource)targetResource).setID((EObject)targetValue, originValueId);
					}
					break;
				}
			}
			index++;
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
