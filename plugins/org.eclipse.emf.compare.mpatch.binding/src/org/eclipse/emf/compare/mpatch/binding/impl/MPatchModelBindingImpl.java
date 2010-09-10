/**
 *  Copyright (c) 2010 Technical University of Denmark.
 *  All rights reserved. This program and the accompanying materials 
 *  are made available under the terms of the Eclipse Public License v1.0 
 *  which accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html 
 *  
 *  Contributors:
 *     Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: MPatchModelBindingImpl.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 */
package org.eclipse.emf.compare.mpatch.binding.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.binding.BindingPackage;
import org.eclipse.emf.compare.mpatch.binding.ChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding;
import org.eclipse.emf.compare.mpatch.binding.Note;
import org.eclipse.emf.compare.mpatch.binding.NoteContainer;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diff Model Binding</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.MPatchModelBindingImpl#getNotes <em>Notes</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.MPatchModelBindingImpl#getAllNotes <em>All Notes</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.MPatchModelBindingImpl#getChangeBindings <em>Change Bindings</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.MPatchModelBindingImpl#getModel <em>Model</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.mpatch.binding.impl.MPatchModelBindingImpl#getMPatchModel <em>MPatch Model</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MPatchModelBindingImpl extends EObjectImpl implements MPatchModelBinding {
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
	 * The cached value of the '{@link #getAllNotes() <em>All Notes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllNotes()
	 * @generated
	 * @ordered
	 */
	protected EList<Note> allNotes;

	/**
	 * The cached value of the '{@link #getChangeBindings() <em>Change Bindings</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeBindings()
	 * @generated
	 * @ordered
	 */
	protected EList<ChangeBinding> changeBindings;

	/**
	 * The cached value of the '{@link #getModel() <em>Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModel()
	 * @generated
	 * @ordered
	 */
	protected EObject model;

	/**
	 * The cached value of the '{@link #getMPatchModel() <em>MPatch Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMPatchModel()
	 * @generated
	 * @ordered
	 */
	protected MPatchModel mPatchModel;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MPatchModelBindingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BindingPackage.Literals.MPATCH_MODEL_BINDING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Note> getNotes() {
		if (notes == null) {
			notes = new EObjectResolvingEList<Note>(Note.class, this, BindingPackage.MPATCH_MODEL_BINDING__NOTES);
		}
		return notes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ChangeBinding> getChangeBindings() {
		if (changeBindings == null) {
			changeBindings = new EObjectContainmentEList<ChangeBinding>(ChangeBinding.class, this, BindingPackage.MPATCH_MODEL_BINDING__CHANGE_BINDINGS);
		}
		return changeBindings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getModel() {
		if (model != null && model.eIsProxy()) {
			InternalEObject oldModel = (InternalEObject)model;
			model = eResolveProxy(oldModel);
			if (model != oldModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BindingPackage.MPATCH_MODEL_BINDING__MODEL, oldModel, model));
			}
		}
		return model;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetModel() {
		return model;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModel(EObject newModel) {
		EObject oldModel = model;
		model = newModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BindingPackage.MPATCH_MODEL_BINDING__MODEL, oldModel, model));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MPatchModel getMPatchModel() {
		if (mPatchModel != null && mPatchModel.eIsProxy()) {
			InternalEObject oldMPatchModel = (InternalEObject)mPatchModel;
			mPatchModel = (MPatchModel)eResolveProxy(oldMPatchModel);
			if (mPatchModel != oldMPatchModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BindingPackage.MPATCH_MODEL_BINDING__MPATCH_MODEL, oldMPatchModel, mPatchModel));
			}
		}
		return mPatchModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MPatchModel basicGetMPatchModel() {
		return mPatchModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMPatchModel(MPatchModel newMPatchModel) {
		MPatchModel oldMPatchModel = mPatchModel;
		mPatchModel = newMPatchModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BindingPackage.MPATCH_MODEL_BINDING__MPATCH_MODEL, oldMPatchModel, mPatchModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Note> getAllNotes() {
		if (allNotes == null) {
			allNotes = new EObjectContainmentEList<Note>(Note.class, this, BindingPackage.MPATCH_MODEL_BINDING__ALL_NOTES);
		}
		return allNotes;
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
			case BindingPackage.MPATCH_MODEL_BINDING__ALL_NOTES:
				return ((InternalEList<?>)getAllNotes()).basicRemove(otherEnd, msgs);
			case BindingPackage.MPATCH_MODEL_BINDING__CHANGE_BINDINGS:
				return ((InternalEList<?>)getChangeBindings()).basicRemove(otherEnd, msgs);
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
			case BindingPackage.MPATCH_MODEL_BINDING__NOTES:
				return getNotes();
			case BindingPackage.MPATCH_MODEL_BINDING__ALL_NOTES:
				return getAllNotes();
			case BindingPackage.MPATCH_MODEL_BINDING__CHANGE_BINDINGS:
				return getChangeBindings();
			case BindingPackage.MPATCH_MODEL_BINDING__MODEL:
				if (resolve) return getModel();
				return basicGetModel();
			case BindingPackage.MPATCH_MODEL_BINDING__MPATCH_MODEL:
				if (resolve) return getMPatchModel();
				return basicGetMPatchModel();
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
			case BindingPackage.MPATCH_MODEL_BINDING__NOTES:
				getNotes().clear();
				getNotes().addAll((Collection<? extends Note>)newValue);
				return;
			case BindingPackage.MPATCH_MODEL_BINDING__ALL_NOTES:
				getAllNotes().clear();
				getAllNotes().addAll((Collection<? extends Note>)newValue);
				return;
			case BindingPackage.MPATCH_MODEL_BINDING__CHANGE_BINDINGS:
				getChangeBindings().clear();
				getChangeBindings().addAll((Collection<? extends ChangeBinding>)newValue);
				return;
			case BindingPackage.MPATCH_MODEL_BINDING__MODEL:
				setModel((EObject)newValue);
				return;
			case BindingPackage.MPATCH_MODEL_BINDING__MPATCH_MODEL:
				setMPatchModel((MPatchModel)newValue);
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
			case BindingPackage.MPATCH_MODEL_BINDING__NOTES:
				getNotes().clear();
				return;
			case BindingPackage.MPATCH_MODEL_BINDING__ALL_NOTES:
				getAllNotes().clear();
				return;
			case BindingPackage.MPATCH_MODEL_BINDING__CHANGE_BINDINGS:
				getChangeBindings().clear();
				return;
			case BindingPackage.MPATCH_MODEL_BINDING__MODEL:
				setModel((EObject)null);
				return;
			case BindingPackage.MPATCH_MODEL_BINDING__MPATCH_MODEL:
				setMPatchModel((MPatchModel)null);
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
			case BindingPackage.MPATCH_MODEL_BINDING__NOTES:
				return notes != null && !notes.isEmpty();
			case BindingPackage.MPATCH_MODEL_BINDING__ALL_NOTES:
				return allNotes != null && !allNotes.isEmpty();
			case BindingPackage.MPATCH_MODEL_BINDING__CHANGE_BINDINGS:
				return changeBindings != null && !changeBindings.isEmpty();
			case BindingPackage.MPATCH_MODEL_BINDING__MODEL:
				return model != null;
			case BindingPackage.MPATCH_MODEL_BINDING__MPATCH_MODEL:
				return mPatchModel != null;
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
		if (baseClass == NoteContainer.class) {
			switch (derivedFeatureID) {
				case BindingPackage.MPATCH_MODEL_BINDING__ALL_NOTES: return BindingPackage.NOTE_CONTAINER__ALL_NOTES;
				default: return -1;
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
		if (baseClass == NoteContainer.class) {
			switch (baseFeatureID) {
				case BindingPackage.NOTE_CONTAINER__ALL_NOTES: return BindingPackage.MPATCH_MODEL_BINDING__ALL_NOTES;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

} //MPatchModelBindingImpl
