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

import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * Utilities for structure comparison
 * 
 * @author Cedric Brun <cedric.brun@obeo.fr>
 * 
 */
public class StructureSimilarity {
	/**
	 * This method returns a double comprised between 0 and 1 representing the
	 * type similarities of the 2 EObjects. - 1 means the 2 objects types are
	 * very similar - 0 means the 2 objects types are very differents
	 * 
	 * @param obj1
	 * @param obj2
	 * @return returns a double comprised between 0 and 1 representing the type
	 *         similarity of the 2 objects
	 * @throws ENodeCastException
	 * @throws FactoryException
	 */
	public static double typeSimilarityMetric(EObject obj1, EObject obj2)
			throws FactoryException {
		return NameSimilarity.nameSimilarityMetric(typeValue(obj1),
				typeValue(obj2));
	}

	/**
	 * This method returns a double comprised between 0 and 1 representing the
	 * relations similarities of the 2 EObjects. - 1 means the 2 objects
	 * surround are very similar - 0 means the 2 objects surround are very
	 * differents
	 * 
	 * @param obj1
	 * @param obj2
	 * @return a double representing the relation similarity
	 * @throws ENodeCastException
	 * @throws FactoryException
	 * @throws FactoryException
	 * @throws ENodeCastException
	 */
	public static double relationsSimilarityMetric(EObject obj1, EObject obj2)
			throws FactoryException {
		return NameSimilarity.nameSimilarityMetric(relationsValue(obj1),
				relationsValue(obj2));
	}

	/**
	 * Return a list containing strings representing the Type of the eobject
	 * 
	 * @param current
	 * @return a list containing strings representing the Type of the eobject
	 * @throws FactoryException
	 */
	public static Collection typeValueList(EObject current)
			throws FactoryException {
		EObject eclass = current.eClass();
		Collection result = new ArrayList();
		List eclassAttributes = new LinkedList();
		if (eclass instanceof EClass)
			eclassAttributes = ((EClass) eclass).getEAllAttributes();
		// first, find the eclass structural feature most similar with name
		if (eclassAttributes.size() > 0) {
			result.add("type:" + current.eClass().getName());//$NON-NLS-1$
			Iterator it = eclassAttributes.iterator();
			while (it.hasNext()) { // for each metamodel feature
				Object next = it.next();
				if (next instanceof EObject) {
					EObject obj = (EObject) next;
					String attributeName = EFactory.eGetAsString(obj, "name");//$NON-NLS-1$
					// get the feature name and the feature value
					result.add("attr:" + obj.eClass().getName() + ":"
							+ attributeName);
				}
			}
			// get children's name
			if (eclass instanceof EClass)
				eclassAttributes = ((EClass) eclass).getEAllReferences();
			it = eclassAttributes.iterator();
			while (it.hasNext()) { // for each metamodel feature
				Object next = it.next();
				if (next instanceof EObject) {
					EObject obj = (EObject) next;
					String attributeName = EFactory.eGetAsString(obj, "name");//$NON-NLS-1$
					// get the feature name and the feature value
					result.add("ref:" + obj.eClass().getName() + ":"
							+ attributeName);
				}
			}
		}
		return result;
	}

	/**
	 * This method returns a String with content corresponding to the EObject
	 * type
	 * 
	 * @param current
	 * @return a String with content corresponding to the EObject type
	 * @throws FactoryException
	 */
	public static String typeValue(EObject current) throws FactoryException {
		Collection values = typeValueList(current);
		String result = ""; //$NON-NLS-1$
		Iterator it = values.iterator();
		while (it.hasNext())
			result += (String) it.next();
		return result;
	}

	/**
	 * This method returns a String representing the EObject attributes values
	 * 
	 * @param current
	 * @return a String representing the EObject attributes values
	 * @throws FactoryException
	 * @throws ENodeCastException
	 */
	public static String relationsValue(EObject current)
			throws FactoryException {
		EObject eclass = current.eClass();
		String result = ""; //$NON-NLS-1$
		List eclassAttributes = new LinkedList();
		if (eclass instanceof EClass)
			eclassAttributes = ((EClass) eclass).getEAllAttributes();
		if (current.eContainer() != null)
			result += NameSimilarity.findName(current.eContainer()) + "\n";//$NON-NLS-1$
		eclassAttributes = current.eContents();
		Iterator it = eclassAttributes.iterator();
		while (it.hasNext()) {
			Object next = it.next();
			if (next instanceof EObject) {
				EObject obj = (EObject) next;
				result += NameSimilarity.findName(obj) + "\n";//$NON-NLS-1$

			}
		}
		return result;
	}
}
