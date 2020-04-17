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
package org.eclipse.emf.compare.ide.ui.tests.contentmergeviewer.util.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This will provide the input model for undo and redo tests.
 * 
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 */
@SuppressWarnings("nls")
public class UtilInputData extends AbstractInputData {
	/**
	 * The left model of "nodes1".
	 * 
	 * @return The loaded left model.
	 * @throws IOException
	 *             Thrown if we could not access either this class's resource, or the file towards which
	 *             <code>string</code> points.
	 */
	public Resource getNodes1Left() throws IOException {
		return loadFromClassLoader("nodes1/left.nodes");
	}

	/**
	 * The right model of "nodes1".
	 * 
	 * @return The loaded right model.
	 * @throws IOException
	 *             Thrown if we could not access either this class's resource, or the file towards which
	 *             <code>string</code> points.
	 */
	public Resource getNodes1Origin() throws IOException {
		return loadFromClassLoader("nodes1/origin.nodes");
	}

	/**
	 * The origin model of "nodes1".
	 * 
	 * @return The loaded origin model.
	 * @throws IOException
	 *             Thrown if we could not access either this class's resource, or the file towards which
	 *             <code>string</code> points.
	 */
	public Resource getNodes1Right() throws IOException {
		return loadFromClassLoader("nodes1/right.nodes");
	}
}
