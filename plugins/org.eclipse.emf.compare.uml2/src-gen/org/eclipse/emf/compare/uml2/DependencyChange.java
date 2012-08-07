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

import org.eclipse.uml2.uml.Dependency;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dependency Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.uml2.DependencyChange#getDependency <em>Dependency</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.uml2.UMLComparePackage#getDependencyChange()
 * @model
 * @generated
 */
public interface DependencyChange extends UMLDiff {
	/**
	 * Returns the value of the '<em><b>Dependency</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dependency</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dependency</em>' reference.
	 * @see #setDependency(Dependency)
	 * @see org.eclipse.emf.compare.uml2.UMLComparePackage#getDependencyChange_Dependency()
	 * @model
	 * @generated
	 */
	Dependency getDependency();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.uml2.DependencyChange#getDependency <em>Dependency</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dependency</em>' reference.
	 * @see #getDependency()
	 * @generated
	 */
	void setDependency(Dependency value);

} // DependencyChange
