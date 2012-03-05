/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Group</b></em>'. <!--
 * end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * DiffGroups are used as container for differences so that we can maintain the original models' hierarchy within the differences model.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup#getRightParent <em>Right Parent</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup#getSubchanges <em>Subchanges</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffGroup()
 * @model
 * @generated
 */
public interface DiffGroup extends DiffElement {
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
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffGroup_RightParent()
	 * @model
	 * @generated
	 */
	EObject getRightParent();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup#getRightParent <em>Right Parent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Right Parent</em>' reference.
	 * @see #getRightParent()
	 * @generated
	 */
	void setRightParent(EObject value);

	/**
	 * Returns the value of the '<em><b>Subchanges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Subchanges</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Subchanges</em>' attribute.
	 * @see #isSetSubchanges()
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffGroup_Subchanges()
	 * @model unsettable="true" transient="true" changeable="false" derived="true"
	 * @generated
	 */
	int getSubchanges();

	/**
	 * Returns whether the value of the '{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup#getSubchanges <em>Subchanges</em>}' attribute is set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return whether the value of the '<em>Subchanges</em>' attribute is set.
	 * @see #getSubchanges()
	 * @generated
	 */
	boolean isSetSubchanges();

} // DiffGroup
