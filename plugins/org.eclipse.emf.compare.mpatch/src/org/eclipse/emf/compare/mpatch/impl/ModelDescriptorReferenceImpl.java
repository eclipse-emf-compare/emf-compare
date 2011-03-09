/**
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: ModelDescriptorReferenceImpl.java,v 1.1 2010/09/10 15:23:07 cbrun Exp $
 */
package org.eclipse.emf.compare.mpatch.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.ModelDescriptorReference;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model Descriptor Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.ModelDescriptorReferenceImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.ModelDescriptorReferenceImpl#getUriReference <em>Uri Reference</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.ModelDescriptorReferenceImpl#getUpperBound <em>Upper Bound</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.ModelDescriptorReferenceImpl#getLowerBound <em>Lower Bound</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.ModelDescriptorReferenceImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.impl.ModelDescriptorReferenceImpl#getResolvesTo <em>Resolves To</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModelDescriptorReferenceImpl extends EObjectImpl implements ModelDescriptorReference {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2010, 2011 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";

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
	 * The cached value of the '{@link #getResolvesTo() <em>Resolves To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResolvesTo()
	 * @generated
	 * @ordered
	 */
	protected IModelDescriptor resolvesTo;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModelDescriptorReferenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MPatchPackage.Literals.MODEL_DESCRIPTOR_REFERENCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EClass getType() {
		if (getResolvesTo() == null)
			return null;
		return getResolvesTo().getType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EClass basicGetType() {
		return getType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void setType(EClass newType) {
		throw new UnsupportedOperationException("This is a derived property!");
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getUriReference() {
		if (getResolvesTo() == null)
			return null;
		return getResolvesTo().getSelfReference().getUriReference();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void setUriReference(String newUriReference) {
		throw new UnsupportedOperationException("This is a derived property!");
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
			eNotify(new ENotificationImpl(this, Notification.SET, MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__UPPER_BOUND, oldUpperBound, upperBound));
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
	 * @generated NOT
	 */
	public String getLabel() {
		final IModelDescriptor descriptor = getResolvesTo();
		if (descriptor == null)
			return "(descriptor not set!)";
//		final String info = " (contains " + descriptor.getAllCrossReferences().size() + " cross-reference" + (descriptor.getAllCrossReferences().size() == 1 ? ")" : "s)");
		return "Descriptor";
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void setLabel(String newLabel) {
		throw new UnsupportedOperationException("This is a derived property!");
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IModelDescriptor getResolvesTo() {
		if (resolvesTo != null && resolvesTo.eIsProxy()) {
			InternalEObject oldResolvesTo = (InternalEObject)resolvesTo;
			resolvesTo = (IModelDescriptor)eResolveProxy(oldResolvesTo);
			if (resolvesTo != oldResolvesTo) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__RESOLVES_TO, oldResolvesTo, resolvesTo));
			}
		}
		return resolvesTo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IModelDescriptor basicGetResolvesTo() {
		return resolvesTo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void setResolvesTo(IModelDescriptor newResolvesTo) {
		final IModelDescriptor oldResolvesTo = resolvesTo;
		resolvesTo = newResolvesTo;
		final EClass oldType = oldResolvesTo == null ? null : oldResolvesTo.getType();
		final EClass newType = resolvesTo == null ? null : resolvesTo.getType();
		final String oldUriReference = oldResolvesTo == null ? null : oldResolvesTo.getSelfReference().getUriReference();
		final String newUriReference = resolvesTo == null ? null : resolvesTo.getSelfReference().getUriReference();
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__RESOLVES_TO, oldResolvesTo, resolvesTo));
			eNotify(new ENotificationImpl(this, Notification.SET, MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__TYPE, oldType, newType));
			eNotify(new ENotificationImpl(this, Notification.SET, MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__URI_REFERENCE, oldUriReference, newUriReference));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<EObject> resolve(EObject model) {
		// delegate resolution to the symbolic reference of the model descriptor
		return getResolvesTo().getSelfReference().resolve(model);
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
		if (!(other instanceof ModelDescriptorReference))
			return false; // wrong type (references are not comparable)
		if (!getType().getInstanceClassName().equals(other.getType().getInstanceClassName()))
			return false; // wrong target type
		if (getLowerBound() != other.getLowerBound() || getUpperBound() != other.getUpperBound())
			return false; // different cardinality
		if (getResolvesTo() == null)
			return false; // we cannot compare if model descriptor is not set
		
		// actual comparison: are the model descritors equal?
		return getResolvesTo().equals(((ModelDescriptorReference)other).getResolvesTo());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__TYPE:
				if (resolve) return getType();
				return basicGetType();
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__URI_REFERENCE:
				return getUriReference();
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__UPPER_BOUND:
				return getUpperBound();
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__LOWER_BOUND:
				return getLowerBound();
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__LABEL:
				return getLabel();
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__RESOLVES_TO:
				if (resolve) return getResolvesTo();
				return basicGetResolvesTo();
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
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__TYPE:
				setType((EClass)newValue);
				return;
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__URI_REFERENCE:
				setUriReference((String)newValue);
				return;
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__UPPER_BOUND:
				setUpperBound((Integer)newValue);
				return;
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__LABEL:
				setLabel((String)newValue);
				return;
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__RESOLVES_TO:
				setResolvesTo((IModelDescriptor)newValue);
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
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__TYPE:
				setType((EClass)null);
				return;
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__URI_REFERENCE:
				setUriReference(URI_REFERENCE_EDEFAULT);
				return;
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__UPPER_BOUND:
				setUpperBound(UPPER_BOUND_EDEFAULT);
				return;
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__LABEL:
				setLabel(LABEL_EDEFAULT);
				return;
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__RESOLVES_TO:
				setResolvesTo((IModelDescriptor)null);
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
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__TYPE:
				return type != null;
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__URI_REFERENCE:
				return URI_REFERENCE_EDEFAULT == null ? uriReference != null : !URI_REFERENCE_EDEFAULT.equals(uriReference);
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__UPPER_BOUND:
				return upperBound != UPPER_BOUND_EDEFAULT;
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__LOWER_BOUND:
				return lowerBound != LOWER_BOUND_EDEFAULT;
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__LABEL:
				return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE__RESOLVES_TO:
				return resolvesTo != null;
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

} //ModelDescriptorReferenceImpl
