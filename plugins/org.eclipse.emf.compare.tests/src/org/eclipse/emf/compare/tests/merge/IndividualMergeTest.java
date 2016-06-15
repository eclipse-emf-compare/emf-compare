/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - added test for bug 455255
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import static com.google.common.base.Predicates.and;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.moved;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.movedInAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.movedInReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.merge.data.IndividualDiffInputData;
import org.eclipse.emf.compare.tests.nodes.NodeEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

@SuppressWarnings("nls")
public class IndividualMergeTest {
	private IndividualDiffInputData input = new IndividualDiffInputData();

	private final IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	@Test
	public void testAttributeMonoChange2WayLtR() throws IOException {
		final Resource left = input.getAttributeMonoChangeLeft();
		final Resource right = input.getAttributeMonoChangeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, "originValue", "leftValue")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("leftValue", originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMonoChange2WayRtL() throws IOException {
		final Resource left = input.getAttributeMonoChangeLeft();
		final Resource right = input.getAttributeMonoChangeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, "originValue", "leftValue")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("originValue", originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMonoChange3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getAttributeMonoChangeLeft();
		final Resource right = input.getAttributeMonoChangeRight();
		final Resource origin = input.getAttributeMonoChangeOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, "originValue", "leftValue")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("leftValue", originNode.eGet(feature));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeEEnumMonoChange2WayLtR() throws IOException {
		final Resource left = input.getAttributeMonoEEnumChangeLeft();
		final Resource right = input.getAttributeMonoEEnumChangeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singlevalueEEnumAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root", featureName, NodeEnum.A, NodeEnum.B)));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "root");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals(NodeEnum.B, originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeEEnumMonoChange2WayRtL() throws IOException {
		final Resource left = input.getAttributeMonoEEnumChangeLeft();
		final Resource right = input.getAttributeMonoEEnumChangeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singlevalueEEnumAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root", featureName, NodeEnum.A, NodeEnum.B)));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "root");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals(NodeEnum.A, originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeEEnumMonoChange3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getAttributeMonoEEnumChangeLeft();
		final Resource right = input.getAttributeMonoEEnumChangeRight();
		final Resource origin = input.getAttributeMonoEEnumChangeOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singlevalueEEnumAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root", featureName, NodeEnum.A, NodeEnum.B)));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "root");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals(NodeEnum.B, originNode.eGet(feature));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeEEnumMonoChange3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getAttributeMonoEEnumChangeLeft();
		final Resource right = input.getAttributeMonoEEnumChangeRight();
		final Resource origin = input.getAttributeMonoEEnumChangeOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singlevalueEEnumAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root", featureName, NodeEnum.A, NodeEnum.B)));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "root");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals(NodeEnum.A, originNode.eGet(feature));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeEEnumMonoChange3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMonoEEnumChangeRight();
		final Resource right = input.getAttributeMonoEEnumChangeLeft();
		final Resource origin = input.getAttributeMonoEEnumChangeOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singlevalueEEnumAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedAttribute("root", featureName, NodeEnum.A, NodeEnum.B)));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "root");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals(NodeEnum.A, originNode.eGet(feature));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeEEnumMonoChange3WayRightChangeRtL() throws IOException {
		final Resource left = input.getAttributeMonoEEnumChangeRight();
		final Resource right = input.getAttributeMonoEEnumChangeLeft();
		final Resource origin = input.getAttributeMonoEEnumChangeOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singlevalueEEnumAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedAttribute("root", featureName, NodeEnum.A, NodeEnum.B)));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "root");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals(NodeEnum.B, originNode.eGet(feature));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMonoChange3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getAttributeMonoChangeLeft();
		final Resource right = input.getAttributeMonoChangeRight();
		final Resource origin = input.getAttributeMonoChangeOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, "originValue", "leftValue")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("originValue", originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMonoChange3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMonoChangeRight();
		final Resource right = input.getAttributeMonoChangeLeft();
		final Resource origin = input.getAttributeMonoChangeOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedAttribute("root.origin", featureName, "originValue", "leftValue")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("originValue", originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMonoChange3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMonoChangeRight();
		final Resource right = input.getAttributeMonoChangeLeft();
		final Resource origin = input.getAttributeMonoChangeOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedAttribute("root.origin", featureName, "originValue", "leftValue")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("leftValue", originNode.eGet(feature));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMonoSet2WayLtR() throws IOException {
		final Resource left = input.getAttributeMonoSetLeft();
		final Resource right = input.getAttributeMonoSetRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, null, "leftValue")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("leftValue", originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMonoSet2WayRtL() throws IOException {
		final Resource left = input.getAttributeMonoSetLeft();
		final Resource right = input.getAttributeMonoSetRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, null, "leftValue")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertNull(originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMonoSet3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getAttributeMonoSetLeft();
		final Resource right = input.getAttributeMonoSetRight();
		final Resource origin = input.getAttributeMonoSetOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, null, "leftValue")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("leftValue", originNode.eGet(feature));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMonoSet3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getAttributeMonoSetLeft();
		final Resource right = input.getAttributeMonoSetRight();
		final Resource origin = input.getAttributeMonoSetOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, null, "leftValue")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertNull(originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMonoSet3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMonoSetRight();
		final Resource right = input.getAttributeMonoSetLeft();
		final Resource origin = input.getAttributeMonoSetOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedAttribute("root.origin", featureName, null, "leftValue")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertNull(originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMonoSet3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMonoSetRight();
		final Resource right = input.getAttributeMonoSetLeft();
		final Resource origin = input.getAttributeMonoSetOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedAttribute("root.origin", featureName, null, "leftValue")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("leftValue", originNode.eGet(feature));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMonoUnset2WayLtR() throws IOException {
		final Resource left = input.getAttributeMonoUnsetLeft();
		final Resource right = input.getAttributeMonoUnsetRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, "originValue", null)));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertNull(originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMonoUnset2WayRtL() throws IOException {
		final Resource left = input.getAttributeMonoUnsetLeft();
		final Resource right = input.getAttributeMonoUnsetRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, "originValue", null)));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("originValue", originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMonoUnset3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getAttributeMonoUnsetLeft();
		final Resource right = input.getAttributeMonoUnsetRight();
		final Resource origin = input.getAttributeMonoUnsetOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, "originValue", null)));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertNull(originNode.eGet(feature));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMonoUnset3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getAttributeMonoUnsetLeft();
		final Resource right = input.getAttributeMonoUnsetRight();
		final Resource origin = input.getAttributeMonoUnsetOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, "originValue", null)));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("originValue", originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMonoUnset3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMonoUnsetRight();
		final Resource right = input.getAttributeMonoUnsetLeft();
		final Resource origin = input.getAttributeMonoUnsetOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedAttribute("root.origin", featureName, "originValue", null)));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("originValue", originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMonoUnset3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMonoUnsetRight();
		final Resource right = input.getAttributeMonoUnsetLeft();
		final Resource origin = input.getAttributeMonoUnsetOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedAttribute("root.origin", featureName, "originValue", null)));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertNull(originNode.eGet(feature));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeEEnumMultiAdd2WayLtR() throws IOException {
		final Resource left = input.getAttributeEEnumMultiAddLeft();
		final Resource right = input.getAttributeEEnumMultiAddRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		final String featureName = "multiValueEEnumAttribute";
		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), addedToAttribute("root", featureName, NodeEnum.A)));
		final Diff diff2 = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), addedToAttribute("root", featureName, NodeEnum.B)));
		final Diff diff3 = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), addedToAttribute("root", featureName, NodeEnum.C)));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diff2).copyLeftToRight(diff2, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diff3).copyLeftToRight(diff3, new BasicMonitor());

		final EObject originNode = getNodeNamed(right, "root");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof List<?>);
		assertEquals(featureValue, Lists.newArrayList(NodeEnum.A, NodeEnum.B, NodeEnum.C));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeEEnumMultiAdd2WayRtL() throws IOException {
		final Resource left = input.getAttributeEEnumMultiAddLeft();
		final Resource right = input.getAttributeEEnumMultiAddRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		final String featureName = "multiValueEEnumAttribute";
		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), addedToAttribute("root", featureName, NodeEnum.A)));
		final Diff diff2 = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), addedToAttribute("root", featureName, NodeEnum.B)));
		final Diff diff3 = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), addedToAttribute("root", featureName, NodeEnum.C)));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diff2).copyRightToLeft(diff2, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diff3).copyRightToLeft(diff3, new BasicMonitor());

		final EObject originNode = getNodeNamed(right, "root");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).isEmpty());

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeEEnumMultiAdd3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getAttributeEEnumMultiAddLeft();
		final Resource right = input.getAttributeEEnumMultiAddRight();
		final Resource origin = input.getAttributeEEnumMultiAddOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		final String featureName = "multiValueEEnumAttribute";
		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), addedToAttribute("root", featureName, NodeEnum.A)));
		final Diff diff2 = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), addedToAttribute("root", featureName, NodeEnum.B)));
		final Diff diff3 = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), addedToAttribute("root", featureName, NodeEnum.C)));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diff2).copyLeftToRight(diff2, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diff3).copyLeftToRight(diff3, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "root");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue)
				.containsAll(Lists.newArrayList(NodeEnum.A, NodeEnum.B, NodeEnum.C)));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeEEnumMultiAdd3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getAttributeEEnumMultiAddLeft();
		final Resource right = input.getAttributeEEnumMultiAddRight();
		final Resource origin = input.getAttributeEEnumMultiAddOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		final String featureName = "multiValueEEnumAttribute";
		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), addedToAttribute("root", featureName, NodeEnum.A)));
		final Diff diff2 = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), addedToAttribute("root", featureName, NodeEnum.B)));
		final Diff diff3 = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), addedToAttribute("root", featureName, NodeEnum.C)));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diff2).copyRightToLeft(diff2, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diff3).copyRightToLeft(diff3, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "root");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).isEmpty());

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeEEnumMultiAdd3WayRightChangeLtR() throws IOException {
		final Resource left = input.getAttributeEEnumMultiAddRight();
		final Resource right = input.getAttributeEEnumMultiAddLeft();
		final Resource origin = input.getAttributeEEnumMultiAddOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		final String featureName = "multiValueEEnumAttribute";
		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.RIGHT), addedToAttribute("root", featureName, NodeEnum.A)));
		final Diff diff2 = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.RIGHT), addedToAttribute("root", featureName, NodeEnum.B)));
		final Diff diff3 = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.RIGHT), addedToAttribute("root", featureName, NodeEnum.C)));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diff2).copyLeftToRight(diff2, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diff3).copyLeftToRight(diff3, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "root");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).isEmpty());

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeEEnumMultiAdd3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMultiAddRight();
		final Resource right = input.getAttributeMultiAddLeft();
		final Resource origin = input.getAttributeMultiAddOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				addedToAttribute("root.origin", featureName, "value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).contains("value1"));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMultiAdd2WayLtR() throws IOException {
		final Resource left = input.getAttributeMultiAddLeft();
		final Resource right = input.getAttributeMultiAddRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), addedToAttribute("root.origin", featureName, "value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMultiAdd2WayRtL() throws IOException {
		final Resource left = input.getAttributeMultiAddLeft();
		final Resource right = input.getAttributeMultiAddRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), addedToAttribute("root.origin", featureName, "value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertFalse(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMultiAdd3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getAttributeMultiAddLeft();
		final Resource right = input.getAttributeMultiAddRight();
		final Resource origin = input.getAttributeMultiAddOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), addedToAttribute("root.origin", featureName, "value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).contains("value1"));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMultiAdd3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getAttributeMultiAddLeft();
		final Resource right = input.getAttributeMultiAddRight();
		final Resource origin = input.getAttributeMultiAddOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), addedToAttribute("root.origin", featureName, "value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertFalse(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMultiAdd3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMultiAddRight();
		final Resource right = input.getAttributeMultiAddLeft();
		final Resource origin = input.getAttributeMultiAddOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				addedToAttribute("root.origin", featureName, "value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertFalse(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMultiAdd3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMultiAddRight();
		final Resource right = input.getAttributeMultiAddLeft();
		final Resource origin = input.getAttributeMultiAddOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				addedToAttribute("root.origin", featureName, "value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).contains("value1"));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMultiDel2WayLtR() throws IOException {
		final Resource left = input.getAttributeMultiDelLeft();
		final Resource right = input.getAttributeMultiDelRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				removedFromAttribute("root.origin", featureName, "value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertFalse(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMultiDel2WayRtL() throws IOException {
		final Resource left = input.getAttributeMultiDelLeft();
		final Resource right = input.getAttributeMultiDelRight();

		IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				removedFromAttribute("root.origin", featureName, "value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMultiDel3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getAttributeMultiDelLeft();
		final Resource right = input.getAttributeMultiDelRight();
		final Resource origin = input.getAttributeMultiDelOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				removedFromAttribute("root.origin", featureName, "value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertFalse(((Collection<?>)featureValue).contains("value1"));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMultiDel3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getAttributeMultiDelLeft();
		final Resource right = input.getAttributeMultiDelRight();
		final Resource origin = input.getAttributeMultiDelOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				removedFromAttribute("root.origin", featureName, "value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMultiDel3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMultiDelRight();
		final Resource right = input.getAttributeMultiDelLeft();
		final Resource origin = input.getAttributeMultiDelOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				removedFromAttribute("root.origin", featureName, "value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testAttributeMultiDel3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMultiDelRight();
		final Resource right = input.getAttributeMultiDelLeft();
		final Resource origin = input.getAttributeMultiDelOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				removedFromAttribute("root.origin", featureName, "value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertFalse(((Collection<?>)featureValue).contains("value1"));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAttributeMultiMove2WayLtR() throws IOException {
		final Resource left = input.getAttributeMultiMoveLeft();
		final Resource right = input.getAttributeMultiMoveRight();

		IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), movedInAttribute("root.origin", featureName, "value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject targetNode = getNodeNamed(right, "origin");
		assertNotNull(targetNode);
		final EObject sourceNode = getNodeNamed(left, "origin");
		assertNotNull(sourceNode);
		final EStructuralFeature feature = targetNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object targetFeatureValue = targetNode.eGet(feature);
		final Object sourceFeatureValue = sourceNode.eGet(feature);
		assertTrue(targetFeatureValue instanceof List<?>);
		assertTrue(sourceFeatureValue instanceof List<?>);
		assertEqualContents(((List<Object>)targetFeatureValue), ((List<Object>)sourceFeatureValue));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAttributeMultiMove2WayRtL() throws IOException {
		final Resource left = input.getAttributeMultiMoveLeft();
		final Resource right = input.getAttributeMultiMoveRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), movedInAttribute("root.origin", featureName, "value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject targetNode = getNodeNamed(left, "origin");
		assertNotNull(targetNode);
		final EObject sourceNode = getNodeNamed(right, "origin");
		assertNotNull(sourceNode);
		final EStructuralFeature feature = targetNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object targetFeatureValue = targetNode.eGet(feature);
		final Object sourceFeatureValue = sourceNode.eGet(feature);
		assertTrue(targetFeatureValue instanceof List<?>);
		assertTrue(sourceFeatureValue instanceof List<?>);
		assertEqualContents(((List<Object>)targetFeatureValue), ((List<Object>)sourceFeatureValue));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAttributeMultiMove3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getAttributeMultiMoveLeft();
		final Resource right = input.getAttributeMultiMoveRight();
		final Resource origin = input.getAttributeMultiMoveOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), movedInAttribute("root.origin", featureName, "value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject targetNode = getNodeNamed(right, "origin");
		assertNotNull(targetNode);
		final EObject sourceNode = getNodeNamed(left, "origin");
		assertNotNull(sourceNode);
		final EStructuralFeature feature = targetNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object targetFeatureValue = targetNode.eGet(feature);
		final Object sourceFeatureValue = sourceNode.eGet(feature);
		assertTrue(targetFeatureValue instanceof List<?>);
		assertTrue(sourceFeatureValue instanceof List<?>);
		assertEqualContents(((List<Object>)targetFeatureValue), ((List<Object>)sourceFeatureValue));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAttributeMultiMove3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getAttributeMultiMoveLeft();
		final Resource right = input.getAttributeMultiMoveRight();
		final Resource origin = input.getAttributeMultiMoveOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), movedInAttribute("root.origin", featureName, "value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject targetNode = getNodeNamed(left, "origin");
		assertNotNull(targetNode);
		final EObject sourceNode = getNodeNamed(origin, "origin");
		assertNotNull(sourceNode);
		final EStructuralFeature feature = targetNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object targetFeatureValue = targetNode.eGet(feature);
		final Object sourceFeatureValue = sourceNode.eGet(feature);
		assertTrue(targetFeatureValue instanceof List<?>);
		assertTrue(sourceFeatureValue instanceof List<?>);
		assertEqualContents(((List<Object>)targetFeatureValue), ((List<Object>)sourceFeatureValue));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAttributeMultiMove3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMultiMoveRight();
		final Resource right = input.getAttributeMultiMoveLeft();
		final Resource origin = input.getAttributeMultiMoveOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				movedInAttribute("root.origin", featureName, "value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject targetNode = getNodeNamed(right, "origin");
		assertNotNull(targetNode);
		final EObject sourceNode = getNodeNamed(origin, "origin");
		assertNotNull(sourceNode);
		final EStructuralFeature feature = targetNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object targetFeatureValue = targetNode.eGet(feature);
		final Object sourceFeatureValue = sourceNode.eGet(feature);
		assertTrue(targetFeatureValue instanceof List<?>);
		assertTrue(sourceFeatureValue instanceof List<?>);
		assertEqualContents(((List<Object>)targetFeatureValue), ((List<Object>)sourceFeatureValue));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAttributeMultiMove3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMultiMoveRight();
		final Resource right = input.getAttributeMultiMoveLeft();
		final Resource origin = input.getAttributeMultiMoveOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				movedInAttribute("root.origin", featureName, "value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject targetNode = getNodeNamed(left, "origin");
		assertNotNull(targetNode);
		final EObject sourceNode = getNodeNamed(right, "origin");
		assertNotNull(sourceNode);
		final EStructuralFeature feature = targetNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object targetFeatureValue = targetNode.eGet(feature);
		final Object sourceFeatureValue = sourceNode.eGet(feature);
		assertTrue(targetFeatureValue instanceof List<?>);
		assertTrue(sourceFeatureValue instanceof List<?>);
		assertEqualContents(((List<Object>)targetFeatureValue), ((List<Object>)sourceFeatureValue));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMonoChange2WayLtR() throws IOException {
		final Resource left = input.getReferenceMonoChangeLeft();
		final Resource right = input.getReferenceMonoChangeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedReference("root.origin", featureName, "root.originValue", "root.targetValue")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(right, "targetValue");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertSame(targetNode, originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMonoChange2WayRtL() throws IOException {
		final Resource left = input.getReferenceMonoChangeLeft();
		final Resource right = input.getReferenceMonoChangeRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedReference("root.origin", featureName, "root.originValue", "root.targetValue")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(left, "originValue");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertSame(targetNode, originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMonoChange3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getReferenceMonoChangeLeft();
		final Resource right = input.getReferenceMonoChangeRight();
		final Resource origin = input.getReferenceMonoChangeOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedReference("root.origin", featureName, "root.originValue", "root.targetValue")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(right, "targetValue");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertSame(targetNode, originNode.eGet(feature));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMonoChange3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getReferenceMonoChangeLeft();
		final Resource right = input.getReferenceMonoChangeRight();
		final Resource origin = input.getReferenceMonoChangeOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedReference("root.origin", featureName, "root.originValue", "root.targetValue")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(left, "originValue");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertSame(targetNode, originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMonoChange3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoChangeRight();
		final Resource right = input.getReferenceMonoChangeLeft();
		final Resource origin = input.getReferenceMonoChangeOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedReference("root.origin", featureName, "root.originValue", "root.targetValue")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(right, "originValue");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertSame(targetNode, originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMonoChange3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoChangeRight();
		final Resource right = input.getReferenceMonoChangeLeft();
		final Resource origin = input.getReferenceMonoChangeOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedReference("root.origin", featureName, "root.originValue", "root.targetValue")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(left, "targetValue");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertSame(targetNode, originNode.eGet(feature));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMonoSet2WayLtR() throws IOException {
		final Resource left = input.getReferenceMonoSetLeft();
		final Resource right = input.getReferenceMonoSetRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedReference("root.origin", featureName, null, "root.left")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(right, "left");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertSame(targetNode, originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMonoSet2WayRtL() throws IOException {
		final Resource left = input.getReferenceMonoSetLeft();
		final Resource right = input.getReferenceMonoSetRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedReference("root.origin", featureName, null, "root.left")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertNull(originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMonoSet3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getReferenceMonoSetLeft();
		final Resource right = input.getReferenceMonoSetRight();
		final Resource origin = input.getReferenceMonoSetOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedReference("root.origin", featureName, null, "root.left")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(right, "left");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertSame(targetNode, originNode.eGet(feature));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMonoSet3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getReferenceMonoSetLeft();
		final Resource right = input.getReferenceMonoSetRight();
		final Resource origin = input.getReferenceMonoSetOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedReference("root.origin", featureName, null, "root.left")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertNull(originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMonoSet3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoSetRight();
		final Resource right = input.getReferenceMonoSetLeft();
		final Resource origin = input.getReferenceMonoSetOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedReference("root.origin", featureName, null, "root.left")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertNull(originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMonoSet3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoSetRight();
		final Resource right = input.getReferenceMonoSetLeft();
		final Resource origin = input.getReferenceMonoSetOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedReference("root.origin", featureName, null, "root.left")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(left, "left");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertSame(targetNode, originNode.eGet(feature));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMonoUnset2WayLtR() throws IOException {
		final Resource left = input.getReferenceMonoUnsetLeft();
		final Resource right = input.getReferenceMonoUnsetRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedReference("root.origin", featureName, "root.target", null)));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertNull(originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMonoUnset2WayRtL() throws IOException {
		final Resource left = input.getReferenceMonoUnsetLeft();
		final Resource right = input.getReferenceMonoUnsetRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedReference("root.origin", featureName, "root.target", null)));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(left, "target");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertSame(targetNode, originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMonoUnset3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getReferenceMonoUnsetLeft();
		final Resource right = input.getReferenceMonoUnsetRight();
		final Resource origin = input.getReferenceMonoUnsetOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedReference("root.origin", featureName, "root.target", null)));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertNull(originNode.eGet(feature));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMonoUnset3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getReferenceMonoUnsetLeft();
		final Resource right = input.getReferenceMonoUnsetRight();
		final Resource origin = input.getReferenceMonoUnsetOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedReference("root.origin", featureName, "root.target", null)));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(left, "target");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertSame(targetNode, originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMonoUnset3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoUnsetRight();
		final Resource right = input.getReferenceMonoUnsetLeft();
		final Resource origin = input.getReferenceMonoUnsetOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedReference("root.origin", featureName, "root.target", null)));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(right, "target");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertSame(targetNode, originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMonoUnset3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoUnsetRight();
		final Resource right = input.getReferenceMonoUnsetLeft();
		final Resource origin = input.getReferenceMonoUnsetOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedReference("root.origin", featureName, "root.target", null)));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertNull(originNode.eGet(feature));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMultiAdd2WayLtR() throws IOException {
		final Resource left = input.getReferenceMultiAddLeft();
		final Resource right = input.getReferenceMultiAddRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				addedToReference("root.origin", featureName, "root.target")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(right, "target");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).contains(targetNode));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMultiAdd2WayRtL() throws IOException {
		final Resource left = input.getReferenceMultiAddLeft();
		final Resource right = input.getReferenceMultiAddRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				addedToReference("root.origin", featureName, "root.target")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(left, "target");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertFalse(((Collection<?>)featureValue).contains(targetNode));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMultiAdd3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getReferenceMultiAddLeft();
		final Resource right = input.getReferenceMultiAddRight();
		final Resource origin = input.getReferenceMultiAddOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				addedToReference("root.origin", featureName, "root.target")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(right, "target");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).contains(targetNode));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMultiAdd3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getReferenceMultiAddLeft();
		final Resource right = input.getReferenceMultiAddRight();
		final Resource origin = input.getReferenceMultiAddOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				addedToReference("root.origin", featureName, "root.target")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(left, "target");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertFalse(((Collection<?>)featureValue).contains(targetNode));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMultiAdd3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMultiAddRight();
		final Resource right = input.getReferenceMultiAddLeft();
		final Resource origin = input.getReferenceMultiAddOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				addedToReference("root.origin", featureName, "root.target")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(right, "target");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertFalse(((Collection<?>)featureValue).contains(targetNode));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMultiAdd3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMultiAddRight();
		final Resource right = input.getReferenceMultiAddLeft();
		final Resource origin = input.getReferenceMultiAddOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				addedToReference("root.origin", featureName, "root.target")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(left, "target");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).contains(targetNode));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMultiDel2WayLtR() throws IOException {
		final Resource left = input.getReferenceMultiDelLeft();
		final Resource right = input.getReferenceMultiDelRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				removedFromReference("root.origin", featureName, "root.target")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(right, "target");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertFalse(((Collection<?>)featureValue).contains(targetNode));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMultiDel2WayRtL() throws IOException {
		final Resource left = input.getReferenceMultiDelLeft();
		final Resource right = input.getReferenceMultiDelRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				removedFromReference("root.origin", featureName, "root.target")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(left, "target");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).contains(targetNode));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMultiDel3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getReferenceMultiDelLeft();
		final Resource right = input.getReferenceMultiDelRight();
		final Resource origin = input.getReferenceMultiDelOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				removedFromReference("root.origin", featureName, "root.target")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(right, "target");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertFalse(((Collection<?>)featureValue).contains(targetNode));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMultiDel3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getReferenceMultiDelLeft();
		final Resource right = input.getReferenceMultiDelRight();
		final Resource origin = input.getReferenceMultiDelOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				removedFromReference("root.origin", featureName, "root.target")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(left, "target");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).contains(targetNode));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMultiDel3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMultiDelRight();
		final Resource right = input.getReferenceMultiDelLeft();
		final Resource origin = input.getReferenceMultiDelOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				removedFromReference("root.origin", featureName, "root.target")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(right, "target");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).contains(targetNode));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceMultiDel3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMultiDelRight();
		final Resource right = input.getReferenceMultiDelLeft();
		final Resource origin = input.getReferenceMultiDelOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				removedFromReference("root.origin", featureName, "root.target")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(left, "target");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertFalse(((Collection<?>)featureValue).contains(targetNode));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReferenceMultiMove2WayLtR() throws IOException {
		final Resource left = input.getReferenceMultiMoveLeft();
		final Resource right = input.getReferenceMultiMoveRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				movedInReference("root.origin", featureName, "root.value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject targetNode = getNodeNamed(right, "origin");
		assertNotNull(targetNode);
		final EObject sourceNode = getNodeNamed(left, "origin");
		assertNotNull(sourceNode);
		final EStructuralFeature feature = targetNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object targetFeatureValue = targetNode.eGet(feature);
		final Object sourceFeatureValue = sourceNode.eGet(feature);
		assertTrue(targetFeatureValue instanceof List<?>);
		assertTrue(sourceFeatureValue instanceof List<?>);
		assertEqualContents(comparison, ((List<EObject>)targetFeatureValue),
				((List<EObject>)sourceFeatureValue));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReferenceMultiMove2WayRtL() throws IOException {
		final Resource left = input.getReferenceMultiMoveLeft();
		final Resource right = input.getReferenceMultiMoveRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				movedInReference("root.origin", featureName, "root.value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject targetNode = getNodeNamed(left, "origin");
		assertNotNull(targetNode);
		final EObject sourceNode = getNodeNamed(right, "origin");
		assertNotNull(sourceNode);
		final EStructuralFeature feature = targetNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object targetFeatureValue = targetNode.eGet(feature);
		final Object sourceFeatureValue = sourceNode.eGet(feature);
		assertTrue(targetFeatureValue instanceof List<?>);
		assertTrue(sourceFeatureValue instanceof List<?>);
		assertEqualContents(comparison, ((List<EObject>)targetFeatureValue),
				((List<EObject>)sourceFeatureValue));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReferenceMultiMove3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getReferenceMultiMoveLeft();
		final Resource right = input.getReferenceMultiMoveRight();
		final Resource origin = input.getReferenceMultiMoveOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				movedInReference("root.origin", featureName, "root.value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject targetNode = getNodeNamed(right, "origin");
		assertNotNull(targetNode);
		final EObject sourceNode = getNodeNamed(left, "origin");
		assertNotNull(sourceNode);
		final EStructuralFeature feature = targetNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object targetFeatureValue = targetNode.eGet(feature);
		final Object sourceFeatureValue = sourceNode.eGet(feature);
		assertTrue(targetFeatureValue instanceof List<?>);
		assertTrue(sourceFeatureValue instanceof List<?>);
		assertEqualContents(comparison, ((List<EObject>)targetFeatureValue),
				((List<EObject>)sourceFeatureValue));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReferenceMultiMove3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getReferenceMultiMoveLeft();
		final Resource right = input.getReferenceMultiMoveRight();
		final Resource origin = input.getReferenceMultiMoveOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				movedInReference("root.origin", featureName, "root.value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject targetNode = getNodeNamed(left, "origin");
		assertNotNull(targetNode);
		final EObject sourceNode = getNodeNamed(origin, "origin");
		assertNotNull(sourceNode);
		final EStructuralFeature feature = targetNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object targetFeatureValue = targetNode.eGet(feature);
		final Object sourceFeatureValue = sourceNode.eGet(feature);
		assertTrue(targetFeatureValue instanceof List<?>);
		assertTrue(sourceFeatureValue instanceof List<?>);
		assertEqualContents(comparison, ((List<EObject>)targetFeatureValue),
				((List<EObject>)sourceFeatureValue));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReferenceMultiMove3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMultiMoveRight();
		final Resource right = input.getReferenceMultiMoveLeft();
		final Resource origin = input.getReferenceMultiMoveOrigin();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				movedInReference("root.origin", featureName, "root.value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject targetNode = getNodeNamed(right, "origin");
		assertNotNull(targetNode);
		final EObject sourceNode = getNodeNamed(origin, "origin");
		assertNotNull(sourceNode);
		final EStructuralFeature feature = targetNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object targetFeatureValue = targetNode.eGet(feature);
		final Object sourceFeatureValue = sourceNode.eGet(feature);
		assertTrue(targetFeatureValue instanceof List<?>);
		assertTrue(sourceFeatureValue instanceof List<?>);
		assertEqualContents(comparison, ((List<EObject>)targetFeatureValue),
				((List<EObject>)sourceFeatureValue));

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReferenceMultiMove3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMultiMoveRight();
		final Resource right = input.getReferenceMultiMoveLeft();
		final Resource origin = input.getReferenceMultiMoveOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "multiValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				movedInReference("root.origin", featureName, "root.value1")));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject targetNode = getNodeNamed(left, "origin");
		assertNotNull(targetNode);
		final EObject sourceNode = getNodeNamed(right, "origin");
		assertNotNull(sourceNode);
		final EStructuralFeature feature = targetNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object targetFeatureValue = targetNode.eGet(feature);
		final Object sourceFeatureValue = sourceNode.eGet(feature);
		assertTrue(targetFeatureValue instanceof List<?>);
		assertTrue(sourceFeatureValue instanceof List<?>);
		assertEqualContents(comparison, ((List<EObject>)targetFeatureValue),
				((List<EObject>)sourceFeatureValue));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceContainmentMultiMove2WayLtR() throws IOException {
		final Resource left = input.getReferenceContainmentMultiMoveLeft();
		final Resource right = input.getReferenceContainmentMultiMoveRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "containmentRef1";
		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), moved("root.value2", featureName)));

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		final EObject targetNode = getNodeNamed(right, "value2");
		assertNotNull(targetNode);
		final EObject targetContainerNode = getNodeNamed(right, "root");
		assertNotNull(targetContainerNode);

		assertEquals(targetContainerNode, targetNode.eContainer());

		final EObject sourceNode = getNodeNamed(left, "value2");
		assertNotNull(sourceNode);
		final EObject sourceContainerNode = getNodeNamed(left, "root");
		assertNotNull(sourceContainerNode);

		assertEquals(sourceContainerNode, sourceNode.eContainer());

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceContainmentMultiMove2WayRtL() throws IOException {
		final Resource left = input.getReferenceContainmentMultiMoveLeft();
		final Resource right = input.getReferenceContainmentMultiMoveRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final String featureName = "containmentRef1";
		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), moved("root.value2", featureName)));

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		final EObject targetNode = getNodeNamed(right, "value2");
		assertNotNull(targetNode);
		final EObject targetContainerNode = getNodeNamed(right, "value3");
		assertNotNull(targetContainerNode);

		assertEquals(targetContainerNode, targetNode.eContainer());

		final EObject sourceNode = getNodeNamed(left, "value2");
		assertNotNull(sourceNode);
		final EObject sourceContainerNode = getNodeNamed(left, "value3");
		assertNotNull(sourceContainerNode);

		assertEquals(sourceContainerNode, sourceNode.eContainer());

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testReferenceDifferentContainmentMove3Way() throws IOException {
		final Resource left = input.getReferenceDifferentContainmentMoveLeft();
		final Resource right = input.getReferenceDifferentContainmentMoveRight();
		final Resource origin = input.getReferenceDifferentContainmentMoveOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		for (Diff diff : differences) {
			mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		}

		scope = new DefaultComparisonScope(right, origin, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	/**
	 * Ensures that the two given lists contain the same elements in the same order. The kind of list does not
	 * matter.
	 * 
	 * @param list1
	 *            First of the two lists to compare.
	 * @param list2
	 *            Second of the two lists to compare.
	 */
	private static <T> void assertEqualContents(List<T> list1, List<T> list2) {
		final int size = list1.size();
		assertEquals(size, list2.size());

		for (int i = 0; i < size; i++) {
			final Object object1 = list1.get(i);
			final Object object2 = list2.get(i);
			assertEquals(object1, object2);
		}
	}

	/**
	 * Ensures that the two given lists contain the same elements in the same order. The kind of list does not
	 * matter.
	 * 
	 * @param list1
	 *            First of the two lists to compare.
	 * @param list2
	 *            Second of the two lists to compare.
	 */
	private static <T extends EObject> void assertEqualContents(Comparison comparison, List<T> list1,
			List<T> list2) {
		final int size = list1.size();
		assertEquals(size, list2.size());

		boolean list2IsOrigin = false;
		for (int i = 0; i < size; i++) {
			final EObject eObject1 = list1.get(i);
			final EObject eObject2 = list2.get(i);
			final Match match = comparison.getMatch(eObject1);
			if (match.getLeft() == eObject1) {
				if (i == 0) {
					if (match.getOrigin() == eObject2) {
						list2IsOrigin = true;
					} else {
						assertSame(match.getRight(), eObject2);
					}
				} else if (list2IsOrigin) {
					assertSame(match.getOrigin(), eObject2);
				} else {
					assertSame(match.getRight(), eObject2);
				}
			} else {
				assertSame(match.getRight(), eObject1);
				if (i == 0) {
					if (match.getOrigin() == eObject2) {
						list2IsOrigin = true;
					} else {
						assertSame(match.getLeft(), eObject2);
					}
				} else if (list2IsOrigin) {
					assertSame(match.getOrigin(), eObject2);
				} else {
					assertSame(match.getLeft(), eObject2);
				}
			}
		}
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
