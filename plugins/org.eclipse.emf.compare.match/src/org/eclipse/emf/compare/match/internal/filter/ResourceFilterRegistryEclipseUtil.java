/*******************************************************************************
 * Copyright (c) 2009, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.internal.filter;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.compare.match.filter.IResourceFilter;

/**
 * This registry will hold all resources filters provided through the extension point
 * org.eclipse.emf.compare.match.resourcefilters.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class ResourceFilterRegistryEclipseUtil {
	/** This will contain the filters registered against the matching engines. */
	private static final Set<IResourceFilter> REGISTERED_FILTERS = new LinkedHashSet<IResourceFilter>();

	/**
	 * Utility classes don't need to be instantiated.
	 */
	private ResourceFilterRegistryEclipseUtil() {
		// Hides default constructor
	}

	/**
	 * Adds a filter to the registry. This will be used internally by the registry listener registered at
	 * plugin startup from the activator.
	 * 
	 * @param filter
	 *            The {@link IResourceFilter} that is to be registered for the matching phase.
	 */
	public static void addFilter(IResourceFilter filter) {
		REGISTERED_FILTERS.add(filter);
	}

	/**
	 * Removes a filter to the registry. This will be used internally by the registry listener registered at
	 * plugin startup from the activator.
	 * 
	 * @param filterClass
	 *            Fully qualified class name of the filter that is to be removed from the registry.
	 */
	public static void removeFilter(String filterClass) {
		for (final IResourceFilter filter : new LinkedHashSet<IResourceFilter>(REGISTERED_FILTERS)) {
			if (filterClass.equals(filter.getClass().getName())) {
				REGISTERED_FILTERS.remove(filter);
			}
		}
	}

	/**
	 * Clears the registry out of all registered filters.
	 */
	public static void clearRegistry() {
		REGISTERED_FILTERS.clear();
	}

	/**
	 * Returns the set of all registered resource filters. <b>Note</b> that the order <b>will</b> change if
	 * the plugin registry itself changes.
	 * 
	 * @return The set of all registered resource filters.
	 */
	public static Set<IResourceFilter> getRegisteredResourceFilters() {
		return REGISTERED_FILTERS;
	}
}
