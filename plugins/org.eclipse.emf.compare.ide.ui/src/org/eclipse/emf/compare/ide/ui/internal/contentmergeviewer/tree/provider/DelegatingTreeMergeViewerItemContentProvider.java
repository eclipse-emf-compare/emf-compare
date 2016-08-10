/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree.provider;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.provider.IMergeViewerItemContentProvider;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.provider.IMergeViewerItemProvider;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.provider.IMergeViewerItemProviderConfiguration;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

/**
 * This implementation of {@link ITreeContentProvider} delegates to the {@link IMergeViewerItemProvider}s and
 * {@link IMergeViewerItemContentProvider}s registered via the content merge viewer customization extension
 * point. If no fitting provider is registered, the calls will be delegated to the AdapterFactory.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public class DelegatingTreeMergeViewerItemContentProvider extends AdapterFactoryContentProvider {

	private IMergeViewerItemProviderConfiguration configuration;

	private Comparison comparison;

	/**
	 * Constructor.
	 * 
	 * @param comparison
	 *            the {@link Comparison}.
	 * @param configuration
	 *            the {@link IMergeViewerItemProviderConfiguration}.
	 */
	public DelegatingTreeMergeViewerItemContentProvider(Comparison comparison,
			IMergeViewerItemProviderConfiguration configuration) {
		super(configuration.getAdapterFactory());
		this.comparison = comparison;
		this.configuration = configuration;
	}

	/**
	 * Get the {@link IMergeViewerItemProviderConfiguration}.
	 * 
	 * @return the {@link IMergeViewerItemProviderConfiguration}.
	 */
	protected IMergeViewerItemProviderConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * Get the {@link Comparison}.
	 * 
	 * @return the {@link Comparison}.
	 */
	protected Comparison getComparison() {
		return comparison;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] getElements(Object object) {
		IMergeViewerItemProvider mergeViewerItemProvider = getMergeViewerItemProvider(object);
		if (mergeViewerItemProvider != null) {
			return mergeViewerItemProvider.getMergeViewerItems(object, getConfiguration()).toArray();
		}
		return super.getElements(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasChildren(Object object) {
		IMergeViewerItemContentProvider contentProvider = getContentProvider(object);
		if (contentProvider != null) {
			return contentProvider.hasChildren(object, getConfiguration());
		}
		return super.hasChildren(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] getChildren(Object object) {
		IMergeViewerItemContentProvider contentProvider = getContentProvider(object);
		if (contentProvider != null) {
			return contentProvider.getChildren(object, getConfiguration());
		}
		return super.getChildren(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getParent(Object object) {
		IMergeViewerItemContentProvider contentProvider = getContentProvider(object);
		if (contentProvider != null) {
			return contentProvider.getParent(object, getConfiguration());
		}
		return super.getParent(object);
	}

	/**
	 * Determines the {@link IMergeViewerItemProvider} for the given {@link Object}.
	 * 
	 * @param object
	 *            the {@link Object} for which an {@link IMergeViewerItemProvider} is to be determined.
	 * @return the determined {@link IMergeViewerItemProvider}.
	 */
	protected IMergeViewerItemProvider getMergeViewerItemProvider(Object object) {
		IMergeViewerItemProvider mergeViewerItemProvider = EMFCompareRCPUIPlugin.getDefault()
				.getContentMergeViewerCustomizationRegistry()
				.getBestFittingMergeViewerItemProvider(getComparison(), object);
		return mergeViewerItemProvider;
	}

	/**
	 * Determines the {@link IMergeViewerItemContentProvider} for the given {@link Object}.
	 * 
	 * @param object
	 *            the {@link Object} for which an {@link IMergeViewerItemContentProvider} is to be determined.
	 * @return the determined {@link IMergeViewerItemContentProvider}.
	 */
	protected IMergeViewerItemContentProvider getContentProvider(Object object) {
		IMergeViewerItemContentProvider mergeViewerItemContentProvider = EMFCompareRCPUIPlugin.getDefault()
				.getContentMergeViewerCustomizationRegistry()
				.getBestFittingMergeViewerItemContentProvider(getComparison(), object);
		return mergeViewerItemContentProvider;
	}

}
