/**
 * Copyright (c) 2013, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.diagram.papyrus.tests.merge.data;

import java.io.IOException;

import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Provides input models to the unit tests of the matching by id.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
@SuppressWarnings("nls")
public class EdgeMergeInputData extends DiagramInputData {

	public Resource getA1EdgeChangeLeft() throws IOException {
		return loadFromClassLoader("edges/a1/left.notation");
	}

	public Resource getA1EdgeChangeRight() throws IOException {
		return loadFromClassLoader("edges/a1/right.notation");
	}

	public Resource getA2EdgeChangeLeft() throws IOException {
		return loadFromClassLoader("edges/a2/left.notation");
	}

	public Resource getA2EdgeChangeRight() throws IOException {
		return loadFromClassLoader("edges/a2/right.notation");
	}

	public Resource getA3EdgeChangeLeft() throws IOException {
		return loadFromClassLoader("edges/a3/left.notation");
	}

	public Resource getA3EdgeChangeRight() throws IOException {
		return loadFromClassLoader("edges/a3/right.notation");
	}

	public Resource getA4EdgeChangeLeft() throws IOException {
		return loadFromClassLoader("edges/a4/left.notation");
	}

	public Resource getA4EdgeChangeRight() throws IOException {
		return loadFromClassLoader("edges/a4/right.notation");
	}

	public Resource getA5EdgeChangeLeft() throws IOException {
		return loadFromClassLoader("edges/a5/left.notation");
	}

	public Resource getA5EdgeChangeRight() throws IOException {
		return loadFromClassLoader("edges/a5/right.notation");
	}

	public Resource getA6EdgeChangeLeft() throws IOException {
		return loadFromClassLoader("edges/a6/left.notation");
	}

	public Resource getA6EdgeChangeRight() throws IOException {
		return loadFromClassLoader("edges/a6/right.notation");
	}

	public Resource getA7EdgeChangeLeft() throws IOException {
		return loadFromClassLoader("edges/a7/left.notation");
	}

	public Resource getA7EdgeChangeRight() throws IOException {
		return loadFromClassLoader("edges/a7/right.notation");
	}
}
