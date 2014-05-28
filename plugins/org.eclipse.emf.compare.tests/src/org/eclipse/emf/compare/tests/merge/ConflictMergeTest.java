/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import static com.google.common.base.Predicates.not;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

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

	@Test
	public void testLeftAddRightDelete_LtR_1() throws IOException {
		// Conflict between B[eSuperTypes add] from left side and B[eClassifiers delete] from right side

		final Resource left = input.getLeftAddRightDeleteLeftConflictScope();
		final Resource right = input.getLeftAddRightDeleteRightConflictScope();
		final Resource origin = input.getLeftAddRightDeleteOriginConflictScope();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

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
		assertFalse(eSuperTypes.isEmpty());

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.REAL)));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), hasConflict(ConflictKind.REAL));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), hasConflict(ConflictKind.REAL));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), hasConflict(ConflictKind.REAL));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.REAL)));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), hasConflict(ConflictKind.REAL));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), hasConflict(ConflictKind.REAL));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

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
		assertFalse(eSuperTypes.isEmpty());

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.REAL)));

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
		assertSame(Integer.valueOf(3), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.LEFT));

		// Merge A[eClassifiers delete] from left to right : true[abstract set]/true[interface set] will be
		// merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final EObject rightEClassA = getNodeNamed(right, "A");
		assertNull(rightEClassA);

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.REAL)));

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
		assertSame(Integer.valueOf(3), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), hasConflict(ConflictKind.REAL));

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
		assertSame(Integer.valueOf(3), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.LEFT));

		// Merge A[eClassifiers delete] from right to left : true[abstract set]/true[interface set] will not
		// be merge.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final EObject leftEClassA = getNodeNamed(left, "A");
		assertNotNull(leftEClassA);
		assertFalse(((EClass)leftEClassA).isAbstract());
		assertFalse(((EClass)leftEClassA).isInterface());

		Iterators.any(differences.iterator(), hasConflict(ConflictKind.REAL));

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
		assertSame(Integer.valueOf(3), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.REAL)));

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
		assertSame(Integer.valueOf(3), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.REAL)));

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
		assertSame(Integer.valueOf(3), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.RIGHT));

		// Merge A[eClassifiers delete] from left to right : true[abstract set]/true[interface set] will not
		// be merge.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final EObject rightEClassA = getNodeNamed(right, "A");
		assertNotNull(rightEClassA);
		assertFalse(((EClass)rightEClassA).isAbstract());
		assertFalse(((EClass)rightEClassA).isInterface());

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.REAL)));

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
		assertSame(Integer.valueOf(3), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), hasConflict(ConflictKind.REAL));

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
		assertSame(Integer.valueOf(3), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.RIGHT));

		// Merge A[eClassifiers delete] from right to left : true[abstract set]/true[interface set] will be
		// merge from right to left too.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final EObject leftEClassA = getNodeNamed(left, "A");
		assertNull(leftEClassA);

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.REAL)));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.REAL)));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), hasConflict(ConflictKind.REAL));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), hasConflict(ConflictKind.REAL));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.RIGHT));

		// Merge EString[java.lang.String] [eType unset] from right to left : EBoolean[boolean] [eType set]
		// will not be merge.
		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());

		final String featureName = "eType";
		final EObject leftEAttributeName = getNodeNamed(left, "name");
		assertNotNull(leftEAttributeName);
		final EStructuralFeature feature = leftEAttributeName.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);
		final EClassifier eType = ((EAttribute)leftEAttributeName).getEType();
		assertNull(eType);

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.REAL)));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.REAL)));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.REAL)));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), hasConflict(ConflictKind.REAL));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.REAL)));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.LEFT));

		// Merge B[eClassifiers delete] from left to right : EString [eStructuralFeatures move]
		// will be merge from left to right too.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final EObject rightEClassB = getNodeNamed(right, "B");
		assertNull(rightEClassB);

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.REAL)));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.RIGHT));

		// Merge EString [eStructuralFeatures move] from left to right : B[eClassifiers delete]
		// will be merge from left to right too.
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

		Iterators.any(differences.iterator(), not(hasConflict(ConflictKind.REAL)));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), hasConflict(ConflictKind.REAL));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.RIGHT));

		// Merge EString [eStructuralFeatures move] from right to left : B[eClassifiers delete]
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
		assertNotNull(eStructuralFeatureNameA);

		Iterators.any(differences.iterator(), hasConflict(ConflictKind.REAL));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

		final Diff diff = Iterators.find(differences.iterator(), fromSide(DifferenceSource.LEFT));
		assertNotNull(diff);
		assertNotNull(diff.getConflict());

		final Diff move = Iterators.find(differences.iterator(), ofKind(DifferenceKind.MOVE));
		assertNotNull(move);

		// Merge EString [eStructuralFeatures move] from left to right : B[eClassifiers delete]
		// will not be merge.
		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());

		final String featureName = "eStructuralFeatures";
		final EObject rightEClassB = getNodeNamed(right, "B");
		assertNull(rightEClassB);

		final EObject rightEClassA = getNodeNamed(right, "A");
		assertNotNull(rightEClassA);
		final EStructuralFeature featureA = rightEClassA.eClass().getEStructuralFeature(featureName);
		assertNotNull(featureA);
		final EStructuralFeature eStructuralFeatureNameA = ((EClass)rightEClassA)
				.getEStructuralFeature("name");
		assertNotNull(eStructuralFeatureNameA);

		Iterators.any(differences.iterator(), hasConflict(ConflictKind.REAL));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), hasConflict(ConflictKind.REAL));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), hasConflict(ConflictKind.REAL));

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
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

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

		Iterators.any(differences.iterator(), hasConflict(ConflictKind.REAL));

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
