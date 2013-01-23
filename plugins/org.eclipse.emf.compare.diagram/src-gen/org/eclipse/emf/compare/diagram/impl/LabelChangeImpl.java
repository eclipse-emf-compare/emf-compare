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
import org.eclipse.emf.compare.diagram.DiagramComparePackage;
import org.eclipse.emf.compare.diagram.LabelChange;
import org.eclipse.emf.compare.diagram.diff.util.GMFLabelUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.gmf.runtime.notation.View;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Label Change</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diagram.impl.LabelChangeImpl#getLeft <em>Left</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diagram.impl.LabelChangeImpl#getRight <em>Right</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diagram.impl.LabelChangeImpl#getOrigin <em>Origin</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LabelChangeImpl extends DiagramDiffImpl implements LabelChange {
	/**
	 * The default value of the '{@link #getLeft() <em>Left</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getLeft()
	 * @generated
	 * @ordered
	 */
	protected static final String LEFT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLeft() <em>Left</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getLeft()
	 * @generated
	 * @ordered
	 */
	protected String left = LEFT_EDEFAULT;

	/**
	 * The default value of the '{@link #getRight() <em>Right</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getRight()
	 * @generated
	 * @ordered
	 */
	protected static final String RIGHT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRight() <em>Right</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getRight()
	 * @generated
	 * @ordered
	 */
	protected String right = RIGHT_EDEFAULT;

	/**
	 * The default value of the '{@link #getOrigin() <em>Origin</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getOrigin()
	 * @generated
	 * @ordered
	 */
	protected static final String ORIGIN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOrigin() <em>Origin</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getOrigin()
	 * @generated
	 * @ordered
	 */
	protected String origin = ORIGIN_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected LabelChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiagramComparePackage.Literals.LABEL_CHANGE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getLeft() {
		return left;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setLeft(String newLeft) {
		String oldLeft = left;
		left = newLeft;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiagramComparePackage.LABEL_CHANGE__LEFT, oldLeft, left));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getRight() {
		return right;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setRight(String newRight) {
		String oldRight = right;
		right = newRight;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiagramComparePackage.LABEL_CHANGE__RIGHT, oldRight, right));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setOrigin(String newOrigin) {
		String oldOrigin = origin;
		origin = newOrigin;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiagramComparePackage.LABEL_CHANGE__ORIGIN, oldOrigin, origin));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiagramComparePackage.LABEL_CHANGE__LEFT:
				return getLeft();
			case DiagramComparePackage.LABEL_CHANGE__RIGHT:
				return getRight();
			case DiagramComparePackage.LABEL_CHANGE__ORIGIN:
				return getOrigin();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DiagramComparePackage.LABEL_CHANGE__LEFT:
				setLeft((String)newValue);
				return;
			case DiagramComparePackage.LABEL_CHANGE__RIGHT:
				setRight((String)newValue);
				return;
			case DiagramComparePackage.LABEL_CHANGE__ORIGIN:
				setOrigin((String)newValue);
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
			case DiagramComparePackage.LABEL_CHANGE__LEFT:
				setLeft(LEFT_EDEFAULT);
				return;
			case DiagramComparePackage.LABEL_CHANGE__RIGHT:
				setRight(RIGHT_EDEFAULT);
				return;
			case DiagramComparePackage.LABEL_CHANGE__ORIGIN:
				setOrigin(ORIGIN_EDEFAULT);
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
			case DiagramComparePackage.LABEL_CHANGE__LEFT:
				return LEFT_EDEFAULT == null ? left != null : !LEFT_EDEFAULT.equals(left);
			case DiagramComparePackage.LABEL_CHANGE__RIGHT:
				return RIGHT_EDEFAULT == null ? right != null : !RIGHT_EDEFAULT.equals(right);
			case DiagramComparePackage.LABEL_CHANGE__ORIGIN:
				return ORIGIN_EDEFAULT == null ? origin != null : !ORIGIN_EDEFAULT.equals(origin);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (left: ");
		result.append(left);
		result.append(", right: ");
		result.append(right);
		result.append(", origin: ");
		result.append(origin);
		result.append(')');
		return result.toString();
	}

	@Override
	public void copyLeftToRight() {
		doMerge(this, true);
	}

	@Override
	public void copyRightToLeft() {
		doMerge(this, false);
	}

	/**
	 * Make the merge of the specified difference according to the direction (applyInOrigin or undoInTarget).
	 * If the difference is an extension, only hidden differences will be merged. Otherwise, it is the
	 * difference itself which is merged.
	 * 
	 * @param pDiff
	 *            The difference.
	 * @param inOrigin
	 *            The direction of the merge.
	 */
	private void doMerge(LabelChange pDiff, boolean inOrigin) {
		final EObject element = pDiff.getMatch().getRight();
		final EObject origin = pDiff.getMatch().getLeft();
		if (element instanceof View && origin instanceof View) {
			final View vElement = (View)element;
			final View vOrigin = (View)origin;
			if (inOrigin) {
				GMFLabelUtil.setLabel(vOrigin, pDiff.getRight());
			} else {
				GMFLabelUtil.setLabel(vElement, pDiff.getLeft());
			}
		}
	}

} // LabelChangeImpl
