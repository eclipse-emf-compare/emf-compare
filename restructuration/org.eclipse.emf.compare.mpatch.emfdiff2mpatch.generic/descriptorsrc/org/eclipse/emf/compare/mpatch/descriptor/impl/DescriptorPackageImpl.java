/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: DescriptorPackageImpl.java,v 1.1 2010/09/10 15:32:55 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.descriptor.impl;

import java.util.Map;

import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.descriptor.DescriptorFactory;
import org.eclipse.emf.compare.mpatch.descriptor.DescriptorPackage;
import org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor;
import org.eclipse.emf.ecore.EAttribute;
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
public class DescriptorPackageImpl extends EPackageImpl implements DescriptorPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass emfModelDescriptorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eAttributeToObjectMapEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eReferenceToDescriptorMapEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eReferenceToElementReferenceMapEClass = null;

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
	 * @see org.eclipse.emf.compare.mpatch.descriptor.DescriptorPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DescriptorPackageImpl() {
		super(eNS_URI, DescriptorFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link DescriptorPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static DescriptorPackage init() {
		if (isInited) return (DescriptorPackage)EPackage.Registry.INSTANCE.getEPackage(DescriptorPackage.eNS_URI);

		// Obtain or create and register package
		DescriptorPackageImpl theDescriptorPackage = (DescriptorPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof DescriptorPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new DescriptorPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		MPatchPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theDescriptorPackage.createPackageContents();

		// Initialize created meta-data
		theDescriptorPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDescriptorPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(DescriptorPackage.eNS_URI, theDescriptorPackage);
		return theDescriptorPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEMFModelDescriptor() {
		return emfModelDescriptorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEMFModelDescriptor_Attributes() {
		return (EReference)emfModelDescriptorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEMFModelDescriptor_SubDescriptors() {
		return (EReference)emfModelDescriptorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEMFModelDescriptor_References() {
		return (EReference)emfModelDescriptorEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEMFModelDescriptor_DescriptorUri() {
		return (EAttribute)emfModelDescriptorEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEAttributeToObjectMap() {
		return eAttributeToObjectMapEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEAttributeToObjectMap_Key() {
		return (EReference)eAttributeToObjectMapEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEAttributeToObjectMap_Value() {
		return (EAttribute)eAttributeToObjectMapEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEReferenceToDescriptorMap() {
		return eReferenceToDescriptorMapEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEReferenceToDescriptorMap_Key() {
		return (EReference)eReferenceToDescriptorMapEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEReferenceToDescriptorMap_Value() {
		return (EReference)eReferenceToDescriptorMapEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEReferenceToElementReferenceMap() {
		return eReferenceToElementReferenceMapEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEReferenceToElementReferenceMap_Value() {
		return (EReference)eReferenceToElementReferenceMapEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEReferenceToElementReferenceMap_Key() {
		return (EReference)eReferenceToElementReferenceMapEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DescriptorFactory getDescriptorFactory() {
		return (DescriptorFactory)getEFactoryInstance();
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
		emfModelDescriptorEClass = createEClass(EMF_MODEL_DESCRIPTOR);
		createEReference(emfModelDescriptorEClass, EMF_MODEL_DESCRIPTOR__ATTRIBUTES);
		createEReference(emfModelDescriptorEClass, EMF_MODEL_DESCRIPTOR__SUB_DESCRIPTORS);
		createEReference(emfModelDescriptorEClass, EMF_MODEL_DESCRIPTOR__REFERENCES);
		createEAttribute(emfModelDescriptorEClass, EMF_MODEL_DESCRIPTOR__DESCRIPTOR_URI);

		eAttributeToObjectMapEClass = createEClass(EATTRIBUTE_TO_OBJECT_MAP);
		createEReference(eAttributeToObjectMapEClass, EATTRIBUTE_TO_OBJECT_MAP__KEY);
		createEAttribute(eAttributeToObjectMapEClass, EATTRIBUTE_TO_OBJECT_MAP__VALUE);

		eReferenceToDescriptorMapEClass = createEClass(EREFERENCE_TO_DESCRIPTOR_MAP);
		createEReference(eReferenceToDescriptorMapEClass, EREFERENCE_TO_DESCRIPTOR_MAP__KEY);
		createEReference(eReferenceToDescriptorMapEClass, EREFERENCE_TO_DESCRIPTOR_MAP__VALUE);

		eReferenceToElementReferenceMapEClass = createEClass(EREFERENCE_TO_ELEMENT_REFERENCE_MAP);
		createEReference(eReferenceToElementReferenceMapEClass, EREFERENCE_TO_ELEMENT_REFERENCE_MAP__VALUE);
		createEReference(eReferenceToElementReferenceMapEClass, EREFERENCE_TO_ELEMENT_REFERENCE_MAP__KEY);
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
		MPatchPackage theMPatchPackage = (MPatchPackage)EPackage.Registry.INSTANCE.getEPackage(MPatchPackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		emfModelDescriptorEClass.getESuperTypes().add(theMPatchPackage.getIModelDescriptor());

		// Initialize classes and features; add operations and parameters
		initEClass(emfModelDescriptorEClass, EMFModelDescriptor.class, "EMFModelDescriptor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEMFModelDescriptor_Attributes(), this.getEAttributeToObjectMap(), null, "attributes", null, 0, -1, EMFModelDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEMFModelDescriptor_SubDescriptors(), this.getEReferenceToDescriptorMap(), null, "subDescriptors", null, 0, -1, EMFModelDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEMFModelDescriptor_References(), this.getEReferenceToElementReferenceMap(), null, "references", null, 0, -1, EMFModelDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEMFModelDescriptor_DescriptorUri(), ecorePackage.getEString(), "descriptorUri", null, 0, 1, EMFModelDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eAttributeToObjectMapEClass, Map.Entry.class, "EAttributeToObjectMap", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEAttributeToObjectMap_Key(), theEcorePackage.getEAttribute(), null, "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEAttributeToObjectMap_Value(), ecorePackage.getEJavaObject(), "value", null, 0, -1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eReferenceToDescriptorMapEClass, Map.Entry.class, "EReferenceToDescriptorMap", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEReferenceToDescriptorMap_Key(), theEcorePackage.getEReference(), null, "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEReferenceToDescriptorMap_Value(), this.getEMFModelDescriptor(), null, "value", null, 0, -1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eReferenceToElementReferenceMapEClass, Map.Entry.class, "EReferenceToElementReferenceMap", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEReferenceToElementReferenceMap_Value(), theMPatchPackage.getIElementReference(), null, "value", null, 0, -1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEReferenceToElementReferenceMap_Key(), theEcorePackage.getEReference(), null, "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //DescriptorPackageImpl
