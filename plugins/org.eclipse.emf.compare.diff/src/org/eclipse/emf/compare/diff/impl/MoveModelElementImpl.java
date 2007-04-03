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

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.compare.diff.DiffPackage;
import org.eclipse.emf.compare.diff.MoveModelElement;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Move Model Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.impl.MoveModelElementImpl#getLeftParent <em>Left Parent</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.impl.MoveModelElementImpl#getRightParent <em>Right Parent</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MoveModelElementImpl extends UpdateModelElementImpl implements
		MoveModelElement {
	/**
	 * The cached value of the '{@link #getLeftParent() <em>Left Parent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeftParent()
	 * @generated
	 * @ordered
	 */
	protected EObject leftParent = null;

	/**
	 * The cached value of the '{@link #getRightParent() <em>Right Parent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRightParent()
	 * @generated
	 * @ordered
	 */
	protected EObject rightParent = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MoveModelElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return DiffPackage.Literals.MOVE_MODEL_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getLeftParent() {
		if (leftParent != null && leftParent.eIsProxy()) {
			InternalEObject oldLeftParent = (InternalEObject) leftParent;
			leftParent = eResolveProxy(oldLeftParent);
			if (leftParent != oldLeftParent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DiffPackage.MOVE_MODEL_ELEMENT__LEFT_PARENT,
							oldLeftParent, leftParent));
			}
		}
		return leftParent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetLeftParent() {
		return leftParent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLeftParent(EObject newLeftParent) {
		EObject oldLeftParent = leftParent;
		leftParent = newLeftParent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffPackage.MOVE_MODEL_ELEMENT__LEFT_PARENT, oldLeftParent,
					leftParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getRightParent() {
		if (rightParent != null && rightParent.eIsProxy()) {
			InternalEObject oldRightParent = (InternalEObject) rightParent;
			rightParent = eResolveProxy(oldRightParent);
			if (rightParent != oldRightParent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DiffPackage.MOVE_MODEL_ELEMENT__RIGHT_PARENT,
							oldRightParent, rightParent));
			}
		}
		return rightParent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetRightParent() {
		return rightParent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRightParent(EObject newRightParent) {
		EObject oldRightParent = rightParent;
		rightParent = newRightParent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffPackage.MOVE_MODEL_ELEMENT__RIGHT_PARENT,
					oldRightParent, rightParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case DiffPackage.MOVE_MODEL_ELEMENT__LEFT_PARENT:
			if (resolve)
				return getLeftParent();
			return basicGetLeftParent();
		case DiffPackage.MOVE_MODEL_ELEMENT__RIGHT_PARENT:
			if (resolve)
				return getRightParent();
			return basicGetRightParent();
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
		case DiffPackage.MOVE_MODEL_ELEMENT__LEFT_PARENT:
			setLeftParent((EObject) newValue);
			return;
		case DiffPackage.MOVE_MODEL_ELEMENT__RIGHT_PARENT:
			setRightParent((EObject) newValue);
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
		case DiffPackage.MOVE_MODEL_ELEMENT__LEFT_PARENT:
			setLeftParent((EObject) null);
			return;
		case DiffPackage.MOVE_MODEL_ELEMENT__RIGHT_PARENT:
			setRightParent((EObject) null);
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
		case DiffPackage.MOVE_MODEL_ELEMENT__LEFT_PARENT:
			return leftParent != null;
		case DiffPackage.MOVE_MODEL_ELEMENT__RIGHT_PARENT:
			return rightParent != null;
		}
		return super.eIsSet(featureID);
	}

} //MoveModelElementImpl