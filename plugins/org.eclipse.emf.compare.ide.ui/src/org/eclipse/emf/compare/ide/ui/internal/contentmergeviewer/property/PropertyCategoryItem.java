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

class PropertyCategoryItem extends PropertyItem {

	private String categoryName;

	PropertyCategoryItem(EMFCompareConfiguration configuration, String categoryName, MergeViewerSide side) {
		super(configuration, null, categoryName, side);
		this.categoryName = categoryName;
	}

	@Override
	protected Object getObject() {
		return categoryName;
	}

	@Override
	protected boolean isMatchingItem(PropertyItem propertyItem) {
		// They match if the other property item is also a category with the same category name.
		return getClass().isInstance(propertyItem) && categoryName.equals(propertyItem.getText());
	}

}
