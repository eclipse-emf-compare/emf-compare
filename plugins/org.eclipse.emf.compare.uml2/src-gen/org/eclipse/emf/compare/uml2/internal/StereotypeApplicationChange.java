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
package org.eclipse.emf.compare.uml2.internal;

import org.eclipse.uml2.uml.Stereotype;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Stereotype Application Change</b></em>
 * '. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange#getStereotype <em>Stereotype</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.uml2.internal.UMLComparePackage#getStereotypeApplicationChange()
 * @model
 * @generated
 */
public interface StereotypeApplicationChange extends UMLDiff {
	/**
	 * Returns the value of the '<em><b>Stereotype</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Stereotype</em>' reference isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Stereotype</em>' reference.
	 * @see #setStereotype(Stereotype)
	 * @see org.eclipse.emf.compare.uml2.internal.UMLComparePackage#getStereotypeApplicationChange_Stereotype()
	 * @model
	 * @generated
	 */
	Stereotype getStereotype();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange#getStereotype <em>Stereotype</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Stereotype</em>' reference.
	 * @see #getStereotype()
	 * @generated
	 */
	void setStereotype(Stereotype value);

} // StereotypeApplicationChange
