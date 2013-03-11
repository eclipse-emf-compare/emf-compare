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
package org.eclipse.emf.compare.diagram.ide.ui.internal;

import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.AbstractMergeViewer;
import org.eclipse.gef.ui.parts.AbstractEditPartViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Abstract graphical merge viewer for comparison of graphical elements.<br>
 * Inspired from org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.StructuredMergeViewer.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 3.0
 */
public abstract class AbstractGraphicalMergeViewer extends AbstractMergeViewer {

	/**
	 * Control of the viewer.
	 */
	private final Control fControl;

	/**
	 * The constructor.
	 * 
	 * @param parent
	 *            The parent composite.
	 * @param side
	 *            The side having to be handle.
	 */
	public AbstractGraphicalMergeViewer(Composite parent, MergeViewerSide side) {
		super(side);
		fControl = createControl(parent);
		hookControl(fControl);
	}

	@Override
	public Control getControl() {
		return fControl;
	}

	/**
	 * It creates and returns the control of the viewer.
	 * 
	 * @param parent
	 *            The parent composite.
	 * @return The control.
	 */
	protected abstract Control createControl(Composite parent);

	/**
	 * Get the encapsulated GEF viewer.
	 * 
	 * @return The graphical viewer.
	 */
	protected abstract AbstractEditPartViewer getGraphicalViewer();

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
	 * @see org.eclipse.jface.viewers.Viewer#setSelection(org.eclipse.jface.viewers.ISelection, boolean)
	 */
	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		getGraphicalViewer().setSelection(selection);
	}

}
