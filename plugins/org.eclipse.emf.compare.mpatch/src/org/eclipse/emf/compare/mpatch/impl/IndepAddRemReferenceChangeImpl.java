/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: IndepAddRemReferenceChangeImpl.java,v 1.1 2010/09/10 15:23:07 cbrun Exp $
 */
package org.eclipse.emf.compare.mpatch.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IndepAddRemReferenceChange;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Indep Add Rem Reference Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.IndepAddRemReferenceChangeImpl#getChangedReference <em>Changed Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class IndepAddRemReferenceChangeImpl extends IndepReferenceChangeImpl implements IndepAddRemReferenceChange {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2010 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";
	/**
	 * The cached value of the '{@link #getChangedReference() <em>Changed Reference</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangedReference()
	 * @generated
	 * @ordered
	 */
	protected IElementReference changedReference;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IndepAddRemReferenceChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MPatchPackage.Literals.INDEP_ADD_REM_REFERENCE_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IElementReference getChangedReference() {
		return changedReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetChangedReference(IElementReference newChangedReference, NotificationChain msgs) {
		IElementReference oldChangedReference = changedReference;
		changedReference = newChangedReference;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE, oldChangedReference, newChangedReference);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setChangedReference(IElementReference newChangedReference) {
		if (newChangedReference != changedReference) {
			NotificationChain msgs = null;
			if (changedReference != null)
				msgs = ((InternalEObject)changedReference).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MPatchPackage.INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE, null, msgs);
			if (newChangedReference != null)
				msgs = ((InternalEObject)newChangedReference).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MPatchPackage.INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE, null, msgs);
			msgs = basicSetChangedReference(newChangedReference, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE, newChangedReference, newChangedReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MPatchPackage.INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE:
				return basicSetChangedReference(null, msgs);
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
			case MPatchPackage.INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE:
				return getChangedReference();
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
			case MPatchPackage.INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE:
				setChangedReference((IElementReference)newValue);
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
			case MPatchPackage.INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE:
				setChangedReference((IElementReference)null);
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
			case MPatchPackage.INDEP_ADD_REM_REFERENCE_CHANGE__CHANGED_REFERENCE:
				return changedReference != null;
		}
		return super.eIsSet(featureID);
	}

} //IndepAddRemReferenceChangeImpl
