/**
 * <copyright>
 * </copyright>
 *
 */
package org.eclipse.emf.compare.epatch;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Assignment Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.epatch.AssignmentValue#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.epatch.AssignmentValue#getRefObject <em>Ref Object</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.epatch.AssignmentValue#getRefFeature <em>Ref Feature</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.epatch.AssignmentValue#getRefIndex <em>Ref Index</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.epatch.AssignmentValue#getNewObject <em>New Object</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.epatch.AssignmentValue#getImport <em>Import</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.epatch.AssignmentValue#getImpFrag <em>Imp Frag</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.epatch.AssignmentValue#getIndex <em>Index</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.epatch.AssignmentValue#getKeyword <em>Keyword</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.epatch.EpatchPackage#getAssignmentValue()
 * @model
 * @generated
 */
public interface AssignmentValue extends EObject
{
  /**
   * Returns the value of the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see #setValue(String)
   * @see org.eclipse.emf.compare.epatch.EpatchPackage#getAssignmentValue_Value()
   * @model
   * @generated
   */
  String getValue();

  /**
   * Sets the value of the '{@link org.eclipse.emf.compare.epatch.AssignmentValue#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
  void setValue(String value);

  /**
   * Returns the value of the '<em><b>Ref Object</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Ref Object</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ref Object</em>' reference.
   * @see #setRefObject(NamedObject)
   * @see org.eclipse.emf.compare.epatch.EpatchPackage#getAssignmentValue_RefObject()
   * @model
   * @generated
   */
  NamedObject getRefObject();

  /**
   * Sets the value of the '{@link org.eclipse.emf.compare.epatch.AssignmentValue#getRefObject <em>Ref Object</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Ref Object</em>' reference.
   * @see #getRefObject()
   * @generated
   */
  void setRefObject(NamedObject value);

  /**
   * Returns the value of the '<em><b>Ref Feature</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Ref Feature</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ref Feature</em>' attribute.
   * @see #setRefFeature(String)
   * @see org.eclipse.emf.compare.epatch.EpatchPackage#getAssignmentValue_RefFeature()
   * @model
   * @generated
   */
  String getRefFeature();

  /**
   * Sets the value of the '{@link org.eclipse.emf.compare.epatch.AssignmentValue#getRefFeature <em>Ref Feature</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Ref Feature</em>' attribute.
   * @see #getRefFeature()
   * @generated
   */
  void setRefFeature(String value);

  /**
   * Returns the value of the '<em><b>Ref Index</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Ref Index</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ref Index</em>' attribute.
   * @see #setRefIndex(int)
   * @see org.eclipse.emf.compare.epatch.EpatchPackage#getAssignmentValue_RefIndex()
   * @model
   * @generated
   */
  int getRefIndex();

  /**
   * Sets the value of the '{@link org.eclipse.emf.compare.epatch.AssignmentValue#getRefIndex <em>Ref Index</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Ref Index</em>' attribute.
   * @see #getRefIndex()
   * @generated
   */
  void setRefIndex(int value);

  /**
   * Returns the value of the '<em><b>New Object</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>New Object</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>New Object</em>' containment reference.
   * @see #setNewObject(CreatedObject)
   * @see org.eclipse.emf.compare.epatch.EpatchPackage#getAssignmentValue_NewObject()
   * @model containment="true"
   * @generated
   */
  CreatedObject getNewObject();

  /**
   * Sets the value of the '{@link org.eclipse.emf.compare.epatch.AssignmentValue#getNewObject <em>New Object</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>New Object</em>' containment reference.
   * @see #getNewObject()
   * @generated
   */
  void setNewObject(CreatedObject value);

  /**
   * Returns the value of the '<em><b>Import</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Import</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Import</em>' reference.
   * @see #setImport(Import)
   * @see org.eclipse.emf.compare.epatch.EpatchPackage#getAssignmentValue_Import()
   * @model
   * @generated
   */
  Import getImport();

  /**
   * Sets the value of the '{@link org.eclipse.emf.compare.epatch.AssignmentValue#getImport <em>Import</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Import</em>' reference.
   * @see #getImport()
   * @generated
   */
  void setImport(Import value);

  /**
   * Returns the value of the '<em><b>Imp Frag</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Imp Frag</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Imp Frag</em>' attribute.
   * @see #setImpFrag(String)
   * @see org.eclipse.emf.compare.epatch.EpatchPackage#getAssignmentValue_ImpFrag()
   * @model
   * @generated
   */
  String getImpFrag();

  /**
   * Sets the value of the '{@link org.eclipse.emf.compare.epatch.AssignmentValue#getImpFrag <em>Imp Frag</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Imp Frag</em>' attribute.
   * @see #getImpFrag()
   * @generated
   */
  void setImpFrag(String value);

  /**
   * Returns the value of the '<em><b>Index</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Index</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Index</em>' attribute.
   * @see #setIndex(int)
   * @see org.eclipse.emf.compare.epatch.EpatchPackage#getAssignmentValue_Index()
   * @model
   * @generated
   */
  int getIndex();

  /**
   * Sets the value of the '{@link org.eclipse.emf.compare.epatch.AssignmentValue#getIndex <em>Index</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Index</em>' attribute.
   * @see #getIndex()
   * @generated
   */
  void setIndex(int value);

  /**
   * Returns the value of the '<em><b>Keyword</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Keyword</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Keyword</em>' attribute.
   * @see #setKeyword(String)
   * @see org.eclipse.emf.compare.epatch.EpatchPackage#getAssignmentValue_Keyword()
   * @model
   * @generated
   */
  String getKeyword();

  /**
   * Sets the value of the '{@link org.eclipse.emf.compare.epatch.AssignmentValue#getKeyword <em>Keyword</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Keyword</em>' attribute.
   * @see #getKeyword()
   * @generated
   */
  void setKeyword(String value);

} // AssignmentValue
