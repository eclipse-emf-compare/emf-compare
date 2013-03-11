/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree;

import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.AbstractMergeViewer;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.TreeMergeViewer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;

/**
 * Specialized {@link org.eclipse.compare.contentmergeviewer.ContentMergeViewer} that uses
 * {@link org.eclipse.jface.viewers.TreeViewer} to display left, right and ancestor {@link EObject}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TreeContentMergeViewer extends EMFCompareContentMergeViewer {

	/**
	 * Bundle name of the property file containing all displayed strings.
	 */
	private static final String BUNDLE_NAME = TreeContentMergeViewer.class.getName();

	/**
	 * The {@link org.eclipse.emf.common.notify.AdapterFactory} used to create
	 * {@link AdapterFactoryContentProvider} and {@link AdapterFactoryLabelProvider} for ancestor, left and
	 * right {@link org.eclipse.jface.viewers.TreeViewer}.
	 */
	private final ComposedAdapterFactory fAdapterFactory;

	/**
	 * Creates a new {@link TreeContentMergeViewer} by calling the super constructor with the given
	 * parameters.
	 * <p>
	 * It calls {@link #buildControl(Composite)} as stated in its javadoc.
	 * <p>
	 * It sets a {@link TreeContentMergeViewerContentProvider specific}
	 * {@link #setContentProvider(org.eclipse.jface.viewers.IContentProvider) content provider} to properly
	 * display ancestor, left and right parts.
	 * 
	 * @param parent
	 *            the parent composite to build the UI in
	 * @param config
	 *            the {@link CompareConfiguration}
	 */
	public TreeContentMergeViewer(Composite parent, CompareConfiguration config) {
		super(SWT.NONE, ResourceBundle.getBundle(BUNDLE_NAME), config);
		fAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		fAdapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		fAdapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());

		buildControl(parent);
		setContentProvider(new TreeContentMergeViewerContentProvider(config, getComparison()));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		fAdapterFactory.dispose();
		super.handleDispose(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getAncestorMergeViewer()
	 */
	@SuppressWarnings("unchecked")
	// see createMergeViewer() to see it is safe
	@Override
	protected TreeMergeViewer getAncestorMergeViewer() {
		return (TreeMergeViewer)super.getAncestorMergeViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getLeftMergeViewer()
	 */
	@SuppressWarnings("unchecked")
	// see createMergeViewer() to see it is safe
	@Override
	protected TreeMergeViewer getLeftMergeViewer() {
		return (TreeMergeViewer)super.getLeftMergeViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getRightMergeViewer()
	 */
	@SuppressWarnings("unchecked")
	// see createMergeViewer() to see it is safe
	@Override
	protected TreeMergeViewer getRightMergeViewer() {
		return (TreeMergeViewer)super.getRightMergeViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#copyDiff(boolean)
	 */
	@Override
	protected void copyDiff(boolean leftToRight) {
		final IStructuredSelection selection;
		if (leftToRight) {
			selection = (IStructuredSelection)getLeftMergeViewer().getSelection();
		} else {
			selection = (IStructuredSelection)getRightMergeViewer().getSelection();
		}

		Object firstElement = selection.getFirstElement();
		List<Diff> differences = getComparison().getDifferences((EObject)firstElement);

		final Command command = getEditingDomain().createCopyAllNonConflictingCommand(differences,
				leftToRight, EMFCompareRCPPlugin.getDefault().getMergerRegistry());
		getEditingDomain().getCommandStack().execute(command);

		refresh();
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#createMergeViewer(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected AbstractMergeViewer createMergeViewer(final Composite parent, MergeViewerSide side) {
		final TreeMergeViewer mergeTreeViewer = new TreeMergeViewer(parent, side);
		IContentProvider contentProvider = new AdapterFactoryContentProvider(fAdapterFactory);
		mergeTreeViewer.setContentProvider(contentProvider);
		ILabelProvider labelProvider = new AdapterFactoryLabelProvider(fAdapterFactory);
		mergeTreeViewer.setLabelProvider(labelProvider);
		return mergeTreeViewer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#paintCenter(org.eclipse.swt.widgets.Canvas,
	 *      org.eclipse.swt.graphics.GC)
	 */
	@Override
	protected void paintCenter(GC g) {

	}

}
