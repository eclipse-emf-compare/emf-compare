/*******************************************************************************
 * Copyright (c) 2009, 2012 Obeo.
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
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.EMFCompareDiffMessages;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Reference Order Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceOrderChangeImpl#getLeftTarget <em>Left Target</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceOrderChangeImpl#getRightTarget <em>Right Target</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ReferenceOrderChangeImpl extends ReferenceChangeImpl implements ReferenceOrderChange {
	/**
	 * The cached value of the '{@link #getLeftTarget() <em>Left Target</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeftTarget()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> leftTarget;

	/**
	 * The cached value of the '{@link #getRightTarget() <em>Right Target</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRightTarget()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> rightTarget;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected ReferenceOrderChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.REFERENCE_ORDER_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getLeftTarget() {
		if (leftTarget == null) {
			leftTarget = new EObjectResolvingEList<EObject>(EObject.class, this,
					DiffPackage.REFERENCE_ORDER_CHANGE__LEFT_TARGET);
		}
		return leftTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getRightTarget() {
		if (rightTarget == null) {
			rightTarget = new EObjectResolvingEList<EObject>(EObject.class, this,
					DiffPackage.REFERENCE_ORDER_CHANGE__RIGHT_TARGET);
		}
		return rightTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.REFERENCE_ORDER_CHANGE__LEFT_TARGET:
				return getLeftTarget();
			case DiffPackage.REFERENCE_ORDER_CHANGE__RIGHT_TARGET:
				return getRightTarget();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DiffPackage.REFERENCE_ORDER_CHANGE__LEFT_TARGET:
				getLeftTarget().clear();
				getLeftTarget().addAll((Collection<? extends EObject>)newValue);
				return;
			case DiffPackage.REFERENCE_ORDER_CHANGE__RIGHT_TARGET:
				getRightTarget().clear();
				getRightTarget().addAll((Collection<? extends EObject>)newValue);
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
			case DiffPackage.REFERENCE_ORDER_CHANGE__LEFT_TARGET:
				getLeftTarget().clear();
				return;
			case DiffPackage.REFERENCE_ORDER_CHANGE__RIGHT_TARGET:
				getRightTarget().clear();
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
			case DiffPackage.REFERENCE_ORDER_CHANGE__LEFT_TARGET:
				return leftTarget != null && !leftTarget.isEmpty();
			case DiffPackage.REFERENCE_ORDER_CHANGE__RIGHT_TARGET:
				return rightTarget != null && !rightTarget.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @generated NOT
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffElementImpl#toString()
	 */
	@Override
	public String toString() {
		return EMFCompareDiffMessages.getString("ReferenceOrderChange.ToString", getReference().getName()); //$NON-NLS-1$
	}
} // ReferenceOrderChangeImpl
