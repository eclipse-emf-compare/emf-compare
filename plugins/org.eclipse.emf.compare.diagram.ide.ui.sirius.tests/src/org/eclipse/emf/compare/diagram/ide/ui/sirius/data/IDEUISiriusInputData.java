/*******************************************************************************
 * Copyright (c) 2020 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.sirius.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This will provide the input model, their representation and other useful methods for all our Sirius tests.
 *
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 */
public class IDEUISiriusInputData extends AbstractInputData {

	/**
	 * The left model of "nodes1".
	 *
	 * @return The loaded left model.
	 * @throws IOException
	 *             Thrown if we could not access either this class's resource, or the file towards which
	 *             <code>string</code> points.
	 */
	public Resource getNodes1Left() throws IOException {
		return loadFromClassLoader("nodes1/left/left.nodes");
	}

	/**
	 * The representation of the "nodes1" left model.
	 *
	 * @return The loaded left representation.
	 * @throws IOException
	 *             Thrown if we could not access either this class's resource, or the file towards which
	 *             <code>string</code> points.
	 */
	public Resource getNodes1LeftRepresentation() throws IOException {
		return loadFromClassLoader("nodes1/left/left.aird");
	}

	/**
	 * The right model of "nodes1".
	 *
	 * @return The loaded right model.
	 * @throws IOException
	 *             Thrown if we could not access either this class's resource, or the file towards which
	 *             <code>string</code> points.
	 */
	public Resource getNodes1Right() throws IOException {
		return loadFromClassLoader("nodes1/right/right.nodes");
	}

	/**
	 * The representation of the "nodes1" right model.
	 *
	 * @return The loaded right representation.
	 * @throws IOException
	 *             Thrown if we could not access either this class's resource, or the file towards which
	 *             <code>string</code> points.
	 */
	public Resource getNodes1RightRepresentation() throws IOException {
		return loadFromClassLoader("nodes1/right/right.aird");
	}
}
