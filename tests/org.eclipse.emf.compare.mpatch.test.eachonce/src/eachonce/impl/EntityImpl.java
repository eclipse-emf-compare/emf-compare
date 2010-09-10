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
 * $Id: EntityImpl.java,v 1.1 2010/09/10 15:40:34 cbrun Exp $
 *******************************************************************************/
package eachonce.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import eachonce.EachoncePackage;
import eachonce.Entity;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Entity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eachonce.impl.EntityImpl#getSingleAttribute <em>Single Attribute</em>}</li>
 *   <li>{@link eachonce.impl.EntityImpl#getMultiAttribute <em>Multi Attribute</em>}</li>
 *   <li>{@link eachonce.impl.EntityImpl#getSingleReference <em>Single Reference</em>}</li>
 *   <li>{@link eachonce.impl.EntityImpl#getMultiReference <em>Multi Reference</em>}</li>
 *   <li>{@link eachonce.impl.EntityImpl#getChildren <em>Children</em>}</li>
 *   <li>{@link eachonce.impl.EntityImpl#getAdditionalAttribute <em>Additional Attribute</em>}</li>
 *   <li>{@link eachonce.impl.EntityImpl#getChildren2 <em>Children2</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EntityImpl extends EObjectImpl implements Entity {
	/**
	 * The default value of the '{@link #getSingleAttribute() <em>Single Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSingleAttribute()
	 * @generated
	 * @ordered
	 */
	protected static final String SINGLE_ATTRIBUTE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSingleAttribute() <em>Single Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSingleAttribute()
	 * @generated
	 * @ordered
	 */
	protected String singleAttribute = SINGLE_ATTRIBUTE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMultiAttribute() <em>Multi Attribute</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultiAttribute()
	 * @generated
	 * @ordered
	 */
	protected EList<String> multiAttribute;

	/**
	 * The cached value of the '{@link #getSingleReference() <em>Single Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSingleReference()
	 * @generated
	 * @ordered
	 */
	protected Entity singleReference;

	/**
	 * The cached value of the '{@link #getMultiReference() <em>Multi Reference</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultiReference()
	 * @generated
	 * @ordered
	 */
	protected EList<Entity> multiReference;

	/**
	 * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildren()
	 * @generated
	 * @ordered
	 */
	protected EList<Entity> children;

	/**
	 * The default value of the '{@link #getAdditionalAttribute() <em>Additional Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdditionalAttribute()
	 * @generated
	 * @ordered
	 */
	protected static final String ADDITIONAL_ATTRIBUTE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAdditionalAttribute() <em>Additional Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdditionalAttribute()
	 * @generated
	 * @ordered
	 */
	protected String additionalAttribute = ADDITIONAL_ATTRIBUTE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getChildren2() <em>Children2</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildren2()
	 * @generated
	 * @ordered
	 */
	protected EList<Entity> children2;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EntityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EachoncePackage.Literals.ENTITY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSingleAttribute() {
		return singleAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSingleAttribute(String newSingleAttribute) {
		String oldSingleAttribute = singleAttribute;
		singleAttribute = newSingleAttribute;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EachoncePackage.ENTITY__SINGLE_ATTRIBUTE, oldSingleAttribute, singleAttribute));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getMultiAttribute() {
		if (multiAttribute == null) {
			multiAttribute = new EDataTypeUniqueEList<String>(String.class, this, EachoncePackage.ENTITY__MULTI_ATTRIBUTE);
		}
		return multiAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Entity getSingleReference() {
		if (singleReference != null && singleReference.eIsProxy()) {
			InternalEObject oldSingleReference = (InternalEObject)singleReference;
			singleReference = (Entity)eResolveProxy(oldSingleReference);
			if (singleReference != oldSingleReference) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, EachoncePackage.ENTITY__SINGLE_REFERENCE, oldSingleReference, singleReference));
			}
		}
		return singleReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Entity basicGetSingleReference() {
		return singleReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSingleReference(Entity newSingleReference) {
		Entity oldSingleReference = singleReference;
		singleReference = newSingleReference;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EachoncePackage.ENTITY__SINGLE_REFERENCE, oldSingleReference, singleReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Entity> getMultiReference() {
		if (multiReference == null) {
			multiReference = new EObjectResolvingEList<Entity>(Entity.class, this, EachoncePackage.ENTITY__MULTI_REFERENCE);
		}
		return multiReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Entity> getChildren() {
		if (children == null) {
			children = new EObjectContainmentEList<Entity>(Entity.class, this, EachoncePackage.ENTITY__CHILDREN);
		}
		return children;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAdditionalAttribute() {
		return additionalAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAdditionalAttribute(String newAdditionalAttribute) {
		String oldAdditionalAttribute = additionalAttribute;
		additionalAttribute = newAdditionalAttribute;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EachoncePackage.ENTITY__ADDITIONAL_ATTRIBUTE, oldAdditionalAttribute, additionalAttribute));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Entity> getChildren2() {
		if (children2 == null) {
			children2 = new EObjectContainmentEList<Entity>(Entity.class, this, EachoncePackage.ENTITY__CHILDREN2);
		}
		return children2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EachoncePackage.ENTITY__CHILDREN:
				return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
			case EachoncePackage.ENTITY__CHILDREN2:
				return ((InternalEList<?>)getChildren2()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EachoncePackage.ENTITY__SINGLE_ATTRIBUTE:
				return getSingleAttribute();
			case EachoncePackage.ENTITY__MULTI_ATTRIBUTE:
				return getMultiAttribute();
			case EachoncePackage.ENTITY__SINGLE_REFERENCE:
				if (resolve) return getSingleReference();
				return basicGetSingleReference();
			case EachoncePackage.ENTITY__MULTI_REFERENCE:
				return getMultiReference();
			case EachoncePackage.ENTITY__CHILDREN:
				return getChildren();
			case EachoncePackage.ENTITY__ADDITIONAL_ATTRIBUTE:
				return getAdditionalAttribute();
			case EachoncePackage.ENTITY__CHILDREN2:
				return getChildren2();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case EachoncePackage.ENTITY__SINGLE_ATTRIBUTE:
				setSingleAttribute((String)newValue);
				return;
			case EachoncePackage.ENTITY__MULTI_ATTRIBUTE:
				getMultiAttribute().clear();
				getMultiAttribute().addAll((Collection<? extends String>)newValue);
				return;
			case EachoncePackage.ENTITY__SINGLE_REFERENCE:
				setSingleReference((Entity)newValue);
				return;
			case EachoncePackage.ENTITY__MULTI_REFERENCE:
				getMultiReference().clear();
				getMultiReference().addAll((Collection<? extends Entity>)newValue);
				return;
			case EachoncePackage.ENTITY__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection<? extends Entity>)newValue);
				return;
			case EachoncePackage.ENTITY__ADDITIONAL_ATTRIBUTE:
				setAdditionalAttribute((String)newValue);
				return;
			case EachoncePackage.ENTITY__CHILDREN2:
				getChildren2().clear();
				getChildren2().addAll((Collection<? extends Entity>)newValue);
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
			case EachoncePackage.ENTITY__SINGLE_ATTRIBUTE:
				setSingleAttribute(SINGLE_ATTRIBUTE_EDEFAULT);
				return;
			case EachoncePackage.ENTITY__MULTI_ATTRIBUTE:
				getMultiAttribute().clear();
				return;
			case EachoncePackage.ENTITY__SINGLE_REFERENCE:
				setSingleReference((Entity)null);
				return;
			case EachoncePackage.ENTITY__MULTI_REFERENCE:
				getMultiReference().clear();
				return;
			case EachoncePackage.ENTITY__CHILDREN:
				getChildren().clear();
				return;
			case EachoncePackage.ENTITY__ADDITIONAL_ATTRIBUTE:
				setAdditionalAttribute(ADDITIONAL_ATTRIBUTE_EDEFAULT);
				return;
			case EachoncePackage.ENTITY__CHILDREN2:
				getChildren2().clear();
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
			case EachoncePackage.ENTITY__SINGLE_ATTRIBUTE:
				return SINGLE_ATTRIBUTE_EDEFAULT == null ? singleAttribute != null : !SINGLE_ATTRIBUTE_EDEFAULT.equals(singleAttribute);
			case EachoncePackage.ENTITY__MULTI_ATTRIBUTE:
				return multiAttribute != null && !multiAttribute.isEmpty();
			case EachoncePackage.ENTITY__SINGLE_REFERENCE:
				return singleReference != null;
			case EachoncePackage.ENTITY__MULTI_REFERENCE:
				return multiReference != null && !multiReference.isEmpty();
			case EachoncePackage.ENTITY__CHILDREN:
				return children != null && !children.isEmpty();
			case EachoncePackage.ENTITY__ADDITIONAL_ATTRIBUTE:
				return ADDITIONAL_ATTRIBUTE_EDEFAULT == null ? additionalAttribute != null : !ADDITIONAL_ATTRIBUTE_EDEFAULT.equals(additionalAttribute);
			case EachoncePackage.ENTITY__CHILDREN2:
				return children2 != null && !children2.isEmpty();
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
		result.append(" (singleAttribute: ");
		result.append(singleAttribute);
		result.append(", multiAttribute: ");
		result.append(multiAttribute);
		result.append(", additionalAttribute: ");
		result.append(additionalAttribute);
		result.append(')');
		return result.toString();
	}

} //EntityImpl
