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
 * $Id: IndepAddRemElementChange.java,v 1.1 2010/09/10 15:23:07 cbrun Exp $
 */
package org.eclipse.emf.compare.mpatch;

import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Indep Add Rem Element Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IndepAddRemElementChange#getSubModel <em>Sub Model</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IndepAddRemElementChange#getContainment <em>Containment</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IndepAddRemElementChange#getSubModelReference <em>Sub Model Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepAddRemElementChange()
 * @model abstract="true"
 * @generated
 */
public interface IndepAddRemElementChange extends IndepElementChange {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2010 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";

	/**
	 * Returns the value of the '<em><b>Sub Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The sub-model descriptor contains an abstract description of a sub-model, including contained elements as well as outgoing references.
	 * IModelDescriptor.requiredReferences returns a list of all outgoing references.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Sub Model</em>' containment reference.
	 * @see #setSubModel(IModelDescriptor)
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepAddRemElementChange_SubModel()
	 * @model containment="true" required="true"
	 * @generated
	 */
	IModelDescriptor getSubModel();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.IndepAddRemElementChange#getSubModel <em>Sub Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sub Model</em>' containment reference.
	 * @see #getSubModel()
	 * @generated
	 */
	void setSubModel(IModelDescriptor value);

	/**
	 * Returns the value of the '<em><b>Containment</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The structural feature of the correspondingElement which contains the added or removed element.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Containment</em>' reference.
	 * @see #setContainment(EReference)
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepAddRemElementChange_Containment()
	 * @model required="true"
	 * @generated
	 */
	EReference getContainment();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.IndepAddRemElementChange#getContainment <em>Containment</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Containment</em>' reference.
	 * @see #getContainment()
	 * @generated
	 */
	void setContainment(EReference value);

	/**
	 * Returns the value of the '<em><b>Sub Model Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sub Model Reference</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sub Model Reference</em>' containment reference.
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepAddRemElementChange_SubModelReference()
	 * @model containment="true" required="true" transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	IElementReference getSubModelReference();

} // IndepAddRemElementChange
