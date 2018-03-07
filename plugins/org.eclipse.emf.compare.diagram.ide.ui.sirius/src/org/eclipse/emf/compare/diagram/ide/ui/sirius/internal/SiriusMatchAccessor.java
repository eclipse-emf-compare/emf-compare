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

import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.DiagramMatchAccessorImpl;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;

public class SiriusMatchAccessor extends DiagramMatchAccessorImpl {
	public SiriusMatchAccessor(Match match, MergeViewerSide side) {
		super(match, side);
	}

	@Override
	public String getType() {
		return "siriuscompare_match"; //$NON-NLS-1$
	}
}
