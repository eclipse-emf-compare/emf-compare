/**
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2diff.impl;

import org.eclipse.emf.compare.diff.metamodel.DiffPackage;

import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffPackage;
import org.eclipse.emf.compare.uml2diff.UMLAbstractionChange;
import org.eclipse.emf.compare.uml2diff.UMLAbstractionChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLAbstractionChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLAssociationChange;
import org.eclipse.emf.compare.uml2diff.UMLAssociationChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLAssociationChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLDiffExtension;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationAddition;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationChange;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationRemoval;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateAttribute;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class UML2DiffPackageImpl extends EPackageImpl implements UML2DiffPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlDiffExtensionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlAbstractionChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlAbstractionChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlAbstractionChangeRightTargetEClass = null;

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
	private EClass umlAssociationChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlAssociationChangeRightTargetEClass = null;

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
	private EClass umlStereotypeAttributeChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlStereotypeAttributeChangeRightTargetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlStereotypeUpdateAttributeEClass = null;

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
	private EClass umlStereotypeApplicationAdditionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass umlStereotypeApplicationRemovalEClass = null;

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
	 * @see org.eclipse.emf.compare.uml2diff.UML2DiffPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private UML2DiffPackageImpl() {
		super(eNS_URI, UML2DiffFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link UML2DiffPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static UML2DiffPackage init() {
		if (isInited) return (UML2DiffPackage)EPackage.Registry.INSTANCE.getEPackage(UML2DiffPackage.eNS_URI);

		// Obtain or create and register package
		UML2DiffPackageImpl theUML2DiffPackage = (UML2DiffPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof UML2DiffPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new UML2DiffPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		DiffPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theUML2DiffPackage.createPackageContents();

		// Initialize created meta-data
		theUML2DiffPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theUML2DiffPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(UML2DiffPackage.eNS_URI, theUML2DiffPackage);
		return theUML2DiffPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLDiffExtension() {
		return umlDiffExtensionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLAbstractionChange() {
		return umlAbstractionChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLAbstractionChangeLeftTarget() {
		return umlAbstractionChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLAbstractionChangeRightTarget() {
		return umlAbstractionChangeRightTargetEClass;
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
	public EClass getUMLAssociationChangeLeftTarget() {
		return umlAssociationChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLAssociationChangeRightTarget() {
		return umlAssociationChangeRightTargetEClass;
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
	public EReference getUMLStereotypePropertyChange_StereotypeApplications() {
		return (EReference)umlStereotypePropertyChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLStereotypeAttributeChangeLeftTarget() {
		return umlStereotypeAttributeChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLStereotypeAttributeChangeRightTarget() {
		return umlStereotypeAttributeChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLStereotypeUpdateAttribute() {
		return umlStereotypeUpdateAttributeEClass;
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
	public EClass getUMLStereotypeApplicationAddition() {
		return umlStereotypeApplicationAdditionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUMLStereotypeApplicationRemoval() {
		return umlStereotypeApplicationRemovalEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UML2DiffFactory getUML2DiffFactory() {
		return (UML2DiffFactory)getEFactoryInstance();
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
		umlDiffExtensionEClass = createEClass(UML_DIFF_EXTENSION);

		umlAbstractionChangeEClass = createEClass(UML_ABSTRACTION_CHANGE);

		umlAbstractionChangeLeftTargetEClass = createEClass(UML_ABSTRACTION_CHANGE_LEFT_TARGET);

		umlAbstractionChangeRightTargetEClass = createEClass(UML_ABSTRACTION_CHANGE_RIGHT_TARGET);

		umlAssociationChangeEClass = createEClass(UML_ASSOCIATION_CHANGE);

		umlAssociationChangeLeftTargetEClass = createEClass(UML_ASSOCIATION_CHANGE_LEFT_TARGET);

		umlAssociationChangeRightTargetEClass = createEClass(UML_ASSOCIATION_CHANGE_RIGHT_TARGET);

		umlStereotypePropertyChangeEClass = createEClass(UML_STEREOTYPE_PROPERTY_CHANGE);
		createEReference(umlStereotypePropertyChangeEClass, UML_STEREOTYPE_PROPERTY_CHANGE__STEREOTYPE_APPLICATIONS);

		umlStereotypeAttributeChangeLeftTargetEClass = createEClass(UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET);

		umlStereotypeAttributeChangeRightTargetEClass = createEClass(UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET);

		umlStereotypeUpdateAttributeEClass = createEClass(UML_STEREOTYPE_UPDATE_ATTRIBUTE);

		umlStereotypeApplicationChangeEClass = createEClass(UML_STEREOTYPE_APPLICATION_CHANGE);

		umlStereotypeApplicationAdditionEClass = createEClass(UML_STEREOTYPE_APPLICATION_ADDITION);

		umlStereotypeApplicationRemovalEClass = createEClass(UML_STEREOTYPE_APPLICATION_REMOVAL);
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
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		umlDiffExtensionEClass.getESuperTypes().add(theDiffPackage.getDiffElement());
		umlDiffExtensionEClass.getESuperTypes().add(theDiffPackage.getAbstractDiffExtension());
		umlAbstractionChangeEClass.getESuperTypes().add(this.getUMLDiffExtension());
		umlAbstractionChangeLeftTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeLeftTarget());
		umlAbstractionChangeLeftTargetEClass.getESuperTypes().add(this.getUMLAbstractionChange());
		umlAbstractionChangeRightTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeRightTarget());
		umlAbstractionChangeRightTargetEClass.getESuperTypes().add(this.getUMLAbstractionChange());
		umlAssociationChangeEClass.getESuperTypes().add(this.getUMLDiffExtension());
		umlAssociationChangeLeftTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeLeftTarget());
		umlAssociationChangeLeftTargetEClass.getESuperTypes().add(this.getUMLAssociationChange());
		umlAssociationChangeRightTargetEClass.getESuperTypes().add(theDiffPackage.getModelElementChangeRightTarget());
		umlAssociationChangeRightTargetEClass.getESuperTypes().add(this.getUMLAssociationChange());
		umlStereotypePropertyChangeEClass.getESuperTypes().add(this.getUMLDiffExtension());
		umlStereotypeAttributeChangeLeftTargetEClass.getESuperTypes().add(theDiffPackage.getAttributeChangeLeftTarget());
		umlStereotypeAttributeChangeLeftTargetEClass.getESuperTypes().add(this.getUMLStereotypePropertyChange());
		umlStereotypeAttributeChangeRightTargetEClass.getESuperTypes().add(theDiffPackage.getAttributeChangeRightTarget());
		umlStereotypeAttributeChangeRightTargetEClass.getESuperTypes().add(this.getUMLStereotypePropertyChange());
		umlStereotypeUpdateAttributeEClass.getESuperTypes().add(theDiffPackage.getUpdateAttribute());
		umlStereotypeUpdateAttributeEClass.getESuperTypes().add(this.getUMLStereotypePropertyChange());
		umlStereotypeApplicationChangeEClass.getESuperTypes().add(this.getUMLDiffExtension());
		umlStereotypeApplicationAdditionEClass.getESuperTypes().add(theDiffPackage.getUpdateModelElement());
		umlStereotypeApplicationAdditionEClass.getESuperTypes().add(this.getUMLStereotypeApplicationChange());
		umlStereotypeApplicationRemovalEClass.getESuperTypes().add(theDiffPackage.getUpdateModelElement());
		umlStereotypeApplicationRemovalEClass.getESuperTypes().add(this.getUMLStereotypeApplicationChange());

		// Initialize classes and features; add operations and parameters
		initEClass(umlDiffExtensionEClass, UMLDiffExtension.class, "UMLDiffExtension", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(umlAbstractionChangeEClass, UMLAbstractionChange.class, "UMLAbstractionChange", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(umlAbstractionChangeLeftTargetEClass, UMLAbstractionChangeLeftTarget.class, "UMLAbstractionChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(umlAbstractionChangeRightTargetEClass, UMLAbstractionChangeRightTarget.class, "UMLAbstractionChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(umlAssociationChangeEClass, UMLAssociationChange.class, "UMLAssociationChange", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(umlAssociationChangeLeftTargetEClass, UMLAssociationChangeLeftTarget.class, "UMLAssociationChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(umlAssociationChangeRightTargetEClass, UMLAssociationChangeRightTarget.class, "UMLAssociationChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(umlStereotypePropertyChangeEClass, UMLStereotypePropertyChange.class, "UMLStereotypePropertyChange", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getUMLStereotypePropertyChange_StereotypeApplications(), theEcorePackage.getEObject(), null, "stereotypeApplications", null, 0, -1, UMLStereotypePropertyChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(umlStereotypeAttributeChangeLeftTargetEClass, UMLStereotypeAttributeChangeLeftTarget.class, "UMLStereotypeAttributeChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(umlStereotypeAttributeChangeRightTargetEClass, UMLStereotypeAttributeChangeRightTarget.class, "UMLStereotypeAttributeChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(umlStereotypeUpdateAttributeEClass, UMLStereotypeUpdateAttribute.class, "UMLStereotypeUpdateAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(umlStereotypeApplicationChangeEClass, UMLStereotypeApplicationChange.class, "UMLStereotypeApplicationChange", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(umlStereotypeApplicationAdditionEClass, UMLStereotypeApplicationAddition.class, "UMLStereotypeApplicationAddition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(umlStereotypeApplicationRemovalEClass, UMLStereotypeApplicationRemoval.class, "UMLStereotypeApplicationRemoval", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //UML2DiffPackageImpl
