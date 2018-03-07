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

import org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.DiagramDiffAccessorImpl;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer;

public class SiriusDiffAccessor extends DiagramDiffAccessorImpl {
	public SiriusDiffAccessor(DiagramDiff diff, IMergeViewer.MergeViewerSide side) {
		super(diff, side);
	}

	@Override
	public String getType() {
		return "siriuscompare_diff"; //$NON-NLS-1$
	}
}
