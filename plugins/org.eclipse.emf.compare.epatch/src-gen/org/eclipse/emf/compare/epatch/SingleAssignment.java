/**
 * <copyright>
 * </copyright>
 *
 */
package org.eclipse.emf.compare.epatch;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Single Assignment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.epatch.SingleAssignment#getLeftValue <em>Left Value</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.epatch.SingleAssignment#getRightValue <em>Right Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.epatch.EpatchPackage#getSingleAssignment()
 * @model
 * @generated
 */
public interface SingleAssignment extends Assignment
{
  /**
   * Returns the value of the '<em><b>Left Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Left Value</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Left Value</em>' containment reference.
   * @see #setLeftValue(AssignmentValue)
   * @see org.eclipse.emf.compare.epatch.EpatchPackage#getSingleAssignment_LeftValue()
   * @model containment="true"
   * @generated
   */
  AssignmentValue getLeftValue();

  /**
   * Sets the value of the '{@link org.eclipse.emf.compare.epatch.SingleAssignment#getLeftValue <em>Left Value</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Left Value</em>' containment reference.
   * @see #getLeftValue()
   * @generated
   */
  void setLeftValue(AssignmentValue value);

  /**
   * Returns the value of the '<em><b>Right Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Right Value</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Right Value</em>' containment reference.
   * @see #setRightValue(AssignmentValue)
   * @see org.eclipse.emf.compare.epatch.EpatchPackage#getSingleAssignment_RightValue()
   * @model containment="true"
   * @generated
   */
  AssignmentValue getRightValue();

  /**
   * Sets the value of the '{@link org.eclipse.emf.compare.epatch.SingleAssignment#getRightValue <em>Right Value</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Right Value</em>' containment reference.
   * @see #getRightValue()
   * @generated
   */
  void setRightValue(AssignmentValue value);

} // SingleAssignment
