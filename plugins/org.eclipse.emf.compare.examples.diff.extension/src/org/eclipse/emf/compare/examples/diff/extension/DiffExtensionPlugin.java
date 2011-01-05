/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
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
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.compare.match.metamodel.provider.MatchEditPlugin;
import org.eclipse.emf.compare.util.EMFCompareMap;
import org.eclipse.emf.ecore.provider.EcoreEditPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * This is the central singleton for the Diff edit plugin.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class DiffExtensionPlugin extends EMFPlugin {
	/**
	 * Keep track of the singleton.
	 */
	public static final DiffExtensionPlugin INSTANCE = new DiffExtensionPlugin();

	/**
	 * Keep track of the singleton. <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	static Implementation plugin;

	/**
	 * Create the instance.
	 */
	public DiffExtensionPlugin() {
		super(new ResourceLocator[] {EcoreEditPlugin.INSTANCE, MatchEditPlugin.INSTANCE, });
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
	 * TODOCBR comment.
	 * 
	 * @param path
	 *            comment.
	 * @return comment.
	 */
	public Object getBundleImage(String path) {
		return plugin.getBundleImage(path);
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
	 * The actual implementation of the Eclipse <b>Plugin</b>.
	 */
	public static class Implementation extends EclipsePlugin {
		/** TODOCBR comment. */
		private Map<String, Object> imageMap = new EMFCompareMap<String, Object>();

		/**
		 * Creates an instance.
		 */
		public Implementation() {
			super();

			// Remember the static instance.
			//
			plugin = this;
		}

		/**
		 * TODOCBR comment.
		 * 
		 * @param path
		 *            comment.
		 * @return comment.
		 */
		public ImageDescriptor findImageDescriptor(String path) {
			final Bundle bundle = Platform.getBundle(getSymbolicName());
			final URL imagePath = bundle.getEntry(path);

			return ImageDescriptor.createFromURL(imagePath);
		}

		/**
		 * TODOCBR comment.
		 * 
		 * @param path
		 *            comment.
		 * @return comment.
		 */
		public Object getBundleImage(String path) {
			Object result = imageMap.get(path);
			if (result == null) {
				result = findImageDescriptor(path).createImage();
				imageMap.put(path, result);
			}
			return result;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
		 */
		@Override
		public void stop(BundleContext context) throws Exception {
			super.stop(context);

			// TODOCBR "keySet" instead of "values"?
			final Iterator<String> imageIterator = imageMap.keySet().iterator();
			while (imageIterator.hasNext()) {
				final Image image = (Image)imageMap.get(imageIterator.next());
				image.dispose();
			}
		}
	}

}
