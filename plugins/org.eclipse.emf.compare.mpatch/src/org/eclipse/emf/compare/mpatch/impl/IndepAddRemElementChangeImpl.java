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
 * $Id: IndepAddRemElementChangeImpl.java,v 1.1 2010/09/10 15:23:07 cbrun Exp $
 */
package org.eclipse.emf.compare.mpatch.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.compare.mpatch.IndepAddRemElementChange;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Indep Add Rem Element Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.IndepAddRemElementChangeImpl#getSubModel <em>Sub Model</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.IndepAddRemElementChangeImpl#getContainment <em>Containment</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.IndepAddRemElementChangeImpl#getSubModelReference <em>Sub Model Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class IndepAddRemElementChangeImpl extends IndepElementChangeImpl implements IndepAddRemElementChange {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2010 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";

	/**
	 * The cached value of the '{@link #getSubModel() <em>Sub Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubModel()
	 * @generated
	 * @ordered
	 */
	protected IModelDescriptor subModel;

	/**
	 * The cached value of the '{@link #getContainment() <em>Containment</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainment()
	 * @generated
	 * @ordered
	 */
	protected EReference containment;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IndepAddRemElementChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MPatchPackage.Literals.INDEP_ADD_REM_ELEMENT_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IModelDescriptor getSubModel() {
		return subModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSubModel(IModelDescriptor newSubModel, NotificationChain msgs) {
		IModelDescriptor oldSubModel = subModel;
		subModel = newSubModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL, oldSubModel, newSubModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSubModel(IModelDescriptor newSubModel) {
		if (newSubModel != subModel) {
			NotificationChain msgs = null;
			if (subModel != null)
				msgs = ((InternalEObject)subModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MPatchPackage.INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL, null, msgs);
			if (newSubModel != null)
				msgs = ((InternalEObject)newSubModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MPatchPackage.INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL, null, msgs);
			msgs = basicSetSubModel(newSubModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL, newSubModel, newSubModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getContainment() {
		if (containment != null && containment.eIsProxy()) {
			InternalEObject oldContainment = (InternalEObject)containment;
			containment = (EReference)eResolveProxy(oldContainment);
			if (containment != oldContainment) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MPatchPackage.INDEP_ADD_REM_ELEMENT_CHANGE__CONTAINMENT, oldContainment, containment));
			}
		}
		return containment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference basicGetContainment() {
		return containment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContainment(EReference newContainment) {
		EReference oldContainment = containment;
		containment = newContainment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_ADD_REM_ELEMENT_CHANGE__CONTAINMENT, oldContainment, containment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public IElementReference getSubModelReference() {
		final IModelDescriptor modelDescriptor = getSubModel();
		if (modelDescriptor != null) {
			return getSubModel().getSelfReference();
		} else {
			return null;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSubModelReference(IElementReference newSubModelReference, NotificationChain msgs) {
		// TODO: implement this method to set the contained 'Sub Model Reference' containment reference
		// -> this method is automatically invoked to keep the containment relationship in synch
		// -> do not modify other features
		// -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MPatchPackage.INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL:
				return basicSetSubModel(null, msgs);
			case MPatchPackage.INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL_REFERENCE:
				return basicSetSubModelReference(null, msgs);
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
			case MPatchPackage.INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL:
				return getSubModel();
			case MPatchPackage.INDEP_ADD_REM_ELEMENT_CHANGE__CONTAINMENT:
				if (resolve) return getContainment();
				return basicGetContainment();
			case MPatchPackage.INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL_REFERENCE:
				return getSubModelReference();
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
			case MPatchPackage.INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL:
				setSubModel((IModelDescriptor)newValue);
				return;
			case MPatchPackage.INDEP_ADD_REM_ELEMENT_CHANGE__CONTAINMENT:
				setContainment((EReference)newValue);
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
			case MPatchPackage.INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL:
				setSubModel((IModelDescriptor)null);
				return;
			case MPatchPackage.INDEP_ADD_REM_ELEMENT_CHANGE__CONTAINMENT:
				setContainment((EReference)null);
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
			case MPatchPackage.INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL:
				return subModel != null;
			case MPatchPackage.INDEP_ADD_REM_ELEMENT_CHANGE__CONTAINMENT:
				return containment != null;
			case MPatchPackage.INDEP_ADD_REM_ELEMENT_CHANGE__SUB_MODEL_REFERENCE:
				return getSubModelReference() != null;
		}
		return super.eIsSet(featureID);
	}

} //IndepAddRemElementChangeImpl
