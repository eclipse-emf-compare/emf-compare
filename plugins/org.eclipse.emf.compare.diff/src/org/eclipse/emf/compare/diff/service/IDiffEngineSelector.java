/*******************************************************************************
 * Copyright (c) 2009, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.service;

import java.util.List;

/**
 * This can be subclassed for clients to display a diff engine selector to users when more than a single diff
 * engine are defined on the same file extension.
 * 
 * @author <a href="laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 1.0
 */
public interface IDiffEngineSelector {
	/**
	 * Implements the actual engine selection.
	 * 
	 * @param engines
	 *            List of all candidates.
	 * @return Engine the user selected.
	 */
	DiffEngineDescriptor selectDiffEngine(List<DiffEngineDescriptor> engines);
}
