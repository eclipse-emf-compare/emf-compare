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
package org.eclipse.emf.compare.diff.metamodel;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Element</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.DiffElement#getSubDiffElements <em>Sub Diff Elements</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.DiffElement#getIsHiddenBy <em>Is Hidden By</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffElement()
 * @model abstract="true"
 * @generated
 */
public interface DiffElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Is Hidden By</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension#getHideElements <em>Hide Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Hidden By</em>' reference list isn't clear, there really should be
	 * more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Hidden By</em>' reference list.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffElement_IsHiddenBy()
	 * @see org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension#getHideElements
	 * @model opposite="hideElements"
	 * @generated
	 */
	EList<AbstractDiffExtension> getIsHiddenBy();

	/**
	 * Returns the value of the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.diff.metamodel.DiffElement}.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of the '<em>Sub Diff Elements</em>' containment reference list isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sub Diff Elements</em>' containment reference list.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffElement_SubDiffElements()
	 * @model containment="true"
	 * @generated
	 */
	EList<DiffElement> getSubDiffElements();

} // DiffElement
