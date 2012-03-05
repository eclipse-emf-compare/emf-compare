/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.metamodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains accessors for the meta objects to
 * represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.diff.metamodel.DiffFactory
 * @model kind="package"
 * @generated
 */
public interface DiffPackage extends EPackage {
	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AbstractDiffExtensionImpl <em>Abstract Diff Extension</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.AbstractDiffExtensionImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAbstractDiffExtension()
	 * @generated
	 */
	int ABSTRACT_DIFF_EXTENSION = 24;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffElementImpl <em>Element</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffElementImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDiffElement()
	 * @generated
	 */
	int DIFF_ELEMENT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffGroupImpl <em>Group</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffGroupImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDiffGroup()
	 * @generated
	 */
	int DIFF_GROUP = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDiffModel()
	 * @generated
	 */
	int DIFF_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Owned Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFF_MODEL__OWNED_ELEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Left Roots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_MODEL__LEFT_ROOTS = 1;

	/**
	 * The feature id for the '<em><b>Right Roots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_MODEL__RIGHT_ROOTS = 2;

	/**
	 * The feature id for the '<em><b>Ancestor Roots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_MODEL__ANCESTOR_ROOTS = 3;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_MODEL_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffResourceSetImpl <em>Resource Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffResourceSetImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDiffResourceSet()
	 * @generated
	 */
	int DIFF_RESOURCE_SET = 1;

	/**
	 * The feature id for the '<em><b>Diff Models</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_RESOURCE_SET__DIFF_MODELS = 0;

	/**
	 * The feature id for the '<em><b>Resource Diffs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_RESOURCE_SET__RESOURCE_DIFFS = 1;

	/**
	 * The number of structural features of the '<em>Resource Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_RESOURCE_SET_FEATURE_COUNT = 2;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFF_ELEMENT__SUB_DIFF_ELEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_ELEMENT__IS_HIDDEN_BY = 1;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_ELEMENT__CONFLICTING = 2;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFF_ELEMENT__KIND = 3;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_ELEMENT__REMOTE = 4;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int DIFF_ELEMENT__REQUIRES = 5;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int DIFF_ELEMENT__REQUIRED_BY = 6;

	/**
	 * The number of structural features of the '<em>Element</em>' class.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_ELEMENT_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ConflictingDiffElementImpl <em>Conflicting Diff Element</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ConflictingDiffElementImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getConflictingDiffElement()
	 * @generated
	 */
	int CONFLICTING_DIFF_ELEMENT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeImpl <em>Attribute Change</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAttributeChange()
	 * @generated
	 */
	int ATTRIBUTE_CHANGE = 14;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeLeftTargetImpl <em>Attribute Change Left Target</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAttributeChangeLeftTarget()
	 * @generated
	 */
	int ATTRIBUTE_CHANGE_LEFT_TARGET = 15;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeRightTargetImpl <em>Attribute Change Right Target</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAttributeChangeRightTarget()
	 * @generated
	 */
	int ATTRIBUTE_CHANGE_RIGHT_TARGET = 16;

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	DiffPackage eINSTANCE = org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl.init();

	/**
	 * The package name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "diff"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "diff"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare/diff/1.1"; //$NON-NLS-1$

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONFLICTING_DIFF_ELEMENT__SUB_DIFF_ELEMENTS = DIFF_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFLICTING_DIFF_ELEMENT__IS_HIDDEN_BY = DIFF_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFLICTING_DIFF_ELEMENT__CONFLICTING = DIFF_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONFLICTING_DIFF_ELEMENT__KIND = DIFF_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFLICTING_DIFF_ELEMENT__REMOTE = DIFF_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int CONFLICTING_DIFF_ELEMENT__REQUIRES = DIFF_ELEMENT__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int CONFLICTING_DIFF_ELEMENT__REQUIRED_BY = DIFF_ELEMENT__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Left Parent</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFLICTING_DIFF_ELEMENT__LEFT_PARENT = DIFF_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFLICTING_DIFF_ELEMENT__RIGHT_PARENT = DIFF_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Origin Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFLICTING_DIFF_ELEMENT__ORIGIN_ELEMENT = DIFF_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Conflicting Diff Element</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONFLICTING_DIFF_ELEMENT_FEATURE_COUNT = DIFF_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFF_GROUP__SUB_DIFF_ELEMENTS = DIFF_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_GROUP__IS_HIDDEN_BY = DIFF_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_GROUP__CONFLICTING = DIFF_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFF_GROUP__KIND = DIFF_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_GROUP__REMOTE = DIFF_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int DIFF_GROUP__REQUIRES = DIFF_ELEMENT__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int DIFF_GROUP__REQUIRED_BY = DIFF_ELEMENT__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_GROUP__RIGHT_PARENT = DIFF_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Subchanges</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_GROUP__SUBCHANGES = DIFF_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Group</em>' class.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_GROUP_FEATURE_COUNT = DIFF_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot <em>Comparison Snapshot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getComparisonSnapshot()
	 * @generated
	 */
	int COMPARISON_SNAPSHOT = 5;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPARISON_SNAPSHOT__DATE = 0;

	/**
	 * The number of structural features of the '<em>Comparison Snapshot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPARISON_SNAPSHOT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ComparisonResourceSnapshotImpl <em>Comparison Resource Snapshot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ComparisonResourceSnapshotImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getComparisonResourceSnapshot()
	 * @generated
	 */
	int COMPARISON_RESOURCE_SNAPSHOT = 6;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPARISON_RESOURCE_SNAPSHOT__DATE = COMPARISON_SNAPSHOT__DATE;

	/**
	 * The feature id for the '<em><b>Diff</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPARISON_RESOURCE_SNAPSHOT__DIFF = COMPARISON_SNAPSHOT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Match</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPARISON_RESOURCE_SNAPSHOT__MATCH = COMPARISON_SNAPSHOT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Comparison Resource Snapshot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPARISON_RESOURCE_SNAPSHOT_FEATURE_COUNT = COMPARISON_SNAPSHOT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ComparisonResourceSetSnapshotImpl <em>Comparison Resource Set Snapshot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ComparisonResourceSetSnapshotImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getComparisonResourceSetSnapshot()
	 * @generated
	 */
	int COMPARISON_RESOURCE_SET_SNAPSHOT = 7;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPARISON_RESOURCE_SET_SNAPSHOT__DATE = COMPARISON_SNAPSHOT__DATE;

	/**
	 * The feature id for the '<em><b>Diff Resource Set</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPARISON_RESOURCE_SET_SNAPSHOT__DIFF_RESOURCE_SET = COMPARISON_SNAPSHOT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Match Resource Set</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPARISON_RESOURCE_SET_SNAPSHOT__MATCH_RESOURCE_SET = COMPARISON_SNAPSHOT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Comparison Resource Set Snapshot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPARISON_RESOURCE_SET_SNAPSHOT_FEATURE_COUNT = COMPARISON_SNAPSHOT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '<em>IMerger</em>' data type. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.IMerger
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getIMerger()
	 * @generated
	 */
	int IMERGER = 30;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeImpl <em>Model Element Change</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getModelElementChange()
	 * @generated
	 */
	int MODEL_ELEMENT_CHANGE = 8;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeLeftTargetImpl <em>Model Element Change Left Target</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getModelElementChangeLeftTarget()
	 * @generated
	 */
	int MODEL_ELEMENT_CHANGE_LEFT_TARGET = 9;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeRightTargetImpl <em>Model Element Change Right Target</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getModelElementChangeRightTarget()
	 * @generated
	 */
	int MODEL_ELEMENT_CHANGE_RIGHT_TARGET = 10;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE__SUB_DIFF_ELEMENTS = DIFF_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE__IS_HIDDEN_BY = DIFF_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE__CONFLICTING = DIFF_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE__KIND = DIFF_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE__REMOTE = DIFF_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int MODEL_ELEMENT_CHANGE__REQUIRES = DIFF_ELEMENT__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int MODEL_ELEMENT_CHANGE__REQUIRED_BY = DIFF_ELEMENT__REQUIRED_BY;

