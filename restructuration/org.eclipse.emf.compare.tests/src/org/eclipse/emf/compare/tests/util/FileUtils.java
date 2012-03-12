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
package org.eclipse.emf.compare.tests.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

/**
 * File utilities for Unit tests getting expected results from the JUnit project.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class FileUtils {
	/**
	 * Utility classes don't need to (and shouldn't) be instantiated.
	 */
	private FileUtils() {
		// prevents instantiation.
	}

	/**
	 * Lists all subdirectories contained within a given folder, with the exception of directories starting
	 * with a "." or directories named "CVS".
	 * 
	 * @param aDirectory
	 *            Directory from which we need to list subfolders.
	 * @return Array composed by all <code>aDirectory</code> subfolders.
	 */
	public static File[] listDirectories(File aDirectory) {
		File[] directories = null;

		if (aDirectory.exists() && aDirectory.isDirectory()) {
			directories = aDirectory.listFiles(new FileFilter() {
				public boolean accept(File file) {
					return file.isDirectory() && !file.getName().startsWith(".") //$NON-NLS-1$
							&& !"CVS".equals(file.getName()); //$NON-NLS-1$
				}
			});
		}
		Arrays.sort(directories);
		return directories;
	}
}
