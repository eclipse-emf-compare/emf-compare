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
 * $Id: EMFModelDescriptor.java,v 1.1 2010/09/10 15:32:56 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.descriptor;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EMF Model Descriptor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor#getAttributes <em>Attributes</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor#getSubDescriptors <em>Sub Descriptors</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor#getReferences <em>References</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor#getDescriptorUri <em>Descriptor Uri</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.descriptor.DescriptorPackage#getEMFModelDescriptor()
 * @model
 * @generated
 */
public interface EMFModelDescriptor extends IModelDescriptor {
	/**
	 * Returns the value of the '<em><b>Attributes</b></em>' map.
	 * The key is of type {@link org.eclipse.emf.ecore.EAttribute},
	 * and the value is of type list of {@link java.lang.Object},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This map contains the values of all attributes of the described element.
	 * The keys are the structural features of the attributes, the values are plain old java objects:
	 * So this might be Strings, Integers, actually any kind of serializable object.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Attributes</em>' map.
	 * @see org.eclipse.emf.compare.mpatch.descriptor.DescriptorPackage#getEMFModelDescriptor_Attributes()
	 * @model mapType="org.eclipse.emf.compare.mpatch.descriptor.EAttributeToObjectMap<org.eclipse.emf.ecore.EAttribute, org.eclipse.emf.ecore.EJavaObject>"
	 * @generated
	 */
	EMap<EAttribute, EList<Object>> getAttributes();

	/**
	 * Returns the value of the '<em><b>Sub Descriptors</b></em>' map.
	 * The key is of type {@link org.eclipse.emf.ecore.EReference},
	 * and the value is of type list of {@link org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This map contains model descriptors for all children.
	 * So the keys are structural features which are containments.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Sub Descriptors</em>' map.
	 * @see org.eclipse.emf.compare.mpatch.descriptor.DescriptorPackage#getEMFModelDescriptor_SubDescriptors()
	 * @model mapType="org.eclipse.emf.compare.mpatch.descriptor.EReferenceToDescriptorMap<org.eclipse.emf.ecore.EReference, org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor>"
	 * @generated
	 */
	EMap<EReference, EList<EMFModelDescriptor>> getSubDescriptors();

	/**
	 * Returns the value of the '<em><b>References</b></em>' map.
	 * The key is of type {@link org.eclipse.emf.ecore.EReference},
	 * and the value is of type list of {@link org.eclipse.emf.compare.mpatch.IElementReference},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A map containing all outgoing references of the described model element.
	 * The key is the structural feature, the value is a symbolic reference.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>References</em>' map.
	 * @see org.eclipse.emf.compare.mpatch.descriptor.DescriptorPackage#getEMFModelDescriptor_References()
	 * @model mapType="org.eclipse.emf.compare.mpatch.descriptor.EReferenceToElementReferenceMap<org.eclipse.emf.ecore.EReference, org.eclipse.emf.compare.mpatch.IElementReference>"
	 * @generated
	 */
	EMap<EReference, EList<IElementReference>> getReferences();

	/**
	 * Returns the value of the '<em><b>Descriptor Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Descriptor Uri</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Descriptor Uri</em>' attribute.
	 * @see #setDescriptorUri(String)
	 * @see org.eclipse.emf.compare.mpatch.descriptor.DescriptorPackage#getEMFModelDescriptor_DescriptorUri()
	 * @model
	 * @generated
	 */
	String getDescriptorUri();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor#getDescriptorUri <em>Descriptor Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Descriptor Uri</em>' attribute.
	 * @see #getDescriptorUri()
	 * @generated
	 */
	void setDescriptorUri(String value);

} // EMFModelDescriptor
