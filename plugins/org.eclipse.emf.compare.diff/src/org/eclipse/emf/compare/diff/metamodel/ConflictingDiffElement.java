/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Conflicting Diff Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This will act as a container for conflictual changes.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getLeftParent <em>Left Parent</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getRightParent <em>Right Parent</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getOriginElement <em>Origin Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getConflictingDiffElement()
 * @model
 * @generated
 */
public interface ConflictingDiffElement extends DiffElement {
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
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getConflictingDiffElement_LeftParent()
	 * @model
	 * @generated
	 */
	EObject getLeftParent();

	/**
	 * Returns the value of the '<em><b>Origin Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Origin Element</em>' reference isn't clear, there really should be more
	 * of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Origin Element</em>' reference.
	 * @see #setOriginElement(EObject)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getConflictingDiffElement_OriginElement()
	 * @model
	 * @generated
	 */
	EObject getOriginElement();

	/**
	 * Returns the value of the '<em><b>Right Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right Parent</em>' reference isn't clear, there really should be more of
	 * a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Right Parent</em>' reference.
	 * @see #setRightParent(EObject)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getConflictingDiffElement_RightParent()
	 * @model
	 * @generated
	 */
	EObject getRightParent();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getLeftParent <em>Left Parent</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Left Parent</em>' reference.
	 * @see #getLeftParent()
	 * @generated
	 */
	void setLeftParent(EObject value);

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getOriginElement <em>Origin Element</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Origin Element</em>' reference.
	 * @see #getOriginElement()
	 * @generated
	 */
	void setOriginElement(EObject value);

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getRightParent <em>Right Parent</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Right Parent</em>' reference.
	 * @see #getRightParent()
	 * @generated
	 */
	void setRightParent(EObject value);

} // ConflictingDiffElement
