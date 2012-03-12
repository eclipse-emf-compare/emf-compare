/**
 * <copyright>
 * </copyright>
 *
 */
package org.eclipse.emf.compare.epatch;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.epatch.EpatchPackage
 * @generated
 */
public interface EpatchFactory extends EFactory
{
  /**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  EpatchFactory eINSTANCE = org.eclipse.emf.compare.epatch.impl.EpatchFactoryImpl.init();

  /**
	 * Returns a new object of class '<em>Epatch</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>Epatch</em>'.
	 * @generated
	 */
  Epatch createEpatch();

  /**
	 * Returns a new object of class '<em>Model Import</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model Import</em>'.
	 * @generated
	 */
  ModelImport createModelImport();

  /**
	 * Returns a new object of class '<em>Resource Import</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>Resource Import</em>'.
	 * @generated
	 */
  ResourceImport createResourceImport();

  /**
	 * Returns a new object of class '<em>EPackage Import</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>EPackage Import</em>'.
	 * @generated
	 */
  EPackageImport createEPackageImport();

  /**
	 * Returns a new object of class '<em>Named Resource</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>Named Resource</em>'.
	 * @generated
	 */
  NamedResource createNamedResource();

  /**
	 * Returns a new object of class '<em>Named Object</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>Named Object</em>'.
	 * @generated
	 */
  NamedObject createNamedObject();

  /**
	 * Returns a new object of class '<em>Object Ref</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>Object Ref</em>'.
	 * @generated
	 */
  ObjectRef createObjectRef();

  /**
	 * Returns a new object of class '<em>Created Object</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>Created Object</em>'.
	 * @generated
	 */
  CreatedObject createCreatedObject();

  /**
	 * Returns a new object of class '<em>Assignment</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>Assignment</em>'.
	 * @generated
	 */
  Assignment createAssignment();

  /**
	 * Returns a new object of class '<em>Single Assignment</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>Single Assignment</em>'.
	 * @generated
	 */
  SingleAssignment createSingleAssignment();

  /**
	 * Returns a new object of class '<em>List Assignment</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>List Assignment</em>'.
	 * @generated
	 */
  ListAssignment createListAssignment();

  /**
	 * Returns a new object of class '<em>Assignment Value</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>Assignment Value</em>'.
	 * @generated
	 */
  AssignmentValue createAssignmentValue();

  /**
	 * Returns a new object of class '<em>Object New</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>Object New</em>'.
	 * @generated
	 */
  ObjectNew createObjectNew();

  /**
	 * Returns a new object of class '<em>Object Copy</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>Object Copy</em>'.
	 * @generated
	 */
  ObjectCopy createObjectCopy();

  /**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
  EpatchPackage getEpatchPackage();

} //EpatchFactory
