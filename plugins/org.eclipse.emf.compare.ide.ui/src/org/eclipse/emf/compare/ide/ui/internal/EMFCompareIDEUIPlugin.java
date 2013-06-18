/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.ide.ui.internal.logical.IModelResolverRegistry;
import org.eclipse.emf.compare.ide.ui.internal.logical.ModelResolverRegistryImpl;
import org.eclipse.emf.compare.ide.ui.internal.logical.ModelResolverRegistryListener;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class, controls the plug-in life cycle.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFCompareIDEUIPlugin extends AbstractUIPlugin {
	/** The plugin ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.ide.ui"; //$NON-NLS-1$

	/** Plug-in's shared instance. */
	private static EMFCompareIDEUIPlugin plugin;

	/** Model resolvers extension point. */
	private static final String MODEL_RESOLVER_PPID = "modelResolvers"; //$NON-NLS-1$

	/** Listener for the model resolver extension point. */
	private AbstractRegistryEventListener modelResolverRegistryListener;

	/** Registry of model resolvers. */
	private IModelResolverRegistry modelResolverRegistry;

	/** Default constructor. */
	public EMFCompareIDEUIPlugin() {
		// Empty constructor
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		final IExtensionRegistry globalRegistry = Platform.getExtensionRegistry();
		modelResolverRegistry = new ModelResolverRegistryImpl();
		modelResolverRegistryListener = new ModelResolverRegistryListener(PLUGIN_ID, MODEL_RESOLVER_PPID,
				getLog(), modelResolverRegistry);
		globalRegistry.addListener(modelResolverRegistryListener);
		modelResolverRegistryListener.readRegistry(globalRegistry);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		final IExtensionRegistry globalRegistry = Platform.getExtensionRegistry();
		globalRegistry.removeListener(modelResolverRegistryListener);
		modelResolverRegistry.clear();

		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance.
	 */
	public static EMFCompareIDEUIPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the registry containing all known model resolvers.
	 * 
	 * @return The registry containing all known model resolvers.
	 */
	public IModelResolverRegistry getModelResolverRegistry() {
		return modelResolverRegistry;
	}

	/**
	 * Log an {@link Exception} in the {@link #getLog() current logger}.
	 * 
	 * @param e
	 *            the exception to be logged.
	 */
	public void log(Throwable e) {
		getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, e.getMessage(), e));
	}
}
