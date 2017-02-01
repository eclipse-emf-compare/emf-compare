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

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor;
import org.eclipse.emf.compare.rcp.internal.extension.IItemRegistry;

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
	 * @param <T>
	 *            Type of item
	 * @return an item or null if nothing has been found.
	 */
	public static <T> T getItem(IItemRegistry<T> registry, String preferenceKey) {
		IItemDescriptor<T> desc = getDefaultItemDescriptor(registry, preferenceKey);
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
	 * @param <T>
	 *            Type of item
	 * @return {@link IItemDescriptor}
	 */
	public static <T> IItemDescriptor<T> getDefaultItemDescriptor(IItemRegistry<T> registry,
			String preferenceKey) {
		IItemDescriptor<T> result = getItemDescriptorFromPref(registry, preferenceKey);
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
	 * @param <T>
	 *            Type of {@link IItemDescriptor}
	 * @return {@link IItemDescriptor} or null if nothing in preferences
	 */
	private static <T> IItemDescriptor<T> getItemDescriptorFromPref(IItemRegistry<T> registry,
			String preferenceKey) {
		String itemKey = Platform.getPreferencesService().getString(EMFCompareRCPPlugin.PLUGIN_ID,
				preferenceKey, null, null);
		IItemDescriptor<T> result = null;
		if (itemKey != null) {
			IItemDescriptor<T> descriptor = registry.getItemDescriptor(itemKey);
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
	 * @param qualifier
	 *            the preference qualifier (plug-in ID)
	 * @param preferenceKey
	 *            Key for this {@link IItemDescriptor} in preferences
	 * @param <T>
	 *            Type of {@link IItemDescriptor}
	 * @return List of {@link IItemDescriptor} or null if nothing in preferences
	 */
	public static <T> List<IItemDescriptor<T>> getItemsDescriptor(IItemRegistry<T> registry, String qualifier,
			String preferenceKey) {
		String diffEngineKey = Platform.getPreferencesService().getString(qualifier, preferenceKey, null,
				null);
		List<IItemDescriptor<T>> result = null;
		if (diffEngineKey != null) {
			String[] diffEngineKeys = diffEngineKey.split(PREFERENCE_DELIMITER);
			for (String nonTrimedKey : diffEngineKeys) {
				String key = nonTrimedKey.trim();
				IItemDescriptor<T> descriptor = registry.getItemDescriptor(key);
				if (descriptor != null) {
					if (result == null) {
						result = new ArrayList<IItemDescriptor<T>>();
					}
					result.add(descriptor);
				}
			}
		}

		return result;
	}

	/**
	 * Get all active item from a registry.
	 * <p>
	 * (Filter out all disable element stored in preferences)
	 * </p>
	 * 
	 * @param registry
	 *            Registry holding all items of this kind
	 * @param qualifier
	 *            The preference qualifier (plug-in ID)
	 * @param disabledItemPreferenceKey
	 *            Preference key where are stored disabled items.
	 * @return {@link Set} of active items
	 * @param <T>
	 *            Item type
	 */
	public static <T> Set<IItemDescriptor<T>> getActiveItems(IItemRegistry<T> registry, String qualifier,
			String disabledItemPreferenceKey) {
		List<IItemDescriptor<T>> itemsDescriptor = getItemsDescriptor(registry, qualifier,
				disabledItemPreferenceKey);

		if (itemsDescriptor == null) {
			return Sets.newLinkedHashSet(registry.getItemDescriptors());
		}
		Set<IItemDescriptor<T>> disableFactories = Sets.newHashSet(itemsDescriptor);
		Set<IItemDescriptor<T>> allFactories = Sets.newHashSet(registry.getItemDescriptors());
		Set<IItemDescriptor<T>> activeFactory = Sets.difference(allFactories, disableFactories);

		return activeFactory;
	}

}
