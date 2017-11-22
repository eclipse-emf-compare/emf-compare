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

import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.util.MergeViewerUtil;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.compare.utils.IEqualityHelper;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ITableItemFontProvider;
import org.eclipse.emf.edit.provider.ITableItemLabelProvider;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;

/**
 * An {@link ItemProvider} used to represent each item in the property tree. It implements
 * {@link IMergeViewerItem} to integrate with the underlying base framework. It is generally intended to work
 * in a {@link TreeViewer tree}, with two columns, a 'Property' column and a 'Value' column, supporting an
 * {@link #getColumnImage(Object, int) image} and {@link #getColumnText(Object, int) label} for each.
 */
@SuppressWarnings("deprecation")
abstract class PropertyItem extends ItemProvider implements ITableItemLabelProvider, ITableItemFontProvider, IMergeViewerItem.Container {

	/** This is the special category name for property descriptors without a category. */
	private static final String MISC_CATEGORY = EMFCompareIDEUIMessages
			.getString("PropertyContentMergeViewer.miscCategory.label"); //$NON-NLS-1$

	private static final String EXPERT_VIEW_FILTER_FLAG = "org.eclipse.ui.views.properties.expert"; //$NON-NLS-1$

	/**
	 * The configuration used by the property item. It is used to know the
	 * {@link EMFCompareConfiguration#getComparison() comparison} and to get
	 * {@link EMFCompareConfiguration#getBooleanProperty(String, boolean) properties}.
	 */
	private EMFCompareConfiguration configuration;

	/**
	 * The {@link #getSide() side} of this property item.
	 */
	private MergeViewerSide side;

	/**
	 * The property item corresponding to the ancestor.
	 */
	protected PropertyItem ancestor;

	/**
	 * The property item corresponding to the left.
	 */
	protected PropertyItem left;

	/**
	 * The property item corresponding to the right.
	 */
	protected PropertyItem right;

	/**
	 * Creates a root property item.
	 * <p>
	 * Builds a property item for the given object on the given side. This is used both to create a
	 * {@link #getRootItem() root} item and to build the children a property value that implements
	 * {@link IItemPropertySource}.
	 * </p>
	 * 
	 * @param configuration
	 *            the compare configuration of the root property item.
	 * @param object
	 *            the side object for the root property item.
	 * @param side
	 *            the side of this root property item.
	 * @return a new root property item.
	 */
	public static PropertyItem createPropertyItem(final EMFCompareConfiguration configuration,
			final Object object, final MergeViewerSide side) {
		final AdapterFactoryItemDelegator itemDelegator = new AdapterFactoryItemDelegator(
				configuration.getAdapterFactory());

		PropertyItem rootItem = new RootPropertyItem(configuration, itemDelegator, object, side);
		List<IItemPropertyDescriptor> propertyDescriptors = getPropertyDescriptors(object, itemDelegator);
		populateRootPropertyItem(rootItem, propertyDescriptors, object, configuration, side);

		return rootItem;
	}

	private static List<IItemPropertyDescriptor> getPropertyDescriptors(final Object object,
			final AdapterFactoryItemDelegator itemDelegator) {
		List<IItemPropertyDescriptor> propertyDescriptors;
		if (object instanceof Resource) {
			// Special case for resources, because those generally have no property descriptors
			propertyDescriptors = Collections
					.singletonList(new ResourcePropertyDescriptor((Resource)object, itemDelegator));
		} else {
			propertyDescriptors = itemDelegator.getPropertyDescriptors(object);
		}
		return propertyDescriptors;
	}

