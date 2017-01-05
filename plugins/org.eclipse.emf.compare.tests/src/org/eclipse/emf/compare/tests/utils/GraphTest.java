/*******************************************************************************
 * Copyright (c) 2015, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Alexandra Buzila - bug 478620
 *     Martin Fleck - bug 507177
 *******************************************************************************/
package org.eclipse.emf.compare.tests.utils;

import org.eclipse.emf.compare.graph.IGraph;
import org.eclipse.emf.compare.internal.utils.Graph;

/**
 * We will use this to test the utility methods exposed by the {@link Graph}. The test methods are inherited
 * from {@link AbstractGraphTest}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class GraphTest extends AbstractGraphTest<String> {

	@Override
	protected IGraph<String> createGraph() {
		return new Graph<String>();
	}

	@Override
	protected String toType(String name) {
		return name;
	}

}
