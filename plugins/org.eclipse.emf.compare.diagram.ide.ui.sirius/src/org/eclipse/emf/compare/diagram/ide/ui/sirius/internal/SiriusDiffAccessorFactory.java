/*******************************************************************************
 * Copyright (c) 2018 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.sirius.internal;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.factory.DiagramDiffAccessorFactory;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer;

/**
 * Specific accessor for sirius. We can't dispose of the sirius session using generic code from the
 * DiagramCMV.
 * 
 * @author lgoubet
 */
public class SiriusDiffAccessorFactory extends DiagramDiffAccessorFactory {
	@Override
	public ITypedElement createAncestor(AdapterFactory adapterFactory, Object target) {
		return new SiriusDiffAccessor((DiagramDiff)target, IMergeViewer.MergeViewerSide.ANCESTOR);
	}

	@Override
	public ITypedElement createLeft(AdapterFactory adapterFactory, Object target) {
		return new SiriusDiffAccessor((DiagramDiff)target, IMergeViewer.MergeViewerSide.LEFT);
	}

	@Override
	public ITypedElement createRight(AdapterFactory adapterFactory, Object target) {
		return new SiriusDiffAccessor((DiagramDiff)target, IMergeViewer.MergeViewerSide.RIGHT);
	}
}
