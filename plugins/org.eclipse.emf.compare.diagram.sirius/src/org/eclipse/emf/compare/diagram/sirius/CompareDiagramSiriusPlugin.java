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
package org.eclipse.emf.compare.diagram.sirius;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * Activator.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class CompareDiagramSiriusPlugin extends Plugin {

	/** The plugin ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.diagram.sirius"; //$NON-NLS-1$

	/** This plug-in's shared instance. */
	private static CompareDiagramSiriusPlugin plugin;

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static CompareDiagramSiriusPlugin getDefault() {
		return plugin;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
}
