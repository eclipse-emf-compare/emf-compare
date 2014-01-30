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
import static org.junit.Assert.assertTrue;

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

	/**
	 * Tests the behavior of attribute <code>left</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testLeft() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE.getMatchResource_Left();
		MatchResource matchResource = CompareFactory.eINSTANCE.createMatchResource();
		matchResource.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.ecore.resource.Resource leftValue = (org.eclipse.emf.ecore.resource.Resource)getValueDistinctFromDefault(feature);

		assertFalse(matchResource.eIsSet(feature));
		assertEquals(feature.getDefaultValue(), matchResource.getLeft());

		matchResource.setLeft(leftValue);
		assertTrue(notified);
		notified = false;
		assertEquals(leftValue, matchResource.getLeft());
		assertEquals(matchResource.getLeft(), matchResource.eGet(feature));
		assertTrue(matchResource.eIsSet(feature));

		matchResource.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), matchResource.getLeft());
		assertEquals(matchResource.getLeft(), matchResource.eGet(feature));
		assertFalse(matchResource.eIsSet(feature));

		matchResource.eSet(feature, leftValue);
		assertTrue(notified);
		notified = false;
		assertEquals(leftValue, matchResource.getLeft());
		assertEquals(matchResource.getLeft(), matchResource.eGet(feature));
		assertTrue(matchResource.eIsSet(feature));

		matchResource.setLeft(null);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), matchResource.getLeft());
		assertEquals(matchResource.getLeft(), matchResource.eGet(feature));
		assertFalse(matchResource.eIsSet(feature));
	}

	/**
	 * Tests the behavior of attribute <code>right</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testRight() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE
				.getMatchResource_Right();
		MatchResource matchResource = CompareFactory.eINSTANCE.createMatchResource();
		matchResource.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.ecore.resource.Resource rightValue = (org.eclipse.emf.ecore.resource.Resource)getValueDistinctFromDefault(feature);

		assertFalse(matchResource.eIsSet(feature));
		assertEquals(feature.getDefaultValue(), matchResource.getRight());

		matchResource.setRight(rightValue);
		assertTrue(notified);
		notified = false;
		assertEquals(rightValue, matchResource.getRight());
		assertEquals(matchResource.getRight(), matchResource.eGet(feature));
		assertTrue(matchResource.eIsSet(feature));

		matchResource.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), matchResource.getRight());
		assertEquals(matchResource.getRight(), matchResource.eGet(feature));
		assertFalse(matchResource.eIsSet(feature));

		matchResource.eSet(feature, rightValue);
		assertTrue(notified);
		notified = false;
		assertEquals(rightValue, matchResource.getRight());
		assertEquals(matchResource.getRight(), matchResource.eGet(feature));
		assertTrue(matchResource.eIsSet(feature));

		matchResource.setRight(null);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), matchResource.getRight());
		assertEquals(matchResource.getRight(), matchResource.eGet(feature));
		assertFalse(matchResource.eIsSet(feature));
	}

	/**
	 * Tests the behavior of attribute <code>origin</code>'s accessors.
	 * 
	 * @generated
	 */
	@Test
	public void testOrigin() {
		EStructuralFeature feature = org.eclipse.emf.compare.ComparePackage.eINSTANCE
				.getMatchResource_Origin();
		MatchResource matchResource = CompareFactory.eINSTANCE.createMatchResource();
		matchResource.eAdapters().add(new MockEAdapter());
		org.eclipse.emf.ecore.resource.Resource originValue = (org.eclipse.emf.ecore.resource.Resource)getValueDistinctFromDefault(feature);

		assertFalse(matchResource.eIsSet(feature));
		assertEquals(feature.getDefaultValue(), matchResource.getOrigin());

		matchResource.setOrigin(originValue);
		assertTrue(notified);
		notified = false;
		assertEquals(originValue, matchResource.getOrigin());
		assertEquals(matchResource.getOrigin(), matchResource.eGet(feature));
		assertTrue(matchResource.eIsSet(feature));

		matchResource.eUnset(feature);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), matchResource.getOrigin());
		assertEquals(matchResource.getOrigin(), matchResource.eGet(feature));
		assertFalse(matchResource.eIsSet(feature));

		matchResource.eSet(feature, originValue);
		assertTrue(notified);
		notified = false;
		assertEquals(originValue, matchResource.getOrigin());
		assertEquals(matchResource.getOrigin(), matchResource.eGet(feature));
		assertTrue(matchResource.eIsSet(feature));

		matchResource.setOrigin(null);
		assertTrue(notified);
		notified = false;
		assertEquals(feature.getDefaultValue(), matchResource.getOrigin());
		assertEquals(matchResource.getOrigin(), matchResource.eGet(feature));
		assertFalse(matchResource.eIsSet(feature));
	}

}