	private static void populateRootPropertyItem(final PropertyItem rootItem,
			final List<IItemPropertyDescriptor> propertyDescriptors, final Object object,
			final EMFCompareConfiguration configuration, final MergeViewerSide side) {
		if (propertyDescriptors == null) {
			return;
		}

		Map<EStructuralFeature, Multimap<Object, Diff>> featureDiffs = buildFeatureToDiffMap(object,
				configuration);

		// A map from category name to a map from property name to the property descriptor item with
		// that name. These both use tree maps to sort the categories and the property descriptors.
		Map<String, Map<String, PropertyItem>> categories = Maps.newTreeMap();
		for (IItemPropertyDescriptor propertyDescriptor : propertyDescriptors) {
			addChildPropertyItem(categories, propertyDescriptor, object, configuration, featureDiffs, side);
		}

		// Compose the results into the children, do so with or without categories, as appropriate.
		EList<PropertyItem> children = rootItem.getPropertyItems();
		// If we're showing categories and there are categories, other than only the misc category...
		if (shouldShowCategories(configuration) && (categories.size() > 1
				|| categories.size() == 1 && categories.get(MISC_CATEGORY) == null)) {
			// Build a category item for each category, adding it to the children, and add the
			// property items as children of that category item.
			for (Map.Entry<String, Map<String, PropertyItem>> entry : categories.entrySet()) {
				PropertyItem categoryItem = new PropertyCategoryItem(configuration, entry.getKey(), side);
				children.add(categoryItem);
				categoryItem.getChildren().addAll(entry.getValue().values());
			}
		} else {
			// Otherwise, compose all the categories into a single map and use those sorted property
			// descriptor items as the children.
			Map<String, PropertyItem> sortedItems = Maps.newTreeMap();
			for (Map<String, PropertyItem> items : categories.values()) {
				sortedItems.putAll(items);
			}
			children.addAll(sortedItems.values());
		}
	}

	private static void addChildPropertyItem(Map<String, Map<String, PropertyItem>> categories,
			IItemPropertyDescriptor itemPropertyDescriptor, Object object,
			EMFCompareConfiguration configuration,
			Map<EStructuralFeature, Multimap<Object, Diff>> featureDiffs, MergeViewerSide side) {
		// If we're not showing advanced properties, skip the property descriptors flagged as
		// expert properties.
		if (!shouldShowAdvancedProperties(configuration)) {
			String[] filterFlags = itemPropertyDescriptor.getFilterFlags(object);
			if (filterFlags != null) {
				for (String filterFlag : filterFlags) {
					if (EXPERT_VIEW_FILTER_FLAG.equals(filterFlag)) {
						return;
					}
				}
			}
		}

		// Get the feature of the property fetch and its corresponding diffs multi-map.
		Object feature = itemPropertyDescriptor.getFeature(object);
		Multimap<Object, Diff> diffs = featureDiffs.remove(feature);
		PropertyItem childItem = new PropertyDescriptorItem(configuration, object, diffs,
				itemPropertyDescriptor, side);

		// Fetch the map for the category, creating one if necessary.
		String category = determineCategory(object, itemPropertyDescriptor);
		Map<String, PropertyItem> items = categories.get(category);
		if (items == null) {
			items = Maps.newTreeMap();
			categories.put(category, items);
		}

		// Put the item in the sorted map.
		items.put(itemPropertyDescriptor.getDisplayName(object), childItem);
	}

	private static String determineCategory(Object object, IItemPropertyDescriptor itemPropertyDescriptor) {
		// Determine the category, using misc if there isn't one.
		String category = itemPropertyDescriptor.getCategory(object);
		if (category == null) {
			category = MISC_CATEGORY;
		}
		return category;
	}

	private static boolean shouldShowCategories(EMFCompareConfiguration configuration) {
		return configuration.getBooleanProperty(PropertyContentMergeViewer.SHOW_CATEGORIES, true);
	}

	public static Match getMatch(EMFCompareConfiguration configuration, Object object) {
		Match match = null;
		if (object instanceof EObject) {
			EObject eObject = (EObject)object;
			match = configuration.getComparison().getMatch(eObject);
		}
		return match;
	}

	private static boolean shouldShowAdvancedProperties(EMFCompareConfiguration configuration) {
		return configuration.getBooleanProperty(PropertyContentMergeViewer.SHOW_ADVANCED_PROPERTIES, false);
	}