	/**
	 * The number of structural features of the '<em>Model Element Change</em>' class.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_FEATURE_COUNT = DIFF_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = MODEL_ELEMENT_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = MODEL_ELEMENT_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_LEFT_TARGET__CONFLICTING = MODEL_ELEMENT_CHANGE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_LEFT_TARGET__KIND = MODEL_ELEMENT_CHANGE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_LEFT_TARGET__REMOTE = MODEL_ELEMENT_CHANGE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRES = MODEL_ELEMENT_CHANGE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRED_BY = MODEL_ELEMENT_CHANGE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Model Element Change Left Target</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = MODEL_ELEMENT_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = MODEL_ELEMENT_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_RIGHT_TARGET__CONFLICTING = MODEL_ELEMENT_CHANGE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_RIGHT_TARGET__KIND = MODEL_ELEMENT_CHANGE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REMOTE = MODEL_ELEMENT_CHANGE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRES = MODEL_ELEMENT_CHANGE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRED_BY = MODEL_ELEMENT_CHANGE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Left Parent</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Model Element Change Right Target</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeImpl <em>Reference Change</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getReferenceChange()
	 * @generated
	 */
	int REFERENCE_CHANGE = 19;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeLeftTargetImpl <em>Reference Change Left Target</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getReferenceChangeLeftTarget()
	 * @generated
	 */
	int REFERENCE_CHANGE_LEFT_TARGET = 20;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeRightTargetImpl <em>Reference Change Right Target</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getReferenceChangeRightTarget()
	 * @generated
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET = 21;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateAttributeImpl <em>Update Attribute</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateAttributeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateAttribute()
	 * @generated
	 */
	int UPDATE_ATTRIBUTE = 18;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateModelElementImpl <em>Update Model Element</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateModelElementImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateModelElement()
	 * @generated
	 */
	int UPDATE_MODEL_ELEMENT = 11;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS = MODEL_ELEMENT_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_MODEL_ELEMENT__IS_HIDDEN_BY = MODEL_ELEMENT_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_MODEL_ELEMENT__CONFLICTING = MODEL_ELEMENT_CHANGE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_MODEL_ELEMENT__KIND = MODEL_ELEMENT_CHANGE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_MODEL_ELEMENT__REMOTE = MODEL_ELEMENT_CHANGE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int UPDATE_MODEL_ELEMENT__REQUIRES = MODEL_ELEMENT_CHANGE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int UPDATE_MODEL_ELEMENT__REQUIRED_BY = MODEL_ELEMENT_CHANGE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_MODEL_ELEMENT__LEFT_ELEMENT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Update Model Element</em>' class.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_MODEL_ELEMENT_FEATURE_COUNT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateReferenceImpl <em>Update Reference</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateReferenceImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateReference()
	 * @generated
	 */
	int UPDATE_REFERENCE = 22;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.MoveModelElementImpl <em>Move Model Element</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.MoveModelElementImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getMoveModelElement()
	 * @generated
	 */
	int MOVE_MODEL_ELEMENT = 12;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS = UPDATE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__IS_HIDDEN_BY = UPDATE_MODEL_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__CONFLICTING = UPDATE_MODEL_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__KIND = UPDATE_MODEL_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__REMOTE = UPDATE_MODEL_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int MOVE_MODEL_ELEMENT__REQUIRES = UPDATE_MODEL_ELEMENT__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int MOVE_MODEL_ELEMENT__REQUIRED_BY = UPDATE_MODEL_ELEMENT__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__RIGHT_ELEMENT = UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__LEFT_ELEMENT = UPDATE_MODEL_ELEMENT__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__LEFT_TARGET = UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__RIGHT_TARGET = UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Move Model Element</em>' class.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT_FEATURE_COUNT = UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateContainmentFeatureImpl <em>Update Containment Feature</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateContainmentFeatureImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateContainmentFeature()
	 * @generated
	 */
	int UPDATE_CONTAINMENT_FEATURE = 13;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_CONTAINMENT_FEATURE__SUB_DIFF_ELEMENTS = MOVE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_CONTAINMENT_FEATURE__IS_HIDDEN_BY = MOVE_MODEL_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_CONTAINMENT_FEATURE__CONFLICTING = MOVE_MODEL_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_CONTAINMENT_FEATURE__KIND = MOVE_MODEL_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_CONTAINMENT_FEATURE__REMOTE = MOVE_MODEL_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int UPDATE_CONTAINMENT_FEATURE__REQUIRES = MOVE_MODEL_ELEMENT__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int UPDATE_CONTAINMENT_FEATURE__REQUIRED_BY = MOVE_MODEL_ELEMENT__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_CONTAINMENT_FEATURE__RIGHT_ELEMENT = MOVE_MODEL_ELEMENT__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_CONTAINMENT_FEATURE__LEFT_ELEMENT = MOVE_MODEL_ELEMENT__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_CONTAINMENT_FEATURE__LEFT_TARGET = MOVE_MODEL_ELEMENT__LEFT_TARGET;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_CONTAINMENT_FEATURE__RIGHT_TARGET = MOVE_MODEL_ELEMENT__RIGHT_TARGET;

