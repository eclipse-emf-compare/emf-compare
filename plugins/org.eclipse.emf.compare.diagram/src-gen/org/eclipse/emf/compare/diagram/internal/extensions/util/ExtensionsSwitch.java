/**
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.diagram.internal.extensions.util;

import org.eclipse.emf.compare.Diff;

import org.eclipse.emf.compare.diagram.internal.extensions.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

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
 * @see org.eclipse.emf.compare.diagram.internal.extensions.ExtensionsPackage
 * @generated
 */
public class ExtensionsSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ExtensionsPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExtensionsSwitch() {
		if (modelPackage == null) {
			modelPackage = ExtensionsPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case ExtensionsPackage.SHOW: {
				Show show = (Show)theEObject;
				T result = caseShow(show);
				if (result == null) result = caseDiagramDiff(show);
				if (result == null) result = caseDiff(show);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExtensionsPackage.HIDE: {
				Hide hide = (Hide)theEObject;
				T result = caseHide(hide);
				if (result == null) result = caseDiagramDiff(hide);
				if (result == null) result = caseDiff(hide);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExtensionsPackage.NODE_CHANGE: {
				NodeChange nodeChange = (NodeChange)theEObject;
				T result = caseNodeChange(nodeChange);
				if (result == null) result = caseDiagramDiff(nodeChange);
				if (result == null) result = caseDiff(nodeChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExtensionsPackage.COORDINATES_CHANGE: {
				CoordinatesChange coordinatesChange = (CoordinatesChange)theEObject;
				T result = caseCoordinatesChange(coordinatesChange);
				if (result == null) result = caseNodeChange(coordinatesChange);
				if (result == null) result = caseDiagramDiff(coordinatesChange);
				if (result == null) result = caseDiff(coordinatesChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExtensionsPackage.EDGE_CHANGE: {
				EdgeChange edgeChange = (EdgeChange)theEObject;
				T result = caseEdgeChange(edgeChange);
				if (result == null) result = caseDiagramDiff(edgeChange);
				if (result == null) result = caseDiff(edgeChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExtensionsPackage.DIAGRAM_DIFF: {
				DiagramDiff diagramDiff = (DiagramDiff)theEObject;
				T result = caseDiagramDiff(diagramDiff);
				if (result == null) result = caseDiff(diagramDiff);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExtensionsPackage.DIAGRAM_CHANGE: {
				DiagramChange diagramChange = (DiagramChange)theEObject;
				T result = caseDiagramChange(diagramChange);
				if (result == null) result = caseDiagramDiff(diagramChange);
				if (result == null) result = caseDiff(diagramChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Show</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Show</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseShow(Show object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Hide</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Hide</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseHide(Hide object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeChange(NodeChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Coordinates Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Coordinates Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCoordinatesChange(CoordinatesChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Edge Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Edge Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEdgeChange(EdgeChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Diagram Diff</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagram Diff</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiagramDiff(DiagramDiff object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Diagram Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagram Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiagramChange(DiagramChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Diff</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diff</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiff(Diff object) {
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
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //ExtensionsSwitch
