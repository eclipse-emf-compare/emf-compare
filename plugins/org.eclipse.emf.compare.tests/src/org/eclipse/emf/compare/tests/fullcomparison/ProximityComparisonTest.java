/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.fullcomparison;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
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

		final IComparisonScope scope = EMFCompare.createDefaultScope(v1, v2);
		Comparison result = EMFCompare.builder().build().compare(scope);
		assertAllMatched(Lists.newArrayList(v1), result);
		assertEquals("We are supposed to have zero diffs", 0, result.getDifferences().size());
	}

	@Test
	public void matchingSmallRenameChanges() throws Exception {
		Resource left = inputData.getCompareLeft();
		Resource right = inputData.getCompareRight();
		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		Comparison result = EMFCompare.builder().setMatchEngine(
				DefaultMatchEngine.create(UseIdentifiers.NEVER)).build().compare(scope);
		assertEquals("We are supposed to have one rename diff", 1, result.getDifferences().size());
	}

	@Test
	public void matchingIndenticInstances() throws Exception {

		EPackage v1 = EcoreUtil.copy(EcorePackage.eINSTANCE);
		EPackage v2 = EcoreUtil.copy(EcorePackage.eINSTANCE);

		final IComparisonScope scope = EMFCompare.createDefaultScope(v1, v2);
		Comparison result = EMFCompare.builder().build().compare(scope);

		assertAllMatched(Lists.newArrayList(v1), result);
		assertEquals("We are supposed to have zero diffs", 0, result.getDifferences().size());
	}

	@Test
	public void smallChangeOnEPackage() throws Exception {

		EPackage v1 = EcoreUtil.copy(EcorePackage.eINSTANCE);
		EPackage v2 = EcoreUtil.copy(EcorePackage.eINSTANCE);
		v2.setName("renamed");

		final IComparisonScope scope = EMFCompare.createDefaultScope(v1, v2);
		Comparison result = EMFCompare.builder().setMatchEngine(
				DefaultMatchEngine.create(UseIdentifiers.NEVER)).build().compare(scope);
		assertAllMatched(Lists.newArrayList(v1), result);
		assertEquals("We are supposed to have a rename", 1, result.getDifferences().size());
	}

	@Test
	public void packageAddDelete() throws Exception {
		final IComparisonScope scope = EMFCompare.createDefaultScope(inputData.getPackageAddDeleteLeft(),
				inputData.getPackageAddDeleteRight());
		Comparison result = EMFCompare.builder().setMatchEngine(
				DefaultMatchEngine.create(UseIdentifiers.NEVER)).build().compare(scope);
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
		final IComparisonScope scope = EMFCompare.createDefaultScope(inputData
				.getPackageAddRemoveNoRenameLeft(), inputData.getPackageAddRemoveNoRenameRight());
		Comparison result = EMFCompare.builder().setMatchEngine(
				DefaultMatchEngine.create(UseIdentifiers.NEVER)).build().compare(scope);
		EMFComparePrettyPrinter.printComparison(result, System.out);
		final List<Diff> differences = result.getDifferences();
		// EMFCompareAssert.assertAddedToReference(differences, "p1.p2", "eSubpackages", "p1.p2.subPackage",
		// DifferenceSource.LEFT);
		// EMFCompareAssert.assertRemovedFromReference(differences, "p1", "eSubpackages", "p1.another",
		// DifferenceSource.LEFT);
		assertEquals("We are supposed to have zero diffs", 2, result.getDifferences().size());
	}

	@Test
	public void alwaysTakeTheClosestNoMatterTheIterationOrder() throws Exception {
		final IComparisonScope scope = EMFCompare.createDefaultScope(inputData.getVerySmallLeft(), inputData
				.getVerySmallRight());
		Comparison result = EMFCompare.builder().setMatchEngine(
				DefaultMatchEngine.create(UseIdentifiers.NEVER)).build().compare(scope);
		assertEquals(
				"The Match took on element which is close enough (in the limits) preventing the next iteration to take it (it was closest)",
				1, result.getDifferences().size());

	}

	private void assertAllMatched(Iterable<? extends EObject> eObjects, Comparison comparison) {
		for (EObject eObject : eObjects) {
			final Match match = comparison.getMatch(eObject);
			assertTrue(eObject + " has no match", match != null);
		}
	}
}
