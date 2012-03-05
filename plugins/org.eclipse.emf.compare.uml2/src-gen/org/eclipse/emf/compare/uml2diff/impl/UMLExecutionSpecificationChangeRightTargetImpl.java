/**
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2diff.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.merge.IMerger;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeRightTargetImpl;
import org.eclipse.emf.compare.uml2diff.UML2DiffPackage;
import org.eclipse.emf.compare.uml2diff.UMLDiffExtension;
import org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChange;
import org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeRightTarget;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>UML Execution Specification Change Right Target</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.uml2diff.impl.UMLExecutionSpecificationChangeRightTargetImpl#getHideElements <em>Hide Elements</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.uml2diff.impl.UMLExecutionSpecificationChangeRightTargetImpl#isIsCollapsed <em>Is Collapsed</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UMLExecutionSpecificationChangeRightTargetImpl extends ModelElementChangeRightTargetImpl implements UMLExecutionSpecificationChangeRightTarget {
	/**
	 * The cached value of the '{@link #getHideElements() <em>Hide Elements</em>}' reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getHideElements()
	 * @generated
	 * @ordered
	 */
	protected EList<DiffElement> hideElements;

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
	 * The cached value of the '{@link #isIsCollapsed() <em>Is Collapsed</em>}' attribute.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #isIsCollapsed()
	 * @generated
	 * @ordered
	 */
	protected boolean isCollapsed = IS_COLLAPSED_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLExecutionSpecificationChangeRightTargetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UML2DiffPackage.Literals.UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DiffElement> getHideElements() {
		if (hideElements == null) {
			hideElements = new EObjectWithInverseResolvingEList.ManyInverse<DiffElement>(DiffElement.class, this, UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS, DiffPackage.DIFF_ELEMENT__IS_HIDDEN_BY);
		}
		return hideElements;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsCollapsed() {
		return isCollapsed;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsCollapsed(boolean newIsCollapsed) {
		boolean oldIsCollapsed = isCollapsed;
		isCollapsed = newIsCollapsed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__IS_COLLAPSED, oldIsCollapsed, isCollapsed));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void visit(DiffModel diffModel) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public String getText() {
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Object getImage() {
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public IMerger provideMerger() {
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getHideElements()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS:
				return ((InternalEList<?>)getHideElements()).basicRemove(otherEnd, msgs);
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
			case UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS:
				return getHideElements();
			case UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__IS_COLLAPSED:
				return isIsCollapsed();
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
			case UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS:
				getHideElements().clear();
				getHideElements().addAll((Collection<? extends DiffElement>)newValue);
				return;
			case UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__IS_COLLAPSED:
				setIsCollapsed((Boolean)newValue);
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
			case UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS:
				getHideElements().clear();
				return;
			case UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__IS_COLLAPSED:
				setIsCollapsed(IS_COLLAPSED_EDEFAULT);
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
			case UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS:
				return hideElements != null && !hideElements.isEmpty();
			case UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__IS_COLLAPSED:
				return isCollapsed != IS_COLLAPSED_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == AbstractDiffExtension.class) {
			switch (derivedFeatureID) {
				case UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS: return DiffPackage.ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS;
				case UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__IS_COLLAPSED: return DiffPackage.ABSTRACT_DIFF_EXTENSION__IS_COLLAPSED;
				default: return -1;
			}
		}
		if (baseClass == UMLDiffExtension.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == UMLExecutionSpecificationChange.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == AbstractDiffExtension.class) {
			switch (baseFeatureID) {
				case DiffPackage.ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS: return UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS;
				case DiffPackage.ABSTRACT_DIFF_EXTENSION__IS_COLLAPSED: return UML2DiffPackage.UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__IS_COLLAPSED;
				default: return -1;
			}
		}
		if (baseClass == UMLDiffExtension.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == UMLExecutionSpecificationChange.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (isCollapsed: "); //$NON-NLS-1$
		result.append(isCollapsed);
		result.append(')');
		return result.toString();
	}

} // UMLExecutionSpecificationChangeRightTargetImpl
