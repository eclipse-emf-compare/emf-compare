/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.diagramdiff.provider;

import org.eclipse.emf.common.notify.Adapter;

/**
 * Extension of DiagramdiffItemProviderAdapterFactory.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public class BusinessDiagramdiffItemProviderAdapterFactory extends DiagramdiffItemProviderAdapterFactory {
	@Override
	public Adapter createDiagramMoveNodeAdapter() {
		if (diagramMoveNodeItemProvider == null) {
			diagramMoveNodeItemProvider = new BusinessDiagramMoveNodeItemProvider(this);
		}
		return diagramMoveNodeItemProvider;
	}

	@Override
	public Adapter createDiagramEdgeChangeAdapter() {
		if (diagramEdgeChangeItemProvider == null) {
			diagramEdgeChangeItemProvider = new BusinessDiagramEdgeChangeItemProvider(this);
		}
		return diagramEdgeChangeItemProvider;
	}

	@Override
	public Adapter createDiagramHideElementAdapter() {
		if (diagramHideElementItemProvider == null) {
			diagramHideElementItemProvider = new BusinessDiagramHideElementItemProvider(this);
		}
		return diagramHideElementItemProvider;
	}

	@Override
	public Adapter createDiagramShowElementAdapter() {
		if (diagramShowElementItemProvider == null) {
			diagramShowElementItemProvider = new BusinessDiagramShowElementItemProvider(this);
		}
		return diagramShowElementItemProvider;
	}

	@Override
	public Adapter createDiagramLabelChangeAdapter() {
		if (diagramLabelChangeItemProvider == null) {
			diagramLabelChangeItemProvider = new BusinessDiagramLabelChangeItemProvider(this);
		}
		return diagramLabelChangeItemProvider;
	}

}
