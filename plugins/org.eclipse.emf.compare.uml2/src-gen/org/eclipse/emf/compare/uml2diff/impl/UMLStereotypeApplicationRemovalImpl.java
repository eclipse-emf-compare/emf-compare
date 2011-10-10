/**
 * Copyright (c) 2011 Obeo.
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
import org.eclipse.emf.compare.diff.metamodel.impl.UpdateModelElementImpl;
import org.eclipse.emf.compare.uml2.diff.internal.merger.UMLStereotypeApplicationRemovalMerger;
import org.eclipse.emf.compare.uml2diff.UML2DiffPackage;
import org.eclipse.emf.compare.uml2diff.UMLDiffExtension;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationChange;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationRemoval;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.uml2.uml.Stereotype;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>UML Stereotype Application Removal</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationRemovalImpl#getHideElements <em>Hide Elements</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationRemovalImpl#isIsCollapsed <em>Is Collapsed</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationRemovalImpl#getStereotype <em>Stereotype</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UMLStereotypeApplicationRemovalImpl extends UpdateModelElementImpl implements UMLStereotypeApplicationRemoval {
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
	 * The cached value of the '{@link #getStereotype() <em>Stereotype</em>}' reference.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #getStereotype()
	 * @generated
	 * @ordered
	 */
	protected Stereotype stereotype;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLStereotypeApplicationRemovalImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UML2DiffPackage.Literals.UML_STEREOTYPE_APPLICATION_REMOVAL;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DiffElement> getHideElements() {
		if (hideElements == null) {
			hideElements = new EObjectWithInverseResolvingEList.ManyInverse<DiffElement>(DiffElement.class, this, UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__HIDE_ELEMENTS, DiffPackage.DIFF_ELEMENT__IS_HIDDEN_BY);
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
			eNotify(new ENotificationImpl(this, Notification.SET, UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__IS_COLLAPSED, oldIsCollapsed, isCollapsed));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Stereotype getStereotype() {
		if (stereotype != null && stereotype.eIsProxy()) {
			InternalEObject oldStereotype = (InternalEObject)stereotype;
			stereotype = (Stereotype)eResolveProxy(oldStereotype);
			if (stereotype != oldStereotype) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__STEREOTYPE, oldStereotype, stereotype));
			}
		}
		return stereotype;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Stereotype basicGetStereotype() {
		return stereotype;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setStereotype(Stereotype newStereotype) {
		Stereotype oldStereotype = stereotype;
		stereotype = newStereotype;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__STEREOTYPE, oldStereotype, stereotype));
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
		return new UMLStereotypeApplicationRemovalMerger();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__HIDE_ELEMENTS:
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
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__HIDE_ELEMENTS:
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
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__HIDE_ELEMENTS:
				return getHideElements();
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__IS_COLLAPSED:
				return isIsCollapsed();
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__STEREOTYPE:
				if (resolve) return getStereotype();
				return basicGetStereotype();
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
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__HIDE_ELEMENTS:
				getHideElements().clear();
				getHideElements().addAll((Collection<? extends DiffElement>)newValue);
				return;
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__IS_COLLAPSED:
				setIsCollapsed((Boolean)newValue);
				return;
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__STEREOTYPE:
				setStereotype((Stereotype)newValue);
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
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__HIDE_ELEMENTS:
				getHideElements().clear();
				return;
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__IS_COLLAPSED:
				setIsCollapsed(IS_COLLAPSED_EDEFAULT);
				return;
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__STEREOTYPE:
				setStereotype((Stereotype)null);
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
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__HIDE_ELEMENTS:
				return hideElements != null && !hideElements.isEmpty();
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__IS_COLLAPSED:
				return isCollapsed != IS_COLLAPSED_EDEFAULT;
			case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__STEREOTYPE:
				return stereotype != null;
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
				case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__HIDE_ELEMENTS: return DiffPackage.ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS;
				case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__IS_COLLAPSED: return DiffPackage.ABSTRACT_DIFF_EXTENSION__IS_COLLAPSED;
				default: return -1;
			}
		}
		if (baseClass == UMLDiffExtension.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == UMLStereotypeApplicationChange.class) {
			switch (derivedFeatureID) {
				case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__STEREOTYPE: return UML2DiffPackage.UML_STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE;
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
				case DiffPackage.ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS: return UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__HIDE_ELEMENTS;
				case DiffPackage.ABSTRACT_DIFF_EXTENSION__IS_COLLAPSED: return UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__IS_COLLAPSED;
				default: return -1;
			}
		}
		if (baseClass == UMLDiffExtension.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == UMLStereotypeApplicationChange.class) {
			switch (baseFeatureID) {
				case UML2DiffPackage.UML_STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE: return UML2DiffPackage.UML_STEREOTYPE_APPLICATION_REMOVAL__STEREOTYPE;
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

} // UMLStereotypeApplicationRemovalImpl
