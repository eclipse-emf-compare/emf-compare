/**
 * Copyright (c) 2012 Obeo.
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
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.diagram.DiagramComparePackage;
import org.eclipse.emf.compare.diagram.DiagramDiff;
import org.eclipse.emf.compare.impl.DiffImpl;
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

	@Override
	public void copyLeftToRight() {
		// Don't merge an already merged (or discarded) diff
		if (getState() != DifferenceState.UNRESOLVED) {
			return;
		}

		setEquivalentDiffAsMerged();

		// Change the diff's state before we actually merge it : this allows us to avoid requirement cycles.
		setState(DifferenceState.MERGED);
		if (getSemanticDiff() != null) {
			for (Diff semanticRefines : getSemanticDiff().getRefines()) {
				semanticRefines.copyLeftToRight();
			}
			getSemanticDiff().copyLeftToRight();
		}

		if (getSource() == DifferenceSource.LEFT) {
			// merge all "requires" diffs
			mergeRequires(false);
		} else {
			// merge all "required by" diffs
			mergeRequiredBy(false);
		}

		for (Diff diff : getRefinedBy()) {
			diff.copyLeftToRight();
		}
	}

	@Override
	public void copyRightToLeft() {
		// Don't merge an already merged (or discarded) diff
		if (getState() != DifferenceState.UNRESOLVED) {
			return;
		}

		setEquivalentDiffAsMerged();

		// Change the diff's state before we actually merge it : this allows us to avoid requirement cycles.
		setState(DifferenceState.MERGED);
		if (getSemanticDiff() != null) {
			for (Diff semanticRefines : getSemanticDiff().getRefines()) {
				semanticRefines.copyRightToLeft();
			}
			getSemanticDiff().copyRightToLeft();
		}

		if (getSource() == DifferenceSource.LEFT) {
			// merge all "required by" diffs
			mergeRequiredBy(true);
		} else {
			mergeRequires(true);
		}

		for (Diff diff : getRefinedBy()) {
			diff.copyRightToLeft();
		}
	}

	private void setEquivalentDiffAsMerged() {
		if (getEquivalence() != null) {
			for (Diff equivalent : getEquivalence().getDifferences()) {
				equivalent.setState(DifferenceState.MERGED);
			}
		}
	}

	/**
	 * This will merge all {@link #getRequiredBy() differences that require us} in the given direction.
	 * 
	 * @param rightToLeft
	 *            If {@code true}, {@link #copyRightToLeft() apply} all {@link #getRequiredBy() differences
	 *            that require us}. Otherwise, {@link #copyLeftToRight() revert} them.
	 */
	protected void mergeRequiredBy(boolean rightToLeft) {
		// TODO log back to the user what we will merge along?
		for (Diff dependency : getRequiredBy()) {
			// TODO: what to do when state = Discarded but is required?
			if (rightToLeft) {
				dependency.copyRightToLeft();
			} else {
				dependency.copyLeftToRight();
			}
		}
	}

	/**
	 * This will merge all {@link #getRequires() required differences} in the given direction.
	 * 
	 * @param rightToLeft
	 *            If {@code true}, {@link #copyRightToLeft() apply} all {@link #getRequires() required
	 *            differences}. Otherwise, {@link #copyLeftToRight() revert} them.
	 */
	protected void mergeRequires(boolean rightToLeft) {
		// TODO log back to the user what we will merge along?
		for (Diff dependency : getRequires()) {
			// TODO: what to do when state = Discarded but is required?
			if (rightToLeft) {
				dependency.copyRightToLeft();
			} else {
				dependency.copyLeftToRight();
			}
		}
	}

} // DiagramDiffImpl
