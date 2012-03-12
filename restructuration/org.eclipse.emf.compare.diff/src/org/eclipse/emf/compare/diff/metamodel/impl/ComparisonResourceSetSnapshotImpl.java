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

import java.util.Date;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Comparison Resource Set Snapshot</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.ComparisonResourceSetSnapshotImpl#getDate <em>Date</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.ComparisonResourceSetSnapshotImpl#getDiffResourceSet <em>Diff Resource Set</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.ComparisonResourceSetSnapshotImpl#getMatchResourceSet <em>Match Resource Set</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ComparisonResourceSetSnapshotImpl extends EObjectImpl implements ComparisonResourceSetSnapshot {
	/**
	 * The default value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected static final Date DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected Date date = DATE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDiffResourceSet() <em>Diff Resource Set</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDiffResourceSet()
	 * @generated
	 * @ordered
	 */
	protected DiffResourceSet diffResourceSet;

	/**
	 * The cached value of the '{@link #getMatchResourceSet() <em>Match Resource Set</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getMatchResourceSet()
	 * @generated
	 * @ordered
	 */
	protected MatchResourceSet matchResourceSet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected ComparisonResourceSetSnapshotImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.COMPARISON_RESOURCE_SET_SNAPSHOT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setDate(Date newDate) {
		Date oldDate = date;
		date = newDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__DATE, oldDate, date));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DiffResourceSet getDiffResourceSet() {
		return diffResourceSet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDiffResourceSet(DiffResourceSet newDiffResourceSet,
			NotificationChain msgs) {
		DiffResourceSet oldDiffResourceSet = diffResourceSet;
		diffResourceSet = newDiffResourceSet;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__DIFF_RESOURCE_SET, oldDiffResourceSet,
					newDiffResourceSet);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setDiffResourceSet(DiffResourceSet newDiffResourceSet) {
		if (newDiffResourceSet != diffResourceSet) {
			NotificationChain msgs = null;
			if (diffResourceSet != null)
				msgs = ((InternalEObject)diffResourceSet).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
						- DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__DIFF_RESOURCE_SET, null, msgs);
			if (newDiffResourceSet != null)
				msgs = ((InternalEObject)newDiffResourceSet).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
						- DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__DIFF_RESOURCE_SET, null, msgs);
			msgs = basicSetDiffResourceSet(newDiffResourceSet, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__DIFF_RESOURCE_SET, newDiffResourceSet,
					newDiffResourceSet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public MatchResourceSet getMatchResourceSet() {
		return matchResourceSet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMatchResourceSet(MatchResourceSet newMatchResourceSet,
			NotificationChain msgs) {
		MatchResourceSet oldMatchResourceSet = matchResourceSet;
		matchResourceSet = newMatchResourceSet;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__MATCH_RESOURCE_SET, oldMatchResourceSet,
					newMatchResourceSet);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setMatchResourceSet(MatchResourceSet newMatchResourceSet) {
		if (newMatchResourceSet != matchResourceSet) {
			NotificationChain msgs = null;
			if (matchResourceSet != null)
				msgs = ((InternalEObject)matchResourceSet).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
						- DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__MATCH_RESOURCE_SET, null, msgs);
			if (newMatchResourceSet != null)
				msgs = ((InternalEObject)newMatchResourceSet).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
						- DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__MATCH_RESOURCE_SET, null, msgs);
			msgs = basicSetMatchResourceSet(newMatchResourceSet, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__MATCH_RESOURCE_SET, newMatchResourceSet,
					newMatchResourceSet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__DIFF_RESOURCE_SET:
				return basicSetDiffResourceSet(null, msgs);
			case DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__MATCH_RESOURCE_SET:
				return basicSetMatchResourceSet(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__DATE:
				return getDate();
			case DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__DIFF_RESOURCE_SET:
				return getDiffResourceSet();
			case DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__MATCH_RESOURCE_SET:
				return getMatchResourceSet();
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
			case DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__DATE:
				setDate((Date)newValue);
				return;
			case DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__DIFF_RESOURCE_SET:
				setDiffResourceSet((DiffResourceSet)newValue);
				return;
			case DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__MATCH_RESOURCE_SET:
				setMatchResourceSet((MatchResourceSet)newValue);
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
			case DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__DATE:
				setDate(DATE_EDEFAULT);
				return;
			case DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__DIFF_RESOURCE_SET:
				setDiffResourceSet((DiffResourceSet)null);
				return;
			case DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__MATCH_RESOURCE_SET:
				setMatchResourceSet((MatchResourceSet)null);
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
			case DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__DATE:
				return DATE_EDEFAULT == null ? date != null : !DATE_EDEFAULT.equals(date);
			case DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__DIFF_RESOURCE_SET:
				return diffResourceSet != null;
			case DiffPackage.COMPARISON_RESOURCE_SET_SNAPSHOT__MATCH_RESOURCE_SET:
				return matchResourceSet != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (date: "); //$NON-NLS-1$
		result.append(date);
		result.append(')');
		return result.toString();
	}

} // ComparisonResourceSetSnapshotImpl
