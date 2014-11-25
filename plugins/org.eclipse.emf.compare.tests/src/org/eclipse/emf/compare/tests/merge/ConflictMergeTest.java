/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
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
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasState;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.referenceValueMatch;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.merge.data.IndividualDiffInputData;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

@SuppressWarnings("nls")
public class ConflictMergeTest {

	private IndividualDiffInputData input = new IndividualDiffInputData();

	private final IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	@SuppressWarnings("unchecked")
	@Test
	public void testLeftAddRightAdd_LtR_1() throws IOException {
		// Conflict between C[containmentRef1 add] from left side and C[containmentRef1 add] from right side

		final Resource left = input.getLeftAddRightAddLeftConflictScope();
		final Resource right = input.getLeftAddRightAddRightConflictScope();
		final Resource origin = input.getLeftAddRightAddOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(6, differences.size());

		final String featureName = "containmentRef1";

		final Diff diffNodeCLeft = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.ADD), referenceValueMatch(featureName,
						"root.A.C", true)));
		final Diff diffNodeCRight = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.ADD), referenceValueMatch(
						featureName, "root.B.C", true)));
		final Diff diffNodeDLeft = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.ADD), referenceValueMatch(featureName,
						"root.A.C.D", true)));
		final Diff diffNodeDRight = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.ADD), referenceValueMatch(
						featureName, "root.B.C.D", true)));
		final Diff diffNodeELeft = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.ADD), referenceValueMatch(featureName,
						"root.A.C.E", true)));
		final Diff diffNodeFRight = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.ADD), referenceValueMatch(
						featureName, "root.B.C.F", true)));

		// Merge C[containmentRef1 add] from left side from left to right : C[containmentRef1 add] from right
		// side will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diffNodeCLeft).copyLeftToRight(diffNodeCLeft,
				new BasicMonitor());

		final EObject rightNodeC = getNodeNamed(right, "C");
		assertNotNull(rightNodeC);
		final EObject rightNodeA = getNodeNamed(right, "A");
		assertNotNull(rightNodeA);
		final EObject rightNodeB = getNodeNamed(right, "B");
		assertNotNull(rightNodeB);
		final EObject rightNodeD = getNodeNamed(right, "D");
		assertNull(rightNodeD);
		final EObject rightNodeF = getNodeNamed(right, "F");
		assertNull(rightNodeF);
		final EStructuralFeature feature = rightNodeA.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<Node> containmentRef1s = ((Node)rightNodeA).getContainmentRef1();
		assertTrue(containmentRef1s.contains(rightNodeC));
		final EList<Node> bContainmentRef1s = ((Node)rightNodeB).getContainmentRef1();
		assertFalse(bContainmentRef1s.contains(rightNodeC));

		assertEquals(DifferenceState.MERGED, diffNodeCLeft.getState());
		assertEquals(DifferenceState.MERGED, diffNodeCRight.getState());
		assertEquals(DifferenceState.UNRESOLVED, diffNodeDLeft.getState());
		assertEquals(DifferenceState.MERGED, diffNodeDRight.getState());
		assertEquals(DifferenceState.UNRESOLVED, diffNodeELeft.getState());
		assertEquals(DifferenceState.MERGED, diffNodeFRight.getState());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testLeftAddRightAdd_LtR_2() throws IOException {
		// Conflict between C[containmentRef1 add] from left side and C[containmentRef1 add] from right side

		final Resource left = input.getLeftAddRightAddLeftConflictScope();
		final Resource right = input.getLeftAddRightAddRightConflictScope();
		final Resource origin = input.getLeftAddRightAddOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(6, differences.size());

		final String featureName = "containmentRef1";

		final Diff diffNodeCLeft = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.ADD), referenceValueMatch(featureName,
						"root.A.C", true)));
		final Diff diffNodeCRight = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.ADD), referenceValueMatch(
						featureName, "root.B.C", true)));
		final Diff diffNodeDLeft = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.ADD), referenceValueMatch(featureName,
						"root.A.C.D", true)));
		final Diff diffNodeDRight = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.ADD), referenceValueMatch(
						featureName, "root.B.C.D", true)));
		final Diff diffNodeELeft = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.ADD), referenceValueMatch(featureName,
						"root.A.C.E", true)));
		final Diff diffNodeFRight = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.ADD), referenceValueMatch(
						featureName, "root.B.C.F", true)));

		// Merge C[containmentRef1 add] from right side from left to right : C[containmentRef1 add] from left
		// side will not be merge
		mergerRegistry.getHighestRankingMerger(diffNodeCRight).copyLeftToRight(diffNodeCRight,
				new BasicMonitor());

		final EObject rightNodeC = getNodeNamed(right, "C");
		assertNull(rightNodeC);
		final EObject rightNodeA = getNodeNamed(right, "A");
		assertNotNull(rightNodeA);
		final EObject rightNodeB = getNodeNamed(right, "B");
		assertNotNull(rightNodeB);
		final EObject rightNodeD = getNodeNamed(right, "D");
		assertNull(rightNodeD);
		final EObject rightNodeF = getNodeNamed(right, "F");
		assertNull(rightNodeF);
		final EStructuralFeature feature = rightNodeA.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<Node> aContainmentRef1s = ((Node)rightNodeA).getContainmentRef1();
		assertFalse(aContainmentRef1s.contains(rightNodeC));
		final EList<Node> bContainmentRef1s = ((Node)rightNodeB).getContainmentRef1();
		assertFalse(bContainmentRef1s.contains(rightNodeC));

		assertEquals(DifferenceState.UNRESOLVED, diffNodeCLeft.getState());
		assertEquals(DifferenceState.MERGED, diffNodeCRight.getState());
		assertEquals(DifferenceState.UNRESOLVED, diffNodeDLeft.getState());
		assertEquals(DifferenceState.MERGED, diffNodeDRight.getState());
		assertEquals(DifferenceState.UNRESOLVED, diffNodeELeft.getState());
		assertEquals(DifferenceState.MERGED, diffNodeFRight.getState());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testLeftAddRightAdd_RtL_1() throws IOException {
		// Conflict between C[containmentRef1 add] from left side and C[containmentRef1 add] from right side

		final Resource left = input.getLeftAddRightAddLeftConflictScope();
		final Resource right = input.getLeftAddRightAddRightConflictScope();
		final Resource origin = input.getLeftAddRightAddOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(6, differences.size());

		final String featureName = "containmentRef1";

		final Diff diffNodeCLeft = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.ADD), referenceValueMatch(featureName,
						"root.A.C", true)));
		final Diff diffNodeCRight = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.ADD), referenceValueMatch(
						featureName, "root.B.C", true)));
		final Diff diffNodeDLeft = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.ADD), referenceValueMatch(featureName,
						"root.A.C.D", true)));
		final Diff diffNodeDRight = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.ADD), referenceValueMatch(
						featureName, "root.B.C.D", true)));
		final Diff diffNodeELeft = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.ADD), referenceValueMatch(featureName,
						"root.A.C.E", true)));
		final Diff diffNodeFRight = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.ADD), referenceValueMatch(
						featureName, "root.B.C.F", true)));

		// Merge C[containmentRef1 add] from left side from right to left : C[containmentRef1 add] from right
		// side will not be merge.
		mergerRegistry.getHighestRankingMerger(diffNodeCLeft).copyRightToLeft(diffNodeCLeft,
				new BasicMonitor());

		final EObject leftNodeC = getNodeNamed(left, "C");
		assertNull(leftNodeC);
		final EObject leftNodeA = getNodeNamed(left, "A");
		assertNotNull(leftNodeA);
		final EObject leftNodeB = getNodeNamed(left, "B");
		assertNotNull(leftNodeB);
		final EObject leftNodeD = getNodeNamed(left, "D");
		assertNull(leftNodeD);
		final EObject leftNodeF = getNodeNamed(left, "F");
		assertNull(leftNodeF);
		final EStructuralFeature feature = leftNodeA.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<Node> containmentRef1s = ((Node)leftNodeA).getContainmentRef1();
		assertFalse(containmentRef1s.contains(leftNodeC));
		final EList<Node> bContainmentRef1s = ((Node)leftNodeB).getContainmentRef1();
		assertFalse(bContainmentRef1s.contains(leftNodeC));

		assertEquals(DifferenceState.MERGED, diffNodeCLeft.getState());
		assertEquals(DifferenceState.UNRESOLVED, diffNodeCRight.getState());
		assertEquals(DifferenceState.MERGED, diffNodeDLeft.getState());
		assertEquals(DifferenceState.UNRESOLVED, diffNodeDRight.getState());
		assertEquals(DifferenceState.MERGED, diffNodeELeft.getState());
		assertEquals(DifferenceState.UNRESOLVED, diffNodeFRight.getState());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testLeftAddRightAdd_RtL_2() throws IOException {
		// Conflict between C[containmentRef1 add] from left side and C[containmentRef1 add] from right side

		final Resource left = input.getLeftAddRightAddLeftConflictScope();
		final Resource right = input.getLeftAddRightAddRightConflictScope();
		final Resource origin = input.getLeftAddRightAddOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(6, differences.size());

		final String featureName = "containmentRef1";

		final Diff diffNodeCLeft = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.ADD), referenceValueMatch(featureName,
						"root.A.C", true)));
		final Diff diffNodeCRight = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.ADD), referenceValueMatch(
						featureName, "root.B.C", true)));
		final Diff diffNodeDLeft = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.ADD), referenceValueMatch(featureName,
						"root.A.C.D", true)));
		final Diff diffNodeDRight = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.ADD), referenceValueMatch(
						featureName, "root.B.C.D", true)));
		final Diff diffNodeELeft = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.ADD), referenceValueMatch(featureName,
						"root.A.C.E", true)));
		final Diff diffNodeFRight = Iterators.find(differences.iterator(), and(
				fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.ADD), referenceValueMatch(
						featureName, "root.B.C.F", true)));

		// Merge C[containmentRef1 add] from right side from right to left : C[containmentRef1 add] from left
		// side will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diffNodeCRight).copyRightToLeft(diffNodeCRight,
				new BasicMonitor());

		final EObject leftNodeC = getNodeNamed(left, "C");
		assertNotNull(leftNodeC);
		final EObject leftNodeA = getNodeNamed(left, "A");
		assertNotNull(leftNodeA);
		final EObject leftNodeB = getNodeNamed(left, "B");
		assertNotNull(leftNodeB);
		final EObject leftNodeD = getNodeNamed(left, "D");
		assertNull(leftNodeD);
		final EObject leftNodeF = getNodeNamed(left, "F");
		assertNull(leftNodeF);
		final EStructuralFeature feature = leftNodeA.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<Node> containmentRef1s = ((Node)leftNodeA).getContainmentRef1();
		assertFalse(containmentRef1s.contains(leftNodeC));
		final EList<Node> bContainmentRef1s = ((Node)leftNodeB).getContainmentRef1();
		assertTrue(bContainmentRef1s.contains(leftNodeC));

		assertEquals(DifferenceState.MERGED, diffNodeCLeft.getState());
		assertEquals(DifferenceState.MERGED, diffNodeCRight.getState());
		assertEquals(DifferenceState.MERGED, diffNodeDLeft.getState());
		assertEquals(DifferenceState.UNRESOLVED, diffNodeDRight.getState());
		assertEquals(DifferenceState.MERGED, diffNodeELeft.getState());
		assertEquals(DifferenceState.UNRESOLVED, diffNodeFRight.getState());
	}

	@Test
	public void testLeftAddRightDelete_LtR_1() throws IOException {
		// Conflict between B[eSuperTypes add] from left side and B[eClassifiers delete] from right side

		final Resource left = input.getLeftAddRightDeleteLeftConflictScope();
		final Resource right = input.getLeftAddRightDeleteRightConflictScope();
		final Resource origin = input.getLeftAddRightDeleteOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.LEFT));

		// Merge B[eSuperTypes add] from left to right : B[eClassifiers delete] will be merge from left to
		// right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final String featureName = "eSuperTypes";
		final EObject rightEClassB = getNodeNamed(right, "B");
		assertNotNull(rightEClassB);
		final EObject rightEClassA = getNodeNamed(right, "A");
		assertNotNull(rightEClassA);
		final EStructuralFeature feature = rightEClassA.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<EClass> eSuperTypes = ((EClass)rightEClassA).getESuperTypes();
		assertTrue(eSuperTypes.contains(rightEClassB));

		assertFalse(Iterators.any(differences.iterator(), not(hasState(DifferenceState.MERGED))));
	}

	@Test
	public void testLeftAddRightDelete_LtR_2() throws IOException {
		// Conflict between B[eSuperTypes add] from left side and B[eClassifiers delete] from right side

		final Resource left = input.getLeftAddRightDeleteLeftConflictScope();
		final Resource right = input.getLeftAddRightDeleteRightConflictScope();
		final Resource origin = input.getLeftAddRightDeleteOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.RIGHT));

		// Merge B[eClassifiers delete] from left to right : B[eSuperTypes add] will not be merge.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final String featureName = "eSuperTypes";
		final EObject rightEClassB = getNodeNamed(right, "B");
		assertNotNull(rightEClassB);
		final EObject rightEClassA = getNodeNamed(right, "A");
		assertNotNull(rightEClassA);
		final EStructuralFeature feature = rightEClassA.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<EClass> eSuperTypes = ((EClass)rightEClassA).getESuperTypes();
		assertTrue(eSuperTypes.isEmpty());

		for (Diff currentDiff : differences) {
			if (currentDiff.getSource() == DifferenceSource.RIGHT) {
				assertTrue(currentDiff.getState() == DifferenceState.MERGED);
			} else {
				assertTrue(currentDiff.getState() == DifferenceState.UNRESOLVED);
			}
		}
	}

	@Test
	public void testLeftAddRightDelete_RtL_1() throws IOException {
		// Conflict between B[eSuperTypes add] from left side and B[eClassifiers delete] from right side

		final Resource left = input.getLeftAddRightDeleteLeftConflictScope();
		final Resource right = input.getLeftAddRightDeleteRightConflictScope();
		final Resource origin = input.getLeftAddRightDeleteOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.LEFT));

		// Merge B[eSuperTypes add] from right to left : B[eClassifiers delete] will not be merge
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final String featureName = "eSuperTypes";
		final EObject leftEClassB = getNodeNamed(left, "B");
		assertNotNull(leftEClassB);
		final EObject leftEClassA = getNodeNamed(left, "A");
		assertNotNull(leftEClassA);
		final EStructuralFeature feature = leftEClassA.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<EClass> eSuperTypes = ((EClass)leftEClassA).getESuperTypes();
		assertTrue(eSuperTypes.isEmpty());

		for (Diff currentDiff : differences) {
			if (currentDiff.getSource() == DifferenceSource.LEFT) {
				assertTrue(currentDiff.getState() == DifferenceState.MERGED);
			} else {
				assertTrue(currentDiff.getState() == DifferenceState.UNRESOLVED);
			}
		}
	}

	@Test
	public void testLeftAddRightDelete_RtL_2() throws IOException {
		// Conflict between B[eSuperTypes add] from left side and B[eClassifiers delete] from right side

		final Resource left = input.getLeftAddRightDeleteLeftConflictScope();
		final Resource right = input.getLeftAddRightDeleteRightConflictScope();
		final Resource origin = input.getLeftAddRightDeleteOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.RIGHT));

		// Merge B[eClassifiers delete] from right to left : B[eSuperTypes add] will be merge from right to
		// left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final String featureName = "eSuperTypes";
		final EObject leftEClassB = getNodeNamed(left, "B");
		assertNull(leftEClassB);
		final EObject leftEClassA = getNodeNamed(left, "A");
		assertNotNull(leftEClassA);
		final EStructuralFeature feature = leftEClassA.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<EClass> eSuperTypes = ((EClass)leftEClassA).getESuperTypes();
		assertTrue(eSuperTypes.isEmpty());

		assertFalse(Iterators.any(differences.iterator(), not(hasState(DifferenceState.MERGED))));
	}

	@Test
	public void testLeftDeleteRightAdd_LtR_1() throws IOException {
		// Conflict between B[eClassifiers delete] from left side and B[eSuperTypes add] from right side

		final Resource left = input.getLeftDeleteRightAddLeftConflictScope();
		final Resource right = input.getLeftDeleteRightAddRightConflictScope();
		final Resource origin = input.getLeftDeleteRightAddOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.LEFT));

		// Merge B[eClassifiers delete] from left to right : B[eSuperTypes add] will be merge from left to
		// right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final String featureName = "eSuperTypes";
		final EObject rightEClassB = getNodeNamed(right, "B");
		assertNull(rightEClassB);
		final EObject rightEClassA = getNodeNamed(right, "A");
		assertNotNull(rightEClassA);
		final EStructuralFeature feature = rightEClassA.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<EClass> eSuperTypes = ((EClass)rightEClassA).getESuperTypes();
		assertTrue(eSuperTypes.isEmpty());

		assertFalse(Iterators.any(differences.iterator(), not(hasState(DifferenceState.MERGED))));
	}

	@Test
	public void testLeftDeleteRightAdd_LtR_2() throws IOException {
		// Conflict between B[eClassifiers delete] from left side and B[eSuperTypes add] from right side

		final Resource left = input.getLeftDeleteRightAddLeftConflictScope();
		final Resource right = input.getLeftDeleteRightAddRightConflictScope();
		final Resource origin = input.getLeftDeleteRightAddOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.RIGHT));

		// Merge B[eSuperTypes add] from left to right : B[eClassifiers delete] will not be merge.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final String featureName = "eSuperTypes";
		final EObject rightEClassB = getNodeNamed(right, "B");
		assertNotNull(rightEClassB);
		final EObject rightEClassA = getNodeNamed(right, "A");
		assertNotNull(rightEClassA);
		final EStructuralFeature feature = rightEClassA.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<EClass> eSuperTypes = ((EClass)rightEClassA).getESuperTypes();
		assertTrue(eSuperTypes.isEmpty());

		for (Diff currentDiff : differences) {
			if (currentDiff.getSource() == DifferenceSource.RIGHT) {
				assertTrue(currentDiff.getState() == DifferenceState.MERGED);
			} else {
				assertTrue(currentDiff.getState() == DifferenceState.UNRESOLVED);
			}
		}
	}

	@Test
	public void testLeftDeleteRightAdd_RtL_1() throws IOException {
		// Conflict between B[eClassifiers delete] from left side and B[eSuperTypes add] from right side

		final Resource left = input.getLeftDeleteRightAddLeftConflictScope();
		final Resource right = input.getLeftDeleteRightAddRightConflictScope();
		final Resource origin = input.getLeftDeleteRightAddOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.LEFT));

		// Merge B[eClassifiers delete] from right to left : B[eSuperTypes add] will not be merge.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final String featureName = "eSuperTypes";
		final EObject leftEClassB = getNodeNamed(left, "B");
		assertNotNull(leftEClassB);
		final EObject leftEClassA = getNodeNamed(left, "A");
		assertNotNull(leftEClassA);
		final EStructuralFeature feature = leftEClassA.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<EClass> eSuperTypes = ((EClass)leftEClassA).getESuperTypes();
		assertTrue(eSuperTypes.isEmpty());

		for (Diff currentDiff : differences) {
			if (currentDiff.getSource() == DifferenceSource.LEFT) {
				assertTrue(currentDiff.getState() == DifferenceState.MERGED);
			} else {
				assertTrue(currentDiff.getState() == DifferenceState.UNRESOLVED);
			}
		}
	}

	@Test
	public void testLeftDeleteRightAdd_RtL_2() throws IOException {
		// Conflict between B[eClassifiers delete] from left side and B[eSuperTypes add] from right side

		final Resource left = input.getLeftDeleteRightAddLeftConflictScope();
		final Resource right = input.getLeftDeleteRightAddRightConflictScope();
		final Resource origin = input.getLeftDeleteRightAddOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.RIGHT));

		// Merge B[eSuperTypes add] from right to left : B[eClassifiers delete] will be merge from right to
		// left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final String featureName = "eSuperTypes";
		final EObject leftEClassB = getNodeNamed(left, "B");
		assertNotNull(leftEClassB);
		final EObject leftEClassA = getNodeNamed(left, "A");
		assertNotNull(leftEClassA);
		final EStructuralFeature feature = leftEClassA.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EList<EClass> eSuperTypes = ((EClass)leftEClassA).getESuperTypes();
		assertTrue(eSuperTypes.contains(leftEClassB));

		assertFalse(Iterators.any(differences.iterator(), not(hasState(DifferenceState.MERGED))));
	}

	@Test
	public void testLeftDeleteRightSet_LtR_1() throws IOException {
		// Conflict between A[eClassifiers delete] from left side and true[abstract set]/true[interface set]
		// from right side

		final Resource left = input.getLeftDeleteRightSetLeftConflictScope();
		final Resource right = input.getLeftDeleteRightSetRightConflictScope();
		final Resource origin = input.getLeftDeleteRightSetOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.LEFT));

		// Merge A[eClassifiers delete] from left to right : true[abstract set]/true[interface set] will be
		// merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final EObject rightEClassA = getNodeNamed(right, "A");
		assertNull(rightEClassA);

		assertFalse(Iterators.any(differences.iterator(), not(hasState(DifferenceState.MERGED))));
	}

	@Test
	public void testLeftDeleteRightSet_LtR_2() throws IOException {
		// Conflict between A[eClassifiers delete] from left side and true[abstract set]/true[interface set]
		// from right side

		final Resource left = input.getLeftDeleteRightSetLeftConflictScope();
		final Resource right = input.getLeftDeleteRightSetRightConflictScope();
		final Resource origin = input.getLeftDeleteRightSetOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		final UnmodifiableIterator<Diff> diffs = Iterators.filter(differences.iterator(),
				fromSide(DifferenceSource.RIGHT));

		// Merge true[abstract set]/true[interface set] from left to right : A[eClassifiers delete] will not
		// be merge.
		Diff abstract_ = diffs.next();
		mergerRegistry.getHighestRankingMerger(abstract_).copyLeftToRight(abstract_, new BasicMonitor());

		final EObject rightEClassA = getNodeNamed(right, "A");
		assertNotNull(rightEClassA);
		assertFalse(((EClass)rightEClassA).isAbstract());
		assertTrue(((EClass)rightEClassA).isInterface());

		Diff interface_ = diffs.next();
		mergerRegistry.getHighestRankingMerger(interface_).copyLeftToRight(interface_, new BasicMonitor());

		assertNotNull(rightEClassA);
		assertFalse(((EClass)rightEClassA).isAbstract());
		assertFalse(((EClass)rightEClassA).isInterface());

		for (Diff currentDiff : differences) {
			if (currentDiff.getSource() == DifferenceSource.RIGHT) {
				assertTrue(currentDiff.getState() == DifferenceState.MERGED);
			} else {
				assertTrue(currentDiff.getState() == DifferenceState.UNRESOLVED);
			}
		}
	}

	@Test
	public void testLeftDeleteRightSet_RtL_1() throws IOException {
		// Conflict between A[eClassifiers delete] from left side and true[abstract set]/true[interface set]
		// from right side

		final Resource left = input.getLeftDeleteRightSetLeftConflictScope();
		final Resource right = input.getLeftDeleteRightSetRightConflictScope();
		final Resource origin = input.getLeftDeleteRightSetOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.LEFT));

		// Merge A[eClassifiers delete] from right to left : true[abstract set]/true[interface set] will not
		// be merge.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final EObject leftEClassA = getNodeNamed(left, "A");
		assertNotNull(leftEClassA);
		assertFalse(((EClass)leftEClassA).isAbstract());
		assertFalse(((EClass)leftEClassA).isInterface());

		for (Diff currentDiff : differences) {
			if (currentDiff.getSource() == DifferenceSource.LEFT) {
				assertTrue(currentDiff.getState() == DifferenceState.MERGED);
			} else {
				assertTrue(currentDiff.getState() == DifferenceState.UNRESOLVED);
			}
		}
	}

	@Test
	public void testLeftDeleteRightSet_RtL_2() throws IOException {
		// Conflict between A[eClassifiers delete] from left side and true[abstract set]/true[interface set]
		// from right side

		final Resource left = input.getLeftDeleteRightSetLeftConflictScope();
		final Resource right = input.getLeftDeleteRightSetRightConflictScope();
		final Resource origin = input.getLeftDeleteRightSetOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		final UnmodifiableIterator<Diff> diffs = Iterators.filter(differences.iterator(),
				fromSide(DifferenceSource.RIGHT));

		// Merge true[abstract set]/true[interface set] from right to left : A[eClassifiers delete] will be
		// merge from right to left too.
		Diff abstract_ = diffs.next();
		mergerRegistry.getHighestRankingMerger(abstract_).copyRightToLeft(abstract_, new BasicMonitor());

		final EObject leftEClassA = getNodeNamed(left, "A");
		assertNotNull(leftEClassA);
		assertTrue(((EClass)leftEClassA).isAbstract());
		assertFalse(((EClass)leftEClassA).isInterface());

		Diff interface_ = diffs.next();
		mergerRegistry.getHighestRankingMerger(interface_).copyRightToLeft(interface_, new BasicMonitor());

		assertNotNull(leftEClassA);
		assertTrue(((EClass)leftEClassA).isAbstract());
		assertTrue(((EClass)leftEClassA).isInterface());

		assertFalse(Iterators.any(differences.iterator(), not(hasState(DifferenceState.MERGED))));
	}

	@Test
	public void testLeftSetRightDelete_LtR_1() throws IOException {
		// Conflict between true[abstract set]/true[interface set] from left side and A[eClassifiers delete]
		// from right side

		final Resource left = input.getLeftSetRightDeleteLeftConflictScope();
		final Resource right = input.getLeftSetRightDeleteRightConflictScope();
		final Resource origin = input.getLeftSetRightDeleteOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		final UnmodifiableIterator<Diff> diffs = Iterators.filter(differences.iterator(),
				fromSide(DifferenceSource.LEFT));

		// Merge true[abstract set]/true[interface set] from left to right : A[eClassifiers delete] will be
		// merge from left to right too.
		Diff abstract_ = diffs.next();
		mergerRegistry.getHighestRankingMerger(abstract_).copyLeftToRight(abstract_, new BasicMonitor());

		final EObject rightEClassA = getNodeNamed(right, "A");
		assertNotNull(rightEClassA);
		assertTrue(((EClass)rightEClassA).isAbstract());
		assertFalse(((EClass)rightEClassA).isInterface());

		Diff interface_ = diffs.next();
		mergerRegistry.getHighestRankingMerger(interface_).copyLeftToRight(interface_, new BasicMonitor());

		assertNotNull(rightEClassA);
		assertTrue(((EClass)rightEClassA).isAbstract());
		assertTrue(((EClass)rightEClassA).isInterface());

		assertFalse(Iterators.any(differences.iterator(), not(hasState(DifferenceState.MERGED))));
	}

	@Test
	public void testLeftSetRightDelete_LtR_2() throws IOException {
		// Conflict between true[abstract set]/true[interface set] from left side and A[eClassifiers delete]
		// from right side

		final Resource left = input.getLeftSetRightDeleteLeftConflictScope();
		final Resource right = input.getLeftSetRightDeleteRightConflictScope();
		final Resource origin = input.getLeftSetRightDeleteOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.RIGHT));

		// Merge A[eClassifiers delete] from left to right : true[abstract set]/true[interface set] will not
		// be merge.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final EObject rightEClassA = getNodeNamed(right, "A");
		assertNotNull(rightEClassA);
		assertFalse(((EClass)rightEClassA).isAbstract());
		assertFalse(((EClass)rightEClassA).isInterface());

		for (Diff currentDiff : differences) {
			if (currentDiff.getSource() == DifferenceSource.RIGHT) {
				assertTrue(currentDiff.getState() == DifferenceState.MERGED);
			} else {
				assertTrue(currentDiff.getState() == DifferenceState.UNRESOLVED);
			}
		}
	}

	@Test
	public void testLeftSetRightDelete_RtL_1() throws IOException {
		// Conflict between true[abstract set]/true[interface set] from left side and A[eClassifiers delete]
		// from right side

		final Resource left = input.getLeftSetRightDeleteLeftConflictScope();
		final Resource right = input.getLeftSetRightDeleteRightConflictScope();
		final Resource origin = input.getLeftSetRightDeleteOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		final UnmodifiableIterator<Diff> diffs = Iterators.filter(differences.iterator(),
				fromSide(DifferenceSource.LEFT));

		// Merge true[abstract set]/true[interface set] from right to left : A[eClassifiers delete] will not
		// be merge.
		Diff abstract_ = diffs.next();
		mergerRegistry.getHighestRankingMerger(abstract_).copyRightToLeft(abstract_, new BasicMonitor());

		final EObject leftEClassA = getNodeNamed(left, "A");
		assertNotNull(leftEClassA);
		assertFalse(((EClass)leftEClassA).isAbstract());
		assertTrue(((EClass)leftEClassA).isInterface());

		Diff interface_ = diffs.next();
		mergerRegistry.getHighestRankingMerger(interface_).copyRightToLeft(interface_, new BasicMonitor());

		assertNotNull(leftEClassA);
		assertFalse(((EClass)leftEClassA).isAbstract());
		assertFalse(((EClass)leftEClassA).isInterface());

		for (Diff currentDiff : differences) {
			if (currentDiff.getSource() == DifferenceSource.LEFT) {
				assertTrue(currentDiff.getState() == DifferenceState.MERGED);
			} else {
				assertTrue(currentDiff.getState() == DifferenceState.UNRESOLVED);
			}
		}
	}

	@Test
	public void testLeftSetRightDelete_RtL_2() throws IOException {
		// Conflict between true[abstract set]/true[interface set] from left side and A[eClassifiers delete]
		// from right side

		final Resource left = input.getLeftSetRightDeleteLeftConflictScope();
		final Resource right = input.getLeftSetRightDeleteRightConflictScope();
		final Resource origin = input.getLeftSetRightDeleteOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.RIGHT));

		// Merge A[eClassifiers delete] from right to left : true[abstract set]/true[interface set] will be
		// merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final EObject leftEClassA = getNodeNamed(left, "A");
		assertNull(leftEClassA);

		assertFalse(Iterators.any(differences.iterator(), not(hasState(DifferenceState.MERGED))));
	}

	@Test
	public void testLeftSetRightUnset_LtR_1() throws IOException {
		// Conflict between EBoolean[boolean] [eType set] from left side and EString[java.lang.String] [eType
		// unset] from right side

		final Resource left = input.getLeftSetRightUnsetLeftConflictScope();
		final Resource right = input.getLeftSetRightUnsetRightConflictScope();
		final Resource origin = input.getLeftSetRightUnsetOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.LEFT));

		// Merge EBoolean[boolean] [eType set] from left to right : EString[java.lang.String] [eType unset]
		// will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final String featureName = "eType";
		final EObject rightEAttributeName = getNodeNamed(right, "name");
		assertNotNull(rightEAttributeName);
		final EStructuralFeature feature = rightEAttributeName.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EClassifier eType = ((EAttribute)rightEAttributeName).getEType();
		assertNotNull(eType);
		assertEquals(eType.getName(), "EBoolean");

		assertFalse(Iterators.any(differences.iterator(), not(hasState(DifferenceState.MERGED))));
	}

	@Test
	public void testLeftSetRightUnset_LtR_2() throws IOException {
		// Conflict between EBoolean[boolean] [eType set] from left side and EString[java.lang.String] [eType
		// unset] from right side

		final Resource left = input.getLeftSetRightUnsetLeftConflictScope();
		final Resource right = input.getLeftSetRightUnsetRightConflictScope();
		final Resource origin = input.getLeftSetRightUnsetOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.RIGHT));

		// Merge EString[java.lang.String] [eType unset] from left to right : EBoolean[boolean] [eType set]
		// will not be merge.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final String featureName = "eType";
		final EObject rightEAttributeName = getNodeNamed(right, "name");
		assertNotNull(rightEAttributeName);
		final EStructuralFeature feature = rightEAttributeName.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EClassifier eType = ((EAttribute)rightEAttributeName).getEType();
		assertNotNull(eType);
		assertEquals(eType.getName(), "EString");

		for (Diff currentDiff : differences) {
			if (currentDiff.getSource() == DifferenceSource.RIGHT) {
				assertTrue(currentDiff.getState() == DifferenceState.MERGED);
			} else {
				assertTrue(currentDiff.getState() == DifferenceState.UNRESOLVED);
			}
		}
	}

	@Test
	public void testLeftSetRightUnset_RtL_1() throws IOException {
		// Conflict between EBoolean[boolean] [eType set] from left side and EString[java.lang.String] [eType
		// unset] from right side

		final Resource left = input.getLeftSetRightUnsetLeftConflictScope();
		final Resource right = input.getLeftSetRightUnsetRightConflictScope();
		final Resource origin = input.getLeftSetRightUnsetOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.LEFT));

		// Merge EBoolean[boolean] [eType set] from right to left : EString[java.lang.String] [eType unset]
		// will not be merge.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final String featureName = "eType";
		final EObject leftEAttributeName = getNodeNamed(left, "name");
		assertNotNull(leftEAttributeName);
		final EStructuralFeature feature = leftEAttributeName.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EClassifier eType = ((EAttribute)leftEAttributeName).getEType();
		assertNotNull(eType);
		assertEquals(eType.getName(), "EString");

		for (Diff currentDiff : differences) {
			if (currentDiff.getSource() == DifferenceSource.LEFT) {
				assertTrue(currentDiff.getState() == DifferenceState.MERGED);
			} else {
				assertTrue(currentDiff.getState() == DifferenceState.UNRESOLVED);
			}
		}
	}

	@Test
	public void testLeftSetRightUnset_RtL_2() throws IOException {
		// Conflict between EBoolean[boolean] [eType set] from left side and EString[java.lang.String] [eType
		// unset] from right side

		final Resource left = input.getLeftSetRightUnsetLeftConflictScope();
		final Resource right = input.getLeftSetRightUnsetRightConflictScope();
		final Resource origin = input.getLeftSetRightUnsetOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.RIGHT));

		// Merge EString[java.lang.String] [eType unset] from right to left : EBoolean[boolean] [eType set]
		// will also be merged
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final String featureName = "eType";
		final EObject leftEAttributeName = getNodeNamed(left, "name");
		assertNotNull(leftEAttributeName);
		final EStructuralFeature feature = leftEAttributeName.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EClassifier eType = ((EAttribute)leftEAttributeName).getEType();
		assertNull(eType);

		assertFalse(Iterators.any(differences.iterator(), not(hasState(DifferenceState.MERGED))));
	}

	@Test
	public void testLeftUnsetRightSet_LtR_1() throws IOException {
		// Conflict between EBoolean[boolean] [eType set] from left side and EString[java.lang.String] [eType
		// unset] from right side

		final Resource left = input.getLeftUnsetRightSetLeftConflictScope();
		final Resource right = input.getLeftUnsetRightSetRightConflictScope();
		final Resource origin = input.getLeftUnsetRightSetOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.LEFT));

		// Merge EString[java.lang.String] [eType unset] from left to right : EBoolean[boolean] [eType set]
		// will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final String featureName = "eType";
		final EObject rightEAttributeName = getNodeNamed(right, "name");
		assertNotNull(rightEAttributeName);
		final EStructuralFeature feature = rightEAttributeName.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EClassifier eType = ((EAttribute)rightEAttributeName).getEType();
		assertNull(eType);

		assertFalse(Iterators.any(differences.iterator(), not(hasState(DifferenceState.MERGED))));
	}

	@Test
	public void testLeftUnsetRightSet_LtR_2() throws IOException {
		// Conflict between EBoolean[boolean] [eType set] from left side and EString[java.lang.String] [eType
		// unset] from right side

		final Resource left = input.getLeftUnsetRightSetLeftConflictScope();
		final Resource right = input.getLeftUnsetRightSetRightConflictScope();
		final Resource origin = input.getLeftUnsetRightSetOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.RIGHT));

		// Merge EBoolean[boolean] [eType set] from left to right : EString[java.lang.String] [eType unset]
		// will not be merge.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final String featureName = "eType";
		final EObject rightEAttributeName = getNodeNamed(right, "name");
		assertNotNull(rightEAttributeName);
		final EStructuralFeature feature = rightEAttributeName.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EClassifier eType = ((EAttribute)rightEAttributeName).getEType();
		assertNotNull(eType);
		assertEquals(eType.getName(), "EString");

		for (Diff currentDiff : differences) {
			if (currentDiff.getSource() == DifferenceSource.RIGHT) {
				assertTrue(currentDiff.getState() == DifferenceState.MERGED);
			} else {
				assertTrue(currentDiff.getState() == DifferenceState.UNRESOLVED);
			}
		}
	}

	@Test
	public void testLeftUnsetRightSet_RtL_1() throws IOException {
		// Conflict between EBoolean[boolean] [eType set] from left side and EString[java.lang.String] [eType
		// unset] from right side

		final Resource left = input.getLeftUnsetRightSetLeftConflictScope();
		final Resource right = input.getLeftUnsetRightSetRightConflictScope();
		final Resource origin = input.getLeftUnsetRightSetOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.LEFT));

		// Merge EString[java.lang.String] [eType unset] from right to left : EBoolean[boolean] [eType set]
		// will not be merge.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final String featureName = "eType";
		final EObject leftEAttributeName = getNodeNamed(left, "name");
		assertNotNull(leftEAttributeName);
		final EStructuralFeature feature = leftEAttributeName.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EClassifier eType = ((EAttribute)leftEAttributeName).getEType();
		assertNotNull(eType);
		assertEquals(eType.getName(), "EString");

		for (Diff currentDiff : differences) {
			if (currentDiff.getSource() == DifferenceSource.LEFT) {
				assertTrue(currentDiff.getState() == DifferenceState.MERGED);
			} else {
				assertTrue(currentDiff.getState() == DifferenceState.UNRESOLVED);
			}
		}
	}

	@Test
	public void testLeftUnsetRightSet_RtL_2() throws IOException {
		// Conflict between EBoolean[boolean] [eType set] from left side and EString[java.lang.String] [eType
		// unset] from right side

		final Resource left = input.getLeftUnsetRightSetLeftConflictScope();
		final Resource right = input.getLeftUnsetRightSetRightConflictScope();
		final Resource origin = input.getLeftUnsetRightSetOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.RIGHT));

		// Merge EBoolean[boolean] [eType set] from right to left : EString[java.lang.String] [eType unset]
		// will be merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final String featureName = "eType";
		final EObject leftEAttributeName = getNodeNamed(left, "name");
		assertNotNull(leftEAttributeName);
		final EStructuralFeature feature = leftEAttributeName.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EClassifier eType = ((EAttribute)leftEAttributeName).getEType();
		assertNotNull(eType);
		assertEquals(eType.getName(), "EBoolean");

		assertFalse(Iterators.any(differences.iterator(), not(hasState(DifferenceState.MERGED))));
	}

	@Test
	public void testLeftDeleteRightMove_LtR_1() throws IOException {
		// Conflict between B[eClassifiers delete] from left side and name : EString [eStructuralFeatures
		// move] from right side

		final Resource left = input.getLeftDeleteRightMoveLeftConflictScope();
		final Resource right = input.getLeftDeleteRightMoveRightConflictScope();
		final Resource origin = input.getLeftDeleteRightMoveOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.LEFT));

		// Merge B[eClassifiers delete] from left to right : EString [eStructuralFeatures move]
		// will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final EObject rightEClassB = getNodeNamed(right, "B");
		assertNull(rightEClassB);

		assertFalse(Iterators.any(differences.iterator(), not(hasState(DifferenceState.MERGED))));
	}

	@Test
	public void testLeftDeleteRightMove_LtR_2() throws IOException {
		// Conflict between B[eClassifiers delete] from left side and name : EString [eStructuralFeatures
		// move] from right side

		final Resource left = input.getLeftDeleteRightMoveLeftConflictScope();
		final Resource right = input.getLeftDeleteRightMoveRightConflictScope();
		final Resource origin = input.getLeftDeleteRightMoveOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.RIGHT));

		// Merge EString [eStructuralFeatures move] from left to right : B[eClassifiers delete]
		// will not be merged
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final String featureName = "eStructuralFeatures";
		final EObject rightEClassB = getNodeNamed(right, "B");
		assertNotNull(rightEClassB);
		final EStructuralFeature featureB = rightEClassB.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureB);
		final EStructuralFeature eStructuralFeatureNameB = ((EClass)rightEClassB)
				.getEStructuralFeature("name");
		assertNull(eStructuralFeatureNameB);

		final EObject rightEClassA = getNodeNamed(right, "A");
		assertNotNull(rightEClassA);
		final EStructuralFeature featureA = rightEClassA.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureA);
		final EStructuralFeature eStructuralFeatureNameA = ((EClass)rightEClassA)
				.getEStructuralFeature("name");
		assertNotNull(eStructuralFeatureNameA);

		for (Diff currentDiff : differences) {
			if (currentDiff.getSource() == DifferenceSource.RIGHT) {
				assertTrue(currentDiff.getState() == DifferenceState.MERGED);
			} else {
				assertTrue(currentDiff.getState() == DifferenceState.UNRESOLVED);
			}
		}
	}

	@Test
	public void testLeftDeleteRightMove_RtL_1() throws IOException {
		// Conflict between B[eClassifiers delete] from left side and name : EString [eStructuralFeatures
		// move] from right side

		final Resource left = input.getLeftDeleteRightMoveLeftConflictScope();
		final Resource right = input.getLeftDeleteRightMoveRightConflictScope();
		final Resource origin = input.getLeftDeleteRightMoveOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.LEFT));

		// Merge B[eClassifiers delete] from right to left : EString [eStructuralFeatures move]
		// will not be merge.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final String featureName = "eStructuralFeatures";
		final EObject leftEClassB = getNodeNamed(left, "B");
		assertNotNull(leftEClassB);
		final EStructuralFeature featureB = leftEClassB.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureB);
		final EStructuralFeature eStructuralFeatureNameB = ((EClass)leftEClassB)
				.getEStructuralFeature("name");
		assertNull(eStructuralFeatureNameB);

		final EObject leftEClassA = getNodeNamed(left, "A");
		assertNotNull(leftEClassA);
		final EStructuralFeature featureA = leftEClassA.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureA);
		final EStructuralFeature eStructuralFeatureNameA = ((EClass)leftEClassA)
				.getEStructuralFeature("name");
		assertNotNull(eStructuralFeatureNameA);

		for (Diff currentDiff : differences) {
			if (currentDiff.getSource() == DifferenceSource.LEFT) {
				assertTrue(currentDiff.getState() == DifferenceState.MERGED);
			} else {
				assertTrue(currentDiff.getState() == DifferenceState.UNRESOLVED);
			}
		}
	}

	@Test
	public void testLeftDeleteRightMove_RtL_2() throws IOException {
		// Conflict between B[eClassifiers delete] from left side and name : EString [eStructuralFeatures
		// move] from right side

		final Resource left = input.getLeftDeleteRightMoveLeftConflictScope();
		final Resource right = input.getLeftDeleteRightMoveRightConflictScope();
		final Resource origin = input.getLeftDeleteRightMoveOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.RIGHT));

		// Merge EString [eStructuralFeatures move] from right to left : B[eClassifiers delete]
		// will be merge too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final String featureName = "eStructuralFeatures";
		final EObject leftEClassB = getNodeNamed(left, "B");
		assertNotNull(leftEClassB);
		final EStructuralFeature featureB = leftEClassB.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureB);
		final EStructuralFeature eStructuralFeatureNameB = ((EClass)leftEClassB)
				.getEStructuralFeature("name");
		assertNotNull(eStructuralFeatureNameB);
		final EObject leftEClassA = getNodeNamed(left, "A");
		assertNotNull(leftEClassA);
		final EStructuralFeature featureA = leftEClassA.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureA);
		final EStructuralFeature eStructuralFeatureNameA = ((EClass)leftEClassA)
				.getEStructuralFeature("name");
		assertNull(eStructuralFeatureNameA);

		assertFalse(Iterators.any(differences.iterator(), not(hasState(DifferenceState.MERGED))));
	}

	@Test
	public void testLeftMoveRightDelete_LtR_1() throws IOException {
		// Conflict name : EString [eStructuralFeatures move] between from left side and
		// B[eClassifiers delete] from right side

		final Resource left = input.getLeftMoveRightDeleteLeftConflictScope();
		final Resource right = input.getLeftMoveRightDeleteRightConflictScope();
		final Resource origin = input.getLeftMoveRightDeleteOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.LEFT));
		assertNotNull(diff);
		assertNotNull(diff.getConflict());

		final Diff move = Iterators.find(differences.iterator(), ofKind(DifferenceKind.MOVE));
		assertNotNull(move);

		// Merge EString [eStructuralFeatures move] from left to right : B[eClassifiers delete]
		// will be merge from left to right.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final String featureName = "eStructuralFeatures";
		final EObject rightEClassB = getNodeNamed(right, "B");
		assertNotNull(rightEClassB);

		final EObject rightEClassA = getNodeNamed(right, "A");
		assertNotNull(rightEClassA);
		final EStructuralFeature featureA = rightEClassA.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureA);
		final EStructuralFeature eStructuralFeatureNameA = ((EClass)rightEClassA)
				.getEStructuralFeature("name");
		assertNull(eStructuralFeatureNameA);
		final EStructuralFeature featureB = rightEClassB.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureB);
		final EStructuralFeature eStructuralFeatureNameB = ((EClass)rightEClassB)
				.getEStructuralFeature("name");
		assertNotNull(eStructuralFeatureNameB);

		assertFalse(Iterators.any(differences.iterator(), not(hasState(DifferenceState.MERGED))));
	}

	@Test
	public void testLeftMoveRightDelete_LtR_2() throws IOException {
		// Conflict name : EString [eStructuralFeatures move] between from left side and
		// B[eClassifiers delete] from right side

		final Resource left = input.getLeftMoveRightDeleteLeftConflictScope();
		final Resource right = input.getLeftMoveRightDeleteRightConflictScope();
		final Resource origin = input.getLeftMoveRightDeleteOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.RIGHT));
		assertNotNull(diff);
		assertNotNull(diff.getConflict());

		final Diff move = Iterators.find(differences.iterator(), ofKind(DifferenceKind.MOVE));
		assertNotNull(move);

		// Merge B[eClassifiers delete] from left to right : EString [eStructuralFeatures move]
		// will not be merge.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final String featureName = "eStructuralFeatures";
		final EObject rightEClassB = getNodeNamed(right, "B");
		assertNotNull(rightEClassB);
		final EStructuralFeature featureB = rightEClassB.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureB);
		final EStructuralFeature eStructuralFeatureNameB = ((EClass)rightEClassB)
				.getEStructuralFeature("name");
		assertNull(eStructuralFeatureNameB);

		final EObject rightEClassA = getNodeNamed(right, "A");
		assertNotNull(rightEClassA);
		final EStructuralFeature featureA = rightEClassA.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureA);
		final EStructuralFeature eStructuralFeatureNameA = ((EClass)rightEClassA)
				.getEStructuralFeature("name");
		assertNotNull(eStructuralFeatureNameA);

		for (Diff currentDiff : differences) {
			if (currentDiff.getSource() == DifferenceSource.RIGHT) {
				assertTrue(currentDiff.getState() == DifferenceState.MERGED);
			} else {
				assertTrue(currentDiff.getState() == DifferenceState.UNRESOLVED);
			}
		}
	}

	@Test
	public void testLeftMoveRightDelete_RtL_1() throws IOException {
		// Conflict name : EString [eStructuralFeatures move] between from left side and
		// B[eClassifiers delete] from right side

		final Resource left = input.getLeftMoveRightDeleteLeftConflictScope();
		final Resource right = input.getLeftMoveRightDeleteRightConflictScope();
		final Resource origin = input.getLeftMoveRightDeleteOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.LEFT));
		assertNotNull(diff);
		assertNotNull(diff.getConflict());

		final Diff move = Iterators.find(differences.iterator(), ofKind(DifferenceKind.MOVE));
		assertNotNull(move);

		// Merge EString [eStructuralFeatures move] from right to left : B[eClassifiers delete]
		// will not be merge.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final String featureName = "eStructuralFeatures";
		final EObject leftEClassB = getNodeNamed(left, "B");
		assertNotNull(leftEClassB);
		final EStructuralFeature featureB = leftEClassB.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureB);
		final EStructuralFeature eStructuralFeatureNameB = ((EClass)leftEClassB)
				.getEStructuralFeature("name");
		assertNull(eStructuralFeatureNameB);

		final EObject leftEClassA = getNodeNamed(left, "A");
		assertNotNull(leftEClassA);
		final EStructuralFeature featureA = leftEClassA.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureA);
		final EStructuralFeature eStructuralFeatureNameA = ((EClass)leftEClassA)
				.getEStructuralFeature("name");
		assertNotNull(eStructuralFeatureNameA);

		for (Diff currentDiff : differences) {
			if (currentDiff.getSource() == DifferenceSource.LEFT) {
				assertTrue(currentDiff.getState() == DifferenceState.MERGED);
			} else {
				assertTrue(currentDiff.getState() == DifferenceState.UNRESOLVED);
			}
		}
	}

	@Test
	public void testLeftMoveRightDelete_RtL_2() throws IOException {
		// Conflict name : EString [eStructuralFeatures move] between from left side and
		// B[eClassifiers delete] from right side

		final Resource left = input.getLeftMoveRightDeleteLeftConflictScope();
		final Resource right = input.getLeftMoveRightDeleteRightConflictScope();
		final Resource origin = input.getLeftMoveRightDeleteOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.RIGHT));
		assertNotNull(diff);
		assertNotNull(diff.getConflict());

		final Diff move = Iterators.find(differences.iterator(), ofKind(DifferenceKind.MOVE));
		assertNotNull(move);

		// Merge B[eClassifiers delete] from right to left : EString [eStructuralFeatures move]
		// will not be merge.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final String featureName = "eStructuralFeatures";
		final EObject leftEClassB = getNodeNamed(left, "B");
		assertNull(leftEClassB);

		final EObject leftEClassA = getNodeNamed(left, "A");
		assertNotNull(leftEClassA);
		final EStructuralFeature featureA = leftEClassA.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureA);
		final EStructuralFeature eStructuralFeatureNameA = ((EClass)leftEClassA)
				.getEStructuralFeature("name");
		assertNull(eStructuralFeatureNameA);

		assertFalse(Iterators.any(differences.iterator(), not(hasState(DifferenceState.MERGED))));
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
