/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.emf.compare.diagram.diagramdiff;

import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramdiffFactory
 * @model kind="package"
 * @generated
 */
public interface DiagramdiffPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "diagramdiff";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare/diff/diagram/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "diagramdiff";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DiagramdiffPackage eINSTANCE = org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.diagramdiff.DiagramDiffExtension <em>Diagram Diff Extension</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramDiffExtension
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffPackageImpl#getDiagramDiffExtension()
	 * @generated
	 */
	int DIAGRAM_DIFF_EXTENSION = 0;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF_EXTENSION__SUB_DIFF_ELEMENTS = DiffPackage.DIFF_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF_EXTENSION__IS_HIDDEN_BY = DiffPackage.DIFF_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF_EXTENSION__CONFLICTING = DiffPackage.DIFF_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF_EXTENSION__KIND = DiffPackage.DIFF_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF_EXTENSION__REMOTE = DiffPackage.DIFF_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF_EXTENSION__HIDE_ELEMENTS = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF_EXTENSION__IS_COLLAPSED = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Diagram Diff Extension</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF_EXTENSION_FEATURE_COUNT = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramShowElementImpl <em>Diagram Show Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramShowElementImpl
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffPackageImpl#getDiagramShowElement()
	 * @generated
	 */
	int DIAGRAM_SHOW_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_SHOW_ELEMENT__SUB_DIFF_ELEMENTS = DiffPackage.UPDATE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_SHOW_ELEMENT__IS_HIDDEN_BY = DiffPackage.UPDATE_MODEL_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_SHOW_ELEMENT__CONFLICTING = DiffPackage.UPDATE_MODEL_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_SHOW_ELEMENT__KIND = DiffPackage.UPDATE_MODEL_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_SHOW_ELEMENT__REMOTE = DiffPackage.UPDATE_MODEL_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_SHOW_ELEMENT__RIGHT_ELEMENT = DiffPackage.UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_SHOW_ELEMENT__LEFT_ELEMENT = DiffPackage.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_SHOW_ELEMENT__HIDE_ELEMENTS = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_SHOW_ELEMENT__IS_COLLAPSED = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Diagram Show Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_SHOW_ELEMENT_FEATURE_COUNT = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramHideElementImpl <em>Diagram Hide Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramHideElementImpl
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffPackageImpl#getDiagramHideElement()
	 * @generated
	 */
	int DIAGRAM_HIDE_ELEMENT = 2;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_HIDE_ELEMENT__SUB_DIFF_ELEMENTS = DiffPackage.UPDATE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_HIDE_ELEMENT__IS_HIDDEN_BY = DiffPackage.UPDATE_MODEL_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_HIDE_ELEMENT__CONFLICTING = DiffPackage.UPDATE_MODEL_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_HIDE_ELEMENT__KIND = DiffPackage.UPDATE_MODEL_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_HIDE_ELEMENT__REMOTE = DiffPackage.UPDATE_MODEL_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_HIDE_ELEMENT__RIGHT_ELEMENT = DiffPackage.UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_HIDE_ELEMENT__LEFT_ELEMENT = DiffPackage.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_HIDE_ELEMENT__HIDE_ELEMENTS = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_HIDE_ELEMENT__IS_COLLAPSED = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Diagram Hide Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_HIDE_ELEMENT_FEATURE_COUNT = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramMoveNodeImpl <em>Diagram Move Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramMoveNodeImpl
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffPackageImpl#getDiagramMoveNode()
	 * @generated
	 */
	int DIAGRAM_MOVE_NODE = 3;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MOVE_NODE__SUB_DIFF_ELEMENTS = DiffPackage.MOVE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MOVE_NODE__IS_HIDDEN_BY = DiffPackage.MOVE_MODEL_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MOVE_NODE__CONFLICTING = DiffPackage.MOVE_MODEL_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MOVE_NODE__KIND = DiffPackage.MOVE_MODEL_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MOVE_NODE__REMOTE = DiffPackage.MOVE_MODEL_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MOVE_NODE__RIGHT_ELEMENT = DiffPackage.MOVE_MODEL_ELEMENT__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MOVE_NODE__LEFT_ELEMENT = DiffPackage.MOVE_MODEL_ELEMENT__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MOVE_NODE__LEFT_TARGET = DiffPackage.MOVE_MODEL_ELEMENT__LEFT_TARGET;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MOVE_NODE__RIGHT_TARGET = DiffPackage.MOVE_MODEL_ELEMENT__RIGHT_TARGET;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MOVE_NODE__HIDE_ELEMENTS = DiffPackage.MOVE_MODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MOVE_NODE__IS_COLLAPSED = DiffPackage.MOVE_MODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Diagram Move Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MOVE_NODE_FEATURE_COUNT = DiffPackage.MOVE_MODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramEdgeChangeImpl <em>Diagram Edge Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramEdgeChangeImpl
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffPackageImpl#getDiagramEdgeChange()
	 * @generated
	 */
	int DIAGRAM_EDGE_CHANGE = 4;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_EDGE_CHANGE__SUB_DIFF_ELEMENTS = DiffPackage.UPDATE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_EDGE_CHANGE__IS_HIDDEN_BY = DiffPackage.UPDATE_MODEL_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_EDGE_CHANGE__CONFLICTING = DiffPackage.UPDATE_MODEL_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_EDGE_CHANGE__KIND = DiffPackage.UPDATE_MODEL_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_EDGE_CHANGE__REMOTE = DiffPackage.UPDATE_MODEL_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_EDGE_CHANGE__RIGHT_ELEMENT = DiffPackage.UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_EDGE_CHANGE__LEFT_ELEMENT = DiffPackage.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_EDGE_CHANGE__HIDE_ELEMENTS = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_EDGE_CHANGE__IS_COLLAPSED = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Diagram Edge Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_EDGE_CHANGE_FEATURE_COUNT = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramLabelChangeImpl <em>Diagram Label Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramLabelChangeImpl
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffPackageImpl#getDiagramLabelChange()
	 * @generated
	 */
	int DIAGRAM_LABEL_CHANGE = 5;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_LABEL_CHANGE__SUB_DIFF_ELEMENTS = DiffPackage.UPDATE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_LABEL_CHANGE__IS_HIDDEN_BY = DiffPackage.UPDATE_MODEL_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_LABEL_CHANGE__CONFLICTING = DiffPackage.UPDATE_MODEL_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_LABEL_CHANGE__KIND = DiffPackage.UPDATE_MODEL_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_LABEL_CHANGE__REMOTE = DiffPackage.UPDATE_MODEL_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_LABEL_CHANGE__RIGHT_ELEMENT = DiffPackage.UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_LABEL_CHANGE__LEFT_ELEMENT = DiffPackage.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_LABEL_CHANGE__HIDE_ELEMENTS = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_LABEL_CHANGE__IS_COLLAPSED = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Diagram Label Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_LABEL_CHANGE_FEATURE_COUNT = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramModelElementChangeLeftTargetImpl <em>Diagram Model Element Change Left Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramModelElementChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffPackageImpl#getDiagramModelElementChangeLeftTarget()
	 * @generated
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_LEFT_TARGET = 6;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_LEFT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_LEFT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_LEFT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Semantic Diff</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_LEFT_TARGET__SEMANTIC_DIFF = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_LEFT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_LEFT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Diagram Model Element Change Left Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramModelElementChangeRightTargetImpl <em>Diagram Model Element Change Right Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramModelElementChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffPackageImpl#getDiagramModelElementChangeRightTarget()
	 * @generated
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_RIGHT_TARGET = 7;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_RIGHT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_RIGHT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Left Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Semantic Diff</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_RIGHT_TARGET__SEMANTIC_DIFF = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_RIGHT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Diagram Model Element Change Right Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChange <em>Diagram Model Element Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChange
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffPackageImpl#getDiagramModelElementChange()
	 * @generated
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE = 8;

	/**
	 * The feature id for the '<em><b>Semantic Diff</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE__SEMANTIC_DIFF = 0;

	/**
	 * The number of structural features of the '<em>Diagram Model Element Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_MODEL_ELEMENT_CHANGE_FEATURE_COUNT = 1;

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.diagramdiff.DiagramDiffExtension <em>Diagram Diff Extension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram Diff Extension</em>'.
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramDiffExtension
	 * @generated
	 */
	EClass getDiagramDiffExtension();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.diagramdiff.DiagramShowElement <em>Diagram Show Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram Show Element</em>'.
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramShowElement
	 * @generated
	 */
	EClass getDiagramShowElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.diagramdiff.DiagramHideElement <em>Diagram Hide Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram Hide Element</em>'.
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramHideElement
	 * @generated
	 */
	EClass getDiagramHideElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.diagramdiff.DiagramMoveNode <em>Diagram Move Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram Move Node</em>'.
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramMoveNode
	 * @generated
	 */
	EClass getDiagramMoveNode();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.diagramdiff.DiagramEdgeChange <em>Diagram Edge Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram Edge Change</em>'.
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramEdgeChange
	 * @generated
	 */
	EClass getDiagramEdgeChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.diagramdiff.DiagramLabelChange <em>Diagram Label Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram Label Change</em>'.
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramLabelChange
	 * @generated
	 */
	EClass getDiagramLabelChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChangeLeftTarget <em>Diagram Model Element Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram Model Element Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChangeLeftTarget
	 * @generated
	 */
	EClass getDiagramModelElementChangeLeftTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChangeRightTarget <em>Diagram Model Element Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram Model Element Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChangeRightTarget
	 * @generated
	 */
	EClass getDiagramModelElementChangeRightTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChange <em>Diagram Model Element Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram Model Element Change</em>'.
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChange
	 * @generated
	 */
	EClass getDiagramModelElementChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChange#getSemanticDiff <em>Semantic Diff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Semantic Diff</em>'.
	 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChange#getSemanticDiff()
	 * @see #getDiagramModelElementChange()
	 * @generated
	 */
	EReference getDiagramModelElementChange_SemanticDiff();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DiagramdiffFactory getDiagramdiffFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.diagramdiff.DiagramDiffExtension <em>Diagram Diff Extension</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramDiffExtension
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffPackageImpl#getDiagramDiffExtension()
		 * @generated
		 */
		EClass DIAGRAM_DIFF_EXTENSION = eINSTANCE.getDiagramDiffExtension();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramShowElementImpl <em>Diagram Show Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramShowElementImpl
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffPackageImpl#getDiagramShowElement()
		 * @generated
		 */
		EClass DIAGRAM_SHOW_ELEMENT = eINSTANCE.getDiagramShowElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramHideElementImpl <em>Diagram Hide Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramHideElementImpl
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffPackageImpl#getDiagramHideElement()
		 * @generated
		 */
		EClass DIAGRAM_HIDE_ELEMENT = eINSTANCE.getDiagramHideElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramMoveNodeImpl <em>Diagram Move Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramMoveNodeImpl
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffPackageImpl#getDiagramMoveNode()
		 * @generated
		 */
		EClass DIAGRAM_MOVE_NODE = eINSTANCE.getDiagramMoveNode();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramEdgeChangeImpl <em>Diagram Edge Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramEdgeChangeImpl
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffPackageImpl#getDiagramEdgeChange()
		 * @generated
		 */
		EClass DIAGRAM_EDGE_CHANGE = eINSTANCE.getDiagramEdgeChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramLabelChangeImpl <em>Diagram Label Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramLabelChangeImpl
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffPackageImpl#getDiagramLabelChange()
		 * @generated
		 */
		EClass DIAGRAM_LABEL_CHANGE = eINSTANCE.getDiagramLabelChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramModelElementChangeLeftTargetImpl <em>Diagram Model Element Change Left Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramModelElementChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffPackageImpl#getDiagramModelElementChangeLeftTarget()
		 * @generated
		 */
		EClass DIAGRAM_MODEL_ELEMENT_CHANGE_LEFT_TARGET = eINSTANCE.getDiagramModelElementChangeLeftTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramModelElementChangeRightTargetImpl <em>Diagram Model Element Change Right Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramModelElementChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffPackageImpl#getDiagramModelElementChangeRightTarget()
		 * @generated
		 */
		EClass DIAGRAM_MODEL_ELEMENT_CHANGE_RIGHT_TARGET = eINSTANCE.getDiagramModelElementChangeRightTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChange <em>Diagram Model Element Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChange
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.impl.DiagramdiffPackageImpl#getDiagramModelElementChange()
		 * @generated
		 */
		EClass DIAGRAM_MODEL_ELEMENT_CHANGE = eINSTANCE.getDiagramModelElementChange();

		/**
		 * The meta object literal for the '<em><b>Semantic Diff</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM_MODEL_ELEMENT_CHANGE__SEMANTIC_DIFF = eINSTANCE.getDiagramModelElementChange_SemanticDiff();

	}

} //DiagramdiffPackage
