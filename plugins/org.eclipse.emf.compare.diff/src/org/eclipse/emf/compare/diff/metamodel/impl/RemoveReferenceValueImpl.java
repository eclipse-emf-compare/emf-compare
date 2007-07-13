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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Remove Reference Value</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoveReferenceValueImpl#getLeftRemovedTarget <em>Left Removed Target</em>}</li>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoveReferenceValueImpl#getRightRemovedTarget <em>Right Removed Target</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
@SuppressWarnings("nls")
public class RemoveReferenceValueImpl extends ReferenceChangeImpl implements RemoveReferenceValue {
	/**
	 * The cached value of the '{@link #getLeftRemovedTarget() <em>Left Removed Target</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getLeftRemovedTarget()
	 * @generated
	 * @ordered
	 */
	protected EObject leftRemovedTarget = null;

	/**
	 * The cached value of the '{@link #getRightRemovedTarget() <em>Right Removed Target</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getRightRemovedTarget()
	 * @generated
	 * @ordered
	 */
	protected EObject rightRemovedTarget = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected RemoveReferenceValueImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.REMOVE_REFERENCE_VALUE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject getLeftRemovedTarget() {
		if (leftRemovedTarget != null && leftRemovedTarget.eIsProxy()) {
			InternalEObject oldLeftRemovedTarget = (InternalEObject)leftRemovedTarget;
			leftRemovedTarget = eResolveProxy(oldLeftRemovedTarget);
			if (leftRemovedTarget != oldLeftRemovedTarget) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DiffPackage.REMOVE_REFERENCE_VALUE__LEFT_REMOVED_TARGET, oldLeftRemovedTarget, leftRemovedTarget));
			}
		}
		return leftRemovedTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject basicGetLeftRemovedTarget() {
		return leftRemovedTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLeftRemovedTarget(EObject newLeftRemovedTarget) {
		EObject oldLeftRemovedTarget = leftRemovedTarget;
		leftRemovedTarget = newLeftRemovedTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiffPackage.REMOVE_REFERENCE_VALUE__LEFT_REMOVED_TARGET, oldLeftRemovedTarget, leftRemovedTarget));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject getRightRemovedTarget() {
		if (rightRemovedTarget != null && rightRemovedTarget.eIsProxy()) {
			InternalEObject oldRightRemovedTarget = (InternalEObject)rightRemovedTarget;
			rightRemovedTarget = eResolveProxy(oldRightRemovedTarget);
			if (rightRemovedTarget != oldRightRemovedTarget) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DiffPackage.REMOVE_REFERENCE_VALUE__RIGHT_REMOVED_TARGET, oldRightRemovedTarget, rightRemovedTarget));
			}
		}
		return rightRemovedTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject basicGetRightRemovedTarget() {
		return rightRemovedTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setRightRemovedTarget(EObject newRightRemovedTarget) {
		EObject oldRightRemovedTarget = rightRemovedTarget;
		rightRemovedTarget = newRightRemovedTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiffPackage.REMOVE_REFERENCE_VALUE__RIGHT_REMOVED_TARGET, oldRightRemovedTarget, rightRemovedTarget));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.REMOVE_REFERENCE_VALUE__LEFT_REMOVED_TARGET:
				if (resolve)
					return getLeftRemovedTarget();
				return basicGetLeftRemovedTarget();
			case DiffPackage.REMOVE_REFERENCE_VALUE__RIGHT_REMOVED_TARGET:
				if (resolve)
					return getRightRemovedTarget();
				return basicGetRightRemovedTarget();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DiffPackage.REMOVE_REFERENCE_VALUE__LEFT_REMOVED_TARGET:
				setLeftRemovedTarget((EObject)newValue);
				return;
			case DiffPackage.REMOVE_REFERENCE_VALUE__RIGHT_REMOVED_TARGET:
				setRightRemovedTarget((EObject)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case DiffPackage.REMOVE_REFERENCE_VALUE__LEFT_REMOVED_TARGET:
				setLeftRemovedTarget((EObject)null);
				return;
			case DiffPackage.REMOVE_REFERENCE_VALUE__RIGHT_REMOVED_TARGET:
				setRightRemovedTarget((EObject)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DiffPackage.REMOVE_REFERENCE_VALUE__LEFT_REMOVED_TARGET:
				return leftRemovedTarget != null;
			case DiffPackage.REMOVE_REFERENCE_VALUE__RIGHT_REMOVED_TARGET:
				return rightRemovedTarget != null;
		}
		return super.eIsSet(featureID);
	}

} // RemoveReferenceValueImpl
