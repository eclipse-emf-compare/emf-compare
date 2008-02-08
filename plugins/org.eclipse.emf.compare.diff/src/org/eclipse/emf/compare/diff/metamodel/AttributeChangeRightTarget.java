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
package org.eclipse.emf.compare.diff.metamodel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Attribute Change Right Target</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget#getRightTarget <em>Right Target</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getAttributeChangeRightTarget()
 * @model
 * @generated
 */
public interface AttributeChangeRightTarget extends AttributeChange {
	/**
	 * Returns the value of the '<em><b>Right Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right Target</em>' reference isn't clear, there really should be more of
	 * a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Right Target</em>' reference.
	 * @see #setRightTarget(EObject)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getAttributeChangeRightTarget_RightTarget()
	 * @model
	 * @generated
	 */
	EObject getRightTarget();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget#getRightTarget <em>Right Target</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Right Target</em>' reference.
	 * @see #getRightTarget()
	 * @generated
	 */
	void setRightTarget(EObject value);

} // AttributeChangeRightTarget
