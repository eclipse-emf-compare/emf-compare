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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Model Element Change Right Target</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget#getLeftParent <em>Left Parent</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget#getRightElement <em>Right Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getModelElementChangeRightTarget()
 * @model
 * @generated
 */
public interface ModelElementChangeRightTarget extends ModelElementChange {
	/**
	 * Returns the value of the '<em><b>Left Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left Parent</em>' reference isn't clear, there really should be more of
	 * a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Left Parent</em>' reference.
	 * @see #setLeftParent(EObject)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getModelElementChangeRightTarget_LeftParent()
	 * @model
	 * @generated
	 */
	EObject getLeftParent();

	/**
	 * Returns the value of the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right Element</em>' reference isn't clear, there really should be more
	 * of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Right Element</em>' reference.
	 * @see #setRightElement(EObject)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getModelElementChangeRightTarget_RightElement()
	 * @model
	 * @generated
	 */
	EObject getRightElement();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget#getLeftParent <em>Left Parent</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Left Parent</em>' reference.
	 * @see #getLeftParent()
	 * @generated
	 */
	void setLeftParent(EObject value);

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget#getRightElement <em>Right Element</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Right Element</em>' reference.
	 * @see #getRightElement()
	 * @generated
	 */
	void setRightElement(EObject value);

} // ModelElementChangeRightTarget
