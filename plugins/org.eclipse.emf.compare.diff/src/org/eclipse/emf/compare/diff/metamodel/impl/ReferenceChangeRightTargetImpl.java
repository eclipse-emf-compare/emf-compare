/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
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
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Reference Change Right Target</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeRightTargetImpl#getRightAddedTarget <em>Right Added Target</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeRightTargetImpl#getLeftAddedTarget <em>Left Added Target</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ReferenceChangeRightTargetImpl extends ReferenceChangeImpl implements ReferenceChangeRightTarget {
	/**
	 * The cached value of the '{@link #getLeftAddedTarget() <em>Left Added Target</em>}' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLeftAddedTarget()
	 * @generated
	 * @ordered
	 */
	protected EObject leftAddedTarget;

	/**
	 * The cached value of the '{@link #getRightAddedTarget() <em>Right Added Target</em>}' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRightAddedTarget()
	 * @generated
	 * @ordered
	 */
	protected EObject rightAddedTarget;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected ReferenceChangeRightTargetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetLeftAddedTarget() {
		return leftAddedTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetRightAddedTarget() {
		return rightAddedTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_ADDED_TARGET:
				if (resolve)
					return getRightAddedTarget();
				return basicGetRightAddedTarget();
			case DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__LEFT_ADDED_TARGET:
				if (resolve)
					return getLeftAddedTarget();
				return basicGetLeftAddedTarget();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_ADDED_TARGET:
				return rightAddedTarget != null;
			case DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__LEFT_ADDED_TARGET:
				return leftAddedTarget != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_ADDED_TARGET:
				setRightAddedTarget((EObject)newValue);
				return;
			case DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__LEFT_ADDED_TARGET:
				setLeftAddedTarget((EObject)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_ADDED_TARGET:
				setRightAddedTarget((EObject)null);
				return;
			case DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__LEFT_ADDED_TARGET:
				setLeftAddedTarget((EObject)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getLeftAddedTarget() {
		if (leftAddedTarget != null && leftAddedTarget.eIsProxy()) {
			InternalEObject oldLeftAddedTarget = (InternalEObject)leftAddedTarget;
			leftAddedTarget = eResolveProxy(oldLeftAddedTarget);
			if (leftAddedTarget != oldLeftAddedTarget) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__LEFT_ADDED_TARGET, oldLeftAddedTarget,
							leftAddedTarget));
			}
		}
		return leftAddedTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getRightAddedTarget() {
		if (rightAddedTarget != null && rightAddedTarget.eIsProxy()) {
			InternalEObject oldRightAddedTarget = (InternalEObject)rightAddedTarget;
			rightAddedTarget = eResolveProxy(oldRightAddedTarget);
			if (rightAddedTarget != oldRightAddedTarget) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_ADDED_TARGET,
							oldRightAddedTarget, rightAddedTarget));
			}
		}
		return rightAddedTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setLeftAddedTarget(EObject newLeftAddedTarget) {
		EObject oldLeftAddedTarget = leftAddedTarget;
		leftAddedTarget = newLeftAddedTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__LEFT_ADDED_TARGET, oldLeftAddedTarget,
					leftAddedTarget));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setRightAddedTarget(EObject newRightAddedTarget) {
		EObject oldRightAddedTarget = rightAddedTarget;
		rightAddedTarget = newRightAddedTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_ADDED_TARGET, oldRightAddedTarget,
					rightAddedTarget));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.REFERENCE_CHANGE_RIGHT_TARGET;
	}

} // ReferenceChangeRightTargetImpl
