/*******************************************************************************
 * Copyright (c) 2009, 2012 Obeo.
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

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.metamodel.MatchPackage;
import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;
import org.eclipse.emf.compare.match.metamodel.UnmatchModel;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Resource Set</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.impl.MatchResourceSetImpl#getMatchModels <em>Match Models</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.impl.MatchResourceSetImpl#getUnmatchedModels <em>Unmatched Models</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MatchResourceSetImpl extends EObjectImpl implements MatchResourceSet {
	/**
	 * The cached value of the '{@link #getMatchModels() <em>Match Models</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getMatchModels()
	 * @generated
	 * @ordered
	 */
	protected EList<MatchModel> matchModels;

	/**
	 * The cached value of the '{@link #getUnmatchedModels() <em>Unmatched Models</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnmatchedModels()
	 * @generated
	 * @ordered
	 */
	protected EList<UnmatchModel> unmatchedModels;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected MatchResourceSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MatchPackage.Literals.MATCH_RESOURCE_SET;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MatchModel> getMatchModels() {
		if (matchModels == null) {
			matchModels = new EObjectContainmentEList<MatchModel>(MatchModel.class, this,
					MatchPackage.MATCH_RESOURCE_SET__MATCH_MODELS);
		}
		return matchModels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<UnmatchModel> getUnmatchedModels() {
		if (unmatchedModels == null) {
			unmatchedModels = new EObjectContainmentEList<UnmatchModel>(UnmatchModel.class, this,
					MatchPackage.MATCH_RESOURCE_SET__UNMATCHED_MODELS);
		}
		return unmatchedModels;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MatchPackage.MATCH_RESOURCE_SET__MATCH_MODELS:
				return ((InternalEList<?>)getMatchModels()).basicRemove(otherEnd, msgs);
			case MatchPackage.MATCH_RESOURCE_SET__UNMATCHED_MODELS:
				return ((InternalEList<?>)getUnmatchedModels()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MatchPackage.MATCH_RESOURCE_SET__MATCH_MODELS:
				return getMatchModels();
			case MatchPackage.MATCH_RESOURCE_SET__UNMATCHED_MODELS:
				return getUnmatchedModels();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MatchPackage.MATCH_RESOURCE_SET__MATCH_MODELS:
				getMatchModels().clear();
				getMatchModels().addAll((Collection<? extends MatchModel>)newValue);
				return;
			case MatchPackage.MATCH_RESOURCE_SET__UNMATCHED_MODELS:
				getUnmatchedModels().clear();
				getUnmatchedModels().addAll((Collection<? extends UnmatchModel>)newValue);
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
			case MatchPackage.MATCH_RESOURCE_SET__MATCH_MODELS:
				getMatchModels().clear();
				return;
			case MatchPackage.MATCH_RESOURCE_SET__UNMATCHED_MODELS:
				getUnmatchedModels().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case MatchPackage.MATCH_RESOURCE_SET__MATCH_MODELS:
				return matchModels != null && !matchModels.isEmpty();
			case MatchPackage.MATCH_RESOURCE_SET__UNMATCHED_MODELS:
				return unmatchedModels != null && !unmatchedModels.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // MatchResourceSetImpl
