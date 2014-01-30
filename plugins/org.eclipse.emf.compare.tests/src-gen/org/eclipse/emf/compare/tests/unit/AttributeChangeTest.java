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
package org.eclipse.emf.compare.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Test;

/**
 * Tests the behavior of the {@link AttributeChange} class.
 * 
 * @generated
 */
public class AttributeChangeTest extends AbstractCompareTest {
	/**
	 * Tests the behavior of reference <code>requires</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testRequires() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Requires();
		AttributeChange attributeChange = CompareFactory.eINSTANCE.createAttributeChange();
		attributeChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Diff requiresValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createDiff();
		List<org.eclipse.emf.compare.Diff> listRequires = new ArrayList<org.eclipse.emf.compare.Diff>(1);
		listRequires.add(requiresValue);

		assertFalse(attributeChange.eIsSet(feature));
		assertTrue(attributeChange.getRequires().isEmpty());

		attributeChange.getRequires().add(requiresValue);
		assertTrue(notified);
		notified = false;
		assertTrue(attributeChange.getRequires().contains(requiresValue));
		assertSame(attributeChange.getRequires(), attributeChange.eGet(feature));
		assertSame(attributeChange.getRequires(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));
		assertTrue(requiresValue.getRequiredBy().contains(attributeChange));

		attributeChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(attributeChange.getRequires().isEmpty());
		assertSame(attributeChange.getRequires(), attributeChange.eGet(feature));
		assertSame(attributeChange.getRequires(), attributeChange.eGet(feature, false));
		assertFalse(attributeChange.eIsSet(feature));
		assertFalse(requiresValue.getRequiredBy().contains(attributeChange));

		attributeChange.eSet(feature, listRequires);
		assertTrue(notified);
		notified = false;
		assertTrue(attributeChange.getRequires().contains(requiresValue));
		assertSame(attributeChange.getRequires(), attributeChange.eGet(feature));
		assertSame(attributeChange.getRequires(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));
		assertTrue(requiresValue.getRequiredBy().contains(attributeChange));
	}

	/**
	 * Tests the behavior of reference <code>requiredBy</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testRequiredBy() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_RequiredBy();
		AttributeChange attributeChange = CompareFactory.eINSTANCE.createAttributeChange();
		attributeChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Diff requiredByValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createDiff();
		List<org.eclipse.emf.compare.Diff> listRequiredBy = new ArrayList<org.eclipse.emf.compare.Diff>(1);
		listRequiredBy.add(requiredByValue);

		assertFalse(attributeChange.eIsSet(feature));
		assertTrue(attributeChange.getRequiredBy().isEmpty());

		attributeChange.getRequiredBy().add(requiredByValue);
		assertTrue(notified);
		notified = false;
		assertTrue(attributeChange.getRequiredBy().contains(requiredByValue));
		assertSame(attributeChange.getRequiredBy(), attributeChange.eGet(feature));
		assertSame(attributeChange.getRequiredBy(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));
		assertTrue(requiredByValue.getRequires().contains(attributeChange));

		attributeChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(attributeChange.getRequiredBy().isEmpty());
		assertSame(attributeChange.getRequiredBy(), attributeChange.eGet(feature));
		assertSame(attributeChange.getRequiredBy(), attributeChange.eGet(feature, false));
		assertFalse(attributeChange.eIsSet(feature));
		assertFalse(requiredByValue.getRequires().contains(attributeChange));

		attributeChange.eSet(feature, listRequiredBy);
		assertTrue(notified);
		notified = false;
		assertTrue(attributeChange.getRequiredBy().contains(requiredByValue));
		assertSame(attributeChange.getRequiredBy(), attributeChange.eGet(feature));
		assertSame(attributeChange.getRequiredBy(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));
		assertTrue(requiredByValue.getRequires().contains(attributeChange));
	}

	/**
	 * Tests the behavior of reference <code>refines</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testRefines() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Refines();
		AttributeChange attributeChange = CompareFactory.eINSTANCE.createAttributeChange();
		attributeChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Diff refinesValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createDiff();
		List<org.eclipse.emf.compare.Diff> listRefines = new ArrayList<org.eclipse.emf.compare.Diff>(1);
		listRefines.add(refinesValue);

		assertFalse(attributeChange.eIsSet(feature));
		assertTrue(attributeChange.getRefines().isEmpty());

		attributeChange.getRefines().add(refinesValue);
		assertTrue(notified);
		notified = false;
		assertTrue(attributeChange.getRefines().contains(refinesValue));
		assertSame(attributeChange.getRefines(), attributeChange.eGet(feature));
		assertSame(attributeChange.getRefines(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));
		assertTrue(refinesValue.getRefinedBy().contains(attributeChange));

		attributeChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(attributeChange.getRefines().isEmpty());
		assertSame(attributeChange.getRefines(), attributeChange.eGet(feature));
		assertSame(attributeChange.getRefines(), attributeChange.eGet(feature, false));
		assertFalse(attributeChange.eIsSet(feature));
		assertFalse(refinesValue.getRefinedBy().contains(attributeChange));

		attributeChange.eSet(feature, listRefines);
		assertTrue(notified);
		notified = false;
		assertTrue(attributeChange.getRefines().contains(refinesValue));
		assertSame(attributeChange.getRefines(), attributeChange.eGet(feature));
		assertSame(attributeChange.getRefines(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));
		assertTrue(refinesValue.getRefinedBy().contains(attributeChange));
	}

	/**
	 * Tests the behavior of reference <code>refinedBy</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testRefinedBy() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_RefinedBy();
		AttributeChange attributeChange = CompareFactory.eINSTANCE.createAttributeChange();
		attributeChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Diff refinedByValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createDiff();
		List<org.eclipse.emf.compare.Diff> listRefinedBy = new ArrayList<org.eclipse.emf.compare.Diff>(1);
		listRefinedBy.add(refinedByValue);

		assertFalse(attributeChange.eIsSet(feature));
		assertTrue(attributeChange.getRefinedBy().isEmpty());

		attributeChange.getRefinedBy().add(refinedByValue);
		assertTrue(notified);
		notified = false;
		assertTrue(attributeChange.getRefinedBy().contains(refinedByValue));
		assertSame(attributeChange.getRefinedBy(), attributeChange.eGet(feature));
		assertSame(attributeChange.getRefinedBy(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));
		assertTrue(refinedByValue.getRefines().contains(attributeChange));

		attributeChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(attributeChange.getRefinedBy().isEmpty());
		assertSame(attributeChange.getRefinedBy(), attributeChange.eGet(feature));
		assertSame(attributeChange.getRefinedBy(), attributeChange.eGet(feature, false));
		assertFalse(attributeChange.eIsSet(feature));
		assertFalse(refinedByValue.getRefines().contains(attributeChange));

		attributeChange.eSet(feature, listRefinedBy);
		assertTrue(notified);
		notified = false;
		assertTrue(attributeChange.getRefinedBy().contains(refinedByValue));
		assertSame(attributeChange.getRefinedBy(), attributeChange.eGet(feature));
		assertSame(attributeChange.getRefinedBy(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));
		assertTrue(refinedByValue.getRefines().contains(attributeChange));
	}

	/**
	 * Tests the behavior of reference <code>match</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testMatch() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Match();
		AttributeChange attributeChange = CompareFactory.eINSTANCE.createAttributeChange();
		attributeChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Match matchValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createMatch();

		assertFalse(attributeChange.eIsSet(feature));
		assertNull(attributeChange.getMatch());

		attributeChange.setMatch(matchValue);
		assertTrue(notified);
		notified = false;
		assertSame(matchValue, attributeChange.getMatch());
		assertSame(attributeChange.getMatch(), attributeChange.eGet(feature));
		assertSame(attributeChange.getMatch(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));
		assertTrue(matchValue.getDifferences().contains(attributeChange));

		attributeChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertNull(attributeChange.getMatch());
		assertSame(attributeChange.getMatch(), attributeChange.eGet(feature));
		assertSame(attributeChange.getMatch(), attributeChange.eGet(feature, false));
		assertFalse(attributeChange.eIsSet(feature));
		assertFalse(matchValue.getDifferences().contains(attributeChange));

		attributeChange.setMatch(matchValue);
		assertTrue(notified);
		notified = false;
		assertSame(matchValue, attributeChange.getMatch());
		assertSame(attributeChange.getMatch(), attributeChange.eGet(feature));
		assertSame(attributeChange.getMatch(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));
		assertTrue(matchValue.getDifferences().contains(attributeChange));

		attributeChange.eSet(feature, matchValue);
		assertTrue(notified);
		notified = false;
		assertSame(matchValue, attributeChange.getMatch());
		assertSame(attributeChange.getMatch(), attributeChange.eGet(feature));
		assertSame(attributeChange.getMatch(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));
		assertTrue(matchValue.getDifferences().contains(attributeChange));

		attributeChange.setMatch(null);
		assertTrue(notified);
		notified = false;
		assertNull(attributeChange.getMatch());
		assertSame(feature.getDefaultValue(), attributeChange.getMatch());
		assertSame(attributeChange.getMatch(), attributeChange.eGet(feature));
		assertSame(attributeChange.getMatch(), attributeChange.eGet(feature, false));
		assertFalse(attributeChange.eIsSet(feature));
		assertFalse(matchValue.getDifferences().contains(attributeChange));
	}

	/**
	 * Tests the behavior of reference <code>equivalence</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testEquivalence() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Equivalence();
		AttributeChange attributeChange = CompareFactory.eINSTANCE.createAttributeChange();
		attributeChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Equivalence equivalenceValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createEquivalence();

		assertFalse(attributeChange.eIsSet(feature));
		assertNull(attributeChange.getEquivalence());

		attributeChange.setEquivalence(equivalenceValue);
		assertTrue(notified);
		notified = false;
		assertSame(equivalenceValue, attributeChange.getEquivalence());
		assertSame(attributeChange.getEquivalence(), attributeChange.eGet(feature));
		assertSame(attributeChange.getEquivalence(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));
		assertTrue(equivalenceValue.getDifferences().contains(attributeChange));

		attributeChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertNull(attributeChange.getEquivalence());
		assertSame(attributeChange.getEquivalence(), attributeChange.eGet(feature));
		assertSame(attributeChange.getEquivalence(), attributeChange.eGet(feature, false));
		assertFalse(attributeChange.eIsSet(feature));
		assertFalse(equivalenceValue.getDifferences().contains(attributeChange));

		attributeChange.setEquivalence(equivalenceValue);
		assertTrue(notified);
		notified = false;
		assertSame(equivalenceValue, attributeChange.getEquivalence());
		assertSame(attributeChange.getEquivalence(), attributeChange.eGet(feature));
		assertSame(attributeChange.getEquivalence(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));
		assertTrue(equivalenceValue.getDifferences().contains(attributeChange));

		attributeChange.eSet(feature, equivalenceValue);
		assertTrue(notified);
		notified = false;
		assertSame(equivalenceValue, attributeChange.getEquivalence());
		assertSame(attributeChange.getEquivalence(), attributeChange.eGet(feature));
		assertSame(attributeChange.getEquivalence(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));
		assertTrue(equivalenceValue.getDifferences().contains(attributeChange));

		attributeChange.setEquivalence(null);
		assertTrue(notified);
		notified = false;
		assertNull(attributeChange.getEquivalence());
		assertSame(feature.getDefaultValue(), attributeChange.getEquivalence());
		assertSame(attributeChange.getEquivalence(), attributeChange.eGet(feature));
		assertSame(attributeChange.getEquivalence(), attributeChange.eGet(feature, false));
		assertFalse(attributeChange.eIsSet(feature));
		assertFalse(equivalenceValue.getDifferences().contains(attributeChange));
	}

	/**
	 * Tests the behavior of reference <code>conflict</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testConflict() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Conflict();
		AttributeChange attributeChange = CompareFactory.eINSTANCE.createAttributeChange();
		attributeChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Conflict conflictValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createConflict();

		assertFalse(attributeChange.eIsSet(feature));
		assertNull(attributeChange.getConflict());

		attributeChange.setConflict(conflictValue);
		assertTrue(notified);
		notified = false;
		assertSame(conflictValue, attributeChange.getConflict());
		assertSame(attributeChange.getConflict(), attributeChange.eGet(feature));
		assertSame(attributeChange.getConflict(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));
		assertTrue(conflictValue.getDifferences().contains(attributeChange));

		attributeChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertNull(attributeChange.getConflict());
		assertSame(attributeChange.getConflict(), attributeChange.eGet(feature));
		assertSame(attributeChange.getConflict(), attributeChange.eGet(feature, false));
		assertFalse(attributeChange.eIsSet(feature));
		assertFalse(conflictValue.getDifferences().contains(attributeChange));

		attributeChange.setConflict(conflictValue);
		assertTrue(notified);
		notified = false;
		assertSame(conflictValue, attributeChange.getConflict());
		assertSame(attributeChange.getConflict(), attributeChange.eGet(feature));
		assertSame(attributeChange.getConflict(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));
		assertTrue(conflictValue.getDifferences().contains(attributeChange));

		attributeChange.eSet(feature, conflictValue);
		assertTrue(notified);
		notified = false;
		assertSame(conflictValue, attributeChange.getConflict());
		assertSame(attributeChange.getConflict(), attributeChange.eGet(feature));
		assertSame(attributeChange.getConflict(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));
		assertTrue(conflictValue.getDifferences().contains(attributeChange));

		attributeChange.setConflict(null);
		assertTrue(notified);
		notified = false;
		assertNull(attributeChange.getConflict());
		assertSame(feature.getDefaultValue(), attributeChange.getConflict());
		assertSame(attributeChange.getConflict(), attributeChange.eGet(feature));
		assertSame(attributeChange.getConflict(), attributeChange.eGet(feature, false));
		assertFalse(attributeChange.eIsSet(feature));
		assertFalse(conflictValue.getDifferences().contains(attributeChange));
	}

	/**
	 * Tests the behavior of reference <code>attribute</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testAttribute() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE
				.getAttributeChange_Attribute();
		AttributeChange attributeChange = CompareFactory.eINSTANCE.createAttributeChange();
		attributeChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.ecore.EAttribute attributeValue = org.eclipse.emf.ecore.EcoreFactory.eINSTANCE
				.createEAttribute();

		assertFalse(attributeChange.eIsSet(feature));
		assertNull(attributeChange.getAttribute());

		attributeChange.setAttribute(attributeValue);
		assertTrue(notified);
		notified = false;
		assertSame(attributeValue, attributeChange.getAttribute());
		assertSame(attributeChange.getAttribute(), attributeChange.eGet(feature));
		assertSame(attributeChange.getAttribute(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));

		attributeChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertNull(attributeChange.getAttribute());
		assertSame(attributeChange.getAttribute(), attributeChange.eGet(feature));
		assertSame(attributeChange.getAttribute(), attributeChange.eGet(feature, false));
		assertFalse(attributeChange.eIsSet(feature));

		attributeChange.setAttribute(attributeValue);
		assertTrue(notified);
		notified = false;
		assertSame(attributeValue, attributeChange.getAttribute());
		assertSame(attributeChange.getAttribute(), attributeChange.eGet(feature));
		assertSame(attributeChange.getAttribute(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));

		attributeChange.eSet(feature, attributeValue);
		assertTrue(notified);
		notified = false;
		assertSame(attributeValue, attributeChange.getAttribute());
		assertSame(attributeChange.getAttribute(), attributeChange.eGet(feature));
		assertSame(attributeChange.getAttribute(), attributeChange.eGet(feature, false));
		assertTrue(attributeChange.eIsSet(feature));

		attributeChange.setAttribute(null);
		assertTrue(notified);
		notified = false;
		assertNull(attributeChange.getAttribute());
		assertSame(feature.getDefaultValue(), attributeChange.getAttribute());
		assertSame(attributeChange.getAttribute(), attributeChange.eGet(feature));
		assertSame(attributeChange.getAttribute(), attributeChange.eGet(feature, false));
		assertFalse(attributeChange.eIsSet(feature));
	}

	/**
	 * Tests the behavior of attribute <code>kind</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testKind() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Kind();
		AttributeChange attributeChange = CompareFactory.eINSTANCE.createAttributeChange();
		attributeChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.DifferenceKind kindValue = (org.eclipse.emf.compare.DifferenceKind)feature
				.getDefaultValue();
		for (org.eclipse.emf.compare.DifferenceKind aDifferenceKind : org.eclipse.emf.compare.DifferenceKind.VALUES) {
			if (kindValue.getValue() != aDifferenceKind.getValue()) {
				kindValue = aDifferenceKind;
				break;
			}
		}

		assertFalse(attributeChange.eIsSet(feature));
		assertEquals(feature.getDefaultValue(), attributeChange.getKind());

		attributeChange.setKind(kindValue);
		assertTrue(notified);
		notified = false;
		assertEquals(kindValue, attributeChange.getKind());
		assertEquals(attributeChange.getKind(), attributeChange.eGet(feature));
		assertTrue(attributeChange.eIsSet(feature));

		attributeChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), attributeChange.getKind());
		assertEquals(attributeChange.getKind(), attributeChange.eGet(feature));
		assertFalse(attributeChange.eIsSet(feature));

		attributeChange.eSet(feature, kindValue);
		assertTrue(notified);
		notified = false;
		assertEquals(kindValue, attributeChange.getKind());
		assertEquals(attributeChange.getKind(), attributeChange.eGet(feature));
		assertTrue(attributeChange.eIsSet(feature));

		attributeChange.setKind(null);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), attributeChange.getKind());
		assertEquals(attributeChange.getKind(), attributeChange.eGet(feature));
		assertFalse(attributeChange.eIsSet(feature));
	}

	/**
	 * Tests the behavior of attribute <code>source</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testSource() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Source();
		AttributeChange attributeChange = CompareFactory.eINSTANCE.createAttributeChange();
		attributeChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.DifferenceSource sourceValue = (org.eclipse.emf.compare.DifferenceSource)feature
				.getDefaultValue();
		for (org.eclipse.emf.compare.DifferenceSource aDifferenceSource : org.eclipse.emf.compare.DifferenceSource.VALUES) {
			if (sourceValue.getValue() != aDifferenceSource.getValue()) {
				sourceValue = aDifferenceSource;
				break;
			}
		}

		assertFalse(attributeChange.eIsSet(feature));
		assertEquals(feature.getDefaultValue(), attributeChange.getSource());

		attributeChange.setSource(sourceValue);
		assertTrue(notified);
		notified = false;
		assertEquals(sourceValue, attributeChange.getSource());
		assertEquals(attributeChange.getSource(), attributeChange.eGet(feature));
		assertTrue(attributeChange.eIsSet(feature));

		attributeChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), attributeChange.getSource());
		assertEquals(attributeChange.getSource(), attributeChange.eGet(feature));
		assertFalse(attributeChange.eIsSet(feature));

		attributeChange.eSet(feature, sourceValue);
		assertTrue(notified);
		notified = false;
		assertEquals(sourceValue, attributeChange.getSource());
		assertEquals(attributeChange.getSource(), attributeChange.eGet(feature));
		assertTrue(attributeChange.eIsSet(feature));

		attributeChange.setSource(null);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), attributeChange.getSource());
		assertEquals(attributeChange.getSource(), attributeChange.eGet(feature));
		assertFalse(attributeChange.eIsSet(feature));
	}

	/**
	 * Tests the behavior of attribute <code>state</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testState() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_State();
		AttributeChange attributeChange = CompareFactory.eINSTANCE.createAttributeChange();
		attributeChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.DifferenceState stateValue = (org.eclipse.emf.compare.DifferenceState)feature
				.getDefaultValue();
		for (org.eclipse.emf.compare.DifferenceState aDifferenceState : org.eclipse.emf.compare.DifferenceState.VALUES) {
			if (stateValue.getValue() != aDifferenceState.getValue()) {
				stateValue = aDifferenceState;
				break;
			}
		}

		assertFalse(attributeChange.eIsSet(feature));
		assertEquals(feature.getDefaultValue(), attributeChange.getState());

		attributeChange.setState(stateValue);
		assertTrue(notified);
		notified = false;
		assertEquals(stateValue, attributeChange.getState());
		assertEquals(attributeChange.getState(), attributeChange.eGet(feature));
		assertTrue(attributeChange.eIsSet(feature));

		attributeChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), attributeChange.getState());
		assertEquals(attributeChange.getState(), attributeChange.eGet(feature));
		assertFalse(attributeChange.eIsSet(feature));

		attributeChange.eSet(feature, stateValue);
		assertTrue(notified);
		notified = false;
		assertEquals(stateValue, attributeChange.getState());
		assertEquals(attributeChange.getState(), attributeChange.eGet(feature));
		assertTrue(attributeChange.eIsSet(feature));

		attributeChange.setState(null);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), attributeChange.getState());
		assertEquals(attributeChange.getState(), attributeChange.eGet(feature));
		assertFalse(attributeChange.eIsSet(feature));
	}

	/**
	 * Tests the behavior of attribute <code>value</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testValue() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE
				.getAttributeChange_Value();
		AttributeChange attributeChange = CompareFactory.eINSTANCE.createAttributeChange();
		attributeChange.eAdapters().add(new MockEAdapter());
		java.lang.Object valueValue = getValueDistinctFromDefault(feature);

		assertFalse(attributeChange.eIsSet(feature));
		assertEquals(feature.getDefaultValue(), attributeChange.getValue());

		attributeChange.setValue(valueValue);
		assertTrue(notified);
		notified = false;
		assertEquals(valueValue, attributeChange.getValue());
		assertEquals(attributeChange.getValue(), attributeChange.eGet(feature));
		assertTrue(attributeChange.eIsSet(feature));

		attributeChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), attributeChange.getValue());
		assertEquals(attributeChange.getValue(), attributeChange.eGet(feature));
		assertFalse(attributeChange.eIsSet(feature));

		attributeChange.eSet(feature, valueValue);
		assertTrue(notified);
		notified = false;
		assertEquals(valueValue, attributeChange.getValue());
		assertEquals(attributeChange.getValue(), attributeChange.eGet(feature));
		assertTrue(attributeChange.eIsSet(feature));

		attributeChange.setValue(null);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), attributeChange.getValue());
		assertEquals(attributeChange.getValue(), attributeChange.eGet(feature));
		assertFalse(attributeChange.eIsSet(feature));
	}

}
