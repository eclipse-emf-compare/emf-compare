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
package org.eclipse.emf.compare.utils;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;

import java.lang.reflect.Array;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * EMF Compare needs its own rules for "equality", which are based on similarity instead of strict equality.
 * These will be used throughout the process.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EqualityHelper {
	/**
	 * A cache keeping track of the URIs for EObjects.
	 */
	private Map<EObject, URI> uriCache = new MapMaker().makeComputingMap(new Function<EObject, URI>() {

		public URI apply(EObject input) {
			return EcoreUtil.getURI(input);
		}
	});

	/**
	 * Check that the two given values are "equal", considering the specifics of EMF.
	 * 
	 * @param comparison
	 *            Provides us with the Match necessary for EObject comparison.
	 * @param object1
	 *            First of the two objects to compare here.
	 * @param object2
	 *            Second of the two objects to compare here.
	 * @return <code>true</code> if both objects are to be considered equal, <code>false</code> otherwise.
	 */
	public boolean matchingValues(Comparison comparison, Object object1, Object object2) {
		final boolean equal;
		if (object1 == object2) {
			equal = true;
		} else if (object1 instanceof EEnumLiteral && object2 instanceof EEnumLiteral) {
			final EEnumLiteral literal1 = (EEnumLiteral)object1;
			final EEnumLiteral literal2 = (EEnumLiteral)object2;
			final String value1 = literal1.getLiteral() + literal1.getValue();
			final String value2 = literal2.getLiteral() + literal2.getValue();
			equal = value1.equals(value2);
		} else if (object1 instanceof EObject && object2 instanceof EObject) {
			// [248442] This will handle FeatureMapEntries detection
			equal = matchingEObjects(comparison, (EObject)object1, (EObject)object2);
		} else if (object1 != null && object1.getClass().isArray() && object2 != null
				&& object2.getClass().isArray()) {
			// [299641] compare arrays by their content instead of instance equality
			equal = matchingArrays(comparison, object1, object2);
		} else if (isEmptyString(object1) || isEmptyString(object2)) {
			// Special case, consider that the empty String is equal to null (unset attributes)
			equal = object1 == null || object2 == null;
		} else {
			equal = object1 != null && object1.equals(object2);
		}
		return equal;
	}

	/**
	 * Returns {@code true} if the given {@code object} is {@code null} or the empty String.
	 * 
	 * @param object
	 *            The object we need to test.
	 * @return {@code true} if the given {@code object} is {@code null} or the empty String.
	 */
	private boolean isEmptyString(Object object) {
		return object instanceof String && ((String)object).length() == 0;
	}

	/**
	 * Compares two values as arrays, checking that their length and content match each other.
	 * 
	 * @param comparison
	 *            Provides us with the Match necessary for EObject comparison.
	 * @param object1
	 *            First of the two objects to compare here.
	 * @param object2
	 *            Second of the two objects to compare here.
	 * @return <code>true</code> if these two arrays are to be considered equal, <code>false</code> otherwise.
	 */
	private boolean matchingArrays(Comparison comparison, Object object1, Object object2) {
		boolean equal = true;
		final int length1 = Array.getLength(object1);
		if (length1 != Array.getLength(object2)) {
			equal = true;
		} else {
			for (int i = 0; i < length1 && equal; i++) {
				final Object element1 = Array.get(object1, i);
				final Object element2 = Array.get(object2, i);
				equal = matchingValues(comparison, element1, element2);
			}
		}
		return equal;
	}

	/**
	 * Compares two values as EObjects, using their Match if it can be found, comparing through their URIs
	 * otherwise.
	 * 
	 * @param comparison
	 *            Provides us with the Match necessary for EObject comparison.
	 * @param object1
	 *            First of the two objects to compare here.
	 * @param object2
	 *            Second of the two objects to compare here.
	 * @return <code>true</code> if these two EObjects are to be considered equal, <code>false</code>
	 *         otherwise.
	 */
	private boolean matchingEObjects(Comparison comparison, EObject object1, EObject object2) {
		final Match match = comparison.getMatch(object1);

		final boolean equal;
		// Match could be null if the value is out of the scope
		if (match != null) {
			equal = match.getLeft() == object2 || match.getRight() == object2 || match.getOrigin() == object2;
		} else {
			final URI uri1 = uriCache.get(object1);
			final URI uri2 = uriCache.get(object2);
			if (uri1.hasFragment() && uri2.hasFragment()) {
				equal = uri1.fragment().equals(uri2.fragment());
			} else {
				equal = uri1.equals(uri2);
			}
		}

		return equal;
	}

	/**
	 * This should only be used when no {@link Comparison} is available or if the two given Objects are known
	 * not to be instances of EObjects. EObjects passed for comparison through here will be compared through
	 * their {@link Object#equals(Object)} implementation.
	 * 
	 * @param object1
	 *            First of the two objects to compare here.
	 * @param object2
	 *            Second of the two objects to compare here.
	 * @return <code>true</code> if both objects are to be considered equal, <code>false</code> otherwise.
	 */
	public boolean matchingValues(Object object1, Object object2) {
		final boolean equal;
		if (object1 == object2) {
			equal = true;
		} else if (object1 instanceof EEnumLiteral && object2 instanceof EEnumLiteral) {
			final EEnumLiteral literal1 = (EEnumLiteral)object1;
			final EEnumLiteral literal2 = (EEnumLiteral)object2;
			final String value1 = literal1.getLiteral() + literal1.getValue();
			final String value2 = literal2.getLiteral() + literal2.getValue();
			equal = value1.equals(value2);
		} else if (object1 != null && object1.getClass().isArray() && object2 != null
				&& object2.getClass().isArray()) {
			// [299641] compare arrays by their content instead of instance equality
			equal = matchingArrays(object1, object2);
		} else if (isEmptyString(object1) || isEmptyString(object2)) {
			// Special case, consider that the empty String is equal to null (unset attributes)
			equal = object1 == null || object2 == null;
		} else {
			equal = object1 != null && object1.equals(object2);
		}
		return equal;
	}

	/**
	 * Compares two values as arrays, checking that their length and content match each other. Note that this
	 * should only be used when no {@link Comparison} is available.
	 * 
	 * @param object1
	 *            First of the two objects to compare here.
	 * @param object2
	 *            Second of the two objects to compare here.
	 * @return <code>true</code> if these two arrays are to be considered equal, <code>false</code> otherwise.
	 */
	private boolean matchingArrays(Object object1, Object object2) {
		boolean equal = true;
		final int length1 = Array.getLength(object1);
		if (length1 != Array.getLength(object2)) {
			equal = true;
		} else {
			for (int i = 0; i < length1 && equal; i++) {
				final Object element1 = Array.get(object1, i);
				final Object element2 = Array.get(object2, i);
				equal = matchingValues(element1, element2);
			}
		}
		return equal;
	}

	/**
	 * The EqualityHelper often needs to get an EObject uri. As such it has an internal cache other might
	 * leverage through this method.
	 * 
	 * @param object
	 *            any EObject.
	 * @return the URI of the given EObject.
	 */
	public URI getURI(EObject object) {
		return uriCache.get(object);
	}
}
