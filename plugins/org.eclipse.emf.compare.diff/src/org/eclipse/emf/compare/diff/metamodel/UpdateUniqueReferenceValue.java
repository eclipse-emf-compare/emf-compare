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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Update Unique Reference Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue#getLeftTarget <em>Left Target</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue#getRightTarget <em>Right Target</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getUpdateUniqueReferenceValue()
 * @model
 * @generated
 */
public interface UpdateUniqueReferenceValue extends UpdateReference {
	/**
	 * Returns the value of the '<em><b>Left Target</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left Target</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Left Target</em>' reference list.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getUpdateUniqueReferenceValue_LeftTarget()
	 * @model type="org.eclipse.emf.ecore.EObject"
	 * @generated
	 */
	EList getLeftTarget();

	/**
	 * Returns the value of the '<em><b>Right Target</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right Target</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Right Target</em>' reference list.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getUpdateUniqueReferenceValue_RightTarget()
	 * @model type="org.eclipse.emf.ecore.EObject"
	 * @generated
	 */
	EList getRightTarget();

} // UpdateUniqueReferenceValue
