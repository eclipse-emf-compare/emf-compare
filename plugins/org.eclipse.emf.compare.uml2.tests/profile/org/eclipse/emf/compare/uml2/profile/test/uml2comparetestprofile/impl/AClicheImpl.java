/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>ACliche</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.AClicheImpl#getSingleValuedAttribute <em>Single Valued Attribute</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.AClicheImpl#getManyValuedAttribute <em>Many Valued Attribute</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.AClicheImpl#getSingleValuedReference <em>Single Valued Reference</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.AClicheImpl#getManyValuedReference <em>Many Valued Reference</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.impl.AClicheImpl#getBase_Class <em>Base Class</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AClicheImpl extends EObjectImpl implements ACliche {
	/**
	 * The default value of the '{@link #getSingleValuedAttribute() <em>Single Valued Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSingleValuedAttribute()
	 * @generated
	 * @ordered
	 */
	protected static final String SINGLE_VALUED_ATTRIBUTE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSingleValuedAttribute() <em>Single Valued Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSingleValuedAttribute()
	 * @generated
	 * @ordered
	 */
	protected String singleValuedAttribute = SINGLE_VALUED_ATTRIBUTE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getManyValuedAttribute() <em>Many Valued Attribute</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getManyValuedAttribute()
	 * @generated
	 * @ordered
	 */
	protected EList<String> manyValuedAttribute;

	/**
	 * The cached value of the '{@link #getSingleValuedReference() <em>Single Valued Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSingleValuedReference()
	 * @generated
	 * @ordered
	 */
	protected org.eclipse.uml2.uml.Class singleValuedReference;

	/**
	 * The cached value of the '{@link #getManyValuedReference() <em>Many Valued Reference</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getManyValuedReference()
	 * @generated
	 * @ordered
	 */
	protected EList<org.eclipse.uml2.uml.Class> manyValuedReference;

	/**
	 * The cached value of the '{@link #getBase_Class() <em>Base Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBase_Class()
	 * @generated
	 * @ordered
	 */
	protected org.eclipse.uml2.uml.Class base_Class;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AClicheImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UML2CompareTestProfilePackage.Literals.ACLICHE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSingleValuedAttribute() {
		return singleValuedAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSingleValuedAttribute(String newSingleValuedAttribute) {
		String oldSingleValuedAttribute = singleValuedAttribute;
		singleValuedAttribute = newSingleValuedAttribute;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UML2CompareTestProfilePackage.ACLICHE__SINGLE_VALUED_ATTRIBUTE, oldSingleValuedAttribute, singleValuedAttribute));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getManyValuedAttribute() {
		if (manyValuedAttribute == null) {
			manyValuedAttribute = new EDataTypeUniqueEList<String>(String.class, this, UML2CompareTestProfilePackage.ACLICHE__MANY_VALUED_ATTRIBUTE);
		}
		return manyValuedAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public org.eclipse.uml2.uml.Class getSingleValuedReference() {
		if (singleValuedReference != null && singleValuedReference.eIsProxy()) {
			InternalEObject oldSingleValuedReference = (InternalEObject)singleValuedReference;
			singleValuedReference = (org.eclipse.uml2.uml.Class)eResolveProxy(oldSingleValuedReference);
			if (singleValuedReference != oldSingleValuedReference) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, UML2CompareTestProfilePackage.ACLICHE__SINGLE_VALUED_REFERENCE, oldSingleValuedReference, singleValuedReference));
			}
		}
		return singleValuedReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public org.eclipse.uml2.uml.Class basicGetSingleValuedReference() {
		return singleValuedReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSingleValuedReference(org.eclipse.uml2.uml.Class newSingleValuedReference) {
		org.eclipse.uml2.uml.Class oldSingleValuedReference = singleValuedReference;
		singleValuedReference = newSingleValuedReference;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UML2CompareTestProfilePackage.ACLICHE__SINGLE_VALUED_REFERENCE, oldSingleValuedReference, singleValuedReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<org.eclipse.uml2.uml.Class> getManyValuedReference() {
		if (manyValuedReference == null) {
			manyValuedReference = new EObjectResolvingEList<org.eclipse.uml2.uml.Class>(org.eclipse.uml2.uml.Class.class, this, UML2CompareTestProfilePackage.ACLICHE__MANY_VALUED_REFERENCE);
		}
		return manyValuedReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public org.eclipse.uml2.uml.Class getManyValuedReference(String name) {
		return getManyValuedReference(name, false, null);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public org.eclipse.uml2.uml.Class getManyValuedReference(String name, boolean ignoreCase, EClass eClass) {
		manyValuedReferenceLoop: for (org.eclipse.uml2.uml.Class manyValuedReference : getManyValuedReference()) {
			if (eClass != null && !eClass.isInstance(manyValuedReference))
				continue manyValuedReferenceLoop;
			if (name != null && !(ignoreCase ? name.equalsIgnoreCase(manyValuedReference.getName()) : name.equals(manyValuedReference.getName())))
				continue manyValuedReferenceLoop;
			return manyValuedReference;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public org.eclipse.uml2.uml.Class getBase_Class() {
		if (base_Class != null && base_Class.eIsProxy()) {
			InternalEObject oldBase_Class = (InternalEObject)base_Class;
			base_Class = (org.eclipse.uml2.uml.Class)eResolveProxy(oldBase_Class);
			if (base_Class != oldBase_Class) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, UML2CompareTestProfilePackage.ACLICHE__BASE_CLASS, oldBase_Class, base_Class));
			}
		}
		return base_Class;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public org.eclipse.uml2.uml.Class basicGetBase_Class() {
		return base_Class;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBase_Class(org.eclipse.uml2.uml.Class newBase_Class) {
		org.eclipse.uml2.uml.Class oldBase_Class = base_Class;
		base_Class = newBase_Class;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UML2CompareTestProfilePackage.ACLICHE__BASE_CLASS, oldBase_Class, base_Class));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case UML2CompareTestProfilePackage.ACLICHE__SINGLE_VALUED_ATTRIBUTE:
				return getSingleValuedAttribute();
			case UML2CompareTestProfilePackage.ACLICHE__MANY_VALUED_ATTRIBUTE:
				return getManyValuedAttribute();
			case UML2CompareTestProfilePackage.ACLICHE__SINGLE_VALUED_REFERENCE:
				if (resolve) return getSingleValuedReference();
				return basicGetSingleValuedReference();
			case UML2CompareTestProfilePackage.ACLICHE__MANY_VALUED_REFERENCE:
				return getManyValuedReference();
			case UML2CompareTestProfilePackage.ACLICHE__BASE_CLASS:
				if (resolve) return getBase_Class();
				return basicGetBase_Class();
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
			case UML2CompareTestProfilePackage.ACLICHE__SINGLE_VALUED_ATTRIBUTE:
				setSingleValuedAttribute((String)newValue);
				return;
			case UML2CompareTestProfilePackage.ACLICHE__MANY_VALUED_ATTRIBUTE:
				getManyValuedAttribute().clear();
				getManyValuedAttribute().addAll((Collection<? extends String>)newValue);
				return;
			case UML2CompareTestProfilePackage.ACLICHE__SINGLE_VALUED_REFERENCE:
				setSingleValuedReference((org.eclipse.uml2.uml.Class)newValue);
				return;
			case UML2CompareTestProfilePackage.ACLICHE__MANY_VALUED_REFERENCE:
				getManyValuedReference().clear();
				getManyValuedReference().addAll((Collection<? extends org.eclipse.uml2.uml.Class>)newValue);
				return;
			case UML2CompareTestProfilePackage.ACLICHE__BASE_CLASS:
				setBase_Class((org.eclipse.uml2.uml.Class)newValue);
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
			case UML2CompareTestProfilePackage.ACLICHE__SINGLE_VALUED_ATTRIBUTE:
				setSingleValuedAttribute(SINGLE_VALUED_ATTRIBUTE_EDEFAULT);
				return;
			case UML2CompareTestProfilePackage.ACLICHE__MANY_VALUED_ATTRIBUTE:
				getManyValuedAttribute().clear();
				return;
			case UML2CompareTestProfilePackage.ACLICHE__SINGLE_VALUED_REFERENCE:
				setSingleValuedReference((org.eclipse.uml2.uml.Class)null);
				return;
			case UML2CompareTestProfilePackage.ACLICHE__MANY_VALUED_REFERENCE:
				getManyValuedReference().clear();
				return;
			case UML2CompareTestProfilePackage.ACLICHE__BASE_CLASS:
				setBase_Class((org.eclipse.uml2.uml.Class)null);
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
			case UML2CompareTestProfilePackage.ACLICHE__SINGLE_VALUED_ATTRIBUTE:
				return SINGLE_VALUED_ATTRIBUTE_EDEFAULT == null ? singleValuedAttribute != null : !SINGLE_VALUED_ATTRIBUTE_EDEFAULT.equals(singleValuedAttribute);
			case UML2CompareTestProfilePackage.ACLICHE__MANY_VALUED_ATTRIBUTE:
				return manyValuedAttribute != null && !manyValuedAttribute.isEmpty();
			case UML2CompareTestProfilePackage.ACLICHE__SINGLE_VALUED_REFERENCE:
				return singleValuedReference != null;
			case UML2CompareTestProfilePackage.ACLICHE__MANY_VALUED_REFERENCE:
				return manyValuedReference != null && !manyValuedReference.isEmpty();
			case UML2CompareTestProfilePackage.ACLICHE__BASE_CLASS:
				return base_Class != null;
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
		result.append(" (singleValuedAttribute: ");
		result.append(singleValuedAttribute);
		result.append(", manyValuedAttribute: ");
		result.append(manyValuedAttribute);
		result.append(')');
		return result.toString();
	}

} //AClicheImpl
