/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.metamodel.impl;

import org.eclipse.emf.compare.diff.merge.api.IMerger;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.AddAttribute;
import org.eclipse.emf.compare.diff.metamodel.AddModelElement;
import org.eclipse.emf.compare.diff.metamodel.AddReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.AddResourceDependency;
import org.eclipse.emf.compare.diff.metamodel.AttributeChange;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.metamodel.DifferenceKind;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChange;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.MoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.RemoteAddAttribute;
import org.eclipse.emf.compare.diff.metamodel.RemoteAddModelElement;
import org.eclipse.emf.compare.diff.metamodel.RemoteAddReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.RemoteAddResourceDependency;
import org.eclipse.emf.compare.diff.metamodel.RemoteMoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.RemoteRemoveAttribute;
import org.eclipse.emf.compare.diff.metamodel.RemoteRemoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.RemoteRemoveReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.RemoteRemoveResourceDependency;
import org.eclipse.emf.compare.diff.metamodel.RemoteUpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.RemoteUpdateContainmentFeature;
import org.eclipse.emf.compare.diff.metamodel.RemoteUpdateUniqueReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.RemoveAttribute;
import org.eclipse.emf.compare.diff.metamodel.RemoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.RemoveResourceDependency;
import org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChange;
import org.eclipse.emf.compare.diff.metamodel.ResourceDiff;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateContainmentFeature;
import org.eclipse.emf.compare.diff.metamodel.UpdateModelElement;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue;
import org.eclipse.emf.compare.match.metamodel.MatchPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * @generated
 */
