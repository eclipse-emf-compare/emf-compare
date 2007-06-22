/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.impl;

import org.eclipse.emf.compare.diff.AddAttribute;
import org.eclipse.emf.compare.diff.AddModelElement;
import org.eclipse.emf.compare.diff.AddReferenceValue;
import org.eclipse.emf.compare.diff.AttributeChange;
import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.diff.DiffFactory;
import org.eclipse.emf.compare.diff.DiffGroup;
import org.eclipse.emf.compare.diff.DiffModel;
import org.eclipse.emf.compare.diff.DiffPackage;
import org.eclipse.emf.compare.diff.ModelElementChange;
import org.eclipse.emf.compare.diff.ModelInputSnapshot;
import org.eclipse.emf.compare.diff.MoveModelElement;
import org.eclipse.emf.compare.diff.ReferenceChange;
import org.eclipse.emf.compare.diff.RemoveAttribute;
import org.eclipse.emf.compare.diff.RemoveModelElement;
import org.eclipse.emf.compare.diff.RemoveReferenceValue;
import org.eclipse.emf.compare.diff.UpdateAttribute;
import org.eclipse.emf.compare.diff.UpdateModelElement;
import org.eclipse.emf.compare.diff.UpdateReference;
import org.eclipse.emf.compare.match.MatchPackage;
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
public class DiffPackageImpl extends EPackageImpl implements DiffPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diffModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diffElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diffGroupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass attributeChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass referenceChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass modelElementChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass addModelElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass removeModelElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass updateModelElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass moveModelElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass addAttributeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass removeAttributeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass updateAttributeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass addReferenceValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass removeReferenceValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass updateReferenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass modelInputSnapshotEClass = null;

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
	 * @see org.eclipse.emf.compare.diff.DiffPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DiffPackageImpl() {
		super(eNS_URI, DiffFactory.eINSTANCE);
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
	public static DiffPackage init() {
		if (isInited) return (DiffPackage)EPackage.Registry.INSTANCE.getEPackage(DiffPackage.eNS_URI);

		// Obtain or create and register package
		DiffPackageImpl theDiffPackage = (DiffPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof DiffPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new DiffPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		EcorePackage.eINSTANCE.eClass();
		MatchPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theDiffPackage.createPackageContents();

		// Initialize created meta-data
		theDiffPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDiffPackage.freeze();

		return theDiffPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiffModel() {
		return diffModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDiffModel_Right() {
		return (EAttribute)diffModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiffModel_OwnedElements() {
		return (EReference)diffModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDiffModel_Left() {
		return (EAttribute)diffModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiffElement() {
		return diffElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiffElement_SubDiffElements() {
		return (EReference)diffElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiffGroup() {
		return diffGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiffGroup_LeftParent() {
		return (EReference)diffGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDiffGroup_Subchanges() {
		return (EAttribute)diffGroupEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAttributeChange() {
		return attributeChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAttributeChange_Attribute() {
		return (EReference)attributeChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAttributeChange_LeftElement() {
		return (EReference)attributeChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAttributeChange_RightElement() {
		return (EReference)attributeChangeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getReferenceChange() {
		return referenceChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getReferenceChange_Reference() {
		return (EReference)referenceChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getReferenceChange_RightElement() {
		return (EReference)referenceChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getReferenceChange_LeftElement() {
		return (EReference)referenceChangeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getModelElementChange() {
		return modelElementChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAddModelElement() {
		return addModelElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAddModelElement_LeftParent() {
		return (EReference)addModelElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAddModelElement_RightElement() {
		return (EReference)addModelElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoveModelElement() {
		return removeModelElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRemoveModelElement_RightParent() {
		return (EReference)removeModelElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRemoveModelElement_LeftElement() {
		return (EReference)removeModelElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUpdateModelElement() {
		return updateModelElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUpdateModelElement_RightElement() {
		return (EReference)updateModelElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUpdateModelElement_LeftElement() {
		return (EReference)updateModelElementEClass.getEStructuralFeatures().get(1);
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
	public EReference getMoveModelElement_LeftParent() {
		return (EReference)moveModelElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMoveModelElement_RightParent() {
		return (EReference)moveModelElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAddAttribute() {
		return addAttributeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoveAttribute() {
		return removeAttributeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUpdateAttribute() {
		return updateAttributeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAddReferenceValue() {
		return addReferenceValueEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAddReferenceValue_LeftAddedTarget() {
		return (EReference)addReferenceValueEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAddReferenceValue_RightAddedTarget() {
		return (EReference)addReferenceValueEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoveReferenceValue() {
		return removeReferenceValueEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRemoveReferenceValue_LeftRemovedTarget() {
		return (EReference)removeReferenceValueEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRemoveReferenceValue_RightRemovedTarget() {
		return (EReference)removeReferenceValueEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUpdateReference() {
		return updateReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getModelInputSnapshot() {
		return modelInputSnapshotEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getModelInputSnapshot_Date() {
		return (EAttribute)modelInputSnapshotEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getModelInputSnapshot_Diff() {
		return (EReference)modelInputSnapshotEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getModelInputSnapshot_Match() {
		return (EReference)modelInputSnapshotEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiffFactory getDiffFactory() {
		return (DiffFactory)getEFactoryInstance();
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
		diffModelEClass = createEClass(DIFF_MODEL);
		createEAttribute(diffModelEClass, DIFF_MODEL__RIGHT);
		createEReference(diffModelEClass, DIFF_MODEL__OWNED_ELEMENTS);
		createEAttribute(diffModelEClass, DIFF_MODEL__LEFT);

		diffElementEClass = createEClass(DIFF_ELEMENT);
		createEReference(diffElementEClass, DIFF_ELEMENT__SUB_DIFF_ELEMENTS);

		diffGroupEClass = createEClass(DIFF_GROUP);
		createEReference(diffGroupEClass, DIFF_GROUP__LEFT_PARENT);
		createEAttribute(diffGroupEClass, DIFF_GROUP__SUBCHANGES);

		attributeChangeEClass = createEClass(ATTRIBUTE_CHANGE);
		createEReference(attributeChangeEClass, ATTRIBUTE_CHANGE__ATTRIBUTE);
		createEReference(attributeChangeEClass, ATTRIBUTE_CHANGE__LEFT_ELEMENT);
		createEReference(attributeChangeEClass, ATTRIBUTE_CHANGE__RIGHT_ELEMENT);

		referenceChangeEClass = createEClass(REFERENCE_CHANGE);
		createEReference(referenceChangeEClass, REFERENCE_CHANGE__REFERENCE);
		createEReference(referenceChangeEClass, REFERENCE_CHANGE__RIGHT_ELEMENT);
		createEReference(referenceChangeEClass, REFERENCE_CHANGE__LEFT_ELEMENT);

		modelElementChangeEClass = createEClass(MODEL_ELEMENT_CHANGE);

		addModelElementEClass = createEClass(ADD_MODEL_ELEMENT);
		createEReference(addModelElementEClass, ADD_MODEL_ELEMENT__LEFT_PARENT);
		createEReference(addModelElementEClass, ADD_MODEL_ELEMENT__RIGHT_ELEMENT);

		removeModelElementEClass = createEClass(REMOVE_MODEL_ELEMENT);
		createEReference(removeModelElementEClass, REMOVE_MODEL_ELEMENT__RIGHT_PARENT);
		createEReference(removeModelElementEClass, REMOVE_MODEL_ELEMENT__LEFT_ELEMENT);

		updateModelElementEClass = createEClass(UPDATE_MODEL_ELEMENT);
		createEReference(updateModelElementEClass, UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT);
		createEReference(updateModelElementEClass, UPDATE_MODEL_ELEMENT__LEFT_ELEMENT);

		moveModelElementEClass = createEClass(MOVE_MODEL_ELEMENT);
		createEReference(moveModelElementEClass, MOVE_MODEL_ELEMENT__LEFT_PARENT);
		createEReference(moveModelElementEClass, MOVE_MODEL_ELEMENT__RIGHT_PARENT);

		addAttributeEClass = createEClass(ADD_ATTRIBUTE);

		removeAttributeEClass = createEClass(REMOVE_ATTRIBUTE);

		updateAttributeEClass = createEClass(UPDATE_ATTRIBUTE);

		addReferenceValueEClass = createEClass(ADD_REFERENCE_VALUE);
		createEReference(addReferenceValueEClass, ADD_REFERENCE_VALUE__LEFT_ADDED_TARGET);
		createEReference(addReferenceValueEClass, ADD_REFERENCE_VALUE__RIGHT_ADDED_TARGET);

		removeReferenceValueEClass = createEClass(REMOVE_REFERENCE_VALUE);
		createEReference(removeReferenceValueEClass, REMOVE_REFERENCE_VALUE__LEFT_REMOVED_TARGET);
		createEReference(removeReferenceValueEClass, REMOVE_REFERENCE_VALUE__RIGHT_REMOVED_TARGET);

		updateReferenceEClass = createEClass(UPDATE_REFERENCE);

		modelInputSnapshotEClass = createEClass(MODEL_INPUT_SNAPSHOT);
		createEAttribute(modelInputSnapshotEClass, MODEL_INPUT_SNAPSHOT__DATE);
		createEReference(modelInputSnapshotEClass, MODEL_INPUT_SNAPSHOT__DIFF);
		createEReference(modelInputSnapshotEClass, MODEL_INPUT_SNAPSHOT__MATCH);
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
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);
		MatchPackage theMatchPackage = (MatchPackage)EPackage.Registry.INSTANCE.getEPackage(MatchPackage.eNS_URI);

		// Add supertypes to classes
		diffGroupEClass.getESuperTypes().add(this.getDiffElement());
		attributeChangeEClass.getESuperTypes().add(this.getDiffElement());
		referenceChangeEClass.getESuperTypes().add(this.getDiffElement());
		modelElementChangeEClass.getESuperTypes().add(this.getDiffElement());
		addModelElementEClass.getESuperTypes().add(this.getModelElementChange());
		removeModelElementEClass.getESuperTypes().add(this.getModelElementChange());
		updateModelElementEClass.getESuperTypes().add(this.getModelElementChange());
		moveModelElementEClass.getESuperTypes().add(this.getUpdateModelElement());
		addAttributeEClass.getESuperTypes().add(this.getAttributeChange());
		removeAttributeEClass.getESuperTypes().add(this.getAttributeChange());
		updateAttributeEClass.getESuperTypes().add(this.getAttributeChange());
		addReferenceValueEClass.getESuperTypes().add(this.getReferenceChange());
		removeReferenceValueEClass.getESuperTypes().add(this.getReferenceChange());
		updateReferenceEClass.getESuperTypes().add(this.getReferenceChange());

		// Initialize classes and features; add operations and parameters
		initEClass(diffModelEClass, DiffModel.class, "DiffModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDiffModel_Right(), ecorePackage.getEString(), "right", null, 0, 1, DiffModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDiffModel_OwnedElements(), this.getDiffElement(), null, "ownedElements", null, 0, -1, DiffModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDiffModel_Left(), ecorePackage.getEString(), "left", null, 0, 1, DiffModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(diffElementEClass, DiffElement.class, "DiffElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDiffElement_SubDiffElements(), this.getDiffElement(), null, "subDiffElements", null, 0, -1, DiffElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(diffGroupEClass, DiffGroup.class, "DiffGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDiffGroup_LeftParent(), ecorePackage.getEObject(), null, "leftParent", null, 0, 1, DiffGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDiffGroup_Subchanges(), ecorePackage.getEInt(), "subchanges", null, 0, 1, DiffGroup.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(attributeChangeEClass, AttributeChange.class, "AttributeChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAttributeChange_Attribute(), theEcorePackage.getEAttribute(), null, "attribute", null, 0, 1, AttributeChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAttributeChange_LeftElement(), ecorePackage.getEObject(), null, "leftElement", null, 0, 1, AttributeChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAttributeChange_RightElement(), ecorePackage.getEObject(), null, "rightElement", null, 0, 1, AttributeChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(referenceChangeEClass, ReferenceChange.class, "ReferenceChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getReferenceChange_Reference(), theEcorePackage.getEReference(), null, "reference", null, 0, 1, ReferenceChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getReferenceChange_RightElement(), ecorePackage.getEObject(), null, "rightElement", null, 0, 1, ReferenceChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getReferenceChange_LeftElement(), ecorePackage.getEObject(), null, "leftElement", null, 0, 1, ReferenceChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(modelElementChangeEClass, ModelElementChange.class, "ModelElementChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(addModelElementEClass, AddModelElement.class, "AddModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAddModelElement_LeftParent(), ecorePackage.getEObject(), null, "leftParent", null, 0, 1, AddModelElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAddModelElement_RightElement(), ecorePackage.getEObject(), null, "rightElement", null, 0, 1, AddModelElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(removeModelElementEClass, RemoveModelElement.class, "RemoveModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRemoveModelElement_RightParent(), ecorePackage.getEObject(), null, "rightParent", null, 0, 1, RemoveModelElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRemoveModelElement_LeftElement(), ecorePackage.getEObject(), null, "leftElement", null, 0, 1, RemoveModelElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(updateModelElementEClass, UpdateModelElement.class, "UpdateModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getUpdateModelElement_RightElement(), ecorePackage.getEObject(), null, "rightElement", null, 0, 1, UpdateModelElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUpdateModelElement_LeftElement(), ecorePackage.getEObject(), null, "leftElement", null, 0, 1, UpdateModelElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(moveModelElementEClass, MoveModelElement.class, "MoveModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMoveModelElement_LeftParent(), ecorePackage.getEObject(), null, "leftParent", null, 0, 1, MoveModelElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMoveModelElement_RightParent(), ecorePackage.getEObject(), null, "rightParent", null, 0, 1, MoveModelElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(addAttributeEClass, AddAttribute.class, "AddAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(removeAttributeEClass, RemoveAttribute.class, "RemoveAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(updateAttributeEClass, UpdateAttribute.class, "UpdateAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(addReferenceValueEClass, AddReferenceValue.class, "AddReferenceValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAddReferenceValue_LeftAddedTarget(), ecorePackage.getEObject(), null, "leftAddedTarget", null, 0, -1, AddReferenceValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAddReferenceValue_RightAddedTarget(), ecorePackage.getEObject(), null, "rightAddedTarget", null, 0, -1, AddReferenceValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(removeReferenceValueEClass, RemoveReferenceValue.class, "RemoveReferenceValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRemoveReferenceValue_LeftRemovedTarget(), ecorePackage.getEObject(), null, "leftRemovedTarget", null, 0, -1, RemoveReferenceValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRemoveReferenceValue_RightRemovedTarget(), ecorePackage.getEObject(), null, "rightRemovedTarget", null, 0, -1, RemoveReferenceValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(updateReferenceEClass, UpdateReference.class, "UpdateReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(modelInputSnapshotEClass, ModelInputSnapshot.class, "ModelInputSnapshot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getModelInputSnapshot_Date(), ecorePackage.getEDate(), "date", null, 0, 1, ModelInputSnapshot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModelInputSnapshot_Diff(), this.getDiffModel(), null, "diff", null, 0, 1, ModelInputSnapshot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModelInputSnapshot_Match(), theMatchPackage.getMatchModel(), null, "match", null, 0, 1, ModelInputSnapshot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //DiffPackageImpl
