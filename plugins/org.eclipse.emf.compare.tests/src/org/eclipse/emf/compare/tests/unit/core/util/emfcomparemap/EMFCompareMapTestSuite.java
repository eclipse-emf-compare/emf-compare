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
package org.eclipse.emf.compare.tests.unit.core.util.emfcomparemap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Launches all the JUnit tests for the {@link EMFCompareMap}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
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
		suite.addTestSuite(ConstructorsTest.class);
		suite.addTestSuite(ContainsKeyValueTest.class);
		suite.addTestSuite(EntrySetTest.class);
		suite.addTestSuite(EqualsTest.class);
		suite.addTestSuite(GetPutTest.class);
		suite.addTestSuite(KeySetTest.class);
		suite.addTestSuite(MethodsTest.class);
		suite.addTestSuite(PutAllTest.class);
		suite.addTestSuite(RemoveTest.class);
		suite.addTestSuite(ValuesTest.class);
		return suite;
	}
}
