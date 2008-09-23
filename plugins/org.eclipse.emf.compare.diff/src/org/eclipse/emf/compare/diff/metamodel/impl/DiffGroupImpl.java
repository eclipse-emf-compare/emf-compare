/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.metamodel.impl;

import java.util.Iterator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.EMFCompareDiffMessages;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.util.DiffAdapterFactory;
import org.eclipse.emf.compare.match.statistic.similarity.NameSimilarity;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Group</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffGroupImpl#getRightParent <em>Right Parent</em>}</li>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffGroupImpl#getSubchanges <em>Subchanges</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class DiffGroupImpl extends DiffElementImpl implements DiffGroup {
	/**
	 * The cached value of the '{@link #getRightParent() <em>Right Parent</em>}' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRightParent()
	 * @generated
	 * @ordered
	 */
	protected EObject rightParent;

	/**
	 * The default value of the '{@link #getSubchanges() <em>Subchanges</em>}' attribute.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #getSubchanges()
	 * @generated
	 * @ordered
	 */
	protected static final int SUBCHANGES_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSubchanges() <em>Subchanges</em>}' attribute.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #getSubchanges()
	 * @generated
	 * @ordered
	 */
	protected int subchanges = SUBCHANGES_EDEFAULT;

	/**
	 * This is true if the Subchanges attribute has been set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean subchangesESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected DiffGroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.DIFF_GROUP__RIGHT_PARENT:
				if (resolve)
					return getRightParent();
				return basicGetRightParent();
			case DiffPackage.DIFF_GROUP__SUBCHANGES:
				return new Integer(getSubchanges());
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DiffPackage.DIFF_GROUP__RIGHT_PARENT:
				return rightParent != null;
			case DiffPackage.DIFF_GROUP__SUBCHANGES:
				return isSetSubchanges();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DiffPackage.DIFF_GROUP__RIGHT_PARENT:
				setRightParent((EObject)newValue);
				return;
			case DiffPackage.DIFF_GROUP__SUBCHANGES:
				setSubchanges(((Integer)newValue).intValue());
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case DiffPackage.DIFF_GROUP__RIGHT_PARENT:
				setRightParent((EObject)null);
				return;
			case DiffPackage.DIFF_GROUP__SUBCHANGES:
				unsetSubchanges();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public int getSubchanges() {
		final Iterator<DiffElement> it = getSubDiffElements().iterator();
		int result = 0;
		while (it.hasNext()) {
			final DiffElement eObj = it.next();
			if (!DiffAdapterFactory.shouldBeHidden(eObj))
				if (eObj instanceof DiffGroup) {
					result += ((DiffGroup)eObj).getSubchanges();
				} else {
					result += 1;
				}

		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSubchanges() {
		return subchangesESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setSubchanges(int newSubchanges) {
		int oldSubchanges = subchanges;
		subchanges = newSubchanges;
		boolean oldSubchangesESet = subchangesESet;
		subchangesESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiffPackage.DIFF_GROUP__SUBCHANGES,
					oldSubchanges, subchanges, !oldSubchangesESet));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @generated NOT
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffElementImpl#toString()
	 */
	@Override
	public String toString() {
		if (rightParent == null)
			return super.toString();
		try {
			return EMFCompareDiffMessages
					.getString(
							"DiffGroupImpl.ToString", getSubchanges(), rightParent.eClass().getName(), NameSimilarity.findName(rightParent)); //$NON-NLS-1$
		} catch (final FactoryException e) {
			return EMFCompareDiffMessages.getString(
					"DiffGroupImpl.ToString", getSubchanges(), rightParent.eClass().getName()); //$NON-NLS-1$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSubchanges() {
		int oldSubchanges = subchanges;
		boolean oldSubchangesESet = subchangesESet;
		subchanges = SUBCHANGES_EDEFAULT;
		subchangesESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DiffPackage.DIFF_GROUP__SUBCHANGES,
					oldSubchanges, SUBCHANGES_EDEFAULT, oldSubchangesESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.DIFF_GROUP;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getRightParent() {
		if (rightParent != null && rightParent.eIsProxy()) {
			InternalEObject oldRightParent = (InternalEObject)rightParent;
			rightParent = eResolveProxy(oldRightParent);
			if (rightParent != oldRightParent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DiffPackage.DIFF_GROUP__RIGHT_PARENT, oldRightParent, rightParent));
			}
		}
		return rightParent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetRightParent() {
		return rightParent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setRightParent(EObject newRightParent) {
		EObject oldRightParent = rightParent;
		rightParent = newRightParent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiffPackage.DIFF_GROUP__RIGHT_PARENT,
					oldRightParent, rightParent));
	}

} // DiffGroupImpl
