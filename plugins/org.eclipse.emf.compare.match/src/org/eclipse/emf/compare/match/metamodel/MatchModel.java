/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
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
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getLeftModel <em>Left Model</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getRightModel <em>Right Model</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getOriginModel <em>Origin Model</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getMatchedElements <em>Matched Elements</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getUnMatchedElements <em>Un Matched Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getMatchModel()
 * @model
 * @generated
 */
public interface MatchModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Left Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left Model</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Left Model</em>' attribute.
	 * @see #setLeftModel(String)
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getMatchModel_LeftModel()
	 * @model unique="false" required="true" ordered="false"
	 * @generated
	 */
	String getLeftModel();

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
	 * Returns the value of the '<em><b>Origin Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Origin Model</em>' attribute isn't clear, there really should be more of
	 * a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Origin Model</em>' attribute.
	 * @see #setOriginModel(String)
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getMatchModel_OriginModel()
	 * @model unique="false" required="true" ordered="false"
	 * @generated
	 */
	String getOriginModel();

	/**
	 * Returns the value of the '<em><b>Right Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right Model</em>' attribute isn't clear, there really should be more of
	 * a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Right Model</em>' attribute.
	 * @see #setRightModel(String)
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getMatchModel_RightModel()
	 * @model unique="false" required="true" ordered="false"
	 * @generated
	 */
	String getRightModel();

	/**
	 * Returns the value of the '<em><b>Un Matched Elements</b></em>' containment reference list. The
	 * list contents are of type {@link org.eclipse.emf.compare.match.metamodel.UnMatchElement}. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Un Matched Elements</em>' containment reference list isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Un Matched Elements</em>' containment reference list.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getMatchModel_UnMatchedElements()
	 * @model type="org.eclipse.emf.compare.match.metamodel.UnMatchElement" containment="true"
	 * @generated
	 */
	EList<UnMatchElement> getUnMatchedElements();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getLeftModel <em>Left Model</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Left Model</em>' attribute.
	 * @see #getLeftModel()
	 * @generated
	 */
	void setLeftModel(String value);

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getOriginModel <em>Origin Model</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Origin Model</em>' attribute.
	 * @see #getOriginModel()
	 * @generated
	 */
	void setOriginModel(String value);

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getRightModel <em>Right Model</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Right Model</em>' attribute.
	 * @see #getRightModel()
	 * @generated
	 */
	void setRightModel(String value);

} // MatchModel
