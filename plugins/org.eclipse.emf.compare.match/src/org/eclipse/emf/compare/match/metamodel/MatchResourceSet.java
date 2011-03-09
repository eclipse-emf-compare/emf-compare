/*******************************************************************************
 * Copyright (c) 2009, 2011 Obeo.
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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Resource Set</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.MatchResourceSet#getMatchModels <em>Match Models</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.MatchResourceSet#getUnmatchedModels <em>Unmatched Models</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getMatchResourceSet()
 * @model
 * @generated
 */
public interface MatchResourceSet extends EObject {
	/**
	 * Returns the value of the '<em><b>Match Models</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.match.metamodel.MatchModel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Match Models</em>' containment reference list isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Match Models</em>' containment reference list.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getMatchResourceSet_MatchModels()
	 * @model containment="true"
	 * @generated
	 */
	EList<MatchModel> getMatchModels();

	/**
	 * Returns the value of the '<em><b>Unmatched Models</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.match.metamodel.UnmatchModel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unmatched Models</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unmatched Models</em>' containment reference list.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getMatchResourceSet_UnmatchedModels()
	 * @model containment="true"
	 * @generated
	 */
	EList<UnmatchModel> getUnmatchedModels();

} // MatchResourceSet
