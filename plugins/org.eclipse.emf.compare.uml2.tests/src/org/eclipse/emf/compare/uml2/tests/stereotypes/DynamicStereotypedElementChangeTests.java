/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.stereotypes;

import java.io.IOException;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.stereotypes.data.dynamic.DynamicStereotypeInputData;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.UMLPlugin;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the {@link org.eclipse.emf.compare.uml2.internal.postprocessor.StereotypedElementChangePostProcessor}
 * for model with dynamic profiles.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class DynamicStereotypedElementChangeTests extends AbstractStereotypedElementChangeTests {

	private DynamicStereotypeInputData input;

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

	static URI registeredURI;

	static Object registeredPackage;

	@BeforeClass
	public static void initEPackageNsURIToProfileLocationMap() {
		beforeClass();
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			// It is required to link the EPackage to the UML package of the UML Profile
			UMLPlugin
					.getEPackageNsURIToProfileLocationMap()
					.put("http://www.eclipse.org/emf/compare/uml2/1.0.0/testprofile", //$NON-NLS-1$
							URI.createURI("pathmap://UML_COMPARE_TESTS_PROFILE/uml2.compare.testprofile.profile.uml#_hZFTgIwkEeC_FYHMbTTxXw")); //$NON-NLS-1$
		} else {
			registeredURI = UMLPlugin.getEPackageNsURIToProfileLocationMap().remove(
					UML2CompareTestProfilePackage.eNS_URI);
			registeredPackage = EPackage.Registry.INSTANCE.remove(UML2CompareTestProfilePackage.eNS_URI);
		}
	}

	@AfterClass
	public static void resetEPackageNsURIToProfileLocationMap() {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			UMLPlugin.getEPackageNsURIToProfileLocationMap().remove(
					"http://www.eclipse.org/emf/compare/uml2/1.0.0/testprofile"); //$NON-NLS-1$
		} else {
			UMLPlugin.getEPackageNsURIToProfileLocationMap().put(UML2CompareTestProfilePackage.eNS_URI,
					registeredURI);
			EPackage.Registry.INSTANCE.put(UML2CompareTestProfilePackage.eNS_URI, registeredPackage);
		}
		afterClass();
	}

	@Before
	@Override
	public void before() {
		super.before();
		input = new DynamicStereotypeInputData();

	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testRemoveStereotypeOnExistingElement(Resource, Resource)
	 */
	@Test
	public void testRemoveStereotypeOnExistingElement() throws IOException {
		testRemoveStereotypeOnExistingElement(input.getA1Right(), input.getA1Left());

	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testAddStereotypedElementMergeLToR(Resource, Resource)
	 */
	@Test
	public void testAddStereotypeElementMergeLToR() throws IOException {
		testAddStereotypedElementMergeLToR(input.getA4Left(), input.getA4Right());

	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testDelStereotypedElementMergeRToL(Resource, Resource)
	 */
	@Test
	public void testDelStereotypeElementMergeLToR() throws IOException {
		testDelStereotypedElementMergeLToR(input.getA4Right(), input.getA4Left());
	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testAddStereotypedElementMergeRToL(Resource, Resource)
	 */
	@Test
	public void testAddStereotypeElementMergeRToL() throws IOException {
		testAddStereotypedElementMergeRToL(input.getA4Left(), input.getA4Right());
	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testDelStereotypedElementMergeRToL(Resource, Resource)
	 */
	@Test
	public void testDellStereotypeElementMergeRToL() throws IOException {
		testDelStereotypedElementMergeRToL(input.getA4Right(), input.getA4Left());
	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testAddStereotypedElementLToR2(Resource, Resource)
	 */
	@Test
	public void testAddStereotypedElementLToR2() throws IOException {
		testAddStereotypedElementLToR2(input.getA3Left(), input.getA3Right());

	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testDelStereotypedElementLToR2(Resource, Resource)
	 */
	@Test
	public void testDelStereotypedElementLToR2() throws IOException {
		testDelStereotypedElementLToR2(input.getA3Right(), input.getA3Left());

	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testAddStereotypedElementRToL2(Resource, Resource)
	 */
	@Test
	public void testAddStereotypedElementRToL2() throws IOException {
		testAddStereotypedElementRToL2(input.getA3Left(), input.getA3Right());
	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testDelStereotypedElementRToL2(Resource, Resource)
	 */
	@Test
	public void testDelStereotypedElementRToL2() throws IOException {
		testDelStereotypedElementRToL2(input.getA3Right(), input.getA3Left());
	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testAddMultipleStereotypesLToR(Resource, Resource)
	 */
	@Test
	public void testAddMultipleStereotypeLToR() throws IOException {
		testAddMultipleStereotypesLToR(input.getA12Left(), input.getA12Right());
	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testAddMultipleStereotypesRToL(Resource, Resource)
	 */
	@Test
	public void testAddMultipleStereotypeRToL() throws IOException {
		testAddMultipleStereotypesRToL(input.getA12Left(), input.getA12Right());
	}
}
