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

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodeFeatureMapContainment;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Node Feature Map Containment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeFeatureMapContainmentImpl#getMap <em>Map</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeFeatureMapContainmentImpl#getFirstKey <em>First Key</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeFeatureMapContainmentImpl#getSecondKey <em>Second Key</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NodeFeatureMapContainmentImpl extends NodeImpl implements NodeFeatureMapContainment {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getMap() <em>Map</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMap()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap map;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NodeFeatureMapContainmentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NodesPackage.Literals.NODE_FEATURE_MAP_CONTAINMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getMap() {
		if (map == null) {
			map = new BasicFeatureMap(this, NodesPackage.NODE_FEATURE_MAP_CONTAINMENT__MAP);
		}
		return map;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Node> getFirstKey() {
		return getMap().list(NodesPackage.Literals.NODE_FEATURE_MAP_CONTAINMENT__FIRST_KEY);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Node> getSecondKey() {
		return getMap().list(NodesPackage.Literals.NODE_FEATURE_MAP_CONTAINMENT__SECOND_KEY);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT__MAP:
				return ((InternalEList<?>)getMap()).basicRemove(otherEnd, msgs);
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT__FIRST_KEY:
				return ((InternalEList<?>)getFirstKey()).basicRemove(otherEnd, msgs);
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT__SECOND_KEY:
				return ((InternalEList<?>)getSecondKey()).basicRemove(otherEnd, msgs);
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
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT__MAP:
				if (coreType) return getMap();
				return ((FeatureMap.Internal)getMap()).getWrapper();
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT__FIRST_KEY:
				return getFirstKey();
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT__SECOND_KEY:
				return getSecondKey();
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
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT__MAP:
				((FeatureMap.Internal)getMap()).set(newValue);
				return;
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT__FIRST_KEY:
				getFirstKey().clear();
				getFirstKey().addAll((Collection<? extends Node>)newValue);
				return;
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT__SECOND_KEY:
				getSecondKey().clear();
				getSecondKey().addAll((Collection<? extends Node>)newValue);
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
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT__MAP:
				getMap().clear();
				return;
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT__FIRST_KEY:
				getFirstKey().clear();
				return;
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT__SECOND_KEY:
				getSecondKey().clear();
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
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT__MAP:
				return map != null && !map.isEmpty();
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT__FIRST_KEY:
				return !getFirstKey().isEmpty();
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT__SECOND_KEY:
				return !getSecondKey().isEmpty();
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
		result.append(" (map: "); //$NON-NLS-1$
		result.append(map);
		result.append(')');
		return result.toString();
	}

} //NodeFeatureMapContainmentImpl
