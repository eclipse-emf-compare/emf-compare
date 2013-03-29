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
package org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl;

import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.IEObjectAccessor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class TreeMergeViewer extends StructuredMergeViewer {

	private IEObjectAccessor fInput;

	private TreeViewer fTreeViewer;

	/**
	 * @param parent
	 */
	public TreeMergeViewer(Composite parent, MergeViewerSide side) {
		super(parent, side);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.AbstractMergeViewer#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createControl(Composite parent) {
		fTreeViewer = new TreeViewer(parent);
		return fTreeViewer.getControl();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.ide.ui.internal.contentmergeviewer.AbstractMergeViewer#getStructuredViewer()
	 */
	@Override
	protected TreeViewer getStructuredViewer() {
		return fTreeViewer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#inputChanged(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void inputChanged(Object input, Object oldInput) {
		if (input instanceof IEObjectAccessor) {
			fInput = ((IEObjectAccessor)input);
			EObject eObject = ((IEObjectAccessor)input).getEObject();
			final Object viewerInput = doGetInput(eObject);
			getStructuredViewer().setInput(viewerInput);
			Object selection = viewerInput;
			if (eObject != null) {
				if (eObject.eContainer() == viewerInput) {
					selection = eObject;
				} else if (eObject.eContainer() == null) {
					selection = eObject;
				}
			}
			getStructuredViewer().setSelection(new StructuredSelection(selection));
			getStructuredViewer().expandToLevel(selection, 1);
		} else {
			getStructuredViewer().setInput(null);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IInputProvider#getInput()
	 */
	@Override
	public Object getInput() {
		return fInput;
	}

	/**
	 * Returns either the {@link EObject#eContainer() container} of the given <code>eObject</code> if it is
	 * not null or its {@link EObject#eResource() containing resource} if it is not null.
	 * 
	 * @param eObject
	 *            the object to get the input from.
	 * @return either the {@link EObject#eContainer()} of the given <code>eObject</code> if it is not null or
	 *         its {@link EObject#eResource() containing resource} if it is not null.
	 */
	private static Object doGetInput(EObject eObject) {
		Object input = null;
		if (eObject != null) {
			if (eObject.eContainer() != null) {
				input = eObject.eContainer();
			} else {
				input = eObject.eResource();
			}
		}
		return input;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#refresh()
	 */
	@Override
	public void refresh() {
		fTreeViewer.refresh();
	}
}
