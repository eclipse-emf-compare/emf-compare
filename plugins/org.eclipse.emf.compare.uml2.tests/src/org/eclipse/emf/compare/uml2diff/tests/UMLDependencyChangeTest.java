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

import junit.framework.TestCase;

import junit.textui.TestRunner;

import org.eclipse.emf.compare.uml2diff.UMLDependencyChange;
import org.eclipse.emf.compare.uml2diff.Uml2diffFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>UML Dependency Change</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class UMLDependencyChangeTest extends UMLExtensionTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(UMLDependencyChangeTest.class);
	}

	/**
	 * Constructs a new UML Dependency Change test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLDependencyChangeTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this UML Dependency Change test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected UMLDependencyChange getFixture() {
		return (UMLDependencyChange)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(Uml2diffFactory.eINSTANCE.createUMLDependencyChange());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
	@Override
	protected void tearDown() throws Exception {
		setFixture(null);
	}

} //UMLDependencyChangeTest
