/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2;

import org.eclipse.uml2.uml.Extend;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Extend Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.uml2.ExtendChange#getExtend <em>Extend</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.uml2.UMLComparePackage#getExtendChange()
 * @model
 * @generated
 */
public interface ExtendChange extends UMLDiff {
	/**
	 * Returns the value of the '<em><b>Extend</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extend</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extend</em>' reference.
	 * @see #setExtend(Extend)
	 * @see org.eclipse.emf.compare.uml2.UMLComparePackage#getExtendChange_Extend()
	 * @model
	 * @generated
	 */
	Extend getExtend();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.uml2.ExtendChange#getExtend <em>Extend</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extend</em>' reference.
	 * @see #getExtend()
	 * @generated
	 */
	void setExtend(Extend value);

} // ExtendChange