	/**
	 * Builds map from each feature to a multi-map of each side value to its corresponding diff.
	 * <p>
	 * We can only do this if object is an {@link EObject} and if <code>match</code> isn't <code>null</code>.
	 * </p>
	 * 
	 * @param object
	 *            The object to build the featureToDiff map for.
	 * @param match
	 *            the match of the object.
	 * @param comparison
	 *            The comparison.
	 * @return map from each feature to a multi-map of each side value to its corresponding diff.
	 */
	private static Map<EStructuralFeature, Multimap<Object, Diff>> buildFeatureToDiffMap(Object object,
			EMFCompareConfiguration configuration) {
		final Match match = getMatch(configuration, object);
		if (match == null || !(object instanceof EObject)) {
			return Maps.newHashMap();
		}

		final Map<EStructuralFeature, Multimap<Object, Diff>> featureDiffs = Maps.newHashMap();
		for (Diff diff : match.getDifferences()) {
			// If that diff affects a specific feature...
			EStructuralFeature eStructuralFeature = MergeViewerUtil.getAffectedFeature(diff);
			if (eStructuralFeature != null) {
				// Get the multi-map for that feature, creating a new one if necessary.
				Multimap<Object, Diff> diffs = featureDiffs.get(eStructuralFeature);
				if (diffs == null) {
					diffs = HashMultimap.create();
					featureDiffs.put(eStructuralFeature, diffs);
				}

				// Get the primary value of this diff and then iterate over the sides.
				Object value = MatchUtil.getValue(diff);
				for (MergeViewerSide valueSide : PropertyContentMergeViewer.MERGE_VIEWER_SIDES) {
					// If there is a corresponding side value for the match...
					EObject sideEObject = MergeViewerUtil.getEObject(match, valueSide);
					if (sideEObject != null) {
						// Get the corresponding value of that feature on that side.
						List<Object> sideValues = ReferenceUtil.getAsList(sideEObject, eStructuralFeature);
						// If the feature is multi-valued...
						if (eStructuralFeature.isMany()) {
							// Find the corresponding side-value of the value on those side values.
							Object sideValue = MergeViewerUtil.matchingValue(value,
									configuration.getComparison(), sideValues);
							if (sideValue != null) {
								diffs.put(sideValue, diff);
							}
						} else if (sideValues.isEmpty()) {
							// Otherwise, directly use what's typically the one value in the side values.
							diffs.put(null, diff);
						} else {
							diffs.put(sideValues.get(0), diff);
						}
					}
				}
			}
		}
		return featureDiffs;
	}

	/**
	 * Creates an instance of a property item.
	 * 
	 * @param configuration
	 *            the compare configuration.
	 * @param image
	 *            the image of this property item.
	 * @param text
	 *            the text of this property item.
	 * @param side
	 *            the side of this property item.
	 */
	public PropertyItem(EMFCompareConfiguration configuration, Object image, String text,
			MergeViewerSide side) {
		super(text, image);
		this.configuration = configuration;
		this.side = side;
		setSidePropertyItem(side, this);
	}

	/**
	 * Returns the corresponding property item for the specified side.
	 * 
	 * @param anySide
	 *            the side of the desired property item.
	 * @return the corresponding property item for the specified side.
	 */
	public PropertyItem getSide(MergeViewerSide anySide) {
		switch (anySide) {
			case ANCESTOR:
				return ancestor;
			case LEFT:
				return left;
			case RIGHT:
			default:
				return right;
		}
	}

	/**
	 * This is called on a {@link #createPropertyItem(EMFCompareConfiguration, Object, MergeViewerSide) root}
	 * item by {@link PropertyContentMergeViewer#buildPropertiesFromSides(Object, Object, Object)} once it has
	 * built all three sides.
	 * 
	 * @param newLeftSide
	 *            the corresponding left-side root property item.
	 * @param newRightSide
	 *            the corresponding right-side root property item.
	 */
	public void reconcile(PropertyItem newLeftSide, PropertyItem newRightSide) {
		associate(MergeViewerSide.LEFT, newLeftSide);
		associate(MergeViewerSide.RIGHT, newRightSide);

		if (newLeftSide != null) {
			newLeftSide.associate(MergeViewerSide.RIGHT, newRightSide);
			reconcile(newLeftSide.getPropertyItems());
		}

		if (newRightSide != null) {
			reconcile(newRightSide.getPropertyItems());
		}

		if (newLeftSide != null && newRightSide != null) {
			newLeftSide.reconcile(newRightSide.getPropertyItems());
		}

		for (PropertyItem propertyItem : getPropertyItems()) {
			propertyItem.reconcile();
		}

		if (newLeftSide != null) {
			for (PropertyItem propertyItem : newLeftSide.getPropertyItems()) {
				propertyItem.reconcile();
			}
		}

		if (newRightSide != null) {
			for (PropertyItem propertyItem : newRightSide.getPropertyItems()) {
				propertyItem.reconcile();
			}
		}
	}

