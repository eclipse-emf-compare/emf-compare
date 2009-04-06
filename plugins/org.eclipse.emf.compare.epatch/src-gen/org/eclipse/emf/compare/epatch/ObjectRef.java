/**
 * <copyright>
 * </copyright>
 *
 */
package org.eclipse.emf.compare.epatch;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Object Ref</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.epatch.ObjectRef#getLeftRes <em>Left Res</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.epatch.ObjectRef#getLeftFrag <em>Left Frag</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.epatch.ObjectRef#getRightRes <em>Right Res</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.epatch.ObjectRef#getRightFrag <em>Right Frag</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.epatch.EpatchPackage#getObjectRef()
 * @model
 * @generated
 */
public interface ObjectRef extends NamedObject
{
  /**
	 * Returns the value of the '<em><b>Left Res</b></em>' reference.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Left Res</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Left Res</em>' reference.
	 * @see #setLeftRes(NamedResource)
	 * @see org.eclipse.emf.compare.epatch.EpatchPackage#getObjectRef_LeftRes()
	 * @model
	 * @generated
	 */
  NamedResource getLeftRes();

  /**
	 * Sets the value of the '{@link org.eclipse.emf.compare.epatch.ObjectRef#getLeftRes <em>Left Res</em>}' reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Left Res</em>' reference.
	 * @see #getLeftRes()
	 * @generated
	 */
  void setLeftRes(NamedResource value);

  /**
	 * Returns the value of the '<em><b>Left Frag</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Left Frag</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Left Frag</em>' attribute.
	 * @see #setLeftFrag(String)
	 * @see org.eclipse.emf.compare.epatch.EpatchPackage#getObjectRef_LeftFrag()
	 * @model
	 * @generated
	 */
  String getLeftFrag();

  /**
	 * Sets the value of the '{@link org.eclipse.emf.compare.epatch.ObjectRef#getLeftFrag <em>Left Frag</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Left Frag</em>' attribute.
	 * @see #getLeftFrag()
	 * @generated
	 */
  void setLeftFrag(String value);

  /**
	 * Returns the value of the '<em><b>Right Res</b></em>' reference.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Right Res</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Right Res</em>' reference.
	 * @see #setRightRes(NamedResource)
	 * @see org.eclipse.emf.compare.epatch.EpatchPackage#getObjectRef_RightRes()
	 * @model
	 * @generated
	 */
  NamedResource getRightRes();

  /**
	 * Sets the value of the '{@link org.eclipse.emf.compare.epatch.ObjectRef#getRightRes <em>Right Res</em>}' reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Right Res</em>' reference.
	 * @see #getRightRes()
	 * @generated
	 */
  void setRightRes(NamedResource value);

  /**
	 * Returns the value of the '<em><b>Right Frag</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Right Frag</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Right Frag</em>' attribute.
	 * @see #setRightFrag(String)
	 * @see org.eclipse.emf.compare.epatch.EpatchPackage#getObjectRef_RightFrag()
	 * @model
	 * @generated
	 */
  String getRightFrag();

  /**
	 * Sets the value of the '{@link org.eclipse.emf.compare.epatch.ObjectRef#getRightFrag <em>Right Frag</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Right Frag</em>' attribute.
	 * @see #getRightFrag()
	 * @generated
	 */
  void setRightFrag(String value);

} // ObjectRef
