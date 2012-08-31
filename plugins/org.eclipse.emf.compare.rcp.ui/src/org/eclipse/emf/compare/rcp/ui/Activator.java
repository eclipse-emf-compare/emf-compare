/** ****************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends AbstractUIPlugin {

	/**
	 * The plug-in ID.
	 */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.rcp.ui"; //$NON-NLS-1$

	/**
	 * the shared instance.
	 */
	private static Activator plugin;

	/** keep track of resources that should be freed when exiting. */
	private static Map<String, Image> resourcesMapper = new HashMap<String, Image>();

	/**
	 * The constructor.
	 */
	public Activator() {

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		disposeCachedImages();
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * <p>
	 * returns a plugin image. The returned image does not need to be explicitly disposed.
	 * </p>
	 * 
	 * @param imagePath
	 *            : plugin relative path to the image
	 * @return Image : plugin hosted image
	 */
	public static Image getImage(String imagePath) {
		Image image = resourcesMapper.get(imagePath);
		if (image == null) {
			ImageDescriptor imageDescriptor = imageDescriptorFromPlugin(PLUGIN_ID, imagePath);
			image = imageDescriptor.createImage();
			resourcesMapper.put(imagePath, image);
		}
		return image;
	}

	/**
	 * <p>
	 * returns a plugin image descriptor.
	 * </p>
	 * 
	 * @param imagePath
	 *            : plugin relative path to the image
	 * @return ImageDescriptor : image descriptor.
	 */
	public static ImageDescriptor getImageDescriptor(String imagePath) {
		return imageDescriptorFromPlugin(PLUGIN_ID, imagePath);
	}

	/**
	 * Dispose image with the given id.
	 * 
	 * @param id
	 *            : dispose system resources associated with the image with the given id.
	 */
	public static void disposeImage(String id) {
		Image image = resourcesMapper.remove(id);
		if (image != null) {
			image.dispose();
		}
	}

	/**
	 * dispose system resources associated with cached images.
	 */
	public static void disposeCachedImages() {
		Iterator<Image> iterator = resourcesMapper.values().iterator();
		while (iterator.hasNext()) {
			iterator.next().dispose();
		}
		resourcesMapper.clear();
	}

}
