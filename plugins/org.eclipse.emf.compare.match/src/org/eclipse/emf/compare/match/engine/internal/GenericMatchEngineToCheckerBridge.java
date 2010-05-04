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
package org.eclipse.emf.compare.match.engine.internal;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.match.internal.statistic.NameSimilarity;
import org.eclipse.emf.compare.match.statistic.MetamodelFilter;
import org.eclipse.emf.ecore.EObject;

/**
 * This class is only here to keep the 1.0 behavior in 1.1 and in that case : clients having specialized
 * content/nameSimilarity in their subclass of generic match engine should still be called.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */

public abstract class GenericMatchEngineToCheckerBridge {
	/**
	 * This will compute the similarity between two {@link EObject}s' contents.
	 * 
	 * @param obj1
	 *            First of the two {@link EObject}s.
	 * @param obj2
	 *            Second of the two {@link EObject}s.
	 * @return <code>double</code> representing the similarity between the two {@link EObject}s' contents. 0
	 *         &lt; value &lt; 1.
	 * @throws FactoryException
	 *             Thrown if we cannot compute the {@link EObject}s' contents similarity metrics.
	 * @see NameSimilarity#contentValue(EObject, MetamodelFilter)
	 */
	public abstract double contentSimilarity(EObject obj1, EObject obj2) throws FactoryException;

	/**
	 * This will compute the similarity between two {@link EObject}s' names.
	 * 
	 * @param obj1
	 *            First of the two {@link EObject}s.
	 * @param obj2
	 *            Second of the two {@link EObject}s.
	 * @return <code>double</code> representing the similarity between the two {@link EObject}s' names. 0 &lt;
	 *         value &lt; 1.
	 * @see NameSimilarity#nameSimilarityMetric(String, String)
	 */
	public abstract double nameSimilarity(EObject obj1, EObject obj2);
}
