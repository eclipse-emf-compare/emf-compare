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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Test;

/**
 * Tests the behavior of the {@link Comparison} class.
 * 
 * @generated
 */
public class ComparisonTest extends AbstractCompareTest {
	/**
	 * Tests the behavior of reference <code>matchedResources</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testMatchedResources() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE
				.getComparison_MatchedResources();
		Comparison comparison = CompareFactory.eINSTANCE.createComparison();
		comparison.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.MatchResource matchedResourcesValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createMatchResource();
		List<org.eclipse.emf.compare.MatchResource> listMatchedResources = new ArrayList<org.eclipse.emf.compare.MatchResource>(
				1);
		listMatchedResources.add(matchedResourcesValue);

		assertFalse(comparison.eIsSet(feature));
		assertTrue(comparison.getMatchedResources().isEmpty());

		comparison.getMatchedResources().add(matchedResourcesValue);
		assertTrue(notified);
		notified = false;
		assertTrue(comparison.getMatchedResources().contains(matchedResourcesValue));
		assertSame(comparison.getMatchedResources(), comparison.eGet(feature));
		assertSame(comparison.getMatchedResources(), comparison.eGet(feature, false));
		assertTrue(comparison.eIsSet(feature));

		comparison.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(comparison.getMatchedResources().isEmpty());
		assertSame(comparison.getMatchedResources(), comparison.eGet(feature));
		assertSame(comparison.getMatchedResources(), comparison.eGet(feature, false));
		assertFalse(comparison.eIsSet(feature));

		comparison.eSet(feature, listMatchedResources);
		assertTrue(notified);
		notified = false;
		assertTrue(comparison.getMatchedResources().contains(matchedResourcesValue));
		assertSame(comparison.getMatchedResources(), comparison.eGet(feature));
		assertSame(comparison.getMatchedResources(), comparison.eGet(feature, false));
		assertTrue(comparison.eIsSet(feature));
	}

	/**
	 * Tests the behavior of reference <code>matches</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testMatches() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getComparison_Matches();
		Comparison comparison = CompareFactory.eINSTANCE.createComparison();
		comparison.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Match matchesValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createMatch();
		List<org.eclipse.emf.compare.Match> listMatches = new ArrayList<org.eclipse.emf.compare.Match>(1);
		listMatches.add(matchesValue);

		assertFalse(comparison.eIsSet(feature));
		assertTrue(comparison.getMatches().isEmpty());

		comparison.getMatches().add(matchesValue);
		assertTrue(notified);
		notified = false;
		assertTrue(comparison.getMatches().contains(matchesValue));
		assertSame(comparison.getMatches(), comparison.eGet(feature));
		assertSame(comparison.getMatches(), comparison.eGet(feature, false));
		assertTrue(comparison.eIsSet(feature));

		comparison.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(comparison.getMatches().isEmpty());
		assertSame(comparison.getMatches(), comparison.eGet(feature));
		assertSame(comparison.getMatches(), comparison.eGet(feature, false));
		assertFalse(comparison.eIsSet(feature));

		comparison.eSet(feature, listMatches);
		assertTrue(notified);
		notified = false;
		assertTrue(comparison.getMatches().contains(matchesValue));
		assertSame(comparison.getMatches(), comparison.eGet(feature));
		assertSame(comparison.getMatches(), comparison.eGet(feature, false));
		assertTrue(comparison.eIsSet(feature));
	}

	/**
	 * Tests the behavior of reference <code>conflicts</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testConflicts() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE
				.getComparison_Conflicts();
		Comparison comparison = CompareFactory.eINSTANCE.createComparison();
		comparison.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Conflict conflictsValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createConflict();
		List<org.eclipse.emf.compare.Conflict> listConflicts = new ArrayList<org.eclipse.emf.compare.Conflict>(
				1);
		listConflicts.add(conflictsValue);

		assertFalse(comparison.eIsSet(feature));
		assertTrue(comparison.getConflicts().isEmpty());

		comparison.getConflicts().add(conflictsValue);
		assertTrue(notified);
		notified = false;
		assertTrue(comparison.getConflicts().contains(conflictsValue));
		assertSame(comparison.getConflicts(), comparison.eGet(feature));
		assertSame(comparison.getConflicts(), comparison.eGet(feature, false));
		assertTrue(comparison.eIsSet(feature));

		comparison.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(comparison.getConflicts().isEmpty());
		assertSame(comparison.getConflicts(), comparison.eGet(feature));
		assertSame(comparison.getConflicts(), comparison.eGet(feature, false));
		assertFalse(comparison.eIsSet(feature));

		comparison.eSet(feature, listConflicts);
		assertTrue(notified);
		notified = false;
		assertTrue(comparison.getConflicts().contains(conflictsValue));
		assertSame(comparison.getConflicts(), comparison.eGet(feature));
		assertSame(comparison.getConflicts(), comparison.eGet(feature, false));
		assertTrue(comparison.eIsSet(feature));
	}

	/**
	 * Tests the behavior of reference <code>equivalences</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testEquivalences() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE
				.getComparison_Equivalences();
		Comparison comparison = CompareFactory.eINSTANCE.createComparison();
		comparison.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Equivalence equivalencesValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createEquivalence();
		List<org.eclipse.emf.compare.Equivalence> listEquivalences = new ArrayList<org.eclipse.emf.compare.Equivalence>(
				1);
		listEquivalences.add(equivalencesValue);

		assertFalse(comparison.eIsSet(feature));
		assertTrue(comparison.getEquivalences().isEmpty());

		comparison.getEquivalences().add(equivalencesValue);
		assertTrue(notified);
		notified = false;
		assertTrue(comparison.getEquivalences().contains(equivalencesValue));
		assertSame(comparison.getEquivalences(), comparison.eGet(feature));
		assertSame(comparison.getEquivalences(), comparison.eGet(feature, false));
		assertTrue(comparison.eIsSet(feature));

		comparison.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(comparison.getEquivalences().isEmpty());
		assertSame(comparison.getEquivalences(), comparison.eGet(feature));
		assertSame(comparison.getEquivalences(), comparison.eGet(feature, false));
		assertFalse(comparison.eIsSet(feature));

		comparison.eSet(feature, listEquivalences);
		assertTrue(notified);
		notified = false;
		assertTrue(comparison.getEquivalences().contains(equivalencesValue));
		assertSame(comparison.getEquivalences(), comparison.eGet(feature));
		assertSame(comparison.getEquivalences(), comparison.eGet(feature, false));
		assertTrue(comparison.eIsSet(feature));
	}

	/**
	 * Tests the behavior of attribute <code>threeWay</code>'s accessors.
	 * 
	 * @generated
	 */
	// generated test. Suppress the warning
	@SuppressWarnings("boxing")
	@Test
	public void testThreeWay() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE
				.getComparison_ThreeWay();
		Comparison comparison = CompareFactory.eINSTANCE.createComparison();
		comparison.eAdapters().add(new MockEAdapter());
		boolean threeWayValue = getBooleanDistinctFromDefault(feature);

