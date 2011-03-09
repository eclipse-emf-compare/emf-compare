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
 * $Id: MPatchSwitch.java,v 1.1 2010/09/10 15:23:07 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.util;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.ChangeGroup;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.compare.mpatch.IndepAddAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepAddElementChange;
import org.eclipse.emf.compare.mpatch.IndepAddReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemElementChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.IndepElementChange;
import org.eclipse.emf.compare.mpatch.IndepMoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.ModelDescriptorReference;
import org.eclipse.emf.compare.mpatch.UnknownChange;
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
 * @see org.eclipse.emf.compare.mpatch.MPatchPackage
 * @generated
 */
public class MPatchSwitch<T> {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2010, 2011 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static MPatchPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MPatchSwitch() {
		if (modelPackage == null) {
			modelPackage = MPatchPackage.eINSTANCE;
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
			case MPatchPackage.MPATCH_MODEL: {
				MPatchModel mPatchModel = (MPatchModel)theEObject;
				T result = caseMPatchModel(mPatchModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.INDEP_CHANGE: {
				IndepChange indepChange = (IndepChange)theEObject;
				T result = caseIndepChange(indepChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.CHANGE_GROUP: {
				ChangeGroup changeGroup = (ChangeGroup)theEObject;
				T result = caseChangeGroup(changeGroup);
				if (result == null) result = caseIndepChange(changeGroup);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.INDEP_ELEMENT_CHANGE: {
				IndepElementChange indepElementChange = (IndepElementChange)theEObject;
				T result = caseIndepElementChange(indepElementChange);
				if (result == null) result = caseIndepChange(indepElementChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.INDEP_ADD_REM_ELEMENT_CHANGE: {
				IndepAddRemElementChange indepAddRemElementChange = (IndepAddRemElementChange)theEObject;
				T result = caseIndepAddRemElementChange(indepAddRemElementChange);
				if (result == null) result = caseIndepElementChange(indepAddRemElementChange);
				if (result == null) result = caseIndepChange(indepAddRemElementChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.INDEP_ADD_ELEMENT_CHANGE: {
				IndepAddElementChange indepAddElementChange = (IndepAddElementChange)theEObject;
				T result = caseIndepAddElementChange(indepAddElementChange);
				if (result == null) result = caseIndepAddRemElementChange(indepAddElementChange);
				if (result == null) result = caseIndepElementChange(indepAddElementChange);
				if (result == null) result = caseIndepChange(indepAddElementChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.INDEP_REMOVE_ELEMENT_CHANGE: {
				IndepRemoveElementChange indepRemoveElementChange = (IndepRemoveElementChange)theEObject;
				T result = caseIndepRemoveElementChange(indepRemoveElementChange);
				if (result == null) result = caseIndepAddRemElementChange(indepRemoveElementChange);
				if (result == null) result = caseIndepElementChange(indepRemoveElementChange);
				if (result == null) result = caseIndepChange(indepRemoveElementChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.INDEP_ATTRIBUTE_CHANGE: {
				IndepAttributeChange indepAttributeChange = (IndepAttributeChange)theEObject;
				T result = caseIndepAttributeChange(indepAttributeChange);
				if (result == null) result = caseIndepChange(indepAttributeChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.INDEP_ADD_REM_ATTRIBUTE_CHANGE: {
				IndepAddRemAttributeChange indepAddRemAttributeChange = (IndepAddRemAttributeChange)theEObject;
				T result = caseIndepAddRemAttributeChange(indepAddRemAttributeChange);
				if (result == null) result = caseIndepAttributeChange(indepAddRemAttributeChange);
				if (result == null) result = caseIndepChange(indepAddRemAttributeChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.INDEP_MOVE_ELEMENT_CHANGE: {
				IndepMoveElementChange indepMoveElementChange = (IndepMoveElementChange)theEObject;
				T result = caseIndepMoveElementChange(indepMoveElementChange);
				if (result == null) result = caseIndepElementChange(indepMoveElementChange);
				if (result == null) result = caseIndepChange(indepMoveElementChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.INDEP_ADD_ATTRIBUTE_CHANGE: {
				IndepAddAttributeChange indepAddAttributeChange = (IndepAddAttributeChange)theEObject;
				T result = caseIndepAddAttributeChange(indepAddAttributeChange);
				if (result == null) result = caseIndepAddRemAttributeChange(indepAddAttributeChange);
				if (result == null) result = caseIndepAttributeChange(indepAddAttributeChange);
				if (result == null) result = caseIndepChange(indepAddAttributeChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.INDEP_REMOVE_ATTRIBUTE_CHANGE: {
				IndepRemoveAttributeChange indepRemoveAttributeChange = (IndepRemoveAttributeChange)theEObject;
				T result = caseIndepRemoveAttributeChange(indepRemoveAttributeChange);
				if (result == null) result = caseIndepAddRemAttributeChange(indepRemoveAttributeChange);
				if (result == null) result = caseIndepAttributeChange(indepRemoveAttributeChange);
				if (result == null) result = caseIndepChange(indepRemoveAttributeChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.INDEP_UPDATE_ATTRIBUTE_CHANGE: {
				IndepUpdateAttributeChange indepUpdateAttributeChange = (IndepUpdateAttributeChange)theEObject;
				T result = caseIndepUpdateAttributeChange(indepUpdateAttributeChange);
				if (result == null) result = caseIndepAttributeChange(indepUpdateAttributeChange);
				if (result == null) result = caseIndepChange(indepUpdateAttributeChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.INDEP_REFERENCE_CHANGE: {
				IndepReferenceChange indepReferenceChange = (IndepReferenceChange)theEObject;
				T result = caseIndepReferenceChange(indepReferenceChange);
				if (result == null) result = caseIndepChange(indepReferenceChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.INDEP_ADD_REM_REFERENCE_CHANGE: {
				IndepAddRemReferenceChange indepAddRemReferenceChange = (IndepAddRemReferenceChange)theEObject;
				T result = caseIndepAddRemReferenceChange(indepAddRemReferenceChange);
				if (result == null) result = caseIndepReferenceChange(indepAddRemReferenceChange);
				if (result == null) result = caseIndepChange(indepAddRemReferenceChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.INDEP_ADD_REFERENCE_CHANGE: {
				IndepAddReferenceChange indepAddReferenceChange = (IndepAddReferenceChange)theEObject;
				T result = caseIndepAddReferenceChange(indepAddReferenceChange);
				if (result == null) result = caseIndepAddRemReferenceChange(indepAddReferenceChange);
				if (result == null) result = caseIndepReferenceChange(indepAddReferenceChange);
				if (result == null) result = caseIndepChange(indepAddReferenceChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.INDEP_REMOVE_REFERENCE_CHANGE: {
				IndepRemoveReferenceChange indepRemoveReferenceChange = (IndepRemoveReferenceChange)theEObject;
				T result = caseIndepRemoveReferenceChange(indepRemoveReferenceChange);
				if (result == null) result = caseIndepAddRemReferenceChange(indepRemoveReferenceChange);
				if (result == null) result = caseIndepReferenceChange(indepRemoveReferenceChange);
				if (result == null) result = caseIndepChange(indepRemoveReferenceChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.INDEP_UPDATE_REFERENCE_CHANGE: {
				IndepUpdateReferenceChange indepUpdateReferenceChange = (IndepUpdateReferenceChange)theEObject;
				T result = caseIndepUpdateReferenceChange(indepUpdateReferenceChange);
				if (result == null) result = caseIndepReferenceChange(indepUpdateReferenceChange);
				if (result == null) result = caseIndepChange(indepUpdateReferenceChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.IELEMENT_REFERENCE: {
				IElementReference iElementReference = (IElementReference)theEObject;
				T result = caseIElementReference(iElementReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.IMODEL_DESCRIPTOR: {
				IModelDescriptor iModelDescriptor = (IModelDescriptor)theEObject;
				T result = caseIModelDescriptor(iModelDescriptor);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.ELEMENT_REFERENCE_TO_EOBJECT_MAP: {
				@SuppressWarnings("unchecked") Map.Entry<IElementReference, EList<EObject>> elementReferenceToEObjectMap = (Map.Entry<IElementReference, EList<EObject>>)theEObject;
				T result = caseElementReferenceToEObjectMap(elementReferenceToEObjectMap);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.EOBJECT_TO_IMODEL_DESCRIPTOR_MAP: {
				@SuppressWarnings("unchecked") Map.Entry<EObject, IModelDescriptor> eObjectToIModelDescriptorMap = (Map.Entry<EObject, IModelDescriptor>)theEObject;
				T result = caseEObjectToIModelDescriptorMap(eObjectToIModelDescriptorMap);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.UNKNOWN_CHANGE: {
				UnknownChange unknownChange = (UnknownChange)theEObject;
				T result = caseUnknownChange(unknownChange);
				if (result == null) result = caseIndepChange(unknownChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MPatchPackage.MODEL_DESCRIPTOR_REFERENCE: {
				ModelDescriptorReference modelDescriptorReference = (ModelDescriptorReference)theEObject;
				T result = caseModelDescriptorReference(modelDescriptorReference);
				if (result == null) result = caseIElementReference(modelDescriptorReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMPatchModel(MPatchModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Indep Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Indep Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIndepChange(IndepChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Change Group</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Change Group</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseChangeGroup(ChangeGroup object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Indep Element Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Indep Element Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIndepElementChange(IndepElementChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Indep Add Rem Element Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Indep Add Rem Element Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIndepAddRemElementChange(IndepAddRemElementChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Indep Add Element Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Indep Add Element Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIndepAddElementChange(IndepAddElementChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Indep Remove Element Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Indep Remove Element Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIndepRemoveElementChange(IndepRemoveElementChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Indep Attribute Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Indep Attribute Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIndepAttributeChange(IndepAttributeChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Indep Add Rem Attribute Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Indep Add Rem Attribute Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIndepAddRemAttributeChange(IndepAddRemAttributeChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Indep Move Element Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Indep Move Element Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIndepMoveElementChange(IndepMoveElementChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Indep Add Attribute Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Indep Add Attribute Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIndepAddAttributeChange(IndepAddAttributeChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Indep Remove Attribute Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Indep Remove Attribute Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIndepRemoveAttributeChange(IndepRemoveAttributeChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Indep Update Attribute Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Indep Update Attribute Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIndepUpdateAttributeChange(IndepUpdateAttributeChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Indep Reference Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Indep Reference Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIndepReferenceChange(IndepReferenceChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Indep Add Rem Reference Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Indep Add Rem Reference Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIndepAddRemReferenceChange(IndepAddRemReferenceChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Indep Add Reference Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Indep Add Reference Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIndepAddReferenceChange(IndepAddReferenceChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Indep Remove Reference Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Indep Remove Reference Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIndepRemoveReferenceChange(IndepRemoveReferenceChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Indep Update Reference Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Indep Update Reference Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIndepUpdateReferenceChange(IndepUpdateReferenceChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IElement Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IElement Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIElementReference(IElementReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IModel Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IModel Descriptor</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIModelDescriptor(IModelDescriptor object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Element Reference To EObject Map</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Element Reference To EObject Map</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseElementReferenceToEObjectMap(Map.Entry<IElementReference, EList<EObject>> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject To IModel Descriptor Map</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject To IModel Descriptor Map</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEObjectToIModelDescriptorMap(Map.Entry<EObject, IModelDescriptor> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Unknown Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Unknown Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUnknownChange(UnknownChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model Descriptor Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model Descriptor Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModelDescriptorReference(ModelDescriptorReference object) {
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

} //MPatchSwitch
