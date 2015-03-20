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
public class NodeMergeInputData extends DiagramInputData {

	public Resource getA1NodeChangeLeft() throws IOException {
		return loadFromClassLoader("nodes/a1/left.notation");
	}

	public Resource getA1NodeChangeRight() throws IOException {
		return loadFromClassLoader("nodes/a1/right.notation");
	}

	public Resource getA2NodeChangeLeft() throws IOException {
		return loadFromClassLoader("nodes/a2/left.notation");
	}

	public Resource getA2NodeChangeRight() throws IOException {
		return loadFromClassLoader("nodes/a2/right.notation");
	}

	public Resource getA3NodeChangeLeft() throws IOException {
		return loadFromClassLoader("nodes/a3/left.notation");
	}

	public Resource getA3NodeChangeRight() throws IOException {
		return loadFromClassLoader("nodes/a3/right.notation");
	}

	public Resource getA4NodeChangeLeft() throws IOException {
		return loadFromClassLoader("nodes/a4/left.notation");
	}

	public Resource getA4NodeChangeRight() throws IOException {
		return loadFromClassLoader("nodes/a4/right.notation");
	}

	public Resource getA4NodeChangeOrigin() throws IOException {
		return loadFromClassLoader("nodes/a4/ancestor.notation");
	}
}
