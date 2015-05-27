/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Borkowski - initial tests
 *******************************************************************************/
package org.eclipse.emf.compare.tests.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings({"boxing", "nls" })
public class MatchUtilFeatureContainsTest {

	List<EObject> featureList;

	InternalEObject value;

	EObject container;

	EClass eClass;

	EStructuralFeature feature;

	@Before
	public void setUp() {
		EPackage ePackage = mock(EPackage.class);

		container = mock(EObject.class);
		feature = mock(EStructuralFeature.class);

		eClass = mock(EClass.class);
		when(eClass.getEPackage()).thenReturn(ePackage);

		when(container.eClass()).thenReturn(eClass);
		when(feature.getEContainingClass()).thenReturn(eClass);

		featureList = new LinkedList<EObject>();
		when(container.eGet(feature, false)).thenReturn(featureList);

		value = mockInternalObject();
		featureList.add(value);
	}

	@Test
	public void test_SameObject() {
		assertTrue(MatchUtil.featureContains(container, feature, value));
	}

	@Test
	public void test_Proxy() {
		URI uri = URI.createFileURI("/my/path");

		InternalEObject proxyValue = mockInternalObject();
		when(proxyValue.eIsProxy()).thenReturn(true);
		when(proxyValue.eProxyURI()).thenReturn(uri);

		when(value.eIsProxy()).thenReturn(true);
		when(value.eProxyURI()).thenReturn(uri);

		assertTrue(MatchUtil.featureContains(container, feature, proxyValue));
	}

	@Test
	public void test_DifferentObject() {
		InternalEObject otherValue = mockInternalObject();

		assertFalse(MatchUtil.featureContains(container, feature, otherValue));
	}

	private InternalEObject mockInternalObject() {
		InternalEObject internalObject = mock(InternalEObject.class);
		when(internalObject.eClass()).thenReturn(eClass);
		return internalObject;
	}
}
