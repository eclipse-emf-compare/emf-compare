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
package org.eclipse.emf.compare;

import java.lang.Iterable;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Match</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A MatchElement describes the mapping between the EObjects of two or three resources. This will reference all three of left, right and origin resources. However, note that instances of this class will also represent unmatched elements, in which case only one of either "left" or "right" will be assigned.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.Match#getSubmatches <em>Submatches</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.Match#getDifferences <em>Differences</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.Match#getLeft <em>Left</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.Match#getRight <em>Right</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.Match#getOrigin <em>Origin</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.ComparePackage#getMatch()
 * @model
 * @generated
 */
public interface Match extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Submatches</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.Match}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The MatchElements will "mimic" the hierarchy of the input model(s). This containment feature will serve that purpose.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Submatches</em>' containment reference list.
	 * @see org.eclipse.emf.compare.ComparePackage#getMatch_Submatches()
	 * @model containment="true"
	 * @generated
	 */
	EList<Match> getSubmatches();

	/**
	 * Returns the value of the '<em><b>Differences</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.Diff}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.compare.Diff#getMatch <em>Match</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This will contain the list of all differences pertaining to this mapping, if any.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Differences</em>' containment reference list.
	 * @see org.eclipse.emf.compare.ComparePackage#getMatch_Differences()
	 * @see org.eclipse.emf.compare.Diff#getMatch
	 * @model opposite="match" containment="true"
	 * @generated
	 */
	EList<Diff> getDifferences();

	/**
	 * Returns the value of the '<em><b>Left</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This can be null in the case of unmatched elements in the right model. In all other cases, it will reference the left element of this mapping.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Left</em>' reference.
	 * @see #setLeft(EObject)
	 * @see org.eclipse.emf.compare.ComparePackage#getMatch_Left()
	 * @model
	 * @generated
	 */
	EObject getLeft();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.Match#getLeft <em>Left</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Left</em>' reference.
	 * @see #getLeft()
	 * @generated
	 */
	void setLeft(EObject value);

	/**
	 * Returns the value of the '<em><b>Right</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This can be null in the case of unmatched elements in the left model. In all other cases, it will reference the right element of this mapping.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Right</em>' reference.
	 * @see #setRight(EObject)
	 * @see org.eclipse.emf.compare.ComparePackage#getMatch_Right()
	 * @model
	 * @generated
	 */
	EObject getRight();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.Match#getRight <em>Right</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Right</em>' reference.
	 * @see #getRight()
	 * @generated
	 */
	void setRight(EObject value);

	/**
	 * Returns the value of the '<em><b>Origin</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This can be null in the case of two-way comparisons, or unmatched elements that are only located in either left or right, i.e an element that was added since the origin, either in the left copy or the right one. In any other course of event, this will reference the origin element of this mapping.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Origin</em>' reference.
	 * @see #setOrigin(EObject)
	 * @see org.eclipse.emf.compare.ComparePackage#getMatch_Origin()
	 * @model
	 * @generated
	 */
	EObject getOrigin();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.Match#getOrigin <em>Origin</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Origin</em>' reference.
	 * @see #getOrigin()
	 * @generated
	 */
	void setOrigin(EObject value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Finds and return the Comparison containing this Match.
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	Comparison getComparison();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Finds and returns all matches contained in this Match and its children, recursively.
	 * <!-- end-model-doc -->
	 * @model kind="operation" dataType="org.eclipse.emf.compare.EIterable<org.eclipse.emf.compare.Match>"
	 * @generated
	 */
	Iterable<Match> getAllSubmatches();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Finds and returns all differences contained in this Match and its children, recursively.
	 * <!-- end-model-doc -->
	 * @model kind="operation" dataType="org.eclipse.emf.compare.EIterable<org.eclipse.emf.compare.Diff>"
	 * @generated
	 */
	Iterable<Diff> getAllDifferences();

} // Match
