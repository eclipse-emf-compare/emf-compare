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
package org.eclipse.emf.compare.tests.unit.core;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.emf.compare.tests.unit.core.util.TestAdapterUtils;
import org.eclipse.emf.compare.tests.unit.core.util.efactory.EFactoryTestSuite;
import org.eclipse.emf.compare.tests.unit.core.util.fastmap.FastMapTestSuite;
import org.eclipse.emf.compare.tests.unit.core.util.modelutils.ModelUtilsTestSuite;

/**
 * Tests for the core org.eclipse.emf.compare plugin.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings("nls")
public final class CoreTestSuite {
	/**
	 * Utility classes don't need to (and shouldn't) be instantiated.
	 */
	private CoreTestSuite() {
		// prevents instantiation
	}

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
	 * @return The testsuite containing all the tests
	 */
	public static Test suite() {
		final TestSuite suite = new TestSuite("Tests for the core plugin.");
		// package org.eclipse.emf.compare
		suite.addTestSuite(TestEMFComparePlugin.class);
		suite.addTestSuite(TestMessages.class);
		// package org.eclipse.emf.compare.util
		suite.addTest(EFactoryTestSuite.suite());
		suite.addTest(FastMapTestSuite.suite());
		suite.addTest(ModelUtilsTestSuite.suite());
		suite.addTestSuite(TestAdapterUtils.class);
		return suite;
	}
}