	/**
	 * The number of structural features of the '<em>Update Containment Feature</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_CONTAINMENT_FEATURE_FEATURE_COUNT = MOVE_MODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__SUB_DIFF_ELEMENTS = DIFF_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__IS_HIDDEN_BY = DIFF_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__CONFLICTING = DIFF_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__KIND = DIFF_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__REMOTE = DIFF_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int ATTRIBUTE_CHANGE__REQUIRES = DIFF_ELEMENT__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int ATTRIBUTE_CHANGE__REQUIRED_BY = DIFF_ELEMENT__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__ATTRIBUTE = DIFF_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__LEFT_ELEMENT = DIFF_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__RIGHT_ELEMENT = DIFF_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Attribute Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_FEATURE_COUNT = DIFF_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = ATTRIBUTE_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = ATTRIBUTE_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_LEFT_TARGET__CONFLICTING = ATTRIBUTE_CHANGE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_LEFT_TARGET__KIND = ATTRIBUTE_CHANGE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_LEFT_TARGET__REMOTE = ATTRIBUTE_CHANGE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int ATTRIBUTE_CHANGE_LEFT_TARGET__REQUIRES = ATTRIBUTE_CHANGE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int ATTRIBUTE_CHANGE_LEFT_TARGET__REQUIRED_BY = ATTRIBUTE_CHANGE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_LEFT_TARGET__ATTRIBUTE = ATTRIBUTE_CHANGE__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_ELEMENT = ATTRIBUTE_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_LEFT_TARGET__RIGHT_ELEMENT = ATTRIBUTE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_TARGET = ATTRIBUTE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Attribute Change Left Target</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_LEFT_TARGET_FEATURE_COUNT = ATTRIBUTE_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = ATTRIBUTE_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = ATTRIBUTE_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_RIGHT_TARGET__CONFLICTING = ATTRIBUTE_CHANGE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_RIGHT_TARGET__KIND = ATTRIBUTE_CHANGE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_RIGHT_TARGET__REMOTE = ATTRIBUTE_CHANGE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int ATTRIBUTE_CHANGE_RIGHT_TARGET__REQUIRES = ATTRIBUTE_CHANGE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int ATTRIBUTE_CHANGE_RIGHT_TARGET__REQUIRED_BY = ATTRIBUTE_CHANGE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_RIGHT_TARGET__ATTRIBUTE = ATTRIBUTE_CHANGE__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_RIGHT_TARGET__LEFT_ELEMENT = ATTRIBUTE_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = ATTRIBUTE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_TARGET = ATTRIBUTE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Attribute Change Right Target</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_RIGHT_TARGET_FEATURE_COUNT = ATTRIBUTE_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeOrderChangeImpl <em>Attribute Order Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.AttributeOrderChangeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAttributeOrderChange()
	 * @generated
	 * @since 1.3
	 */
	int ATTRIBUTE_ORDER_CHANGE = 17;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int ATTRIBUTE_ORDER_CHANGE__SUB_DIFF_ELEMENTS = ATTRIBUTE_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int ATTRIBUTE_ORDER_CHANGE__IS_HIDDEN_BY = ATTRIBUTE_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int ATTRIBUTE_ORDER_CHANGE__CONFLICTING = ATTRIBUTE_CHANGE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int ATTRIBUTE_ORDER_CHANGE__KIND = ATTRIBUTE_CHANGE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int ATTRIBUTE_ORDER_CHANGE__REMOTE = ATTRIBUTE_CHANGE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int ATTRIBUTE_ORDER_CHANGE__REQUIRES = ATTRIBUTE_CHANGE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int ATTRIBUTE_ORDER_CHANGE__REQUIRED_BY = ATTRIBUTE_CHANGE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int ATTRIBUTE_ORDER_CHANGE__ATTRIBUTE = ATTRIBUTE_CHANGE__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int ATTRIBUTE_ORDER_CHANGE__LEFT_ELEMENT = ATTRIBUTE_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int ATTRIBUTE_ORDER_CHANGE__RIGHT_ELEMENT = ATTRIBUTE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The number of structural features of the '<em>Attribute Order Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int ATTRIBUTE_ORDER_CHANGE_FEATURE_COUNT = ATTRIBUTE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE__SUB_DIFF_ELEMENTS = ATTRIBUTE_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE__IS_HIDDEN_BY = ATTRIBUTE_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE__CONFLICTING = ATTRIBUTE_CHANGE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE__KIND = ATTRIBUTE_CHANGE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE__REMOTE = ATTRIBUTE_CHANGE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int UPDATE_ATTRIBUTE__REQUIRES = ATTRIBUTE_CHANGE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int UPDATE_ATTRIBUTE__REQUIRED_BY = ATTRIBUTE_CHANGE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE__ATTRIBUTE = ATTRIBUTE_CHANGE__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE__LEFT_ELEMENT = ATTRIBUTE_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE__RIGHT_ELEMENT = ATTRIBUTE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The number of structural features of the '<em>Update Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE_FEATURE_COUNT = ATTRIBUTE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__SUB_DIFF_ELEMENTS = DIFF_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__IS_HIDDEN_BY = DIFF_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__CONFLICTING = DIFF_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__KIND = DIFF_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__REMOTE = DIFF_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int REFERENCE_CHANGE__REQUIRES = DIFF_ELEMENT__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int REFERENCE_CHANGE__REQUIRED_BY = DIFF_ELEMENT__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__REFERENCE = DIFF_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__RIGHT_ELEMENT = DIFF_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__LEFT_ELEMENT = DIFF_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Reference Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_FEATURE_COUNT = DIFF_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = REFERENCE_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = REFERENCE_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_LEFT_TARGET__CONFLICTING = REFERENCE_CHANGE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_LEFT_TARGET__KIND = REFERENCE_CHANGE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_LEFT_TARGET__REMOTE = REFERENCE_CHANGE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int REFERENCE_CHANGE_LEFT_TARGET__REQUIRES = REFERENCE_CHANGE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int REFERENCE_CHANGE_LEFT_TARGET__REQUIRED_BY = REFERENCE_CHANGE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_LEFT_TARGET__REFERENCE = REFERENCE_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_LEFT_TARGET__RIGHT_ELEMENT = REFERENCE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_LEFT_TARGET__LEFT_ELEMENT = REFERENCE_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_LEFT_TARGET__LEFT_TARGET = REFERENCE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_LEFT_TARGET__RIGHT_TARGET = REFERENCE_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Reference Change Left Target</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_LEFT_TARGET_FEATURE_COUNT = REFERENCE_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = REFERENCE_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = REFERENCE_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET__CONFLICTING = REFERENCE_CHANGE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET__KIND = REFERENCE_CHANGE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET__REMOTE = REFERENCE_CHANGE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET__REQUIRES = REFERENCE_CHANGE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET__REQUIRED_BY = REFERENCE_CHANGE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET__REFERENCE = REFERENCE_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = REFERENCE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET__LEFT_ELEMENT = REFERENCE_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_TARGET = REFERENCE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET__LEFT_TARGET = REFERENCE_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Reference Change Right Target</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET_FEATURE_COUNT = REFERENCE_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE__SUB_DIFF_ELEMENTS = REFERENCE_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE__IS_HIDDEN_BY = REFERENCE_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE__CONFLICTING = REFERENCE_CHANGE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE__KIND = REFERENCE_CHANGE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE__REMOTE = REFERENCE_CHANGE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int UPDATE_REFERENCE__REQUIRES = REFERENCE_CHANGE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int UPDATE_REFERENCE__REQUIRED_BY = REFERENCE_CHANGE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE__REFERENCE = REFERENCE_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE__RIGHT_ELEMENT = REFERENCE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE__LEFT_ELEMENT = REFERENCE_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE__LEFT_TARGET = REFERENCE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE__RIGHT_TARGET = REFERENCE_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Update Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE_FEATURE_COUNT = REFERENCE_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceOrderChangeImpl <em>Reference Order Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ReferenceOrderChangeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getReferenceOrderChange()
	 * @generated
	 */
	int REFERENCE_ORDER_CHANGE = 23;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_ORDER_CHANGE__SUB_DIFF_ELEMENTS = REFERENCE_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_ORDER_CHANGE__IS_HIDDEN_BY = REFERENCE_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_ORDER_CHANGE__CONFLICTING = REFERENCE_CHANGE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_ORDER_CHANGE__KIND = REFERENCE_CHANGE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_ORDER_CHANGE__REMOTE = REFERENCE_CHANGE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int REFERENCE_ORDER_CHANGE__REQUIRES = REFERENCE_CHANGE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int REFERENCE_ORDER_CHANGE__REQUIRED_BY = REFERENCE_CHANGE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_ORDER_CHANGE__REFERENCE = REFERENCE_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_ORDER_CHANGE__RIGHT_ELEMENT = REFERENCE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_ORDER_CHANGE__LEFT_ELEMENT = REFERENCE_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_ORDER_CHANGE__LEFT_TARGET = REFERENCE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_ORDER_CHANGE__RIGHT_TARGET = REFERENCE_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Reference Order Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_ORDER_CHANGE_FEATURE_COUNT = REFERENCE_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_DIFF_EXTENSION__IS_COLLAPSED = 1;

