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
 * A representation of the model object '<em><b>Conflicting Diff Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getLeftParent <em>Left Parent</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getRightParent <em>Right Parent</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getLeftDiff <em>Left Diff</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getRightDiff <em>Right Diff</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getConflictingDiffElement()
 * @model
 * @generated
 */
@SuppressWarnings("nls")
public interface ConflictingDiffElement extends DiffElement {
	/**
	 * Returns the value of the '<em><b>Left Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left Parent</em>' reference isn't clear,
	 * there really should be more of a description here...
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
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getLeftParent <em>Left Parent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Left Parent</em>' reference.
	 * @see #getLeftParent()
	 * @generated
	 */
	void setLeftParent(EObject value);

	/**
	 * Returns the value of the '<em><b>Right Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right Parent</em>' reference isn't clear,
	 * there really should be more of a description here...
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
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getRightParent <em>Right Parent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Right Parent</em>' reference.
	 * @see #getRightParent()
	 * @generated
	 */
	void setRightParent(EObject value);

	/**
	 * Returns the value of the '<em><b>Left Diff</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left Diff</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Left Diff</em>' containment reference.
	 * @see #setLeftDiff(ConflictingDiffGroup)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getConflictingDiffElement_LeftDiff()
	 * @model containment="true"
	 * @generated
	 */
	ConflictingDiffGroup getLeftDiff();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getLeftDiff <em>Left Diff</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Left Diff</em>' containment reference.
	 * @see #getLeftDiff()
	 * @generated
	 */
	void setLeftDiff(ConflictingDiffGroup value);

	/**
	 * Returns the value of the '<em><b>Right Diff</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right Diff</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Right Diff</em>' containment reference.
	 * @see #setRightDiff(ConflictingDiffGroup)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getConflictingDiffElement_RightDiff()
	 * @model containment="true"
	 * @generated
	 */
	ConflictingDiffGroup getRightDiff();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getRightDiff <em>Right Diff</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Right Diff</em>' containment reference.
	 * @see #getRightDiff()
	 * @generated
	 */
	void setRightDiff(ConflictingDiffGroup value);

} // ConflictingDiffElement