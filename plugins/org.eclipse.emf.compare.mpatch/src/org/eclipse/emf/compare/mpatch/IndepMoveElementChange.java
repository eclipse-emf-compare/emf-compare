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
 * $Id: IndepMoveElementChange.java,v 1.1 2010/09/10 15:23:07 cbrun Exp $
 */
package org.eclipse.emf.compare.mpatch;

import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Indep Move Element Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IndepMoveElementChange#getOldContainment <em>Old Containment</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IndepMoveElementChange#getNewContainment <em>New Containment</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IndepMoveElementChange#getOldParent <em>Old Parent</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.IndepMoveElementChange#getNewParent <em>New Parent</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepMoveElementChange()
 * @model
 * @generated
 */
public interface IndepMoveElementChange extends IndepElementChange {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2010, 2011 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";

	/**
	 * Returns the value of the '<em><b>Old Containment</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Containment</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Old Containment</em>' reference.
	 * @see #setOldContainment(EReference)
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepMoveElementChange_OldContainment()
	 * @model required="true"
	 * @generated
	 */
	EReference getOldContainment();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.IndepMoveElementChange#getOldContainment <em>Old Containment</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Old Containment</em>' reference.
	 * @see #getOldContainment()
	 * @generated
	 */
	void setOldContainment(EReference value);

	/**
	 * Returns the value of the '<em><b>New Containment</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Containment</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Containment</em>' reference.
	 * @see #setNewContainment(EReference)
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepMoveElementChange_NewContainment()
	 * @model required="true"
	 * @generated
	 */
	EReference getNewContainment();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.IndepMoveElementChange#getNewContainment <em>New Containment</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Containment</em>' reference.
	 * @see #getNewContainment()
	 * @generated
	 */
	void setNewContainment(EReference value);

	/**
	 * Returns the value of the '<em><b>Old Parent</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Parent</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Old Parent</em>' containment reference.
	 * @see #setOldParent(IElementReference)
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepMoveElementChange_OldParent()
	 * @model containment="true" required="true"
	 * @generated
	 */
	IElementReference getOldParent();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.IndepMoveElementChange#getOldParent <em>Old Parent</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Old Parent</em>' containment reference.
	 * @see #getOldParent()
	 * @generated
	 */
	void setOldParent(IElementReference value);

	/**
	 * Returns the value of the '<em><b>New Parent</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Parent</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Parent</em>' containment reference.
	 * @see #setNewParent(IElementReference)
	 * @see org.eclipse.emf.compare.mpatch.MPatchPackage#getIndepMoveElementChange_NewParent()
	 * @model containment="true" required="true"
	 * @generated
	 */
	IElementReference getNewParent();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.IndepMoveElementChange#getNewParent <em>New Parent</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Parent</em>' containment reference.
	 * @see #getNewParent()
	 * @generated
	 */
	void setNewParent(IElementReference value);

} // IndepMoveElementChange
