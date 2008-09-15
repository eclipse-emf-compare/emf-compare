/**
 * <copyright>
 * </copyright>
 *
 * $Id: UpdateContainmentFeature.java,v 1.1 2008/09/15 13:20:46 lgoubet Exp $
 */
package org.eclipse.emf.compare.diff.metamodel;

import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Update Containment Feature</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.UpdateContainmentFeature#getLeftContainmentFeature <em>Left Containment Feature</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.UpdateContainmentFeature#getRightContainmentFeature <em>Right Containment Feature</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getUpdateContainmentFeature()
 * @model
 * @generated
 */
public interface UpdateContainmentFeature extends MoveModelElement {
	/**
	 * Returns the value of the '<em><b>Left Containment Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left Containment Feature</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Left Containment Feature</em>' reference.
	 * @see #setLeftContainmentFeature(EStructuralFeature)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getUpdateContainmentFeature_LeftContainmentFeature()
	 * @model
	 * @generated
	 */
	EStructuralFeature getLeftContainmentFeature();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.UpdateContainmentFeature#getLeftContainmentFeature <em>Left Containment Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Left Containment Feature</em>' reference.
	 * @see #getLeftContainmentFeature()
	 * @generated
	 */
	void setLeftContainmentFeature(EStructuralFeature value);

	/**
	 * Returns the value of the '<em><b>Right Containment Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right Containment Feature</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Right Containment Feature</em>' reference.
	 * @see #setRightContainmentFeature(EStructuralFeature)
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#getUpdateContainmentFeature_RightContainmentFeature()
	 * @model
	 * @generated
	 */
	EStructuralFeature getRightContainmentFeature();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.diff.metamodel.UpdateContainmentFeature#getRightContainmentFeature <em>Right Containment Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Right Containment Feature</em>' reference.
	 * @see #getRightContainmentFeature()
	 * @generated
	 */
	void setRightContainmentFeature(EStructuralFeature value);

} // UpdateContainmentFeature
