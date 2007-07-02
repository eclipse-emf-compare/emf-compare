/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.metamodel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Add Reference Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.AddReferenceValue#getRightAddedTarget <em>Right Added Target</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.AddReferenceValue#getLeftAddedTarget <em>Left Added Target</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getAddReferenceValue()
 * @model
 * @generated
 */
public interface AddReferenceValue extends ReferenceChange {
	/**
	 * Returns the value of the '<em><b>Left Added Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left Added Target</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Left Added Target</em>' reference.
	 * @see #setLeftAddedTarget(EObject)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getAddReferenceValue_LeftAddedTarget()
	 * @model
	 * @generated
	 */
	EObject getLeftAddedTarget();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.AddReferenceValue#getLeftAddedTarget <em>Left Added Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Left Added Target</em>' reference.
	 * @see #getLeftAddedTarget()
	 * @generated
	 */
	void setLeftAddedTarget(EObject value);

	/**
	 * Returns the value of the '<em><b>Right Added Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right Added Target</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Right Added Target</em>' reference.
	 * @see #setRightAddedTarget(EObject)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getAddReferenceValue_RightAddedTarget()
	 * @model
	 * @generated
	 */
	EObject getRightAddedTarget();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.AddReferenceValue#getRightAddedTarget <em>Right Added Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Right Added Target</em>' reference.
	 * @see #getRightAddedTarget()
	 * @generated
	 */
	void setRightAddedTarget(EObject value);

} // AddReferenceValue