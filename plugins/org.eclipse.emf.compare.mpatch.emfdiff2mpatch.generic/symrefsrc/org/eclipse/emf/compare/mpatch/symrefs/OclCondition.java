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
 * $Id: OclCondition.java,v 1.1 2010/09/10 15:32:55 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.symrefs;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ocl Condition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.OclCondition#getExpression <em>Expression</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.OclCondition#isCheckType <em>Check Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.symrefs.SymrefsPackage#getOclCondition()
 * @model
 * @generated
 */
public interface OclCondition extends Condition {
	/**
	 * Returns the value of the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expression</em>' attribute.
	 * @see #setExpression(String)
	 * @see org.eclipse.emf.compare.mpatch.symrefs.SymrefsPackage#getOclCondition_Expression()
	 * @model
	 * @generated
	 */
	String getExpression();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.symrefs.OclCondition#getExpression <em>Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expression</em>' attribute.
	 * @see #getExpression()
	 * @generated
	 */
	void setExpression(String value);

	/**
	 * Returns the value of the '<em><b>Check Type</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Check Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Check Type</em>' attribute.
	 * @see #setCheckType(boolean)
	 * @see org.eclipse.emf.compare.mpatch.symrefs.SymrefsPackage#getOclCondition_CheckType()
	 * @model default="true"
	 * @generated
	 */
	boolean isCheckType();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.symrefs.OclCondition#isCheckType <em>Check Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Check Type</em>' attribute.
	 * @see #isCheckType()
	 * @generated
	 */
	void setCheckType(boolean value);

} // OclCondition
