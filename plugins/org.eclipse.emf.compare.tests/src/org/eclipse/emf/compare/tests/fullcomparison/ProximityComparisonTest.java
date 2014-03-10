/**
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.fullcomparison;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.eobject.EcoreWeightProvider;
import org.eclipse.emf.compare.match.eobject.WeightProvider;
import org.eclipse.emf.compare.match.eobject.WeightProviderDescriptorRegistryImpl;
import org.eclipse.emf.compare.match.eobject.internal.WeightProviderDescriptorImpl;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryImpl;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryRegistryImpl;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.framework.EMFCompareAssert;
import org.eclipse.emf.compare.tests.framework.EMFCompareTestBase;
import org.eclipse.emf.compare.tests.fullcomparison.data.distance.DistanceMatchInputData;
import org.eclipse.emf.compare.tests.suite.AllTests;
import org.eclipse.emf.compare.utils.EMFComparePrettyPrinter;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("nls")
public class ProximityComparisonTest extends EMFCompareTestBase {
	@Before
	public void setUp() throws Exception {
		AllTests.fillEMFRegistries();
	}

	private DistanceMatchInputData inputData = new DistanceMatchInputData();

	@Test
	public void singleEObjectTest() throws Exception {
		EPackage v1 = EcoreUtil.copy(EcorePackage.eINSTANCE);
		EPackage v2 = EcoreUtil.copy(EcorePackage.eINSTANCE);
		v1.getEClassifiers().clear();
		v2.getEClassifiers().clear();

		final IComparisonScope scope = new DefaultComparisonScope(v1, v2, null);
		Comparison result = EMFCompare.builder().build().compare(scope);
		assertAllMatched(Lists.newArrayList(v1), result);
		assertEquals("We are supposed to have zero diffs", 0, result.getDifferences().size());
	}

	@Test
	public void matchingSmallRenameChanges() throws Exception {
		Resource left = inputData.getCompareLeft();
		Resource right = inputData.getCompareRight();
		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		IMatchEngine.Factory.Registry matchEngineFactoryRegistry = new MatchEngineFactoryRegistryImpl();
		matchEngineFactoryRegistry.add(new MatchEngineFactoryImpl(UseIdentifiers.NEVER));
		Comparison result = EMFCompare.builder().setMatchEngineFactoryRegistry(matchEngineFactoryRegistry)
				.build().compare(scope);
		assertEquals("We are supposed to have one rename diff", 1, result.getDifferences().size());
	}

	@Test
	public void matchingIndenticInstances() throws Exception {

		EPackage v1 = EcoreUtil.copy(EcorePackage.eINSTANCE);
		EPackage v2 = EcoreUtil.copy(EcorePackage.eINSTANCE);

		final IComparisonScope scope = new DefaultComparisonScope(v1, v2, null);
		Comparison result = EMFCompare.builder().build().compare(scope);

		assertAllMatched(Lists.newArrayList(v1), result);
		assertEquals("We are supposed to have zero diffs", 0, result.getDifferences().size());
	}

	@Test
	public void smallChangeOnEPackage() throws Exception {

		EPackage v1 = EcoreUtil.copy(EcorePackage.eINSTANCE);
		EPackage v2 = EcoreUtil.copy(EcorePackage.eINSTANCE);
		v2.setName("renamed");

		final IComparisonScope scope = new DefaultComparisonScope(v1, v2, null);
		IMatchEngine.Factory.Registry matchEngineFactoryRegistry = new MatchEngineFactoryRegistryImpl();
		matchEngineFactoryRegistry.add(new MatchEngineFactoryImpl(UseIdentifiers.NEVER));
		Comparison result = EMFCompare.builder().setMatchEngineFactoryRegistry(matchEngineFactoryRegistry)
				.build().compare(scope);
		assertAllMatched(Lists.newArrayList(v1), result);
		assertEquals("We are supposed to have a rename", 1, result.getDifferences().size());
	}

	@Test
	public void packageAddDelete() throws Exception {
		final IComparisonScope scope = new DefaultComparisonScope(inputData.getPackageAddDeleteLeft(),
				inputData.getPackageAddDeleteRight(), null);
		IMatchEngine.Factory.Registry matchEngineFactoryRegistry = new MatchEngineFactoryRegistryImpl();
		matchEngineFactoryRegistry.add(new MatchEngineFactoryImpl(UseIdentifiers.NEVER));
		Comparison result = EMFCompare.builder().setMatchEngineFactoryRegistry(matchEngineFactoryRegistry)
				.build().compare(scope);
		EMFComparePrettyPrinter.printComparison(result, System.out);
		final List<Diff> differences = result.getDifferences();
		EMFCompareAssert.assertAddedToReference(differences, "p1.p2", "eSubpackages", "p1.p2.subPackage",
				DifferenceSource.LEFT);
		EMFCompareAssert.assertRemovedFromReference(differences, "p1", "eSubpackages", "p1.another",
				DifferenceSource.LEFT);
		assertEquals("We are supposed to have zero diffs", 2, result.getDifferences().size());
	}

	@Test
	public void packageAddRemoveNoRename() throws Exception {
		final IComparisonScope scope = new DefaultComparisonScope(
				inputData.getPackageAddRemoveNoRenameLeft(), inputData.getPackageAddRemoveNoRenameRight(),
				null);
		IMatchEngine.Factory.Registry matchEngineFactoryRegistry = new MatchEngineFactoryRegistryImpl();
		matchEngineFactoryRegistry.add(new MatchEngineFactoryImpl(UseIdentifiers.NEVER));
		Comparison result = EMFCompare.builder().setMatchEngineFactoryRegistry(matchEngineFactoryRegistry)
				.build().compare(scope);
		EMFComparePrettyPrinter.printComparison(result, System.out);
		assertEquals("We are supposed to have zero diffs", 2, result.getDifferences().size());
	}

	@Test
	public void alwaysTakeTheClosestNoMatterTheIterationOrder() throws Exception {
		final IComparisonScope scope = new DefaultComparisonScope(inputData.getVerySmallLeft(), inputData
				.getVerySmallRight(), null);
		IMatchEngine.Factory.Registry matchEngineFactoryRegistry = new MatchEngineFactoryRegistryImpl();
		matchEngineFactoryRegistry.add(new MatchEngineFactoryImpl(UseIdentifiers.NEVER));
		Comparison result = EMFCompare.builder().setMatchEngineFactoryRegistry(matchEngineFactoryRegistry)
				.build().compare(scope);
		assertEquals(
				"The Match took on element which is close enough (in the limits) preventing the next iteration to take it (it was closest)",
				1, result.getDifferences().size());

	}

	@Test
	public void addRemoveAndNotRename() throws Exception {
		final IComparisonScope scope = new DefaultComparisonScope(inputData.get391657Left(), inputData
				.get391657Right(), null);
		IMatchEngine.Factory.Registry matchEngineFactoryRegistry = new MatchEngineFactoryRegistryImpl();
		WeightProvider.Descriptor.Registry weightProviderRegistry = new WeightProviderDescriptorRegistryImpl();
		EcoreWeightProvider weightProvider = new EcoreWeightProvider();
		WeightProviderDescriptorImpl descriptor = new WeightProviderDescriptorImpl(weightProvider, 100,
				Pattern.compile(".*"));
		weightProviderRegistry.put(weightProvider.getClass().getName(), descriptor);
		matchEngineFactoryRegistry.add(new MatchEngineFactoryImpl(UseIdentifiers.NEVER,
				weightProviderRegistry));
		Comparison result = EMFCompare.builder().setMatchEngineFactoryRegistry(matchEngineFactoryRegistry)
				.build().compare(scope);
		Iterator<AttributeChange> attrChanges = Iterators.filter(result.getDifferences().iterator(),
				AttributeChange.class);
		assertFalse("We are supposed to detect an addition/remove (and not a rename if that's what we get)",
				attrChanges.hasNext());

	}

	@Test
	public void resourceRootChange() throws Exception {
		final IComparisonScope scope = new DefaultComparisonScope(inputData.get390666Left(), inputData
				.get390666Right(), inputData.get390666Ancestor());
		IMatchEngine.Factory.Registry matchEngineFactoryRegistry = new MatchEngineFactoryRegistryImpl();
		matchEngineFactoryRegistry.add(new MatchEngineFactoryImpl(UseIdentifiers.NEVER));
		Comparison result = EMFCompare.builder().setMatchEngineFactoryRegistry(matchEngineFactoryRegistry)
				.build().compare(scope);
		Iterator<ResourceAttachmentChange> attrChanges = Iterators.filter(result.getDifferences().iterator(),
				ResourceAttachmentChange.class);
		assertTrue("We are supposed to detect a new attachment to a resource", attrChanges.hasNext());
	}

	@Test
	public void moveInAReferenceShouldNotAffectMatch() throws Exception {
		/*
		 * See bug #391798 : moving elements a lot in a reference (like in changing index) should not affect
		 * the matching very much.
		 */
		final IComparisonScope scope = new DefaultComparisonScope(inputData.get391798Left(), inputData
				.get391798Right(), null);
		IMatchEngine.Factory.Registry matchEngineFactoryRegistry = new MatchEngineFactoryRegistryImpl();
		matchEngineFactoryRegistry.add(new MatchEngineFactoryImpl(UseIdentifiers.NEVER));
		Comparison result = EMFCompare.builder().setMatchEngineFactoryRegistry(matchEngineFactoryRegistry)
				.build().compare(scope);
		for (Diff dif : result.getDifferences()) {
			if (dif instanceof ReferenceChange && dif.getKind() == DifferenceKind.MOVE) {

			} else {
				fail("We should only detect moves in the reference.");
			}
		}

	}

	private void assertAllMatched(Iterable<? extends EObject> eObjects, Comparison comparison) {
		for (EObject eObject : eObjects) {
			final Match match = comparison.getMatch(eObject);
			assertTrue(eObject + " has no match", match != null);
		}
	}
}
