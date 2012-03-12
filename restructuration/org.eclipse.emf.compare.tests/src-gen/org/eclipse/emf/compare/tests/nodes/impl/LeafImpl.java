/**
 * Copyright (c) 2009, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *     Stephen McCants - Initial API and implementation
 */
package org.eclipse.emf.compare.tests.nodes.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.compare.tests.external.Holder;
import org.eclipse.emf.compare.tests.external.NoncontainmentHolder;
import org.eclipse.emf.compare.tests.external.StringHolder;

import org.eclipse.emf.compare.tests.nodes.Leaf;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;

import org.eclipse.emf.compare.tests.nonemf.NonEMFStringHolder;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Leaf</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.LeafImpl#getNoncontainmentHolder <em>Noncontainment Holder</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.LeafImpl#getContainmentHolder <em>Containment Holder</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.LeafImpl#getNumber <em>Number</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.LeafImpl#getHolder <em>Holder</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.LeafImpl#getNonEMF <em>Non EMF</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.LeafImpl#getNoncontainmentNoncontainmentHolder <em>Noncontainment Noncontainment Holder</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LeafImpl extends NodeImpl implements Leaf {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2009, 2012 IBM Corporation and others.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n \r\nContributors:\r\n    Stephen McCants - Initial API and implementation"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getNoncontainmentHolder() <em>Noncontainment Holder</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNoncontainmentHolder()
	 * @generated
	 * @ordered
	 */
	protected StringHolder noncontainmentHolder;

	/**
	 * The cached value of the '{@link #getContainmentHolder() <em>Containment Holder</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainmentHolder()
	 * @generated
	 * @ordered
	 */
	protected StringHolder containmentHolder;

	/**
	 * The default value of the '{@link #getNumber() <em>Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumber()
	 * @generated
	 * @ordered
	 */
	protected static final int NUMBER_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNumber() <em>Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumber()
	 * @generated
	 * @ordered
	 */
	protected int number = NUMBER_EDEFAULT;

	/**
	 * The cached value of the '{@link #getHolder() <em>Holder</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHolder()
	 * @generated
	 * @ordered
	 */
	protected Holder holder;

	/**
	 * The default value of the '{@link #getNonEMF() <em>Non EMF</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNonEMF()
	 * @generated
	 * @ordered
	 */
	protected static final NonEMFStringHolder NON_EMF_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNonEMF() <em>Non EMF</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNonEMF()
	 * @generated
	 * @ordered
	 */
	protected NonEMFStringHolder nonEMF = NON_EMF_EDEFAULT;

	/**
	 * The cached value of the '{@link #getNoncontainmentNoncontainmentHolder() <em>Noncontainment Noncontainment Holder</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNoncontainmentNoncontainmentHolder()
	 * @generated
	 * @ordered
	 */
	protected NoncontainmentHolder noncontainmentNoncontainmentHolder;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LeafImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NodesPackage.Literals.LEAF;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StringHolder getNoncontainmentHolder() {
		if (noncontainmentHolder != null && noncontainmentHolder.eIsProxy()) {
			InternalEObject oldNoncontainmentHolder = (InternalEObject)noncontainmentHolder;
			noncontainmentHolder = (StringHolder)eResolveProxy(oldNoncontainmentHolder);
			if (noncontainmentHolder != oldNoncontainmentHolder) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, NodesPackage.LEAF__NONCONTAINMENT_HOLDER, oldNoncontainmentHolder, noncontainmentHolder));
			}
		}
		return noncontainmentHolder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StringHolder basicGetNoncontainmentHolder() {
		return noncontainmentHolder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNoncontainmentHolder(StringHolder newNoncontainmentHolder) {
		StringHolder oldNoncontainmentHolder = noncontainmentHolder;
		noncontainmentHolder = newNoncontainmentHolder;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NodesPackage.LEAF__NONCONTAINMENT_HOLDER, oldNoncontainmentHolder, noncontainmentHolder));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StringHolder getContainmentHolder() {
		return containmentHolder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetContainmentHolder(StringHolder newContainmentHolder, NotificationChain msgs) {
		StringHolder oldContainmentHolder = containmentHolder;
		containmentHolder = newContainmentHolder;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, NodesPackage.LEAF__CONTAINMENT_HOLDER, oldContainmentHolder, newContainmentHolder);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContainmentHolder(StringHolder newContainmentHolder) {
		if (newContainmentHolder != containmentHolder) {
			NotificationChain msgs = null;
			if (containmentHolder != null)
				msgs = ((InternalEObject)containmentHolder).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - NodesPackage.LEAF__CONTAINMENT_HOLDER, null, msgs);
			if (newContainmentHolder != null)
				msgs = ((InternalEObject)newContainmentHolder).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - NodesPackage.LEAF__CONTAINMENT_HOLDER, null, msgs);
			msgs = basicSetContainmentHolder(newContainmentHolder, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NodesPackage.LEAF__CONTAINMENT_HOLDER, newContainmentHolder, newContainmentHolder));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNumber(int newNumber) {
		int oldNumber = number;
		number = newNumber;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NodesPackage.LEAF__NUMBER, oldNumber, number));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Holder getHolder() {
		return holder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetHolder(Holder newHolder, NotificationChain msgs) {
		Holder oldHolder = holder;
		holder = newHolder;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, NodesPackage.LEAF__HOLDER, oldHolder, newHolder);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHolder(Holder newHolder) {
		if (newHolder != holder) {
			NotificationChain msgs = null;
			if (holder != null)
				msgs = ((InternalEObject)holder).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - NodesPackage.LEAF__HOLDER, null, msgs);
			if (newHolder != null)
				msgs = ((InternalEObject)newHolder).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - NodesPackage.LEAF__HOLDER, null, msgs);
			msgs = basicSetHolder(newHolder, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NodesPackage.LEAF__HOLDER, newHolder, newHolder));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NonEMFStringHolder getNonEMF() {
		return nonEMF;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNonEMF(NonEMFStringHolder newNonEMF) {
		NonEMFStringHolder oldNonEMF = nonEMF;
		nonEMF = newNonEMF;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NodesPackage.LEAF__NON_EMF, oldNonEMF, nonEMF));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NoncontainmentHolder getNoncontainmentNoncontainmentHolder() {
		if (noncontainmentNoncontainmentHolder != null && noncontainmentNoncontainmentHolder.eIsProxy()) {
			InternalEObject oldNoncontainmentNoncontainmentHolder = (InternalEObject)noncontainmentNoncontainmentHolder;
			noncontainmentNoncontainmentHolder = (NoncontainmentHolder)eResolveProxy(oldNoncontainmentNoncontainmentHolder);
			if (noncontainmentNoncontainmentHolder != oldNoncontainmentNoncontainmentHolder) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, NodesPackage.LEAF__NONCONTAINMENT_NONCONTAINMENT_HOLDER, oldNoncontainmentNoncontainmentHolder, noncontainmentNoncontainmentHolder));
			}
		}
		return noncontainmentNoncontainmentHolder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NoncontainmentHolder basicGetNoncontainmentNoncontainmentHolder() {
		return noncontainmentNoncontainmentHolder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNoncontainmentNoncontainmentHolder(NoncontainmentHolder newNoncontainmentNoncontainmentHolder) {
		NoncontainmentHolder oldNoncontainmentNoncontainmentHolder = noncontainmentNoncontainmentHolder;
		noncontainmentNoncontainmentHolder = newNoncontainmentNoncontainmentHolder;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NodesPackage.LEAF__NONCONTAINMENT_NONCONTAINMENT_HOLDER, oldNoncontainmentNoncontainmentHolder, noncontainmentNoncontainmentHolder));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case NodesPackage.LEAF__CONTAINMENT_HOLDER:
				return basicSetContainmentHolder(null, msgs);
			case NodesPackage.LEAF__HOLDER:
				return basicSetHolder(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case NodesPackage.LEAF__NONCONTAINMENT_HOLDER:
				if (resolve) return getNoncontainmentHolder();
				return basicGetNoncontainmentHolder();
			case NodesPackage.LEAF__CONTAINMENT_HOLDER:
				return getContainmentHolder();
			case NodesPackage.LEAF__NUMBER:
				return getNumber();
			case NodesPackage.LEAF__HOLDER:
				return getHolder();
			case NodesPackage.LEAF__NON_EMF:
				return getNonEMF();
			case NodesPackage.LEAF__NONCONTAINMENT_NONCONTAINMENT_HOLDER:
				if (resolve) return getNoncontainmentNoncontainmentHolder();
				return basicGetNoncontainmentNoncontainmentHolder();
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
			case NodesPackage.LEAF__NONCONTAINMENT_HOLDER:
				setNoncontainmentHolder((StringHolder)newValue);
				return;
			case NodesPackage.LEAF__CONTAINMENT_HOLDER:
				setContainmentHolder((StringHolder)newValue);
				return;
			case NodesPackage.LEAF__NUMBER:
				setNumber((Integer)newValue);
				return;
			case NodesPackage.LEAF__HOLDER:
				setHolder((Holder)newValue);
				return;
			case NodesPackage.LEAF__NON_EMF:
				setNonEMF((NonEMFStringHolder)newValue);
				return;
			case NodesPackage.LEAF__NONCONTAINMENT_NONCONTAINMENT_HOLDER:
				setNoncontainmentNoncontainmentHolder((NoncontainmentHolder)newValue);
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
			case NodesPackage.LEAF__NONCONTAINMENT_HOLDER:
				setNoncontainmentHolder((StringHolder)null);
				return;
			case NodesPackage.LEAF__CONTAINMENT_HOLDER:
				setContainmentHolder((StringHolder)null);
				return;
			case NodesPackage.LEAF__NUMBER:
				setNumber(NUMBER_EDEFAULT);
				return;
			case NodesPackage.LEAF__HOLDER:
				setHolder((Holder)null);
				return;
			case NodesPackage.LEAF__NON_EMF:
				setNonEMF(NON_EMF_EDEFAULT);
				return;
			case NodesPackage.LEAF__NONCONTAINMENT_NONCONTAINMENT_HOLDER:
				setNoncontainmentNoncontainmentHolder((NoncontainmentHolder)null);
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
			case NodesPackage.LEAF__NONCONTAINMENT_HOLDER:
				return noncontainmentHolder != null;
			case NodesPackage.LEAF__CONTAINMENT_HOLDER:
				return containmentHolder != null;
			case NodesPackage.LEAF__NUMBER:
				return number != NUMBER_EDEFAULT;
			case NodesPackage.LEAF__HOLDER:
				return holder != null;
			case NodesPackage.LEAF__NON_EMF:
				return NON_EMF_EDEFAULT == null ? nonEMF != null : !NON_EMF_EDEFAULT.equals(nonEMF);
			case NodesPackage.LEAF__NONCONTAINMENT_NONCONTAINMENT_HOLDER:
				return noncontainmentNoncontainmentHolder != null;
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
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (number: "); //$NON-NLS-1$
		result.append(number);
		result.append(", nonEMF: "); //$NON-NLS-1$
		result.append(nonEMF);
		result.append(')');
		return result.toString();
	}

} //LeafImpl
