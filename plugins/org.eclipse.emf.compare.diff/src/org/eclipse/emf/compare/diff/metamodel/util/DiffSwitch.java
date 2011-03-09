/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.metamodel.util;

import java.util.List;

import org.eclipse.emf.compare.diff.metamodel.*;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.AttributeChange;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChange;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.MoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChange;
import org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ResourceDiff;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateContainmentFeature;
import org.eclipse.emf.compare.diff.metamodel.UpdateModelElement;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance hierarchy. It supports the call
 * {@link #doSwitch(EObject) doSwitch(object)} to invoke the <code>caseXXX</code> method for each class of the
 * model, starting with the actual class of the object and proceeding up the inheritance hierarchy until a
 * non-null result is returned, which is the result of the switch. <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage
 * @generated
 */

public class DiffSwitch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected static DiffPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DiffSwitch() {
		if (modelPackage == null) {
			modelPackage = DiffPackage.eINSTANCE;
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Abstract Diff Extension</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Abstract Diff Extension</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseAbstractDiffExtension(AbstractDiffExtension object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Resource Diff</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Resource Diff</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseResourceDiff(ResourceDiff object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Resource Dependency Change</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Resource Dependency Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseResourceDependencyChange(ResourceDependencyChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Resource Dependency Change Left Target</em>'.
	 * <!-- begin-user-doc --> This implementation returns
	 * null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Resource Dependency Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseResourceDependencyChangeLeftTarget(ResourceDependencyChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Resource Dependency Change Right Target</em>'.
	 * <!-- begin-user-doc --> This implementation returns
	 * null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Resource Dependency Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseResourceDependencyChangeRightTarget(ResourceDependencyChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute Change</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseAttributeChange(AttributeChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute Change Left Target</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseAttributeChangeLeftTarget(AttributeChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute Change Right Target</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseAttributeChangeRightTarget(AttributeChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Conflicting Diff Element</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Conflicting Diff Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseConflictingDiffElement(ConflictingDiffElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Element</em>'.
	 * <!-- begin-user-doc
	 * --> This implementation returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseDiffElement(DiffElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Group</em>'.
	 * <!-- begin-user-doc
	 * --> This implementation returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Group</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseDiffGroup(DiffGroup object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Comparison Snapshot</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Comparison Snapshot</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseComparisonSnapshot(ComparisonSnapshot object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Comparison Snapshot</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Comparison Snapshot</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseComparisonResourceSnapshot(ComparisonResourceSnapshot object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Comparison Resource Set Snapshot</em>'.
	 * <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Comparison Resource Set Snapshot</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseComparisonResourceSetSnapshot(ComparisonResourceSetSnapshot object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
	 * <!-- begin-user-doc
	 * --> This implementation returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseDiffModel(DiffModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Resource Set</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Resource Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiffResourceSet(DiffResourceSet object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model Element Change</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model Element Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseModelElementChange(ModelElementChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model Element Change Left Target</em>'.
	 * <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model Element Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseModelElementChangeLeftTarget(ModelElementChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model Element Change Right Target</em>'.
	 * <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model Element Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseModelElementChangeRightTarget(ModelElementChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Move Model Element</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Move Model Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseMoveModelElement(MoveModelElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Update Containment Feature</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Update Containment Feature</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUpdateContainmentFeature(UpdateContainmentFeature object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Reference Change</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Reference Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseReferenceChange(ReferenceChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Reference Change Left Target</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Reference Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseReferenceChangeLeftTarget(ReferenceChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Reference Change Right Target</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Reference Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseReferenceChangeRightTarget(ReferenceChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Update Attribute</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Update Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseUpdateAttribute(UpdateAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Update Model Element</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Update Model Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseUpdateModelElement(UpdateModelElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Update Reference</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Update Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
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
	 * <!-- begin-user-doc
	 * --> This implementation returns null; returning a non-null result will terminate the switch, but this
	 * is the last case anyway. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T defaultCase(EObject object) {
		return null;
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
		} else {
			List<EClass> eSuperTypes = theEClass.getESuperTypes();
			return eSuperTypes.isEmpty() ? defaultCase(theEObject) : doSwitch(eSuperTypes.get(0), theEObject);
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
			case DiffPackage.DIFF_MODEL: {
				DiffModel diffModel = (DiffModel)theEObject;
				T result = caseDiffModel(diffModel);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.DIFF_RESOURCE_SET: {
				DiffResourceSet diffResourceSet = (DiffResourceSet)theEObject;
				T result = caseDiffResourceSet(diffResourceSet);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.DIFF_ELEMENT: {
				DiffElement diffElement = (DiffElement)theEObject;
				T result = caseDiffElement(diffElement);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.CONFLICTING_DIFF_ELEMENT: {
				ConflictingDiffElement conflictingDiffElement = (ConflictingDiffElement)theEObject;
				T result = caseConflictingDiffElement(conflictingDiffElement);
				if (result == null)
					result = caseDiffElement(conflictingDiffElement);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.DIFF_GROUP: {
				DiffGroup diffGroup = (DiffGroup)theEObject;
				T result = caseDiffGroup(diffGroup);
				if (result == null)
					result = caseDiffElement(diffGroup);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.COMPARISON_SNAPSHOT: {
				ComparisonSnapshot comparisonSnapshot = (ComparisonSnapshot)theEObject;
				T result = caseComparisonSnapshot(comparisonSnapshot);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.COMPARISON_RESOURCE_SNAPSHOT: {
				ComparisonResourceSnapshot comparisonResourceSnapshot = (ComparisonResourceSnapshot)theEObject;
				T result = caseComparisonResourceSnapshot(comparisonResourceSnapshot);
				if (result == null)
					result = caseComparisonSnapshot(comparisonResourceSnapshot);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT: {
				ComparisonResourceSetSnapshot comparisonResourceSetSnapshot = (ComparisonResourceSetSnapshot)theEObject;
				T result = caseComparisonResourceSetSnapshot(comparisonResourceSetSnapshot);
				if (result == null)
					result = caseComparisonSnapshot(comparisonResourceSetSnapshot);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.MODEL_ELEMENT_CHANGE: {
				ModelElementChange modelElementChange = (ModelElementChange)theEObject;
				T result = caseModelElementChange(modelElementChange);
				if (result == null)
					result = caseDiffElement(modelElementChange);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET: {
				ModelElementChangeLeftTarget modelElementChangeLeftTarget = (ModelElementChangeLeftTarget)theEObject;
				T result = caseModelElementChangeLeftTarget(modelElementChangeLeftTarget);
				if (result == null)
					result = caseModelElementChange(modelElementChangeLeftTarget);
				if (result == null)
					result = caseDiffElement(modelElementChangeLeftTarget);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET: {
				ModelElementChangeRightTarget modelElementChangeRightTarget = (ModelElementChangeRightTarget)theEObject;
				T result = caseModelElementChangeRightTarget(modelElementChangeRightTarget);
				if (result == null)
					result = caseModelElementChange(modelElementChangeRightTarget);
				if (result == null)
					result = caseDiffElement(modelElementChangeRightTarget);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.UPDATE_MODEL_ELEMENT: {
				UpdateModelElement updateModelElement = (UpdateModelElement)theEObject;
				T result = caseUpdateModelElement(updateModelElement);
				if (result == null)
					result = caseModelElementChange(updateModelElement);
				if (result == null)
					result = caseDiffElement(updateModelElement);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.MOVE_MODEL_ELEMENT: {
				MoveModelElement moveModelElement = (MoveModelElement)theEObject;
				T result = caseMoveModelElement(moveModelElement);
				if (result == null)
					result = caseUpdateModelElement(moveModelElement);
				if (result == null)
					result = caseModelElementChange(moveModelElement);
				if (result == null)
					result = caseDiffElement(moveModelElement);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.UPDATE_CONTAINMENT_FEATURE: {
				UpdateContainmentFeature updateContainmentFeature = (UpdateContainmentFeature)theEObject;
				T result = caseUpdateContainmentFeature(updateContainmentFeature);
				if (result == null)
					result = caseMoveModelElement(updateContainmentFeature);
				if (result == null)
					result = caseUpdateModelElement(updateContainmentFeature);
				if (result == null)
					result = caseModelElementChange(updateContainmentFeature);
				if (result == null)
					result = caseDiffElement(updateContainmentFeature);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.ATTRIBUTE_CHANGE: {
				AttributeChange attributeChange = (AttributeChange)theEObject;
				T result = caseAttributeChange(attributeChange);
				if (result == null)
					result = caseDiffElement(attributeChange);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET: {
				AttributeChangeLeftTarget attributeChangeLeftTarget = (AttributeChangeLeftTarget)theEObject;
				T result = caseAttributeChangeLeftTarget(attributeChangeLeftTarget);
				if (result == null)
					result = caseAttributeChange(attributeChangeLeftTarget);
				if (result == null)
					result = caseDiffElement(attributeChangeLeftTarget);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET: {
				AttributeChangeRightTarget attributeChangeRightTarget = (AttributeChangeRightTarget)theEObject;
				T result = caseAttributeChangeRightTarget(attributeChangeRightTarget);
				if (result == null)
					result = caseAttributeChange(attributeChangeRightTarget);
				if (result == null)
					result = caseDiffElement(attributeChangeRightTarget);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.UPDATE_ATTRIBUTE: {
				UpdateAttribute updateAttribute = (UpdateAttribute)theEObject;
				T result = caseUpdateAttribute(updateAttribute);
				if (result == null)
					result = caseAttributeChange(updateAttribute);
				if (result == null)
					result = caseDiffElement(updateAttribute);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.REFERENCE_CHANGE: {
				ReferenceChange referenceChange = (ReferenceChange)theEObject;
				T result = caseReferenceChange(referenceChange);
				if (result == null)
					result = caseDiffElement(referenceChange);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.REFERENCE_CHANGE_LEFT_TARGET: {
				ReferenceChangeLeftTarget referenceChangeLeftTarget = (ReferenceChangeLeftTarget)theEObject;
				T result = caseReferenceChangeLeftTarget(referenceChangeLeftTarget);
				if (result == null)
					result = caseReferenceChange(referenceChangeLeftTarget);
				if (result == null)
					result = caseDiffElement(referenceChangeLeftTarget);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET: {
				ReferenceChangeRightTarget referenceChangeRightTarget = (ReferenceChangeRightTarget)theEObject;
				T result = caseReferenceChangeRightTarget(referenceChangeRightTarget);
				if (result == null)
					result = caseReferenceChange(referenceChangeRightTarget);
				if (result == null)
					result = caseDiffElement(referenceChangeRightTarget);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.UPDATE_REFERENCE: {
				UpdateReference updateReference = (UpdateReference)theEObject;
				T result = caseUpdateReference(updateReference);
				if (result == null)
					result = caseReferenceChange(updateReference);
				if (result == null)
					result = caseDiffElement(updateReference);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.REFERENCE_ORDER_CHANGE: {
				ReferenceOrderChange referenceOrderChange = (ReferenceOrderChange)theEObject;
				T result = caseReferenceOrderChange(referenceOrderChange);
				if (result == null)
					result = caseReferenceChange(referenceOrderChange);
				if (result == null)
					result = caseDiffElement(referenceOrderChange);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.ABSTRACT_DIFF_EXTENSION: {
				AbstractDiffExtension abstractDiffExtension = (AbstractDiffExtension)theEObject;
				T result = caseAbstractDiffExtension(abstractDiffExtension);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.RESOURCE_DIFF: {
				ResourceDiff resourceDiff = (ResourceDiff)theEObject;
				T result = caseResourceDiff(resourceDiff);
				if (result == null)
					result = caseDiffElement(resourceDiff);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.RESOURCE_DEPENDENCY_CHANGE: {
				ResourceDependencyChange resourceDependencyChange = (ResourceDependencyChange)theEObject;
				T result = caseResourceDependencyChange(resourceDependencyChange);
				if (result == null)
					result = caseResourceDiff(resourceDependencyChange);
				if (result == null)
					result = caseDiffElement(resourceDependencyChange);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.RESOURCE_DEPENDENCY_CHANGE_LEFT_TARGET: {
				ResourceDependencyChangeLeftTarget resourceDependencyChangeLeftTarget = (ResourceDependencyChangeLeftTarget)theEObject;
				T result = caseResourceDependencyChangeLeftTarget(resourceDependencyChangeLeftTarget);
				if (result == null)
					result = caseResourceDependencyChange(resourceDependencyChangeLeftTarget);
				if (result == null)
					result = caseResourceDiff(resourceDependencyChangeLeftTarget);
				if (result == null)
					result = caseDiffElement(resourceDependencyChangeLeftTarget);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.RESOURCE_DEPENDENCY_CHANGE_RIGHT_TARGET: {
				ResourceDependencyChangeRightTarget resourceDependencyChangeRightTarget = (ResourceDependencyChangeRightTarget)theEObject;
				T result = caseResourceDependencyChangeRightTarget(resourceDependencyChangeRightTarget);
				if (result == null)
					result = caseResourceDependencyChange(resourceDependencyChangeRightTarget);
				if (result == null)
					result = caseResourceDiff(resourceDependencyChangeRightTarget);
				if (result == null)
					result = caseDiffElement(resourceDependencyChangeRightTarget);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			default:
				return defaultCase(theEObject);
		}
	}

} // DiffSwitch
