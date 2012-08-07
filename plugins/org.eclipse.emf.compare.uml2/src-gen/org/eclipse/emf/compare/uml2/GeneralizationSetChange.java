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

import org.eclipse.uml2.uml.GeneralizationSet;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Generalization Set Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.uml2.GeneralizationSetChange#getGeneralizationSet <em>Generalization Set</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.uml2.UMLComparePackage#getGeneralizationSetChange()
 * @model
 * @generated
 */
public interface GeneralizationSetChange extends UMLDiff {
	/**
	 * Returns the value of the '<em><b>Generalization Set</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Generalization Set</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generalization Set</em>' reference.
	 * @see #setGeneralizationSet(GeneralizationSet)
	 * @see org.eclipse.emf.compare.uml2.UMLComparePackage#getGeneralizationSetChange_GeneralizationSet()
	 * @model
	 * @generated
	 */
	GeneralizationSet getGeneralizationSet();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.uml2.GeneralizationSetChange#getGeneralizationSet <em>Generalization Set</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generalization Set</em>' reference.
	 * @see #getGeneralizationSet()
	 * @generated
	 */
	void setGeneralizationSet(GeneralizationSet value);

} // GeneralizationSetChange
