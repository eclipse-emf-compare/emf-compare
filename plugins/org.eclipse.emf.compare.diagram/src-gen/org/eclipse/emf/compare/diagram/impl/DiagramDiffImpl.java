/**
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.diagram.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.diagram.DiagramComparePackage;
import org.eclipse.emf.compare.diagram.DiagramDiff;
import org.eclipse.emf.compare.impl.DiffImpl;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Diagram Diff</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.diagram.impl.DiagramDiffImpl#getSemanticDiff <em>Semantic Diff</em>}</li>
 * <li>{@link org.eclipse.emf.compare.diagram.impl.DiagramDiffImpl#getView <em>View</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public abstract class DiagramDiffImpl extends DiffImpl implements DiagramDiff {
	/**
	 * The cached value of the '{@link #getSemanticDiff() <em>Semantic Diff</em>}' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSemanticDiff()
	 * @generated
	 * @ordered
	 */
	protected Diff semanticDiff;

	/**
	 * The cached value of the '{@link #getView() <em>View</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getView()
	 * @generated
	 * @ordered
	 */
	protected EObject view;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DiagramDiffImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiagramComparePackage.Literals.DIAGRAM_DIFF;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Diff getSemanticDiff() {
		if (semanticDiff != null && semanticDiff.eIsProxy()) {
			InternalEObject oldSemanticDiff = (InternalEObject)semanticDiff;
			semanticDiff = (Diff)eResolveProxy(oldSemanticDiff);
			if (semanticDiff != oldSemanticDiff) {
				if (eNotificationRequired()) {
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DiagramComparePackage.DIAGRAM_DIFF__SEMANTIC_DIFF, oldSemanticDiff, semanticDiff));
				}
			}
		}
		return semanticDiff;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Diff basicGetSemanticDiff() {
		return semanticDiff;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSemanticDiff(Diff newSemanticDiff) {
		Diff oldSemanticDiff = semanticDiff;
		semanticDiff = newSemanticDiff;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiagramComparePackage.DIAGRAM_DIFF__SEMANTIC_DIFF, oldSemanticDiff, semanticDiff));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject getView() {
		if (view != null && view.eIsProxy()) {
			InternalEObject oldView = (InternalEObject)view;
			view = eResolveProxy(oldView);
			if (view != oldView) {
				if (eNotificationRequired()) {
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DiagramComparePackage.DIAGRAM_DIFF__VIEW, oldView, view));
				}
			}
		}
		return view;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject basicGetView() {
		return view;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setView(EObject newView) {
		EObject oldView = view;
		view = newView;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, DiagramComparePackage.DIAGRAM_DIFF__VIEW,
					oldView, view));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiagramComparePackage.DIAGRAM_DIFF__SEMANTIC_DIFF:
				if (resolve) {
					return getSemanticDiff();
				}
				return basicGetSemanticDiff();
			case DiagramComparePackage.DIAGRAM_DIFF__VIEW:
				if (resolve) {
					return getView();
				}
				return basicGetView();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DiagramComparePackage.DIAGRAM_DIFF__SEMANTIC_DIFF:
				setSemanticDiff((Diff)newValue);
				return;
			case DiagramComparePackage.DIAGRAM_DIFF__VIEW:
				setView((EObject)newValue);
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
			case DiagramComparePackage.DIAGRAM_DIFF__SEMANTIC_DIFF:
				setSemanticDiff((Diff)null);
				return;
			case DiagramComparePackage.DIAGRAM_DIFF__VIEW:
				setView((EObject)null);
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
			case DiagramComparePackage.DIAGRAM_DIFF__SEMANTIC_DIFF:
				return semanticDiff != null;
			case DiagramComparePackage.DIAGRAM_DIFF__VIEW:
				return view != null;
		}
		return super.eIsSet(featureID);
	}

	@Deprecated
	@Override
	public void copyLeftToRight() {
		final IMerger merger = IMerger.RegistryImpl.createStandaloneInstance().getHighestRankingMerger(this);
		merger.copyLeftToRight(this, new BasicMonitor());
	}

	@Deprecated
	@Override
	public void copyRightToLeft() {
		final IMerger merger = IMerger.RegistryImpl.createStandaloneInstance().getHighestRankingMerger(this);
		merger.copyRightToLeft(this, new BasicMonitor());
	}
} // DiagramDiffImpl
