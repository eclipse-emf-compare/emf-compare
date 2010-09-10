/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: IdEmfReferenceImpl.java,v 1.1 2010/09/10 15:32:55 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.symrefs.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.symrefs.IdEmfReference;
import org.eclipse.emf.compare.mpatch.symrefs.SymrefsPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Id Emf Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.IdEmfReferenceImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.IdEmfReferenceImpl#getUriReference <em>Uri Reference</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.IdEmfReferenceImpl#getUpperBound <em>Upper Bound</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.IdEmfReferenceImpl#getLowerBound <em>Lower Bound</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.IdEmfReferenceImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.IdEmfReferenceImpl#getIdAttributeValue <em>Id Attribute Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IdEmfReferenceImpl extends EObjectImpl implements IdEmfReference {
	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected EClass type;

	/**
	 * The default value of the '{@link #getUriReference() <em>Uri Reference</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUriReference()
	 * @generated
	 * @ordered
	 */
	protected static final String URI_REFERENCE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUriReference() <em>Uri Reference</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUriReference()
	 * @generated
	 * @ordered
	 */
	protected String uriReference = URI_REFERENCE_EDEFAULT;

	/**
	 * The default value of the '{@link #getUpperBound() <em>Upper Bound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpperBound()
	 * @generated
	 * @ordered
	 */
	protected static final int UPPER_BOUND_EDEFAULT = 1;

	/**
	 * The cached value of the '{@link #getUpperBound() <em>Upper Bound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpperBound()
	 * @generated
	 * @ordered
	 */
	protected int upperBound = UPPER_BOUND_EDEFAULT;

	/**
	 * The default value of the '{@link #getLowerBound() <em>Lower Bound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLowerBound()
	 * @generated
	 * @ordered
	 */
	protected static final int LOWER_BOUND_EDEFAULT = 1;

	/**
	 * The cached value of the '{@link #getLowerBound() <em>Lower Bound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLowerBound()
	 * @generated
	 * @ordered
	 */
	protected int lowerBound = LOWER_BOUND_EDEFAULT;

	/**
	 * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected static final String LABEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected String label = LABEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getIdAttributeValue() <em>Id Attribute Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdAttributeValue()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_ATTRIBUTE_VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIdAttributeValue() <em>Id Attribute Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdAttributeValue()
	 * @generated
	 * @ordered
	 */
	protected String idAttributeValue = ID_ATTRIBUTE_VALUE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IdEmfReferenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SymrefsPackage.Literals.ID_EMF_REFERENCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getType() {
		if (type != null && type.eIsProxy()) {
			InternalEObject oldType = (InternalEObject)type;
			type = (EClass)eResolveProxy(oldType);
			if (type != oldType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SymrefsPackage.ID_EMF_REFERENCE__TYPE, oldType, type));
			}
		}
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass basicGetType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(EClass newType) {
		EClass oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SymrefsPackage.ID_EMF_REFERENCE__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUriReference() {
		return uriReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUriReference(String newUriReference) {
		String oldUriReference = uriReference;
		uriReference = newUriReference;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SymrefsPackage.ID_EMF_REFERENCE__URI_REFERENCE, oldUriReference, uriReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getUpperBound() {
		return upperBound;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean setUpperBound(int newUpperBound) {
		if (newUpperBound != 1) 
			return false; // only 1 allowed as upper bound!
		int oldUpperBound = upperBound;
		upperBound = newUpperBound;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SymrefsPackage.ID_EMF_REFERENCE__UPPER_BOUND, oldUpperBound, upperBound));
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getLowerBound() {
		return lowerBound;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLabel(String newLabel) {
		String oldLabel = label;
		label = newLabel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SymrefsPackage.ID_EMF_REFERENCE__LABEL, oldLabel, label));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIdAttributeValue() {
		return idAttributeValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIdAttributeValue(String newIdAttributeValue) {
		String oldIdAttributeValue = idAttributeValue;
		idAttributeValue = newIdAttributeValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SymrefsPackage.ID_EMF_REFERENCE__ID_ATTRIBUTE_VALUE, oldIdAttributeValue, idAttributeValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<EObject> resolve(EObject model) {
		if (model.eResource() == null) {
			throw new IllegalArgumentException("The model must be contained in a resource to resolve id references!");
		}
		final EList<EObject> result = new BasicEList<EObject>();
		
		// just ask the resource of the target model to resolve the unique id ;-)
		final EObject eObject = model.eResource().getEObject(getIdAttributeValue());
		if (eObject != null) {
			result.add(eObject);
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean resolvesEqual(IElementReference other) {
		// are we comparing with ourself?
		if (equals(other))
			return true;
		
		// static checks
		if (!(other instanceof IdEmfReference))
			return false; // wrong type (references are not comparable)
		if (!getType().getInstanceClassName().equals(other.getType().getInstanceClassName()))
			return false; // wrong target type
		if (getLowerBound() != other.getLowerBound() || getUpperBound() != other.getUpperBound())
			return false; // different cardinality
		
		// actual comparison: id attribute value
		return ((IdEmfReference)other).getIdAttributeValue().equals(getIdAttributeValue());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SymrefsPackage.ID_EMF_REFERENCE__TYPE:
				if (resolve) return getType();
				return basicGetType();
			case SymrefsPackage.ID_EMF_REFERENCE__URI_REFERENCE:
				return getUriReference();
			case SymrefsPackage.ID_EMF_REFERENCE__UPPER_BOUND:
				return getUpperBound();
			case SymrefsPackage.ID_EMF_REFERENCE__LOWER_BOUND:
				return getLowerBound();
			case SymrefsPackage.ID_EMF_REFERENCE__LABEL:
				return getLabel();
			case SymrefsPackage.ID_EMF_REFERENCE__ID_ATTRIBUTE_VALUE:
				return getIdAttributeValue();
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
			case SymrefsPackage.ID_EMF_REFERENCE__TYPE:
				setType((EClass)newValue);
				return;
			case SymrefsPackage.ID_EMF_REFERENCE__URI_REFERENCE:
				setUriReference((String)newValue);
				return;
			case SymrefsPackage.ID_EMF_REFERENCE__UPPER_BOUND:
				setUpperBound((Integer)newValue);
				return;
			case SymrefsPackage.ID_EMF_REFERENCE__LABEL:
				setLabel((String)newValue);
				return;
			case SymrefsPackage.ID_EMF_REFERENCE__ID_ATTRIBUTE_VALUE:
				setIdAttributeValue((String)newValue);
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
			case SymrefsPackage.ID_EMF_REFERENCE__TYPE:
				setType((EClass)null);
				return;
			case SymrefsPackage.ID_EMF_REFERENCE__URI_REFERENCE:
				setUriReference(URI_REFERENCE_EDEFAULT);
				return;
			case SymrefsPackage.ID_EMF_REFERENCE__UPPER_BOUND:
				setUpperBound(UPPER_BOUND_EDEFAULT);
				return;
			case SymrefsPackage.ID_EMF_REFERENCE__LABEL:
				setLabel(LABEL_EDEFAULT);
				return;
			case SymrefsPackage.ID_EMF_REFERENCE__ID_ATTRIBUTE_VALUE:
				setIdAttributeValue(ID_ATTRIBUTE_VALUE_EDEFAULT);
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
			case SymrefsPackage.ID_EMF_REFERENCE__TYPE:
				return type != null;
			case SymrefsPackage.ID_EMF_REFERENCE__URI_REFERENCE:
				return URI_REFERENCE_EDEFAULT == null ? uriReference != null : !URI_REFERENCE_EDEFAULT.equals(uriReference);
			case SymrefsPackage.ID_EMF_REFERENCE__UPPER_BOUND:
				return upperBound != UPPER_BOUND_EDEFAULT;
			case SymrefsPackage.ID_EMF_REFERENCE__LOWER_BOUND:
				return lowerBound != LOWER_BOUND_EDEFAULT;
			case SymrefsPackage.ID_EMF_REFERENCE__LABEL:
				return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
			case SymrefsPackage.ID_EMF_REFERENCE__ID_ATTRIBUTE_VALUE:
				return ID_ATTRIBUTE_VALUE_EDEFAULT == null ? idAttributeValue != null : !ID_ATTRIBUTE_VALUE_EDEFAULT.equals(idAttributeValue);
		}
		return super.eIsSet(featureID);
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
		result.append(" (uriReference: ");
		result.append(uriReference);
		result.append(", upperBound: ");
		result.append(upperBound);
		result.append(", lowerBound: ");
		result.append(lowerBound);
		result.append(", label: ");
		result.append(label);
		result.append(", idAttributeValue: ");
		result.append(idAttributeValue);
		result.append(')');
		return result.toString();
	}

} //IdEmfReferenceImpl
