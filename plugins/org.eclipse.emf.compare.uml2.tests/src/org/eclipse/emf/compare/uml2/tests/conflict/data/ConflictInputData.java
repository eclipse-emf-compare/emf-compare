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
package org.eclipse.emf.compare.uml2.tests.conflict.data;

import java.io.IOException;

import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.ecore.resource.Resource;

@SuppressWarnings("nls")
public class ConflictInputData extends AbstractUMLInputData {
	public Resource getA1Left() throws IOException {
		return loadFromClassLoader("a1/left.uml");
	}

	public Resource getA1Right() throws IOException {
		return loadFromClassLoader("a1/right.uml");
	}

	public Resource getA1Origin() throws IOException {
		return loadFromClassLoader("a1/origin.uml");
	}
}
