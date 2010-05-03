/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.engine;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.match.statistic.MetamodelFilter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Class responsible to check similarity of elements based on an internal strategy.
 * 
 * @since 1.1
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public abstract class AbstractSimilarityChecker {
	/**
	 * The filter.
	 */
	protected MetamodelFilter filter;

	/**
	 * Create a new checker.
	 * 
	 * @param metamodelFilter
	 *            a metamodel filter the checker can use to know whether a feature alwaas has the same value
	 *            or not in the models.
	 */
	public AbstractSimilarityChecker(MetamodelFilter metamodelFilter) {
		this.filter = metamodelFilter;
	}

	/**
	 * Should determine whether an element is similar to the other one or not.
	 * 
	 * @param obj1
	 *            an element.
	 * @param obj2
	 *            another element.
	 * @return true if those elements have the same identity.
	 * @throws FactoryException
	 *             on error accessing features.
	 */
	public abstract boolean isSimilar(EObject obj1, EObject obj2) throws FactoryException;

	/**
	 * Convenience method if the checker need to be initialized in some way.
	 * 
	 * @param leftObject
	 *            root of the left model.
	 * @param rightObject
	 *            root of the right model.
	 * @throws FactoryException
	 *             on error accessing features.
	 */
	public abstract void init(EObject leftObject, EObject rightObject) throws FactoryException;

	/**
	 * Convenience method if the checker need to be initialized in some way.
	 * 
	 * @param leftResource
	 *            the left resource.
	 * @param rightResource
	 *            the right resource.
	 * @throws FactoryException
	 *             on error accessing features.
	 */
	public abstract void init(Resource leftResource, Resource rightResource) throws FactoryException;

	/**
	 * Returns an absolute comparison metric between the two given {@link EObject}s.
	 * 
	 * @param obj1
	 *            The first {@link EObject} to compare.
	 * @param obj2
	 *            Second of the {@link EObject}s to compare.
	 * @return An absolute comparison metric. 0 &lt; value &lt; 1.
	 * @throws FactoryException
	 *             Thrown if we cannot compute the content similarity.
	 */
	public abstract double absoluteMetric(EObject obj1, EObject obj2) throws FactoryException;

	/**
	 * If a checker is able to return the matching element quickly it should define that. Not every checker
	 * might be able to provide that !
	 * 
	 * @param obj1
	 *            object to match.
	 * @return corresponding object if found.
	 */
	public EObject fastLookup(EObject obj1) {
		return null;
	}

}
