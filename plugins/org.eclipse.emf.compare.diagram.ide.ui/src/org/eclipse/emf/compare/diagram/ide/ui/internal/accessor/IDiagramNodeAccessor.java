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

import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Input for selection of nodes related to a match.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public interface IDiagramNodeAccessor {

	/**
	 * Get the related comparison.
	 * 
	 * @return The comparison.
	 */
	Comparison getComparison();

	/**
	 * Get the object from the related match of this input and the given side.
	 * 
	 * @param side
	 *            The side to scan.
	 * @return The object.
	 */
	EObject getEObject(MergeViewerSide side);

	/**
	 * Get the diagram from the given side.
	 * 
	 * @param side
	 *            The side t scan.
	 * @return The diagram.
	 */
	Diagram getDiagram(MergeViewerSide side);

	/**
	 * Get the diagram related to this input.
	 * 
	 * @return The diagram.
	 */
	Diagram getOwnedDiagram();

	/**
	 * Get the view related to this input.
	 * 
	 * @return The view.
	 */
	View getOwnedView();

	/**
	 * Get the side considered as the origin version.
	 * 
	 * @return The origin side.
	 */
	MergeViewerSide getOriginSide();

	/**
	 * Get the side of this input.
	 * 
	 * @return The side.
	 */
	MergeViewerSide getSide();

	/**
	 * Get all the differences of the comparison.
	 * 
	 * @return The differences.
	 */
	List<Diff> getAllDiffs();

}
