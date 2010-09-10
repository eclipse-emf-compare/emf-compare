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
 * $Id: AddReferenceChangeBindingImpl.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.binding.AddReferenceChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.BindingPackage;
import org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Add Reference Change Binding</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.AddReferenceChangeBindingImpl#getChangedReference <em>Changed Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AddReferenceChangeBindingImpl extends ChangeBindingImpl implements AddReferenceChangeBinding {
	/**
	 * The cached value of the '{@link #getChangedReference() <em>Changed Reference</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangedReference()
	 * @generated
	 * @ordered
	 */
	protected EList<ElementChangeBinding> changedReference;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AddReferenceChangeBindingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BindingPackage.Literals.ADD_REFERENCE_CHANGE_BINDING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ElementChangeBinding> getChangedReference() {
		if (changedReference == null) {
			changedReference = new EObjectContainmentEList<ElementChangeBinding>(ElementChangeBinding.class, this, BindingPackage.ADD_REFERENCE_CHANGE_BINDING__CHANGED_REFERENCE);
		}
		return changedReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case BindingPackage.ADD_REFERENCE_CHANGE_BINDING__CHANGED_REFERENCE:
				return ((InternalEList<?>)getChangedReference()).basicRemove(otherEnd, msgs);
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
			case BindingPackage.ADD_REFERENCE_CHANGE_BINDING__CHANGED_REFERENCE:
				return getChangedReference();
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
			case BindingPackage.ADD_REFERENCE_CHANGE_BINDING__CHANGED_REFERENCE:
				getChangedReference().clear();
				getChangedReference().addAll((Collection<? extends ElementChangeBinding>)newValue);
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
			case BindingPackage.ADD_REFERENCE_CHANGE_BINDING__CHANGED_REFERENCE:
				getChangedReference().clear();
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
			case BindingPackage.ADD_REFERENCE_CHANGE_BINDING__CHANGED_REFERENCE:
				return changedReference != null && !changedReference.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //AddReferenceChangeBindingImpl
