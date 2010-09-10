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
 * $Id: SubModelBindingImpl.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.compare.mpatch.binding.BindingPackage;
import org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.SubModelBinding;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sub Model Binding</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.SubModelBindingImpl#getModelDescriptor <em>Model Descriptor</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.SubModelBindingImpl#getSubModelReferences <em>Sub Model References</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.SubModelBindingImpl#getSelfElement <em>Self Element</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.SubModelBindingImpl#getSelfReference <em>Self Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SubModelBindingImpl extends ElementChangeBindingImpl implements SubModelBinding {
	/**
	 * The cached value of the '{@link #getModelDescriptor() <em>Model Descriptor</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelDescriptor()
	 * @generated
	 * @ordered
	 */
	protected IModelDescriptor modelDescriptor;

	/**
	 * The cached value of the '{@link #getSubModelReferences() <em>Sub Model References</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubModelReferences()
	 * @generated
	 * @ordered
	 */
	protected EList<ElementChangeBinding> subModelReferences;

	/**
	 * The cached value of the '{@link #getSelfElement() <em>Self Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelfElement()
	 * @generated
	 * @ordered
	 */
	protected EObject selfElement;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SubModelBindingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BindingPackage.Literals.SUB_MODEL_BINDING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IModelDescriptor getModelDescriptor() {
		if (modelDescriptor != null && modelDescriptor.eIsProxy()) {
			InternalEObject oldModelDescriptor = (InternalEObject)modelDescriptor;
			modelDescriptor = (IModelDescriptor)eResolveProxy(oldModelDescriptor);
			if (modelDescriptor != oldModelDescriptor) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BindingPackage.SUB_MODEL_BINDING__MODEL_DESCRIPTOR, oldModelDescriptor, modelDescriptor));
			}
		}
		return modelDescriptor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IModelDescriptor basicGetModelDescriptor() {
		return modelDescriptor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModelDescriptor(IModelDescriptor newModelDescriptor) {
		IModelDescriptor oldModelDescriptor = modelDescriptor;
		modelDescriptor = newModelDescriptor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BindingPackage.SUB_MODEL_BINDING__MODEL_DESCRIPTOR, oldModelDescriptor, modelDescriptor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ElementChangeBinding> getSubModelReferences() {
		if (subModelReferences == null) {
			subModelReferences = new EObjectContainmentEList<ElementChangeBinding>(ElementChangeBinding.class, this, BindingPackage.SUB_MODEL_BINDING__SUB_MODEL_REFERENCES);
		}
		return subModelReferences;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getSelfElement() {
		if (selfElement != null && selfElement.eIsProxy()) {
			InternalEObject oldSelfElement = (InternalEObject)selfElement;
			selfElement = eResolveProxy(oldSelfElement);
			if (selfElement != oldSelfElement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BindingPackage.SUB_MODEL_BINDING__SELF_ELEMENT, oldSelfElement, selfElement));
			}
		}
		return selfElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetSelfElement() {
		return selfElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSelfElement(EObject newSelfElement) {
		EObject oldSelfElement = selfElement;
		selfElement = newSelfElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BindingPackage.SUB_MODEL_BINDING__SELF_ELEMENT, oldSelfElement, selfElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IElementReference getSelfReference() {
		IElementReference selfReference = basicGetSelfReference();
		return selfReference != null && selfReference.eIsProxy() ? (IElementReference)eResolveProxy((InternalEObject)selfReference) : selfReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public IElementReference basicGetSelfReference() {
		if (getModelDescriptor() != null)
			return getModelDescriptor().getSelfReference();
		else return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case BindingPackage.SUB_MODEL_BINDING__SUB_MODEL_REFERENCES:
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
			case BindingPackage.SUB_MODEL_BINDING__MODEL_DESCRIPTOR:
				if (resolve) return getModelDescriptor();
				return basicGetModelDescriptor();
			case BindingPackage.SUB_MODEL_BINDING__SUB_MODEL_REFERENCES:
				return getSubModelReferences();
			case BindingPackage.SUB_MODEL_BINDING__SELF_ELEMENT:
				if (resolve) return getSelfElement();
				return basicGetSelfElement();
			case BindingPackage.SUB_MODEL_BINDING__SELF_REFERENCE:
				if (resolve) return getSelfReference();
				return basicGetSelfReference();
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
			case BindingPackage.SUB_MODEL_BINDING__MODEL_DESCRIPTOR:
				setModelDescriptor((IModelDescriptor)newValue);
				return;
			case BindingPackage.SUB_MODEL_BINDING__SUB_MODEL_REFERENCES:
				getSubModelReferences().clear();
				getSubModelReferences().addAll((Collection<? extends ElementChangeBinding>)newValue);
				return;
			case BindingPackage.SUB_MODEL_BINDING__SELF_ELEMENT:
				setSelfElement((EObject)newValue);
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
			case BindingPackage.SUB_MODEL_BINDING__MODEL_DESCRIPTOR:
				setModelDescriptor((IModelDescriptor)null);
				return;
			case BindingPackage.SUB_MODEL_BINDING__SUB_MODEL_REFERENCES:
				getSubModelReferences().clear();
				return;
			case BindingPackage.SUB_MODEL_BINDING__SELF_ELEMENT:
				setSelfElement((EObject)null);
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
			case BindingPackage.SUB_MODEL_BINDING__MODEL_DESCRIPTOR:
				return modelDescriptor != null;
			case BindingPackage.SUB_MODEL_BINDING__SUB_MODEL_REFERENCES:
				return subModelReferences != null && !subModelReferences.isEmpty();
			case BindingPackage.SUB_MODEL_BINDING__SELF_ELEMENT:
				return selfElement != null;
			case BindingPackage.SUB_MODEL_BINDING__SELF_REFERENCE:
				return basicGetSelfReference() != null;
		}
		return super.eIsSet(featureID);
	}

} //SubModelBindingImpl
