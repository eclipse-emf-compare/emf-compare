/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.implications.data;

import java.io.IOException;

import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

public class ImplicationsInputData extends AbstractUMLInputData {
	public Resource getA1Left() throws IOException {
		return loadFromClassLoader("a1/left.uml", createResourceSet()); //$NON-NLS-1$
	}

	public Resource getA1Right() throws IOException {
		return loadFromClassLoader("a1/right.uml", createResourceSet()); //$NON-NLS-1$
	}

	public Resource getA2Left() throws IOException {
		return loadFromClassLoader("a2/left.uml", createResourceSet()); //$NON-NLS-1$
	}

	public Resource getA2Right() throws IOException {
		return loadFromClassLoader("a2/right.uml", createResourceSet()); //$NON-NLS-1$
	}

	public Resource getA3Left() throws IOException {
		return loadFromClassLoader("a3/left.uml", createResourceSet()); //$NON-NLS-1$
	}

	public Resource getA3Right() throws IOException {
		return loadFromClassLoader("a3/right.uml", createResourceSet()); //$NON-NLS-1$
	}

	private ResourceSet createResourceSet() {
		final ResourceSet resourceSet = new ResourceSetImpl();
		UMLResourcesUtil.init(resourceSet);
		return resourceSet;
	}
}
