/**
 * <copyright>
 * </copyright>
 *
 */
package org.eclipse.emf.compare.epatch.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.compare.epatch.EpatchPackage;
import org.eclipse.emf.compare.epatch.NamedResource;
import org.eclipse.emf.compare.epatch.ObjectRef;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Object Ref</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.epatch.impl.ObjectRefImpl#getLeftRes <em>Left Res</em>}</li>
 * <li>{@link org.eclipse.emf.compare.epatch.impl.ObjectRefImpl#getLeftFrag <em>Left Frag</em>}</li>
 * <li>{@link org.eclipse.emf.compare.epatch.impl.ObjectRefImpl#getRightRes <em>Right Res</em>}</li>
 * <li>{@link org.eclipse.emf.compare.epatch.impl.ObjectRefImpl#getRightFrag <em>Right Frag</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ObjectRefImpl extends NamedObjectImpl implements ObjectRef {
	/**
	 * The cached value of the '{@link #getLeftRes() <em>Left Res</em>}' reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getLeftRes()
	 * @generated
	 * @ordered
	 */
	protected NamedResource leftRes;

	/**
	 * The default value of the '{@link #getLeftFrag() <em>Left Frag</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getLeftFrag()
	 * @generated
	 * @ordered
	 */
	protected static final String LEFT_FRAG_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLeftFrag() <em>Left Frag</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getLeftFrag()
	 * @generated
	 * @ordered
	 */
	protected String leftFrag = LEFT_FRAG_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRightRes() <em>Right Res</em>}' reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getRightRes()
	 * @generated
	 * @ordered
	 */
	protected NamedResource rightRes;

	/**
	 * The default value of the '{@link #getRightFrag() <em>Right Frag</em>}' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #getRightFrag()
	 * @generated
	 * @ordered
	 */
	protected static final String RIGHT_FRAG_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRightFrag() <em>Right Frag</em>}' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #getRightFrag()
	 * @generated
	 * @ordered
	 */
	protected String rightFrag = RIGHT_FRAG_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ObjectRefImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EpatchPackage.Literals.OBJECT_REF;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NamedResource getLeftRes() {
		if (leftRes != null && leftRes.eIsProxy()) {
			InternalEObject oldLeftRes = (InternalEObject)leftRes;
			leftRes = (NamedResource)eResolveProxy(oldLeftRes);
			if (leftRes != oldLeftRes) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							EpatchPackage.OBJECT_REF__LEFT_RES, oldLeftRes, leftRes));
			}
		}
		return leftRes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NamedResource basicGetLeftRes() {
		return leftRes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLeftRes(NamedResource newLeftRes) {
		NamedResource oldLeftRes = leftRes;
		leftRes = newLeftRes;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EpatchPackage.OBJECT_REF__LEFT_RES,
					oldLeftRes, leftRes));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getLeftFrag() {
		return leftFrag;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLeftFrag(String newLeftFrag) {
		String oldLeftFrag = leftFrag;
		leftFrag = newLeftFrag;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EpatchPackage.OBJECT_REF__LEFT_FRAG,
					oldLeftFrag, leftFrag));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NamedResource getRightRes() {
		if (rightRes != null && rightRes.eIsProxy()) {
			InternalEObject oldRightRes = (InternalEObject)rightRes;
			rightRes = (NamedResource)eResolveProxy(oldRightRes);
			if (rightRes != oldRightRes) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							EpatchPackage.OBJECT_REF__RIGHT_RES, oldRightRes, rightRes));
			}
		}
		return rightRes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NamedResource basicGetRightRes() {
		return rightRes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setRightRes(NamedResource newRightRes) {
		NamedResource oldRightRes = rightRes;
		rightRes = newRightRes;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EpatchPackage.OBJECT_REF__RIGHT_RES,
					oldRightRes, rightRes));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getRightFrag() {
		return rightFrag;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setRightFrag(String newRightFrag) {
		String oldRightFrag = rightFrag;
		rightFrag = newRightFrag;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EpatchPackage.OBJECT_REF__RIGHT_FRAG,
					oldRightFrag, rightFrag));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EpatchPackage.OBJECT_REF__LEFT_RES:
				if (resolve)
					return getLeftRes();
				return basicGetLeftRes();
			case EpatchPackage.OBJECT_REF__LEFT_FRAG:
				return getLeftFrag();
			case EpatchPackage.OBJECT_REF__RIGHT_RES:
				if (resolve)
					return getRightRes();
				return basicGetRightRes();
			case EpatchPackage.OBJECT_REF__RIGHT_FRAG:
				return getRightFrag();
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
			case EpatchPackage.OBJECT_REF__LEFT_RES:
				setLeftRes((NamedResource)newValue);
				return;
			case EpatchPackage.OBJECT_REF__LEFT_FRAG:
				setLeftFrag((String)newValue);
				return;
			case EpatchPackage.OBJECT_REF__RIGHT_RES:
				setRightRes((NamedResource)newValue);
				return;
			case EpatchPackage.OBJECT_REF__RIGHT_FRAG:
				setRightFrag((String)newValue);
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
			case EpatchPackage.OBJECT_REF__LEFT_RES:
				setLeftRes((NamedResource)null);
				return;
			case EpatchPackage.OBJECT_REF__LEFT_FRAG:
				setLeftFrag(LEFT_FRAG_EDEFAULT);
				return;
			case EpatchPackage.OBJECT_REF__RIGHT_RES:
				setRightRes((NamedResource)null);
				return;
			case EpatchPackage.OBJECT_REF__RIGHT_FRAG:
				setRightFrag(RIGHT_FRAG_EDEFAULT);
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
			case EpatchPackage.OBJECT_REF__LEFT_RES:
				return leftRes != null;
			case EpatchPackage.OBJECT_REF__LEFT_FRAG:
				return LEFT_FRAG_EDEFAULT == null ? leftFrag != null : !LEFT_FRAG_EDEFAULT.equals(leftFrag);
			case EpatchPackage.OBJECT_REF__RIGHT_RES:
				return rightRes != null;
			case EpatchPackage.OBJECT_REF__RIGHT_FRAG:
				return RIGHT_FRAG_EDEFAULT == null ? rightFrag != null : !RIGHT_FRAG_EDEFAULT
						.equals(rightFrag);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (leftFrag: ");
		result.append(leftFrag);
		result.append(", rightFrag: ");
		result.append(rightFrag);
		result.append(')');
		return result.toString();
	}

} // ObjectRefImpl
