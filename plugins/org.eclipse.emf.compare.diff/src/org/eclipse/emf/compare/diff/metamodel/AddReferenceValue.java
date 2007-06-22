/**
 * <copyright>
 * </copyright>
 *
 * $Id: AddReferenceValue.java,v 1.1 2007/06/22 12:59:46 cbrun Exp $
 */
package org.eclipse.emf.compare.diff.metamodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Add Reference Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.AddReferenceValue#getLeftAddedTarget <em>Left Added Target</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.AddReferenceValue#getRightAddedTarget <em>Right Added Target</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getAddReferenceValue()
 * @model
 * @generated
 */
public interface AddReferenceValue extends ReferenceChange {
	/**
	 * Returns the value of the '<em><b>Left Added Target</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left Added Target</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Left Added Target</em>' reference list.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getAddReferenceValue_LeftAddedTarget()
	 * @model type="org.eclipse.emf.ecore.EObject"
	 * @generated
	 */
	EList getLeftAddedTarget();

	/**
	 * Returns the value of the '<em><b>Right Added Target</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right Added Target</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Right Added Target</em>' reference list.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getAddReferenceValue_RightAddedTarget()
	 * @model type="org.eclipse.emf.ecore.EObject"
	 * @generated
	 */
	EList getRightAddedTarget();

} // AddReferenceValue