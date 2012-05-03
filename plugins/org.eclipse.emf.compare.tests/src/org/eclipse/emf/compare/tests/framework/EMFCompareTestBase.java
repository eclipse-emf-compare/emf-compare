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
package org.eclipse.emf.compare.tests.framework;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

// TODO move asserts to their own utility class
/**
 * This provides a number of utility methods for EMF Compare tests.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFCompareTestBase {
	/**
	 * Returns all proper content of the given resource in the form of a list.
	 * 
	 * @param res
	 *            The resource which content we need.
	 * @return The list of all of the given resource's contained EObjects.
	 */
	protected static List<EObject> getAllProperContent(Resource res) {
		if (res == null) {
			return Lists.newArrayList();
		}

		final Iterator<Object> properContent = EcoreUtil.getAllProperContents(res, false);
		final Iterator<EObject> filter = Iterators.filter(properContent, EObject.class);
		return Lists.newArrayList(filter);
	}
}
