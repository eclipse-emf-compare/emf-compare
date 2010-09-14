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
 * $Id: IModelDescriptor.java,v 1.2 2010/09/14 09:45:44 pkonemann Exp $
 */
package org.eclipse.emf.compare.mpatch;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IModel Descriptor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IModelDescriptor#getCrossReferences <em>Cross References</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IModelDescriptor#getAllCrossReferences <em>All Cross References</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IModelDescriptor#getSelfReference <em>Self Reference</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IModelDescriptor#getAllSelfReferences <em>All Self References</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IModelDescriptor#getSubModelDescriptors <em>Sub Model Descriptors</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IModelDescriptor#getDescriptorUris <em>Descriptor Uris</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IModelDescriptor#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIModelDescriptor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IModelDescriptor extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2010 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";

	/**
	 * Returns the value of the '<em><b>Cross References</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.mpatch.IElementReference}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Returns all cross-references for this model descriptors (and not for sub descriptors).
	 * Cross-references are those which refer to other model elements.
	 * Note that this does also include self references!
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Cross References</em>' reference list.
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIModelDescriptor_CrossReferences()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	EList<IElementReference> getCrossReferences();

	/**
	 * Returns the value of the '<em><b>All Cross References</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.mpatch.IElementReference}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Returns all cross-references for this as well as for all subdescriptors.
	 * Cross-references are those which refer to other model elements.
	 * Note that this does also include self references!
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>All Cross References</em>' reference list.
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIModelDescriptor_AllCrossReferences()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	EList<IElementReference> getAllCrossReferences();

	/**
	 * Returns the value of the '<em><b>Descriptor Uris</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Descriptor Uris</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Descriptor Uris</em>' attribute list.
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIModelDescriptor_DescriptorUris()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	EList<String> getDescriptorUris();

	/**
	 * Returns the value of the '<em><b>All Self References</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.mpatch.IElementReference}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Returns the local self reference as well as the self references from all sub descriptors.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>All Self References</em>' reference list.
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIModelDescriptor_AllSelfReferences()
	 * @model required="true" transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	EList<IElementReference> getAllSelfReferences();

	/**
	 * Returns the value of the '<em><b>Sub Model Descriptors</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.mpatch.IModelDescriptor}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Returns all sub descriptors of this model descriptors.
	 * It does not include nested model descriptors but only direct children!
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Sub Model Descriptors</em>' reference list.
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIModelDescriptor_SubModelDescriptors()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	EList<IModelDescriptor> getSubModelDescriptors();

	/**
	 * Returns the value of the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The eclass of the element that is described.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Type</em>' reference.
	 * @see #setType(EClass)
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIModelDescriptor_Type()
	 * @model required="true"
	 * @generated
	 */
	EClass getType();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.IModelDescriptor#getType <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' reference.
	 * @see #getType()
	 * @generated
	 */
	void setType(EClass value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This applies the described element to a given modelElement using the given containment.
	 * I.e. all model-descriptors restore their structure, that is the model elements and their attributes.
	 * 
	 * Please note that the references must be restored separately using <b>applyCrossReferences()</b>!
	 * The reason for the separation is that the restoring of cross references requires all model elements to exist before.
	 * 
	 * The result is a map from all model elements within the containment tree of the created element to their respective model descriptors.
	 * <!-- end-model-doc -->
	 * @model mapType="org.eclipse.emf.compare.mpatch.EObjectToIModelDescriptorMap<org.eclipse.emf.ecore.EObject, org.eclipse.emf.compare.mpatch.IModelDescriptor>"
	 * @generated
	 */
	EMap<EObject, IModelDescriptor> applyStructure(EObject modelElement, EReference containment);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This is a semantical equals-check; if the arguments and the object itself describe the same model elements, this operation must return true.
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	boolean describesEqual(IModelDescriptor other);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This operation checks whether the given element is described by this model descriptor.
	 * However, the check only includes the containment tree and optionally the elements' attributes, never their references!
	 * 
	 * Just like applyStructure, the result is a map from all model elements within the containment tree of the given element to their respective model descriptors.
	 * <!-- end-model-doc -->
	 * @model mapType="org.eclipse.emf.compare.mpatch.EObjectToIModelDescriptorMap<org.eclipse.emf.ecore.EObject, org.eclipse.emf.compare.mpatch.IModelDescriptor>"
	 * @generated
	 */
	EMap<EObject, IModelDescriptor> isDescriptorFor(EObject element, boolean checkAttributes);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This restores all cross references of the object given in the parameter.
	 * The parameter must be created by <b>applyStructure</b> before!
	 * 
	 * It returns all references which were <b>not</b> successfully restored; this is a subset of <b>crossReferences</b>.
	 * <!-- end-model-doc -->
	 * @model resolvedCrossReferencesMapType="org.eclipse.emf.compare.mpatch.ElementReferenceToEObjectMap<org.eclipse.emf.compare.mpatch.IElementReference, org.eclipse.emf.ecore.EObject>"
	 * @generated
	 */
	EList<IElementReference> applyCrossReferences(EObject createdObject, EMap<IElementReference, EList<EObject>> resolvedCrossReferences);

	/**
	 * Returns the value of the '<em><b>Self Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Self Reference</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Returns a symbolic reference to the element this particular model descriptor describes.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Self Reference</em>' containment reference.
	 * @see #setSelfReference(IElementReference)
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIModelDescriptor_SelfReference()
	 * @model containment="true" required="true"
	 * @generated
	 */
	IElementReference getSelfReference();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.IModelDescriptor#getSelfReference <em>Self Reference</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Self Reference</em>' containment reference.
	 * @see #getSelfReference()
	 * @generated
	 */
	void setSelfReference(IElementReference value);

} // IModelDescriptor
