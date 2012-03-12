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
 * $Id: ElementChangeBinding.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding;

import org.eclipse.emf.compare.mpatch.IElementReference;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Element Change Binding</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding#getElementReference <em>Element Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getElementChangeBinding()
 * @model
 * @generated
 */
public interface ElementChangeBinding extends ElementBinding {
	/**
	 * Returns the value of the '<em><b>Element Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Element Reference</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Element Reference</em>' reference.
	 * @see #setElementReference(IElementReference)
	 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage#getElementChangeBinding_ElementReference()
	 * @model required="true"
	 * @generated
	 */
	IElementReference getElementReference();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding#getElementReference <em>Element Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Element Reference</em>' reference.
	 * @see #getElementReference()
	 * @generated
	 */
	void setElementReference(IElementReference value);

} // ElementChangeBinding
