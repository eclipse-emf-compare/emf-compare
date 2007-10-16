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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Model</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl#getLeft <em>Left</em>}</li>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl#getOrigin <em>Origin</em>}</li>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl#getRight <em>Right</em>}</li>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl#getOwnedElements <em>Owned Elements</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
@SuppressWarnings("nls")
public class DiffModelImpl extends EObjectImpl implements DiffModel {
	/**
	 * The default value of the '{@link #getLeft() <em>Left</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getLeft()
	 * @generated
	 * @ordered
	 */
	protected static final String LEFT_EDEFAULT = null;

	/**
	 * The default value of the '{@link #getOrigin() <em>Origin</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getOrigin()
	 * @generated
	 * @ordered
	 */
	protected static final String ORIGIN_EDEFAULT = null;

	/**
	 * The default value of the '{@link #getRight() <em>Right</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getRight()
	 * @generated
	 * @ordered
	 */
	protected static final String RIGHT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLeft() <em>Left</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getLeft()
	 * @generated
	 * @ordered
	 */
	protected String left = LEFT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getOrigin() <em>Origin</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getOrigin()
	 * @generated
	 * @ordered
	 */
	protected String origin = ORIGIN_EDEFAULT;

	/**
	 * The cached value of the '{@link #getOwnedElements() <em>Owned Elements</em>}' containment reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOwnedElements()
	 * @generated
	 * @ordered
	 */
	protected EList ownedElements;

	/**
	 * The cached value of the '{@link #getRight() <em>Right</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getRight()
	 * @generated
	 * @ordered
	 */
	protected String right = RIGHT_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DiffModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.DIFF_MODEL__LEFT:
				return getLeft();
			case DiffPackage.DIFF_MODEL__ORIGIN:
				return getOrigin();
			case DiffPackage.DIFF_MODEL__RIGHT:
				return getRight();
			case DiffPackage.DIFF_MODEL__OWNED_ELEMENTS:
				return getOwnedElements();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DiffPackage.DIFF_MODEL__OWNED_ELEMENTS:
				return ((InternalEList)getOwnedElements()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DiffPackage.DIFF_MODEL__LEFT:
				return LEFT_EDEFAULT == null ? left != null : !LEFT_EDEFAULT.equals(left);
			case DiffPackage.DIFF_MODEL__ORIGIN:
				return ORIGIN_EDEFAULT == null ? origin != null : !ORIGIN_EDEFAULT.equals(origin);
			case DiffPackage.DIFF_MODEL__RIGHT:
				return RIGHT_EDEFAULT == null ? right != null : !RIGHT_EDEFAULT.equals(right);
			case DiffPackage.DIFF_MODEL__OWNED_ELEMENTS:
				return ownedElements != null && !ownedElements.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DiffPackage.DIFF_MODEL__LEFT:
				setLeft((String)newValue);
				return;
			case DiffPackage.DIFF_MODEL__ORIGIN:
				setOrigin((String)newValue);
				return;
			case DiffPackage.DIFF_MODEL__RIGHT:
				setRight((String)newValue);
				return;
			case DiffPackage.DIFF_MODEL__OWNED_ELEMENTS:
				getOwnedElements().clear();
				getOwnedElements().addAll((Collection)newValue);
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
			case DiffPackage.DIFF_MODEL__LEFT:
				setLeft(LEFT_EDEFAULT);
				return;
			case DiffPackage.DIFF_MODEL__ORIGIN:
				setOrigin(ORIGIN_EDEFAULT);
				return;
			case DiffPackage.DIFF_MODEL__RIGHT:
				setRight(RIGHT_EDEFAULT);
				return;
			case DiffPackage.DIFF_MODEL__OWNED_ELEMENTS:
				getOwnedElements().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getLeft() {
		return left;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getOwnedElements() {
		if (ownedElements == null) {
			ownedElements = new EObjectContainmentEList(DiffElement.class, this,
					DiffPackage.DIFF_MODEL__OWNED_ELEMENTS);
		}
		return ownedElements;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getRight() {
		return right;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLeft(String newLeft) {
		String oldLeft = left;
		left = newLeft;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiffPackage.DIFF_MODEL__LEFT, oldLeft, left));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOrigin(String newOrigin) {
		String oldOrigin = origin;
		origin = newOrigin;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiffPackage.DIFF_MODEL__ORIGIN, oldOrigin,
					origin));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setRight(String newRight) {
		String oldRight = right;
		right = newRight;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiffPackage.DIFF_MODEL__RIGHT, oldRight,
					right));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (left: ");
		result.append(left);
		result.append(", origin: ");
		result.append(origin);
		result.append(", right: ");
		result.append(right);
		result.append(')');
		return result.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.DIFF_MODEL;
	}

} // DiffModelImpl
