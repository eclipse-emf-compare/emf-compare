/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui;

import com.google.common.collect.Maps;

import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class, controls the plug-in life cycle.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFCompareIDEUIPlugin extends AbstractUIPlugin {
	/** The plugin ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.ide.ui"; //$NON-NLS-1$

	/** Plug-in's shared instance. */
	private static EMFCompareIDEUIPlugin plugin;

	/** Caches the images that were loaded by EMF Compare. */
	private Map<String, Image> imageMap = Maps.newHashMap();

	/**
	 * Default constructor.
	 */
	public EMFCompareIDEUIPlugin() {
		// Empty implementation
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
		for (Image image : imageMap.values()) {
			image.dispose();
		}
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance.
	 */
	public static EMFCompareIDEUIPlugin getDefault() {
		return plugin;
	}

	/**
	 * Loads an image from this plugin's path and returns it.
	 * 
	 * @param path
	 *            Path to the image we are to load.
	 * @return The loaded image.
	 */
	public Image getImage(String path) {
		Image result = imageMap.get(path);
		if (result != null) {
			return result;
		}
		final ImageDescriptor descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(
				EMFCompareIDEUIPlugin.PLUGIN_ID, path);
		if (descriptor != null) {
			result = descriptor.createImage();
			imageMap.put(path, result);
		}
		return result;
	}
}
