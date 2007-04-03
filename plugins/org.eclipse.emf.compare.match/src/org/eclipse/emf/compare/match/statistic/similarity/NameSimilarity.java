/*  
 * Copyright (c) 2006, Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.match.statistic.similarity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * This class provides services to work on strings and to compare EObjects
 * 
 * @author www.obeo.fr
 * 
 */
public class NameSimilarity {

	/**
	 * This method returns a list of strings called "pairs", for example
	 * 
	 * pairs("MyString")
	 * 
	 * returns
	 * 
	 * ["MY","YS","ST","TR","RI","IN","NG"]
	 * 
	 * @param source :
	 *            the string to process
	 * @return a list of string corresponding to the possibles pairs of the
	 *         source one
	 */
	public static List pairs(String source) {
		List result = new LinkedList();
		if (source != null) {
			for (int i = 0; i < source.length() - 1; i = i + 1)
				result.add(source.toUpperCase().substring(i, i + 2));
			if (source.length() % 2 == 1 && source.length() > 1)
				result.add(source.toUpperCase().substring(source.length() - 2,
						source.length() - 1));
		}
		return result;
	}

	/**
	 * Return a metric result about name similarity. It compares 2 strings and
	 * return a double comprised between 0 and 1. The more it is, the more the
	 * strings are equals
	 * 
	 */
	public static double nameSimilarityMetric(String str1, String str2) {

		double result = 0;
		if (str1 == null || str2 == null)
			return 0;
		if (str1.length() == 1 || str2.length() == 1) {
			if (str1.equals(str2))
				return 1.0;
			return 0;
		}

		List pairs1 = pairs(str1);
		List pairs2 = pairs(str2);

		double union = pairs1.size() + pairs2.size();

		if (union == 0)
			return 0;

		pairs1.retainAll(pairs2);

		int inter = pairs1.size();
		result = (inter * 2.0) / union;
		if (result > 1)
			result = 1;
		if (result == 1.0 && !str1.equals(str2))
			return 0.999999;
		return result;
	}

	/**
	 * This service allow to find the best EObject corresponding to the name.
	 * 
	 * @param current
	 *            current node
	 * @param str
	 *            the string used to find the element
	 * @throws ENodeCastException
	 */
	public EObject find(EObject current, String str) {
		EObject result = current;
		double max = 0;

		TreeIterator it = current.eAllContents();
		while (it.hasNext()) {
			Object next = it.next();
			if (next instanceof EObject
					&& nameSimilarityMetric(next.toString(), str) > max) {
				max = nameSimilarityMetric(next.toString(), str);
				result = (EObject) next;
			}
		}

		return result;
	}

	/**
	 * This service allow to find the best EObject corresponding to the name.
	 * 
	 * @param current
	 *            current node
	 * @param str
	 *            the string used to find the element
	 * @throws ENodeCastException
	 * @throws FactoryException
	 */
	public static List find(EObject current, EObject search, double threshold)
			throws FactoryException {
		List result = new LinkedList();
		EObject resultObject = null;
		double max = 0;

		TreeIterator it = current.eAllContents();
		while (it.hasNext()) {
			Object next = it.next();
			double similarity = nameSimilarityMetric(
					contentValue((EObject) next), contentValue(search));
			if (next instanceof EObject
					&& ((EObject) next).eClass().getName().equals(
							search.eClass().getName()) && similarity > max
					&& similarity > threshold) {

				max = nameSimilarityMetric(contentValue((EObject) next),
						contentValue(search));
				resultObject = (EObject) next;
			}
		}
		if (resultObject != null)
			result.add(resultObject);
		return result;
	}

	public static List findInList(List data, EObject search, double threshold)
			throws FactoryException {
		List result = new LinkedList();
		EObject resultObject = null;
		double max = 0;

		Iterator it = data.iterator();
		while (it.hasNext()) {
			Object next = it.next();
			double similarity = nameSimilarityMetric(
					contentValue((EObject) next), contentValue(search));
			if (next instanceof EObject && similarity > max
					&& similarity > threshold) {

				max = nameSimilarityMetric(contentValue((EObject) next),
						contentValue(search));
				resultObject = (EObject) next;
			}
		}
		if (resultObject != null)
			result.add(resultObject);
		return result;
	}

