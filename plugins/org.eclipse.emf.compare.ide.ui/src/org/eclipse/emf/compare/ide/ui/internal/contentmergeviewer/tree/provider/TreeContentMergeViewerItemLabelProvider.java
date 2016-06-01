/*******************************************************************************
 * Copyright (c) 2012, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - extracted into own class
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree.provider;

import java.util.ResourceBundle;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Label Provider for {@link IMergeViewerItem}s.
 */
public class TreeContentMergeViewerItemLabelProvider extends AdapterFactoryLabelProvider {

	private final MergeViewerSide side;

	private ResourceBundle resourceBundle;

	/**
	 * Constructor.
	 * 
	 * @param resourceBundle
	 *            the {@link ResourceBundle}. Used for error messages.
	 * @param adapterFactory
	 *            the {@link AdapterFactory}.
	 * @param side
	 *            the {@link MergeViewerSide}.
	 */
	public TreeContentMergeViewerItemLabelProvider(ResourceBundle resourceBundle,
			AdapterFactory adapterFactory, MergeViewerSide side) {
		super(adapterFactory);
		this.side = side;
		this.resourceBundle = resourceBundle;
	}

	@Override
	public String getText(Object object) {
		if (object instanceof IMergeViewerItem) {
			final String text;
			IMergeViewerItem mergeViewerItem = (IMergeViewerItem)object;
			final Object value = mergeViewerItem.getSideValue(side);
			if (value instanceof EObject && ((EObject)value).eIsProxy()) {
				text = "proxy : " + ((InternalEObject)value).eProxyURI().toString(); //$NON-NLS-1$
			} else if (mergeViewerItem.isInsertionPoint()) {
				// workaround for 406513: Windows specific issue. Only labels of (Tree/Table)Item are
				// selectable on Windows platform. The labels of placeholders in (Tree/Table)Viewer
				// are one whitespace. Placeholder are then selectable at the very left of itself.
				// Add a 42 whitespaces label to workaround.
				text = "                                          "; //$NON-NLS-1$
			} else if (value == null && mergeViewerItem.getSideValue(side.opposite()) instanceof Resource) {
				text = resourceBundle.getString("UnkownResource"); //$NON-NLS-1$
			} else if (value == null && mergeViewerItem.getLeft() == null
					&& mergeViewerItem.getRight() == null
					&& mergeViewerItem.getAncestor() instanceof Resource) {
				text = resourceBundle.getString("UnkownResource"); //$NON-NLS-1$
			} else {
				text = super.getText(value);
			}
			return text;
		}
		return super.getText(object);
	}

	@Override
	public Image getImage(Object object) {
		if (object instanceof IMergeViewerItem) {
			IMergeViewerItem mergeViewerItem = (IMergeViewerItem)object;
			if (mergeViewerItem.isInsertionPoint()) {
				return null;
			} else if (mergeViewerItem.getSideValue(side) == null
					&& mergeViewerItem.getSideValue(side.opposite()) instanceof Resource) {
				return super.getImage(mergeViewerItem.getSideValue(side.opposite()));
			} else if (mergeViewerItem.getLeft() == null && mergeViewerItem.getRight() == null
					&& mergeViewerItem.getAncestor() instanceof Resource) {
				return super.getImage(mergeViewerItem.getAncestor());
			} else {
				return super.getImage(mergeViewerItem.getSideValue(side));
			}
		}
		return super.getImage(object);
	}
}