	/**
	 * The number of structural features of the '<em>Abstract Diff Extension</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_DIFF_EXTENSION_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ResourceDiffImpl <em>Resource Diff</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ResourceDiffImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getResourceDiff()
	 * @generated
	 */
	int RESOURCE_DIFF = 25;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DIFF__SUB_DIFF_ELEMENTS = DIFF_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DIFF__IS_HIDDEN_BY = DIFF_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DIFF__CONFLICTING = DIFF_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DIFF__KIND = DIFF_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DIFF__REMOTE = DIFF_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int RESOURCE_DIFF__REQUIRES = DIFF_ELEMENT__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int RESOURCE_DIFF__REQUIRED_BY = DIFF_ELEMENT__REQUIRED_BY;

	/**
	 * The number of structural features of the '<em>Resource Diff</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DIFF_FEATURE_COUNT = DIFF_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ResourceDependencyChangeImpl <em>Resource Dependency Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ResourceDependencyChangeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getResourceDependencyChange()
	 * @generated
	 */
	int RESOURCE_DEPENDENCY_CHANGE = 26;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE__SUB_DIFF_ELEMENTS = RESOURCE_DIFF__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE__IS_HIDDEN_BY = RESOURCE_DIFF__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE__CONFLICTING = RESOURCE_DIFF__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE__KIND = RESOURCE_DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE__REMOTE = RESOURCE_DIFF__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int RESOURCE_DEPENDENCY_CHANGE__REQUIRES = RESOURCE_DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int RESOURCE_DEPENDENCY_CHANGE__REQUIRED_BY = RESOURCE_DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Roots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE__ROOTS = RESOURCE_DIFF_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Resource Dependency Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE_FEATURE_COUNT = RESOURCE_DIFF_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ResourceDependencyChangeLeftTargetImpl <em>Resource Dependency Change Left Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ResourceDependencyChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getResourceDependencyChangeLeftTarget()
	 * @generated
	 */
	int RESOURCE_DEPENDENCY_CHANGE_LEFT_TARGET = 27;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = RESOURCE_DEPENDENCY_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = RESOURCE_DEPENDENCY_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE_LEFT_TARGET__CONFLICTING = RESOURCE_DEPENDENCY_CHANGE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE_LEFT_TARGET__KIND = RESOURCE_DEPENDENCY_CHANGE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE_LEFT_TARGET__REMOTE = RESOURCE_DEPENDENCY_CHANGE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int RESOURCE_DEPENDENCY_CHANGE_LEFT_TARGET__REQUIRES = RESOURCE_DEPENDENCY_CHANGE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int RESOURCE_DEPENDENCY_CHANGE_LEFT_TARGET__REQUIRED_BY = RESOURCE_DEPENDENCY_CHANGE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Roots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE_LEFT_TARGET__ROOTS = RESOURCE_DEPENDENCY_CHANGE__ROOTS;

	/**
	 * The number of structural features of the '<em>Resource Dependency Change Left Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE_LEFT_TARGET_FEATURE_COUNT = RESOURCE_DEPENDENCY_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ResourceDependencyChangeRightTargetImpl <em>Resource Dependency Change Right Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ResourceDependencyChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getResourceDependencyChangeRightTarget()
	 * @generated
	 */
	int RESOURCE_DEPENDENCY_CHANGE_RIGHT_TARGET = 28;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = RESOURCE_DEPENDENCY_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = RESOURCE_DEPENDENCY_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE_RIGHT_TARGET__CONFLICTING = RESOURCE_DEPENDENCY_CHANGE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE_RIGHT_TARGET__KIND = RESOURCE_DEPENDENCY_CHANGE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE_RIGHT_TARGET__REMOTE = RESOURCE_DEPENDENCY_CHANGE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int RESOURCE_DEPENDENCY_CHANGE_RIGHT_TARGET__REQUIRES = RESOURCE_DEPENDENCY_CHANGE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	int RESOURCE_DEPENDENCY_CHANGE_RIGHT_TARGET__REQUIRED_BY = RESOURCE_DEPENDENCY_CHANGE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Roots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE_RIGHT_TARGET__ROOTS = RESOURCE_DEPENDENCY_CHANGE__ROOTS;

