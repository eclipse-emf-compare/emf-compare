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

import org.eclipse.emf.compare.tests.nodes.NodeOppositeRefOneToOne;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Node Opposite Ref One To One</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeOppositeRefOneToOneImpl#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeOppositeRefOneToOneImpl#getDestination <em>Destination</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
//Generated code, suppressing all warnings
@SuppressWarnings("all")
public class NodeOppositeRefOneToOneImpl extends NodeImpl implements NodeOppositeRefOneToOne {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getSource() <em>Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSource()
	 * @generated
	 * @ordered
	 */
	protected NodeOppositeRefOneToOne source;

	/**
	 * The cached value of the '{@link #getDestination() <em>Destination</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestination()
	 * @generated
	 * @ordered
	 */
	protected NodeOppositeRefOneToOne destination;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NodeOppositeRefOneToOneImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NodesPackage.Literals.NODE_OPPOSITE_REF_ONE_TO_ONE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeOppositeRefOneToOne getSource() {
		if (source != null && source.eIsProxy()) {
			InternalEObject oldSource = (InternalEObject)source;
			source = (NodeOppositeRefOneToOne)eResolveProxy(oldSource);
			if (source != oldSource) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__SOURCE, oldSource, source));
			}
		}
		return source;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeOppositeRefOneToOne basicGetSource() {
		return source;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSource(NodeOppositeRefOneToOne newSource, NotificationChain msgs) {
		NodeOppositeRefOneToOne oldSource = source;
		source = newSource;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__SOURCE, oldSource, newSource);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSource(NodeOppositeRefOneToOne newSource) {
		if (newSource != source) {
			NotificationChain msgs = null;
			if (source != null)
				msgs = ((InternalEObject)source).eInverseRemove(this, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__DESTINATION, NodeOppositeRefOneToOne.class, msgs);
			if (newSource != null)
				msgs = ((InternalEObject)newSource).eInverseAdd(this, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__DESTINATION, NodeOppositeRefOneToOne.class, msgs);
			msgs = basicSetSource(newSource, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__SOURCE, newSource, newSource));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeOppositeRefOneToOne getDestination() {
		if (destination != null && destination.eIsProxy()) {
			InternalEObject oldDestination = (InternalEObject)destination;
			destination = (NodeOppositeRefOneToOne)eResolveProxy(oldDestination);
			if (destination != oldDestination) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__DESTINATION, oldDestination, destination));
			}
		}
		return destination;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeOppositeRefOneToOne basicGetDestination() {
		return destination;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDestination(NodeOppositeRefOneToOne newDestination, NotificationChain msgs) {
		NodeOppositeRefOneToOne oldDestination = destination;
		destination = newDestination;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__DESTINATION, oldDestination, newDestination);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDestination(NodeOppositeRefOneToOne newDestination) {
		if (newDestination != destination) {
			NotificationChain msgs = null;
			if (destination != null)
				msgs = ((InternalEObject)destination).eInverseRemove(this, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__SOURCE, NodeOppositeRefOneToOne.class, msgs);
			if (newDestination != null)
				msgs = ((InternalEObject)newDestination).eInverseAdd(this, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__SOURCE, NodeOppositeRefOneToOne.class, msgs);
			msgs = basicSetDestination(newDestination, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__DESTINATION, newDestination, newDestination));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__SOURCE:
				if (source != null)
					msgs = ((InternalEObject)source).eInverseRemove(this, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__DESTINATION, NodeOppositeRefOneToOne.class, msgs);
				return basicSetSource((NodeOppositeRefOneToOne)otherEnd, msgs);
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__DESTINATION:
				if (destination != null)
					msgs = ((InternalEObject)destination).eInverseRemove(this, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__SOURCE, NodeOppositeRefOneToOne.class, msgs);
				return basicSetDestination((NodeOppositeRefOneToOne)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__SOURCE:
				return basicSetSource(null, msgs);
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__DESTINATION:
				return basicSetDestination(null, msgs);
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
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__SOURCE:
				if (resolve) return getSource();
				return basicGetSource();
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__DESTINATION:
				if (resolve) return getDestination();
				return basicGetDestination();
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
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__SOURCE:
				setSource((NodeOppositeRefOneToOne)newValue);
				return;
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__DESTINATION:
				setDestination((NodeOppositeRefOneToOne)newValue);
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
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__SOURCE:
				setSource((NodeOppositeRefOneToOne)null);
				return;
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__DESTINATION:
				setDestination((NodeOppositeRefOneToOne)null);
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
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__SOURCE:
				return source != null;
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_ONE__DESTINATION:
				return destination != null;
		}
		return super.eIsSet(featureID);
	}

} //NodeOppositeRefOneToOneImpl
