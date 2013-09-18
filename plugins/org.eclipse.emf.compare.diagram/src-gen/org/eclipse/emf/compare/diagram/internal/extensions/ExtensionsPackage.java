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
package org.eclipse.emf.compare.diagram.internal.extensions;

import org.eclipse.emf.compare.ComparePackage;

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
 * @see org.eclipse.emf.compare.diagram.internal.extensions.ExtensionsFactory
 * @model kind="package"
 * @generated
 */
public interface ExtensionsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "extensions";

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
	ExtensionsPackage eINSTANCE = org.eclipse.emf.compare.diagram.internal.extensions.impl.ExtensionsPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.internal.extensions.impl.DiagramDiffImpl <em>Diagram Diff</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.DiagramDiffImpl
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.ExtensionsPackageImpl#getDiagramDiff()
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
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF__IMPLIES = ComparePackage.DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF__IMPLIED_BY = ComparePackage.DIFF__IMPLIED_BY;

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
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DIFF__PRIME_REFINING = ComparePackage.DIFF__PRIME_REFINING;

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
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.internal.extensions.impl.ShowImpl <em>Show</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.ShowImpl
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.ExtensionsPackageImpl#getShow()
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
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOW__IMPLIES = DIAGRAM_DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOW__IMPLIED_BY = DIAGRAM_DIFF__IMPLIED_BY;

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
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHOW__PRIME_REFINING = DIAGRAM_DIFF__PRIME_REFINING;

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
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.internal.extensions.impl.HideImpl <em>Hide</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.HideImpl
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.ExtensionsPackageImpl#getHide()
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
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIDE__IMPLIES = DIAGRAM_DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIDE__IMPLIED_BY = DIAGRAM_DIFF__IMPLIED_BY;

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
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIDE__PRIME_REFINING = DIAGRAM_DIFF__PRIME_REFINING;

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
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.internal.extensions.impl.NodeChangeImpl <em>Node Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.NodeChangeImpl
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.ExtensionsPackageImpl#getNodeChange()
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
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_CHANGE__IMPLIES = DIAGRAM_DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_CHANGE__IMPLIED_BY = DIAGRAM_DIFF__IMPLIED_BY;

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
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_CHANGE__PRIME_REFINING = DIAGRAM_DIFF__PRIME_REFINING;

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
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.internal.extensions.impl.CoordinatesChangeImpl <em>Coordinates Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.CoordinatesChangeImpl
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.ExtensionsPackageImpl#getCoordinatesChange()
	 * @generated
	 */
	int COORDINATES_CHANGE = 3;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COORDINATES_CHANGE__MATCH = NODE_CHANGE__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COORDINATES_CHANGE__REQUIRES = NODE_CHANGE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COORDINATES_CHANGE__REQUIRED_BY = NODE_CHANGE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COORDINATES_CHANGE__IMPLIES = NODE_CHANGE__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COORDINATES_CHANGE__IMPLIED_BY = NODE_CHANGE__IMPLIED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COORDINATES_CHANGE__REFINES = NODE_CHANGE__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COORDINATES_CHANGE__REFINED_BY = NODE_CHANGE__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COORDINATES_CHANGE__PRIME_REFINING = NODE_CHANGE__PRIME_REFINING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COORDINATES_CHANGE__KIND = NODE_CHANGE__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COORDINATES_CHANGE__SOURCE = NODE_CHANGE__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COORDINATES_CHANGE__STATE = NODE_CHANGE__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COORDINATES_CHANGE__EQUIVALENCE = NODE_CHANGE__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COORDINATES_CHANGE__CONFLICT = NODE_CHANGE__CONFLICT;

	/**
	 * The feature id for the '<em><b>Semantic Diff</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COORDINATES_CHANGE__SEMANTIC_DIFF = NODE_CHANGE__SEMANTIC_DIFF;

	/**
	 * The feature id for the '<em><b>View</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COORDINATES_CHANGE__VIEW = NODE_CHANGE__VIEW;

	/**
	 * The number of structural features of the '<em>Coordinates Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COORDINATES_CHANGE_FEATURE_COUNT = NODE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.internal.extensions.impl.EdgeChangeImpl <em>Edge Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.EdgeChangeImpl
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.ExtensionsPackageImpl#getEdgeChange()
	 * @generated
	 */
	int EDGE_CHANGE = 4;

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
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_CHANGE__IMPLIES = DIAGRAM_DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_CHANGE__IMPLIED_BY = DIAGRAM_DIFF__IMPLIED_BY;

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
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_CHANGE__PRIME_REFINING = DIAGRAM_DIFF__PRIME_REFINING;

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
	 * The meta object id for the '{@link org.eclipse.emf.compare.diagram.internal.extensions.impl.DiagramChangeImpl <em>Diagram Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.DiagramChangeImpl
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.ExtensionsPackageImpl#getDiagramChange()
	 * @generated
	 */
	int DIAGRAM_CHANGE = 6;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_CHANGE__MATCH = DIAGRAM_DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_CHANGE__REQUIRES = DIAGRAM_DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_CHANGE__REQUIRED_BY = DIAGRAM_DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_CHANGE__IMPLIES = DIAGRAM_DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_CHANGE__IMPLIED_BY = DIAGRAM_DIFF__IMPLIED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_CHANGE__REFINES = DIAGRAM_DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_CHANGE__REFINED_BY = DIAGRAM_DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_CHANGE__PRIME_REFINING = DIAGRAM_DIFF__PRIME_REFINING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_CHANGE__KIND = DIAGRAM_DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_CHANGE__SOURCE = DIAGRAM_DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_CHANGE__STATE = DIAGRAM_DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_CHANGE__EQUIVALENCE = DIAGRAM_DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_CHANGE__CONFLICT = DIAGRAM_DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Semantic Diff</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_CHANGE__SEMANTIC_DIFF = DIAGRAM_DIFF__SEMANTIC_DIFF;

	/**
	 * The feature id for the '<em><b>View</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_CHANGE__VIEW = DIAGRAM_DIFF__VIEW;

	/**
	 * The number of structural features of the '<em>Diagram Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_CHANGE_FEATURE_COUNT = DIAGRAM_DIFF_FEATURE_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.internal.extensions.Show <em>Show</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Show</em>'.
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.Show
	 * @generated
	 */
	EClass getShow();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.internal.extensions.Hide <em>Hide</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Hide</em>'.
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.Hide
	 * @generated
	 */
	EClass getHide();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.internal.extensions.NodeChange <em>Node Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node Change</em>'.
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.NodeChange
	 * @generated
	 */
	EClass getNodeChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.internal.extensions.CoordinatesChange <em>Coordinates Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Coordinates Change</em>'.
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.CoordinatesChange
	 * @generated
	 */
	EClass getCoordinatesChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.internal.extensions.EdgeChange <em>Edge Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Edge Change</em>'.
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.EdgeChange
	 * @generated
	 */
	EClass getEdgeChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff <em>Diagram Diff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram Diff</em>'.
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff
	 * @generated
	 */
	EClass getDiagramDiff();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff#getSemanticDiff <em>Semantic Diff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Semantic Diff</em>'.
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff#getSemanticDiff()
	 * @see #getDiagramDiff()
	 * @generated
	 */
	EReference getDiagramDiff_SemanticDiff();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff#getView <em>View</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>View</em>'.
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff#getView()
	 * @see #getDiagramDiff()
	 * @generated
	 */
	EReference getDiagramDiff_View();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diagram.internal.extensions.DiagramChange <em>Diagram Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram Change</em>'.
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.DiagramChange
	 * @generated
	 */
	EClass getDiagramChange();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ExtensionsFactory getExtensionsFactory();

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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.internal.extensions.impl.ShowImpl <em>Show</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.ShowImpl
		 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.ExtensionsPackageImpl#getShow()
		 * @generated
		 */
		EClass SHOW = eINSTANCE.getShow();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.internal.extensions.impl.HideImpl <em>Hide</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.HideImpl
		 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.ExtensionsPackageImpl#getHide()
		 * @generated
		 */
		EClass HIDE = eINSTANCE.getHide();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.internal.extensions.impl.NodeChangeImpl <em>Node Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.NodeChangeImpl
		 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.ExtensionsPackageImpl#getNodeChange()
		 * @generated
		 */
		EClass NODE_CHANGE = eINSTANCE.getNodeChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.internal.extensions.impl.CoordinatesChangeImpl <em>Coordinates Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.CoordinatesChangeImpl
		 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.ExtensionsPackageImpl#getCoordinatesChange()
		 * @generated
		 */
		EClass COORDINATES_CHANGE = eINSTANCE.getCoordinatesChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.internal.extensions.impl.EdgeChangeImpl <em>Edge Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.EdgeChangeImpl
		 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.ExtensionsPackageImpl#getEdgeChange()
		 * @generated
		 */
		EClass EDGE_CHANGE = eINSTANCE.getEdgeChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.internal.extensions.impl.DiagramDiffImpl <em>Diagram Diff</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.DiagramDiffImpl
		 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.ExtensionsPackageImpl#getDiagramDiff()
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

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diagram.internal.extensions.impl.DiagramChangeImpl <em>Diagram Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.DiagramChangeImpl
		 * @see org.eclipse.emf.compare.diagram.internal.extensions.impl.ExtensionsPackageImpl#getDiagramChange()
		 * @generated
		 */
		EClass DIAGRAM_CHANGE = eINSTANCE.getDiagramChange();

	}

} //ExtensionsPackage
