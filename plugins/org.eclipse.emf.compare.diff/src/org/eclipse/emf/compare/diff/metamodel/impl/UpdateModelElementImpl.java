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
import org.eclipse.emf.compare.diff.metamodel.UpdateModelElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Update Model Element</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateModelElementImpl#getRightElement <em>Right Element</em>}</li>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateModelElementImpl#getLeftElement <em>Left Element</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
@SuppressWarnings("nls")
public class UpdateModelElementImpl extends ModelElementChangeImpl implements UpdateModelElement {
	/**
	 * The cached value of the '{@link #getRightElement() <em>Right Element</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRightElement()
	 * @generated
	 * @ordered
	 */
	protected EObject rightElement = null;

	/**
	 * The cached value of the '{@link #getLeftElement() <em>Left Element</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLeftElement()
	 * @generated
	 * @ordered
	 */
	protected EObject leftElement = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected UpdateModelElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.UPDATE_MODEL_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject getRightElement() {
		if (rightElement != null && rightElement.eIsProxy()) {
			InternalEObject oldRightElement = (InternalEObject)rightElement;
			rightElement = eResolveProxy(oldRightElement);
			if (rightElement != oldRightElement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DiffPackage.UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT, oldRightElement, rightElement));
			}
		}
		return rightElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject basicGetRightElement() {
		return rightElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setRightElement(EObject newRightElement) {
		EObject oldRightElement = rightElement;
		rightElement = newRightElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiffPackage.UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT, oldRightElement, rightElement));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject getLeftElement() {
		if (leftElement != null && leftElement.eIsProxy()) {
			InternalEObject oldLeftElement = (InternalEObject)leftElement;
			leftElement = eResolveProxy(oldLeftElement);
			if (leftElement != oldLeftElement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DiffPackage.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT, oldLeftElement, leftElement));
			}
		}
		return leftElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject basicGetLeftElement() {
		return leftElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLeftElement(EObject newLeftElement) {
		EObject oldLeftElement = leftElement;
		leftElement = newLeftElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiffPackage.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT, oldLeftElement, leftElement));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT:
				if (resolve)
					return getRightElement();
				return basicGetRightElement();
			case DiffPackage.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT:
				if (resolve)
					return getLeftElement();
				return basicGetLeftElement();
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
			case DiffPackage.UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT:
				setRightElement((EObject)newValue);
				return;
			case DiffPackage.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT:
				setLeftElement((EObject)newValue);
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
			case DiffPackage.UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT:
				setRightElement((EObject)null);
				return;
			case DiffPackage.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT:
				setLeftElement((EObject)null);
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
			case DiffPackage.UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT:
				return rightElement != null;
			case DiffPackage.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT:
				return leftElement != null;
		}
		return super.eIsSet(featureID);
	}

} // UpdateModelElementImpl
