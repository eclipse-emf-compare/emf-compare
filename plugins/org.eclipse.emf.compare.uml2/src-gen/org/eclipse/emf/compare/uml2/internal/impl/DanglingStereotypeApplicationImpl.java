/**
 * Copyright (c) 2012, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2.internal.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.compare.impl.ResourceAttachmentChangeImpl;
import org.eclipse.emf.compare.internal.spec.ResourceAttachmentChangeSpec;
import org.eclipse.emf.compare.uml2.internal.DanglingStereotypeApplication;
import org.eclipse.emf.compare.uml2.internal.UMLComparePackage;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dangling Stereotype Application</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.uml2.internal.impl.DanglingStereotypeApplicationImpl#getDiscriminant <em>Discriminant</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.uml2.internal.impl.DanglingStereotypeApplicationImpl#getEReference <em>EReference</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DanglingStereotypeApplicationImpl extends ResourceAttachmentChangeSpec implements DanglingStereotypeApplication {
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DanglingStereotypeApplicationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UMLComparePackage.Literals.DANGLING_STEREOTYPE_APPLICATION;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							UMLComparePackage.DANGLING_STEREOTYPE_APPLICATION__DISCRIMINANT, oldDiscriminant,
							discriminant));
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
			eNotify(new ENotificationImpl(this, Notification.SET,
					UMLComparePackage.DANGLING_STEREOTYPE_APPLICATION__DISCRIMINANT, oldDiscriminant,
					discriminant));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							UMLComparePackage.DANGLING_STEREOTYPE_APPLICATION__EREFERENCE, oldEReference,
							eReference));
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
			eNotify(new ENotificationImpl(this, Notification.SET,
					UMLComparePackage.DANGLING_STEREOTYPE_APPLICATION__EREFERENCE, oldEReference, eReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case UMLComparePackage.DANGLING_STEREOTYPE_APPLICATION__DISCRIMINANT:
				if (resolve)
					return getDiscriminant();
				return basicGetDiscriminant();
			case UMLComparePackage.DANGLING_STEREOTYPE_APPLICATION__EREFERENCE:
				if (resolve)
					return getEReference();
				return basicGetEReference();
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
			case UMLComparePackage.DANGLING_STEREOTYPE_APPLICATION__DISCRIMINANT:
				setDiscriminant((EObject)newValue);
				return;
			case UMLComparePackage.DANGLING_STEREOTYPE_APPLICATION__EREFERENCE:
				setEReference((EReference)newValue);
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
			case UMLComparePackage.DANGLING_STEREOTYPE_APPLICATION__DISCRIMINANT:
				setDiscriminant((EObject)null);
				return;
			case UMLComparePackage.DANGLING_STEREOTYPE_APPLICATION__EREFERENCE:
				setEReference((EReference)null);
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
			case UMLComparePackage.DANGLING_STEREOTYPE_APPLICATION__DISCRIMINANT:
				return discriminant != null;
			case UMLComparePackage.DANGLING_STEREOTYPE_APPLICATION__EREFERENCE:
				return eReference != null;
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
				case UMLComparePackage.DANGLING_STEREOTYPE_APPLICATION__DISCRIMINANT:
					return UMLComparePackage.UML_DIFF__DISCRIMINANT;
				case UMLComparePackage.DANGLING_STEREOTYPE_APPLICATION__EREFERENCE:
					return UMLComparePackage.UML_DIFF__EREFERENCE;
				default:
					return -1;
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
				case UMLComparePackage.UML_DIFF__DISCRIMINANT:
					return UMLComparePackage.DANGLING_STEREOTYPE_APPLICATION__DISCRIMINANT;
				case UMLComparePackage.UML_DIFF__EREFERENCE:
					return UMLComparePackage.DANGLING_STEREOTYPE_APPLICATION__EREFERENCE;
				default:
					return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

} //DanglingStereotypeApplicationImpl
