/*******************************************************************************
 * Copyright (c) 2006, Intalio Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Intalio Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.legacy;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class CompareEngineUIPlugin extends AbstractUIPlugin {

	// The plug-in ID
	/**
	 * 
	 */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.ui.legacy";

	// The shared instance
	private static CompareEngineUIPlugin plugin;

	/**
	 * The constructor
	 */
	public CompareEngineUIPlugin() {
		plugin = this;
	}

	/**
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static CompareEngineUIPlugin getDefault() {
		return plugin;
	}

}
