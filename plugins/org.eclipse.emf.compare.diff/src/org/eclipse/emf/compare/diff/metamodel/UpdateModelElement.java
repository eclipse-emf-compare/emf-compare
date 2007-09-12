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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Update Model Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.UpdateModelElement#getRightElement <em>Right Element</em>}</li>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.UpdateModelElement#getLeftElement <em>Left Element</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getUpdateModelElement()
 * @model
 * @generated
 */
@SuppressWarnings("nls")
public interface UpdateModelElement extends ModelElementChange {
	/**
	 * Returns the value of the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right Element</em>' reference isn't clear, there really should be more
	 * of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Right Element</em>' reference.
	 * @see #setRightElement(EObject)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getUpdateModelElement_RightElement()
	 * @model
	 * @generated
	 */
	EObject getRightElement();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.UpdateModelElement#getRightElement <em>Right Element</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Right Element</em>' reference.
	 * @see #getRightElement()
	 * @generated
	 */
	void setRightElement(EObject value);

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
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getUpdateModelElement_LeftElement()
	 * @model
	 * @generated
	 */
	EObject getLeftElement();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.UpdateModelElement#getLeftElement <em>Left Element</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Left Element</em>' reference.
	 * @see #getLeftElement()
	 * @generated
	 */
	void setLeftElement(EObject value);

} // UpdateModelElement
