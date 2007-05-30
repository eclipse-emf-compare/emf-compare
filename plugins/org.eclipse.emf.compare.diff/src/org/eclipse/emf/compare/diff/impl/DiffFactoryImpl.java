/*  
 * Copyright (c) 2006, Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.diff.impl;

import org.eclipse.emf.compare.diff.*;

import org.eclipse.emf.compare.diff.AddAttribute;
import org.eclipse.emf.compare.diff.AddModelElement;
import org.eclipse.emf.compare.diff.AddReferenceValue;
import org.eclipse.emf.compare.diff.AttributeChange;
import org.eclipse.emf.compare.diff.DiffFactory;
import org.eclipse.emf.compare.diff.DiffModel;
import org.eclipse.emf.compare.diff.DiffPackage;
import org.eclipse.emf.compare.diff.ModelElementChange;
import org.eclipse.emf.compare.diff.ReferenceChange;
import org.eclipse.emf.compare.diff.RemoveAttribute;
import org.eclipse.emf.compare.diff.RemoveModelElement;
import org.eclipse.emf.compare.diff.RemoveReferenceValue;
import org.eclipse.emf.compare.diff.UpdateAttribute;
import org.eclipse.emf.compare.diff.UpdateModelElement;
import org.eclipse.emf.compare.diff.UpdateReference;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DiffFactoryImpl extends EFactoryImpl implements DiffFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DiffFactory init() {
		try {
			DiffFactory theDiffFactory = (DiffFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/emf/compare/diff/1.0"); 
			if (theDiffFactory != null) {
				return theDiffFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DiffFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiffFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case DiffPackage.DIFF_MODEL: return createDiffModel();
			case DiffPackage.DIFF_GROUP: return createDiffGroup();
			case DiffPackage.ATTRIBUTE_CHANGE: return createAttributeChange();
			case DiffPackage.REFERENCE_CHANGE: return createReferenceChange();
			case DiffPackage.MODEL_ELEMENT_CHANGE: return createModelElementChange();
			case DiffPackage.ADD_MODEL_ELEMENT: return createAddModelElement();
			case DiffPackage.REMOVE_MODEL_ELEMENT: return createRemoveModelElement();
			case DiffPackage.UPDATE_MODEL_ELEMENT: return createUpdateModelElement();
			case DiffPackage.MOVE_MODEL_ELEMENT: return createMoveModelElement();
			case DiffPackage.ADD_ATTRIBUTE: return createAddAttribute();
			case DiffPackage.REMOVE_ATTRIBUTE: return createRemoveAttribute();
			case DiffPackage.UPDATE_ATTRIBUTE: return createUpdateAttribute();
			case DiffPackage.ADD_REFERENCE_VALUE: return createAddReferenceValue();
			case DiffPackage.REMOVE_REFERENCE_VALUE: return createRemoveReferenceValue();
			case DiffPackage.UPDATE_REFERENCE: return createUpdateReference();
			case DiffPackage.MODEL_INPUT_SNAPSHOT: return createModelInputSnapshot();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiffModel createDiffModel() {
		DiffModelImpl diffModel = new DiffModelImpl();
		return diffModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiffGroup createDiffGroup() {
		DiffGroupImpl diffGroup = new DiffGroupImpl();
		return diffGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AttributeChange createAttributeChange() {
		AttributeChangeImpl attributeChange = new AttributeChangeImpl();
		return attributeChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ReferenceChange createReferenceChange() {
		ReferenceChangeImpl referenceChange = new ReferenceChangeImpl();
		return referenceChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelElementChange createModelElementChange() {
		ModelElementChangeImpl modelElementChange = new ModelElementChangeImpl();
		return modelElementChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AddModelElement createAddModelElement() {
		AddModelElementImpl addModelElement = new AddModelElementImpl();
		return addModelElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RemoveModelElement createRemoveModelElement() {
		RemoveModelElementImpl removeModelElement = new RemoveModelElementImpl();
		return removeModelElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UpdateModelElement createUpdateModelElement() {
		UpdateModelElementImpl updateModelElement = new UpdateModelElementImpl();
		return updateModelElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MoveModelElement createMoveModelElement() {
		MoveModelElementImpl moveModelElement = new MoveModelElementImpl();
		return moveModelElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AddAttribute createAddAttribute() {
		AddAttributeImpl addAttribute = new AddAttributeImpl();
		return addAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RemoveAttribute createRemoveAttribute() {
		RemoveAttributeImpl removeAttribute = new RemoveAttributeImpl();
		return removeAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UpdateAttribute createUpdateAttribute() {
		UpdateAttributeImpl updateAttribute = new UpdateAttributeImpl();
		return updateAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AddReferenceValue createAddReferenceValue() {
		AddReferenceValueImpl addReferenceValue = new AddReferenceValueImpl();
		return addReferenceValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RemoveReferenceValue createRemoveReferenceValue() {
		RemoveReferenceValueImpl removeReferenceValue = new RemoveReferenceValueImpl();
		return removeReferenceValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UpdateReference createUpdateReference() {
		UpdateReferenceImpl updateReference = new UpdateReferenceImpl();
		return updateReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelInputSnapshot createModelInputSnapshot() {
		ModelInputSnapshotImpl modelInputSnapshot = new ModelInputSnapshotImpl();
		return modelInputSnapshot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiffPackage getDiffPackage() {
		return (DiffPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static DiffPackage getPackage() {
		return DiffPackage.eINSTANCE;
	}

} //DiffFactoryImpl
