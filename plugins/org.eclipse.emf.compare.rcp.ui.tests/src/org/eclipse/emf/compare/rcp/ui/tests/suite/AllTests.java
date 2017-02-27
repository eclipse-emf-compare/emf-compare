/*******************************************************************************
 * Copyright (c) 2013, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 486923
 *     Tanja Mayerhofer - bug 501864
 *     Martin Fleck - bug 510968 and bug 510970
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.tests.suite;

import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.rcp.ui.tests.contentmergeviewer.accessor.match.MatchAccessorTest;
import org.eclipse.emf.compare.rcp.ui.tests.match.RCPMatchEngineFactoryRegistryTest;
import org.eclipse.emf.compare.rcp.ui.tests.mergeviewer.TableOrTreeMergeViewerElementComparerTest;
import org.eclipse.emf.compare.rcp.ui.tests.mergeviewer.item.MergeViewerItemFeatureMapsTest;
import org.eclipse.emf.compare.rcp.ui.tests.mergeviewer.item.MergeViewerItemPseudoConflictTest;
import org.eclipse.emf.compare.rcp.ui.tests.mergeviewer.item.MergeViewerItemTest;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.filters.TechnicalitiesFilterTests;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.filters.TestFeatureMapDifferencesFilter;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.ConflictsGroupTest;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.ResourceAttachmentChangeInGroupsTest;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.TestBasicDifferenceGroupImpl;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.TestComparisonTreeNodeItemProviderSpec;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.TestMatchTreeNodeItemProviderSpec;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.TestReferenceChangeTreeNodeItemProviderSpec;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.ThreeWayComparisonGroupProviderTest;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.match.MatchOfContainmentReferenceChangeAdapterTest;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;
import org.eclipse.emf.compare.tests.nodes.util.NodesResourceFactoryImpl;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.textui.TestRunner;

@RunWith(Suite.class)
@SuiteClasses({TestComparisonTreeNodeItemProviderSpec.class, TestMatchTreeNodeItemProviderSpec.class,
		TestReferenceChangeTreeNodeItemProviderSpec.class, MergeViewerItemTest.class,
		MergeViewerItemPseudoConflictTest.class, MergeViewerItemFeatureMapsTest.class,
		TestBasicDifferenceGroupImpl.class, BugsTestSuite.class, TestFeatureMapDifferencesFilter.class,
		RCPMatchEngineFactoryRegistryTest.class, ThreeWayComparisonGroupProviderTest.class,
		ConflictsGroupTest.class, MatchAccessorTest.class, TechnicalitiesFilterTests.class,
		MatchOfContainmentReferenceChangeAdapterTest.class, ResourceAttachmentChangeInGroupsTest.class,
		TableOrTreeMergeViewerElementComparerTest.class })
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

	@BeforeClass
	public static void fillEMFRegistries() {
		EPackage.Registry.INSTANCE.put(ComparePackage.eNS_URI, ComparePackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(NodesPackage.eNS_URI, NodesPackage.eINSTANCE);

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("nodes", //$NON-NLS-1$
				new NodesResourceFactoryImpl());
	}
}
