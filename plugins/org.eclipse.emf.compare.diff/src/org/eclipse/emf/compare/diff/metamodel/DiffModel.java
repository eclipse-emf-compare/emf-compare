/**
 * <copyright>
 * </copyright>
 *
 * $Id: DiffModel.java,v 1.1 2007/06/22 12:59:46 cbrun Exp $
 */
package org.eclipse.emf.compare.diff.metamodel;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getRight <em>Right</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getOwnedElements <em>Owned Elements</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getLeft <em>Left</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffModel()
 * @model
 * @generated
 */
public interface DiffModel extends EObject {
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
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffModel_Right()
	 * @model
	 * @generated
	 */
	String getRight();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getRight <em>Right</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Right</em>' attribute.
	 * @see #getRight()
	 * @generated
	 */
	void setRight(String value);

	/**
	 * Returns the value of the '<em><b>Owned Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.diff.metamodel.DiffElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owned Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Elements</em>' containment reference list.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffModel_OwnedElements()
	 * @model type="org.eclipse.emf.compare.diff.metamodel.DiffElement" containment="true"
	 * @generated
	 */
	EList getOwnedElements();

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
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getDiffModel_Left()
	 * @model
	 * @generated
	 */
	String getLeft();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getLeft <em>Left</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Left</em>' attribute.
	 * @see #getLeft()
	 * @generated
	 */
	void setLeft(String value);

} // DiffModel