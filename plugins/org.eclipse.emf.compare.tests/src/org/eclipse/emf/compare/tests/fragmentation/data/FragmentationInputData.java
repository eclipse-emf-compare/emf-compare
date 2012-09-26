/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.fragmentation.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

// We'll use a resource set for these tests
@SuppressWarnings("nls")
public class FragmentationInputData extends AbstractInputData {
	public Resource getControlLeft() throws IOException {
		return loadFromClassLoader("control/left.nodes", new ResourceSetImpl());
	}

	public Resource getControlOrigin() throws IOException {
		return loadFromClassLoader("control/origin.nodes", new ResourceSetImpl());
	}

	public Resource getControlRight() throws IOException {
		return loadFromClassLoader("control/right.nodes", new ResourceSetImpl());
	}

	public Resource getDeletedRootLeft() throws IOException {
		return loadFromClassLoader("deletedroot/left.nodes", new ResourceSetImpl());
	}

	public Resource getDeletedRootOrigin() throws IOException {
		return loadFromClassLoader("deletedroot/origin.nodes", new ResourceSetImpl());
	}

	public Resource getDeletedRootRight() throws IOException {
		return loadFromClassLoader("deletedroot/right.nodes", new ResourceSetImpl());
	}
}
