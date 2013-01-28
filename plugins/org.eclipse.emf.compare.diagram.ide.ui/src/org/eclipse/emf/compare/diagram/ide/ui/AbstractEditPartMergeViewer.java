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
package org.eclipse.emf.compare.diagram.ide.ui;

import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.gef.ui.parts.AbstractEditPartViewer;
import org.eclipse.jface.viewers.ISelection;

/**
 * Abstract viewer for comparison of graphical elements.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 3.0
 */
public abstract class AbstractEditPartMergeViewer extends AbstractEditPartViewer {

	/** The side that manages this viewer. */
	private final MergeViewerSide fSide;

	/**
	 * Constructor.
	 * 
	 * @param side
	 *            The side having to be handle.
	 */
	public AbstractEditPartMergeViewer(MergeViewerSide side) {
		fSide = side;
	}

	/**
	 * Set the input of the viewer.
	 * 
	 * @param object
	 *            The input.
	 */
	public abstract void setInput(Object object);

	/**
	 * Returns the wrapped {@link AbstractEditPartViewer}.
	 * 
	 * @return The viewer.
	 */
	protected abstract AbstractEditPartViewer getGraphicalViewer();

	/**
	 * {@inheritDoc}
	 */
	public MergeViewerSide getSide() {
		return fSide;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	@Override
	public ISelection getSelection() {
		return getGraphicalViewer().getSelection();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider(org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void setSelection(ISelection selection) {
		getGraphicalViewer().setSelection(selection);
	}

}
