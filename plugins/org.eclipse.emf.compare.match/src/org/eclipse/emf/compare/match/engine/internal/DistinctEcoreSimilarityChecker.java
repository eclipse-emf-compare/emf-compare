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
import org.eclipse.emf.compare.match.statistic.MetamodelFilter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Specialization of the heuristic based similarity checker making sure before computing complex metrics that
 * both EClasses are the same.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class DistinctEcoreSimilarityChecker extends StatisticBasedSimilarityChecker {
	/**
	 * Create a new checker.
	 * 
	 * @param filter
	 *            a metamodel filter the checker can use to know whether a feature alwaas has the same value
	 *            or not in the models.
	 */
	public DistinctEcoreSimilarityChecker(MetamodelFilter filter) {
		super(filter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSimilar(EObject obj1, EObject obj2) throws FactoryException {
		if (!EcoreUtil.equals(obj1.eClass(), obj2.eClass())) {
			return false;
		}
		return super.isSimilar(obj1, obj2);
	}

}
