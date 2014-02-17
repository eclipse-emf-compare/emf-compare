/**
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2.internal.util;

import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.uml2.internal.AssociationChange;
import org.eclipse.emf.compare.uml2.internal.DirectedRelationshipChange;
import org.eclipse.emf.compare.uml2.internal.ExecutionSpecificationChange;
import org.eclipse.emf.compare.uml2.internal.ExtendChange;
import org.eclipse.emf.compare.uml2.internal.GeneralizationSetChange;
import org.eclipse.emf.compare.uml2.internal.IntervalConstraintChange;
import org.eclipse.emf.compare.uml2.internal.MessageChange;
import org.eclipse.emf.compare.uml2.internal.ProfileApplicationChange;
import org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange;
import org.eclipse.emf.compare.uml2.internal.StereotypeAttributeChange;
import org.eclipse.emf.compare.uml2.internal.StereotypeReferenceChange;
import org.eclipse.emf.compare.uml2.internal.UMLComparePackage;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance hierarchy. It supports the call
 * {@link #doSwitch(EObject) doSwitch(object)} to invoke the <code>caseXXX</code> method for each class of the
 * model, starting with the actual class of the object and proceeding up the inheritance hierarchy until a
 * non-null result is returned, which is the result of the switch. <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.uml2.internal.UMLComparePackage
 * @generated
 */
public class UMLCompareSwitch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected static UMLComparePackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public UMLCompareSwitch() {
		if (modelPackage == null) {
			modelPackage = UMLComparePackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public T doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case UMLComparePackage.ASSOCIATION_CHANGE: {
				AssociationChange associationChange = (AssociationChange)theEObject;
				T result = caseAssociationChange(associationChange);
				if (result == null) result = caseUMLDiff(associationChange);
				if (result == null) result = caseDiff(associationChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UMLComparePackage.EXTEND_CHANGE: {
				ExtendChange extendChange = (ExtendChange)theEObject;
				T result = caseExtendChange(extendChange);
				if (result == null) result = caseUMLDiff(extendChange);
				if (result == null) result = caseDiff(extendChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UMLComparePackage.GENERALIZATION_SET_CHANGE: {
				GeneralizationSetChange generalizationSetChange = (GeneralizationSetChange)theEObject;
				T result = caseGeneralizationSetChange(generalizationSetChange);
				if (result == null) result = caseUMLDiff(generalizationSetChange);
				if (result == null) result = caseDiff(generalizationSetChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UMLComparePackage.EXECUTION_SPECIFICATION_CHANGE: {
				ExecutionSpecificationChange executionSpecificationChange = (ExecutionSpecificationChange)theEObject;
				T result = caseExecutionSpecificationChange(executionSpecificationChange);
				if (result == null) result = caseUMLDiff(executionSpecificationChange);
				if (result == null) result = caseDiff(executionSpecificationChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UMLComparePackage.INTERVAL_CONSTRAINT_CHANGE: {
				IntervalConstraintChange intervalConstraintChange = (IntervalConstraintChange)theEObject;
				T result = caseIntervalConstraintChange(intervalConstraintChange);
				if (result == null) result = caseUMLDiff(intervalConstraintChange);
				if (result == null) result = caseDiff(intervalConstraintChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UMLComparePackage.MESSAGE_CHANGE: {
				MessageChange messageChange = (MessageChange)theEObject;
				T result = caseMessageChange(messageChange);
				if (result == null) result = caseUMLDiff(messageChange);
				if (result == null) result = caseDiff(messageChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UMLComparePackage.STEREOTYPE_ATTRIBUTE_CHANGE: {
				StereotypeAttributeChange stereotypeAttributeChange = (StereotypeAttributeChange)theEObject;
				T result = caseStereotypeAttributeChange(stereotypeAttributeChange);
				if (result == null) result = caseUMLDiff(stereotypeAttributeChange);
				if (result == null) result = caseDiff(stereotypeAttributeChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE: {
				StereotypeApplicationChange stereotypeApplicationChange = (StereotypeApplicationChange)theEObject;
				T result = caseStereotypeApplicationChange(stereotypeApplicationChange);
				if (result == null) result = caseUMLDiff(stereotypeApplicationChange);
				if (result == null) result = caseDiff(stereotypeApplicationChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UMLComparePackage.STEREOTYPE_REFERENCE_CHANGE: {
				StereotypeReferenceChange stereotypeReferenceChange = (StereotypeReferenceChange)theEObject;
				T result = caseStereotypeReferenceChange(stereotypeReferenceChange);
				if (result == null) result = caseUMLDiff(stereotypeReferenceChange);
				if (result == null) result = caseDiff(stereotypeReferenceChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UMLComparePackage.PROFILE_APPLICATION_CHANGE: {
				ProfileApplicationChange profileApplicationChange = (ProfileApplicationChange)theEObject;
				T result = caseProfileApplicationChange(profileApplicationChange);
				if (result == null) result = caseUMLDiff(profileApplicationChange);
				if (result == null) result = caseDiff(profileApplicationChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UMLComparePackage.DIRECTED_RELATIONSHIP_CHANGE: {
				DirectedRelationshipChange directedRelationshipChange = (DirectedRelationshipChange)theEObject;
				T result = caseDirectedRelationshipChange(directedRelationshipChange);
				if (result == null) result = caseUMLDiff(directedRelationshipChange);
				if (result == null) result = caseDiff(directedRelationshipChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UMLComparePackage.UML_DIFF: {
				UMLDiff umlDiff = (UMLDiff)theEObject;
				T result = caseUMLDiff(umlDiff);
				if (result == null) result = caseDiff(umlDiff);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Association Change</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Association Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAssociationChange(AssociationChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Extend Change</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Extend Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExtendChange(ExtendChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Generalization Set Change</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Generalization Set Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGeneralizationSetChange(GeneralizationSetChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Execution Specification Change</em>'.
	 * <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Execution Specification Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExecutionSpecificationChange(ExecutionSpecificationChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Interval Constraint Change</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Interval Constraint Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIntervalConstraintChange(IntervalConstraintChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Message Change</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Message Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMessageChange(MessageChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Stereotype Attribute Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Stereotype Attribute Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStereotypeAttributeChange(StereotypeAttributeChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Stereotype Application Change</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Stereotype Application Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStereotypeApplicationChange(StereotypeApplicationChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Stereotype Reference Change</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Stereotype Reference Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStereotypeReferenceChange(StereotypeReferenceChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Profile Application Change</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Profile Application Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseProfileApplicationChange(ProfileApplicationChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Directed Relationship Change</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Directed Relationship Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDirectedRelationshipChange(DirectedRelationshipChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Diff</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Diff</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLDiff(UMLDiff object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Diff</em>'.
	 * <!-- begin-user-doc
	 * --> This implementation returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diff</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiff(Diff object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc
	 * --> This implementation returns null; returning a non-null result will terminate the switch, but this
	 * is the last case anyway. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public T defaultCase(EObject object) {
		return null;
	}

} // UMLCompareSwitch
