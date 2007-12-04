/**
 * 
 *  Copyright (c) 2006, 2007 Obeo.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *      Obeo - initial API and implementation
 * 
 *
 * $Id: AddUMLAssociationImpl.java,v 1.2 2007/12/04 13:14:50 lgoubet Exp $
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
import org.eclipse.emf.compare.diff.metamodel.ModelElementChange;
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
 * <li>{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.AddUMLAssociationImpl#getSubDiffElements <em>Sub Diff Elements</em>}</li>
 * <li>{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.AddUMLAssociationImpl#getIsHiddenBy <em>Is Hidden By</em>}</li>
 * <li>{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.AddUMLAssociationImpl#getLeftParent <em>Left Parent</em>}</li>
 * <li>{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.AddUMLAssociationImpl#getRightElement <em>Right Element</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class AddUMLAssociationImpl extends UMLAssociationDiffImpl implements AddUMLAssociation {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String copyright = "\n Copyright (c) 2006, 2007 Obeo.\n All rights reserved. This program and the accompanying materials\n are made available under the terms of the Eclipse Public License v1.0\n which accompanies this distribution, and is available at\n http://www.eclipse.org/legal/epl-v10.html\n \n Contributors:\n     Obeo - initial API and implementation\n";

	/**
	 * The cached value of the '{@link #getIsHiddenBy() <em>Is Hidden By</em>}' reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getIsHiddenBy()
	 * @generated
	 * @ordered
	 */
	protected EList isHiddenBy;

	/**
	 * The cached value of the '{@link #getLeftParent() <em>Left Parent</em>}' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLeftParent()
	 * @generated
	 * @ordered
	 */
	protected EObject leftParent;

	/**
	 * The cached value of the '{@link #getRightElement() <em>Right Element</em>}' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRightElement()
	 * @generated
	 * @ordered
	 */
	protected EObject rightElement;

	/**
	 * The cached value of the '{@link #getSubDiffElements() <em>Sub Diff Elements</em>}' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSubDiffElements()
	 * @generated
	 * @ordered
	 */
	protected EList subDiffElements;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected AddUMLAssociationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject basicGetLeftParent() {
		return leftParent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject basicGetRightElement() {
		return rightElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == DiffElement.class) {
			switch (derivedFeatureID) {
				case DiffExtensionPackage.ADD_UML_ASSOCIATION__SUB_DIFF_ELEMENTS:
					return DiffPackage.DIFF_ELEMENT__SUB_DIFF_ELEMENTS;
				case DiffExtensionPackage.ADD_UML_ASSOCIATION__IS_HIDDEN_BY:
					return DiffPackage.DIFF_ELEMENT__IS_HIDDEN_BY;
				default:
					return -1;
			}
		}
		if (baseClass == ModelElementChange.class) {
			switch (derivedFeatureID) {
				default:
					return -1;
			}
		}
		if (baseClass == ModelElementChangeRightTarget.class) {
			switch (derivedFeatureID) {
				case DiffExtensionPackage.ADD_UML_ASSOCIATION__LEFT_PARENT:
					return DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT;
				case DiffExtensionPackage.ADD_UML_ASSOCIATION__RIGHT_ELEMENT:
					return DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;
				default:
					return -1;
			}
		}
		if (baseClass == AddModelElement.class) {
			switch (derivedFeatureID) {
				default:
					return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == DiffElement.class) {
			switch (baseFeatureID) {
				case DiffPackage.DIFF_ELEMENT__SUB_DIFF_ELEMENTS:
					return DiffExtensionPackage.ADD_UML_ASSOCIATION__SUB_DIFF_ELEMENTS;
				case DiffPackage.DIFF_ELEMENT__IS_HIDDEN_BY:
					return DiffExtensionPackage.ADD_UML_ASSOCIATION__IS_HIDDEN_BY;
				default:
					return -1;
			}
		}
		if (baseClass == ModelElementChange.class) {
			switch (baseFeatureID) {
				default:
					return -1;
			}
		}
		if (baseClass == ModelElementChangeRightTarget.class) {
			switch (baseFeatureID) {
				case DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT:
					return DiffExtensionPackage.ADD_UML_ASSOCIATION__LEFT_PARENT;
				case DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT:
					return DiffExtensionPackage.ADD_UML_ASSOCIATION__RIGHT_ELEMENT;
				default:
					return -1;
			}
		}
		if (baseClass == AddModelElement.class) {
			switch (baseFeatureID) {
				default:
					return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__SUB_DIFF_ELEMENTS:
				return getSubDiffElements();
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__IS_HIDDEN_BY:
				return getIsHiddenBy();
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__LEFT_PARENT:
				if (resolve)
					return getLeftParent();
				return basicGetLeftParent();
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__RIGHT_ELEMENT:
				if (resolve)
					return getRightElement();
				return basicGetRightElement();
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
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__IS_HIDDEN_BY:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getIsHiddenBy()).basicAdd(otherEnd,
						msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
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
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__SUB_DIFF_ELEMENTS:
				return subDiffElements != null && !subDiffElements.isEmpty();
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__IS_HIDDEN_BY:
				return isHiddenBy != null && !isHiddenBy.isEmpty();
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__LEFT_PARENT:
				return leftParent != null;
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__RIGHT_ELEMENT:
				return rightElement != null;
		}
		return super.eIsSet(featureID);
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
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__SUB_DIFF_ELEMENTS:
				getSubDiffElements().clear();
				getSubDiffElements().addAll((Collection<? extends DiffElement>)newValue);
				return;
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__IS_HIDDEN_BY:
				getIsHiddenBy().clear();
				getIsHiddenBy().addAll((Collection<? extends AbstractDiffExtension>)newValue);
				return;
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__LEFT_PARENT:
				setLeftParent((EObject)newValue);
				return;
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__RIGHT_ELEMENT:
				setRightElement((EObject)newValue);
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
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__SUB_DIFF_ELEMENTS:
				getSubDiffElements().clear();
				return;
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__IS_HIDDEN_BY:
				getIsHiddenBy().clear();
				return;
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__LEFT_PARENT:
				setLeftParent((EObject)null);
				return;
			case DiffExtensionPackage.ADD_UML_ASSOCIATION__RIGHT_ELEMENT:
				setRightElement((EObject)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getIsHiddenBy() {
		if (isHiddenBy == null) {
			isHiddenBy = new EObjectWithInverseResolvingEList.ManyInverse<AbstractDiffExtension>(
					AbstractDiffExtension.class, this,
					DiffExtensionPackage.ADD_UML_ASSOCIATION__IS_HIDDEN_BY,
					DiffPackage.ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS);
		}
		return isHiddenBy;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject getLeftParent() {
		if (leftParent != null && leftParent.eIsProxy()) {
			InternalEObject oldLeftParent = (InternalEObject)leftParent;
			leftParent = eResolveProxy(oldLeftParent);
			if (leftParent != oldLeftParent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DiffExtensionPackage.ADD_UML_ASSOCIATION__LEFT_PARENT, oldLeftParent, leftParent));
			}
		}
		return leftParent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EObject getRightElement() {
		if (rightElement != null && rightElement.eIsProxy()) {
			InternalEObject oldRightElement = (InternalEObject)rightElement;
			rightElement = eResolveProxy(oldRightElement);
			if (rightElement != oldRightElement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DiffExtensionPackage.ADD_UML_ASSOCIATION__RIGHT_ELEMENT, oldRightElement,
							rightElement));
			}
		}
		return rightElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getSubDiffElements() {
		if (subDiffElements == null) {
			subDiffElements = new EObjectContainmentEList<DiffElement>(DiffElement.class, this,
					DiffExtensionPackage.ADD_UML_ASSOCIATION__SUB_DIFF_ELEMENTS);
		}
		return subDiffElements;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLeftParent(EObject newLeftParent) {
		EObject oldLeftParent = leftParent;
		leftParent = newLeftParent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffExtensionPackage.ADD_UML_ASSOCIATION__LEFT_PARENT, oldLeftParent, leftParent));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setRightElement(EObject newRightElement) {
		EObject oldRightElement = rightElement;
		rightElement = newRightElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					DiffExtensionPackage.ADD_UML_ASSOCIATION__RIGHT_ELEMENT, oldRightElement, rightElement));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DiffExtensionPackage.Literals.ADD_UML_ASSOCIATION;
	}

} // AddUMLAssociationImpl
