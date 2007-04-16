/*  
 * Copyright (c) 2006, Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.diff;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.diff.DiffPackage
 * @generated
 */
public interface DiffFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DiffFactory eINSTANCE = org.eclipse.emf.compare.diff.impl.DiffFactoryImpl
			.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	DiffModel createDiffModel();

	/**
	 * Returns a new object of class '<em>Group</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Group</em>'.
	 * @generated
	 */
	DiffGroup createDiffGroup();

	/**
	 * Returns a new object of class '<em>Attribute Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Attribute Change</em>'.
	 * @generated
	 */
	AttributeChange createAttributeChange();

	/**
	 * Returns a new object of class '<em>Reference Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Reference Change</em>'.
	 * @generated
	 */
	ReferenceChange createReferenceChange();

	/**
	 * Returns a new object of class '<em>Model Element Change</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model Element Change</em>'.
	 * @generated
	 */
	ModelElementChange createModelElementChange();

	/**
	 * Returns a new object of class '<em>Add Model Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Add Model Element</em>'.
	 * @generated
	 */
	AddModelElement createAddModelElement();

	/**
	 * Returns a new object of class '<em>Remove Model Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Remove Model Element</em>'.
	 * @generated
	 */
	RemoveModelElement createRemoveModelElement();

	/**
	 * Returns a new object of class '<em>Update Model Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Update Model Element</em>'.
	 * @generated
	 */
	UpdateModelElement createUpdateModelElement();

	/**
	 * Returns a new object of class '<em>Move Model Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Move Model Element</em>'.
	 * @generated
	 */
	MoveModelElement createMoveModelElement();

	/**
	 * Returns a new object of class '<em>Add Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Add Attribute</em>'.
	 * @generated
	 */
	AddAttribute createAddAttribute();

	/**
	 * Returns a new object of class '<em>Remove Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Remove Attribute</em>'.
	 * @generated
	 */
	RemoveAttribute createRemoveAttribute();

	/**
	 * Returns a new object of class '<em>Update Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Update Attribute</em>'.
	 * @generated
	 */
	UpdateAttribute createUpdateAttribute();

	/**
	 * Returns a new object of class '<em>Add Reference Value</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Add Reference Value</em>'.
	 * @generated
	 */
	AddReferenceValue createAddReferenceValue();

	/**
	 * Returns a new object of class '<em>Remove Reference Value</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Remove Reference Value</em>'.
	 * @generated
	 */
	RemoveReferenceValue createRemoveReferenceValue();

	/**
	 * Returns a new object of class '<em>Update Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Update Reference</em>'.
	 * @generated
	 */
	UpdateReference createUpdateReference();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	DiffPackage getDiffPackage();

} //DiffFactory
