/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ReferenceChange;
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

	@Test
	public void testReferenceMonoChange2WayLtR() throws IOException {
		final Resource left = input.getReferenceMonoChangeLeftOutOfScope();
		final Resource right = input.getReferenceMonoChangeRightOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right);

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

		diff.copyLeftToRight();
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);

		assertTrue(leftValue.eIsProxy());
		assertTrue(rightValue.eIsProxy());

		assertEquals(((InternalEObject)leftValue).eProxyURI(), ((InternalEObject)rightValue).eProxyURI());

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoChange2WayRtL() throws IOException {
		final Resource left = input.getReferenceMonoChangeLeftOutOfScope();
		final Resource right = input.getReferenceMonoChangeRightOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right);

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

		diff.copyRightToLeft();
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);

		assertTrue(leftValue.eIsProxy());
		assertTrue(rightValue.eIsProxy());

		assertEquals(((InternalEObject)leftValue).eProxyURI(), ((InternalEObject)rightValue).eProxyURI());

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoChange3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getReferenceMonoChangeLeftOutOfScope();
		final Resource right = input.getReferenceMonoChangeRightOutOfScope();
		final Resource origin = input.getReferenceMonoChangeOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyLeftToRight();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoChange3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getReferenceMonoChangeLeftOutOfScope();
		final Resource right = input.getReferenceMonoChangeRightOutOfScope();
		final Resource origin = input.getReferenceMonoChangeOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyRightToLeft();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoChange3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoChangeRightOutOfScope();
		final Resource right = input.getReferenceMonoChangeLeftOutOfScope();
		final Resource origin = input.getReferenceMonoChangeOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyLeftToRight();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoChange3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoChangeRightOutOfScope();
		final Resource right = input.getReferenceMonoChangeLeftOutOfScope();
		final Resource origin = input.getReferenceMonoChangeOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyRightToLeft();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoSet2WayLtR() throws IOException {
		final Resource left = input.getReferenceMonoSetLeftOutOfScope();
		final Resource right = input.getReferenceMonoSetRightOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right);

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

		diff.copyLeftToRight();
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);

		assertTrue(leftValue.eIsProxy());
		assertTrue(rightValue.eIsProxy());

		assertEquals(((InternalEObject)leftValue).eProxyURI(), ((InternalEObject)rightValue).eProxyURI());

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoSet2WayRtL() throws IOException {
		final Resource left = input.getReferenceMonoSetLeftOutOfScope();
		final Resource right = input.getReferenceMonoSetRightOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right);

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

		diff.copyRightToLeft();
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);

		assertNull(leftValue);
		assertNull(rightValue);

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoSet3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getReferenceMonoSetLeftOutOfScope();
		final Resource right = input.getReferenceMonoSetRightOutOfScope();
		final Resource origin = input.getReferenceMonoSetOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyLeftToRight();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoSet3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getReferenceMonoSetLeftOutOfScope();
		final Resource right = input.getReferenceMonoSetRightOutOfScope();
		final Resource origin = input.getReferenceMonoSetOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyRightToLeft();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoSet3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoSetRightOutOfScope();
		final Resource right = input.getReferenceMonoSetLeftOutOfScope();
		final Resource origin = input.getReferenceMonoSetOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyLeftToRight();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoSet3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoSetRightOutOfScope();
		final Resource right = input.getReferenceMonoSetLeftOutOfScope();
		final Resource origin = input.getReferenceMonoSetOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyRightToLeft();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoUnset2WayLtR() throws IOException {
		final Resource left = input.getReferenceMonoUnsetLeftOutOfScope();
		final Resource right = input.getReferenceMonoUnsetRightOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right);

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

		diff.copyLeftToRight();
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);

		assertNull(leftValue);
		assertNull(rightValue);

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoUnset2WayRtL() throws IOException {
		final Resource left = input.getReferenceMonoUnsetLeftOutOfScope();
		final Resource right = input.getReferenceMonoUnsetRightOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right);

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

		diff.copyRightToLeft();
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		final EStructuralFeature feature = leftDiffContainer.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final EObject leftValue = (EObject)leftDiffContainer.eGet(feature, false);
		final EObject rightValue = (EObject)rightDiffContainer.eGet(feature, false);

		assertTrue(leftValue.eIsProxy());
		assertTrue(rightValue.eIsProxy());

		assertEquals(((InternalEObject)leftValue).eProxyURI(), ((InternalEObject)rightValue).eProxyURI());

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoUnset3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getReferenceMonoUnsetLeftOutOfScope();
		final Resource right = input.getReferenceMonoUnsetRightOutOfScope();
		final Resource origin = input.getReferenceMonoUnsetOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyLeftToRight();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoUnset3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getReferenceMonoUnsetLeftOutOfScope();
		final Resource right = input.getReferenceMonoUnsetRightOutOfScope();
		final Resource origin = input.getReferenceMonoUnsetOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyRightToLeft();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoUnset3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoUnsetRightOutOfScope();
		final Resource right = input.getReferenceMonoUnsetLeftOutOfScope();
		final Resource origin = input.getReferenceMonoUnsetOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyLeftToRight();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoUnset3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoUnsetRightOutOfScope();
		final Resource right = input.getReferenceMonoUnsetLeftOutOfScope();
		final Resource origin = input.getReferenceMonoUnsetOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyRightToLeft();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiAdd2WayLtR() throws IOException {
		final Resource left = input.getReferenceMultiAddLeftOutOfScope();
		final Resource right = input.getReferenceMultiAddRightOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right);

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

		diff.copyLeftToRight();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiAdd2WayRtL() throws IOException {
		final Resource left = input.getReferenceMultiAddLeftOutOfScope();
		final Resource right = input.getReferenceMultiAddRightOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right);

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

		diff.copyRightToLeft();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiAdd3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getReferenceMultiAddLeftOutOfScope();
		final Resource right = input.getReferenceMultiAddRightOutOfScope();
		final Resource origin = input.getReferenceMultiAddOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyLeftToRight();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiAdd3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getReferenceMultiAddLeftOutOfScope();
		final Resource right = input.getReferenceMultiAddRightOutOfScope();
		final Resource origin = input.getReferenceMultiAddOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyRightToLeft();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiAdd3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMultiAddRightOutOfScope();
		final Resource right = input.getReferenceMultiAddLeftOutOfScope();
		final Resource origin = input.getReferenceMultiAddOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyLeftToRight();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiAdd3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMultiAddRightOutOfScope();
		final Resource right = input.getReferenceMultiAddLeftOutOfScope();
		final Resource origin = input.getReferenceMultiAddOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyRightToLeft();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiDel2WayLtR() throws IOException {
		final Resource left = input.getReferenceMultiDelLeftOutOfScope();
		final Resource right = input.getReferenceMultiDelRightOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right);

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

		diff.copyLeftToRight();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiDel2WayRtL() throws IOException {
		final Resource left = input.getReferenceMultiDelLeftOutOfScope();
		final Resource right = input.getReferenceMultiDelRightOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right);

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

		diff.copyRightToLeft();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiDel3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getReferenceMultiDelLeftOutOfScope();
		final Resource right = input.getReferenceMultiDelRightOutOfScope();
		final Resource origin = input.getReferenceMultiDelOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyLeftToRight();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiDel3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMultiDelRightOutOfScope();
		final Resource right = input.getReferenceMultiDelLeftOutOfScope();
		final Resource origin = input.getReferenceMultiDelOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyRightToLeft();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiDel3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMultiDelRightOutOfScope();
		final Resource right = input.getReferenceMultiDelLeftOutOfScope();
		final Resource origin = input.getReferenceMultiDelOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyLeftToRight();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiDel3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getReferenceMultiDelLeftOutOfScope();
		final Resource right = input.getReferenceMultiDelRightOutOfScope();
		final Resource origin = input.getReferenceMultiDelOriginOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right, origin);

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

		diff.copyRightToLeft();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMultiMove2WayLtR() throws IOException {
		final Resource left = input.getReferenceMultiMoveLeftOutOfScope();
		final Resource right = input.getReferenceMultiMoveRightOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right);

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
		assertFalse(leftProxyIndex == rightProxyIndex);

		diff.copyLeftToRight();
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());

		leftProxyIndex = -1;
		rightProxyIndex = -1;
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
	}

	@Test
	public void testReferenceMultiMove2WayRtL() throws IOException {
		final Resource left = input.getReferenceMultiMoveLeftOutOfScope();
		final Resource right = input.getReferenceMultiMoveRightOutOfScope();

		Comparison comparison = EMFCompare.compare(left, right);

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
		assertFalse(leftProxyIndex == rightProxyIndex);

		diff.copyRightToLeft();
		// The proxy should not have been resolved by the merge operation
		assertTrue(diff.getValue().eIsProxy());

		leftValue = (List<EObject>)leftDiffContainer.eGet(feature, false);
		rightValue = (List<EObject>)rightDiffContainer.eGet(feature, false);

		assertEquals(2, leftValue.size());
		assertEquals(2, rightValue.size());

		leftProxyIndex = -1;
		rightProxyIndex = -1;
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
	}

	// @Test
	// public void testReferenceMultiMove3WayLeftChangeLtR() throws IOException {
	// final Resource left = input.getReferenceMultiMoveLeft();
	// final Resource right = input.getReferenceMultiMoveRight();
	// final Resource origin = input.getReferenceMultiMoveOrigin();
	//
	// Comparison comparison = EMFCompare.compare(left, right, origin);
	//
	// final List<Diff> differences = comparison.getDifferences();
	// assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));
	//
	// final String featureName = "multiValuedReference";
	// final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
	// movedInReference("root.origin", featureName, "root.value1")));
	//
	// diff.copyLeftToRight();
	// final EObject targetNode = getNodeNamed(right, "origin");
	// assertNotNull(targetNode);
	// final EObject sourceNode = getNodeNamed(left, "origin");
	// assertNotNull(sourceNode);
	// final EStructuralFeature feature = targetNode.eClass().getEStructuralFeature(featureName);
	// assertNotNull(feature);
	//
	// final Object targetFeatureValue = targetNode.eGet(feature);
	// final Object sourceFeatureValue = sourceNode.eGet(feature);
	// assertTrue(targetFeatureValue instanceof List<?>);
	// assertTrue(sourceFeatureValue instanceof List<?>);
	// assertEqualContents(comparison, ((List<EObject>)targetFeatureValue),
	// ((List<EObject>)sourceFeatureValue));
	//
	// comparison = EMFCompare.compare(left, right);
	// assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	// }

	// @Test
	// public void testReferenceMultiMove3WayLeftChangeRtL() throws IOException {
	// final Resource left = input.getReferenceMultiMoveLeft();
	// final Resource right = input.getReferenceMultiMoveRight();
	// final Resource origin = input.getReferenceMultiMoveOrigin();
	//
	// Comparison comparison = EMFCompare.compare(left, right, origin);
	//
	// final List<Diff> differences = comparison.getDifferences();
	// assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));
	//
	// final String featureName = "multiValuedReference";
	// final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
	// movedInReference("root.origin", featureName, "root.value1")));
	//
	// diff.copyRightToLeft();
	// final EObject targetNode = getNodeNamed(left, "origin");
	// assertNotNull(targetNode);
	// final EObject sourceNode = getNodeNamed(origin, "origin");
	// assertNotNull(sourceNode);
	// final EStructuralFeature feature = targetNode.eClass().getEStructuralFeature(featureName);
	// assertNotNull(feature);
	//
	// final Object targetFeatureValue = targetNode.eGet(feature);
	// final Object sourceFeatureValue = sourceNode.eGet(feature);
	// assertTrue(targetFeatureValue instanceof List<?>);
	// assertTrue(sourceFeatureValue instanceof List<?>);
	// assertEqualContents(comparison, ((List<EObject>)targetFeatureValue),
	// ((List<EObject>)sourceFeatureValue));
	//
	// comparison = EMFCompare.compare(left, right);
	// assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	// }

	// @Test
	// public void testReferenceMultiMove3WayRightChangeLtR() throws IOException {
	// // In order to have changes on the right side, we'll invert right and left
	// final Resource left = input.getReferenceMultiMoveRight();
	// final Resource right = input.getReferenceMultiMoveLeft();
	// final Resource origin = input.getReferenceMultiMoveOrigin();
	//
	// Comparison comparison = EMFCompare.compare(left, right, origin);
	//
	// final List<Diff> differences = comparison.getDifferences();
	// assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));
	//
	// final String featureName = "multiValuedReference";
	// final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
	// movedInReference("root.origin", featureName, "root.value1")));
	//
	// diff.copyLeftToRight();
	// final EObject targetNode = getNodeNamed(right, "origin");
	// assertNotNull(targetNode);
	// final EObject sourceNode = getNodeNamed(origin, "origin");
	// assertNotNull(sourceNode);
	// final EStructuralFeature feature = targetNode.eClass().getEStructuralFeature(featureName);
	// assertNotNull(feature);
	//
	// final Object targetFeatureValue = targetNode.eGet(feature);
	// final Object sourceFeatureValue = sourceNode.eGet(feature);
	// assertTrue(targetFeatureValue instanceof List<?>);
	// assertTrue(sourceFeatureValue instanceof List<?>);
	// assertEqualContents(comparison, ((List<EObject>)targetFeatureValue),
	// ((List<EObject>)sourceFeatureValue));
	//
	// comparison = EMFCompare.compare(left, right);
	// assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	// }

	// @Test
	// public void testReferenceMultiMove3WayRightChangeRtL() throws IOException {
	// // In order to have changes on the right side, we'll invert right and left
	// final Resource left = input.getReferenceMultiMoveRight();
	// final Resource right = input.getReferenceMultiMoveLeft();
	// final Resource origin = input.getReferenceMultiMoveOrigin();
	//
	// Comparison comparison = EMFCompare.compare(left, right, origin);
	//
	// final List<Diff> differences = comparison.getDifferences();
	// assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));
	//
	// final String featureName = "multiValuedReference";
	// final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
	// movedInReference("root.origin", featureName, "root.value1")));
	//
	// diff.copyRightToLeft();
	// final EObject targetNode = getNodeNamed(left, "origin");
	// assertNotNull(targetNode);
	// final EObject sourceNode = getNodeNamed(right, "origin");
	// assertNotNull(sourceNode);
	// final EStructuralFeature feature = targetNode.eClass().getEStructuralFeature(featureName);
	// assertNotNull(feature);
	//
	// final Object targetFeatureValue = targetNode.eGet(feature);
	// final Object sourceFeatureValue = sourceNode.eGet(feature);
	// assertTrue(targetFeatureValue instanceof List<?>);
	// assertTrue(sourceFeatureValue instanceof List<?>);
	// assertEqualContents(comparison, ((List<EObject>)targetFeatureValue),
	// ((List<EObject>)sourceFeatureValue));
	//
	// comparison = EMFCompare.compare(left, right);
	// assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	// }

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
