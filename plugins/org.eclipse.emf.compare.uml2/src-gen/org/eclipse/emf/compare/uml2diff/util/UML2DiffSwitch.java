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
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateModelElement;

import org.eclipse.emf.compare.uml2diff.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

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
public class UML2DiffSwitch<T> {
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
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public T doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		}
		else {
			List<EClass> eSuperTypes = theEClass.getESuperTypes();
			return
				eSuperTypes.isEmpty() ?
					defaultCase(theEObject) :
					doSwitch(eSuperTypes.get(0), theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
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
			case UML2DiffPackage.UML_ABSTRACTION_CHANGE: {
				UMLAbstractionChange umlAbstractionChange = (UMLAbstractionChange)theEObject;
				T result = caseUMLAbstractionChange(umlAbstractionChange);
				if (result == null) result = caseUMLDiffExtension(umlAbstractionChange);
				if (result == null) result = caseDiffElement(umlAbstractionChange);
				if (result == null) result = caseAbstractDiffExtension(umlAbstractionChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_ABSTRACTION_CHANGE_LEFT_TARGET: {
				UMLAbstractionChangeLeftTarget umlAbstractionChangeLeftTarget = (UMLAbstractionChangeLeftTarget)theEObject;
				T result = caseUMLAbstractionChangeLeftTarget(umlAbstractionChangeLeftTarget);
				if (result == null) result = caseModelElementChangeLeftTarget(umlAbstractionChangeLeftTarget);
				if (result == null) result = caseUMLAbstractionChange(umlAbstractionChangeLeftTarget);
				if (result == null) result = caseModelElementChange(umlAbstractionChangeLeftTarget);
				if (result == null) result = caseUMLDiffExtension(umlAbstractionChangeLeftTarget);
				if (result == null) result = caseDiffElement(umlAbstractionChangeLeftTarget);
				if (result == null) result = caseAbstractDiffExtension(umlAbstractionChangeLeftTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UML2DiffPackage.UML_ABSTRACTION_CHANGE_RIGHT_TARGET: {
				UMLAbstractionChangeRightTarget umlAbstractionChangeRightTarget = (UMLAbstractionChangeRightTarget)theEObject;
				T result = caseUMLAbstractionChangeRightTarget(umlAbstractionChangeRightTarget);
				if (result == null) result = caseModelElementChangeRightTarget(umlAbstractionChangeRightTarget);
				if (result == null) result = caseUMLAbstractionChange(umlAbstractionChangeRightTarget);
				if (result == null) result = caseModelElementChange(umlAbstractionChangeRightTarget);
				if (result == null) result = caseUMLDiffExtension(umlAbstractionChangeRightTarget);
				if (result == null) result = caseDiffElement(umlAbstractionChangeRightTarget);
				if (result == null) result = caseAbstractDiffExtension(umlAbstractionChangeRightTarget);
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
	 * Returns the result of interpreting the object as an instance of '<em>UML Abstraction Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Abstraction Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLAbstractionChange(UMLAbstractionChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Abstraction Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Abstraction Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLAbstractionChangeLeftTarget(UMLAbstractionChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Abstraction Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Abstraction Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLAbstractionChangeRightTarget(UMLAbstractionChangeRightTarget object) {
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
	public T defaultCase(EObject object) {
		return null;
	}

} //UML2DiffSwitch
