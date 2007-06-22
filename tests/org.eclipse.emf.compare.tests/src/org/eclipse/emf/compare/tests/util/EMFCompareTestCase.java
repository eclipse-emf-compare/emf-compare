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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.emf.compare.tests.EMFCompareTestPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

/**
 * TestCase with useful utility methods.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class EMFCompareTestCase extends TestCase {
	/** The plug-in ID. */
	public static final String PLUGIN_ID = ""; // TODO check that Id //$NON-NLS-1$

	/** Class file for the tests. */
	public static final String CLASS_FILE = ""; //$NON-NLS-1$
	
	protected Date start;

	/**
	 * Assert file contents are strictly equals.
	 * 
	 * @param file1
	 * 			First file of the comparison.
	 * @param file2
	 * 			Second file of the comparison.
	 */
	public void assertFileContentsEqual(final File file1, final File file2) {
		assertTrue("File contents are not equals for " + file1.getName() //$NON-NLS-1$
				+ " and " + file2.getName(), EMFCompareTestCase.readFile(file1, //$NON-NLS-1$
				false).equals(EMFCompareTestCase.readFile(file2, false)));
	}

	/**
	 * Save a model in a file.
	 * 
	 * @param root
	 * 			Root of the objects to be serialized in a file.
	 * @param outputFile
	 * 			File where the objects have to be saved.
	 * @throws IOException
	 * 			Thrown if an I/O operation has failed or been interrupted during the
	 * 			saving process.
	 */
	@SuppressWarnings("unchecked")
	public void save(final EObject root, final File outputFile) throws IOException {
		final FileOutputStream out = new FileOutputStream(outputFile);

		final XMIResourceImpl resource = new XMIResourceImpl();
		resource.getContents().add(root);
		resource.save(out, Collections.EMPTY_MAP);
	}

	/**
	 * Returns a file bundled inside a plugin knowing its path.
	 * 
	 * @param path
	 * 			Full path of the file resolvable from the plug-in's root.
	 * @return
	 * 			A file bundled inside a plugin.
	 */
	public File pluginFile(final String path) {
		return new File(getPluginDirectory() + path);
	}

	/**
	 * Compare the contents of the files of two directories given a filename suffix.
	 * 
	 * @param dir1
	 * 			First directory of the comparison.
	 * @param dir2
	 * 			Second directory of the comparison.
	 * @param fileNameSuffixExpected
	 * 			Suffix of the files to be compared.
	 */
	public void compareDirs(final File dir1, final File dir2,
			final String fileNameSuffixExpected) {
		if (!dir1.isDirectory() || !dir2.isDirectory()) {
			throw new RuntimeException("dir1 and dir2 are not folders! "); //$NON-NLS-1$
		}

		// saving files from dir2 in a hashmap
		final HashMap<String, File> files2 = new HashMap<String, File>();
		for (int i = 0; i < dir2.listFiles().length; i++) {
			files2.put(dir2.listFiles()[i].getName(), dir2.listFiles()[i]);
		}

		for (int i = 0; i < dir1.listFiles().length; i++) {
			final File file1 = dir1.listFiles()[i];
			if (files2.containsKey(file1.getName() + fileNameSuffixExpected)) {
				assertFileContentsEqual(file1, (File)files2.get(file1
						.getName() + fileNameSuffixExpected));
			} else {
				throw new RuntimeException(file1.getName()
						+ fileNameSuffixExpected + " is missing for comparison"); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Deletes a given file.
	 * 
	 * @param file
	 * 			{@link java.io.File File} to be deleted.
	 */
	public static void delete(final File file) {
		if (file.isDirectory()) {
			final File[] children = file.listFiles();
			for (int i = 0; i < children.length; i++) {
				delete(children[i]);
			}
		}

		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * Reads a given file and serializes it as a {@link java.lang.String String}.
	 * 
	 * @param file
	 * 			File to read.
	 * @param useSystemLineSeparator
	 * 			<code>True</code> if the serialized String should use the System's line
	 * 			separator, <code>false</code> otherwise.
	 * @return
	 * 			The file contents as a String.
	 */
	public static String readFile(final File file, final boolean useSystemLineSeparator) {
		final StringBuffer stringBuffer = new StringBuffer();
		try {
			final BufferedReader in = new BufferedReader(new FileReader(file));
			try {
				int size = 0;
				final int bufferSize = 512;
				final char[] buff = new char[bufferSize];
				while ((size = in.read(buff)) >= 0) {
					stringBuffer.append(buff, 0, size);
				}
			} finally {
				if (in != null) {
					in.close();
				}
			}
		} catch (final IOException exception) {
			throw new RuntimeException(exception);
		}
		
		String nl = "\n"; //$NON-NLS-1$
		if (useSystemLineSeparator)
			nl = System.getProperties().getProperty("line.separator"); //$NON-NLS-1$
		
		return stringBuffer.toString().replaceAll("\\r\\n", "\n") //$NON-NLS-1$ //$NON-NLS-2$
				.replaceAll("[\\n|\\r]", nl); //$NON-NLS-1$
	}

	/**
	 * Returns the given plugin's root directory.
	 * 
	 * @param pluginID
	 * 			ID of the plugin.
	 * @return
	 * 			The plugin's root directory.
	 */
	public static String getPluginDirectory(final String pluginID) {
		String path = new String();
		try {
			if (Platform.isRunning()) {
				final File file = new File(FileLocator.toFileURL(
						Platform.getBundle(pluginID).getEntry("/")).getFile()); //$NON-NLS-1$
				if (file.isDirectory())
					return file.getAbsolutePath();
			}
		} catch (IOException e) {
			// No operation, thrown if the given plugin cannot be resolved.
		}

		final File parentDirectory = new File(getPluginDirectory());
		final File[] plugins = parentDirectory.listFiles();
		for (int i = 0; i < plugins.length; i++) {
			if (plugins[i].isDirectory()) {
				final String name = plugins[i].getName();
				if (name.equals(pluginID) || name.startsWith(pluginID + "_")) { //$NON-NLS-1$
					path = plugins[i].getAbsolutePath();
				}
			}
		}

		return path;
	}

	/**
	 * Returns the plugin's shared instance.
	 * 
	 * @return
	 * 			The plugin's shared instance.
	 */
	public static Plugin getPlugin() {
		return EMFCompareTestPlugin.getDefault();
	}

	/**
	 * Returns this plugin's root directory.
	 * 
	 * @return
	 * 			The plugin directory.
	 */
	public static String getPluginDirectory() {
		String path = new String();
		try {
			return new File(FileLocator.toFileURL(
					getPlugin().getBundle().getEntry("/")).getFile()).toString(); //$NON-NLS-1$
		} catch (IOException e) {
			// No operation, thrown if the plugin cannot be resolved.
		}

		final URL url = ClassLoader
				.getSystemResource(EMFCompareTestCase.CLASS_FILE);
		if (url != null) {
			String resourcePath = url.getPath();
			resourcePath = resourcePath
					.substring(0, resourcePath.indexOf(EMFCompareTestCase.PLUGIN_ID));
			if (resourcePath.startsWith("file:")) { //$NON-NLS-1$
				resourcePath = resourcePath.substring("file:".length()); //$NON-NLS-1$
			}
			final File parentDir = new File(resourcePath);
			if (parentDir.isDirectory()) {
				final File[] files = parentDir.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory() &&
							files[i].getName().startsWith(EMFCompareTestCase.PLUGIN_ID)) {
						path = files[i].getAbsolutePath();
						break;
					}
				}
			}
		}

		return path;
	}

	/**
	 * Starts the current timer. Allows counting the number of milliseconds ellapsed
	 * in the process of retrieving the adapter factory.
	 */
	public void startTimer() {
		start = Calendar.getInstance().getTime();
	}

	/**
	 * Stops the timer and return the number of milliseconds elapsed.
	 * 
	 * @return
	 * 			The number of milliseconds elapsed.
	 */
	public long endTimer() {
		final Date end = Calendar.getInstance().getTime();
		return end.getTime() - start.getTime();
	}
}
