/**
 * <copyright>
 * </copyright>
 *
 */
package org.eclipse.emf.compare.epatch.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.compare.epatch.*;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.epatch.EpatchPackage
 * @generated
 */
public class EpatchAdapterFactory extends AdapterFactoryImpl
{
  /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  protected static EpatchPackage modelPackage;

  /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EpatchAdapterFactory()
  {
		if (modelPackage == null) {
			modelPackage = EpatchPackage.eINSTANCE;
		}
	}

  /**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
  @Override
  public boolean isFactoryForType(Object object)
  {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

  /**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  protected EpatchSwitch<Adapter> modelSwitch =
    new EpatchSwitch<Adapter>() {
			@Override
			public Adapter caseEpatch(Epatch object) {
				return createEpatchAdapter();
			}
			@Override
			public Adapter caseModelImport(ModelImport object) {
				return createModelImportAdapter();
			}
			@Override
			public Adapter caseResourceImport(ResourceImport object) {
				return createResourceImportAdapter();
			}
			@Override
			public Adapter caseEPackageImport(EPackageImport object) {
				return createEPackageImportAdapter();
			}
			@Override
			public Adapter caseNamedResource(NamedResource object) {
				return createNamedResourceAdapter();
			}
			@Override
			public Adapter caseNamedObject(NamedObject object) {
				return createNamedObjectAdapter();
			}
			@Override
			public Adapter caseObjectRef(ObjectRef object) {
				return createObjectRefAdapter();
			}
			@Override
			public Adapter caseCreatedObject(CreatedObject object) {
				return createCreatedObjectAdapter();
			}
			@Override
			public Adapter caseAssignment(Assignment object) {
				return createAssignmentAdapter();
			}
			@Override
			public Adapter caseSingleAssignment(SingleAssignment object) {
				return createSingleAssignmentAdapter();
			}
			@Override
			public Adapter caseListAssignment(ListAssignment object) {
				return createListAssignmentAdapter();
			}
			@Override
			public Adapter caseAssignmentValue(AssignmentValue object) {
				return createAssignmentValueAdapter();
			}
			@Override
			public Adapter caseObjectNew(ObjectNew object) {
				return createObjectNewAdapter();
			}
			@Override
			public Adapter caseObjectCopy(ObjectCopy object) {
				return createObjectCopyAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

  /**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
  @Override
  public Adapter createAdapter(Notifier target)
  {
		return modelSwitch.doSwitch((EObject)target);
	}


  /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.epatch.Epatch <em>Epatch</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.epatch.Epatch
	 * @generated
	 */
  public Adapter createEpatchAdapter()
  {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.epatch.ModelImport <em>Model Import</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.epatch.ModelImport
	 * @generated
	 */
  public Adapter createModelImportAdapter()
  {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.epatch.ResourceImport <em>Resource Import</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.epatch.ResourceImport
	 * @generated
	 */
  public Adapter createResourceImportAdapter()
  {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.epatch.EPackageImport <em>EPackage Import</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.epatch.EPackageImport
	 * @generated
	 */
  public Adapter createEPackageImportAdapter()
  {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.epatch.NamedResource <em>Named Resource</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.epatch.NamedResource
	 * @generated
	 */
  public Adapter createNamedResourceAdapter()
  {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.epatch.NamedObject <em>Named Object</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.epatch.NamedObject
	 * @generated
	 */
  public Adapter createNamedObjectAdapter()
  {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.epatch.ObjectRef <em>Object Ref</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.epatch.ObjectRef
	 * @generated
	 */
  public Adapter createObjectRefAdapter()
  {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.epatch.CreatedObject <em>Created Object</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.epatch.CreatedObject
	 * @generated
	 */
  public Adapter createCreatedObjectAdapter()
  {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.epatch.Assignment <em>Assignment</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.epatch.Assignment
	 * @generated
	 */
  public Adapter createAssignmentAdapter()
  {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.epatch.SingleAssignment <em>Single Assignment</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.epatch.SingleAssignment
	 * @generated
	 */
  public Adapter createSingleAssignmentAdapter()
  {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.epatch.ListAssignment <em>List Assignment</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.epatch.ListAssignment
	 * @generated
	 */
  public Adapter createListAssignmentAdapter()
  {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.epatch.AssignmentValue <em>Assignment Value</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.epatch.AssignmentValue
	 * @generated
	 */
  public Adapter createAssignmentValueAdapter()
  {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.epatch.ObjectNew <em>Object New</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.epatch.ObjectNew
	 * @generated
	 */
  public Adapter createObjectNewAdapter()
  {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.epatch.ObjectCopy <em>Object Copy</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.epatch.ObjectCopy
	 * @generated
	 */
  public Adapter createObjectCopyAdapter()
  {
		return null;
	}

  /**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
  public Adapter createEObjectAdapter()
  {
		return null;
	}

} //EpatchAdapterFactory
