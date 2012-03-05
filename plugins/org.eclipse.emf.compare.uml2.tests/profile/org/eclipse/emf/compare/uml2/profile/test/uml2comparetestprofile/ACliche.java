/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>ACliche</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche#getSingleValuedAttribute
 * <em>Single Valued Attribute</em>}</li>
 * <li>{@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche#getManyValuedAttribute
 * <em>Many Valued Attribute</em>}</li>
 * <li>
 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche#getSingleValuedReference
 * <em>Single Valued Reference</em>}</li>
 * <li>{@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche#getManyValuedReference
 * <em>Many Valued Reference</em>}</li>
 * <li>{@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche#getBase_Class <em>Base
 * Class</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage#getACliche()
 * @model
 * @generated
 */
public interface ACliche extends EObject {
	/**
	 * Returns the value of the '<em><b>Single Valued Attribute</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Single Valued Attribute</em>' attribute isn't clear, there really should be
	 * more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Single Valued Attribute</em>' attribute.
	 * @see #setSingleValuedAttribute(String)
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage#getACliche_SingleValuedAttribute()
	 * @model ordered="false"
	 * @generated
	 */
	String getSingleValuedAttribute();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche#getSingleValuedAttribute
	 * <em>Single Valued Attribute</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Single Valued Attribute</em>' attribute.
	 * @see #getSingleValuedAttribute()
	 * @generated
	 */
	void setSingleValuedAttribute(String value);

	/**
	 * Returns the value of the '<em><b>Many Valued Attribute</b></em>' attribute list. The list contents are
	 * of type {@link java.lang.String}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Many Valued Attribute</em>' attribute list isn't clear, there really should
	 * be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Many Valued Attribute</em>' attribute list.
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage#getACliche_ManyValuedAttribute()
	 * @model
	 * @generated
	 */
	EList<String> getManyValuedAttribute();

	/**
	 * Returns the value of the '<em><b>Single Valued Reference</b></em>' reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Single Valued Reference</em>' reference isn't clear, there really should be
	 * more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Single Valued Reference</em>' reference.
	 * @see #setSingleValuedReference(org.eclipse.uml2.uml.Class)
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage#getACliche_SingleValuedReference()
	 * @model ordered="false"
	 * @generated
	 */
	org.eclipse.uml2.uml.Class getSingleValuedReference();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche#getSingleValuedReference
	 * <em>Single Valued Reference</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Single Valued Reference</em>' reference.
	 * @see #getSingleValuedReference()
	 * @generated
	 */
	void setSingleValuedReference(org.eclipse.uml2.uml.Class value);

	/**
	 * Returns the value of the '<em><b>Many Valued Reference</b></em>' reference list. The list contents are
	 * of type {@link org.eclipse.uml2.uml.Class}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Many Valued Reference</em>' reference list isn't clear, there really should
	 * be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Many Valued Reference</em>' reference list.
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage#getACliche_ManyValuedReference()
	 * @model
	 * @generated
	 */
	EList<org.eclipse.uml2.uml.Class> getManyValuedReference();

	/**
	 * Retrieves the first {@link org.eclipse.uml2.uml.Class} with the specified '<em><b>Name</b></em>' from
	 * the '<em><b>Many Valued Reference</b></em>' reference list. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param name
	 *            The '<em><b>Name</b></em>' of the {@link org.eclipse.uml2.uml.Class} to retrieve, or
	 *            <code>null</code>.
	 * @return The first {@link org.eclipse.uml2.uml.Class} with the specified '<em><b>Name</b></em>', or
	 *         <code>null</code>.
	 * @see #getManyValuedReference()
	 * @generated
	 */
	org.eclipse.uml2.uml.Class getManyValuedReference(String name);

	/**
	 * Retrieves the first {@link org.eclipse.uml2.uml.Class} with the specified '<em><b>Name</b></em>' from
	 * the '<em><b>Many Valued Reference</b></em>' reference list. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param name
	 *            The '<em><b>Name</b></em>' of the {@link org.eclipse.uml2.uml.Class} to retrieve, or
	 *            <code>null</code>.
	 * @param ignoreCase
	 *            Whether to ignore case in {@link java.lang.String} comparisons.
	 * @param eClass
	 *            The Ecore class of the {@link org.eclipse.uml2.uml.Class} to retrieve, or <code>null</code>.
	 * @return The first {@link org.eclipse.uml2.uml.Class} with the specified '<em><b>Name</b></em>', or
	 *         <code>null</code>.
	 * @see #getManyValuedReference()
	 * @generated
	 */
	org.eclipse.uml2.uml.Class getManyValuedReference(String name, boolean ignoreCase, EClass eClass);

	/**
	 * Returns the value of the '<em><b>Base Class</b></em>' reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Class</em>' reference isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Base Class</em>' reference.
	 * @see #setBase_Class(org.eclipse.uml2.uml.Class)
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage#getACliche_Base_Class()
	 * @model required="true" ordered="false"
	 * @generated
	 */
	org.eclipse.uml2.uml.Class getBase_Class();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche#getBase_Class
	 * <em>Base Class</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Base Class</em>' reference.
	 * @see #getBase_Class()
	 * @generated
	 */
	void setBase_Class(org.eclipse.uml2.uml.Class value);

} // ACliche
