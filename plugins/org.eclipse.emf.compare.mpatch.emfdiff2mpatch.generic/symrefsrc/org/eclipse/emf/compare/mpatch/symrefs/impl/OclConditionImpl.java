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
 * $Id: OclConditionImpl.java,v 1.1 2010/09/10 15:32:55 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.symrefs.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.symrefs.Condition;
import org.eclipse.emf.compare.mpatch.symrefs.ElementSetReference;
import org.eclipse.emf.compare.mpatch.symrefs.OclCondition;
import org.eclipse.emf.compare.mpatch.symrefs.SymrefsPackage;
import org.eclipse.emf.compare.mpatch.symrefs.util.OCLConditionHelper;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ocl Condition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.OclConditionImpl#getElementReference <em>Element Reference</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.OclConditionImpl#getExpression <em>Expression</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.OclConditionImpl#isCheckType <em>Check Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OclConditionImpl extends EObjectImpl implements OclCondition {
	/**
	 * The default value of the '{@link #getExpression() <em>Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExpression() <em>Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExpression()
	 * @generated
	 * @ordered
	 */
	protected String expression = EXPRESSION_EDEFAULT;

	/**
	 * The default value of the '{@link #isCheckType() <em>Check Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCheckType()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CHECK_TYPE_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isCheckType() <em>Check Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCheckType()
	 * @generated
	 * @ordered
	 */
	protected boolean checkType = CHECK_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OclConditionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SymrefsPackage.Literals.OCL_CONDITION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ElementSetReference getElementReference() {
		if (eContainerFeatureID() != SymrefsPackage.OCL_CONDITION__ELEMENT_REFERENCE) return null;
		return (ElementSetReference)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetElementReference(ElementSetReference newElementReference, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newElementReference, SymrefsPackage.OCL_CONDITION__ELEMENT_REFERENCE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setElementReference(ElementSetReference newElementReference) {
		if (newElementReference != eInternalContainer() || (eContainerFeatureID() != SymrefsPackage.OCL_CONDITION__ELEMENT_REFERENCE && newElementReference != null)) {
			if (EcoreUtil.isAncestor(this, newElementReference))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newElementReference != null)
				msgs = ((InternalEObject)newElementReference).eInverseAdd(this, SymrefsPackage.ELEMENT_SET_REFERENCE__CONDITIONS, ElementSetReference.class, msgs);
			msgs = basicSetElementReference(newElementReference, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SymrefsPackage.OCL_CONDITION__ELEMENT_REFERENCE, newElementReference, newElementReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExpression(String newExpression) {
		String oldExpression = expression;
		expression = newExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SymrefsPackage.OCL_CONDITION__EXPRESSION, oldExpression, expression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isCheckType() {
		return checkType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCheckType(boolean newCheckType) {
		boolean oldCheckType = checkType;
		checkType = newCheckType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SymrefsPackage.OCL_CONDITION__CHECK_TYPE, oldCheckType, checkType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<EObject> collectValidElements(EObject model) {
		final Collection<EObject> elements = OCLConditionHelper.collectValidElements(this, model);
		return new BasicEList<EObject>(elements);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean sameCondition(Condition other) {
		if (!(other instanceof OclCondition))
			return false;
		return getExpression().equals(((OclCondition)other).getExpression());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SymrefsPackage.OCL_CONDITION__ELEMENT_REFERENCE:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetElementReference((ElementSetReference)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SymrefsPackage.OCL_CONDITION__ELEMENT_REFERENCE:
				return basicSetElementReference(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case SymrefsPackage.OCL_CONDITION__ELEMENT_REFERENCE:
				return eInternalContainer().eInverseRemove(this, SymrefsPackage.ELEMENT_SET_REFERENCE__CONDITIONS, ElementSetReference.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SymrefsPackage.OCL_CONDITION__ELEMENT_REFERENCE:
				return getElementReference();
			case SymrefsPackage.OCL_CONDITION__EXPRESSION:
				return getExpression();
			case SymrefsPackage.OCL_CONDITION__CHECK_TYPE:
				return isCheckType();
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
			case SymrefsPackage.OCL_CONDITION__ELEMENT_REFERENCE:
				setElementReference((ElementSetReference)newValue);
				return;
			case SymrefsPackage.OCL_CONDITION__EXPRESSION:
				setExpression((String)newValue);
				return;
			case SymrefsPackage.OCL_CONDITION__CHECK_TYPE:
				setCheckType((Boolean)newValue);
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
			case SymrefsPackage.OCL_CONDITION__ELEMENT_REFERENCE:
				setElementReference((ElementSetReference)null);
				return;
			case SymrefsPackage.OCL_CONDITION__EXPRESSION:
				setExpression(EXPRESSION_EDEFAULT);
				return;
			case SymrefsPackage.OCL_CONDITION__CHECK_TYPE:
				setCheckType(CHECK_TYPE_EDEFAULT);
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
			case SymrefsPackage.OCL_CONDITION__ELEMENT_REFERENCE:
				return getElementReference() != null;
			case SymrefsPackage.OCL_CONDITION__EXPRESSION:
				return EXPRESSION_EDEFAULT == null ? expression != null : !EXPRESSION_EDEFAULT.equals(expression);
			case SymrefsPackage.OCL_CONDITION__CHECK_TYPE:
				return checkType != CHECK_TYPE_EDEFAULT;
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
		result.append(" (expression: ");
		result.append(expression);
		result.append(", checkType: ");
		result.append(checkType);
		result.append(')');
		return result.toString();
	}

} //OclConditionImpl