	/**
	 * Return a list of String representing the object content.
	 * 
	 * @param current
	 * @return
	 * @throws FactoryException
	 */
	public static Collection contentValueListWithName(EObject current)
			throws FactoryException {
		EObject eclass = current.eClass();
		Collection result = new ArrayList();
		List eclassAttributes = new LinkedList();
		if (eclass instanceof EClass)
			eclassAttributes = ((EClass) eclass).getEAllAttributes();
		// first, find the eclass structural feature most similar with name
		if (eclassAttributes.size() > 0) {

			Iterator it = eclassAttributes.iterator();
			while (it.hasNext()) { // for each metamodel feature
				Object next = it.next();
				if (next instanceof EObject) {
					EObject obj = (EObject) next;
					String attributeName = EFactory.eGetAsString(obj, "name");
					// get the feature name and the feature value
					String value = EFactory
							.eGetAsString(current, attributeName);
					if (value != null && value.length() < 30)
						result.add(attributeName + " : " + value);
				}
			}

		}
		return result;
	}

	/**
	 * Return a String representing the object value
	 * 
	 * @param current
	 * @return
	 * @throws FactoryException
	 */
	public static String contentValueWithName(EObject current)
			throws FactoryException {
		Collection values = contentValueListWithName(current);
		String result = "";
		Iterator it = values.iterator();
		while (it.hasNext())
			result += (String) it.next();
		return result;
	}

	/**
	 * Return a string representations of all the features value
	 * 
	 * @param current
	 * @return
	 * @throws FactoryException
	 */
	public static String contentValue(EObject current) throws FactoryException {
		EObject eclass = current.eClass();
		String result = "";
		List eclassAttributes = new LinkedList();
		if (eclass instanceof EClass)

		{
			eclassAttributes = ((EClass) eclass).getEAllAttributes();
			// result += ((EClass)eclass).getName();
		}
		// first, find the eclass structural feature most similar with name
		if (eclassAttributes.size() > 0) {

			Iterator it = eclassAttributes.iterator();
			while (it.hasNext()) { // for each metamodel feature
				Object next = it.next();
				if (next instanceof EObject) {
					EObject obj = (EObject) next;
					String attributeName = EFactory.eGetAsString(obj, "name");
					// get the feature name and the feature value
					String value = EFactory
							.eGetAsString(current, attributeName);
					if (value != null && value.length() < 40)
						result += EFactory.eGetAsString(current, attributeName)
								+ " ";
				}
			}

		}
		return result;
	}

	/**
	 * find the property most similar to a name one on any object
	 * 
	 * @param current
	 * @return
	 * @throws ENodeCastException
	 * @throws FactoryException
	 */
	public static String findName(EObject current) throws FactoryException {

		if (current == null)
			return "";

		EAttribute nameFeature = findNameFeature(current);
		if (nameFeature != null) {
			String bestFeatureName = nameFeature.getName();
			// now we should return the feature value
			String result = EFactory.eGetAsString(current, bestFeatureName);
			if (result != null)
				return result;
			else
				return current.eClass().getName(); // TODOCBR, if the element as an
											// attribute, pick one, else use the
											// Class name
		} else {// eClass has no features, just keep the toString...
			return current.eClass().getName();
		}

	}

	/**
	 * Return the feature which seems to be the name
	 * 
	 * @param current
	 * @return
	 * @throws FactoryException
	 */
	public static EAttribute findNameFeature(EObject current)
			throws FactoryException {

		EObject eclass = current.eClass();

		List eclassAttributes = new LinkedList();
		if (eclass instanceof EClass)
			eclassAttributes = ((EClass) eclass).getEAllAttributes();
		EAttribute bestFeature = null;
		if (eclassAttributes.size() > 0) {
			bestFeature = (EAttribute) eclassAttributes.get(0);
		}
		// first, find the eclass structural feature most similar with name
		if (eclassAttributes.size() > 0) {
			double max = 0;
			Iterator it = eclassAttributes.iterator();
			while (it.hasNext()) { // for each metamodel feature
				Object next = it.next();
				if (next instanceof EObject) {
					EObject obj = (EObject) next;
					String attributeName = EFactory.eGetAsString(obj, "name");
					// if the attributeName is more similar with "name" than
					// the other one
					if (nameSimilarityMetric(attributeName, "name") > max) {
						max = nameSimilarityMetric(attributeName, "name");
						bestFeature = (EAttribute) obj;
					}

				}
			}
		}
		// now we should return the feature value
		return bestFeature;

	}

	private static double lastScore = 100;

	private static String lastSearch = "";

	public EObject findIncremental(EObject current, String str) {
		if (!str.equals(lastSearch))
			lastScore = 100;
		EObject result = current;
		double max = 0;
		TreeIterator it = current.eAllContents();
		while (it.hasNext()) {
			Object next = it.next();
			if (next instanceof EObject
					&& nameSimilarityMetric(next.toString(), str) > max
					&& nameSimilarityMetric(next.toString(), str) < lastScore) {
				max = nameSimilarityMetric(next.toString(), str);
				result = (EObject) next;
			}
		}
		lastSearch = str;
		lastScore = max;
		return result;
	}

}
