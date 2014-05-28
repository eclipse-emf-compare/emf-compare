/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver.registry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IStorage;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ThreadedModelResolver;
import org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * This registry implements its own strategy to define the "best" resolver to use.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class ModelResolverRegistry {
	/** Keeps track of the extensions providing model resolvers. */
	private final Map<String, ModelResolverDescriptor> registeredDescriptors;

	/**
	 * The threaded model resolver is special in that we use it when resolving is disabled. This will keep a
	 * reference to it.
	 */
	private ThreadedModelResolver defaultResolver;

	/**
	 * Initializes our registry.
	 * 
	 * @param preferenceStore
	 *            Preference store this can query for resolver-related information.
	 */
	public ModelResolverRegistry() {
		this.registeredDescriptors = new LinkedHashMap<String, ModelResolverDescriptor>();
	}

	/**
	 * Returns a view of the descriptors registered in this registry.
	 * 
	 * @return A view of the descriptors registered in this registry.
	 */
	public List<ModelResolverDescriptor> getRegisteredDescriptors() {
		return new ArrayList<ModelResolverDescriptor>(registeredDescriptors.values());
	}

	/**
	 * Returns a {@link IModelResolver} that handles the given IStorage.
	 * <p>
	 * This will iterate over all the registered resolvers, selecting the highest-ranking resolver that can
	 * resolve the target models.
	 * </p>
	 * 
	 * @param sourceStorage
	 *            Source of the comparison
	 * @return a {@link IModelResolver} that is able to handle the IStorage.
	 */
	public IModelResolver getBestResolverFor(IStorage sourceStorage) {
		if (!isEnabled()) {
			return defaultResolver;
		}

		ModelResolverDescriptor resolver = null;
		for (ModelResolverDescriptor candidate : registeredDescriptors.values()) {
			if (resolver == null || resolver.getRanking() < candidate.getRanking()) {
				if (candidate.getModelResolver().canResolve(sourceStorage)) {
					resolver = candidate;
				}
			}
		}

		if (resolver != null) {
			return resolver.getModelResolver();
		}
		return defaultResolver;
	}

	/**
	 * This can be used to check whether model resolution is currently enabled.
	 * 
	 * @return <code>true</code> if the model resolution is enabled, <code>false</code> otherwise.
	 */
	public boolean isEnabled() {
		final IPreferenceStore store = EMFCompareIDEUIPlugin.getDefault().getPreferenceStore();
		return !store.getBoolean(EMFCompareUIPreferences.DISABLE_RESOLVERS_PREFERENCE);
	}

	/**
	 * Registers a new resolver within this registry instance.
	 * 
	 * @param key
	 *            Identifier of this resolver.
	 * @param descriptor
	 *            The resolver to register.
	 */
	void addResolver(String key, ModelResolverDescriptor descriptor) {
		if (ThreadedModelResolver.class.getName().equals(key)) {
			defaultResolver = (ThreadedModelResolver)descriptor.getModelResolver();
		}
		registeredDescriptors.put(key, descriptor);
	}

	/**
	 * Removes the resolver identified by <code>key</code> from this registry.
	 * 
	 * @param key
	 *            Identifier of the resolver we are to remove from this registry.
	 * @return The removed resolver, if any.
	 */
	ModelResolverDescriptor removeResolver(String key) {
		final ModelResolverDescriptor descriptor = registeredDescriptors.remove(key);
		if (descriptor != null) {
			descriptor.dispose();
		}
		return descriptor;
	}

	/** Clears out all registered resolvers from this registry. */
	public void clear() {
		final Iterator<ModelResolverDescriptor> descriptors = registeredDescriptors.values().iterator();
		while (descriptors.hasNext()) {
			descriptors.next().dispose();
			descriptors.remove();
		}
	}
}
