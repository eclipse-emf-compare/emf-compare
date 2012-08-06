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
package org.eclipse.emf.compare.uml2diff.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.compare.impl.ReferenceChangeImpl;
import org.eclipse.emf.compare.uml2diff.UMLExtension;
import org.eclipse.emf.compare.impl.DiffImpl;

import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationChange;
import org.eclipse.emf.compare.uml2diff.Uml2diffPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.uml2.uml.Stereotype;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>UML Stereotype Application Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationChangeImpl#getDiscriminant <em>Discriminant</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationChangeImpl#getStereotype <em>Stereotype</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UMLStereotypeApplicationChangeImpl extends ReferenceChangeImpl implements UMLStereotypeApplicationChange {
	/**
	 * The cached value of the '{@link #getDiscriminant() <em>Discriminant</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiscriminant()
	 * @generated
	 * @ordered
	 */
	protected EObject discriminant;
	/**
	 * The cached value of the '{@link #getStereotype() <em>Stereotype</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStereotype()
	 * @generated
	 * @ordered
	 */
	protected Stereotype stereotype;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLStereotypeApplicationChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Uml2diffPackage.Literals.UML_STEREOTYPE_APPLICATION_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getDiscriminant() {
		if (discriminant != null && discriminant.eIsProxy()) {
			InternalEObject oldDiscriminant = (InternalEObject)discriminant;
			discriminant = eResolveProxy(oldDiscriminant);
			if (discriminant != oldDiscriminant) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, Uml2diffPackage.UML_STEREOTYPE_APPLICATION_CHANGE__DISCRIMINANT, oldDiscriminant, discriminant));
			}
		}
		return discriminant;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetDiscriminant() {
		return discriminant;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDiscriminant(EObject newDiscriminant) {
		EObject oldDiscriminant = discriminant;
		discriminant = newDiscriminant;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Uml2diffPackage.UML_STEREOTYPE_APPLICATION_CHANGE__DISCRIMINANT, oldDiscriminant, discriminant));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Stereotype getStereotype() {
		if (stereotype != null && stereotype.eIsProxy()) {
			InternalEObject oldStereotype = (InternalEObject)stereotype;
			stereotype = (Stereotype)eResolveProxy(oldStereotype);
			if (stereotype != oldStereotype) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, Uml2diffPackage.UML_STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE, oldStereotype, stereotype));
			}
		}
		return stereotype;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Stereotype basicGetStereotype() {
		return stereotype;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStereotype(Stereotype newStereotype) {
		Stereotype oldStereotype = stereotype;
		stereotype = newStereotype;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Uml2diffPackage.UML_STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE, oldStereotype, stereotype));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Uml2diffPackage.UML_STEREOTYPE_APPLICATION_CHANGE__DISCRIMINANT:
				if (resolve) return getDiscriminant();
				return basicGetDiscriminant();
			case Uml2diffPackage.UML_STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE:
				if (resolve) return getStereotype();
				return basicGetStereotype();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Uml2diffPackage.UML_STEREOTYPE_APPLICATION_CHANGE__DISCRIMINANT:
				setDiscriminant((EObject)newValue);
				return;
			case Uml2diffPackage.UML_STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE:
				setStereotype((Stereotype)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case Uml2diffPackage.UML_STEREOTYPE_APPLICATION_CHANGE__DISCRIMINANT:
				setDiscriminant((EObject)null);
				return;
			case Uml2diffPackage.UML_STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE:
				setStereotype((Stereotype)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case Uml2diffPackage.UML_STEREOTYPE_APPLICATION_CHANGE__DISCRIMINANT:
				return discriminant != null;
			case Uml2diffPackage.UML_STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE:
				return stereotype != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == UMLExtension.class) {
			switch (derivedFeatureID) {
				case Uml2diffPackage.UML_STEREOTYPE_APPLICATION_CHANGE__DISCRIMINANT: return Uml2diffPackage.UML_EXTENSION__DISCRIMINANT;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == UMLExtension.class) {
			switch (baseFeatureID) {
				case Uml2diffPackage.UML_EXTENSION__DISCRIMINANT: return Uml2diffPackage.UML_STEREOTYPE_APPLICATION_CHANGE__DISCRIMINANT;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

} //UMLStereotypeApplicationChangeImpl
