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

import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.DiagramDiff;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.ecore.EObject;

/**
 * Accessor on a graphical difference.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramIDEDiffAccessorImpl extends DiagramIDEMatchAccessorImpl implements IDiagramDiffAccessor {

	/** A graphical difference. */
	private DiagramDiff fDiff;

	/**
	 * Constructor.
	 * 
	 * @param diff
	 *            A graphical difference.
	 * @param side
	 *            The side where the change is.
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

	public DiagramDiff getDiff() {
		return fDiff;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.accessor.IEObjectAccessor#getEObject()
	 */
	public EObject getEObject() {
		if (fDiff instanceof DiagramDiff) {
			return ((DiagramDiff)fDiff).getView();
		}
		return null;
	}

	@Override
	public EObject getEObject(MergeViewerSide side) {
		EObject obj = getEObject();
		Match eObjectMatch = fComparison.getMatch(obj);
		return getEObject(eObjectMatch, side);
	}

}
