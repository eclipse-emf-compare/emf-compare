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

abstract class DiffPropertyItem extends PropertyItem {

	private Diff diff;

	private Object value;

	DiffPropertyItem(EMFCompareConfiguration configuration, Diff diff, Object value,
			MergeViewerSide side) {
		super(configuration, null, "", side); //$NON-NLS-1$
		this.diff = diff;
		this.value = value;
	}

	DiffPropertyItem(EMFCompareConfiguration configuration, IItemLabelProvider itemLabelProvider,
			Diff diff, Object value, MergeViewerSide side) {
		super(configuration, itemLabelProvider.getImage(value), itemLabelProvider.getText(value), side);
		this.diff = diff;
		this.value = value;
	}

	@Override
	protected boolean isMatchingItem(PropertyItem propertyItem) {
		return getDiff() == propertyItem.getDiff() || isMatchingValue(getObject(), propertyItem.getObject());
	}

	@Override
	protected Object getObject() {
		return value;
	}

	@Override
	public Diff getDiff() {
		return diff;
	}

	@Override
	public String getText(Object object) {
		return ""; //$NON-NLS-1$
	}

	@Override
	public Object getImage(Object object) {
		return null;
	}

	@Override
	protected String getPropertyText() {
		return text;
	}

	@Override
	protected Object getPropertyImage() {
		return image;
	}

}
