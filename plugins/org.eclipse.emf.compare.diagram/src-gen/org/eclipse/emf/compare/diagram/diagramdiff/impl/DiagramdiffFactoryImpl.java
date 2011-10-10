/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.emf.compare.diagram.diagramdiff.impl;

import org.eclipse.emf.compare.diagram.diagramdiff.DiagramEdgeChange;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramHideElement;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramLabelChange;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChangeRightTarget;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramMoveNode;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramShowElement;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramdiffFactory;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramdiffPackage;
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
public class DiagramdiffFactoryImpl extends EFactoryImpl implements DiagramdiffFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DiagramdiffFactory init() {
		try {
			DiagramdiffFactory theDiagramdiffFactory = (DiagramdiffFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/emf/compare/diff/diagram/1.0"); //$NON-NLS-1$ 
			if (theDiagramdiffFactory != null) {
				return theDiagramdiffFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DiagramdiffFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramdiffFactoryImpl() {
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
			case DiagramdiffPackage.DIAGRAM_SHOW_ELEMENT: return createDiagramShowElement();
			case DiagramdiffPackage.DIAGRAM_HIDE_ELEMENT: return createDiagramHideElement();
			case DiagramdiffPackage.DIAGRAM_MOVE_NODE: return createDiagramMoveNode();
			case DiagramdiffPackage.DIAGRAM_EDGE_CHANGE: return createDiagramEdgeChange();
			case DiagramdiffPackage.DIAGRAM_LABEL_CHANGE: return createDiagramLabelChange();
			case DiagramdiffPackage.DIAGRAM_MODEL_ELEMENT_CHANGE_LEFT_TARGET: return createDiagramModelElementChangeLeftTarget();
			case DiagramdiffPackage.DIAGRAM_MODEL_ELEMENT_CHANGE_RIGHT_TARGET: return createDiagramModelElementChangeRightTarget();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramShowElement createDiagramShowElement() {
		DiagramShowElementImpl diagramShowElement = new DiagramShowElementImpl();
		return diagramShowElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramHideElement createDiagramHideElement() {
		DiagramHideElementImpl diagramHideElement = new DiagramHideElementImpl();
		return diagramHideElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramMoveNode createDiagramMoveNode() {
		DiagramMoveNodeImpl diagramMoveNode = new DiagramMoveNodeImpl();
		return diagramMoveNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramEdgeChange createDiagramEdgeChange() {
		DiagramEdgeChangeImpl diagramEdgeChange = new DiagramEdgeChangeImpl();
		return diagramEdgeChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramLabelChange createDiagramLabelChange() {
		DiagramLabelChangeImpl diagramLabelChange = new DiagramLabelChangeImpl();
		return diagramLabelChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramModelElementChangeLeftTarget createDiagramModelElementChangeLeftTarget() {
		DiagramModelElementChangeLeftTargetImpl diagramModelElementChangeLeftTarget = new DiagramModelElementChangeLeftTargetImpl();
		return diagramModelElementChangeLeftTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramModelElementChangeRightTarget createDiagramModelElementChangeRightTarget() {
		DiagramModelElementChangeRightTargetImpl diagramModelElementChangeRightTarget = new DiagramModelElementChangeRightTargetImpl();
		return diagramModelElementChangeRightTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramdiffPackage getDiagramdiffPackage() {
		return (DiagramdiffPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DiagramdiffPackage getPackage() {
		return DiagramdiffPackage.eINSTANCE;
	}

} //DiagramdiffFactoryImpl
