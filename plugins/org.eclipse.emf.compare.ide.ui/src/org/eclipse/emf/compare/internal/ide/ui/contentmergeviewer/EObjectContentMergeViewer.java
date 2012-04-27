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

import java.util.ResourceBundle;

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
 * Specialized {@link ContentMergeViewer} that uses {@link TreeViewer} to display left, right and ancestor
 * {@link EObject}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EObjectContentMergeViewer extends ContentMergeViewer {

	/**
	 * Bundle name of the property file containing all displayed strings.
	 */
	private static final String BUNDLE_NAME = "org.eclipse.emf.compare.internal.ide.ui.contentmergeviewer.EObjectContentMergeViewer"; //$NON-NLS-1$

	/**
	 * The {@link TreeViewer} for the ancestor part.
	 */
	private TreeViewer fAncestorViewer;

	/**
	 * The {@link TreeViewer} for the left part.
	 */
	private TreeViewer fLeftViewer;

	/**
	 * The {@link TreeViewer} for the right part.
	 */
	private TreeViewer fRightViewer;

	/**
	 * The {@link AdapterFactory} used to create {@link AdapterFactoryContentProvider} and
	 * {@link AdapterFactoryLabelProvider} for ancestor, left and right {@link TreeViewer}.
	 */
	private final AdapterFactory fAdapterFactory;

/**
	 * Creates a new {@link EObjectContentMergeViewer} by calling the super constructor with the given
	 * parameters.
	 * <p>
	 * It calls {@link #buildControl(Composite)} as stated in its javadoc.
	 * <p>
	 * It sets a {@link EObjectMergeViewerContentProvider specific}
	 * {@link #setContentProvider(org.eclipse.jface.viewers.IContentProvider) content provider to properly display ancestor, left and right parts.
	 * 
	 * @param parent the parent composite to build the UI in
	 * @param config the {@link CompareConfiguration} 
	 */
	public EObjectContentMergeViewer(Composite parent, CompareConfiguration config) {
		super(SWT.NONE, ResourceBundle.getBundle(BUNDLE_NAME), config);
		fAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		buildControl(parent);
		setContentProvider(new EObjectMergeViewerContentProvider(config));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#createControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createControls(Composite parent) {
		fAncestorViewer = createViewer(parent);
		fLeftViewer = createViewer(parent);
		fRightViewer = createViewer(parent);
	}

	/**
	 * Creates a new {@link TreeViewer} a set the appropriate {@link AdapterFactoryContentProvider} and
	 * {@link AdapterFactoryLabelProvider}.
	 * 
	 * @param parent
	 *            the parent composite of the returned {@link TreeViewer}
	 * @return the new TreeViewer
	 */
	private TreeViewer createViewer(Composite parent) {
		TreeViewer viewer = new TreeViewer(parent);
		viewer.setContentProvider(new AdapterFactoryContentProvider(fAdapterFactory));
		viewer.setLabelProvider(new AdapterFactoryLabelProvider(fAdapterFactory));
		return viewer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#handleResizeAncestor(int, int, int, int)
	 */
	@Override
	protected void handleResizeAncestor(int x, int y, int width, int height) {
		if (width > 0) {
			fAncestorViewer.getControl().setVisible(true);
			fAncestorViewer.getControl().setBounds(x, y, width, height);
		} else {
			fAncestorViewer.getControl().setVisible(false);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#handleResizeLeftRight(int, int, int,
	 *      int, int, int)
	 */
	@Override
	protected void handleResizeLeftRight(int x, int y, int width1, int centerWidth, int width2, int height) {
		fLeftViewer.getControl().setBounds(x, y, width1, height);
		fRightViewer.getControl().setBounds(x + width1 + centerWidth, y, width2, height);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#updateContent(java.lang.Object,
	 *      java.lang.Object, java.lang.Object)
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
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#copy(boolean)
	 */
	@Override
	protected void copy(boolean leftToRight) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#getContents(boolean)
	 */
	@Override
	protected byte[] getContents(boolean left) {
		return null;
	}
}
