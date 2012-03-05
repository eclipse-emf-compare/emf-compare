/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;

/**
 * The registry which will hold the contextual menus contributed through the
 * "org.eclipse.emf.compare.ui.contextual.menus" extension point.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.3
 */
public enum ContextualMenuRegistry {

	/** Singleton instance of the registry. */
	INSTANCE;

	/** Name of the extension point to parse for extensions. */
	public static final String CONTEXTUAL_MENU_EXTENSION_POINT = "org.eclipse.emf.compare.ui.contextual.menus"; //$NON-NLS-1$

	/** The separator used to convert the list of descriptors into a string value. */
	private static final String SEPARATOR = ";"; //$NON-NLS-1$

	/** This Map will be in charge of storing the different contributed descriptors. */
	private final Map<String, ContextualMenuDescriptor> storage = new HashMap<String, ContextualMenuDescriptor>();

	/** Hides default constructor. */
	private ContextualMenuRegistry() {
		// Hides default constructor.
	}

	/**
	 * This will parse the currently running platform for extensions and store all the contextual menus that
	 * can be found.
	 */
	public void parseInitialContributions() {
		final IExtension[] extensions = Platform.getExtensionRegistry()
				.getExtensionPoint(CONTEXTUAL_MENU_EXTENSION_POINT).getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			final IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
			for (int j = 0; j < configElements.length; j++) {
				addExtension(configElements[j]);
			}
		}
	}

	/**
	 * Returns all registered descriptors.
	 * 
	 * @return all registered descriptors.
	 */
	public synchronized List<ContextualMenuDescriptor> getDescriptors() {
		return new ArrayList<ContextualMenuDescriptor>(storage.values());
	}

	/**
	 * Adds a descriptor for the given configuration element to the list of contributed extensions.
	 * 
	 * @param element
	 *            The element for which we are to add a descriptor for.
	 */
	public synchronized void addExtension(IConfigurationElement element) {
		final ContextualMenuDescriptor desc = new ContextualMenuDescriptor(element);
		storage.put(desc.getID(), desc);
	}

	/**
	 * Removes a given descriptor from the list of known contributions.
	 * 
	 * @param element
	 *            The element for which we are to remove a descriptor for.
	 */
	public synchronized void removeExtension(IConfigurationElement element) {
		final ContextualMenuDescriptor desc = new ContextualMenuDescriptor(element);
		storage.remove(desc.getID());
	}

	/**
	 * Clears this registry from any contributed descriptors.
	 */
	public synchronized void clearRegistry() {
		storage.clear();
	}

	/**
	 * Returns the {@link ContextualMenuDescriptor} in relation to its id.
	 * 
	 * @param id
	 *            The id.
	 * @return The descriptor.
	 */
	public ContextualMenuDescriptor getDescriptor(String id) {
		return storage.get(id);

	}

	/**
	 * Transforms a list of {@link ContextualMenuDescriptor} into a string value which owns the id of the
	 * descriptors, separated by the separator {@link SEPARATOR}.
	 * 
	 * @param descriptors
	 *            The descriptors.
	 * @return The result.
	 */
	public String getDescriptors(List<ContextualMenuDescriptor> descriptors) {
		boolean firstElement = true;
		final StringBuffer result = new StringBuffer();
		for (ContextualMenuDescriptor desc : descriptors) {
			if (!firstElement) {
				result.append(SEPARATOR);
			}
			result.append(desc.getID());
			firstElement = false;
		}
		return result.toString();
	}

	/**
	 * Transforms a string value into a list of {@link ContextualMenuDescriptor}. The string value has to own
	 * the id of the descriptors, separated by the separator {@link SEPARATOR}.
	 * 
	 * @param descriptors
	 *            The string value.
	 * @return The list.
	 */
	public List<ContextualMenuDescriptor> getDescriptors(String descriptors) {
		final List<ContextualMenuDescriptor> result = new ArrayList<ContextualMenuDescriptor>();
		final String[] values = descriptors.split(SEPARATOR);
		for (String value : values) {
			final ContextualMenuDescriptor desc = getDescriptor(value);
			if (desc != null)
				result.add(desc);
		}
		return result;
	}

	/**
	 * Transforms a string value into a list of {@link IContextualMenu}. The string value has to own the id of
	 * the descriptors, separated by the separator {@link SEPARATOR}.
	 * 
	 * @param descriptors
	 *            The string value.
	 * @return The list.
	 */
	public List<IContextualMenu> getFilters(String descriptors) {
		final List<IContextualMenu> result = new ArrayList<IContextualMenu>();
		for (ContextualMenuDescriptor desc : getDescriptors(descriptors)) {
			result.add(desc.getExtension());
		}
		return result;
	}
}
