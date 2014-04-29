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
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ThreadedModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.osgi.service.prefs.Preferences;

/**
 * This registry implements its own strategy to define the "best" resolver to use.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class ModelResolverRegistry {
	/** Preference key pointing at the "user resolver" id. */
	private static final String USER_RESOLVER_PREF_KEY = "org.eclipse.emf.compare.ide.ui.user.resolver"; //$NON-NLS-1$

	/** Preference key telling us whether the model resolution is currently enabled. */
	private static final String ENABLE_RESOLVING_PREF_KEY = "org.eclipse.emf.compare.ide.ui.enable.resolving"; //$NON-NLS-1$

	/** Preference store to query for model resolver information. */
	private final Preferences preferenceStore;

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
	public ModelResolverRegistry(Preferences preferenceStore) {
		this.preferenceStore = preferenceStore;
		this.registeredDescriptors = new LinkedHashMap<String, ModelResolverDescriptor>();
	}

	public List<ModelResolverDescriptor> getRegisteredDescriptors() {
		return new ArrayList<ModelResolverDescriptor>(registeredDescriptors.values());
	}

	/**
	 * Returns a {@link IModelResolver} that handles the given IStorage.
	 * <p>
	 * First of all, a user-selected resolver will always take precedence over the other, unless it cannot be
	 * used against the target models. If there are no user-selected resolver, or if that resolver cannot be
	 * user, we iterate over all the registered resolvers, selecting the highest-ranking resolver that can
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

		ModelResolverDescriptor resolver = getSelectedResolver();

		if (resolver == null || !resolver.getModelResolver().canResolve(sourceStorage)) {
			for (ModelResolverDescriptor candidate : registeredDescriptors.values()) {
				if (resolver == null || resolver.getRanking() < candidate.getRanking()) {
					if (candidate.getModelResolver().canResolve(sourceStorage)) {
						resolver = candidate;
					}
				}
			}
		}

		if (resolver != null) {
			return resolver.getModelResolver();
		}
		return null;
	}

	/**
	 * Returns the resolver currently selected in the preferences, if any.
	 * 
	 * @return the resolver currently selected in the preferences, <code>null</code> if none.
	 */
	public ModelResolverDescriptor getSelectedResolver() {
		final String selectedKey = preferenceStore.get(USER_RESOLVER_PREF_KEY, null);
		return registeredDescriptors.get(selectedKey);
	}

	/**
	 * Sets the resolver selected by the user in the preferences, if any.
	 * 
	 * @param selected
	 *            The resolver selected by the user in the preferences, <code>null</code> if none.
	 */
	public void setSelectedResolver(String selected) {
		preferenceStore.put(USER_RESOLVER_PREF_KEY, selected);
	}

	/**
	 * This can be used to check whether model resolution is currently enabled. <code>true</code> by default.
	 * 
	 * @return <code>true</code> if the model resolution is enabled, <code>false</code> otherwise.
	 */
	public boolean isEnabled() {
		return preferenceStore.getBoolean(ENABLE_RESOLVING_PREF_KEY, true);
	}

	/**
	 * Disables or enables model resolution.
	 * 
	 * @param enabled
	 *            <code>true</code> if model resolution should be enabled, <code>false</code> otherwise.
	 */
	public void toggleEnablement(boolean enabled) {
		if (!enabled) {
			defaultResolver.setResolutionScope(CrossReferenceResolutionScope.SELF);
		} else {
			/*
			 * FIXME create and use a preference value, along with the proper preference page changes to allow
			 * users to choose this.
			 */
			defaultResolver.setResolutionScope(CrossReferenceResolutionScope.CONTAINER);
		}
		preferenceStore.putBoolean(ENABLE_RESOLVING_PREF_KEY, enabled);
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
