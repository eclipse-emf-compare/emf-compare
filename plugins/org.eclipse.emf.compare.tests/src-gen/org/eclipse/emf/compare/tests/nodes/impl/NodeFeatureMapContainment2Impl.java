/**
 * Copyright (c) 2011, 2014 Obeo.
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
import org.eclipse.emf.compare.tests.nodes.NodeFeatureMapContainment2;
import org.eclipse.emf.compare.tests.nodes.NodeMultipleContainment;
import org.eclipse.emf.compare.tests.nodes.NodeSingleValueContainment;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Node Feature Map Containment2</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeFeatureMapContainment2Impl#getMap2 <em>Map2</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeFeatureMapContainment2Impl#getMultiple <em>Multiple</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeFeatureMapContainment2Impl#getSingle <em>Single</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NodeFeatureMapContainment2Impl extends NodeImpl implements NodeFeatureMapContainment2 {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getMap2() <em>Map2</em>}' attribute list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getMap2()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap map2;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected NodeFeatureMapContainment2Impl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NodesPackage.Literals.NODE_FEATURE_MAP_CONTAINMENT2;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getMap2() {
		if (map2 == null) {
			map2 = new BasicFeatureMap(this, NodesPackage.NODE_FEATURE_MAP_CONTAINMENT2__MAP2);
		}
		return map2;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NodeMultipleContainment> getMultiple() {
		return getMap2().list(NodesPackage.Literals.NODE_FEATURE_MAP_CONTAINMENT2__MULTIPLE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NodeSingleValueContainment> getSingle() {
		return getMap2().list(NodesPackage.Literals.NODE_FEATURE_MAP_CONTAINMENT2__SINGLE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT2__MAP2:
				return ((InternalEList<?>)getMap2()).basicRemove(otherEnd, msgs);
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT2__MULTIPLE:
				return ((InternalEList<?>)getMultiple()).basicRemove(otherEnd, msgs);
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT2__SINGLE:
				return ((InternalEList<?>)getSingle()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT2__MAP2:
				if (coreType) return getMap2();
				return ((FeatureMap.Internal)getMap2()).getWrapper();
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT2__MULTIPLE:
				return getMultiple();
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT2__SINGLE:
				return getSingle();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT2__MAP2:
				((FeatureMap.Internal)getMap2()).set(newValue);
				return;
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT2__MULTIPLE:
				getMultiple().clear();
				getMultiple().addAll((Collection<? extends NodeMultipleContainment>)newValue);
				return;
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT2__SINGLE:
				getSingle().clear();
				getSingle().addAll((Collection<? extends NodeSingleValueContainment>)newValue);
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
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT2__MAP2:
				getMap2().clear();
				return;
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT2__MULTIPLE:
				getMultiple().clear();
				return;
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT2__SINGLE:
				getSingle().clear();
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
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT2__MAP2:
				return map2 != null && !map2.isEmpty();
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT2__MULTIPLE:
				return !getMultiple().isEmpty();
			case NodesPackage.NODE_FEATURE_MAP_CONTAINMENT2__SINGLE:
				return !getSingle().isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (map2: "); //$NON-NLS-1$
		result.append(map2);
		result.append(')');
		return result.toString();
	}

} // NodeFeatureMapContainment2Impl
