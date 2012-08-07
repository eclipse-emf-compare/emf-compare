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
package org.eclipse.emf.compare.uml2.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.compare.uml2.DependencyChange;
import org.eclipse.emf.compare.uml2.UMLComparePackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.uml2.uml.Dependency;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dependency Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.uml2.impl.DependencyChangeImpl#getDependency <em>Dependency</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DependencyChangeImpl extends UMLDiffImpl implements DependencyChange {
	/**
	 * The cached value of the '{@link #getDependency() <em>Dependency</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDependency()
	 * @generated
	 * @ordered
	 */
	protected Dependency dependency;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DependencyChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UMLComparePackage.Literals.DEPENDENCY_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Dependency getDependency() {
		if (dependency != null && dependency.eIsProxy()) {
			InternalEObject oldDependency = (InternalEObject)dependency;
			dependency = (Dependency)eResolveProxy(oldDependency);
			if (dependency != oldDependency) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, UMLComparePackage.DEPENDENCY_CHANGE__DEPENDENCY, oldDependency, dependency));
			}
		}
		return dependency;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Dependency basicGetDependency() {
		return dependency;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDependency(Dependency newDependency) {
		Dependency oldDependency = dependency;
		dependency = newDependency;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UMLComparePackage.DEPENDENCY_CHANGE__DEPENDENCY, oldDependency, dependency));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case UMLComparePackage.DEPENDENCY_CHANGE__DEPENDENCY:
				if (resolve) return getDependency();
				return basicGetDependency();
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
			case UMLComparePackage.DEPENDENCY_CHANGE__DEPENDENCY:
				setDependency((Dependency)newValue);
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
			case UMLComparePackage.DEPENDENCY_CHANGE__DEPENDENCY:
				setDependency((Dependency)null);
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
			case UMLComparePackage.DEPENDENCY_CHANGE__DEPENDENCY:
				return dependency != null;
		}
		return super.eIsSet(featureID);
	}

} //DependencyChangeImpl