	/**
	 * Set the appropriate bidirectional side associations.
	 * 
	 * @param otherSide
	 *            the side of that other property item.
	 * @param propertyItem
	 *            the other property item.
	 */
	private void associate(MergeViewerSide otherSide, PropertyItem propertyItem) {
		setSidePropertyItem(side, this);
		setSidePropertyItem(otherSide, propertyItem);
		if (propertyItem != null) {
			propertyItem.setSidePropertyItem(side, this);
			propertyItem.setSidePropertyItem(otherSide, propertyItem);
		}
	}

	/**
	 * Set the value of the appropriate side's field.
	 * 
	 * @param otherSide
	 *            the side to set.
	 * @param propertyItem
	 *            the value to which to set it.
	 */
	private void setSidePropertyItem(MergeViewerSide otherSide, PropertyItem propertyItem) {
		switch (otherSide) {
			case ANCESTOR:
				ancestor = propertyItem;
				break;
			case LEFT:
				left = propertyItem;
				break;
			case RIGHT:
				right = propertyItem;
				break;
		}
	}

	/**
	 * Reconcile's this side's properties against the other side property items.
	 * 
	 * @param otherPropertyItems
	 *            the other side's property items.
	 */
	private void reconcile(EList<PropertyItem> otherPropertyItems) {
		EList<PropertyItem> propertyItems = getPropertyItems();
		List<PropertyItem> remainingOtherPropertyItems = Lists.newArrayList(otherPropertyItems);
		for (PropertyItem propertyItem : propertyItems) {
			// This will associate the items, removing them once associated.
			propertyItem.findMatchingItem(remainingOtherPropertyItems, true);
		}
	}

	/**
	 * Reconcile the properties items of the sides against each other, and then recursively reconcile all the
	 * property items of each side.
	 */
	protected void reconcile() {
		switch (side) {
			case ANCESTOR:
				if (left != null) {
					reconcile(left.getPropertyItems());
				}
				if (right != null) {
					reconcile(right.getPropertyItems());
				}
				break;
			case LEFT:
				if (right != null) {
					left.reconcile(right.getPropertyItems());
				}
				break;
		}

		for (PropertyItem propertyItem : getPropertyItems()) {
			propertyItem.reconcile();
		}
	}

	/**
	 * Finds a matching item in the property items.
	 * 
	 * @param propertyItem
	 *            the item to find.
	 * @param propertyItems
	 *            the items in which to find it.
	 * @param associate
	 *            whether to associate the matching item and to remove it from the property items.
	 * @return the matching item.
	 */
	private PropertyItem findMatchingItem(List<? extends PropertyItem> propertyItems, boolean associate) {
		for (PropertyItem otherPropertyItem : propertyItems) {
			if (isMatchingItem(otherPropertyItem)) {
				if (associate) {
					associate(otherPropertyItem.side, otherPropertyItem);
					propertyItems.remove(otherPropertyItem);
				}
				return otherPropertyItem;
			}
		}
		return null;
	}

	/**
	 * Returns whether this property item matches the specified property item.
	 * 
	 * @param propertyItem
	 *            the property item against which to match.
	 * @return whether this property item matches the specified property item.
	 */
	protected abstract boolean isMatchingItem(PropertyItem propertyItem);

	/**
	 * Determines if the two values {@link IEqualityHelper#matchingValues(Object, Object) match} using the
	 * comparison's equality helper.
	 * 
	 * @param value1
	 *            the first value.
	 * @param value2
	 *            the second value.
	 * @return whether the two values match.
	 */
	protected boolean isMatchingValue(Object value1, Object value2) {
		IEqualityHelper equalityHelper = configuration.getComparison().getEqualityHelper();
		return equalityHelper.matchingValues(value1, value2);
	}

	/**
	 * Finds the corresponding property item of the specified property item somewhere within the receiver
	 * property item.
	 * 
	 * @param propertyItem
	 *            the property item to find.
	 * @return the corresponding property item or the deepest property item in the tree along the path of the
	 *         specified property item.
	 */
	public PropertyItem findItem(PropertyItem propertyItem) {
		PropertyItem propertyItemParent = propertyItem.getParent();
		if (propertyItemParent == null) {
			return this;
		} else {
			PropertyItem foundParent = findItem(propertyItemParent);

			PropertyItem findMatchingItem = propertyItem.findMatchingItem(foundParent.getPropertyItems(),
					false);

			if (findMatchingItem == null) {
				return this;
			} else {
				return findMatchingItem;
			}
		}
	}

