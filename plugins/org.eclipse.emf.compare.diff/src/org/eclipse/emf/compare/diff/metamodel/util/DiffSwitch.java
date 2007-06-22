/**
 * <copyright>
 * </copyright>
 *
 * $Id: DiffSwitch.java,v 1.1 2007/06/22 12:59:47 cbrun Exp $
 */
package org.eclipse.emf.compare.diff.metamodel.util;

import java.util.List;

import org.eclipse.emf.compare.diff.metamodel.AddAttribute;
import org.eclipse.emf.compare.diff.metamodel.AddModelElement;
import org.eclipse.emf.compare.diff.metamodel.AddReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.AttributeChange;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChange;
import org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot;
import org.eclipse.emf.compare.diff.metamodel.MoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.diff.metamodel.RemoveAttribute;
import org.eclipse.emf.compare.diff.metamodel.RemoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateModelElement;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
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
 * @see org.eclipse.emf.compare.diff.metamodel.DiffPackage
 * @generated
 */
public class DiffSwitch {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static DiffPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiffSwitch() {
		if (modelPackage == null) {
			modelPackage = DiffPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public Object doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected Object doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		} else {
			List eSuperTypes = theEClass.getESuperTypes();
			return eSuperTypes.isEmpty() ? defaultCase(theEObject) : doSwitch((EClass)eSuperTypes.get(0),
					theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected Object doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case DiffPackage.DIFF_MODEL: {
				DiffModel diffModel = (DiffModel)theEObject;
				Object result = caseDiffModel(diffModel);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.DIFF_ELEMENT: {
				DiffElement diffElement = (DiffElement)theEObject;
				Object result = caseDiffElement(diffElement);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.DIFF_GROUP: {
				DiffGroup diffGroup = (DiffGroup)theEObject;
				Object result = caseDiffGroup(diffGroup);
				if (result == null)
					result = caseDiffElement(diffGroup);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.ATTRIBUTE_CHANGE: {
				AttributeChange attributeChange = (AttributeChange)theEObject;
				Object result = caseAttributeChange(attributeChange);
				if (result == null)
					result = caseDiffElement(attributeChange);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.REFERENCE_CHANGE: {
				ReferenceChange referenceChange = (ReferenceChange)theEObject;
				Object result = caseReferenceChange(referenceChange);
				if (result == null)
					result = caseDiffElement(referenceChange);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.MODEL_ELEMENT_CHANGE: {
				ModelElementChange modelElementChange = (ModelElementChange)theEObject;
				Object result = caseModelElementChange(modelElementChange);
				if (result == null)
					result = caseDiffElement(modelElementChange);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.ADD_MODEL_ELEMENT: {
				AddModelElement addModelElement = (AddModelElement)theEObject;
				Object result = caseAddModelElement(addModelElement);
				if (result == null)
					result = caseModelElementChange(addModelElement);
				if (result == null)
					result = caseDiffElement(addModelElement);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.REMOVE_MODEL_ELEMENT: {
				RemoveModelElement removeModelElement = (RemoveModelElement)theEObject;
				Object result = caseRemoveModelElement(removeModelElement);
				if (result == null)
					result = caseModelElementChange(removeModelElement);
				if (result == null)
					result = caseDiffElement(removeModelElement);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.UPDATE_MODEL_ELEMENT: {
				UpdateModelElement updateModelElement = (UpdateModelElement)theEObject;
				Object result = caseUpdateModelElement(updateModelElement);
				if (result == null)
					result = caseModelElementChange(updateModelElement);
				if (result == null)
					result = caseDiffElement(updateModelElement);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.MOVE_MODEL_ELEMENT: {
				MoveModelElement moveModelElement = (MoveModelElement)theEObject;
				Object result = caseMoveModelElement(moveModelElement);
				if (result == null)
					result = caseUpdateModelElement(moveModelElement);
				if (result == null)
					result = caseModelElementChange(moveModelElement);
				if (result == null)
					result = caseDiffElement(moveModelElement);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.ADD_ATTRIBUTE: {
				AddAttribute addAttribute = (AddAttribute)theEObject;
				Object result = caseAddAttribute(addAttribute);
				if (result == null)
					result = caseAttributeChange(addAttribute);
				if (result == null)
					result = caseDiffElement(addAttribute);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.REMOVE_ATTRIBUTE: {
				RemoveAttribute removeAttribute = (RemoveAttribute)theEObject;
				Object result = caseRemoveAttribute(removeAttribute);
				if (result == null)
					result = caseAttributeChange(removeAttribute);
				if (result == null)
					result = caseDiffElement(removeAttribute);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.UPDATE_ATTRIBUTE: {
				UpdateAttribute updateAttribute = (UpdateAttribute)theEObject;
				Object result = caseUpdateAttribute(updateAttribute);
				if (result == null)
					result = caseAttributeChange(updateAttribute);
				if (result == null)
					result = caseDiffElement(updateAttribute);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.ADD_REFERENCE_VALUE: {
				AddReferenceValue addReferenceValue = (AddReferenceValue)theEObject;
				Object result = caseAddReferenceValue(addReferenceValue);
				if (result == null)
					result = caseReferenceChange(addReferenceValue);
				if (result == null)
					result = caseDiffElement(addReferenceValue);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.REMOVE_REFERENCE_VALUE: {
				RemoveReferenceValue removeReferenceValue = (RemoveReferenceValue)theEObject;
				Object result = caseRemoveReferenceValue(removeReferenceValue);
				if (result == null)
					result = caseReferenceChange(removeReferenceValue);
				if (result == null)
					result = caseDiffElement(removeReferenceValue);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.UPDATE_REFERENCE: {
				UpdateReference updateReference = (UpdateReference)theEObject;
				Object result = caseUpdateReference(updateReference);
				if (result == null)
					result = caseReferenceChange(updateReference);
				if (result == null)
					result = caseDiffElement(updateReference);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case DiffPackage.MODEL_INPUT_SNAPSHOT: {
				ModelInputSnapshot modelInputSnapshot = (ModelInputSnapshot)theEObject;
				Object result = caseModelInputSnapshot(modelInputSnapshot);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			default:
				return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseDiffModel(DiffModel object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseDiffElement(DiffElement object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Group</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Group</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseDiffGroup(DiffGroup object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Attribute Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Attribute Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAttributeChange(AttributeChange object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Reference Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Reference Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseReferenceChange(ReferenceChange object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Model Element Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Model Element Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseModelElementChange(ModelElementChange object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Add Model Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Add Model Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAddModelElement(AddModelElement object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Remove Model Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Remove Model Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseRemoveModelElement(RemoveModelElement object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Update Model Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Update Model Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseUpdateModelElement(UpdateModelElement object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Move Model Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Move Model Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseMoveModelElement(MoveModelElement object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Add Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Add Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAddAttribute(AddAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Remove Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Remove Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseRemoveAttribute(RemoveAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Update Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Update Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseUpdateAttribute(UpdateAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Add Reference Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Add Reference Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAddReferenceValue(AddReferenceValue object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Remove Reference Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Remove Reference Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseRemoveReferenceValue(RemoveReferenceValue object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Update Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Update Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseUpdateReference(UpdateReference object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Model Input Snapshot</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Model Input Snapshot</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseModelInputSnapshot(ModelInputSnapshot object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public Object defaultCase(EObject object) {
		return null;
	}

} //DiffSwitch
