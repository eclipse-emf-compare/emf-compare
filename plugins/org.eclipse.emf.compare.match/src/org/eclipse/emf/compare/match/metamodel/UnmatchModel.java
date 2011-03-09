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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Unmatch Model</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.UnmatchModel#getRoots <em>Roots</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.UnmatchModel#isRemote <em>Remote</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.UnmatchModel#getSide <em>Side</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getUnmatchModel()
 * @model
 * @generated
 */
public interface UnmatchModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Roots</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Roots</em>' reference list isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Roots</em>' reference list.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getUnmatchModel_Roots()
	 * @model
	 * @generated
	 */
	EList<EObject> getRoots();

	/**
	 * Returns the value of the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Remote</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Remote</em>' attribute.
	 * @see #setRemote(boolean)
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getUnmatchModel_Remote()
	 * @model
	 * @generated
	 */
	boolean isRemote();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.match.metamodel.UnmatchModel#isRemote <em>Remote</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Remote</em>' attribute.
	 * @see #isRemote()
	 * @generated
	 */
	void setRemote(boolean value);

	/**
	 * Returns the value of the '<em><b>Side</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.emf.compare.match.metamodel.Side}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Side</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Side</em>' attribute.
	 * @see org.eclipse.emf.compare.match.metamodel.Side
	 * @see #setSide(Side)
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getUnmatchModel_Side()
	 * @model
	 * @generated
	 */
	Side getSide();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.match.metamodel.UnmatchModel#getSide <em>Side</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Side</em>' attribute.
	 * @see org.eclipse.emf.compare.match.metamodel.Side
	 * @see #getSide()
	 * @generated
	 */
	void setSide(Side value);

} // UnmatchModel
