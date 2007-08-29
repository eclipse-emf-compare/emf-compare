/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.examples.diff.extension;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.compare.match.metamodel.provider.MatchEditPlugin;
import org.eclipse.emf.ecore.provider.EcoreEditPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * This is the central singleton for the Diff edit plugin.
 */
@SuppressWarnings("nls")
public final class DiffExtensionPlugin extends EMFPlugin {
	/**
	 * Keep track of the singleton.
	 */
	public static final DiffExtensionPlugin INSTANCE = new DiffExtensionPlugin();

	/**
	 * Keep track of the singleton. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 */
	static Implementation plugin;

	/**
	 * Create the instance.
	 */
	public DiffExtensionPlugin() {
		super(new ResourceLocator[] { EcoreEditPlugin.INSTANCE,
				MatchEditPlugin.INSTANCE, });
	}

	/**
	 * Returns the singleton instance of the Eclipse plugin.
	 * 
	 * @return the singleton instance.
	 */
	@Override
	public ResourceLocator getPluginResourceLocator() {
		return plugin;
	}

	/**
	 * Returns the singleton instance of the Eclipse plugin.
	 * 
	 * @return the singleton instance.
	 */
	public static Implementation getPlugin() {
		return plugin;
	}

	/**
	 * The actual implementation of the Eclipse <b>Plugin</b>.
	 */
	public static class Implementation extends EclipsePlugin {
		/**
		 * Creates an instance.
		 */
		public Implementation() {
			super();

			// Remember the static instance.
			//
			plugin = this;
		}

		private Map imageMap = new HashMap();

		public ImageDescriptor findImageDescriptor(String path) {
			Bundle bundle = Platform.getBundle(getSymbolicName());
			URL imagePath = bundle.getEntry(path);

			return ImageDescriptor.createFromURL(imagePath);
		}

		public Object getBundleImage(String path) {
			Object result = imageMap.get(path);
			if (result == null) {
				result = findImageDescriptor(path).createImage();
				imageMap.put(path, result);
			}
			return result;
		}

		/**
		 * @override
		 */
		public void stop(BundleContext context) throws Exception {
			super.stop(context);

			Iterator imageIterator = imageMap.keySet().iterator();
			while (imageIterator.hasNext()) {
				Image image = (Image) imageMap.get(imageIterator.next());
				image.dispose();
			}
		}
	}

	public Object getBundleImage(String path) {
		return plugin.getBundleImage(path);
	}

}
