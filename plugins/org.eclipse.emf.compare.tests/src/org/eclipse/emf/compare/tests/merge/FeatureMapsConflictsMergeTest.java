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
import org.eclipse.emf.compare.DifferenceSource;
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
public class FeatureMapsConflictsMergeTest {

	private IndividualDiffInputData input = new IndividualDiffInputData();

	private final IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	@Test
	public void testConflictContainmentLeftAddRightAddWithSameKey_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftAddRightAddWithSameKeyScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftAddRightAddWithSameKeyScope();
		final Resource origin = input.getFeatureMapContainmentOriginConflictLeftAddRightAddWithSameKeyScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 doesn't exists in the ancestor, is contained under mapNode2 in left, and contained under
		// mapNode1 in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey add] from left to right : the left
		// FeatureMapChange <firstKey> Node node1 [map add] will be merge from left to right too.
		// The rights diffs will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 doesn't contains node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)leftMapNode1FirstKey).isEmpty());
		// mapNode1 doesn't contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)rightMapNode1FirstKey).isEmpty());
		// mapNode2 contains node1 through the feature firstKey in left
		final EObject leftMapNode2 = getNodeNamed(left, "mapNode2");
		assertNotNull(leftMapNode2);
		final Object leftMapNode2FirstKey = leftMapNode2.eGet(eSFFirstKey);
		assertTrue(leftMapNode2FirstKey instanceof List);
		assertEquals(1, ((List)leftMapNode2FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode2FirstKey).get(0));
		// mapNode2 contains node1 through the feature firstKey in right
		final EObject rightMapNode2 = getNodeNamed(right, "mapNode2");
		assertNotNull(rightMapNode2);
		final Object rightMapNode2FirstKey = rightMapNode2.eGet(eSFFirstKey);
		assertTrue(rightMapNode2FirstKey instanceof List);
		assertEquals(1, ((List)rightMapNode2FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode2FirstKey).get(0));
		// mapNode1 doesn't contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)leftMapNode1Map).isEmpty());
		// mapNode1 doesn't contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)rightMapNode1Map).isEmpty());
		// mapNode2 contains node1 through the map in left
		final Object leftMapNode2Map = leftMapNode2.eGet(eSFmap);
		assertTrue(leftMapNode2Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMapNode2Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode2Map).get(eSFFirstKey, 0, true));
		// mapNode2 contains node1 through the map in right
		final Object rightMapNode2Map = rightMapNode2.eGet(eSFmap);
		assertTrue(rightMapNode2Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode2Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode2Map).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(4, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmentLeftAddRightAddWithSameKey_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftAddRightAddWithSameKeyScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftAddRightAddWithSameKeyScope();
		final Resource origin = input.getFeatureMapContainmentOriginConflictLeftAddRightAddWithSameKeyScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 doesn't exists in the ancestor, is contained under mapNode2 in left, and contained under
		// mapNode1 in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map add] from left to right : the left
		// ReferenceChange Node node1 [firstKey add] will be merge from left to right too.
		// The rights diffs will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 doesn't contains node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)leftMapNode1FirstKey).isEmpty());
		// mapNode1 doesn't contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)rightMapNode1FirstKey).isEmpty());
		// mapNode2 contains node1 through the feature firstKey in left
		final EObject leftMapNode2 = getNodeNamed(left, "mapNode2");
		assertNotNull(leftMapNode2);
		final Object leftMapNode2FirstKey = leftMapNode2.eGet(eSFFirstKey);
		assertTrue(leftMapNode2FirstKey instanceof List);
		assertEquals(1, ((List)leftMapNode2FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode2FirstKey).get(0));
		// mapNode2 contains node1 through the feature firstKey in right
		final EObject rightMapNode2 = getNodeNamed(right, "mapNode2");
		assertNotNull(rightMapNode2);
		final Object rightMapNode2FirstKey = rightMapNode2.eGet(eSFFirstKey);
		assertTrue(rightMapNode2FirstKey instanceof List);
		assertEquals(1, ((List)rightMapNode2FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode2FirstKey).get(0));
		// mapNode1 doesn't contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)leftMapNode1Map).isEmpty());
		// mapNode1 doesn't contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)rightMapNode1Map).isEmpty());
		// mapNode2 contains node1 through the map in left
		final Object leftMapNode2Map = leftMapNode2.eGet(eSFmap);
		assertTrue(leftMapNode2Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMapNode2Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode2Map).get(eSFFirstKey, 0, true));
		// mapNode2 contains node1 through the map in right
		final Object rightMapNode2Map = rightMapNode2.eGet(eSFmap);
		assertTrue(rightMapNode2Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode2Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode2Map).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(4, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmentLeftAddRightAddWithSameKey_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftAddRightAddWithSameKeyScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftAddRightAddWithSameKeyScope();
		final Resource origin = input.getFeatureMapContainmentOriginConflictLeftAddRightAddWithSameKeyScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 doesn't exists in the ancestor, is contained under mapNode2 in left, and contained under
		// mapNode1 in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey add] from right to left : the left
		// FeatureMapChange <firstKey> Node node1 [map add] will be merge from right to left too.
		// The rights diffs won't be merge from right to left.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 doesn't exists anymore in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNull(leftNode1);
		// node1 exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 doesn't contains node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)leftMapNode1FirstKey).isEmpty());
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof List);
		assertEquals(1, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(0));
		// mapNode2 doesn't contains node1 through the feature firstKey in left
		final EObject leftMapNode2 = getNodeNamed(left, "mapNode2");
		assertNotNull(leftMapNode2);
		final Object leftMapNode2FirstKey = leftMapNode2.eGet(eSFFirstKey);
		assertTrue(leftMapNode2FirstKey instanceof Collection);
		assertTrue(((Collection)leftMapNode2FirstKey).isEmpty());
		// mapNode2 doesn't contains node1 through the feature firstKey in right
		final EObject rightMapNode2 = getNodeNamed(right, "mapNode2");
		assertNotNull(rightMapNode2);
		final Object rightMapNode2FirstKey = rightMapNode2.eGet(eSFFirstKey);
		assertTrue(rightMapNode2FirstKey instanceof Collection);
		assertTrue(((Collection)rightMapNode2FirstKey).isEmpty());
		// mapNode1 doesn't contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)leftMapNode1Map).isEmpty());
		// mapNode1 contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 0, true));
		// mapNode2 doesn't contains node1 through the map in left
		final Object leftMapNode2Map = leftMapNode2.eGet(eSFmap);
		assertTrue(leftMapNode2Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)leftMapNode2Map).isEmpty());
		// mapNode2 doesn't contains node1 through the map in right
		final Object rightMapNode2Map = rightMapNode2.eGet(eSFmap);
		assertTrue(rightMapNode2Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)rightMapNode2Map).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmentLeftAddRightAddWithSameKey_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftAddRightAddWithSameKeyScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftAddRightAddWithSameKeyScope();
		final Resource origin = input.getFeatureMapContainmentOriginConflictLeftAddRightAddWithSameKeyScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 doesn't exists in the ancestor, is contained under mapNode2 in left, and contained under
		// mapNode1 in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map add] from right to left : the left
		// ReferenceChange Node node1 [firstKey add] will be merge from right to left too.
		// The rights diffs won't be merge from right to left.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 doesn't exists anymore in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNull(leftNode1);
		// node1 exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 doesn't contains node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)leftMapNode1FirstKey).isEmpty());
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof List);
		assertEquals(1, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(0));
		// mapNode2 doesn't contains node1 through the feature firstKey in left
		final EObject leftMapNode2 = getNodeNamed(left, "mapNode2");
		assertNotNull(leftMapNode2);
		final Object leftMapNode2FirstKey = leftMapNode2.eGet(eSFFirstKey);
		assertTrue(leftMapNode2FirstKey instanceof Collection);
		assertTrue(((Collection)leftMapNode2FirstKey).isEmpty());
		// mapNode2 doesn't contains node1 through the feature firstKey in right
		final EObject rightMapNode2 = getNodeNamed(right, "mapNode2");
		assertNotNull(rightMapNode2);
		final Object rightMapNode2FirstKey = rightMapNode2.eGet(eSFFirstKey);
		assertTrue(rightMapNode2FirstKey instanceof Collection);
		assertTrue(((Collection)rightMapNode2FirstKey).isEmpty());
		// mapNode1 doesn't contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)leftMapNode1Map).isEmpty());
		// mapNode1 contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 0, true));
		// mapNode2 doesn't contains node1 through the map in left
		final Object leftMapNode2Map = leftMapNode2.eGet(eSFmap);
		assertTrue(leftMapNode2Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)leftMapNode2Map).isEmpty());
		// mapNode2 doesn't contains node1 through the map in right
		final Object rightMapNode2Map = rightMapNode2.eGet(eSFmap);
		assertTrue(rightMapNode2Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)rightMapNode2Map).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmentLeftAddRightAddWithDifferentKey_LtR_1() throws IOException {
		final Resource left = input
				.getFeatureMapContainmentLeftConflictLeftAddRightAddWithDifferentKeyScope();
		final Resource right = input
				.getFeatureMapContainmentRightConflictLeftAddRightAddWithDifferentKeyScope();
		final Resource origin = input
				.getFeatureMapContainmentOriginConflictLeftAddRightAddWithDifferentKeyScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 doesn't exists in the ancestor, has firstKey in left, and secondKey in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey add] from left to right : the left
		// FeatureMapChange <firstKey> Node node1 [map add] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(1, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(0));
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof List);
		assertEquals(1, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(0));
		// mapNode1 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 0, true));
		// mapNode1 contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(4, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmentLeftAddRightAddWithDifferentKey_LtR_2() throws IOException {
		final Resource left = input
				.getFeatureMapContainmentLeftConflictLeftAddRightAddWithDifferentKeyScope();
		final Resource right = input
				.getFeatureMapContainmentRightConflictLeftAddRightAddWithDifferentKeyScope();
		final Resource origin = input
				.getFeatureMapContainmentOriginConflictLeftAddRightAddWithDifferentKeyScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 doesn't exists in the ancestor, has firstKey in left, and secondKey in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map add] from left to right : the left
		// ReferenceChange Node node1 [firstKey add] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(1, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(0));
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof List);
		assertEquals(1, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(0));
		// mapNode1 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 0, true));
		// mapNode1 contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(4, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmentLeftAddRightAddWithDifferentKey_RtL_1() throws IOException {
		final Resource left = input
				.getFeatureMapContainmentLeftConflictLeftAddRightAddWithDifferentKeyScope();
		final Resource right = input
				.getFeatureMapContainmentRightConflictLeftAddRightAddWithDifferentKeyScope();
		final Resource origin = input
				.getFeatureMapContainmentOriginConflictLeftAddRightAddWithDifferentKeyScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 doesn't exists in the ancestor, has firstKey in left, and secondKey in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey add] from right to left : the left
		// FeatureMapChange <firstKey> Node node1 [map add] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 doesn't exists anymore in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 doesn't contains node1 in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFSecondKey = leftMapNode1.eClass().getEStructuralFeature("secondKey");
		final Object leftMapNode1SecondKey = leftMapNode1.eGet(eSFSecondKey);
		assertTrue(leftMapNode1SecondKey instanceof List);
		assertTrue(((List)leftMapNode1SecondKey).isEmpty());
		// mapNode1 contains node1 through the feature secondKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1SecondKey = rightMapNode1.eGet(eSFSecondKey);
		assertTrue(rightMapNode1SecondKey instanceof List);
		assertEquals(1, ((List)rightMapNode1SecondKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1SecondKey).get(0));
		// mapNode1 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)leftMapNode1Map).isEmpty());
		// mapNode1 contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFSecondKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmentLeftAddRightAddWithDifferentKey_RtL_2() throws IOException {
		final Resource left = input
				.getFeatureMapContainmentLeftConflictLeftAddRightAddWithDifferentKeyScope();
		final Resource right = input
				.getFeatureMapContainmentRightConflictLeftAddRightAddWithDifferentKeyScope();
		final Resource origin = input
				.getFeatureMapContainmentOriginConflictLeftAddRightAddWithDifferentKeyScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 doesn't exists in the ancestor, has firstKey in left, and secondKey in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map add] from right to left : the left
		// ReferenceChange Node node1 [firstKey add] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 doesn't exists anymore in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 doesn't contains node1 in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFSecondKey = leftMapNode1.eClass().getEStructuralFeature("secondKey");
		final Object leftMapNode1SecondKey = leftMapNode1.eGet(eSFSecondKey);
		assertTrue(leftMapNode1SecondKey instanceof List);
		assertTrue(((List)leftMapNode1SecondKey).isEmpty());
		// mapNode1 contains node1 through the feature secondKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1SecondKey = rightMapNode1.eGet(eSFSecondKey);
		assertTrue(rightMapNode1SecondKey instanceof List);
		assertEquals(1, ((List)rightMapNode1SecondKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1SecondKey).get(0));
		// mapNode1 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)leftMapNode1Map).isEmpty());
		// mapNode1 contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFSecondKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmentLeftKeyChangeRightDelete_Left_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftKeyChangeRightDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftKeyChangeRightDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginConflictLeftKeyChangeRightDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 has firstKey in the ancestor, has secondKey in left, and doesn't exists in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [secondKey move] from left to right : the left
		// FeatureMapChange <secondKey> Node node1 [map entry key change] will be merge from left to right
		// too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature secondKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFSecondKey = leftMapNode1.eClass().getEStructuralFeature("secondKey");
		assertNotNull(eSFSecondKey);
		final Object leftMapNode1SecondKey = leftMapNode1.eGet(eSFSecondKey);
		assertTrue(leftMapNode1SecondKey instanceof List);
		assertEquals(leftNode1, ((List)leftMapNode1SecondKey).get(0));
		// mapNode1 contains node1 through the feature secondKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1SecondKey = rightMapNode1.eGet(eSFSecondKey);
		assertTrue(rightMapNode1SecondKey instanceof List);
		assertEquals(rightNode1, ((List)rightMapNode1SecondKey).get(0));
		// mapNode1 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFSecondKey, 0, true));
		// mapNode1 doesn't contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFSecondKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(4, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmentLeftKeyChangeRightDelete_Right_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftKeyChangeRightDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftKeyChangeRightDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginConflictLeftKeyChangeRightDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 has firstKey in the ancestor, has secondKey in left, and doesn't exists in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), instanceOf(ReferenceChange.class)));

		// Merge the right ReferenceChange Node node1 [firstKey delete] from left to right : the right
		// FeatureMapChange <firstKey> Node node1 [map delete] will be merge from left to right
		// too.
		// The rights diffs will remains unmerged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature secondKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFSecondKey = leftMapNode1.eClass().getEStructuralFeature("secondKey");
		assertNotNull(eSFSecondKey);
		final Object leftMapNode1SecondKey = leftMapNode1.eGet(eSFSecondKey);
		assertTrue(leftMapNode1SecondKey instanceof List);
		assertEquals(leftNode1, ((List)leftMapNode1SecondKey).get(0));
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof List);
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(0));
		// mapNode1 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFSecondKey, 0, true));
		// mapNode1 doesn't contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmentLeftKeyChangeRightDelete_Left_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftKeyChangeRightDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftKeyChangeRightDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginConflictLeftKeyChangeRightDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 has firstKey in the ancestor, has secondKey in left, and doesn't exists in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.CHANGE), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <secondKey> Node node1 [map entry key change] from left to right :
		// the left ReferenceChange Node node1 [secondKey move] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature secondKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFSecondKey = leftMapNode1.eClass().getEStructuralFeature("secondKey");
		assertNotNull(eSFSecondKey);
		final Object leftMapNode1SecondKey = leftMapNode1.eGet(eSFSecondKey);
		assertTrue(leftMapNode1SecondKey instanceof List);
		assertEquals(leftNode1, ((List)leftMapNode1SecondKey).get(0));
		// mapNode1 contains node1 through the feature secondKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1SecondKey = rightMapNode1.eGet(eSFSecondKey);
		assertTrue(rightMapNode1SecondKey instanceof List);
		assertEquals(rightNode1, ((List)rightMapNode1SecondKey).get(0));
		// mapNode1 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFSecondKey, 0, true));
		// mapNode1 doesn't contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFSecondKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(4, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmentLeftKeyChangeRightDelete_Right_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftKeyChangeRightDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftKeyChangeRightDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginConflictLeftKeyChangeRightDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 has firstKey in the ancestor, has secondKey in left, and doesn't exists in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), instanceOf(FeatureMapChange.class)));

		// Merge the right FeatureMapChange <firstKey> Node node1 [map delete] from left to right : the right
		// ReferenceChange Node node1 [firstKey delete] will be merge from left to right too.
		// The rights diffs will remains unmerged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature secondKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFSecondKey = leftMapNode1.eClass().getEStructuralFeature("secondKey");
		assertNotNull(eSFSecondKey);
		final Object leftMapNode1SecondKey = leftMapNode1.eGet(eSFSecondKey);
		assertTrue(leftMapNode1SecondKey instanceof List);
		assertEquals(leftNode1, ((List)leftMapNode1SecondKey).get(0));
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof List);
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(0));
		// mapNode1 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFSecondKey, 0, true));
		// mapNode1 doesn't contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmentLeftKeyChangeRightDelete_Left_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftKeyChangeRightDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftKeyChangeRightDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginConflictLeftKeyChangeRightDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 has firstKey in the ancestor, has secondKey in left, and doesn't exists in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [secondKey move] from right to left : the left
		// FeatureMapChange <secondKey> Node node1 [map entry key change] will be merge from right to left
		// too.
		// The rights diffs will remains unmerged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 doesn't exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(0));
		// mapNode1 doesn't contains node1 in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)rightMapNode1FirstKey).isEmpty());
		// mapNode1 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 0, true));
		// mapNode1 doesn't contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)rightMapNode1Map).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmentLeftKeyChangeRightDelete_Right_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftKeyChangeRightDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftKeyChangeRightDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginConflictLeftKeyChangeRightDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 has firstKey in the ancestor, has secondKey in left, and doesn't exists in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey delete] from right to left : the left
		// FeatureMapChange <firstKey> Node node1 [map delete] will be merge from right to left
		// too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 doesn't exists anymore in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNull(leftNode1);
		// node1 doesn't exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNull(rightNode1);
		// mapNode1 doesn't contains node1 in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)leftMapNode1FirstKey).isEmpty());
		// mapNode1 doesn't contains node1 in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)rightMapNode1FirstKey).isEmpty());
		// mapNode1 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)leftMapNode1Map).isEmpty());
		// mapNode1 doesn't contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)rightMapNode1Map).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(4, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmentLeftKeyChangeRightDelete_Left_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftKeyChangeRightDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftKeyChangeRightDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginConflictLeftKeyChangeRightDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 has firstKey in the ancestor, has secondKey in left, and doesn't exists in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.CHANGE), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <secondKey> Node node1 [map entry key change] from right to left :
		// the left ReferenceChange Node node1 [secondKey move] will be merge from right to left too.
		// The rights diffs will remains unmerged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 doesn't exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(0));
		// mapNode1 doesn't contains node1 in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)rightMapNode1FirstKey).isEmpty());
		// mapNode1 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 0, true));
		// mapNode1 doesn't contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)rightMapNode1Map).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmentLeftKeyChangeRightDelete_Right_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftKeyChangeRightDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftKeyChangeRightDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginConflictLeftKeyChangeRightDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 has firstKey in the ancestor, has secondKey in left, and doesn't exists in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), instanceOf(FeatureMapChange.class)));

		// Merge the right FeatureMapChange <firstKey> Node node1 [map delete] from right to left :
		// the right ReferenceChange Node node1 [firstKey delete] will be merge from right to left
		// too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 doesn't exists anymore in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNull(leftNode1);
		// node1 doesn't exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNull(rightNode1);
		// mapNode1 doesn't contains node1 in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)leftMapNode1FirstKey).isEmpty());
		// mapNode1 doesn't contains node1 in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)rightMapNode1FirstKey).isEmpty());
		// mapNode1 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)leftMapNode1Map).isEmpty());
		// mapNode1 doesn't contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)rightMapNode1Map).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(4, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmenLeftMoveRightMove_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftMoveRightMoveScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftMoveRightMoveScope();
		final Resource origin = input.getFeatureMapContainmentOriginConflictLeftMoveRightMoveScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 is contained under mapNode1 in the ancestor, under mapNode2 in left, and
		// under mapNode3 in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey add] from left to right : the left
		// FeatureMapChange <firstKey> Node node1 [map add] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode2 contains node1 through the feature firstKey in left
		final EObject leftMapNode2 = getNodeNamed(left, "mapNode2");
		assertNotNull(leftMapNode2);
		final EStructuralFeature eSFFirstKey = leftMapNode2.eClass().getEStructuralFeature("firstKey");
		final Object leftMapNode2FirstKey = leftMapNode2.eGet(eSFFirstKey);
		assertTrue(leftMapNode2FirstKey instanceof List);
		assertEquals(1, ((List)leftMapNode2FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode2FirstKey).get(0));
		// mapNode2 contains node1 through the feature firstKey in right
		final EObject rightMapNode2 = getNodeNamed(right, "mapNode2");
		assertNotNull(rightMapNode2);
		final Object rightMapNode2FirstKey = rightMapNode2.eGet(eSFFirstKey);
		assertTrue(rightMapNode2FirstKey instanceof List);
		assertEquals(1, ((List)rightMapNode2FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode2FirstKey).get(0));
		// mapNode2 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode2.eClass().getEStructuralFeature("map");
		final Object leftMapNode2Map = leftMapNode2.eGet(eSFmap);
		assertTrue(leftMapNode2Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMapNode2Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode2Map).get(eSFFirstKey, 0, true));
		// mapNode2 contains node1 through the map in right
		final Object rightMapNode2Map = rightMapNode2.eGet(eSFmap);
		assertTrue(rightMapNode2Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode2Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode2Map).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(4, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmenLeftMoveRightMove_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftMoveRightMoveScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftMoveRightMoveScope();
		final Resource origin = input.getFeatureMapContainmentOriginConflictLeftMoveRightMoveScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 is contained under mapNode1 in the ancestor, under mapNode2 in left, and
		// under mapNode3 in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map add] from left to right : the left
		// ReferenceChange Node node1 [firstKey add] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode2 contains node1 through the feature firstKey in left
		final EObject leftMapNode2 = getNodeNamed(left, "mapNode2");
		assertNotNull(leftMapNode2);
		final EStructuralFeature eSFFirstKey = leftMapNode2.eClass().getEStructuralFeature("firstKey");
		final Object leftMapNode2FirstKey = leftMapNode2.eGet(eSFFirstKey);
		assertTrue(leftMapNode2FirstKey instanceof List);
		assertEquals(1, ((List)leftMapNode2FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode2FirstKey).get(0));
		// mapNode2 contains node1 through the feature firstKey in right
		final EObject rightMapNode2 = getNodeNamed(right, "mapNode2");
		assertNotNull(rightMapNode2);
		final Object rightMapNode2FirstKey = rightMapNode2.eGet(eSFFirstKey);
		assertTrue(rightMapNode2FirstKey instanceof List);
		assertEquals(1, ((List)rightMapNode2FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode2FirstKey).get(0));
		// mapNode2 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode2.eClass().getEStructuralFeature("map");
		final Object leftMapNode2Map = leftMapNode2.eGet(eSFmap);
		assertTrue(leftMapNode2Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMapNode2Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode2Map).get(eSFFirstKey, 0, true));
		// mapNode2 contains node1 through the map in right
		final Object rightMapNode2Map = rightMapNode2.eGet(eSFmap);
		assertTrue(rightMapNode2Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode2Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode2Map).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(4, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmenLeftMoveRightMove_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftMoveRightMoveScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftMoveRightMoveScope();
		final Resource origin = input.getFeatureMapContainmentOriginConflictLeftMoveRightMoveScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 is contained under mapNode1 in the ancestor, under mapNode2 in left, and
		// under mapNode3 in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey add] from right to left : the left
		// FeatureMapChange <firstKey> Node node1 [map add] will be merge from right to left too.
		// The rights diffs will remains unmerged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode3 contains node1 through the feature firstKey in left
		final EObject leftMapNode3 = getNodeNamed(left, "mapNode3");
		assertNotNull(leftMapNode3);
		final EStructuralFeature eSFFirstKey = leftMapNode3.eClass().getEStructuralFeature("firstKey");
		final Object leftMapNode3FirstKey = leftMapNode3.eGet(eSFFirstKey);
		assertTrue(leftMapNode3FirstKey instanceof List);
		assertEquals(1, ((List)leftMapNode3FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode3FirstKey).get(0));
		// mapNode3 contains node1 through the feature firstKey in right
		final EObject rightMapNode3 = getNodeNamed(right, "mapNode3");
		assertNotNull(rightMapNode3);
		final Object rightMapNode3FirstKey = rightMapNode3.eGet(eSFFirstKey);
		assertTrue(rightMapNode3FirstKey instanceof List);
		assertEquals(1, ((List)rightMapNode3FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode3FirstKey).get(0));
		// mapNode3 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode3.eClass().getEStructuralFeature("map");
		final Object leftMapNode3Map = leftMapNode3.eGet(eSFmap);
		assertTrue(leftMapNode3Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMapNode3Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode3Map).get(eSFFirstKey, 0, true));
		// mapNode3 contains node1 through the map in right
		final Object rightMapNode3Map = rightMapNode3.eGet(eSFmap);
		assertTrue(rightMapNode3Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode3Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode3Map).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(4, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmenLeftMoveRightMove_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftMoveRightMoveScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftMoveRightMoveScope();
		final Resource origin = input.getFeatureMapContainmentOriginConflictLeftMoveRightMoveScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 is contained under mapNode1 in the ancestor, under mapNode2 in left, and
		// under mapNode3 in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map add] from right to left : the left
		// ReferenceChange Node node1 [firstKey add] will be merge from right to left too.
		// The rights diffs will remains unmerged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode3 contains node1 through the feature firstKey in left
		final EObject leftMapNode3 = getNodeNamed(left, "mapNode3");
		assertNotNull(leftMapNode3);
		final EStructuralFeature eSFFirstKey = leftMapNode3.eClass().getEStructuralFeature("firstKey");
		final Object leftMapNode3FirstKey = leftMapNode3.eGet(eSFFirstKey);
		assertTrue(leftMapNode3FirstKey instanceof List);
		assertEquals(1, ((List)leftMapNode3FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode3FirstKey).get(0));
		// mapNode3 contains node1 through the feature firstKey in right
		final EObject rightMapNode3 = getNodeNamed(right, "mapNode3");
		assertNotNull(rightMapNode3);
		final Object rightMapNode3FirstKey = rightMapNode3.eGet(eSFFirstKey);
		assertTrue(rightMapNode3FirstKey instanceof List);
		assertEquals(1, ((List)rightMapNode3FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode3FirstKey).get(0));
		// mapNode3 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode3.eClass().getEStructuralFeature("map");
		final Object leftMapNode3Map = leftMapNode3.eGet(eSFmap);
		assertTrue(leftMapNode3Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMapNode3Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode3Map).get(eSFFirstKey, 0, true));
		// mapNode3 contains node1 through the map in right
		final Object rightMapNode3Map = rightMapNode3.eGet(eSFmap);
		assertTrue(rightMapNode3Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode3Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode3Map).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(4, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmentLeftMoveOrderRightMoveOrder_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftMoveOrderRightMoveOrderScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftMoveOrderRightMoveOrderScope();
		final Resource origin = input
				.getFeatureMapContainmentOriginConflictLeftMoveOrderRightMoveOrderScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 is in the first position in the ancestor, in the second position in left, and in third
		// position in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey move] from left to right : the left
		// FeatureMapChange <firstKey> Node node1 [map move] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 at the second position in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(3, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(1));
		// mapNode1 contains node1 at the second position in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof List);
		assertEquals(3, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(1));
		// mapNode1 contains node1 at the second position of the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(3, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 1, true));
		// mapNode1 contains node1 at the second position of the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(3, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(4, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmentLeftMoveOrderRightMoveOrder_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftMoveOrderRightMoveOrderScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftMoveOrderRightMoveOrderScope();
		final Resource origin = input
				.getFeatureMapContainmentOriginConflictLeftMoveOrderRightMoveOrderScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 is in the first position in the ancestor, in the second position in left, and in third
		// position in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map move] from left to right : the left
		// ReferenceChange Node node1 [firstKey move] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 at the second position in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(3, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(1));
		// mapNode1 contains node1 at the second position in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof List);
		assertEquals(3, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(1));
		// mapNode1 contains node1 at the second position of the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(3, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 1, true));
		// mapNode1 contains node1 at the second position of the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(3, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(4, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmentLeftMoveOrderRightMoveOrder_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftMoveOrderRightMoveOrderScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftMoveOrderRightMoveOrderScope();
		final Resource origin = input
				.getFeatureMapContainmentOriginConflictLeftMoveOrderRightMoveOrderScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 is in the first position in the ancestor, in the second position in left, and in third
		// position in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey move] from right to left : the left
		// FeatureMapChange <firstKey> Node node1 [map move] will be merge from right to left too.
		// The rights diffs will remains unmerged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 at the first position in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(3, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(0));
		// mapNode1 contains node1 at the first position in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof List);
		assertEquals(3, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(2));
		// mapNode1 contains node1 at the first position of the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(3, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 0, true));
		// mapNode1 contains node1 at the first position of the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(3, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 2, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void testConflictContainmentLeftMoveOrderRightMoveOrder_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftConflictLeftMoveOrderRightMoveOrderScope();
		final Resource right = input.getFeatureMapContainmentRightConflictLeftMoveOrderRightMoveOrderScope();
		final Resource origin = input
				.getFeatureMapContainmentOriginConflictLeftMoveOrderRightMoveOrderScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 is in the first position in the ancestor, in the second position in left, and in third
		// position in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map move] from right to left : the left
		// ReferenceChange Node node1 [firstKey move] will be merge from right to left too.
		// The rights diffs will remains unmerged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 at the first position in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(3, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(0));
		// mapNode1 contains node1 at the first position in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof List);
		assertEquals(3, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(2));
		// mapNode1 contains node1 at the first position of the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(3, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 0, true));
		// mapNode1 contains node1 at the first position of the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(3, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 2, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void testConflictNonContainmentLeftMoveOrderRightMoveOrder_LtR_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input
				.getFeatureMapNonContainmentLeftConflictLeftMoveOrderRightMoveOrderScope(rs);
		final Resource right = input
				.getFeatureMapNonContainmentRightConflictLeftMoveOrderRightMoveOrderScope(rs);
		final Resource origin = input
				.getFeatureMapNonContainmentOriginConflictLeftMoveOrderRightMoveOrderScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 is in the first position in the ancestor, in the second position in left, and in third
		// position in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey move] from left to right : the left
		// FeatureMapChange <firstKey> Node node1 [map move] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 at the second position in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(3, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(1));
		// mapNode1 contains node1 at the second position in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof List);
		assertEquals(3, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(1));
		// mapNode1 contains node1 at the second position of the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(3, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 1, true));
		// mapNode1 contains node1 at the second position of the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(3, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(4, comparison.getDifferences().size());
	}

	@Test
	public void testConflictNonContainmentLeftMoveOrderRightMoveOrder_LtR_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input
				.getFeatureMapNonContainmentLeftConflictLeftMoveOrderRightMoveOrderScope(rs);
		final Resource right = input
				.getFeatureMapNonContainmentRightConflictLeftMoveOrderRightMoveOrderScope(rs);
		final Resource origin = input
				.getFeatureMapNonContainmentOriginConflictLeftMoveOrderRightMoveOrderScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 is in the first position in the ancestor, in the second position in left, and in third
		// position in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map move] from left to right : the left
		// ReferenceChange Node node1 [firstKey move] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 at the second position in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(3, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(1));
		// mapNode1 contains node1 at the second position in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof List);
		assertEquals(3, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(1));
		// mapNode1 contains node1 at the second position of the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(3, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 1, true));
		// mapNode1 contains node1 at the second position of the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(3, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(4, comparison.getDifferences().size());
	}

	@Test
	public void testConflictNonContainmentLeftMoveOrderRightMoveOrder_RtL_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input
				.getFeatureMapNonContainmentLeftConflictLeftMoveOrderRightMoveOrderScope(rs);
		final Resource right = input
				.getFeatureMapNonContainmentRightConflictLeftMoveOrderRightMoveOrderScope(rs);
		final Resource origin = input
				.getFeatureMapNonContainmentOriginConflictLeftMoveOrderRightMoveOrderScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 is in the first position in the ancestor, in the second position in left, and in third
		// position in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey move] from right to left : the left
		// FeatureMapChange <firstKey> Node node1 [map move] will be merge from right to left too.
		// The rights diffs will remains unmerged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 at the first position in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(3, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(0));
		// mapNode1 contains node1 at the first position in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof List);
		assertEquals(3, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(2));
		// mapNode1 contains node1 at the first position of the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(3, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 0, true));
		// mapNode1 contains node1 at the first position of the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(3, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 2, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
	}

	@Test
	public void testConflictNonContainmentLeftMoveOrderRightMoveOrder_RtL_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input
				.getFeatureMapNonContainmentLeftConflictLeftMoveOrderRightMoveOrderScope(rs);
		final Resource right = input
				.getFeatureMapNonContainmentRightConflictLeftMoveOrderRightMoveOrderScope(rs);
		final Resource origin = input
				.getFeatureMapNonContainmentOriginConflictLeftMoveOrderRightMoveOrderScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 is in the first position in the ancestor, in the second position in left, and in third
		// position in right.
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map move] from right to left : the left
		// ReferenceChange Node node1 [firstKey move] will be merge from right to left too.
		// The rights diffs will remains unmerged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 at the first position in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(3, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(0));
		// mapNode1 contains node1 at the first position in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof List);
		assertEquals(3, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(2));
		// mapNode1 contains node1 at the first position of the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(3, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 0, true));
		// mapNode1 contains node1 at the first position of the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(3, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 2, true));

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
