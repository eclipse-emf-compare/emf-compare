/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
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
import org.eclipse.emf.compare.match.metamodel.MatchPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Element</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.match.metamodel.impl.MatchElementImpl#getSimilarity <em>Similarity</em>}</li>
 * <li>{@link org.eclipse.emf.compare.match.metamodel.impl.MatchElementImpl#getSubMatchElements <em>Sub Match Elements</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
@SuppressWarnings("nls")
public abstract class MatchElementImpl extends EObjectImpl implements MatchElement {
	/**
	 * The default value of the '{@link #getSimilarity() <em>Similarity</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSimilarity()
	 * @generated
	 * @ordered
	 */
	protected static final double SIMILARITY_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getSimilarity() <em>Similarity</em>}' attribute. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #getSimilarity()
	 * @generated
	 * @ordered
	 */
	protected double similarity = SIMILARITY_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSubMatchElements() <em>Sub Match Elements</em>}' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSubMatchElements()
	 * @generated
	 * @ordered
	 */
	protected EList subMatchElements = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected MatchElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MatchPackage.Literals.MATCH_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getSimilarity() {
		return similarity;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSimilarity(double newSimilarity) {
		double oldSimilarity = similarity;
		similarity = newSimilarity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MatchPackage.MATCH_ELEMENT__SIMILARITY,
					oldSimilarity, similarity));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getSubMatchElements() {
		if (subMatchElements == null) {
			subMatchElements = new EObjectContainmentEList(MatchElement.class, this,
					MatchPackage.MATCH_ELEMENT__SUB_MATCH_ELEMENTS);
		}
		return subMatchElements;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MatchPackage.MATCH_ELEMENT__SUB_MATCH_ELEMENTS:
				return ((InternalEList)getSubMatchElements()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MatchPackage.MATCH_ELEMENT__SIMILARITY:
				return new Double(getSimilarity());
			case MatchPackage.MATCH_ELEMENT__SUB_MATCH_ELEMENTS:
				return getSubMatchElements();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MatchPackage.MATCH_ELEMENT__SIMILARITY:
				setSimilarity(((Double)newValue).doubleValue());
				return;
			case MatchPackage.MATCH_ELEMENT__SUB_MATCH_ELEMENTS:
				getSubMatchElements().clear();
				getSubMatchElements().addAll((Collection)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case MatchPackage.MATCH_ELEMENT__SIMILARITY:
				setSimilarity(SIMILARITY_EDEFAULT);
				return;
			case MatchPackage.MATCH_ELEMENT__SUB_MATCH_ELEMENTS:
				getSubMatchElements().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case MatchPackage.MATCH_ELEMENT__SIMILARITY:
				return similarity != SIMILARITY_EDEFAULT;
			case MatchPackage.MATCH_ELEMENT__SUB_MATCH_ELEMENTS:
				return subMatchElements != null && !subMatchElements.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (similarity: "); //$NON-NLS-1$
		result.append(similarity);
		result.append(')');
		return result.toString();
	}

} // MatchElementImpl
