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

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareConstants;
import org.eclipse.emf.compare.rcp.ui.internal.util.MergeViewerUtil;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.utils.IEqualityHelper;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

class PropertyDescriptorItem extends PropertyItem {

	final private Object object;

	private Diff propertyDiff;

	final private Object editableValue;

	private List<?> listValue;

	final private Object propertyValue;

	final Multimap<Object, Diff> diffs;

	final private IItemPropertyDescriptor itemPropertyDescriptor;

	private boolean hasCheckedForChildren;

	private boolean needsReconcile;

	public PropertyDescriptorItem(EMFCompareConfiguration configuration, final Object object,
			final Multimap<Object, Diff> diffs, IItemPropertyDescriptor itemPropertyDescriptor,
			final MergeViewerSide side) {
		super(configuration, null, itemPropertyDescriptor.getDisplayName(object), side);

		this.itemPropertyDescriptor = itemPropertyDescriptor;
		this.object = object;
		this.diffs = diffs;

		propertyValue = itemPropertyDescriptor.getPropertyValue(object);
		if (propertyValue instanceof IItemPropertySource) {
			IItemPropertySource itemPropertySource = (IItemPropertySource)propertyValue;
			editableValue = itemPropertySource.getEditableValue(object);
		} else {
			editableValue = propertyValue;
		}

		// List handling is used even for feature's that aren't multi-valued but whose value is a
		// list.
		if (editableValue instanceof List<?>) {
			listValue = (List<?>)editableValue;
		} else {
			listValue = null;
		}

		// Determine the side of the object from its match.
		MergeViewerSide matchSide = getSide(getMatch(configuration, object));

		// If it's not a list, or it's a value for a feature that isn't multi-valued...
		Object feature = itemPropertyDescriptor.getFeature(object);
		if (listValue == null) {
			// Consume the diff(s) for the overall property.
			propertyDiff = getDiff(editableValue, matchSide);
		} else if (feature instanceof EStructuralFeature && !((EStructuralFeature)feature).isMany()) {
			// Consume the diff(s) for the overall property rather than using any diffs for the
			// value items in the list.
			if (diffs != null && !diffs.keySet().isEmpty()) {
				propertyDiff = getDiff(diffs.keySet().iterator().next(), matchSide);
			}
		}

		initializeListOfValueChildren();
	}

	private void initializeListOfValueChildren() {
		if (getListValue() == null) {
			return;
		}

		EList<PropertyItem> propertyItems = getPropertyItems();
		for (Object value : getListValue()) {
			Diff diff = getDiff(value, getSide());
			propertyItems.add(new PropertyListElementItem(getConfiguration(), getLabelProvider(), diff, value,
					getSide()));
		}

		if (haveDiffs() && listValue.size() < EMFCompareConstants.LIST_SIZE_INSERTION_POINT_THRESHOLD) {
			createPlaceholders(propertyItems);
		}
	}

	private void createPlaceholders(EList<PropertyItem> propertyItems) {
		// We only create placeholder for the left and right sides...
		if (getSide() == MergeViewerSide.LEFT || getSide() == MergeViewerSide.RIGHT) {
			// We only want to use each diff once.
			Set<Diff> usedDiffs = Sets.newHashSet();
			Comparison comparison = getConfiguration().getComparison();

			// Iterate over the entries...
			for (Map.Entry<Object, Diff> entry : diffs.entries()) {
				// Fetch the value and diff, checking if we haven't already used it...
				Object value = entry.getKey();
				Diff diff = entry.getValue();
				if (usedDiffs.add(diff)) {
					// Determine the affected feature (generally that should be the
					// feature from above)...
					EStructuralFeature affectedFeature = MergeViewerUtil.getAffectedFeature(diff);
					// If there is an affected feature that's multi-valued.
					if (affectedFeature != null && affectedFeature.isMany()) {
						// Determine the insertion index...
						int insertionIndex = DiffUtil.findInsertionIndex(comparison, diff,
								getSide() == MergeViewerSide.LEFT);

						int index;
						if (propertyItems.size() > 0) {
							// Correct the index based on how many placeholders are already
							// earlier in the list of children.
							List<PropertyItem> subList = propertyItems.subList(0, insertionIndex);
							final int count = size(filter(subList, IMergeViewerItem.IS_INSERTION_POINT));
							index = Math.min(insertionIndex + count, propertyItems.size());
						} else {
							index = 0;
						}

						// Create the placeholder and insert it at the appropriate
						// place in the list.
						PropertyValuePlaceholderItem placeholderItem = new PropertyValuePlaceholderItem(
								getConfiguration(), diff, value, getSide());
						propertyItems.add(index, placeholderItem);
					}
				}
			}
		}
	}

