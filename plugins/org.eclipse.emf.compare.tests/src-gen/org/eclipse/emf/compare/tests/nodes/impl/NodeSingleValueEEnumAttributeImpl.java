/**
 * Copyright (c) 2014 Obeo.
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

import org.eclipse.emf.compare.tests.nodes.NodeEnum;
import org.eclipse.emf.compare.tests.nodes.NodeSingleValueEEnumAttribute;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Node Single Value EEnum Attribute</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.nodes.impl.NodeSingleValueEEnumAttributeImpl#getSinglevalueEEnumAttribute <em>Singlevalue EEnum Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NodeSingleValueEEnumAttributeImpl extends NodeImpl implements NodeSingleValueEEnumAttribute {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2011, 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The default value of the '{@link #getSinglevalueEEnumAttribute() <em>Singlevalue EEnum Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSinglevalueEEnumAttribute()
	 * @generated
	 * @ordered
	 */
	protected static final NodeEnum SINGLEVALUE_EENUM_ATTRIBUTE_EDEFAULT = NodeEnum.A;

	/**
	 * The cached value of the '{@link #getSinglevalueEEnumAttribute() <em>Singlevalue EEnum Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSinglevalueEEnumAttribute()
	 * @generated
	 * @ordered
	 */
	protected NodeEnum singlevalueEEnumAttribute = SINGLEVALUE_EENUM_ATTRIBUTE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NodeSingleValueEEnumAttributeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NodesPackage.Literals.NODE_SINGLE_VALUE_EENUM_ATTRIBUTE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeEnum getSinglevalueEEnumAttribute() {
		return singlevalueEEnumAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSinglevalueEEnumAttribute(NodeEnum newSinglevalueEEnumAttribute) {
		NodeEnum oldSinglevalueEEnumAttribute = singlevalueEEnumAttribute;
		singlevalueEEnumAttribute = newSinglevalueEEnumAttribute == null ? SINGLEVALUE_EENUM_ATTRIBUTE_EDEFAULT : newSinglevalueEEnumAttribute;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NodesPackage.NODE_SINGLE_VALUE_EENUM_ATTRIBUTE__SINGLEVALUE_EENUM_ATTRIBUTE, oldSinglevalueEEnumAttribute, singlevalueEEnumAttribute));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case NodesPackage.NODE_SINGLE_VALUE_EENUM_ATTRIBUTE__SINGLEVALUE_EENUM_ATTRIBUTE:
				return getSinglevalueEEnumAttribute();
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
			case NodesPackage.NODE_SINGLE_VALUE_EENUM_ATTRIBUTE__SINGLEVALUE_EENUM_ATTRIBUTE:
				setSinglevalueEEnumAttribute((NodeEnum)newValue);
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
			case NodesPackage.NODE_SINGLE_VALUE_EENUM_ATTRIBUTE__SINGLEVALUE_EENUM_ATTRIBUTE:
				setSinglevalueEEnumAttribute(SINGLEVALUE_EENUM_ATTRIBUTE_EDEFAULT);
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
			case NodesPackage.NODE_SINGLE_VALUE_EENUM_ATTRIBUTE__SINGLEVALUE_EENUM_ATTRIBUTE:
				return singlevalueEEnumAttribute != SINGLEVALUE_EENUM_ATTRIBUTE_EDEFAULT;
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
		result.append(" (singlevalueEEnumAttribute: "); //$NON-NLS-1$
		result.append(singlevalueEEnumAttribute);
		result.append(')');
		return result.toString();
	}

} //NodeSingleValueEEnumAttributeImpl
