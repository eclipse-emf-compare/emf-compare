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
package org.eclipse.emf.compare.diff.metamodel.impl;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.metamodel.AddReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Add Reference Value</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.AddReferenceValueImpl#getLeftAddedTarget <em>Left Added Target</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.AddReferenceValueImpl#getRightAddedTarget <em>Right Added Target</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AddReferenceValueImpl extends ReferenceChangeImpl implements AddReferenceValue {
	/**
	 * The cached value of the '{@link #getLeftAddedTarget() <em>Left Added Target</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeftAddedTarget()
	 * @generated
	 * @ordered
	 */
	protected EList leftAddedTarget = null;

	/**
	 * The cached value of the '{@link #getRightAddedTarget() <em>Right Added Target</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRightAddedTarget()
	 * @generated
	 * @ordered
	 */
	protected EList rightAddedTarget = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AddReferenceValueImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return DiffPackage.Literals.ADD_REFERENCE_VALUE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getLeftAddedTarget() {
		if (leftAddedTarget == null) {
			leftAddedTarget = new EObjectResolvingEList(EObject.class, this,
					DiffPackage.ADD_REFERENCE_VALUE__LEFT_ADDED_TARGET);
		}
		return leftAddedTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getRightAddedTarget() {
		if (rightAddedTarget == null) {
			rightAddedTarget = new EObjectResolvingEList(EObject.class, this,
					DiffPackage.ADD_REFERENCE_VALUE__RIGHT_ADDED_TARGET);
		}
		return rightAddedTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.ADD_REFERENCE_VALUE__LEFT_ADDED_TARGET:
				return getLeftAddedTarget();
			case DiffPackage.ADD_REFERENCE_VALUE__RIGHT_ADDED_TARGET:
				return getRightAddedTarget();
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
			case DiffPackage.ADD_REFERENCE_VALUE__LEFT_ADDED_TARGET:
				getLeftAddedTarget().clear();
				getLeftAddedTarget().addAll((Collection)newValue);
				return;
			case DiffPackage.ADD_REFERENCE_VALUE__RIGHT_ADDED_TARGET:
				getRightAddedTarget().clear();
				getRightAddedTarget().addAll((Collection)newValue);
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
			case DiffPackage.ADD_REFERENCE_VALUE__LEFT_ADDED_TARGET:
				getLeftAddedTarget().clear();
				return;
			case DiffPackage.ADD_REFERENCE_VALUE__RIGHT_ADDED_TARGET:
				getRightAddedTarget().clear();
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
			case DiffPackage.ADD_REFERENCE_VALUE__LEFT_ADDED_TARGET:
				return leftAddedTarget != null && !leftAddedTarget.isEmpty();
			case DiffPackage.ADD_REFERENCE_VALUE__RIGHT_ADDED_TARGET:
				return rightAddedTarget != null && !rightAddedTarget.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //AddReferenceValueImpl