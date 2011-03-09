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
 * $Id: IndepMoveElementChangeImpl.java,v 1.1 2010/09/10 15:23:07 cbrun Exp $
 */
package org.eclipse.emf.compare.mpatch.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IndepMoveElementChange;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Indep Move Element Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.IndepMoveElementChangeImpl#getOldContainment <em>Old Containment</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.IndepMoveElementChangeImpl#getNewContainment <em>New Containment</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.IndepMoveElementChangeImpl#getOldParent <em>Old Parent</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.IndepMoveElementChangeImpl#getNewParent <em>New Parent</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IndepMoveElementChangeImpl extends IndepElementChangeImpl implements IndepMoveElementChange {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2010, 2011 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";

	/**
	 * The cached value of the '{@link #getOldContainment() <em>Old Containment</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOldContainment()
	 * @generated
	 * @ordered
	 */
	protected EReference oldContainment;

	/**
	 * The cached value of the '{@link #getNewContainment() <em>New Containment</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewContainment()
	 * @generated
	 * @ordered
	 */
	protected EReference newContainment;

	/**
	 * The cached value of the '{@link #getOldParent() <em>Old Parent</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOldParent()
	 * @generated
	 * @ordered
	 */
	protected IElementReference oldParent;

	/**
	 * The cached value of the '{@link #getNewParent() <em>New Parent</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewParent()
	 * @generated
	 * @ordered
	 */
	protected IElementReference newParent;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IndepMoveElementChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MPatchPackage.Literals.INDEP_MOVE_ELEMENT_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOldContainment() {
		if (oldContainment != null && oldContainment.eIsProxy()) {
			InternalEObject oldOldContainment = (InternalEObject)oldContainment;
			oldContainment = (EReference)eResolveProxy(oldOldContainment);
			if (oldContainment != oldOldContainment) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__OLD_CONTAINMENT, oldOldContainment, oldContainment));
			}
		}
		return oldContainment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference basicGetOldContainment() {
		return oldContainment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOldContainment(EReference newOldContainment) {
		EReference oldOldContainment = oldContainment;
		oldContainment = newOldContainment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__OLD_CONTAINMENT, oldOldContainment, oldContainment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNewContainment() {
		if (newContainment != null && newContainment.eIsProxy()) {
			InternalEObject oldNewContainment = (InternalEObject)newContainment;
			newContainment = (EReference)eResolveProxy(oldNewContainment);
			if (newContainment != oldNewContainment) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__NEW_CONTAINMENT, oldNewContainment, newContainment));
			}
		}
		return newContainment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference basicGetNewContainment() {
		return newContainment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewContainment(EReference newNewContainment) {
		EReference oldNewContainment = newContainment;
		newContainment = newNewContainment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__NEW_CONTAINMENT, oldNewContainment, newContainment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IElementReference getOldParent() {
		return oldParent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOldParent(IElementReference newOldParent, NotificationChain msgs) {
		IElementReference oldOldParent = oldParent;
		oldParent = newOldParent;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__OLD_PARENT, oldOldParent, newOldParent);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOldParent(IElementReference newOldParent) {
		if (newOldParent != oldParent) {
			NotificationChain msgs = null;
			if (oldParent != null)
				msgs = ((InternalEObject)oldParent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__OLD_PARENT, null, msgs);
			if (newOldParent != null)
				msgs = ((InternalEObject)newOldParent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__OLD_PARENT, null, msgs);
			msgs = basicSetOldParent(newOldParent, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__OLD_PARENT, newOldParent, newOldParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IElementReference getNewParent() {
		return newParent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetNewParent(IElementReference newNewParent, NotificationChain msgs) {
		IElementReference oldNewParent = newParent;
		newParent = newNewParent;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__NEW_PARENT, oldNewParent, newNewParent);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewParent(IElementReference newNewParent) {
		if (newNewParent != newParent) {
			NotificationChain msgs = null;
			if (newParent != null)
				msgs = ((InternalEObject)newParent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__NEW_PARENT, null, msgs);
			if (newNewParent != null)
				msgs = ((InternalEObject)newNewParent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__NEW_PARENT, null, msgs);
			msgs = basicSetNewParent(newNewParent, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__NEW_PARENT, newNewParent, newNewParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__OLD_PARENT:
				return basicSetOldParent(null, msgs);
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__NEW_PARENT:
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
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__OLD_CONTAINMENT:
				if (resolve) return getOldContainment();
				return basicGetOldContainment();
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__NEW_CONTAINMENT:
				if (resolve) return getNewContainment();
				return basicGetNewContainment();
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__OLD_PARENT:
				return getOldParent();
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__NEW_PARENT:
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
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__OLD_CONTAINMENT:
				setOldContainment((EReference)newValue);
				return;
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__NEW_CONTAINMENT:
				setNewContainment((EReference)newValue);
				return;
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__OLD_PARENT:
				setOldParent((IElementReference)newValue);
				return;
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__NEW_PARENT:
				setNewParent((IElementReference)newValue);
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
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__OLD_CONTAINMENT:
				setOldContainment((EReference)null);
				return;
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__NEW_CONTAINMENT:
				setNewContainment((EReference)null);
				return;
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__OLD_PARENT:
				setOldParent((IElementReference)null);
				return;
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__NEW_PARENT:
				setNewParent((IElementReference)null);
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
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__OLD_CONTAINMENT:
				return oldContainment != null;
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__NEW_CONTAINMENT:
				return newContainment != null;
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__OLD_PARENT:
				return oldParent != null;
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE__NEW_PARENT:
				return newParent != null;
		}
		return super.eIsSet(featureID);
	}

} //IndepMoveElementChangeImpl
