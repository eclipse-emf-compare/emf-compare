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
package org.eclipse.emf.compare.diff.metamodel.util;

import java.util.List;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.AddAttribute;
import org.eclipse.emf.compare.diff.metamodel.AddModelElement;
import org.eclipse.emf.compare.diff.metamodel.AddReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.AttributeChange;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.GenericDiffElement;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChange;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot;
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
import org.eclipse.emf.compare.diff.metamodel.RemoteUpdateUniqueReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.RemoveAttribute;
import org.eclipse.emf.compare.diff.metamodel.RemoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateModelElement;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance hierarchy. It supports the call
 * {@link #doSwitch(EObject) doSwitch(object)} to invoke the <code>caseXXX</code> method for each class of
 * the model, starting with the actual class of the object and proceeding up the inheritance hierarchy until a
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
	 * Returns the result of interpreting the object as an instance of '<em>Add Attribute</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Add Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseAddAttribute(AddAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Add Model Element</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Add Model Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseAddModelElement(AddModelElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Add Reference Value</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Add Reference Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseAddReferenceValue(AddReferenceValue object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Element</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseDiffElement(DiffElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Group</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Group</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseDiffGroup(DiffGroup object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseDiffModel(DiffModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Generic Diff Element</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Generic Diff Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseGenericDiffElement(GenericDiffElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model Element Change</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
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
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
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
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
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
	 * Returns the result of interpreting the object as an instance of '<em>Model Input Snapshot</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model Input Snapshot</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseModelInputSnapshot(ModelInputSnapshot object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Remote Add Attribute</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Remote Add Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseRemoteAddAttribute(RemoteAddAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Remote Add Model Element</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Remote Add Model Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseRemoteAddModelElement(RemoteAddModelElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Remote Add Reference Value</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Remote Add Reference Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseRemoteAddReferenceValue(RemoteAddReferenceValue object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Remote Move Model Element</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Remote Move Model Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseRemoteMoveModelElement(RemoteMoveModelElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Remote Remove Attribute</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Remote Remove Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseRemoteRemoveAttribute(RemoteRemoveAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Remote Remove Model Element</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Remote Remove Model Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseRemoteRemoveModelElement(RemoteRemoveModelElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Remote Remove Reference Value</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Remote Remove Reference Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseRemoteRemoveReferenceValue(RemoteRemoveReferenceValue object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Remote Update Attribute</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Remote Update Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseRemoteUpdateAttribute(RemoteUpdateAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Remote Update Unique Reference Value</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Remote Update Unique Reference Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseRemoteUpdateUniqueReferenceValue(RemoteUpdateUniqueReferenceValue object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Remove Attribute</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Remove Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseRemoveAttribute(RemoveAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Remove Model Element</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Remove Model Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseRemoveModelElement(RemoveModelElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Remove Reference Value</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Remove Reference Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseRemoveReferenceValue(RemoveReferenceValue object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Update Model Element</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
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
	 * Returns the result of interpreting the object as an instance of '<em>Update Unique Reference Value</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Update Unique Reference Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	@SuppressWarnings("unused")
	public T caseUpdateUniqueReferenceValue(UpdateUniqueReferenceValue object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch, but this is the last case anyway. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
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
		}
		List<EClass> eSuperTypes = theEClass.getESuperTypes();
		return eSuperTypes.isEmpty() ? defaultCase(theEObject) : doSwitch(eSuperTypes.get(0), theEObject);
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
			case DiffPackage.DIFF_ELEMENT: {
				DiffElement diffElement = (DiffElement)theEObject;
				T result = caseDiffElement(diffElement);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.GENERIC_DIFF_ELEMENT: {
				GenericDiffElement genericDiffElement = (GenericDiffElement)theEObject;
				T result = caseGenericDiffElement(genericDiffElement);
				if (result == null)
					result = caseDiffElement(genericDiffElement);
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
			case DiffPackage.MODEL_INPUT_SNAPSHOT: {
				ModelInputSnapshot modelInputSnapshot = (ModelInputSnapshot)theEObject;
				T result = caseModelInputSnapshot(modelInputSnapshot);
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
			case DiffPackage.ADD_MODEL_ELEMENT: {
				AddModelElement addModelElement = (AddModelElement)theEObject;
				T result = caseAddModelElement(addModelElement);
				if (result == null)
					result = caseModelElementChangeRightTarget(addModelElement);
				if (result == null)
					result = caseModelElementChange(addModelElement);
				if (result == null)
					result = caseDiffElement(addModelElement);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.REMOTE_ADD_MODEL_ELEMENT: {
				RemoteAddModelElement remoteAddModelElement = (RemoteAddModelElement)theEObject;
				T result = caseRemoteAddModelElement(remoteAddModelElement);
				if (result == null)
					result = caseModelElementChangeLeftTarget(remoteAddModelElement);
				if (result == null)
					result = caseModelElementChange(remoteAddModelElement);
				if (result == null)
					result = caseDiffElement(remoteAddModelElement);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.REMOVE_MODEL_ELEMENT: {
				RemoveModelElement removeModelElement = (RemoveModelElement)theEObject;
				T result = caseRemoveModelElement(removeModelElement);
				if (result == null)
					result = caseModelElementChangeLeftTarget(removeModelElement);
				if (result == null)
					result = caseModelElementChange(removeModelElement);
				if (result == null)
					result = caseDiffElement(removeModelElement);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.REMOTE_REMOVE_MODEL_ELEMENT: {
				RemoteRemoveModelElement remoteRemoveModelElement = (RemoteRemoveModelElement)theEObject;
				T result = caseRemoteRemoveModelElement(remoteRemoveModelElement);
				if (result == null)
					result = caseModelElementChangeRightTarget(remoteRemoveModelElement);
				if (result == null)
					result = caseModelElementChange(remoteRemoveModelElement);
				if (result == null)
					result = caseDiffElement(remoteRemoveModelElement);
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
			case DiffPackage.REMOTE_MOVE_MODEL_ELEMENT: {
				RemoteMoveModelElement remoteMoveModelElement = (RemoteMoveModelElement)theEObject;
				T result = caseRemoteMoveModelElement(remoteMoveModelElement);
				if (result == null)
					result = caseMoveModelElement(remoteMoveModelElement);
				if (result == null)
					result = caseUpdateModelElement(remoteMoveModelElement);
				if (result == null)
					result = caseModelElementChange(remoteMoveModelElement);
				if (result == null)
					result = caseDiffElement(remoteMoveModelElement);
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
			case DiffPackage.ADD_ATTRIBUTE: {
				AddAttribute addAttribute = (AddAttribute)theEObject;
				T result = caseAddAttribute(addAttribute);
				if (result == null)
					result = caseAttributeChangeRightTarget(addAttribute);
				if (result == null)
					result = caseAttributeChange(addAttribute);
				if (result == null)
					result = caseDiffElement(addAttribute);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.REMOTE_ADD_ATTRIBUTE: {
				RemoteAddAttribute remoteAddAttribute = (RemoteAddAttribute)theEObject;
				T result = caseRemoteAddAttribute(remoteAddAttribute);
				if (result == null)
					result = caseAttributeChangeLeftTarget(remoteAddAttribute);
				if (result == null)
					result = caseAttributeChange(remoteAddAttribute);
				if (result == null)
					result = caseDiffElement(remoteAddAttribute);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.REMOVE_ATTRIBUTE: {
				RemoveAttribute removeAttribute = (RemoveAttribute)theEObject;
				T result = caseRemoveAttribute(removeAttribute);
				if (result == null)
					result = caseAttributeChangeLeftTarget(removeAttribute);
				if (result == null)
					result = caseAttributeChange(removeAttribute);
				if (result == null)
					result = caseDiffElement(removeAttribute);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.REMOTE_REMOVE_ATTRIBUTE: {
				RemoteRemoveAttribute remoteRemoveAttribute = (RemoteRemoveAttribute)theEObject;
				T result = caseRemoteRemoveAttribute(remoteRemoveAttribute);
				if (result == null)
					result = caseAttributeChangeRightTarget(remoteRemoveAttribute);
				if (result == null)
					result = caseAttributeChange(remoteRemoveAttribute);
				if (result == null)
					result = caseDiffElement(remoteRemoveAttribute);
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
			case DiffPackage.REMOTE_UPDATE_ATTRIBUTE: {
				RemoteUpdateAttribute remoteUpdateAttribute = (RemoteUpdateAttribute)theEObject;
				T result = caseRemoteUpdateAttribute(remoteUpdateAttribute);
				if (result == null)
					result = caseUpdateAttribute(remoteUpdateAttribute);
				if (result == null)
					result = caseAttributeChange(remoteUpdateAttribute);
				if (result == null)
					result = caseDiffElement(remoteUpdateAttribute);
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
			case DiffPackage.ADD_REFERENCE_VALUE: {
				AddReferenceValue addReferenceValue = (AddReferenceValue)theEObject;
				T result = caseAddReferenceValue(addReferenceValue);
				if (result == null)
					result = caseReferenceChangeRightTarget(addReferenceValue);
				if (result == null)
					result = caseReferenceChange(addReferenceValue);
				if (result == null)
					result = caseDiffElement(addReferenceValue);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.REMOTE_ADD_REFERENCE_VALUE: {
				RemoteAddReferenceValue remoteAddReferenceValue = (RemoteAddReferenceValue)theEObject;
				T result = caseRemoteAddReferenceValue(remoteAddReferenceValue);
				if (result == null)
					result = caseReferenceChangeLeftTarget(remoteAddReferenceValue);
				if (result == null)
					result = caseReferenceChange(remoteAddReferenceValue);
				if (result == null)
					result = caseDiffElement(remoteAddReferenceValue);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.REMOVE_REFERENCE_VALUE: {
				RemoveReferenceValue removeReferenceValue = (RemoveReferenceValue)theEObject;
				T result = caseRemoveReferenceValue(removeReferenceValue);
				if (result == null)
					result = caseReferenceChangeLeftTarget(removeReferenceValue);
				if (result == null)
					result = caseReferenceChange(removeReferenceValue);
				if (result == null)
					result = caseDiffElement(removeReferenceValue);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.REMOTE_REMOVE_REFERENCE_VALUE: {
				RemoteRemoveReferenceValue remoteRemoveReferenceValue = (RemoteRemoveReferenceValue)theEObject;
				T result = caseRemoteRemoveReferenceValue(remoteRemoveReferenceValue);
				if (result == null)
					result = caseReferenceChangeRightTarget(remoteRemoveReferenceValue);
				if (result == null)
					result = caseReferenceChange(remoteRemoveReferenceValue);
				if (result == null)
					result = caseDiffElement(remoteRemoveReferenceValue);
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
			case DiffPackage.UPDATE_UNIQUE_REFERENCE_VALUE: {
				UpdateUniqueReferenceValue updateUniqueReferenceValue = (UpdateUniqueReferenceValue)theEObject;
				T result = caseUpdateUniqueReferenceValue(updateUniqueReferenceValue);
				if (result == null)
					result = caseUpdateReference(updateUniqueReferenceValue);
				if (result == null)
					result = caseReferenceChange(updateUniqueReferenceValue);
				if (result == null)
					result = caseDiffElement(updateUniqueReferenceValue);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.REMOTE_UPDATE_UNIQUE_REFERENCE_VALUE: {
				RemoteUpdateUniqueReferenceValue remoteUpdateUniqueReferenceValue = (RemoteUpdateUniqueReferenceValue)theEObject;
				T result = caseRemoteUpdateUniqueReferenceValue(remoteUpdateUniqueReferenceValue);
				if (result == null)
					result = caseUpdateUniqueReferenceValue(remoteUpdateUniqueReferenceValue);
				if (result == null)
					result = caseUpdateReference(remoteUpdateUniqueReferenceValue);
				if (result == null)
					result = caseReferenceChange(remoteUpdateUniqueReferenceValue);
				if (result == null)
					result = caseDiffElement(remoteUpdateUniqueReferenceValue);
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
			default:
				return defaultCase(theEObject);
		}
	}

} // DiffSwitch
