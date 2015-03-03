/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.match.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

@SuppressWarnings("nls")
public class MatchInputData extends AbstractInputData {

	public Resource getProxyMatchingA1Left() throws IOException {
		return loadFromClassLoader("proxy/a1/left.nodes", new ResourceSetImpl());
	}

	public Resource getProxyMatchingA1Right() throws IOException {
		return loadFromClassLoader("proxy/a1/right.nodes", new ResourceSetImpl());
	}

	public Resource getProxyMatchingA2Left() throws IOException {
		return loadFromClassLoader("proxy/a2/left.nodes", new ResourceSetImpl());
	}

	public Resource getProxyMatchingA2Right() throws IOException {
		return loadFromClassLoader("proxy/a2/right.nodes", new ResourceSetImpl());
	}
}
