/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.engine.internal;

import org.eclipse.emf.compare.match.engine.AbstractSimilarityChecker;
import org.eclipse.emf.compare.match.statistic.MetamodelFilter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * A similarity checker leveraging XMI Id's.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class XMIIDSimilarityChecker extends EcoreIDSimilarityChecker {

	/**
	 * Create a new checker.
	 * 
	 * @param filter
	 *            a metamodel filter the checker can use to know whether a feature alwaas has the same value
	 *            or not in the models.
	 * @param fallback
	 *            checker to call if the elements have no ID at all.
	 */
	public XMIIDSimilarityChecker(MetamodelFilter filter, AbstractSimilarityChecker fallback) {
		super(filter, fallback);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String computeID(EObject obj) {
		final Resource eResource = obj.eResource();
		if (eResource instanceof XMIResource) {
			return ((XMIResource)eResource).getID(obj);
		}
		return super.computeID(obj);
	}

}
