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
package org.eclipse.emf.compare.diagram;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Label Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diagram.LabelChange#getLeft <em>Left</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diagram.LabelChange#getRight <em>Right</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diagram.LabelChange#getOrigin <em>Origin</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diagram.DiagramComparePackage#getLabelChange()
 * @model
 * @generated
 */
public interface LabelChange extends DiagramDiff {

	/**
	 * Returns the value of the '<em><b>Left</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Left</em>' attribute.
	 * @see #setLeft(String)
	 * @see org.eclipse.emf.compare.diagram.DiagramComparePackage#getLabelChange_Left()
	 * @model
	 * @generated
	 */
	String getLeft();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diagram.LabelChange#getLeft <em>Left</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Left</em>' attribute.
	 * @see #getLeft()
	 * @generated
	 */
	void setLeft(String value);

	/**
	 * Returns the value of the '<em><b>Right</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Right</em>' attribute.
	 * @see #setRight(String)
	 * @see org.eclipse.emf.compare.diagram.DiagramComparePackage#getLabelChange_Right()
	 * @model
	 * @generated
	 */
	String getRight();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diagram.LabelChange#getRight <em>Right</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Right</em>' attribute.
	 * @see #getRight()
	 * @generated
	 */
	void setRight(String value);

	/**
	 * Returns the value of the '<em><b>Origin</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Origin</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Origin</em>' attribute.
	 * @see #setOrigin(String)
	 * @see org.eclipse.emf.compare.diagram.DiagramComparePackage#getLabelChange_Origin()
	 * @model
	 * @generated
	 */
	String getOrigin();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diagram.LabelChange#getOrigin <em>Origin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Origin</em>' attribute.
	 * @see #getOrigin()
	 * @generated
	 */
	void setOrigin(String value);
} // LabelChange
