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
 * $Id: UpdateReferenceChangeBindingImpl.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.compare.mpatch.binding.BindingPackage;
import org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.UpdateReferenceChangeBinding;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Update Reference Change Binding</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.UpdateReferenceChangeBindingImpl#getNewReference <em>New Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UpdateReferenceChangeBindingImpl extends ChangeBindingImpl implements UpdateReferenceChangeBinding {
	/**
	 * The cached value of the '{@link #getNewReference() <em>New Reference</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewReference()
	 * @generated
	 * @ordered
	 */
	protected ElementChangeBinding newReference;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UpdateReferenceChangeBindingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BindingPackage.Literals.UPDATE_REFERENCE_CHANGE_BINDING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ElementChangeBinding getNewReference() {
		return newReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetNewReference(ElementChangeBinding newNewReference, NotificationChain msgs) {
		ElementChangeBinding oldNewReference = newReference;
		newReference = newNewReference;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BindingPackage.UPDATE_REFERENCE_CHANGE_BINDING__NEW_REFERENCE, oldNewReference, newNewReference);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewReference(ElementChangeBinding newNewReference) {
		if (newNewReference != newReference) {
			NotificationChain msgs = null;
			if (newReference != null)
				msgs = ((InternalEObject)newReference).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BindingPackage.UPDATE_REFERENCE_CHANGE_BINDING__NEW_REFERENCE, null, msgs);
			if (newNewReference != null)
				msgs = ((InternalEObject)newNewReference).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BindingPackage.UPDATE_REFERENCE_CHANGE_BINDING__NEW_REFERENCE, null, msgs);
			msgs = basicSetNewReference(newNewReference, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BindingPackage.UPDATE_REFERENCE_CHANGE_BINDING__NEW_REFERENCE, newNewReference, newNewReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case BindingPackage.UPDATE_REFERENCE_CHANGE_BINDING__NEW_REFERENCE:
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
			case BindingPackage.UPDATE_REFERENCE_CHANGE_BINDING__NEW_REFERENCE:
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
			case BindingPackage.UPDATE_REFERENCE_CHANGE_BINDING__NEW_REFERENCE:
				setNewReference((ElementChangeBinding)newValue);
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
			case BindingPackage.UPDATE_REFERENCE_CHANGE_BINDING__NEW_REFERENCE:
				setNewReference((ElementChangeBinding)null);
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
			case BindingPackage.UPDATE_REFERENCE_CHANGE_BINDING__NEW_REFERENCE:
				return newReference != null;
		}
		return super.eIsSet(featureID);
	}

} //UpdateReferenceChangeBindingImpl
