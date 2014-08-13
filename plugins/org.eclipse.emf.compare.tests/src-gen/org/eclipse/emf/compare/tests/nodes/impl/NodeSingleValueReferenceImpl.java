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
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodeSingleValueReference;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Node Single Value Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeSingleValueReferenceImpl#getSingleValuedReference <em>Single Valued Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
//Generated code, suppressing all warnings
@SuppressWarnings("all")
public class NodeSingleValueReferenceImpl extends NodeImpl implements NodeSingleValueReference {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getSingleValuedReference() <em>Single Valued Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSingleValuedReference()
	 * @generated
	 * @ordered
	 */
	protected Node singleValuedReference;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NodeSingleValueReferenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NodesPackage.Literals.NODE_SINGLE_VALUE_REFERENCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Node getSingleValuedReference() {
		if (singleValuedReference != null && singleValuedReference.eIsProxy()) {
			InternalEObject oldSingleValuedReference = (InternalEObject)singleValuedReference;
			singleValuedReference = (Node)eResolveProxy(oldSingleValuedReference);
			if (singleValuedReference != oldSingleValuedReference) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, NodesPackage.NODE_SINGLE_VALUE_REFERENCE__SINGLE_VALUED_REFERENCE, oldSingleValuedReference, singleValuedReference));
			}
		}
		return singleValuedReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Node basicGetSingleValuedReference() {
		return singleValuedReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSingleValuedReference(Node newSingleValuedReference) {
		Node oldSingleValuedReference = singleValuedReference;
		singleValuedReference = newSingleValuedReference;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NodesPackage.NODE_SINGLE_VALUE_REFERENCE__SINGLE_VALUED_REFERENCE, oldSingleValuedReference, singleValuedReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case NodesPackage.NODE_SINGLE_VALUE_REFERENCE__SINGLE_VALUED_REFERENCE:
				if (resolve) return getSingleValuedReference();
				return basicGetSingleValuedReference();
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
			case NodesPackage.NODE_SINGLE_VALUE_REFERENCE__SINGLE_VALUED_REFERENCE:
				setSingleValuedReference((Node)newValue);
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
			case NodesPackage.NODE_SINGLE_VALUE_REFERENCE__SINGLE_VALUED_REFERENCE:
				setSingleValuedReference((Node)null);
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
			case NodesPackage.NODE_SINGLE_VALUE_REFERENCE__SINGLE_VALUED_REFERENCE:
				return singleValuedReference != null;
		}
		return super.eIsSet(featureID);
	}

} //NodeSingleValueReferenceImpl
