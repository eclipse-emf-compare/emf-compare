/*******************************************************************************
 * Copyright (c) 2008, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.examples.addressbook.addressbook.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage;
import org.eclipse.emf.compare.examples.addressbook.addressbook.Electronic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Electronic</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.ElectronicImpl#getEmail <em>Email
 * </em>}</li>
 * <li>{@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.ElectronicImpl#getWebsite <em>
 * Website</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ElectronicImpl extends ContactImpl implements Electronic {
	/**
	 * The default value of the '{@link #getEmail() <em>Email</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getEmail()
	 * @generated
	 * @ordered
	 */
	protected static final String EMAIL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEmail() <em>Email</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getEmail()
	 * @generated
	 * @ordered
	 */
	protected String email = EMAIL_EDEFAULT;

	/**
	 * The default value of the '{@link #getWebsite() <em>Website</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getWebsite()
	 * @generated
	 * @ordered
	 */
	protected static final String WEBSITE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWebsite() <em>Website</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getWebsite()
	 * @generated
	 * @ordered
	 */
	protected String website = WEBSITE_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ElectronicImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AddressbookPackage.Literals.ELECTRONIC;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setEmail(String newEmail) {
		final String oldEmail = email;
		email = newEmail;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AddressbookPackage.ELECTRONIC__EMAIL,
					oldEmail, email));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setWebsite(String newWebsite) {
		final String oldWebsite = website;
		website = newWebsite;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AddressbookPackage.ELECTRONIC__WEBSITE,
					oldWebsite, website));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AddressbookPackage.ELECTRONIC__EMAIL:
				return getEmail();
			case AddressbookPackage.ELECTRONIC__WEBSITE:
				return getWebsite();
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
			case AddressbookPackage.ELECTRONIC__EMAIL:
				setEmail((String)newValue);
				return;
			case AddressbookPackage.ELECTRONIC__WEBSITE:
				setWebsite((String)newValue);
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
			case AddressbookPackage.ELECTRONIC__EMAIL:
				setEmail(EMAIL_EDEFAULT);
				return;
			case AddressbookPackage.ELECTRONIC__WEBSITE:
				setWebsite(WEBSITE_EDEFAULT);
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
			case AddressbookPackage.ELECTRONIC__EMAIL:
				return EMAIL_EDEFAULT == null ? email != null : !EMAIL_EDEFAULT.equals(email);
			case AddressbookPackage.ELECTRONIC__WEBSITE:
				return WEBSITE_EDEFAULT == null ? website != null : !WEBSITE_EDEFAULT.equals(website);
		}
		return super.eIsSet(featureID);
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

		final StringBuffer result = new StringBuffer(super.toString());
		result.append(" (email: ");
		result.append(email);
		result.append(", website: ");
		result.append(website);
		result.append(')');
		return result.toString();
	}

} // ElectronicImpl