public class DiffPackageImpl extends EPackageImpl implements DiffPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractDiffExtensionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass resourceDiffEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass resourceDependencyChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass addResourceDependencyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass removeResourceDependencyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass remoteAddResourceDependencyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass remoteRemoveResourceDependencyEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass addAttributeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass addModelElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass addReferenceValueEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass attributeChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass attributeChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass attributeChangeRightTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass conflictingDiffElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diffElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum differenceKindEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diffGroupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass comparisonSnapshotEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass comparisonResourceSnapshotEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass comparisonResourceSetSnapshotEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diffModelEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diffResourceSetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType iMergerEDataType = null;

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
	private EClass modelElementChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass modelElementChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass modelElementChangeRightTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass moveModelElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass updateContainmentFeatureEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass referenceChangeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass referenceChangeLeftTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass referenceChangeRightTargetEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass remoteAddAttributeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass remoteAddModelElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass remoteAddReferenceValueEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass remoteMoveModelElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass remoteUpdateContainmentFeatureEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass remoteRemoveAttributeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass remoteRemoveModelElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass remoteRemoveReferenceValueEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass remoteUpdateAttributeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass remoteUpdateUniqueReferenceValueEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass removeAttributeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass removeModelElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass removeReferenceValueEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass updateAttributeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass updateModelElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass updateReferenceEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass updateUniqueReferenceValueEClass = null;

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
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DiffPackageImpl() {
		super(eNS_URI, DiffFactory.eINSTANCE);
	}

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it
	 * depends. Simple dependencies are satisfied by calling this method on all dependent packages before
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
	public static DiffPackage init() {
		if (isInited)
			return (DiffPackage)EPackage.Registry.INSTANCE.getEPackage(DiffPackage.eNS_URI);

		// Obtain or create and register package
		DiffPackageImpl theDiffPackage = (DiffPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof DiffPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(eNS_URI)
				: new DiffPackageImpl());

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
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		diffModelEClass = createEClass(DIFF_MODEL);
		createEReference(diffModelEClass, DIFF_MODEL__OWNED_ELEMENTS);
		createEReference(diffModelEClass, DIFF_MODEL__LEFT_ROOTS);
		createEReference(diffModelEClass, DIFF_MODEL__RIGHT_ROOTS);
		createEReference(diffModelEClass, DIFF_MODEL__ANCESTOR_ROOTS);

		diffResourceSetEClass = createEClass(DIFF_RESOURCE_SET);
		createEReference(diffResourceSetEClass, DIFF_RESOURCE_SET__DIFF_MODELS);
		createEReference(diffResourceSetEClass, DIFF_RESOURCE_SET__RESOURCE_DIFFS);

		diffElementEClass = createEClass(DIFF_ELEMENT);
		createEReference(diffElementEClass, DIFF_ELEMENT__SUB_DIFF_ELEMENTS);
		createEReference(diffElementEClass, DIFF_ELEMENT__IS_HIDDEN_BY);
		createEAttribute(diffElementEClass, DIFF_ELEMENT__CONFLICTING);
		createEAttribute(diffElementEClass, DIFF_ELEMENT__KIND);

		conflictingDiffElementEClass = createEClass(CONFLICTING_DIFF_ELEMENT);
		createEReference(conflictingDiffElementEClass, CONFLICTING_DIFF_ELEMENT__LEFT_PARENT);
		createEReference(conflictingDiffElementEClass, CONFLICTING_DIFF_ELEMENT__RIGHT_PARENT);
		createEReference(conflictingDiffElementEClass, CONFLICTING_DIFF_ELEMENT__ORIGIN_ELEMENT);

		diffGroupEClass = createEClass(DIFF_GROUP);
		createEReference(diffGroupEClass, DIFF_GROUP__RIGHT_PARENT);
		createEAttribute(diffGroupEClass, DIFF_GROUP__SUBCHANGES);

		comparisonSnapshotEClass = createEClass(COMPARISON_SNAPSHOT);
		createEAttribute(comparisonSnapshotEClass, COMPARISON_SNAPSHOT__DATE);

		comparisonResourceSnapshotEClass = createEClass(COMPARISON_RESOURCE_SNAPSHOT);
		createEReference(comparisonResourceSnapshotEClass, COMPARISON_RESOURCE_SNAPSHOT__DIFF);
		createEReference(comparisonResourceSnapshotEClass, COMPARISON_RESOURCE_SNAPSHOT__MATCH);

		comparisonResourceSetSnapshotEClass = createEClass(COMPARISON_RESOURCE_SET_SNAPSHOT);
		createEReference(comparisonResourceSetSnapshotEClass,
				COMPARISON_RESOURCE_SET_SNAPSHOT__DIFF_RESOURCE_SET);
		createEReference(comparisonResourceSetSnapshotEClass,
				COMPARISON_RESOURCE_SET_SNAPSHOT__MATCH_RESOURCE_SET);

		modelElementChangeEClass = createEClass(MODEL_ELEMENT_CHANGE);

		modelElementChangeLeftTargetEClass = createEClass(MODEL_ELEMENT_CHANGE_LEFT_TARGET);
		createEReference(modelElementChangeLeftTargetEClass, MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT);
		createEReference(modelElementChangeLeftTargetEClass, MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT);

		modelElementChangeRightTargetEClass = createEClass(MODEL_ELEMENT_CHANGE_RIGHT_TARGET);
		createEReference(modelElementChangeRightTargetEClass, MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT);
		createEReference(modelElementChangeRightTargetEClass,
				MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT);

		addModelElementEClass = createEClass(ADD_MODEL_ELEMENT);

		remoteAddModelElementEClass = createEClass(REMOTE_ADD_MODEL_ELEMENT);

		removeModelElementEClass = createEClass(REMOVE_MODEL_ELEMENT);

		remoteRemoveModelElementEClass = createEClass(REMOTE_REMOVE_MODEL_ELEMENT);

		updateModelElementEClass = createEClass(UPDATE_MODEL_ELEMENT);
		createEReference(updateModelElementEClass, UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT);
		createEReference(updateModelElementEClass, UPDATE_MODEL_ELEMENT__LEFT_ELEMENT);

		moveModelElementEClass = createEClass(MOVE_MODEL_ELEMENT);
		createEReference(moveModelElementEClass, MOVE_MODEL_ELEMENT__LEFT_TARGET);
		createEReference(moveModelElementEClass, MOVE_MODEL_ELEMENT__RIGHT_TARGET);

		updateContainmentFeatureEClass = createEClass(UPDATE_CONTAINMENT_FEATURE);

		remoteMoveModelElementEClass = createEClass(REMOTE_MOVE_MODEL_ELEMENT);

		remoteUpdateContainmentFeatureEClass = createEClass(REMOTE_UPDATE_CONTAINMENT_FEATURE);

		attributeChangeEClass = createEClass(ATTRIBUTE_CHANGE);
		createEReference(attributeChangeEClass, ATTRIBUTE_CHANGE__ATTRIBUTE);
		createEReference(attributeChangeEClass, ATTRIBUTE_CHANGE__LEFT_ELEMENT);
		createEReference(attributeChangeEClass, ATTRIBUTE_CHANGE__RIGHT_ELEMENT);

		attributeChangeLeftTargetEClass = createEClass(ATTRIBUTE_CHANGE_LEFT_TARGET);
		createEAttribute(attributeChangeLeftTargetEClass, ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_TARGET);

		attributeChangeRightTargetEClass = createEClass(ATTRIBUTE_CHANGE_RIGHT_TARGET);
		createEAttribute(attributeChangeRightTargetEClass, ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_TARGET);

		addAttributeEClass = createEClass(ADD_ATTRIBUTE);

		remoteAddAttributeEClass = createEClass(REMOTE_ADD_ATTRIBUTE);

		removeAttributeEClass = createEClass(REMOVE_ATTRIBUTE);

		remoteRemoveAttributeEClass = createEClass(REMOTE_REMOVE_ATTRIBUTE);

		updateAttributeEClass = createEClass(UPDATE_ATTRIBUTE);

		remoteUpdateAttributeEClass = createEClass(REMOTE_UPDATE_ATTRIBUTE);

		referenceChangeEClass = createEClass(REFERENCE_CHANGE);
		createEReference(referenceChangeEClass, REFERENCE_CHANGE__REFERENCE);
		createEReference(referenceChangeEClass, REFERENCE_CHANGE__RIGHT_ELEMENT);
		createEReference(referenceChangeEClass, REFERENCE_CHANGE__LEFT_ELEMENT);

		referenceChangeLeftTargetEClass = createEClass(REFERENCE_CHANGE_LEFT_TARGET);
		createEReference(referenceChangeLeftTargetEClass, REFERENCE_CHANGE_LEFT_TARGET__LEFT_TARGET);
		createEReference(referenceChangeLeftTargetEClass, REFERENCE_CHANGE_LEFT_TARGET__RIGHT_TARGET);

		referenceChangeRightTargetEClass = createEClass(REFERENCE_CHANGE_RIGHT_TARGET);
		createEReference(referenceChangeRightTargetEClass, REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_TARGET);
		createEReference(referenceChangeRightTargetEClass, REFERENCE_CHANGE_RIGHT_TARGET__LEFT_TARGET);

		addReferenceValueEClass = createEClass(ADD_REFERENCE_VALUE);

		remoteAddReferenceValueEClass = createEClass(REMOTE_ADD_REFERENCE_VALUE);

		removeReferenceValueEClass = createEClass(REMOVE_REFERENCE_VALUE);

		remoteRemoveReferenceValueEClass = createEClass(REMOTE_REMOVE_REFERENCE_VALUE);

		updateReferenceEClass = createEClass(UPDATE_REFERENCE);

		updateUniqueReferenceValueEClass = createEClass(UPDATE_UNIQUE_REFERENCE_VALUE);
		createEReference(updateUniqueReferenceValueEClass, UPDATE_UNIQUE_REFERENCE_VALUE__LEFT_TARGET);
		createEReference(updateUniqueReferenceValueEClass, UPDATE_UNIQUE_REFERENCE_VALUE__RIGHT_TARGET);

		remoteUpdateUniqueReferenceValueEClass = createEClass(REMOTE_UPDATE_UNIQUE_REFERENCE_VALUE);

		abstractDiffExtensionEClass = createEClass(ABSTRACT_DIFF_EXTENSION);
		createEReference(abstractDiffExtensionEClass, ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS);
		createEAttribute(abstractDiffExtensionEClass, ABSTRACT_DIFF_EXTENSION__IS_COLLAPSED);

		resourceDiffEClass = createEClass(RESOURCE_DIFF);

		resourceDependencyChangeEClass = createEClass(RESOURCE_DEPENDENCY_CHANGE);
		createEReference(resourceDependencyChangeEClass, RESOURCE_DEPENDENCY_CHANGE__ROOTS);

		addResourceDependencyEClass = createEClass(ADD_RESOURCE_DEPENDENCY);

		removeResourceDependencyEClass = createEClass(REMOVE_RESOURCE_DEPENDENCY);

		remoteAddResourceDependencyEClass = createEClass(REMOTE_ADD_RESOURCE_DEPENDENCY);

		remoteRemoveResourceDependencyEClass = createEClass(REMOTE_REMOVE_RESOURCE_DEPENDENCY);

		// Create enums
		differenceKindEEnum = createEEnum(DIFFERENCE_KIND);

		// Create data types
		iMergerEDataType = createEDataType(IMERGER);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAbstractDiffExtension() {
		return abstractDiffExtensionEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAbstractDiffExtension_HideElements() {
		return (EReference)abstractDiffExtensionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAbstractDiffExtension_IsCollapsed() {
		return (EAttribute)abstractDiffExtensionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getResourceDiff() {
		return resourceDiffEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getResourceDependencyChange() {
		return resourceDependencyChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getResourceDependencyChange_Roots() {
		return (EReference)resourceDependencyChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAddResourceDependency() {
		return addResourceDependencyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoveResourceDependency() {
		return removeResourceDependencyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoteAddResourceDependency() {
		return remoteAddResourceDependencyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoteRemoveResourceDependency() {
		return remoteRemoveResourceDependencyEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAddAttribute() {
		return addAttributeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAddModelElement() {
		return addModelElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAddReferenceValue() {
		return addReferenceValueEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAttributeChange() {
		return attributeChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAttributeChange_Attribute() {
		return (EReference)attributeChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAttributeChange_LeftElement() {
		return (EReference)attributeChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAttributeChange_RightElement() {
		return (EReference)attributeChangeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAttributeChangeLeftTarget() {
		return attributeChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAttributeChangeLeftTarget_LeftTarget() {
		return (EAttribute)attributeChangeLeftTargetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAttributeChangeRightTarget() {
		return attributeChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAttributeChangeRightTarget_RightTarget() {
		return (EAttribute)attributeChangeRightTargetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConflictingDiffElement() {
		return conflictingDiffElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConflictingDiffElement_LeftParent() {
		return (EReference)conflictingDiffElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConflictingDiffElement_OriginElement() {
		return (EReference)conflictingDiffElementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConflictingDiffElement_RightParent() {
		return (EReference)conflictingDiffElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiffElement() {
		return diffElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDiffElement_Conflicting() {
		return (EAttribute)diffElementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiffElement_IsHiddenBy() {
		return (EReference)diffElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDiffElement_Kind() {
		return (EAttribute)diffElementEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiffElement_SubDiffElements() {
		return (EReference)diffElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getDifferenceKind() {
		return differenceKindEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DiffFactory getDiffFactory() {
		return (DiffFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiffGroup() {
		return diffGroupEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiffGroup_RightParent() {
		return (EReference)diffGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
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
	public EClass getComparisonSnapshot() {
		return comparisonSnapshotEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getComparisonSnapshot_Date() {
		return (EAttribute)comparisonSnapshotEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getComparisonResourceSnapshot() {
		return comparisonResourceSnapshotEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getComparisonResourceSnapshot_Diff() {
		return (EReference)comparisonResourceSnapshotEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getComparisonResourceSnapshot_Match() {
		return (EReference)comparisonResourceSnapshotEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getComparisonResourceSetSnapshot() {
		return comparisonResourceSetSnapshotEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getComparisonResourceSetSnapshot_DiffResourceSet() {
		return (EReference)comparisonResourceSetSnapshotEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getComparisonResourceSetSnapshot_MatchResourceSet() {
		return (EReference)comparisonResourceSetSnapshotEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiffModel() {
		return diffModelEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiffModel_OwnedElements() {
		return (EReference)diffModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiffModel_LeftRoots() {
		return (EReference)diffModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiffModel_RightRoots() {
		return (EReference)diffModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiffModel_AncestorRoots() {
		return (EReference)diffModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiffResourceSet() {
		return diffResourceSetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiffResourceSet_DiffModels() {
		return (EReference)diffResourceSetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiffResourceSet_ResourceDiffs() {
		return (EReference)diffResourceSetEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getIMerger() {
		return iMergerEDataType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getModelElementChange() {
		return modelElementChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getModelElementChangeLeftTarget() {
		return modelElementChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getModelElementChangeLeftTarget_LeftElement() {
		return (EReference)modelElementChangeLeftTargetEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getModelElementChangeLeftTarget_RightParent() {
		return (EReference)modelElementChangeLeftTargetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getModelElementChangeRightTarget() {
		return modelElementChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getModelElementChangeRightTarget_LeftParent() {
		return (EReference)modelElementChangeRightTargetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getModelElementChangeRightTarget_RightElement() {
		return (EReference)modelElementChangeRightTargetEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMoveModelElement() {
		return moveModelElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMoveModelElement_LeftTarget() {
		return (EReference)moveModelElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMoveModelElement_RightTarget() {
		return (EReference)moveModelElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUpdateContainmentFeature() {
		return updateContainmentFeatureEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getReferenceChange() {
		return referenceChangeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getReferenceChange_LeftElement() {
		return (EReference)referenceChangeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getReferenceChange_Reference() {
		return (EReference)referenceChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getReferenceChange_RightElement() {
		return (EReference)referenceChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getReferenceChangeLeftTarget() {
		return referenceChangeLeftTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getReferenceChangeLeftTarget_LeftTarget() {
		return (EReference)referenceChangeLeftTargetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getReferenceChangeLeftTarget_RightTarget() {
		return (EReference)referenceChangeLeftTargetEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getReferenceChangeRightTarget() {
		return referenceChangeRightTargetEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getReferenceChangeRightTarget_RightTarget() {
		return (EReference)referenceChangeRightTargetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getReferenceChangeRightTarget_LeftTarget() {
		return (EReference)referenceChangeRightTargetEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoteAddAttribute() {
		return remoteAddAttributeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoteAddModelElement() {
		return remoteAddModelElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoteAddReferenceValue() {
		return remoteAddReferenceValueEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoteMoveModelElement() {
		return remoteMoveModelElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoteUpdateContainmentFeature() {
		return remoteUpdateContainmentFeatureEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoteRemoveAttribute() {
		return remoteRemoveAttributeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoteRemoveModelElement() {
		return remoteRemoveModelElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoteRemoveReferenceValue() {
		return remoteRemoveReferenceValueEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoteUpdateAttribute() {
		return remoteUpdateAttributeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoteUpdateUniqueReferenceValue() {
		return remoteUpdateUniqueReferenceValueEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoveAttribute() {
		return removeAttributeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoveModelElement() {
		return removeModelElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoveReferenceValue() {
		return removeReferenceValueEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUpdateAttribute() {
		return updateAttributeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUpdateModelElement() {
		return updateModelElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUpdateModelElement_LeftElement() {
		return (EReference)updateModelElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUpdateModelElement_RightElement() {
		return (EReference)updateModelElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUpdateReference() {
		return updateReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUpdateUniqueReferenceValue() {
		return updateUniqueReferenceValueEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUpdateUniqueReferenceValue_LeftTarget() {
		return (EReference)updateUniqueReferenceValueEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUpdateUniqueReferenceValue_RightTarget() {
		return (EReference)updateUniqueReferenceValueEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
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
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE
				.getEPackage(EcorePackage.eNS_URI);
		MatchPackage theMatchPackage = (MatchPackage)EPackage.Registry.INSTANCE
				.getEPackage(MatchPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		conflictingDiffElementEClass.getESuperTypes().add(this.getDiffElement());
		diffGroupEClass.getESuperTypes().add(this.getDiffElement());
		comparisonResourceSnapshotEClass.getESuperTypes().add(this.getComparisonSnapshot());
		comparisonResourceSetSnapshotEClass.getESuperTypes().add(this.getComparisonSnapshot());
		modelElementChangeEClass.getESuperTypes().add(this.getDiffElement());
		modelElementChangeLeftTargetEClass.getESuperTypes().add(this.getModelElementChange());
		modelElementChangeRightTargetEClass.getESuperTypes().add(this.getModelElementChange());
		addModelElementEClass.getESuperTypes().add(this.getModelElementChangeLeftTarget());
		remoteAddModelElementEClass.getESuperTypes().add(this.getModelElementChangeRightTarget());
		removeModelElementEClass.getESuperTypes().add(this.getModelElementChangeRightTarget());
		remoteRemoveModelElementEClass.getESuperTypes().add(this.getModelElementChangeLeftTarget());
		updateModelElementEClass.getESuperTypes().add(this.getModelElementChange());
		moveModelElementEClass.getESuperTypes().add(this.getUpdateModelElement());
		updateContainmentFeatureEClass.getESuperTypes().add(this.getMoveModelElement());
		remoteMoveModelElementEClass.getESuperTypes().add(this.getMoveModelElement());
		remoteUpdateContainmentFeatureEClass.getESuperTypes().add(this.getUpdateContainmentFeature());
		attributeChangeEClass.getESuperTypes().add(this.getDiffElement());
		attributeChangeLeftTargetEClass.getESuperTypes().add(this.getAttributeChange());
		attributeChangeRightTargetEClass.getESuperTypes().add(this.getAttributeChange());
		addAttributeEClass.getESuperTypes().add(this.getAttributeChangeLeftTarget());
		remoteAddAttributeEClass.getESuperTypes().add(this.getAttributeChangeRightTarget());
		removeAttributeEClass.getESuperTypes().add(this.getAttributeChangeRightTarget());
		remoteRemoveAttributeEClass.getESuperTypes().add(this.getAttributeChangeLeftTarget());
		updateAttributeEClass.getESuperTypes().add(this.getAttributeChange());
		remoteUpdateAttributeEClass.getESuperTypes().add(this.getUpdateAttribute());
		referenceChangeEClass.getESuperTypes().add(this.getDiffElement());
		referenceChangeLeftTargetEClass.getESuperTypes().add(this.getReferenceChange());
		referenceChangeRightTargetEClass.getESuperTypes().add(this.getReferenceChange());
		addReferenceValueEClass.getESuperTypes().add(this.getReferenceChangeLeftTarget());
		remoteAddReferenceValueEClass.getESuperTypes().add(this.getReferenceChangeRightTarget());
		removeReferenceValueEClass.getESuperTypes().add(this.getReferenceChangeRightTarget());
		remoteRemoveReferenceValueEClass.getESuperTypes().add(this.getReferenceChangeLeftTarget());
		updateReferenceEClass.getESuperTypes().add(this.getReferenceChange());
		updateUniqueReferenceValueEClass.getESuperTypes().add(this.getUpdateReference());
		remoteUpdateUniqueReferenceValueEClass.getESuperTypes().add(this.getUpdateUniqueReferenceValue());
		resourceDiffEClass.getESuperTypes().add(this.getDiffElement());
		resourceDependencyChangeEClass.getESuperTypes().add(this.getResourceDiff());
		addResourceDependencyEClass.getESuperTypes().add(this.getResourceDependencyChange());
		removeResourceDependencyEClass.getESuperTypes().add(this.getResourceDependencyChange());
		remoteAddResourceDependencyEClass.getESuperTypes().add(this.getResourceDependencyChange());
		remoteRemoveResourceDependencyEClass.getESuperTypes().add(this.getResourceDependencyChange());

		// Initialize classes and features; add operations and parameters
		initEClass(diffModelEClass, DiffModel.class,
				"DiffModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getDiffModel_OwnedElements(),
				this.getDiffElement(),
				null,
				"ownedElements", null, 0, -1, DiffModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getDiffModel_LeftRoots(),
				theEcorePackage.getEObject(),
				null,
				"leftRoots", null, 0, -1, DiffModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getDiffModel_RightRoots(),
				theEcorePackage.getEObject(),
				null,
				"rightRoots", null, 0, -1, DiffModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getDiffModel_AncestorRoots(),
				theEcorePackage.getEObject(),
				null,
				"ancestorRoots", null, 0, -1, DiffModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(diffResourceSetEClass, DiffResourceSet.class,
				"DiffResourceSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getDiffResourceSet_DiffModels(),
				this.getDiffModel(),
				null,
				"diffModels", null, 0, -1, DiffResourceSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getDiffResourceSet_ResourceDiffs(),
				this.getResourceDiff(),
				null,
				"resourceDiffs", null, 0, -1, DiffResourceSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(diffElementEClass, DiffElement.class,
				"DiffElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getDiffElement_SubDiffElements(),
				this.getDiffElement(),
				null,
				"subDiffElements", null, 0, -1, DiffElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getDiffElement_IsHiddenBy(),
				this.getAbstractDiffExtension(),
				this.getAbstractDiffExtension_HideElements(),
				"isHiddenBy", null, 0, -1, DiffElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(
				getDiffElement_Conflicting(),
				theEcorePackage.getEBoolean(),
				"conflicting", null, 0, 1, DiffElement.class, IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(
				getDiffElement_Kind(),
				this.getDifferenceKind(),
				"kind", "", 0, 1, DiffElement.class, IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

		initEClass(conflictingDiffElementEClass, ConflictingDiffElement.class,
				"ConflictingDiffElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getConflictingDiffElement_LeftParent(),
				ecorePackage.getEObject(),
				null,
				"leftParent", null, 0, 1, ConflictingDiffElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getConflictingDiffElement_RightParent(),
				ecorePackage.getEObject(),
				null,
				"rightParent", null, 0, 1, ConflictingDiffElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getConflictingDiffElement_OriginElement(),
				theEcorePackage.getEObject(),
				null,
				"originElement", null, 0, 1, ConflictingDiffElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(diffGroupEClass, DiffGroup.class,
				"DiffGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getDiffGroup_RightParent(),
				ecorePackage.getEObject(),
				null,
				"rightParent", null, 0, 1, DiffGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(
				getDiffGroup_Subchanges(),
				ecorePackage.getEInt(),
				"subchanges", null, 0, 1, DiffGroup.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(comparisonSnapshotEClass, ComparisonSnapshot.class,
				"ComparisonSnapshot", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(
				getComparisonSnapshot_Date(),
				ecorePackage.getEDate(),
				"date", null, 0, 1, ComparisonSnapshot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(comparisonResourceSnapshotEClass, ComparisonResourceSnapshot.class,
				"ComparisonResourceSnapshot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getComparisonResourceSnapshot_Diff(),
				this.getDiffModel(),
				null,
				"diff", null, 0, 1, ComparisonResourceSnapshot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getComparisonResourceSnapshot_Match(),
				theMatchPackage.getMatchModel(),
				null,
				"match", null, 0, 1, ComparisonResourceSnapshot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(comparisonResourceSetSnapshotEClass, ComparisonResourceSetSnapshot.class,
				"ComparisonResourceSetSnapshot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getComparisonResourceSetSnapshot_DiffResourceSet(),
				this.getDiffResourceSet(),
				null,
				"diffResourceSet", null, 0, 1, ComparisonResourceSetSnapshot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getComparisonResourceSetSnapshot_MatchResourceSet(),
				theMatchPackage.getMatchResourceSet(),
				null,
				"matchResourceSet", null, 0, 1, ComparisonResourceSetSnapshot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(modelElementChangeEClass, ModelElementChange.class,
				"ModelElementChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(modelElementChangeLeftTargetEClass, ModelElementChangeLeftTarget.class,
				"ModelElementChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getModelElementChangeLeftTarget_RightParent(),
				ecorePackage.getEObject(),
				null,
				"rightParent", null, 0, 1, ModelElementChangeLeftTarget.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getModelElementChangeLeftTarget_LeftElement(),
				ecorePackage.getEObject(),
				null,
				"leftElement", null, 0, 1, ModelElementChangeLeftTarget.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(modelElementChangeRightTargetEClass, ModelElementChangeRightTarget.class,
				"ModelElementChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getModelElementChangeRightTarget_LeftParent(),
				ecorePackage.getEObject(),
				null,
				"leftParent", null, 0, 1, ModelElementChangeRightTarget.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getModelElementChangeRightTarget_RightElement(),
				ecorePackage.getEObject(),
				null,
				"rightElement", null, 0, 1, ModelElementChangeRightTarget.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(addModelElementEClass, AddModelElement.class,
				"AddModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(remoteAddModelElementEClass, RemoteAddModelElement.class,
				"RemoteAddModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(removeModelElementEClass, RemoveModelElement.class,
				"RemoveModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(remoteRemoveModelElementEClass, RemoteRemoveModelElement.class,
				"RemoteRemoveModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(updateModelElementEClass, UpdateModelElement.class,
				"UpdateModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getUpdateModelElement_RightElement(),
				ecorePackage.getEObject(),
				null,
				"rightElement", null, 0, 1, UpdateModelElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getUpdateModelElement_LeftElement(),
				ecorePackage.getEObject(),
				null,
				"leftElement", null, 0, 1, UpdateModelElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(moveModelElementEClass, MoveModelElement.class,
				"MoveModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getMoveModelElement_LeftTarget(),
				ecorePackage.getEObject(),
				null,
				"leftTarget", null, 0, 1, MoveModelElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getMoveModelElement_RightTarget(),
				ecorePackage.getEObject(),
				null,
				"rightTarget", null, 0, 1, MoveModelElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(updateContainmentFeatureEClass, UpdateContainmentFeature.class,
				"UpdateContainmentFeature", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(remoteMoveModelElementEClass, RemoteMoveModelElement.class,
				"RemoteMoveModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(remoteUpdateContainmentFeatureEClass, RemoteUpdateContainmentFeature.class,
				"RemoteUpdateContainmentFeature", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(attributeChangeEClass, AttributeChange.class,
				"AttributeChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getAttributeChange_Attribute(),
				theEcorePackage.getEAttribute(),
				null,
				"attribute", null, 0, 1, AttributeChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getAttributeChange_LeftElement(),
				ecorePackage.getEObject(),
				null,
				"leftElement", null, 0, 1, AttributeChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getAttributeChange_RightElement(),
				ecorePackage.getEObject(),
				null,
				"rightElement", null, 0, 1, AttributeChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(attributeChangeLeftTargetEClass, AttributeChangeLeftTarget.class,
				"AttributeChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(
				getAttributeChangeLeftTarget_LeftTarget(),
				theEcorePackage.getEJavaObject(),
				"leftTarget", null, 0, 1, AttributeChangeLeftTarget.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(attributeChangeRightTargetEClass, AttributeChangeRightTarget.class,
				"AttributeChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(
				getAttributeChangeRightTarget_RightTarget(),
				theEcorePackage.getEJavaObject(),
				"rightTarget", null, 0, 1, AttributeChangeRightTarget.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(addAttributeEClass, AddAttribute.class,
				"AddAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(remoteAddAttributeEClass, RemoteAddAttribute.class,
				"RemoteAddAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(removeAttributeEClass, RemoveAttribute.class,
				"RemoveAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(remoteRemoveAttributeEClass, RemoteRemoveAttribute.class,
				"RemoteRemoveAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(updateAttributeEClass, UpdateAttribute.class,
				"UpdateAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(remoteUpdateAttributeEClass, RemoteUpdateAttribute.class,
				"RemoteUpdateAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(referenceChangeEClass, ReferenceChange.class,
				"ReferenceChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getReferenceChange_Reference(),
				theEcorePackage.getEReference(),
				null,
				"reference", null, 0, 1, ReferenceChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getReferenceChange_RightElement(),
				ecorePackage.getEObject(),
				null,
				"rightElement", null, 0, 1, ReferenceChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getReferenceChange_LeftElement(),
				ecorePackage.getEObject(),
				null,
				"leftElement", null, 0, 1, ReferenceChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(referenceChangeLeftTargetEClass, ReferenceChangeLeftTarget.class,
				"ReferenceChangeLeftTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getReferenceChangeLeftTarget_LeftTarget(),
				ecorePackage.getEObject(),
				null,
				"leftTarget", null, 0, 1, ReferenceChangeLeftTarget.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getReferenceChangeLeftTarget_RightTarget(),
				ecorePackage.getEObject(),
				null,
				"rightTarget", null, 0, 1, ReferenceChangeLeftTarget.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(referenceChangeRightTargetEClass, ReferenceChangeRightTarget.class,
				"ReferenceChangeRightTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getReferenceChangeRightTarget_RightTarget(),
				ecorePackage.getEObject(),
				null,
				"rightTarget", null, 0, 1, ReferenceChangeRightTarget.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getReferenceChangeRightTarget_LeftTarget(),
				ecorePackage.getEObject(),
				null,
				"leftTarget", null, 0, 1, ReferenceChangeRightTarget.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(addReferenceValueEClass, AddReferenceValue.class,
				"AddReferenceValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(remoteAddReferenceValueEClass, RemoteAddReferenceValue.class,
				"RemoteAddReferenceValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(removeReferenceValueEClass, RemoveReferenceValue.class,
				"RemoveReferenceValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(remoteRemoveReferenceValueEClass, RemoteRemoveReferenceValue.class,
				"RemoteRemoveReferenceValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(updateReferenceEClass, UpdateReference.class,
				"UpdateReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(updateUniqueReferenceValueEClass, UpdateUniqueReferenceValue.class,
				"UpdateUniqueReferenceValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getUpdateUniqueReferenceValue_LeftTarget(),
				ecorePackage.getEObject(),
				null,
				"leftTarget", null, 0, 1, UpdateUniqueReferenceValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getUpdateUniqueReferenceValue_RightTarget(),
				ecorePackage.getEObject(),
				null,
				"rightTarget", null, 0, 1, UpdateUniqueReferenceValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(remoteUpdateUniqueReferenceValueEClass, RemoteUpdateUniqueReferenceValue.class,
				"RemoteUpdateUniqueReferenceValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(abstractDiffExtensionEClass, AbstractDiffExtension.class,
				"AbstractDiffExtension", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getAbstractDiffExtension_HideElements(),
				this.getDiffElement(),
				this.getDiffElement_IsHiddenBy(),
				"hideElements", null, 0, -1, AbstractDiffExtension.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(
				getAbstractDiffExtension_IsCollapsed(),
				ecorePackage.getEBoolean(),
				"isCollapsed", "false", 0, 1, AbstractDiffExtension.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

		EOperation op = addEOperation(abstractDiffExtensionEClass, null, "visit", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, this.getDiffModel(), "diffModel", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(abstractDiffExtensionEClass, theEcorePackage.getEString(),
				"getText", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(abstractDiffExtensionEClass, theEcorePackage.getEJavaObject(),
				"getImage", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(abstractDiffExtensionEClass, this.getIMerger(),
				"provideMerger", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(resourceDiffEClass, ResourceDiff.class,
				"ResourceDiff", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(resourceDependencyChangeEClass, ResourceDependencyChange.class,
				"ResourceDependencyChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getResourceDependencyChange_Roots(),
				theEcorePackage.getEObject(),
				null,
				"roots", null, 0, -1, ResourceDependencyChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(addResourceDependencyEClass, AddResourceDependency.class,
				"AddResourceDependency", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(removeResourceDependencyEClass, RemoveResourceDependency.class,
				"RemoveResourceDependency", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(remoteAddResourceDependencyEClass, RemoteAddResourceDependency.class,
				"RemoteAddResourceDependency", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(remoteRemoveResourceDependencyEClass, RemoteRemoveResourceDependency.class,
				"RemoteRemoveResourceDependency", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		// Initialize enums and add enum literals
		initEEnum(differenceKindEEnum, DifferenceKind.class, "DifferenceKind"); //$NON-NLS-1$
		addEEnumLiteral(differenceKindEEnum, DifferenceKind.ADDITION);
		addEEnumLiteral(differenceKindEEnum, DifferenceKind.DELETION);
		addEEnumLiteral(differenceKindEEnum, DifferenceKind.CHANGE);
		addEEnumLiteral(differenceKindEEnum, DifferenceKind.MOVE);
		addEEnumLiteral(differenceKindEEnum, DifferenceKind.CONFLICT);

		// Initialize data types
		initEDataType(iMergerEDataType, IMerger.class,
				"IMerger", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		// Create resource
		createResource(eNS_URI);
	}

} // DiffPackageImpl
