/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Martin Taal - [299641] Compare arrays by their content instead of instance equality
 *******************************************************************************/
package org.eclipse.emf.compare.diff.internal;

import java.lang.reflect.Array;
import java.util.List;

import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.MatchPackage;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;

/**
 * Utility class to diff Collection values.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class DiffCollectionsHelper {
	/**
	 * the cross referencer used to retrive the matches.
	 */
	private EcoreUtil.CrossReferencer crossReferencer;

	/**
	 * Create a new utility to diff collections.
	 * 
	 * @param referencer
	 *            the cross referencer used to retrieve the matches.
	 */
	public DiffCollectionsHelper(CrossReferencer referencer) {
		this.crossReferencer = referencer;
	}

	/**
	 * Return the number of missing occurrence from tested list comparing with reference list.
	 * 
	 * @param referenceList
	 *            the reference list.
	 * @param testedList
	 *            the list to test.
	 * @param value
	 *            the value to look occurrences for.
	 * @return the number of "value" occurrences missing in testedList to be like referenceList.
	 */
	public int getNumberOfMissingOccurrence(List<Object> referenceList, List<Object> testedList, Object value) {
		final int expectedOccurrences = getNumberOfOccurrences(referenceList, value);
		final int actualOccurrences = getNumberOfOccurrences(testedList, value);
		return expectedOccurrences - actualOccurrences;
	}

	/**
	 * Return the number of occurrences in a list.
	 * 
	 * @param values
	 *            list.
	 * @param value
	 *            element to look for.
	 * @return the number of occurrences in it.
	 */
	private int getNumberOfOccurrences(List<Object> values, Object value) {
		int i = 0;
		for (Object aValue : values) {
			if (!areDistinctValues(aValue, value)) {
				i++;
			}
		}
		return i;
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
	public boolean areDistinctValues(Object left, Object right) {
		final boolean distinct;
		if (left instanceof EEnumLiteral && right instanceof EEnumLiteral) {
			final StringBuilder value1 = new StringBuilder();
			value1.append(((EEnumLiteral)left).getLiteral()).append(((EEnumLiteral)left).getValue());
			final StringBuilder value2 = new StringBuilder();
			value2.append(((EEnumLiteral)right).getLiteral()).append(((EEnumLiteral)right).getValue());
			distinct = !value1.toString().equals(value2.toString());
		} else if (left instanceof EObject && right instanceof EObject) {
			// [248442] This will handle FeatureMapEntries detection
			distinct = left != getMatchedEObject((EObject)right);
		} else if (left != null && left.getClass().isArray()) {
			// [299641] compare arrays by their content instead of instance equality
			distinct = areDistinctArrays(left, right);
		} else {
			distinct = left != null && !left.equals(right) || left == null && left != right;
		}
		return distinct;
	}

	/**
	 * Return the left or right matched EObject from the one given. More specifically, this will return the
	 * left matched element if the given {@link EObject} is the right one, or the right matched element if the
	 * given {@link EObject} is either the left or the origin one.
	 * 
	 * @param from
	 *            The original {@link EObject}.
	 * @return The matched {@link EObject}.
	 */
	protected final EObject getMatchedEObject(EObject from) {
		EObject matchedEObject = null;
		if (crossReferencer != null && from != null && crossReferencer.get(from) != null) {
			for (final org.eclipse.emf.ecore.EStructuralFeature.Setting setting : crossReferencer.get(from)) {
				if (setting.getEObject() instanceof Match2Elements) {
					if (setting.getEStructuralFeature().getFeatureID() == MatchPackage.MATCH2_ELEMENTS__LEFT_ELEMENT) {
						matchedEObject = ((Match2Elements)setting.getEObject()).getRightElement();
					} else if (setting.getEStructuralFeature().getFeatureID() == MatchPackage.MATCH2_ELEMENTS__RIGHT_ELEMENT) {
						matchedEObject = ((Match2Elements)setting.getEObject()).getLeftElement();
					}
				}
			}
		}
		return matchedEObject;
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
	private boolean areDistinctArrays(Object left, Object right) {
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
