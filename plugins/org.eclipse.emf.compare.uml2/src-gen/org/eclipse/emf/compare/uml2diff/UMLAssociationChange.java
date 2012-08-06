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
package org.eclipse.emf.compare.uml2diff;

import org.eclipse.emf.compare.Diff;
import org.eclipse.uml2.uml.Association;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>UML Association Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.uml2diff.UMLAssociationChange#getAssociation <em>Association</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.uml2diff.Uml2diffPackage#getUMLAssociationChange()
 * @model
 * @generated
 */
public interface UMLAssociationChange extends UMLExtension {

	/**
	 * Returns the value of the '<em><b>Association</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Association</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Association</em>' reference.
	 * @see #setAssociation(Association)
	 * @see org.eclipse.emf.compare.uml2diff.Uml2diffPackage#getUMLAssociationChange_Association()
	 * @model
	 * @generated
	 */
	Association getAssociation();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.uml2diff.UMLAssociationChange#getAssociation <em>Association</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Association</em>' reference.
	 * @see #getAssociation()
	 * @generated
	 */
	void setAssociation(Association value);
} // UMLAssociationChange
