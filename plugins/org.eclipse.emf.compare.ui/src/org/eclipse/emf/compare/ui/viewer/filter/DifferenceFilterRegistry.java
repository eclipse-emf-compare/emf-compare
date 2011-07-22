/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;

/**
 * The registry which will hold the difference filters contributed through the
 * "org.eclipse.emf.compare.ui.diff.filter" extension point.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @since 1.2
 */
public enum DifferenceFilterRegistry {

	/** Singleton instance of the registry. */
	INSTANCE;

	/** Name of the extension point to parse for extensions. */
	public static final String DIFF_FILTER_EXTENSION_POINT = "org.eclipse.emf.compare.ui.diff.filter"; //$NON-NLS-1$

	/** This Map will be in charge of storing the different contributed descriptors. */
	private final Map<String, DifferenceFilterDescriptor> storage = new HashMap<String, DifferenceFilterDescriptor>();

	/** Hides default constructor. */
	private DifferenceFilterRegistry() {
		// Hides default constructor.
	}

	/**
	 * This will parse the currently running platform for extensions and store all the difference filters that
	 * can be found.
	 */
	public void parseInitialContributions() {
		final IExtension[] extensions = Platform.getExtensionRegistry()
				.getExtensionPoint(DIFF_FILTER_EXTENSION_POINT).getExtensions();
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
	public synchronized List<DifferenceFilterDescriptor> getDescriptors() {
		return new ArrayList<DifferenceFilterDescriptor>(storage.values());
	}

	/**
	 * Adds a descriptor for the given configuration element to the list of contributed extensions.
	 * 
	 * @param element
	 *            The element for which we are to add a descriptor for.
	 */
	public synchronized void addExtension(IConfigurationElement element) {
		final DifferenceFilterDescriptor desc = new DifferenceFilterDescriptor(element);
		storage.put(desc.getID(), desc);
	}

	/**
	 * Removes a given descriptor from the list of known contributions.
	 * 
	 * @param element
	 *            The element for which we are to remove a descriptor for.
	 */
	public synchronized void removeExtension(IConfigurationElement element) {
		final DifferenceFilterDescriptor desc = new DifferenceFilterDescriptor(element);
		storage.remove(desc.getID());
	}

	/**
	 * Clears this registry from any contributed descriptors.
	 */
	public synchronized void clearRegistry() {
		storage.clear();
	}
}
