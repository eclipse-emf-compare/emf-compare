/*******************************************************************************
 * Copyright (c) 2008, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.metamodel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Unmatch Element</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.UnmatchElement#getElement <em>Element</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.UnmatchElement#isConflicting <em>Conflicting</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.UnmatchElement#isRemote <em>Remote</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.UnmatchElement#getSide <em>Side</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getUnmatchElement()
 * @model
 * @generated
 */
public interface UnmatchElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Element</em>' reference isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Element</em>' reference.
	 * @see #setElement(EObject)
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getUnmatchElement_Element()
	 * @model required="true" ordered="false"
	 * @generated
	 */
	EObject getElement();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.match.metamodel.UnmatchElement#getElement <em>Element</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Element</em>' reference.
	 * @see #getElement()
	 * @generated
	 */
	void setElement(EObject value);

	/**
	 * Returns the value of the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Conflicting</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Conflicting</em>' attribute.
	 * @see #setConflicting(boolean)
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getUnmatchElement_Conflicting()
	 * @model
	 * @generated
	 */
	boolean isConflicting();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.match.metamodel.UnmatchElement#isConflicting <em>Conflicting</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Conflicting</em>' attribute.
	 * @see #isConflicting()
	 * @generated
	 */
	void setConflicting(boolean value);

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
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getUnmatchElement_Remote()
	 * @model
	 * @generated
	 */
	boolean isRemote();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.match.metamodel.UnmatchElement#isRemote <em>Remote</em>}' attribute.
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
	 * If the meaning of the '<em>Side</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Side</em>' attribute.
	 * @see org.eclipse.emf.compare.match.metamodel.Side
	 * @see #setSide(Side)
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#getUnmatchElement_Side()
	 * @model
	 * @generated
	 */
	Side getSide();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.match.metamodel.UnmatchElement#getSide <em>Side</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Side</em>' attribute.
	 * @see org.eclipse.emf.compare.match.metamodel.Side
	 * @see #getSide()
	 * @generated
	 */
	void setSide(Side value);

} // UnmatchElement
