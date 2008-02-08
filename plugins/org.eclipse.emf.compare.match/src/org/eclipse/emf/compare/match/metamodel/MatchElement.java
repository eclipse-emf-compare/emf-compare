/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Element</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.MatchElement#getSimilarity <em>Similarity</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.MatchElement#getSubMatchElements <em>Sub Match Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getMatchElement()
 * @model abstract="true"
 * @generated
 */
public interface MatchElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Similarity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Similarity</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Similarity</em>' attribute.
	 * @see #setSimilarity(double)
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getMatchElement_Similarity()
	 * @model unique="false" required="true" ordered="false"
	 * @generated
	 */
	double getSimilarity();

	/**
	 * Returns the value of the '<em><b>Sub Match Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.match.metamodel.MatchElement}.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of the '<em>Sub Match Elements</em>' containment reference list isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sub Match Elements</em>' containment reference list.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getMatchElement_SubMatchElements()
	 * @model containment="true"
	 * @generated
	 */
	EList<MatchElement> getSubMatchElements();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.match.metamodel.MatchElement#getSimilarity <em>Similarity</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Similarity</em>' attribute.
	 * @see #getSimilarity()
	 * @generated
	 */
	void setSimilarity(double value);

} // MatchElement
