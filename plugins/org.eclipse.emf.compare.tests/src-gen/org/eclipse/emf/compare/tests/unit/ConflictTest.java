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
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Test;

/**
 * Tests the behavior of the {@link Conflict} class.
 * 
 * @generated
 */
public class ConflictTest extends AbstractCompareTest {
	/**
	 * Tests the behavior of reference <code>differences</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testDifferences() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE
				.getConflict_Differences();
		Conflict conflict = CompareFactory.eINSTANCE.createConflict();
		conflict.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Diff differencesValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createDiff();
		List<org.eclipse.emf.compare.Diff> listDifferences = new ArrayList<org.eclipse.emf.compare.Diff>(1);
		listDifferences.add(differencesValue);

		assertFalse(conflict.eIsSet(feature));
		assertTrue(conflict.getDifferences().isEmpty());

		conflict.getDifferences().add(differencesValue);
		assertTrue(notified);
		notified = false;
		assertTrue(conflict.getDifferences().contains(differencesValue));
		assertSame(conflict.getDifferences(), conflict.eGet(feature));
		assertSame(conflict.getDifferences(), conflict.eGet(feature, false));
		assertTrue(conflict.eIsSet(feature));
		assertTrue(differencesValue.getConflict() == conflict);

		conflict.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(conflict.getDifferences().isEmpty());
		assertSame(conflict.getDifferences(), conflict.eGet(feature));
		assertSame(conflict.getDifferences(), conflict.eGet(feature, false));
		assertFalse(conflict.eIsSet(feature));
		assertFalse(differencesValue.getConflict() == conflict);

		conflict.eSet(feature, listDifferences);
		assertTrue(notified);
		notified = false;
		assertTrue(conflict.getDifferences().contains(differencesValue));
		assertSame(conflict.getDifferences(), conflict.eGet(feature));
		assertSame(conflict.getDifferences(), conflict.eGet(feature, false));
		assertTrue(conflict.eIsSet(feature));
		assertTrue(differencesValue.getConflict() == conflict);
	}

	/**
	 * Tests the behavior of attribute <code>kind</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testKind() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getConflict_Kind();
		Conflict conflict = CompareFactory.eINSTANCE.createConflict();
		conflict.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.ConflictKind kindValue = (org.eclipse.emf.compare.ConflictKind)feature
				.getDefaultValue();
		for (org.eclipse.emf.compare.ConflictKind aConflictKind : org.eclipse.emf.compare.ConflictKind.VALUES) {
			if (kindValue.getValue() != aConflictKind.getValue()) {
				kindValue = aConflictKind;
				break;
			}
		}

		assertFalse(conflict.eIsSet(feature));
		assertEquals(feature.getDefaultValue(), conflict.getKind());

		conflict.setKind(kindValue);
		assertTrue(notified);
		notified = false;
		assertEquals(kindValue, conflict.getKind());
		assertEquals(conflict.getKind(), conflict.eGet(feature));
		assertTrue(conflict.eIsSet(feature));

		conflict.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), conflict.getKind());
		assertEquals(conflict.getKind(), conflict.eGet(feature));
		assertFalse(conflict.eIsSet(feature));

		conflict.eSet(feature, kindValue);
		assertTrue(notified);
		notified = false;
		assertEquals(kindValue, conflict.getKind());
		assertEquals(conflict.getKind(), conflict.eGet(feature));
		assertTrue(conflict.eIsSet(feature));

		conflict.setKind(null);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), conflict.getKind());
		assertEquals(conflict.getKind(), conflict.eGet(feature));
		assertFalse(conflict.eIsSet(feature));
	}

}
