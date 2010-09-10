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
 * $Id: MPatchModel.java,v 1.1 2010/09/10 15:23:07 cbrun Exp $
 */
package org.eclipse.emf.compare.mpatch;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MPatch</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.MPatchModel#getChanges <em>Changes</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.MPatchModel#getOldModel <em>Old Model</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.MPatchModel#getNewModel <em>New Model</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.MPatchModel#getEmfdiff <em>Emfdiff</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getMPatchModel()
 * @model
 * @generated
 */
public interface MPatchModel extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2010 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";

	/**
	 * Returns the value of the '<em><b>Changes</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.mpatch.IndepChange}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Changes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Changes</em>' containment reference list.
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getMPatchModel_Changes()
	 * @model containment="true"
	 * @generated
	 */
	EList<IndepChange> getChanges();

	/**
	 * Returns the value of the '<em><b>Old Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Model</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Old Model</em>' attribute.
	 * @see #setOldModel(String)
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getMPatchModel_OldModel()
	 * @model
	 * @generated
	 */
	String getOldModel();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.MPatchModel#getOldModel <em>Old Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Old Model</em>' attribute.
	 * @see #getOldModel()
	 * @generated
	 */
	void setOldModel(String value);

	/**
	 * Returns the value of the '<em><b>New Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Model</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Model</em>' attribute.
	 * @see #setNewModel(String)
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getMPatchModel_NewModel()
	 * @model
	 * @generated
	 */
	String getNewModel();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.MPatchModel#getNewModel <em>New Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Model</em>' attribute.
	 * @see #getNewModel()
	 * @generated
	 */
	void setNewModel(String value);

	/**
	 * Returns the value of the '<em><b>Emfdiff</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Emfdiff</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Emfdiff</em>' attribute.
	 * @see #setEmfdiff(String)
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getMPatchModel_Emfdiff()
	 * @model
	 * @generated
	 */
	String getEmfdiff();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.MPatchModel#getEmfdiff <em>Emfdiff</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Emfdiff</em>' attribute.
	 * @see #getEmfdiff()
	 * @generated
	 */
	void setEmfdiff(String value);

} // MPatchModel
