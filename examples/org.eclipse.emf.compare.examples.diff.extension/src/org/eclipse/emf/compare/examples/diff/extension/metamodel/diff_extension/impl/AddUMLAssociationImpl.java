/**
 * 
 *  Copyright (c) 2006, 2009 Obeo.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *      Obeo - initial API and implementation
 * 
 *
 * $Id: AddUMLAssociationImpl.java,v 1.6 2009/01/26 19:57:42 lgoubet Exp $
 */
package org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.AddModelElement;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.DifferenceKind;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChange;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;

import org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.AddUMLAssociation;
import org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.DiffExtensionPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Add UML Association</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.AddUMLAssociationImpl#getSubDiffElements <em>Sub Diff Elements</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.AddUMLAssociationImpl#getIsHiddenBy <em>Is Hidden By</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.AddUMLAssociationImpl#isConflicting <em>Conflicting</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.AddUMLAssociationImpl#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.AddUMLAssociationImpl#getRightParent <em>Right Parent</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.AddUMLAssociationImpl#getLeftElement <em>Left Element</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AddUMLAssociationImpl extends UMLAssociationDiffImpl implements AddUMLAssociation {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "\n Copyright (c) 2006, 2009 Obeo.\n All rights reserved. This program and the accompanying materials\n are made available under the terms of the Eclipse Public License v1.0\n which accompanies this distribution, and is available at\n http://www.eclipse.org/legal/epl-v10.html\n \n Contributors:\n     Obeo - initial API and implementation\n";

	/**
	 * The cached value of the '{@link #getSubDiffElements() <em>Sub Diff Elements</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getSubDiffElements()
	 * @generated
	 * @ordered
	 */
	protected EList<DiffElement> subDiffElements;

	/**
	 * The cached value of the '{@link #getIsHiddenBy() <em>Is Hidden By</em>}' reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getIsHiddenBy()
	 * @generated
	 * @ordered
	 */
	protected EList<AbstractDiffExtension> isHiddenBy;

	/**
	 * The default value of the '{@link #isConflicting() <em>Conflicting</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isConflicting()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CONFLICTING_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isConflicting() <em>Conflicting</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isConflicting()
	 * @generated
	 * @ordered
	 */
	protected boolean conflicting = CONFLICTING_EDEFAULT;

	/**
	 * The default value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected static final DifferenceKind KIND_EDEFAULT = DifferenceKind.ADDITION;

	/**
	 * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected DifferenceKind kind = KIND_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRightParent() <em>Right Parent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRightParent()
	 * @generated
	 * @ordered
	 */
	protected EObject rightParent;

	/**
	 * The cached value of the '{@link #getLeftElement() <em>Left Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeftElement()
	 * @generated
	 * @ordered
	 */
	protected EObject leftElement;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected AddUMLAssociationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == DiffElement.class) {
			switch (derivedFeatureID) {
				case DiffExtensionPackage.ADD_UML_ASSOCIATION__SUB_DIFF_ELEMENTS: return DiffPackage.DIFF_ELEMENT__SUB_DIFF_ELEMENTS;
				case DiffExtensionPackage.ADD_UML_ASSOCIATION__IS_HIDDEN_BY: return DiffPackage.DIFF_ELEMENT__IS_HIDDEN_BY;
				case DiffExtensionPackage.ADD_UML_ASSOCIATION__CONFLICTING: return DiffPackage.DIFF_ELEMENT__CONFLICTING;
				case DiffExtensionPackage.ADD_UML_ASSOCIATION__KIND: return DiffPackage.DIFF_ELEMENT__KIND;
				default: return -1;
			}
		}
		if (baseClass == ModelElementChange.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == ModelElementChangeLeftTarget.class) {
			switch (derivedFeatureID) {
				case DiffExtensionPackage.ADD_UML_ASSOCIATION__RIGHT_PARENT: return DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT;
				case DiffExtensionPackage.ADD_UML_ASSOCIATION__LEFT_ELEMENT: return DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT;
				default: return -1;
			}
		}
		if (baseClass == AddModelElement.class) {
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
		if (baseClass == DiffElement.class) {
			switch (baseFeatureID) {
				case DiffPackage.DIFF_ELEMENT__SUB_DIFF_ELEMENTS: return DiffExtensionPackage.ADD_UML_ASSOCIATION__SUB_DIFF_ELEMENTS;
				case DiffPackage.DIFF_ELEMENT__IS_HIDDEN_BY: return DiffExtensionPackage.ADD_UML_ASSOCIATION__IS_HIDDEN_BY;
				case DiffPackage.DIFF_ELEMENT__CONFLICTING: return DiffExtensionPackage.ADD_UML_ASSOCIATION__CONFLICTING;
				case DiffPackage.DIFF_ELEMENT__KIND: return DiffExtensionPackage.ADD_UML_ASSOCIATION__KIND;
				default: return -1;
			}
		}
		if (baseClass == ModelElementChange.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == ModelElementChangeLeftTarget.class) {
			switch (baseFeatureID) {
				case DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT: return DiffExtensionPackage.ADD_UML_ASSOCIATION__RIGHT_PARENT;
				case DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT: return DiffExtensionPackage.ADD_UML_ASSOCIATION__LEFT_ELEMENT;
				default: return -1;
			}
		}
		if (baseClass == AddModelElement.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (conflicting: ");
		result.append(conflicting);
		result.append(", kind: ");
		result.append(kind);
		result.append(')');
		return result.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__SUB_DIFF_ELEMENTS:
				return getSubDiffElements();
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__IS_HIDDEN_BY:
				return getIsHiddenBy();
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__CONFLICTING:
				return isConflicting() ? Boolean.TRUE : Boolean.FALSE;
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__KIND:
				return getKind();
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__RIGHT_PARENT:
				if (resolve) return getRightParent();
				return basicGetRightParent();
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__LEFT_ELEMENT:
				if (resolve) return getLeftElement();
				return basicGetLeftElement();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__IS_HIDDEN_BY:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getIsHiddenBy()).basicAdd(otherEnd, msgs);
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
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__SUB_DIFF_ELEMENTS:
				return ((InternalEList<?>)getSubDiffElements()).basicRemove(otherEnd, msgs);
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__IS_HIDDEN_BY:
				return ((InternalEList<?>)getIsHiddenBy()).basicRemove(otherEnd, msgs);
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
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__SUB_DIFF_ELEMENTS:
				return subDiffElements != null && !subDiffElements.isEmpty();
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__IS_HIDDEN_BY:
				return isHiddenBy != null && !isHiddenBy.isEmpty();
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__CONFLICTING:
				return conflicting != CONFLICTING_EDEFAULT;
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__KIND:
				return kind != KIND_EDEFAULT;
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__RIGHT_PARENT:
				return rightParent != null;
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__LEFT_ELEMENT:
				return leftElement != null;
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
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__SUB_DIFF_ELEMENTS:
				getSubDiffElements().clear();
				getSubDiffElements().addAll((Collection<? extends DiffElement>)newValue);
				return;
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__IS_HIDDEN_BY:
				getIsHiddenBy().clear();
				getIsHiddenBy().addAll((Collection<? extends AbstractDiffExtension>)newValue);
				return;
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__RIGHT_PARENT:
				setRightParent((EObject)newValue);
				return;
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__LEFT_ELEMENT:
				setLeftElement((EObject)newValue);
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
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__SUB_DIFF_ELEMENTS:
				getSubDiffElements().clear();
				return;
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__IS_HIDDEN_BY:
				getIsHiddenBy().clear();
				return;
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__RIGHT_PARENT:
				setRightParent((EObject)null);
				return;
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__LEFT_ELEMENT:
				setLeftElement((EObject)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AbstractDiffExtension> getIsHiddenBy() {
		if (isHiddenBy == null) {
			isHiddenBy = new EObjectWithInverseResolvingEList.ManyInverse<AbstractDiffExtension>(AbstractDiffExtension.class, this, DiffExtensionPackage.ADD_UML_ASSOCIATION__IS_HIDDEN_BY, DiffPackage.ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS);
		}
		return isHiddenBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isConflicting() {
		return conflicting;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DifferenceKind getKind() {
		return kind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getRightParent() {
		if (rightParent != null && rightParent.eIsProxy()) {
			InternalEObject oldRightParent = (InternalEObject)rightParent;
			rightParent = eResolveProxy(oldRightParent);
			if (rightParent != oldRightParent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DiffExtensionPackage.ADD_UML_ASSOCIATION__RIGHT_PARENT, oldRightParent, rightParent));
			}
		}
		return rightParent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetRightParent() {
		return rightParent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRightParent(EObject newRightParent) {
		EObject oldRightParent = rightParent;
		rightParent = newRightParent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiffExtensionPackage.ADD_UML_ASSOCIATION__RIGHT_PARENT, oldRightParent, rightParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getLeftElement() {
		if (leftElement != null && leftElement.eIsProxy()) {
			InternalEObject oldLeftElement = (InternalEObject)leftElement;
			leftElement = eResolveProxy(oldLeftElement);
			if (leftElement != oldLeftElement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DiffExtensionPackage.ADD_UML_ASSOCIATION__LEFT_ELEMENT, oldLeftElement, leftElement));
			}
		}
		return leftElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetLeftElement() {
		return leftElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLeftElement(EObject newLeftElement) {
		EObject oldLeftElement = leftElement;
		leftElement = newLeftElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DiffExtensionPackage.ADD_UML_ASSOCIATION__LEFT_ELEMENT, oldLeftElement, leftElement));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DiffElement> getSubDiffElements() {
		if (subDiffElements == null) {
			subDiffElements = new EObjectContainmentEList<DiffElement>(DiffElement.class, this, DiffExtensionPackage.ADD_UML_ASSOCIATION__SUB_DIFF_ELEMENTS);
		}
		return subDiffElements;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffExtensionPackage.Literals.ADD_UML_ASSOCIATION;
	}

} // AddUMLAssociationImpl
