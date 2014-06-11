/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.FeatureMapChange;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.merge.data.IndividualDiffInputData;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;
import org.eclipse.emf.compare.tests.nodes.util.NodesResourceFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A FeatureMapChange is always associated with a ReferenceChange. The FeatureMapChange is equivalent to his
 * ReferenceChange and vice-versa. Each case will be done on the FeatureMapChange and on the ReferenceChange.
 */
@SuppressWarnings({"nls", "rawtypes", "unchecked" })
public class FeatureMaps3wayMergeTest {

	@BeforeClass
	public static void fillEMFRegistries() {
		EPackage.Registry.INSTANCE.put(ComparePackage.eNS_URI, ComparePackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(NodesPackage.eNS_URI, NodesPackage.eINSTANCE);

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("nodes", //$NON-NLS-1$
				new NodesResourceFactoryImpl());
	}

	private IndividualDiffInputData input = new IndividualDiffInputData();

	private final IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	@Test
	public void test3wayContainmentAdd_Left_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftAddScope();
		final Resource right = input.getFeatureMapContainmentRightAddScope();
		final Resource origin = input.getFeatureMapContainmentOriginAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1[firstKey add] from left to right : the FeatureMapChange
		// <firstKey> Node node1 [map add] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).contains(rightNode1));
		// mapNode1 contains node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).basicContains(eSFFirstKey, rightNode1));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentAdd_Right_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftAddScope();
		final Resource right = input.getFeatureMapContainmentRightAddScope();
		final Resource origin = input.getFeatureMapContainmentOriginAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node2[firstKey add] from left to right : the FeatureMapChange
		// <firstKey> Node node2 [map add] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node2 still doesn't exists in left
		final EObject leftNode2 = getNodeNamed(left, "node2");
		assertNull(leftNode2);
		// node2 doesn't exists anymore in right
		final EObject rightNode2 = getNodeNamed(right, "node2");
		assertNull(rightNode2);
		// mapNode1 contains node2 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).isEmpty());
		// mapNode1 contains node2 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentAdd_Left_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftAddScope();
		final Resource right = input.getFeatureMapContainmentRightAddScope();
		final Resource origin = input.getFeatureMapContainmentOriginAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map add] from left to right : the ReferenceChange
		// Node node1[firstKey add] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).contains(rightNode1));
		// mapNode1 contains node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).basicContains(eSFFirstKey, rightNode1));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentAdd_Right_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftAddScope();
		final Resource right = input.getFeatureMapContainmentRightAddScope();
		final Resource origin = input.getFeatureMapContainmentOriginAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node2 [map add] from left to right : the ReferenceChange
		// Node node2 [firstKey add] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node2 doesn't still exists in left
		final EObject leftNode2 = getNodeNamed(left, "node2");
		assertNull(leftNode2);
		// node2 doesn't exists anymore in right
		final EObject rightNode2 = getNodeNamed(right, "node2");
		assertNull(rightNode2);
		// mapNode1 doesn't contains node2 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).isEmpty());
		// mapNode1 doesn't contains node2 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentAdd_Left_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftAddScope();
		final Resource right = input.getFeatureMapContainmentRightAddScope();
		final Resource origin = input.getFeatureMapContainmentOriginAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1[firstKey add] from right to left : the FeatureMapChange
		// <firstKey> Node node1 [map add] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still doesn't exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNull(rightNode1);
		// node1 doesn't exists anymore in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNull(leftNode1);
		// mapNode1 doesn't contains node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).isEmpty());
		// mapNode1 doesn't contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = leftMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentAdd_Right_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftAddScope();
		final Resource right = input.getFeatureMapContainmentRightAddScope();
		final Resource origin = input.getFeatureMapContainmentOriginAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node2 [firstKey add] from right to left : the FeatureMapChange
		// <firstKey> Node node2 [map add] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node2 still exists in right
		final EObject rightNode2 = getNodeNamed(right, "node2");
		assertNotNull(rightNode2);
		// node2 exists in left
		final EObject leftNode2 = getNodeNamed(left, "node2");
		assertNotNull(leftNode2);
		// mapNode1 contains node2 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).contains(leftNode2));
		// mapNode1 contains node2 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = leftMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).contains(eSFFirstKey, leftNode2));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentAdd_Left_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftAddScope();
		final Resource right = input.getFeatureMapContainmentRightAddScope();
		final Resource origin = input.getFeatureMapContainmentOriginAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map add] from right to left : the
		// ReferenceChange Node node1[firstKey add] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still doesn't exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNull(rightNode1);
		// node1 doesn't exists anymore in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNull(leftNode1);
		// mapNode1 doesn't contains node1 through the feature firstKey in right
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).isEmpty());
		// mapNode1 doesn't contains node1 through the map in right
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = leftMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentAdd_Right_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftAddScope();
		final Resource right = input.getFeatureMapContainmentRightAddScope();
		final Resource origin = input.getFeatureMapContainmentOriginAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node2 [map add] from right to left : the
		// ReferenceChange Node node2 [firstKey add] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node2 still exists in right
		final EObject rightNode2 = getNodeNamed(right, "node2");
		assertNotNull(rightNode2);
		// node2 exists in left
		final EObject leftNode2 = getNodeNamed(left, "node2");
		assertNotNull(leftNode2);
		// mapNode1 contains node2 through the feature firstKey in right
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).contains(leftNode2));
		// mapNode1 contains node2 through the map in right
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = leftMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).contains(eSFFirstKey, leftNode2));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentDelete_Left_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node2 [firstKey delete] from left to right : the FeatureMapChange
		// <firstKey> Node node2 [map delete] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node2 doesn't still exists in left
		final EObject leftNode2 = getNodeNamed(left, "node2");
		assertNull(leftNode2);
		// node2 deosn't exists anymore in right
		final EObject rightNode2 = getNodeNamed(right, "node2");
		assertNull(rightNode2);
		// mapNode1 doesn't contains node2 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).isEmpty());
		// mapNode1 doesn't contains node2 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentDelete_Right_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1 [firstKey delete] from left to right : the FeatureMapChange
		// <firstKey> Node node1 [map delete] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).contains(rightNode1));
		// mapNode1 contains node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).contains(eSFFirstKey, rightNode1));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentDelete_Left_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node2 [map delete] from left to right : the
		// ReferenceChange Node node2 [firstKey delete] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node2 doesn't still exists in left
		final EObject leftNode2 = getNodeNamed(left, "node2");
		assertNull(leftNode2);
		// node2 deosn't exists anymore in right
		final EObject rightNode2 = getNodeNamed(right, "node2");
		assertNull(rightNode2);
		// mapNode1 doesn't contains node2 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).isEmpty());
		// mapNode1 doesn't contains node2 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentDelete_Right_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node2 [map delete] from left to right : the
		// ReferenceChange Node node2 [firstKey delete] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).contains(rightNode1));
		// mapNode1 contains node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).contains(eSFFirstKey, rightNode1));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentDelete_Left_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node2 [firstKey delete] from right to left: the FeatureMapChange
		// <firstKey> Node node2 [map delete] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node2 still exists in right
		final EObject rightNode2 = getNodeNamed(right, "node2");
		assertNotNull(rightNode2);
		// node2 exists in left
		final EObject leftNode2 = getNodeNamed(left, "node2");
		assertNotNull(leftNode2);
		// mapNode1 contains node2 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).contains(leftNode2));
		// mapNode1 contains node2 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = leftMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).basicContains(eSFFirstKey, leftNode2));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentDelete_Right_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1 [firstKey delete] from right to left: the FeatureMapChange
		// <firstKey> Node node1 [map delete] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 doesn't still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNull(rightNode1);
		// node1 doesn't exists anymore in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNull(leftNode1);
		// mapNode1 doesn't contains node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).isEmpty());
		// mapNode1 doesn't contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = leftMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentDelete_Left_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node2 [map delete] from right to left: the
		// ReferenceChange Node node2 [firstKey delete] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node2 still exists in right
		final EObject rightNode2 = getNodeNamed(right, "node2");
		assertNotNull(rightNode2);
		// node2 exists in left
		final EObject leftNode2 = getNodeNamed(left, "node2");
		assertNotNull(leftNode2);
		// mapNode1 contains node2 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).contains(leftNode2));
		// mapNode1 contains node2 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = leftMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).basicContains(eSFFirstKey, leftNode2));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentDelete_Right_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map delete] from right to left: the
		// ReferenceChange Node node1 [firstKey delete] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 doesn't still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNull(rightNode1);
		// node1 doesn't exists anymore in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNull(leftNode1);
		// mapNode1 doesn't contains node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).isEmpty());
		// mapNode1 doesn't contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = leftMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentKeyChange_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightKeyChangeScope();
		final Resource origin = input.getFeatureMapContainmentOriginKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1[firstKey move] from left to right : the FeatureMapChange
		// <firstKey> Node node1 [map entry key change] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).contains(rightNode1));
		// mapNode1 contains node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).basicContains(eSFFirstKey, rightNode1));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentKeyChange_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightKeyChangeScope();
		final Resource origin = input.getFeatureMapContainmentOriginKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.CHANGE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map entry key change] from left to right : the
		// ReferenceChange Node node1[firstKey move] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).contains(rightNode1));
		// mapNode1 contains node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).basicContains(eSFFirstKey, rightNode1));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentKeyChange_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightKeyChangeScope();
		final Resource origin = input.getFeatureMapContainmentOriginKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1[firstKey move] from right to left : the FeatureMapChange
		// <firstKey> Node node1 [map entry key change] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// mapNode1 contains node1 through the feature secondKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFSecondKey = leftMapNode1.eClass().getEStructuralFeature("secondKey");
		assertNotNull(eSFSecondKey);
		final Object secondKey = leftMapNode1.eGet(eSFSecondKey);
		assertTrue(secondKey instanceof Collection);
		assertTrue(((Collection)secondKey).contains(leftNode1));
		// mapNode1 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = leftMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).basicContains(eSFSecondKey, leftNode1));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentKeyChange_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightKeyChangeScope();
		final Resource origin = input.getFeatureMapContainmentOriginKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.CHANGE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map entry key change] from left to right : the
		// ReferenceChange Node node1[firstKey move] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// mapNode1 contains node1 through the feature secondKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFSecondKey = leftMapNode1.eClass().getEStructuralFeature("secondKey");
		assertNotNull(eSFSecondKey);
		final Object secondKey = leftMapNode1.eGet(eSFSecondKey);
		assertTrue(secondKey instanceof Collection);
		assertTrue(((Collection)secondKey).contains(leftNode1));
		// mapNode1 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = leftMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).basicContains(eSFSecondKey, leftNode1));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentMove_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveScope();
		final Resource right = input.getFeatureMapContainmentRightMoveScope();
		final Resource origin = input.getFeatureMapContainmentOriginMoveScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1[firstKey move] from left to right : the FeatureMapChange
		// <firstKey> Node node1 [map move] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).contains(rightNode1));
		// mapNode1 contains node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).basicContains(eSFFirstKey, rightNode1));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentMove_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveScope();
		final Resource right = input.getFeatureMapContainmentRightMoveScope();
		final Resource origin = input.getFeatureMapContainmentOriginMoveScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map move] from left to right : the
		// ReferenceChange Node node1[firstKey move] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).contains(rightNode1));
		// mapNode1 contains node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).basicContains(eSFFirstKey, rightNode1));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentMove_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveScope();
		final Resource right = input.getFeatureMapContainmentRightMoveScope();
		final Resource origin = input.getFeatureMapContainmentOriginMoveScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1[firstKey move] from right to left : the FeatureMapChange
		// <firstKey> Node node1 [map move] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in right
		final EObject rNode1 = getNodeNamed(right, "node1");
		assertNotNull(rNode1);
		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// mapNode2 contains node1 through the feature firstKey in left
		final EObject leftMapNode2 = getNodeNamed(left, "mapNode2");
		assertNotNull(leftMapNode2);
		final EStructuralFeature eSFFirstKey = leftMapNode2.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = leftMapNode2.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).contains(leftNode1));
		// mapNode2 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode2.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = leftMapNode2.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).basicContains(eSFFirstKey, leftNode1));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentMove_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveScope();
		final Resource right = input.getFeatureMapContainmentRightMoveScope();
		final Resource origin = input.getFeatureMapContainmentOriginMoveScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map move] from right to left : the
		// ReferenceChange Node node1[firstKey move] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// mapNode2 contains node1 through the feature firstKey in left
		final EObject leftMapNode2 = getNodeNamed(left, "mapNode2");
		assertNotNull(leftMapNode2);
		final EStructuralFeature eSFFirstKey = leftMapNode2.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = leftMapNode2.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).contains(leftNode1));
		// mapNode2 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode2.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = leftMapNode2.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).basicContains(eSFFirstKey, leftNode1));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentMoveOrder_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveOrderScope();
		final Resource right = input.getFeatureMapContainmentRightMoveOrderScope();
		final Resource origin = input.getFeatureMapContainmentOriginMoveOrderScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1 [firstKey move] from left to right : the FeatureMapChange
		// <firstKey> Node node1 [map move] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in right, in the last position
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof List);
		assertEquals(2, ((List)firstKey).size());
		assertEquals(rightNode1, ((List)firstKey).get(1));
		// mapNode1 contains node1 through the map in right, in the last position
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)map).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentMoveOrder_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveOrderScope();
		final Resource right = input.getFeatureMapContainmentRightMoveOrderScope();
		final Resource origin = input.getFeatureMapContainmentOriginMoveOrderScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map move] from left to right : the
		// ReferenceChange Node node1[firstKey move] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in right, in the last position
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof List);
		assertEquals(2, ((List)firstKey).size());
		assertEquals(rightNode1, ((List)firstKey).get(1));
		// mapNode1 contains node1 through the map in right, in the last position
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)map).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentMoveOrder_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveOrderScope();
		final Resource right = input.getFeatureMapContainmentRightMoveOrderScope();
		final Resource origin = input.getFeatureMapContainmentOriginMoveOrderScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1[firstKey move] from right to left : the FeatureMapChange
		// <firstKey> Node node1 [map move] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// mapNode1 contains node1 through the feature firstKey in left, in the first position
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof List);
		assertEquals(2, ((List)firstKey).size());
		assertEquals(leftNode1, ((List)firstKey).get(0));
		// mapNode1 contains node1 through the map in left, in the first position
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = leftMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)map).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentMoveOrder_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveOrderScope();
		final Resource right = input.getFeatureMapContainmentRightMoveOrderScope();
		final Resource origin = input.getFeatureMapContainmentOriginMoveOrderScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map move] from right to left : the
		// ReferenceChange Node node1[firstKey move] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// mapNode1 contains node1 through the feature firstKey in left, in the first position
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof List);
		assertEquals(2, ((List)firstKey).size());
		assertEquals(leftNode1, ((List)firstKey).get(0));
		// mapNode1 contains node1 through the map in left, in the first position
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = leftMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)map).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentMoveKeyChange_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightMoveKeyChangeScope();
		final Resource origin = input.getFeatureMapContainmentOriginMoveKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1[firstKey move] from left to right : the FeatureMapChange
		// <firstKey> Node node1 [map move] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).contains(rightNode1));
		// mapNode1 contains node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).basicContains(eSFFirstKey, rightNode1));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentMoveKeyChange_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightMoveKeyChangeScope();
		final Resource origin = input.getFeatureMapContainmentOriginMoveKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1[firstKey move] from right to left : the FeatureMapChange
		// <firstKey> Node node1 [map move] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode2 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode2");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFSecondKey = rightMapNode1.eClass().getEStructuralFeature("secondKey");
		assertNotNull(eSFSecondKey);
		final Object firstKey = rightMapNode1.eGet(eSFSecondKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).contains(rightNode1));
		// mapNode2 contains node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).basicContains(eSFSecondKey, rightNode1));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentMoveKeyChange_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightMoveKeyChangeScope();
		final Resource origin = input.getFeatureMapContainmentOriginMoveKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map move] from right to left : the
		// ReferenceChange Node node1[firstKey move] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode2 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode2");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFSecondKey = rightMapNode1.eClass().getEStructuralFeature("secondKey");
		assertNotNull(eSFSecondKey);
		final Object firstKey = rightMapNode1.eGet(eSFSecondKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).contains(rightNode1));
		// mapNode2 contains node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).basicContains(eSFSecondKey, rightNode1));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayContainmentMoveKeyChange_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightMoveKeyChangeScope();
		final Resource origin = input.getFeatureMapContainmentOriginMoveKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map move] from left to right : the
		// ReferenceChange Node node1[firstKey move] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object firstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(firstKey instanceof Collection);
		assertTrue(((Collection)firstKey).contains(rightNode1));
		// mapNode1 contains node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)map).basicContains(eSFFirstKey, rightNode1));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentAdd_Left_LtR_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftAddScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightAddScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginAddScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1[firstKey add] from left to right : the FeatureMapChange
		// <firstKey> Node node1 [map add] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 references node1 through the feature firstKey in right
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertEquals(1, ((List)leftFirstKey).size());
		assertEquals(leftNode1, ((List)leftFirstKey).get(0));
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertEquals(2, ((List)rightFirstKey).size());
		assertEquals(rightNode1, ((List)rightFirstKey).get(1));
		// mapNode1 references node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMap).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMap).get(eSFFirstKey, 0, true));
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)rightMap).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMap).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentAdd_Right_LtR_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftAddScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightAddScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginAddScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node2 [firstKey add] from left to right : the FeatureMapChange
		// <firstKey> Node node2 [map add] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node2 still exists in left
		final EObject leftNode2 = getNodeNamed(left, "node2");
		assertNotNull(leftNode2);
		// node2 still exists in right
		final EObject rightNode2 = getNodeNamed(right, "node2");
		assertNotNull(rightNode2);
		// mapNode1 doesn't references node2 through the feature firstKey in right
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertEquals(1, ((List)leftFirstKey).size());
		assertFalse(((List)leftFirstKey).contains(leftNode2));
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertTrue(((List)rightFirstKey).isEmpty());
		// mapNode1 doesn't references node2 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMap).size());
		assertFalse(((BasicFeatureMap)leftMap).contains(eSFFirstKey, leftNode2));
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)rightMap).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentAdd_Left_LtR_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftAddScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightAddScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginAddScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map add] from left to right : the ReferenceChange
		// Node node1[firstKey add] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 references node1 through the feature firstKey in right
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertEquals(1, ((List)leftFirstKey).size());
		assertEquals(leftNode1, ((List)leftFirstKey).get(0));
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertEquals(2, ((List)rightFirstKey).size());
		assertEquals(rightNode1, ((List)rightFirstKey).get(1));
		// mapNode1 references node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMap).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMap).get(eSFFirstKey, 0, true));
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)rightMap).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMap).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentAdd_Right_LtR_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftAddScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightAddScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginAddScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map add] from left to right : the ReferenceChange
		// Node node1[firstKey add] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node2 still exists in left
		final EObject leftNode2 = getNodeNamed(left, "node2");
		assertNotNull(leftNode2);
		// node2 still exists in right
		final EObject rightNode2 = getNodeNamed(right, "node2");
		assertNotNull(rightNode2);
		// mapNode1 doesn't references node2 through the feature firstKey in right
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertEquals(1, ((List)leftFirstKey).size());
		assertFalse(((List)leftFirstKey).contains(leftNode2));
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertTrue(((List)rightFirstKey).isEmpty());
		// mapNode1 doesn't references node2 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMap).size());
		assertFalse(((BasicFeatureMap)leftMap).contains(eSFFirstKey, leftNode2));
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)rightMap).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentAdd_Left_RtL_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftAddScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightAddScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginAddScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1[firstKey add] from right to left : the FeatureMapChange
		// <firstKey> Node node1 [map add] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 references node1 through the feature firstKey in right
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertEquals(0, ((List)leftFirstKey).size());
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertEquals(1, ((List)rightFirstKey).size());
		// mapNode1 references node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(0, ((BasicFeatureMap)leftMap).size());
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMap).size());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentAdd_Right_RtL_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftAddScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightAddScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginAddScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node2 [firstKey add] from right to left : the FeatureMapChange
		// <firstKey> Node node2 [map add] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node2 still exists in left
		final EObject leftNode2 = getNodeNamed(left, "node2");
		assertNotNull(leftNode2);
		// node2 still exists in right
		final EObject rightNode2 = getNodeNamed(right, "node2");
		assertNotNull(rightNode2);
		// mapNode1 references node2 through the feature firstKey in right
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertEquals(2, ((List)leftFirstKey).size());
		assertEquals(leftNode2, ((List)leftFirstKey).get(1));
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertEquals(1, ((List)rightFirstKey).size());
		assertEquals(rightNode2, ((List)rightFirstKey).get(0));
		// mapNode1 references node2 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)leftMap).size());
		assertEquals(leftNode2, ((BasicFeatureMap)leftMap).get(eSFFirstKey, 1, true));
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMap).size());
		assertEquals(rightNode2, ((BasicFeatureMap)rightMap).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentAdd_Left_RtL_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftAddScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightAddScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginAddScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map add] from right to left : the ReferenceChange
		// Node node1[firstKey add] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 references node1 through the feature firstKey in right
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertEquals(0, ((List)leftFirstKey).size());
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertEquals(1, ((List)rightFirstKey).size());
		// mapNode1 references node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(0, ((BasicFeatureMap)leftMap).size());
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMap).size());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentAdd_Right_RtL_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftAddScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightAddScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginAddScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map add] from right to left : the ReferenceChange
		// Node node1[firstKey add] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node2 still exists in left
		final EObject leftNode2 = getNodeNamed(left, "node2");
		assertNotNull(leftNode2);
		// node2 still exists in right
		final EObject rightNode2 = getNodeNamed(right, "node2");
		assertNotNull(rightNode2);
		// mapNode1 references node2 through the feature firstKey in right
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertEquals(2, ((List)leftFirstKey).size());
		assertEquals(leftNode2, ((List)leftFirstKey).get(1));
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertEquals(1, ((List)rightFirstKey).size());
		assertEquals(rightNode2, ((List)rightFirstKey).get(0));
		// mapNode1 references node2 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)leftMap).size());
		assertEquals(leftNode2, ((BasicFeatureMap)leftMap).get(eSFFirstKey, 1, true));
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMap).size());
		assertEquals(rightNode2, ((BasicFeatureMap)rightMap).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentDelete_Left_LtR_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftDeleteScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightDeleteScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginDeleteScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node2 [firstKey delete] from left to right : the FeatureMapChange
		// <firstKey> Node node2 [map delete] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node2 still exists in left
		final EObject leftNode2 = getNodeNamed(left, "node2");
		assertNotNull(leftNode2);
		// node2 still exists in right
		final EObject rightNode2 = getNodeNamed(right, "node2");
		assertNotNull(rightNode2);
		// mapNode1 doesn't references node2 through the feature firstKey in right
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertEquals(1, ((List)leftFirstKey).size());
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertTrue(((List)rightFirstKey).isEmpty());
		// mapNode1 doesn't references node2 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMap).size());
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)rightMap).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentDelete_Right_LtR_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftDeleteScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightDeleteScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginDeleteScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1 [firstKey delete] from left to right : the FeatureMapChange
		// <firstKey> Node node1 [map delete] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 references node1 through the feature firstKey in right
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertEquals(1, ((List)leftFirstKey).size());
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertEquals(2, ((List)rightFirstKey).size());
		assertEquals(rightNode1, ((List)rightFirstKey).get(1));
		// mapNode1 references node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMap).size());
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)rightMap).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMap).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentDelete_Left_LtR_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftDeleteScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightDeleteScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginDeleteScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node2 [map delete] from left to right : the
		// ReferenceChange Node node2 [firstKey delete] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node2 still exists in left
		final EObject leftNode2 = getNodeNamed(left, "node2");
		assertNotNull(leftNode2);
		// node2 still exists in right
		final EObject rightNode2 = getNodeNamed(right, "node2");
		assertNotNull(rightNode2);
		// mapNode1 doesn't references node2 through the feature firstKey in right
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertEquals(1, ((List)leftFirstKey).size());
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertTrue(((List)rightFirstKey).isEmpty());
		// mapNode1 doesn't references node2 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMap).size());
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)rightMap).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentDelete_Right_LtR_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftDeleteScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightDeleteScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginDeleteScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map delete] from left to right : the
		// ReferenceChange Node node1 [firstKey delete] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 references node1 through the feature firstKey in right
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertEquals(1, ((List)leftFirstKey).size());
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertEquals(2, ((List)rightFirstKey).size());
		assertEquals(rightNode1, ((List)rightFirstKey).get(1));
		// mapNode1 references node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMap).size());
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)rightMap).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMap).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentDelete_Left_RtL_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftDeleteScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightDeleteScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginDeleteScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node2 [firstKey delete] from right to left : the FeatureMapChange
		// <firstKey> Node node2 [map delete] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node2 still exists in left
		final EObject leftNode2 = getNodeNamed(left, "node2");
		assertNotNull(leftNode2);
		// node2 still exists in right
		final EObject rightNode2 = getNodeNamed(right, "node2");
		assertNotNull(rightNode2);
		// mapNode1 references node2 through the feature firstKey in right
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertEquals(2, ((List)leftFirstKey).size());
		assertEquals(leftNode2, ((List)leftFirstKey).get(1));
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertEquals(1, ((List)rightFirstKey).size());
		assertEquals(rightNode2, ((List)rightFirstKey).get(0));
		// mapNode1 references node2 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)leftMap).size());
		assertEquals(leftNode2, ((BasicFeatureMap)leftMap).get(eSFFirstKey, 1, true));
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMap).size());
		assertEquals(rightNode2, ((BasicFeatureMap)rightMap).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentDelete_Right_RtL_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftDeleteScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightDeleteScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginDeleteScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1 [firstKey delete] from right to left : the FeatureMapChange
		// <firstKey> Node node1 [map delete] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 doesn't references node1 through the feature firstKey in right
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertTrue(((List)leftFirstKey).isEmpty());
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertEquals(1, ((List)rightFirstKey).size());
		// mapNode1 doesn't references node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)leftMap).isEmpty());
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMap).size());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentDelete_Left_RtL_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftDeleteScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightDeleteScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginDeleteScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node2 [map delete] from right to left : the
		// ReferenceChange Node node2 [firstKey delete] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node2 still exists in left
		final EObject leftNode2 = getNodeNamed(left, "node2");
		assertNotNull(leftNode2);
		// node2 still exists in right
		final EObject rightNode2 = getNodeNamed(right, "node2");
		assertNotNull(rightNode2);
		// mapNode1 references node2 through the feature firstKey in right
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertEquals(2, ((List)leftFirstKey).size());
		assertEquals(leftNode2, ((List)leftFirstKey).get(1));
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertEquals(1, ((List)rightFirstKey).size());
		assertEquals(rightNode2, ((List)rightFirstKey).get(0));
		// mapNode1 references node2 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)leftMap).size());
		assertEquals(leftNode2, ((BasicFeatureMap)leftMap).get(eSFFirstKey, 1, true));
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMap).size());
		assertEquals(rightNode2, ((BasicFeatureMap)rightMap).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentDelete_Right_RtL_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftDeleteScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightDeleteScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginDeleteScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node2 [map delete] from right to left : the
		// ReferenceChange Node node2 [firstKey delete] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 doesn't references node1 through the feature firstKey in right
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertTrue(((List)leftFirstKey).isEmpty());
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertEquals(1, ((List)rightFirstKey).size());
		// mapNode1 doesn't references node1 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)leftMap).isEmpty());
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMap).size());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentMoveOrder_LtR_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftMoveOrderScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightMoveOrderScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginMoveOrderScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1 [firstKey move] from left to right : the FeatureMapChange
		// <firstKey> Node node1 [map move] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 references node1 through the feature firstKey in right, in the last position
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertEquals(2, ((List)leftFirstKey).size());
		assertEquals(leftNode1, ((List)leftFirstKey).get(1));
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertEquals(2, ((List)rightFirstKey).size());
		assertEquals(rightNode1, ((List)rightFirstKey).get(1));
		// mapNode1 references node1 through the map in right, in the last position
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)leftMap).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMap).get(eSFFirstKey, 1, true));
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)rightMap).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMap).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentMoveOrder_LtR_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftMoveOrderScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightMoveOrderScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginMoveOrderScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map move] from left to right : the
		// ReferenceChange Node node1[firstKey move] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 references node1 through the feature firstKey in right, in the last position
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertEquals(2, ((List)leftFirstKey).size());
		assertEquals(leftNode1, ((List)leftFirstKey).get(1));
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertEquals(2, ((List)rightFirstKey).size());
		assertEquals(rightNode1, ((List)rightFirstKey).get(1));
		// mapNode1 references node1 through the map in right, in the last position
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)leftMap).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMap).get(eSFFirstKey, 1, true));
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)rightMap).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMap).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(6), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentMoveOrder_RtL_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftMoveOrderScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightMoveOrderScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginMoveOrderScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1 [firstKey move] from right to left : the FeatureMapChange
		// <firstKey> Node node1 [map move] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 references node1 through the feature firstKey in right, in the last position
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertEquals(2, ((List)leftFirstKey).size());
		assertEquals(leftNode1, ((List)leftFirstKey).get(0));
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertEquals(2, ((List)rightFirstKey).size());
		assertEquals(rightNode1, ((List)rightFirstKey).get(0));
		// mapNode1 references node1 through the map in right, in the last position
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)leftMap).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMap).get(eSFFirstKey, 0, true));
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)rightMap).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMap).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void test3wayNonContainmentMoveOrder_RtL_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftMoveOrderScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightMoveOrderScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginMoveOrderScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// In this case, left and right diffs represent the same case. There is no need to test both sides.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map move] from right to left : the
		// ReferenceChange Node node1[firstKey move] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 references node1 through the feature firstKey in right, in the last position
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = rightMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftFirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftFirstKey instanceof List);
		assertEquals(2, ((List)leftFirstKey).size());
		assertEquals(leftNode1, ((List)leftFirstKey).get(0));
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertEquals(2, ((List)rightFirstKey).size());
		assertEquals(rightNode1, ((List)rightFirstKey).get(0));
		// mapNode1 references node1 through the map in right, in the last position
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)leftMap).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMap).get(eSFFirstKey, 0, true));
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)rightMap).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMap).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getDifferences().size()));
	}

	private EObject getNodeNamed(Resource res, String name) {
		final Iterator<EObject> iterator = EcoreUtil.getAllProperContents(res, false);
		while (iterator.hasNext()) {
			final EObject next = iterator.next();
			final EStructuralFeature nameFeature = next.eClass().getEStructuralFeature("name");
			if (nameFeature != null && name.equals(next.eGet(nameFeature))) {
				return next;
			}
		}
		return null;
	}
}