		assertFalse(comparison.eIsSet(feature));
		assertEquals(((Boolean)feature.getDefaultValue()).booleanValue(), ((Boolean)comparison.isThreeWay())
				.booleanValue());

		comparison.setThreeWay(threeWayValue);
		assertTrue(notified);
		notified = false;
		assertEquals(threeWayValue, ((Boolean)comparison.isThreeWay()).booleanValue());
		assertEquals(((Boolean)comparison.isThreeWay()).booleanValue(), ((Boolean)comparison.eGet(feature))
				.booleanValue());
		assertTrue(comparison.eIsSet(feature));

		comparison.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertEquals(((Boolean)feature.getDefaultValue()).booleanValue(), ((Boolean)comparison.isThreeWay())
				.booleanValue());
		assertEquals(((Boolean)comparison.isThreeWay()).booleanValue(), ((Boolean)comparison.eGet(feature))
				.booleanValue());
		assertFalse(comparison.eIsSet(feature));

		comparison.eSet(feature, threeWayValue);
		assertTrue(notified);
		notified = false;
		assertEquals(threeWayValue, ((Boolean)comparison.isThreeWay()).booleanValue());
		assertEquals(((Boolean)comparison.isThreeWay()).booleanValue(), ((Boolean)comparison.eGet(feature))
				.booleanValue());
		assertTrue(comparison.eIsSet(feature));

		comparison.setThreeWay(((Boolean)feature.getDefaultValue()).booleanValue());
		assertTrue(notified);
		notified = false;
		assertEquals(((Boolean)feature.getDefaultValue()).booleanValue(), ((Boolean)comparison.isThreeWay())
				.booleanValue());
		assertEquals(((Boolean)comparison.isThreeWay()).booleanValue(), ((Boolean)comparison.eGet(feature))
				.booleanValue());
		assertFalse(comparison.eIsSet(feature));
	}

}
