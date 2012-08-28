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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

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

	// @Test
	// public void testReferenceMonoChange2WayRtL() throws IOException {
	// final Resource left = input.getReferenceMonoChangeLeft();
	// final Resource right = input.getReferenceMonoChangeRight();
	//
	// Comparison comparison = EMFCompare.compare(left, right);
	//
	// final List<Diff> differences = comparison.getDifferences();
	// assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));
	//
	// final String featureName = "singleValuedReference";
	// final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
	// changedReference("root.origin", featureName, "root.originValue", "root.targetValue")));
	//
	// diff.copyRightToLeft();
	// final EObject originNode = getNodeNamed(left, "origin");
	// assertNotNull(originNode);
	// final EObject targetNode = getNodeNamed(left, "originValue");
	// assertNotNull(targetNode);
	// final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
	// assertNotNull(feature);
	//
	// assertSame(targetNode, originNode.eGet(feature));
	//
	// comparison = EMFCompare.compare(left, right);
	// assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	// }
	//
	// @Test
	// public void testReferenceMonoChange3WayLeftChangeLtR() throws IOException {
	// final Resource left = input.getReferenceMonoChangeLeft();
	// final Resource right = input.getReferenceMonoChangeRight();
	// final Resource origin = input.getReferenceMonoChangeOrigin();
	//
	// Comparison comparison = EMFCompare.compare(left, right, origin);
	//
	// final List<Diff> differences = comparison.getDifferences();
	// assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));
	//
	// final String featureName = "singleValuedReference";
	// final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
	// changedReference("root.origin", featureName, "root.originValue", "root.targetValue")));
	//
	// diff.copyLeftToRight();
	// final EObject originNode = getNodeNamed(right, "origin");
	// assertNotNull(originNode);
	// final EObject targetNode = getNodeNamed(right, "targetValue");
	// assertNotNull(targetNode);
	// final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
	// assertNotNull(feature);
	//
	// assertSame(targetNode, originNode.eGet(feature));
	//
	// comparison = EMFCompare.compare(left, right);
	// assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	// }
	//
	// @Test
	// public void testReferenceMonoChange3WayLeftChangeRtL() throws IOException {
	// final Resource left = input.getReferenceMonoChangeLeft();
	// final Resource right = input.getReferenceMonoChangeRight();
	// final Resource origin = input.getReferenceMonoChangeOrigin();
	//
	// Comparison comparison = EMFCompare.compare(left, right, origin);
	//
	// final List<Diff> differences = comparison.getDifferences();
	// assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));
	//
	// final String featureName = "singleValuedReference";
	// final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
	// changedReference("root.origin", featureName, "root.originValue", "root.targetValue")));
	//
	// diff.copyRightToLeft();
	// final EObject originNode = getNodeNamed(left, "origin");
	// assertNotNull(originNode);
	// final EObject targetNode = getNodeNamed(left, "originValue");
	// assertNotNull(targetNode);
	// final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
	// assertNotNull(feature);
	//
	// assertSame(targetNode, originNode.eGet(feature));
	//
	// comparison = EMFCompare.compare(left, right);
	// assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	// }
	//
	// @Test
	// public void testReferenceMonoChange3WayRightChangeLtR() throws IOException {
	// // In order to have changes on the right side, we'll invert right and left
	// final Resource left = input.getReferenceMonoChangeRight();
	// final Resource right = input.getReferenceMonoChangeLeft();
	// final Resource origin = input.getReferenceMonoChangeOrigin();
	//
	// Comparison comparison = EMFCompare.compare(left, right, origin);
	//
	// final List<Diff> differences = comparison.getDifferences();
	// assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));
	//
	// final String featureName = "singleValuedReference";
	// final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
	// changedReference("root.origin", featureName, "root.originValue", "root.targetValue")));
	//
	// diff.copyLeftToRight();
	// final EObject originNode = getNodeNamed(right, "origin");
	// assertNotNull(originNode);
	// final EObject targetNode = getNodeNamed(right, "originValue");
	// assertNotNull(targetNode);
	// final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
	// assertNotNull(feature);
	//
	// assertSame(targetNode, originNode.eGet(feature));
	//
	// comparison = EMFCompare.compare(left, right);
	// assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	// }
	//
	// @Test
	// public void testReferenceMonoChange3WayRightChangeRtL() throws IOException {
	// // In order to have changes on the right side, we'll invert right and left
	// final Resource left = input.getReferenceMonoChangeRight();
	// final Resource right = input.getReferenceMonoChangeLeft();
	// final Resource origin = input.getReferenceMonoChangeOrigin();
	//
	// Comparison comparison = EMFCompare.compare(left, right, origin);
	//
	// final List<Diff> differences = comparison.getDifferences();
	// assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));
	//
	// final String featureName = "singleValuedReference";
	// final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.RIGHT),
	// changedReference("root.origin", featureName, "root.originValue", "root.targetValue")));
	//
	// diff.copyRightToLeft();
	// final EObject originNode = getNodeNamed(left, "origin");
	// assertNotNull(originNode);
	// final EObject targetNode = getNodeNamed(left, "targetValue");
	// assertNotNull(targetNode);
	// final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
	// assertNotNull(feature);
	//
	// assertSame(targetNode, originNode.eGet(feature));
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
