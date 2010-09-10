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
 * $Id: ChangeBindingImpl.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.binding.BindingPackage;
import org.eclipse.emf.compare.mpatch.binding.ChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.Note;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Change Binding</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.ChangeBindingImpl#getNotes <em>Notes</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.ChangeBindingImpl#getChange <em>Change</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.ChangeBindingImpl#getCorrespondingElements <em>Corresponding Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class ChangeBindingImpl extends EObjectImpl implements ChangeBinding {
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
	 * The cached value of the '{@link #getChange() <em>Change</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChange()
	 * @generated
	 * @ordered
	 */
	protected IndepChange change;

	/**
	 * The cached value of the '{@link #getCorrespondingElements() <em>Corresponding Elements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCorrespondingElements()
	 * @generated
	 * @ordered
	 */
	protected EList<ElementChangeBinding> correspondingElements;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChangeBindingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BindingPackage.Literals.CHANGE_BINDING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Note> getNotes() {
		if (notes == null) {
			notes = new EObjectResolvingEList<Note>(Note.class, this, BindingPackage.CHANGE_BINDING__NOTES);
		}
		return notes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IndepChange getChange() {
		if (change != null && change.eIsProxy()) {
			InternalEObject oldChange = (InternalEObject)change;
			change = (IndepChange)eResolveProxy(oldChange);
			if (change != oldChange) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BindingPackage.CHANGE_BINDING__CHANGE, oldChange, change));
			}
		}
		return change;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IndepChange basicGetChange() {
		return change;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setChange(IndepChange newChange) {
		IndepChange oldChange = change;
		change = newChange;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BindingPackage.CHANGE_BINDING__CHANGE, oldChange, change));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ElementChangeBinding> getCorrespondingElements() {
		if (correspondingElements == null) {
			correspondingElements = new EObjectContainmentEList<ElementChangeBinding>(ElementChangeBinding.class, this, BindingPackage.CHANGE_BINDING__CORRESPONDING_ELEMENTS);
		}
		return correspondingElements;
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case BindingPackage.CHANGE_BINDING__CORRESPONDING_ELEMENTS:
				return ((InternalEList<?>)getCorrespondingElements()).basicRemove(otherEnd, msgs);
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
			case BindingPackage.CHANGE_BINDING__NOTES:
				return getNotes();
			case BindingPackage.CHANGE_BINDING__CHANGE:
				if (resolve) return getChange();
				return basicGetChange();
			case BindingPackage.CHANGE_BINDING__CORRESPONDING_ELEMENTS:
				return getCorrespondingElements();
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
			case BindingPackage.CHANGE_BINDING__NOTES:
				getNotes().clear();
				getNotes().addAll((Collection<? extends Note>)newValue);
				return;
			case BindingPackage.CHANGE_BINDING__CHANGE:
				setChange((IndepChange)newValue);
				return;
			case BindingPackage.CHANGE_BINDING__CORRESPONDING_ELEMENTS:
				getCorrespondingElements().clear();
				getCorrespondingElements().addAll((Collection<? extends ElementChangeBinding>)newValue);
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
			case BindingPackage.CHANGE_BINDING__NOTES:
				getNotes().clear();
				return;
			case BindingPackage.CHANGE_BINDING__CHANGE:
				setChange((IndepChange)null);
				return;
			case BindingPackage.CHANGE_BINDING__CORRESPONDING_ELEMENTS:
				getCorrespondingElements().clear();
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
			case BindingPackage.CHANGE_BINDING__NOTES:
				return notes != null && !notes.isEmpty();
			case BindingPackage.CHANGE_BINDING__CHANGE:
				return change != null;
			case BindingPackage.CHANGE_BINDING__CORRESPONDING_ELEMENTS:
				return correspondingElements != null && !correspondingElements.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ChangeBindingImpl
