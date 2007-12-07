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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Model Element Change Left Target</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget#getRightParent <em>Right Parent</em>}</li>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget#getLeftElement <em>Left Element</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getModelElementChangeLeftTarget()
 * @model
 * @generated
 */
public interface ModelElementChangeLeftTarget extends ModelElementChange {
	/**
	 * Returns the value of the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left Element</em>' reference isn't clear, there really should be more of
	 * a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Left Element</em>' reference.
	 * @see #setLeftElement(EObject)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getModelElementChangeLeftTarget_LeftElement()
	 * @model
	 * @generated
	 */
	EObject getLeftElement();

	/**
	 * Returns the value of the '<em><b>Right Parent</b></em>' reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right Parent</em>' reference isn't clear, there really should be more of
	 * a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Right Parent</em>' reference.
	 * @see #setRightParent(EObject)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getModelElementChangeLeftTarget_RightParent()
	 * @model
	 * @generated
	 */
	EObject getRightParent();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget#getLeftElement <em>Left Element</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Left Element</em>' reference.
	 * @see #getLeftElement()
	 * @generated
	 */
	void setLeftElement(EObject value);

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget#getRightParent <em>Right Parent</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Right Parent</em>' reference.
	 * @see #getRightParent()
	 * @generated
	 */
	void setRightParent(EObject value);

} // ModelElementChangeLeftTarget
