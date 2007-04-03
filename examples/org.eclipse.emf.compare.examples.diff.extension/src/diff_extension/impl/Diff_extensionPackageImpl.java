/**
 * <copyright>
 * </copyright>
 *
 * $Id: Diff_extensionPackageImpl.java,v 1.1 2007/04/03 06:37:08 cbrun Exp $
 */
package diff_extension.impl;

import diff_extension.Diff_extensionFactory;
import diff_extension.Diff_extensionPackage;
import diff_extension.MoveModelElement;
import diff_extension.RenameModelElement;

import org.eclipse.emf.compare.diff.DiffPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Diff_extensionPackageImpl extends EPackageImpl implements Diff_extensionPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass renameModelElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass moveModelElementEClass = null;

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
	 * @see diff_extension.Diff_extensionPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private Diff_extensionPackageImpl() {
		super(eNS_URI, Diff_extensionFactory.eINSTANCE);
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
	public static Diff_extensionPackage init() {
		if (isInited) return (Diff_extensionPackage)EPackage.Registry.INSTANCE.getEPackage(Diff_extensionPackage.eNS_URI);

		// Obtain or create and register package
		Diff_extensionPackageImpl theDiff_extensionPackage = (Diff_extensionPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof Diff_extensionPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new Diff_extensionPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		DiffPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theDiff_extensionPackage.createPackageContents();

		// Initialize created meta-data
		theDiff_extensionPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDiff_extensionPackage.freeze();

		return theDiff_extensionPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRenameModelElement() {
		return renameModelElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMoveModelElement() {
		return moveModelElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Diff_extensionFactory getDiff_extensionFactory() {
		return (Diff_extensionFactory)getEFactoryInstance();
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
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		renameModelElementEClass = createEClass(RENAME_MODEL_ELEMENT);

		moveModelElementEClass = createEClass(MOVE_MODEL_ELEMENT);
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
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		DiffPackage theDiffPackage = (DiffPackage)EPackage.Registry.INSTANCE.getEPackage(DiffPackage.eNS_URI);

		// Add supertypes to classes
		renameModelElementEClass.getESuperTypes().add(theDiffPackage.getUpdateAttribute());
		moveModelElementEClass.getESuperTypes().add(theDiffPackage.getUpdateModelElement());

		// Initialize classes and features; add operations and parameters
		initEClass(renameModelElementEClass, RenameModelElement.class, "RenameModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(moveModelElementEClass, MoveModelElement.class, "MoveModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //Diff_extensionPackageImpl
