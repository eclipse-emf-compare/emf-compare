/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.unit.core.util.modelutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.compare.tests.EMFCompareTestPlugin;
import org.eclipse.emf.compare.tests.util.FileUtils;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * Tests the behavior of {@link ModelUtils#save(EObject, String)} and {@link ModelUtils#serialize(EObject)}.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings("nls")
public class TestSaveSerialize extends TestCase {
	/** Full path to the directory containing the non-regression models. */
	private static final String INPUT_DIRECTORY = "/inputs";

	/**
	 * This array contains references to the root of all the models contained by {@link INPUT_DIRECTORY} or
	 * its subfolders.
	 */
	private List<EObject> models = new ArrayList<EObject>();

	/** Full path to the directory where will put the temporary saved files. */
	private String outputDirectory;

	/**
	 * Default constructor. Scans for model files in {@link #INPUT_DIRECTORY}.
	 */
	public TestSaveSerialize() {
		File inputDir = null;
		try {
			inputDir = new File(FileLocator.toFileURL(
					EMFCompareTestPlugin.getDefault().getBundle().getEntry(INPUT_DIRECTORY)).getFile());
			outputDirectory = FileLocator.toFileURL(
					EMFCompareTestPlugin.getDefault().getBundle().getEntry("/data")).getFile();
		} catch (IOException e) {
			// shouldn't happen
			assert false;
		}

		final File[] directories = FileUtils.listDirectories(inputDir);
		if (directories != null) {
			for (int i = 0; i < directories.length; i++) {
				scanForModels(directories[i]);
			}
		}
	}

	/**
	 * Tests {@link ModelUtils#save(EObject, String)} with <code>null</code> as the object to save. Expects
	 * a {@link NullPointerException} to be thrown.
	 */
	public void testSaveNullRoot() {
		try {
			ModelUtils.save(null, outputDirectory);
			fail("Expected NullPointerException hasn't been thrown by save().");
		} catch (NullPointerException e) {
			// We were expecting this
		} catch (IOException e) {
			fail("Unexpected IOException has been thrown by save().");
		}
	}

	/**
	 * Tests {@link ModelUtils#save(EObject, String)} with a valid EObject and a valid path where it has to be
	 * saved. Expects a non-empty File to be created at the specified path.
	 */
	public void testSaveValidEObject() {
		for (EObject modelRoot : models) {
			try {
				final String filePath = modelRoot.eResource().getURI().toFileString();
				final String filename = filePath.substring(filePath.lastIndexOf(File.separatorChar) + 1);
				ModelUtils.save(modelRoot, outputDirectory + File.separatorChar + filename);

				final File savedFile = new File(outputDirectory + File.separatorChar + filename);
				assertTrue("File hasn't been saved.", savedFile.exists() && savedFile.isFile());

				final FileInputStream fsInput = new FileInputStream(savedFile);
				assertNotSame("Saved file is empty.", -1, fsInput.read());

				// Cleans up before next loop
				fsInput.close();
				savedFile.delete();
			} catch (IOException e) {
				fail("Unexpected IOException has been thrown by a valid call to save().");
			}
		}
	}

	/**
	 * Tests {@link ModelUtils#save(EObject, String)} with <code>null</code> as the path where to save.
	 * Expects a {@link NullPointerException} to be thrown.
	 */
	public void testSaveValidEObjectNullPath() {
		for (EObject modelRoot : models) {
			try {
				ModelUtils.save(modelRoot, null);
				fail("Expected NullPointerException hasn't been thrown by save().");
			} catch (NullPointerException e) {
				// We were expecting this
			} catch (IOException e) {
				fail("Unexpected IOException has been thrown by save().");
			}
		}
	}

	/**
	 * Tests {@link ModelUtils#serialize(EObject)} with <code>null</code> as the object to serialize.
	 * Expects a {@link NullPointerException} to be thrown.
	 */
	public void testSerializeNullRoot() {
		try {
			ModelUtils.serialize(null);
			fail("Expected NullPointerException hasn't been thrown by serialize() operation.");
		} catch (NullPointerException e) {
			// We were expecting this
		} catch (IOException e) {
			fail("Unexpected IOException has been thrown by serialize() operation.");
		}
	}

	/**
	 * Tests {@link ModelUtils#serialize(EObject)} with a valid EObject to serialize. Expects a non-empty
	 * String to be returned.
	 */
	public void testSerializeValidEObject() {
		for (EObject modelRoot : models) {
			try {
				final String result = ModelUtils.serialize(modelRoot);

				assertNotNull("EObject hasn't been serialized by serialize().", result);
				assertFalse("EObject has been serialized as an empty String", result.equals(""));
				assertTrue("EObject hasn't been serialized as an XML object.", result.startsWith("<?xml"));
			} catch (IOException e) {
				fail("Unexpected IOException has been thrown by a valid call to save().");
			}
		}
	}

	/**
	 * Called from initializer, this method allows retrieval of references to the files corresponding to the
	 * non-regression models.
	 * 
	 * @param folder
	 *            Folder in which model files are to be found.
	 */
	private void scanForModels(File folder) {
		// Ignores the folder containing non-standard models (uml, gmfgen, ...)
		if (folder.getName().contains("nonstd"))
			return;

		final File[] subFolders = FileUtils.listDirectories(folder);
		if (subFolders.length != 0) {
			for (File aSubFolder : subFolders) {
				scanForModels(aSubFolder);
			}
		} else if (folder.exists() && folder.isDirectory()) {
			final File[] files = folder.listFiles();
			for (File aFile : files) {
				// All files are considered models regardless of their extension
				if (!aFile.isDirectory() && !aFile.getName().startsWith(".")) {
					try {
						models.add(ModelUtils.load(aFile, new ResourceSetImpl()));
					} catch (IOException e) {
						// Shouldn't happen
						assert false;
					}
				}
			}
		}
	}
}
