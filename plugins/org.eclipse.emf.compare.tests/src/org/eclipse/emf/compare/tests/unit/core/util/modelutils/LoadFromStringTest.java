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
package org.eclipse.emf.compare.tests.unit.core.util.modelutils;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.emf.compare.tests.EMFCompareTestPlugin;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * Tests the behavior of {@link ModelUtils#load(String)}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("nls")
public class LoadFromStringTest extends TestCase {
	/** This holds pathes to unreadable or inexistant files. */
	private static final String[] INVALID_PATHES = {"/etc/shadow", "/etc/sudoers",
			"/inputs/attribute/attributeChange/v1.ecore",
			File.listRoots()[0].getAbsolutePath() + "projectname/modelname.extension", };

	/** Message displayed when an unexpected {@link IOException} is thrown. */
	private static final String MESSAGE_IOEXCEPTION_UNEXPECTED = "UnExpected IOException has been thrown.";

	/** Message displayed when an unexpected {@link NullPointerException} is thrown. */
	private static final String MESSAGE_NPE_UNEXPECTED = "UnExpected NullPointerException has been thrown.";

	/** Prefix of all the valid pathes. */
	private static final String PATH_PREFIX = '/' + EMFCompareTestPlugin.PLUGIN_ID + '/' + "inputs";

	/** This holds strings referencing valid model locations. */
	private static final String[] VALID_PATHES = {PATH_PREFIX + "/attribute/attributeChange/v1.ecore",
			PATH_PREFIX + "/genmodel/attributeChange/v1.genmodel",
			"platform:/plugin/org.eclipse.emf.compare.diff/model/diff.ecore", };

	/**
	 * Tries and call {@link ModelUtils#load(String, ResourceSet)} with an invalid path and no resourceSet.
	 * <p>
	 * Expects an NPE to be thrown.
	 * </p>
	 */
	public void testLoadInvalidPathNullResourceSet() {
		for (String path : INVALID_PATHES) {
			try {
				ModelUtils.load(path, null);
				fail("Expected NullPointerException hasn't been thrown.");
			} catch (NullPointerException e) {
				// Expected behavior
			} catch (IOException e) {
				fail(MESSAGE_IOEXCEPTION_UNEXPECTED);
			}
		}
	}

	/**
	 * Tries and call {@link ModelUtils#load(String, ResourceSet)} with an invalid path but a valid
	 * resourceSet.
	 * <p>
	 * Expects an IOException to be thrown.
	 * </p>
	 */
	public void testLoadInvalidPathValidResourceSet() {
		for (String path : INVALID_PATHES) {
			final ResourceSet resourceSet = new ResourceSetImpl();
			try {
				ModelUtils.load(path, resourceSet);
				fail("Expected IOException hasn't been thrown.");
			} catch (IOException e) {
				// Expected behavior
			}
		}
	}

	/**
	 * Tries and call {@link ModelUtils#load(String, ResourceSet)} with <code>null</code> arguments or an
	 * empty String.
	 * <p>
	 * Expects an Illegal argument exception to be thrown.
	 * </p>
	 */
	public void testLoadNullPath() {
		final String errMsg = "Unexpected NullPointerException has been thrown.";
		try {
			ModelUtils.load((String)null, null);
			fail(errMsg);
		} catch (NullPointerException e) {
			fail(MESSAGE_NPE_UNEXPECTED);
		} catch (IOException e) {
			fail(MESSAGE_IOEXCEPTION_UNEXPECTED);
		} catch (IllegalArgumentException e) {
			// Expected behavior
		}
		try {
			ModelUtils.load("", null);
			fail(errMsg);
		} catch (NullPointerException e) {
			fail(MESSAGE_NPE_UNEXPECTED);
		} catch (IOException e) {
			fail(MESSAGE_IOEXCEPTION_UNEXPECTED);
		} catch (IllegalArgumentException e) {
			// Expected behavior
		}
	}

	/**
	 * Tries and call {@link ModelUtils#load(String, ResourceSet)} with a valid path but no resourceSet.
	 * <p>
	 * Expects an NPE to be thrown.
	 * </p>
	 */
	public void testLoadValidPathNullResourceSet() {
		final String errMsg = "Expected NullPointerException hasn't been thrown.";
		for (String path : VALID_PATHES) {
			try {
				ModelUtils.load(path, null);
				fail(errMsg);
			} catch (NullPointerException e) {
				// Expected behavior
			} catch (IOException e) {
				fail(MESSAGE_IOEXCEPTION_UNEXPECTED);
			}
		}
	}

	/**
	 * Tries and call {@link ModelUtils#load(String, ResourceSet)} with both a valid path and a valid
	 * resourceSet.
	 * <p>
	 * Expects a non-null EObject associated to the resourceSet to be returned.
	 * </p>
	 */
	public void testLoadValidPathValidResourceSet() {
		for (String path : VALID_PATHES) {
			final ResourceSet resourceSet = new ResourceSetImpl();
			try {
				final EObject result = ModelUtils.load(path, resourceSet);
				assertNotNull("ModelUtils didn't load the expected model.", result);
				assertEquals("Loaded model hasn't been associated with the expected resourceSet.",
						resourceSet, result.eResource().getResourceSet());
				assertTrue("Loaded model isn't the expected one.", result.eResource().getURI().toString()
						.endsWith(path));
			} catch (IOException e) {
				fail(MESSAGE_IOEXCEPTION_UNEXPECTED);
			}
		}
	}
}
