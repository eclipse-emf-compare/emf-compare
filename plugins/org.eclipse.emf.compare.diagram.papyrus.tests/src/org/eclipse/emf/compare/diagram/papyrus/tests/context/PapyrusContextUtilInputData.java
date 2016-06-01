/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.context;

import java.io.IOException;

import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class PapyrusContextUtilInputData extends DiagramInputData {

	public Resource getPapyrusLeft() throws IOException {
		return loadFromClassLoader("data/papyrus/left.di");
	}

	public Resource getPapyrusRight() throws IOException {
		return loadFromClassLoader("data/papyrus/right.di");
	}

	public Resource getEcoreLeft() throws IOException {
		return loadFromClassLoader("data/ecore/left.ecore");
	}

	public Resource getEcoreRight() throws IOException {
		return loadFromClassLoader("data/ecore/right.ecore");
	}
}