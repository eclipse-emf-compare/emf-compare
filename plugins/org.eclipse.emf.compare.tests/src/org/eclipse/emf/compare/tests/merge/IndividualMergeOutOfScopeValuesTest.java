/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.merge.data.IndividualDiffInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

@SuppressWarnings({"nls", "unchecked" })
public class IndividualMergeOutOfScopeValuesTest {
	private IndividualDiffInputData input = new IndividualDiffInputData();

	private final IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	@Test
	public void testReferenceMonoChange2WayLtR() throws IOException {
		final Resource left = input.getReferenceMonoChangeLeftOutOfScope();
		final Resource right = input.getReferenceMonoChangeRightOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);

		assertTrue(leftValue.eIsProxy());
		assertTrue(rightValue.eIsProxy());

		assertEquals(((InternalEObject)leftValue).eProxyURI(), ((InternalEObject)rightValue).eProxyURI());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoChange2WayRtL() throws IOException {
		final Resource left = input.getReferenceMonoChangeLeftOutOfScope();
		final Resource right = input.getReferenceMonoChangeRightOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);

		assertTrue(leftValue.eIsProxy());
		assertTrue(rightValue.eIsProxy());

		assertEquals(((InternalEObject)leftValue).eProxyURI(), ((InternalEObject)rightValue).eProxyURI());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoChange3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getReferenceMonoChangeLeftOutOfScope();
		final Resource right = input.getReferenceMonoChangeRightOutOfScope();
		final Resource origin = input.getReferenceMonoChangeOriginOutOfScope();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);
		final EObject originValue = (EObject)originDiffContainer.eGet(feature, false);

		assertTrue(leftValue.eIsProxy());
		assertTrue(rightValue.eIsProxy());
		assertTrue(originValue.eIsProxy());

		// We need left and right to point to the same proxy uri after the merge
		assertEquals(((InternalEObject)leftValue).eProxyURI(), ((InternalEObject)rightValue).eProxyURI());
		// Though the origin did not change and still points to the "old" proxy
		assertFalse(((InternalEObject)leftValue).eProxyURI().equals(
				((InternalEObject)originValue).eProxyURI()));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoChange3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getReferenceMonoChangeLeftOutOfScope();
		final Resource right = input.getReferenceMonoChangeRightOutOfScope();
		final Resource origin = input.getReferenceMonoChangeOriginOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);
		final EObject originValue = (EObject)originDiffContainer.eGet(feature, false);

		assertTrue(leftValue.eIsProxy());
		assertTrue(rightValue.eIsProxy());
		assertTrue(originValue.eIsProxy());

		// We need left and right to point to the same proxy uri after the merge
		assertEquals(((InternalEObject)leftValue).eProxyURI(), ((InternalEObject)rightValue).eProxyURI());
		// And coincidentally, "origin" also points to that same proxy in this case
		assertEquals(((InternalEObject)leftValue).eProxyURI(), ((InternalEObject)originValue).eProxyURI());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoChange3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoChangeRightOutOfScope();
		final Resource right = input.getReferenceMonoChangeLeftOutOfScope();
		final Resource origin = input.getReferenceMonoChangeOriginOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);
		final EObject originValue = (EObject)originDiffContainer.eGet(feature, false);

		assertTrue(leftValue.eIsProxy());
		assertTrue(rightValue.eIsProxy());
		assertTrue(originValue.eIsProxy());

		// We need left and right to point to the same proxy uri after the merge
		assertEquals(((InternalEObject)leftValue).eProxyURI(), ((InternalEObject)rightValue).eProxyURI());
		// And coincidentally, "origin" also points to that same proxy in this case
		assertEquals(((InternalEObject)leftValue).eProxyURI(), ((InternalEObject)originValue).eProxyURI());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoChange3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoChangeRightOutOfScope();
		final Resource right = input.getReferenceMonoChangeLeftOutOfScope();
		final Resource origin = input.getReferenceMonoChangeOriginOutOfScope();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);
		final EObject originValue = (EObject)originDiffContainer.eGet(feature, false);

		assertTrue(leftValue.eIsProxy());
		assertTrue(rightValue.eIsProxy());
		assertTrue(originValue.eIsProxy());

		// We need left and right to point to the same proxy uri after the merge
		assertEquals(((InternalEObject)leftValue).eProxyURI(), ((InternalEObject)rightValue).eProxyURI());
		// Though the origin did not change and still points to the "old" proxy
		assertFalse(((InternalEObject)leftValue).eProxyURI().equals(
				((InternalEObject)originValue).eProxyURI()));

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoSet2WayLtR() throws IOException {
		final Resource left = input.getReferenceMonoSetLeftOutOfScope();
		final Resource right = input.getReferenceMonoSetRightOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);

		assertTrue(leftValue.eIsProxy());
		assertTrue(rightValue.eIsProxy());

		assertEquals(((InternalEObject)leftValue).eProxyURI(), ((InternalEObject)rightValue).eProxyURI());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoSet2WayRtL() throws IOException {
		final Resource left = input.getReferenceMonoSetLeftOutOfScope();
		final Resource right = input.getReferenceMonoSetRightOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);

		assertNull(leftValue);
		assertNull(rightValue);

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoSet3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getReferenceMonoSetLeftOutOfScope();
		final Resource right = input.getReferenceMonoSetRightOutOfScope();
		final Resource origin = input.getReferenceMonoSetOriginOutOfScope();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);
		final EObject originValue = (EObject)originDiffContainer.eGet(feature, false);

		assertTrue(leftValue.eIsProxy());
		assertTrue(rightValue.eIsProxy());
		assertNull(originValue);

		assertEquals(((InternalEObject)leftValue).eProxyURI(), ((InternalEObject)rightValue).eProxyURI());

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoSet3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getReferenceMonoSetLeftOutOfScope();
		final Resource right = input.getReferenceMonoSetRightOutOfScope();
		final Resource origin = input.getReferenceMonoSetOriginOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);
		final EObject originValue = (EObject)originDiffContainer.eGet(feature, false);

		assertNull(leftValue);
		assertNull(rightValue);
		assertNull(originValue);

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoSet3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoSetRightOutOfScope();
		final Resource right = input.getReferenceMonoSetLeftOutOfScope();
		final Resource origin = input.getReferenceMonoSetOriginOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);
		final EObject originValue = (EObject)originDiffContainer.eGet(feature, false);

		assertNull(leftValue);
		assertNull(rightValue);
		assertNull(originValue);

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoSet3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoSetRightOutOfScope();
		final Resource right = input.getReferenceMonoSetLeftOutOfScope();
		final Resource origin = input.getReferenceMonoSetOriginOutOfScope();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);
		final EObject originValue = (EObject)originDiffContainer.eGet(feature, false);

		assertTrue(leftValue.eIsProxy());
		assertTrue(rightValue.eIsProxy());
		assertNull(originValue);

		assertEquals(((InternalEObject)leftValue).eProxyURI(), ((InternalEObject)rightValue).eProxyURI());

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoUnset2WayLtR() throws IOException {
		final Resource left = input.getReferenceMonoUnsetLeftOutOfScope();
		final Resource right = input.getReferenceMonoUnsetRightOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);

		assertNull(leftValue);
		assertNull(rightValue);

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoUnset2WayRtL() throws IOException {
		final Resource left = input.getReferenceMonoUnsetLeftOutOfScope();
		final Resource right = input.getReferenceMonoUnsetRightOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);

		assertTrue(leftValue.eIsProxy());
		assertTrue(rightValue.eIsProxy());

		assertEquals(((InternalEObject)leftValue).eProxyURI(), ((InternalEObject)rightValue).eProxyURI());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoUnset3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getReferenceMonoUnsetLeftOutOfScope();
		final Resource right = input.getReferenceMonoUnsetRightOutOfScope();
		final Resource origin = input.getReferenceMonoUnsetOriginOutOfScope();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);
		final EObject originValue = (EObject)originDiffContainer.eGet(feature, false);

		assertNull(leftValue);
		assertNull(rightValue);
		assertTrue(originValue.eIsProxy());

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoUnset3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getReferenceMonoUnsetLeftOutOfScope();
		final Resource right = input.getReferenceMonoUnsetRightOutOfScope();
		final Resource origin = input.getReferenceMonoUnsetOriginOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);
		final EObject originValue = (EObject)originDiffContainer.eGet(feature, false);

		assertTrue(leftValue.eIsProxy());
		assertTrue(rightValue.eIsProxy());
		assertTrue(originValue.eIsProxy());

		assertEquals(((InternalEObject)leftValue).eProxyURI(), ((InternalEObject)rightValue).eProxyURI());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoUnset3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoUnsetRightOutOfScope();
		final Resource right = input.getReferenceMonoUnsetLeftOutOfScope();
		final Resource origin = input.getReferenceMonoUnsetOriginOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);
		final EObject originValue = (EObject)originDiffContainer.eGet(feature, false);

		assertTrue(leftValue.eIsProxy());
		assertTrue(rightValue.eIsProxy());
		assertTrue(originValue.eIsProxy());

		assertEquals(((InternalEObject)leftValue).eProxyURI(), ((InternalEObject)rightValue).eProxyURI());

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoUnset3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoUnsetRightOutOfScope();
		final Resource right = input.getReferenceMonoUnsetLeftOutOfScope();
		final Resource origin = input.getReferenceMonoUnsetOriginOutOfScope();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);
		final EObject originValue = (EObject)originDiffContainer.eGet(feature, false);

		assertNull(leftValue);
		assertNull(rightValue);
		assertTrue(originValue.eIsProxy());

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiAdd2WayLtR() throws IOException {
		final Resource left = input.getReferenceMultiAddLeftOutOfScope();
		final Resource right = input.getReferenceMultiAddRightOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final List<EObject> leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		final List<EObject> rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);

		assertEquals(3, leftValue.size());
		assertEquals(3, rightValue.size());

		final URI proxyURI = ((InternalEObject)diff.getValue()).eProxyURI();
		int leftProxyIndex = -1;
		int rightProxyIndex = -1;
		for (int i = 0; i < leftValue.size() && leftProxyIndex == -1; i++) {
			final EObject candidate = leftValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				leftProxyIndex = i;
			}
		}
		for (int i = 0; i < rightValue.size() && rightProxyIndex == -1; i++) {
			final EObject candidate = rightValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				rightProxyIndex = i;
			}
		}
		assertTrue(leftProxyIndex != -1);
		assertTrue(rightProxyIndex != -1);
		assertEquals(leftProxyIndex, rightProxyIndex);

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiAdd2WayRtL() throws IOException {
		final Resource left = input.getReferenceMultiAddLeftOutOfScope();
		final Resource right = input.getReferenceMultiAddRightOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final List<EObject> leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		final List<EObject> rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());

		final URI proxyURI = ((InternalEObject)diff.getValue()).eProxyURI();
		int leftProxyIndex = -1;
		int rightProxyIndex = -1;
		for (int i = 0; i < leftValue.size() && leftProxyIndex == -1; i++) {
			final EObject candidate = leftValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				leftProxyIndex = i;
			}
		}
		for (int i = 0; i < rightValue.size() && rightProxyIndex == -1; i++) {
			final EObject candidate = rightValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				rightProxyIndex = i;
			}
		}
		assertEquals(-1, leftProxyIndex);
		assertEquals(-1, rightProxyIndex);

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiAdd3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getReferenceMultiAddLeftOutOfScope();
		final Resource right = input.getReferenceMultiAddRightOutOfScope();
		final Resource origin = input.getReferenceMultiAddOriginOutOfScope();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final List<EObject> leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		final List<EObject> rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);
		final List<EObject> originValue = (List<EObject>)originDiffContainer.eGet(feature, false);

		assertEquals(3, leftValue.size());
		assertEquals(3, rightValue.size());
		assertEquals(2, originValue.size());

		final URI proxyURI = ((InternalEObject)diff.getValue()).eProxyURI();
		int leftProxyIndex = -1;
		int rightProxyIndex = -1;
		int originProxyIndex = -1;
		for (int i = 0; i < leftValue.size() && leftProxyIndex == -1; i++) {
			final EObject candidate = leftValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				leftProxyIndex = i;
			}
		}
		for (int i = 0; i < rightValue.size() && rightProxyIndex == -1; i++) {
			final EObject candidate = rightValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				rightProxyIndex = i;
			}
		}
		for (int i = 0; i < originValue.size() && originProxyIndex == -1; i++) {
			final EObject candidate = originValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				originProxyIndex = i;
			}
		}
		assertTrue(leftProxyIndex != -1);
		assertTrue(rightProxyIndex != -1);
		assertEquals(-1, originProxyIndex);
		assertEquals(leftProxyIndex, rightProxyIndex);

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiAdd3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getReferenceMultiAddLeftOutOfScope();
		final Resource right = input.getReferenceMultiAddRightOutOfScope();
		final Resource origin = input.getReferenceMultiAddOriginOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final List<EObject> leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		final List<EObject> rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);
		final List<EObject> originValue = (List<EObject>)originDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());
		assertEquals(2, originValue.size());

		final URI proxyURI = ((InternalEObject)diff.getValue()).eProxyURI();
		int leftProxyIndex = -1;
		int rightProxyIndex = -1;
		int originProxyIndex = -1;
		for (int i = 0; i < leftValue.size() && leftProxyIndex == -1; i++) {
			final EObject candidate = leftValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				leftProxyIndex = i;
			}
		}
		for (int i = 0; i < rightValue.size() && rightProxyIndex == -1; i++) {
			final EObject candidate = rightValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				rightProxyIndex = i;
			}
		}
		for (int i = 0; i < originValue.size() && originProxyIndex == -1; i++) {
			final EObject candidate = originValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				originProxyIndex = i;
			}
		}
		assertEquals(-1, leftProxyIndex);
		assertEquals(-1, rightProxyIndex);
		assertEquals(-1, originProxyIndex);

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiAdd3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMultiAddRightOutOfScope();
		final Resource right = input.getReferenceMultiAddLeftOutOfScope();
		final Resource origin = input.getReferenceMultiAddOriginOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final List<EObject> leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		final List<EObject> rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);
		final List<EObject> originValue = (List<EObject>)originDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());
		assertEquals(2, originValue.size());

		final URI proxyURI = ((InternalEObject)diff.getValue()).eProxyURI();
		int leftProxyIndex = -1;
		int rightProxyIndex = -1;
		int originProxyIndex = -1;
		for (int i = 0; i < leftValue.size() && leftProxyIndex == -1; i++) {
			final EObject candidate = leftValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				leftProxyIndex = i;
			}
		}
		for (int i = 0; i < rightValue.size() && rightProxyIndex == -1; i++) {
			final EObject candidate = rightValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				rightProxyIndex = i;
			}
		}
		for (int i = 0; i < originValue.size() && originProxyIndex == -1; i++) {
			final EObject candidate = originValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				originProxyIndex = i;
			}
		}
		assertEquals(-1, leftProxyIndex);
		assertEquals(-1, rightProxyIndex);
		assertEquals(-1, originProxyIndex);

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiAdd3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMultiAddRightOutOfScope();
		final Resource right = input.getReferenceMultiAddLeftOutOfScope();
		final Resource origin = input.getReferenceMultiAddOriginOutOfScope();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final List<EObject> leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		final List<EObject> rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);
		final List<EObject> originValue = (List<EObject>)originDiffContainer.eGet(feature, false);

		assertEquals(3, leftValue.size());
		assertEquals(3, rightValue.size());
		assertEquals(2, originValue.size());

		final URI proxyURI = ((InternalEObject)diff.getValue()).eProxyURI();
		int leftProxyIndex = -1;
		int rightProxyIndex = -1;
		int originProxyIndex = -1;
		for (int i = 0; i < leftValue.size() && leftProxyIndex == -1; i++) {
			final EObject candidate = leftValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				leftProxyIndex = i;
			}
		}
		for (int i = 0; i < rightValue.size() && rightProxyIndex == -1; i++) {
			final EObject candidate = rightValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				rightProxyIndex = i;
			}
		}
		for (int i = 0; i < originValue.size() && originProxyIndex == -1; i++) {
			final EObject candidate = originValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				originProxyIndex = i;
			}
		}
		assertTrue(leftProxyIndex != -1);
		assertTrue(rightProxyIndex != -1);
		assertEquals(-1, originProxyIndex);
		assertEquals(leftProxyIndex, rightProxyIndex);

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiDel2WayLtR() throws IOException {
		final Resource left = input.getReferenceMultiDelLeftOutOfScope();
		final Resource right = input.getReferenceMultiDelRightOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final List<EObject> leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		final List<EObject> rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());

		final URI proxyURI = ((InternalEObject)diff.getValue()).eProxyURI();
		int leftProxyIndex = -1;
		int rightProxyIndex = -1;
		for (int i = 0; i < leftValue.size() && leftProxyIndex == -1; i++) {
			final EObject candidate = leftValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				leftProxyIndex = i;
			}
		}
		for (int i = 0; i < rightValue.size() && rightProxyIndex == -1; i++) {
			final EObject candidate = rightValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				rightProxyIndex = i;
			}
		}
		assertEquals(-1, leftProxyIndex);
		assertEquals(-1, rightProxyIndex);

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiDel2WayRtL() throws IOException {
		final Resource left = input.getReferenceMultiDelLeftOutOfScope();
		final Resource right = input.getReferenceMultiDelRightOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final List<EObject> leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		final List<EObject> rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);

		assertEquals(3, leftValue.size());
		assertEquals(3, rightValue.size());

		final URI proxyURI = ((InternalEObject)diff.getValue()).eProxyURI();
		int leftProxyIndex = -1;
		int rightProxyIndex = -1;
		for (int i = 0; i < leftValue.size() && leftProxyIndex == -1; i++) {
			final EObject candidate = leftValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				leftProxyIndex = i;
			}
		}
		for (int i = 0; i < rightValue.size() && rightProxyIndex == -1; i++) {
			final EObject candidate = rightValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				rightProxyIndex = i;
			}
		}
		assertTrue(leftProxyIndex != -1);
		assertTrue(rightProxyIndex != -1);
		assertEquals(leftProxyIndex, rightProxyIndex);

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiDel3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getReferenceMultiDelLeftOutOfScope();
		final Resource right = input.getReferenceMultiDelRightOutOfScope();
		final Resource origin = input.getReferenceMultiDelOriginOutOfScope();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final List<EObject> leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		final List<EObject> rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);
		final List<EObject> originValue = (List<EObject>)originDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());
		assertEquals(3, originValue.size());

		final URI proxyURI = ((InternalEObject)diff.getValue()).eProxyURI();
		int leftProxyIndex = -1;
		int rightProxyIndex = -1;
		int originProxyIndex = -1;
		for (int i = 0; i < leftValue.size() && leftProxyIndex == -1; i++) {
			final EObject candidate = leftValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				leftProxyIndex = i;
			}
		}
		for (int i = 0; i < rightValue.size() && rightProxyIndex == -1; i++) {
			final EObject candidate = rightValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				rightProxyIndex = i;
			}
		}
		for (int i = 0; i < originValue.size() && originProxyIndex == -1; i++) {
			final EObject candidate = originValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				originProxyIndex = i;
			}
		}
		assertEquals(-1, leftProxyIndex);
		assertEquals(-1, rightProxyIndex);
		// "deleted" value is the third (0-based)
		assertEquals(2, originProxyIndex);

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiDel3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMultiDelRightOutOfScope();
		final Resource right = input.getReferenceMultiDelLeftOutOfScope();
		final Resource origin = input.getReferenceMultiDelOriginOutOfScope();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final List<EObject> leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		final List<EObject> rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);
		final List<EObject> originValue = (List<EObject>)originDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());
		assertEquals(3, originValue.size());

		final URI proxyURI = ((InternalEObject)diff.getValue()).eProxyURI();
		int leftProxyIndex = -1;
		int rightProxyIndex = -1;
		int originProxyIndex = -1;
		for (int i = 0; i < leftValue.size() && leftProxyIndex == -1; i++) {
			final EObject candidate = leftValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				leftProxyIndex = i;
			}
		}
		for (int i = 0; i < rightValue.size() && rightProxyIndex == -1; i++) {
			final EObject candidate = rightValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				rightProxyIndex = i;
			}
		}
		for (int i = 0; i < originValue.size() && originProxyIndex == -1; i++) {
			final EObject candidate = originValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				originProxyIndex = i;
			}
		}
		assertEquals(-1, leftProxyIndex);
		assertEquals(-1, rightProxyIndex);
		// "deleted" value is the third (0-based)
		assertEquals(2, originProxyIndex);

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiDel3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMultiDelRightOutOfScope();
		final Resource right = input.getReferenceMultiDelLeftOutOfScope();
		final Resource origin = input.getReferenceMultiDelOriginOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final List<EObject> leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		final List<EObject> rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);
		final List<EObject> originValue = (List<EObject>)originDiffContainer.eGet(feature, false);

		assertEquals(3, leftValue.size());
		assertEquals(3, rightValue.size());
		assertEquals(3, originValue.size());

		final URI proxyURI = ((InternalEObject)diff.getValue()).eProxyURI();
		int leftProxyIndex = -1;
		int rightProxyIndex = -1;
		int originProxyIndex = -1;
		for (int i = 0; i < leftValue.size() && leftProxyIndex == -1; i++) {
			final EObject candidate = leftValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				leftProxyIndex = i;
			}
		}
		for (int i = 0; i < rightValue.size() && rightProxyIndex == -1; i++) {
			final EObject candidate = rightValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				rightProxyIndex = i;
			}
		}
		for (int i = 0; i < originValue.size() && originProxyIndex == -1; i++) {
			final EObject candidate = originValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				originProxyIndex = i;
			}
		}
		assertTrue(leftProxyIndex != -1);
		assertTrue(rightProxyIndex != -1);
		// "deleted" value is the third (0-based)
		assertEquals(2, originProxyIndex);
		assertEquals(leftProxyIndex, rightProxyIndex);

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiDel3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getReferenceMultiDelLeftOutOfScope();
		final Resource right = input.getReferenceMultiDelRightOutOfScope();
		final Resource origin = input.getReferenceMultiDelOriginOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final List<EObject> leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		final List<EObject> rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);
		final List<EObject> originValue = (List<EObject>)originDiffContainer.eGet(feature, false);

		assertEquals(3, leftValue.size());
		assertEquals(3, rightValue.size());
		assertEquals(3, originValue.size());

		final URI proxyURI = ((InternalEObject)diff.getValue()).eProxyURI();
		int leftProxyIndex = -1;
		int rightProxyIndex = -1;
		int originProxyIndex = -1;
		for (int i = 0; i < leftValue.size() && leftProxyIndex == -1; i++) {
			final EObject candidate = leftValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				leftProxyIndex = i;
			}
		}
		for (int i = 0; i < rightValue.size() && rightProxyIndex == -1; i++) {
			final EObject candidate = rightValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				rightProxyIndex = i;
			}
		}
		for (int i = 0; i < originValue.size() && originProxyIndex == -1; i++) {
			final EObject candidate = originValue.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				originProxyIndex = i;
			}
		}
		assertTrue(leftProxyIndex != -1);
		assertTrue(rightProxyIndex != -1);
		// "deleted" value is the third (0-based)
		assertEquals(2, originProxyIndex);
		assertEquals(leftProxyIndex, rightProxyIndex);

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiMove2WayLtR() throws IOException {
		final Resource left = input.getReferenceMultiMoveLeftOutOfScope();
		final Resource right = input.getReferenceMultiMoveRightOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		List<EObject> leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		List<EObject> rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());

		final URI proxyURI = ((InternalEObject)diff.getValue()).eProxyURI();
		int leftProxyIndex = findProxyIndexIn(proxyURI, leftValue);
		int rightProxyIndex = findProxyIndexIn(proxyURI, rightValue);

		assertTrue(leftProxyIndex != -1);
		assertTrue(rightProxyIndex != -1);
		assertFalse(leftProxyIndex == rightProxyIndex);

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());

		leftProxyIndex = findProxyIndexIn(proxyURI, leftValue);
		rightProxyIndex = findProxyIndexIn(proxyURI, rightValue);

		assertTrue(leftProxyIndex != -1);
		assertTrue(rightProxyIndex != -1);
		assertEquals(leftProxyIndex, rightProxyIndex);

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiMove2WayRtL() throws IOException {
		final Resource left = input.getReferenceMultiMoveLeftOutOfScope();
		final Resource right = input.getReferenceMultiMoveRightOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		List<EObject> leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		List<EObject> rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());

		final URI proxyURI = ((InternalEObject)diff.getValue()).eProxyURI();
		int leftProxyIndex = findProxyIndexIn(proxyURI, leftValue);
		int rightProxyIndex = findProxyIndexIn(proxyURI, rightValue);

		assertTrue(leftProxyIndex != -1);
		assertTrue(rightProxyIndex != -1);
		assertFalse(leftProxyIndex == rightProxyIndex);

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());

		leftProxyIndex = findProxyIndexIn(proxyURI, leftValue);
		rightProxyIndex = findProxyIndexIn(proxyURI, rightValue);

		assertTrue(leftProxyIndex != -1);
		assertTrue(rightProxyIndex != -1);
		assertEquals(leftProxyIndex, rightProxyIndex);

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiMove3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getReferenceMultiMoveLeftOutOfScope();
		final Resource right = input.getReferenceMultiMoveRightOutOfScope();
		final Resource origin = input.getReferenceMultiMoveOriginOutOfScope();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		List<EObject> leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		List<EObject> rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);
		List<EObject> originValue = (List<EObject>)originDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());
		assertEquals(2, rightValue.size());

		final URI proxyURI = ((InternalEObject)diff.getValue()).eProxyURI();
		int leftProxyIndex = findProxyIndexIn(proxyURI, leftValue);
		int rightProxyIndex = findProxyIndexIn(proxyURI, rightValue);
		int originProxyIndex = findProxyIndexIn(proxyURI, originValue);

		assertTrue(leftProxyIndex != -1);
		assertTrue(rightProxyIndex != -1);
		assertTrue(originProxyIndex != -1);
		assertFalse(leftProxyIndex == rightProxyIndex);
		assertEquals(rightProxyIndex, originProxyIndex);

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);
		originValue = (List<EObject>)originDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());
		assertEquals(2, originValue.size());

		leftProxyIndex = findProxyIndexIn(proxyURI, leftValue);
		rightProxyIndex = findProxyIndexIn(proxyURI, rightValue);
		originProxyIndex = findProxyIndexIn(proxyURI, originValue);

		assertTrue(leftProxyIndex != -1);
		assertTrue(rightProxyIndex != -1);
		assertTrue(originProxyIndex != -1);
		assertEquals(leftProxyIndex, rightProxyIndex);
		assertFalse(leftProxyIndex == originProxyIndex);

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiMove3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getReferenceMultiMoveLeftOutOfScope();
		final Resource right = input.getReferenceMultiMoveRightOutOfScope();
		final Resource origin = input.getReferenceMultiMoveOriginOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		List<EObject> leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		List<EObject> rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);
		List<EObject> originValue = (List<EObject>)originDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());
		assertEquals(2, rightValue.size());

		final URI proxyURI = ((InternalEObject)diff.getValue()).eProxyURI();
		int leftProxyIndex = findProxyIndexIn(proxyURI, leftValue);
		int rightProxyIndex = findProxyIndexIn(proxyURI, rightValue);
		int originProxyIndex = findProxyIndexIn(proxyURI, originValue);

		assertTrue(leftProxyIndex != -1);
		assertTrue(rightProxyIndex != -1);
		assertTrue(originProxyIndex != -1);
		assertFalse(leftProxyIndex == rightProxyIndex);
		assertEquals(rightProxyIndex, originProxyIndex);

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);
		originValue = (List<EObject>)originDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());
		assertEquals(2, originValue.size());

		leftProxyIndex = findProxyIndexIn(proxyURI, leftValue);
		rightProxyIndex = findProxyIndexIn(proxyURI, rightValue);
		originProxyIndex = findProxyIndexIn(proxyURI, originValue);

		assertTrue(leftProxyIndex != -1);
		assertTrue(rightProxyIndex != -1);
		assertTrue(originProxyIndex != -1);
		assertEquals(leftProxyIndex, rightProxyIndex);
		assertEquals(leftProxyIndex, originProxyIndex);

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiMove3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMultiMoveRightOutOfScope();
		final Resource right = input.getReferenceMultiMoveLeftOutOfScope();
		final Resource origin = input.getReferenceMultiMoveOriginOutOfScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		List<EObject> leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		List<EObject> rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);
		List<EObject> originValue = (List<EObject>)originDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());
		assertEquals(2, rightValue.size());

		final URI proxyURI = ((InternalEObject)diff.getValue()).eProxyURI();
		int leftProxyIndex = findProxyIndexIn(proxyURI, leftValue);
		int rightProxyIndex = findProxyIndexIn(proxyURI, rightValue);
		int originProxyIndex = findProxyIndexIn(proxyURI, originValue);

		assertTrue(leftProxyIndex != -1);
		assertTrue(rightProxyIndex != -1);
		assertTrue(originProxyIndex != -1);
		assertFalse(leftProxyIndex == rightProxyIndex);
		assertEquals(leftProxyIndex, originProxyIndex);

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);
		originValue = (List<EObject>)originDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());
		assertEquals(2, originValue.size());

		leftProxyIndex = findProxyIndexIn(proxyURI, leftValue);
		rightProxyIndex = findProxyIndexIn(proxyURI, rightValue);
		originProxyIndex = findProxyIndexIn(proxyURI, originValue);

		assertTrue(leftProxyIndex != -1);
		assertTrue(rightProxyIndex != -1);
		assertTrue(originProxyIndex != -1);
		assertEquals(leftProxyIndex, rightProxyIndex);
		assertEquals(leftProxyIndex, originProxyIndex);

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiMove3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMultiMoveRightOutOfScope();
		final Resource right = input.getReferenceMultiMoveLeftOutOfScope();
		final Resource origin = input.getReferenceMultiMoveOriginOutOfScope();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedReference";
		assertTrue(differences.get(0) instanceof ReferenceChange);

		final ReferenceChange diff = (ReferenceChange)differences.get(0);

		assertTrue(diff.getValue().eIsProxy());
		assertEquals(featureName, diff.getReference().getName());

		final EObject leftDiffContainer = getNodeNamed(left, "origin");
		final EObject rightDiffContainer = getNodeNamed(right, "origin");
		final EObject originDiffContainer = getNodeNamed(origin, "origin");
		assertSame(leftDiffContainer, diff.getMatch().getLeft());
		assertSame(rightDiffContainer, diff.getMatch().getRight());
		assertSame(originDiffContainer, diff.getMatch().getOrigin());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		List<EObject> leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		List<EObject> rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);
		List<EObject> originValue = (List<EObject>)originDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());
		assertEquals(2, rightValue.size());

		final URI proxyURI = ((InternalEObject)diff.getValue()).eProxyURI();
		int leftProxyIndex = findProxyIndexIn(proxyURI, leftValue);
		int rightProxyIndex = findProxyIndexIn(proxyURI, rightValue);
		int originProxyIndex = findProxyIndexIn(proxyURI, originValue);

		assertTrue(leftProxyIndex != -1);
		assertTrue(rightProxyIndex != -1);
		assertTrue(originProxyIndex != -1);
		assertFalse(leftProxyIndex == rightProxyIndex);
		assertEquals(leftProxyIndex, originProxyIndex);

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);
		originValue = (List<EObject>)originDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());
		assertEquals(2, originValue.size());

		leftProxyIndex = findProxyIndexIn(proxyURI, leftValue);
		rightProxyIndex = findProxyIndexIn(proxyURI, rightValue);
		originProxyIndex = findProxyIndexIn(proxyURI, originValue);

		assertTrue(leftProxyIndex != -1);
		assertTrue(rightProxyIndex != -1);
		assertTrue(originProxyIndex != -1);
		assertEquals(leftProxyIndex, rightProxyIndex);
		assertFalse(leftProxyIndex == originProxyIndex);

		// We should have no difference between left and right ... though they might be different from origin
		scope = new DefaultComparisonScope(left, right, null);
		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
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

	private int findProxyIndexIn(URI proxyURI, List<EObject> list) {
		for (int i = 0; i < list.size(); i++) {
			final EObject candidate = list.get(i);
			if (candidate.eIsProxy() && proxyURI.equals(((InternalEObject)candidate).eProxyURI())) {
				return i;
			}
		}
		return -1;
	}
}
