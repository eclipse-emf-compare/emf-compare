/*******************************************************************************
 * Copyright (c) 2006, Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * Activator for the EMF Compare's UI plugin.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class EMFCompareUIPlugin extends AbstractUIPlugin {
	/** This plugin's ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.ui"; //$NON-NLS-1$
	
	/** This plugin's shared instance. */
	private static EMFCompareUIPlugin plugin;
	
	/** Default Constructor. */
	public EMFCompareUIPlugin() {
		super();
		plugin = this;
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
	
	/**
	 * Returns the plugin's shared instance.
	 * 
	 * @return
	 * 			The plugin's shared instance.
	 */
	public static EMFCompareUIPlugin getDefault() {
		return plugin;
	}
}
