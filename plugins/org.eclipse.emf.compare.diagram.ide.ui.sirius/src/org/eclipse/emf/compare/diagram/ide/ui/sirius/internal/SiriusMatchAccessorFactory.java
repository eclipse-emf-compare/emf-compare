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
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.factory.DiagramMatchAccessorFactory;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer;

public class SiriusMatchAccessorFactory extends DiagramMatchAccessorFactory {
	@Override
	public ITypedElement createAncestor(AdapterFactory adapterFactory, Object target) {
		return new SiriusMatchAccessor((Match)target, IMergeViewer.MergeViewerSide.ANCESTOR);
	}

	@Override
	public ITypedElement createLeft(AdapterFactory adapterFactory, Object target) {
		return new SiriusMatchAccessor((Match)target, IMergeViewer.MergeViewerSide.LEFT);
	}

	@Override
	public ITypedElement createRight(AdapterFactory adapterFactory, Object target) {
		return new SiriusMatchAccessor((Match)target, IMergeViewer.MergeViewerSide.RIGHT);
	}
}
