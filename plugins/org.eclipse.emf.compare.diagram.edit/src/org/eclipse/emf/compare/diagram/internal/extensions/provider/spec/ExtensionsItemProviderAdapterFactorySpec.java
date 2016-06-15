/*******************************************************************************
 * Copyright (c) 2013, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.internal.extensions.provider.spec;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.diagram.internal.extensions.provider.ExtensionsItemProviderAdapterFactory;
import org.eclipse.emf.compare.provider.IItemDescriptionProvider;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.ISemanticObjectLabelProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

/**
 * Specific item provider adapter factory to use the {@link ForwardingDiagramDiffItemProvider} providers.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class ExtensionsItemProviderAdapterFactorySpec extends ExtensionsItemProviderAdapterFactory {

	/** Item provider used for the Show change. */
	ShowItemProviderSpec fShowItemProvider;

	/** Item provider used for the Hide change. */
	HideItemProviderSpec fHideItemProvider;

	/** Item provider used for the Node change. */
	NodeChangeItemProviderSpec fNodeChangeItemProvider;

	/** Item provider used for the Edge change. */
	EdgeChangeItemProviderSpec fEdgeChangeItemProvider;

	/** Item provider used for the coordinates change. */
	CoordinatesChangeItemProviderSpec fCoordinatesChangeItemProvider;

	/** Item provider used for the diagram change. */
	DiagramChangeItemProviderSpec fDiagramChangeItemProvider;

	/**
	 * Constructor.
	 */
	public ExtensionsItemProviderAdapterFactorySpec() {
		super();
		supportedTypes.add(IItemStyledLabelProvider.class);
		supportedTypes.add(IItemDescriptionProvider.class);
		supportedTypes.add(ISemanticObjectLabelProvider.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.provider.ExtensionsItemProviderAdapterFactory#createEdgeChangeAdapter()
	 */
	@Override
	public Adapter createEdgeChangeAdapter() {
		if (fEdgeChangeItemProvider == null) {
			fEdgeChangeItemProvider = new EdgeChangeItemProviderSpec(
					(ItemProviderAdapter)super.createEdgeChangeAdapter());
		}
		return fEdgeChangeItemProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.provider.ExtensionsItemProviderAdapterFactory#createHideAdapter()
	 */
	@Override
	public Adapter createHideAdapter() {
		if (fHideItemProvider == null) {
			fHideItemProvider = new HideItemProviderSpec((ItemProviderAdapter)super.createHideAdapter());
		}
		return fHideItemProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.provider.ExtensionsItemProviderAdapterFactory#createNodeChangeAdapter()
	 */
	@Override
	public Adapter createNodeChangeAdapter() {
		if (fNodeChangeItemProvider == null) {
			fNodeChangeItemProvider = new NodeChangeItemProviderSpec(
					(ItemProviderAdapter)super.createNodeChangeAdapter());
		}

		return fNodeChangeItemProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.provider.ExtensionsItemProviderAdapterFactory#createShowAdapter()
	 */
	@Override
	public Adapter createShowAdapter() {
		if (fShowItemProvider == null) {
			fShowItemProvider = new ShowItemProviderSpec((ItemProviderAdapter)super.createShowAdapter());
		}
		return fShowItemProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.provider.ExtensionsItemProviderAdapterFactory#createCoordinatesChangeAdapter()
	 */
	@Override
	public Adapter createCoordinatesChangeAdapter() {
		if (fCoordinatesChangeItemProvider == null) {
			fCoordinatesChangeItemProvider = new CoordinatesChangeItemProviderSpec(
					(ItemProviderAdapter)super.createCoordinatesChangeAdapter());
		}
		return fCoordinatesChangeItemProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.provider.ExtensionsItemProviderAdapterFactory#createDiagramChangeAdapter()
	 */
	@Override
	public Adapter createDiagramChangeAdapter() {
		if (fDiagramChangeItemProvider == null) {
			fDiagramChangeItemProvider = new DiagramChangeItemProviderSpec(
					(ItemProviderAdapter)super.createDiagramChangeAdapter());
		}
		return fDiagramChangeItemProvider;
	}
}
