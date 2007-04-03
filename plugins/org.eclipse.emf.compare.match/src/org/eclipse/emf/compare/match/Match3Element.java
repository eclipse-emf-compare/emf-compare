/*  
 * Copyright (c) 2006, Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.match;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Match3 Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.match.Match3Element#getOriginElement <em>Origin Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.match.MatchPackage#getMatch3Element()
 * @model
 * @generated
 */
public interface Match3Element extends MatchElement {
	/**
	 * Returns the value of the '<em><b>Origin Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Origin Element</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Origin Element</em>' reference.
	 * @see #setOriginElement(EObject)
	 * @see org.eclipse.emf.compare.match.MatchPackage#getMatch3Element_OriginElement()
	 * @model required="true" ordered="false"
	 * @generated
	 */
	EObject getOriginElement();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.match.Match3Element#getOriginElement <em>Origin Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Origin Element</em>' reference.
	 * @see #getOriginElement()
	 * @generated
	 */
	void setOriginElement(EObject value);

} // Match3Element