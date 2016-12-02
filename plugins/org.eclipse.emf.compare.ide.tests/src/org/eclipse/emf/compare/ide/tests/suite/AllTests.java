/*******************************************************************************
 * Copyright (c) 2012, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Tobias Ortmayr - bug 507157
 *******************************************************************************/
package org.eclipse.emf.compare.ide.tests.suite;

import org.eclipse.emf.compare.ide.utils.tests.Bug471045Test;
import org.eclipse.emf.compare.ide.utils.tests.Bug507157Test;
import org.eclipse.emf.compare.ide.utils.tests.ResourceUtil_BinaryIdentical2Test;
import org.eclipse.emf.compare.ide.utils.tests.ResourceUtil_BinaryIdentical2_ReadLimitTest;
import org.eclipse.emf.compare.ide.utils.tests.ResourceUtil_BinaryIdentical3Test;
import org.eclipse.emf.compare.ide.utils.tests.ResourceUtil_BinaryIdentical3_ReadLimitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.textui.TestRunner;

@RunWith(Suite.class)
@SuiteClasses({ResourceUtil_BinaryIdentical2Test.class, ResourceUtil_BinaryIdentical2_ReadLimitTest.class,
		ResourceUtil_BinaryIdentical3Test.class, ResourceUtil_BinaryIdentical3_ReadLimitTest.class,
		Bug471045Test.class, Bug507157Test.class })
public class AllTests {
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
}
