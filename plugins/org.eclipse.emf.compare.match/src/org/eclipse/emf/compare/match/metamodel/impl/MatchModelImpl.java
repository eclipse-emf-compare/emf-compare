/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
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
import org.eclipse.emf.compare.match.metamodel.UnmatchElement;
import org.eclipse.emf.compare.match.metamodel.UnmatchElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Model</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.impl.MatchModelImpl#getMatchedElements <em>Matched Elements</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.impl.MatchModelImpl#getUnmatchedElements <em>Unmatched Elements</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.impl.MatchModelImpl#getLeftRoots <em>Left Roots</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.impl.MatchModelImpl#getRightRoots <em>Right Roots</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.match.metamodel.impl.MatchModelImpl#getAncestorRoots <em>Ancestor Roots</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MatchModelImpl extends EObjectImpl implements MatchModel {
	/**
	 * The cached value of the '{@link #getMatchedElements() <em>Matched Elements</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getMatchedElements()
	 * @generated
	 * @ordered
	 */
	protected EList<MatchElement> matchedElements;

	/**
	 * The cached value of the '{@link #getUnmatchedElements() <em>Unmatched Elements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnmatchedElements()
	 * @generated
	 * @ordered
	 */
	protected EList<UnmatchElement> unmatchedElements;

	/**
	 * The cached value of the '{@link #getLeftRoots() <em>Left Roots</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeftRoots()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> leftRoots;

	/**
	 * The cached value of the '{@link #getRightRoots() <em>Right Roots</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRightRoots()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> rightRoots;

	/**
	 * The cached value of the '{@link #getAncestorRoots() <em>Ancestor Roots</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAncestorRoots()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> ancestorRoots;

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
			case MatchPackage.MATCH_MODEL__MATCHED_ELEMENTS:
				return getMatchedElements();
			case MatchPackage.MATCH_MODEL__UNMATCHED_ELEMENTS:
				return getUnmatchedElements();
			case MatchPackage.MATCH_MODEL__LEFT_ROOTS:
				return getLeftRoots();
			case MatchPackage.MATCH_MODEL__RIGHT_ROOTS:
				return getRightRoots();
			case MatchPackage.MATCH_MODEL__ANCESTOR_ROOTS:
				return getAncestorRoots();
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
			case MatchPackage.MATCH_MODEL__UNMATCHED_ELEMENTS:
				return ((InternalEList<?>)getUnmatchedElements()).basicRemove(otherEnd, msgs);
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
			case MatchPackage.MATCH_MODEL__MATCHED_ELEMENTS:
				return matchedElements != null && !matchedElements.isEmpty();
			case MatchPackage.MATCH_MODEL__UNMATCHED_ELEMENTS:
				return unmatchedElements != null && !unmatchedElements.isEmpty();
			case MatchPackage.MATCH_MODEL__LEFT_ROOTS:
				return leftRoots != null && !leftRoots.isEmpty();
			case MatchPackage.MATCH_MODEL__RIGHT_ROOTS:
				return rightRoots != null && !rightRoots.isEmpty();
			case MatchPackage.MATCH_MODEL__ANCESTOR_ROOTS:
				return ancestorRoots != null && !ancestorRoots.isEmpty();
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
			case MatchPackage.MATCH_MODEL__MATCHED_ELEMENTS:
				getMatchedElements().clear();
				getMatchedElements().addAll((Collection<? extends MatchElement>)newValue);
				return;
			case MatchPackage.MATCH_MODEL__UNMATCHED_ELEMENTS:
				getUnmatchedElements().clear();
				getUnmatchedElements().addAll((Collection<? extends UnmatchElement>)newValue);
				return;
			case MatchPackage.MATCH_MODEL__LEFT_ROOTS:
				getLeftRoots().clear();
				getLeftRoots().addAll((Collection<? extends EObject>)newValue);
				return;
			case MatchPackage.MATCH_MODEL__RIGHT_ROOTS:
				getRightRoots().clear();
				getRightRoots().addAll((Collection<? extends EObject>)newValue);
				return;
			case MatchPackage.MATCH_MODEL__ANCESTOR_ROOTS:
				getAncestorRoots().clear();
				getAncestorRoots().addAll((Collection<? extends EObject>)newValue);
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
			case MatchPackage.MATCH_MODEL__MATCHED_ELEMENTS:
				getMatchedElements().clear();
				return;
			case MatchPackage.MATCH_MODEL__UNMATCHED_ELEMENTS:
				getUnmatchedElements().clear();
				return;
			case MatchPackage.MATCH_MODEL__LEFT_ROOTS:
				getLeftRoots().clear();
				return;
			case MatchPackage.MATCH_MODEL__RIGHT_ROOTS:
				getRightRoots().clear();
				return;
			case MatchPackage.MATCH_MODEL__ANCESTOR_ROOTS:
				getAncestorRoots().clear();
				return;
		}
		super.eUnset(featureID);
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<UnmatchElement> getUnmatchedElements() {
		if (unmatchedElements == null) {
			unmatchedElements = new EObjectContainmentEList<UnmatchElement>(UnmatchElement.class, this,
					MatchPackage.MATCH_MODEL__UNMATCHED_ELEMENTS);
		}
		return unmatchedElements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getLeftRoots() {
		if (leftRoots == null) {
			leftRoots = new EObjectResolvingEList<EObject>(EObject.class, this,
					MatchPackage.MATCH_MODEL__LEFT_ROOTS);
		}
		return leftRoots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getRightRoots() {
		if (rightRoots == null) {
			rightRoots = new EObjectResolvingEList<EObject>(EObject.class, this,
					MatchPackage.MATCH_MODEL__RIGHT_ROOTS);
		}
		return rightRoots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getAncestorRoots() {
		if (ancestorRoots == null) {
			ancestorRoots = new EObjectResolvingEList<EObject>(EObject.class, this,
					MatchPackage.MATCH_MODEL__ANCESTOR_ROOTS);
		}
		return ancestorRoots;
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
