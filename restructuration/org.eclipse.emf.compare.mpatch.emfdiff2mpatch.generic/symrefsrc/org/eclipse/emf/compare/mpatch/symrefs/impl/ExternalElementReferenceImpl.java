/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: ExternalElementReferenceImpl.java,v 1.1 2010/09/10 15:32:55 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.symrefs.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.symrefs.ExternalElementReference;
import org.eclipse.emf.compare.mpatch.symrefs.SymrefsPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>External Element Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.ExternalElementReferenceImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.ExternalElementReferenceImpl#getUriReference <em>Uri Reference</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.ExternalElementReferenceImpl#getUpperBound <em>Upper Bound</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.ExternalElementReferenceImpl#getLowerBound <em>Lower Bound</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.ExternalElementReferenceImpl#getLabel <em>Label</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ExternalElementReferenceImpl extends EObjectImpl implements ExternalElementReference {
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExternalElementReferenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SymrefsPackage.Literals.EXTERNAL_ELEMENT_REFERENCE;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__TYPE, oldType, type));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__TYPE, oldType, type));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__URI_REFERENCE, oldUriReference, uriReference));
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
		int oldUpperBound = upperBound;
		upperBound = newUpperBound;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__UPPER_BOUND, oldUpperBound, upperBound));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__LABEL, oldLabel, label));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<EObject> resolve(EObject model) {
		// 1. get the current resourceset
		if (model.eResource() == null) {
			throw new IllegalArgumentException("model element must be contained in a resource!");
		}
		ResourceSet resourceSet = model.eResource().getResourceSet();
		if (resourceSet == null) {
			resourceSet = new ResourceSetImpl();
			resourceSet.getResources().add(model.eResource());
		}
		
		// 2. get the uri the resource
		final String resourceUri = getUriReference().substring(0, getUriReference().indexOf("#"));
		final Resource newResource = resourceSet.getResource(URI.createURI(resourceUri), true);
		
		// 3. resolve the element
		final String fragment = getUriReference().substring(getUriReference().indexOf("#") + 1);
		final EObject resolvedElement = newResource.getEObject(fragment);
		
		// 4. return the element
		final EList<EObject> resolvedElements = new BasicEList<EObject>();
		if (resolvedElement != null)
			resolvedElements.add(resolvedElement);
		return resolvedElements;
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
		if (!(other instanceof ExternalElementReference))
			return false; // wrong type (references are not comparable)
		if (!getType().getInstanceClassName().equals(other.getType().getInstanceClassName()))
			return false; // wrong target type
		if (getLowerBound() != other.getLowerBound() || getUpperBound() != other.getUpperBound())
			return false; // different cardinality
		
		// actual comparison
		return getUriReference().equals(other.getUriReference());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__TYPE:
				if (resolve) return getType();
				return basicGetType();
			case SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__URI_REFERENCE:
				return getUriReference();
			case SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__UPPER_BOUND:
				return getUpperBound();
			case SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__LOWER_BOUND:
				return getLowerBound();
			case SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__LABEL:
				return getLabel();
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
			case SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__TYPE:
				setType((EClass)newValue);
				return;
			case SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__URI_REFERENCE:
				setUriReference((String)newValue);
				return;
			case SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__UPPER_BOUND:
				setUpperBound((Integer)newValue);
				return;
			case SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__LABEL:
				setLabel((String)newValue);
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
			case SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__TYPE:
				setType((EClass)null);
				return;
			case SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__URI_REFERENCE:
				setUriReference(URI_REFERENCE_EDEFAULT);
				return;
			case SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__UPPER_BOUND:
				setUpperBound(UPPER_BOUND_EDEFAULT);
				return;
			case SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__LABEL:
				setLabel(LABEL_EDEFAULT);
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
			case SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__TYPE:
				return type != null;
			case SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__URI_REFERENCE:
				return URI_REFERENCE_EDEFAULT == null ? uriReference != null : !URI_REFERENCE_EDEFAULT.equals(uriReference);
			case SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__UPPER_BOUND:
				return upperBound != UPPER_BOUND_EDEFAULT;
			case SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__LOWER_BOUND:
				return lowerBound != LOWER_BOUND_EDEFAULT;
			case SymrefsPackage.EXTERNAL_ELEMENT_REFERENCE__LABEL:
				return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
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
		result.append(')');
		return result.toString();
	}

} //ExternalElementReferenceImpl
