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

import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

class RootPropertyItem extends PropertyItem {

	private Object object;

	RootPropertyItem(EMFCompareConfiguration configuration, IItemLabelProvider itemLabelProvider,
			Object object, MergeViewerSide side) {
		super(configuration, itemLabelProvider.getImage(object), itemLabelProvider.getText(object), side);
		this.object = object;
	}

	@Override
	protected Object getObject() {
		return object;
	}

	@Override
	protected boolean isMatchingItem(PropertyItem propertyItem) {
		// A root item always matches another root item.
		return getClass().isInstance(propertyItem);
	}

}
