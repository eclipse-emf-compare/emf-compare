/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.papyrus.tests.suite;

import org.eclipse.emf.compare.uml2.papyrus.tests.profiles.migration.ProfileMigrationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.textui.TestRunner;

/**
 * This test suite allows us to launch all tests for EMF Compare for UML at once. This test suite should
 * contain classes that need the UI in order to be executed. Tests that do not need the UI should go to
 * {@link AllTests}.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
@RunWith(Suite.class)
@SuiteClasses({ProfileMigrationTest.class })
public class AllUITests {

	/**
	 * Standalone launcher for all of compare's tests.
	 * 
	 * @generated
	 */
	public static void main(final String[] args) {
		TestRunner.run(suite());
	}

	/**
	 * This will return a suite populated with all tests available through this class.
	 * 
	 * @generated
	 */
	public static Test suite() {
		return new JUnit4TestAdapter(AllUITests.class);
	}

}
