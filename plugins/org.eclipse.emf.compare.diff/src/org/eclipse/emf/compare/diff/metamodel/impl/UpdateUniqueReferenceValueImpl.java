/**
 * <copyright>
 * </copyright>
 *
 * $Id: UpdateUniqueReferenceValueImpl.java,v 1.1 2007/06/25 16:05:20 cbrun Exp $
 */
package org.eclipse.emf.compare.diff.metamodel.impl;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Update Unique Reference Value</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateUniqueReferenceValueImpl#getLeftTarget <em>Left Target</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateUniqueReferenceValueImpl#getRightTarget <em>Right Target</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UpdateUniqueReferenceValueImpl extends UpdateReferenceImpl implements UpdateUniqueReferenceValue {
	/**
	 * The cached value of the '{@link #getLeftTarget() <em>Left Target</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeftTarget()
	 * @generated
	 * @ordered
	 */
	protected EList leftTarget = null;

	/**
	 * The cached value of the '{@link #getRightTarget() <em>Right Target</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRightTarget()
	 * @generated
	 * @ordered
	 */
	protected EList rightTarget = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UpdateUniqueReferenceValueImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return DiffPackage.Literals.UPDATE_UNIQUE_REFERENCE_VALUE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getLeftTarget() {
		if (leftTarget == null) {
			leftTarget = new EObjectResolvingEList(EObject.class, this,
					DiffPackage.UPDATE_UNIQUE_REFERENCE_VALUE__LEFT_TARGET);
		}
		return leftTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getRightTarget() {
		if (rightTarget == null) {
			rightTarget = new EObjectResolvingEList(EObject.class, this,
					DiffPackage.UPDATE_UNIQUE_REFERENCE_VALUE__RIGHT_TARGET);
		}
		return rightTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.UPDATE_UNIQUE_REFERENCE_VALUE__LEFT_TARGET:
				return getLeftTarget();
			case DiffPackage.UPDATE_UNIQUE_REFERENCE_VALUE__RIGHT_TARGET:
				return getRightTarget();
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
			case DiffPackage.UPDATE_UNIQUE_REFERENCE_VALUE__LEFT_TARGET:
				getLeftTarget().clear();
				getLeftTarget().addAll((Collection)newValue);
				return;
			case DiffPackage.UPDATE_UNIQUE_REFERENCE_VALUE__RIGHT_TARGET:
				getRightTarget().clear();
				getRightTarget().addAll((Collection)newValue);
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
			case DiffPackage.UPDATE_UNIQUE_REFERENCE_VALUE__LEFT_TARGET:
				getLeftTarget().clear();
				return;
			case DiffPackage.UPDATE_UNIQUE_REFERENCE_VALUE__RIGHT_TARGET:
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
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DiffPackage.UPDATE_UNIQUE_REFERENCE_VALUE__LEFT_TARGET:
				return leftTarget != null && !leftTarget.isEmpty();
			case DiffPackage.UPDATE_UNIQUE_REFERENCE_VALUE__RIGHT_TARGET:
				return rightTarget != null && !rightTarget.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //UpdateUniqueReferenceValueImpl
