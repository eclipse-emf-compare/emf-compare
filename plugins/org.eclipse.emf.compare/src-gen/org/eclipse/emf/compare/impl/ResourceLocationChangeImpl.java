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
package org.eclipse.emf.compare.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.ResourceLocationChange;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource Location Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.impl.ResourceLocationChangeImpl#getBaseLocation <em>Base Location</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.impl.ResourceLocationChangeImpl#getChangedLocation <em>Changed Location</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 * @since 3.2
 */
public class ResourceLocationChangeImpl extends DiffImpl implements ResourceLocationChange {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("hiding")
	public static final String copyright = "Copyright (c) 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The default value of the '{@link #getBaseLocation() <em>Base Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseLocation()
	 * @generated
	 * @ordered
	 */
	protected static final String BASE_LOCATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBaseLocation() <em>Base Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseLocation()
	 * @generated
	 * @ordered
	 */
	protected String baseLocation = BASE_LOCATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getChangedLocation() <em>Changed Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangedLocation()
	 * @generated
	 * @ordered
	 */
	protected static final String CHANGED_LOCATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getChangedLocation() <em>Changed Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangedLocation()
	 * @generated
	 * @ordered
	 */
	protected String changedLocation = CHANGED_LOCATION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ResourceLocationChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ComparePackage.Literals.RESOURCE_LOCATION_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBaseLocation() {
		return baseLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseLocation(String newBaseLocation) {
		String oldBaseLocation = baseLocation;
		baseLocation = newBaseLocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ComparePackage.RESOURCE_LOCATION_CHANGE__BASE_LOCATION, oldBaseLocation, baseLocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getChangedLocation() {
		return changedLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setChangedLocation(String newChangedLocation) {
		String oldChangedLocation = changedLocation;
		changedLocation = newChangedLocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ComparePackage.RESOURCE_LOCATION_CHANGE__CHANGED_LOCATION, oldChangedLocation,
					changedLocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ComparePackage.RESOURCE_LOCATION_CHANGE__BASE_LOCATION:
				return getBaseLocation();
			case ComparePackage.RESOURCE_LOCATION_CHANGE__CHANGED_LOCATION:
				return getChangedLocation();
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
			case ComparePackage.RESOURCE_LOCATION_CHANGE__BASE_LOCATION:
				setBaseLocation((String)newValue);
				return;
			case ComparePackage.RESOURCE_LOCATION_CHANGE__CHANGED_LOCATION:
				setChangedLocation((String)newValue);
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
			case ComparePackage.RESOURCE_LOCATION_CHANGE__BASE_LOCATION:
				setBaseLocation(BASE_LOCATION_EDEFAULT);
				return;
			case ComparePackage.RESOURCE_LOCATION_CHANGE__CHANGED_LOCATION:
				setChangedLocation(CHANGED_LOCATION_EDEFAULT);
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
			case ComparePackage.RESOURCE_LOCATION_CHANGE__BASE_LOCATION:
				return BASE_LOCATION_EDEFAULT == null ? baseLocation != null : !BASE_LOCATION_EDEFAULT
						.equals(baseLocation);
			case ComparePackage.RESOURCE_LOCATION_CHANGE__CHANGED_LOCATION:
				return CHANGED_LOCATION_EDEFAULT == null ? changedLocation != null
						: !CHANGED_LOCATION_EDEFAULT.equals(changedLocation);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (baseLocation: "); //$NON-NLS-1$
		result.append(baseLocation);
		result.append(", changedLocation: "); //$NON-NLS-1$
		result.append(changedLocation);
		result.append(')');
		return result.toString();
	}

} //ResourceLocationChangeImpl
