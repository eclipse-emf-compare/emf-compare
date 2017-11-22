/*******************************************************************************
 * Copyright (c) 2018 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.property;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;

class PropertyValuePlaceholderItem extends DiffPropertyItem {

	PropertyValuePlaceholderItem(EMFCompareConfiguration configuration, Diff diff, Object value,
			MergeViewerSide side) {
		super(configuration, diff, value, side);
	}

	@Override
	public boolean isInsertionPoint() {
		return true;
	}

}
