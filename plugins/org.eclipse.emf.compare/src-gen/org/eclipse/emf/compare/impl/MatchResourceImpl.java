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
import org.eclipse.emf.compare.MatchResource;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Match Resource</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.impl.MatchResourceImpl#getLeftURI <em>Left URI</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.impl.MatchResourceImpl#getRightURI <em>Right URI</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.impl.MatchResourceImpl#getOriginURI <em>Origin URI</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MatchResourceImpl extends MinimalEObjectImpl implements MatchResource {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The default value of the '{@link #getLeftURI() <em>Left URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeftURI()
	 * @generated
	 * @ordered
	 */
	protected static final String LEFT_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLeftURI() <em>Left URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeftURI()
	 * @generated
	 * @ordered
	 */
	protected String leftURI = LEFT_URI_EDEFAULT;

	/**
	 * The default value of the '{@link #getRightURI() <em>Right URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRightURI()
	 * @generated
	 * @ordered
	 */
	protected static final String RIGHT_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRightURI() <em>Right URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRightURI()
	 * @generated
	 * @ordered
	 */
	protected String rightURI = RIGHT_URI_EDEFAULT;

	/**
	 * The default value of the '{@link #getOriginURI() <em>Origin URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginURI()
	 * @generated
	 * @ordered
	 */
	protected static final String ORIGIN_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOriginURI() <em>Origin URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginURI()
	 * @generated
	 * @ordered
	 */
	protected String originURI = ORIGIN_URI_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MatchResourceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ComparePackage.Literals.MATCH_RESOURCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLeftURI() {
		return leftURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLeftURI(String newLeftURI) {
		String oldLeftURI = leftURI;
		leftURI = newLeftURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComparePackage.MATCH_RESOURCE__LEFT_URI,
					oldLeftURI, leftURI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRightURI() {
		return rightURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRightURI(String newRightURI) {
		String oldRightURI = rightURI;
		rightURI = newRightURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComparePackage.MATCH_RESOURCE__RIGHT_URI,
					oldRightURI, rightURI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOriginURI() {
		return originURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginURI(String newOriginURI) {
		String oldOriginURI = originURI;
		originURI = newOriginURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComparePackage.MATCH_RESOURCE__ORIGIN_URI,
					oldOriginURI, originURI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ComparePackage.MATCH_RESOURCE__LEFT_URI:
				return getLeftURI();
			case ComparePackage.MATCH_RESOURCE__RIGHT_URI:
				return getRightURI();
			case ComparePackage.MATCH_RESOURCE__ORIGIN_URI:
				return getOriginURI();
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
			case ComparePackage.MATCH_RESOURCE__LEFT_URI:
				setLeftURI((String)newValue);
				return;
			case ComparePackage.MATCH_RESOURCE__RIGHT_URI:
				setRightURI((String)newValue);
				return;
			case ComparePackage.MATCH_RESOURCE__ORIGIN_URI:
				setOriginURI((String)newValue);
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
			case ComparePackage.MATCH_RESOURCE__LEFT_URI:
				setLeftURI(LEFT_URI_EDEFAULT);
				return;
			case ComparePackage.MATCH_RESOURCE__RIGHT_URI:
				setRightURI(RIGHT_URI_EDEFAULT);
				return;
			case ComparePackage.MATCH_RESOURCE__ORIGIN_URI:
				setOriginURI(ORIGIN_URI_EDEFAULT);
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
			case ComparePackage.MATCH_RESOURCE__LEFT_URI:
				return LEFT_URI_EDEFAULT == null ? leftURI != null : !LEFT_URI_EDEFAULT.equals(leftURI);
			case ComparePackage.MATCH_RESOURCE__RIGHT_URI:
				return RIGHT_URI_EDEFAULT == null ? rightURI != null : !RIGHT_URI_EDEFAULT.equals(rightURI);
			case ComparePackage.MATCH_RESOURCE__ORIGIN_URI:
				return ORIGIN_URI_EDEFAULT == null ? originURI != null : !ORIGIN_URI_EDEFAULT
						.equals(originURI);
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
		result.append(" (leftURI: "); //$NON-NLS-1$
		result.append(leftURI);
		result.append(", rightURI: "); //$NON-NLS-1$
		result.append(rightURI);
		result.append(", originURI: "); //$NON-NLS-1$
		result.append(originURI);
		result.append(')');
		return result.toString();
	}

} //MatchResourceImpl
