/**
 * <copyright>
 * </copyright>
 *
 * $Id: DiffGroupImpl.java,v 1.1 2007/04/16 14:58:47 cbrun Exp $
 */
package org.eclipse.emf.compare.diff.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.compare.diff.DiffGroup;
import org.eclipse.emf.compare.diff.DiffPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.impl.DiffGroupImpl#getLeftParent <em>Left Parent</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiffGroupImpl extends DiffElementImpl implements DiffGroup {
	/**
	 * The cached value of the '{@link #getLeftParent() <em>Left Parent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeftParent()
	 * @generated
	 * @ordered
	 */
	protected EObject leftParent = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DiffGroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return DiffPackage.Literals.DIFF_GROUP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getLeftParent() {
		if (leftParent != null && leftParent.eIsProxy()) {
			InternalEObject oldLeftParent = (InternalEObject) leftParent;
			leftParent = eResolveProxy(oldLeftParent);
			if (leftParent != oldLeftParent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DiffPackage.DIFF_GROUP__LEFT_PARENT, oldLeftParent,
							leftParent));
			}
		}
		return leftParent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetLeftParent() {
		return leftParent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLeftParent(EObject newLeftParent) {
		EObject oldLeftParent = leftParent;
		leftParent = newLeftParent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffPackage.DIFF_GROUP__LEFT_PARENT, oldLeftParent,
					leftParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case DiffPackage.DIFF_GROUP__LEFT_PARENT:
			if (resolve)
				return getLeftParent();
			return basicGetLeftParent();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case DiffPackage.DIFF_GROUP__LEFT_PARENT:
			setLeftParent((EObject) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
		case DiffPackage.DIFF_GROUP__LEFT_PARENT:
			setLeftParent((EObject) null);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case DiffPackage.DIFF_GROUP__LEFT_PARENT:
			return leftParent != null;
		}
		return super.eIsSet(featureID);
	}

} //DiffGroupImpl