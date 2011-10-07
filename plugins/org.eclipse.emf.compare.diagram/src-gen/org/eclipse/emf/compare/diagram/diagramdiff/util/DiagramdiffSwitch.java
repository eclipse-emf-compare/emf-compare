/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.emf.compare.diagram.diagramdiff.util;

import java.util.List;

import org.eclipse.emf.compare.diagram.diagramdiff.DiagramDiffExtension;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramEdgeChange;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramHideElement;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramLabelChange;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChange;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChangeRightTarget;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramMoveNode;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramShowElement;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramdiffPackage;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChange;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.MoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.UpdateModelElement;
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
 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramdiffPackage
 * @generated
 */
public class DiagramdiffSwitch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static DiagramdiffPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramdiffSwitch() {
		if (modelPackage == null) {
			modelPackage = DiagramdiffPackage.eINSTANCE;
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
			case DiagramdiffPackage.DIAGRAM_DIFF_EXTENSION: {
				DiagramDiffExtension diagramDiffExtension = (DiagramDiffExtension)theEObject;
				T result = caseDiagramDiffExtension(diagramDiffExtension);
				if (result == null) result = caseDiffElement(diagramDiffExtension);
				if (result == null) result = caseAbstractDiffExtension(diagramDiffExtension);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DiagramdiffPackage.DIAGRAM_SHOW_ELEMENT: {
				DiagramShowElement diagramShowElement = (DiagramShowElement)theEObject;
				T result = caseDiagramShowElement(diagramShowElement);
				if (result == null) result = caseUpdateModelElement(diagramShowElement);
				if (result == null) result = caseDiagramDiffExtension(diagramShowElement);
				if (result == null) result = caseModelElementChange(diagramShowElement);
				if (result == null) result = caseAbstractDiffExtension(diagramShowElement);
				if (result == null) result = caseDiffElement(diagramShowElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DiagramdiffPackage.DIAGRAM_HIDE_ELEMENT: {
				DiagramHideElement diagramHideElement = (DiagramHideElement)theEObject;
				T result = caseDiagramHideElement(diagramHideElement);
				if (result == null) result = caseUpdateModelElement(diagramHideElement);
				if (result == null) result = caseDiagramDiffExtension(diagramHideElement);
				if (result == null) result = caseModelElementChange(diagramHideElement);
				if (result == null) result = caseAbstractDiffExtension(diagramHideElement);
				if (result == null) result = caseDiffElement(diagramHideElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DiagramdiffPackage.DIAGRAM_MOVE_NODE: {
				DiagramMoveNode diagramMoveNode = (DiagramMoveNode)theEObject;
				T result = caseDiagramMoveNode(diagramMoveNode);
				if (result == null) result = caseMoveModelElement(diagramMoveNode);
				if (result == null) result = caseDiagramDiffExtension(diagramMoveNode);
				if (result == null) result = caseUpdateModelElement(diagramMoveNode);
				if (result == null) result = caseAbstractDiffExtension(diagramMoveNode);
				if (result == null) result = caseModelElementChange(diagramMoveNode);
				if (result == null) result = caseDiffElement(diagramMoveNode);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DiagramdiffPackage.DIAGRAM_EDGE_CHANGE: {
				DiagramEdgeChange diagramEdgeChange = (DiagramEdgeChange)theEObject;
				T result = caseDiagramEdgeChange(diagramEdgeChange);
				if (result == null) result = caseUpdateModelElement(diagramEdgeChange);
				if (result == null) result = caseDiagramDiffExtension(diagramEdgeChange);
				if (result == null) result = caseModelElementChange(diagramEdgeChange);
				if (result == null) result = caseAbstractDiffExtension(diagramEdgeChange);
				if (result == null) result = caseDiffElement(diagramEdgeChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DiagramdiffPackage.DIAGRAM_LABEL_CHANGE: {
				DiagramLabelChange diagramLabelChange = (DiagramLabelChange)theEObject;
				T result = caseDiagramLabelChange(diagramLabelChange);
				if (result == null) result = caseUpdateModelElement(diagramLabelChange);
				if (result == null) result = caseDiagramDiffExtension(diagramLabelChange);
				if (result == null) result = caseModelElementChange(diagramLabelChange);
				if (result == null) result = caseAbstractDiffExtension(diagramLabelChange);
				if (result == null) result = caseDiffElement(diagramLabelChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DiagramdiffPackage.DIAGRAM_MODEL_ELEMENT_CHANGE_LEFT_TARGET: {
				DiagramModelElementChangeLeftTarget diagramModelElementChangeLeftTarget = (DiagramModelElementChangeLeftTarget)theEObject;
				T result = caseDiagramModelElementChangeLeftTarget(diagramModelElementChangeLeftTarget);
				if (result == null) result = caseModelElementChangeLeftTarget(diagramModelElementChangeLeftTarget);
				if (result == null) result = caseDiagramModelElementChange(diagramModelElementChangeLeftTarget);
				if (result == null) result = caseDiagramDiffExtension(diagramModelElementChangeLeftTarget);
				if (result == null) result = caseModelElementChange(diagramModelElementChangeLeftTarget);
				if (result == null) result = caseAbstractDiffExtension(diagramModelElementChangeLeftTarget);
				if (result == null) result = caseDiffElement(diagramModelElementChangeLeftTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DiagramdiffPackage.DIAGRAM_MODEL_ELEMENT_CHANGE_RIGHT_TARGET: {
				DiagramModelElementChangeRightTarget diagramModelElementChangeRightTarget = (DiagramModelElementChangeRightTarget)theEObject;
				T result = caseDiagramModelElementChangeRightTarget(diagramModelElementChangeRightTarget);
				if (result == null) result = caseModelElementChangeRightTarget(diagramModelElementChangeRightTarget);
				if (result == null) result = caseDiagramModelElementChange(diagramModelElementChangeRightTarget);
				if (result == null) result = caseDiagramDiffExtension(diagramModelElementChangeRightTarget);
				if (result == null) result = caseModelElementChange(diagramModelElementChangeRightTarget);
				if (result == null) result = caseAbstractDiffExtension(diagramModelElementChangeRightTarget);
				if (result == null) result = caseDiffElement(diagramModelElementChangeRightTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DiagramdiffPackage.DIAGRAM_MODEL_ELEMENT_CHANGE: {
				DiagramModelElementChange diagramModelElementChange = (DiagramModelElementChange)theEObject;
				T result = caseDiagramModelElementChange(diagramModelElementChange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Diagram Diff Extension</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagram Diff Extension</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiagramDiffExtension(DiagramDiffExtension object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Diagram Show Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagram Show Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiagramShowElement(DiagramShowElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Diagram Hide Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagram Hide Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiagramHideElement(DiagramHideElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Diagram Move Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagram Move Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiagramMoveNode(DiagramMoveNode object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Diagram Edge Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagram Edge Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiagramEdgeChange(DiagramEdgeChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Diagram Label Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagram Label Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiagramLabelChange(DiagramLabelChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Diagram Model Element Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagram Model Element Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiagramModelElementChangeLeftTarget(DiagramModelElementChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Diagram Model Element Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagram Model Element Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiagramModelElementChangeRightTarget(DiagramModelElementChangeRightTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Diagram Model Element Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagram Model Element Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiagramModelElementChange(DiagramModelElementChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiffElement(DiffElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Abstract Diff Extension</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Abstract Diff Extension</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAbstractDiffExtension(AbstractDiffExtension object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model Element Change</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model Element Change</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModelElementChange(ModelElementChange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Update Model Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Update Model Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUpdateModelElement(UpdateModelElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Move Model Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Move Model Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMoveModelElement(MoveModelElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model Element Change Left Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model Element Change Left Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModelElementChangeLeftTarget(ModelElementChangeLeftTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model Element Change Right Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model Element Change Right Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModelElementChangeRightTarget(ModelElementChangeRightTarget object) {
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

} //DiagramdiffSwitch
