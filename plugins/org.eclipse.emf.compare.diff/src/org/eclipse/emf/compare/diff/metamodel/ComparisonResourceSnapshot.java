/*******************************************************************************
 * Copyright (c) 2009, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.metamodel;

import org.eclipse.emf.compare.match.metamodel.MatchModel;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Comparison Snapshot</b></em>'. <!--
 * end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This can be used to hold the result of single-elements comparison (Resource with Resource, EObject with EObject, ...).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot#getDiff <em>Diff</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot#getMatch <em>Match</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getComparisonResourceSnapshot()
 * @model
 * @generated
 */
public interface ComparisonResourceSnapshot extends ComparisonSnapshot {
	/**
	 * Returns the value of the '<em><b>Diff</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diff</em>' containment reference isn't clear, there really should be more of
	 * a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diff</em>' containment reference.
	 * @see #setDiff(DiffModel)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getComparisonResourceSnapshot_Diff()
	 * @model containment="true"
	 * @generated
	 */
	DiffModel getDiff();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot#getDiff <em>Diff</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diff</em>' containment reference.
	 * @see #getDiff()
	 * @generated
	 */
	void setDiff(DiffModel value);

	/**
	 * Returns the value of the '<em><b>Match</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Match</em>' containment reference isn't clear, there really should be more
	 * of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Match</em>' containment reference.
	 * @see #setMatch(MatchModel)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getComparisonResourceSnapshot_Match()
	 * @model containment="true"
	 * @generated
	 */
	MatchModel getMatch();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot#getMatch <em>Match</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Match</em>' containment reference.
	 * @see #getMatch()
	 * @generated
	 */
	void setMatch(MatchModel value);

} // ComparisonResourceSnapshot
