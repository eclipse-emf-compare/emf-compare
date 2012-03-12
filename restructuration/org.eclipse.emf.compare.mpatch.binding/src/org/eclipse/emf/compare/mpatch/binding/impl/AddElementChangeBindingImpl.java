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
 * $Id: AddElementChangeBindingImpl.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.binding.AddElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.BindingPackage;
import org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.SubModelBinding;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Add Element Change Binding</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.AddElementChangeBindingImpl#getSubModelReferences <em>Sub Model References</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AddElementChangeBindingImpl extends ChangeBindingImpl implements AddElementChangeBinding {
	/**
	 * The cached value of the '{@link #getSubModelReferences() <em>Sub Model References</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubModelReferences()
	 * @generated
	 * @ordered
	 */
	protected EList<SubModelBinding> subModelReferences;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected AddElementChangeBindingImpl() {
		super();
		
		// in case the submodelreferences change, we need to notify that the corresponding elements have changed, too!
		eAdapters().add(new AdapterImpl(){
			@Override
			public void notifyChanged(Notification msg) {
				final int featureID = msg.getFeatureID(AddElementChangeBinding.class);
				if (featureID == BindingPackage.ADD_ELEMENT_CHANGE_BINDING__SUB_MODEL_REFERENCES && eNotificationRequired())
					//          ENotificationImpl(InternalEObject notifier, int eventType, int featureID, Object oldValue, Object newValue)
					eNotify(new ENotificationImpl(AddElementChangeBindingImpl.this, msg.getEventType(), 
							BindingPackage.ADD_ELEMENT_CHANGE_BINDING__CORRESPONDING_ELEMENTS, null, 
							getCorrespondingElements()));
			}
		});
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BindingPackage.Literals.ADD_ELEMENT_CHANGE_BINDING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SubModelBinding> getSubModelReferences() {
		if (subModelReferences == null) {
			subModelReferences = new EObjectContainmentEList<SubModelBinding>(SubModelBinding.class, this, BindingPackage.ADD_ELEMENT_CHANGE_BINDING__SUB_MODEL_REFERENCES);
		}
		return subModelReferences;
	}

	/*
	 * The corresponding elements are actually the sub model bindings!
	 * So we return them as an unmodifiable list.
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.compare.mpatch.binding.impl.ChangeBindingImpl#getCorrespondingElements()
	 */
	@Override
	public EList<ElementChangeBinding> getCorrespondingElements() {
		/*
		 * I'm not sure whether the resulting EList needs to implement Setting.
		 * Because BasicEList is not compatible with the cross referencer!!!
		 * So if we get a classcastexception using the cross referencer, for instance, we might need to use EObjectEList instead!
		 */
		return new BasicEList.UnmodifiableEList<ElementChangeBinding>(getSubModelReferences().size(), getSubModelReferences().toArray());
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case BindingPackage.ADD_ELEMENT_CHANGE_BINDING__SUB_MODEL_REFERENCES:
				return ((InternalEList<?>)getSubModelReferences()).basicRemove(otherEnd, msgs);
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
			case BindingPackage.ADD_ELEMENT_CHANGE_BINDING__SUB_MODEL_REFERENCES:
				return getSubModelReferences();
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
			case BindingPackage.ADD_ELEMENT_CHANGE_BINDING__SUB_MODEL_REFERENCES:
				getSubModelReferences().clear();
				getSubModelReferences().addAll((Collection<? extends SubModelBinding>)newValue);
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
			case BindingPackage.ADD_ELEMENT_CHANGE_BINDING__SUB_MODEL_REFERENCES:
				getSubModelReferences().clear();
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
			case BindingPackage.ADD_ELEMENT_CHANGE_BINDING__SUB_MODEL_REFERENCES:
				return subModelReferences != null && !subModelReferences.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //AddElementChangeBindingImpl
