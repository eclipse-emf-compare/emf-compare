/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - adds test classes
 *     Stefan Dirix - add EMFModelProviderTest
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.suite;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.textui.TestRunner;

import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.ide.ui.tests.command.MergeAllCommandTests;
import org.eclipse.emf.compare.ide.ui.tests.compareconfiguration.EMFCompareConfigurationTest;
import org.eclipse.emf.compare.ide.ui.tests.contentmergeviewer.notloadedfragment.NotLoadedFragmentItemTest;
import org.eclipse.emf.compare.ide.ui.tests.logical.modelprovider.EMFModelProviderTest;
import org.eclipse.emf.compare.ide.ui.tests.logical.resolver.DependencyGraphUpdaterTest;
import org.eclipse.emf.compare.ide.ui.tests.logical.resolver.GraphResolutionTest;
import org.eclipse.emf.compare.ide.ui.tests.logical.resolver.LocalMonitoredProxyCreationListenerTest;
import org.eclipse.emf.compare.ide.ui.tests.logical.resolver.RemoteMonitoredProxyCreationListenerTest;
import org.eclipse.emf.compare.ide.ui.tests.logical.resolver.RenameDetectorTest;
import org.eclipse.emf.compare.ide.ui.tests.logical.resolver.ResolutionEventsTest;
import org.eclipse.emf.compare.ide.ui.tests.logical.resolver.ResourceComputationSchedulerTest;
import org.eclipse.emf.compare.ide.ui.tests.logical.resolver.ResourceComputationSchedulerWithEventBusTest;
import org.eclipse.emf.compare.ide.ui.tests.logical.resolver.SimilarityComputerTest;
import org.eclipse.emf.compare.ide.ui.tests.logical.resolver.ThreadedModelResolverGraphTest;
import org.eclipse.emf.compare.ide.ui.tests.logical.resolver.ThreadedModelResolverWithCustomDependencyProviderTest;
import org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.NavigatableTest;
import org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions.MergeActionTest;
import org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions.MergeNonConflictingRunnableTest;
import org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions.PseudoConflictsMergeActionTest;
import org.eclipse.emf.compare.ide.ui.tests.unit.DependenciesTest;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;
import org.eclipse.emf.compare.tests.nodes.util.NodesResourceFactoryImpl;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({EMFCompareConfigurationTest.class, DependenciesTest.class, MergeActionTest.class,
		PseudoConflictsMergeActionTest.class, BugsTestSuite.class, NavigatableTest.class,
		/*
		 * FIXME Reintegrate this test when local comparisons work again NotLoadedFragmentNodeTest.class,
		 */
		NotLoadedFragmentItemTest.class, ResolutionEventsTest.class, ResourceComputationSchedulerTest.class,
		ResourceComputationSchedulerWithEventBusTest.class, ThreadedModelResolverGraphTest.class,
		ThreadedModelResolverWithCustomDependencyProviderTest.class, DependencyGraphUpdaterTest.class,
		GraphResolutionTest.class, EMFModelProviderTest.class, MergeAllCommandTests.class,
		LocalMonitoredProxyCreationListenerTest.class, RemoteMonitoredProxyCreationListenerTest.class,
		MergeNonConflictingRunnableTest.class, RenameDetectorTest.class, SimilarityComputerTest.class })
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
