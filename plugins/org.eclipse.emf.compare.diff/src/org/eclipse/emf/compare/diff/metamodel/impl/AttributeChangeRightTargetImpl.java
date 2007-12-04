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
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Attribute Change Right Target</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeRightTargetImpl#getRightTarget <em>Right Target</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class AttributeChangeRightTargetImpl extends AttributeChangeImpl implements AttributeChangeRightTarget {
	/**
	 * The cached value of the '{@link #getRightTarget() <em>Right Target</em>}' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRightTarget()
	 * @generated
	 * @ordered
	 */
	protected EObject rightTarget;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected AttributeChangeRightTargetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject basicGetRightTarget() {
		return rightTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_TARGET:
				if (resolve)
					return getRightTarget();
				return basicGetRightTarget();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_TARGET:
				return rightTarget != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_TARGET:
				setRightTarget((EObject)newValue);
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
			case DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_TARGET:
				setRightTarget((EObject)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject getRightTarget() {
		if (rightTarget != null && rightTarget.eIsProxy()) {
			InternalEObject oldRightTarget = (InternalEObject)rightTarget;
			rightTarget = eResolveProxy(oldRightTarget);
			if (rightTarget != oldRightTarget) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_TARGET, oldRightTarget,
							rightTarget));
			}
		}
		return rightTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setRightTarget(EObject newRightTarget) {
		EObject oldRightTarget = rightTarget;
		rightTarget = newRightTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_TARGET, oldRightTarget, rightTarget));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.ATTRIBUTE_CHANGE_RIGHT_TARGET;
	}

} // AttributeChangeRightTargetImpl