	/**
	 * The number of structural features of the '<em>Resource Dependency Change Right Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DEPENDENCY_CHANGE_RIGHT_TARGET_FEATURE_COUNT = RESOURCE_DEPENDENCY_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.DifferenceKind <em>Difference Kind</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.diff.metamodel.DifferenceKind
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDifferenceKind()
	 * @generated
	 */
	int DIFFERENCE_KIND = 29;

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension <em>Abstract Diff Extension</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Diff Extension</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension
	 * @generated
	 */
	EClass getAbstractDiffExtension();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension#getHideElements <em>Hide Elements</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Hide Elements</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension#getHideElements()
	 * @see #getAbstractDiffExtension()
	 * @generated
	 */
	EReference getAbstractDiffExtension_HideElements();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension#isIsCollapsed <em>Is Collapsed</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Collapsed</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension#isIsCollapsed()
	 * @see #getAbstractDiffExtension()
	 * @generated
	 */
	EAttribute getAbstractDiffExtension_IsCollapsed();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ResourceDiff <em>Resource Diff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource Diff</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ResourceDiff
	 * @generated
	 */
	EClass getResourceDiff();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChange <em>Resource Dependency Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource Dependency Change</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChange
	 * @generated
	 */
	EClass getResourceDependencyChange();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChange#getRoots <em>Roots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Roots</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChange#getRoots()
	 * @see #getResourceDependencyChange()
	 * @generated
	 */
	EReference getResourceDependencyChange_Roots();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChangeLeftTarget <em>Resource Dependency Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource Dependency Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChangeLeftTarget
	 * @generated
	 */
	EClass getResourceDependencyChangeLeftTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChangeRightTarget <em>Resource Dependency Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource Dependency Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChangeRightTarget
	 * @generated
	 */
	EClass getResourceDependencyChangeRightTarget();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.emf.compare.diff.metamodel.DifferenceKind <em>Difference Kind</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Difference Kind</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DifferenceKind
	 * @generated
	 */
	EEnum getDifferenceKind();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChange <em>Attribute Change</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute Change</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChange
	 * @generated
	 */
	EClass getAttributeChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChange#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Attribute</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChange#getAttribute()
	 * @see #getAttributeChange()
	 * @generated
	 */
	EReference getAttributeChange_Attribute();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChange#getLeftElement <em>Left Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChange#getLeftElement()
	 * @see #getAttributeChange()
	 * @generated
	 */
	EReference getAttributeChange_LeftElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChange#getRightElement <em>Right Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChange#getRightElement()
	 * @see #getAttributeChange()
	 * @generated
	 */
	EReference getAttributeChange_RightElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget <em>Attribute Change Left Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget
	 * @generated
	 */
	EClass getAttributeChangeLeftTarget();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget#getLeftTarget <em>Left Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Left Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget#getLeftTarget()
	 * @see #getAttributeChangeLeftTarget()
	 * @generated
	 */
	EAttribute getAttributeChangeLeftTarget_LeftTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget <em>Attribute Change Right Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget
	 * @generated
	 */
	EClass getAttributeChangeRightTarget();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget#getRightTarget <em>Right Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Right Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget#getRightTarget()
	 * @see #getAttributeChangeRightTarget()
	 * @generated
	 */
	EAttribute getAttributeChangeRightTarget_RightTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.AttributeOrderChange <em>Attribute Order Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute Order Change</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeOrderChange
	 * @generated
	 * @since 1.3
	 */
	EClass getAttributeOrderChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement <em>Conflicting Diff Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Conflicting Diff Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement
	 * @generated
	 */
	EClass getConflictingDiffElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getLeftParent <em>Left Parent</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left Parent</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getLeftParent()
	 * @see #getConflictingDiffElement()
	 * @generated
	 */
	EReference getConflictingDiffElement_LeftParent();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getOriginElement <em>Origin Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Origin Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getOriginElement()
	 * @see #getConflictingDiffElement()
	 * @generated
	 */
	EReference getConflictingDiffElement_OriginElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getRightParent <em>Right Parent</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right Parent</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getRightParent()
	 * @see #getConflictingDiffElement()
	 * @generated
	 */
	EReference getConflictingDiffElement_RightParent();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.DiffElement <em>Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffElement
	 * @generated
	 */
	EClass getDiffElement();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.diff.metamodel.DiffElement#getIsHiddenBy <em>Is Hidden By</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Is Hidden By</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffElement#getIsHiddenBy()
	 * @see #getDiffElement()
	 * @generated
	 */
	EReference getDiffElement_IsHiddenBy();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.DiffElement#isConflicting <em>Conflicting</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Conflicting</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffElement#isConflicting()
	 * @see #getDiffElement()
	 * @generated
	 */
	EAttribute getDiffElement_Conflicting();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.DiffElement#getKind <em>Kind</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Kind</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffElement#getKind()
	 * @see #getDiffElement()
	 * @generated
	 */
	EAttribute getDiffElement_Kind();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.DiffElement#isRemote <em>Remote</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Remote</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffElement#isRemote()
	 * @see #getDiffElement()
	 * @generated
	 */
	EAttribute getDiffElement_Remote();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.diff.metamodel.DiffElement#getRequires <em>Requires</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Requires</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffElement#getRequires()
	 * @see #getDiffElement()
	 * @generated
	 * @since 1.3
	 */
	EReference getDiffElement_Requires();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.diff.metamodel.DiffElement#getRequiredBy <em>Required By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Required By</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffElement#getRequiredBy()
	 * @see #getDiffElement()
	 * @generated
	 * @since 1.3
	 */
	EReference getDiffElement_RequiredBy();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.diff.metamodel.DiffElement#getSubDiffElements <em>Sub Diff Elements</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sub Diff Elements</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffElement#getSubDiffElements()
	 * @see #getDiffElement()
	 * @generated
	 */
	EReference getDiffElement_SubDiffElements();

