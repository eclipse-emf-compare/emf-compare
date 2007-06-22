/**
 * <copyright>
 * </copyright>
 *
 * $Id: RemoveReferenceValue.java,v 1.1 2007/06/22 12:59:46 cbrun Exp $
 */
package org.eclipse.emf.compare.diff.metamodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Remove Reference Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue#getLeftRemovedTarget <em>Left Removed Target</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue#getRightRemovedTarget <em>Right Removed Target</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getRemoveReferenceValue()
 * @model
 * @generated
 */
public interface RemoveReferenceValue extends ReferenceChange {
	/**
	 * Returns the value of the '<em><b>Left Removed Target</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left Removed Target</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Left Removed Target</em>' reference list.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getRemoveReferenceValue_LeftRemovedTarget()
	 * @model type="org.eclipse.emf.ecore.EObject"
	 * @generated
	 */
	EList getLeftRemovedTarget();

	/**
	 * Returns the value of the '<em><b>Right Removed Target</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right Removed Target</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Right Removed Target</em>' reference list.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getRemoveReferenceValue_RightRemovedTarget()
	 * @model type="org.eclipse.emf.ecore.EObject"
	 * @generated
	 */
	EList getRightRemovedTarget();

} // RemoveReferenceValue