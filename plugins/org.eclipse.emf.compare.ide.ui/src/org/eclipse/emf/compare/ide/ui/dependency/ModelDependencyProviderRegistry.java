/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.dependency;

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.addAll;

import com.google.common.collect.Iterables;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;

/**
 * The registry managing the registered dependency extension point information.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 * @since 4.1
 */
public class ModelDependencyProviderRegistry {

	/** Keeps track of the extensions providing model resolvers. */
	private final Map<String, DependencyProviderDescriptor> registeredDescriptors;

	/**
	 * Constructs and initialized this registry.
	 */
	public ModelDependencyProviderRegistry() {
		registeredDescriptors = new LinkedHashMap<String, DependencyProviderDescriptor>();
	}

	/**
	 * Returns the set of all {@link URI URIs} that are determined as a dependency by the registered
	 * dependency providers. If multiple providers declare dependencies the results are combined.
	 * 
	 * @param uri
	 *            The {@link URI} for which the dependencies are to be determined.
	 * @return The set of dependencies of {@code uri}. If {@code uri} has no dependency, the returned set is
	 *         empty.
	 */
	public Set<URI> getDependencies(URI uri) {
		final Set<URI> uris = new LinkedHashSet<URI>();
		for (DependencyProviderDescriptor descriptor : registeredDescriptors.values()) {
			IDependencyProvider provider = descriptor.getDependencyProvider();
			if (provider != null && provider.apply(uri)) {
				Collection<URI> dependencies = provider.getDependencies(uri);
				if (dependencies != null) {
					addAll(uris, Iterables.filter(dependencies, notNull()));
				}
			}
		}
		return uris;
	}

	/**
	 * Adds the given {@link DependencyProviderDescriptor} to this registry, using the given {@code className}
	 * as the identifier.
	 * 
	 * @param className
	 *            The identifier for the given {@link DependencyProviderDescriptor}.
	 * @param descriptor
	 *            The {@link DependencyProviderDescriptor} which is to be added to this registry.
	 */
	public void addProvider(String className, DependencyProviderDescriptor descriptor) {
		registeredDescriptors.put(className, descriptor);
	}

	/**
	 * Removes the {@link DependencyProviderDescriptor} and its managed {@link IDependencyProvider} identified
	 * by the given {@code className} from this registry.
	 * 
	 * @param className
	 *            Identifier of the provider we are to remove from this registry.
	 * @return The removed {@link DependencyProviderDescriptor}, if any.
	 */
	public DependencyProviderDescriptor removeProvider(String className) {
		return registeredDescriptors.remove(className);
	}

	/** Clears out all registered providers from this registry. */
	public void clear() {
		registeredDescriptors.clear();
	}
}
