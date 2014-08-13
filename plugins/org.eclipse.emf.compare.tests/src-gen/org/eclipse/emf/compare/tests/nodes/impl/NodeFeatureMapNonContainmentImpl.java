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
import org.eclipse.emf.compare.tests.nodes.NodeFeatureMapNonContainment;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Node Feature Map Non Containment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeFeatureMapNonContainmentImpl#getMapNC <em>Map NC</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeFeatureMapNonContainmentImpl#getFirstKeyNC <em>First Key NC</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeFeatureMapNonContainmentImpl#getSecondKeyNC <em>Second Key NC</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NodeFeatureMapNonContainmentImpl extends NodeImpl implements NodeFeatureMapNonContainment {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getMapNC() <em>Map NC</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMapNC()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap mapNC;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NodeFeatureMapNonContainmentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NodesPackage.Literals.NODE_FEATURE_MAP_NON_CONTAINMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getMapNC() {
		if (mapNC == null) {
			mapNC = new BasicFeatureMap(this, NodesPackage.NODE_FEATURE_MAP_NON_CONTAINMENT__MAP_NC);
		}
		return mapNC;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Node> getFirstKeyNC() {
		return getMapNC().list(NodesPackage.Literals.NODE_FEATURE_MAP_NON_CONTAINMENT__FIRST_KEY_NC);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Node> getSecondKeyNC() {
		return getMapNC().list(NodesPackage.Literals.NODE_FEATURE_MAP_NON_CONTAINMENT__SECOND_KEY_NC);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case NodesPackage.NODE_FEATURE_MAP_NON_CONTAINMENT__MAP_NC:
				return ((InternalEList<?>)getMapNC()).basicRemove(otherEnd, msgs);
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
			case NodesPackage.NODE_FEATURE_MAP_NON_CONTAINMENT__MAP_NC:
				if (coreType) return getMapNC();
				return ((FeatureMap.Internal)getMapNC()).getWrapper();
			case NodesPackage.NODE_FEATURE_MAP_NON_CONTAINMENT__FIRST_KEY_NC:
				return getFirstKeyNC();
			case NodesPackage.NODE_FEATURE_MAP_NON_CONTAINMENT__SECOND_KEY_NC:
				return getSecondKeyNC();
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
			case NodesPackage.NODE_FEATURE_MAP_NON_CONTAINMENT__MAP_NC:
				((FeatureMap.Internal)getMapNC()).set(newValue);
				return;
			case NodesPackage.NODE_FEATURE_MAP_NON_CONTAINMENT__FIRST_KEY_NC:
				getFirstKeyNC().clear();
				getFirstKeyNC().addAll((Collection<? extends Node>)newValue);
				return;
			case NodesPackage.NODE_FEATURE_MAP_NON_CONTAINMENT__SECOND_KEY_NC:
				getSecondKeyNC().clear();
				getSecondKeyNC().addAll((Collection<? extends Node>)newValue);
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
			case NodesPackage.NODE_FEATURE_MAP_NON_CONTAINMENT__MAP_NC:
				getMapNC().clear();
				return;
			case NodesPackage.NODE_FEATURE_MAP_NON_CONTAINMENT__FIRST_KEY_NC:
				getFirstKeyNC().clear();
				return;
			case NodesPackage.NODE_FEATURE_MAP_NON_CONTAINMENT__SECOND_KEY_NC:
				getSecondKeyNC().clear();
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
			case NodesPackage.NODE_FEATURE_MAP_NON_CONTAINMENT__MAP_NC:
				return mapNC != null && !mapNC.isEmpty();
			case NodesPackage.NODE_FEATURE_MAP_NON_CONTAINMENT__FIRST_KEY_NC:
				return !getFirstKeyNC().isEmpty();
			case NodesPackage.NODE_FEATURE_MAP_NON_CONTAINMENT__SECOND_KEY_NC:
				return !getSecondKeyNC().isEmpty();
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
		result.append(" (mapNC: "); //$NON-NLS-1$
		result.append(mapNC);
		result.append(')');
		return result.toString();
	}

} //NodeFeatureMapNonContainmentImpl
