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
package org.eclipse.emf.compare.tests.suite;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.eclipse.emf.compare.tests.FuzzyTest;
import org.eclipse.emf.compare.tests.NodesFuzzyTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This class will be used to cover all of our OSGi-dependant tests (anything that needs be run as
 * "plugin test" and cannot run as standalone "JUnit test" should be added here). Both Suites will be launched
 * by the tycho build.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@RunWith(Suite.class)
@SuiteClasses({FuzzyTest.class, NodesFuzzyTest.class })
public class AllPluginTests {
	/**
	 * This will return a suite populated with all tests available through this class.
	 * 
	 * @generated
	 */
	public static Test suite() {
		return new JUnit4TestAdapter(CompareTestSuite.class);
	}
}
