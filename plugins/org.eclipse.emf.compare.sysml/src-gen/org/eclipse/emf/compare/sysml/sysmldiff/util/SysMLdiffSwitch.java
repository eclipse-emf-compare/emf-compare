/**
 *  Copyright (c) 2011 Atos Origin.
 *  
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *  Atos Origin - Initial API and implementation
 * 
 */
package org.eclipse.emf.compare.sysml.sysmldiff.util;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.AttributeChange;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLDiffExtension;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeAttributeChange;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypePropertyChangeLeftTarget;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypePropertyChangeRightTarget;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceChangeLeftTarget;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceChangeRightTarget;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceOrderChange;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeUpdateAttribute;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeUpdateReference;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLdiffPackage;
import org.eclipse.emf.compare.uml2diff.UMLDiffExtension;
import org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance hierarchy. It supports the call
 * {@link #doSwitch(EObject) doSwitch(object)} to invoke the <code>caseXXX</code> method for each class of the
 * model, starting with the actual class of the object and proceeding up the inheritance hierarchy until a
 * non-null result is returned, which is the result of the switch. <!-- end-user-doc -->
 * 
 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLdiffPackage
 * @generated
 */
public class SysMLdiffSwitch<T> extends Switch<T> {
	/**
	 * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static SysMLdiffPackage modelPackage;

	/**
	 * Creates an instance of the switch. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SysMLdiffSwitch() {
		if (modelPackage == null) {
			modelPackage = SysMLdiffPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields
	 * that result. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case SysMLdiffPackage.SYS_ML_DIFF_EXTENSION: {
				SysMLDiffExtension sysMLDiffExtension = (SysMLDiffExtension)theEObject;
				T result = caseSysMLDiffExtension(sysMLDiffExtension);
				if (result == null)
					result = caseDiffElement(sysMLDiffExtension);
				if (result == null)
					result = caseAbstractDiffExtension(sysMLDiffExtension);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case SysMLdiffPackage.SYS_ML_STEREOTYPE_ATTRIBUTE_CHANGE: {
				SysMLStereotypeAttributeChange sysMLStereotypeAttributeChange = (SysMLStereotypeAttributeChange)theEObject;
				T result = caseSysMLStereotypeAttributeChange(sysMLStereotypeAttributeChange);
				if (result == null)
					result = caseUMLStereotypePropertyChange(sysMLStereotypeAttributeChange);
				if (result == null)
					result = caseUMLDiffExtension(sysMLStereotypeAttributeChange);
				if (result == null)
					result = caseDiffElement(sysMLStereotypeAttributeChange);
				if (result == null)
					result = caseAbstractDiffExtension(sysMLStereotypeAttributeChange);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case SysMLdiffPackage.SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET: {
				SysMLStereotypePropertyChangeLeftTarget sysMLStereotypePropertyChangeLeftTarget = (SysMLStereotypePropertyChangeLeftTarget)theEObject;
				T result = caseSysMLStereotypePropertyChangeLeftTarget(sysMLStereotypePropertyChangeLeftTarget);
				if (result == null)
					result = caseAttributeChangeLeftTarget(sysMLStereotypePropertyChangeLeftTarget);
				if (result == null)
					result = caseSysMLStereotypeAttributeChange(sysMLStereotypePropertyChangeLeftTarget);
				if (result == null)
					result = caseAttributeChange(sysMLStereotypePropertyChangeLeftTarget);
				if (result == null)
					result = caseUMLStereotypePropertyChange(sysMLStereotypePropertyChangeLeftTarget);
				if (result == null)
					result = caseUMLDiffExtension(sysMLStereotypePropertyChangeLeftTarget);
				if (result == null)
					result = caseDiffElement(sysMLStereotypePropertyChangeLeftTarget);
				if (result == null)
					result = caseAbstractDiffExtension(sysMLStereotypePropertyChangeLeftTarget);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case SysMLdiffPackage.SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET: {
				SysMLStereotypePropertyChangeRightTarget sysMLStereotypePropertyChangeRightTarget = (SysMLStereotypePropertyChangeRightTarget)theEObject;
				T result = caseSysMLStereotypePropertyChangeRightTarget(sysMLStereotypePropertyChangeRightTarget);
				if (result == null)
					result = caseAttributeChangeRightTarget(sysMLStereotypePropertyChangeRightTarget);
				if (result == null)
					result = caseSysMLStereotypeAttributeChange(sysMLStereotypePropertyChangeRightTarget);
				if (result == null)
					result = caseAttributeChange(sysMLStereotypePropertyChangeRightTarget);
				if (result == null)
					result = caseUMLStereotypePropertyChange(sysMLStereotypePropertyChangeRightTarget);
				if (result == null)
					result = caseUMLDiffExtension(sysMLStereotypePropertyChangeRightTarget);
				if (result == null)
					result = caseDiffElement(sysMLStereotypePropertyChangeRightTarget);
				if (result == null)
					result = caseAbstractDiffExtension(sysMLStereotypePropertyChangeRightTarget);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case SysMLdiffPackage.SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET: {
				SysMLStereotypeReferenceChangeLeftTarget sysMLStereotypeReferenceChangeLeftTarget = (SysMLStereotypeReferenceChangeLeftTarget)theEObject;
				T result = caseSysMLStereotypeReferenceChangeLeftTarget(sysMLStereotypeReferenceChangeLeftTarget);
				if (result == null)
					result = caseReferenceChangeLeftTarget(sysMLStereotypeReferenceChangeLeftTarget);
				if (result == null)
					result = caseSysMLStereotypeAttributeChange(sysMLStereotypeReferenceChangeLeftTarget);
				if (result == null)
					result = caseReferenceChange(sysMLStereotypeReferenceChangeLeftTarget);
				if (result == null)
					result = caseUMLStereotypePropertyChange(sysMLStereotypeReferenceChangeLeftTarget);
				if (result == null)
					result = caseUMLDiffExtension(sysMLStereotypeReferenceChangeLeftTarget);
				if (result == null)
					result = caseDiffElement(sysMLStereotypeReferenceChangeLeftTarget);
				if (result == null)
					result = caseAbstractDiffExtension(sysMLStereotypeReferenceChangeLeftTarget);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case SysMLdiffPackage.SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET: {
				SysMLStereotypeReferenceChangeRightTarget sysMLStereotypeReferenceChangeRightTarget = (SysMLStereotypeReferenceChangeRightTarget)theEObject;
				T result = caseSysMLStereotypeReferenceChangeRightTarget(sysMLStereotypeReferenceChangeRightTarget);
				if (result == null)
					result = caseReferenceChangeRightTarget(sysMLStereotypeReferenceChangeRightTarget);
				if (result == null)
					result = caseSysMLStereotypeAttributeChange(sysMLStereotypeReferenceChangeRightTarget);
				if (result == null)
					result = caseReferenceChange(sysMLStereotypeReferenceChangeRightTarget);
				if (result == null)
					result = caseUMLStereotypePropertyChange(sysMLStereotypeReferenceChangeRightTarget);
				if (result == null)
					result = caseUMLDiffExtension(sysMLStereotypeReferenceChangeRightTarget);
				if (result == null)
					result = caseDiffElement(sysMLStereotypeReferenceChangeRightTarget);
				if (result == null)
					result = caseAbstractDiffExtension(sysMLStereotypeReferenceChangeRightTarget);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case SysMLdiffPackage.SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE: {
				SysMLStereotypeReferenceOrderChange sysMLStereotypeReferenceOrderChange = (SysMLStereotypeReferenceOrderChange)theEObject;
				T result = caseSysMLStereotypeReferenceOrderChange(sysMLStereotypeReferenceOrderChange);
				if (result == null)
					result = caseReferenceOrderChange(sysMLStereotypeReferenceOrderChange);
				if (result == null)
					result = caseSysMLStereotypeAttributeChange(sysMLStereotypeReferenceOrderChange);
				if (result == null)
					result = caseReferenceChange(sysMLStereotypeReferenceOrderChange);
				if (result == null)
					result = caseUMLStereotypePropertyChange(sysMLStereotypeReferenceOrderChange);
				if (result == null)
					result = caseUMLDiffExtension(sysMLStereotypeReferenceOrderChange);
				if (result == null)
					result = caseDiffElement(sysMLStereotypeReferenceOrderChange);
				if (result == null)
					result = caseAbstractDiffExtension(sysMLStereotypeReferenceOrderChange);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case SysMLdiffPackage.SYS_ML_STEREOTYPE_UPDATE_ATTRIBUTE: {
				SysMLStereotypeUpdateAttribute sysMLStereotypeUpdateAttribute = (SysMLStereotypeUpdateAttribute)theEObject;
				T result = caseSysMLStereotypeUpdateAttribute(sysMLStereotypeUpdateAttribute);
				if (result == null)
					result = caseUpdateAttribute(sysMLStereotypeUpdateAttribute);
				if (result == null)
					result = caseSysMLStereotypeAttributeChange(sysMLStereotypeUpdateAttribute);
				if (result == null)
					result = caseAttributeChange(sysMLStereotypeUpdateAttribute);
				if (result == null)
					result = caseUMLStereotypePropertyChange(sysMLStereotypeUpdateAttribute);
				if (result == null)
					result = caseUMLDiffExtension(sysMLStereotypeUpdateAttribute);
				if (result == null)
					result = caseDiffElement(sysMLStereotypeUpdateAttribute);
				if (result == null)
					result = caseAbstractDiffExtension(sysMLStereotypeUpdateAttribute);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			case SysMLdiffPackage.SYS_ML_STEREOTYPE_UPDATE_REFERENCE: {
				SysMLStereotypeUpdateReference sysMLStereotypeUpdateReference = (SysMLStereotypeUpdateReference)theEObject;
				T result = caseSysMLStereotypeUpdateReference(sysMLStereotypeUpdateReference);
				if (result == null)
					result = caseUpdateReference(sysMLStereotypeUpdateReference);
				if (result == null)
					result = caseSysMLStereotypeAttributeChange(sysMLStereotypeUpdateReference);
				if (result == null)
					result = caseReferenceChange(sysMLStereotypeUpdateReference);
				if (result == null)
					result = caseUMLStereotypePropertyChange(sysMLStereotypeUpdateReference);
				if (result == null)
					result = caseUMLDiffExtension(sysMLStereotypeUpdateReference);
				if (result == null)
					result = caseDiffElement(sysMLStereotypeUpdateReference);
				if (result == null)
					result = caseAbstractDiffExtension(sysMLStereotypeUpdateReference);
				if (result == null)
					result = defaultCase(theEObject);
				return result;
			}
			default:
				return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Sys ML Diff Extension</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Sys ML Diff Extension</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSysMLDiffExtension(SysMLDiffExtension object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Sys ML Stereotype Attribute Change</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Sys ML Stereotype Attribute Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSysMLStereotypeAttributeChange(SysMLStereotypeAttributeChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Sys ML Stereotype Property Change Left Target</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Sys ML Stereotype Property Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSysMLStereotypePropertyChangeLeftTarget(SysMLStereotypePropertyChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Sys ML Stereotype Property Change Right Target</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Sys ML Stereotype Property Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSysMLStereotypePropertyChangeRightTarget(SysMLStereotypePropertyChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Sys ML Stereotype Reference Change Left Target</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Sys ML Stereotype Reference Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSysMLStereotypeReferenceChangeLeftTarget(SysMLStereotypeReferenceChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Sys ML Stereotype Reference Change Right Target</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Sys ML Stereotype Reference Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSysMLStereotypeReferenceChangeRightTarget(SysMLStereotypeReferenceChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Sys ML Stereotype Reference Order Change</em>'. <!-- begin-user-doc --> This implementation returns
	 * null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Sys ML Stereotype Reference Order Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSysMLStereotypeReferenceOrderChange(SysMLStereotypeReferenceOrderChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Sys ML Stereotype Update Attribute</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Sys ML Stereotype Update Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSysMLStereotypeUpdateAttribute(SysMLStereotypeUpdateAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Sys ML Stereotype Update Reference</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Sys ML Stereotype Update Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSysMLStereotypeUpdateReference(SysMLStereotypeUpdateReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Element</em>'. <!-- begin-user-doc
	 * --> This implementation returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiffElement(DiffElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Abstract Diff Extension</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Abstract Diff Extension</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAbstractDiffExtension(AbstractDiffExtension object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UML Diff Extension</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UML Diff Extension</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLDiffExtension(UMLDiffExtension object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>UML Stereotype Property Change</em>'. <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>UML Stereotype Property Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUMLStereotypePropertyChange(UMLStereotypePropertyChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute Change</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAttributeChange(AttributeChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute Change Left Target</em>
	 * '. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute Change Left Target</em>
	 *         '.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAttributeChangeLeftTarget(AttributeChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute Change Right Target</em>
	 * '. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute Change Right Target</em>
	 *         '.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAttributeChangeRightTarget(AttributeChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Reference Change</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Reference Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseReferenceChange(ReferenceChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Reference Change Left Target</em>
	 * '. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Reference Change Left Target</em>
	 *         '.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseReferenceChangeLeftTarget(ReferenceChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Reference Change Right Target</em>
	 * '. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Reference Change Right Target</em>
	 *         '.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseReferenceChangeRightTarget(ReferenceChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Reference Order Change</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Reference Order Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseReferenceOrderChange(ReferenceOrderChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Update Attribute</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Update Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUpdateAttribute(UpdateAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Update Reference</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Update Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUpdateReference(UpdateReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'. <!-- begin-user-doc
	 * --> This implementation returns null; returning a non-null result will terminate the switch, but this
	 * is the last case anyway. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} // SysMLdiffSwitch
