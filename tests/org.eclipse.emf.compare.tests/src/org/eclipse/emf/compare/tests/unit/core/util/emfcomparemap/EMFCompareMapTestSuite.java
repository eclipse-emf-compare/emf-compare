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
package org.eclipse.emf.compare.tests.unit.core.util.emfcomparemap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Launches all the JUnit tests for the {@link EMFCompareMap}.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings("nls")
public class EMFCompareMapTestSuite extends TestCase {
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
		final TestSuite suite = new TestSuite("Tests for the EMFCompareMap behavior");
		suite.addTestSuite(TestConstructors.class);
		suite.addTestSuite(TestContainsKeyValue.class);
		suite.addTestSuite(TestEntrySet.class);
		suite.addTestSuite(TestEquals.class);
		suite.addTestSuite(TestGetPut.class);
		suite.addTestSuite(TestKeySet.class);
		suite.addTestSuite(TestMethods.class);
		suite.addTestSuite(TestPutAll.class);
		suite.addTestSuite(TestRemove.class);
		suite.addTestSuite(TestValues.class);
		return suite;
	}
}
