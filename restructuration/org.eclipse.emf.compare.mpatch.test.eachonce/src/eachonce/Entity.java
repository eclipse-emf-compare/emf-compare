/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: Entity.java,v 1.1 2010/09/10 15:40:34 cbrun Exp $
 *******************************************************************************/
package eachonce;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eachonce.Entity#getSingleAttribute <em>Single Attribute</em>}</li>
 *   <li>{@link eachonce.Entity#getMultiAttribute <em>Multi Attribute</em>}</li>
 *   <li>{@link eachonce.Entity#getSingleReference <em>Single Reference</em>}</li>
 *   <li>{@link eachonce.Entity#getMultiReference <em>Multi Reference</em>}</li>
 *   <li>{@link eachonce.Entity#getChildren <em>Children</em>}</li>
 *   <li>{@link eachonce.Entity#getAdditionalAttribute <em>Additional Attribute</em>}</li>
 *   <li>{@link eachonce.Entity#getChildren2 <em>Children2</em>}</li>
 * </ul>
 * </p>
 *
 * @see eachonce.EachoncePackage#getEntity()
 * @model
 * @generated
 */
public interface Entity extends EObject {
	/**
	 * Returns the value of the '<em><b>Single Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Single Attribute</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Single Attribute</em>' attribute.
	 * @see #setSingleAttribute(String)
	 * @see eachonce.EachoncePackage#getEntity_SingleAttribute()
	 * @model id="true"
	 * @generated
	 */
	String getSingleAttribute();

	/**
	 * Sets the value of the '{@link eachonce.Entity#getSingleAttribute <em>Single Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Single Attribute</em>' attribute.
	 * @see #getSingleAttribute()
	 * @generated
	 */
	void setSingleAttribute(String value);

	/**
	 * Returns the value of the '<em><b>Multi Attribute</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multi Attribute</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multi Attribute</em>' attribute list.
	 * @see eachonce.EachoncePackage#getEntity_MultiAttribute()
	 * @model
	 * @generated
	 */
	EList<String> getMultiAttribute();

	/**
	 * Returns the value of the '<em><b>Single Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Single Reference</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Single Reference</em>' reference.
	 * @see #setSingleReference(Entity)
	 * @see eachonce.EachoncePackage#getEntity_SingleReference()
	 * @model
	 * @generated
	 */
	Entity getSingleReference();

	/**
	 * Sets the value of the '{@link eachonce.Entity#getSingleReference <em>Single Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Single Reference</em>' reference.
	 * @see #getSingleReference()
	 * @generated
	 */
	void setSingleReference(Entity value);

	/**
	 * Returns the value of the '<em><b>Multi Reference</b></em>' reference list.
	 * The list contents are of type {@link eachonce.Entity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multi Reference</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multi Reference</em>' reference list.
	 * @see eachonce.EachoncePackage#getEntity_MultiReference()
	 * @model
	 * @generated
	 */
	EList<Entity> getMultiReference();

	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link eachonce.Entity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see eachonce.EachoncePackage#getEntity_Children()
	 * @model containment="true"
	 * @generated
	 */
	EList<Entity> getChildren();

	/**
	 * Returns the value of the '<em><b>Additional Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Additional Attribute</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Additional Attribute</em>' attribute.
	 * @see #setAdditionalAttribute(String)
	 * @see eachonce.EachoncePackage#getEntity_AdditionalAttribute()
	 * @model
	 * @generated
	 */
	String getAdditionalAttribute();

	/**
	 * Sets the value of the '{@link eachonce.Entity#getAdditionalAttribute <em>Additional Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Additional Attribute</em>' attribute.
	 * @see #getAdditionalAttribute()
	 * @generated
	 */
	void setAdditionalAttribute(String value);

	/**
	 * Returns the value of the '<em><b>Children2</b></em>' containment reference list.
	 * The list contents are of type {@link eachonce.Entity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children2</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children2</em>' containment reference list.
	 * @see eachonce.EachoncePackage#getEntity_Children2()
	 * @model containment="true"
	 * @generated
	 */
	EList<Entity> getChildren2();

} // Entity
