/*******************************************************************************
 * Copyright (c) 2008, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.metamodel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.compare.match.metamodel.MatchPackage;
import org.eclipse.emf.compare.match.metamodel.Side;
import org.eclipse.emf.compare.match.metamodel.UnmatchElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Unmatch Element</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.impl.UnmatchElementImpl#getElement <em>Element</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.impl.UnmatchElementImpl#isConflicting <em>Conflicting</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.impl.UnmatchElementImpl#isRemote <em>Remote</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.impl.UnmatchElementImpl#getSide <em>Side</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UnmatchElementImpl extends EObjectImpl implements UnmatchElement {
	/**
	 * The cached value of the '{@link #getElement() <em>Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElement()
	 * @generated
	 * @ordered
	 */
	protected EObject element;

	/**
	 * The default value of the '{@link #isConflicting() <em>Conflicting</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isConflicting()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CONFLICTING_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isConflicting() <em>Conflicting</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isConflicting()
	 * @generated
	 * @ordered
	 */
	protected boolean conflicting = CONFLICTING_EDEFAULT;

	/**
	 * The default value of the '{@link #isRemote() <em>Remote</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRemote()
	 * @generated
	 * @ordered
	 */
	protected static final boolean REMOTE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isRemote() <em>Remote</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRemote()
	 * @generated
	 * @ordered
	 */
	protected boolean remote = REMOTE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSide() <em>Side</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSide()
	 * @generated
	 * @ordered
	 */
	protected static final Side SIDE_EDEFAULT = Side.LEFT;

	/**
	 * The cached value of the '{@link #getSide() <em>Side</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSide()
	 * @generated
	 * @ordered
	 */
	protected Side side = SIDE_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected UnmatchElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MatchPackage.Literals.UNMATCH_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getElement() {
		if (element != null && element.eIsProxy()) {
			InternalEObject oldElement = (InternalEObject)element;
			element = eResolveProxy(oldElement);
			if (element != oldElement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							MatchPackage.UNMATCH_ELEMENT__ELEMENT, oldElement, element));
			}
		}
		return element;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetElement() {
		return element;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setElement(EObject newElement) {
		EObject oldElement = element;
		element = newElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MatchPackage.UNMATCH_ELEMENT__ELEMENT,
					oldElement, element));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isConflicting() {
		return conflicting;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConflicting(boolean newConflicting) {
		boolean oldConflicting = conflicting;
		conflicting = newConflicting;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MatchPackage.UNMATCH_ELEMENT__CONFLICTING,
					oldConflicting, conflicting));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isRemote() {
		return remote;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRemote(boolean newRemote) {
		boolean oldRemote = remote;
		remote = newRemote;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MatchPackage.UNMATCH_ELEMENT__REMOTE,
					oldRemote, remote));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Side getSide() {
		return side;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSide(Side newSide) {
		Side oldSide = side;
		side = newSide == null ? SIDE_EDEFAULT : newSide;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MatchPackage.UNMATCH_ELEMENT__SIDE,
					oldSide, side));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MatchPackage.UNMATCH_ELEMENT__ELEMENT:
				if (resolve)
					return getElement();
				return basicGetElement();
			case MatchPackage.UNMATCH_ELEMENT__CONFLICTING:
				return isConflicting();
			case MatchPackage.UNMATCH_ELEMENT__REMOTE:
				return isRemote() ? Boolean.TRUE : Boolean.FALSE;
			case MatchPackage.UNMATCH_ELEMENT__SIDE:
				return getSide();
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
			case MatchPackage.UNMATCH_ELEMENT__ELEMENT:
				setElement((EObject)newValue);
				return;
			case MatchPackage.UNMATCH_ELEMENT__CONFLICTING:
				setConflicting((Boolean)newValue);
				return;
			case MatchPackage.UNMATCH_ELEMENT__REMOTE:
				setRemote(((Boolean)newValue).booleanValue());
				return;
			case MatchPackage.UNMATCH_ELEMENT__SIDE:
				setSide((Side)newValue);
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
			case MatchPackage.UNMATCH_ELEMENT__ELEMENT:
				setElement((EObject)null);
				return;
			case MatchPackage.UNMATCH_ELEMENT__CONFLICTING:
				setConflicting(CONFLICTING_EDEFAULT);
				return;
			case MatchPackage.UNMATCH_ELEMENT__REMOTE:
				setRemote(REMOTE_EDEFAULT);
				return;
			case MatchPackage.UNMATCH_ELEMENT__SIDE:
				setSide(SIDE_EDEFAULT);
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
			case MatchPackage.UNMATCH_ELEMENT__ELEMENT:
				return element != null;
			case MatchPackage.UNMATCH_ELEMENT__CONFLICTING:
				return conflicting != CONFLICTING_EDEFAULT;
			case MatchPackage.UNMATCH_ELEMENT__REMOTE:
				return remote != REMOTE_EDEFAULT;
			case MatchPackage.UNMATCH_ELEMENT__SIDE:
				return side != SIDE_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (conflicting: "); //$NON-NLS-1$
		result.append(conflicting);
		result.append(", remote: "); //$NON-NLS-1$
		result.append(remote);
		result.append(", side: "); //$NON-NLS-1$
		result.append(side);
		result.append(')');
		return result.toString();
	}

} // UnmatchElementImpl
