/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.internal.extension.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor;
import org.eclipse.emf.compare.rcp.internal.extension.IItemRegistry;
import org.osgi.service.prefs.Preferences;

/**
 * Util class for item.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public final class ItemUtil {

	/** Delimiter character used to serialize a list into preferences. */
	public static final String PREFERENCE_DELIMITER = ";"; //$NON-NLS-1$

	/**
	 * Private Constructor.
	 */
	private ItemUtil() {
	}

	/**
	 * Get an item using the preferences. If nothing has been set in the preferences then the highest ranking
	 * item is returned
	 * 
	 * @param registry
	 *            The item registry
	 * @param preferenceKey
	 *            The preference to retrieve the key.
	 * @param itemPreferences
	 *            Eclipse preference where are stored the items to use
	 * @param <T>
	 *            Type of item
	 * @return an item or null if nothing has been found.
	 */
	public static <T> T getItem(IItemRegistry<T> registry, String preferenceKey,
			IEclipsePreferences itemPreferences) {
		IItemDescriptor<T> desc = getDefaultItemDescriptor(registry, preferenceKey, itemPreferences);
		if (desc != null) {
			return desc.getItem();
		}
		return null;
	}

	/**
	 * Get an item descriptor using the preferences. If nothing has been set in the preferences then the
	 * highest ranking item is returned
	 * 
	 * @param registry
	 *            {@link IItemRegistry} of the item type
	 * @param preferenceKey
	 *            Preference key use to retrieve the item
	 * @param itemPreferences
	 *            {@link IEclipsePreferences} where are stored the item preferences.
	 * @param <T>
	 *            Type of item
	 * @return {@link IItemDescriptor}
	 */
	public static <T> IItemDescriptor<T> getDefaultItemDescriptor(IItemRegistry<T> registry,
			String preferenceKey, IEclipsePreferences itemPreferences) {
		IItemDescriptor<T> result = getItemDescriptorFromPref(registry, itemPreferences, preferenceKey);
		if (result == null) {
			IItemDescriptor<T> descriptor = registry.getHighestRankingDescriptor();
			if (descriptor != null) {
				result = descriptor;
			}
		}
		return result;
	}

	/**
	 * Get a list of {@link IItemDescriptor} from preferences.
	 * 
	 * @param registry
	 *            Registry for the {@link IItemDescriptor}
	 * @param preferenceKey
	 *            Key for this {@link IItemDescriptor} in preferences
	 * @param itemPreferences
	 *            {@link IEclipsePreferences} where are stored {@link IItemDescriptor} values
	 * @param <T>
	 *            Type of {@link IItemDescriptor}
	 * @return {@link IItemDescriptor} or null if nothing in preferences
	 */
	private static <T> IItemDescriptor<T> getItemDescriptorFromPref(IItemRegistry<T> registry,
			IEclipsePreferences itemPreferences, String preferenceKey) {
		String itemKey = itemPreferences.get(preferenceKey, null);
		IItemDescriptor<T> result = null;
		if (itemKey != null) {
			IItemDescriptor<T> descritpor = registry.getItemDescriptor(itemKey);
			if (descritpor != null) {
				result = descritpor;
			}
		}
		return result;
	}

	/**
	 * Get a list of {@link IItemDescriptor} from preferences.
	 * 
	 * @param registry
	 *            Registry for the {@link IItemDescriptor}
	 * @param preferenceKey
	 *            Key for this {@link IItemDescriptor} in preferences
	 * @param itemPreferences
	 *            {@link Preferences} where are stored {@link IItemDescriptor} values
	 * @param <T>
	 *            Type of {@link IItemDescriptor}
	 * @return List of {@link IItemDescriptor} or null if nothing in preferences
	 */
	public static <T> List<IItemDescriptor<T>> getItemsDescriptor(IItemRegistry<T> registry,
			String preferenceKey, Preferences itemPreferences) {
		String diffEngineKey = itemPreferences.get(preferenceKey, null);
		List<IItemDescriptor<T>> result = null;
		if (diffEngineKey != null) {
			String[] diffEngineKeys = diffEngineKey.split(PREFERENCE_DELIMITER);
			for (String nonTrimedKey : diffEngineKeys) {
				String key = nonTrimedKey.trim();
				IItemDescriptor<T> descritpor = registry.getItemDescriptor(key);
				if (descritpor != null) {
					if (result == null) {
						result = new ArrayList<IItemDescriptor<T>>();
					}
					result.add(descritpor);
				}
			}
		}

		return result;
	}

	/**
	 * Return the node holding the configuration for an item.
	 * 
	 * @param type
	 *            Type of item.
	 * @param itemId
	 *            Id of the item we want to retrieve the configuration for.
	 * @return The {@link Preferences} holding the configuration.
	 */
	public static Preferences getConfigurationPreferenceNode(String type, String itemId) {
		return EMFCompareRCPPlugin.getDefault().getEMFComparePreferences().node(type).node(itemId);
	}

	/**
	 * Get all active item from a registry.
	 * <p>
	 * (Filter out all disable element stored in preferences)
	 * </p>
	 * 
	 * @param registry
	 *            Registry holding all items of this kind
	 * @param disabledItemPreferenceKey
	 *            Preference key where are stored disabled items.
	 * @return {@link Set} of active items
	 * @param <T>
	 *            Item type
	 */
	public static <T> Set<IItemDescriptor<T>> getActiveItems(IItemRegistry<T> registry,
			String disabledItemPreferenceKey) {
		List<IItemDescriptor<T>> itemsDescriptor = ItemUtil.getItemsDescriptor(registry,
				disabledItemPreferenceKey, EMFCompareRCPPlugin.getDefault().getEMFComparePreferences());

		if (itemsDescriptor == null) {
			return Sets.newLinkedHashSet(registry.getItemDescriptors());
		}
		Set<IItemDescriptor<T>> disableFactories = Sets.newHashSet(itemsDescriptor);
		Set<IItemDescriptor<T>> allFactories = Sets.newHashSet(registry.getItemDescriptors());
		Set<IItemDescriptor<T>> activeFactory = Sets.difference(allFactories, disableFactories);

		return activeFactory;
	}

	/**
	 * Return an ordered list of {@link IItemDescriptor}. The order in the list is either define by the rank
	 * in the registry or from preference is the rank has been overloaded. If any descriptor has been added or
	 * removed from last modification in the preference. This method will merge the modification.
	 * 
	 * @param orderedDefaultDescriptor
	 *            List of ordered default {@link IItemDescriptor}.
	 * @param descriptorRegistry
	 *            Registry of descriptor.
	 * @param orderedItemPreferenceKey
	 *            Key in preferences where are stored the new order of descriptor
	 * @param preferences
	 *            holding user preferences.
	 * @return Ordered list of descriptor.
	 * @param <T>
	 *            Descriptor type.
	 */
	public static <T> List<IItemDescriptor<T>> getOrderedItems(
			List<IItemDescriptor<T>> orderedDefaultDescriptor, IItemRegistry<T> descriptorRegistry,
			String orderedItemPreferenceKey, Preferences preferences) {
		List<IItemDescriptor<T>> itemsDescriptor = ItemUtil.getItemsDescriptor(descriptorRegistry,
				orderedItemPreferenceKey, preferences);

		if (itemsDescriptor == null) {
			itemsDescriptor = orderedDefaultDescriptor;
		} else {
			HashSet<IItemDescriptor<T>> descriptorFromPrefSet = Sets.newLinkedHashSet(itemsDescriptor);
			HashSet<IItemDescriptor<T>> defaultDescriptorSet = Sets
					.newLinkedHashSet(orderedDefaultDescriptor);

			// Remove descriptor
			SetView<IItemDescriptor<T>> descriptorToRemove = Sets.difference(descriptorFromPrefSet,
					defaultDescriptorSet);
			Iterables.removeAll(itemsDescriptor, descriptorToRemove);

			// Add new descriptor
			SetView<IItemDescriptor<T>> descriptorToAdd = Sets.difference(defaultDescriptorSet,
					descriptorFromPrefSet);
			Iterables.addAll(itemsDescriptor, descriptorToAdd);

		}
		return itemsDescriptor;
	}

}
