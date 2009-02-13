/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.metamodel;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Model</b></em>'. <!--
 * end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Root of the differences model for single resources differencing.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getOwnedElements <em>Owned Elements</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getLeftRoots <em>Left Roots</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getRightRoots <em>Right Roots</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getAncestorRoots <em>Ancestor Roots</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffModel()
 * @model
 * @generated
 */
public interface DiffModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Owned Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.diff.metamodel.DiffElement}.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of the '<em>Owned Elements</em>' containment reference list isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Elements</em>' containment reference list.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffModel_OwnedElements()
	 * @model containment="true"
	 * @generated
	 */
	EList<DiffElement> getOwnedElements();

	/**
	 * Returns the value of the '<em><b>Left Roots</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left Roots</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Left Roots</em>' reference list.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffModel_LeftRoots()
	 * @model
	 * @generated
	 */
	EList<EObject> getLeftRoots();

	/**
	 * Returns the value of the '<em><b>Right Roots</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right Roots</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Right Roots</em>' reference list.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffModel_RightRoots()
	 * @model
	 * @generated
	 */
	EList<EObject> getRightRoots();

	/**
	 * Returns the value of the '<em><b>Ancestor Roots</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ancestor Roots</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ancestor Roots</em>' reference list.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffModel_AncestorRoots()
	 * @model
	 * @generated
	 */
	EList<EObject> getAncestorRoots();

} // DiffModel
