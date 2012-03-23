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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Comparison</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This will act as the "root" of a comparison. It will reference one match for every root of the input models, along with the differences detected for each of them.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.Comparison#getMatchedResources <em>Matched Resources</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.Comparison#getMatches <em>Matches</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.Comparison#getConflicts <em>Conflicts</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.ComparePackage#getComparison()
 * @model
 * @generated
 */
public interface Comparison extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Matched Resources</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.MatchResource}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This contains the mappings for each compared Resource.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Matched Resources</em>' containment reference list.
	 * @see org.eclipse.emf.compare.ComparePackage#getComparison_MatchedResources()
	 * @model containment="true"
	 * @generated
	 */
	EList<MatchResource> getMatchedResources();

	/**
	 * Returns the value of the '<em><b>Matches</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.Match}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This contains the match tree "mimicking" the input models' hierarchy.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Matches</em>' containment reference list.
	 * @see org.eclipse.emf.compare.ComparePackage#getComparison_Matches()
	 * @model containment="true"
	 * @generated
	 */
	EList<Match> getMatches();

	/**
	 * Returns the value of the '<em><b>Conflicts</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.Conflict}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * If we detected any conflict during the comparison process, this will contain them.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Conflicts</em>' containment reference list.
	 * @see org.eclipse.emf.compare.ComparePackage#getComparison_Conflicts()
	 * @model containment="true"
	 * @generated
	 */
	EList<Conflict> getConflicts();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Returns all differences contained by this Comparison and its children.
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<Diff> getDifferences();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Returns all differences that were detected on the given EObject or one of its counterpart in left, right or origin.
	 * @param element The EObject for which we seek all related differences.
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	EList<Diff> getDifferences(EObject element);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Finds and return the Match for the given EObject.
	 * @param element The EObject for which we seek the match.
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	Match getMatch(EObject element);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Adds the given diff under the match corresponding to the given EObject.
	 * @param element EObject on which we need to add a new difference.
	 * @param newDiff The difference we are to add to the comparison model.
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	void addDiff(EObject element, Diff newDiff);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Checks whether this comparison has been launched for three way comparison or if it only has two sides to compare.
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean isThreeWay();

} // Comparison