	/**
	 * Returns the children, which must be property items.
	 * 
	 * @return the children.
	 */
	@SuppressWarnings("unchecked")
	public EList<PropertyItem> getPropertyItems() {
		return (EList<PropertyItem>)(EList<?>)children;
	}

	protected boolean isModified() {
		return false;
	}

	/**
	 * Returns the parent, which must be a property item.
	 * 
	 * @return the parent.
	 */
	@Override
	public PropertyItem getParent() {
		return (PropertyItem)super.getParent();
	}

	/**
	 * Returns the primary object of this property item.
	 * 
	 * @return the primary object of this property item.
	 */
	protected abstract Object getObject();

	/**
	 * Returns the root property item.
	 * 
	 * @return the root property item.
	 */
	public PropertyItem getRootItem() {
		PropertyItem rootItem = this;
		while (rootItem.getParent() != null) {
			rootItem = rootItem.getParent();
		}
		return rootItem;
	}

	/**
	 * This must be called when the item property descriptor is expanded and collapsed. For lists it's
	 * designed to hide the property image and property text while the list is expanded, showing it again when
	 * it's collapsed.
	 * 
	 * @param treeItem
	 *            the item being expanded or collapsed.
	 * @param expanded
	 *            whether the item is expanded as opposite to collapsed.
	 */
	public void update(TreeItem treeItem, boolean expanded) {
	}

	/**
	 * Returns the text for the value column of the property item.
	 * 
	 * @return the text for the value column of the property item.
	 */
	protected String getPropertyText() {
		return ""; //$NON-NLS-1$
	}

	/**
	 * Returns the image for the value column of the property item.
	 * 
	 * @return the image for the value column of the property item.
	 */
	protected Object getPropertyImage() {
		return null;
	}

	/**
	 * Returns the text for the property column or value column.
	 * 
	 * @param object
	 *            the object which is generally ignored.
	 * @param columnIndex
	 *            either {@code 0} or {@code 1}, for the property column or value column respectively.
	 * @return the text for the property column or value column.
	 */
	public String getColumnText(Object object, int columnIndex) {
		if (columnIndex == 0) {
			return getText(object);
		} else {
			return getPropertyText();
		}
	}

	/**
	 * Returns the image for the property column or value column.
	 * 
	 * @param object
	 *            the object which is generally ignored.
	 * @param columnIndex
	 *            either {@code 0} or {@code 1}, for the property column or value column respectively.
	 * @return the image for the property column or value column.
	 */
	public Object getColumnImage(Object object, int columnIndex) {
		if (columnIndex == 0) {
			return getImage(object);
		} else {
			return getPropertyImage();
		}
	}

	/**
	 * Returns the font for the property column or value column. {@link #isModified() Modified} property items
	 * will be shown in bold font.
	 * 
	 * @param object
	 *            the object, which is ignored.
	 * @param columnIndex
	 *            either {@code 0} or {@code 1}, for the property column or value column respectively.
	 * @return the font for the property column or value column.
	 */
	public Object getFont(Object object, int columnIndex) {
		if (isModified()) {
			return IItemFontProvider.BOLD_FONT;
		} else {
			return null;
		}
	}

	/**
	 * Returns the diff associated with this property item.
	 * 
	 * @return the diff associated with this property item.
	 */
	@Override
	public Diff getDiff() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getLeft() {
		if (left != null) {
			return left.getObject();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getRight() {
		if (right != null) {
			return right.getObject();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getAncestor() {
		if (ancestor != null) {
			return ancestor.getObject();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getSideValue(MergeViewerSide anySide) {
		switch (anySide) {
			case ANCESTOR:
				return getAncestor();
			case LEFT:
				return getLeft();
			case RIGHT:
			default:
				return getRight();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MergeViewerSide getSide() {
		return side;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isInsertionPoint() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void notifyChanged(Notification notification) {
	}

	/**
	 * {@inheritDoc}
	 */
	public Notifier getTarget() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTarget(Notifier newTarget) {
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isAdapterForType(Object type) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasChildren(IDifferenceGroupProvider group, Predicate<? super EObject> predicate) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public IMergeViewerItem[] getChildren(IDifferenceGroupProvider group,
			Predicate<? super EObject> predicate) {
		return null;
	}

	public EMFCompareConfiguration getConfiguration() {
		return configuration;
	}
}
