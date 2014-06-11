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
import static org.junit.Assert.assertSame;
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
public class FeatureMapsPseudoConflictsMergeTest {

	private IndividualDiffInputData input = new IndividualDiffInputData();

	private final IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	@Test
	public void testConflictContainmentPseudoConflictAdd_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictAddScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictAddScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 doesn't exists in the ancestor, exists in left and right under mapNode1.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

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
		assertTrue(rightMapNode1FirstKey instanceof Collection);
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
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictContainmentPseudoConflictAdd_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictAddScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictAddScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 doesn't exists in the ancestor, exists in left and right under mapNode1.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

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
		assertTrue(rightMapNode1FirstKey instanceof Collection);
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
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictContainmentPseudoConflictAdd_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictAddScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictAddScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 doesn't exists in the ancestor, exists in left and right under mapNode1.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey add] from right to left : the left
		// FeatureMapChange <firstKey> Node node1 [map add] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
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
		assertTrue(rightMapNode1FirstKey instanceof Collection);
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
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictContainmentPseudoConflictAdd_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictAddScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictAddScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 doesn't exists in the ancestor, exists in left and right under mapNode1.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map add] from right to left : the left
		// ReferenceChange Node node1 [firstKey add] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
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
		assertTrue(rightMapNode1FirstKey instanceof Collection);
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
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictContainmentPseudoConflictDelete_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode1, but not in left and right .
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey delete] from left to right : the left
		// FeatureMapChange <firstKey> Node node1 [map delete] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 doesn't still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNull(leftNode1);
		// node1 doesn't still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNull(rightNode1);
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
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)rightMapNode1FirstKey).isEmpty());
		// mapNode1 doesn't doesn't contains node1 through the map in left
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
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictContainmentPseudoConflictDelete_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode1, but not in left and right .
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map delete] from left to right : the left
		// ReferenceChange Node node1 [firstKey delete] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 doesn't still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNull(leftNode1);
		// node1 doesn't still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNull(rightNode1);
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
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)rightMapNode1FirstKey).isEmpty());
		// mapNode1 doesn't doesn't contains node1 through the map in left
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
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictContainmentPseudoConflictDelete_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode1, but not in left and right .
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey delete] from right to left : the left
		// FeatureMapChange <firstKey> Node node1 [map delete] will be merge from right to left too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 doesn't still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNull(leftNode1);
		// node1 doesn't still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNull(rightNode1);
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
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)rightMapNode1FirstKey).isEmpty());
		// mapNode1 doesn't doesn't contains node1 through the map in left
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
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictContainmentPseudoConflictDelete_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictDeleteScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictDeleteScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode1, but not in left and right .
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map delete] from right to left : the left
		// ReferenceChange Node node1 [firstKey delete] will be merge from right to left too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 doesn't still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNull(leftNode1);
		// node1 doesn't still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNull(rightNode1);
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
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)rightMapNode1FirstKey).isEmpty());
		// mapNode1 doesn't doesn't contains node1 through the map in left
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
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictContainmentPseudoConflictKeyChange_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictKeyChangeScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode1 with key secondKey, exists in left and right under
		// mapNode1 with key fisrtKey.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey move] from left to right : the left
		// FeatureMapChange <firstKey> Node node1 [map entry key change] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
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
		assertTrue(rightMapNode1FirstKey instanceof Collection);
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
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictContainmentPseudoConflictKeyChange_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictKeyChangeScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode1 with key secondKey, exists in left and right under
		// mapNode1 with key fisrtKey.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.CHANGE), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map entry key change] from left to right :
		// the left ReferenceChange Node node1 [firstKey move] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
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
		assertTrue(rightMapNode1FirstKey instanceof Collection);
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
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictContainmentPseudoConflictKeyChange_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictKeyChangeScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode1 with key secondKey, exists in left and right under
		// mapNode1 with key fisrtKey.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey move] from right to left : the left
		// FeatureMapChange <firstKey> Node node1 [map entry key change] will be merge from right to left too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
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
		assertTrue(rightMapNode1FirstKey instanceof Collection);
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
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictContainmentPseudoConflictKeyChange_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictKeyChangeScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictKeyChangeScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictKeyChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode1 with key secondKey, exists in left and right under
		// mapNode1 with key fisrtKey.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.CHANGE), instanceOf(FeatureMapChange.class)));

		// Merge the FeatureMapChange <firstKey> Node node1 [map entry key change] left from right to left :
		// the left ReferenceChange Node node1 [firstKey move] will be merge from right to left too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
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
		assertTrue(rightMapNode1FirstKey instanceof Collection);
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
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictContainmentPseudoConflictMoveOrder_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictMoveOrderScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictMoveOrderScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictMoveOrderScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode1 in first position, exists in left and right under
		// mapNode1 in last position.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey move] from left to right : the left
		// FeatureMapChange <firstKey> Node node1 [map move] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(2, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(1));
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertEquals(2, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(1));
		// mapNode1 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 1, true));
		// mapNode1 contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictContainmentPseudoConflictMoveOrder_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictMoveOrderScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictMoveOrderScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictMoveOrderScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode1 in first position, exists in left and right under
		// mapNode1 in last position.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map move] from left to right : the left
		// ReferenceChange Node node1 [firstKey move] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(2, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(1));
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertEquals(2, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(1));
		// mapNode1 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 1, true));
		// mapNode1 contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictContainmentPseudoConflictMoveOrder_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictMoveOrderScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictMoveOrderScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictMoveOrderScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode1 in first position, exists in left and right under
		// mapNode1 in last position.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey move] from right to left : the left
		// FeatureMapChange <firstKey> Node node1 [map move] will be merge from right to left too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(2, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(1));
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertEquals(2, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(1));
		// mapNode1 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 1, true));
		// mapNode1 contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictContainmentPseudoConflictMoveOrder_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictMoveOrderScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictMoveOrderScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictMoveOrderScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode1 in first position, exists in left and right under
		// mapNode1 in last position.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map move] from right to left : the left
		// ReferenceChange Node node1 [firstKey move] will be merge from right to left too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 contains node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNode1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKey");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(2, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(1));
		// mapNode1 contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNode1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertEquals(2, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(1));
		// mapNode1 contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("map");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 1, true));
		// mapNode1 contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictContainmentPseudoConflictMove_LtR_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictMoveScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictMoveScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictMoveScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode2, exists in left and right under mapNode1.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey move] from left to right : the left
		// FeatureMapChange <firstKey> Node node1 [map move] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
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
		assertTrue(rightMapNode1FirstKey instanceof Collection);
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
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictContainmentPseudoConflictMove_LtR_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictMoveScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictMoveScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictMoveScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode2, exists in left and right under mapNode1.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map move] from left to right : the left
		// ReferenceChange Node node1 [firstKey move] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
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
		assertTrue(rightMapNode1FirstKey instanceof Collection);
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
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictContainmentPseudoConflictMove_RtL_1() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictMoveScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictMoveScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictMoveScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode2, exists in left and right under mapNode1.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey move] from right to left : the left
		// FeatureMapChange <firstKey> Node node1 [map move] will be merge from right to left too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
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
		assertTrue(rightMapNode1FirstKey instanceof Collection);
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
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictContainmentPseudoConflictMove_RtL_2() throws IOException {
		final Resource left = input.getFeatureMapContainmentLeftPseudoConflictMoveScope();
		final Resource right = input.getFeatureMapContainmentRightPseudoConflictMoveScope();
		final Resource origin = input.getFeatureMapContainmentOriginPseudoConflictMoveScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode2, exists in left and right under mapNode1.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map move] from right to left : the left
		// ReferenceChange Node node1 [firstKey move] will be merge from right to left too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
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
		assertTrue(rightMapNode1FirstKey instanceof Collection);
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
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictNonContainmentPseudoConflictAdd_LtR_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftPseudoConflictAddScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightPseudoConflictAddScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginPseudoConflictAddScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 doesn't referenced by mapNC1 in the ancestor, referenced in left and right.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

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
		// mapNode1 references node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(1, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(0));
		// mapNode1 references node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertEquals(1, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(0));
		// mapNode1 references node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 0, true));
		// mapNode1 references node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictNonContainmentPseudoConflictAdd_LtR_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftPseudoConflictAddScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightPseudoConflictAddScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginPseudoConflictAddScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 doesn't referenced by mapNC1 in the ancestor, referenced in left and right.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

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
		// mapNode1 references node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(1, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(0));
		// mapNode1 references node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertEquals(1, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(0));
		// mapNode1 references node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 0, true));
		// mapNode1 references node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictNonContainmentPseudoConflictAdd_RtL_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftPseudoConflictAddScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightPseudoConflictAddScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginPseudoConflictAddScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 doesn't referenced by mapNC1 in the ancestor, referenced in left and right.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey add] from right to left : the left
		// FeatureMapChange <firstKey> Node node1 [map add] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 references node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(1, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(0));
		// mapNode1 references node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertEquals(1, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(0));
		// mapNode1 references node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 0, true));
		// mapNode1 references node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictNonContainmentPseudoConflictAdd_RtL_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftPseudoConflictAddScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightPseudoConflictAddScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginPseudoConflictAddScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 doesn't referenced by mapNC1 in the ancestor, referenced in left and right.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map add] from right to left : the left
		// ReferenceChange Node node1 [firstKey add] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 references node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(1, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(0));
		// mapNode1 references node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertEquals(1, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(0));
		// mapNode1 references node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 0, true));
		// mapNode1 references node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(1, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 0, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictNonContainmentPseudoConflictDelete_LtR_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftPseudoConflictDeleteScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightPseudoConflictDeleteScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginPseudoConflictDeleteScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 is referenced by mapNC1 in the ancestor, not referenced in left and right.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey delete] from left to right : the left
		// FeatureMapChange <firstKey> Node node1 [map delete] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 doesn't contains node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)leftMapNode1FirstKey).isEmpty());
		// mapNode1 doesn't contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)rightMapNode1FirstKey).isEmpty());
		// mapNode1 doesn't doesn't contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)leftMapNode1Map).isEmpty());
		// mapNode1 doesn't contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)rightMapNode1Map).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictNonContainmentPseudoConflictDelete_LtR_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftPseudoConflictDeleteScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightPseudoConflictDeleteScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginPseudoConflictDeleteScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 is referenced by mapNC1 in the ancestor, not referenced in left and right.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map delete] from left to right : the left
		// ReferenceChange Node node1 [firstKey delete] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 doesn't contains node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)leftMapNode1FirstKey).isEmpty());
		// mapNode1 doesn't contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)rightMapNode1FirstKey).isEmpty());
		// mapNode1 doesn't doesn't contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)leftMapNode1Map).isEmpty());
		// mapNode1 doesn't contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)rightMapNode1Map).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictNonContainmentPseudoConflictDelete_RtL_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftPseudoConflictDeleteScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightPseudoConflictDeleteScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginPseudoConflictDeleteScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 is referenced by mapNC1 in the ancestor, not referenced in left and right.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey delete] from right to left : the left
		// FeatureMapChange <firstKey> Node node1 [map delete] will be merge from right to left too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 doesn't contains node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)leftMapNode1FirstKey).isEmpty());
		// mapNode1 doesn't contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)rightMapNode1FirstKey).isEmpty());
		// mapNode1 doesn't doesn't contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)leftMapNode1Map).isEmpty());
		// mapNode1 doesn't contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)rightMapNode1Map).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictNonContainmentPseudoConflictDelete_RtL_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftPseudoConflictDeleteScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightPseudoConflictDeleteScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginPseudoConflictDeleteScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 is referenced by mapNC1 in the ancestor, not referenced in left and right.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map delete] from right to left : the left
		// ReferenceChange Node node1 [firstKey delete] will be merge from right to left too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 doesn't contains node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)leftMapNode1FirstKey).isEmpty());
		// mapNode1 doesn't contains node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertTrue(((Collection)rightMapNode1FirstKey).isEmpty());
		// mapNode1 doesn't doesn't contains node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)leftMapNode1Map).isEmpty());
		// mapNode1 doesn't contains node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertTrue(((BasicFeatureMap)rightMapNode1Map).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictNonContainmentPseudoConflictMoveOrder_LtR_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftPseudoConflictMoveOrderScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightPseudoConflictMoveOrderScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginPseudoConflictMoveOrderScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode1 in first position, exists in left and right under
		// mapNode1 in last position.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

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
		// mapNode1 references node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(2, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(1));
		// mapNode1 references node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertEquals(2, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(1));
		// mapNode1 references node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 1, true));
		// mapNode1 references node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictNonContainmentPseudoConflictMoveOrder_LtR_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftPseudoConflictMoveOrderScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightPseudoConflictMoveOrderScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginPseudoConflictMoveOrderScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode1 in first position, exists in left and right under
		// mapNode1 in last position.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

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
		// mapNode1 references node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(2, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(1));
		// mapNode1 references node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertEquals(2, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(1));
		// mapNode1 references node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 1, true));
		// mapNode1 references node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictNonContainmentPseudoConflictMoveOrder_RtL_1() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftPseudoConflictMoveOrderScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightPseudoConflictMoveOrderScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginPseudoConflictMoveOrderScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode1 in first position, exists in left and right under
		// mapNode1 in last position.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(ReferenceChange.class)));

		// Merge the left ReferenceChange Node node1 [firstKey add] from right to left : the left
		// FeatureMapChange <firstKey> Node node1 [map add] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 references node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(2, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(1));
		// mapNode1 references node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertEquals(2, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(1));
		// mapNode1 references node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 1, true));
		// mapNode1 references node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testConflictNonContainmentPseudoConflictMoveOrder_RtL_2() throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource left = input.getFeatureMapNonContainmentLeftPseudoConflictMoveOrderScope(rs);
		final Resource right = input.getFeatureMapNonContainmentRightPseudoConflictMoveOrderScope(rs);
		final Resource origin = input.getFeatureMapNonContainmentOriginPseudoConflictMoveOrderScope(rs);

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// the node1 exists in the ancestor under mapNode1 in first position, exists in left and right under
		// mapNode1 in last position.
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.MOVE), instanceOf(FeatureMapChange.class)));

		// Merge the left FeatureMapChange <firstKey> Node node1 [map add] from right to left : the left
		// ReferenceChange Node node1 [firstKey add] will be merge from left to right too.
		// The rights diffs will be mark as merged.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		// node1 still exists in left
		final EObject leftNode1 = getNodeNamed(left, "node1");
		assertNotNull(leftNode1);
		// node1 still exists in right
		final EObject rightNode1 = getNodeNamed(right, "node1");
		assertNotNull(rightNode1);
		// mapNode1 references node1 through the feature firstKey in left
		final EObject leftMapNode1 = getNodeNamed(left, "mapNC1");
		assertNotNull(leftMapNode1);
		final EStructuralFeature eSFFirstKey = leftMapNode1.eClass().getEStructuralFeature("firstKeyNC");
		assertNotNull(eSFFirstKey);
		final Object leftMapNode1FirstKey = leftMapNode1.eGet(eSFFirstKey);
		assertTrue(leftMapNode1FirstKey instanceof List);
		assertEquals(2, ((List)leftMapNode1FirstKey).size());
		assertEquals(leftNode1, ((List)leftMapNode1FirstKey).get(1));
		// mapNode1 references node1 through the feature firstKey in right
		final EObject rightMapNode1 = getNodeNamed(right, "mapNC1");
		assertNotNull(rightMapNode1);
		final Object rightMapNode1FirstKey = rightMapNode1.eGet(eSFFirstKey);
		assertTrue(rightMapNode1FirstKey instanceof Collection);
		assertEquals(2, ((List)rightMapNode1FirstKey).size());
		assertEquals(rightNode1, ((List)rightMapNode1FirstKey).get(1));
		// mapNode1 references node1 through the map in left
		final EStructuralFeature eSFmap = leftMapNode1.eClass().getEStructuralFeature("mapNC");
		assertNotNull(eSFmap);
		final Object leftMapNode1Map = leftMapNode1.eGet(eSFmap);
		assertTrue(leftMapNode1Map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)leftMapNode1Map).size());
		assertEquals(leftNode1, ((BasicFeatureMap)leftMapNode1Map).get(eSFFirstKey, 1, true));
		// mapNode1 references node1 through the map in right
		final Object rightMapNode1Map = rightMapNode1.eGet(eSFmap);
		assertTrue(rightMapNode1Map instanceof BasicFeatureMap);
		assertEquals(2, ((BasicFeatureMap)rightMapNode1Map).size());
		assertEquals(rightNode1, ((BasicFeatureMap)rightMapNode1Map).get(eSFFirstKey, 1, true));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(4), Integer.valueOf(comparison.getDifferences().size()));
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
