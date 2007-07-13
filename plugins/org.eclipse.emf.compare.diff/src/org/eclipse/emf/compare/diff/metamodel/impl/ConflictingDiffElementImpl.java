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
package org.eclipse.emf.compare.diff.metamodel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Conflicting Diff Element</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.ConflictingDiffElementImpl#getLeftParent <em>Left Parent</em>}</li>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.ConflictingDiffElementImpl#getRightParent <em>Right Parent</em>}</li>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.ConflictingDiffElementImpl#getLeftDiff <em>Left Diff</em>}</li>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.ConflictingDiffElementImpl#getRightDiff <em>Right Diff</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
@SuppressWarnings("nls")
public class ConflictingDiffElementImpl extends DiffElementImpl implements ConflictingDiffElement {
	/**
	 * The cached value of the '{@link #getLeftParent() <em>Left Parent</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLeftParent()
	 * @generated
	 * @ordered
	 */
	protected EObject leftParent = null;

	/**
	 * The cached value of the '{@link #getRightParent() <em>Right Parent</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRightParent()
	 * @generated
	 * @ordered
	 */
	protected EObject rightParent = null;

	/**
	 * The cached value of the '{@link #getLeftDiff() <em>Left Diff</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLeftDiff()
	 * @generated
	 * @ordered
	 */
	protected ConflictingDiffGroup leftDiff = null;

	/**
	 * The cached value of the '{@link #getRightDiff() <em>Right Diff</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRightDiff()
	 * @generated
	 * @ordered
	 */
	protected ConflictingDiffGroup rightDiff = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ConflictingDiffElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.CONFLICTING_DIFF_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject getLeftParent() {
		if (leftParent != null && leftParent.eIsProxy()) {
			InternalEObject oldLeftParent = (InternalEObject)leftParent;
			leftParent = eResolveProxy(oldLeftParent);
			if (leftParent != oldLeftParent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_PARENT, oldLeftParent, leftParent));
			}
		}
		return leftParent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject basicGetLeftParent() {
		return leftParent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLeftParent(EObject newLeftParent) {
		EObject oldLeftParent = leftParent;
		leftParent = newLeftParent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_PARENT, oldLeftParent, leftParent));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject getRightParent() {
		if (rightParent != null && rightParent.eIsProxy()) {
			InternalEObject oldRightParent = (InternalEObject)rightParent;
			rightParent = eResolveProxy(oldRightParent);
			if (rightParent != oldRightParent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_PARENT, oldRightParent, rightParent));
			}
		}
		return rightParent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject basicGetRightParent() {
		return rightParent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setRightParent(EObject newRightParent) {
		EObject oldRightParent = rightParent;
		rightParent = newRightParent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_PARENT, oldRightParent, rightParent));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ConflictingDiffGroup getLeftDiff() {
		return leftDiff;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetLeftDiff(ConflictingDiffGroup newLeftDiff, NotificationChain msgs) {
		ConflictingDiffGroup oldLeftDiff = leftDiff;
		leftDiff = newLeftDiff;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_DIFF, oldLeftDiff, newLeftDiff);
			if (msgs == null)
				return notification;
			msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLeftDiff(ConflictingDiffGroup newLeftDiff) {
		if (newLeftDiff != leftDiff) {
			NotificationChain msgs = null;
			if (leftDiff != null)
				msgs = ((InternalEObject)leftDiff).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_DIFF, null, msgs);
			if (newLeftDiff != null)
				msgs = ((InternalEObject)newLeftDiff).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_DIFF, null, msgs);
			msgs = basicSetLeftDiff(newLeftDiff, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_DIFF, newLeftDiff, newLeftDiff));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ConflictingDiffGroup getRightDiff() {
		return rightDiff;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetRightDiff(ConflictingDiffGroup newRightDiff, NotificationChain msgs) {
		ConflictingDiffGroup oldRightDiff = rightDiff;
		rightDiff = newRightDiff;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_DIFF, oldRightDiff, newRightDiff);
			if (msgs == null)
				return notification;
			msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setRightDiff(ConflictingDiffGroup newRightDiff) {
		if (newRightDiff != rightDiff) {
			NotificationChain msgs = null;
			if (rightDiff != null)
				msgs = ((InternalEObject)rightDiff).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_DIFF, null, msgs);
			if (newRightDiff != null)
				msgs = ((InternalEObject)newRightDiff).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_DIFF, null, msgs);
			msgs = basicSetRightDiff(newRightDiff, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_DIFF, newRightDiff, newRightDiff));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_DIFF:
				return basicSetLeftDiff(null, msgs);
			case DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_DIFF:
				return basicSetRightDiff(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_PARENT:
				if (resolve)
					return getLeftParent();
				return basicGetLeftParent();
			case DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_PARENT:
				if (resolve)
					return getRightParent();
				return basicGetRightParent();
			case DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_DIFF:
				return getLeftDiff();
			case DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_DIFF:
				return getRightDiff();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_PARENT:
				setLeftParent((EObject)newValue);
				return;
			case DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_PARENT:
				setRightParent((EObject)newValue);
				return;
			case DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_DIFF:
				setLeftDiff((ConflictingDiffGroup)newValue);
				return;
			case DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_DIFF:
				setRightDiff((ConflictingDiffGroup)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_PARENT:
				setLeftParent((EObject)null);
				return;
			case DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_PARENT:
				setRightParent((EObject)null);
				return;
			case DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_DIFF:
				setLeftDiff((ConflictingDiffGroup)null);
				return;
			case DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_DIFF:
				setRightDiff((ConflictingDiffGroup)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_PARENT:
				return leftParent != null;
			case DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_PARENT:
				return rightParent != null;
			case DiffPackage.CONFLICTING_DIFF_ELEMENT__LEFT_DIFF:
				return leftDiff != null;
			case DiffPackage.CONFLICTING_DIFF_ELEMENT__RIGHT_DIFF:
				return rightDiff != null;
		}
		return super.eIsSet(featureID);
	}

} // ConflictingDiffElementImpl
