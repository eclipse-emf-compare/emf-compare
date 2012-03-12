/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: IndepAttributeChangeImpl.java,v 1.1 2010/09/10 15:23:07 cbrun Exp $
 */
package org.eclipse.emf.compare.mpatch.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.compare.mpatch.IndepAttributeChange;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Indep Attribute Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.IndepAttributeChangeImpl#getChangedAttribute <em>Changed Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class IndepAttributeChangeImpl extends IndepChangeImpl implements IndepAttributeChange {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2010, 2011 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";
	/**
	 * The cached value of the '{@link #getChangedAttribute() <em>Changed Attribute</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangedAttribute()
	 * @generated
	 * @ordered
	 */
	protected EAttribute changedAttribute;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IndepAttributeChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MPatchPackage.Literals.INDEP_ATTRIBUTE_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangedAttribute() {
		if (changedAttribute != null && changedAttribute.eIsProxy()) {
			InternalEObject oldChangedAttribute = (InternalEObject)changedAttribute;
			changedAttribute = (EAttribute)eResolveProxy(oldChangedAttribute);
			if (changedAttribute != oldChangedAttribute) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MPatchPackage.INDEP_ATTRIBUTE_CHANGE__CHANGED_ATTRIBUTE, oldChangedAttribute, changedAttribute));
			}
		}
		return changedAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute basicGetChangedAttribute() {
		return changedAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setChangedAttribute(EAttribute newChangedAttribute) {
		EAttribute oldChangedAttribute = changedAttribute;
		changedAttribute = newChangedAttribute;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_ATTRIBUTE_CHANGE__CHANGED_ATTRIBUTE, oldChangedAttribute, changedAttribute));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MPatchPackage.INDEP_ATTRIBUTE_CHANGE__CHANGED_ATTRIBUTE:
				if (resolve) return getChangedAttribute();
				return basicGetChangedAttribute();
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
			case MPatchPackage.INDEP_ATTRIBUTE_CHANGE__CHANGED_ATTRIBUTE:
				setChangedAttribute((EAttribute)newValue);
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
			case MPatchPackage.INDEP_ATTRIBUTE_CHANGE__CHANGED_ATTRIBUTE:
				setChangedAttribute((EAttribute)null);
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
			case MPatchPackage.INDEP_ATTRIBUTE_CHANGE__CHANGED_ATTRIBUTE:
				return changedAttribute != null;
		}
		return super.eIsSet(featureID);
	}

} //IndepAttributeChangeImpl
