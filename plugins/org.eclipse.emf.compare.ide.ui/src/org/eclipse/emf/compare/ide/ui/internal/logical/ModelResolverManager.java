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
package org.eclipse.emf.compare.ide.ui.internal.logical;

import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.rcp.internal.tracer.TracingConstant;
import org.osgi.service.prefs.Preferences;

/**
 * Manager of {@link IModelResolver}.
 * <p>
 * This manager will be used to keep track of all {@link IModelResolver}s that have been registered against
 * the extension point.
 * </p>
 * <p>
 * This manager can also:
 * <ul>
 * <li>Define a "forced" resolver. It should be evaluated before any other.</li>
 * <li>Disable/Enable resolving mechanism.</li>
 * </ul>
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class ModelResolverManager {

	/** preference key pointing at the "forced resolver" id. */
	private static final String FORCED_RESOLVER_PREF_KEY = "org.eclipse.emf.compare.ide.ui.forced.resolver"; //$NON-NLS-1$

	/** Preference key pointing at the "disable resolving" value. */
	private static final String DISABLE_RESOLVING_PREF_KEY = "org.eclipse.emf.compare.ide.ui.disable.resolving"; //$NON-NLS-1$

	/** {@link Preferences} holding default resolver value. */
	private final Preferences preferenceStore;

	/** registry of ModelResolverDescriptor. */
	private Map<String, ModelResolverDescriptor> descriptors = Collections
			.synchronizedMap(new LinkedHashMap<String, ModelResolverDescriptor>());

	/**
	 * Constructor.
	 * 
	 * @param preferenceStore
	 *            {@link Preferences} holding default resolver value.
	 */
	public ModelResolverManager(Preferences preferenceStore) {
		this.preferenceStore = preferenceStore;
	}

	/**
	 * Add a new resolver.
	 * 
	 * @param resolver
	 *            Resolver instance
	 * @param id
	 *            Id of the resolver.
	 * @param label
	 *            Human readable label of the resolver or <code>bull</code>
	 * @param description
	 *            Human readable description of the resolver or <code>null</code>.
	 * @return {@link ModelResolverDescriptor} of on old {@link IModelResolver} registered with the same id.
	 */
	ModelResolverDescriptor add(IModelResolver resolver, String id, String label, String description) {
		return descriptors.put(id, new ModelResolverDescriptor(resolver, id, label, description));
	}

	/**
	 * Clear the registry.
	 */
	void clear() {
		for (ModelResolverDescriptor descriptor : descriptors.values()) {
			descriptor.getModelResolver().dispose();
		}
		descriptors.clear();
	}

	/**
	 * Get all registered {@link ModelResolverDescriptor}.
	 * 
	 * @return
	 */
	public Collection<ModelResolverDescriptor> getAllResolver() {
		return descriptors.values();
	}

	/**
	 * Get the user selected resolver.
	 * <p>
	 * The user selected resolver will be evaluated before any other ModelResolverDescriptor
	 * </p>
	 * 
	 * @return The user selected resolver.
	 */
	public ModelResolverDescriptor getUserSelectedResolver() {
		ModelResolverDescriptor result = null;
		String curentDefaultResolver = preferenceStore.get(FORCED_RESOLVER_PREF_KEY, null);
		// Get default from preferences
		if (curentDefaultResolver != null) {
			ModelResolverDescriptor modelResolverDescriptor = descriptors.get(curentDefaultResolver);
			if (modelResolverDescriptor != null) {
				result = modelResolverDescriptor;
			}
		}
		return result;
	}

	/**
	 * Return the descriptor with the specific id.
	 * 
	 * @param id
	 *            ID of the descriptor.
	 * @return ModelResolverDescriptor
	 */
	public ModelResolverDescriptor getDescriptor(String id) {
		return descriptors.get(Preconditions.checkNotNull(id));
	}

	/**
	 * Return true if the model resolving is enabled.
	 * <p>
	 * The default value is true
	 * </p>
	 * 
	 * @return True if the resolving mechanism is disabled.
	 */
	public boolean isResolutionEnable() {
		return Boolean.valueOf(preferenceStore.get(DISABLE_RESOLVING_PREF_KEY, Boolean.TRUE.toString()))
				.booleanValue();
	}

	/**
	 * Set the resolving mechanism on or off.
	 * 
	 * @param isEnabled
	 */
	public void setResolution(boolean isEnabled) {
		if (isEnabled) {
			preferenceStore.remove(DISABLE_RESOLVING_PREF_KEY);
		} else {
			preferenceStore.put(DISABLE_RESOLVING_PREF_KEY, Boolean.toString(false));
		}
		if (TracingConstant.CONFIGURATION_TRACING_ACTIVATED) {
			StringBuilder trace = new StringBuilder("Preference "); //$NON-NLS-1$
			trace.append(DISABLE_RESOLVING_PREF_KEY).append(" has been set to:\n").append( //$NON-NLS-1$
					preferenceStore.get(DISABLE_RESOLVING_PREF_KEY, "")); //$NON-NLS-1$
			EMFCompareIDEUIPlugin.getDefault().log(IStatus.INFO, trace.toString());
		}
	}

	/**
	 * Remove the ModelResolverDescriptor register with this key from the registry.
	 * 
	 * @param className
	 * @return
	 */
	ModelResolverDescriptor remove(String className) {
		return descriptors.remove(className);
	}

	/**
	 * Set the forced resolver.
	 * <p>
	 * The forced resolver will be evaluated before any other ModelResolverDescriptor
	 * </p>
	 * 
	 * @param forcedResolver
	 */
	public void setForcedResolver(ModelResolverDescriptor forcedResolver) {
		if (forcedResolver != null) {
			preferenceStore.put(FORCED_RESOLVER_PREF_KEY, forcedResolver.getId());
		} else {
			preferenceStore.remove(FORCED_RESOLVER_PREF_KEY);
		}
		if (TracingConstant.CONFIGURATION_TRACING_ACTIVATED) {
			StringBuilder trace = new StringBuilder("Preference "); //$NON-NLS-1$
			trace.append(FORCED_RESOLVER_PREF_KEY).append(" has been set to:\n").append( //$NON-NLS-1$
					preferenceStore.get(FORCED_RESOLVER_PREF_KEY, "")); //$NON-NLS-1$
			EMFCompareIDEUIPlugin.getDefault().log(IStatus.INFO, trace.toString());
		}
	}

}
