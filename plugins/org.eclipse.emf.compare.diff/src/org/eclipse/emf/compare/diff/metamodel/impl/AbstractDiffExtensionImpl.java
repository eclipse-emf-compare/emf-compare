/**
 * <copyright>
 * </copyright>
 *
 * $Id: AbstractDiffExtensionImpl.java,v 1.5 2007/10/16 11:27:50 lgoubet Exp $
 */
package org.eclipse.emf.compare.diff.metamodel.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.diff.generic.DiffMaker;
import org.eclipse.emf.compare.diff.merge.api.AbstractMerger;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Abstract Diff Extension</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.AbstractDiffExtensionImpl#getHideElements <em>Hide Elements</em>}</li>
 * <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.AbstractDiffExtensionImpl#isIsCollapsed <em>Is Collapsed</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public abstract class AbstractDiffExtensionImpl extends EObjectImpl implements AbstractDiffExtension {
	/**
	 * The default value of the '{@link #isIsCollapsed() <em>Is Collapsed</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isIsCollapsed()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_COLLAPSED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #getHideElements() <em>Hide Elements</em>}' reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getHideElements()
	 * @generated
	 * @ordered
	 */
	protected EList hideElements;

	/**
	 * The cached value of the '{@link #isIsCollapsed() <em>Is Collapsed</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isIsCollapsed()
	 * @generated
	 * @ordered
	 */
	protected boolean isCollapsed = IS_COLLAPSED_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected AbstractDiffExtensionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffPackage.ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS:
				return getHideElements();
			case DiffPackage.ABSTRACT_DIFF_EXTENSION__IS_COLLAPSED:
				return isIsCollapsed() ? Boolean.TRUE : Boolean.FALSE;
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DiffPackage.ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS:
				return ((InternalEList)getHideElements()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DiffPackage.ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS:
				return ((InternalEList)getHideElements()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DiffPackage.ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS:
				return hideElements != null && !hideElements.isEmpty();
			case DiffPackage.ABSTRACT_DIFF_EXTENSION__IS_COLLAPSED:
				return isCollapsed != IS_COLLAPSED_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DiffPackage.ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS:
				getHideElements().clear();
				getHideElements().addAll((Collection)newValue);
				return;
			case DiffPackage.ABSTRACT_DIFF_EXTENSION__IS_COLLAPSED:
				setIsCollapsed(((Boolean)newValue).booleanValue());
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case DiffPackage.ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS:
				getHideElements().clear();
				return;
			case DiffPackage.ABSTRACT_DIFF_EXTENSION__IS_COLLAPSED:
				setIsCollapsed(IS_COLLAPSED_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getHideElements() {
		if (hideElements == null) {
			hideElements = new EObjectWithInverseResolvingEList.ManyInverse(DiffElement.class, this,
					DiffPackage.ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS,
					DiffPackage.DIFF_ELEMENT__IS_HIDDEN_BY);
		}
		return hideElements;
	}

	/**
	 * <!-- begin-user-doc --> Should return a org.eclipse.swt.graphics.Image object representing the
	 * {@link AbstractDiffExtension}. <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Object getImage() {
		return null;
	}

	/**
	 * <!-- begin-user-doc --> Should return the text used to represent the {@link AbstractDiffExtension};
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public String getText() {
		return "Diff Extension"; //$NON-NLS-1$
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isIsCollapsed() {
		return isCollapsed;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setIsCollapsed(boolean newIsCollapsed) {
		boolean oldIsCollapsed = isCollapsed;
		isCollapsed = newIsCollapsed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffPackage.ABSTRACT_DIFF_EXTENSION__IS_COLLAPSED, oldIsCollapsed, isCollapsed));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (isCollapsed: ");
		result.append(isCollapsed);
		result.append(')');
		return result.toString();
	}

	/**
	 * <!-- begin-user-doc --> This method is called by the {@link DiffMaker} to let the diff extension a
	 * chance to refactor the diff model and plug themselves inside. <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void visit(DiffModel diffModel) {
		// you should redefine this method
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected EClass eStaticClass() {
		return DiffPackage.Literals.ABSTRACT_DIFF_EXTENSION;
	}

} // AbstractDiffExtensionImpl
