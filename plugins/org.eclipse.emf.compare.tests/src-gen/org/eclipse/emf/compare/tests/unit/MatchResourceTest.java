package org.eclipse.emf.compare.tests.unit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Test;

/**
 * Tests the behavior of the {@link MatchResource} class.
 * 
 * @generated
 */
public class MatchResourceTest extends AbstractCompareTest {

	/**
	 * Tests the behavior of attribute <code>leftURI</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testLeftURI() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE
				.getMatchResource_LeftURI();
		MatchResource matchResource = CompareFactory.eINSTANCE.createMatchResource();
		matchResource.eAdapters().add(new MockEAdapter());
		java.lang.String leftURIValue = (java.lang.String)getValueDistinctFromDefault(feature);

		assertFalse(matchResource.eIsSet(feature));
		assertEquals(feature.getDefaultValue(), matchResource.getLeftURI());

		matchResource.setLeftURI(leftURIValue);
		assertTrue(notified);
		notified = false;
		assertEquals(leftURIValue, matchResource.getLeftURI());
		assertEquals(matchResource.getLeftURI(), matchResource.eGet(feature));
		assertTrue(matchResource.eIsSet(feature));

		matchResource.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), matchResource.getLeftURI());
		assertEquals(matchResource.getLeftURI(), matchResource.eGet(feature));
		assertFalse(matchResource.eIsSet(feature));

		matchResource.eSet(feature, leftURIValue);
		assertTrue(notified);
		notified = false;
		assertEquals(leftURIValue, matchResource.getLeftURI());
		assertEquals(matchResource.getLeftURI(), matchResource.eGet(feature));
		assertTrue(matchResource.eIsSet(feature));

		matchResource.setLeftURI(null);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), matchResource.getLeftURI());
		assertEquals(matchResource.getLeftURI(), matchResource.eGet(feature));
		assertFalse(matchResource.eIsSet(feature));
	}

	/**
	 * Tests the behavior of attribute <code>rightURI</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testRightURI() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE
				.getMatchResource_RightURI();
		MatchResource matchResource = CompareFactory.eINSTANCE.createMatchResource();
		matchResource.eAdapters().add(new MockEAdapter());
		java.lang.String rightURIValue = (java.lang.String)getValueDistinctFromDefault(feature);

		assertFalse(matchResource.eIsSet(feature));
		assertEquals(feature.getDefaultValue(), matchResource.getRightURI());

		matchResource.setRightURI(rightURIValue);
		assertTrue(notified);
		notified = false;
		assertEquals(rightURIValue, matchResource.getRightURI());
		assertEquals(matchResource.getRightURI(), matchResource.eGet(feature));
		assertTrue(matchResource.eIsSet(feature));

		matchResource.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), matchResource.getRightURI());
		assertEquals(matchResource.getRightURI(), matchResource.eGet(feature));
		assertFalse(matchResource.eIsSet(feature));

		matchResource.eSet(feature, rightURIValue);
		assertTrue(notified);
		notified = false;
		assertEquals(rightURIValue, matchResource.getRightURI());
		assertEquals(matchResource.getRightURI(), matchResource.eGet(feature));
		assertTrue(matchResource.eIsSet(feature));

		matchResource.setRightURI(null);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), matchResource.getRightURI());
		assertEquals(matchResource.getRightURI(), matchResource.eGet(feature));
		assertFalse(matchResource.eIsSet(feature));
	}

	/**
	 * Tests the behavior of attribute <code>originURI</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testOriginURI() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE
				.getMatchResource_OriginURI();
		MatchResource matchResource = CompareFactory.eINSTANCE.createMatchResource();
		matchResource.eAdapters().add(new MockEAdapter());
		java.lang.String originURIValue = (java.lang.String)getValueDistinctFromDefault(feature);

		assertFalse(matchResource.eIsSet(feature));
		assertEquals(feature.getDefaultValue(), matchResource.getOriginURI());

		matchResource.setOriginURI(originURIValue);
		assertTrue(notified);
		notified = false;
		assertEquals(originURIValue, matchResource.getOriginURI());
		assertEquals(matchResource.getOriginURI(), matchResource.eGet(feature));
		assertTrue(matchResource.eIsSet(feature));

		matchResource.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), matchResource.getOriginURI());
		assertEquals(matchResource.getOriginURI(), matchResource.eGet(feature));
		assertFalse(matchResource.eIsSet(feature));

		matchResource.eSet(feature, originURIValue);
		assertTrue(notified);
		notified = false;
		assertEquals(originURIValue, matchResource.getOriginURI());
		assertEquals(matchResource.getOriginURI(), matchResource.eGet(feature));
		assertTrue(matchResource.eIsSet(feature));

		matchResource.setOriginURI(null);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), matchResource.getOriginURI());
		assertEquals(matchResource.getOriginURI(), matchResource.eGet(feature));
		assertFalse(matchResource.eIsSet(feature));
	}

}