	/**
	 * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DiffFactory getDiffFactory();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup <em>Group</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Group</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffGroup
	 * @generated
	 */
	EClass getDiffGroup();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup#getRightParent <em>Right Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right Parent</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffGroup#getRightParent()
	 * @see #getDiffGroup()
	 * @generated
	 */
	EReference getDiffGroup_RightParent();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup#getSubchanges <em>Subchanges</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Subchanges</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffGroup#getSubchanges()
	 * @see #getDiffGroup()
	 * @generated
	 */
	EAttribute getDiffGroup_Subchanges();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot <em>Comparison Snapshot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Comparison Snapshot</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot
	 * @generated
	 */
	EClass getComparisonSnapshot();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot#getDate()
	 * @see #getComparisonSnapshot()
	 * @generated
	 */
	EAttribute getComparisonSnapshot_Date();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot <em>Comparison Resource Snapshot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Comparison Resource Snapshot</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot
	 * @generated
	 */
	EClass getComparisonResourceSnapshot();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot#getDiff <em>Diff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Diff</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot#getDiff()
	 * @see #getComparisonResourceSnapshot()
	 * @generated
	 */
	EReference getComparisonResourceSnapshot_Diff();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot#getMatch <em>Match</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Match</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot#getMatch()
	 * @see #getComparisonResourceSnapshot()
	 * @generated
	 */
	EReference getComparisonResourceSnapshot_Match();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot <em>Comparison Resource Set Snapshot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Comparison Resource Set Snapshot</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot
	 * @generated
	 */
	EClass getComparisonResourceSetSnapshot();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot#getDiffResourceSet <em>Diff Resource Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Diff Resource Set</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot#getDiffResourceSet()
	 * @see #getComparisonResourceSetSnapshot()
	 * @generated
	 */
	EReference getComparisonResourceSetSnapshot_DiffResourceSet();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot#getMatchResourceSet <em>Match Resource Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Match Resource Set</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot#getMatchResourceSet()
	 * @see #getComparisonResourceSetSnapshot()
	 * @generated
	 */
	EReference getComparisonResourceSetSnapshot_MatchResourceSet();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.DiffModel <em>Model</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffModel
	 * @generated
	 */
	EClass getDiffModel();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getOwnedElements <em>Owned Elements</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Owned Elements</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffModel#getOwnedElements()
	 * @see #getDiffModel()
	 * @generated
	 */
	EReference getDiffModel_OwnedElements();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getLeftRoots <em>Left Roots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Left Roots</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffModel#getLeftRoots()
	 * @see #getDiffModel()
	 * @generated
	 */
	EReference getDiffModel_LeftRoots();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getRightRoots <em>Right Roots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Right Roots</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffModel#getRightRoots()
	 * @see #getDiffModel()
	 * @generated
	 */
	EReference getDiffModel_RightRoots();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getAncestorRoots <em>Ancestor Roots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Ancestor Roots</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffModel#getAncestorRoots()
	 * @see #getDiffModel()
	 * @generated
	 */
	EReference getDiffModel_AncestorRoots();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.DiffResourceSet <em>Resource Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource Set</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffResourceSet
	 * @generated
	 */
	EClass getDiffResourceSet();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.diff.metamodel.DiffResourceSet#getDiffModels <em>Diff Models</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Diff Models</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffResourceSet#getDiffModels()
	 * @see #getDiffResourceSet()
	 * @generated
	 */
	EReference getDiffResourceSet_DiffModels();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.diff.metamodel.DiffResourceSet#getResourceDiffs <em>Resource Diffs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Resource Diffs</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffResourceSet#getResourceDiffs()
	 * @see #getDiffResourceSet()
	 * @generated
	 */
	EReference getDiffResourceSet_ResourceDiffs();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.emf.compare.diff.merge.IMerger <em>IMerger</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>IMerger</em>'.
	 * @see org.eclipse.emf.compare.diff.merge.IMerger
	 * @model instanceClass="org.eclipse.emf.compare.diff.merge.IMerger" serializeable="false"
	 * @generated
	 */
	EDataType getIMerger();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChange <em>Model Element Change</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model Element Change</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChange
	 * @generated
	 */
	EClass getModelElementChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget <em>Model Element Change Left Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model Element Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget
	 * @generated
	 */
	EClass getModelElementChangeLeftTarget();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget#getLeftElement <em>Left Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget#getLeftElement()
	 * @see #getModelElementChangeLeftTarget()
	 * @generated
	 */
	EReference getModelElementChangeLeftTarget_LeftElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget#getRightParent <em>Right Parent</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right Parent</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget#getRightParent()
	 * @see #getModelElementChangeLeftTarget()
	 * @generated
	 */
	EReference getModelElementChangeLeftTarget_RightParent();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget <em>Model Element Change Right Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model Element Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget
	 * @generated
	 */
	EClass getModelElementChangeRightTarget();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget#getLeftParent <em>Left Parent</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left Parent</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget#getLeftParent()
	 * @see #getModelElementChangeRightTarget()
	 * @generated
	 */
	EReference getModelElementChangeRightTarget_LeftParent();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget#getRightElement <em>Right Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget#getRightElement()
	 * @see #getModelElementChangeRightTarget()
	 * @generated
	 */
	EReference getModelElementChangeRightTarget_RightElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.MoveModelElement <em>Move Model Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Move Model Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.MoveModelElement
	 * @generated
	 */
	EClass getMoveModelElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.MoveModelElement#getLeftTarget <em>Left Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.MoveModelElement#getLeftTarget()
	 * @see #getMoveModelElement()
	 * @generated
	 */
	EReference getMoveModelElement_LeftTarget();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.MoveModelElement#getRightTarget <em>Right Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.MoveModelElement#getRightTarget()
	 * @see #getMoveModelElement()
	 * @generated
	 */
	EReference getMoveModelElement_RightTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.UpdateContainmentFeature <em>Update Containment Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Update Containment Feature</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateContainmentFeature
	 * @generated
	 */
	EClass getUpdateContainmentFeature();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChange <em>Reference Change</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Reference Change</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChange
	 * @generated
	 */
	EClass getReferenceChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChange#getLeftElement <em>Left Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChange#getLeftElement()
	 * @see #getReferenceChange()
	 * @generated
	 */
	EReference getReferenceChange_LeftElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChange#getReference <em>Reference</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Reference</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChange#getReference()
	 * @see #getReferenceChange()
	 * @generated
	 */
	EReference getReferenceChange_Reference();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChange#getRightElement <em>Right Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChange#getRightElement()
	 * @see #getReferenceChange()
	 * @generated
	 */
	EReference getReferenceChange_RightElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget <em>Reference Change Left Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Reference Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget
	 * @generated
	 */
	EClass getReferenceChangeLeftTarget();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget#getLeftTarget <em>Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget#getLeftTarget()
	 * @see #getReferenceChangeLeftTarget()
	 * @generated
	 */
	EReference getReferenceChangeLeftTarget_LeftTarget();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget#getRightTarget <em>Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget#getRightTarget()
	 * @see #getReferenceChangeLeftTarget()
	 * @generated
	 */
	EReference getReferenceChangeLeftTarget_RightTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget <em>Reference Change Right Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Reference Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget
	 * @generated
	 */
	EClass getReferenceChangeRightTarget();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget#getRightTarget <em>Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget#getRightTarget()
	 * @see #getReferenceChangeRightTarget()
	 * @generated
	 */
	EReference getReferenceChangeRightTarget_RightTarget();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget#getLeftTarget <em>Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget#getLeftTarget()
	 * @see #getReferenceChangeRightTarget()
	 * @generated
	 */
	EReference getReferenceChangeRightTarget_LeftTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.UpdateAttribute <em>Update Attribute</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Update Attribute</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateAttribute
	 * @generated
	 */
	EClass getUpdateAttribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.UpdateModelElement <em>Update Model Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Update Model Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateModelElement
	 * @generated
	 */
	EClass getUpdateModelElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.UpdateModelElement#getLeftElement <em>Left Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateModelElement#getLeftElement()
	 * @see #getUpdateModelElement()
	 * @generated
	 */
	EReference getUpdateModelElement_LeftElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.UpdateModelElement#getRightElement <em>Right Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateModelElement#getRightElement()
	 * @see #getUpdateModelElement()
	 * @generated
	 */
	EReference getUpdateModelElement_RightElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.UpdateReference <em>Update Reference</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Update Reference</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateReference
	 * @generated
	 */
	EClass getUpdateReference();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.UpdateReference#getLeftTarget <em>Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateReference#getLeftTarget()
	 * @see #getUpdateReference()
	 * @generated
	 */
	EReference getUpdateReference_LeftTarget();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.UpdateReference#getRightTarget <em>Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateReference#getRightTarget()
	 * @see #getUpdateReference()
	 * @generated
	 */
	EReference getUpdateReference_RightTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange <em>Reference Order Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Reference Order Change</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange
	 * @generated
	 */
	EClass getReferenceOrderChange();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange#getLeftTarget <em>Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Left Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange#getLeftTarget()
	 * @see #getReferenceOrderChange()
	 * @generated
	 */
	EReference getReferenceOrderChange_LeftTarget();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange#getRightTarget <em>Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Right Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange#getRightTarget()
	 * @see #getReferenceOrderChange()
	 * @generated
	 */
	EReference getReferenceOrderChange_RightTarget();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("hiding")
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AbstractDiffExtensionImpl <em>Abstract Diff Extension</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.AbstractDiffExtensionImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAbstractDiffExtension()
		 * @generated
		 */
		EClass ABSTRACT_DIFF_EXTENSION = eINSTANCE.getAbstractDiffExtension();

		/**
		 * The meta object literal for the '<em><b>Hide Elements</b></em>' reference list feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS = eINSTANCE.getAbstractDiffExtension_HideElements();

		/**
		 * The meta object literal for the '<em><b>Is Collapsed</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ABSTRACT_DIFF_EXTENSION__IS_COLLAPSED = eINSTANCE.getAbstractDiffExtension_IsCollapsed();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ResourceDiffImpl <em>Resource Diff</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ResourceDiffImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getResourceDiff()
		 * @generated
		 */
		EClass RESOURCE_DIFF = eINSTANCE.getResourceDiff();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ResourceDependencyChangeImpl <em>Resource Dependency Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ResourceDependencyChangeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getResourceDependencyChange()
		 * @generated
		 */
		EClass RESOURCE_DEPENDENCY_CHANGE = eINSTANCE.getResourceDependencyChange();

