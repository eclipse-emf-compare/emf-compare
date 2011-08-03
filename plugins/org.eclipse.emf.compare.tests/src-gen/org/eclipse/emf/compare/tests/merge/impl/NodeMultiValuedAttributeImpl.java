/**
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.merge.impl;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.tests.merge.MergePackage;
import org.eclipse.emf.compare.tests.merge.NodeMultiValuedAttribute;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Node Multi Valued Attribute</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.tests.merge.impl.NodeMultiValuedAttributeImpl#getMultiValuedAttribute <em>Multi Valued Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NodeMultiValuedAttributeImpl extends NodeImpl implements NodeMultiValuedAttribute {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2011 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getMultiValuedAttribute() <em>Multi Valued Attribute</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultiValuedAttribute()
	 * @generated
	 * @ordered
	 */
	protected EList<String> multiValuedAttribute;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NodeMultiValuedAttributeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MergePackage.Literals.NODE_MULTI_VALUED_ATTRIBUTE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getMultiValuedAttribute() {
		if (multiValuedAttribute == null) {
			multiValuedAttribute = new EDataTypeUniqueEList<String>(String.class, this, MergePackage.NODE_MULTI_VALUED_ATTRIBUTE__MULTI_VALUED_ATTRIBUTE);
		}
		return multiValuedAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MergePackage.NODE_MULTI_VALUED_ATTRIBUTE__MULTI_VALUED_ATTRIBUTE:
				return getMultiValuedAttribute();
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
			case MergePackage.NODE_MULTI_VALUED_ATTRIBUTE__MULTI_VALUED_ATTRIBUTE:
				getMultiValuedAttribute().clear();
				getMultiValuedAttribute().addAll((Collection<? extends String>)newValue);
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
			case MergePackage.NODE_MULTI_VALUED_ATTRIBUTE__MULTI_VALUED_ATTRIBUTE:
				getMultiValuedAttribute().clear();
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
			case MergePackage.NODE_MULTI_VALUED_ATTRIBUTE__MULTI_VALUED_ATTRIBUTE:
				return multiValuedAttribute != null && !multiValuedAttribute.isEmpty();
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
		result.append(" (multiValuedAttribute: "); //$NON-NLS-1$
		result.append(multiValuedAttribute);
		result.append(')');
		return result.toString();
	}

} //NodeMultiValuedAttributeImpl
