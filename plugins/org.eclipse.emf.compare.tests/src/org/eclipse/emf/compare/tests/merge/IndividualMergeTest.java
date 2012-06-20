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

import static com.google.common.base.Predicates.and;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.movedInAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromAttribute;

import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.tests.merge.data.IndividualDiffInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

@SuppressWarnings("nls")
public class IndividualMergeTest {
	private IndividualDiffInputData input = new IndividualDiffInputData();

	@Test
	public void testAttributeMonoChange2WayLtR() throws IOException {
		final Resource left = input.getAttributeMonoChangeLeft();
		final Resource right = input.getAttributeMonoChangeRight();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, "originValue", "leftValue")));

		diff.copyLeftToRight();
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("leftValue", originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMonoChange2WayRtL() throws IOException {
		final Resource left = input.getAttributeMonoChangeLeft();
		final Resource right = input.getAttributeMonoChangeRight();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, "originValue", "leftValue")));

		diff.copyRightToLeft();
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("originValue", originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMonoChange3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getAttributeMonoChangeLeft();
		final Resource right = input.getAttributeMonoChangeRight();
		final Resource origin = input.getAttributeMonoChangeOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, "originValue", "leftValue")));

		diff.copyLeftToRight();
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("leftValue", originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMonoChange3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getAttributeMonoChangeLeft();
		final Resource right = input.getAttributeMonoChangeRight();
		final Resource origin = input.getAttributeMonoChangeOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, "originValue", "leftValue")));

		diff.copyRightToLeft();
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("originValue", originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMonoChange3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMonoChangeRight();
		final Resource right = input.getAttributeMonoChangeLeft();
		final Resource origin = input.getAttributeMonoChangeOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedAttribute("root.origin", featureName, "originValue", "leftValue")));

		diff.copyLeftToRight();
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("originValue", originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMonoChange3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMonoChangeRight();
		final Resource right = input.getAttributeMonoChangeLeft();
		final Resource origin = input.getAttributeMonoChangeOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedAttribute("root.origin", featureName, "originValue", "leftValue")));

		diff.copyRightToLeft();
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("leftValue", originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMonoSet2WayLtR() throws IOException {
		final Resource left = input.getAttributeMonoSetLeft();
		final Resource right = input.getAttributeMonoSetRight();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, null, "leftValue")));

		diff.copyLeftToRight();
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("leftValue", originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMonoSet2WayRtL() throws IOException {
		final Resource left = input.getAttributeMonoSetLeft();
		final Resource right = input.getAttributeMonoSetRight();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, null, "leftValue")));

		diff.copyRightToLeft();
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertNull(originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMonoSet3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getAttributeMonoSetLeft();
		final Resource right = input.getAttributeMonoSetRight();
		final Resource origin = input.getAttributeMonoSetOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, null, "leftValue")));

		diff.copyLeftToRight();
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("leftValue", originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMonoSet3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getAttributeMonoSetLeft();
		final Resource right = input.getAttributeMonoSetRight();
		final Resource origin = input.getAttributeMonoSetOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, null, "leftValue")));

		diff.copyRightToLeft();
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertNull(originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMonoSet3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMonoSetRight();
		final Resource right = input.getAttributeMonoSetLeft();
		final Resource origin = input.getAttributeMonoSetOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedAttribute("root.origin", featureName, null, "leftValue")));

		diff.copyLeftToRight();
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertNull(originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMonoSet3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMonoSetRight();
		final Resource right = input.getAttributeMonoSetLeft();
		final Resource origin = input.getAttributeMonoSetOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedAttribute("root.origin", featureName, null, "leftValue")));

		diff.copyRightToLeft();
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("leftValue", originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMonoUnset2WayLtR() throws IOException {
		final Resource left = input.getAttributeMonoUnsetLeft();
		final Resource right = input.getAttributeMonoUnsetRight();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, "originValue", null)));

		diff.copyLeftToRight();
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertNull(originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMonoUnset2WayRtL() throws IOException {
		final Resource left = input.getAttributeMonoUnsetLeft();
		final Resource right = input.getAttributeMonoUnsetRight();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, "originValue", null)));

		diff.copyRightToLeft();
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("originValue", originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMonoUnset3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getAttributeMonoUnsetLeft();
		final Resource right = input.getAttributeMonoUnsetRight();
		final Resource origin = input.getAttributeMonoUnsetOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, "originValue", null)));

		diff.copyLeftToRight();
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertNull(originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMonoUnset3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getAttributeMonoUnsetLeft();
		final Resource right = input.getAttributeMonoUnsetRight();
		final Resource origin = input.getAttributeMonoUnsetOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, "originValue", null)));

		diff.copyRightToLeft();
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("originValue", originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMonoUnset3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMonoUnsetRight();
		final Resource right = input.getAttributeMonoUnsetLeft();
		final Resource origin = input.getAttributeMonoUnsetOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedAttribute("root.origin", featureName, "originValue", null)));

		diff.copyLeftToRight();
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("originValue", originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMonoUnset3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMonoUnsetRight();
		final Resource right = input.getAttributeMonoUnsetLeft();
		final Resource origin = input.getAttributeMonoUnsetOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedAttribute("root.origin", featureName, "originValue", null)));

		diff.copyRightToLeft();
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertNull(originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMultiAdd2WayLtR() throws IOException {
		final Resource left = input.getAttributeMultiAddLeft();
		final Resource right = input.getAttributeMultiAddRight();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				addedToAttribute("root.origin", featureName, "value1")));

		diff.copyLeftToRight();
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMultiAdd2WayRtL() throws IOException {
		final Resource left = input.getAttributeMultiAddLeft();
		final Resource right = input.getAttributeMultiAddRight();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				addedToAttribute("root.origin", featureName, "value1")));

		diff.copyRightToLeft();
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertFalse(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMultiAdd3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getAttributeMultiAddLeft();
		final Resource right = input.getAttributeMultiAddRight();
		final Resource origin = input.getAttributeMultiAddOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				addedToAttribute("root.origin", featureName, "value1")));

		diff.copyLeftToRight();
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMultiAdd3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getAttributeMultiAddLeft();
		final Resource right = input.getAttributeMultiAddRight();
		final Resource origin = input.getAttributeMultiAddOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				addedToAttribute("root.origin", featureName, "value1")));

		diff.copyRightToLeft();
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertFalse(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMultiAdd3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMultiAddRight();
		final Resource right = input.getAttributeMultiAddLeft();
		final Resource origin = input.getAttributeMultiAddOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				addedToAttribute("root.origin", featureName, "value1")));

		diff.copyLeftToRight();
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertFalse(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMultiAdd3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMultiAddRight();
		final Resource right = input.getAttributeMultiAddLeft();
		final Resource origin = input.getAttributeMultiAddOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				addedToAttribute("root.origin", featureName, "value1")));

		diff.copyRightToLeft();
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMultiDel2WayLtR() throws IOException {
		final Resource left = input.getAttributeMultiDelLeft();
		final Resource right = input.getAttributeMultiDelRight();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				removedFromAttribute("root.origin", featureName, "value1")));

		diff.copyLeftToRight();
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertFalse(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMultiDel2WayRtL() throws IOException {
		final Resource left = input.getAttributeMultiDelLeft();
		final Resource right = input.getAttributeMultiDelRight();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				removedFromAttribute("root.origin", featureName, "value1")));

		diff.copyRightToLeft();
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMultiDel3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getAttributeMultiDelLeft();
		final Resource right = input.getAttributeMultiDelRight();
		final Resource origin = input.getAttributeMultiDelOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				removedFromAttribute("root.origin", featureName, "value1")));

		diff.copyLeftToRight();
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertFalse(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMultiDel3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getAttributeMultiDelLeft();
		final Resource right = input.getAttributeMultiDelRight();
		final Resource origin = input.getAttributeMultiDelOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				removedFromAttribute("root.origin", featureName, "value1")));

		diff.copyRightToLeft();
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMultiDel3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMultiDelRight();
		final Resource right = input.getAttributeMultiDelLeft();
		final Resource origin = input.getAttributeMultiDelOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				removedFromAttribute("root.origin", featureName, "value1")));

		diff.copyLeftToRight();
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertTrue(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testAttributeMultiDel3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMultiDelRight();
		final Resource right = input.getAttributeMultiDelLeft();
		final Resource origin = input.getAttributeMultiDelOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				removedFromAttribute("root.origin", featureName, "value1")));

		diff.copyRightToLeft();
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		final Object featureValue = originNode.eGet(feature);
		assertTrue(featureValue instanceof Collection<?>);
		assertFalse(((Collection<?>)featureValue).contains("value1"));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAttributeMultiMove2WayLtR() throws IOException {
		final Resource left = input.getAttributeMultiMoveLeft();
		final Resource right = input.getAttributeMultiMoveRight();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				movedInAttribute("root.origin", featureName, "value1")));

		diff.copyLeftToRight();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAttributeMultiMove2WayRtL() throws IOException {
		final Resource left = input.getAttributeMultiMoveLeft();
		final Resource right = input.getAttributeMultiMoveRight();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				movedInAttribute("root.origin", featureName, "value1")));

		diff.copyRightToLeft();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAttributeMultiMove3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getAttributeMultiMoveLeft();
		final Resource right = input.getAttributeMultiMoveRight();
		final Resource origin = input.getAttributeMultiMoveOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				movedInAttribute("root.origin", featureName, "value1")));

		diff.copyLeftToRight();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAttributeMultiMove3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getAttributeMultiMoveLeft();
		final Resource right = input.getAttributeMultiMoveRight();
		final Resource origin = input.getAttributeMultiMoveOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				movedInAttribute("root.origin", featureName, "value1")));

		diff.copyRightToLeft();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAttributeMultiMove3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMultiMoveRight();
		final Resource right = input.getAttributeMultiMoveLeft();
		final Resource origin = input.getAttributeMultiMoveOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				movedInAttribute("root.origin", featureName, "value1")));

		diff.copyLeftToRight();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAttributeMultiMove3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getAttributeMultiMoveRight();
		final Resource right = input.getAttributeMultiMoveLeft();
		final Resource origin = input.getAttributeMultiMoveOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "multiValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				movedInAttribute("root.origin", featureName, "value1")));

		diff.copyRightToLeft();
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

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoChange2WayLtR() throws IOException {
		final Resource left = input.getReferenceMonoChangeLeft();
		final Resource right = input.getReferenceMonoChangeRight();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedReference("root.origin", featureName, "root.originValue", "root.targetValue")));

		diff.copyLeftToRight();
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(right, "targetValue");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertSame(targetNode, originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoChange2WayRtL() throws IOException {
		final Resource left = input.getReferenceMonoChangeLeft();
		final Resource right = input.getReferenceMonoChangeRight();

		Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedReference("root.origin", featureName, "root.originValue", "root.targetValue")));

		diff.copyRightToLeft();
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(left, "originValue");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertSame(targetNode, originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoChange3WayLeftChangeLtR() throws IOException {
		final Resource left = input.getReferenceMonoChangeLeft();
		final Resource right = input.getReferenceMonoChangeRight();
		final Resource origin = input.getReferenceMonoChangeOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedReference("root.origin", featureName, "root.originValue", "root.targetValue")));

		diff.copyLeftToRight();
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(right, "targetValue");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertSame(targetNode, originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoChange3WayLeftChangeRtL() throws IOException {
		final Resource left = input.getReferenceMonoChangeLeft();
		final Resource right = input.getReferenceMonoChangeRight();
		final Resource origin = input.getReferenceMonoChangeOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedReference("root.origin", featureName, "root.originValue", "root.targetValue")));

		diff.copyRightToLeft();
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(left, "originValue");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertSame(targetNode, originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoChange3WayRightChangeLtR() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoChangeRight();
		final Resource right = input.getReferenceMonoChangeLeft();
		final Resource origin = input.getReferenceMonoChangeOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedReference("root.origin", featureName, "root.originValue", "root.targetValue")));

		diff.copyLeftToRight();
		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(right, "originValue");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertSame(targetNode, originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	@Test
	public void testReferenceMonoChange3WayRightChangeRtL() throws IOException {
		// In order to have changes on the right side, we'll invert right and left
		final Resource left = input.getReferenceMonoChangeRight();
		final Resource right = input.getReferenceMonoChangeLeft();
		final Resource origin = input.getReferenceMonoChangeOrigin();

		Comparison comparison = EMFCompare.compare(left, right, origin);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedReference";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
				changedReference("root.origin", featureName, "root.originValue", "root.targetValue")));

		diff.copyRightToLeft();
		final EObject originNode = getNodeNamed(left, "origin");
		assertNotNull(originNode);
		final EObject targetNode = getNodeNamed(left, "targetValue");
		assertNotNull(targetNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertSame(targetNode, originNode.eGet(feature));

		comparison = EMFCompare.compare(left, right);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
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
		assertSame(Integer.valueOf(size), Integer.valueOf(list2.size()));

		for (int i = 0; i < size; i++) {
			final Object object1 = list1.get(i);
			final Object object2 = list2.get(i);
			assertEquals(object1, object2);
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
