/**
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.nodes.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodeSingleValueContainment;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Node Single Value Containment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeSingleValueContainmentImpl#getSingleValueContainment <em>Single Value Containment</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
//Generated code, suppressing all warnings
@SuppressWarnings("all")
public class NodeSingleValueContainmentImpl extends NodeImpl implements NodeSingleValueContainment {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getSingleValueContainment() <em>Single Value Containment</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSingleValueContainment()
	 * @generated
	 * @ordered
	 */
	protected Node singleValueContainment;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NodeSingleValueContainmentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NodesPackage.Literals.NODE_SINGLE_VALUE_CONTAINMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Node getSingleValueContainment() {
		if (singleValueContainment != null && singleValueContainment.eIsProxy()) {
			InternalEObject oldSingleValueContainment = (InternalEObject)singleValueContainment;
			singleValueContainment = (Node)eResolveProxy(oldSingleValueContainment);
			if (singleValueContainment != oldSingleValueContainment) {
				InternalEObject newSingleValueContainment = (InternalEObject)singleValueContainment;
				NotificationChain msgs = oldSingleValueContainment.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - NodesPackage.NODE_SINGLE_VALUE_CONTAINMENT__SINGLE_VALUE_CONTAINMENT, null, null);
				if (newSingleValueContainment.eInternalContainer() == null) {
					msgs = newSingleValueContainment.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - NodesPackage.NODE_SINGLE_VALUE_CONTAINMENT__SINGLE_VALUE_CONTAINMENT, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, NodesPackage.NODE_SINGLE_VALUE_CONTAINMENT__SINGLE_VALUE_CONTAINMENT, oldSingleValueContainment, singleValueContainment));
			}
		}
		return singleValueContainment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Node basicGetSingleValueContainment() {
		return singleValueContainment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSingleValueContainment(Node newSingleValueContainment, NotificationChain msgs) {
		Node oldSingleValueContainment = singleValueContainment;
		singleValueContainment = newSingleValueContainment;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, NodesPackage.NODE_SINGLE_VALUE_CONTAINMENT__SINGLE_VALUE_CONTAINMENT, oldSingleValueContainment, newSingleValueContainment);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSingleValueContainment(Node newSingleValueContainment) {
		if (newSingleValueContainment != singleValueContainment) {
			NotificationChain msgs = null;
			if (singleValueContainment != null)
				msgs = ((InternalEObject)singleValueContainment).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - NodesPackage.NODE_SINGLE_VALUE_CONTAINMENT__SINGLE_VALUE_CONTAINMENT, null, msgs);
			if (newSingleValueContainment != null)
				msgs = ((InternalEObject)newSingleValueContainment).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - NodesPackage.NODE_SINGLE_VALUE_CONTAINMENT__SINGLE_VALUE_CONTAINMENT, null, msgs);
			msgs = basicSetSingleValueContainment(newSingleValueContainment, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NodesPackage.NODE_SINGLE_VALUE_CONTAINMENT__SINGLE_VALUE_CONTAINMENT, newSingleValueContainment, newSingleValueContainment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case NodesPackage.NODE_SINGLE_VALUE_CONTAINMENT__SINGLE_VALUE_CONTAINMENT:
				return basicSetSingleValueContainment(null, msgs);
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
			case NodesPackage.NODE_SINGLE_VALUE_CONTAINMENT__SINGLE_VALUE_CONTAINMENT:
				if (resolve) return getSingleValueContainment();
				return basicGetSingleValueContainment();
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
			case NodesPackage.NODE_SINGLE_VALUE_CONTAINMENT__SINGLE_VALUE_CONTAINMENT:
				setSingleValueContainment((Node)newValue);
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
			case NodesPackage.NODE_SINGLE_VALUE_CONTAINMENT__SINGLE_VALUE_CONTAINMENT:
				setSingleValueContainment((Node)null);
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
			case NodesPackage.NODE_SINGLE_VALUE_CONTAINMENT__SINGLE_VALUE_CONTAINMENT:
				return singleValueContainment != null;
		}
		return super.eIsSet(featureID);
	}

} //NodeSingleValueContainmentImpl
