/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.internal.accessor;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.DiagramDiff;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.MergeViewer.MergeViewerSide;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramIDEDiffAccessorImpl extends DiagramIDEMatchAccessorImpl implements IDiagramDiffAccessor {

	private DiagramDiff fDiff;

	/**
	 * @param diff
	 * @param side
	 */
	public DiagramIDEDiffAccessorImpl(DiagramDiff diff, MergeViewerSide side) {
		super(diff.getMatch(), side);
		this.fDiff = diff;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.DiagramIDEMatchAccessorImpl#getType()
	 */
	@Override
	public String getType() {
		return DiagramContentMergeViewerConstants.DIFF_NODE_TYPE;
	}

	public Comparison getComparison() {
		return fComparison;
	}

	public DiagramDiff getDiff() {
		return fDiff;
	}

	public EObject getEObject() {
		if (fDiff instanceof DiagramDiff) {
			return ((DiagramDiff)fDiff).getView();
		}
		return null;
	}

	public Diagram getDiagram() {
		EObject obj = getEObject();
		return getDiagram(obj);
	}

	@Override
	public EObject getEObject(MergeViewerSide side) {
		EObject obj = getEObject();
		Match eObjectMatch = fComparison.getMatch(obj);
		return getEObject(eObjectMatch, side);
	}

	@Override
	public Diagram getDiagram(MergeViewerSide side) {
		EObject obj = getEObject(side);
		if (obj != null) {
			return getDiagram(obj);
		} else {
			Diagram diagram = getDiagram();
			if (diagram != null) {
				Match diagramMatch = fComparison.getMatch(diagram);
				return (Diagram)getEObject(diagramMatch, side);
			}
		}
		return null;
	}

}
