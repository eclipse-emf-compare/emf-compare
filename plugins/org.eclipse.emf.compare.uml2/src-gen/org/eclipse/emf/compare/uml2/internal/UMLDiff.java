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

import org.eclipse.emf.compare.Diff;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>UML Diff</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.uml2.internal.UMLDiff#getDiscriminant <em>Discriminant</em>}</li>
 * <li>{@link org.eclipse.emf.compare.uml2.internal.UMLDiff#getEReference <em>EReference</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.compare.uml2.internal.UMLComparePackage#getUMLDiff()
 * @model abstract="true"
 * @generated
 */
public interface UMLDiff extends Diff {
	/**
	 * Returns the value of the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discriminant</em>' reference isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discriminant</em>' reference.
	 * @see #setDiscriminant(EObject)
	 * @see org.eclipse.emf.compare.uml2.internal.UMLComparePackage#getUMLDiff_Discriminant()
	 * @model
	 * @generated
	 */
	EObject getDiscriminant();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.uml2.internal.UMLDiff#getDiscriminant <em>Discriminant</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discriminant</em>' reference.
	 * @see #getDiscriminant()
	 * @generated
	 */
	void setDiscriminant(EObject value);

	/**
	 * Returns the value of the '<em><b>EReference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>EReference</em>' reference isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>EReference</em>' reference.
	 * @see #setEReference(EReference)
	 * @see org.eclipse.emf.compare.uml2.internal.UMLComparePackage#getUMLDiff_EReference()
	 * @model
	 * @generated
	 */
	EReference getEReference();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.uml2.internal.UMLDiff#getEReference <em>EReference</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>EReference</em>' reference.
	 * @see #getEReference()
	 * @generated
	 */
	void setEReference(EReference value);

} // UMLDiff
