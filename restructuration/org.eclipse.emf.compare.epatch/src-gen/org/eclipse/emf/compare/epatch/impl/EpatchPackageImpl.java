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
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EpatchPackageImpl extends EPackageImpl implements EpatchPackage
{
  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  private EClass epatchEClass = null;

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  private EClass modelImportEClass = null;

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  private EClass resourceImportEClass = null;

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  private EClass ePackageImportEClass = null;

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  private EClass namedResourceEClass = null;

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  private EClass namedObjectEClass = null;

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  private EClass objectRefEClass = null;

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  private EClass createdObjectEClass = null;

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  private EClass assignmentEClass = null;

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  private EClass singleAssignmentEClass = null;

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  private EClass listAssignmentEClass = null;

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  private EClass assignmentValueEClass = null;

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  private EClass objectNewEClass = null;

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  private EClass objectCopyEClass = null;

  /**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.emf.compare.epatch.EpatchPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
  private EpatchPackageImpl()
  {
		super(eNS_URI, EpatchFactory.eINSTANCE);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  private static boolean isInited = false;

  /**
	 * Creates, registers, and initializes the <b>Package</b> for this
	 * model, and for any others upon which it depends.  Simple
	 * dependencies are satisfied by calling this method on all
	 * dependent packages before doing anything else.  This method drives
	 * initialization for interdependent packages directly, in parallel
	 * with this package, itself.
	 * <p>Of this package and its interdependencies, all packages which
	 * have not yet been registered by their URI values are first created
	 * and registered.  The packages are then initialized in two steps:
	 * meta-model objects for all of the packages are created before any
	 * are initialized, since one package's meta-model objects may refer to
	 * those of another.
	 * <p>Invocation of this method will not affect any packages that have
	 * already been initialized.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
  public static EpatchPackage init()
  {
		if (isInited) return (EpatchPackage)EPackage.Registry.INSTANCE.getEPackage(EpatchPackage.eNS_URI);

		// Obtain or create and register package
		EpatchPackageImpl theEpatchPackage = (EpatchPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EpatchPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new EpatchPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theEpatchPackage.createPackageContents();

		// Initialize created meta-data
		theEpatchPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theEpatchPackage.freeze();

		return theEpatchPackage;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EClass getEpatch()
  {
		return epatchEClass;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EAttribute getEpatch_Name()
  {
		return (EAttribute)epatchEClass.getEStructuralFeatures().get(0);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EReference getEpatch_ModelImports()
  {
		return (EReference)epatchEClass.getEStructuralFeatures().get(1);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EReference getEpatch_Resources()
  {
		return (EReference)epatchEClass.getEStructuralFeatures().get(2);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EReference getEpatch_Objects()
  {
		return (EReference)epatchEClass.getEStructuralFeatures().get(3);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EClass getModelImport()
  {
		return modelImportEClass;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EAttribute getModelImport_Name()
  {
		return (EAttribute)modelImportEClass.getEStructuralFeatures().get(0);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EClass getResourceImport()
  {
		return resourceImportEClass;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EAttribute getResourceImport_Uri()
  {
		return (EAttribute)resourceImportEClass.getEStructuralFeatures().get(0);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EClass getEPackageImport()
  {
		return ePackageImportEClass;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EAttribute getEPackageImport_NsURI()
  {
		return (EAttribute)ePackageImportEClass.getEStructuralFeatures().get(0);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EClass getNamedResource()
  {
		return namedResourceEClass;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EAttribute getNamedResource_Name()
  {
		return (EAttribute)namedResourceEClass.getEStructuralFeatures().get(0);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EAttribute getNamedResource_LeftUri()
  {
		return (EAttribute)namedResourceEClass.getEStructuralFeatures().get(1);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EReference getNamedResource_LeftRoot()
  {
		return (EReference)namedResourceEClass.getEStructuralFeatures().get(2);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EAttribute getNamedResource_RightUri()
  {
		return (EAttribute)namedResourceEClass.getEStructuralFeatures().get(3);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EReference getNamedResource_RightRoot()
  {
		return (EReference)namedResourceEClass.getEStructuralFeatures().get(4);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EClass getNamedObject()
  {
		return namedObjectEClass;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EAttribute getNamedObject_Name()
  {
		return (EAttribute)namedObjectEClass.getEStructuralFeatures().get(0);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EReference getNamedObject_Assignments()
  {
		return (EReference)namedObjectEClass.getEStructuralFeatures().get(1);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EClass getObjectRef()
  {
		return objectRefEClass;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EReference getObjectRef_LeftRes()
  {
		return (EReference)objectRefEClass.getEStructuralFeatures().get(0);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EAttribute getObjectRef_LeftFrag()
  {
		return (EAttribute)objectRefEClass.getEStructuralFeatures().get(1);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EReference getObjectRef_RightRes()
  {
		return (EReference)objectRefEClass.getEStructuralFeatures().get(2);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EAttribute getObjectRef_RightFrag()
  {
		return (EAttribute)objectRefEClass.getEStructuralFeatures().get(3);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EClass getCreatedObject()
  {
		return createdObjectEClass;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EClass getAssignment()
  {
		return assignmentEClass;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EAttribute getAssignment_Feature()
  {
		return (EAttribute)assignmentEClass.getEStructuralFeatures().get(0);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EClass getSingleAssignment()
  {
		return singleAssignmentEClass;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EReference getSingleAssignment_LeftValue()
  {
		return (EReference)singleAssignmentEClass.getEStructuralFeatures().get(0);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EReference getSingleAssignment_RightValue()
  {
		return (EReference)singleAssignmentEClass.getEStructuralFeatures().get(1);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EClass getListAssignment()
  {
		return listAssignmentEClass;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EReference getListAssignment_LeftValues()
  {
		return (EReference)listAssignmentEClass.getEStructuralFeatures().get(0);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EReference getListAssignment_RightValues()
  {
		return (EReference)listAssignmentEClass.getEStructuralFeatures().get(1);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EClass getAssignmentValue()
  {
		return assignmentValueEClass;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EAttribute getAssignmentValue_Value()
  {
		return (EAttribute)assignmentValueEClass.getEStructuralFeatures().get(0);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EReference getAssignmentValue_RefObject()
  {
		return (EReference)assignmentValueEClass.getEStructuralFeatures().get(1);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EAttribute getAssignmentValue_RefFeature()
  {
		return (EAttribute)assignmentValueEClass.getEStructuralFeatures().get(2);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EAttribute getAssignmentValue_RefIndex()
  {
		return (EAttribute)assignmentValueEClass.getEStructuralFeatures().get(3);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EReference getAssignmentValue_NewObject()
  {
		return (EReference)assignmentValueEClass.getEStructuralFeatures().get(4);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EReference getAssignmentValue_Import()
  {
		return (EReference)assignmentValueEClass.getEStructuralFeatures().get(5);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EAttribute getAssignmentValue_ImpFrag()
  {
		return (EAttribute)assignmentValueEClass.getEStructuralFeatures().get(6);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EAttribute getAssignmentValue_Index()
  {
		return (EAttribute)assignmentValueEClass.getEStructuralFeatures().get(7);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EAttribute getAssignmentValue_Keyword()
  {
		return (EAttribute)assignmentValueEClass.getEStructuralFeatures().get(8);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EClass getObjectNew()
  {
		return objectNewEClass;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EReference getObjectNew_Import()
  {
		return (EReference)objectNewEClass.getEStructuralFeatures().get(0);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EAttribute getObjectNew_ImpFrag()
  {
		return (EAttribute)objectNewEClass.getEStructuralFeatures().get(1);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EClass getObjectCopy()
  {
		return objectCopyEClass;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EReference getObjectCopy_Resource()
  {
		return (EReference)objectCopyEClass.getEStructuralFeatures().get(0);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EAttribute getObjectCopy_Fragment()
  {
		return (EAttribute)objectCopyEClass.getEStructuralFeatures().get(1);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public EpatchFactory getEpatchFactory()
  {
		return (EpatchFactory)getEFactoryInstance();
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  private boolean isCreated = false;

  /**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public void createPackageContents()
  {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		epatchEClass = createEClass(EPATCH);
		createEAttribute(epatchEClass, EPATCH__NAME);
		createEReference(epatchEClass, EPATCH__MODEL_IMPORTS);
		createEReference(epatchEClass, EPATCH__RESOURCES);
		createEReference(epatchEClass, EPATCH__OBJECTS);

		modelImportEClass = createEClass(MODEL_IMPORT);
		createEAttribute(modelImportEClass, MODEL_IMPORT__NAME);

		resourceImportEClass = createEClass(RESOURCE_IMPORT);
		createEAttribute(resourceImportEClass, RESOURCE_IMPORT__URI);

		ePackageImportEClass = createEClass(EPACKAGE_IMPORT);
		createEAttribute(ePackageImportEClass, EPACKAGE_IMPORT__NS_URI);

		namedResourceEClass = createEClass(NAMED_RESOURCE);
		createEAttribute(namedResourceEClass, NAMED_RESOURCE__NAME);
		createEAttribute(namedResourceEClass, NAMED_RESOURCE__LEFT_URI);
		createEReference(namedResourceEClass, NAMED_RESOURCE__LEFT_ROOT);
		createEAttribute(namedResourceEClass, NAMED_RESOURCE__RIGHT_URI);
		createEReference(namedResourceEClass, NAMED_RESOURCE__RIGHT_ROOT);

		namedObjectEClass = createEClass(NAMED_OBJECT);
		createEAttribute(namedObjectEClass, NAMED_OBJECT__NAME);
		createEReference(namedObjectEClass, NAMED_OBJECT__ASSIGNMENTS);

		objectRefEClass = createEClass(OBJECT_REF);
		createEReference(objectRefEClass, OBJECT_REF__LEFT_RES);
		createEAttribute(objectRefEClass, OBJECT_REF__LEFT_FRAG);
		createEReference(objectRefEClass, OBJECT_REF__RIGHT_RES);
		createEAttribute(objectRefEClass, OBJECT_REF__RIGHT_FRAG);

		createdObjectEClass = createEClass(CREATED_OBJECT);

		assignmentEClass = createEClass(ASSIGNMENT);
		createEAttribute(assignmentEClass, ASSIGNMENT__FEATURE);

		singleAssignmentEClass = createEClass(SINGLE_ASSIGNMENT);
		createEReference(singleAssignmentEClass, SINGLE_ASSIGNMENT__LEFT_VALUE);
		createEReference(singleAssignmentEClass, SINGLE_ASSIGNMENT__RIGHT_VALUE);

		listAssignmentEClass = createEClass(LIST_ASSIGNMENT);
		createEReference(listAssignmentEClass, LIST_ASSIGNMENT__LEFT_VALUES);
		createEReference(listAssignmentEClass, LIST_ASSIGNMENT__RIGHT_VALUES);

		assignmentValueEClass = createEClass(ASSIGNMENT_VALUE);
		createEAttribute(assignmentValueEClass, ASSIGNMENT_VALUE__VALUE);
		createEReference(assignmentValueEClass, ASSIGNMENT_VALUE__REF_OBJECT);
		createEAttribute(assignmentValueEClass, ASSIGNMENT_VALUE__REF_FEATURE);
		createEAttribute(assignmentValueEClass, ASSIGNMENT_VALUE__REF_INDEX);
		createEReference(assignmentValueEClass, ASSIGNMENT_VALUE__NEW_OBJECT);
		createEReference(assignmentValueEClass, ASSIGNMENT_VALUE__IMPORT);
		createEAttribute(assignmentValueEClass, ASSIGNMENT_VALUE__IMP_FRAG);
		createEAttribute(assignmentValueEClass, ASSIGNMENT_VALUE__INDEX);
		createEAttribute(assignmentValueEClass, ASSIGNMENT_VALUE__KEYWORD);

		objectNewEClass = createEClass(OBJECT_NEW);
		createEReference(objectNewEClass, OBJECT_NEW__IMPORT);
		createEAttribute(objectNewEClass, OBJECT_NEW__IMP_FRAG);

		objectCopyEClass = createEClass(OBJECT_COPY);
		createEReference(objectCopyEClass, OBJECT_COPY__RESOURCE);
		createEAttribute(objectCopyEClass, OBJECT_COPY__FRAGMENT);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  private boolean isInitialized = false;

  /**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public void initializePackageContents()
  {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		resourceImportEClass.getESuperTypes().add(this.getModelImport());
		ePackageImportEClass.getESuperTypes().add(this.getModelImport());
		objectRefEClass.getESuperTypes().add(this.getNamedObject());
		createdObjectEClass.getESuperTypes().add(this.getNamedObject());
		singleAssignmentEClass.getESuperTypes().add(this.getAssignment());
		listAssignmentEClass.getESuperTypes().add(this.getAssignment());
		objectNewEClass.getESuperTypes().add(this.getCreatedObject());
		objectCopyEClass.getESuperTypes().add(this.getCreatedObject());

		// Initialize classes and features; add operations and parameters
		initEClass(epatchEClass, Epatch.class, "Epatch", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEpatch_Name(), ecorePackage.getEString(), "name", null, 0, 1, Epatch.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEpatch_ModelImports(), this.getModelImport(), null, "modelImports", null, 0, -1, Epatch.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEpatch_Resources(), this.getNamedResource(), null, "resources", null, 0, -1, Epatch.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEpatch_Objects(), this.getObjectRef(), null, "objects", null, 0, -1, Epatch.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(modelImportEClass, ModelImport.class, "ModelImport", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getModelImport_Name(), ecorePackage.getEString(), "name", null, 0, 1, ModelImport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(resourceImportEClass, ResourceImport.class, "ResourceImport", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getResourceImport_Uri(), ecorePackage.getEString(), "uri", null, 0, 1, ResourceImport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(ePackageImportEClass, EPackageImport.class, "EPackageImport", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEPackageImport_NsURI(), ecorePackage.getEString(), "nsURI", null, 0, 1, EPackageImport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(namedResourceEClass, NamedResource.class, "NamedResource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNamedResource_Name(), ecorePackage.getEString(), "name", null, 0, 1, NamedResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedResource_LeftUri(), ecorePackage.getEString(), "leftUri", null, 0, 1, NamedResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getNamedResource_LeftRoot(), this.getCreatedObject(), null, "leftRoot", null, 0, 1, NamedResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedResource_RightUri(), ecorePackage.getEString(), "rightUri", null, 0, 1, NamedResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getNamedResource_RightRoot(), this.getCreatedObject(), null, "rightRoot", null, 0, 1, NamedResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(namedObjectEClass, NamedObject.class, "NamedObject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNamedObject_Name(), ecorePackage.getEString(), "name", null, 0, 1, NamedObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getNamedObject_Assignments(), this.getAssignment(), null, "assignments", null, 0, -1, NamedObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(objectRefEClass, ObjectRef.class, "ObjectRef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getObjectRef_LeftRes(), this.getNamedResource(), null, "leftRes", null, 0, 1, ObjectRef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getObjectRef_LeftFrag(), ecorePackage.getEString(), "leftFrag", null, 0, 1, ObjectRef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getObjectRef_RightRes(), this.getNamedResource(), null, "rightRes", null, 0, 1, ObjectRef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getObjectRef_RightFrag(), ecorePackage.getEString(), "rightFrag", null, 0, 1, ObjectRef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(createdObjectEClass, CreatedObject.class, "CreatedObject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(assignmentEClass, Assignment.class, "Assignment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAssignment_Feature(), ecorePackage.getEString(), "feature", null, 0, 1, Assignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(singleAssignmentEClass, SingleAssignment.class, "SingleAssignment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSingleAssignment_LeftValue(), this.getAssignmentValue(), null, "leftValue", null, 0, 1, SingleAssignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSingleAssignment_RightValue(), this.getAssignmentValue(), null, "rightValue", null, 0, 1, SingleAssignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(listAssignmentEClass, ListAssignment.class, "ListAssignment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getListAssignment_LeftValues(), this.getAssignmentValue(), null, "leftValues", null, 0, -1, ListAssignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getListAssignment_RightValues(), this.getAssignmentValue(), null, "rightValues", null, 0, -1, ListAssignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(assignmentValueEClass, AssignmentValue.class, "AssignmentValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAssignmentValue_Value(), ecorePackage.getEString(), "value", null, 0, 1, AssignmentValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAssignmentValue_RefObject(), this.getNamedObject(), null, "refObject", null, 0, 1, AssignmentValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssignmentValue_RefFeature(), ecorePackage.getEString(), "refFeature", null, 0, 1, AssignmentValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssignmentValue_RefIndex(), ecorePackage.getEInt(), "refIndex", null, 0, 1, AssignmentValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAssignmentValue_NewObject(), this.getCreatedObject(), null, "newObject", null, 0, 1, AssignmentValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAssignmentValue_Import(), this.getModelImport(), null, "import", null, 0, 1, AssignmentValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssignmentValue_ImpFrag(), ecorePackage.getEString(), "impFrag", null, 0, 1, AssignmentValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssignmentValue_Index(), ecorePackage.getEInt(), "index", null, 0, 1, AssignmentValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssignmentValue_Keyword(), ecorePackage.getEString(), "keyword", null, 0, 1, AssignmentValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(objectNewEClass, ObjectNew.class, "ObjectNew", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getObjectNew_Import(), this.getModelImport(), null, "import", null, 0, 1, ObjectNew.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getObjectNew_ImpFrag(), ecorePackage.getEString(), "impFrag", null, 0, 1, ObjectNew.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(objectCopyEClass, ObjectCopy.class, "ObjectCopy", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getObjectCopy_Resource(), this.getNamedResource(), null, "resource", null, 0, 1, ObjectCopy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getObjectCopy_Fragment(), ecorePackage.getEString(), "fragment", null, 0, 1, ObjectCopy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //EpatchPackageImpl
