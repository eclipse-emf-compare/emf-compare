/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.property;

import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Specialized tree viewer with the appropriate style for showing two-column properties, appropriate
 * {@link TreeViewer#setSelection(ISelection) selection handling} and
 * {@link TreeViewer#setExpandedState(Object, boolean) expansion handling}, and without a
 * {@link TreeViewer#setComparer(IElementComparer) comparer}.
 */
final class PropertyTreeViewer extends TreeViewer {

	private PropertyItem rootPropertyItem;

	PropertyTreeViewer(Composite parent, PropertyItem propertyItem, int style) {
		super(parent, style);
		this.rootPropertyItem = propertyItem;
	}

	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		// Specialize selection so that it finds the appropriate property item for the side.
		if (rootPropertyItem != null && selection != null && !selection.isEmpty()) {
			PropertyItem propertyItem = (PropertyItem)((IStructuredSelection)selection).getFirstElement();
			// Get the item for this viewer's side.
			PropertyItem sidePropertyItem = propertyItem.getSide(rootPropertyItem.getSide());
			// If there isn't one...
			if (sidePropertyItem == null) {
				// Clear the selection.
				super.setSelection(new StructuredSelection(), reveal);
			} else {
				// If it isn't an item in this viewer's root property item.
				if (sidePropertyItem.getRootItem() != rootPropertyItem) {
					// Find the corresponding item in the root property item.
					// This will always be non-nul.
					sidePropertyItem = rootPropertyItem.findItem(sidePropertyItem);
				}
				// Set the appropriate selection.
				super.setSelection(new StructuredSelection(sidePropertyItem), reveal);
			}
		} else {
			// Let the viewer do it's normal thing, which generally will clear the selection.
			super.setSelection(selection, reveal);
		}
	}

	@Override
	public void setComparer(IElementComparer comparer) {
		// We don't want to use the comparer set by the base class during creation because it's
		// specialized for a different implement of IMergeViewerItem than the one implemented by
		// PropertyItem.
	}

	@Override
	protected void setExpanded(Item item, boolean expand) {
		// Also update the property item when this occurs.
		super.setExpanded(item, expand);
		PropertyItem propertyItem = (PropertyItem)item.getData();
		propertyItem.update((TreeItem)item, expand);
	}
}
