/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: SubModelBinding.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sub Model Binding</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.SubModelBinding#getModelDescriptor <em>Model Descriptor</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.SubModelBinding#getSubModelReferences <em>Sub Model References</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.SubModelBinding#getSelfElement <em>Self Element</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.SubModelBinding#getSelfReference <em>Self Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getSubModelBinding()
 * @model
 * @generated
 */
public interface SubModelBinding extends ElementChangeBinding {
	/**
	 * Returns the value of the '<em><b>Model Descriptor</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Descriptor</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Descriptor</em>' reference.
	 * @see #setModelDescriptor(IModelDescriptor)
	 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getSubModelBinding_ModelDescriptor()
	 * @model required="true"
	 * @generated
	 */
	IModelDescriptor getModelDescriptor();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.binding.SubModelBinding#getModelDescriptor <em>Model Descriptor</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model Descriptor</em>' reference.
	 * @see #getModelDescriptor()
	 * @generated
	 */
	void setModelDescriptor(IModelDescriptor value);

	/**
	 * Returns the value of the '<em><b>Sub Model References</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sub Model References</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sub Model References</em>' containment reference list.
	 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getSubModelBinding_SubModelReferences()
	 * @model containment="true"
	 * @generated
	 */
	EList<ElementChangeBinding> getSubModelReferences();

	/**
	 * Returns the value of the '<em><b>Self Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Self Element</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Self Element</em>' reference.
	 * @see #setSelfElement(EObject)
	 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getSubModelBinding_SelfElement()
	 * @model
	 * @generated
	 */
	EObject getSelfElement();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.binding.SubModelBinding#getSelfElement <em>Self Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Self Element</em>' reference.
	 * @see #getSelfElement()
	 * @generated
	 */
	void setSelfElement(EObject value);

	/**
	 * Returns the value of the '<em><b>Self Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Self Reference</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Self Reference</em>' reference.
	 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getSubModelBinding_SelfReference()
	 * @model required="true" transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	IElementReference getSelfReference();

} // SubModelBinding
