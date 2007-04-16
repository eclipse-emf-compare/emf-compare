/**
 * <copyright>
 * </copyright>
 *
 * $Id: DiffGroup.java,v 1.1 2007/04/16 14:58:46 cbrun Exp $
 */
package org.eclipse.emf.compare.diff;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.DiffGroup#getLeftParent <em>Left Parent</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diff.DiffPackage#getDiffGroup()
 * @model
 * @generated
 */
public interface DiffGroup extends DiffElement {
	/**
	 * Returns the value of the '<em><b>Left Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left Parent</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Left Parent</em>' reference.
	 * @see #setLeftParent(EObject)
	 * @see org.eclipse.emf.compare.diff.DiffPackage#getDiffGroup_LeftParent()
	 * @model
	 * @generated
	 */
	EObject getLeftParent();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.DiffGroup#getLeftParent <em>Left Parent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Left Parent</em>' reference.
	 * @see #getLeftParent()
	 * @generated
	 */
	void setLeftParent(EObject value);

} // DiffGroup