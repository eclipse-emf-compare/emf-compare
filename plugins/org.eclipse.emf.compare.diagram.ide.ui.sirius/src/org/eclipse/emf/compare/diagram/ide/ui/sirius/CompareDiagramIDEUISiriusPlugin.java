/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.sirius;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class CompareDiagramIDEUISiriusPlugin extends AbstractUIPlugin {

	/** ID of this plug-in. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.diagram.ide.ui.sirius"; //$NON-NLS-1$

	/** The shared instance. */
	private static CompareDiagramIDEUISiriusPlugin plugin;

	/**
	 * The constructor.
	 */
	public CompareDiagramIDEUISiriusPlugin() {
		// Do nothing
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
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static CompareDiagramIDEUISiriusPlugin getDefault() {
		return plugin;
	}

}
