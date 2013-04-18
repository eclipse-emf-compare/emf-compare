/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.internal.accessor;

import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;

/**
 * Input for selection of elements related to a difference.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public interface IDiagramDiffAccessor extends IDiagramNodeAccessor {

	/**
	 * Get the difference related to this input.
	 * 
	 * @return The graphical difference.
	 */
	DiagramDiff getDiff();

}
