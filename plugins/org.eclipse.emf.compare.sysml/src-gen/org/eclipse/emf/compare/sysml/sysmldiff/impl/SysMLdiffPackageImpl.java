/**
 *  Copyright (c) 2011 Atos Origin.
 *  
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *  Atos Origin - Initial API and implementation
 * 
 */
package org.eclipse.emf.compare.sysml.sysmldiff.impl;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLDiffExtension;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeAttributeChange;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypePropertyChangeLeftTarget;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypePropertyChangeRightTarget;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceChangeLeftTarget;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceChangeRightTarget;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceOrderChange;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeUpdateAttribute;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeUpdateReference;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLdiffFactory;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLdiffPackage;
import org.eclipse.emf.compare.uml2diff.UML2DiffPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.papyrus.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class SysMLdiffPackageImpl extends EPackageImpl implements SysMLdiffPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass sysMLDiffExtensionEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass sysMLStereotypeAttributeChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass sysMLStereotypePropertyChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass sysMLStereotypePropertyChangeRightTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass sysMLStereotypeReferenceChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass sysMLStereotypeReferenceChangeRightTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass sysMLStereotypeReferenceOrderChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass sysMLStereotypeUpdateAttributeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass sysMLStereotypeUpdateReferenceEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory method {@link #init init()},
	 * which also performs initialization of the package, or returns the registered package, if one already
	 * exists. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLdiffPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private SysMLdiffPackageImpl() {
		super(eNS_URI, SysMLdiffFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it
	 * depends.
	 * <p>
	 * This method is used to initialize {@link SysMLdiffPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the
	 * package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static SysMLdiffPackage init() {
		if (isInited)
			return (SysMLdiffPackage)EPackage.Registry.INSTANCE.getEPackage(SysMLdiffPackage.eNS_URI);

		// Obtain or create and register package
		SysMLdiffPackageImpl theSysMLdiffPackage = (SysMLdiffPackageImpl)(EPackage.Registry.INSTANCE
				.get(eNS_URI) instanceof SysMLdiffPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
				: new SysMLdiffPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		SysmlPackage.eINSTANCE.eClass();
		UML2DiffPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theSysMLdiffPackage.createPackageContents();

		// Initialize created meta-data
		theSysMLdiffPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theSysMLdiffPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(SysMLdiffPackage.eNS_URI, theSysMLdiffPackage);
		return theSysMLdiffPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getSysMLDiffExtension() {
		return sysMLDiffExtensionEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getSysMLStereotypeAttributeChange() {
		return sysMLStereotypeAttributeChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getSysMLStereotypePropertyChangeLeftTarget() {
		return sysMLStereotypePropertyChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getSysMLStereotypePropertyChangeRightTarget() {
		return sysMLStereotypePropertyChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getSysMLStereotypeReferenceChangeLeftTarget() {
		return sysMLStereotypeReferenceChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getSysMLStereotypeReferenceChangeRightTarget() {
		return sysMLStereotypeReferenceChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getSysMLStereotypeReferenceOrderChange() {
		return sysMLStereotypeReferenceOrderChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getSysMLStereotypeUpdateAttribute() {
		return sysMLStereotypeUpdateAttributeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getSysMLStereotypeUpdateReference() {
		return sysMLStereotypeUpdateReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SysMLdiffFactory getSysMLdiffFactory() {
		return (SysMLdiffFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is guarded to have no affect on any
	 * invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		sysMLDiffExtensionEClass = createEClass(SYS_ML_DIFF_EXTENSION);

		sysMLStereotypeAttributeChangeEClass = createEClass(SYS_ML_STEREOTYPE_ATTRIBUTE_CHANGE);

		sysMLStereotypePropertyChangeLeftTargetEClass = createEClass(SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET);

		sysMLStereotypePropertyChangeRightTargetEClass = createEClass(SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET);

		sysMLStereotypeReferenceChangeLeftTargetEClass = createEClass(SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET);

		sysMLStereotypeReferenceChangeRightTargetEClass = createEClass(SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET);

		sysMLStereotypeReferenceOrderChangeEClass = createEClass(SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE);

		sysMLStereotypeUpdateAttributeEClass = createEClass(SYS_ML_STEREOTYPE_UPDATE_ATTRIBUTE);

		sysMLStereotypeUpdateReferenceEClass = createEClass(SYS_ML_STEREOTYPE_UPDATE_REFERENCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This method is guarded to have no affect
	 * on any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		DiffPackage theDiffPackage = (DiffPackage)EPackage.Registry.INSTANCE.getEPackage(DiffPackage.eNS_URI);
		UML2DiffPackage theUML2DiffPackage = (UML2DiffPackage)EPackage.Registry.INSTANCE
				.getEPackage(UML2DiffPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		sysMLDiffExtensionEClass.getESuperTypes().add(theDiffPackage.getDiffElement());
		sysMLDiffExtensionEClass.getESuperTypes().add(theDiffPackage.getAbstractDiffExtension());
		sysMLStereotypeAttributeChangeEClass.getESuperTypes().add(
				theUML2DiffPackage.getUMLStereotypePropertyChange());
		sysMLStereotypePropertyChangeLeftTargetEClass.getESuperTypes().add(
				theDiffPackage.getAttributeChangeLeftTarget());
		sysMLStereotypePropertyChangeLeftTargetEClass.getESuperTypes().add(
				this.getSysMLStereotypeAttributeChange());
		sysMLStereotypePropertyChangeRightTargetEClass.getESuperTypes().add(
				theDiffPackage.getAttributeChangeRightTarget());
		sysMLStereotypePropertyChangeRightTargetEClass.getESuperTypes().add(
				this.getSysMLStereotypeAttributeChange());
		sysMLStereotypeReferenceChangeLeftTargetEClass.getESuperTypes().add(
				theDiffPackage.getReferenceChangeLeftTarget());
		sysMLStereotypeReferenceChangeLeftTargetEClass.getESuperTypes().add(
				this.getSysMLStereotypeAttributeChange());
		sysMLStereotypeReferenceChangeRightTargetEClass.getESuperTypes().add(
				theDiffPackage.getReferenceChangeRightTarget());
		sysMLStereotypeReferenceChangeRightTargetEClass.getESuperTypes().add(
				this.getSysMLStereotypeAttributeChange());
		sysMLStereotypeReferenceOrderChangeEClass.getESuperTypes().add(
				theDiffPackage.getReferenceOrderChange());
		sysMLStereotypeReferenceOrderChangeEClass.getESuperTypes().add(
				this.getSysMLStereotypeAttributeChange());
		sysMLStereotypeUpdateAttributeEClass.getESuperTypes().add(theDiffPackage.getUpdateAttribute());
		sysMLStereotypeUpdateAttributeEClass.getESuperTypes().add(this.getSysMLStereotypeAttributeChange());
		sysMLStereotypeUpdateReferenceEClass.getESuperTypes().add(theDiffPackage.getUpdateReference());
		sysMLStereotypeUpdateReferenceEClass.getESuperTypes().add(this.getSysMLStereotypeAttributeChange());

		// Initialize classes and features; add operations and parameters
		initEClass(sysMLDiffExtensionEClass, SysMLDiffExtension.class, "SysMLDiffExtension", IS_ABSTRACT, //$NON-NLS-1$
				IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(sysMLStereotypeAttributeChangeEClass, SysMLStereotypeAttributeChange.class,
				"SysMLStereotypeAttributeChange", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(sysMLStereotypePropertyChangeLeftTargetEClass,
				SysMLStereotypePropertyChangeLeftTarget.class, "SysMLStereotypePropertyChangeLeftTarget", //$NON-NLS-1$
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(sysMLStereotypePropertyChangeRightTargetEClass,
				SysMLStereotypePropertyChangeRightTarget.class, "SysMLStereotypePropertyChangeRightTarget", //$NON-NLS-1$
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(sysMLStereotypeReferenceChangeLeftTargetEClass,
				SysMLStereotypeReferenceChangeLeftTarget.class, "SysMLStereotypeReferenceChangeLeftTarget", //$NON-NLS-1$
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(sysMLStereotypeReferenceChangeRightTargetEClass,
				SysMLStereotypeReferenceChangeRightTarget.class, "SysMLStereotypeReferenceChangeRightTarget", //$NON-NLS-1$
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(sysMLStereotypeReferenceOrderChangeEClass, SysMLStereotypeReferenceOrderChange.class,
				"SysMLStereotypeReferenceOrderChange", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);

		initEClass(sysMLStereotypeUpdateAttributeEClass, SysMLStereotypeUpdateAttribute.class,
				"SysMLStereotypeUpdateAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(sysMLStereotypeUpdateReferenceEClass, SysMLStereotypeUpdateReference.class,
				"SysMLStereotypeUpdateReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.topcased.org/EType
		createETypeAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.topcased.org/EType</b>. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected void createETypeAnnotations() {
		String source = "http://www.topcased.org/EType"; //$NON-NLS-1$
		addAnnotation(sysMLStereotypePropertyChangeLeftTargetEClass, source, new String[] {}, new URI[] {URI
				.createURI(SysmlPackage.eNS_URI).appendFragment("//blocks/Block")}); //$NON-NLS-1$
		addAnnotation(sysMLStereotypePropertyChangeRightTargetEClass, source, new String[] {}, new URI[] {URI
				.createURI(SysmlPackage.eNS_URI).appendFragment("//blocks/Block")}); //$NON-NLS-1$
	}

} // SysMLdiffPackageImpl
