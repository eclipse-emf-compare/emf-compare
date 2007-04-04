/*  
 * Copyright (c) 2006, Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */

package org.eclipse.emf.compare.match.statistic;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * 
 * The activator class controls the plug-in life cycle
 */
public class MatchingSimilarityPlugin extends Plugin {

	/**
	 * 
	 * @return the plugin ID
	 */
	public String getID() {
		return PLUGIN_ID;
	}
	
	/**
	 * the plugin ID
	 */
	public static final String PLUGIN_ID = "fr.obeo.mda.evolution.detection.similarity";

	// The shared instance
	private static MatchingSimilarityPlugin plugin;
	
	/**
	 * The constructor
	 */
	public MatchingSimilarityPlugin() {
		plugin = this;
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static MatchingSimilarityPlugin getDefault() {
		return plugin;
	}

}
