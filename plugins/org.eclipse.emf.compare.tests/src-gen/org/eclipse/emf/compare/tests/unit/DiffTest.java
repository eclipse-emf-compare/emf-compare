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
package org.eclipse.emf.compare.tests.unit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Test;

/**
 * Tests the behavior of the {@link Diff} class.
 * 
 * @generated
 */
public class DiffTest extends AbstractCompareTest {
	/**
	 * Tests the behavior of reference <code>requires</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testRequires() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Requires();
		Diff diff = CompareFactory.eINSTANCE.createDiff();
		diff.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Diff requiresValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createDiff();
		List<org.eclipse.emf.compare.Diff> listRequires = new ArrayList<org.eclipse.emf.compare.Diff>(1);
		listRequires.add(requiresValue);

		assertFalse(diff.eIsSet(feature));
		assertTrue(diff.getRequires().isEmpty());

		diff.getRequires().add(requiresValue);
		assertTrue(notified);
		notified = false;
		assertTrue(diff.getRequires().contains(requiresValue));
		assertSame(diff.getRequires(), diff.eGet(feature));
		assertSame(diff.getRequires(), diff.eGet(feature, false));
		assertTrue(diff.eIsSet(feature));
		assertTrue(requiresValue.getRequiredBy().contains(diff));

		diff.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(diff.getRequires().isEmpty());
		assertSame(diff.getRequires(), diff.eGet(feature));
		assertSame(diff.getRequires(), diff.eGet(feature, false));
		assertFalse(diff.eIsSet(feature));
		assertFalse(requiresValue.getRequiredBy().contains(diff));

		diff.eSet(feature, listRequires);
		assertTrue(notified);
		notified = false;
		assertTrue(diff.getRequires().contains(requiresValue));
		assertSame(diff.getRequires(), diff.eGet(feature));
		assertSame(diff.getRequires(), diff.eGet(feature, false));
		assertTrue(diff.eIsSet(feature));
		assertTrue(requiresValue.getRequiredBy().contains(diff));
	}

	/**
	 * Tests the behavior of reference <code>requiredBy</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testRequiredBy() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_RequiredBy();
		Diff diff = CompareFactory.eINSTANCE.createDiff();
		diff.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Diff requiredByValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createDiff();
		List<org.eclipse.emf.compare.Diff> listRequiredBy = new ArrayList<org.eclipse.emf.compare.Diff>(1);
		listRequiredBy.add(requiredByValue);

		assertFalse(diff.eIsSet(feature));
		assertTrue(diff.getRequiredBy().isEmpty());

		diff.getRequiredBy().add(requiredByValue);
		assertTrue(notified);
		notified = false;
		assertTrue(diff.getRequiredBy().contains(requiredByValue));
		assertSame(diff.getRequiredBy(), diff.eGet(feature));
		assertSame(diff.getRequiredBy(), diff.eGet(feature, false));
		assertTrue(diff.eIsSet(feature));
		assertTrue(requiredByValue.getRequires().contains(diff));

		diff.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(diff.getRequiredBy().isEmpty());
		assertSame(diff.getRequiredBy(), diff.eGet(feature));
		assertSame(diff.getRequiredBy(), diff.eGet(feature, false));
		assertFalse(diff.eIsSet(feature));
		assertFalse(requiredByValue.getRequires().contains(diff));

		diff.eSet(feature, listRequiredBy);
		assertTrue(notified);
		notified = false;
		assertTrue(diff.getRequiredBy().contains(requiredByValue));
		assertSame(diff.getRequiredBy(), diff.eGet(feature));
		assertSame(diff.getRequiredBy(), diff.eGet(feature, false));
		assertTrue(diff.eIsSet(feature));
		assertTrue(requiredByValue.getRequires().contains(diff));
	}

	/**
	 * Tests the behavior of reference <code>refines</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testRefines() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Refines();
		Diff diff = CompareFactory.eINSTANCE.createDiff();
		diff.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Diff refinesValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createDiff();
		List<org.eclipse.emf.compare.Diff> listRefines = new ArrayList<org.eclipse.emf.compare.Diff>(1);
		listRefines.add(refinesValue);

		assertFalse(diff.eIsSet(feature));
		assertTrue(diff.getRefines().isEmpty());

		diff.getRefines().add(refinesValue);
		assertTrue(notified);
		notified = false;
		assertTrue(diff.getRefines().contains(refinesValue));
		assertSame(diff.getRefines(), diff.eGet(feature));
		assertSame(diff.getRefines(), diff.eGet(feature, false));
		assertTrue(diff.eIsSet(feature));
		assertTrue(refinesValue.getRefinedBy().contains(diff));

		diff.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(diff.getRefines().isEmpty());
		assertSame(diff.getRefines(), diff.eGet(feature));
		assertSame(diff.getRefines(), diff.eGet(feature, false));
		assertFalse(diff.eIsSet(feature));
		assertFalse(refinesValue.getRefinedBy().contains(diff));

		diff.eSet(feature, listRefines);
		assertTrue(notified);
		notified = false;
		assertTrue(diff.getRefines().contains(refinesValue));
		assertSame(diff.getRefines(), diff.eGet(feature));
		assertSame(diff.getRefines(), diff.eGet(feature, false));
		assertTrue(diff.eIsSet(feature));
		assertTrue(refinesValue.getRefinedBy().contains(diff));
	}

	/**
	 * Tests the behavior of reference <code>refinedBy</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testRefinedBy() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_RefinedBy();
		Diff diff = CompareFactory.eINSTANCE.createDiff();
		diff.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Diff refinedByValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createDiff();
		List<org.eclipse.emf.compare.Diff> listRefinedBy = new ArrayList<org.eclipse.emf.compare.Diff>(1);
		listRefinedBy.add(refinedByValue);

		assertFalse(diff.eIsSet(feature));
		assertTrue(diff.getRefinedBy().isEmpty());

		diff.getRefinedBy().add(refinedByValue);
		assertTrue(notified);
		notified = false;
		assertTrue(diff.getRefinedBy().contains(refinedByValue));
		assertSame(diff.getRefinedBy(), diff.eGet(feature));
		assertSame(diff.getRefinedBy(), diff.eGet(feature, false));
		assertTrue(diff.eIsSet(feature));
		assertTrue(refinedByValue.getRefines().contains(diff));

		diff.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(diff.getRefinedBy().isEmpty());
		assertSame(diff.getRefinedBy(), diff.eGet(feature));
		assertSame(diff.getRefinedBy(), diff.eGet(feature, false));
		assertFalse(diff.eIsSet(feature));
		assertFalse(refinedByValue.getRefines().contains(diff));

		diff.eSet(feature, listRefinedBy);
		assertTrue(notified);
		notified = false;
		assertTrue(diff.getRefinedBy().contains(refinedByValue));
		assertSame(diff.getRefinedBy(), diff.eGet(feature));
		assertSame(diff.getRefinedBy(), diff.eGet(feature, false));
		assertTrue(diff.eIsSet(feature));
		assertTrue(refinedByValue.getRefines().contains(diff));
	}

	/**
	 * Tests the behavior of reference <code>match</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testMatch() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Match();
		Diff diff = CompareFactory.eINSTANCE.createDiff();
		diff.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Match matchValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createMatch();

		assertFalse(diff.eIsSet(feature));
		assertNull(diff.getMatch());

		diff.setMatch(matchValue);
		assertTrue(notified);
		notified = false;
		assertSame(matchValue, diff.getMatch());
		assertSame(diff.getMatch(), diff.eGet(feature));
		assertSame(diff.getMatch(), diff.eGet(feature, false));
		assertTrue(diff.eIsSet(feature));
		assertTrue(matchValue.getDifferences().contains(diff));

		diff.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertNull(diff.getMatch());
		assertSame(diff.getMatch(), diff.eGet(feature));
		assertSame(diff.getMatch(), diff.eGet(feature, false));
		assertFalse(diff.eIsSet(feature));
		assertFalse(matchValue.getDifferences().contains(diff));

		diff.setMatch(matchValue);
		assertTrue(notified);
		notified = false;
		assertSame(matchValue, diff.getMatch());
		assertSame(diff.getMatch(), diff.eGet(feature));
		assertSame(diff.getMatch(), diff.eGet(feature, false));
		assertTrue(diff.eIsSet(feature));
		assertTrue(matchValue.getDifferences().contains(diff));

		diff.eSet(feature, matchValue);
		assertTrue(notified);
		notified = false;
		assertSame(matchValue, diff.getMatch());
		assertSame(diff.getMatch(), diff.eGet(feature));
		assertSame(diff.getMatch(), diff.eGet(feature, false));
		assertTrue(diff.eIsSet(feature));
		assertTrue(matchValue.getDifferences().contains(diff));

		diff.setMatch(null);
		assertTrue(notified);
		notified = false;
		assertNull(diff.getMatch());
		assertSame(feature.getDefaultValue(), diff.getMatch());
		assertSame(diff.getMatch(), diff.eGet(feature));
		assertSame(diff.getMatch(), diff.eGet(feature, false));
		assertFalse(diff.eIsSet(feature));
		assertFalse(matchValue.getDifferences().contains(diff));
	}

	/**
	 * Tests the behavior of reference <code>equivalence</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testEquivalence() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Equivalence();
		Diff diff = CompareFactory.eINSTANCE.createDiff();
		diff.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Equivalence equivalenceValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createEquivalence();

		assertFalse(diff.eIsSet(feature));
		assertNull(diff.getEquivalence());

		diff.setEquivalence(equivalenceValue);
		assertTrue(notified);
		notified = false;
		assertSame(equivalenceValue, diff.getEquivalence());
		assertSame(diff.getEquivalence(), diff.eGet(feature));
		assertSame(diff.getEquivalence(), diff.eGet(feature, false));
		assertTrue(diff.eIsSet(feature));
		assertTrue(equivalenceValue.getDifferences().contains(diff));

		diff.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertNull(diff.getEquivalence());
		assertSame(diff.getEquivalence(), diff.eGet(feature));
		assertSame(diff.getEquivalence(), diff.eGet(feature, false));
		assertFalse(diff.eIsSet(feature));
		assertFalse(equivalenceValue.getDifferences().contains(diff));

		diff.setEquivalence(equivalenceValue);
		assertTrue(notified);
		notified = false;
		assertSame(equivalenceValue, diff.getEquivalence());
		assertSame(diff.getEquivalence(), diff.eGet(feature));
		assertSame(diff.getEquivalence(), diff.eGet(feature, false));
		assertTrue(diff.eIsSet(feature));
		assertTrue(equivalenceValue.getDifferences().contains(diff));

		diff.eSet(feature, equivalenceValue);
		assertTrue(notified);
		notified = false;
		assertSame(equivalenceValue, diff.getEquivalence());
		assertSame(diff.getEquivalence(), diff.eGet(feature));
		assertSame(diff.getEquivalence(), diff.eGet(feature, false));
		assertTrue(diff.eIsSet(feature));
		assertTrue(equivalenceValue.getDifferences().contains(diff));

		diff.setEquivalence(null);
		assertTrue(notified);
		notified = false;
		assertNull(diff.getEquivalence());
		assertSame(feature.getDefaultValue(), diff.getEquivalence());
		assertSame(diff.getEquivalence(), diff.eGet(feature));
		assertSame(diff.getEquivalence(), diff.eGet(feature, false));
		assertFalse(diff.eIsSet(feature));
		assertFalse(equivalenceValue.getDifferences().contains(diff));
	}

	/**
	 * Tests the behavior of reference <code>conflict</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testConflict() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Conflict();
		Diff diff = CompareFactory.eINSTANCE.createDiff();
		diff.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Conflict conflictValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createConflict();

		assertFalse(diff.eIsSet(feature));
		assertNull(diff.getConflict());

		diff.setConflict(conflictValue);
		assertTrue(notified);
		notified = false;
		assertSame(conflictValue, diff.getConflict());
		assertSame(diff.getConflict(), diff.eGet(feature));
		assertSame(diff.getConflict(), diff.eGet(feature, false));
		assertTrue(diff.eIsSet(feature));
		assertTrue(conflictValue.getDifferences().contains(diff));

		diff.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertNull(diff.getConflict());
		assertSame(diff.getConflict(), diff.eGet(feature));
		assertSame(diff.getConflict(), diff.eGet(feature, false));
		assertFalse(diff.eIsSet(feature));
		assertFalse(conflictValue.getDifferences().contains(diff));

		diff.setConflict(conflictValue);
		assertTrue(notified);
		notified = false;
		assertSame(conflictValue, diff.getConflict());
		assertSame(diff.getConflict(), diff.eGet(feature));
		assertSame(diff.getConflict(), diff.eGet(feature, false));
		assertTrue(diff.eIsSet(feature));
		assertTrue(conflictValue.getDifferences().contains(diff));

		diff.eSet(feature, conflictValue);
		assertTrue(notified);
		notified = false;
		assertSame(conflictValue, diff.getConflict());
		assertSame(diff.getConflict(), diff.eGet(feature));
		assertSame(diff.getConflict(), diff.eGet(feature, false));
		assertTrue(diff.eIsSet(feature));
		assertTrue(conflictValue.getDifferences().contains(diff));

		diff.setConflict(null);
		assertTrue(notified);
		notified = false;
		assertNull(diff.getConflict());
		assertSame(feature.getDefaultValue(), diff.getConflict());
		assertSame(diff.getConflict(), diff.eGet(feature));
		assertSame(diff.getConflict(), diff.eGet(feature, false));
		assertFalse(diff.eIsSet(feature));
		assertFalse(conflictValue.getDifferences().contains(diff));
	}

	/**
	 * Tests the behavior of attribute <code>kind</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testKind() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Kind();
		Diff diff = CompareFactory.eINSTANCE.createDiff();
		diff.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.DifferenceKind kindValue = (org.eclipse.emf.compare.DifferenceKind)feature
				.getDefaultValue();
		for (org.eclipse.emf.compare.DifferenceKind aDifferenceKind : org.eclipse.emf.compare.DifferenceKind.VALUES) {
			if (kindValue.getValue() != aDifferenceKind.getValue()) {
				kindValue = aDifferenceKind;
				break;
			}
		}

		assertFalse(diff.eIsSet(feature));
		assertEquals(feature.getDefaultValue(), diff.getKind());

		diff.setKind(kindValue);
		assertTrue(notified);
		notified = false;
		assertEquals(kindValue, diff.getKind());
		assertEquals(diff.getKind(), diff.eGet(feature));
		assertTrue(diff.eIsSet(feature));

		diff.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), diff.getKind());
		assertEquals(diff.getKind(), diff.eGet(feature));
		assertFalse(diff.eIsSet(feature));

		diff.eSet(feature, kindValue);
		assertTrue(notified);
		notified = false;
		assertEquals(kindValue, diff.getKind());
		assertEquals(diff.getKind(), diff.eGet(feature));
		assertTrue(diff.eIsSet(feature));

		diff.setKind(null);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), diff.getKind());
		assertEquals(diff.getKind(), diff.eGet(feature));
		assertFalse(diff.eIsSet(feature));
	}

	/**
	 * Tests the behavior of attribute <code>source</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testSource() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Source();
		Diff diff = CompareFactory.eINSTANCE.createDiff();
		diff.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.DifferenceSource sourceValue = (org.eclipse.emf.compare.DifferenceSource)feature
				.getDefaultValue();
		for (org.eclipse.emf.compare.DifferenceSource aDifferenceSource : org.eclipse.emf.compare.DifferenceSource.VALUES) {
			if (sourceValue.getValue() != aDifferenceSource.getValue()) {
				sourceValue = aDifferenceSource;
				break;
			}
		}

		assertFalse(diff.eIsSet(feature));
		assertEquals(feature.getDefaultValue(), diff.getSource());

		diff.setSource(sourceValue);
		assertTrue(notified);
		notified = false;
		assertEquals(sourceValue, diff.getSource());
		assertEquals(diff.getSource(), diff.eGet(feature));
		assertTrue(diff.eIsSet(feature));

		diff.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), diff.getSource());
		assertEquals(diff.getSource(), diff.eGet(feature));
		assertFalse(diff.eIsSet(feature));

		diff.eSet(feature, sourceValue);
		assertTrue(notified);
		notified = false;
		assertEquals(sourceValue, diff.getSource());
		assertEquals(diff.getSource(), diff.eGet(feature));
		assertTrue(diff.eIsSet(feature));

		diff.setSource(null);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), diff.getSource());
		assertEquals(diff.getSource(), diff.eGet(feature));
		assertFalse(diff.eIsSet(feature));
	}

	/**
	 * Tests the behavior of attribute <code>state</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testState() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_State();
		Diff diff = CompareFactory.eINSTANCE.createDiff();
		diff.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.DifferenceState stateValue = (org.eclipse.emf.compare.DifferenceState)feature
				.getDefaultValue();
		for (org.eclipse.emf.compare.DifferenceState aDifferenceState : org.eclipse.emf.compare.DifferenceState.VALUES) {
			if (stateValue.getValue() != aDifferenceState.getValue()) {
				stateValue = aDifferenceState;
				break;
			}
		}

		assertFalse(diff.eIsSet(feature));
		assertEquals(feature.getDefaultValue(), diff.getState());

		diff.setState(stateValue);
		assertTrue(notified);
		notified = false;
		assertEquals(stateValue, diff.getState());
		assertEquals(diff.getState(), diff.eGet(feature));
		assertTrue(diff.eIsSet(feature));

		diff.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), diff.getState());
		assertEquals(diff.getState(), diff.eGet(feature));
		assertFalse(diff.eIsSet(feature));

		diff.eSet(feature, stateValue);
		assertTrue(notified);
		notified = false;
		assertEquals(stateValue, diff.getState());
		assertEquals(diff.getState(), diff.eGet(feature));
		assertTrue(diff.eIsSet(feature));

		diff.setState(null);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), diff.getState());
		assertEquals(diff.getState(), diff.eGet(feature));
		assertFalse(diff.eIsSet(feature));
	}

}
