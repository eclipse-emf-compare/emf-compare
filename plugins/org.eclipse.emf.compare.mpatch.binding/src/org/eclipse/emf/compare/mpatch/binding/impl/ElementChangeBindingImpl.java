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
 * $Id: ElementChangeBindingImpl.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.binding.BindingPackage;
import org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.Note;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Element Change Binding</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.ElementChangeBindingImpl#getNotes <em>Notes</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.ElementChangeBindingImpl#getModelElement <em>Model Element</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.ElementChangeBindingImpl#isIgnore <em>Ignore</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.ElementChangeBindingImpl#getElementReference <em>Element Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ElementChangeBindingImpl extends EObjectImpl implements ElementChangeBinding {
	/**
	 * The cached value of the '{@link #getNotes() <em>Notes</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotes()
	 * @generated
	 * @ordered
	 */
	protected EList<Note> notes;
	/**
	 * The cached value of the '{@link #getModelElement() <em>Model Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelElement()
	 * @generated
	 * @ordered
	 */
	protected EObject modelElement;
	/**
	 * The default value of the '{@link #isIgnore() <em>Ignore</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIgnore()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IGNORE_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isIgnore() <em>Ignore</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIgnore()
	 * @generated
	 * @ordered
	 */
	protected boolean ignore = IGNORE_EDEFAULT;
	/**
	 * The cached value of the '{@link #getElementReference() <em>Element Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElementReference()
	 * @generated
	 * @ordered
	 */
	protected IElementReference elementReference;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ElementChangeBindingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BindingPackage.Literals.ELEMENT_CHANGE_BINDING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Note> getNotes() {
		if (notes == null) {
			notes = new EObjectResolvingEList<Note>(Note.class, this, BindingPackage.ELEMENT_CHANGE_BINDING__NOTES);
		}
		return notes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getModelElement() {
		if (modelElement != null && modelElement.eIsProxy()) {
			InternalEObject oldModelElement = (InternalEObject)modelElement;
			modelElement = eResolveProxy(oldModelElement);
			if (modelElement != oldModelElement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BindingPackage.ELEMENT_CHANGE_BINDING__MODEL_ELEMENT, oldModelElement, modelElement));
			}
		}
		return modelElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetModelElement() {
		return modelElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModelElement(EObject newModelElement) {
		EObject oldModelElement = modelElement;
		modelElement = newModelElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BindingPackage.ELEMENT_CHANGE_BINDING__MODEL_ELEMENT, oldModelElement, modelElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIgnore() {
		return ignore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIgnore(boolean newIgnore) {
		boolean oldIgnore = ignore;
		ignore = newIgnore;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BindingPackage.ELEMENT_CHANGE_BINDING__IGNORE, oldIgnore, ignore));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IElementReference getElementReference() {
		if (elementReference != null && elementReference.eIsProxy()) {
			InternalEObject oldElementReference = (InternalEObject)elementReference;
			elementReference = (IElementReference)eResolveProxy(oldElementReference);
			if (elementReference != oldElementReference) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BindingPackage.ELEMENT_CHANGE_BINDING__ELEMENT_REFERENCE, oldElementReference, elementReference));
			}
		}
		return elementReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IElementReference basicGetElementReference() {
		return elementReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setElementReference(IElementReference newElementReference) {
		IElementReference oldElementReference = elementReference;
		elementReference = newElementReference;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BindingPackage.ELEMENT_CHANGE_BINDING__ELEMENT_REFERENCE, oldElementReference, elementReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNote() {
				String note = "";
				for (Note aNote : getNotes())
					note += (note.length() > 0 ? "\n" : "") + aNote.getNote();
				return note;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case BindingPackage.ELEMENT_CHANGE_BINDING__NOTES:
				return getNotes();
			case BindingPackage.ELEMENT_CHANGE_BINDING__MODEL_ELEMENT:
				if (resolve) return getModelElement();
				return basicGetModelElement();
			case BindingPackage.ELEMENT_CHANGE_BINDING__IGNORE:
				return isIgnore();
			case BindingPackage.ELEMENT_CHANGE_BINDING__ELEMENT_REFERENCE:
				if (resolve) return getElementReference();
				return basicGetElementReference();
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
			case BindingPackage.ELEMENT_CHANGE_BINDING__NOTES:
				getNotes().clear();
				getNotes().addAll((Collection<? extends Note>)newValue);
				return;
			case BindingPackage.ELEMENT_CHANGE_BINDING__MODEL_ELEMENT:
				setModelElement((EObject)newValue);
				return;
			case BindingPackage.ELEMENT_CHANGE_BINDING__IGNORE:
				setIgnore((Boolean)newValue);
				return;
			case BindingPackage.ELEMENT_CHANGE_BINDING__ELEMENT_REFERENCE:
				setElementReference((IElementReference)newValue);
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
			case BindingPackage.ELEMENT_CHANGE_BINDING__NOTES:
				getNotes().clear();
				return;
			case BindingPackage.ELEMENT_CHANGE_BINDING__MODEL_ELEMENT:
				setModelElement((EObject)null);
				return;
			case BindingPackage.ELEMENT_CHANGE_BINDING__IGNORE:
				setIgnore(IGNORE_EDEFAULT);
				return;
			case BindingPackage.ELEMENT_CHANGE_BINDING__ELEMENT_REFERENCE:
				setElementReference((IElementReference)null);
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
			case BindingPackage.ELEMENT_CHANGE_BINDING__NOTES:
				return notes != null && !notes.isEmpty();
			case BindingPackage.ELEMENT_CHANGE_BINDING__MODEL_ELEMENT:
				return modelElement != null;
			case BindingPackage.ELEMENT_CHANGE_BINDING__IGNORE:
				return ignore != IGNORE_EDEFAULT;
			case BindingPackage.ELEMENT_CHANGE_BINDING__ELEMENT_REFERENCE:
				return elementReference != null;
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
		result.append(" (ignore: ");
		result.append(ignore);
		result.append(')');
		return result.toString();
	}

} //ElementChangeBindingImpl
