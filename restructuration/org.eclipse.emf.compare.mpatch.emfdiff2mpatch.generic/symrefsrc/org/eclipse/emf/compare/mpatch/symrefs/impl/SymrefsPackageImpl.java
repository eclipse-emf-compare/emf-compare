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
 * $Id: SymrefsPackageImpl.java,v 1.1 2010/09/10 15:32:55 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.symrefs.impl;

import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.symrefs.Condition;
import org.eclipse.emf.compare.mpatch.symrefs.ElementSetReference;
import org.eclipse.emf.compare.mpatch.symrefs.ExternalElementReference;
import org.eclipse.emf.compare.mpatch.symrefs.IdEmfReference;
import org.eclipse.emf.compare.mpatch.symrefs.OclCondition;
import org.eclipse.emf.compare.mpatch.symrefs.SymrefsFactory;
import org.eclipse.emf.compare.mpatch.symrefs.SymrefsPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
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
public class SymrefsPackageImpl extends EPackageImpl implements SymrefsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass externalElementReferenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass idEmfReferenceEClass = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass elementSetReferenceEClass = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass conditionEClass = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass oclConditionEClass = null;
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
	 * @see org.eclipse.emf.compare.mpatch.symrefs.SymrefsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private SymrefsPackageImpl() {
		super(eNS_URI, SymrefsFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link SymrefsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static SymrefsPackage init() {
		if (isInited) return (SymrefsPackage)EPackage.Registry.INSTANCE.getEPackage(SymrefsPackage.eNS_URI);

		// Obtain or create and register package
		SymrefsPackageImpl theSymrefsPackage = (SymrefsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof SymrefsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new SymrefsPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		MPatchPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theSymrefsPackage.createPackageContents();

		// Initialize created meta-data
		theSymrefsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theSymrefsPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(SymrefsPackage.eNS_URI, theSymrefsPackage);
		return theSymrefsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExternalElementReference() {
		return externalElementReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIdEmfReference() {
		return idEmfReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIdEmfReference_IdAttributeValue() {
		return (EAttribute)idEmfReferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getElementSetReference() {
		return elementSetReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getElementSetReference_Context() {
		return (EReference)elementSetReferenceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getElementSetReference_Conditions() {
		return (EReference)elementSetReferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCondition() {
		return conditionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCondition_ElementReference() {
		return (EReference)conditionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOclCondition() {
		return oclConditionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOclCondition_Expression() {
		return (EAttribute)oclConditionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOclCondition_CheckType() {
		return (EAttribute)oclConditionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SymrefsFactory getSymrefsFactory() {
		return (SymrefsFactory)getEFactoryInstance();
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
		externalElementReferenceEClass = createEClass(EXTERNAL_ELEMENT_REFERENCE);

		idEmfReferenceEClass = createEClass(ID_EMF_REFERENCE);
		createEAttribute(idEmfReferenceEClass, ID_EMF_REFERENCE__ID_ATTRIBUTE_VALUE);

		elementSetReferenceEClass = createEClass(ELEMENT_SET_REFERENCE);
		createEReference(elementSetReferenceEClass, ELEMENT_SET_REFERENCE__CONDITIONS);
		createEReference(elementSetReferenceEClass, ELEMENT_SET_REFERENCE__CONTEXT);

		conditionEClass = createEClass(CONDITION);
		createEReference(conditionEClass, CONDITION__ELEMENT_REFERENCE);

		oclConditionEClass = createEClass(OCL_CONDITION);
		createEAttribute(oclConditionEClass, OCL_CONDITION__EXPRESSION);
		createEAttribute(oclConditionEClass, OCL_CONDITION__CHECK_TYPE);
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
		externalElementReferenceEClass.getESuperTypes().add(theMPatchPackage.getIElementReference());
		idEmfReferenceEClass.getESuperTypes().add(theMPatchPackage.getIElementReference());
		elementSetReferenceEClass.getESuperTypes().add(theMPatchPackage.getIElementReference());
		oclConditionEClass.getESuperTypes().add(this.getCondition());

		// Initialize classes and features; add operations and parameters
		initEClass(externalElementReferenceEClass, ExternalElementReference.class, "ExternalElementReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(idEmfReferenceEClass, IdEmfReference.class, "IdEmfReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIdEmfReference_IdAttributeValue(), ecorePackage.getEString(), "idAttributeValue", null, 0, 1, IdEmfReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(elementSetReferenceEClass, ElementSetReference.class, "ElementSetReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getElementSetReference_Conditions(), this.getCondition(), this.getCondition_ElementReference(), "conditions", null, 0, -1, ElementSetReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElementSetReference_Context(), theMPatchPackage.getIElementReference(), null, "context", null, 0, 1, ElementSetReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(conditionEClass, Condition.class, "Condition", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCondition_ElementReference(), this.getElementSetReference(), this.getElementSetReference_Conditions(), "elementReference", null, 0, 1, Condition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(conditionEClass, ecorePackage.getEObject(), "collectValidElements", 0, -1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEObject(), "model", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(conditionEClass, ecorePackage.getEBoolean(), "sameCondition", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getCondition(), "other", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(oclConditionEClass, OclCondition.class, "OclCondition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getOclCondition_Expression(), theEcorePackage.getEString(), "expression", null, 0, 1, OclCondition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOclCondition_CheckType(), ecorePackage.getEBoolean(), "checkType", "true", 0, 1, OclCondition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //SymrefsPackageImpl
