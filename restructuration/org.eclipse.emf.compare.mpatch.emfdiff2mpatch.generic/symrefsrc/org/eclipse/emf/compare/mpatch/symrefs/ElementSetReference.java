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
 * $Id: ElementSetReference.java,v 1.1 2010/09/10 15:32:55 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.symrefs;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.IElementReference;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Element Set Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.ElementSetReference#getConditions <em>Conditions</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.ElementSetReference#getContext <em>Context</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.symrefs.SymrefsPackage#getElementSetReference()
 * @model
 * @generated
 */
public interface ElementSetReference extends IElementReference {
	/**
	 * Returns the value of the '<em><b>Context</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Context</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Context</em>' containment reference.
	 * @see #setContext(IElementReference)
	 * @see org.eclipse.emf.compare.mpatch.symrefs.SymrefsPackage#getElementSetReference_Context()
	 * @model containment="true"
	 * @generated
	 */
	IElementReference getContext();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.symrefs.ElementSetReference#getContext <em>Context</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Context</em>' containment reference.
	 * @see #getContext()
	 * @generated
	 */
	void setContext(IElementReference value);

	/**
	 * Returns the value of the '<em><b>Conditions</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.mpatch.symrefs.Condition}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.compare.mpatch.symrefs.Condition#getElementReference <em>Element Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Conditions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Conditions</em>' containment reference list.
	 * @see org.eclipse.emf.compare.mpatch.symrefs.SymrefsPackage#getElementSetReference_Conditions()
	 * @see org.eclipse.emf.compare.mpatch.symrefs.Condition#getElementReference
	 * @model opposite="elementReference" containment="true"
	 * @generated
	 */
	EList<Condition> getConditions();

} // ElementSetReference
