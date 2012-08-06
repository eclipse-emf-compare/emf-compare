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
package org.eclipse.emf.compare.uml2diff.util;

import org.eclipse.emf.compare.Diff;

import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2diff.*;

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
 * @see org.eclipse.emf.compare.uml2diff.Uml2diffPackage
 * @generated
 */
public class Uml2diffSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static Uml2diffPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Uml2diffSwitch() {
		if (modelPackage == null) {
			modelPackage = Uml2diffPackage.eINSTANCE;
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
			case Uml2diffPackage.UML_ASSOCIATION_CHANGE: {
				UMLAssociationChange umlAssociationChange = (UMLAssociationChange)theEObject;
				T result = caseUMLAssociationChange(umlAssociationChange);
				if (result == null) result = caseUMLExtension(umlAssociationChange);
				if (result == null) result = caseDiff(umlAssociationChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Uml2diffPackage.UML_DEPENDENCY_CHANGE: {
				UMLDependencyChange umlDependencyChange = (UMLDependencyChange)theEObject;
				T result = caseUMLDependencyChange(umlDependencyChange);
				if (result == null) result = caseUMLExtension(umlDependencyChange);
				if (result == null) result = caseDiff(umlDependencyChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Uml2diffPackage.UML_INTERFACE_REALIZATION_CHANGE: {
				UMLInterfaceRealizationChange umlInterfaceRealizationChange = (UMLInterfaceRealizationChange)theEObject;
				T result = caseUMLInterfaceRealizationChange(umlInterfaceRealizationChange);
				if (result == null) result = caseUMLExtension(umlInterfaceRealizationChange);
				if (result == null) result = caseDiff(umlInterfaceRealizationChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Uml2diffPackage.UML_SUBSTITUTION_CHANGE: {
				UMLSubstitutionChange umlSubstitutionChange = (UMLSubstitutionChange)theEObject;
				T result = caseUMLSubstitutionChange(umlSubstitutionChange);
				if (result == null) result = caseUMLExtension(umlSubstitutionChange);
				if (result == null) result = caseDiff(umlSubstitutionChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Uml2diffPackage.UML_EXTEND_CHANGE: {
				UMLExtendChange umlExtendChange = (UMLExtendChange)theEObject;
				T result = caseUMLExtendChange(umlExtendChange);
				if (result == null) result = caseUMLExtension(umlExtendChange);
				if (result == null) result = caseDiff(umlExtendChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Uml2diffPackage.UML_GENERALIZATION_SET_CHANGE: {
				UMLGeneralizationSetChange umlGeneralizationSetChange = (UMLGeneralizationSetChange)theEObject;
				T result = caseUMLGeneralizationSetChange(umlGeneralizationSetChange);
				if (result == null) result = caseUMLExtension(umlGeneralizationSetChange);
				if (result == null) result = caseDiff(umlGeneralizationSetChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Uml2diffPackage.UML_EXECUTION_SPECIFICATION_CHANGE: {
				UMLExecutionSpecificationChange umlExecutionSpecificationChange = (UMLExecutionSpecificationChange)theEObject;
				T result = caseUMLExecutionSpecificationChange(umlExecutionSpecificationChange);
				if (result == null) result = caseUMLExtension(umlExecutionSpecificationChange);
				if (result == null) result = caseDiff(umlExecutionSpecificationChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Uml2diffPackage.UML_DESTRUCTION_EVENT_CHANGE: {
				UMLDestructionEventChange umlDestructionEventChange = (UMLDestructionEventChange)theEObject;
				T result = caseUMLDestructionEventChange(umlDestructionEventChange);
				if (result == null) result = caseUMLExtension(umlDestructionEventChange);
				if (result == null) result = caseDiff(umlDestructionEventChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Uml2diffPackage.UML_INTERVAL_CONSTRAINT_CHANGE: {
				UMLIntervalConstraintChange umlIntervalConstraintChange = (UMLIntervalConstraintChange)theEObject;
				T result = caseUMLIntervalConstraintChange(umlIntervalConstraintChange);
				if (result == null) result = caseUMLExtension(umlIntervalConstraintChange);
				if (result == null) result = caseDiff(umlIntervalConstraintChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Uml2diffPackage.UML_MESSAGE_CHANGE: {
				UMLMessageChange umlMessageChange = (UMLMessageChange)theEObject;
				T result = caseUMLMessageChange(umlMessageChange);
				if (result == null) result = caseUMLExtension(umlMessageChange);
				if (result == null) result = caseDiff(umlMessageChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Uml2diffPackage.UML_STEREOTYPE_PROPERTY_CHANGE: {
				UMLStereotypePropertyChange umlStereotypePropertyChange = (UMLStereotypePropertyChange)theEObject;
				T result = caseUMLStereotypePropertyChange(umlStereotypePropertyChange);
				if (result == null) result = caseUMLExtension(umlStereotypePropertyChange);
				if (result == null) result = caseDiff(umlStereotypePropertyChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Uml2diffPackage.UML_STEREOTYPE_APPLICATION_CHANGE: {
				UMLStereotypeApplicationChange umlStereotypeApplicationChange = (UMLStereotypeApplicationChange)theEObject;
				T result = caseUMLStereotypeApplicationChange(umlStereotypeApplicationChange);
				if (result == null) result = caseReferenceChange(umlStereotypeApplicationChange);
				if (result == null) result = caseUMLExtension(umlStereotypeApplicationChange);
				if (result == null) result = caseDiff(umlStereotypeApplicationChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Uml2diffPackage.UML_STEREOTYPE_REFERENCE_CHANGE: {
				UMLStereotypeReferenceChange umlStereotypeReferenceChange = (UMLStereotypeReferenceChange)theEObject;
				T result = caseUMLStereotypeReferenceChange(umlStereotypeReferenceChange);
				if (result == null) result = caseUMLExtension(umlStereotypeReferenceChange);
				if (result == null) result = caseDiff(umlStereotypeReferenceChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Uml2diffPackage.UML_PROFILE_APPLICATION_CHANGE: {
				UMLProfileApplicationChange umlProfileApplicationChange = (UMLProfileApplicationChange)theEObject;
				T result = caseUMLProfileApplicationChange(umlProfileApplicationChange);
				if (result == null) result = caseReferenceChange(umlProfileApplicationChange);
				if (result == null) result = caseUMLExtension(umlProfileApplicationChange);
				if (result == null) result = caseDiff(umlProfileApplicationChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Uml2diffPackage.UML_EXTENSION: {
				UMLExtension umlExtension = (UMLExtension)theEObject;
				T result = caseUMLExtension(umlExtension);
				if (result == null) result = caseDiff(umlExtension);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
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
	 * Returns the result of interpreting the object as an instance of '<em>UML Interface Realization Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Interface Realization Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLInterfaceRealizationChange(UMLInterfaceRealizationChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Substitution Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Substitution Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLSubstitutionChange(UMLSubstitutionChange object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>UML Stereotype Reference Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Stereotype Reference Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLStereotypeReferenceChange(UMLStereotypeReferenceChange object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>UML Extension</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Extension</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLExtension(UMLExtension object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Diff</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diff</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiff(Diff object) {
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

} //Uml2diffSwitch
