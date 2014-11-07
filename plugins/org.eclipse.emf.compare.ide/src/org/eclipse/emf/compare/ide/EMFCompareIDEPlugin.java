/*******************************************************************************
 * Copyright (c) 2011, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.ide.internal.hook.ResourceSetHookRegistry;
import org.eclipse.emf.compare.ide.internal.hook.ResourceSetHookRegistryListener;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFCompareIDEPlugin extends Plugin {
	/** The plug-in ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.ide"; //$NON-NLS-1$

	/** This plugin's shared instance. */
	private static EMFCompareIDEPlugin plugin;

	/** Registry of {@link org.eclipse.emf.compare.rcp.internal.hook.IResourceSetHook}. */
	private ResourceSetHookRegistry resourceSetHookRegistry;

	/** The registry listener that will fill the {@link ResourceSetHookRegistry}. */
	private ResourceSetHookRegistryListener resourceSetHookRegistryListener;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		plugin = this;
		super.start(context);

		final IExtensionRegistry registry = Platform.getExtensionRegistry();

		setUpResourceSetHookRegistry(registry);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;

		final IExtensionRegistry registry = Platform.getExtensionRegistry();

		discardResourceSetHookRegistry(registry);

		super.stop(context);
	}

	/**
	 * Log the given message with the given severity to the logger of this plugin.
	 * 
	 * @param severity
	 *            the severity of the message.
	 * @param message
	 *            the message to log.
	 */
	public void log(int severity, String message) {
		getLog().log(new Status(severity, PLUGIN_ID, message));
	}

	/**
	 * Log the given exception to the logger of this plugin.
	 * 
	 * @param throwable
	 *            the throwable to log.
	 */
	public void log(Throwable throwable) {
		getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, throwable.getMessage(), throwable));
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static EMFCompareIDEPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the {@link ResourceSetHookRegistry}.
	 * <p>
	 * It contains all hooks registered against the ResourceSetHook extension point.
	 * </p>
	 * 
	 * @return {@link ResourceSetHookRegistry}.
	 * @since 3.2
	 */
	public ResourceSetHookRegistry getResourceSetHookRegistry() {
		return resourceSetHookRegistry;
	}

	/**
	 * Sets up the {@link ResourceSetHookRegistry}.
	 * 
	 * @param registry
	 *            {@link IExtensionRegistry} to listen in order to fill the registry
	 */
	private void setUpResourceSetHookRegistry(IExtensionRegistry registry) {
		// Sets up the resource set hook registry
		resourceSetHookRegistry = new ResourceSetHookRegistry();
		resourceSetHookRegistryListener = new ResourceSetHookRegistryListener(getLog(),
				resourceSetHookRegistry);
		registry.addListener(resourceSetHookRegistryListener, PLUGIN_ID + '.'
				+ ResourceSetHookRegistryListener.EXT_ID);
		resourceSetHookRegistryListener.readRegistry(registry);

	}

	/**
	 * Discards the resource set hook registry.
	 * 
	 * @param registry
	 *            IExtensionRegistry to remove listener
	 */
	private void discardResourceSetHookRegistry(IExtensionRegistry registry) {
		// Unregisters the resource set hook registry
		registry.removeListener(resourceSetHookRegistryListener);
		resourceSetHookRegistryListener = null;
		resourceSetHookRegistry = null;
	}

}
