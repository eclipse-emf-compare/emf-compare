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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodeMultiValueReference;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Node Multi Value Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeMultiValueReferenceImpl#getMultiValuedReference <em>Multi Valued Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
//Generated code, suppressing all warnings
@SuppressWarnings("all")
public class NodeMultiValueReferenceImpl extends NodeImpl implements NodeMultiValueReference {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getMultiValuedReference() <em>Multi Valued Reference</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultiValuedReference()
	 * @generated
	 * @ordered
	 */
	protected EList<Node> multiValuedReference;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NodeMultiValueReferenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NodesPackage.Literals.NODE_MULTI_VALUE_REFERENCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Node> getMultiValuedReference() {
		if (multiValuedReference == null) {
			multiValuedReference = new EObjectResolvingEList<Node>(Node.class, this, NodesPackage.NODE_MULTI_VALUE_REFERENCE__MULTI_VALUED_REFERENCE);
		}
		return multiValuedReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case NodesPackage.NODE_MULTI_VALUE_REFERENCE__MULTI_VALUED_REFERENCE:
				return getMultiValuedReference();
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
			case NodesPackage.NODE_MULTI_VALUE_REFERENCE__MULTI_VALUED_REFERENCE:
				getMultiValuedReference().clear();
				getMultiValuedReference().addAll((Collection<? extends Node>)newValue);
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
			case NodesPackage.NODE_MULTI_VALUE_REFERENCE__MULTI_VALUED_REFERENCE:
				getMultiValuedReference().clear();
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
			case NodesPackage.NODE_MULTI_VALUE_REFERENCE__MULTI_VALUED_REFERENCE:
				return multiValuedReference != null && !multiValuedReference.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //NodeMultiValueReferenceImpl
