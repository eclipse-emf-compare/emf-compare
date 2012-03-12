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
 * $Id: MoveElementChangeBindingImpl.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.compare.mpatch.binding.BindingPackage;
import org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.MoveElementChangeBinding;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Move Element Change Binding</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.MoveElementChangeBindingImpl#getNewParent <em>New Parent</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MoveElementChangeBindingImpl extends ChangeBindingImpl implements MoveElementChangeBinding {
	/**
	 * The cached value of the '{@link #getNewParent() <em>New Parent</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewParent()
	 * @generated
	 * @ordered
	 */
	protected ElementChangeBinding newParent;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MoveElementChangeBindingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BindingPackage.Literals.MOVE_ELEMENT_CHANGE_BINDING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ElementChangeBinding getNewParent() {
		return newParent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetNewParent(ElementChangeBinding newNewParent, NotificationChain msgs) {
		ElementChangeBinding oldNewParent = newParent;
		newParent = newNewParent;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BindingPackage.MOVE_ELEMENT_CHANGE_BINDING__NEW_PARENT, oldNewParent, newNewParent);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewParent(ElementChangeBinding newNewParent) {
		if (newNewParent != newParent) {
			NotificationChain msgs = null;
			if (newParent != null)
				msgs = ((InternalEObject)newParent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BindingPackage.MOVE_ELEMENT_CHANGE_BINDING__NEW_PARENT, null, msgs);
			if (newNewParent != null)
				msgs = ((InternalEObject)newNewParent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BindingPackage.MOVE_ELEMENT_CHANGE_BINDING__NEW_PARENT, null, msgs);
			msgs = basicSetNewParent(newNewParent, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BindingPackage.MOVE_ELEMENT_CHANGE_BINDING__NEW_PARENT, newNewParent, newNewParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case BindingPackage.MOVE_ELEMENT_CHANGE_BINDING__NEW_PARENT:
				return basicSetNewParent(null, msgs);
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
			case BindingPackage.MOVE_ELEMENT_CHANGE_BINDING__NEW_PARENT:
				return getNewParent();
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
			case BindingPackage.MOVE_ELEMENT_CHANGE_BINDING__NEW_PARENT:
				setNewParent((ElementChangeBinding)newValue);
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
			case BindingPackage.MOVE_ELEMENT_CHANGE_BINDING__NEW_PARENT:
				setNewParent((ElementChangeBinding)null);
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
			case BindingPackage.MOVE_ELEMENT_CHANGE_BINDING__NEW_PARENT:
				return newParent != null;
		}
		return super.eIsSet(featureID);
	}

} //MoveElementChangeBindingImpl
