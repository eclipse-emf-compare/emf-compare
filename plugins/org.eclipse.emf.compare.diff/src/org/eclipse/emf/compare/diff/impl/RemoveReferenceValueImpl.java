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

import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.DiffPackage;
import org.eclipse.emf.compare.diff.RemoveReferenceValue;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Remove Reference Value</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.impl.RemoveReferenceValueImpl#getLeftRemovedTarget <em>Left Removed Target</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.impl.RemoveReferenceValueImpl#getRightRemovedTarget <em>Right Removed Target</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RemoveReferenceValueImpl extends ReferenceChangeImpl implements
		RemoveReferenceValue {
	/**
	 * The cached value of the '{@link #getLeftRemovedTarget() <em>Left Removed Target</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeftRemovedTarget()
	 * @generated
	 * @ordered
	 */
	protected EList leftRemovedTarget = null;

	/**
	 * The cached value of the '{@link #getRightRemovedTarget() <em>Right Removed Target</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRightRemovedTarget()
	 * @generated
	 * @ordered
	 */
	protected EList rightRemovedTarget = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RemoveReferenceValueImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return DiffPackage.Literals.REMOVE_REFERENCE_VALUE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getLeftRemovedTarget() {
		if (leftRemovedTarget == null) {
			leftRemovedTarget = new EObjectResolvingEList(EObject.class, this, DiffPackage.REMOVE_REFERENCE_VALUE__LEFT_REMOVED_TARGET);
		}
		return leftRemovedTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getRightRemovedTarget() {
		if (rightRemovedTarget == null) {
			rightRemovedTarget = new EObjectResolvingEList(EObject.class, this, DiffPackage.REMOVE_REFERENCE_VALUE__RIGHT_REMOVED_TARGET);
		}
		return rightRemovedTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.REMOVE_REFERENCE_VALUE__LEFT_REMOVED_TARGET:
				return getLeftRemovedTarget();
			case DiffPackage.REMOVE_REFERENCE_VALUE__RIGHT_REMOVED_TARGET:
				return getRightRemovedTarget();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DiffPackage.REMOVE_REFERENCE_VALUE__LEFT_REMOVED_TARGET:
				getLeftRemovedTarget().clear();
				getLeftRemovedTarget().addAll((Collection)newValue);
				return;
			case DiffPackage.REMOVE_REFERENCE_VALUE__RIGHT_REMOVED_TARGET:
				getRightRemovedTarget().clear();
				getRightRemovedTarget().addAll((Collection)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case DiffPackage.REMOVE_REFERENCE_VALUE__LEFT_REMOVED_TARGET:
				getLeftRemovedTarget().clear();
				return;
			case DiffPackage.REMOVE_REFERENCE_VALUE__RIGHT_REMOVED_TARGET:
				getRightRemovedTarget().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DiffPackage.REMOVE_REFERENCE_VALUE__LEFT_REMOVED_TARGET:
				return leftRemovedTarget != null && !leftRemovedTarget.isEmpty();
			case DiffPackage.REMOVE_REFERENCE_VALUE__RIGHT_REMOVED_TARGET:
				return rightRemovedTarget != null && !rightRemovedTarget.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //RemoveReferenceValueImpl