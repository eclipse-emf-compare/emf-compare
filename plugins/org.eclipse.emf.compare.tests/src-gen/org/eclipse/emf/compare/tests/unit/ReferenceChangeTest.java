package org.eclipse.emf.compare.tests.unit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Test;

/**
 * Tests the behavior of the {@link ReferenceChange} class.
 * 
 * @generated
 */
public class ReferenceChangeTest extends AbstractCompareTest {
	/**
	 * Tests the behavior of reference <code>requires</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testRequires() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Requires();
		ReferenceChange referenceChange = CompareFactory.eINSTANCE.createReferenceChange();
		referenceChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Diff requiresValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createDiff();
		List<org.eclipse.emf.compare.Diff> listRequires = new ArrayList<org.eclipse.emf.compare.Diff>(1);
		listRequires.add(requiresValue);

		assertFalse(referenceChange.eIsSet(feature));
		assertTrue(referenceChange.getRequires().isEmpty());

		referenceChange.getRequires().add(requiresValue);
		assertTrue(notified);
		notified = false;
		assertTrue(referenceChange.getRequires().contains(requiresValue));
		assertSame(referenceChange.getRequires(), referenceChange.eGet(feature));
		assertSame(referenceChange.getRequires(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));
		assertTrue(requiresValue.getRequiredBy().contains(referenceChange));

		referenceChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(referenceChange.getRequires().isEmpty());
		assertSame(referenceChange.getRequires(), referenceChange.eGet(feature));
		assertSame(referenceChange.getRequires(), referenceChange.eGet(feature, false));
		assertFalse(referenceChange.eIsSet(feature));
		assertFalse(requiresValue.getRequiredBy().contains(referenceChange));

		referenceChange.eSet(feature, listRequires);
		assertTrue(notified);
		notified = false;
		assertTrue(referenceChange.getRequires().contains(requiresValue));
		assertSame(referenceChange.getRequires(), referenceChange.eGet(feature));
		assertSame(referenceChange.getRequires(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));
		assertTrue(requiresValue.getRequiredBy().contains(referenceChange));
	}

	/**
	 * Tests the behavior of reference <code>requiredBy</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testRequiredBy() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_RequiredBy();
		ReferenceChange referenceChange = CompareFactory.eINSTANCE.createReferenceChange();
		referenceChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Diff requiredByValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createDiff();
		List<org.eclipse.emf.compare.Diff> listRequiredBy = new ArrayList<org.eclipse.emf.compare.Diff>(1);
		listRequiredBy.add(requiredByValue);

		assertFalse(referenceChange.eIsSet(feature));
		assertTrue(referenceChange.getRequiredBy().isEmpty());

		referenceChange.getRequiredBy().add(requiredByValue);
		assertTrue(notified);
		notified = false;
		assertTrue(referenceChange.getRequiredBy().contains(requiredByValue));
		assertSame(referenceChange.getRequiredBy(), referenceChange.eGet(feature));
		assertSame(referenceChange.getRequiredBy(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));
		assertTrue(requiredByValue.getRequires().contains(referenceChange));

		referenceChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(referenceChange.getRequiredBy().isEmpty());
		assertSame(referenceChange.getRequiredBy(), referenceChange.eGet(feature));
		assertSame(referenceChange.getRequiredBy(), referenceChange.eGet(feature, false));
		assertFalse(referenceChange.eIsSet(feature));
		assertFalse(requiredByValue.getRequires().contains(referenceChange));

		referenceChange.eSet(feature, listRequiredBy);
		assertTrue(notified);
		notified = false;
		assertTrue(referenceChange.getRequiredBy().contains(requiredByValue));
		assertSame(referenceChange.getRequiredBy(), referenceChange.eGet(feature));
		assertSame(referenceChange.getRequiredBy(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));
		assertTrue(requiredByValue.getRequires().contains(referenceChange));
	}

	/**
	 * Tests the behavior of reference <code>refines</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testRefines() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Refines();
		ReferenceChange referenceChange = CompareFactory.eINSTANCE.createReferenceChange();
		referenceChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Diff refinesValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createDiff();
		List<org.eclipse.emf.compare.Diff> listRefines = new ArrayList<org.eclipse.emf.compare.Diff>(1);
		listRefines.add(refinesValue);

		assertFalse(referenceChange.eIsSet(feature));
		assertTrue(referenceChange.getRefines().isEmpty());

		referenceChange.getRefines().add(refinesValue);
		assertTrue(notified);
		notified = false;
		assertTrue(referenceChange.getRefines().contains(refinesValue));
		assertSame(referenceChange.getRefines(), referenceChange.eGet(feature));
		assertSame(referenceChange.getRefines(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));
		assertTrue(refinesValue.getRefinedBy().contains(referenceChange));

		referenceChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(referenceChange.getRefines().isEmpty());
		assertSame(referenceChange.getRefines(), referenceChange.eGet(feature));
		assertSame(referenceChange.getRefines(), referenceChange.eGet(feature, false));
		assertFalse(referenceChange.eIsSet(feature));
		assertFalse(refinesValue.getRefinedBy().contains(referenceChange));

		referenceChange.eSet(feature, listRefines);
		assertTrue(notified);
		notified = false;
		assertTrue(referenceChange.getRefines().contains(refinesValue));
		assertSame(referenceChange.getRefines(), referenceChange.eGet(feature));
		assertSame(referenceChange.getRefines(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));
		assertTrue(refinesValue.getRefinedBy().contains(referenceChange));
	}

	/**
	 * Tests the behavior of reference <code>refinedBy</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testRefinedBy() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_RefinedBy();
		ReferenceChange referenceChange = CompareFactory.eINSTANCE.createReferenceChange();
		referenceChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Diff refinedByValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createDiff();
		List<org.eclipse.emf.compare.Diff> listRefinedBy = new ArrayList<org.eclipse.emf.compare.Diff>(1);
		listRefinedBy.add(refinedByValue);

		assertFalse(referenceChange.eIsSet(feature));
		assertTrue(referenceChange.getRefinedBy().isEmpty());

		referenceChange.getRefinedBy().add(refinedByValue);
		assertTrue(notified);
		notified = false;
		assertTrue(referenceChange.getRefinedBy().contains(refinedByValue));
		assertSame(referenceChange.getRefinedBy(), referenceChange.eGet(feature));
		assertSame(referenceChange.getRefinedBy(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));
		assertTrue(refinedByValue.getRefines().contains(referenceChange));

		referenceChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(referenceChange.getRefinedBy().isEmpty());
		assertSame(referenceChange.getRefinedBy(), referenceChange.eGet(feature));
		assertSame(referenceChange.getRefinedBy(), referenceChange.eGet(feature, false));
		assertFalse(referenceChange.eIsSet(feature));
		assertFalse(refinedByValue.getRefines().contains(referenceChange));

		referenceChange.eSet(feature, listRefinedBy);
		assertTrue(notified);
		notified = false;
		assertTrue(referenceChange.getRefinedBy().contains(refinedByValue));
		assertSame(referenceChange.getRefinedBy(), referenceChange.eGet(feature));
		assertSame(referenceChange.getRefinedBy(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));
		assertTrue(refinedByValue.getRefines().contains(referenceChange));
	}

	/**
	 * Tests the behavior of reference <code>match</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testMatch() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Match();
		ReferenceChange referenceChange = CompareFactory.eINSTANCE.createReferenceChange();
		referenceChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Match matchValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createMatch();

		assertFalse(referenceChange.eIsSet(feature));
		assertNull(referenceChange.getMatch());

		referenceChange.setMatch(matchValue);
		assertTrue(notified);
		notified = false;
		assertSame(matchValue, referenceChange.getMatch());
		assertSame(referenceChange.getMatch(), referenceChange.eGet(feature));
		assertSame(referenceChange.getMatch(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));
		assertTrue(matchValue.getDifferences().contains(referenceChange));

		referenceChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertNull(referenceChange.getMatch());
		assertSame(referenceChange.getMatch(), referenceChange.eGet(feature));
		assertSame(referenceChange.getMatch(), referenceChange.eGet(feature, false));
		assertFalse(referenceChange.eIsSet(feature));
		assertFalse(matchValue.getDifferences().contains(referenceChange));

		referenceChange.setMatch(matchValue);
		assertTrue(notified);
		notified = false;
		assertSame(matchValue, referenceChange.getMatch());
		assertSame(referenceChange.getMatch(), referenceChange.eGet(feature));
		assertSame(referenceChange.getMatch(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));
		assertTrue(matchValue.getDifferences().contains(referenceChange));

		referenceChange.eSet(feature, matchValue);
		assertTrue(notified);
		notified = false;
		assertSame(matchValue, referenceChange.getMatch());
		assertSame(referenceChange.getMatch(), referenceChange.eGet(feature));
		assertSame(referenceChange.getMatch(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));
		assertTrue(matchValue.getDifferences().contains(referenceChange));

		referenceChange.setMatch(null);
		assertTrue(notified);
		notified = false;
		assertNull(referenceChange.getMatch());
		assertSame(feature.getDefaultValue(), referenceChange.getMatch());
		assertSame(referenceChange.getMatch(), referenceChange.eGet(feature));
		assertSame(referenceChange.getMatch(), referenceChange.eGet(feature, false));
		assertFalse(referenceChange.eIsSet(feature));
		assertFalse(matchValue.getDifferences().contains(referenceChange));
	}

	/**
	 * Tests the behavior of reference <code>equivalentDiffs</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testEquivalence() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Equivalence();
		ReferenceChange referenceChange = CompareFactory.eINSTANCE.createReferenceChange();
		referenceChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Equivalence equivalentDiffsValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createEquivalence();

		assertFalse(referenceChange.eIsSet(feature));
		assertNull(referenceChange.getEquivalence());

		referenceChange.setEquivalence(equivalentDiffsValue);
		assertTrue(notified);
		notified = false;
		assertSame(equivalentDiffsValue, referenceChange.getEquivalence());
		assertSame(referenceChange.getEquivalence(), referenceChange.eGet(feature));
		assertSame(referenceChange.getEquivalence(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));
		assertTrue(equivalentDiffsValue.getDifferences().contains(referenceChange));

		referenceChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertNull(referenceChange.getEquivalence());
		assertSame(referenceChange.getEquivalence(), referenceChange.eGet(feature));
		assertSame(referenceChange.getEquivalence(), referenceChange.eGet(feature, false));
		assertFalse(referenceChange.eIsSet(feature));
		assertFalse(equivalentDiffsValue.getDifferences().contains(referenceChange));

		referenceChange.setEquivalence(equivalentDiffsValue);
		assertTrue(notified);
		notified = false;
		assertSame(equivalentDiffsValue, referenceChange.getEquivalence());
		assertSame(referenceChange.getEquivalence(), referenceChange.eGet(feature));
		assertSame(referenceChange.getEquivalence(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));
		assertTrue(equivalentDiffsValue.getDifferences().contains(referenceChange));

		referenceChange.eSet(feature, equivalentDiffsValue);
		assertTrue(notified);
		notified = false;
		assertSame(equivalentDiffsValue, referenceChange.getEquivalence());
		assertSame(referenceChange.getEquivalence(), referenceChange.eGet(feature));
		assertSame(referenceChange.getEquivalence(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));
		assertTrue(equivalentDiffsValue.getDifferences().contains(referenceChange));

		referenceChange.setEquivalence(null);
		assertTrue(notified);
		notified = false;
		assertNull(referenceChange.getEquivalence());
		assertSame(feature.getDefaultValue(), referenceChange.getEquivalence());
		assertSame(referenceChange.getEquivalence(), referenceChange.eGet(feature));
		assertSame(referenceChange.getEquivalence(), referenceChange.eGet(feature, false));
		assertFalse(referenceChange.eIsSet(feature));
		assertFalse(equivalentDiffsValue.getDifferences().contains(referenceChange));
	}

	/**
	 * Tests the behavior of reference <code>conflict</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testConflict() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Conflict();
		ReferenceChange referenceChange = CompareFactory.eINSTANCE.createReferenceChange();
		referenceChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Conflict conflictValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createConflict();

		assertFalse(referenceChange.eIsSet(feature));
		assertNull(referenceChange.getConflict());

		referenceChange.setConflict(conflictValue);
		assertTrue(notified);
		notified = false;
		assertSame(conflictValue, referenceChange.getConflict());
		assertSame(referenceChange.getConflict(), referenceChange.eGet(feature));
		assertSame(referenceChange.getConflict(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));
		assertTrue(conflictValue.getDifferences().contains(referenceChange));

		referenceChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertNull(referenceChange.getConflict());
		assertSame(referenceChange.getConflict(), referenceChange.eGet(feature));
		assertSame(referenceChange.getConflict(), referenceChange.eGet(feature, false));
		assertFalse(referenceChange.eIsSet(feature));
		assertFalse(conflictValue.getDifferences().contains(referenceChange));

		referenceChange.setConflict(conflictValue);
		assertTrue(notified);
		notified = false;
		assertSame(conflictValue, referenceChange.getConflict());
		assertSame(referenceChange.getConflict(), referenceChange.eGet(feature));
		assertSame(referenceChange.getConflict(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));
		assertTrue(conflictValue.getDifferences().contains(referenceChange));

		referenceChange.eSet(feature, conflictValue);
		assertTrue(notified);
		notified = false;
		assertSame(conflictValue, referenceChange.getConflict());
		assertSame(referenceChange.getConflict(), referenceChange.eGet(feature));
		assertSame(referenceChange.getConflict(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));
		assertTrue(conflictValue.getDifferences().contains(referenceChange));

		referenceChange.setConflict(null);
		assertTrue(notified);
		notified = false;
		assertNull(referenceChange.getConflict());
		assertSame(feature.getDefaultValue(), referenceChange.getConflict());
		assertSame(referenceChange.getConflict(), referenceChange.eGet(feature));
		assertSame(referenceChange.getConflict(), referenceChange.eGet(feature, false));
		assertFalse(referenceChange.eIsSet(feature));
		assertFalse(conflictValue.getDifferences().contains(referenceChange));
	}

	/**
	 * Tests the behavior of reference <code>reference</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testReference() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE
				.getReferenceChange_Reference();
		ReferenceChange referenceChange = CompareFactory.eINSTANCE.createReferenceChange();
		referenceChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.ecore.EReference referenceValue = org.eclipse.emf.ecore.EcoreFactory.eINSTANCE
				.createEReference();

		assertFalse(referenceChange.eIsSet(feature));
		assertNull(referenceChange.getReference());

		referenceChange.setReference(referenceValue);
		assertTrue(notified);
		notified = false;
		assertSame(referenceValue, referenceChange.getReference());
		assertSame(referenceChange.getReference(), referenceChange.eGet(feature));
		assertSame(referenceChange.getReference(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));

		referenceChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertNull(referenceChange.getReference());
		assertSame(referenceChange.getReference(), referenceChange.eGet(feature));
		assertSame(referenceChange.getReference(), referenceChange.eGet(feature, false));
		assertFalse(referenceChange.eIsSet(feature));

		referenceChange.setReference(referenceValue);
		assertTrue(notified);
		notified = false;
		assertSame(referenceValue, referenceChange.getReference());
		assertSame(referenceChange.getReference(), referenceChange.eGet(feature));
		assertSame(referenceChange.getReference(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));

		referenceChange.eSet(feature, referenceValue);
		assertTrue(notified);
		notified = false;
		assertSame(referenceValue, referenceChange.getReference());
		assertSame(referenceChange.getReference(), referenceChange.eGet(feature));
		assertSame(referenceChange.getReference(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));

		referenceChange.setReference(null);
		assertTrue(notified);
		notified = false;
		assertNull(referenceChange.getReference());
		assertSame(feature.getDefaultValue(), referenceChange.getReference());
		assertSame(referenceChange.getReference(), referenceChange.eGet(feature));
		assertSame(referenceChange.getReference(), referenceChange.eGet(feature, false));
		assertFalse(referenceChange.eIsSet(feature));
	}

	/**
	 * Tests the behavior of reference <code>value</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testValue() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE
				.getReferenceChange_Value();
		ReferenceChange referenceChange = CompareFactory.eINSTANCE.createReferenceChange();
		referenceChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.ecore.EObject valueValue = org.eclipse.emf.ecore.EcoreFactory.eINSTANCE
				.createEObject();

		assertFalse(referenceChange.eIsSet(feature));
		assertNull(referenceChange.getValue());

		referenceChange.setValue(valueValue);
		assertTrue(notified);
		notified = false;
		assertSame(valueValue, referenceChange.getValue());
		assertSame(referenceChange.getValue(), referenceChange.eGet(feature));
		assertSame(referenceChange.getValue(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));

		referenceChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertNull(referenceChange.getValue());
		assertSame(referenceChange.getValue(), referenceChange.eGet(feature));
		assertSame(referenceChange.getValue(), referenceChange.eGet(feature, false));
		assertFalse(referenceChange.eIsSet(feature));

		referenceChange.setValue(valueValue);
		assertTrue(notified);
		notified = false;
		assertSame(valueValue, referenceChange.getValue());
		assertSame(referenceChange.getValue(), referenceChange.eGet(feature));
		assertSame(referenceChange.getValue(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));

		referenceChange.eSet(feature, valueValue);
		assertTrue(notified);
		notified = false;
		assertSame(valueValue, referenceChange.getValue());
		assertSame(referenceChange.getValue(), referenceChange.eGet(feature));
		assertSame(referenceChange.getValue(), referenceChange.eGet(feature, false));
		assertTrue(referenceChange.eIsSet(feature));

		referenceChange.setValue(null);
		assertTrue(notified);
		notified = false;
		assertNull(referenceChange.getValue());
		assertSame(feature.getDefaultValue(), referenceChange.getValue());
		assertSame(referenceChange.getValue(), referenceChange.eGet(feature));
		assertSame(referenceChange.getValue(), referenceChange.eGet(feature, false));
		assertFalse(referenceChange.eIsSet(feature));
	}

	/**
	 * Tests the behavior of attribute <code>kind</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testKind() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getDiff_Kind();
		ReferenceChange referenceChange = CompareFactory.eINSTANCE.createReferenceChange();
		referenceChange.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.DifferenceKind kindValue = (org.eclipse.emf.compare.DifferenceKind)feature
				.getDefaultValue();
		for (org.eclipse.emf.compare.DifferenceKind aDifferenceKind : org.eclipse.emf.compare.DifferenceKind.VALUES) {
			if (kindValue.getValue() != aDifferenceKind.getValue()) {
				kindValue = aDifferenceKind;
				break;
			}
		}

		assertFalse(referenceChange.eIsSet(feature));
		assertEquals(feature.getDefaultValue(), referenceChange.getKind());

		referenceChange.setKind(kindValue);
		assertTrue(notified);
		notified = false;
		assertEquals(kindValue, referenceChange.getKind());
		assertEquals(referenceChange.getKind(), referenceChange.eGet(feature));
		assertTrue(referenceChange.eIsSet(feature));

		referenceChange.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), referenceChange.getKind());
		assertEquals(referenceChange.getKind(), referenceChange.eGet(feature));
		assertFalse(referenceChange.eIsSet(feature));

		referenceChange.eSet(feature, kindValue);
		assertTrue(notified);
		notified = false;
		assertEquals(kindValue, referenceChange.getKind());
		assertEquals(referenceChange.getKind(), referenceChange.eGet(feature));
		assertTrue(referenceChange.eIsSet(feature));

		referenceChange.setKind(null);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), referenceChange.getKind());
		assertEquals(referenceChange.getKind(), referenceChange.eGet(feature));
		assertFalse(referenceChange.eIsSet(feature));
	}

}
