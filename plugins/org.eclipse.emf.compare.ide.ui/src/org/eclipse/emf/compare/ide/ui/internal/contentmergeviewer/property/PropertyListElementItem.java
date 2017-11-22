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
import org.eclipse.emf.edit.provider.IItemLabelProvider;

class PropertyListElementItem extends DiffPropertyItem {

	PropertyListElementItem(EMFCompareConfiguration configuration, IItemLabelProvider itemLabelProvider,
			Diff diff, Object value, MergeViewerSide side) {
		super(configuration, itemLabelProvider, diff, value, side);
	}

	@Override
	protected boolean isModified() {
		if (getDiff() != null) {
			// We won't mark it as modified because then it will be bold, but there will be a box
			// around it so we don't want that.
			return false;
		} else {
			// Otherwise, it's modified if the other side is missing.
			switch (getSide()) {
				case LEFT:
					return getRight() == null;
				case RIGHT:
					return getLeft() == null;
				default:
					return false;
			}
		}
	}

}