	private boolean haveDiffs() {
		return diffs != null && !diffs.isEmpty();
	}

	private IItemLabelProvider getLabelProvider() {
		return itemPropertyDescriptor.getLabelProvider(object);
	}

	/**
	 * Returns the side of the object within the match.
	 * 
	 * @param match
	 *            the match used to determine the side.
	 * @return the side of the object within the match.
	 */
	private MergeViewerSide getSide(Match match) {
		if (match == null) {
			return null;
		} else if (match.getOrigin() == object) {
			return MergeViewerSide.ANCESTOR;
		} else if (match.getLeft() == object) {
			return MergeViewerSide.LEFT;
		} else if (match.getRight() == object) {
			return MergeViewerSide.RIGHT;
		} else {
			return null;
		}
	}

	private Diff getDiff(Object value, MergeViewerSide preferredSide) {
		if (!haveDiffs() || diffs.get(value) == null || diffs.get(value).isEmpty()) {
			return null;
		}

		Diff result = null;
		Collection<Diff> diffCandidates = diffs.get(value);
		if (preferredSide != null) {
			Iterator<Diff> diffIterator = diffCandidates.iterator();
			while (result == null && diffIterator.hasNext()) {
				Diff candidate = diffIterator.next();
				if (isDiffOnSide(candidate, preferredSide)) {
					result = candidate;
				}
			}
		}
		if (result == null) {
			result = diffCandidates.iterator().next();
		}

		// Clear all the entries that use the resulting diff.
		diffs.values().removeAll(Collections.singleton(result));
		return result;
	}

	private boolean isDiffOnSide(Diff diff, MergeViewerSide side) {
		return (MergeViewerSide.LEFT == side && diff.getSource() == DifferenceSource.LEFT)
				|| (MergeViewerSide.RIGHT == side && diff.getSource() == DifferenceSource.RIGHT);
	}

	@Override
	protected Object getObject() {
		return propertyValue;
	}

	@Override
	protected boolean isMatchingItem(PropertyItem propertyItem) {
		return propertyItem instanceof PropertyDescriptorItem
				&& itemPropertyDescriptor.getDisplayName(object).equals(propertyItem.getText());
	}

	@Override
	protected boolean isModified() {
		if (haveDiffs()) {
			return true;
		}

		// Even if it's not directly known to be modified, if any of the other side values are
		// different, from this one, still consider it to be modified.
		boolean isList = isList();
		boolean result = false;
		for (int i = 0; i < PropertyContentMergeViewer.MERGE_VIEWER_SIDES.length && !result; i++) {
			MergeViewerSide otherSide = PropertyContentMergeViewer.MERGE_VIEWER_SIDES[i];
			if (otherSide != getSide()) {
				PropertyDescriptorItem sidePropertyItem = getSide(otherSide);
				if (sidePropertyItem != null) {
					if (isList) {
						result = !equivalentLists(getListValue(), sidePropertyItem.getListValue());
					} else if (!isList) {
						result = !Objects.equals(getPropertyText(), sidePropertyItem.getPropertyText());
					}
				}
			}
		}

		return result;
	}

	private boolean equivalentLists(List<?> list, List<?> otherList) {
		if (list == null) {
			return otherList != null;
		} else if (otherList == null) {
			return false;
		} else if (list.size() != otherList.size()) {
			return false;
		} else {
			IEqualityHelper equalityHelper = getConfiguration().getComparison().getEqualityHelper();
			for (int i = 0, size = list.size(); i < size; ++i) {
				if (!equalityHelper.matchingValues(list.get(i), otherList.get(i))) {
					return false;
				}
			}
			return true;
		}
	}

	protected List<?> getListValue() {
		return listValue;
	}

	protected Object getEditableValue() {
		return editableValue;
	}

