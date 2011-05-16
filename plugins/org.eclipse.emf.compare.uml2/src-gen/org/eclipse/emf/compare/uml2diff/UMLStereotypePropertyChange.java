/**
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2diff;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>UML Stereotype Property Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange#getStereotypeApplications <em>Stereotype Applications</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.uml2diff.UML2DiffPackage#getUMLStereotypePropertyChange()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface UMLStereotypePropertyChange extends UMLDiffExtension {
	/**
	 * Returns the value of the '<em><b>Stereotype Applications</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Stereotype Applications</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Stereotype Applications</em>' reference list.
	 * @see org.eclipse.emf.compare.uml2diff.UML2DiffPackage#getUMLStereotypePropertyChange_StereotypeApplications()
	 * @model
	 * @generated
	 */
	EList<EObject> getStereotypeApplications();

} // UMLStereotypePropertyChange
