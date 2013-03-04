/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.postprocessor.PostProcessorRegistryImpl;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;
import org.eclipse.emf.compare.rcp.internal.merger.MergerExtensionRegistryListener;
import org.eclipse.emf.compare.rcp.internal.postprocessor.PostProcessorRegistryListener;
import org.osgi.framework.BundleContext;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareRCPPlugin extends Plugin {

	/** The plug-in ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.rcp"; //$NON-NLS-1$

	public static final String POST_PROCESSOR_PPID = "postProcessor"; //$NON-NLS-1$

	/** The id of the merger extension point. */
	public static final String MERGER_PPID = "merger"; //$NON-NLS-1$

	// This plugin is a singleton, so it's quite ok to keep the plugin in a static field.
	private static EMFCompareRCPPlugin plugin;

	private IMerger.Registry mergerRegistry;

	private AbstractRegistryEventListener mergerRegistryListener;

	/**
	 * The registry that will hold references to all post processors.
	 */
	private PostProcessorRegistryImpl postProcessorRegistry;

	/** The registry listener that will be used to react to post processor changes. */
	private PostProcessorRegistryListener postProcessorRegistryListener;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		EMFCompareRCPPlugin.plugin = this;

		final IExtensionRegistry registry = Platform.getExtensionRegistry();

		mergerRegistry = new IMerger.RegistryImpl();
		mergerRegistryListener = new MergerExtensionRegistryListener(PLUGIN_ID, MERGER_PPID, getLog(),
				mergerRegistry);
		registry.addListener(mergerRegistryListener, PLUGIN_ID + '.' + MERGER_PPID);
		mergerRegistryListener.readRegistry(registry);

		postProcessorRegistry = new PostProcessorRegistryImpl();
		postProcessorRegistryListener = new PostProcessorRegistryListener(PLUGIN_ID, POST_PROCESSOR_PPID,
				getLog(), postProcessorRegistry);
		registry.addListener(postProcessorRegistryListener, PLUGIN_ID + '.' + POST_PROCESSOR_PPID);
		postProcessorRegistryListener.readRegistry(registry);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		EMFCompareRCPPlugin.plugin = null;

		final IExtensionRegistry registry = Platform.getExtensionRegistry();

		registry.removeListener(postProcessorRegistryListener);
		postProcessorRegistryListener = null;
		postProcessorRegistry = null;

		registry.removeListener(mergerRegistryListener);
		mergerRegistryListener = null;
		mergerRegistry = null;
	}

	/**
	 * Returns the merger registry to which extension will be registered.
	 * 
	 * @return the merger registry to which extension will be registered
	 * @since 3.0
	 */
	public IMerger.Registry getMergerRegistry() {
		return mergerRegistry;
	}

	/**
	 * Returns the post processor registry to which extension will be registered.
	 * 
	 * @return the post processor registry to which extension will be registered
	 */
	public PostProcessorRegistryImpl getPostProcessorRegistry() {
		return postProcessorRegistry;
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
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static EMFCompareRCPPlugin getDefault() {
		return plugin;
	}

}
