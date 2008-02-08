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
package org.eclipse.emf.compare.match.metamodel.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.match.metamodel.MatchElement;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.metamodel.MatchPackage;
import org.eclipse.emf.compare.match.metamodel.UnMatchElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Model</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.impl.MatchModelImpl#getLeftModel <em>Left Model</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.impl.MatchModelImpl#getRightModel <em>Right Model</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.impl.MatchModelImpl#getOriginModel <em>Origin Model</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.impl.MatchModelImpl#getMatchedElements <em>Matched Elements</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.impl.MatchModelImpl#getUnMatchedElements <em>Un Matched Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MatchModelImpl extends EObjectImpl implements MatchModel {
	/**
	 * The default value of the '{@link #getLeftModel() <em>Left Model</em>}' attribute.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #getLeftModel()
	 * @generated
	 * @ordered
	 */
	protected static final String LEFT_MODEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLeftModel() <em>Left Model</em>}' attribute.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #getLeftModel()
	 * @generated
	 * @ordered
	 */
	protected String leftModel = LEFT_MODEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getRightModel() <em>Right Model</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRightModel()
	 * @generated
	 * @ordered
	 */
	protected static final String RIGHT_MODEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRightModel() <em>Right Model</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRightModel()
	 * @generated
	 * @ordered
	 */
	protected String rightModel = RIGHT_MODEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getOriginModel() <em>Origin Model</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOriginModel()
	 * @generated
	 * @ordered
	 */
	protected static final String ORIGIN_MODEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOriginModel() <em>Origin Model</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOriginModel()
	 * @generated
	 * @ordered
	 */
	protected String originModel = ORIGIN_MODEL_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMatchedElements() <em>Matched Elements</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getMatchedElements()
	 * @generated
	 * @ordered
	 */
	protected EList<MatchElement> matchedElements;

	/**
	 * The cached value of the '{@link #getUnMatchedElements() <em>Un Matched Elements</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getUnMatchedElements()
	 * @generated
	 * @ordered
	 */
	protected EList<UnMatchElement> unMatchedElements;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected MatchModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MatchPackage.MATCH_MODEL__LEFT_MODEL:
				return getLeftModel();
			case MatchPackage.MATCH_MODEL__RIGHT_MODEL:
				return getRightModel();
			case MatchPackage.MATCH_MODEL__ORIGIN_MODEL:
				return getOriginModel();
			case MatchPackage.MATCH_MODEL__MATCHED_ELEMENTS:
				return getMatchedElements();
			case MatchPackage.MATCH_MODEL__UN_MATCHED_ELEMENTS:
				return getUnMatchedElements();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MatchPackage.MATCH_MODEL__MATCHED_ELEMENTS:
				return ((InternalEList<?>)getMatchedElements()).basicRemove(otherEnd, msgs);
			case MatchPackage.MATCH_MODEL__UN_MATCHED_ELEMENTS:
				return ((InternalEList<?>)getUnMatchedElements()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case MatchPackage.MATCH_MODEL__LEFT_MODEL:
				return LEFT_MODEL_EDEFAULT == null ? leftModel != null : !LEFT_MODEL_EDEFAULT
						.equals(leftModel);
			case MatchPackage.MATCH_MODEL__RIGHT_MODEL:
				return RIGHT_MODEL_EDEFAULT == null ? rightModel != null : !RIGHT_MODEL_EDEFAULT
						.equals(rightModel);
			case MatchPackage.MATCH_MODEL__ORIGIN_MODEL:
				return ORIGIN_MODEL_EDEFAULT == null ? originModel != null : !ORIGIN_MODEL_EDEFAULT
						.equals(originModel);
			case MatchPackage.MATCH_MODEL__MATCHED_ELEMENTS:
				return matchedElements != null && !matchedElements.isEmpty();
			case MatchPackage.MATCH_MODEL__UN_MATCHED_ELEMENTS:
				return unMatchedElements != null && !unMatchedElements.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MatchPackage.MATCH_MODEL__LEFT_MODEL:
				setLeftModel((String)newValue);
				return;
			case MatchPackage.MATCH_MODEL__RIGHT_MODEL:
				setRightModel((String)newValue);
				return;
			case MatchPackage.MATCH_MODEL__ORIGIN_MODEL:
				setOriginModel((String)newValue);
				return;
			case MatchPackage.MATCH_MODEL__MATCHED_ELEMENTS:
				getMatchedElements().clear();
				getMatchedElements().addAll((Collection<? extends MatchElement>)newValue);
				return;
			case MatchPackage.MATCH_MODEL__UN_MATCHED_ELEMENTS:
				getUnMatchedElements().clear();
				getUnMatchedElements().addAll((Collection<? extends UnMatchElement>)newValue);
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
			case MatchPackage.MATCH_MODEL__LEFT_MODEL:
				setLeftModel(LEFT_MODEL_EDEFAULT);
				return;
			case MatchPackage.MATCH_MODEL__RIGHT_MODEL:
				setRightModel(RIGHT_MODEL_EDEFAULT);
				return;
			case MatchPackage.MATCH_MODEL__ORIGIN_MODEL:
				setOriginModel(ORIGIN_MODEL_EDEFAULT);
				return;
			case MatchPackage.MATCH_MODEL__MATCHED_ELEMENTS:
				getMatchedElements().clear();
				return;
			case MatchPackage.MATCH_MODEL__UN_MATCHED_ELEMENTS:
				getUnMatchedElements().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getLeftModel() {
		return leftModel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MatchElement> getMatchedElements() {
		if (matchedElements == null) {
			matchedElements = new EObjectContainmentEList<MatchElement>(MatchElement.class, this,
					MatchPackage.MATCH_MODEL__MATCHED_ELEMENTS);
		}
		return matchedElements;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getOriginModel() {
		return originModel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getRightModel() {
		return rightModel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<UnMatchElement> getUnMatchedElements() {
		if (unMatchedElements == null) {
			unMatchedElements = new EObjectContainmentEList<UnMatchElement>(UnMatchElement.class, this,
					MatchPackage.MATCH_MODEL__UN_MATCHED_ELEMENTS);
		}
		return unMatchedElements;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setLeftModel(String newLeftModel) {
		String oldLeftModel = leftModel;
		leftModel = newLeftModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MatchPackage.MATCH_MODEL__LEFT_MODEL,
					oldLeftModel, leftModel));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginModel(String newOriginModel) {
		String oldOriginModel = originModel;
		originModel = newOriginModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MatchPackage.MATCH_MODEL__ORIGIN_MODEL,
					oldOriginModel, originModel));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setRightModel(String newRightModel) {
		String oldRightModel = rightModel;
		rightModel = newRightModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MatchPackage.MATCH_MODEL__RIGHT_MODEL,
					oldRightModel, rightModel));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (leftModel: "); //$NON-NLS-1$
		result.append(leftModel);
		result.append(", rightModel: "); //$NON-NLS-1$
		result.append(rightModel);
		result.append(", originModel: "); //$NON-NLS-1$
		result.append(originModel);
		result.append(')');
		return result.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MatchPackage.Literals.MATCH_MODEL;
	}

} // MatchModelImpl
