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
 * $Id: ChangeBinding.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.IndepChange;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Change Binding</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.ChangeBinding#getChange <em>Change</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.ChangeBinding#getCorrespondingElements <em>Corresponding Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getChangeBinding()
 * @model abstract="true"
 * @generated
 */
public interface ChangeBinding extends NoteElement {
	/**
	 * Returns the value of the '<em><b>Change</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Change</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Change</em>' reference.
	 * @see #setChange(IndepChange)
	 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getChangeBinding_Change()
	 * @model required="true"
	 * @generated
	 */
	IndepChange getChange();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.binding.ChangeBinding#getChange <em>Change</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Change</em>' reference.
	 * @see #getChange()
	 * @generated
	 */
	void setChange(IndepChange value);

	/**
	 * Returns the value of the '<em><b>Corresponding Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Corresponding Elements</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Corresponding Elements</em>' containment reference list.
	 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getChangeBinding_CorrespondingElements()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<ElementChangeBinding> getCorrespondingElements();

} // ChangeBinding
