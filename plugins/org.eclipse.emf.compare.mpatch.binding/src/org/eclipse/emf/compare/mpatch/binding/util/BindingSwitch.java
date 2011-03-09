/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: BindingSwitch.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.binding.util;

import java.util.List;

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
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.mpatch.binding.BindingPackage
 * @generated
 */
public class BindingSwitch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static BindingPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BindingSwitch() {
		if (modelPackage == null) {
			modelPackage = BindingPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public T doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		}
		else {
			List<EClass> eSuperTypes = theEClass.getESuperTypes();
			return
				eSuperTypes.isEmpty() ?
					defaultCase(theEObject) :
					doSwitch(eSuperTypes.get(0), theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case BindingPackage.NOTE_ELEMENT: {
				NoteElement noteElement = (NoteElement)theEObject;
				T result = caseNoteElement(noteElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BindingPackage.MPATCH_MODEL_BINDING: {
				MPatchModelBinding mPatchModelBinding = (MPatchModelBinding)theEObject;
				T result = caseMPatchModelBinding(mPatchModelBinding);
				if (result == null) result = caseNoteElement(mPatchModelBinding);
				if (result == null) result = caseNoteContainer(mPatchModelBinding);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BindingPackage.CHANGE_BINDING: {
				ChangeBinding changeBinding = (ChangeBinding)theEObject;
				T result = caseChangeBinding(changeBinding);
				if (result == null) result = caseNoteElement(changeBinding);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BindingPackage.ELEMENT_BINDING: {
				ElementBinding elementBinding = (ElementBinding)theEObject;
				T result = caseElementBinding(elementBinding);
				if (result == null) result = caseNoteElement(elementBinding);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BindingPackage.ELEMENT_CHANGE_BINDING: {
				ElementChangeBinding elementChangeBinding = (ElementChangeBinding)theEObject;
				T result = caseElementChangeBinding(elementChangeBinding);
				if (result == null) result = caseElementBinding(elementChangeBinding);
				if (result == null) result = caseNoteElement(elementChangeBinding);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BindingPackage.SUB_MODEL_BINDING: {
				SubModelBinding subModelBinding = (SubModelBinding)theEObject;
				T result = caseSubModelBinding(subModelBinding);
				if (result == null) result = caseElementChangeBinding(subModelBinding);
				if (result == null) result = caseElementBinding(subModelBinding);
				if (result == null) result = caseNoteElement(subModelBinding);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BindingPackage.ATTRIBUTE_CHANGE_BINDING: {
				AttributeChangeBinding attributeChangeBinding = (AttributeChangeBinding)theEObject;
				T result = caseAttributeChangeBinding(attributeChangeBinding);
				if (result == null) result = caseChangeBinding(attributeChangeBinding);
				if (result == null) result = caseNoteElement(attributeChangeBinding);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BindingPackage.ADD_ELEMENT_CHANGE_BINDING: {
				AddElementChangeBinding addElementChangeBinding = (AddElementChangeBinding)theEObject;
				T result = caseAddElementChangeBinding(addElementChangeBinding);
				if (result == null) result = caseChangeBinding(addElementChangeBinding);
				if (result == null) result = caseNoteElement(addElementChangeBinding);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BindingPackage.MOVE_ELEMENT_CHANGE_BINDING: {
				MoveElementChangeBinding moveElementChangeBinding = (MoveElementChangeBinding)theEObject;
				T result = caseMoveElementChangeBinding(moveElementChangeBinding);
				if (result == null) result = caseChangeBinding(moveElementChangeBinding);
				if (result == null) result = caseNoteElement(moveElementChangeBinding);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BindingPackage.ADD_REFERENCE_CHANGE_BINDING: {
				AddReferenceChangeBinding addReferenceChangeBinding = (AddReferenceChangeBinding)theEObject;
				T result = caseAddReferenceChangeBinding(addReferenceChangeBinding);
				if (result == null) result = caseChangeBinding(addReferenceChangeBinding);
				if (result == null) result = caseNoteElement(addReferenceChangeBinding);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BindingPackage.UPDATE_REFERENCE_CHANGE_BINDING: {
				UpdateReferenceChangeBinding updateReferenceChangeBinding = (UpdateReferenceChangeBinding)theEObject;
				T result = caseUpdateReferenceChangeBinding(updateReferenceChangeBinding);
				if (result == null) result = caseChangeBinding(updateReferenceChangeBinding);
				if (result == null) result = caseNoteElement(updateReferenceChangeBinding);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BindingPackage.REMOVE_ELEMENT_CHANGE_BINDING: {
				RemoveElementChangeBinding removeElementChangeBinding = (RemoveElementChangeBinding)theEObject;
				T result = caseRemoveElementChangeBinding(removeElementChangeBinding);
				if (result == null) result = caseChangeBinding(removeElementChangeBinding);
				if (result == null) result = caseNoteElement(removeElementChangeBinding);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BindingPackage.REMOVE_REFERENCE_CHANGE_BINDING: {
				RemoveReferenceChangeBinding removeReferenceChangeBinding = (RemoveReferenceChangeBinding)theEObject;
				T result = caseRemoveReferenceChangeBinding(removeReferenceChangeBinding);
				if (result == null) result = caseChangeBinding(removeReferenceChangeBinding);
				if (result == null) result = caseNoteElement(removeReferenceChangeBinding);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BindingPackage.NOTE: {
				Note note = (Note)theEObject;
				T result = caseNote(note);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BindingPackage.NOTE_CONTAINER: {
				NoteContainer noteContainer = (NoteContainer)theEObject;
				T result = caseNoteContainer(noteContainer);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Change Binding</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Change Binding</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseChangeBinding(ChangeBinding object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Element Change Binding</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Element Change Binding</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseElementChangeBinding(ElementChangeBinding object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Element Binding</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Element Binding</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseElementBinding(ElementBinding object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Sub Model Binding</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Sub Model Binding</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSubModelBinding(SubModelBinding object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute Change Binding</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute Change Binding</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAttributeChangeBinding(AttributeChangeBinding object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Add Element Change Binding</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Add Element Change Binding</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAddElementChangeBinding(AddElementChangeBinding object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Move Element Change Binding</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Move Element Change Binding</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMoveElementChangeBinding(MoveElementChangeBinding object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Add Reference Change Binding</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Add Reference Change Binding</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAddReferenceChangeBinding(AddReferenceChangeBinding object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Update Reference Change Binding</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Update Reference Change Binding</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUpdateReferenceChangeBinding(UpdateReferenceChangeBinding object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Note Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Note Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNoteElement(NoteElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>MPatch Model Binding</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>MPatch Model Binding</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMPatchModelBinding(MPatchModelBinding object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Remove Element Change Binding</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Remove Element Change Binding</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRemoveElementChangeBinding(RemoveElementChangeBinding object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Remove Reference Change Binding</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Remove Reference Change Binding</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRemoveReferenceChangeBinding(RemoveReferenceChangeBinding object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Note</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Note</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNote(Note object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Note Container</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Note Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNoteContainer(NoteContainer object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public T defaultCase(EObject object) {
		return null;
	}

} //BindingSwitch
