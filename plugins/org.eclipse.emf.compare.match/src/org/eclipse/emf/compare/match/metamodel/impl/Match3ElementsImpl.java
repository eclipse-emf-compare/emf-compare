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
package org.eclipse.emf.compare.match.metamodel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.compare.match.metamodel.Match3Elements;
import org.eclipse.emf.compare.match.metamodel.MatchPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Match3 Elements</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.impl.Match3ElementsImpl#getOriginElement <em>Origin Element</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class Match3ElementsImpl extends Match2ElementsImpl implements Match3Elements {
	/**
	 * The cached value of the '{@link #getOriginElement() <em>Origin Element</em>}' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOriginElement()
	 * @generated
	 * @ordered
	 */
	protected EObject originElement;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected Match3ElementsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MatchPackage.Literals.MATCH3_ELEMENTS;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getOriginElement() {
		if (originElement != null && originElement.eIsProxy()) {
			InternalEObject oldOriginElement = (InternalEObject)originElement;
			originElement = eResolveProxy(oldOriginElement);
			if (originElement != oldOriginElement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							MatchPackage.MATCH3_ELEMENTS__ORIGIN_ELEMENT, oldOriginElement, originElement));
			}
		}
		return originElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetOriginElement() {
		return originElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginElement(EObject newOriginElement) {
		EObject oldOriginElement = originElement;
		originElement = newOriginElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MatchPackage.MATCH3_ELEMENTS__ORIGIN_ELEMENT, oldOriginElement, originElement));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MatchPackage.MATCH3_ELEMENTS__ORIGIN_ELEMENT:
				if (resolve)
					return getOriginElement();
				return basicGetOriginElement();
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
			case MatchPackage.MATCH3_ELEMENTS__ORIGIN_ELEMENT:
				setOriginElement((EObject)newValue);
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
			case MatchPackage.MATCH3_ELEMENTS__ORIGIN_ELEMENT:
				setOriginElement((EObject)null);
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
			case MatchPackage.MATCH3_ELEMENTS__ORIGIN_ELEMENT:
				return originElement != null;
		}
		return super.eIsSet(featureID);
	}

} // Match3ElementsImpl
