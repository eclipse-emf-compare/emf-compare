/*******************************************************************************
 * Copyright (c) 2012, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Christian W. Damus - bug 522064
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.profiles.data.dynamic;

import java.io.IOException;

import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.ecore.resource.Resource;

@SuppressWarnings("nls")
public class DynamicProfileInputData extends AbstractUMLInputData {

	public Resource getA1Left() throws IOException {
		return loadFromClassLoader("a1/left.uml");
	}

	public Resource getA1Right() throws IOException {
		return loadFromClassLoader("a1/right.uml");
	}

	public Resource getA2Left() throws IOException {
		return loadFromClassLoader("a2/left.uml");
	}

	public Resource getA2Right() throws IOException {
		return loadFromClassLoader("a2/right.uml");
	}

	public Resource getA3Left() throws IOException {
		return loadFromClassLoader("a3/left.uml");
	}

	public Resource getA3Right() throws IOException {
		return loadFromClassLoader("a3/right.uml");
	}

	public Resource getA3Origin() throws IOException {
		return loadFromClassLoader("a3/origin.uml");
	}

	public Resource getA4Left() throws IOException {
		return loadFromClassLoader("a4/left.uml");
	}

	public Resource getA4Right() throws IOException {
		return loadFromClassLoader("a4/right.uml");
	}

	public Resource getA4Origin() throws IOException {
		return loadFromClassLoader("a4/origin.uml");
	}

	public Resource getA5Left() throws IOException {
		return loadFromClassLoader("a5/left.uml");
	}

	public Resource getA5Right() throws IOException {
		return loadFromClassLoader("a5/right.uml");
	}

	public Resource getA5Origin() throws IOException {
		return loadFromClassLoader("a5/origin.uml");
	}
}
