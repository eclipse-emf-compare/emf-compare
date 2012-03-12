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
 * $Id: ChangeGroupImpl.java,v 1.1 2010/09/10 15:23:07 cbrun Exp $
 */
package org.eclipse.emf.compare.mpatch.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.ChangeGroup;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Change Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.ChangeGroupImpl#getSubChanges <em>Sub Changes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ChangeGroupImpl extends IndepChangeImpl implements ChangeGroup {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2010, 2011 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation"; //$NON-NLS-1$
	/**
	 * The cached value of the '{@link #getSubChanges() <em>Sub Changes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubChanges()
	 * @generated
	 * @ordered
	 */
	protected EList<IndepChange> subChanges;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChangeGroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MPatchPackage.Literals.CHANGE_GROUP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IndepChange> getSubChanges() {
		if (subChanges == null) {
			subChanges = new EObjectContainmentEList<IndepChange>(IndepChange.class, this, MPatchPackage.CHANGE_GROUP__SUB_CHANGES);
		}
		return subChanges;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MPatchPackage.CHANGE_GROUP__SUB_CHANGES:
				return ((InternalEList<?>)getSubChanges()).basicRemove(otherEnd, msgs);
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
			case MPatchPackage.CHANGE_GROUP__SUB_CHANGES:
				return getSubChanges();
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
			case MPatchPackage.CHANGE_GROUP__SUB_CHANGES:
				getSubChanges().clear();
				getSubChanges().addAll((Collection<? extends IndepChange>)newValue);
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
			case MPatchPackage.CHANGE_GROUP__SUB_CHANGES:
				getSubChanges().clear();
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
			case MPatchPackage.CHANGE_GROUP__SUB_CHANGES:
				return subChanges != null && !subChanges.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ChangeGroupImpl
