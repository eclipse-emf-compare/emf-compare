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
package org.eclipse.emf.compare.uml2.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.compare.impl.ReferenceChangeImpl;

import org.eclipse.emf.compare.uml2.StereotypeApplicationChange;
import org.eclipse.emf.compare.uml2.UMLComparePackage;
import org.eclipse.emf.compare.uml2.UMLDiff;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.uml2.uml.Stereotype;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Stereotype Application Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.uml2.impl.StereotypeApplicationChangeImpl#getDiscriminant <em>Discriminant</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.uml2.impl.StereotypeApplicationChangeImpl#getEReference <em>EReference</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.uml2.impl.StereotypeApplicationChangeImpl#getStereotype <em>Stereotype</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StereotypeApplicationChangeImpl extends ReferenceChangeImpl implements StereotypeApplicationChange {
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
	 * The cached value of the '{@link #getEReference() <em>EReference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEReference()
	 * @generated
	 * @ordered
	 */
	protected EReference eReference;

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
	protected StereotypeApplicationChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UMLComparePackage.Literals.STEREOTYPE_APPLICATION_CHANGE;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__DISCRIMINANT, oldDiscriminant, discriminant));
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
			eNotify(new ENotificationImpl(this, Notification.SET, UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__DISCRIMINANT, oldDiscriminant, discriminant));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEReference() {
		if (eReference != null && eReference.eIsProxy()) {
			InternalEObject oldEReference = (InternalEObject)eReference;
			eReference = (EReference)eResolveProxy(oldEReference);
			if (eReference != oldEReference) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__EREFERENCE, oldEReference, eReference));
			}
		}
		return eReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference basicGetEReference() {
		return eReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEReference(EReference newEReference) {
		EReference oldEReference = eReference;
		eReference = newEReference;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__EREFERENCE, oldEReference, eReference));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE, oldStereotype, stereotype));
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
			eNotify(new ENotificationImpl(this, Notification.SET, UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE, oldStereotype, stereotype));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__DISCRIMINANT:
				if (resolve) return getDiscriminant();
				return basicGetDiscriminant();
			case UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__EREFERENCE:
				if (resolve) return getEReference();
				return basicGetEReference();
			case UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE:
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
			case UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__DISCRIMINANT:
				setDiscriminant((EObject)newValue);
				return;
			case UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__EREFERENCE:
				setEReference((EReference)newValue);
				return;
			case UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE:
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
			case UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__DISCRIMINANT:
				setDiscriminant((EObject)null);
				return;
			case UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__EREFERENCE:
				setEReference((EReference)null);
				return;
			case UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE:
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
			case UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__DISCRIMINANT:
				return discriminant != null;
			case UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__EREFERENCE:
				return eReference != null;
			case UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE:
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
		if (baseClass == UMLDiff.class) {
			switch (derivedFeatureID) {
				case UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__DISCRIMINANT: return UMLComparePackage.UML_DIFF__DISCRIMINANT;
				case UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__EREFERENCE: return UMLComparePackage.UML_DIFF__EREFERENCE;
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
		if (baseClass == UMLDiff.class) {
			switch (baseFeatureID) {
				case UMLComparePackage.UML_DIFF__DISCRIMINANT: return UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__DISCRIMINANT;
				case UMLComparePackage.UML_DIFF__EREFERENCE: return UMLComparePackage.STEREOTYPE_APPLICATION_CHANGE__EREFERENCE;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

} //StereotypeApplicationChangeImpl
