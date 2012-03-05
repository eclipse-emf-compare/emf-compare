/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
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
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.EMFCompareDiffMessages;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.compare.match.internal.statistic.NameSimilarity;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Update Reference</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateReferenceImpl#getLeftTarget <em>Left Target</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateReferenceImpl#getRightTarget <em>Right Target</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public class UpdateReferenceImpl extends ReferenceChangeImpl implements UpdateReference {
	/**
	 * The cached value of the '{@link #getLeftTarget() <em>Left Target</em>}' reference.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #getLeftTarget()
	 * @generated
	 * @ordered
	 */
	protected EObject leftTarget;

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
	 * @generated
	 */
	protected UpdateReferenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.UPDATE_REFERENCE;
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
							DiffPackage.UPDATE_REFERENCE__LEFT_TARGET, oldLeftTarget, leftTarget));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DiffPackage.UPDATE_REFERENCE__LEFT_TARGET,
					oldLeftTarget, leftTarget));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getRightTarget() {
		if (rightTarget != null && rightTarget.eIsProxy()) {
			InternalEObject oldRightTarget = (InternalEObject)rightTarget;
			rightTarget = eResolveProxy(oldRightTarget);
			if (rightTarget != oldRightTarget) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DiffPackage.UPDATE_REFERENCE__RIGHT_TARGET, oldRightTarget, rightTarget));
			}
		}
		return rightTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetRightTarget() {
		return rightTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setRightTarget(EObject newRightTarget) {
		EObject oldRightTarget = rightTarget;
		rightTarget = newRightTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiffPackage.UPDATE_REFERENCE__RIGHT_TARGET,
					oldRightTarget, rightTarget));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.UPDATE_REFERENCE__LEFT_TARGET:
				if (resolve)
					return getLeftTarget();
				return basicGetLeftTarget();
			case DiffPackage.UPDATE_REFERENCE__RIGHT_TARGET:
				if (resolve)
					return getRightTarget();
				return basicGetRightTarget();
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
			case DiffPackage.UPDATE_REFERENCE__LEFT_TARGET:
				setLeftTarget((EObject)newValue);
				return;
			case DiffPackage.UPDATE_REFERENCE__RIGHT_TARGET:
				setRightTarget((EObject)newValue);
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
			case DiffPackage.UPDATE_REFERENCE__LEFT_TARGET:
				setLeftTarget((EObject)null);
				return;
			case DiffPackage.UPDATE_REFERENCE__RIGHT_TARGET:
				setRightTarget((EObject)null);
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
			case DiffPackage.UPDATE_REFERENCE__LEFT_TARGET:
				return leftTarget != null;
			case DiffPackage.UPDATE_REFERENCE__RIGHT_TARGET:
				return rightTarget != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @generated NOT
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffElementImpl#toString()
	 */
	@Override
	public String toString() {
		String toString = null;
		if (isRemote()) {
			try {
				toString = EMFCompareDiffMessages.getString(
						"RemoteUpdateReferenceImpl.ToString", //$NON-NLS-1$
						reference.getName(), NameSimilarity.findName(leftElement),
						leftElement.eGet(reference), rightElement.eGet(reference));
			} catch (final FactoryException e) {
				toString = EMFCompareDiffMessages.getString(
						"RemoteUpdateReferenceImpl.ToString", //$NON-NLS-1$
						reference.getName(), leftElement.eClass().getName(), leftElement.eGet(reference),
						rightElement.eGet(reference));
			}
		} else {
			try {
				toString = EMFCompareDiffMessages.getString(
						"UpdateReferenceImpl.ToString", //$NON-NLS-1$
						reference.getName(), NameSimilarity.findName(leftElement),
						rightElement.eGet(reference), leftElement.eGet(reference));
			} catch (final FactoryException e) {
				toString = EMFCompareDiffMessages.getString(
						"UpdateReferenceImpl.ToString", //$NON-NLS-1$
						reference.getName(), leftElement.eClass().getName(), rightElement.eGet(reference),
						leftElement.eGet(reference));
			}
		}
		return toString;
	}

} // UpdateReferenceImpl
