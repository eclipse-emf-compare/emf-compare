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
 * $Id: MPatchModelImpl.java,v 1.1 2010/09/10 15:23:07 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Indep Diff Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.MPatchModelImpl#getChanges <em>Changes</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.MPatchModelImpl#getOldModel <em>Old Model</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.MPatchModelImpl#getNewModel <em>New Model</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.MPatchModelImpl#getEmfdiff <em>Emfdiff</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MPatchModelImpl extends EObjectImpl implements MPatchModel {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2010, 2011 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";

	/**
	 * The cached value of the '{@link #getChanges() <em>Changes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChanges()
	 * @generated
	 * @ordered
	 */
	protected EList<IndepChange> changes;

	/**
	 * The default value of the '{@link #getOldModel() <em>Old Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOldModel()
	 * @generated
	 * @ordered
	 */
	protected static final String OLD_MODEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOldModel() <em>Old Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOldModel()
	 * @generated
	 * @ordered
	 */
	protected String oldModel = OLD_MODEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getNewModel() <em>New Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewModel()
	 * @generated
	 * @ordered
	 */
	protected static final String NEW_MODEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNewModel() <em>New Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewModel()
	 * @generated
	 * @ordered
	 */
	protected String newModel = NEW_MODEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getEmfdiff() <em>Emfdiff</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEmfdiff()
	 * @generated
	 * @ordered
	 */
	protected static final String EMFDIFF_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEmfdiff() <em>Emfdiff</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEmfdiff()
	 * @generated
	 * @ordered
	 */
	protected String emfdiff = EMFDIFF_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MPatchModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MPatchPackage.Literals.MPATCH_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IndepChange> getChanges() {
		if (changes == null) {
			changes = new EObjectContainmentEList<IndepChange>(IndepChange.class, this, MPatchPackage.MPATCH_MODEL__CHANGES);
		}
		return changes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOldModel() {
		return oldModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOldModel(String newOldModel) {
		String oldOldModel = oldModel;
		oldModel = newOldModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MPatchPackage.MPATCH_MODEL__OLD_MODEL, oldOldModel, oldModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNewModel() {
		return newModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewModel(String newNewModel) {
		String oldNewModel = newModel;
		newModel = newNewModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MPatchPackage.MPATCH_MODEL__NEW_MODEL, oldNewModel, newModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getEmfdiff() {
		return emfdiff;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEmfdiff(String newEmfdiff) {
		String oldEmfdiff = emfdiff;
		emfdiff = newEmfdiff;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MPatchPackage.MPATCH_MODEL__EMFDIFF, oldEmfdiff, emfdiff));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MPatchPackage.MPATCH_MODEL__CHANGES:
				return ((InternalEList<?>)getChanges()).basicRemove(otherEnd, msgs);
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
			case MPatchPackage.MPATCH_MODEL__CHANGES:
				return getChanges();
			case MPatchPackage.MPATCH_MODEL__OLD_MODEL:
				return getOldModel();
			case MPatchPackage.MPATCH_MODEL__NEW_MODEL:
				return getNewModel();
			case MPatchPackage.MPATCH_MODEL__EMFDIFF:
				return getEmfdiff();
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
			case MPatchPackage.MPATCH_MODEL__CHANGES:
				getChanges().clear();
				getChanges().addAll((Collection<? extends IndepChange>)newValue);
				return;
			case MPatchPackage.MPATCH_MODEL__OLD_MODEL:
				setOldModel((String)newValue);
				return;
			case MPatchPackage.MPATCH_MODEL__NEW_MODEL:
				setNewModel((String)newValue);
				return;
			case MPatchPackage.MPATCH_MODEL__EMFDIFF:
				setEmfdiff((String)newValue);
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
			case MPatchPackage.MPATCH_MODEL__CHANGES:
				getChanges().clear();
				return;
			case MPatchPackage.MPATCH_MODEL__OLD_MODEL:
				setOldModel(OLD_MODEL_EDEFAULT);
				return;
			case MPatchPackage.MPATCH_MODEL__NEW_MODEL:
				setNewModel(NEW_MODEL_EDEFAULT);
				return;
			case MPatchPackage.MPATCH_MODEL__EMFDIFF:
				setEmfdiff(EMFDIFF_EDEFAULT);
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
			case MPatchPackage.MPATCH_MODEL__CHANGES:
				return changes != null && !changes.isEmpty();
			case MPatchPackage.MPATCH_MODEL__OLD_MODEL:
				return OLD_MODEL_EDEFAULT == null ? oldModel != null : !OLD_MODEL_EDEFAULT.equals(oldModel);
			case MPatchPackage.MPATCH_MODEL__NEW_MODEL:
				return NEW_MODEL_EDEFAULT == null ? newModel != null : !NEW_MODEL_EDEFAULT.equals(newModel);
			case MPatchPackage.MPATCH_MODEL__EMFDIFF:
				return EMFDIFF_EDEFAULT == null ? emfdiff != null : !EMFDIFF_EDEFAULT.equals(emfdiff);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (oldModel: "); //$NON-NLS-1$
		result.append(oldModel);
		result.append(", newModel: "); //$NON-NLS-1$
		result.append(newModel);
		result.append(", emfdiff: "); //$NON-NLS-1$
		result.append(emfdiff);
		result.append(')');
		return result.toString();
	}

} //MPatchModelImpl
