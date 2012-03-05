/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
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

import org.eclipse.emf.compare.tests.merge.AllMergeTests;
import org.eclipse.emf.compare.tests.unit.core.CoreTestSuite;
import org.eclipse.emf.compare.tests.unit.diff.DiffTestSuite;
import org.eclipse.emf.compare.tests.unit.diff.UMLHistoryDiff;
import org.eclipse.emf.compare.tests.unit.diff.UMLHistoryDiffWithResource;
import org.eclipse.emf.compare.tests.unit.match.MatchTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Launches all the JUnit tests for EMF compare.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@RunWith(Suite.class)
@SuiteClasses({CoreTestSuite.class, MatchTestSuite.class, AllMergeTests.class, UMLHistoryDiff.class,
		UMLHistoryDiffWithResource.class, DiffTestSuite.class, })
public class AllTests {
	/**
	 * Creates the {@link junit.framework.TestSuite TestSuite} for all the test.
	 * 
	 * @return The test suite containing all the tests
	 */
	public static Test suite() {
		return new JUnit4TestAdapter(AllTests.class);
	}
}
