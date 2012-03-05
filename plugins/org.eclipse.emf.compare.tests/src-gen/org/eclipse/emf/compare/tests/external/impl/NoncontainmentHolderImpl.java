/**
 * Copyright (c) 2009, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *     Stephen McCants - Initial API and implementation
 */
package org.eclipse.emf.compare.tests.external.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.compare.tests.external.ExternalPackage;
import org.eclipse.emf.compare.tests.external.NoncontainmentHolder;
import org.eclipse.emf.compare.tests.external.StringHolder;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Noncontainment Holder</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.external.impl.NoncontainmentHolderImpl#getStringHolder <em>String Holder</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NoncontainmentHolderImpl extends EObjectImpl implements NoncontainmentHolder {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2009, 2012 IBM Corporation and others.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n \r\nContributors:\r\n    Stephen McCants - Initial API and implementation"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getStringHolder() <em>String Holder</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStringHolder()
	 * @generated
	 * @ordered
	 */
	protected StringHolder stringHolder;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NoncontainmentHolderImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExternalPackage.Literals.NONCONTAINMENT_HOLDER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StringHolder getStringHolder() {
		if (stringHolder != null && stringHolder.eIsProxy()) {
			InternalEObject oldStringHolder = (InternalEObject)stringHolder;
			stringHolder = (StringHolder)eResolveProxy(oldStringHolder);
			if (stringHolder != oldStringHolder) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExternalPackage.NONCONTAINMENT_HOLDER__STRING_HOLDER, oldStringHolder, stringHolder));
			}
		}
		return stringHolder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StringHolder basicGetStringHolder() {
		return stringHolder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStringHolder(StringHolder newStringHolder) {
		StringHolder oldStringHolder = stringHolder;
		stringHolder = newStringHolder;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExternalPackage.NONCONTAINMENT_HOLDER__STRING_HOLDER, oldStringHolder, stringHolder));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ExternalPackage.NONCONTAINMENT_HOLDER__STRING_HOLDER:
				if (resolve) return getStringHolder();
				return basicGetStringHolder();
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
			case ExternalPackage.NONCONTAINMENT_HOLDER__STRING_HOLDER:
				setStringHolder((StringHolder)newValue);
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
			case ExternalPackage.NONCONTAINMENT_HOLDER__STRING_HOLDER:
				setStringHolder((StringHolder)null);
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
			case ExternalPackage.NONCONTAINMENT_HOLDER__STRING_HOLDER:
				return stringHolder != null;
		}
		return super.eIsSet(featureID);
	}

} //NoncontainmentHolderImpl
