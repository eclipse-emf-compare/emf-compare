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

import org.eclipse.emf.compare.uml2diff.*;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffPackage;
import org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLAssociationChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLAssociationChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLDependencyChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLDependencyChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLExtendChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLExtendChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationAddition;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationRemoval;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeLeftTarget;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeRightTarget;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceOrderChange;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateAttribute;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateReference;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * @generated
 */
public class UML2DiffFactoryImpl extends EFactoryImpl implements UML2DiffFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static UML2DiffFactory init() {
		try {
			UML2DiffFactory theUML2DiffFactory = (UML2DiffFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/emf/compare/diff/uml2/1.0"); 
			if (theUML2DiffFactory != null) {
				return theUML2DiffFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new UML2DiffFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UML2DiffFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case UML2DiffPackage.UML_ASSOCIATION_CHANGE_LEFT_TARGET: return createUMLAssociationChangeLeftTarget();
			case UML2DiffPackage.UML_ASSOCIATION_CHANGE_RIGHT_TARGET: return createUMLAssociationChangeRightTarget();
			case UML2DiffPackage.UML_ASSOCIATION_BRANCH_CHANGE_LEFT_TARGET: return createUMLAssociationBranchChangeLeftTarget();
			case UML2DiffPackage.UML_ASSOCIATION_BRANCH_CHANGE_RIGHT_TARGET: return createUMLAssociationBranchChangeRightTarget();
			case UML2DiffPackage.UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET: return createUMLDependencyBranchChangeLeftTarget();
			case UML2DiffPackage.UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET: return createUMLDependencyBranchChangeRightTarget();
			case UML2DiffPackage.UML_GENERALIZATION_SET_CHANGE_LEFT_TARGET: return createUMLGeneralizationSetChangeLeftTarget();
			case UML2DiffPackage.UML_GENERALIZATION_SET_CHANGE_RIGHT_TARGET: return createUMLGeneralizationSetChangeRightTarget();
			case UML2DiffPackage.UML_DEPENDENCY_CHANGE_LEFT_TARGET: return createUMLDependencyChangeLeftTarget();
			case UML2DiffPackage.UML_DEPENDENCY_CHANGE_RIGHT_TARGET: return createUMLDependencyChangeRightTarget();
			case UML2DiffPackage.UML_EXTEND_CHANGE_LEFT_TARGET: return createUMLExtendChangeLeftTarget();
			case UML2DiffPackage.UML_EXTEND_CHANGE_RIGHT_TARGET: return createUMLExtendChangeRightTarget();
			case UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_LEFT_TARGET: return createUMLExecutionSpecificationChangeLeftTarget();
			case UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET: return createUMLExecutionSpecificationChangeRightTarget();
			case UML2DiffPackage.UML_DESTRUCTION_EVENT_CHANGE_LEFT_TARGET: return createUMLDestructionEventChangeLeftTarget();
			case UML2DiffPackage.UML_DESTRUCTION_EVENT_CHANGE_RIGHT_TARGET: return createUMLDestructionEventChangeRightTarget();
			case UML2DiffPackage.UML_INTERVAL_CONSTRAINT_CHANGE_LEFT_TARGET: return createUMLIntervalConstraintChangeLeftTarget();
			case UML2DiffPackage.UML_INTERVAL_CONSTRAINT_CHANGE_RIGHT_TARGET: return createUMLIntervalConstraintChangeRightTarget();
			case UML2DiffPackage.UML_MESSAGE_CHANGE_LEFT_TARGET: return createUMLMessageChangeLeftTarget();
			case UML2DiffPackage.UML_MESSAGE_CHANGE_RIGHT_TARGET: return createUMLMessageChangeRightTarget();
			case UML2DiffPackage.UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET: return createUMLStereotypeAttributeChangeLeftTarget();
			case UML2DiffPackage.UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET: return createUMLStereotypeAttributeChangeRightTarget();
			case UML2DiffPackage.UML_STEREOTYPE_UPDATE_ATTRIBUTE: return createUMLStereotypeUpdateAttribute();
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_ADDITION: return createUMLStereotypeApplicationAddition();
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL: return createUMLStereotypeApplicationRemoval();
			case UML2DiffPackage.UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET: return createUMLStereotypeReferenceChangeLeftTarget();
			case UML2DiffPackage.UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET: return createUMLStereotypeReferenceChangeRightTarget();
			case UML2DiffPackage.UML_STEREOTYPE_UPDATE_REFERENCE: return createUMLStereotypeUpdateReference();
			case UML2DiffPackage.UML_STEREOTYPE_REFERENCE_ORDER_CHANGE: return createUMLStereotypeReferenceOrderChange();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UMLAssociationChangeLeftTarget createUMLAssociationChangeLeftTarget() {
		UMLAssociationChangeLeftTargetImpl umlAssociationChangeLeftTarget = new UMLAssociationChangeLeftTargetImpl();
		return umlAssociationChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UMLAssociationChangeRightTarget createUMLAssociationChangeRightTarget() {
		UMLAssociationChangeRightTargetImpl umlAssociationChangeRightTarget = new UMLAssociationChangeRightTargetImpl();
		return umlAssociationChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLAssociationBranchChangeLeftTarget createUMLAssociationBranchChangeLeftTarget() {
		UMLAssociationBranchChangeLeftTargetImpl umlAssociationBranchChangeLeftTarget = new UMLAssociationBranchChangeLeftTargetImpl();
		return umlAssociationBranchChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLAssociationBranchChangeRightTarget createUMLAssociationBranchChangeRightTarget() {
		UMLAssociationBranchChangeRightTargetImpl umlAssociationBranchChangeRightTarget = new UMLAssociationBranchChangeRightTargetImpl();
		return umlAssociationBranchChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLDependencyBranchChangeLeftTarget createUMLDependencyBranchChangeLeftTarget() {
		UMLDependencyBranchChangeLeftTargetImpl umlDependencyBranchChangeLeftTarget = new UMLDependencyBranchChangeLeftTargetImpl();
		return umlDependencyBranchChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLDependencyBranchChangeRightTarget createUMLDependencyBranchChangeRightTarget() {
		UMLDependencyBranchChangeRightTargetImpl umlDependencyBranchChangeRightTarget = new UMLDependencyBranchChangeRightTargetImpl();
		return umlDependencyBranchChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLGeneralizationSetChangeLeftTarget createUMLGeneralizationSetChangeLeftTarget() {
		UMLGeneralizationSetChangeLeftTargetImpl umlGeneralizationSetChangeLeftTarget = new UMLGeneralizationSetChangeLeftTargetImpl();
		return umlGeneralizationSetChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLGeneralizationSetChangeRightTarget createUMLGeneralizationSetChangeRightTarget() {
		UMLGeneralizationSetChangeRightTargetImpl umlGeneralizationSetChangeRightTarget = new UMLGeneralizationSetChangeRightTargetImpl();
		return umlGeneralizationSetChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLDependencyChangeLeftTarget createUMLDependencyChangeLeftTarget() {
		UMLDependencyChangeLeftTargetImpl umlDependencyChangeLeftTarget = new UMLDependencyChangeLeftTargetImpl();
		return umlDependencyChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLDependencyChangeRightTarget createUMLDependencyChangeRightTarget() {
		UMLDependencyChangeRightTargetImpl umlDependencyChangeRightTarget = new UMLDependencyChangeRightTargetImpl();
		return umlDependencyChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLExtendChangeLeftTarget createUMLExtendChangeLeftTarget() {
		UMLExtendChangeLeftTargetImpl umlExtendChangeLeftTarget = new UMLExtendChangeLeftTargetImpl();
		return umlExtendChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLExtendChangeRightTarget createUMLExtendChangeRightTarget() {
		UMLExtendChangeRightTargetImpl umlExtendChangeRightTarget = new UMLExtendChangeRightTargetImpl();
		return umlExtendChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLExecutionSpecificationChangeLeftTarget createUMLExecutionSpecificationChangeLeftTarget() {
		UMLExecutionSpecificationChangeLeftTargetImpl umlExecutionSpecificationChangeLeftTarget = new UMLExecutionSpecificationChangeLeftTargetImpl();
		return umlExecutionSpecificationChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLExecutionSpecificationChangeRightTarget createUMLExecutionSpecificationChangeRightTarget() {
		UMLExecutionSpecificationChangeRightTargetImpl umlExecutionSpecificationChangeRightTarget = new UMLExecutionSpecificationChangeRightTargetImpl();
		return umlExecutionSpecificationChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLDestructionEventChangeLeftTarget createUMLDestructionEventChangeLeftTarget() {
		UMLDestructionEventChangeLeftTargetImpl umlDestructionEventChangeLeftTarget = new UMLDestructionEventChangeLeftTargetImpl();
		return umlDestructionEventChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLDestructionEventChangeRightTarget createUMLDestructionEventChangeRightTarget() {
		UMLDestructionEventChangeRightTargetImpl umlDestructionEventChangeRightTarget = new UMLDestructionEventChangeRightTargetImpl();
		return umlDestructionEventChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLIntervalConstraintChangeLeftTarget createUMLIntervalConstraintChangeLeftTarget() {
		UMLIntervalConstraintChangeLeftTargetImpl umlIntervalConstraintChangeLeftTarget = new UMLIntervalConstraintChangeLeftTargetImpl();
		return umlIntervalConstraintChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLIntervalConstraintChangeRightTarget createUMLIntervalConstraintChangeRightTarget() {
		UMLIntervalConstraintChangeRightTargetImpl umlIntervalConstraintChangeRightTarget = new UMLIntervalConstraintChangeRightTargetImpl();
		return umlIntervalConstraintChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLMessageChangeLeftTarget createUMLMessageChangeLeftTarget() {
		UMLMessageChangeLeftTargetImpl umlMessageChangeLeftTarget = new UMLMessageChangeLeftTargetImpl();
		return umlMessageChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLMessageChangeRightTarget createUMLMessageChangeRightTarget() {
		UMLMessageChangeRightTargetImpl umlMessageChangeRightTarget = new UMLMessageChangeRightTargetImpl();
		return umlMessageChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UMLStereotypeAttributeChangeLeftTarget createUMLStereotypeAttributeChangeLeftTarget() {
		UMLStereotypeAttributeChangeLeftTargetImpl umlStereotypeAttributeChangeLeftTarget = new UMLStereotypeAttributeChangeLeftTargetImpl();
		return umlStereotypeAttributeChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UMLStereotypeAttributeChangeRightTarget createUMLStereotypeAttributeChangeRightTarget() {
		UMLStereotypeAttributeChangeRightTargetImpl umlStereotypeAttributeChangeRightTarget = new UMLStereotypeAttributeChangeRightTargetImpl();
		return umlStereotypeAttributeChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UMLStereotypeUpdateAttribute createUMLStereotypeUpdateAttribute() {
		UMLStereotypeUpdateAttributeImpl umlStereotypeUpdateAttribute = new UMLStereotypeUpdateAttributeImpl();
		return umlStereotypeUpdateAttribute;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UMLStereotypeApplicationAddition createUMLStereotypeApplicationAddition() {
		UMLStereotypeApplicationAdditionImpl umlStereotypeApplicationAddition = new UMLStereotypeApplicationAdditionImpl();
		return umlStereotypeApplicationAddition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UMLStereotypeApplicationRemoval createUMLStereotypeApplicationRemoval() {
		UMLStereotypeApplicationRemovalImpl umlStereotypeApplicationRemoval = new UMLStereotypeApplicationRemovalImpl();
		return umlStereotypeApplicationRemoval;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UMLStereotypeReferenceChangeLeftTarget createUMLStereotypeReferenceChangeLeftTarget() {
		UMLStereotypeReferenceChangeLeftTargetImpl umlStereotypeReferenceChangeLeftTarget = new UMLStereotypeReferenceChangeLeftTargetImpl();
		return umlStereotypeReferenceChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UMLStereotypeReferenceChangeRightTarget createUMLStereotypeReferenceChangeRightTarget() {
		UMLStereotypeReferenceChangeRightTargetImpl umlStereotypeReferenceChangeRightTarget = new UMLStereotypeReferenceChangeRightTargetImpl();
		return umlStereotypeReferenceChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UMLStereotypeUpdateReference createUMLStereotypeUpdateReference() {
		UMLStereotypeUpdateReferenceImpl umlStereotypeUpdateReference = new UMLStereotypeUpdateReferenceImpl();
		return umlStereotypeUpdateReference;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UMLStereotypeReferenceOrderChange createUMLStereotypeReferenceOrderChange() {
		UMLStereotypeReferenceOrderChangeImpl umlStereotypeReferenceOrderChange = new UMLStereotypeReferenceOrderChangeImpl();
		return umlStereotypeReferenceOrderChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UML2DiffPackage getUML2DiffPackage() {
		return (UML2DiffPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static UML2DiffPackage getPackage() {
		return UML2DiffPackage.eINSTANCE;
	}

} // UML2DiffFactoryImpl
