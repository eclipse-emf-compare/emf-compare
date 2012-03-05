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
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.match.internal.statistic.NameSimilarity;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Attribute Change Right Target</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeRightTargetImpl#getRightTarget <em>Right Target</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AttributeChangeRightTargetImpl extends AttributeChangeImpl implements AttributeChangeRightTarget {
	/**
	 * The default value of the '{@link #getRightTarget() <em>Right Target</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRightTarget()
	 * @generated
	 * @ordered
	 */
	protected static final Object RIGHT_TARGET_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRightTarget() <em>Right Target</em>}' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRightTarget()
	 * @generated
	 * @ordered
	 */
	protected Object rightTarget = RIGHT_TARGET_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected AttributeChangeRightTargetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_TARGET:
				return getRightTarget();
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
			case DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_TARGET:
				return RIGHT_TARGET_EDEFAULT == null ? rightTarget != null : !RIGHT_TARGET_EDEFAULT
						.equals(rightTarget);
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
				toString = EMFCompareDiffMessages.getString("RemoteRemoveAttributeValueImpl.ToString", //$NON-NLS-1$
						rightTarget, attribute.getName(), NameSimilarity.findName(rightElement));
			} catch (final FactoryException e) {
				toString = EMFCompareDiffMessages.getString("RemoteRemoveAttributeValueImpl.ToString", //$NON-NLS-1$
						rightTarget, attribute.getName(), rightElement.eClass().getName());
			}
		} else {
			try {
				toString = EMFCompareDiffMessages.getString("AddAttributeValueImpl.ToString", //$NON-NLS-1$
						rightTarget, attribute.getName(), NameSimilarity.findName(rightElement));
			} catch (final FactoryException e) {
				toString = EMFCompareDiffMessages.getString("AddAttributeValueImpl.ToString", rightTarget, //$NON-NLS-1$
						attribute.getName(), rightElement.eClass().getName());
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
			case DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_TARGET:
				setRightTarget(newValue);
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
			case DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_TARGET:
				setRightTarget(RIGHT_TARGET_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Object getRightTarget() {
		return rightTarget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setRightTarget(Object newRightTarget) {
		Object oldRightTarget = rightTarget;
		rightTarget = newRightTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_TARGET, oldRightTarget, rightTarget));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.ATTRIBUTE_CHANGE_RIGHT_TARGET;
	}

} // AttributeChangeRightTargetImpl
