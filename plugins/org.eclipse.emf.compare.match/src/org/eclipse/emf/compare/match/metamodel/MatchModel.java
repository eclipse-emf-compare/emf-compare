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
package org.eclipse.emf.compare.match.metamodel;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Model</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getMatchedElements <em>Matched Elements</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getUnmatchedElements <em>Unmatched Elements</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getLeftRoots <em>Left Roots</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getRightRoots <em>Right Roots</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getAncestorRoots <em>Ancestor Roots</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getMatchModel()
 * @model
 * @generated
 */
public interface MatchModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Matched Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.match.metamodel.MatchElement}.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of the '<em>Matched Elements</em>' containment reference list isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Matched Elements</em>' containment reference list.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getMatchModel_MatchedElements()
	 * @model containment="true"
	 * @generated
	 */
	EList<MatchElement> getMatchedElements();

	/**
	 * Returns the value of the '<em><b>Unmatched Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.match.metamodel.UnmatchElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unmatched Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unmatched Elements</em>' containment reference list.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getMatchModel_UnmatchedElements()
	 * @model containment="true"
	 * @generated
	 */
	EList<UnmatchElement> getUnmatchedElements();

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
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getMatchModel_LeftRoots()
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
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getMatchModel_RightRoots()
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
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getMatchModel_AncestorRoots()
	 * @model
	 * @generated
	 */
	EList<EObject> getAncestorRoots();

} // MatchModel
