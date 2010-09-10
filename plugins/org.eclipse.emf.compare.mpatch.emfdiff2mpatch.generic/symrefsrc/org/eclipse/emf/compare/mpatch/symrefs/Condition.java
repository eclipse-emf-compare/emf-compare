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
 * $Id: Condition.java,v 1.1 2010/09/10 15:32:55 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.symrefs;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Condition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.Condition#getElementReference <em>Element Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.symrefs.SymrefsPackage#getCondition()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface Condition extends EObject {
	/**
	 * Returns the value of the '<em><b>Element Reference</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.compare.mpatch.symrefs.ElementSetReference#getConditions <em>Conditions</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Element Reference</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Element Reference</em>' container reference.
	 * @see #setElementReference(ElementSetReference)
	 * @see org.eclipse.emf.compare.mpatch.symrefs.SymrefsPackage#getCondition_ElementReference()
	 * @see org.eclipse.emf.compare.mpatch.symrefs.ElementSetReference#getConditions
	 * @model opposite="conditions" transient="false"
	 * @generated
	 */
	ElementSetReference getElementReference();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.symrefs.Condition#getElementReference <em>Element Reference</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Element Reference</em>' container reference.
	 * @see #getElementReference()
	 * @generated
	 */
	void setElementReference(ElementSetReference value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	EList<EObject> collectValidElements(EObject model);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	boolean sameCondition(Condition other);

} // Condition
