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
package org.eclipse.emf.compare.tests.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.compare.tests.EMFCompareTestPlugin;

/**
 * TestCase with useful utility methods.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class EMFCompareTestCase extends TestCase {
	/** Class file for the tests. */
	public static final String CLASS_FILE = ""; //$NON-NLS-1$

	/** The plug-in ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.tests.EMFCompareTestPlugin"; //$NON-NLS-1$

	/**
	 * Returns this plugin's root directory.
	 * 
	 * @return The plugin directory.
	 */
	public static String getPluginDirectory() {
		String path = new String();
		try {
			return new File(FileLocator.toFileURL(EMFCompareTestPlugin.getDefault().getBundle().getEntry("/")).getFile()).toString(); //$NON-NLS-1$
		} catch (IOException e) {
			// No operation, thrown if the plugin cannot be resolved.
		}

		final URL url = ClassLoader.getSystemResource(EMFCompareTestCase.CLASS_FILE);
		if (url != null) {
			String resourcePath = url.getPath();
			resourcePath = resourcePath.substring(0, resourcePath.indexOf(EMFCompareTestCase.PLUGIN_ID));
			if (resourcePath.startsWith("file:")) { //$NON-NLS-1$
				resourcePath = resourcePath.substring("file:".length()); //$NON-NLS-1$
			}
			final File parentDir = new File(resourcePath);
			if (parentDir.isDirectory()) {
				final File[] files = parentDir.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory() && files[i].getName().startsWith(EMFCompareTestCase.PLUGIN_ID)) {
						path = files[i].getAbsolutePath();
						break;
					}
				}
			}
		}

		return path;
	}

	/**
	 * Returns a file bundled inside a plugin knowing its path.
	 * 
	 * @param path
	 *            Full path of the file resolvable from the plug-in's root.
	 * @return A file bundled inside a plugin.
	 */
	public File pluginFile(final String path) {
		return new File(getPluginDirectory() + path);
	}
}
