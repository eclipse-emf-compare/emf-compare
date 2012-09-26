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

import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.tests.conflict.ConflictDetectionTest;
import org.eclipse.emf.compare.tests.diff.DiffUtilTest;
import org.eclipse.emf.compare.tests.diff.URIDistanceTest;
import org.eclipse.emf.compare.tests.equi.EquiComputingTest;
import org.eclipse.emf.compare.tests.fragmentation.FragmentationTest;
import org.eclipse.emf.compare.tests.fullcomparison.DynamicInstanceComparisonTest;
import org.eclipse.emf.compare.tests.fullcomparison.IdentifierComparisonTest;
import org.eclipse.emf.compare.tests.fullcomparison.ProximityComparisonTest;
import org.eclipse.emf.compare.tests.merge.IndividualMergeOutOfScopeValuesTest;
import org.eclipse.emf.compare.tests.merge.IndividualMergeTest;
import org.eclipse.emf.compare.tests.merge.MultipleMergeTest;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;
import org.eclipse.emf.compare.tests.nodes.util.NodesResourceFactoryImpl;
import org.eclipse.emf.compare.tests.postprocess.PostProcessorTest;
import org.eclipse.emf.compare.tests.req.ReqComputingTest;
import org.eclipse.emf.compare.tests.scope.DefaultComparisonScopeTest;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This test suite allows us to launch all tests for EMF Compare at once.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@RunWith(Suite.class)
@SuiteClasses({CompareTestSuite.class, DefaultComparisonScopeTest.class, IdentifierComparisonTest.class,
		ConflictDetectionTest.class, ReqComputingTest.class, EquiComputingTest.class, DiffUtilTest.class,
		MultipleMergeTest.class, PostProcessorTest.class, IndividualMergeTest.class,
		IndividualMergeOutOfScopeValuesTest.class, ProximityComparisonTest.class,
		DynamicInstanceComparisonTest.class, URIDistanceTest.class, FragmentationTest.class })
public class AllTests {
	/**
	 * Standalone launcher for all of compare's tests.
	 * 
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	/**
	 * This will return a suite populated with all tests available through this class.
	 * 
	 * @generated
	 */
	public static Test suite() {
		return new JUnit4TestAdapter(CompareTestSuite.class);
	}

	@BeforeClass
	public static void fillEMFRegistries() {
		EPackage.Registry.INSTANCE.put(ComparePackage.eNS_URI, ComparePackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(NodesPackage.eNS_URI, NodesPackage.eINSTANCE);

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("nodes", //$NON-NLS-1$
				new NodesResourceFactoryImpl());
	}
}
