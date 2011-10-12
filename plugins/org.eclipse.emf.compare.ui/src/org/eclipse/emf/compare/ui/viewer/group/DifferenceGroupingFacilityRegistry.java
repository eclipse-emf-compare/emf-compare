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
package org.eclipse.emf.compare.ui.viewer.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;

/**
 * The registry which will hold the difference filters contributed through the
 * "org.eclipse.emf.compare.ui.diff.group" extension point.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @since 1.3
 */
public enum DifferenceGroupingFacilityRegistry {

	/** Singleton instance of the registry. */
	INSTANCE;

	/** Name of the extension point to parse for extensions. */
	public static final String DIFF_GROUPING_EXTENSION_POINT = "org.eclipse.emf.compare.ui.diff.group"; //$NON-NLS-1$

	/**
	 * ID for grouping per kind of changes.
	 * 
	 * @since 1.3
	 */
	public static final String GROUPING_PER_KIND_OF_CHANGES_ID = "changes.kind"; //$NON-NLS-1$

	/**
	 * ID for grouping per meta-model element.
	 * 
	 * @since 1.3
	 */
	public static final String GROUPING_PER_METAMODEL_ELEMENT_ID = "eclass.kind"; //$NON-NLS-1$

	/** This Map will be in charge of storing the different contributed descriptors. */
	private final Map<String, DifferenceGroupingFacilityDescriptor> storage = new HashMap<String, DifferenceGroupingFacilityDescriptor>();

	/** Hides default constructor. */
	private DifferenceGroupingFacilityRegistry() {
		// Hides default constructor.
	}

	/**
	 * This will parse the currently running platform for extensions and store all the difference grouping
	 * facilities that can be found.
	 */
	public void parseInitialContributions() {
		final IExtension[] extensions = Platform.getExtensionRegistry()
				.getExtensionPoint(DIFF_GROUPING_EXTENSION_POINT).getExtensions();
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
	public synchronized List<DifferenceGroupingFacilityDescriptor> getDescriptors() {
		return new ArrayList<DifferenceGroupingFacilityDescriptor>(storage.values());
	}

	/**
	 * Adds a descriptor for the given configuration element to the list of contributed extensions.
	 * 
	 * @param element
	 *            The element for which we are to add a descriptor for.
	 */
	public synchronized void addExtension(IConfigurationElement element) {
		final DifferenceGroupingFacilityDescriptor desc = new DifferenceGroupingFacilityDescriptor(element);
		storage.put(desc.getID(), desc);
	}

	/**
	 * Removes a given descriptor from the list of known contributions.
	 * 
	 * @param element
	 *            The element for which we are to remove a descriptor for.
	 */
	public synchronized void removeExtension(IConfigurationElement element) {
		final DifferenceGroupingFacilityDescriptor desc = new DifferenceGroupingFacilityDescriptor(element);
		storage.remove(desc.getID());
	}

	/**
	 * Clears this registry from any contributed descriptors.
	 */
	public synchronized void clearRegistry() {
		storage.clear();
	}

	/**
	 * Transforms a string value into a {@link IDifferenceGroupingFacility}. The string value has to be the id
	 * of the descriptor.
	 * 
	 * @param descriptor
	 *            The id of the descriptor.
	 * @return the {@link IDifferenceGroupingFacility}.
	 * @since 1.3
	 */
	public IDifferenceGroupingFacility getGroupingFacility(String descriptor) {
		final DifferenceGroupingFacilityDescriptor desc = storage.get(descriptor);
		if (desc != null) {
			return desc.getExtension();
		}
		return null;
	}
}
