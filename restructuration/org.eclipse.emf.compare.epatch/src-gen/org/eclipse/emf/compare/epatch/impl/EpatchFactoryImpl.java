/**
 * <copyright>
 * </copyright>
 *
 */
package org.eclipse.emf.compare.epatch.impl;

import org.eclipse.emf.compare.epatch.Assignment;
import org.eclipse.emf.compare.epatch.AssignmentValue;
import org.eclipse.emf.compare.epatch.CreatedObject;
import org.eclipse.emf.compare.epatch.EPackageImport;
import org.eclipse.emf.compare.epatch.Epatch;
import org.eclipse.emf.compare.epatch.EpatchFactory;
import org.eclipse.emf.compare.epatch.EpatchPackage;
import org.eclipse.emf.compare.epatch.ListAssignment;
import org.eclipse.emf.compare.epatch.ModelImport;
import org.eclipse.emf.compare.epatch.NamedObject;
import org.eclipse.emf.compare.epatch.NamedResource;
import org.eclipse.emf.compare.epatch.ObjectCopy;
import org.eclipse.emf.compare.epatch.ObjectNew;
import org.eclipse.emf.compare.epatch.ObjectRef;
import org.eclipse.emf.compare.epatch.ResourceImport;
import org.eclipse.emf.compare.epatch.SingleAssignment;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EpatchFactoryImpl extends EFactoryImpl implements EpatchFactory
{
  /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public static EpatchFactory init()
  {
		try {
			EpatchFactory theEpatchFactory = (EpatchFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/emf/compare/epatch/0.1"); 
			if (theEpatchFactory != null) {
				return theEpatchFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new EpatchFactoryImpl();
	}

  /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EpatchFactoryImpl()
  {
		super();
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  @Override
  public EObject create(EClass eClass)
  {
		switch (eClass.getClassifierID()) {
			case EpatchPackage.EPATCH: return createEpatch();
			case EpatchPackage.MODEL_IMPORT: return createModelImport();
			case EpatchPackage.RESOURCE_IMPORT: return createResourceImport();
			case EpatchPackage.EPACKAGE_IMPORT: return createEPackageImport();
			case EpatchPackage.NAMED_RESOURCE: return createNamedResource();
			case EpatchPackage.NAMED_OBJECT: return createNamedObject();
			case EpatchPackage.OBJECT_REF: return createObjectRef();
			case EpatchPackage.CREATED_OBJECT: return createCreatedObject();
			case EpatchPackage.ASSIGNMENT: return createAssignment();
			case EpatchPackage.SINGLE_ASSIGNMENT: return createSingleAssignment();
			case EpatchPackage.LIST_ASSIGNMENT: return createListAssignment();
			case EpatchPackage.ASSIGNMENT_VALUE: return createAssignmentValue();
			case EpatchPackage.OBJECT_NEW: return createObjectNew();
			case EpatchPackage.OBJECT_COPY: return createObjectCopy();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public Epatch createEpatch()
  {
		EpatchImpl epatch = new EpatchImpl();
		return epatch;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public ModelImport createModelImport()
  {
		ModelImportImpl modelImport = new ModelImportImpl();
		return modelImport;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public ResourceImport createResourceImport()
  {
		ResourceImportImpl resourceImport = new ResourceImportImpl();
		return resourceImport;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EPackageImport createEPackageImport()
  {
		EPackageImportImpl ePackageImport = new EPackageImportImpl();
		return ePackageImport;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public NamedResource createNamedResource()
  {
		NamedResourceImpl namedResource = new NamedResourceImpl();
		return namedResource;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public NamedObject createNamedObject()
  {
		NamedObjectImpl namedObject = new NamedObjectImpl();
		return namedObject;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public ObjectRef createObjectRef()
  {
		ObjectRefImpl objectRef = new ObjectRefImpl();
		return objectRef;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public CreatedObject createCreatedObject()
  {
		CreatedObjectImpl createdObject = new CreatedObjectImpl();
		return createdObject;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public Assignment createAssignment()
  {
		AssignmentImpl assignment = new AssignmentImpl();
		return assignment;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public SingleAssignment createSingleAssignment()
  {
		SingleAssignmentImpl singleAssignment = new SingleAssignmentImpl();
		return singleAssignment;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public ListAssignment createListAssignment()
  {
		ListAssignmentImpl listAssignment = new ListAssignmentImpl();
		return listAssignment;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public AssignmentValue createAssignmentValue()
  {
		AssignmentValueImpl assignmentValue = new AssignmentValueImpl();
		return assignmentValue;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public ObjectNew createObjectNew()
  {
		ObjectNewImpl objectNew = new ObjectNewImpl();
		return objectNew;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public ObjectCopy createObjectCopy()
  {
		ObjectCopyImpl objectCopy = new ObjectCopyImpl();
		return objectCopy;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EpatchPackage getEpatchPackage()
  {
		return (EpatchPackage)getEPackage();
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
  @Deprecated
  public static EpatchPackage getPackage()
  {
		return EpatchPackage.eINSTANCE;
	}

} //EpatchFactoryImpl
