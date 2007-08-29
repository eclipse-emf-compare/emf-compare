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
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Conflicting Diff Element</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.ConflictingDiffElementImpl#getLeftParent <em>Left Parent</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.ConflictingDiffElementImpl#getRightParent <em>Right Parent</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.ConflictingDiffElementImpl#getOriginElement <em>Origin Element</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
@SuppressWarnings("nls")
public class ConflictingDiffElementImpl extends DiffElementImpl implements
		ConflictingDiffElement {
	/**
	 * The cached value of the '{@link #getLeftParent() <em>Left Parent</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getLeftParent()
	 * @generated
	 * @ordered
	 */
	protected EObject leftParent;

	/**
	 * The cached value of the '{@link #getRightParent() <em>Right Parent</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getRightParent()
	 * @generated
	 * @ordered
	 */
	protected EObject rightParent;

	/**
	 * The cached value of the '{@link #getOriginElement() <em>Origin Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginElement()
	 * @generated
	 * @ordered
	 */
	protected EObject originElement;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected ConflictingDiffElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.CONFLICTING_DIFF_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getLeftParent() {
		if (leftParent != null && leftParent.eIsProxy()) {
			InternalEObject oldLeftParent = (InternalEObject) leftParent;
			leftParent = eResolveProxy(oldLeftParent);
			if (leftParent != oldLeftParent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_PARENT,
							oldLeftParent, leftParent));
			}
		}
		return leftParent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetLeftParent() {
		return leftParent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setLeftParent(EObject newLeftParent) {
		EObject oldLeftParent = leftParent;
		leftParent = newLeftParent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_PARENT,
					oldLeftParent, leftParent));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getRightParent() {
		if (rightParent != null && rightParent.eIsProxy()) {
			InternalEObject oldRightParent = (InternalEObject) rightParent;
			rightParent = eResolveProxy(oldRightParent);
			if (rightParent != oldRightParent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_PARENT,
							oldRightParent, rightParent));
			}
		}
		return rightParent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetRightParent() {
		return rightParent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setRightParent(EObject newRightParent) {
		EObject oldRightParent = rightParent;
		rightParent = newRightParent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_PARENT,
					oldRightParent, rightParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getOriginElement() {
		if (originElement != null && originElement.eIsProxy()) {
			InternalEObject oldOriginElement = (InternalEObject) originElement;
			originElement = eResolveProxy(oldOriginElement);
			if (originElement != oldOriginElement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(
							this,
							Notification.RESOLVE,
							DiffPackage.CONFLICTING_DIFF_ELEMENT__ORIGIN_ELEMENT,
							oldOriginElement, originElement));
			}
		}
		return originElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetOriginElement() {
		return originElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginElement(EObject newOriginElement) {
		EObject oldOriginElement = originElement;
		originElement = newOriginElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffPackage.CONFLICTING_DIFF_ELEMENT__ORIGIN_ELEMENT,
					oldOriginElement, originElement));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_PARENT:
			if (resolve)
				return getLeftParent();
			return basicGetLeftParent();
		case DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_PARENT:
			if (resolve)
				return getRightParent();
			return basicGetRightParent();
		case DiffPackage.CONFLICTING_DIFF_ELEMENT__ORIGIN_ELEMENT:
			if (resolve)
				return getOriginElement();
			return basicGetOriginElement();
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
		case DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_PARENT:
			setLeftParent((EObject) newValue);
			return;
		case DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_PARENT:
			setRightParent((EObject) newValue);
			return;
		case DiffPackage.CONFLICTING_DIFF_ELEMENT__ORIGIN_ELEMENT:
			setOriginElement((EObject) newValue);
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
		case DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_PARENT:
			setLeftParent((EObject) null);
			return;
		case DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_PARENT:
			setRightParent((EObject) null);
			return;
		case DiffPackage.CONFLICTING_DIFF_ELEMENT__ORIGIN_ELEMENT:
			setOriginElement((EObject) null);
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
		case DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_PARENT:
			return leftParent != null;
		case DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_PARENT:
			return rightParent != null;
		case DiffPackage.CONFLICTING_DIFF_ELEMENT__ORIGIN_ELEMENT:
			return originElement != null;
		}
		return super.eIsSet(featureID);
	}

} // ConflictingDiffElementImpl
