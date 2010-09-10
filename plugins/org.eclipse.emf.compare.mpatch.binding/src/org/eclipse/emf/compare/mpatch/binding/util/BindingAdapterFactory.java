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
 * $Id: BindingAdapterFactory.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.compare.mpatch.binding.AddElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.AddReferenceChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.AttributeChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.BindingPackage;
import org.eclipse.emf.compare.mpatch.binding.ChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.ElementBinding;
import org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding;
import org.eclipse.emf.compare.mpatch.binding.MoveElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.Note;
import org.eclipse.emf.compare.mpatch.binding.NoteContainer;
import org.eclipse.emf.compare.mpatch.binding.NoteElement;
import org.eclipse.emf.compare.mpatch.binding.RemoveElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.RemoveReferenceChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.SubModelBinding;
import org.eclipse.emf.compare.mpatch.binding.UpdateReferenceChangeBinding;
import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage
 * @generated
 */
public class BindingAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static BindingPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BindingAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = BindingPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BindingSwitch<Adapter> modelSwitch =
		new BindingSwitch<Adapter>() {
			@Override
			public Adapter caseNoteElement(NoteElement object) {
				return createNoteElementAdapter();
			}
			@Override
			public Adapter caseMPatchModelBinding(MPatchModelBinding object) {
				return createMPatchModelBindingAdapter();
			}
			@Override
			public Adapter caseChangeBinding(ChangeBinding object) {
				return createChangeBindingAdapter();
			}
			@Override
			public Adapter caseElementBinding(ElementBinding object) {
				return createElementBindingAdapter();
			}
			@Override
			public Adapter caseElementChangeBinding(ElementChangeBinding object) {
				return createElementChangeBindingAdapter();
			}
			@Override
			public Adapter caseSubModelBinding(SubModelBinding object) {
				return createSubModelBindingAdapter();
			}
			@Override
			public Adapter caseAttributeChangeBinding(AttributeChangeBinding object) {
				return createAttributeChangeBindingAdapter();
			}
			@Override
			public Adapter caseAddElementChangeBinding(AddElementChangeBinding object) {
				return createAddElementChangeBindingAdapter();
			}
			@Override
			public Adapter caseMoveElementChangeBinding(MoveElementChangeBinding object) {
				return createMoveElementChangeBindingAdapter();
			}
			@Override
			public Adapter caseAddReferenceChangeBinding(AddReferenceChangeBinding object) {
				return createAddReferenceChangeBindingAdapter();
			}
			@Override
			public Adapter caseUpdateReferenceChangeBinding(UpdateReferenceChangeBinding object) {
				return createUpdateReferenceChangeBindingAdapter();
			}
			@Override
			public Adapter caseRemoveElementChangeBinding(RemoveElementChangeBinding object) {
				return createRemoveElementChangeBindingAdapter();
			}
			@Override
			public Adapter caseRemoveReferenceChangeBinding(RemoveReferenceChangeBinding object) {
				return createRemoveReferenceChangeBindingAdapter();
			}
			@Override
			public Adapter caseNote(Note object) {
				return createNoteAdapter();
			}
			@Override
			public Adapter caseNoteContainer(NoteContainer object) {
				return createNoteContainerAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.binding.ChangeBinding <em>Change Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.binding.ChangeBinding
	 * @generated
	 */
	public Adapter createChangeBindingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding <em>Element Change Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding
	 * @generated
	 */
	public Adapter createElementChangeBindingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.binding.ElementBinding <em>Element Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.binding.ElementBinding
	 * @generated
	 */
	public Adapter createElementBindingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.binding.SubModelBinding <em>Sub Model Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.binding.SubModelBinding
	 * @generated
	 */
	public Adapter createSubModelBindingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.binding.AttributeChangeBinding <em>Attribute Change Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.binding.AttributeChangeBinding
	 * @generated
	 */
	public Adapter createAttributeChangeBindingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.binding.AddElementChangeBinding <em>Add Element Change Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.binding.AddElementChangeBinding
	 * @generated
	 */
	public Adapter createAddElementChangeBindingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.binding.MoveElementChangeBinding <em>Move Element Change Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.binding.MoveElementChangeBinding
	 * @generated
	 */
	public Adapter createMoveElementChangeBindingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.binding.AddReferenceChangeBinding <em>Add Reference Change Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.binding.AddReferenceChangeBinding
	 * @generated
	 */
	public Adapter createAddReferenceChangeBindingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.binding.UpdateReferenceChangeBinding <em>Update Reference Change Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.binding.UpdateReferenceChangeBinding
	 * @generated
	 */
	public Adapter createUpdateReferenceChangeBindingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.binding.NoteElement <em>Note Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.binding.NoteElement
	 * @generated
	 */
	public Adapter createNoteElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding <em>MPatch Model Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding
	 * @generated
	 */
	public Adapter createMPatchModelBindingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.binding.RemoveElementChangeBinding <em>Remove Element Change Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.binding.RemoveElementChangeBinding
	 * @generated
	 */
	public Adapter createRemoveElementChangeBindingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.binding.RemoveReferenceChangeBinding <em>Remove Reference Change Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.binding.RemoveReferenceChangeBinding
	 * @generated
	 */
	public Adapter createRemoveReferenceChangeBindingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.binding.Note <em>Note</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.binding.Note
	 * @generated
	 */
	public Adapter createNoteAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.binding.NoteContainer <em>Note Container</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.binding.NoteContainer
	 * @generated
	 */
	public Adapter createNoteContainerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //BindingAdapterFactory
