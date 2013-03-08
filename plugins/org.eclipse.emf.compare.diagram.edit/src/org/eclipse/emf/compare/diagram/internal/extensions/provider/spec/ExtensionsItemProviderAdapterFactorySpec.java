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
package org.eclipse.emf.compare.diagram.internal.extensions.provider.spec;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.diagram.internal.extensions.provider.ExtensionsItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

/**
 * Specific item provider adapter factory to use the {@link ForwardingDiagramDiffItemProvider} providers.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class ExtensionsItemProviderAdapterFactorySpec extends ExtensionsItemProviderAdapterFactory {

	/** Item provider used for the Show change. */
	ForwardingDiagramDiffItemProvider fShowItemProvider;

	/** Item provider used for the Hide change. */
	ForwardingDiagramDiffItemProvider fHideItemProvider;

	/** Item provider used for the Node change. */
	NodeChangeItemProviderSpec fNodeChangeItemProvider;

	/** Item provider used for the Edge change. */
	ForwardingDiagramDiffItemProvider fEdgeChangeItemProvider;

	/** Item provider used for the coordinates change. */
	ForwardingDiagramDiffItemProvider fCoordinatesChangeItemProvider;

	@Override
	public Adapter createEdgeChangeAdapter() {
		if (fEdgeChangeItemProvider == null) {
			fEdgeChangeItemProvider = new ForwardingDiagramDiffItemProvider((ItemProviderAdapter)super
					.createEdgeChangeAdapter());
		}
		return fEdgeChangeItemProvider;
	}

	@Override
	public Adapter createHideAdapter() {
		if (fHideItemProvider == null) {
			fHideItemProvider = new ForwardingDiagramDiffItemProvider((ItemProviderAdapter)super
					.createHideAdapter());
		}
		return fHideItemProvider;
	}

	@Override
	public Adapter createNodeChangeAdapter() {
		if (fNodeChangeItemProvider == null) {
			fNodeChangeItemProvider = new NodeChangeItemProviderSpec((ItemProviderAdapter)super
					.createNodeChangeAdapter());
		}

		return fNodeChangeItemProvider;
	}

	@Override
	public Adapter createShowAdapter() {
		if (fShowItemProvider == null) {
			fShowItemProvider = new ForwardingDiagramDiffItemProvider((ItemProviderAdapter)super
					.createShowAdapter());
		}
		return fShowItemProvider;
	}

	@Override
	public Adapter createCoordinatesChangeAdapter() {
		if (fCoordinatesChangeItemProvider == null) {
			fCoordinatesChangeItemProvider = new ForwardingDiagramDiffItemProvider((ItemProviderAdapter)super
					.createCoordinatesChangeAdapter());
		}
		return fCoordinatesChangeItemProvider;
	}
}
