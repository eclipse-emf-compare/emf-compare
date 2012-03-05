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
package org.eclipse.emf.compare.match.filter;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.compare.match.internal.filter.ResourceFilterRegistryEclipseUtil;

/**
 * This registry will hold all resources filters available for the match to filter resources out of the
 * "to-be-matched" list of resources. An example of this is the BinaryIdenticalResourcesFilter.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class ResourceFilterRegistry {
	/** Singleton instance of the registry. */
	public static final ResourceFilterRegistry INSTANCE = new ResourceFilterRegistry();

	/** This will contain the filters registered against the matching engines. */
	private final Set<IResourceFilter> registeredFilters = new LinkedHashSet<IResourceFilter>();

	/**
	 * This class is a singleton. Access instance through {@link #INSTANCE}.
	 */
	private ResourceFilterRegistry() {
		// Hides default constructor
	}

	/**
	 * Manually adds a filter to the registry. This doesn't need to be called on filters registered through
	 * the extension point.
	 * 
	 * @param filter
	 *            The {@link IResourceFilter} that is to be registered for the matching phase.
	 */
	public void addFilter(IResourceFilter filter) {
		registeredFilters.add(filter);
	}

	/**
	 * Removes a filter from the registry. Filters added through the extension point cannot be removed through
	 * this method.
	 * 
	 * @param filter
	 *            The filter that is to be removed from the registry.
	 */
	public void removeFilter(IResourceFilter filter) {
		registeredFilters.remove(filter);
	}

	/**
	 * Clears the registry out of all registered filters.
	 */
	public void clearRegistry() {
		registeredFilters.clear();
	}

	/**
	 * Returns the set of all registered resource filters. <b>Note</b> that the order could have changed if
	 * this is run within a running Eclipse and the plugin registry itself changed.
	 * 
	 * @return The set of all registered resource filters.
	 */
	public Set<IResourceFilter> getRegisteredResourceFilters() {
		final Set<IResourceFilter> compound = new LinkedHashSet<IResourceFilter>();
		if (EMFPlugin.IS_ECLIPSE_RUNNING) {
			compound.addAll(ResourceFilterRegistryEclipseUtil.getRegisteredResourceFilters());
		}
		compound.addAll(registeredFilters);
		return compound;
	}
}
