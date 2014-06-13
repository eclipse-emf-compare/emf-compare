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
import java.util.Map;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.stereotypes.data.static_.StaticStereotypeInputData;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.UMLPlugin;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the {@link org.eclipse.emf.compare.uml2.internal.postprocessor.StereotypedElementChangePostProcessor}
 * for model with static profiles.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class StaticStereotypedElementChangeTests extends AbstractStereotypedElementChangeTests {

	private StaticStereotypeInputData input;

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

	@BeforeClass
	public static void fillRegistriesForStatic() {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			EPackage.Registry.INSTANCE.put(UML2CompareTestProfilePackage.eNS_URI,
					UML2CompareTestProfilePackage.eINSTANCE); // registers
			// against
			// EPackage.Registry
			// It is required to link the EPackage to the UML package of the UML Profile
			Map<String, URI> ePackageNsURIToProfileLocationMap = UMLPlugin
					.getEPackageNsURIToProfileLocationMap();
			ePackageNsURIToProfileLocationMap
					.put(UML2CompareTestProfilePackage.eNS_URI,
							URI.createURI("pathmap://UML_COMPARE_TESTS_PROFILE/uml2.compare.testprofile.profile.uml#_hZFTgIwkEeC_FYHMbTTxXw")); //$NON-NLS-1$
		}
	}

	@AfterClass
	public static void resetRegistriesForStatic() {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			UMLPlugin.getEPackageNsURIToProfileLocationMap().remove(UML2CompareTestProfilePackage.eNS_URI);
			EPackage.Registry.INSTANCE.remove(UML2CompareTestProfilePackage.eNS_URI);
		}
	}

	@Before
	@Override
	public void before() {
		super.before();
		input = new StaticStereotypeInputData();
	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testRemoveStereotypeOnExistingElement(Resource, Resource)
	 */
	@Test
	public void testRemoveStereotypeOnExistingElement() throws IOException {
		testRemoveStereotypeOnExistingElement(input.getB1Right(), input.getB1Left());

	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testAddStereotypedElementMergeLToR(Resource, Resource)
	 */
	@Test
	public void testAddStereotypeElementMergeLToR() throws IOException {
		testAddStereotypedElementMergeLToR(input.getB4Left(), input.getB4Right());

	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testDelStereotypedElementMergeRToL(Resource, Resource)
	 */
	@Test
	public void testDelStereotypeElementMergeLToR() throws IOException {
		// Reverses inputs to create Del diff
		testDelStereotypedElementMergeLToR(input.getB4Right(), input.getB4Left());
	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testAddStereotypedElementMergeRToL(Resource, Resource)
	 */
	@Test
	public void testAddStereotypeElementMergeRToL() throws IOException {
		testAddStereotypedElementMergeRToL(input.getB4Left(), input.getB4Right());
	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testDelStereotypedElementMergeRToL(Resource, Resource)
	 */
	@Test
	public void testDellStereotypeElementMergeRToL() throws IOException {
		testDelStereotypedElementMergeRToL(input.getB4Right(), input.getB4Left());
	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testAddStereotypedElementLToR2(Resource, Resource)
	 */
	@Test
	public void testAddStereotypedElementLToR2() throws IOException {
		testAddStereotypedElementLToR2(input.getB3Left(), input.getB3Right());

	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testDelStereotypedElementLToR2(Resource, Resource)
	 */
	@Test
	public void testDelStereotypedElementLToR2() throws IOException {
		testDelStereotypedElementLToR2(input.getB3Right(), input.getB3Left());

	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testAddStereotypedElementRToL2(Resource, Resource)
	 */
	@Test
	public void testAddStereotypedElementRToL2() throws IOException {
		testAddStereotypedElementRToL2(input.getB3Left(), input.getB3Right());
	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testDelStereotypedElementRToL2(Resource, Resource)
	 */
	@Test
	public void testDelStereotypedElementRToL2() throws IOException {
		testDelStereotypedElementRToL2(input.getB3Right(), input.getB3Left());
	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testAddMultipleStereotypesLToR(Resource, Resource)
	 */
	@Test
	public void testAddMultipleStereotypeLToR() throws IOException {
		testAddMultipleStereotypesLToR(input.getB12Left(), input.getB12Right());
	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testAddMultipleStereotypesRToL(Resource, Resource)
	 */
	@Test
	public void testAddMultipleStereotypeRToL() throws IOException {
		testAddMultipleStereotypesRToL(input.getB12Left(), input.getB12Right());
	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testAbstractDelConflictRToL(Resource, Resource)
	 */
	@Test
	public void testDelConflictRToL() throws IOException {
		testDelConflictRToL(input.getB13Left(), input.getB13Right(), input.getB13Ancestor());
	}

	/**
	 * @see AbstractStereotypedElementChangeTests#testAbstractDelConflictLToR(Resource, Resource)
	 */
	@Test
	public void testDelConflictLToR() throws IOException {
		testAbstractDelConflictLToR(input.getB13Left(), input.getB13Right(), input.getB13Ancestor());
	}
}
