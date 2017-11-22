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

import com.google.common.collect.ImmutableList;

import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.ICompareAccessor;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * A specialized {@link ICompareAccessor} to wrap a {@link PropertyItem}.
 */
class PropertyAccessor implements ICompareAccessor {
	/**
	 * The {@link PropertyItem#createPropertyItem(EMFCompareConfiguration, Object, MergeViewerSide) root}
	 * property item.
	 */
	final PropertyItem rootPropertyItem;

	/**
	 * The {@link #getInitialItem() initial} item.
	 * 
	 * @see #setInitialItem(Diff)
	 * @see #setInitialItem(PropertyItem)
	 */
	private PropertyItem initialItem;

	/**
	 * Creates an instance for the given configuration, side object, and side.
	 * 
	 * @param configuration
	 *            the compare configuration
	 * @param object
	 *            the side object.
	 * @param side
	 *            the side of that object.
	 */
	public PropertyAccessor(EMFCompareConfiguration configuration, Object object, MergeViewerSide side) {
		this.rootPropertyItem = PropertyItem.createPropertyItem(configuration, object, side);
	}

	/**
	 * Returns the root property item of this property accessor.
	 * 
	 * @return the root property item of this property accessor.
	 */
	public PropertyItem getRootPropertyItem() {
		return rootPropertyItem;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return rootPropertyItem.getText();
	}

	/**
	 * {@inheritDoc}
	 */
	public Image getImage() {
		return ExtendedImageRegistry.INSTANCE.getImage(rootPropertyItem.getImage());
	}

	/**
	 * {@inheritDoc}
	 */
	public String getType() {
		return "property"; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	public Comparison getComparison() {
		return rootPropertyItem.getConfiguration().getComparison();
	}

	/**
	 * {@inheritDoc}
	 */
	public IMergeViewerItem getInitialItem() {
		return initialItem;
	}

	/**
	 * {@inheritDoc}
	 */
	public ImmutableList<? extends IMergeViewerItem> getItems() {
		return ImmutableList.copyOf(rootPropertyItem.getPropertyItems());
	}

	/**
	 * Set the {@link #getInitialItem() initial} item based on the given diff. If the diff is {@code null}, it
	 * will set the first property item with a {@link PropertyItem#getDiff() diff} or that
	 * {@link PropertyItem#isModified() is modified}.
	 * 
	 * @param diff
	 *            the diff or {@code null}.
	 */
	public void setInitialItem(Diff diff) {
		initialItem = findPropertyItem(diff, rootPropertyItem.getPropertyItems());
		// If we didn't find one for the given diff...
		if (initialItem == null && diff != null) {
			// Look for one with a diff or that is modified.
			initialItem = findPropertyItem(null, rootPropertyItem.getPropertyItems());
		}
	}

	/**
	 * Finds the property item with the given diff in the given list or property items. This method works
	 * recursively on the {@link PropertyItem#getPropertyItems() children} of each property item. If the diff
	 * is {@code null}, it will find the first property item with a {@link PropertyItem#getDiff() diff} or
	 * that {@link PropertyItem#isModified() is modified}.
	 * 
	 * @param diff
	 *            the diff or {@code null}.
	 * @param propertyItems
	 *            the list of {@link PropertyItem property items}.
	 * @return The appropriate {@link #getInitialItem() initial} item.
	 */
	private PropertyItem findPropertyItem(Diff diff, List<PropertyItem> propertyItems) {
		for (PropertyItem propertyItem : propertyItems) {
			if (isPropertyItemForDiff(diff, propertyItem)
					|| (diff == null && isPropertyItemModifiedOrHasDiff(propertyItem))) {
				return propertyItem;
			}

			// Force the children to be computed, if necessary.
			if (propertyItem.hasChildren(propertyItem)) {
				PropertyItem result = findPropertyItem(diff, propertyItem.getPropertyItems());
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}

	private boolean isPropertyItemForDiff(Diff diff, PropertyItem propertyItem) {
		return diff != null && propertyItem.getDiff() == diff;
	}

	private boolean isPropertyItemModifiedOrHasDiff(PropertyItem propertyItem) {
		return propertyItem.getDiff() != null || propertyItem.isModified();
	}

	/**
	 * Sets the {@link #getInitialItem() initial} item based on the given property item. It will
	 * {@link PropertyItem#findItem(PropertyItem) find} the item of the appropriate
	 * {@link PropertyItem#getSide() side} in the root property item.
	 * 
	 * @param propertyItem
	 *            a property item or {@code null}.
	 */
	public void setInitialItem(PropertyItem propertyItem) {
		if (propertyItem == null) {
			initialItem = null;
		} else {
			PropertyItem sidePropertyItem = propertyItem.getSide(rootPropertyItem.getSide());
			if (sidePropertyItem == null) {
				initialItem = null;
			} else if (sidePropertyItem.getRootItem() != rootPropertyItem) {
				initialItem = rootPropertyItem.findItem(sidePropertyItem);
			} else {
				initialItem = sidePropertyItem;
			}
		}
	}
}
