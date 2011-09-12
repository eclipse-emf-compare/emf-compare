/*******************************************************************************
 * Copyright (c) 2009, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.internal.merge.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.diff.merge.DefaultMerger;
import org.eclipse.emf.compare.diff.metamodel.AttributeOrderChange;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * Merger for an {@link AttributeOrderChange} operation.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("unchecked")
public class AttributeOrderChangeMerger extends DefaultMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.DefaultMerger#doApplyInOrigin()
	 */
	@Override
	public void doApplyInOrigin() {
		final AttributeOrderChange theDiff = (AttributeOrderChange)this.diff;
		final EAttribute attribute = theDiff.getAttribute();
		final EObject leftElement = theDiff.getLeftElement();
		final EObject rightElement = theDiff.getRightElement();

		final List<Object> leftList = (List<Object>)leftElement.eGet(attribute);
		final List<Object> rightList = (List<Object>)rightElement.eGet(attribute);

		/*
		 * We need to transform the "left" list into the "right" list, modulo missing values. In practical
		 * terms, this means that we'll simply leave untouched any element that has no match in the "right"
		 * list (elements that were deleted) while reordering the others in the order they have in the "right"
		 * list.
		 */
		final List<Object> leftCopy = new ArrayList<Object>(leftList);
		final List<Object> result = new ArrayList<Object>(leftList.size());
		// Add all unmatched values in the result list
		for (int i = 0; i < leftList.size(); i++) {
			final Object left = leftList.get(i);

			boolean hasMatch = false;
			for (int j = 0; !hasMatch && j < rightList.size(); j++) {
				hasMatch = !areDistinctValues(left, rightList.get(j));
			}

			if (!hasMatch) {
				leftCopy.remove(left);
				result.add(left);
			}
		}
		// Then reorder according to the right list's order
		for (int i = 0; i < rightList.size(); i++) {
			final Object right = rightList.get(i);

			Object leftMatch = null;
			for (int j = 0; leftMatch == null && j < leftCopy.size(); j++) {
				if (!areDistinctValues(right, leftCopy.get(j))) {
					leftMatch = leftCopy.get(j);
				}
			}

			if (leftMatch != null) {
				leftCopy.remove(leftMatch);
				result.add(leftMatch);
			}
		}
		// Finally, set the value of our attribute to this new list
		leftElement.eSet(attribute, result);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.DefaultMerger#doUndoInTarget()
	 */
	@Override
	public void doUndoInTarget() {
		final AttributeOrderChange theDiff = (AttributeOrderChange)this.diff;
		final EAttribute attribute = theDiff.getAttribute();
		final EObject leftElement = theDiff.getLeftElement();
		final EObject rightElement = theDiff.getRightElement();

		final List<Object> leftList = (List<Object>)leftElement.eGet(attribute);
		final List<Object> rightList = (List<Object>)rightElement.eGet(attribute);

		/*
		 * We need to transform the "right" list into the "left" list, modulo missing values. In practical
		 * terms, this means that we'll simply leave untouched any element that has no match in the "left"
		 * list (elements that were added) while reordering the others in the order they have in the "left"
		 * list.
		 */
		final List<Object> rightCopy = new ArrayList<Object>(rightList);
		final List<Object> result = new ArrayList<Object>(rightList.size());
		// Add all unmatched values in the result list
		for (int i = 0; i < rightList.size(); i++) {
			final Object right = rightList.get(i);

			boolean hasMatch = false;
			for (int j = 0; !hasMatch && j < leftList.size(); j++) {
				hasMatch = !areDistinctValues(right, leftList.get(j));
			}

			if (!hasMatch) {
				rightCopy.remove(right);
				result.add(right);
			}
		}
		// Then reorder according to the left list's order
		for (int i = 0; i < leftList.size(); i++) {
			final Object left = leftList.get(i);

			Object rightMatch = null;
			for (int j = 0; rightMatch == null && j < rightCopy.size(); j++) {
				if (!areDistinctValues(left, rightCopy.get(j))) {
					rightMatch = rightCopy.get(j);
				}
			}

			if (rightMatch != null) {
				rightCopy.remove(rightMatch);
				result.add(rightMatch);
			}
		}
		// Finally, set the value of our attribute to this new list
		rightElement.eSet(attribute, result);
	}

	/**
	 * Compare values by equality handling specifics of EMF.
	 * 
	 * @param left
	 *            object 1.
	 * @param right
	 *            object 2
	 * @return true if both objects are not equals.
	 */
	private static boolean areDistinctValues(Object left, Object right) {
		final boolean distinct;
		if (left instanceof EEnumLiteral && right instanceof EEnumLiteral) {
			final StringBuilder value1 = new StringBuilder();
			value1.append(((EEnumLiteral)left).getLiteral()).append(((EEnumLiteral)left).getValue());
			final StringBuilder value2 = new StringBuilder();
			value2.append(((EEnumLiteral)right).getLiteral()).append(((EEnumLiteral)right).getValue());
			distinct = !value1.toString().equals(value2.toString());
		} else if (left != null && left.getClass().isArray()) {
			// [299641] compare arrays by their content instead of instance equality
			distinct = areDistinctArrays(left, right);
		} else if (left instanceof FeatureMap.Entry || right instanceof FeatureMap.Entry) {
			distinct = areDistinctEntries(left, right);
		} else {
			distinct = left != null && !left.equals(right) || left == null && left != right;
		}
		return distinct;
	}

	/**
	 * Compares the two given value while assuming one or both is a {@link FeatureMap.Entry}.
	 * 
	 * @param left
	 *            Left of the two values to compare.
	 * @param right
	 *            Right of the two values to compare.
	 * @return <code>true</code> if the two values are distinct, <code>false</code> otherwise.
	 */
	private static boolean areDistinctEntries(Object left, Object right) {
		final boolean distinct;
		if (left instanceof FeatureMap.Entry && right instanceof FeatureMap.Entry) {
			distinct = areDistinctValues(((FeatureMap.Entry)left).getValue(),
					((FeatureMap.Entry)right).getValue());
		} else if (left instanceof FeatureMap.Entry) {
			distinct = areDistinctValues(((FeatureMap.Entry)left).getValue(), right);
		} else {
			distinct = areDistinctValues(left, ((FeatureMap.Entry)right).getValue());
		}
		return distinct;
	}

	/**
	 * Compares two values as arrays, checking that the length and content of both matches each other.
	 * 
	 * @param left
	 *            The value of the attribute from the left compare resource.
	 * @param right
	 *            The value of the attribute from the right compare resource.
	 * @return <code>true</code> if the <code>left</code> value is distinct from the <code>right</code> value.
	 */
	private static boolean areDistinctArrays(Object left, Object right) {
		boolean distinct = false;
		// we know left is a non-null array.
		if (right == null || !right.getClass().isArray()) {
			distinct = true;
		} else {
			final int leftLength = Array.getLength(left);
			final int rightLength = Array.getLength(right);
			if (leftLength != rightLength) {
				distinct = true;
			} else {
				for (int i = 0; i < leftLength; i++) {
					final Object leftElement = Array.get(left, i);
					final Object rightElement = Array.get(right, i);
					if (areDistinctValues(leftElement, rightElement)) {
						distinct = true;
						break;
					}
				}
			}
		}
		return distinct;
	}
}