		/**
		 * The meta object literal for the '<em><b>Roots</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESOURCE_DEPENDENCY_CHANGE__ROOTS = eINSTANCE.getResourceDependencyChange_Roots();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ResourceDependencyChangeLeftTargetImpl <em>Resource Dependency Change Left Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ResourceDependencyChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getResourceDependencyChangeLeftTarget()
		 * @generated
		 */
		EClass RESOURCE_DEPENDENCY_CHANGE_LEFT_TARGET = eINSTANCE.getResourceDependencyChangeLeftTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ResourceDependencyChangeRightTargetImpl <em>Resource Dependency Change Right Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ResourceDependencyChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getResourceDependencyChangeRightTarget()
		 * @generated
		 */
		EClass RESOURCE_DEPENDENCY_CHANGE_RIGHT_TARGET = eINSTANCE.getResourceDependencyChangeRightTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.DifferenceKind <em>Difference Kind</em>}' enum.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.DifferenceKind
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDifferenceKind()
		 * @generated
		 */
		EEnum DIFFERENCE_KIND = eINSTANCE.getDifferenceKind();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeImpl <em>Attribute Change</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAttributeChange()
		 * @generated
		 */
		EClass ATTRIBUTE_CHANGE = eINSTANCE.getAttributeChange();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ATTRIBUTE_CHANGE__ATTRIBUTE = eINSTANCE.getAttributeChange_Attribute();

		/**
		 * The meta object literal for the '<em><b>Left Element</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ATTRIBUTE_CHANGE__LEFT_ELEMENT = eINSTANCE.getAttributeChange_LeftElement();

		/**
		 * The meta object literal for the '<em><b>Right Element</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ATTRIBUTE_CHANGE__RIGHT_ELEMENT = eINSTANCE.getAttributeChange_RightElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeLeftTargetImpl <em>Attribute Change Left Target</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAttributeChangeLeftTarget()
		 * @generated
		 */
		EClass ATTRIBUTE_CHANGE_LEFT_TARGET = eINSTANCE.getAttributeChangeLeftTarget();

		/**
		 * The meta object literal for the '<em><b>Left Target</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_TARGET = eINSTANCE
				.getAttributeChangeLeftTarget_LeftTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeRightTargetImpl <em>Attribute Change Right Target</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAttributeChangeRightTarget()
		 * @generated
		 */
		EClass ATTRIBUTE_CHANGE_RIGHT_TARGET = eINSTANCE.getAttributeChangeRightTarget();

		/**
		 * The meta object literal for the '<em><b>Right Target</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_TARGET = eINSTANCE
				.getAttributeChangeRightTarget_RightTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeOrderChangeImpl <em>Attribute Order Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.AttributeOrderChangeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAttributeOrderChange()
		 * @generated
		 * @since 1.3
		 */
		EClass ATTRIBUTE_ORDER_CHANGE = eINSTANCE.getAttributeOrderChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ConflictingDiffElementImpl <em>Conflicting Diff Element</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ConflictingDiffElementImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getConflictingDiffElement()
		 * @generated
		 */
		EClass CONFLICTING_DIFF_ELEMENT = eINSTANCE.getConflictingDiffElement();

		/**
		 * The meta object literal for the '<em><b>Left Parent</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CONFLICTING_DIFF_ELEMENT__LEFT_PARENT = eINSTANCE.getConflictingDiffElement_LeftParent();

		/**
		 * The meta object literal for the '<em><b>Origin Element</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CONFLICTING_DIFF_ELEMENT__ORIGIN_ELEMENT = eINSTANCE
				.getConflictingDiffElement_OriginElement();

		/**
		 * The meta object literal for the '<em><b>Right Parent</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CONFLICTING_DIFF_ELEMENT__RIGHT_PARENT = eINSTANCE.getConflictingDiffElement_RightParent();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffElementImpl <em>Element</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffElementImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDiffElement()
		 * @generated
		 */
		EClass DIFF_ELEMENT = eINSTANCE.getDiffElement();

		/**
		 * The meta object literal for the '<em><b>Is Hidden By</b></em>' reference list feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIFF_ELEMENT__IS_HIDDEN_BY = eINSTANCE.getDiffElement_IsHiddenBy();

		/**
		 * The meta object literal for the '<em><b>Conflicting</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DIFF_ELEMENT__CONFLICTING = eINSTANCE.getDiffElement_Conflicting();

		/**
		 * The meta object literal for the '<em><b>Kind</b></em>' attribute feature.
		 * <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIFF_ELEMENT__KIND = eINSTANCE.getDiffElement_Kind();

		/**
		 * The meta object literal for the '<em><b>Remote</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIFF_ELEMENT__REMOTE = eINSTANCE.getDiffElement_Remote();

		/**
		 * The meta object literal for the '<em><b>Requires</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 * @since 1.3
		 */
		EReference DIFF_ELEMENT__REQUIRES = eINSTANCE.getDiffElement_Requires();

		/**
		 * The meta object literal for the '<em><b>Required By</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 * @since 1.3
		 */
		EReference DIFF_ELEMENT__REQUIRED_BY = eINSTANCE.getDiffElement_RequiredBy();

		/**
		 * The meta object literal for the '<em><b>Sub Diff Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIFF_ELEMENT__SUB_DIFF_ELEMENTS = eINSTANCE.getDiffElement_SubDiffElements();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffGroupImpl <em>Group</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffGroupImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDiffGroup()
		 * @generated
		 */
		EClass DIFF_GROUP = eINSTANCE.getDiffGroup();

		/**
		 * The meta object literal for the '<em><b>Right Parent</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIFF_GROUP__RIGHT_PARENT = eINSTANCE.getDiffGroup_RightParent();

		/**
		 * The meta object literal for the '<em><b>Subchanges</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DIFF_GROUP__SUBCHANGES = eINSTANCE.getDiffGroup_Subchanges();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot <em>Comparison Snapshot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getComparisonSnapshot()
		 * @generated
		 */
		EClass COMPARISON_SNAPSHOT = eINSTANCE.getComparisonSnapshot();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPARISON_SNAPSHOT__DATE = eINSTANCE.getComparisonSnapshot_Date();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ComparisonResourceSnapshotImpl <em>Comparison Resource Snapshot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ComparisonResourceSnapshotImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getComparisonResourceSnapshot()
		 * @generated
		 */
		EClass COMPARISON_RESOURCE_SNAPSHOT = eINSTANCE.getComparisonResourceSnapshot();

		/**
		 * The meta object literal for the '<em><b>Diff</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPARISON_RESOURCE_SNAPSHOT__DIFF = eINSTANCE.getComparisonResourceSnapshot_Diff();

		/**
		 * The meta object literal for the '<em><b>Match</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPARISON_RESOURCE_SNAPSHOT__MATCH = eINSTANCE.getComparisonResourceSnapshot_Match();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ComparisonResourceSetSnapshotImpl <em>Comparison Resource Set Snapshot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ComparisonResourceSetSnapshotImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getComparisonResourceSetSnapshot()
		 * @generated
		 */
		EClass COMPARISON_RESOURCE_SET_SNAPSHOT = eINSTANCE.getComparisonResourceSetSnapshot();

		/**
		 * The meta object literal for the '<em><b>Diff Resource Set</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPARISON_RESOURCE_SET_SNAPSHOT__DIFF_RESOURCE_SET = eINSTANCE
				.getComparisonResourceSetSnapshot_DiffResourceSet();

		/**
		 * The meta object literal for the '<em><b>Match Resource Set</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPARISON_RESOURCE_SET_SNAPSHOT__MATCH_RESOURCE_SET = eINSTANCE
				.getComparisonResourceSetSnapshot_MatchResourceSet();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDiffModel()
		 * @generated
		 */
		EClass DIFF_MODEL = eINSTANCE.getDiffModel();

		/**
		 * The meta object literal for the '<em><b>Owned Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIFF_MODEL__OWNED_ELEMENTS = eINSTANCE.getDiffModel_OwnedElements();

		/**
		 * The meta object literal for the '<em><b>Left Roots</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIFF_MODEL__LEFT_ROOTS = eINSTANCE.getDiffModel_LeftRoots();

		/**
		 * The meta object literal for the '<em><b>Right Roots</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIFF_MODEL__RIGHT_ROOTS = eINSTANCE.getDiffModel_RightRoots();

		/**
		 * The meta object literal for the '<em><b>Ancestor Roots</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIFF_MODEL__ANCESTOR_ROOTS = eINSTANCE.getDiffModel_AncestorRoots();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffResourceSetImpl <em>Resource Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffResourceSetImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDiffResourceSet()
		 * @generated
		 */
		EClass DIFF_RESOURCE_SET = eINSTANCE.getDiffResourceSet();

		/**
		 * The meta object literal for the '<em><b>Diff Models</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIFF_RESOURCE_SET__DIFF_MODELS = eINSTANCE.getDiffResourceSet_DiffModels();

		/**
		 * The meta object literal for the '<em><b>Resource Diffs</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIFF_RESOURCE_SET__RESOURCE_DIFFS = eINSTANCE.getDiffResourceSet_ResourceDiffs();

		/**
		 * The meta object literal for the '<em>IMerger</em>' data type.
		 * <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.merge.IMerger
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getIMerger()
		 * @generated
		 */
		EDataType IMERGER = eINSTANCE.getIMerger();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeImpl <em>Model Element Change</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getModelElementChange()
		 * @generated
		 */
		EClass MODEL_ELEMENT_CHANGE = eINSTANCE.getModelElementChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeLeftTargetImpl <em>Model Element Change Left Target</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getModelElementChangeLeftTarget()
		 * @generated
		 */
		EClass MODEL_ELEMENT_CHANGE_LEFT_TARGET = eINSTANCE.getModelElementChangeLeftTarget();

		/**
		 * The meta object literal for the '<em><b>Left Element</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT = eINSTANCE
				.getModelElementChangeLeftTarget_LeftElement();

		/**
		 * The meta object literal for the '<em><b>Right Parent</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT = eINSTANCE
				.getModelElementChangeLeftTarget_RightParent();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeRightTargetImpl <em>Model Element Change Right Target</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getModelElementChangeRightTarget()
		 * @generated
		 */
		EClass MODEL_ELEMENT_CHANGE_RIGHT_TARGET = eINSTANCE.getModelElementChangeRightTarget();

		/**
		 * The meta object literal for the '<em><b>Left Parent</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT = eINSTANCE
				.getModelElementChangeRightTarget_LeftParent();

		/**
		 * The meta object literal for the '<em><b>Right Element</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = eINSTANCE
				.getModelElementChangeRightTarget_RightElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.MoveModelElementImpl <em>Move Model Element</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.MoveModelElementImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getMoveModelElement()
		 * @generated
		 */
		EClass MOVE_MODEL_ELEMENT = eINSTANCE.getMoveModelElement();

		/**
		 * The meta object literal for the '<em><b>Left Target</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MOVE_MODEL_ELEMENT__LEFT_TARGET = eINSTANCE.getMoveModelElement_LeftTarget();

		/**
		 * The meta object literal for the '<em><b>Right Target</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MOVE_MODEL_ELEMENT__RIGHT_TARGET = eINSTANCE.getMoveModelElement_RightTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateContainmentFeatureImpl <em>Update Containment Feature</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateContainmentFeatureImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateContainmentFeature()
		 * @generated
		 */
		EClass UPDATE_CONTAINMENT_FEATURE = eINSTANCE.getUpdateContainmentFeature();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeImpl <em>Reference Change</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getReferenceChange()
		 * @generated
		 */
		EClass REFERENCE_CHANGE = eINSTANCE.getReferenceChange();

		/**
		 * The meta object literal for the '<em><b>Left Element</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference REFERENCE_CHANGE__LEFT_ELEMENT = eINSTANCE.getReferenceChange_LeftElement();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference REFERENCE_CHANGE__REFERENCE = eINSTANCE.getReferenceChange_Reference();

		/**
		 * The meta object literal for the '<em><b>Right Element</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference REFERENCE_CHANGE__RIGHT_ELEMENT = eINSTANCE.getReferenceChange_RightElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeLeftTargetImpl <em>Reference Change Left Target</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getReferenceChangeLeftTarget()
		 * @generated
		 */
		EClass REFERENCE_CHANGE_LEFT_TARGET = eINSTANCE.getReferenceChangeLeftTarget();

		/**
		 * The meta object literal for the '<em><b>Left Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REFERENCE_CHANGE_LEFT_TARGET__LEFT_TARGET = eINSTANCE
				.getReferenceChangeLeftTarget_LeftTarget();

		/**
		 * The meta object literal for the '<em><b>Right Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REFERENCE_CHANGE_LEFT_TARGET__RIGHT_TARGET = eINSTANCE
				.getReferenceChangeLeftTarget_RightTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeRightTargetImpl <em>Reference Change Right Target</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getReferenceChangeRightTarget()
		 * @generated
		 */
		EClass REFERENCE_CHANGE_RIGHT_TARGET = eINSTANCE.getReferenceChangeRightTarget();

		/**
		 * The meta object literal for the '<em><b>Right Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_TARGET = eINSTANCE
				.getReferenceChangeRightTarget_RightTarget();

		/**
		 * The meta object literal for the '<em><b>Left Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REFERENCE_CHANGE_RIGHT_TARGET__LEFT_TARGET = eINSTANCE
				.getReferenceChangeRightTarget_LeftTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateAttributeImpl <em>Update Attribute</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateAttributeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateAttribute()
		 * @generated
		 */
		EClass UPDATE_ATTRIBUTE = eINSTANCE.getUpdateAttribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateModelElementImpl <em>Update Model Element</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateModelElementImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateModelElement()
		 * @generated
		 */
		EClass UPDATE_MODEL_ELEMENT = eINSTANCE.getUpdateModelElement();

		/**
		 * The meta object literal for the '<em><b>Left Element</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference UPDATE_MODEL_ELEMENT__LEFT_ELEMENT = eINSTANCE.getUpdateModelElement_LeftElement();

		/**
		 * The meta object literal for the '<em><b>Right Element</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT = eINSTANCE.getUpdateModelElement_RightElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateReferenceImpl <em>Update Reference</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateReferenceImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateReference()
		 * @generated
		 */
		EClass UPDATE_REFERENCE = eINSTANCE.getUpdateReference();

		/**
		 * The meta object literal for the '<em><b>Left Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UPDATE_REFERENCE__LEFT_TARGET = eINSTANCE.getUpdateReference_LeftTarget();

		/**
		 * The meta object literal for the '<em><b>Right Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UPDATE_REFERENCE__RIGHT_TARGET = eINSTANCE.getUpdateReference_RightTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceOrderChangeImpl <em>Reference Order Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ReferenceOrderChangeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getReferenceOrderChange()
		 * @generated
		 */
		EClass REFERENCE_ORDER_CHANGE = eINSTANCE.getReferenceOrderChange();

		/**
		 * The meta object literal for the '<em><b>Left Target</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REFERENCE_ORDER_CHANGE__LEFT_TARGET = eINSTANCE.getReferenceOrderChange_LeftTarget();

		/**
		 * The meta object literal for the '<em><b>Right Target</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REFERENCE_ORDER_CHANGE__RIGHT_TARGET = eINSTANCE.getReferenceOrderChange_RightTarget();

	}

} // DiffPackage
