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
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Test;

/**
 * Tests the behavior of the {@link ResourceAttachmentChange} class.
 * 
 * @generated
 */
public class ResourceAttachmentChangeTest extends AbstractCompareTest {
	/**
	 * Tests the behavior of reference <code>requires</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testRequires() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Requires();
		ResourceAttachmentChange resourceAttachmentChange = CompareFactory.eINSTANCE
				.createResourceAttachmentChange();
		resourceAttachmentChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Diff requiresValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createDiff();
		List<org.eclipse.emf.compare.Diff> listRequires = new ArrayList<org.eclipse.emf.compare.Diff>(1);
		listRequires.add(requiresValue);

		assertFalse(resourceAttachmentChange.eIsSet(feature));
		assertTrue(resourceAttachmentChange.getRequires().isEmpty());

		resourceAttachmentChange.getRequires().add(requiresValue);
		assertTrue(notified);
		notified = false;
		assertTrue(resourceAttachmentChange.getRequires().contains(requiresValue));
		assertSame(resourceAttachmentChange.getRequires(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getRequires(), resourceAttachmentChange.eGet(feature, false));
		assertTrue(resourceAttachmentChange.eIsSet(feature));
		assertTrue(requiresValue.getRequiredBy().contains(resourceAttachmentChange));

		resourceAttachmentChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(resourceAttachmentChange.getRequires().isEmpty());
		assertSame(resourceAttachmentChange.getRequires(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getRequires(), resourceAttachmentChange.eGet(feature, false));
		assertFalse(resourceAttachmentChange.eIsSet(feature));
		assertFalse(requiresValue.getRequiredBy().contains(resourceAttachmentChange));

		resourceAttachmentChange.eSet(feature, listRequires);
		assertTrue(notified);
		notified = false;
		assertTrue(resourceAttachmentChange.getRequires().contains(requiresValue));
		assertSame(resourceAttachmentChange.getRequires(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getRequires(), resourceAttachmentChange.eGet(feature, false));
		assertTrue(resourceAttachmentChange.eIsSet(feature));
		assertTrue(requiresValue.getRequiredBy().contains(resourceAttachmentChange));
	}

	/**
	 * Tests the behavior of reference <code>requiredBy</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testRequiredBy() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_RequiredBy();
		ResourceAttachmentChange resourceAttachmentChange = CompareFactory.eINSTANCE
				.createResourceAttachmentChange();
		resourceAttachmentChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Diff requiredByValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createDiff();
		List<org.eclipse.emf.compare.Diff> listRequiredBy = new ArrayList<org.eclipse.emf.compare.Diff>(1);
		listRequiredBy.add(requiredByValue);

		assertFalse(resourceAttachmentChange.eIsSet(feature));
		assertTrue(resourceAttachmentChange.getRequiredBy().isEmpty());

		resourceAttachmentChange.getRequiredBy().add(requiredByValue);
		assertTrue(notified);
		notified = false;
		assertTrue(resourceAttachmentChange.getRequiredBy().contains(requiredByValue));
		assertSame(resourceAttachmentChange.getRequiredBy(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getRequiredBy(), resourceAttachmentChange.eGet(feature, false));
		assertTrue(resourceAttachmentChange.eIsSet(feature));
		assertTrue(requiredByValue.getRequires().contains(resourceAttachmentChange));

		resourceAttachmentChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(resourceAttachmentChange.getRequiredBy().isEmpty());
		assertSame(resourceAttachmentChange.getRequiredBy(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getRequiredBy(), resourceAttachmentChange.eGet(feature, false));
		assertFalse(resourceAttachmentChange.eIsSet(feature));
		assertFalse(requiredByValue.getRequires().contains(resourceAttachmentChange));

		resourceAttachmentChange.eSet(feature, listRequiredBy);
		assertTrue(notified);
		notified = false;
		assertTrue(resourceAttachmentChange.getRequiredBy().contains(requiredByValue));
		assertSame(resourceAttachmentChange.getRequiredBy(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getRequiredBy(), resourceAttachmentChange.eGet(feature, false));
		assertTrue(resourceAttachmentChange.eIsSet(feature));
		assertTrue(requiredByValue.getRequires().contains(resourceAttachmentChange));
	}

	/**
	 * Tests the behavior of reference <code>refines</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testRefines() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Refines();
		ResourceAttachmentChange resourceAttachmentChange = CompareFactory.eINSTANCE
				.createResourceAttachmentChange();
		resourceAttachmentChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Diff refinesValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createDiff();
		List<org.eclipse.emf.compare.Diff> listRefines = new ArrayList<org.eclipse.emf.compare.Diff>(1);
		listRefines.add(refinesValue);

		assertFalse(resourceAttachmentChange.eIsSet(feature));
		assertTrue(resourceAttachmentChange.getRefines().isEmpty());

		resourceAttachmentChange.getRefines().add(refinesValue);
		assertTrue(notified);
		notified = false;
		assertTrue(resourceAttachmentChange.getRefines().contains(refinesValue));
		assertSame(resourceAttachmentChange.getRefines(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getRefines(), resourceAttachmentChange.eGet(feature, false));
		assertTrue(resourceAttachmentChange.eIsSet(feature));
		assertTrue(refinesValue.getRefinedBy().contains(resourceAttachmentChange));

		resourceAttachmentChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(resourceAttachmentChange.getRefines().isEmpty());
		assertSame(resourceAttachmentChange.getRefines(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getRefines(), resourceAttachmentChange.eGet(feature, false));
		assertFalse(resourceAttachmentChange.eIsSet(feature));
		assertFalse(refinesValue.getRefinedBy().contains(resourceAttachmentChange));

		resourceAttachmentChange.eSet(feature, listRefines);
		assertTrue(notified);
		notified = false;
		assertTrue(resourceAttachmentChange.getRefines().contains(refinesValue));
		assertSame(resourceAttachmentChange.getRefines(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getRefines(), resourceAttachmentChange.eGet(feature, false));
		assertTrue(resourceAttachmentChange.eIsSet(feature));
		assertTrue(refinesValue.getRefinedBy().contains(resourceAttachmentChange));
	}

	/**
	 * Tests the behavior of reference <code>refinedBy</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testRefinedBy() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_RefinedBy();
		ResourceAttachmentChange resourceAttachmentChange = CompareFactory.eINSTANCE
				.createResourceAttachmentChange();
		resourceAttachmentChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Diff refinedByValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createDiff();
		List<org.eclipse.emf.compare.Diff> listRefinedBy = new ArrayList<org.eclipse.emf.compare.Diff>(1);
		listRefinedBy.add(refinedByValue);

		assertFalse(resourceAttachmentChange.eIsSet(feature));
		assertTrue(resourceAttachmentChange.getRefinedBy().isEmpty());

		resourceAttachmentChange.getRefinedBy().add(refinedByValue);
		assertTrue(notified);
		notified = false;
		assertTrue(resourceAttachmentChange.getRefinedBy().contains(refinedByValue));
		assertSame(resourceAttachmentChange.getRefinedBy(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getRefinedBy(), resourceAttachmentChange.eGet(feature, false));
		assertTrue(resourceAttachmentChange.eIsSet(feature));
		assertTrue(refinedByValue.getRefines().contains(resourceAttachmentChange));

		resourceAttachmentChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(resourceAttachmentChange.getRefinedBy().isEmpty());
		assertSame(resourceAttachmentChange.getRefinedBy(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getRefinedBy(), resourceAttachmentChange.eGet(feature, false));
		assertFalse(resourceAttachmentChange.eIsSet(feature));
		assertFalse(refinedByValue.getRefines().contains(resourceAttachmentChange));

		resourceAttachmentChange.eSet(feature, listRefinedBy);
		assertTrue(notified);
		notified = false;
		assertTrue(resourceAttachmentChange.getRefinedBy().contains(refinedByValue));
		assertSame(resourceAttachmentChange.getRefinedBy(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getRefinedBy(), resourceAttachmentChange.eGet(feature, false));
		assertTrue(resourceAttachmentChange.eIsSet(feature));
		assertTrue(refinedByValue.getRefines().contains(resourceAttachmentChange));
	}

	/**
	 * Tests the behavior of reference <code>match</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testMatch() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Match();
		ResourceAttachmentChange resourceAttachmentChange = CompareFactory.eINSTANCE
				.createResourceAttachmentChange();
		resourceAttachmentChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Match matchValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createMatch();

		assertFalse(resourceAttachmentChange.eIsSet(feature));
		assertNull(resourceAttachmentChange.getMatch());

		resourceAttachmentChange.setMatch(matchValue);
		assertTrue(notified);
		notified = false;
		assertSame(matchValue, resourceAttachmentChange.getMatch());
		assertSame(resourceAttachmentChange.getMatch(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getMatch(), resourceAttachmentChange.eGet(feature, false));
		assertTrue(resourceAttachmentChange.eIsSet(feature));
		assertTrue(matchValue.getDifferences().contains(resourceAttachmentChange));

		resourceAttachmentChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertNull(resourceAttachmentChange.getMatch());
		assertSame(resourceAttachmentChange.getMatch(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getMatch(), resourceAttachmentChange.eGet(feature, false));
		assertFalse(resourceAttachmentChange.eIsSet(feature));
		assertFalse(matchValue.getDifferences().contains(resourceAttachmentChange));

		resourceAttachmentChange.setMatch(matchValue);
		assertTrue(notified);
		notified = false;
		assertSame(matchValue, resourceAttachmentChange.getMatch());
		assertSame(resourceAttachmentChange.getMatch(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getMatch(), resourceAttachmentChange.eGet(feature, false));
		assertTrue(resourceAttachmentChange.eIsSet(feature));
		assertTrue(matchValue.getDifferences().contains(resourceAttachmentChange));

		resourceAttachmentChange.eSet(feature, matchValue);
		assertTrue(notified);
		notified = false;
		assertSame(matchValue, resourceAttachmentChange.getMatch());
		assertSame(resourceAttachmentChange.getMatch(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getMatch(), resourceAttachmentChange.eGet(feature, false));
		assertTrue(resourceAttachmentChange.eIsSet(feature));
		assertTrue(matchValue.getDifferences().contains(resourceAttachmentChange));

		resourceAttachmentChange.setMatch(null);
		assertTrue(notified);
		notified = false;
		assertNull(resourceAttachmentChange.getMatch());
		assertSame(feature.getDefaultValue(), resourceAttachmentChange.getMatch());
		assertSame(resourceAttachmentChange.getMatch(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getMatch(), resourceAttachmentChange.eGet(feature, false));
		assertFalse(resourceAttachmentChange.eIsSet(feature));
		assertFalse(matchValue.getDifferences().contains(resourceAttachmentChange));
	}

	/**
	 * Tests the behavior of reference <code>equivalentDiffs</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testEquivalence() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Equivalence();
		ResourceAttachmentChange resourceAttachmentChange = CompareFactory.eINSTANCE
				.createResourceAttachmentChange();
		resourceAttachmentChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Equivalence equivalentDiffsValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createEquivalence();

		assertFalse(resourceAttachmentChange.eIsSet(feature));
		assertNull(resourceAttachmentChange.getEquivalence());

		resourceAttachmentChange.setEquivalence(equivalentDiffsValue);
		assertTrue(notified);
		notified = false;
		assertSame(equivalentDiffsValue, resourceAttachmentChange.getEquivalence());
		assertSame(resourceAttachmentChange.getEquivalence(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getEquivalence(), resourceAttachmentChange.eGet(feature, false));
		assertTrue(resourceAttachmentChange.eIsSet(feature));
		assertTrue(equivalentDiffsValue.getDifferences().contains(resourceAttachmentChange));

		resourceAttachmentChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertNull(resourceAttachmentChange.getEquivalence());
		assertSame(resourceAttachmentChange.getEquivalence(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getEquivalence(), resourceAttachmentChange.eGet(feature, false));
		assertFalse(resourceAttachmentChange.eIsSet(feature));
		assertFalse(equivalentDiffsValue.getDifferences().contains(resourceAttachmentChange));

		resourceAttachmentChange.setEquivalence(equivalentDiffsValue);
		assertTrue(notified);
		notified = false;
		assertSame(equivalentDiffsValue, resourceAttachmentChange.getEquivalence());
		assertSame(resourceAttachmentChange.getEquivalence(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getEquivalence(), resourceAttachmentChange.eGet(feature, false));
		assertTrue(resourceAttachmentChange.eIsSet(feature));
		assertTrue(equivalentDiffsValue.getDifferences().contains(resourceAttachmentChange));

		resourceAttachmentChange.eSet(feature, equivalentDiffsValue);
		assertTrue(notified);
		notified = false;
		assertSame(equivalentDiffsValue, resourceAttachmentChange.getEquivalence());
		assertSame(resourceAttachmentChange.getEquivalence(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getEquivalence(), resourceAttachmentChange.eGet(feature, false));
		assertTrue(resourceAttachmentChange.eIsSet(feature));
		assertTrue(equivalentDiffsValue.getDifferences().contains(resourceAttachmentChange));

		resourceAttachmentChange.setEquivalence(null);
		assertTrue(notified);
		notified = false;
		assertNull(resourceAttachmentChange.getEquivalence());
		assertSame(feature.getDefaultValue(), resourceAttachmentChange.getEquivalence());
		assertSame(resourceAttachmentChange.getEquivalence(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getEquivalence(), resourceAttachmentChange.eGet(feature, false));
		assertFalse(resourceAttachmentChange.eIsSet(feature));
		assertFalse(equivalentDiffsValue.getDifferences().contains(resourceAttachmentChange));
	}

	/**
	 * Tests the behavior of reference <code>conflict</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testConflict() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Conflict();
		ResourceAttachmentChange resourceAttachmentChange = CompareFactory.eINSTANCE
				.createResourceAttachmentChange();
		resourceAttachmentChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Conflict conflictValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createConflict();

		assertFalse(resourceAttachmentChange.eIsSet(feature));
		assertNull(resourceAttachmentChange.getConflict());

		resourceAttachmentChange.setConflict(conflictValue);
		assertTrue(notified);
		notified = false;
		assertSame(conflictValue, resourceAttachmentChange.getConflict());
		assertSame(resourceAttachmentChange.getConflict(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getConflict(), resourceAttachmentChange.eGet(feature, false));
		assertTrue(resourceAttachmentChange.eIsSet(feature));
		assertTrue(conflictValue.getDifferences().contains(resourceAttachmentChange));

		resourceAttachmentChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertNull(resourceAttachmentChange.getConflict());
		assertSame(resourceAttachmentChange.getConflict(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getConflict(), resourceAttachmentChange.eGet(feature, false));
		assertFalse(resourceAttachmentChange.eIsSet(feature));
		assertFalse(conflictValue.getDifferences().contains(resourceAttachmentChange));

		resourceAttachmentChange.setConflict(conflictValue);
		assertTrue(notified);
		notified = false;
		assertSame(conflictValue, resourceAttachmentChange.getConflict());
		assertSame(resourceAttachmentChange.getConflict(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getConflict(), resourceAttachmentChange.eGet(feature, false));
		assertTrue(resourceAttachmentChange.eIsSet(feature));
		assertTrue(conflictValue.getDifferences().contains(resourceAttachmentChange));

		resourceAttachmentChange.eSet(feature, conflictValue);
		assertTrue(notified);
		notified = false;
		assertSame(conflictValue, resourceAttachmentChange.getConflict());
		assertSame(resourceAttachmentChange.getConflict(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getConflict(), resourceAttachmentChange.eGet(feature, false));
		assertTrue(resourceAttachmentChange.eIsSet(feature));
		assertTrue(conflictValue.getDifferences().contains(resourceAttachmentChange));

		resourceAttachmentChange.setConflict(null);
		assertTrue(notified);
		notified = false;
		assertNull(resourceAttachmentChange.getConflict());
		assertSame(feature.getDefaultValue(), resourceAttachmentChange.getConflict());
		assertSame(resourceAttachmentChange.getConflict(), resourceAttachmentChange.eGet(feature));
		assertSame(resourceAttachmentChange.getConflict(), resourceAttachmentChange.eGet(feature, false));
		assertFalse(resourceAttachmentChange.eIsSet(feature));
		assertFalse(conflictValue.getDifferences().contains(resourceAttachmentChange));
	}

	/**
	 * Tests the behavior of attribute <code>kind</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testKind() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Kind();
		ResourceAttachmentChange resourceAttachmentChange = CompareFactory.eINSTANCE
				.createResourceAttachmentChange();
		resourceAttachmentChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.DifferenceKind kindValue = (org.eclipse.emf.compare.DifferenceKind)feature
				.getDefaultValue();
		for (org.eclipse.emf.compare.DifferenceKind aDifferenceKind : org.eclipse.emf.compare.DifferenceKind.VALUES) {
			if (kindValue.getValue() != aDifferenceKind.getValue()) {
				kindValue = aDifferenceKind;
				break;
			}
		}

		assertFalse(resourceAttachmentChange.eIsSet(feature));
		assertEquals(feature.getDefaultValue(), resourceAttachmentChange.getKind());

		resourceAttachmentChange.setKind(kindValue);
		assertTrue(notified);
		notified = false;
		assertEquals(kindValue, resourceAttachmentChange.getKind());
		assertEquals(resourceAttachmentChange.getKind(), resourceAttachmentChange.eGet(feature));
		assertTrue(resourceAttachmentChange.eIsSet(feature));

		resourceAttachmentChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), resourceAttachmentChange.getKind());
		assertEquals(resourceAttachmentChange.getKind(), resourceAttachmentChange.eGet(feature));
		assertFalse(resourceAttachmentChange.eIsSet(feature));

		resourceAttachmentChange.eSet(feature, kindValue);
		assertTrue(notified);
		notified = false;
		assertEquals(kindValue, resourceAttachmentChange.getKind());
		assertEquals(resourceAttachmentChange.getKind(), resourceAttachmentChange.eGet(feature));
		assertTrue(resourceAttachmentChange.eIsSet(feature));

		resourceAttachmentChange.setKind(null);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), resourceAttachmentChange.getKind());
		assertEquals(resourceAttachmentChange.getKind(), resourceAttachmentChange.eGet(feature));
		assertFalse(resourceAttachmentChange.eIsSet(feature));
	}

	/**
	 * Tests the behavior of attribute <code>resourceURI</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testResourceURI() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE
				.getResourceAttachmentChange_ResourceURI();
		ResourceAttachmentChange resourceAttachmentChange = CompareFactory.eINSTANCE
				.createResourceAttachmentChange();
		resourceAttachmentChange.eAdapters().add(new MockEAdapter());
		java.lang.String resourceURIValue = (java.lang.String)getValueDistinctFromDefault(feature);

		assertFalse(resourceAttachmentChange.eIsSet(feature));
		assertEquals(feature.getDefaultValue(), resourceAttachmentChange.getResourceURI());

		resourceAttachmentChange.setResourceURI(resourceURIValue);
		assertTrue(notified);
		notified = false;
		assertEquals(resourceURIValue, resourceAttachmentChange.getResourceURI());
		assertEquals(resourceAttachmentChange.getResourceURI(), resourceAttachmentChange.eGet(feature));
		assertTrue(resourceAttachmentChange.eIsSet(feature));

		resourceAttachmentChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), resourceAttachmentChange.getResourceURI());
		assertEquals(resourceAttachmentChange.getResourceURI(), resourceAttachmentChange.eGet(feature));
		assertFalse(resourceAttachmentChange.eIsSet(feature));

		resourceAttachmentChange.eSet(feature, resourceURIValue);
		assertTrue(notified);
		notified = false;
		assertEquals(resourceURIValue, resourceAttachmentChange.getResourceURI());
		assertEquals(resourceAttachmentChange.getResourceURI(), resourceAttachmentChange.eGet(feature));
		assertTrue(resourceAttachmentChange.eIsSet(feature));

		resourceAttachmentChange.setResourceURI(null);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), resourceAttachmentChange.getResourceURI());
		assertEquals(resourceAttachmentChange.getResourceURI(), resourceAttachmentChange.eGet(feature));
		assertFalse(resourceAttachmentChange.eIsSet(feature));
	}

}
