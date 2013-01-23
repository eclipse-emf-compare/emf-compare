/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.diagram;

import org.eclipse.emf.compare.ComparePackage;

import org.eclipse.emf.ecore.EAttribute;
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
 * @see org.eclipse.emf.compare.diagram.DiagramCompareFactory
 * @model kind="package"
 * @generated
 */
public interface DiagramComparePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "diagram";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare/diagram/2.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "diagramcompare";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DiagramComparePackage eINSTANCE = org.eclipse.emf.compare.diagram.impl.DiagramComparePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.impl.DiagramDiffImpl <em>Diagram Diff</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.impl.DiagramDiffImpl
	 * @see org.eclipse.emf.compare.diagram.impl.DiagramComparePackageImpl#getDiagramDiff()
	 * @generated
	 */
	int DIAGRAM_DIFF = 5;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF__MATCH = ComparePackage.DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF__REQUIRES = ComparePackage.DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF__REQUIRED_BY = ComparePackage.DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF__REFINES = ComparePackage.DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF__REFINED_BY = ComparePackage.DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF__KIND = ComparePackage.DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF__SOURCE = ComparePackage.DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF__STATE = ComparePackage.DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF__EQUIVALENCE = ComparePackage.DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF__CONFLICT = ComparePackage.DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Semantic Diff</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF__SEMANTIC_DIFF = ComparePackage.DIFF_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>View</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF__VIEW = ComparePackage.DIFF_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Diagram Diff</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF_FEATURE_COUNT = ComparePackage.DIFF_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.impl.ShowImpl <em>Show</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.impl.ShowImpl
	 * @see org.eclipse.emf.compare.diagram.impl.DiagramComparePackageImpl#getShow()
	 * @generated
	 */
	int SHOW = 0;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOW__MATCH = DIAGRAM_DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOW__REQUIRES = DIAGRAM_DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOW__REQUIRED_BY = DIAGRAM_DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOW__REFINES = DIAGRAM_DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOW__REFINED_BY = DIAGRAM_DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOW__KIND = DIAGRAM_DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOW__SOURCE = DIAGRAM_DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOW__STATE = DIAGRAM_DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOW__EQUIVALENCE = DIAGRAM_DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOW__CONFLICT = DIAGRAM_DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Semantic Diff</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOW__SEMANTIC_DIFF = DIAGRAM_DIFF__SEMANTIC_DIFF;

	/**
	 * The feature id for the '<em><b>View</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOW__VIEW = DIAGRAM_DIFF__VIEW;

	/**
	 * The number of structural features of the '<em>Show</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOW_FEATURE_COUNT = DIAGRAM_DIFF_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.impl.HideImpl <em>Hide</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.impl.HideImpl
	 * @see org.eclipse.emf.compare.diagram.impl.DiagramComparePackageImpl#getHide()
	 * @generated
	 */
	int HIDE = 1;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIDE__MATCH = DIAGRAM_DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIDE__REQUIRES = DIAGRAM_DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIDE__REQUIRED_BY = DIAGRAM_DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIDE__REFINES = DIAGRAM_DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIDE__REFINED_BY = DIAGRAM_DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIDE__KIND = DIAGRAM_DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIDE__SOURCE = DIAGRAM_DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIDE__STATE = DIAGRAM_DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIDE__EQUIVALENCE = DIAGRAM_DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIDE__CONFLICT = DIAGRAM_DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Semantic Diff</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIDE__SEMANTIC_DIFF = DIAGRAM_DIFF__SEMANTIC_DIFF;

	/**
	 * The feature id for the '<em><b>View</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIDE__VIEW = DIAGRAM_DIFF__VIEW;

	/**
	 * The number of structural features of the '<em>Hide</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIDE_FEATURE_COUNT = DIAGRAM_DIFF_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.impl.NodeChangeImpl <em>Node Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.impl.NodeChangeImpl
	 * @see org.eclipse.emf.compare.diagram.impl.DiagramComparePackageImpl#getNodeChange()
	 * @generated
	 */
	int NODE_CHANGE = 2;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_CHANGE__MATCH = DIAGRAM_DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_CHANGE__REQUIRES = DIAGRAM_DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_CHANGE__REQUIRED_BY = DIAGRAM_DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_CHANGE__REFINES = DIAGRAM_DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_CHANGE__REFINED_BY = DIAGRAM_DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_CHANGE__KIND = DIAGRAM_DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_CHANGE__SOURCE = DIAGRAM_DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_CHANGE__STATE = DIAGRAM_DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_CHANGE__EQUIVALENCE = DIAGRAM_DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_CHANGE__CONFLICT = DIAGRAM_DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Semantic Diff</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_CHANGE__SEMANTIC_DIFF = DIAGRAM_DIFF__SEMANTIC_DIFF;

	/**
	 * The feature id for the '<em><b>View</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_CHANGE__VIEW = DIAGRAM_DIFF__VIEW;

	/**
	 * The number of structural features of the '<em>Node Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_CHANGE_FEATURE_COUNT = DIAGRAM_DIFF_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.impl.EdgeChangeImpl <em>Edge Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.impl.EdgeChangeImpl
	 * @see org.eclipse.emf.compare.diagram.impl.DiagramComparePackageImpl#getEdgeChange()
	 * @generated
	 */
	int EDGE_CHANGE = 3;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_CHANGE__MATCH = DIAGRAM_DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_CHANGE__REQUIRES = DIAGRAM_DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_CHANGE__REQUIRED_BY = DIAGRAM_DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_CHANGE__REFINES = DIAGRAM_DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_CHANGE__REFINED_BY = DIAGRAM_DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_CHANGE__KIND = DIAGRAM_DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_CHANGE__SOURCE = DIAGRAM_DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_CHANGE__STATE = DIAGRAM_DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_CHANGE__EQUIVALENCE = DIAGRAM_DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_CHANGE__CONFLICT = DIAGRAM_DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Semantic Diff</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_CHANGE__SEMANTIC_DIFF = DIAGRAM_DIFF__SEMANTIC_DIFF;

	/**
	 * The feature id for the '<em><b>View</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_CHANGE__VIEW = DIAGRAM_DIFF__VIEW;

	/**
	 * The number of structural features of the '<em>Edge Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_CHANGE_FEATURE_COUNT = DIAGRAM_DIFF_FEATURE_COUNT + 0;


	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.impl.LabelChangeImpl <em>Label Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.impl.LabelChangeImpl
	 * @see org.eclipse.emf.compare.diagram.impl.DiagramComparePackageImpl#getLabelChange()
	 * @generated
	 */
	int LABEL_CHANGE = 4;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABEL_CHANGE__MATCH = DIAGRAM_DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABEL_CHANGE__REQUIRES = DIAGRAM_DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABEL_CHANGE__REQUIRED_BY = DIAGRAM_DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABEL_CHANGE__REFINES = DIAGRAM_DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABEL_CHANGE__REFINED_BY = DIAGRAM_DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABEL_CHANGE__KIND = DIAGRAM_DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABEL_CHANGE__SOURCE = DIAGRAM_DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABEL_CHANGE__STATE = DIAGRAM_DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABEL_CHANGE__EQUIVALENCE = DIAGRAM_DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABEL_CHANGE__CONFLICT = DIAGRAM_DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Semantic Diff</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABEL_CHANGE__SEMANTIC_DIFF = DIAGRAM_DIFF__SEMANTIC_DIFF;

	/**
	 * The feature id for the '<em><b>View</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABEL_CHANGE__VIEW = DIAGRAM_DIFF__VIEW;

	/**
	 * The feature id for the '<em><b>Left</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABEL_CHANGE__LEFT = DIAGRAM_DIFF_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABEL_CHANGE__RIGHT = DIAGRAM_DIFF_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Origin</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABEL_CHANGE__ORIGIN = DIAGRAM_DIFF_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Label Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABEL_CHANGE_FEATURE_COUNT = DIAGRAM_DIFF_FEATURE_COUNT + 3;


	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.Show <em>Show</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Show</em>'.
	 * @see org.eclipse.emf.compare.diagram.Show
	 * @generated
	 */
	EClass getShow();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.Hide <em>Hide</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Hide</em>'.
	 * @see org.eclipse.emf.compare.diagram.Hide
	 * @generated
	 */
	EClass getHide();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.NodeChange <em>Node Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node Change</em>'.
	 * @see org.eclipse.emf.compare.diagram.NodeChange
	 * @generated
	 */
	EClass getNodeChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.EdgeChange <em>Edge Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Edge Change</em>'.
	 * @see org.eclipse.emf.compare.diagram.EdgeChange
	 * @generated
	 */
	EClass getEdgeChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.LabelChange <em>Label Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Label Change</em>'.
	 * @see org.eclipse.emf.compare.diagram.LabelChange
	 * @generated
	 */
	EClass getLabelChange();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diagram.LabelChange#getLeft <em>Left</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Left</em>'.
	 * @see org.eclipse.emf.compare.diagram.LabelChange#getLeft()
	 * @see #getLabelChange()
	 * @generated
	 */
	EAttribute getLabelChange_Left();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diagram.LabelChange#getRight <em>Right</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Right</em>'.
	 * @see org.eclipse.emf.compare.diagram.LabelChange#getRight()
	 * @see #getLabelChange()
	 * @generated
	 */
	EAttribute getLabelChange_Right();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diagram.LabelChange#getOrigin <em>Origin</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Origin</em>'.
	 * @see org.eclipse.emf.compare.diagram.LabelChange#getOrigin()
	 * @see #getLabelChange()
	 * @generated
	 */
	EAttribute getLabelChange_Origin();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.DiagramDiff <em>Diagram Diff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram Diff</em>'.
	 * @see org.eclipse.emf.compare.diagram.DiagramDiff
	 * @generated
	 */
	EClass getDiagramDiff();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diagram.DiagramDiff#getSemanticDiff <em>Semantic Diff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Semantic Diff</em>'.
	 * @see org.eclipse.emf.compare.diagram.DiagramDiff#getSemanticDiff()
	 * @see #getDiagramDiff()
	 * @generated
	 */
	EReference getDiagramDiff_SemanticDiff();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diagram.DiagramDiff#getView <em>View</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>View</em>'.
	 * @see org.eclipse.emf.compare.diagram.DiagramDiff#getView()
	 * @see #getDiagramDiff()
	 * @generated
	 */
	EReference getDiagramDiff_View();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DiagramCompareFactory getDiagramCompareFactory();

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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.impl.ShowImpl <em>Show</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.impl.ShowImpl
		 * @see org.eclipse.emf.compare.diagram.impl.DiagramComparePackageImpl#getShow()
		 * @generated
		 */
		EClass SHOW = eINSTANCE.getShow();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.impl.HideImpl <em>Hide</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.impl.HideImpl
		 * @see org.eclipse.emf.compare.diagram.impl.DiagramComparePackageImpl#getHide()
		 * @generated
		 */
		EClass HIDE = eINSTANCE.getHide();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.impl.NodeChangeImpl <em>Node Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.impl.NodeChangeImpl
		 * @see org.eclipse.emf.compare.diagram.impl.DiagramComparePackageImpl#getNodeChange()
		 * @generated
		 */
		EClass NODE_CHANGE = eINSTANCE.getNodeChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.impl.EdgeChangeImpl <em>Edge Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.impl.EdgeChangeImpl
		 * @see org.eclipse.emf.compare.diagram.impl.DiagramComparePackageImpl#getEdgeChange()
		 * @generated
		 */
		EClass EDGE_CHANGE = eINSTANCE.getEdgeChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.impl.LabelChangeImpl <em>Label Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.impl.LabelChangeImpl
		 * @see org.eclipse.emf.compare.diagram.impl.DiagramComparePackageImpl#getLabelChange()
		 * @generated
		 */
		EClass LABEL_CHANGE = eINSTANCE.getLabelChange();

		/**
		 * The meta object literal for the '<em><b>Left</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LABEL_CHANGE__LEFT = eINSTANCE.getLabelChange_Left();

		/**
		 * The meta object literal for the '<em><b>Right</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LABEL_CHANGE__RIGHT = eINSTANCE.getLabelChange_Right();

		/**
		 * The meta object literal for the '<em><b>Origin</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LABEL_CHANGE__ORIGIN = eINSTANCE.getLabelChange_Origin();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.impl.DiagramDiffImpl <em>Diagram Diff</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.impl.DiagramDiffImpl
		 * @see org.eclipse.emf.compare.diagram.impl.DiagramComparePackageImpl#getDiagramDiff()
		 * @generated
		 */
		EClass DIAGRAM_DIFF = eINSTANCE.getDiagramDiff();

		/**
		 * The meta object literal for the '<em><b>Semantic Diff</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM_DIFF__SEMANTIC_DIFF = eINSTANCE.getDiagramDiff_SemanticDiff();

		/**
		 * The meta object literal for the '<em><b>View</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM_DIFF__VIEW = eINSTANCE.getDiagramDiff_View();

	}

} //DiagramComparePackage
