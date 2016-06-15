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
import static com.google.common.base.Predicates.not;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.merge.data.IndividualDiffInputData;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodeSingleValueAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

@SuppressWarnings("nls")
public class PseudoConflictMergeTest {

	private IndividualDiffInputData input = new IndividualDiffInputData();

	private final IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	@Test
	public void testPseudoAdd_LtR_1() throws IOException {
		// Pseudo Conflict between Node A[containmentRef1 add] from left side and Node A[containmentRef1 add]
		// from right side

		final Resource left = input.getLeftPseudoConflictAddScope();
		final Resource right = input.getRightPseudoConflictAddScope();
		final Resource origin = input.getOriginPseudoConflictAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.ADD)));

		// Merge -> Node A[containmentRef1 add] from left to right : <- Node A[containmentRef1 add] will be
		// merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final String featureName = "containmentRef1";
		final EObject leftNodeRoot = getNodeNamed(left, "root");
		assertNotNull(leftNodeRoot);
		final EObject rightNodeRoot = getNodeNamed(right, "root");
		assertNotNull(rightNodeRoot);
		final EObject leftNodeA = getNodeNamed(left, "A");
		assertNotNull(leftNodeA);
		final EObject rightNodeA = getNodeNamed(right, "A");
		assertNotNull(rightNodeA);
		final EStructuralFeature feature = rightNodeRoot.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<Node> containmentRef1 = ((Node)rightNodeRoot).getContainmentRef1();
		assertFalse(containmentRef1.isEmpty());

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.PSEUDO)));

	}

	@Test
	public void testPseudoAdd_LtR_2() throws IOException {
		// Pseudo Conflict between Node A[containmentRef1 add] from left side and Node A[containmentRef1 add]
		// from right side

		final Resource left = input.getLeftPseudoConflictAddScope();
		final Resource right = input.getRightPseudoConflictAddScope();
		final Resource origin = input.getOriginPseudoConflictAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.ADD)));

		// Merge <- Node A[containmentRef1 add] from left to right : -> Node A[containmentRef1 add] will be
		// merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final String featureName = "containmentRef1";
		final EObject leftNodeRoot = getNodeNamed(left, "root");
		assertNotNull(leftNodeRoot);
		final EObject rightNodeRoot = getNodeNamed(right, "root");
		assertNotNull(rightNodeRoot);
		final EObject leftNodeA = getNodeNamed(left, "A");
		assertNotNull(leftNodeA);
		final EObject rightNodeA = getNodeNamed(right, "A");
		assertNotNull(rightNodeA);
		final EStructuralFeature feature = rightNodeRoot.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<Node> containmentRef1 = ((Node)rightNodeRoot).getContainmentRef1();
		assertFalse(containmentRef1.isEmpty());

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.PSEUDO)));

	}

	@Test
	public void testPseudoAdd_RtL_1() throws IOException {
		// Pseudo Conflict between Node A[containmentRef1 add] from left side and Node A[containmentRef1 add]
		// from right side

		final Resource left = input.getLeftPseudoConflictAddScope();
		final Resource right = input.getRightPseudoConflictAddScope();
		final Resource origin = input.getOriginPseudoConflictAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.ADD)));

		// Merge -> Node A[containmentRef1 add] from right to left : <- Node A[containmentRef1 add] will be
		// merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final String featureName = "containmentRef1";
		final EObject leftNodeRoot = getNodeNamed(left, "root");
		assertNotNull(leftNodeRoot);
		final EObject rightNodeRoot = getNodeNamed(right, "root");
		assertNotNull(rightNodeRoot);
		final EObject leftNodeA = getNodeNamed(left, "A");
		assertNotNull(leftNodeA);
		final EObject rightNodeA = getNodeNamed(right, "A");
		assertNotNull(rightNodeA);
		final EStructuralFeature feature = leftNodeRoot.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<Node> containmentRef1 = ((Node)leftNodeRoot).getContainmentRef1();
		assertFalse(containmentRef1.isEmpty());

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.PSEUDO)));

	}

	@Test
	public void testPseudoAdd_RtL_2() throws IOException {
		// Pseudo Conflict between Node A[containmentRef1 add] from left side and Node A[containmentRef1 add]
		// from right side

		final Resource left = input.getLeftPseudoConflictAddScope();
		final Resource right = input.getRightPseudoConflictAddScope();
		final Resource origin = input.getOriginPseudoConflictAddScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.ADD)));

		// Merge <- Node A[containmentRef1 add] from right to left : -> Node A[containmentRef1 add] will be
		// merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final String featureName = "containmentRef1";
		final EObject leftNodeRoot = getNodeNamed(left, "root");
		assertNotNull(leftNodeRoot);
		final EObject rightNodeRoot = getNodeNamed(right, "root");
		assertNotNull(rightNodeRoot);
		final EObject leftNodeA = getNodeNamed(left, "A");
		assertNotNull(leftNodeA);
		final EObject rightNodeA = getNodeNamed(right, "A");
		assertNotNull(rightNodeA);
		final EStructuralFeature feature = leftNodeRoot.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<Node> containmentRef1 = ((Node)leftNodeRoot).getContainmentRef1();
		assertFalse(containmentRef1.isEmpty());

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.PSEUDO)));

	}

	@Test
	public void testPseudoDelete_LtR_1() throws IOException {
		// Pseudo Conflict between Node B[containmentRef1 delete] from left side and Node B[containmentRef1
		// delete] from right side

		final Resource left = input.getLeftPseudoConflictDeleteScope();
		final Resource right = input.getRightPseudoConflictDeleteScope();
		final Resource origin = input.getOriginPseudoConflictDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.DELETE)));

		// Merge -> Node B[containmentRef1 delete] from left to right : <- Node B[containmentRef1 delete] will
		// be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final String featureName = "containmentRef1";
		final EObject leftNodeRoot = getNodeNamed(left, "root");
		assertNotNull(leftNodeRoot);
		final EObject rightNodeRoot = getNodeNamed(right, "root");
		assertNotNull(rightNodeRoot);
		final EObject leftNodeB = getNodeNamed(left, "B");
		assertNull(leftNodeB);
		final EObject rightNodeB = getNodeNamed(right, "B");
		assertNull(rightNodeB);
		final EStructuralFeature feature = rightNodeRoot.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<Node> containmentRef1 = ((Node)rightNodeRoot).getContainmentRef1();
		assertTrue(containmentRef1.isEmpty());

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.PSEUDO)));

	}

	@Test
	public void testPseudoDelete_LtR_2() throws IOException {
		// Pseudo Conflict between Node B[containmentRef1 delete] from left side and Node B[containmentRef1
		// delete] from right side

		final Resource left = input.getLeftPseudoConflictDeleteScope();
		final Resource right = input.getRightPseudoConflictDeleteScope();
		final Resource origin = input.getOriginPseudoConflictDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.DELETE)));

		// Merge <- Node B[containmentRef1 delete] from left to right : -> Node B[containmentRef1 delete] will
		// be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final String featureName = "containmentRef1";
		final EObject leftNodeRoot = getNodeNamed(left, "root");
		assertNotNull(leftNodeRoot);
		final EObject rightNodeRoot = getNodeNamed(right, "root");
		assertNotNull(rightNodeRoot);
		final EObject leftNodeB = getNodeNamed(left, "B");
		assertNull(leftNodeB);
		final EObject rightNodeB = getNodeNamed(right, "B");
		assertNull(rightNodeB);
		final EStructuralFeature feature = rightNodeRoot.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<Node> containmentRef1 = ((Node)rightNodeRoot).getContainmentRef1();
		assertTrue(containmentRef1.isEmpty());

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.PSEUDO)));

	}

	@Test
	public void testPseudoDelete_RtL_1() throws IOException {
		// Pseudo Conflict between Node B[containmentRef1 delete] from left side and Node B[containmentRef1
		// delete] from right side

		final Resource left = input.getLeftPseudoConflictDeleteScope();
		final Resource right = input.getRightPseudoConflictDeleteScope();
		final Resource origin = input.getOriginPseudoConflictDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.DELETE)));

		// Merge -> Node B[containmentRef1 delete] from right to left : <- Node B[containmentRef1 delete] will
		// be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final String featureName = "containmentRef1";
		final EObject leftNodeRoot = getNodeNamed(left, "root");
		assertNotNull(leftNodeRoot);
		final EObject rightNodeRoot = getNodeNamed(right, "root");
		assertNotNull(rightNodeRoot);
		final EObject leftNodeB = getNodeNamed(left, "B");
		assertNull(leftNodeB);
		final EObject rightNodeB = getNodeNamed(right, "B");
		assertNull(rightNodeB);
		final EStructuralFeature feature = leftNodeRoot.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<Node> containmentRef1 = ((Node)leftNodeRoot).getContainmentRef1();
		assertTrue(containmentRef1.isEmpty());

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.PSEUDO)));

	}

	@Test
	public void testPseudoDelete_RtL_2() throws IOException {
		// Pseudo Conflict between Node B[containmentRef1 delete] from left side and Node B[containmentRef1
		// delete] from right side

		final Resource left = input.getLeftPseudoConflictDeleteScope();
		final Resource right = input.getRightPseudoConflictDeleteScope();
		final Resource origin = input.getOriginPseudoConflictDeleteScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.DELETE)));

		// Merge <- Node B[containmentRef1 delete] from right to left : -> Node B[containmentRef1 delete] will
		// be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final String featureName = "containmentRef1";
		final EObject leftNodeRoot = getNodeNamed(left, "root");
		assertNotNull(leftNodeRoot);
		final EObject rightNodeRoot = getNodeNamed(right, "root");
		assertNotNull(rightNodeRoot);
		final EObject leftNodeB = getNodeNamed(left, "B");
		assertNull(leftNodeB);
		final EObject rightNodeB = getNodeNamed(right, "B");
		assertNull(rightNodeB);
		final EStructuralFeature feature = leftNodeRoot.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<Node> containmentRef1 = ((Node)leftNodeRoot).getContainmentRef1();
		assertTrue(containmentRef1.isEmpty());

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.PSEUDO)));

	}

	@Test
	public void testPseudoChange_LtR_1() throws IOException {
		// Pseudo Conflict between Hello[singleValuedAttribute changed] from left side and
		// Hello[singleValuedAttribute changed] from right side

		final Resource left = input.getLeftPseudoConflictChangeScope();
		final Resource right = input.getRightPseudoConflictChangeScope();
		final Resource origin = input.getOriginPseudoConflictChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.CHANGE)));

		// Merge -> Hello[singleValuedAttribute changed] from left to right : <- Hello[singleValuedAttribute
		// changed] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final String featureName = "singleValuedAttribute";
		final EObject leftNodeD = getNodeNamed(left, "D");
		assertNotNull(leftNodeD);
		final EObject rightNodeD = getNodeNamed(right, "D");
		assertNotNull(rightNodeD);
		final EStructuralFeature featureRight = rightNodeD.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureRight);
		final EStructuralFeature featureLeft = leftNodeD.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureLeft);
		final String singleValuedAttributeRight = ((NodeSingleValueAttribute)rightNodeD)
				.getSingleValuedAttribute();
		assertEquals("GoodBye", singleValuedAttributeRight);
		final String singleValuedAttributeLeft = ((NodeSingleValueAttribute)leftNodeD)
				.getSingleValuedAttribute();
		assertEquals("GoodBye", singleValuedAttributeLeft);

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.PSEUDO)));

	}

	@Test
	public void testPseudoChange_LtR_2() throws IOException {
		// Pseudo Conflict between Hello[singleValuedAttribute changed] from left side and
		// Hello[singleValuedAttribute changed] from right side

		final Resource left = input.getLeftPseudoConflictChangeScope();
		final Resource right = input.getRightPseudoConflictChangeScope();
		final Resource origin = input.getOriginPseudoConflictChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.CHANGE)));

		// Merge <- Hello[singleValuedAttribute changed] from left to right : -> Hello[singleValuedAttribute
		// changed] will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final String featureName = "singleValuedAttribute";
		final EObject leftNodeD = getNodeNamed(left, "D");
		assertNotNull(leftNodeD);
		final EObject rightNodeD = getNodeNamed(right, "D");
		assertNotNull(rightNodeD);
		final EStructuralFeature featureRight = rightNodeD.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureRight);
		final EStructuralFeature featureLeft = leftNodeD.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureLeft);
		final String singleValuedAttributeRight = ((NodeSingleValueAttribute)rightNodeD)
				.getSingleValuedAttribute();
		assertEquals("GoodBye", singleValuedAttributeRight);
		final String singleValuedAttributeLeft = ((NodeSingleValueAttribute)leftNodeD)
				.getSingleValuedAttribute();
		assertEquals("GoodBye", singleValuedAttributeLeft);

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.PSEUDO)));

	}

	@Test
	public void testPseudoChange_RtL_1() throws IOException {
		// Pseudo Conflict between Hello[singleValuedAttribute changed] from left side and
		// Hello[singleValuedAttribute changed] from right side

		final Resource left = input.getLeftPseudoConflictChangeScope();
		final Resource right = input.getRightPseudoConflictChangeScope();
		final Resource origin = input.getOriginPseudoConflictChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.CHANGE)));

		// Merge -> Hello[singleValuedAttribute changed] from right to left : <- Hello[singleValuedAttribute
		// changed] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final String featureName = "singleValuedAttribute";
		final EObject leftNodeD = getNodeNamed(left, "D");
		assertNotNull(leftNodeD);
		final EObject rightNodeD = getNodeNamed(right, "D");
		assertNotNull(rightNodeD);
		final EStructuralFeature featureRight = rightNodeD.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureRight);
		final EStructuralFeature featureLeft = leftNodeD.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureLeft);
		final String singleValuedAttributeRight = ((NodeSingleValueAttribute)rightNodeD)
				.getSingleValuedAttribute();
		assertEquals("GoodBye", singleValuedAttributeRight);
		final String singleValuedAttributeLeft = ((NodeSingleValueAttribute)leftNodeD)
				.getSingleValuedAttribute();
		assertEquals("GoodBye", singleValuedAttributeLeft);

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.PSEUDO)));

	}

	@Test
	public void testPseudoChange_RtL_2() throws IOException {
		// Pseudo Conflict between Hello[singleValuedAttribute changed] from left side and
		// Hello[singleValuedAttribute changed] from right side

		final Resource left = input.getLeftPseudoConflictChangeScope();
		final Resource right = input.getRightPseudoConflictChangeScope();
		final Resource origin = input.getOriginPseudoConflictChangeScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.CHANGE)));

		// Merge -> Hello[singleValuedAttribute changed] from right to left : <- Hello[singleValuedAttribute
		// changed] will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final String featureName = "singleValuedAttribute";
		final EObject leftNodeD = getNodeNamed(left, "D");
		assertNotNull(leftNodeD);
		final EObject rightNodeD = getNodeNamed(right, "D");
		assertNotNull(rightNodeD);
		final EStructuralFeature featureRight = rightNodeD.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureRight);
		final EStructuralFeature featureLeft = leftNodeD.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureLeft);
		final String singleValuedAttributeRight = ((NodeSingleValueAttribute)rightNodeD)
				.getSingleValuedAttribute();
		assertEquals("GoodBye", singleValuedAttributeRight);
		final String singleValuedAttributeLeft = ((NodeSingleValueAttribute)leftNodeD)
				.getSingleValuedAttribute();
		assertEquals("GoodBye", singleValuedAttributeLeft);

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.PSEUDO)));

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
