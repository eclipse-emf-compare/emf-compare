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

import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Comparison Resource Set Snapshot</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This can be used to hold the result of ResourceSet-wide comparisons.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot#getDiffResourceSet <em>Diff Resource Set</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot#getMatchResourceSet <em>Match Resource Set</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getComparisonResourceSetSnapshot()
 * @model
 * @generated
 */
public interface ComparisonResourceSetSnapshot extends ComparisonSnapshot {
	/**
	 * Returns the value of the '<em><b>Diff Resource Set</b></em>' containment reference.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of the '<em>Diff Resource Set</em>' containment reference isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diff Resource Set</em>' containment reference.
	 * @see #setDiffResourceSet(DiffResourceSet)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getComparisonResourceSetSnapshot_DiffResourceSet()
	 * @model containment="true"
	 * @generated
	 */
	DiffResourceSet getDiffResourceSet();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot#getDiffResourceSet <em>Diff Resource Set</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diff Resource Set</em>' containment reference.
	 * @see #getDiffResourceSet()
	 * @generated
	 */
	void setDiffResourceSet(DiffResourceSet value);

	/**
	 * Returns the value of the '<em><b>Match Resource Set</b></em>' containment reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Match Resource Set</em>' containment reference isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Match Resource Set</em>' containment reference.
	 * @see #setMatchResourceSet(MatchResourceSet)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getComparisonResourceSetSnapshot_MatchResourceSet()
	 * @model containment="true"
	 * @generated
	 */
	MatchResourceSet getMatchResourceSet();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot#getMatchResourceSet <em>Match Resource Set</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Match Resource Set</em>' containment reference.
	 * @see #getMatchResourceSet()
	 * @generated
	 */
	void setMatchResourceSet(MatchResourceSet value);

} // ComparisonResourceSetSnapshot
