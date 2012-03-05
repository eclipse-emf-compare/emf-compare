/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.diagram.provider.internal.ViewLabelProviderExtensionRegistry;
import org.eclipse.emf.compare.diagram.provider.internal.ViewLabelProviderRegistryListener;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class GMFCompare extends AbstractUIPlugin {
	/** The plug-in ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.diagram"; //$NON-NLS-1$

	/** This plug-in's shared instance. */
	private static GMFCompare plugin;

	/** The registry listener that will be used to listen to extension changes. */
	private ViewLabelProviderRegistryListener registryListener = new ViewLabelProviderRegistryListener();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		plugin = this;
		super.start(context);

		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.addListener(registryListener, ViewLabelProviderRegistryListener.EXTENSION_ID);
		registryListener.parseInitialContributions();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.removeListener(registryListener);
		ViewLabelProviderExtensionRegistry.INSTANCE.clearRegistry();

		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static GMFCompare getDefault() {
		return plugin;
	}

}
