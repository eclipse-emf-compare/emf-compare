/*******************************************************************************
 * Copyright (c) 2009, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.metamodel.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.metamodel.ResourceDiff;
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
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffResourceSetImpl#getDiffModels <em>Diff Models</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffResourceSetImpl#getResourceDiffs <em>Resource Diffs</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiffResourceSetImpl extends EObjectImpl implements DiffResourceSet {
	/**
	 * The cached value of the '{@link #getDiffModels() <em>Diff Models</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDiffModels()
	 * @generated
	 * @ordered
	 */
	protected EList<DiffModel> diffModels;

	/**
	 * The cached value of the '{@link #getResourceDiffs() <em>Resource Diffs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResourceDiffs()
	 * @generated
	 * @ordered
	 */
	protected EList<ResourceDiff> resourceDiffs;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected DiffResourceSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffPackage.Literals.DIFF_RESOURCE_SET;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DiffModel> getDiffModels() {
		if (diffModels == null) {
			diffModels = new EObjectContainmentEList<DiffModel>(DiffModel.class, this,
					DiffPackage.DIFF_RESOURCE_SET__DIFF_MODELS);
		}
		return diffModels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ResourceDiff> getResourceDiffs() {
		if (resourceDiffs == null) {
			resourceDiffs = new EObjectContainmentEList<ResourceDiff>(ResourceDiff.class, this,
					DiffPackage.DIFF_RESOURCE_SET__RESOURCE_DIFFS);
		}
		return resourceDiffs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getSubchanges() {
		int totalSubchanges = 0;
		for (DiffModel subDiff : getDiffModels()) {
			totalSubchanges += subDiff.getSubchanges();
		}
		return totalSubchanges;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DiffPackage.DIFF_RESOURCE_SET__DIFF_MODELS:
				return ((InternalEList<?>)getDiffModels()).basicRemove(otherEnd, msgs);
			case DiffPackage.DIFF_RESOURCE_SET__RESOURCE_DIFFS:
				return ((InternalEList<?>)getResourceDiffs()).basicRemove(otherEnd, msgs);
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
			case DiffPackage.DIFF_RESOURCE_SET__DIFF_MODELS:
				return getDiffModels();
			case DiffPackage.DIFF_RESOURCE_SET__RESOURCE_DIFFS:
				return getResourceDiffs();
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
			case DiffPackage.DIFF_RESOURCE_SET__DIFF_MODELS:
				getDiffModels().clear();
				getDiffModels().addAll((Collection<? extends DiffModel>)newValue);
				return;
			case DiffPackage.DIFF_RESOURCE_SET__RESOURCE_DIFFS:
				getResourceDiffs().clear();
				getResourceDiffs().addAll((Collection<? extends ResourceDiff>)newValue);
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
			case DiffPackage.DIFF_RESOURCE_SET__DIFF_MODELS:
				getDiffModels().clear();
				return;
			case DiffPackage.DIFF_RESOURCE_SET__RESOURCE_DIFFS:
				getResourceDiffs().clear();
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
			case DiffPackage.DIFF_RESOURCE_SET__DIFF_MODELS:
				return diffModels != null && !diffModels.isEmpty();
			case DiffPackage.DIFF_RESOURCE_SET__RESOURCE_DIFFS:
				return resourceDiffs != null && !resourceDiffs.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // DiffResourceSetImpl
