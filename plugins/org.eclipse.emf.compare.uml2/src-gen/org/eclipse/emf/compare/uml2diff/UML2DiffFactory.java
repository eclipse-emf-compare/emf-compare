/**
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2diff;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.uml2diff.UML2DiffPackage
 * @generated
 */
public interface UML2DiffFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	UML2DiffFactory eINSTANCE = org.eclipse.emf.compare.uml2diff.impl.UML2DiffFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>UML Abstraction Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Abstraction Change Left Target</em>'.
	 * @generated
	 */
	UMLAbstractionChangeLeftTarget createUMLAbstractionChangeLeftTarget();

	/**
	 * Returns a new object of class '<em>UML Abstraction Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Abstraction Change Right Target</em>'.
	 * @generated
	 */
	UMLAbstractionChangeRightTarget createUMLAbstractionChangeRightTarget();

	/**
	 * Returns a new object of class '<em>UML Association Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Association Change Left Target</em>'.
	 * @generated
	 */
	UMLAssociationChangeLeftTarget createUMLAssociationChangeLeftTarget();

	/**
	 * Returns a new object of class '<em>UML Association Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Association Change Right Target</em>'.
	 * @generated
	 */
	UMLAssociationChangeRightTarget createUMLAssociationChangeRightTarget();

	/**
	 * Returns a new object of class '<em>UML Stereotype Attribute Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Stereotype Attribute Change Left Target</em>'.
	 * @generated
	 */
	UMLStereotypeAttributeChangeLeftTarget createUMLStereotypeAttributeChangeLeftTarget();

	/**
	 * Returns a new object of class '<em>UML Stereotype Attribute Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Stereotype Attribute Change Right Target</em>'.
	 * @generated
	 */
	UMLStereotypeAttributeChangeRightTarget createUMLStereotypeAttributeChangeRightTarget();

	/**
	 * Returns a new object of class '<em>UML Stereotype Update Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Stereotype Update Attribute</em>'.
	 * @generated
	 */
	UMLStereotypeUpdateAttribute createUMLStereotypeUpdateAttribute();

	/**
	 * Returns a new object of class '<em>UML Stereotype Application Addition</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Stereotype Application Addition</em>'.
	 * @generated
	 */
	UMLStereotypeApplicationAddition createUMLStereotypeApplicationAddition();

	/**
	 * Returns a new object of class '<em>UML Stereotype Application Removal</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UML Stereotype Application Removal</em>'.
	 * @generated
	 */
	UMLStereotypeApplicationRemoval createUMLStereotypeApplicationRemoval();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	UML2DiffPackage getUML2DiffPackage();

} //UML2DiffFactory
