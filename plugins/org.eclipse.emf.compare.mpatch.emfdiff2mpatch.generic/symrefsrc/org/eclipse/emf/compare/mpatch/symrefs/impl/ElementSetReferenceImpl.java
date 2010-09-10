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
 * $Id: ElementSetReferenceImpl.java,v 1.1 2010/09/10 15:32:55 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.symrefs.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.symrefs.Condition;
import org.eclipse.emf.compare.mpatch.symrefs.ElementSetReference;
import org.eclipse.emf.compare.mpatch.symrefs.SymrefsPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Element Set Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.ElementSetReferenceImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.ElementSetReferenceImpl#getUriReference <em>Uri Reference</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.ElementSetReferenceImpl#getUpperBound <em>Upper Bound</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.ElementSetReferenceImpl#getLowerBound <em>Lower Bound</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.ElementSetReferenceImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.ElementSetReferenceImpl#getConditions <em>Conditions</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.symrefs.impl.ElementSetReferenceImpl#getContext <em>Context</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ElementSetReferenceImpl extends EObjectImpl implements ElementSetReference {
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
	 * The cached value of the '{@link #getConditions() <em>Conditions</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConditions()
	 * @generated
	 * @ordered
	 */
	protected EList<Condition> conditions;

	/**
	 * The cached value of the '{@link #getContext() <em>Context</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContext()
	 * @generated
	 * @ordered
	 */
	protected IElementReference context;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ElementSetReferenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SymrefsPackage.Literals.ELEMENT_SET_REFERENCE;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SymrefsPackage.ELEMENT_SET_REFERENCE__TYPE, oldType, type));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SymrefsPackage.ELEMENT_SET_REFERENCE__TYPE, oldType, type));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SymrefsPackage.ELEMENT_SET_REFERENCE__URI_REFERENCE, oldUriReference, uriReference));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SymrefsPackage.ELEMENT_SET_REFERENCE__UPPER_BOUND, oldUpperBound, upperBound));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SymrefsPackage.ELEMENT_SET_REFERENCE__LABEL, oldLabel, label));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IElementReference getContext() {
		return context;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetContext(IElementReference newContext, NotificationChain msgs) {
		IElementReference oldContext = context;
		context = newContext;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SymrefsPackage.ELEMENT_SET_REFERENCE__CONTEXT, oldContext, newContext);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContext(IElementReference newContext) {
		if (newContext != context) {
			NotificationChain msgs = null;
			if (context != null)
				msgs = ((InternalEObject)context).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SymrefsPackage.ELEMENT_SET_REFERENCE__CONTEXT, null, msgs);
			if (newContext != null)
				msgs = ((InternalEObject)newContext).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SymrefsPackage.ELEMENT_SET_REFERENCE__CONTEXT, null, msgs);
			msgs = basicSetContext(newContext, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SymrefsPackage.ELEMENT_SET_REFERENCE__CONTEXT, newContext, newContext));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Condition> getConditions() {
		if (conditions == null) {
			conditions = new EObjectContainmentWithInverseEList<Condition>(Condition.class, this, SymrefsPackage.ELEMENT_SET_REFERENCE__CONDITIONS, SymrefsPackage.CONDITION__ELEMENT_REFERENCE);
		}
		return conditions;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EList<EObject> resolve(EObject model) {
		final Set<EObject> context;
		final Set<EObject> result;

		// resolve the context, if it exists
		if (getContext() != null) {
			context = new HashSet<EObject>(getContext().resolve(model)); // remove duplicates ;-)
			result = new HashSet<EObject>();

			if (context.isEmpty()) {

				// maybe the context is exactly the parent element?! so lets check this particular case
				if (model.eContainer() != null) {
					final EList<EObject> potentialContext = getContext().resolve(model.eContainer());
					if (potentialContext.contains(model.eContainer())) {

						// ok, the context is exactly the parent, so it is safe to resolve on the current model element
						result.addAll(resolveConditions(model));
					}
				}
			} else {

				// collect result for all context elements
				for (EObject contextElement : context) {
					result.addAll(resolveConditions(contextElement));
				}
				// however, the context itself should not be counted!
				result.removeAll(context);
			}
		} else {
			result = new HashSet<EObject>(resolveConditions(model));
		}
		return new BasicEList<EObject>(result);
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
		if (!(other instanceof ElementSetReference))
			return false; // wrong type (references are not comparable)
		if (!getType().getInstanceClassName().equals(other.getType().getInstanceClassName()))
			return false; // wrong target type
		if (getLowerBound() != other.getLowerBound() || getUpperBound() != other.getUpperBound())
			return false; // different cardinality
		
		// actual comparison: conditions
		if (getConditions().size() != ((ElementSetReference)other).getConditions().size())
			return false;
		own: for (Condition c1 : getConditions()) {
			for (Condition c2 : ((ElementSetReference)other).getConditions()) {
				if (c1.sameCondition(c2)) {
					continue own;
				}
				return false;
			}
		}
		
		// context?
		if (getContext() != null) {
			if (((ElementSetReference)other).getContext() == null)
				return false;
			if (!((ElementSetReference)other).getContext().resolvesEqual(getContext()))
				return false;
		}
		
		return true;
	}

	private List<EObject> resolveConditions(EObject model) {
		// create the intersection of all elements of all conditions
		// this assumes that _all_ conditions must hold for the collected elements
		List<EObject> result = null;
		for (final Condition condition : getConditions()) {
			final EList<EObject> tmpResult = condition.collectValidElements(model);
			if (result == null) {
				result = new ArrayList<EObject>(tmpResult);
			} else {
				result.retainAll(tmpResult);
			}
			
			// we definitely have to have some result here, otherwise we cannot retain anything!
			// so if result is empty here, lets abort to improve performance!
			if (result.isEmpty())
				break;
		}
		return result == null ? Collections.<EObject> emptyList() : result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SymrefsPackage.ELEMENT_SET_REFERENCE__CONDITIONS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getConditions()).basicAdd(otherEnd, msgs);
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
			case SymrefsPackage.ELEMENT_SET_REFERENCE__CONDITIONS:
				return ((InternalEList<?>)getConditions()).basicRemove(otherEnd, msgs);
			case SymrefsPackage.ELEMENT_SET_REFERENCE__CONTEXT:
				return basicSetContext(null, msgs);
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
			case SymrefsPackage.ELEMENT_SET_REFERENCE__TYPE:
				if (resolve) return getType();
				return basicGetType();
			case SymrefsPackage.ELEMENT_SET_REFERENCE__URI_REFERENCE:
				return getUriReference();
			case SymrefsPackage.ELEMENT_SET_REFERENCE__UPPER_BOUND:
				return getUpperBound();
			case SymrefsPackage.ELEMENT_SET_REFERENCE__LOWER_BOUND:
				return getLowerBound();
			case SymrefsPackage.ELEMENT_SET_REFERENCE__LABEL:
				return getLabel();
			case SymrefsPackage.ELEMENT_SET_REFERENCE__CONDITIONS:
				return getConditions();
			case SymrefsPackage.ELEMENT_SET_REFERENCE__CONTEXT:
				return getContext();
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
			case SymrefsPackage.ELEMENT_SET_REFERENCE__TYPE:
				setType((EClass)newValue);
				return;
			case SymrefsPackage.ELEMENT_SET_REFERENCE__URI_REFERENCE:
				setUriReference((String)newValue);
				return;
			case SymrefsPackage.ELEMENT_SET_REFERENCE__UPPER_BOUND:
				setUpperBound((Integer)newValue);
				return;
			case SymrefsPackage.ELEMENT_SET_REFERENCE__LABEL:
				setLabel((String)newValue);
				return;
			case SymrefsPackage.ELEMENT_SET_REFERENCE__CONDITIONS:
				getConditions().clear();
				getConditions().addAll((Collection<? extends Condition>)newValue);
				return;
			case SymrefsPackage.ELEMENT_SET_REFERENCE__CONTEXT:
				setContext((IElementReference)newValue);
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
			case SymrefsPackage.ELEMENT_SET_REFERENCE__TYPE:
				setType((EClass)null);
				return;
			case SymrefsPackage.ELEMENT_SET_REFERENCE__URI_REFERENCE:
				setUriReference(URI_REFERENCE_EDEFAULT);
				return;
			case SymrefsPackage.ELEMENT_SET_REFERENCE__UPPER_BOUND:
				setUpperBound(UPPER_BOUND_EDEFAULT);
				return;
			case SymrefsPackage.ELEMENT_SET_REFERENCE__LABEL:
				setLabel(LABEL_EDEFAULT);
				return;
			case SymrefsPackage.ELEMENT_SET_REFERENCE__CONDITIONS:
				getConditions().clear();
				return;
			case SymrefsPackage.ELEMENT_SET_REFERENCE__CONTEXT:
				setContext((IElementReference)null);
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
			case SymrefsPackage.ELEMENT_SET_REFERENCE__TYPE:
				return type != null;
			case SymrefsPackage.ELEMENT_SET_REFERENCE__URI_REFERENCE:
				return URI_REFERENCE_EDEFAULT == null ? uriReference != null : !URI_REFERENCE_EDEFAULT.equals(uriReference);
			case SymrefsPackage.ELEMENT_SET_REFERENCE__UPPER_BOUND:
				return upperBound != UPPER_BOUND_EDEFAULT;
			case SymrefsPackage.ELEMENT_SET_REFERENCE__LOWER_BOUND:
				return lowerBound != LOWER_BOUND_EDEFAULT;
			case SymrefsPackage.ELEMENT_SET_REFERENCE__LABEL:
				return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
			case SymrefsPackage.ELEMENT_SET_REFERENCE__CONDITIONS:
				return conditions != null && !conditions.isEmpty();
			case SymrefsPackage.ELEMENT_SET_REFERENCE__CONTEXT:
				return context != null;
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

} //ElementSetReferenceImpl
