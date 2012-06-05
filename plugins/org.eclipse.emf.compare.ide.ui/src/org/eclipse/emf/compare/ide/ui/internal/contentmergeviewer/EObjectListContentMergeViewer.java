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
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer;

import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.ContentMergeViewer;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IManyStructuralFeatureAccessor;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EObjectListContentMergeViewer extends ContentMergeViewer {

	/**
	 * Bundle name of the property file containing all displayed strings.
	 */
	private static final String BUNDLE_NAME = "org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EObjectListContentMergeViewer"; //$NON-NLS-1$

	private TableViewer fAncestorViewer;

	private TableViewer fLeftViewer;

	private TableViewer fRightViewer;

	private final AdapterFactory fAdapterFactory;

	/**
	 * Call the super constructor.
	 * 
	 * @see EObjectListContentMergeViewer
	 */
	protected EObjectListContentMergeViewer(Composite parent, CompareConfiguration config) {
		super(SWT.NONE, ResourceBundle.getBundle(BUNDLE_NAME), config);
		fAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		buildControl(parent);
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
	private TableViewer createViewer(Composite parent) {
		TableViewer viewer = new TableViewer(parent);
		viewer.setContentProvider(new ArrayContentProvider());
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
	protected void handleResizeLeftRight(int x, int y, int leftWidth, int centerWidth, int rightWidth,
			int height) {
		fLeftViewer.getControl().setBounds(x, y, leftWidth, height);
		fRightViewer.getControl().setBounds(x + leftWidth + centerWidth, y, rightWidth, height);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#updateContent(java.lang.Object,
	 *      java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void updateContent(Object ancestor, Object left, Object right) {
		doUpdateContent(ancestor, fAncestorViewer);
		doUpdateContent(left, fLeftViewer);
		doUpdateContent(right, fRightViewer);
	}

	private static void doUpdateContent(Object object, StructuredViewer viewer) {
		if (object instanceof IManyStructuralFeatureAccessor<?>) {
			final IManyStructuralFeatureAccessor<?> manyFeatureAccessor = (IManyStructuralFeatureAccessor<?>)object;
			final List<?> values = manyFeatureAccessor.getValues();
			viewer.setInput(values);
			final Object value = manyFeatureAccessor.getValue();
			if (values.contains(value)) {
				viewer.setSelection(new StructuredSelection(value), true);
			} else {
				viewer.setSelection(StructuredSelection.EMPTY, true);
			}
		} else {
			viewer.setInput(null);
		}
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
