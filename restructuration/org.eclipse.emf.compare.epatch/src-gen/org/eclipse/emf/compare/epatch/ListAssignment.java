/**
 * <copyright>
 * </copyright>
 *
 */
package org.eclipse.emf.compare.epatch;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>List Assignment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.epatch.ListAssignment#getLeftValues <em>Left Values</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.epatch.ListAssignment#getRightValues <em>Right Values</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.epatch.EpatchPackage#getListAssignment()
 * @model
 * @generated
 */
public interface ListAssignment extends Assignment
{
  /**
	 * Returns the value of the '<em><b>Left Values</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.epatch.AssignmentValue}.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Left Values</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Left Values</em>' containment reference list.
	 * @see org.eclipse.emf.compare.epatch.EpatchPackage#getListAssignment_LeftValues()
	 * @model containment="true"
	 * @generated
	 */
  EList<AssignmentValue> getLeftValues();

  /**
	 * Returns the value of the '<em><b>Right Values</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.epatch.AssignmentValue}.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Right Values</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Right Values</em>' containment reference list.
	 * @see org.eclipse.emf.compare.epatch.EpatchPackage#getListAssignment_RightValues()
	 * @model containment="true"
	 * @generated
	 */
  EList<AssignmentValue> getRightValues();

} // ListAssignment
