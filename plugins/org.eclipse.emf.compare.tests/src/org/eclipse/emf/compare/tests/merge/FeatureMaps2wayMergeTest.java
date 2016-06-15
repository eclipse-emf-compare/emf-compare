/*******************************************************************************
 * Copyright (c) 2014 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Alexandra Buzila - Fixes for bug 446252
 *     Stefan Dirix - Testcases for Bugs 453218 and 454579
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.not;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.isEquivalentTo;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.FeatureMapChange;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.merge.data.IndividualDiffInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

/**
 * A FeatureMapChange is always associated with a ReferenceChange. The FeatureMapChange is equivalent to his
 * ReferenceChange and vice-versa. Each case will be done on the FeatureMapChange and on the ReferenceChange.
 */
@SuppressWarnings({"nls", "rawtypes", "unchecked" })
public class FeatureMaps2wayMergeTest {

	private IndividualDiffInputData input = new IndividualDiffInputData();

	private final IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	@Test
	public void test2wayContainmentAdd_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftAddScope();
		final Resource right = input.getFeatureMapContainmentRightAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 add diffs, 2 delete diffs (delete diffs are detected because same models are used for 2-way
		// and 3-way comparison, so the right add detected in 3-way is a delete in 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentAdd_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftAddScope();
		final Resource right = input.getFeatureMapContainmentRightAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 add diffs, 2 delete diffs (delete diffs are detected because same models are used for 2-way
		// and 3-way comparison, so the right add detected in 3-way is a delete in 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentAdd_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftAddScope();
		final Resource right = input.getFeatureMapContainmentRightAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 add diffs, 2 delete diffs (delete diffs are detected because same models are used for 2-way
		// and 3-way comparison, so the right add detected in 3-way is a delete in 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

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
		assertEquals(0, ((Collection)firstKey).size());
		// mapNode1 doesn't contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = leftMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertEquals(0, ((BasicFeatureMap)map).size(eSFFirstKey));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentAdd_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftAddScope();
		final Resource right = input.getFeatureMapContainmentRightAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 add diffs, 2 delete diffs (delete diffs are detected because same models are used for 2-way
		// and 3-way comparison, so the right add detected in 3-way is a delete in 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

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
		assertEquals(0, ((Collection)firstKey).size());
		// mapNode1 doesn't contains node1 through the map in right
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = leftMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertEquals(0, ((BasicFeatureMap)map).size(eSFFirstKey));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentDelete_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 delete diffs, 2 add diffs (add diffs are detected because same models are used for 2-way
		// and 3-way comparison, so the right delete detected in 3-way is a add in 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.DELETE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node2[firstKey delete] from left to right : the FeatureMapChange
		// <firstKey> Node node2 [map delete] will be merge from left to right too.
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
		assertEquals(0, ((Collection)firstKey).size());
		// mapNode1 doesn't contains node2 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertEquals(0, ((BasicFeatureMap)map).size(eSFFirstKey));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentDelete_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 delete diffs, 2 add diffs (add diffs are detected because same models are used for 2-way
		// and 3-way comparison, so the right delete detected in 3-way is a add in 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.DELETE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node2 [map delete] from left to right : the
		// ReferenceChange Node node2[firstKey delete] will be merge from left to right too.
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
		assertEquals(0, ((Collection)firstKey).size());
		// mapNode1 doesn't contains node2 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object map = rightMapNode1.eGet(eSFmap);
		assertTrue(map instanceof BasicFeatureMap);
		assertEquals(0, ((BasicFeatureMap)map).size(eSFFirstKey));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentDelete_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 delete diffs, 2 add diffs (add diffs are detected because same models are used for 2-way
		// and 3-way comparison, so the right delete detected in 3-way is a add in 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.DELETE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node2[firstKey delete] from right to left: the FeatureMapChange
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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentDelete_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 delete diffs, 2 add diffs (add diffs are detected because same models are used for 2-way
		// and 3-way comparison, so the right delete detected in 3-way is a add in 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.DELETE), instanceOf(FeatureMapChange.class)));

		// Merge the ReferenceChange Node node2[firstKey delete] from right to left: the FeatureMapChange
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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentKeyChange_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 move diffs, 2 key change diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right move/key changes detected in 3-way are a simple move/key changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentKeyChange_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 move diffs, 2 key change diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right move/key changes detected in 3-way are a simple move/key changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.CHANGE), instanceOf(FeatureMapChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentKeyChange_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 move diffs, 2 key change diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right move/key changes detected in 3-way are a simple move/key changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentKeyChange_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 move diffs, 2 key change diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right move/key changes detected in 3-way are a simple move/key changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.CHANGE), instanceOf(FeatureMapChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentMove_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveScope();
		final Resource right = input.getFeatureMapContainmentRightMoveScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 4 moves diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right moves changes detected in 3-way are the same changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentMove_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveScope();
		final Resource right = input.getFeatureMapContainmentRightMoveScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 4 moves diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right moves changes detected in 3-way are the same changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentMove_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveScope();
		final Resource right = input.getFeatureMapContainmentRightMoveScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 4 moves diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right moves changes detected in 3-way are the same changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1[firstKey move] from right to left : the FeatureMapChange
		// <firstKey> Node node1 [map move] will be merge from right to left too.
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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentMove_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveScope();
		final Resource right = input.getFeatureMapContainmentRightMoveScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 4 moves diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right moves changes detected in 3-way are the same changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentMoveOrder_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveOrderScope();
		final Resource right = input.getFeatureMapContainmentRightMoveOrderScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 4 moves diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right moves changes detected in 3-way are the same changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1[firstKey move] from left to right : the FeatureMapChange
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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentMoveOrder_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveOrderScope();
		final Resource right = input.getFeatureMapContainmentRightMoveOrderScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 4 moves diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right moves changes detected in 3-way are the same changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentMoveOrder_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveOrderScope();
		final Resource right = input.getFeatureMapContainmentRightMoveOrderScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 4 moves diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right moves changes detected in 3-way are the same changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentMoveOrder_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveOrderScope();
		final Resource right = input.getFeatureMapContainmentRightMoveOrderScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 4 moves diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right moves changes detected in 3-way are the same changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentMoveKeyChange_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightMoveKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 4 moves diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right moves changes detected in 3-way are the same changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentMoveKeyChange_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightMoveKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 4 moves diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right moves changes detected in 3-way are the same changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentMoveKeyChange_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightMoveKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 4 moves diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right moves changes detected in 3-way are the same changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1[firstKey move] from left to right : the FeatureMapChange
		// <firstKey> Node node1 [map move] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode2 contains node1 through the feature secondKey in right
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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentMoveKeyChange_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftMoveKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightMoveKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 4 moves diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right moves changes detected in 3-way are the same changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map move] from left to right : the
		// ReferenceChange Node node1[firstKey move] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode2 contains node1 through the feature secondKey in right
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
		// after one diff and it's equivalent have been correctly merged, we should have
		// two more equivalent differences left
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentMoveToInside_RtL() throws IOException {
		final ResourceSet resourceSet = new ResourceSetImpl();

		final Resource left = input.getFeatureMapContainmentMoveInside(resourceSet);
		final Resource right = input.getFeatureMapContainmentMoveOutside(resourceSet);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		// reject all -> element a is moved out from a featuremap-containment
		for (Diff diff : differences) {
			mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		}

		// no differences should be left
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentMoveToInside_LtR() throws IOException {
		final ResourceSet resourceSet = new ResourceSetImpl();

		final Resource left = input.getFeatureMapContainmentMoveInside(resourceSet);
		final Resource right = input.getFeatureMapContainmentMoveOutside(resourceSet);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		// accept all -> element a is moved into a featuremap-containment
		for (Diff diff : differences) {
			mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		}

		// no differences should be left
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentMoveToOutside_LtR() throws IOException {
		final ResourceSet resourceSet = new ResourceSetImpl();

		final Resource left = input.getFeatureMapContainmentMoveOutside(resourceSet);
		final Resource right = input.getFeatureMapContainmentMoveInside(resourceSet);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		// check if each featuremapchange / referencechange pair is equivalent
		final Diff featureMapChangeDiff = Iterators.find(differences.iterator(),
				and(instanceOf(FeatureMapChange.class), ofKind(DifferenceKind.MOVE)));

		final Diff referenceChangeDiff = Iterators.find(differences.iterator(),
				and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.MOVE),
						not(isEquivalentTo(featureMapChangeDiff))));

		assertEquals(2, featureMapChangeDiff.getEquivalence().getDifferences().size());
		assertEquals(2, referenceChangeDiff.getEquivalence().getDifferences().size());

		// accept both
		mergerRegistry.getHighestRankingMerger(featureMapChangeDiff).copyLeftToRight(featureMapChangeDiff,
				new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(referenceChangeDiff).copyLeftToRight(referenceChangeDiff,
				new BasicMonitor());

		// no differences should be left
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void test2wayContainmentMoveToOutside_RtL() throws IOException {
		final ResourceSet resourceSet = new ResourceSetImpl();

		final Resource left = input.getFeatureMapContainmentMoveOutside(resourceSet);
		final Resource right = input.getFeatureMapContainmentMoveInside(resourceSet);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		// check if each featuremapchange / referencechange pair is equivalent
		final Diff featureMapChangeDiff = Iterators.find(differences.iterator(),
				and(instanceOf(FeatureMapChange.class), ofKind(DifferenceKind.MOVE)));

		final Diff referenceChangeDiff = Iterators.find(differences.iterator(),
				and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.MOVE),
						not(isEquivalentTo(featureMapChangeDiff))));

		assertEquals(2, featureMapChangeDiff.getEquivalence().getDifferences().size());
		assertEquals(2, referenceChangeDiff.getEquivalence().getDifferences().size());

		// reject both
		mergerRegistry.getHighestRankingMerger(featureMapChangeDiff).copyRightToLeft(featureMapChangeDiff,
				new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(referenceChangeDiff).copyRightToLeft(referenceChangeDiff,
				new BasicMonitor());

		// no differences should be left
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void test2wayNonContainmentAdd_LtR_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftAddScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightAddScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 add diffs, 2 delete diffs (delete diffs are detected because same models are used for 2-way
		// and 3-way comparison, so the right add detected in 3-way is a delete in 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayNonContainmentAdd_LtR_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftAddScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightAddScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 add diffs, 2 delete diffs (delete diffs are detected because same models are used for 2-way
		// and 3-way comparison, so the right add detected in 3-way is a delete in 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayNonContainmentAdd_RtL_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftAddScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightAddScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 add diffs, 2 delete diffs (delete diffs are detected because same models are used for 2-way
		// and 3-way comparison, so the right add detected in 3-way is a delete in 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayNonContainmentAdd_RtL_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftAddScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightAddScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 add diffs, 2 delete diffs (delete diffs are detected because same models are used for 2-way
		// and 3-way comparison, so the right add detected in 3-way is a delete in 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayNonContainmentDelete_LtR_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftDeleteScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightDeleteScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 add diffs, 2 delete diffs (add diffs are detected because same models are used for 2-way
		// and 3-way comparison, so the left delete detected in 3-way is a add in 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.DELETE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node2[firstKey delete] from left to right : the FeatureMapChange
		// <firstKey> Node node2 [map delete] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

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
		assertEquals(1, ((List)leftFirstKey).size());
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertEquals(0, ((List)rightFirstKey).size());
		// mapNode1 references node2 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMap).size());
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertEquals(0, ((BasicFeatureMap)rightMap).size());

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayNonContainmentDelete_LtR_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftDeleteScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightDeleteScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 add diffs, 2 delete diffs (add diffs are detected because same models are used for 2-way
		// and 3-way comparison, so the left delete detected in 3-way is a add in 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.DELETE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node2 [map delete] from left to right : the
		// ReferenceChange Node node2[firstKey delete] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

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
		assertEquals(1, ((List)leftFirstKey).size());
		final Object rightFirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightFirstKey instanceof List);
		assertEquals(0, ((List)rightFirstKey).size());
		// mapNode1 references node2 through the map in right
		final EStructuralFeature eSFmap = rightMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMap = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMap instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMap).size());
		final Object rightMap = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMap instanceof BasicFeatureMap);
		assertEquals(0, ((BasicFeatureMap)rightMap).size());

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayNonContainmentDelete_RtL_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftDeleteScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightDeleteScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 add diffs, 2 delete diffs (add diffs are detected because same models are used for 2-way
		// and 3-way comparison, so the left delete detected in 3-way is a add in 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.DELETE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node2[firstKey delete] from right to left : the FeatureMapChange
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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayNonContainmentDelete_RtL_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftDeleteScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightDeleteScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 2 add diffs, 2 delete diffs (add diffs are detected because same models are used for 2-way
		// and 3-way comparison, so the left delete detected in 3-way is a add in 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.DELETE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node2 [map delete] from right to left : the
		// ReferenceChange Node node2[firstKey delete] will be merge from right to left too.
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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayNonContainmentMoveOrder_LtR_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftMoveOrderScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightMoveOrderScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 4 moves diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right moves changes detected in 3-way are the same changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1[firstKey move] from left to right : the FeatureMapChange
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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayNonContainmentMoveOrder_LtR_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftMoveOrderScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightMoveOrderScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 4 moves diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right moves changes detected in 3-way are the same changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayNonContainmentMoveOrder_RtL_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftMoveOrderScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightMoveOrderScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 4 moves diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right moves changes detected in 3-way are the same changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the ReferenceChange Node node1[firstKey move] from right to left : the FeatureMapChange
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
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void test2wayNonContainmentMoveOrder_RtL_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftMoveOrderScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightMoveOrderScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// 4 moves diffs (detected because same models are used for 2-way and 3-way
		// comparison, so the right moves changes detected in 3-way are the same changes in
		// 2-way).
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

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
		assertEquals(2, comparison.getDifferences().size());
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
