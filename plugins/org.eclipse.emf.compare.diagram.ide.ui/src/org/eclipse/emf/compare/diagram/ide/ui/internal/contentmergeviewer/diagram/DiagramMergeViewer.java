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
package org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram;

import java.util.Collection;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.compare.diagram.ide.ui.GraphicalMergeViewer;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.parts.AbstractEditPartViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

class DiagramMergeViewer extends GraphicalMergeViewer {

	// private IEObjectAccessor fInput;

	private AbstractEditPartViewer fTreeViewer;

	/**
	 * @param parent
	 */
	public DiagramMergeViewer(Composite parent, MergeViewerSide side) {
		super(parent, side);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.MergeViewer#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createControl(Composite parent) {
		fTreeViewer = new DiagramGraphicalViewer();
		return fTreeViewer.getControl();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.AbstractMergeViewer#getStructuredViewer()
	 */
	@Override
	protected AbstractEditPartViewer getGraphicalViewer() {
		return fTreeViewer;
	}

	public EditPart findObjectAtExcluding(Point location, Collection exclusionSet, Conditional conditional) {
		// TODO Auto-generated method stub
		return null;
	}

	// /**
	// * {@inheritDoc}
	// *
	// * @see
	// org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer#setInput(java.lang.Object)
	// */
	// @Override
	// public void setInput(Object input) {
	// if (input instanceof IEObjectAccessor) {
	// fInput = ((IEObjectAccessor)input);
	// EObject eObject = ((IEObjectAccessor)input).getEObject();
	// final Object viewerInput = doGetInput(eObject);
	// getGraphicalViewer().setInput(viewerInput);
	// Object selection = viewerInput;
	// if (eObject != null) {
	// if (eObject.eContainer() == viewerInput) {
	// selection = eObject;
	// } else if (eObject.eContainer() == null) {
	// selection = eObject;
	// }
	// }
	// getGraphicalViewer().setSelection(new StructuredSelection(selection));
	// getGraphicalViewer().expandToLevel(selection, 1);
	// } else {
	// getGraphicalViewer().setInput(null);
	// }
	// }

	// /**
	// * {@inheritDoc}
	// *
	// * @see org.eclipse.jface.viewers.IInputProvider#getInput()
	// */
	// @Override
	// public Object getInput() {
	// return fInput;
	// }

	// /**
	// * Returns either the {@link EObject#eContainer() container} of the given <code>eObject</code> if it is
	// * not null or its {@link EObject#eResource() containing resource} if it is not null.
	// *
	// * @param eObject
	// * the object to get the input from.
	// * @return either the {@link EObject#eContainer()} of the given <code>eObject</code> if it is not null
	// or
	// * its {@link EObject#eResource() containing resource} if it is not null.
	// */
	// private static Object doGetInput(EObject eObject) {
	// Object input = null;
	// if (eObject != null) {
	// if (eObject.eContainer() != null) {
	// input = eObject.eContainer();
	// } else {
	// input = eObject.eResource();
	// }
	// }
	// return input;
	// }

	// /**
	// * {@inheritDoc}
	// *
	// * @see org.eclipse.jface.viewers.Viewer#refresh()
	// */
	// @Override
	// public void refresh() {
	// fTreeViewer.refresh();
	// }
}
