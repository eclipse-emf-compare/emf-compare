/*******************************************************************************
 * Copyright (c) 2011, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl;

import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche2;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche3;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfileFactory;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.uml2.types.TypesPackage;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class UML2CompareTestProfilePackageImpl extends EPackageImpl implements UML2CompareTestProfilePackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass aClicheEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass aCliche2EClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass aCliche3EClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory method {@link #init init()},
	 * which also performs initialization of the package, or returns the registered package, if one already
	 * exists. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private UML2CompareTestProfilePackageImpl() {
		super(eNS_URI, UML2CompareTestProfileFactory.eINSTANCE);
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
	 * This method is used to initialize {@link UML2CompareTestProfilePackage#eINSTANCE} when that field is
	 * accessed. Clients should not invoke it directly. Instead, they should simply access that field to
	 * obtain the package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static UML2CompareTestProfilePackage init() {
		if (isInited)
			return (UML2CompareTestProfilePackage)EPackage.Registry.INSTANCE
					.getEPackage(UML2CompareTestProfilePackage.eNS_URI);

		// Obtain or create and register package
		UML2CompareTestProfilePackageImpl theUML2CompareTestProfilePackage = (UML2CompareTestProfilePackageImpl)(EPackage.Registry.INSTANCE
				.get(eNS_URI) instanceof UML2CompareTestProfilePackageImpl
						? EPackage.Registry.INSTANCE.get(eNS_URI) : new UML2CompareTestProfilePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		UMLPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theUML2CompareTestProfilePackage.createPackageContents();

		// Initialize created meta-data
		theUML2CompareTestProfilePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theUML2CompareTestProfilePackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(UML2CompareTestProfilePackage.eNS_URI,
				theUML2CompareTestProfilePackage);
		return theUML2CompareTestProfilePackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getACliche() {
		return aClicheEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getACliche_SingleValuedAttribute() {
		return (EAttribute)aClicheEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getACliche_ManyValuedAttribute() {
		return (EAttribute)aClicheEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getACliche_SingleValuedReference() {
		return (EReference)aClicheEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getACliche_ManyValuedReference() {
		return (EReference)aClicheEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getACliche_Base_Class() {
		return (EReference)aClicheEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getACliche2() {
		return aCliche2EClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getACliche2_SingleValuedAttribute() {
		return (EAttribute)aCliche2EClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getACliche2_ManyValuedAttribute() {
		return (EAttribute)aCliche2EClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getACliche2_SingleValuedReference() {
		return (EReference)aCliche2EClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getACliche2_ManyValuedReference() {
		return (EReference)aCliche2EClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getACliche2_Base_Class() {
		return (EReference)aCliche2EClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getACliche3() {
		return aCliche3EClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getACliche3_Base_Class() {
		return (EReference)aCliche3EClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public UML2CompareTestProfileFactory getUML2CompareTestProfileFactory() {
		return (UML2CompareTestProfileFactory)getEFactoryInstance();
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
		aClicheEClass = createEClass(ACLICHE);
		createEAttribute(aClicheEClass, ACLICHE__SINGLE_VALUED_ATTRIBUTE);
		createEAttribute(aClicheEClass, ACLICHE__MANY_VALUED_ATTRIBUTE);
		createEReference(aClicheEClass, ACLICHE__SINGLE_VALUED_REFERENCE);
		createEReference(aClicheEClass, ACLICHE__MANY_VALUED_REFERENCE);
		createEReference(aClicheEClass, ACLICHE__BASE_CLASS);

		aCliche2EClass = createEClass(ACLICHE2);
		createEAttribute(aCliche2EClass, ACLICHE2__SINGLE_VALUED_ATTRIBUTE);
		createEAttribute(aCliche2EClass, ACLICHE2__MANY_VALUED_ATTRIBUTE);
		createEReference(aCliche2EClass, ACLICHE2__SINGLE_VALUED_REFERENCE);
		createEReference(aCliche2EClass, ACLICHE2__MANY_VALUED_REFERENCE);
		createEReference(aCliche2EClass, ACLICHE2__BASE_CLASS);

		aCliche3EClass = createEClass(ACLICHE3);
		createEReference(aCliche3EClass, ACLICHE3__BASE_CLASS);
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
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE
				.getEPackage(TypesPackage.eNS_URI);
		UMLPackage theUMLPackage = (UMLPackage)EPackage.Registry.INSTANCE.getEPackage(UMLPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(aClicheEClass, ACliche.class, "ACliche", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getACliche_SingleValuedAttribute(), theTypesPackage.getString(),
				"singleValuedAttribute", null, 0, 1, ACliche.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEAttribute(getACliche_ManyValuedAttribute(), theTypesPackage.getString(), "manyValuedAttribute", //$NON-NLS-1$
				null, 0, -1, ACliche.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getACliche_SingleValuedReference(), theUMLPackage.getClass_(), null,
				"singleValuedReference", null, 0, 1, ACliche.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				!IS_ORDERED);
		initEReference(getACliche_ManyValuedReference(), theUMLPackage.getClass_(), null,
				"manyValuedReference", null, 0, -1, ACliche.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
				!IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getACliche_Base_Class(), theUMLPackage.getClass_(), null, "base_Class", null, 1, 1, //$NON-NLS-1$
				ACliche.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(aCliche2EClass, ACliche2.class, "ACliche2", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getACliche2_SingleValuedAttribute(), theTypesPackage.getString(),
				"singleValuedAttribute", null, 0, 1, ACliche2.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEAttribute(getACliche2_ManyValuedAttribute(), theTypesPackage.getString(), "manyValuedAttribute", //$NON-NLS-1$
				null, 0, -1, ACliche2.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getACliche2_SingleValuedReference(), theUMLPackage.getClass_(), null,
				"singleValuedReference", null, 0, 1, ACliche2.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				!IS_ORDERED);
		initEReference(getACliche2_ManyValuedReference(), theUMLPackage.getClass_(), null,
				"manyValuedReference", null, 0, -1, ACliche2.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getACliche2_Base_Class(), theUMLPackage.getClass_(), null, "base_Class", null, 1, 1, //$NON-NLS-1$
				ACliche2.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(aCliche3EClass, ACliche3.class, "ACliche3", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getACliche3_Base_Class(), theUMLPackage.getClass_(), null, "base_Class", null, 1, 1, //$NON-NLS-1$
				ACliche3.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.eclipse.org/uml2/2.0.0/UML
		createUMLAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.eclipse.org/uml2/2.0.0/UML</b>. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void createUMLAnnotations() {
		String source = "http://www.eclipse.org/uml2/2.0.0/UML"; //$NON-NLS-1$
		addAnnotation(this, source, new String[] {"originalName", "UML2CompareTestProfile" //$NON-NLS-1$ //$NON-NLS-2$
		});
	}

} // UML2CompareTestProfilePackageImpl
