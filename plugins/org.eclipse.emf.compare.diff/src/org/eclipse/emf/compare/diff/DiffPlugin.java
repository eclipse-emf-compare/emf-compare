/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * Activator for EMF Compare's diff plugin.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class DiffPlugin extends Plugin {
	/** This plugin's ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.diff"; //$NON-NLS-1$

	/** This plugin's shared instance. */
	private static DiffPlugin plugin;

	/** Default Constructor. */
	public DiffPlugin() {
		plugin = this;
	}

	/**
	 * Returns the plugin's shared instance.
	 * 
	 * @return The plugin's shared instance.
	 */
	public static DiffPlugin getDefault() {
		return plugin;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see AbstractUIPlugin#stop(BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
}
