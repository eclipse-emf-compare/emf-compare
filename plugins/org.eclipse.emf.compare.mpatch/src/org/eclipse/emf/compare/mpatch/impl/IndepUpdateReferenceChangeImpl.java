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
 * $Id: IndepUpdateReferenceChangeImpl.java,v 1.1 2010/09/10 15:23:07 cbrun Exp $
 */
package org.eclipse.emf.compare.mpatch.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Indep Update Reference Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.IndepUpdateReferenceChangeImpl#getOldReference <em>Old Reference</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.IndepUpdateReferenceChangeImpl#getNewReference <em>New Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IndepUpdateReferenceChangeImpl extends IndepReferenceChangeImpl implements IndepUpdateReferenceChange {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2010, 2011 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";

	/**
	 * The cached value of the '{@link #getOldReference() <em>Old Reference</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOldReference()
	 * @generated
	 * @ordered
	 */
	protected IElementReference oldReference;

	/**
	 * The cached value of the '{@link #getNewReference() <em>New Reference</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewReference()
	 * @generated
	 * @ordered
	 */
	protected IElementReference newReference;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IndepUpdateReferenceChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MPatchPackage.Literals.INDEP_UPDATE_REFERENCE_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IElementReference getOldReference() {
		return oldReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOldReference(IElementReference newOldReference, NotificationChain msgs) {
		IElementReference oldOldReference = oldReference;
		oldReference = newOldReference;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE__OLD_REFERENCE, oldOldReference, newOldReference);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOldReference(IElementReference newOldReference) {
		if (newOldReference != oldReference) {
			NotificationChain msgs = null;
			if (oldReference != null)
				msgs = ((InternalEObject)oldReference).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE__OLD_REFERENCE, null, msgs);
			if (newOldReference != null)
				msgs = ((InternalEObject)newOldReference).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE__OLD_REFERENCE, null, msgs);
			msgs = basicSetOldReference(newOldReference, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE__OLD_REFERENCE, newOldReference, newOldReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IElementReference getNewReference() {
		return newReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetNewReference(IElementReference newNewReference, NotificationChain msgs) {
		IElementReference oldNewReference = newReference;
		newReference = newNewReference;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE__NEW_REFERENCE, oldNewReference, newNewReference);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewReference(IElementReference newNewReference) {
		if (newNewReference != newReference) {
			NotificationChain msgs = null;
			if (newReference != null)
				msgs = ((InternalEObject)newReference).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE__NEW_REFERENCE, null, msgs);
			if (newNewReference != null)
				msgs = ((InternalEObject)newNewReference).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE__NEW_REFERENCE, null, msgs);
			msgs = basicSetNewReference(newNewReference, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE__NEW_REFERENCE, newNewReference, newNewReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE__OLD_REFERENCE:
				return basicSetOldReference(null, msgs);
			case MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE__NEW_REFERENCE:
				return basicSetNewReference(null, msgs);
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
			case MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE__OLD_REFERENCE:
				return getOldReference();
			case MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE__NEW_REFERENCE:
				return getNewReference();
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
			case MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE__OLD_REFERENCE:
				setOldReference((IElementReference)newValue);
				return;
			case MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE__NEW_REFERENCE:
				setNewReference((IElementReference)newValue);
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
			case MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE__OLD_REFERENCE:
				setOldReference((IElementReference)null);
				return;
			case MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE__NEW_REFERENCE:
				setNewReference((IElementReference)null);
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
			case MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE__OLD_REFERENCE:
				return oldReference != null;
			case MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE__NEW_REFERENCE:
				return newReference != null;
		}
		return super.eIsSet(featureID);
	}

} //IndepUpdateReferenceChangeImpl
