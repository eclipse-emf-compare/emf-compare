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

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.tests.nodes.NodeOppositeRefOneToMany;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Node Opposite Ref One To Many</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeOppositeRefOneToManyImpl#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeOppositeRefOneToManyImpl#getDestination <em>Destination</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
//Generated code, suppressing all warnings
@SuppressWarnings("all")
public class NodeOppositeRefOneToManyImpl extends NodeImpl implements NodeOppositeRefOneToMany {
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
	protected NodeOppositeRefOneToMany source;

	/**
	 * The cached value of the '{@link #getDestination() <em>Destination</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestination()
	 * @generated
	 * @ordered
	 */
	protected EList<NodeOppositeRefOneToMany> destination;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NodeOppositeRefOneToManyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NodesPackage.Literals.NODE_OPPOSITE_REF_ONE_TO_MANY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeOppositeRefOneToMany getSource() {
		if (source != null && source.eIsProxy()) {
			InternalEObject oldSource = (InternalEObject)source;
			source = (NodeOppositeRefOneToMany)eResolveProxy(oldSource);
			if (source != oldSource) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__SOURCE, oldSource, source));
			}
		}
		return source;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeOppositeRefOneToMany basicGetSource() {
		return source;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSource(NodeOppositeRefOneToMany newSource, NotificationChain msgs) {
		NodeOppositeRefOneToMany oldSource = source;
		source = newSource;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__SOURCE, oldSource, newSource);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSource(NodeOppositeRefOneToMany newSource) {
		if (newSource != source) {
			NotificationChain msgs = null;
			if (source != null)
				msgs = ((InternalEObject)source).eInverseRemove(this, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__DESTINATION, NodeOppositeRefOneToMany.class, msgs);
			if (newSource != null)
				msgs = ((InternalEObject)newSource).eInverseAdd(this, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__DESTINATION, NodeOppositeRefOneToMany.class, msgs);
			msgs = basicSetSource(newSource, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__SOURCE, newSource, newSource));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NodeOppositeRefOneToMany> getDestination() {
		if (destination == null) {
			destination = new EObjectWithInverseResolvingEList<NodeOppositeRefOneToMany>(NodeOppositeRefOneToMany.class, this, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__DESTINATION, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__SOURCE);
		}
		return destination;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__SOURCE:
				if (source != null)
					msgs = ((InternalEObject)source).eInverseRemove(this, NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__DESTINATION, NodeOppositeRefOneToMany.class, msgs);
				return basicSetSource((NodeOppositeRefOneToMany)otherEnd, msgs);
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__DESTINATION:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getDestination()).basicAdd(otherEnd, msgs);
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
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__SOURCE:
				return basicSetSource(null, msgs);
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__DESTINATION:
				return ((InternalEList<?>)getDestination()).basicRemove(otherEnd, msgs);
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
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__SOURCE:
				if (resolve) return getSource();
				return basicGetSource();
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__DESTINATION:
				return getDestination();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__SOURCE:
				setSource((NodeOppositeRefOneToMany)newValue);
				return;
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__DESTINATION:
				getDestination().clear();
				getDestination().addAll((Collection<? extends NodeOppositeRefOneToMany>)newValue);
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
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__SOURCE:
				setSource((NodeOppositeRefOneToMany)null);
				return;
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__DESTINATION:
				getDestination().clear();
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
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__SOURCE:
				return source != null;
			case NodesPackage.NODE_OPPOSITE_REF_ONE_TO_MANY__DESTINATION:
				return destination != null && !destination.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //NodeOppositeRefOneToManyImpl
