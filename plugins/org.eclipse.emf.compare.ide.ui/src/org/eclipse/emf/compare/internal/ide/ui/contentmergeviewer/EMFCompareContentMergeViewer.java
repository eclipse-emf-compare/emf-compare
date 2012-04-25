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
package org.eclipse.emf.compare.internal.ide.ui.contentmergeviewer;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.ContentMergeViewer;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.internal.ide.ui.contentmergeviewer.provider.IEObjectAccessor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareContentMergeViewer extends ContentMergeViewer {

	private TreeViewer fAncestorViewer;

	private TreeViewer fLeftViewer;

	private TreeViewer fRightViewer;

	private final AdapterFactory fAdapterFactory;

	/**
	 * @param parent
	 * @param config
	 */
	public EMFCompareContentMergeViewer(Composite parent, CompareConfiguration config) {
		super(SWT.NONE, null, config);
		fAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		buildControl(parent);
		setContentProvider(new EMFCompareMergeViewerContentProvider(config));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.compare.contentmergeviewer.ContentMergeViewer#createControls(org.eclipse.swt.widgets.Composite
	 * )
	 */
	@Override
	protected void createControls(Composite parent) {
		fAncestorViewer = createViewer(parent);
		fLeftViewer = createViewer(parent);
		fRightViewer = createViewer(parent);
	}

	private TreeViewer createViewer(Composite parent) {
		TreeViewer viewer = new TreeViewer(parent);
		viewer.setContentProvider(new AdapterFactoryContentProvider(fAdapterFactory));
		viewer.setLabelProvider(new AdapterFactoryLabelProvider(fAdapterFactory));
		return viewer;
	}

	@Override
	protected void handleResizeAncestor(int x, int y, int width, int height) {
		if (width > 0) {
			fAncestorViewer.getControl().setVisible(true);
			fAncestorViewer.getControl().setBounds(x, y, width, height);
		} else {
			fAncestorViewer.getControl().setVisible(false);
		}
	}

	@Override
	protected void handleResizeLeftRight(int x, int y, int width1, int centerWidth, int width2, int height) {
		fLeftViewer.getControl().setBounds(x, y, width1, height);
		fRightViewer.getControl().setBounds(x + width1 + centerWidth, y, width2, height);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#updateContent(java.lang.Object,
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void updateContent(Object ancestor, Object left, Object right) {
		if (ancestor instanceof IEObjectAccessor) {
			EObject ancestorEObject = ((IEObjectAccessor)ancestor).getEObject();
			fAncestorViewer.setInput(doGetInput(ancestorEObject));
		} else {
			fAncestorViewer.setInput(null);
		}

		if (left instanceof IEObjectAccessor) {
			EObject leftEObject = ((IEObjectAccessor)left).getEObject();
			fLeftViewer.setInput(doGetInput(leftEObject));
		} else {
			fLeftViewer.setInput(null);
		}

		if (right instanceof IEObjectAccessor) {
			EObject rightEObject = ((IEObjectAccessor)right).getEObject();
			fRightViewer.setInput(doGetInput(rightEObject));
		} else {
			fRightViewer.setInput(null);
		}

	}

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

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#copy(boolean)
	 */
	@Override
	protected void copy(boolean leftToRight) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#getContents(boolean)
	 */
	@Override
	protected byte[] getContents(boolean left) {
		return left ? "Left".getBytes() : "Right".getBytes();
	}

}
