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
 * $Id: UpdateReferenceChangeBinding.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Update Reference Change Binding</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.UpdateReferenceChangeBinding#getNewReference <em>New Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getUpdateReferenceChangeBinding()
 * @model
 * @generated
 */
public interface UpdateReferenceChangeBinding extends ChangeBinding {
	/**
	 * Returns the value of the '<em><b>New Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Reference</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Reference</em>' containment reference.
	 * @see #setNewReference(ElementChangeBinding)
	 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getUpdateReferenceChangeBinding_NewReference()
	 * @model containment="true"
	 * @generated
	 */
	ElementChangeBinding getNewReference();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.binding.UpdateReferenceChangeBinding#getNewReference <em>New Reference</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Reference</em>' containment reference.
	 * @see #getNewReference()
	 * @generated
	 */
	void setNewReference(ElementChangeBinding value);

} // UpdateReferenceChangeBinding
