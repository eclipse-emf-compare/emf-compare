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
package org.eclipse.emf.compare.ui;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.util.EMFComparePreferenceKeys;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * Activator for the EMF Compare's UI plugin.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
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
	 * Returns the plugin's shared instance.
	 * 
	 * @return The plugin's shared instance.
	 */
	public static EMFCompareUIPlugin getDefault() {
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
		EMFComparePlugin.getDefault().getPluginPreferences().setValue(
				EMFComparePreferenceKeys.PREFERENCES_KEY_SEARCH_WINDOW,
				getPreferenceStore().getInt(EMFComparePreferenceKeys.PREFERENCES_KEY_SEARCH_WINDOW));
		EMFComparePlugin.getDefault().getPluginPreferences().setValue(
				EMFComparePreferenceKeys.PREFERENCES_KEY_IGNORE_ID,
				getPreferenceStore().getBoolean(EMFComparePreferenceKeys.PREFERENCES_KEY_IGNORE_ID));
		EMFComparePlugin.getDefault().getPluginPreferences().setValue(
				EMFComparePreferenceKeys.PREFERENCES_KEY_IGNORE_XMIID,
				getPreferenceStore().getBoolean(EMFComparePreferenceKeys.PREFERENCES_KEY_IGNORE_XMIID));
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
