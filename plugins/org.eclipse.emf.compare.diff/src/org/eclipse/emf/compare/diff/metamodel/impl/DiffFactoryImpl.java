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
import org.eclipse.emf.compare.diff.metamodel.*;
import org.eclipse.emf.compare.diff.metamodel.AddAttribute;
import org.eclipse.emf.compare.diff.metamodel.AddModelElement;
import org.eclipse.emf.compare.diff.metamodel.AddReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.AttributeChange;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
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
import org.eclipse.emf.compare.diff.metamodel.RemoteMoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.RemoteRemoveAttribute;
import org.eclipse.emf.compare.diff.metamodel.RemoteRemoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.RemoteRemoveReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.RemoteUpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.RemoteUpdateContainmentFeature;
import org.eclipse.emf.compare.diff.metamodel.RemoteUpdateUniqueReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.RemoveAttribute;
import org.eclipse.emf.compare.diff.metamodel.RemoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateContainmentFeature;
import org.eclipse.emf.compare.diff.metamodel.UpdateModelElement;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * @generated
 */
public class DiffFactoryImpl extends EFactoryImpl implements DiffFactory {
	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DiffFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DiffPackage getPackage() {
		return DiffPackage.eINSTANCE;
	}

	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static DiffFactory init() {
		try {
			DiffFactory theDiffFactory = (DiffFactory)EPackage.Registry.INSTANCE
					.getEFactory("http://www.eclipse.org/emf/compare/diff/1.1"); //$NON-NLS-1$ 
			if (theDiffFactory != null) {
				return theDiffFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DiffFactoryImpl();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unused")
	public String convertDifferenceKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIMergerToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case DiffPackage.DIFFERENCE_KIND:
				return convertDifferenceKindToString(eDataType, instanceValue);
			case DiffPackage.IMERGER:
				return convertIMergerToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException(
						"The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case DiffPackage.DIFF_MODEL:
				return createDiffModel();
			case DiffPackage.DIFF_RESOURCE_SET:
				return createDiffResourceSet();
			case DiffPackage.CONFLICTING_DIFF_ELEMENT:
				return createConflictingDiffElement();
			case DiffPackage.DIFF_GROUP:
				return createDiffGroup();
			case DiffPackage.COMPARISON_RESOURCE_SNAPSHOT:
				return createComparisonResourceSnapshot();
			case DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT:
				return createComparisonResourceSetSnapshot();
			case DiffPackage.MODEL_ELEMENT_CHANGE:
				return createModelElementChange();
			case DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET:
				return createModelElementChangeLeftTarget();
			case DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET:
				return createModelElementChangeRightTarget();
			case DiffPackage.ADD_MODEL_ELEMENT:
				return createAddModelElement();
			case DiffPackage.REMOTE_ADD_MODEL_ELEMENT:
				return createRemoteAddModelElement();
			case DiffPackage.REMOVE_MODEL_ELEMENT:
				return createRemoveModelElement();
			case DiffPackage.REMOTE_REMOVE_MODEL_ELEMENT:
				return createRemoteRemoveModelElement();
			case DiffPackage.UPDATE_MODEL_ELEMENT:
				return createUpdateModelElement();
			case DiffPackage.MOVE_MODEL_ELEMENT:
				return createMoveModelElement();
			case DiffPackage.UPDATE_CONTAINMENT_FEATURE:
				return createUpdateContainmentFeature();
			case DiffPackage.REMOTE_MOVE_MODEL_ELEMENT:
				return createRemoteMoveModelElement();
			case DiffPackage.REMOTE_UPDATE_CONTAINMENT_FEATURE:
				return createRemoteUpdateContainmentFeature();
			case DiffPackage.ATTRIBUTE_CHANGE:
				return createAttributeChange();
			case DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET:
				return createAttributeChangeLeftTarget();
			case DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET:
				return createAttributeChangeRightTarget();
			case DiffPackage.ADD_ATTRIBUTE:
				return createAddAttribute();
			case DiffPackage.REMOTE_ADD_ATTRIBUTE:
				return createRemoteAddAttribute();
			case DiffPackage.REMOVE_ATTRIBUTE:
				return createRemoveAttribute();
			case DiffPackage.REMOTE_REMOVE_ATTRIBUTE:
				return createRemoteRemoveAttribute();
			case DiffPackage.UPDATE_ATTRIBUTE:
				return createUpdateAttribute();
			case DiffPackage.REMOTE_UPDATE_ATTRIBUTE:
				return createRemoteUpdateAttribute();
			case DiffPackage.REFERENCE_CHANGE:
				return createReferenceChange();
			case DiffPackage.REFERENCE_CHANGE_LEFT_TARGET:
				return createReferenceChangeLeftTarget();
			case DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET:
				return createReferenceChangeRightTarget();
			case DiffPackage.ADD_REFERENCE_VALUE:
				return createAddReferenceValue();
			case DiffPackage.REMOTE_ADD_REFERENCE_VALUE:
				return createRemoteAddReferenceValue();
			case DiffPackage.REMOVE_REFERENCE_VALUE:
				return createRemoveReferenceValue();
			case DiffPackage.REMOTE_REMOVE_REFERENCE_VALUE:
				return createRemoteRemoveReferenceValue();
			case DiffPackage.UPDATE_REFERENCE:
				return createUpdateReference();
			case DiffPackage.UPDATE_UNIQUE_REFERENCE_VALUE:
				return createUpdateUniqueReferenceValue();
			case DiffPackage.REMOTE_UPDATE_UNIQUE_REFERENCE_VALUE:
				return createRemoteUpdateUniqueReferenceValue();
			case DiffPackage.RESOURCE_DIFF:
				return createResourceDiff();
			case DiffPackage.RESOURCE_DEPENDENCY_CHANGE:
				return createResourceDependencyChange();
			case DiffPackage.ADD_RESOURCE_DEPENDENCY:
				return createAddResourceDependency();
			case DiffPackage.REMOVE_RESOURCE_DEPENDENCY:
				return createRemoveResourceDependency();
			case DiffPackage.REMOTE_ADD_RESOURCE_DEPENDENCY:
				return createRemoteAddResourceDependency();
			case DiffPackage.REMOTE_REMOVE_RESOURCE_DEPENDENCY:
				return createRemoteRemoveResourceDependency();
			default:
				throw new IllegalArgumentException(
						"The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AddAttribute createAddAttribute() {
		AddAttributeImpl addAttribute = new AddAttributeImpl();
		return addAttribute;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AddModelElement createAddModelElement() {
		AddModelElementImpl addModelElement = new AddModelElementImpl();
		return addModelElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AddReferenceValue createAddReferenceValue() {
		AddReferenceValueImpl addReferenceValue = new AddReferenceValueImpl();
		return addReferenceValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AttributeChange createAttributeChange() {
		AttributeChangeImpl attributeChange = new AttributeChangeImpl();
		return attributeChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AttributeChangeLeftTarget createAttributeChangeLeftTarget() {
		AttributeChangeLeftTargetImpl attributeChangeLeftTarget = new AttributeChangeLeftTargetImpl();
		return attributeChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AttributeChangeRightTarget createAttributeChangeRightTarget() {
		AttributeChangeRightTargetImpl attributeChangeRightTarget = new AttributeChangeRightTargetImpl();
		return attributeChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ConflictingDiffElement createConflictingDiffElement() {
		ConflictingDiffElementImpl conflictingDiffElement = new ConflictingDiffElementImpl();
		return conflictingDiffElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DifferenceKind createDifferenceKindFromString(EDataType eDataType, String initialValue) {
		DifferenceKind result = DifferenceKind.get(initialValue);
		if (result == null)
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DiffGroup createDiffGroup() {
		DiffGroupImpl diffGroup = new DiffGroupImpl();
		return diffGroup;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ComparisonResourceSnapshot createComparisonResourceSnapshot() {
		ComparisonResourceSnapshotImpl comparisonResourceSnapshot = new ComparisonResourceSnapshotImpl();
		return comparisonResourceSnapshot;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ComparisonResourceSetSnapshot createComparisonResourceSetSnapshot() {
		ComparisonResourceSetSnapshotImpl comparisonResourceSetSnapshot = new ComparisonResourceSetSnapshotImpl();
		return comparisonResourceSetSnapshot;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DiffModel createDiffModel() {
		DiffModelImpl diffModel = new DiffModelImpl();
		return diffModel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DiffResourceSet createDiffResourceSet() {
		DiffResourceSetImpl diffResourceSet = new DiffResourceSetImpl();
		return diffResourceSet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case DiffPackage.DIFFERENCE_KIND:
				return createDifferenceKindFromString(eDataType, initialValue);
			case DiffPackage.IMERGER:
				return createIMergerFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException(
						"The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public IMerger createIMergerFromString(EDataType eDataType, String initialValue) {
		return (IMerger)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ModelElementChange createModelElementChange() {
		ModelElementChangeImpl modelElementChange = new ModelElementChangeImpl();
		return modelElementChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ModelElementChangeLeftTarget createModelElementChangeLeftTarget() {
		ModelElementChangeLeftTargetImpl modelElementChangeLeftTarget = new ModelElementChangeLeftTargetImpl();
		return modelElementChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ModelElementChangeRightTarget createModelElementChangeRightTarget() {
		ModelElementChangeRightTargetImpl modelElementChangeRightTarget = new ModelElementChangeRightTargetImpl();
		return modelElementChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public MoveModelElement createMoveModelElement() {
		MoveModelElementImpl moveModelElement = new MoveModelElementImpl();
		return moveModelElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UpdateContainmentFeature createUpdateContainmentFeature() {
		UpdateContainmentFeatureImpl updateContainmentFeature = new UpdateContainmentFeatureImpl();
		return updateContainmentFeature;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ReferenceChange createReferenceChange() {
		ReferenceChangeImpl referenceChange = new ReferenceChangeImpl();
		return referenceChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ReferenceChangeLeftTarget createReferenceChangeLeftTarget() {
		ReferenceChangeLeftTargetImpl referenceChangeLeftTarget = new ReferenceChangeLeftTargetImpl();
		return referenceChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ReferenceChangeRightTarget createReferenceChangeRightTarget() {
		ReferenceChangeRightTargetImpl referenceChangeRightTarget = new ReferenceChangeRightTargetImpl();
		return referenceChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public RemoteAddAttribute createRemoteAddAttribute() {
		RemoteAddAttributeImpl remoteAddAttribute = new RemoteAddAttributeImpl();
		return remoteAddAttribute;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public RemoteAddModelElement createRemoteAddModelElement() {
		RemoteAddModelElementImpl remoteAddModelElement = new RemoteAddModelElementImpl();
		return remoteAddModelElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public RemoteAddReferenceValue createRemoteAddReferenceValue() {
		RemoteAddReferenceValueImpl remoteAddReferenceValue = new RemoteAddReferenceValueImpl();
		return remoteAddReferenceValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public RemoteMoveModelElement createRemoteMoveModelElement() {
		RemoteMoveModelElementImpl remoteMoveModelElement = new RemoteMoveModelElementImpl();
		return remoteMoveModelElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public RemoteUpdateContainmentFeature createRemoteUpdateContainmentFeature() {
		RemoteUpdateContainmentFeatureImpl remoteUpdateContainmentFeature = new RemoteUpdateContainmentFeatureImpl();
		return remoteUpdateContainmentFeature;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public RemoteRemoveAttribute createRemoteRemoveAttribute() {
		RemoteRemoveAttributeImpl remoteRemoveAttribute = new RemoteRemoveAttributeImpl();
		return remoteRemoveAttribute;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public RemoteRemoveModelElement createRemoteRemoveModelElement() {
		RemoteRemoveModelElementImpl remoteRemoveModelElement = new RemoteRemoveModelElementImpl();
		return remoteRemoveModelElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public RemoteRemoveReferenceValue createRemoteRemoveReferenceValue() {
		RemoteRemoveReferenceValueImpl remoteRemoveReferenceValue = new RemoteRemoveReferenceValueImpl();
		return remoteRemoveReferenceValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public RemoteUpdateAttribute createRemoteUpdateAttribute() {
		RemoteUpdateAttributeImpl remoteUpdateAttribute = new RemoteUpdateAttributeImpl();
		return remoteUpdateAttribute;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public RemoteUpdateUniqueReferenceValue createRemoteUpdateUniqueReferenceValue() {
		RemoteUpdateUniqueReferenceValueImpl remoteUpdateUniqueReferenceValue = new RemoteUpdateUniqueReferenceValueImpl();
		return remoteUpdateUniqueReferenceValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceDiff createResourceDiff() {
		ResourceDiffImpl resourceDiff = new ResourceDiffImpl();
		return resourceDiff;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceDependencyChange createResourceDependencyChange() {
		ResourceDependencyChangeImpl resourceDependencyChange = new ResourceDependencyChangeImpl();
		return resourceDependencyChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AddResourceDependency createAddResourceDependency() {
		AddResourceDependencyImpl addResourceDependency = new AddResourceDependencyImpl();
		return addResourceDependency;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RemoveResourceDependency createRemoveResourceDependency() {
		RemoveResourceDependencyImpl removeResourceDependency = new RemoveResourceDependencyImpl();
		return removeResourceDependency;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RemoteAddResourceDependency createRemoteAddResourceDependency() {
		RemoteAddResourceDependencyImpl remoteAddResourceDependency = new RemoteAddResourceDependencyImpl();
		return remoteAddResourceDependency;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RemoteRemoveResourceDependency createRemoteRemoveResourceDependency() {
		RemoteRemoveResourceDependencyImpl remoteRemoveResourceDependency = new RemoteRemoveResourceDependencyImpl();
		return remoteRemoveResourceDependency;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public RemoveAttribute createRemoveAttribute() {
		RemoveAttributeImpl removeAttribute = new RemoveAttributeImpl();
		return removeAttribute;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public RemoveModelElement createRemoveModelElement() {
		RemoveModelElementImpl removeModelElement = new RemoveModelElementImpl();
		return removeModelElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public RemoveReferenceValue createRemoveReferenceValue() {
		RemoveReferenceValueImpl removeReferenceValue = new RemoveReferenceValueImpl();
		return removeReferenceValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UpdateAttribute createUpdateAttribute() {
		UpdateAttributeImpl updateAttribute = new UpdateAttributeImpl();
		return updateAttribute;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UpdateModelElement createUpdateModelElement() {
		UpdateModelElementImpl updateModelElement = new UpdateModelElementImpl();
		return updateModelElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UpdateReference createUpdateReference() {
		UpdateReferenceImpl updateReference = new UpdateReferenceImpl();
		return updateReference;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UpdateUniqueReferenceValue createUpdateUniqueReferenceValue() {
		UpdateUniqueReferenceValueImpl updateUniqueReferenceValue = new UpdateUniqueReferenceValueImpl();
		return updateUniqueReferenceValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DiffPackage getDiffPackage() {
		return (DiffPackage)getEPackage();
	}

} // DiffFactoryImpl