	protected boolean isList() {
		// Note that even single-valued features are treated as lists if the single value is a
		// list. This logic checks if all the side values are lists...
		boolean allNull = true;
		boolean anyNonList = false;
		for (MergeViewerSide otherSide : PropertyContentMergeViewer.MERGE_VIEWER_SIDES) {
			PropertyDescriptorItem sidePropertyItem = getSide(otherSide);
			if (sidePropertyItem != null) {
				if (sidePropertyItem.getEditableValue() != null) {
					allNull = false;
					if (sidePropertyItem.getListValue() == null) {
						anyNonList = true;
					}
				}
			}
		}
		// If none are not a list and they're not all null, then it's a list-type value.
		return !anyNonList && !allNull;
	}

	@Override
	public PropertyDescriptorItem getSide(MergeViewerSide anySide) {
		return (PropertyDescriptorItem)super.getSide(anySide);
	}

	@Override
	protected Object getPropertyImage() {
		return getLabelProvider().getImage(editableValue);
	}

	@Override
	protected String getPropertyText() {
		if (isList()) {
			// If it's list type property descriptor, compute the label from the size of the list.
			if (getListValue() == null || getListValue().isEmpty()) {
				return ""; //$NON-NLS-1$
			} else {
				return "..." + listValue.size(); //$NON-NLS-1$
			}
		} else {
			return getLabelProvider().getText(editableValue);
		}
	}

	@Override
	public void update(TreeItem treeItem, boolean expanded) {
		// This is called when the item property descriptor is expanded and collapsed.
		// For lists it's designed to hide the property image and property text while the list is
		// expanded, showing it again when it's collapsed.
		if (getListValue() != null && !getListValue().isEmpty()) {
			if (expanded) {
				treeItem.setImage(1, (Image)null);
				treeItem.setText(1, ""); //$NON-NLS-1$
			} else {
				treeItem.setImage(1, ExtendedImageRegistry.getInstance().getImage(getPropertyImage()));
				treeItem.setText(1, getPropertyText());
			}
		}
	}

	/**
	 * Returns whether this property item has children.
	 * <p>
	 * This implementation is specialized to handle the case that the {@link #getObject() property value} is
	 * an {@link IItemPropertySource}.
	 * <p>
	 * 
	 * @param thisObject
	 *            this object is generally ignored.
	 * @return whether this property item has children.
	 */
	@Override
	public boolean hasChildren(Object thisObject) {
		boolean hasChildren = super.hasChildren(thisObject);
		// If there aren't currently any children and we haven't checked if the property value is
		// a property source...
		if (!hasChildren && !hasCheckedForChildren) {
			// Now we've check, or rather are about to check.
			hasCheckedForChildren = true;

			// If the property value is a property source.
			if (propertyValue instanceof IItemPropertySource) {
				// Create the property item for it.
				PropertyItem propertyItem = createPropertyItem(getConfiguration(), propertyValue, getSide());

				// If this property item has children...
				EList<PropertyItem> propertyChildren = propertyItem.getPropertyItems();
				if (!propertyChildren.isEmpty()) {
					// We'll need to reconcile this property item against the other sides.
					needsReconcile = true;

					// Transfer the children.
					children.addAll(propertyChildren);

					// In this case of course we have children.
					hasChildren = true;
				}
			}

			// Do the same check on all three sides.
			if (ancestor != null && ancestor != this) {
				ancestor.hasChildren(ancestor);
			}
			if (left != null && left != this) {
				left.hasChildren(left);
			}
			if (right != null && right != this) {
				right.hasChildren(right);
			}

			// Reconcile all three sides.
			PropertyDescriptorItem ancestorPropertyDescriptorItem = (PropertyDescriptorItem)ancestor;
			if (ancestorPropertyDescriptorItem != null && ancestorPropertyDescriptorItem.needsReconcile) {
				ancestorPropertyDescriptorItem.reconcile();
				ancestorPropertyDescriptorItem.needsReconcile = false;
			}
			PropertyDescriptorItem leftPropertyDescriptorItem = (PropertyDescriptorItem)left;
			if (leftPropertyDescriptorItem != null && leftPropertyDescriptorItem.needsReconcile) {
				leftPropertyDescriptorItem.reconcile();
				leftPropertyDescriptorItem.needsReconcile = false;
			}
			PropertyDescriptorItem rightPropertyDescriptorItem = (PropertyDescriptorItem)right;
			if (rightPropertyDescriptorItem != null && rightPropertyDescriptorItem.needsReconcile) {
				rightPropertyDescriptorItem.reconcile();
				rightPropertyDescriptorItem.needsReconcile = false;
			}
		}
		return hasChildren;
	}

	@Override
	public Diff getDiff() {
		return propertyDiff;
	}
}
