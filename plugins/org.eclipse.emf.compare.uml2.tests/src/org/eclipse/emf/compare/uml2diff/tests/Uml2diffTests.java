/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2diff.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>uml2diff</b></em>' package.
 * <!-- end-user-doc -->
 * @generated
 */
public class Uml2diffTests extends TestSuite {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Test suite() {
		TestSuite suite = new Uml2diffTests("uml2diff Tests"); //$NON-NLS-1$
		suite.addTestSuite(UMLAssociationChangeTest.class);
		suite.addTestSuite(UMLDependencyChangeTest.class);
		suite.addTestSuite(UMLInterfaceRealizationChangeTest.class);
		suite.addTestSuite(UMLSubstitutionChangeTest.class);
		suite.addTestSuite(UMLExtendChangeTest.class);
		suite.addTestSuite(UMLGeneralizationSetChangeTest.class);
		suite.addTestSuite(UMLExecutionSpecificationChangeTest.class);
		suite.addTestSuite(UMLDestructionEventChangeTest.class);
		suite.addTestSuite(UMLIntervalConstraintChangeTest.class);
		suite.addTestSuite(UMLMessageChangeTest.class);
		suite.addTestSuite(UMLStereotypePropertyChangeTest.class);
		suite.addTestSuite(UMLStereotypeApplicationChangeTest.class);
		suite.addTestSuite(UMLStereotypeReferenceChangeTest.class);
		suite.addTestSuite(UMLProfileApplicationChangeTest.class);
		return suite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Uml2diffTests(String name) {
		super(name);
	}

} //Uml2diffTests
