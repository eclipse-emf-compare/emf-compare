/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.internal.statistic;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.match.statistic.MetamodelFilter;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.EMFCompareMap;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * This class provides services to work on strings and to compare EObjects.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class NameSimilarity {
	/** {@link String} assumed to be the name of the feature that holds the name of an object. */
	private static final String EOBJECT_NAME_FEATURE = "name"; //$NON-NLS-1$

	/** Maximum length of the {@link String} we consider for a feature's value. */
	private static final int MAX_FEATURE_VALUE_LENGTH = 50;

	/** This map associates an {@link EClass} with the {@link EAttribute} that is assumed to hold its name. */
	private static final Map<String, EAttribute> NAME_FEATURE_CACHE = new EMFCompareMap<String, EAttribute>();

	/**
	 * Utility classes don't need to (and shouldn't) be instantiated.
	 */
	private NameSimilarity() {
		// prevents instantiation
	}

	/**
	 * Returns a string representation of all the features' values for a given {@link EObject}.
	 * 
	 * @param current
	 *            Object for which we need {@link String} representation.
	 * @return A string representation of all the features' values for a given {@link EObject}.
	 * @throws FactoryException
	 *             Thrown if one of the operation on {@link EObject} fails.
	 */
	public static String contentValue(EObject current) throws FactoryException {
		return contentValue(current, null);
	}

	/**
	 * Returns a string representation of the features' values for a given {@link EObject} after applying the
	 * given <code>filter</code>.
	 * 
	 * @param current
	 *            Object for which we need {@link String} representation.
	 * @param filter
	 *            Allows filtering of pertinent features.
	 * @return A string representation of all the features' values for a given {@link EObject}.
	 * @throws FactoryException
	 *             Thrown if one of the operation on {@link EObject} fails.
	 */
	public static String contentValue(EObject current, MetamodelFilter filter) throws FactoryException {
		final EObject eclass = current.eClass();
		final StringBuilder result = new StringBuilder();
		List<EStructuralFeature> eclassAttributes = new ArrayList<EStructuralFeature>();
		if (filter != null) {
			eclassAttributes = filter.getFilteredFeatures(current);
			eclassAttributes.remove(findNameFeature(current));
		} else {
			eclassAttributes.addAll(((EClass)eclass).getEAllAttributes());
		}
		if (eclassAttributes.size() > 0) {
			for (final EStructuralFeature feature : eclassAttributes) {
				if (feature instanceof EAttribute) {
					final String value = EFactory.eGetAsString(current, feature.getName());
					if (value != null && value.length() < MAX_FEATURE_VALUE_LENGTH) {
						result.append(value).append(" "); //$NON-NLS-1$
					}
				}
			}
		}
		return result.toString();
	}

	/**
	 * Finds the property which is the best candidate to be the name of an {@link EObject}.
	 * 
	 * @param current
	 *            {@link EObject} we seek the name for.
	 * @return The best candidate to be the name of the given object.
	 * @throws FactoryException
	 *             Thrown if an operation on <code>current</code> fails.
	 */
	public static String findName(EObject current) throws FactoryException {
		String name = ""; //$NON-NLS-1$
		if (current != null) {
			final EAttribute nameFeature = findNameFeature(current);
			if (nameFeature != null) {
				final String bestFeatureName = nameFeature.getName();
				name = EFactory.eGetAsString(current, bestFeatureName);
				if (name == null || "".equals(name)) { //$NON-NLS-1$
					// TODOCBR, if the element as an attribute, pick one, else use the Class name
					name = current.eClass().getName();
				}
			} else {
				name = current.eClass().getName();
			}
		}
		return name;
	}

	/**
	 * Returns the feature which seems to be the name of the given {@link EObject}.
	 * 
	 * @param current
	 *            {@link EObject} we seek the name feature of.
	 * @return The feature which seems to be the name of the given {@link EObject}.
	 */
	public static EAttribute findNameFeature(EObject current) {
		final EClass eclass = current.eClass();
		EAttribute bestFeature = NAME_FEATURE_CACHE.get(eclass.getName() + eclass.hashCode());

		if (bestFeature == null) {
			final List<EAttribute> eClassAttributes = eclass.getEAllAttributes();
			if (eClassAttributes.size() > 0) {
				bestFeature = eClassAttributes.get(0);
			}
			// first, find the eclass structural feature most similar with name
			if (eClassAttributes.size() > 0) {
				double max = 0d;
				for (final EAttribute attribute : eClassAttributes) {
					final String attributeName = attribute.getName();
					// if the attributeName is more similar with "name" than the other one
					if (nameSimilarityMetric(attributeName, EOBJECT_NAME_FEATURE) > max) {
						max = nameSimilarityMetric(attributeName, EOBJECT_NAME_FEATURE);
						bestFeature = attribute;
					}
				}
				NAME_FEATURE_CACHE.put(eclass.getName() + eclass.hashCode(), bestFeature);
			}
		}
		// now we should return the feature value
		return bestFeature;
	}

	/**
	 * Return a metric result about name similarity. It compares 2 strings and return a double comprised
	 * between 0 and 1. The greater this metric, the more equal the strings are.
	 * 
	 * @param str1
	 *            First of the two {@link String}s to compare.
	 * @param str2
	 *            Second of the two {@link String}s to compare.
	 * @return A metric result about name similarity (0 &lt;= value &lt;= 1).
	 */
	public static double nameSimilarityMetric(String str1, String str2) {
		double result = 0d;
		final double almostEquals = 0.999999d;
		if (str1 != null && str2 != null) {
			if (str1.equals(str2)) {
				result = 1d;
			} else if (str1.length() <= 2 && str2.length() <= 2) {
				int equalChars = 0;
				final char[] str1Value = str1.toCharArray();
				final char[] str2Value = str2.toCharArray();
				for (int i = 0; i < Math.min(str1Value.length, str2Value.length); i++) {
					if (str1Value[i] == str2Value[i]) {
						equalChars++;
					}
				}
				if (str1Value.length != str2Value.length) {
					result = (double)equalChars / (double)(str1Value.length + str2Value.length);
				} else {
					result = equalChars * 2d / (str1Value.length + str2Value.length);
				}
			} else if (str1.length() != 1 && str2.length() != 1) {
				final String string1 = str1.toLowerCase();
				final String string2 = str2.toLowerCase();

				final List<String> pairs1 = pairs(string1);
				final List<String> pairs2 = pairs(string2);

				final double union = pairs1.size() + pairs2.size();

				final int inter = retainCount(pairs1, pairSet(string2));

				result = inter * 2d / union;
				if (result > 1) {
					result = 1d;
				} else if (result > almostEquals) {
					result = almostEquals;
				}
			}
		}
		return result;
	}

	private static int retainCount(List<String> a, Set<String> b) {
		int count = 0;
		for (String aPair : a) {
			if (b.contains(aPair))
				count++;
		}
		return count;
	}

	/**
	 * This method returns a {@link List} of {@link String}s called "pairs". For example,
	 * 
	 * <pre>
	 * pairs(&quot;MyString&quot;)
	 * </pre>
	 * 
	 * returns ["My","yS","St","tr","ri","in","ng"]
	 * 
	 * @param source
	 *            The {@link String} to process.
	 * @return A {@link List} of {@link String} corresponding to the possibles pairs of the source one.
	 */
	public static List<String> pairs(String source) {
		final List<String> result = new ArrayList<String>();
		if (source != null) {
			final int length = source.length();
			for (int i = 0; i < length - 1; i++) {
				result.add(source.substring(i, i + 2));
			}
		}
		return result;
	}

	/**
	 * This method returns a {@link Set} of {@link String}s called "pairs". For example,
	 * 
	 * <pre>
	 * pairs(&quot;MyString&quot;)
	 * </pre>
	 * 
	 * returns ["My","yS","St","tr","ri","in","ng"]
	 * 
	 * @param source
	 *            The {@link String} to process.
	 * @return A {@link Set} of {@link String} corresponding to the possibles pairs of the source one.
	 */
	private static Set<String> pairSet(String source) {
		final Set<String> result = new LinkedHashSet<String>();
		if (source != null) {
			final int length = source.length();
			for (int i = 0; i < length - 1; i++) {
				result.add(source.substring(i, i + 2));
			}
		}
		return result;
	}

}
