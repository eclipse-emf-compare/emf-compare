/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.tests.suite;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.textui.TestRunner;

import org.eclipse.emf.compare.rcp.ui.tests.mergeviewer.item.MergeViewerItemTest;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.TestBasicDifferenceGroupImpl;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.TestComparisonTreeNodeItemProviderSpec;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.TestMatchTreeNodeItemProviderSpec;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.TestReferenceChangeTreeNodeItemProviderSpec;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TestComparisonTreeNodeItemProviderSpec.class, 
			   TestMatchTreeNodeItemProviderSpec.class, 
			   TestReferenceChangeTreeNodeItemProviderSpec.class,
			   MergeViewerItemTest.class,
			   TestBasicDifferenceGroupImpl.class})
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
