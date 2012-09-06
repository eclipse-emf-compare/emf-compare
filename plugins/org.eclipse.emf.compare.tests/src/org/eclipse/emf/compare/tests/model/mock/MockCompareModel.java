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
package org.eclipse.emf.compare.tests.model.mock;

import java.io.IOException;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This class will be used to create a testing instance of a comparison model.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("nls")
public class MockCompareModel extends AbstractInputData {
	public Resource getLeftModel() throws IOException {
		return loadFromClassloader("extlibraryLeft.ecore");
	}

	public Resource getRightModel() throws IOException {
		return loadFromClassloader("extlibraryRight.ecore");
	}

	public Resource getOriginModel() throws IOException {
		return loadFromClassloader("extlibraryOrigin.ecore");
	}

	public Comparison createComparisonModel() throws IOException {
		final IComparisonScope scope = EMFCompare.createDefaultScope(getLeftModel(), getRightModel(),
				getOriginModel());
		return EMFCompare.newComparator(scope).compare();
	}
}
