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
package org.eclipse.emf.compare.uml2diff.util;

import java.util.List;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.AttributeChange;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChange;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateModelElement;

import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.compare.uml2diff.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.uml2diff.UML2DiffPackage
 * @generated
 */
public class UML2DiffSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static UML2DiffPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UML2DiffSwitch() {
		if (modelPackage == null) {
			modelPackage = UML2DiffPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case UML2DiffPackage.UML_DIFF_EXTENSION: {
				UMLDiffExtension umlDiffExtension = (UMLDiffExtension)theEObject;
				T result = caseUMLDiffExtension(umlDiffExtension);
				if (result == null) result = caseDiffElement(umlDiffExtension);
				if (result == null) result = caseAbstractDiffExtension(umlDiffExtension);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_ASSOCIATION_CHANGE: {
				UMLAssociationChange umlAssociationChange = (UMLAssociationChange)theEObject;
				T result = caseUMLAssociationChange(umlAssociationChange);
				if (result == null) result = caseUMLDiffExtension(umlAssociationChange);
				if (result == null) result = caseDiffElement(umlAssociationChange);
				if (result == null) result = caseAbstractDiffExtension(umlAssociationChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_ASSOCIATION_CHANGE_LEFT_TARGET: {
				UMLAssociationChangeLeftTarget umlAssociationChangeLeftTarget = (UMLAssociationChangeLeftTarget)theEObject;
				T result = caseUMLAssociationChangeLeftTarget(umlAssociationChangeLeftTarget);
				if (result == null) result = caseModelElementChangeLeftTarget(umlAssociationChangeLeftTarget);
				if (result == null) result = caseUMLAssociationChange(umlAssociationChangeLeftTarget);
				if (result == null) result = caseModelElementChange(umlAssociationChangeLeftTarget);
				if (result == null) result = caseUMLDiffExtension(umlAssociationChangeLeftTarget);
				if (result == null) result = caseDiffElement(umlAssociationChangeLeftTarget);
				if (result == null) result = caseAbstractDiffExtension(umlAssociationChangeLeftTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_ASSOCIATION_CHANGE_RIGHT_TARGET: {
				UMLAssociationChangeRightTarget umlAssociationChangeRightTarget = (UMLAssociationChangeRightTarget)theEObject;
				T result = caseUMLAssociationChangeRightTarget(umlAssociationChangeRightTarget);
				if (result == null) result = caseModelElementChangeRightTarget(umlAssociationChangeRightTarget);
				if (result == null) result = caseUMLAssociationChange(umlAssociationChangeRightTarget);
				if (result == null) result = caseModelElementChange(umlAssociationChangeRightTarget);
				if (result == null) result = caseUMLDiffExtension(umlAssociationChangeRightTarget);
				if (result == null) result = caseDiffElement(umlAssociationChangeRightTarget);
				if (result == null) result = caseAbstractDiffExtension(umlAssociationChangeRightTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_ASSOCIATION_BRANCH_CHANGE: {
				UMLAssociationBranchChange umlAssociationBranchChange = (UMLAssociationBranchChange)theEObject;
				T result = caseUMLAssociationBranchChange(umlAssociationBranchChange);
				if (result == null) result = caseUMLDiffExtension(umlAssociationBranchChange);
				if (result == null) result = caseDiffElement(umlAssociationBranchChange);
				if (result == null) result = caseAbstractDiffExtension(umlAssociationBranchChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_ASSOCIATION_BRANCH_CHANGE_LEFT_TARGET: {
				UMLAssociationBranchChangeLeftTarget umlAssociationBranchChangeLeftTarget = (UMLAssociationBranchChangeLeftTarget)theEObject;
				T result = caseUMLAssociationBranchChangeLeftTarget(umlAssociationBranchChangeLeftTarget);
				if (result == null) result = caseModelElementChangeLeftTarget(umlAssociationBranchChangeLeftTarget);
				if (result == null) result = caseUMLAssociationBranchChange(umlAssociationBranchChangeLeftTarget);
				if (result == null) result = caseModelElementChange(umlAssociationBranchChangeLeftTarget);
				if (result == null) result = caseUMLDiffExtension(umlAssociationBranchChangeLeftTarget);
				if (result == null) result = caseDiffElement(umlAssociationBranchChangeLeftTarget);
				if (result == null) result = caseAbstractDiffExtension(umlAssociationBranchChangeLeftTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_ASSOCIATION_BRANCH_CHANGE_RIGHT_TARGET: {
				UMLAssociationBranchChangeRightTarget umlAssociationBranchChangeRightTarget = (UMLAssociationBranchChangeRightTarget)theEObject;
				T result = caseUMLAssociationBranchChangeRightTarget(umlAssociationBranchChangeRightTarget);
				if (result == null) result = caseModelElementChangeRightTarget(umlAssociationBranchChangeRightTarget);
				if (result == null) result = caseUMLAssociationBranchChange(umlAssociationBranchChangeRightTarget);
				if (result == null) result = caseModelElementChange(umlAssociationBranchChangeRightTarget);
				if (result == null) result = caseUMLDiffExtension(umlAssociationBranchChangeRightTarget);
				if (result == null) result = caseDiffElement(umlAssociationBranchChangeRightTarget);
				if (result == null) result = caseAbstractDiffExtension(umlAssociationBranchChangeRightTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_DEPENDENCY_BRANCH_CHANGE: {
				UMLDependencyBranchChange umlDependencyBranchChange = (UMLDependencyBranchChange)theEObject;
				T result = caseUMLDependencyBranchChange(umlDependencyBranchChange);
				if (result == null) result = caseUMLDiffExtension(umlDependencyBranchChange);
				if (result == null) result = caseDiffElement(umlDependencyBranchChange);
				if (result == null) result = caseAbstractDiffExtension(umlDependencyBranchChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET: {
				UMLDependencyBranchChangeLeftTarget umlDependencyBranchChangeLeftTarget = (UMLDependencyBranchChangeLeftTarget)theEObject;
				T result = caseUMLDependencyBranchChangeLeftTarget(umlDependencyBranchChangeLeftTarget);
				if (result == null) result = caseReferenceChangeLeftTarget(umlDependencyBranchChangeLeftTarget);
				if (result == null) result = caseUMLDependencyBranchChange(umlDependencyBranchChangeLeftTarget);
				if (result == null) result = caseReferenceChange(umlDependencyBranchChangeLeftTarget);
				if (result == null) result = caseUMLDiffExtension(umlDependencyBranchChangeLeftTarget);
				if (result == null) result = caseDiffElement(umlDependencyBranchChangeLeftTarget);
				if (result == null) result = caseAbstractDiffExtension(umlDependencyBranchChangeLeftTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET: {
				UMLDependencyBranchChangeRightTarget umlDependencyBranchChangeRightTarget = (UMLDependencyBranchChangeRightTarget)theEObject;
				T result = caseUMLDependencyBranchChangeRightTarget(umlDependencyBranchChangeRightTarget);
				if (result == null) result = caseReferenceChangeRightTarget(umlDependencyBranchChangeRightTarget);
				if (result == null) result = caseUMLDependencyBranchChange(umlDependencyBranchChangeRightTarget);
				if (result == null) result = caseReferenceChange(umlDependencyBranchChangeRightTarget);
				if (result == null) result = caseUMLDiffExtension(umlDependencyBranchChangeRightTarget);
				if (result == null) result = caseDiffElement(umlDependencyBranchChangeRightTarget);
				if (result == null) result = caseAbstractDiffExtension(umlDependencyBranchChangeRightTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_GENERALIZATION_SET_CHANGE: {
				UMLGeneralizationSetChange umlGeneralizationSetChange = (UMLGeneralizationSetChange)theEObject;
				T result = caseUMLGeneralizationSetChange(umlGeneralizationSetChange);
				if (result == null) result = caseUMLDiffExtension(umlGeneralizationSetChange);
				if (result == null) result = caseDiffElement(umlGeneralizationSetChange);
				if (result == null) result = caseAbstractDiffExtension(umlGeneralizationSetChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_GENERALIZATION_SET_CHANGE_LEFT_TARGET: {
				UMLGeneralizationSetChangeLeftTarget umlGeneralizationSetChangeLeftTarget = (UMLGeneralizationSetChangeLeftTarget)theEObject;
				T result = caseUMLGeneralizationSetChangeLeftTarget(umlGeneralizationSetChangeLeftTarget);
				if (result == null) result = caseModelElementChangeLeftTarget(umlGeneralizationSetChangeLeftTarget);
				if (result == null) result = caseUMLGeneralizationSetChange(umlGeneralizationSetChangeLeftTarget);
				if (result == null) result = caseModelElementChange(umlGeneralizationSetChangeLeftTarget);
				if (result == null) result = caseUMLDiffExtension(umlGeneralizationSetChangeLeftTarget);
				if (result == null) result = caseDiffElement(umlGeneralizationSetChangeLeftTarget);
				if (result == null) result = caseAbstractDiffExtension(umlGeneralizationSetChangeLeftTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_GENERALIZATION_SET_CHANGE_RIGHT_TARGET: {
				UMLGeneralizationSetChangeRightTarget umlGeneralizationSetChangeRightTarget = (UMLGeneralizationSetChangeRightTarget)theEObject;
				T result = caseUMLGeneralizationSetChangeRightTarget(umlGeneralizationSetChangeRightTarget);
				if (result == null) result = caseModelElementChangeRightTarget(umlGeneralizationSetChangeRightTarget);
				if (result == null) result = caseUMLGeneralizationSetChange(umlGeneralizationSetChangeRightTarget);
				if (result == null) result = caseModelElementChange(umlGeneralizationSetChangeRightTarget);
				if (result == null) result = caseUMLDiffExtension(umlGeneralizationSetChangeRightTarget);
				if (result == null) result = caseDiffElement(umlGeneralizationSetChangeRightTarget);
				if (result == null) result = caseAbstractDiffExtension(umlGeneralizationSetChangeRightTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_DEPENDENCY_CHANGE: {
				UMLDependencyChange umlDependencyChange = (UMLDependencyChange)theEObject;
				T result = caseUMLDependencyChange(umlDependencyChange);
				if (result == null) result = caseUMLDiffExtension(umlDependencyChange);
				if (result == null) result = caseDiffElement(umlDependencyChange);
				if (result == null) result = caseAbstractDiffExtension(umlDependencyChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_DEPENDENCY_CHANGE_LEFT_TARGET: {
				UMLDependencyChangeLeftTarget umlDependencyChangeLeftTarget = (UMLDependencyChangeLeftTarget)theEObject;
				T result = caseUMLDependencyChangeLeftTarget(umlDependencyChangeLeftTarget);
				if (result == null) result = caseModelElementChangeLeftTarget(umlDependencyChangeLeftTarget);
				if (result == null) result = caseUMLDependencyChange(umlDependencyChangeLeftTarget);
				if (result == null) result = caseModelElementChange(umlDependencyChangeLeftTarget);
				if (result == null) result = caseUMLDiffExtension(umlDependencyChangeLeftTarget);
				if (result == null) result = caseDiffElement(umlDependencyChangeLeftTarget);
				if (result == null) result = caseAbstractDiffExtension(umlDependencyChangeLeftTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_DEPENDENCY_CHANGE_RIGHT_TARGET: {
				UMLDependencyChangeRightTarget umlDependencyChangeRightTarget = (UMLDependencyChangeRightTarget)theEObject;
				T result = caseUMLDependencyChangeRightTarget(umlDependencyChangeRightTarget);
				if (result == null) result = caseModelElementChangeRightTarget(umlDependencyChangeRightTarget);
				if (result == null) result = caseUMLDependencyChange(umlDependencyChangeRightTarget);
				if (result == null) result = caseModelElementChange(umlDependencyChangeRightTarget);
				if (result == null) result = caseUMLDiffExtension(umlDependencyChangeRightTarget);
				if (result == null) result = caseDiffElement(umlDependencyChangeRightTarget);
				if (result == null) result = caseAbstractDiffExtension(umlDependencyChangeRightTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_EXTEND_CHANGE: {
				UMLExtendChange umlExtendChange = (UMLExtendChange)theEObject;
				T result = caseUMLExtendChange(umlExtendChange);
				if (result == null) result = caseUMLDiffExtension(umlExtendChange);
				if (result == null) result = caseDiffElement(umlExtendChange);
				if (result == null) result = caseAbstractDiffExtension(umlExtendChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_EXTEND_CHANGE_LEFT_TARGET: {
				UMLExtendChangeLeftTarget umlExtendChangeLeftTarget = (UMLExtendChangeLeftTarget)theEObject;
				T result = caseUMLExtendChangeLeftTarget(umlExtendChangeLeftTarget);
				if (result == null) result = caseModelElementChangeLeftTarget(umlExtendChangeLeftTarget);
				if (result == null) result = caseUMLExtendChange(umlExtendChangeLeftTarget);
				if (result == null) result = caseModelElementChange(umlExtendChangeLeftTarget);
				if (result == null) result = caseUMLDiffExtension(umlExtendChangeLeftTarget);
				if (result == null) result = caseDiffElement(umlExtendChangeLeftTarget);
				if (result == null) result = caseAbstractDiffExtension(umlExtendChangeLeftTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_EXTEND_CHANGE_RIGHT_TARGET: {
				UMLExtendChangeRightTarget umlExtendChangeRightTarget = (UMLExtendChangeRightTarget)theEObject;
				T result = caseUMLExtendChangeRightTarget(umlExtendChangeRightTarget);
				if (result == null) result = caseModelElementChangeRightTarget(umlExtendChangeRightTarget);
				if (result == null) result = caseUMLExtendChange(umlExtendChangeRightTarget);
				if (result == null) result = caseModelElementChange(umlExtendChangeRightTarget);
				if (result == null) result = caseUMLDiffExtension(umlExtendChangeRightTarget);
				if (result == null) result = caseDiffElement(umlExtendChangeRightTarget);
				if (result == null) result = caseAbstractDiffExtension(umlExtendChangeRightTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE: {
				UMLExecutionSpecificationChange umlExecutionSpecificationChange = (UMLExecutionSpecificationChange)theEObject;
				T result = caseUMLExecutionSpecificationChange(umlExecutionSpecificationChange);
				if (result == null) result = caseUMLDiffExtension(umlExecutionSpecificationChange);
				if (result == null) result = caseDiffElement(umlExecutionSpecificationChange);
				if (result == null) result = caseAbstractDiffExtension(umlExecutionSpecificationChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_LEFT_TARGET: {
				UMLExecutionSpecificationChangeLeftTarget umlExecutionSpecificationChangeLeftTarget = (UMLExecutionSpecificationChangeLeftTarget)theEObject;
				T result = caseUMLExecutionSpecificationChangeLeftTarget(umlExecutionSpecificationChangeLeftTarget);
				if (result == null) result = caseModelElementChangeLeftTarget(umlExecutionSpecificationChangeLeftTarget);
				if (result == null) result = caseUMLExecutionSpecificationChange(umlExecutionSpecificationChangeLeftTarget);
				if (result == null) result = caseModelElementChange(umlExecutionSpecificationChangeLeftTarget);
				if (result == null) result = caseUMLDiffExtension(umlExecutionSpecificationChangeLeftTarget);
				if (result == null) result = caseDiffElement(umlExecutionSpecificationChangeLeftTarget);
				if (result == null) result = caseAbstractDiffExtension(umlExecutionSpecificationChangeLeftTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET: {
				UMLExecutionSpecificationChangeRightTarget umlExecutionSpecificationChangeRightTarget = (UMLExecutionSpecificationChangeRightTarget)theEObject;
				T result = caseUMLExecutionSpecificationChangeRightTarget(umlExecutionSpecificationChangeRightTarget);
				if (result == null) result = caseModelElementChangeRightTarget(umlExecutionSpecificationChangeRightTarget);
				if (result == null) result = caseUMLExecutionSpecificationChange(umlExecutionSpecificationChangeRightTarget);
				if (result == null) result = caseModelElementChange(umlExecutionSpecificationChangeRightTarget);
				if (result == null) result = caseUMLDiffExtension(umlExecutionSpecificationChangeRightTarget);
				if (result == null) result = caseDiffElement(umlExecutionSpecificationChangeRightTarget);
				if (result == null) result = caseAbstractDiffExtension(umlExecutionSpecificationChangeRightTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_DESTRUCTION_EVENT_CHANGE: {
				UMLDestructionEventChange umlDestructionEventChange = (UMLDestructionEventChange)theEObject;
				T result = caseUMLDestructionEventChange(umlDestructionEventChange);
				if (result == null) result = caseUMLDiffExtension(umlDestructionEventChange);
				if (result == null) result = caseDiffElement(umlDestructionEventChange);
				if (result == null) result = caseAbstractDiffExtension(umlDestructionEventChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_DESTRUCTION_EVENT_CHANGE_LEFT_TARGET: {
				UMLDestructionEventChangeLeftTarget umlDestructionEventChangeLeftTarget = (UMLDestructionEventChangeLeftTarget)theEObject;
				T result = caseUMLDestructionEventChangeLeftTarget(umlDestructionEventChangeLeftTarget);
				if (result == null) result = caseModelElementChangeLeftTarget(umlDestructionEventChangeLeftTarget);
				if (result == null) result = caseUMLDestructionEventChange(umlDestructionEventChangeLeftTarget);
				if (result == null) result = caseModelElementChange(umlDestructionEventChangeLeftTarget);
				if (result == null) result = caseUMLDiffExtension(umlDestructionEventChangeLeftTarget);
				if (result == null) result = caseDiffElement(umlDestructionEventChangeLeftTarget);
				if (result == null) result = caseAbstractDiffExtension(umlDestructionEventChangeLeftTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_DESTRUCTION_EVENT_CHANGE_RIGHT_TARGET: {
				UMLDestructionEventChangeRightTarget umlDestructionEventChangeRightTarget = (UMLDestructionEventChangeRightTarget)theEObject;
				T result = caseUMLDestructionEventChangeRightTarget(umlDestructionEventChangeRightTarget);
				if (result == null) result = caseModelElementChangeRightTarget(umlDestructionEventChangeRightTarget);
				if (result == null) result = caseUMLDestructionEventChange(umlDestructionEventChangeRightTarget);
				if (result == null) result = caseModelElementChange(umlDestructionEventChangeRightTarget);
				if (result == null) result = caseUMLDiffExtension(umlDestructionEventChangeRightTarget);
				if (result == null) result = caseDiffElement(umlDestructionEventChangeRightTarget);
				if (result == null) result = caseAbstractDiffExtension(umlDestructionEventChangeRightTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_INTERVAL_CONSTRAINT_CHANGE: {
				UMLIntervalConstraintChange umlIntervalConstraintChange = (UMLIntervalConstraintChange)theEObject;
				T result = caseUMLIntervalConstraintChange(umlIntervalConstraintChange);
				if (result == null) result = caseUMLDiffExtension(umlIntervalConstraintChange);
				if (result == null) result = caseDiffElement(umlIntervalConstraintChange);
				if (result == null) result = caseAbstractDiffExtension(umlIntervalConstraintChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_INTERVAL_CONSTRAINT_CHANGE_LEFT_TARGET: {
				UMLIntervalConstraintChangeLeftTarget umlIntervalConstraintChangeLeftTarget = (UMLIntervalConstraintChangeLeftTarget)theEObject;
				T result = caseUMLIntervalConstraintChangeLeftTarget(umlIntervalConstraintChangeLeftTarget);
				if (result == null) result = caseModelElementChangeLeftTarget(umlIntervalConstraintChangeLeftTarget);
				if (result == null) result = caseUMLIntervalConstraintChange(umlIntervalConstraintChangeLeftTarget);
				if (result == null) result = caseModelElementChange(umlIntervalConstraintChangeLeftTarget);
				if (result == null) result = caseUMLDiffExtension(umlIntervalConstraintChangeLeftTarget);
				if (result == null) result = caseDiffElement(umlIntervalConstraintChangeLeftTarget);
				if (result == null) result = caseAbstractDiffExtension(umlIntervalConstraintChangeLeftTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_INTERVAL_CONSTRAINT_CHANGE_RIGHT_TARGET: {
				UMLIntervalConstraintChangeRightTarget umlIntervalConstraintChangeRightTarget = (UMLIntervalConstraintChangeRightTarget)theEObject;
				T result = caseUMLIntervalConstraintChangeRightTarget(umlIntervalConstraintChangeRightTarget);
				if (result == null) result = caseModelElementChangeRightTarget(umlIntervalConstraintChangeRightTarget);
				if (result == null) result = caseUMLIntervalConstraintChange(umlIntervalConstraintChangeRightTarget);
				if (result == null) result = caseModelElementChange(umlIntervalConstraintChangeRightTarget);
				if (result == null) result = caseUMLDiffExtension(umlIntervalConstraintChangeRightTarget);
				if (result == null) result = caseDiffElement(umlIntervalConstraintChangeRightTarget);
				if (result == null) result = caseAbstractDiffExtension(umlIntervalConstraintChangeRightTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_MESSAGE_CHANGE: {
				UMLMessageChange umlMessageChange = (UMLMessageChange)theEObject;
				T result = caseUMLMessageChange(umlMessageChange);
				if (result == null) result = caseUMLDiffExtension(umlMessageChange);
				if (result == null) result = caseDiffElement(umlMessageChange);
				if (result == null) result = caseAbstractDiffExtension(umlMessageChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_MESSAGE_CHANGE_LEFT_TARGET: {
				UMLMessageChangeLeftTarget umlMessageChangeLeftTarget = (UMLMessageChangeLeftTarget)theEObject;
				T result = caseUMLMessageChangeLeftTarget(umlMessageChangeLeftTarget);
				if (result == null) result = caseModelElementChangeLeftTarget(umlMessageChangeLeftTarget);
				if (result == null) result = caseUMLMessageChange(umlMessageChangeLeftTarget);
				if (result == null) result = caseModelElementChange(umlMessageChangeLeftTarget);
				if (result == null) result = caseUMLDiffExtension(umlMessageChangeLeftTarget);
				if (result == null) result = caseDiffElement(umlMessageChangeLeftTarget);
				if (result == null) result = caseAbstractDiffExtension(umlMessageChangeLeftTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_MESSAGE_CHANGE_RIGHT_TARGET: {
				UMLMessageChangeRightTarget umlMessageChangeRightTarget = (UMLMessageChangeRightTarget)theEObject;
				T result = caseUMLMessageChangeRightTarget(umlMessageChangeRightTarget);
				if (result == null) result = caseModelElementChangeRightTarget(umlMessageChangeRightTarget);
				if (result == null) result = caseUMLMessageChange(umlMessageChangeRightTarget);
				if (result == null) result = caseModelElementChange(umlMessageChangeRightTarget);
				if (result == null) result = caseUMLDiffExtension(umlMessageChangeRightTarget);
				if (result == null) result = caseDiffElement(umlMessageChangeRightTarget);
				if (result == null) result = caseAbstractDiffExtension(umlMessageChangeRightTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_STEREOTYPE_PROPERTY_CHANGE: {
				UMLStereotypePropertyChange umlStereotypePropertyChange = (UMLStereotypePropertyChange)theEObject;
				T result = caseUMLStereotypePropertyChange(umlStereotypePropertyChange);
				if (result == null) result = caseUMLDiffExtension(umlStereotypePropertyChange);
				if (result == null) result = caseDiffElement(umlStereotypePropertyChange);
				if (result == null) result = caseAbstractDiffExtension(umlStereotypePropertyChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET: {
				UMLStereotypeAttributeChangeLeftTarget umlStereotypeAttributeChangeLeftTarget = (UMLStereotypeAttributeChangeLeftTarget)theEObject;
				T result = caseUMLStereotypeAttributeChangeLeftTarget(umlStereotypeAttributeChangeLeftTarget);
				if (result == null) result = caseAttributeChangeLeftTarget(umlStereotypeAttributeChangeLeftTarget);
				if (result == null) result = caseUMLStereotypePropertyChange(umlStereotypeAttributeChangeLeftTarget);
				if (result == null) result = caseAttributeChange(umlStereotypeAttributeChangeLeftTarget);
				if (result == null) result = caseUMLDiffExtension(umlStereotypeAttributeChangeLeftTarget);
				if (result == null) result = caseDiffElement(umlStereotypeAttributeChangeLeftTarget);
				if (result == null) result = caseAbstractDiffExtension(umlStereotypeAttributeChangeLeftTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET: {
				UMLStereotypeAttributeChangeRightTarget umlStereotypeAttributeChangeRightTarget = (UMLStereotypeAttributeChangeRightTarget)theEObject;
				T result = caseUMLStereotypeAttributeChangeRightTarget(umlStereotypeAttributeChangeRightTarget);
				if (result == null) result = caseAttributeChangeRightTarget(umlStereotypeAttributeChangeRightTarget);
				if (result == null) result = caseUMLStereotypePropertyChange(umlStereotypeAttributeChangeRightTarget);
				if (result == null) result = caseAttributeChange(umlStereotypeAttributeChangeRightTarget);
				if (result == null) result = caseUMLDiffExtension(umlStereotypeAttributeChangeRightTarget);
				if (result == null) result = caseDiffElement(umlStereotypeAttributeChangeRightTarget);
				if (result == null) result = caseAbstractDiffExtension(umlStereotypeAttributeChangeRightTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_STEREOTYPE_UPDATE_ATTRIBUTE: {
				UMLStereotypeUpdateAttribute umlStereotypeUpdateAttribute = (UMLStereotypeUpdateAttribute)theEObject;
				T result = caseUMLStereotypeUpdateAttribute(umlStereotypeUpdateAttribute);
				if (result == null) result = caseUpdateAttribute(umlStereotypeUpdateAttribute);
				if (result == null) result = caseUMLStereotypePropertyChange(umlStereotypeUpdateAttribute);
				if (result == null) result = caseAttributeChange(umlStereotypeUpdateAttribute);
				if (result == null) result = caseUMLDiffExtension(umlStereotypeUpdateAttribute);
				if (result == null) result = caseDiffElement(umlStereotypeUpdateAttribute);
				if (result == null) result = caseAbstractDiffExtension(umlStereotypeUpdateAttribute);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_CHANGE: {
				UMLStereotypeApplicationChange umlStereotypeApplicationChange = (UMLStereotypeApplicationChange)theEObject;
				T result = caseUMLStereotypeApplicationChange(umlStereotypeApplicationChange);
				if (result == null) result = caseUMLDiffExtension(umlStereotypeApplicationChange);
				if (result == null) result = caseDiffElement(umlStereotypeApplicationChange);
				if (result == null) result = caseAbstractDiffExtension(umlStereotypeApplicationChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_ADDITION: {
				UMLStereotypeApplicationAddition umlStereotypeApplicationAddition = (UMLStereotypeApplicationAddition)theEObject;
				T result = caseUMLStereotypeApplicationAddition(umlStereotypeApplicationAddition);
				if (result == null) result = caseUpdateModelElement(umlStereotypeApplicationAddition);
				if (result == null) result = caseUMLStereotypeApplicationChange(umlStereotypeApplicationAddition);
				if (result == null) result = caseModelElementChange(umlStereotypeApplicationAddition);
				if (result == null) result = caseUMLDiffExtension(umlStereotypeApplicationAddition);
				if (result == null) result = caseDiffElement(umlStereotypeApplicationAddition);
				if (result == null) result = caseAbstractDiffExtension(umlStereotypeApplicationAddition);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL: {
				UMLStereotypeApplicationRemoval umlStereotypeApplicationRemoval = (UMLStereotypeApplicationRemoval)theEObject;
				T result = caseUMLStereotypeApplicationRemoval(umlStereotypeApplicationRemoval);
				if (result == null) result = caseUpdateModelElement(umlStereotypeApplicationRemoval);
				if (result == null) result = caseUMLStereotypeApplicationChange(umlStereotypeApplicationRemoval);
				if (result == null) result = caseModelElementChange(umlStereotypeApplicationRemoval);
				if (result == null) result = caseUMLDiffExtension(umlStereotypeApplicationRemoval);
				if (result == null) result = caseDiffElement(umlStereotypeApplicationRemoval);
				if (result == null) result = caseAbstractDiffExtension(umlStereotypeApplicationRemoval);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET: {
				UMLStereotypeReferenceChangeLeftTarget umlStereotypeReferenceChangeLeftTarget = (UMLStereotypeReferenceChangeLeftTarget)theEObject;
				T result = caseUMLStereotypeReferenceChangeLeftTarget(umlStereotypeReferenceChangeLeftTarget);
				if (result == null) result = caseReferenceChangeLeftTarget(umlStereotypeReferenceChangeLeftTarget);
				if (result == null) result = caseUMLStereotypePropertyChange(umlStereotypeReferenceChangeLeftTarget);
				if (result == null) result = caseReferenceChange(umlStereotypeReferenceChangeLeftTarget);
				if (result == null) result = caseUMLDiffExtension(umlStereotypeReferenceChangeLeftTarget);
				if (result == null) result = caseDiffElement(umlStereotypeReferenceChangeLeftTarget);
				if (result == null) result = caseAbstractDiffExtension(umlStereotypeReferenceChangeLeftTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET: {
				UMLStereotypeReferenceChangeRightTarget umlStereotypeReferenceChangeRightTarget = (UMLStereotypeReferenceChangeRightTarget)theEObject;
				T result = caseUMLStereotypeReferenceChangeRightTarget(umlStereotypeReferenceChangeRightTarget);
				if (result == null) result = caseReferenceChangeRightTarget(umlStereotypeReferenceChangeRightTarget);
				if (result == null) result = caseUMLStereotypePropertyChange(umlStereotypeReferenceChangeRightTarget);
				if (result == null) result = caseReferenceChange(umlStereotypeReferenceChangeRightTarget);
				if (result == null) result = caseUMLDiffExtension(umlStereotypeReferenceChangeRightTarget);
				if (result == null) result = caseDiffElement(umlStereotypeReferenceChangeRightTarget);
				if (result == null) result = caseAbstractDiffExtension(umlStereotypeReferenceChangeRightTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_STEREOTYPE_UPDATE_REFERENCE: {
				UMLStereotypeUpdateReference umlStereotypeUpdateReference = (UMLStereotypeUpdateReference)theEObject;
				T result = caseUMLStereotypeUpdateReference(umlStereotypeUpdateReference);
				if (result == null) result = caseUpdateReference(umlStereotypeUpdateReference);
				if (result == null) result = caseUMLStereotypePropertyChange(umlStereotypeUpdateReference);
				if (result == null) result = caseReferenceChange(umlStereotypeUpdateReference);
				if (result == null) result = caseUMLDiffExtension(umlStereotypeUpdateReference);
				if (result == null) result = caseDiffElement(umlStereotypeUpdateReference);
				if (result == null) result = caseAbstractDiffExtension(umlStereotypeUpdateReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_STEREOTYPE_REFERENCE_ORDER_CHANGE: {
				UMLStereotypeReferenceOrderChange umlStereotypeReferenceOrderChange = (UMLStereotypeReferenceOrderChange)theEObject;
				T result = caseUMLStereotypeReferenceOrderChange(umlStereotypeReferenceOrderChange);
				if (result == null) result = caseReferenceOrderChange(umlStereotypeReferenceOrderChange);
				if (result == null) result = caseUMLStereotypePropertyChange(umlStereotypeReferenceOrderChange);
				if (result == null) result = caseReferenceChange(umlStereotypeReferenceOrderChange);
				if (result == null) result = caseUMLDiffExtension(umlStereotypeReferenceOrderChange);
				if (result == null) result = caseDiffElement(umlStereotypeReferenceOrderChange);
				if (result == null) result = caseAbstractDiffExtension(umlStereotypeReferenceOrderChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_PROFILE_APPLICATION_CHANGE: {
				UMLProfileApplicationChange umlProfileApplicationChange = (UMLProfileApplicationChange)theEObject;
				T result = caseUMLProfileApplicationChange(umlProfileApplicationChange);
				if (result == null) result = caseUMLDiffExtension(umlProfileApplicationChange);
				if (result == null) result = caseDiffElement(umlProfileApplicationChange);
				if (result == null) result = caseAbstractDiffExtension(umlProfileApplicationChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_PROFILE_APPLICATION_ADDITION: {
				UMLProfileApplicationAddition umlProfileApplicationAddition = (UMLProfileApplicationAddition)theEObject;
				T result = caseUMLProfileApplicationAddition(umlProfileApplicationAddition);
				if (result == null) result = caseUpdateModelElement(umlProfileApplicationAddition);
				if (result == null) result = caseUMLProfileApplicationChange(umlProfileApplicationAddition);
				if (result == null) result = caseModelElementChange(umlProfileApplicationAddition);
				if (result == null) result = caseUMLDiffExtension(umlProfileApplicationAddition);
				if (result == null) result = caseDiffElement(umlProfileApplicationAddition);
				if (result == null) result = caseAbstractDiffExtension(umlProfileApplicationAddition);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_PROFILE_APPLICATION_REMOVAL: {
				UMLProfileApplicationRemoval umlProfileApplicationRemoval = (UMLProfileApplicationRemoval)theEObject;
				T result = caseUMLProfileApplicationRemoval(umlProfileApplicationRemoval);
				if (result == null) result = caseUpdateModelElement(umlProfileApplicationRemoval);
				if (result == null) result = caseUMLProfileApplicationChange(umlProfileApplicationRemoval);
				if (result == null) result = caseModelElementChange(umlProfileApplicationRemoval);
				if (result == null) result = caseUMLDiffExtension(umlProfileApplicationRemoval);
				if (result == null) result = caseDiffElement(umlProfileApplicationRemoval);
				if (result == null) result = caseAbstractDiffExtension(umlProfileApplicationRemoval);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Diff Extension</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Diff Extension</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLDiffExtension(UMLDiffExtension object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Association Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Association Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLAssociationChange(UMLAssociationChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Association Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Association Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLAssociationChangeLeftTarget(UMLAssociationChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Association Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Association Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLAssociationChangeRightTarget(UMLAssociationChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Association Branch Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Association Branch Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLAssociationBranchChange(UMLAssociationBranchChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Association Branch Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Association Branch Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLAssociationBranchChangeLeftTarget(UMLAssociationBranchChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Association Branch Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Association Branch Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLAssociationBranchChangeRightTarget(UMLAssociationBranchChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Dependency Branch Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Dependency Branch Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLDependencyBranchChange(UMLDependencyBranchChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Dependency Branch Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Dependency Branch Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLDependencyBranchChangeLeftTarget(UMLDependencyBranchChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Dependency Branch Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Dependency Branch Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLDependencyBranchChangeRightTarget(UMLDependencyBranchChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Generalization Set Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Generalization Set Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLGeneralizationSetChange(UMLGeneralizationSetChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Generalization Set Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Generalization Set Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLGeneralizationSetChangeLeftTarget(UMLGeneralizationSetChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Generalization Set Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Generalization Set Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLGeneralizationSetChangeRightTarget(UMLGeneralizationSetChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Dependency Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Dependency Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLDependencyChange(UMLDependencyChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Dependency Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Dependency Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLDependencyChangeLeftTarget(UMLDependencyChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Dependency Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Dependency Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLDependencyChangeRightTarget(UMLDependencyChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Extend Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Extend Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLExtendChange(UMLExtendChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Extend Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Extend Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLExtendChangeLeftTarget(UMLExtendChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Extend Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Extend Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLExtendChangeRightTarget(UMLExtendChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Execution Specification Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Execution Specification Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLExecutionSpecificationChange(UMLExecutionSpecificationChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Execution Specification Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Execution Specification Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLExecutionSpecificationChangeLeftTarget(UMLExecutionSpecificationChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Execution Specification Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Execution Specification Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLExecutionSpecificationChangeRightTarget(UMLExecutionSpecificationChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Destruction Event Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Destruction Event Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLDestructionEventChange(UMLDestructionEventChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Destruction Event Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Destruction Event Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLDestructionEventChangeLeftTarget(UMLDestructionEventChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Destruction Event Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Destruction Event Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLDestructionEventChangeRightTarget(UMLDestructionEventChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Interval Constraint Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Interval Constraint Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLIntervalConstraintChange(UMLIntervalConstraintChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Interval Constraint Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Interval Constraint Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLIntervalConstraintChangeLeftTarget(UMLIntervalConstraintChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Interval Constraint Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Interval Constraint Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLIntervalConstraintChangeRightTarget(UMLIntervalConstraintChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Message Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Message Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLMessageChange(UMLMessageChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Message Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Message Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLMessageChangeLeftTarget(UMLMessageChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Message Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Message Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLMessageChangeRightTarget(UMLMessageChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Stereotype Property Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Stereotype Property Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLStereotypePropertyChange(UMLStereotypePropertyChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Stereotype Attribute Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Stereotype Attribute Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLStereotypeAttributeChangeLeftTarget(UMLStereotypeAttributeChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Stereotype Attribute Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Stereotype Attribute Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLStereotypeAttributeChangeRightTarget(UMLStereotypeAttributeChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Stereotype Update Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Stereotype Update Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLStereotypeUpdateAttribute(UMLStereotypeUpdateAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Stereotype Application Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Stereotype Application Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLStereotypeApplicationChange(UMLStereotypeApplicationChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Stereotype Application Addition</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Stereotype Application Addition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLStereotypeApplicationAddition(UMLStereotypeApplicationAddition object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Stereotype Application Removal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Stereotype Application Removal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLStereotypeApplicationRemoval(UMLStereotypeApplicationRemoval object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Stereotype Reference Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Stereotype Reference Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLStereotypeReferenceChangeLeftTarget(UMLStereotypeReferenceChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Stereotype Reference Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Stereotype Reference Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLStereotypeReferenceChangeRightTarget(UMLStereotypeReferenceChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Stereotype Update Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Stereotype Update Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLStereotypeUpdateReference(UMLStereotypeUpdateReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Stereotype Reference Order Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Stereotype Reference Order Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLStereotypeReferenceOrderChange(UMLStereotypeReferenceOrderChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Profile Application Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Profile Application Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLProfileApplicationChange(UMLProfileApplicationChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Profile Application Addition</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Profile Application Addition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLProfileApplicationAddition(UMLProfileApplicationAddition object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Profile Application Removal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Profile Application Removal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLProfileApplicationRemoval(UMLProfileApplicationRemoval object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiffElement(DiffElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Abstract Diff Extension</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Abstract Diff Extension</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAbstractDiffExtension(AbstractDiffExtension object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model Element Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model Element Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModelElementChange(ModelElementChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model Element Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model Element Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModelElementChangeLeftTarget(ModelElementChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model Element Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model Element Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModelElementChangeRightTarget(ModelElementChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAttributeChange(AttributeChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAttributeChangeLeftTarget(AttributeChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAttributeChangeRightTarget(AttributeChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Update Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Update Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUpdateAttribute(UpdateAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Update Model Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Update Model Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUpdateModelElement(UpdateModelElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Reference Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Reference Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseReferenceChange(ReferenceChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Reference Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Reference Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseReferenceChangeLeftTarget(ReferenceChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Reference Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Reference Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseReferenceChangeRightTarget(ReferenceChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Update Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Update Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUpdateReference(UpdateReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Reference Order Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Reference Order Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseReferenceOrderChange(ReferenceOrderChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //UML2DiffSwitch
