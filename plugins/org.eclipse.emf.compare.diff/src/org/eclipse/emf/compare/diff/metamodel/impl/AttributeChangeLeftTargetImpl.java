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
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Attribute Change Left Target</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeLeftTargetImpl#getLeftTarget <em>Left Target</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AttributeChangeLeftTargetImpl extends AttributeChangeImpl implements AttributeChangeLeftTarget {
	/**
	 * The cached value of the '{@link #getLeftTarget() <em>Left Target</em>}' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLeftTarget()
	 * @generated
	 * @ordered
	 */
	protected EObject leftTarget;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected AttributeChangeLeftTargetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.ATTRIBUTE_CHANGE_LEFT_TARGET;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getLeftTarget() {
		if (leftTarget != null && leftTarget.eIsProxy()) {
			InternalEObject oldLeftTarget = (InternalEObject)leftTarget;
			leftTarget = eResolveProxy(oldLeftTarget);
			if (leftTarget != oldLeftTarget) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_TARGET, oldLeftTarget, leftTarget));
			}
		}
		return leftTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetLeftTarget() {
		return leftTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setLeftTarget(EObject newLeftTarget) {
		EObject oldLeftTarget = leftTarget;
		leftTarget = newLeftTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_TARGET, oldLeftTarget, leftTarget));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_TARGET:
				if (resolve)
					return getLeftTarget();
				return basicGetLeftTarget();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_TARGET:
				setLeftTarget((EObject)newValue);
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
			case DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_TARGET:
				setLeftTarget((EObject)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_TARGET:
				return leftTarget != null;
		}
		return super.eIsSet(featureID);
	}

} // AttributeChangeLeftTargetImpl
