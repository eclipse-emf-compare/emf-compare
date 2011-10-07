/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.diagramdiff;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.gmf.runtime.notation.Edge;

/**
 * Overriding of {@link DiagramEdgeChange} for specific interface.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public interface BusinessDiagramEdgeChange extends DiagramEdgeChange, BusinessDiffExtension {

	/**
	 * Returns the left edge.
	 * 
	 * @return The left Edge.
	 */
	Edge getLeftEdge();

	/**
	 * Returns the right edge.
	 * 
	 * @return The right Edge.
	 */
	Edge getRightEdge();

	/**
	 * Returns the related EAttribute.
	 * 
	 * @return The EAttribute.
	 */
	EAttribute getAttribute();

}
