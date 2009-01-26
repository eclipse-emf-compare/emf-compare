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

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Model</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl#getOwnedElements <em>Owned Elements</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl#getLeftRoots <em>Left Roots</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl#getRightRoots <em>Right Roots</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl#getAncestorRoots <em>Ancestor Roots</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiffModelImpl extends EObjectImpl implements DiffModel {
	/**
	 * The cached value of the '{@link #getOwnedElements() <em>Owned Elements</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getOwnedElements()
	 * @generated
	 * @ordered
	 */
	protected EList<DiffElement> ownedElements;

	/**
	 * The cached value of the '{@link #getLeftRoots() <em>Left Roots</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeftRoots()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> leftRoots;

	/**
	 * The cached value of the '{@link #getRightRoots() <em>Right Roots</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRightRoots()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> rightRoots;

	/**
	 * The cached value of the '{@link #getAncestorRoots() <em>Ancestor Roots</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAncestorRoots()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> ancestorRoots;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected DiffModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.DIFF_MODEL__OWNED_ELEMENTS:
				return getOwnedElements();
			case DiffPackage.DIFF_MODEL__LEFT_ROOTS:
				return getLeftRoots();
			case DiffPackage.DIFF_MODEL__RIGHT_ROOTS:
				return getRightRoots();
			case DiffPackage.DIFF_MODEL__ANCESTOR_ROOTS:
				return getAncestorRoots();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DiffPackage.DIFF_MODEL__OWNED_ELEMENTS:
				return ((InternalEList<?>)getOwnedElements()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DiffPackage.DIFF_MODEL__OWNED_ELEMENTS:
				return ownedElements != null && !ownedElements.isEmpty();
			case DiffPackage.DIFF_MODEL__LEFT_ROOTS:
				return leftRoots != null && !leftRoots.isEmpty();
			case DiffPackage.DIFF_MODEL__RIGHT_ROOTS:
				return rightRoots != null && !rightRoots.isEmpty();
			case DiffPackage.DIFF_MODEL__ANCESTOR_ROOTS:
				return ancestorRoots != null && !ancestorRoots.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DiffPackage.DIFF_MODEL__OWNED_ELEMENTS:
				getOwnedElements().clear();
				getOwnedElements().addAll((Collection<? extends DiffElement>)newValue);
				return;
			case DiffPackage.DIFF_MODEL__LEFT_ROOTS:
				getLeftRoots().clear();
				getLeftRoots().addAll((Collection<? extends EObject>)newValue);
				return;
			case DiffPackage.DIFF_MODEL__RIGHT_ROOTS:
				getRightRoots().clear();
				getRightRoots().addAll((Collection<? extends EObject>)newValue);
				return;
			case DiffPackage.DIFF_MODEL__ANCESTOR_ROOTS:
				getAncestorRoots().clear();
				getAncestorRoots().addAll((Collection<? extends EObject>)newValue);
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
			case DiffPackage.DIFF_MODEL__OWNED_ELEMENTS:
				getOwnedElements().clear();
				return;
			case DiffPackage.DIFF_MODEL__LEFT_ROOTS:
				getLeftRoots().clear();
				return;
			case DiffPackage.DIFF_MODEL__RIGHT_ROOTS:
				getRightRoots().clear();
				return;
			case DiffPackage.DIFF_MODEL__ANCESTOR_ROOTS:
				getAncestorRoots().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DiffElement> getOwnedElements() {
		if (ownedElements == null) {
			ownedElements = new EObjectContainmentEList<DiffElement>(DiffElement.class, this,
					DiffPackage.DIFF_MODEL__OWNED_ELEMENTS);
		}
		return ownedElements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getLeftRoots() {
		if (leftRoots == null) {
			leftRoots = new EObjectResolvingEList<EObject>(EObject.class, this,
					DiffPackage.DIFF_MODEL__LEFT_ROOTS);
		}
		return leftRoots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getRightRoots() {
		if (rightRoots == null) {
			rightRoots = new EObjectResolvingEList<EObject>(EObject.class, this,
					DiffPackage.DIFF_MODEL__RIGHT_ROOTS);
		}
		return rightRoots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getAncestorRoots() {
		if (ancestorRoots == null) {
			ancestorRoots = new EObjectResolvingEList<EObject>(EObject.class, this,
					DiffPackage.DIFF_MODEL__ANCESTOR_ROOTS);
		}
		return ancestorRoots;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.DIFF_MODEL;
	}

} // DiffModelImpl
