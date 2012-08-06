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

import org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChange;
import org.eclipse.emf.compare.uml2diff.Uml2diffPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.uml2.uml.GeneralizationSet;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>UML Generalization Set Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.uml2diff.impl.UMLGeneralizationSetChangeImpl#getGeneralizationSet <em>Generalization Set</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UMLGeneralizationSetChangeImpl extends UMLExtensionImpl implements UMLGeneralizationSetChange {
	/**
	 * The cached value of the '{@link #getGeneralizationSet() <em>Generalization Set</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeneralizationSet()
	 * @generated
	 * @ordered
	 */
	protected GeneralizationSet generalizationSet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLGeneralizationSetChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Uml2diffPackage.Literals.UML_GENERALIZATION_SET_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GeneralizationSet getGeneralizationSet() {
		if (generalizationSet != null && generalizationSet.eIsProxy()) {
			InternalEObject oldGeneralizationSet = (InternalEObject)generalizationSet;
			generalizationSet = (GeneralizationSet)eResolveProxy(oldGeneralizationSet);
			if (generalizationSet != oldGeneralizationSet) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, Uml2diffPackage.UML_GENERALIZATION_SET_CHANGE__GENERALIZATION_SET, oldGeneralizationSet, generalizationSet));
			}
		}
		return generalizationSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GeneralizationSet basicGetGeneralizationSet() {
		return generalizationSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGeneralizationSet(GeneralizationSet newGeneralizationSet) {
		GeneralizationSet oldGeneralizationSet = generalizationSet;
		generalizationSet = newGeneralizationSet;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Uml2diffPackage.UML_GENERALIZATION_SET_CHANGE__GENERALIZATION_SET, oldGeneralizationSet, generalizationSet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Uml2diffPackage.UML_GENERALIZATION_SET_CHANGE__GENERALIZATION_SET:
				if (resolve) return getGeneralizationSet();
				return basicGetGeneralizationSet();
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
			case Uml2diffPackage.UML_GENERALIZATION_SET_CHANGE__GENERALIZATION_SET:
				setGeneralizationSet((GeneralizationSet)newValue);
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
			case Uml2diffPackage.UML_GENERALIZATION_SET_CHANGE__GENERALIZATION_SET:
				setGeneralizationSet((GeneralizationSet)null);
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
			case Uml2diffPackage.UML_GENERALIZATION_SET_CHANGE__GENERALIZATION_SET:
				return generalizationSet != null;
		}
		return super.eIsSet(featureID);
	}

} //UMLGeneralizationSetChangeImpl
