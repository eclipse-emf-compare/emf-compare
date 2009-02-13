/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
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
import org.eclipse.emf.compare.diff.EMFCompareDiffMessages;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Model Element Change Right Target</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeRightTargetImpl#getLeftParent <em>Left Parent</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeRightTargetImpl#getRightElement <em>Right Element</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */

public class ModelElementChangeRightTargetImpl extends ModelElementChangeImpl implements ModelElementChangeRightTarget {
	/**
	 * The cached value of the '{@link #getLeftParent() <em>Left Parent</em>}' reference.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #getLeftParent()
	 * @generated
	 * @ordered
	 */
	protected EObject leftParent;

	/**
	 * The cached value of the '{@link #getRightElement() <em>Right Element</em>}' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRightElement()
	 * @generated
	 * @ordered
	 */
	protected EObject rightElement;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected ModelElementChangeRightTargetImpl() {
		super();
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
	public EObject basicGetRightElement() {
		return rightElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT:
				if (resolve)
					return getLeftParent();
				return basicGetLeftParent();
			case DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT:
				if (resolve)
					return getRightElement();
				return basicGetRightElement();
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
			case DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT:
				return leftParent != null;
			case DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT:
				return rightElement != null;
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
			case DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT:
				setLeftParent((EObject)newValue);
				return;
			case DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT:
				setRightElement((EObject)newValue);
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
			case DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT:
				setLeftParent((EObject)null);
				return;
			case DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT:
				setRightElement((EObject)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getLeftParent() {
		if (leftParent != null && leftParent.eIsProxy()) {
			InternalEObject oldLeftParent = (InternalEObject)leftParent;
			leftParent = eResolveProxy(oldLeftParent);
			if (leftParent != oldLeftParent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT, oldLeftParent,
							leftParent));
			}
		}
		return leftParent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getRightElement() {
		if (rightElement != null && rightElement.eIsProxy()) {
			InternalEObject oldRightElement = (InternalEObject)rightElement;
			rightElement = eResolveProxy(oldRightElement);
			if (rightElement != oldRightElement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT, oldRightElement,
							rightElement));
			}
		}
		return rightElement;
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
					DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT, oldLeftParent, leftParent));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setRightElement(EObject newRightElement) {
		EObject oldRightElement = rightElement;
		rightElement = newRightElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT, oldRightElement,
					rightElement));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @generated NOT
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffElementImpl#toString()
	 */
	@Override
	public String toString() {
		if (isRemote())
			return EMFCompareDiffMessages.getString("RemoteAddModelElementImpl.ToString", rightElement); //$NON-NLS-1$
		return EMFCompareDiffMessages.getString("RemoveModelElementImpl.ToString", rightElement); //$NON-NLS-1$
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.MODEL_ELEMENT_CHANGE_RIGHT_TARGET;
	}

} // ModelElementChangeRightTargetImpl
