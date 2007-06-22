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
package org.eclipse.emf.compare.match.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.compare.match.Match3Element;
import org.eclipse.emf.compare.match.MatchPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Match3 Element</b></em>'.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a> <!-- end-user-doc -->
 *         <p>
 *         The following features are implemented:
 *         <ul>
 *         <li>{@link org.eclipse.emf.compare.match.impl.Match3ElementImpl#getOriginElement <em>Origin Element</em>}</li>
 *         </ul>
 *         </p>
 * @generated
 */
public class Match3ElementImpl extends MatchElementImpl implements Match3Element {
	/**
	 * The cached value of the '{@link #getOriginElement() <em>Origin Element</em>}' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOriginElement()
	 * @generated
	 * @ordered
	 */
	protected EObject originElement = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Match3ElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected EClass eStaticClass() {
		return MatchPackage.Literals.MATCH3_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject getOriginElement() {
		if (originElement != null && originElement.eIsProxy()) {
			InternalEObject oldOriginElement = (InternalEObject)originElement;
			originElement = eResolveProxy(oldOriginElement);
			if (originElement != oldOriginElement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							MatchPackage.MATCH3_ELEMENT__ORIGIN_ELEMENT, oldOriginElement, originElement));
			}
		}
		return originElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject basicGetOriginElement() {
		return originElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOriginElement(EObject newOriginElement) {
		EObject oldOriginElement = originElement;
		originElement = newOriginElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MatchPackage.MATCH3_ELEMENT__ORIGIN_ELEMENT, oldOriginElement, originElement));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MatchPackage.MATCH3_ELEMENT__ORIGIN_ELEMENT:
				if (resolve)
					return getOriginElement();
				return basicGetOriginElement();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MatchPackage.MATCH3_ELEMENT__ORIGIN_ELEMENT:
				setOriginElement((EObject)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case MatchPackage.MATCH3_ELEMENT__ORIGIN_ELEMENT:
				setOriginElement((EObject)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case MatchPackage.MATCH3_ELEMENT__ORIGIN_ELEMENT:
				return originElement != null;
		}
		return super.eIsSet(featureID);
	}

} // Match3ElementImpl
