/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.statistic.similarity;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.match.statistic.MetamodelFilter;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Utilities for structure comparison.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public final class StructureSimilarity {
	/**
	 * Utility classes don't need to (and shouldn't) be instantiated.
	 */
	private StructureSimilarity() {
		// prevents instantiation
	}

	/**
	 * This method returns a double comprised between 0 and 1 representing the relations similarities of the 2
	 * EObjects. - 1 means the 2 objects surroundings are very similar - 0 means the 2 objects surroundings
	 * are very different.
	 * 
	 * @param obj1
	 *            First of the two {@link EObject}s to compare.
	 * @param obj2
	 *            Second of the two {@link EObject}s to compare.
	 * @param filter
	 *            Allows filtering of the pertinent features.
	 * @return A <code>double</code> representing the relation similarity (0 &lt; value &lt; 1).
	 * @throws FactoryException
	 *             Thrown if we cannot compute the similarity.
	 */
	public static double relationsSimilarityMetric(EObject obj1, EObject obj2, MetamodelFilter filter)
			throws FactoryException {
		return NameSimilarity
				.nameSimilarityMetric(relationsValue(obj1, filter), relationsValue(obj2, filter));
	}

	/**
	 * This method returns a double comprised between 0 and 1 representing the type similarities of the 2
	 * EObjects. - 1 means the 2 objects types are very similar - 0 means the 2 objects types are very
	 * differents.
	 * 
	 * @param obj1
	 *            First of the two {@link EObject}s to compare.
	 * @param obj2
	 *            Second of the two {@link EObject}s to compare.
	 * @return Returns a <code>double</code> representing the type similarity of the 2 objects (0 &lt; value
	 *         &lt; 1).
	 * @throws FactoryException
	 *             Thrown if we cannot compute the similarity.
	 */
	public static double typeSimilarityMetric(EObject obj1, EObject obj2) throws FactoryException {
		final int attributesCount = obj1.eClass().getEAllAttributes().size()
				+ obj2.eClass().getEAllAttributes().size();
		final int referencesCount = obj1.eClass().getEAllReferences().size()
				+ obj2.eClass().getEAllReferences().size();
		final double attributesSimilarity = NameSimilarity.nameSimilarityMetric(attributeTypeValue(obj1),
				attributeTypeValue(obj2));
		final double referencesSimilarity = NameSimilarity.nameSimilarityMetric(referenceTypeValue(obj1),
				referenceTypeValue(obj2));

		double similarity = attributesSimilarity * attributesCount + referencesSimilarity * referencesCount;
		similarity /= attributesCount + referencesCount;

		return similarity;
	}

	/**
	 * This method returns a {@link String} with content corresponding to the given {@link EObject}'s
	 * attributes type.
	 * 
	 * @param current
	 *            {@link EObject} we need the attributes' type value of.
	 * @return A {@link String} with content corresponding to the {@link EObject}'s attributes type.
	 * @throws FactoryException
	 *             Thrown if we cannot retrieve one of <code>current</code>'s attributes name.
	 */
	private static String attributeTypeValue(EObject current) throws FactoryException {
		final StringBuilder result = new StringBuilder();

		final List<EAttribute> attributes = current.eClass().getEAllAttributes();
		for (EAttribute attribute : attributes)
			result.append(attribute.eClass().getName()).append(NameSimilarity.findName(attribute));

		return result.toString();
	}

	/**
	 * Returns a String composed of the names of all the {@link EObject}'s children.
	 * 
	 * @param current
	 *            {@link EObject} we need the children value of.
	 * @param filter
	 *            Allows filtering of the pertinent features.
	 * @return String composed of the names of all the {@link EObject}'s children.
	 * @throws FactoryException
	 *             Thrown if we cannot retrieve the {@link EObject} features or their values.
	 */
	private static String childrenValue(EObject current, MetamodelFilter filter) throws FactoryException {
		final EObject eclass = current.eClass();
		final StringBuilder result = new StringBuilder();
		List<EStructuralFeature> eObjectFeatures = new ArrayList<EStructuralFeature>();
		if (eclass instanceof EClass) {
			if (filter != null)
				eObjectFeatures = filter.getFilteredFeatures(current);
			else
				eObjectFeatures.addAll(((EClass)eclass).getEAllReferences());
		}
		for (EStructuralFeature feature : eObjectFeatures) {
			if (feature instanceof EReference && !((EReference)feature).isDerived()) {
				final Object value = current.eGet(feature);
				if (value instanceof List) {
					for (Object next : (List)value) {
						if (next instanceof EObject) {
							final String objName = NameSimilarity.findName((EObject)next);
							result.append(objName);
						}
					}
				} else if (value instanceof EObject) {
					final String objName = NameSimilarity.findName((EObject)value);
					result.append(objName);
				}
			}
		}
		return result.toString();
	}

	/**
	 * This method returns a {@link String} with content corresponding to the given {@link EObject}'s
	 * references type.
	 * 
	 * @param current
	 *            {@link EObject} we need the references' type value of.
	 * @return A {@link String} with content corresponding to the {@link EObject}'s references type.
	 * @throws FactoryException
	 *             Thrown if we cannot retrieve one of <code>current</code>'s references name.
	 */
	private static String referenceTypeValue(EObject current) throws FactoryException {
		final StringBuilder result = new StringBuilder();

		final List<EReference> references = current.eClass().getEAllReferences();
		for (EReference reference : references)
			result.append(reference.eClass().getName()).append(NameSimilarity.findName(reference));

		return result.toString();
	}

	/**
	 * This method returns a {@link String} representing the {@link EObject} attributes' values.
	 * 
	 * @param current
	 *            {@link EObject} we need the attributes' values of.
	 * @param filter
	 *            Allows filtering of the pertinent features.
	 * @return A {@link String} representing the {@link EObject} attributes' values.
	 * @throws FactoryException
	 *             Thrown if we cannot retrieve the {@link EObject} features or their values.
	 */
	private static String relationsValue(EObject current, MetamodelFilter filter) throws FactoryException {
		final StringBuilder result = new StringBuilder();
		result.append(childrenValue(current, filter));
		final EObject container = current.eContainer();
		if (container != null)
			result.append(NameSimilarity.findName(container));

		return result.toString();
	}
}
