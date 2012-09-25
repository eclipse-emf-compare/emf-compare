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
package org.eclipse.emf.compare.ide.ui.tests.suite;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.textui.TestRunner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.osgi.framework.Bundle;

@RunWith(Suite.class)
@SuiteClasses({})
public class AllTests {
	private static final String TEST_PROJECT_NAME = "testProject"; //$NON-NLS-1$

	private static final String INPUT_FOLDER_NAME = "inputs"; //$NON-NLS-1$

	private static final IProgressMonitor MONITOR = new NullProgressMonitor();

	/**
	 * Launches the test with the given arguments.
	 * 
	 * @param args
	 *            Arguments of the testCase.
	 */
	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	/**
	 * Creates the {@link junit.framework.TestSuite TestSuite} for all the test.
	 * 
	 * @return The test suite containing all the tests
	 */
	public static Test suite() {
		return new JUnit4TestAdapter(AllTests.class);
	}

	@BeforeClass
	public static void createTestProject() {
		// Setting up the test project and its content
		IProject testProject = ResourcesPlugin.getWorkspace().getRoot().getProject(TEST_PROJECT_NAME);
		try {
			testProject.create(MONITOR);
			testProject.open(MONITOR);
			IFolder inputFolder = testProject.getFolder(INPUT_FOLDER_NAME);
			inputFolder.create(true, true, MONITOR);
			createEcoreFolder(inputFolder);
		} catch (CoreException e) {
			fail("Couldn't create test project '" + testProject + '\''); //$NON-NLS-1$
		}
	}

	private static void createEcoreFolder(IFolder inputFolder) throws CoreException {
		final String folderName = "ecore"; //$NON-NLS-1$
		final String[] models = new String[] {"books.ecore", "library.ecore", "writers.ecore", }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		IFolder ecoreFolder = inputFolder.getFolder(folderName);
		ecoreFolder.create(true, true, MONITOR);
		for (String model : models) {
			IFile modelFile = ecoreFolder.getFile(model);
			InputStream stream = null;
			try {
				stream = getModelInputStream(INPUT_FOLDER_NAME + '/' + folderName + '/' + model);
				modelFile.create(stream, true, MONITOR);
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						// Swallow this
					}
				}
			}
		}
	}

	private static InputStream getModelInputStream(String modelPath) {
		final String bundleName = "org.eclipse.emf.compare.ide.ui.tests"; //$NON-NLS-1$

		Bundle ideTestsBundle = Platform.getBundle(bundleName);
		URL entry = ideTestsBundle.getEntry(modelPath);
		if (entry != null) {
			try {
				return entry.openStream();
			} catch (IOException e) {
				// Test will fail out of the "if"
			}
		}

		fail("Couldn't retrieve test model '" + modelPath + '\''); //$NON-NLS-1$
		// Keeps compiler happy, but cannot be reached
		return null;
	}
}
