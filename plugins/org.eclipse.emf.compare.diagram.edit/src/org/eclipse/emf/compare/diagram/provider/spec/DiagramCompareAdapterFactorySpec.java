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
package org.eclipse.emf.compare.diagram.provider.spec;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.diagram.provider.DiagramCompareItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

/**
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramCompareAdapterFactorySpec extends DiagramCompareItemProviderAdapterFactory {

	ForwardingDiagramDiffItemProvider fShowItemProvider;

	ForwardingDiagramDiffItemProvider fHideItemProvider;

	ForwardingDiagramDiffItemProvider fNodeChangeItemProvider;

	ForwardingDiagramDiffItemProvider fEdgeChangeItemProvider;

	ForwardingDiagramDiffItemProvider fLabelChangeItemProvider;

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
	public Adapter createLabelChangeAdapter() {
		if (fLabelChangeItemProvider == null) {
			fLabelChangeItemProvider = new LabelChangeItemProviderSpec((ItemProviderAdapter)super
					.createLabelChangeAdapter());
		}
		return fLabelChangeItemProvider;
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
}
