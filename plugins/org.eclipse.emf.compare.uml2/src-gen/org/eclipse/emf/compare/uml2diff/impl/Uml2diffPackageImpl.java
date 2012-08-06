/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2diff.impl;

import org.eclipse.emf.compare.ComparePackage;

import org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChange;
import org.eclipse.emf.compare.uml2diff.UMLAssociationChange;
import org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChange;
import org.eclipse.emf.compare.uml2diff.UMLDependencyChange;
import org.eclipse.emf.compare.uml2diff.UMLDestructionEventChange;
import org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChange;
import org.eclipse.emf.compare.uml2diff.UMLExtendChange;
import org.eclipse.emf.compare.uml2diff.UMLExtension;
import org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChange;
import org.eclipse.emf.compare.uml2diff.UMLInterfaceRealizationChange;
import org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChange;
import org.eclipse.emf.compare.uml2diff.UMLMessageChange;
import org.eclipse.emf.compare.uml2diff.UMLProfileApplicationChange;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationChange;
import org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChange;
import org.eclipse.emf.compare.uml2diff.UMLSubstitutionChange;
import org.eclipse.emf.compare.uml2diff.Uml2diffFactory;
import org.eclipse.emf.compare.uml2diff.Uml2diffPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.uml2.uml.UMLPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Uml2diffPackageImpl extends EPackageImpl implements Uml2diffPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlAssociationChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlGeneralizationSetChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlDependencyChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlInterfaceRealizationChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlSubstitutionChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlExtendChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlExecutionSpecificationChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlDestructionEventChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlIntervalConstraintChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlMessageChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlStereotypePropertyChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlStereotypeApplicationChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlStereotypeReferenceChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlProfileApplicationChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlExtensionEClass = null;

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
	 * @see org.eclipse.emf.compare.uml2diff.Uml2diffPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private Uml2diffPackageImpl() {
		super(eNS_URI, Uml2diffFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link Uml2diffPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static Uml2diffPackage init() {
		if (isInited) return (Uml2diffPackage)EPackage.Registry.INSTANCE.getEPackage(Uml2diffPackage.eNS_URI);

		// Obtain or create and register package
		Uml2diffPackageImpl theUml2diffPackage = (Uml2diffPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof Uml2diffPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new Uml2diffPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		ComparePackage.eINSTANCE.eClass();
		UMLPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theUml2diffPackage.createPackageContents();

		// Initialize created meta-data
		theUml2diffPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theUml2diffPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(Uml2diffPackage.eNS_URI, theUml2diffPackage);
		return theUml2diffPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLAssociationChange() {
		return umlAssociationChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUMLAssociationChange_Association() {
		return (EReference)umlAssociationChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLGeneralizationSetChange() {
		return umlGeneralizationSetChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUMLGeneralizationSetChange_GeneralizationSet() {
		return (EReference)umlGeneralizationSetChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLDependencyChange() {
		return umlDependencyChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUMLDependencyChange_Dependency() {
		return (EReference)umlDependencyChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLInterfaceRealizationChange() {
		return umlInterfaceRealizationChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLSubstitutionChange() {
		return umlSubstitutionChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLExtendChange() {
		return umlExtendChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUMLExtendChange_Extend() {
		return (EReference)umlExtendChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLExecutionSpecificationChange() {
		return umlExecutionSpecificationChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLDestructionEventChange() {
		return umlDestructionEventChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLIntervalConstraintChange() {
		return umlIntervalConstraintChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLMessageChange() {
		return umlMessageChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLStereotypePropertyChange() {
		return umlStereotypePropertyChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUMLStereotypePropertyChange_Stereotype() {
		return (EReference)umlStereotypePropertyChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLStereotypeApplicationChange() {
		return umlStereotypeApplicationChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUMLStereotypeApplicationChange_Stereotype() {
		return (EReference)umlStereotypeApplicationChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLStereotypeReferenceChange() {
		return umlStereotypeReferenceChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLProfileApplicationChange() {
		return umlProfileApplicationChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUMLProfileApplicationChange_Profile() {
		return (EReference)umlProfileApplicationChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLExtension() {
		return umlExtensionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUMLExtension_Discriminant() {
		return (EReference)umlExtensionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Uml2diffFactory getUml2diffFactory() {
		return (Uml2diffFactory)getEFactoryInstance();
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
		umlAssociationChangeEClass = createEClass(UML_ASSOCIATION_CHANGE);
		createEReference(umlAssociationChangeEClass, UML_ASSOCIATION_CHANGE__ASSOCIATION);

		umlDependencyChangeEClass = createEClass(UML_DEPENDENCY_CHANGE);
		createEReference(umlDependencyChangeEClass, UML_DEPENDENCY_CHANGE__DEPENDENCY);

		umlInterfaceRealizationChangeEClass = createEClass(UML_INTERFACE_REALIZATION_CHANGE);

		umlSubstitutionChangeEClass = createEClass(UML_SUBSTITUTION_CHANGE);

		umlExtendChangeEClass = createEClass(UML_EXTEND_CHANGE);
		createEReference(umlExtendChangeEClass, UML_EXTEND_CHANGE__EXTEND);

		umlGeneralizationSetChangeEClass = createEClass(UML_GENERALIZATION_SET_CHANGE);
		createEReference(umlGeneralizationSetChangeEClass, UML_GENERALIZATION_SET_CHANGE__GENERALIZATION_SET);

		umlExecutionSpecificationChangeEClass = createEClass(UML_EXECUTION_SPECIFICATION_CHANGE);

		umlDestructionEventChangeEClass = createEClass(UML_DESTRUCTION_EVENT_CHANGE);

		umlIntervalConstraintChangeEClass = createEClass(UML_INTERVAL_CONSTRAINT_CHANGE);

		umlMessageChangeEClass = createEClass(UML_MESSAGE_CHANGE);

		umlStereotypePropertyChangeEClass = createEClass(UML_STEREOTYPE_PROPERTY_CHANGE);
		createEReference(umlStereotypePropertyChangeEClass, UML_STEREOTYPE_PROPERTY_CHANGE__STEREOTYPE);

		umlStereotypeApplicationChangeEClass = createEClass(UML_STEREOTYPE_APPLICATION_CHANGE);
		createEReference(umlStereotypeApplicationChangeEClass, UML_STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE);

		umlStereotypeReferenceChangeEClass = createEClass(UML_STEREOTYPE_REFERENCE_CHANGE);

		umlProfileApplicationChangeEClass = createEClass(UML_PROFILE_APPLICATION_CHANGE);
		createEReference(umlProfileApplicationChangeEClass, UML_PROFILE_APPLICATION_CHANGE__PROFILE);

		umlExtensionEClass = createEClass(UML_EXTENSION);
		createEReference(umlExtensionEClass, UML_EXTENSION__DISCRIMINANT);
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
		UMLPackage theUMLPackage = (UMLPackage)EPackage.Registry.INSTANCE.getEPackage(UMLPackage.eNS_URI);
		ComparePackage theComparePackage = (ComparePackage)EPackage.Registry.INSTANCE.getEPackage(ComparePackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		umlAssociationChangeEClass.getESuperTypes().add(this.getUMLExtension());
		umlDependencyChangeEClass.getESuperTypes().add(this.getUMLExtension());
		umlInterfaceRealizationChangeEClass.getESuperTypes().add(this.getUMLExtension());
		umlSubstitutionChangeEClass.getESuperTypes().add(this.getUMLExtension());
		umlExtendChangeEClass.getESuperTypes().add(this.getUMLExtension());
		umlGeneralizationSetChangeEClass.getESuperTypes().add(this.getUMLExtension());
		umlExecutionSpecificationChangeEClass.getESuperTypes().add(this.getUMLExtension());
		umlDestructionEventChangeEClass.getESuperTypes().add(this.getUMLExtension());
		umlIntervalConstraintChangeEClass.getESuperTypes().add(this.getUMLExtension());
		umlMessageChangeEClass.getESuperTypes().add(this.getUMLExtension());
		umlStereotypePropertyChangeEClass.getESuperTypes().add(this.getUMLExtension());
		umlStereotypeApplicationChangeEClass.getESuperTypes().add(theComparePackage.getReferenceChange());
		umlStereotypeApplicationChangeEClass.getESuperTypes().add(this.getUMLExtension());
		umlStereotypeReferenceChangeEClass.getESuperTypes().add(this.getUMLExtension());
		umlProfileApplicationChangeEClass.getESuperTypes().add(theComparePackage.getReferenceChange());
		umlProfileApplicationChangeEClass.getESuperTypes().add(this.getUMLExtension());
		umlExtensionEClass.getESuperTypes().add(theComparePackage.getDiff());

		// Initialize classes and features; add operations and parameters
		initEClass(umlAssociationChangeEClass, UMLAssociationChange.class, "UMLAssociationChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getUMLAssociationChange_Association(), theUMLPackage.getAssociation(), null, "association", null, 0, 1, UMLAssociationChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(umlDependencyChangeEClass, UMLDependencyChange.class, "UMLDependencyChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getUMLDependencyChange_Dependency(), theUMLPackage.getDependency(), null, "dependency", null, 0, 1, UMLDependencyChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(umlInterfaceRealizationChangeEClass, UMLInterfaceRealizationChange.class, "UMLInterfaceRealizationChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlSubstitutionChangeEClass, UMLSubstitutionChange.class, "UMLSubstitutionChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlExtendChangeEClass, UMLExtendChange.class, "UMLExtendChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getUMLExtendChange_Extend(), theUMLPackage.getExtend(), null, "extend", null, 0, 1, UMLExtendChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(umlGeneralizationSetChangeEClass, UMLGeneralizationSetChange.class, "UMLGeneralizationSetChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getUMLGeneralizationSetChange_GeneralizationSet(), theUMLPackage.getGeneralizationSet(), null, "generalizationSet", null, 0, 1, UMLGeneralizationSetChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(umlExecutionSpecificationChangeEClass, UMLExecutionSpecificationChange.class, "UMLExecutionSpecificationChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlDestructionEventChangeEClass, UMLDestructionEventChange.class, "UMLDestructionEventChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlIntervalConstraintChangeEClass, UMLIntervalConstraintChange.class, "UMLIntervalConstraintChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlMessageChangeEClass, UMLMessageChange.class, "UMLMessageChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlStereotypePropertyChangeEClass, UMLStereotypePropertyChange.class, "UMLStereotypePropertyChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getUMLStereotypePropertyChange_Stereotype(), theUMLPackage.getStereotype(), null, "stereotype", null, 0, 1, UMLStereotypePropertyChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(umlStereotypeApplicationChangeEClass, UMLStereotypeApplicationChange.class, "UMLStereotypeApplicationChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getUMLStereotypeApplicationChange_Stereotype(), theUMLPackage.getStereotype(), null, "stereotype", null, 0, 1, UMLStereotypeApplicationChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(umlStereotypeReferenceChangeEClass, UMLStereotypeReferenceChange.class, "UMLStereotypeReferenceChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(umlProfileApplicationChangeEClass, UMLProfileApplicationChange.class, "UMLProfileApplicationChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getUMLProfileApplicationChange_Profile(), theUMLPackage.getProfile(), null, "profile", null, 0, 1, UMLProfileApplicationChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(umlExtensionEClass, UMLExtension.class, "UMLExtension", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getUMLExtension_Discriminant(), theEcorePackage.getEObject(), null, "discriminant", null, 0, 1, UMLExtension.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		// Create resource
		createResource(eNS_URI);
	}

} //Uml2diffPackageImpl
