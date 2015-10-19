/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.suite;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.textui.TestRunner;

import org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions.TestBug434822;
import org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions.TestBug434827;
import org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions.TestBug434828;
import org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions.TestBug434828_2;
import org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions.TestBug470503;
import org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions.TestBug475586;
import org.eclipse.emf.compare.ide.ui.tests.unit.TestBug459131;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Aggregator of tests related to a bug described in bugzilla.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
@RunWith(Suite.class)
@SuiteClasses({TestBug434827.class,
	TestBug434822.class,
	TestBug434828.class,
	TestBug434828_2.class,
	TestBug459131.class,
	TestBug470503.class,
	TestBug475586.class})
public class BugsTestSuite {
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
