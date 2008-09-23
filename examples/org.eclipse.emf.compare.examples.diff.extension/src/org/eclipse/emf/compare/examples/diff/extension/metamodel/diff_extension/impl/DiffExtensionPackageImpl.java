/**
 * 
 *  Copyright (c) 2006, 2007, 2008 Obeo.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *      Obeo - initial API and implementation
 * 
 *
 * $Id: DiffExtensionPackageImpl.java,v 1.5 2008/09/23 16:00:15 lgoubet Exp $
 */
package org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl;

import org.eclipse.emf.compare.diff.metamodel.DiffPackage;

import org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.AddUMLAssociation;
import org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.DiffExtensionFactory;
import org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.DiffExtensionPackage;
import org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.UMLAssociationDiff;

import org.eclipse.emf.compare.match.metamodel.MatchPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * @generated
 */
public class DiffExtensionPackageImpl extends EPackageImpl implements DiffExtensionPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "\n Copyright (c) 2006, 2007, 2008 Obeo.\n All rights reserved. This program and the accompanying materials\n are made available under the terms of the Eclipse Public License v1.0\n which accompanies this distribution, and is available at\n http://www.eclipse.org/legal/epl-v10.html\n \n Contributors:\n     Obeo - initial API and implementation\n";

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass addUMLAssociationEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlAssociationDiffEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.DiffExtensionPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DiffExtensionPackageImpl() {
		super(eNS_URI, DiffExtensionFactory.eINSTANCE);
	}

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which
	 * it depends. Simple dependencies are satisfied by calling this method on all dependent packages before
	 * doing anything else. This method drives initialization for interdependent packages directly, in
	 * parallel with this package, itself.
	 * <p>
	 * Of this package and its interdependencies, all packages which have not yet been registered by their URI
	 * values are first created and registered. The packages are then initialized in two steps: meta-model
	 * objects for all of the packages are created before any are initialized, since one package's meta-model
	 * objects may refer to those of another.
	 * <p>
	 * Invocation of this method will not affect any packages that have already been initialized. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static DiffExtensionPackage init() {
		if (isInited) return (DiffExtensionPackage)EPackage.Registry.INSTANCE.getEPackage(DiffExtensionPackage.eNS_URI);

		// Obtain or create and register package
		DiffExtensionPackageImpl theDiffExtensionPackage = (DiffExtensionPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof DiffExtensionPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new DiffExtensionPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		DiffPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theDiffExtensionPackage.createPackageContents();

		// Initialize created meta-data
		theDiffExtensionPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDiffExtensionPackage.freeze();

		return theDiffExtensionPackage;
	}

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		addUMLAssociationEClass = createEClass(ADD_UML_ASSOCIATION);

		umlAssociationDiffEClass = createEClass(UML_ASSOCIATION_DIFF);
		createEReference(umlAssociationDiffEClass, UML_ASSOCIATION_DIFF__PROPERTIES);
		createEAttribute(umlAssociationDiffEClass, UML_ASSOCIATION_DIFF__IS_NAVIGABLE);
		createEReference(umlAssociationDiffEClass, UML_ASSOCIATION_DIFF__CONTAINER_PACKAGE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAddUMLAssociation() {
		return addUMLAssociationEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DiffExtensionFactory getDiffExtensionFactory() {
		return (DiffExtensionFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLAssociationDiff() {
		return umlAssociationDiffEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUMLAssociationDiff_ContainerPackage() {
		return (EReference)umlAssociationDiffEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUMLAssociationDiff_IsNavigable() {
		return (EAttribute)umlAssociationDiffEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUMLAssociationDiff_Properties() {
		return (EReference)umlAssociationDiffEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
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
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		addUMLAssociationEClass.getESuperTypes().add(this.getUMLAssociationDiff());
		addUMLAssociationEClass.getESuperTypes().add(theDiffPackage.getAddModelElement());
		umlAssociationDiffEClass.getESuperTypes().add(theDiffPackage.getAbstractDiffExtension());

		// Initialize classes and features; add operations and parameters
		initEClass(addUMLAssociationEClass, AddUMLAssociation.class, "AddUMLAssociation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(umlAssociationDiffEClass, UMLAssociationDiff.class, "UMLAssociationDiff", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getUMLAssociationDiff_Properties(), ecorePackage.getEObject(), null, "properties", null, 1, -1, UMLAssociationDiff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUMLAssociationDiff_IsNavigable(), theEcorePackage.getEBoolean(), "isNavigable", "false", 0, 1, UMLAssociationDiff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUMLAssociationDiff_ContainerPackage(), ecorePackage.getEObject(), null, "containerPackage", null, 1, 1, UMLAssociationDiff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} // DiffExtensionPackageImpl
