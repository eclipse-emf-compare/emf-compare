/**
 * <copyright>
 * </copyright>
 *
 * $Id: UpdateContainmentFeatureImpl.java,v 1.1 2008/09/15 13:20:47 lgoubet Exp $
 */
package org.eclipse.emf.compare.diff.metamodel.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.UpdateContainmentFeature;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Update Containment Feature</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateContainmentFeatureImpl#getLeftContainmentFeature <em>Left Containment Feature</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateContainmentFeatureImpl#getRightContainmentFeature <em>Right Containment Feature</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UpdateContainmentFeatureImpl extends MoveModelElementImpl implements UpdateContainmentFeature {
	/**
	 * The cached value of the '{@link #getLeftContainmentFeature() <em>Left Containment Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeftContainmentFeature()
	 * @generated
	 * @ordered
	 */
	protected EStructuralFeature leftContainmentFeature;

	/**
	 * The cached value of the '{@link #getRightContainmentFeature() <em>Right Containment Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRightContainmentFeature()
	 * @generated
	 * @ordered
	 */
	protected EStructuralFeature rightContainmentFeature;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UpdateContainmentFeatureImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.UPDATE_CONTAINMENT_FEATURE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EStructuralFeature getLeftContainmentFeature() {
		if (leftContainmentFeature != null && leftContainmentFeature.eIsProxy()) {
			InternalEObject oldLeftContainmentFeature = (InternalEObject)leftContainmentFeature;
			leftContainmentFeature = (EStructuralFeature)eResolveProxy(oldLeftContainmentFeature);
			if (leftContainmentFeature != oldLeftContainmentFeature) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DiffPackage.UPDATE_CONTAINMENT_FEATURE__LEFT_CONTAINMENT_FEATURE,
							oldLeftContainmentFeature, leftContainmentFeature));
			}
		}
		return leftContainmentFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EStructuralFeature basicGetLeftContainmentFeature() {
		return leftContainmentFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLeftContainmentFeature(EStructuralFeature newLeftContainmentFeature) {
		EStructuralFeature oldLeftContainmentFeature = leftContainmentFeature;
		leftContainmentFeature = newLeftContainmentFeature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffPackage.UPDATE_CONTAINMENT_FEATURE__LEFT_CONTAINMENT_FEATURE,
					oldLeftContainmentFeature, leftContainmentFeature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EStructuralFeature getRightContainmentFeature() {
		if (rightContainmentFeature != null && rightContainmentFeature.eIsProxy()) {
			InternalEObject oldRightContainmentFeature = (InternalEObject)rightContainmentFeature;
			rightContainmentFeature = (EStructuralFeature)eResolveProxy(oldRightContainmentFeature);
			if (rightContainmentFeature != oldRightContainmentFeature) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DiffPackage.UPDATE_CONTAINMENT_FEATURE__RIGHT_CONTAINMENT_FEATURE,
							oldRightContainmentFeature, rightContainmentFeature));
			}
		}
		return rightContainmentFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EStructuralFeature basicGetRightContainmentFeature() {
		return rightContainmentFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRightContainmentFeature(EStructuralFeature newRightContainmentFeature) {
		EStructuralFeature oldRightContainmentFeature = rightContainmentFeature;
		rightContainmentFeature = newRightContainmentFeature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffPackage.UPDATE_CONTAINMENT_FEATURE__RIGHT_CONTAINMENT_FEATURE,
					oldRightContainmentFeature, rightContainmentFeature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.UPDATE_CONTAINMENT_FEATURE__LEFT_CONTAINMENT_FEATURE:
				if (resolve)
					return getLeftContainmentFeature();
				return basicGetLeftContainmentFeature();
			case DiffPackage.UPDATE_CONTAINMENT_FEATURE__RIGHT_CONTAINMENT_FEATURE:
				if (resolve)
					return getRightContainmentFeature();
				return basicGetRightContainmentFeature();
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
			case DiffPackage.UPDATE_CONTAINMENT_FEATURE__LEFT_CONTAINMENT_FEATURE:
				setLeftContainmentFeature((EStructuralFeature)newValue);
				return;
			case DiffPackage.UPDATE_CONTAINMENT_FEATURE__RIGHT_CONTAINMENT_FEATURE:
				setRightContainmentFeature((EStructuralFeature)newValue);
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
			case DiffPackage.UPDATE_CONTAINMENT_FEATURE__LEFT_CONTAINMENT_FEATURE:
				setLeftContainmentFeature((EStructuralFeature)null);
				return;
			case DiffPackage.UPDATE_CONTAINMENT_FEATURE__RIGHT_CONTAINMENT_FEATURE:
				setRightContainmentFeature((EStructuralFeature)null);
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
			case DiffPackage.UPDATE_CONTAINMENT_FEATURE__LEFT_CONTAINMENT_FEATURE:
				return leftContainmentFeature != null;
			case DiffPackage.UPDATE_CONTAINMENT_FEATURE__RIGHT_CONTAINMENT_FEATURE:
				return rightContainmentFeature != null;
		}
		return super.eIsSet(featureID);
	}

} //UpdateContainmentFeatureImpl
