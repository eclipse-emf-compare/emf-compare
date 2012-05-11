package org.eclipse.emf.compare.tests.unit;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Test;

/**
 * Tests the behavior of the {@link Equivalence} class.
 * 
 * @generated
 */
public class EquivalenceTest extends AbstractCompareTest {
	/**
	 * Tests the behavior of reference <code>differences</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testDifferences() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE
				.getEquivalence_Differences();
		Equivalence equivalence = CompareFactory.eINSTANCE.createEquivalence();
		equivalence.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.compare.Diff differencesValue = org.eclipse.emf.compare.CompareFactory.eINSTANCE
				.createDiff();
		List<org.eclipse.emf.compare.Diff> listDifferences = new ArrayList<org.eclipse.emf.compare.Diff>(1);
		listDifferences.add(differencesValue);

		assertFalse(equivalence.eIsSet(feature));
		assertTrue(equivalence.getDifferences().isEmpty());

		equivalence.getDifferences().add(differencesValue);
		assertTrue(notified);
		notified = false;
		assertTrue(equivalence.getDifferences().contains(differencesValue));
		assertSame(equivalence.getDifferences(), equivalence.eGet(feature));
		assertSame(equivalence.getDifferences(), equivalence.eGet(feature, false));
		assertTrue(equivalence.eIsSet(feature));
		assertTrue(differencesValue.getEquivalence() == equivalence);

		equivalence.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertTrue(equivalence.getDifferences().isEmpty());
		assertSame(equivalence.getDifferences(), equivalence.eGet(feature));
		assertSame(equivalence.getDifferences(), equivalence.eGet(feature, false));
		assertFalse(equivalence.eIsSet(feature));
		assertFalse(differencesValue.getEquivalence() == equivalence);

		equivalence.eSet(feature, listDifferences);
		assertTrue(notified);
		notified = false;
		assertTrue(equivalence.getDifferences().contains(differencesValue));
		assertSame(equivalence.getDifferences(), equivalence.eGet(feature));
		assertSame(equivalence.getDifferences(), equivalence.eGet(feature, false));
		assertTrue(equivalence.eIsSet(feature));
		assertTrue(differencesValue.getEquivalence() == equivalence);
	}

}
