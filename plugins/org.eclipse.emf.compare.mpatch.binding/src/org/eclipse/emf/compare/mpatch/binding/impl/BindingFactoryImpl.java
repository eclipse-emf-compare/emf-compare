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
 * $Id: BindingFactoryImpl.java,v 1.2 2010/09/14 09:45:45 pkonemann Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding.impl;

import java.util.Map;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.binding.AddElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.AddReferenceChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.AttributeChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.BindingFactory;
import org.eclipse.emf.compare.mpatch.binding.BindingPackage;
import org.eclipse.emf.compare.mpatch.binding.ChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding;
import org.eclipse.emf.compare.mpatch.binding.MoveElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.Note;
import org.eclipse.emf.compare.mpatch.binding.NoteElement;
import org.eclipse.emf.compare.mpatch.binding.RemoveElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.RemoveReferenceChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.SubModelBinding;
import org.eclipse.emf.compare.mpatch.binding.UpdateReferenceChangeBinding;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class BindingFactoryImpl extends EFactoryImpl implements BindingFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static BindingFactory init() {
		try {
			BindingFactory theBindingFactory = (BindingFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/emf/compare/mpatch/1.0/binding"); 
			if (theBindingFactory != null) {
				return theBindingFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new BindingFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BindingFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case BindingPackage.MPATCH_MODEL_BINDING: return createMPatchModelBinding();
			case BindingPackage.ELEMENT_CHANGE_BINDING: return createElementChangeBinding();
			case BindingPackage.SUB_MODEL_BINDING: return createSubModelBinding();
			case BindingPackage.ATTRIBUTE_CHANGE_BINDING: return createAttributeChangeBinding();
			case BindingPackage.ADD_ELEMENT_CHANGE_BINDING: return createAddElementChangeBinding();
			case BindingPackage.MOVE_ELEMENT_CHANGE_BINDING: return createMoveElementChangeBinding();
			case BindingPackage.ADD_REFERENCE_CHANGE_BINDING: return createAddReferenceChangeBinding();
			case BindingPackage.UPDATE_REFERENCE_CHANGE_BINDING: return createUpdateReferenceChangeBinding();
			case BindingPackage.REMOVE_ELEMENT_CHANGE_BINDING: return createRemoveElementChangeBinding();
			case BindingPackage.REMOVE_REFERENCE_CHANGE_BINDING: return createRemoveReferenceChangeBinding();
			case BindingPackage.NOTE: return createNote();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MPatchModelBinding createMPatchModelBinding() {
		MPatchModelBindingImpl mPatchModelBinding = new MPatchModelBindingImpl();
		return mPatchModelBinding;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ElementChangeBinding createElementChangeBinding() {
		ElementChangeBindingImpl elementChangeBinding = new ElementChangeBindingImpl();
		return elementChangeBinding;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SubModelBinding createSubModelBinding() {
		SubModelBindingImpl subModelBinding = new SubModelBindingImpl();
		return subModelBinding;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AttributeChangeBinding createAttributeChangeBinding() {
		AttributeChangeBindingImpl attributeChangeBinding = new AttributeChangeBindingImpl();
		return attributeChangeBinding;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AddElementChangeBinding createAddElementChangeBinding() {
		AddElementChangeBindingImpl addElementChangeBinding = new AddElementChangeBindingImpl();
		return addElementChangeBinding;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MoveElementChangeBinding createMoveElementChangeBinding() {
		MoveElementChangeBindingImpl moveElementChangeBinding = new MoveElementChangeBindingImpl();
		return moveElementChangeBinding;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AddReferenceChangeBinding createAddReferenceChangeBinding() {
		AddReferenceChangeBindingImpl addReferenceChangeBinding = new AddReferenceChangeBindingImpl();
		return addReferenceChangeBinding;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UpdateReferenceChangeBinding createUpdateReferenceChangeBinding() {
		UpdateReferenceChangeBindingImpl updateReferenceChangeBinding = new UpdateReferenceChangeBindingImpl();
		return updateReferenceChangeBinding;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RemoveElementChangeBinding createRemoveElementChangeBinding() {
		RemoveElementChangeBindingImpl removeElementChangeBinding = new RemoveElementChangeBindingImpl();
		return removeElementChangeBinding;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RemoveReferenceChangeBinding createRemoveReferenceChangeBinding() {
		RemoveReferenceChangeBindingImpl removeReferenceChangeBinding = new RemoveReferenceChangeBindingImpl();
		return removeReferenceChangeBinding;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Note createNote() {
		NoteImpl note = new NoteImpl();
		return note;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BindingPackage getBindingPackage() {
		return (BindingPackage)getEPackage();
	}

	@Override
	public void createNote(NoteElement noteElement, String note) {
		EObject noteContainer = noteElement;
		while (!(noteContainer instanceof MPatchModelBinding) && noteContainer.eContainer() != null)
			noteContainer = noteContainer.eContainer();
		final Note aNote = createNote();
		aNote.setNote(note);
		((MPatchModelBinding)noteContainer).getAllNotes().add(aNote);
		noteElement.getNotes().add(aNote);
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static BindingPackage getPackage() {
		return BindingPackage.eINSTANCE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.compare.mpatch.diffbinding.MpatchbindingFactory#createChangeBindingForChange(org.eclipse.emf.compare.mpatch.IndepChange)
	 */
	@Override
	public ChangeBinding createChangeBindingForChange(IndepChange change) {
		ChangeBinding binding = null;
		
		// create the correct binding
		switch (change.eClass().getClassifierID()) {
		case MPatchPackage.INDEP_ADD_ELEMENT_CHANGE:
			binding = createAddElementChangeBinding();
			break;
		case MPatchPackage.INDEP_REMOVE_ELEMENT_CHANGE:
			binding = createRemoveElementChangeBinding();
//			((AddRemoveElementChangeBinding)binding).setSubModelReferences(createSubModelBinding(((IndepAddRemElementChange)change).getSubModel()));
			break;
		case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE:
			binding = createMoveElementChangeBinding();
//			((MoveElementChangeBinding)binding).setOldParent(createElementBinding(((IndepMoveElementChange)change).getOldParent()));
//			((MoveElementChangeBinding)binding).setNewParent(createElementBinding(((IndepMoveElementChange)change).getNewParent()));
			break;
		case MPatchPackage.INDEP_ADD_REFERENCE_CHANGE:
			binding = createAddReferenceChangeBinding();
			break;
		case MPatchPackage.INDEP_REMOVE_REFERENCE_CHANGE:
			binding = createRemoveReferenceChangeBinding();
//			((AddRemoveReferenceChangeBinding)binding).setChangedReference(createElementBinding(((IndepAddReferenceChange)change).getChangedReference()));
			break;
		case MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE:
			binding = createUpdateReferenceChangeBinding();
//			if (((IndepUpdateReferenceChange)change).getOldReference() != null)
//				((UpdateReferenceChangeBinding)binding).setOldReference(createElementBinding(((IndepUpdateReferenceChange)change).getOldReference()));
//			if (((IndepUpdateReferenceChange)change).getNewReference() != null)
//				((UpdateReferenceChangeBinding)binding).setNewReference(createElementBinding(((IndepUpdateReferenceChange)change).getNewReference()));
			break;
		case MPatchPackage.INDEP_ADD_ATTRIBUTE_CHANGE:
		case MPatchPackage.INDEP_REMOVE_ATTRIBUTE_CHANGE:
		case MPatchPackage.INDEP_UPDATE_ATTRIBUTE_CHANGE:
			binding = createAttributeChangeBinding();
		}
		
		// set the change, if a binding was created
		if (binding != null) {
			binding.setChange(change);
//			binding.setCorrespondingElements(createElementBinding(change.getCorrespondingElement()));
		}
		return binding;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.compare.mpatch.diffbinding.MpatchbindingFactory#createElementBinding(org.eclipse.emf.compare.mpatch.IElementReference, org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public ElementChangeBinding createElementChangeBinding(IElementReference elementReference, EObject modelElement) {
		final ElementChangeBinding binding = createElementChangeBinding();
		binding.setElementReference(elementReference);
		binding.setModelElement(modelElement);
		return binding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.compare.mpatch.diffbinding.MpatchbindingFactory#createSubModelBinding(org.eclipse.emf.compare.mpatch.IModelDescriptor, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public SubModelBinding createSubModelBinding(final EMap<EObject, IModelDescriptor> modelDescriptors, final EObject element, 
			final IElementReference parentReference, final EObject parent, final Map<EObject, SubModelBinding> addedElementToSubModelBindingMap) {
	
		// basic submodelbinding
		final SubModelBinding binding = createSubModelBinding();
		binding.setModelDescriptor(modelDescriptors.get(element));
		binding.setElementReference(parentReference);
		binding.setModelElement(parent);
		binding.setSelfElement(element);
		addedElementToSubModelBindingMap.put(element, binding);
		
		// local cross references...
//		for (IElementReference ref : subModelDescriptor.getCrossReferences()) {
//			binding.getSubModelReferences().add(createElementBinding(ref));
//		}
		
		// children...
		for (EObject child : element.eContents()) {
			binding.getSubModelReferences().add(createSubModelBinding(modelDescriptors, child, modelDescriptors.get(element).getSelfReference(), element, addedElementToSubModelBindingMap));
		}
		return binding;
	}

} //BindingFactoryImpl
