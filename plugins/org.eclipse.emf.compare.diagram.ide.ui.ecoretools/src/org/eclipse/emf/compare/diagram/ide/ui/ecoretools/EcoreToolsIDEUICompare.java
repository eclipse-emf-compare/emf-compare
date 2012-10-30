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
package org.eclipse.emf.compare.diagram.ide.ui.ecoretools;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * Activator.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public class EcoreToolsIDEUICompare extends Plugin {

	/** The plug-in ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.diagram.ide.ui.ecoretools"; //$NON-NLS-1$

	/**
	 * Context.
	 */
	private static EcoreToolsIDEUICompare plugin;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		plugin = this;
		super.start(bundleContext);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		plugin = null;
	}

}
