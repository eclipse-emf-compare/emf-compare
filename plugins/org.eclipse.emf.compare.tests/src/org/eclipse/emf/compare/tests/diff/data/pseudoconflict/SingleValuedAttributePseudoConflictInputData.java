/*******************************************************************************
 * Copyright (c) 2019 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent Goubet - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.diff.data.pseudoconflict;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

@SuppressWarnings("nls")
public class SingleValuedAttributePseudoConflictInputData extends AbstractInputData {
	public Resource getSingleValueAttributePseudoConflictLeft() throws IOException {
		return loadFromClassLoader("left.nodes");
	}

	public Resource getSingleValueAttributePseudoConflictRight() throws IOException {
		return loadFromClassLoader("right.nodes");
	}

	public Resource getSingleValueAttributePseudoConflictOrigin() throws IOException {
		return loadFromClassLoader("origin.nodes");
	}
}
