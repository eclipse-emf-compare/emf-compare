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
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree;

import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.AbstractMergeViewer;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IEObjectAccessor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

class TreeMergeViewer extends AbstractMergeViewer<Tree> {

	private IEObjectAccessor fInput;

	/**
	 * @param parent
	 */
	public TreeMergeViewer(Composite parent, MergeViewerSide side) {
		super(parent, side);
	}

	public Tree getControl() {
		return getStructuredViewer().getTree();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.AbstractMergeViewer#getStructuredViewer()
	 */
	@Override
	protected TreeViewer getStructuredViewer() {
		return (TreeViewer)super.getStructuredViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.AbstractMergeViewer#createStructuredViewer(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected StructuredViewer createStructuredViewer(Composite parent) {
		return new TreeViewer(parent);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer#setSelection(java.lang.Object)
	 */
	public void setSelection(Object input) {
		if (input instanceof IEObjectAccessor) {
			EObject eObject = ((IEObjectAccessor)input).getEObject();
			final Object viewerInput = doGetInput(eObject);
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
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer#setSelection(org.eclipse.emf.compare.Match)
	 */
	public void setSelection(Match match) {
		final EObject eObject;
		switch (getSide()) {
			case ANCESTOR:
				eObject = match.getOrigin();
				break;
			case LEFT:
				eObject = match.getLeft();
				break;
			case RIGHT:
				eObject = match.getRight();
				break;
			default:
				throw new IllegalStateException();
		}
		final ISelection selection;
		if (eObject != null) {
			selection = new StructuredSelection(eObject);
		} else {
			selection = StructuredSelection.EMPTY;
		}
		setSelection(selection);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer#setInput(java.lang.Object)
	 */
	public void setInput(Object input) {
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
}
