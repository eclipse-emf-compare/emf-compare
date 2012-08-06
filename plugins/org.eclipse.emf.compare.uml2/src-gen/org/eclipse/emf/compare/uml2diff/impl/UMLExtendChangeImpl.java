/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2diff.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.compare.impl.DiffImpl;

import org.eclipse.emf.compare.uml2diff.UMLExtendChange;
import org.eclipse.emf.compare.uml2diff.Uml2diffPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.uml2.uml.Extend;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>UML Extend Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.uml2diff.impl.UMLExtendChangeImpl#getExtend <em>Extend</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UMLExtendChangeImpl extends UMLExtensionImpl implements UMLExtendChange {
	/**
	 * The cached value of the '{@link #getExtend() <em>Extend</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtend()
	 * @generated
	 * @ordered
	 */
	protected Extend extend;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLExtendChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Uml2diffPackage.Literals.UML_EXTEND_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Extend getExtend() {
		if (extend != null && extend.eIsProxy()) {
			InternalEObject oldExtend = (InternalEObject)extend;
			extend = (Extend)eResolveProxy(oldExtend);
			if (extend != oldExtend) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, Uml2diffPackage.UML_EXTEND_CHANGE__EXTEND, oldExtend, extend));
			}
		}
		return extend;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Extend basicGetExtend() {
		return extend;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExtend(Extend newExtend) {
		Extend oldExtend = extend;
		extend = newExtend;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Uml2diffPackage.UML_EXTEND_CHANGE__EXTEND, oldExtend, extend));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Uml2diffPackage.UML_EXTEND_CHANGE__EXTEND:
				if (resolve) return getExtend();
				return basicGetExtend();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Uml2diffPackage.UML_EXTEND_CHANGE__EXTEND:
				setExtend((Extend)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case Uml2diffPackage.UML_EXTEND_CHANGE__EXTEND:
				setExtend((Extend)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case Uml2diffPackage.UML_EXTEND_CHANGE__EXTEND:
				return extend != null;
		}
		return super.eIsSet(featureID);
	}

} //UMLExtendChangeImpl
