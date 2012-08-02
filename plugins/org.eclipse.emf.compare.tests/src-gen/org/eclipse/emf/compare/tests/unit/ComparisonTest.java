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

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

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

}
