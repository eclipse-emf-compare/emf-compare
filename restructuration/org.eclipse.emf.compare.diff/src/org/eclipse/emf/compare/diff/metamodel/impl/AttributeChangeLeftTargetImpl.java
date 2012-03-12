/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.metamodel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.EMFCompareDiffMessages;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.match.internal.statistic.NameSimilarity;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Attribute Change Left Target</b></em>
 * '. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeLeftTargetImpl#getLeftTarget <em>Left Target</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AttributeChangeLeftTargetImpl extends AttributeChangeImpl implements AttributeChangeLeftTarget {
	/**
	 * The default value of the '{@link #getLeftTarget() <em>Left Target</em>}' attribute.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #getLeftTarget()
	 * @generated
	 * @ordered
	 */
	protected static final Object LEFT_TARGET_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLeftTarget() <em>Left Target</em>}' attribute.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #getLeftTarget()
	 * @generated
	 * @ordered
	 */
	protected Object leftTarget = LEFT_TARGET_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected AttributeChangeLeftTargetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_TARGET:
				return getLeftTarget();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_TARGET:
				return LEFT_TARGET_EDEFAULT == null ? leftTarget != null : !LEFT_TARGET_EDEFAULT
						.equals(leftTarget);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public String toString() {
		String toString = null;
		if (isRemote()) {
			try {
				toString = EMFCompareDiffMessages.getString("RemoteAddReferenceValueImpl.ToString", //$NON-NLS-1$
						leftTarget, attribute.getName(), NameSimilarity.findName(leftElement));
			} catch (final FactoryException e) {
				toString = EMFCompareDiffMessages.getString("RemoteAddReferenceValueImpl.ToString", //$NON-NLS-1$
						leftTarget, attribute.getName(), leftElement.eClass().getName());
			}
		} else {
			try {
				toString = EMFCompareDiffMessages.getString("RemoveReferenceValueImpl.ToString", //$NON-NLS-1$
						leftTarget, attribute.getName(), NameSimilarity.findName(leftElement));
			} catch (final FactoryException e) {
				toString = EMFCompareDiffMessages.getString("RemoveReferenceValueImpl.ToString", leftTarget, //$NON-NLS-1$
						attribute.getName(), leftElement.eClass().getName());
			}
		}
		return toString;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_TARGET:
				setLeftTarget(newValue);
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
			case DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_TARGET:
				setLeftTarget(LEFT_TARGET_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Object getLeftTarget() {
		return leftTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setLeftTarget(Object newLeftTarget) {
		Object oldLeftTarget = leftTarget;
		leftTarget = newLeftTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_TARGET, oldLeftTarget, leftTarget));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.ATTRIBUTE_CHANGE_LEFT_TARGET;
	}

} // AttributeChangeLeftTargetImpl
