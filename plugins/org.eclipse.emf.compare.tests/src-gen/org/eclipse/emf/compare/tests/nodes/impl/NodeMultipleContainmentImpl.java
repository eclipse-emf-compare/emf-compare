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
import org.eclipse.emf.compare.tests.nodes.NodeMultipleContainment;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Node Multiple Containment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeMultipleContainmentImpl#getContainmentRef2 <em>Containment Ref2</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeMultipleContainmentImpl#getContainmentRef3 <em>Containment Ref3</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
// Generated code, suppressing all warnings
@SuppressWarnings("all")
public class NodeMultipleContainmentImpl extends NodeImpl implements NodeMultipleContainment {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getContainmentRef2() <em>Containment Ref2</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainmentRef2()
	 * @generated
	 * @ordered
	 */
	protected EList<Node> containmentRef2;

	/**
	 * The cached value of the '{@link #getContainmentRef3() <em>Containment Ref3</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainmentRef3()
	 * @generated
	 * @ordered
	 */
	protected EList<Node> containmentRef3;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NodeMultipleContainmentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NodesPackage.Literals.NODE_MULTIPLE_CONTAINMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Node> getContainmentRef2() {
		if (containmentRef2 == null) {
			containmentRef2 = new EObjectContainmentEList.Resolving<Node>(Node.class, this, NodesPackage.NODE_MULTIPLE_CONTAINMENT__CONTAINMENT_REF2);
		}
		return containmentRef2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Node> getContainmentRef3() {
		if (containmentRef3 == null) {
			containmentRef3 = new EObjectContainmentEList.Resolving<Node>(Node.class, this, NodesPackage.NODE_MULTIPLE_CONTAINMENT__CONTAINMENT_REF3);
		}
		return containmentRef3;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case NodesPackage.NODE_MULTIPLE_CONTAINMENT__CONTAINMENT_REF2:
				return ((InternalEList<?>)getContainmentRef2()).basicRemove(otherEnd, msgs);
			case NodesPackage.NODE_MULTIPLE_CONTAINMENT__CONTAINMENT_REF3:
				return ((InternalEList<?>)getContainmentRef3()).basicRemove(otherEnd, msgs);
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
			case NodesPackage.NODE_MULTIPLE_CONTAINMENT__CONTAINMENT_REF2:
				return getContainmentRef2();
			case NodesPackage.NODE_MULTIPLE_CONTAINMENT__CONTAINMENT_REF3:
				return getContainmentRef3();
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
			case NodesPackage.NODE_MULTIPLE_CONTAINMENT__CONTAINMENT_REF2:
				getContainmentRef2().clear();
				getContainmentRef2().addAll((Collection<? extends Node>)newValue);
				return;
			case NodesPackage.NODE_MULTIPLE_CONTAINMENT__CONTAINMENT_REF3:
				getContainmentRef3().clear();
				getContainmentRef3().addAll((Collection<? extends Node>)newValue);
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
			case NodesPackage.NODE_MULTIPLE_CONTAINMENT__CONTAINMENT_REF2:
				getContainmentRef2().clear();
				return;
			case NodesPackage.NODE_MULTIPLE_CONTAINMENT__CONTAINMENT_REF3:
				getContainmentRef3().clear();
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
			case NodesPackage.NODE_MULTIPLE_CONTAINMENT__CONTAINMENT_REF2:
				return containmentRef2 != null && !containmentRef2.isEmpty();
			case NodesPackage.NODE_MULTIPLE_CONTAINMENT__CONTAINMENT_REF3:
				return containmentRef3 != null && !containmentRef3.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //NodeMultipleContainmentImpl
