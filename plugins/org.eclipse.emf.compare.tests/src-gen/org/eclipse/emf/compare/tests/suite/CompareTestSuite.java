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
import junit.textui.TestRunner;

import org.eclipse.emf.compare.tests.unit.AttributeChangeTest;
import org.eclipse.emf.compare.tests.unit.CompareAdapterFactoryTest;
import org.eclipse.emf.compare.tests.unit.CompareFactoryTest;
import org.eclipse.emf.compare.tests.unit.CompareSwitchTest;
import org.eclipse.emf.compare.tests.unit.ComparisonTest;
import org.eclipse.emf.compare.tests.unit.ConflictKindTest;
import org.eclipse.emf.compare.tests.unit.ConflictTest;
import org.eclipse.emf.compare.tests.unit.DiffTest;
import org.eclipse.emf.compare.tests.unit.DifferenceKindTest;
import org.eclipse.emf.compare.tests.unit.DifferenceSourceTest;
import org.eclipse.emf.compare.tests.unit.DifferenceStateTest;
import org.eclipse.emf.compare.tests.unit.EquivalenceTest;
import org.eclipse.emf.compare.tests.unit.MatchResourceTest;
import org.eclipse.emf.compare.tests.unit.MatchTest;
import org.eclipse.emf.compare.tests.unit.ReferenceChangeTest;
import org.eclipse.emf.compare.tests.unit.ResourceAttachmentChangeTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This test suite allows clients to launch all tests generated for package compare at once.
 * 
 * @generated
 */
@RunWith(Suite.class)
@SuiteClasses({ComparisonTest.class, MatchResourceTest.class, MatchTest.class, DiffTest.class,
		ResourceAttachmentChangeTest.class, ReferenceChangeTest.class, AttributeChangeTest.class,
		ConflictTest.class, EquivalenceTest.class, DifferenceKindTest.class, DifferenceSourceTest.class,
		DifferenceStateTest.class, ConflictKindTest.class, CompareAdapterFactoryTest.class,
		CompareFactoryTest.class, CompareSwitchTest.class, })
public class CompareTestSuite {
	/**
	 * Standalone launcher for package compare's tests.
	 * 
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	/**
	 * This will return a suite populated with all generated tests for package compare.
	 * 
	 * @generated
	 */
	public static Test suite() {
		return new JUnit4TestAdapter(CompareTestSuite.class);
	}
}
