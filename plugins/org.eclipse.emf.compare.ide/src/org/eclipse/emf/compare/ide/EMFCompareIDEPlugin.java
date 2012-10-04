/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
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
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.emf.compare.extension.PostProcessorRegistry;
import org.eclipse.emf.compare.ide.internal.extension.PostProcessorRegistryListener;
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

	/**
	 * The registry that will hold references to all post processors.
	 */
	private PostProcessorRegistry postProcessorRegistry;

	/** The registry listener that will be used to react to post processor changes. */
	private PostProcessorRegistryListener postProcessorListener;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		plugin = this;
		super.start(context);

		this.postProcessorRegistry = new PostProcessorRegistry();
		this.postProcessorListener = new PostProcessorRegistryListener(postProcessorRegistry);

		final IExtensionRegistry registry = Platform.getExtensionRegistry();

		registry.addListener(postProcessorListener,
				PostProcessorRegistryListener.POST_PROCESSOR_EXTENSION_POINT);
		postProcessorListener.parseInitialContributions();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;

		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.removeListener(postProcessorListener);
	}

	/**
	 * Returns the post processor registry to which extension will be registered.
	 * 
	 * @return the post processor registry to which extension will be registered
	 */
	public PostProcessorRegistry getPostProcessorRegistry() {
		return postProcessorRegistry;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static EMFCompareIDEPlugin getDefault() {
		return plugin;
	}
}
