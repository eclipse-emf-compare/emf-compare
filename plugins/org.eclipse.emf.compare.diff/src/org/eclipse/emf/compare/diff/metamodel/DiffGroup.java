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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Group</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup#getLeftParent <em>Left Parent</em>}</li>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup#getSubchanges <em>Subchanges</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffGroup()
 * @model
 * @generated
 */
@SuppressWarnings("nls")
public interface DiffGroup extends DiffElement {
	/**
	 * Returns the value of the '<em><b>Left Parent</b></em>' reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left Parent</em>' reference isn't clear, there really should be more of
	 * a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Left Parent</em>' reference.
	 * @see #setLeftParent(EObject)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffGroup_LeftParent()
	 * @model
	 * @generated
	 */
	EObject getLeftParent();

	/**
	 * Returns the value of the '<em><b>Subchanges</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Subchanges</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Subchanges</em>' attribute.
	 * @see #isSetSubchanges()
	 * @see #unsetSubchanges()
	 * @see #setSubchanges(int)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffGroup_Subchanges()
	 * @model unsettable="true" transient="true" derived="true"
	 * @generated
	 */
	int getSubchanges();

	/**
	 * Returns whether the value of the '{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup#getSubchanges <em>Subchanges</em>}'
	 * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Subchanges</em>' attribute is set.
	 * @see #unsetSubchanges()
	 * @see #getSubchanges()
	 * @see #setSubchanges(int)
	 * @generated
	 */
	boolean isSetSubchanges();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup#getLeftParent <em>Left Parent</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Left Parent</em>' reference.
	 * @see #getLeftParent()
	 * @generated
	 */
	void setLeftParent(EObject value);

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup#getSubchanges <em>Subchanges</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Subchanges</em>' attribute.
	 * @see #isSetSubchanges()
	 * @see #unsetSubchanges()
	 * @see #getSubchanges()
	 * @generated
	 */
	void setSubchanges(int value);

	/**
	 * Unsets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup#getSubchanges <em>Subchanges</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetSubchanges()
	 * @see #getSubchanges()
	 * @see #setSubchanges(int)
	 * @generated
	 */
	void unsetSubchanges();

} // DiffGroup
