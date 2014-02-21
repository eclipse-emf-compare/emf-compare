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
package org.eclipse.emf.compare.rcp.internal.engine.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.internal.engine.IItemDescriptor;
import org.eclipse.emf.compare.rcp.internal.engine.IItemRegistry;
import org.osgi.service.prefs.Preferences;

/**
 * Util class for item.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public final class ItemUtil {

	/** Delimiter character used to serialize a list into preferences. */
	public static final String PREFFERENCE_DELIMITER = ";"; //$NON-NLS-1$

	/**
	 * Private Constructor.
	 */
	private ItemUtil() {
	}

	/**
	 * Get an engine using the preferences. If nothing has been set in the preferences then the highest
	 * ranking engine is returned
	 * 
	 * @param registry
	 *            The engine registry
	 * @param preferenceKey
	 *            The preference to retrieve the key.
	 * @param enginePreferences
	 *            Eclipse preference where are stored the engines to use
	 * @param <T>
	 *            Type of engine
	 * @return an engine or null if nothing has been found.
	 */
	public static <T> T getItem(IItemRegistry<T> registry, String preferenceKey,
			IEclipsePreferences enginePreferences) {
		IItemDescriptor<T> desc = getDefaultItemDescriptor(registry, preferenceKey, enginePreferences);
		if (desc != null) {
			return desc.getItem();
		}
		return null;
	}

	/**
	 * Get an item descriptor using the preferences. If nothing has been set in the preferences then the
	 * highest ranking engine is returned
	 * 
	 * @param registry
	 *            {@link IItemRegistry} of the engine type
	 * @param preferenceKey
	 *            Preference key use to retrieve the engine
	 * @param enginePreferences
	 *            {@link IEclipsePreferences} where are stored the engine preferences.
	 * @param <T>
	 *            Type of engine
	 * @return {@link IItemDescriptor}
	 */
	public static <T> IItemDescriptor<T> getDefaultItemDescriptor(IItemRegistry<T> registry,
			String preferenceKey, IEclipsePreferences enginePreferences) {
		IItemDescriptor<T> result = getItemDescriptorFromPref(registry, enginePreferences, preferenceKey);
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
	 * @param enginePreferences
	 *            {@link IEclipsePreferences} where are stored {@link IItemDescriptor} values
	 * @param <T>
	 *            Type of {@link IItemDescriptor}
	 * @return {@link IItemDescriptor} or null if nothing in preferences
	 */
	private static <T> IItemDescriptor<T> getItemDescriptorFromPref(IItemRegistry<T> registry,
			IEclipsePreferences enginePreferences, String preferenceKey) {
		String engineKey = enginePreferences.get(preferenceKey, null);
		IItemDescriptor<T> result = null;
		if (engineKey != null) {
			IItemDescriptor<T> descritpor = registry.getItemDescriptor(engineKey);
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
	 * @param enginePreferences
	 *            {@link IEclipsePreferences} where are stored {@link IItemDescriptor} values
	 * @param <T>
	 *            Type of {@link IItemDescriptor}
	 * @return List of {@link IItemDescriptor} or null if nothing in preferences
	 */
	public static <T> List<IItemDescriptor<T>> getItemsDescriptor(IItemRegistry<T> registry,
			String preferenceKey, IEclipsePreferences enginePreferences) {
		String diffEngineKey = enginePreferences.get(preferenceKey, null);
		List<IItemDescriptor<T>> result = null;
		if (diffEngineKey != null) {
			String[] diffEngineKeys = diffEngineKey.split(PREFFERENCE_DELIMITER);
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
}
