/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
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
 * 
 * @see org.eclipse.emf.compare.diff.metamodel.DiffFactory
 * @model kind="package"
 * @generated
 */
public interface DiffPackage extends EPackage {
	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AbstractDiffExtensionImpl <em>Abstract Diff Extension</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.AbstractDiffExtensionImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAbstractDiffExtension()
	 * @generated
	 */
	int ABSTRACT_DIFF_EXTENSION = 35;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffElementImpl <em>Element</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffElementImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDiffElement()
	 * @generated
	 */
	int DIFF_ELEMENT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffGroupImpl <em>Group</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffGroupImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDiffGroup()
	 * @generated
	 */
	int DIFF_GROUP = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl <em>Model</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDiffModel()
	 * @generated
	 */
	int DIFF_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Left</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFF_MODEL__LEFT = 0;

	/**
	 * The feature id for the '<em><b>Origin</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFF_MODEL__ORIGIN = 1;

	/**
	 * The feature id for the '<em><b>Right</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFF_MODEL__RIGHT = 2;

	/**
	 * The feature id for the '<em><b>Owned Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFF_MODEL__OWNED_ELEMENTS = 3;

	/**
	 * The number of structural features of the '<em>Model</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFF_MODEL_FEATURE_COUNT = 4;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFF_ELEMENT__SUB_DIFF_ELEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFF_ELEMENT__IS_HIDDEN_BY = 1;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The number of structural features of the '<em>Element</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFF_ELEMENT_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ConflictingDiffElementImpl <em>Conflicting Diff Element</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ConflictingDiffElementImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getConflictingDiffElement()
	 * @generated
	 */
	int CONFLICTING_DIFF_ELEMENT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeImpl <em>Attribute Change</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAttributeChange()
	 * @generated
	 */
	int ATTRIBUTE_CHANGE = 16;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeLeftTargetImpl <em>Attribute Change Left Target</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAttributeChangeLeftTarget()
	 * @generated
	 */
	int ATTRIBUTE_CHANGE_LEFT_TARGET = 17;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeRightTargetImpl <em>Attribute Change Right Target</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAttributeChangeRightTarget()
	 * @generated
	 */
	int ATTRIBUTE_CHANGE_RIGHT_TARGET = 18;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AddAttributeImpl <em>Add Attribute</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.AddAttributeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAddAttribute()
	 * @generated
	 */
	int ADD_ATTRIBUTE = 19;

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	DiffPackage eINSTANCE = org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl.init();

	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "diff"; //$NON-NLS-1$

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "diff"; //$NON-NLS-1$

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare/diff/1.1"; //$NON-NLS-1$

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.GenericDiffElementImpl <em>Generic Diff Element</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.GenericDiffElementImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getGenericDiffElement()
	 * @generated
	 */
	int GENERIC_DIFF_ELEMENT = 2;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GENERIC_DIFF_ELEMENT__SUB_DIFF_ELEMENTS = DIFF_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GENERIC_DIFF_ELEMENT__IS_HIDDEN_BY = DIFF_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GENERIC_DIFF_ELEMENT__CONFLICTING = DIFF_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GENERIC_DIFF_ELEMENT__KIND = DIFF_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GENERIC_DIFF_ELEMENT__LEFT_ELEMENT = DIFF_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GENERIC_DIFF_ELEMENT__RIGHT_ELEMENT = DIFF_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Generic Diff Element</em>' class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GENERIC_DIFF_ELEMENT_FEATURE_COUNT = DIFF_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONFLICTING_DIFF_ELEMENT__SUB_DIFF_ELEMENTS = DIFF_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONFLICTING_DIFF_ELEMENT__IS_HIDDEN_BY = DIFF_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Left Parent</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONFLICTING_DIFF_ELEMENT__LEFT_PARENT = DIFF_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONFLICTING_DIFF_ELEMENT__RIGHT_PARENT = DIFF_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Origin Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFF_GROUP__IS_HIDDEN_BY = DIFF_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Left Parent</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFF_GROUP__LEFT_PARENT = DIFF_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Subchanges</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFF_GROUP__SUBCHANGES = DIFF_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Group</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFF_GROUP_FEATURE_COUNT = DIFF_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '<em>IMerger</em>' data type. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.IMerger
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getIMerger()
	 * @generated
	 */
	int IMERGER = 37;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeImpl <em>Model Element Change</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getModelElementChange()
	 * @generated
	 */
	int MODEL_ELEMENT_CHANGE = 6;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeLeftTargetImpl <em>Model Element Change Left Target</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getModelElementChangeLeftTarget()
	 * @generated
	 */
	int MODEL_ELEMENT_CHANGE_LEFT_TARGET = 7;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeRightTargetImpl <em>Model Element Change Right Target</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getModelElementChangeRightTarget()
	 * @generated
	 */
	int MODEL_ELEMENT_CHANGE_RIGHT_TARGET = 8;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AddModelElementImpl <em>Add Model Element</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.AddModelElementImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAddModelElement()
	 * @generated
	 */
	int ADD_MODEL_ELEMENT = 9;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelInputSnapshotImpl <em>Model Input Snapshot</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ModelInputSnapshotImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getModelInputSnapshot()
	 * @generated
	 */
	int MODEL_INPUT_SNAPSHOT = 5;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_INPUT_SNAPSHOT__DATE = 0;

	/**
	 * The feature id for the '<em><b>Diff</b></em>' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_INPUT_SNAPSHOT__DIFF = 1;

	/**
	 * The feature id for the '<em><b>Match</b></em>' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_INPUT_SNAPSHOT__MATCH = 2;

	/**
	 * The number of structural features of the '<em>Model Input Snapshot</em>' class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_INPUT_SNAPSHOT_FEATURE_COUNT = 3;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE__SUB_DIFF_ELEMENTS = DIFF_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE__IS_HIDDEN_BY = DIFF_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The number of structural features of the '<em>Model Element Change</em>' class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = MODEL_ELEMENT_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Right Parent</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = MODEL_ELEMENT_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Left Parent</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_MODEL_ELEMENT__SUB_DIFF_ELEMENTS = MODEL_ELEMENT_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_MODEL_ELEMENT__IS_HIDDEN_BY = MODEL_ELEMENT_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_MODEL_ELEMENT__CONFLICTING = MODEL_ELEMENT_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_MODEL_ELEMENT__KIND = MODEL_ELEMENT_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Left Parent</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_MODEL_ELEMENT__LEFT_PARENT = MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_MODEL_ELEMENT__RIGHT_ELEMENT = MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The number of structural features of the '<em>Add Model Element</em>' class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_MODEL_ELEMENT_FEATURE_COUNT = MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeImpl <em>Reference Change</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getReferenceChange()
	 * @generated
	 */
	int REFERENCE_CHANGE = 25;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeLeftTargetImpl <em>Reference Change Left Target</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getReferenceChangeLeftTarget()
	 * @generated
	 */
	int REFERENCE_CHANGE_LEFT_TARGET = 26;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeRightTargetImpl <em>Reference Change Right Target</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getReferenceChangeRightTarget()
	 * @generated
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET = 27;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AddReferenceValueImpl <em>Add Reference Value</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.AddReferenceValueImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAddReferenceValue()
	 * @generated
	 */
	int ADD_REFERENCE_VALUE = 28;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoteAddAttributeImpl <em>Remote Add Attribute</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoteAddAttributeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoteAddAttribute()
	 * @generated
	 */
	int REMOTE_ADD_ATTRIBUTE = 20;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoteAddModelElementImpl <em>Remote Add Model Element</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoteAddModelElementImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoteAddModelElement()
	 * @generated
	 */
	int REMOTE_ADD_MODEL_ELEMENT = 10;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_MODEL_ELEMENT__SUB_DIFF_ELEMENTS = MODEL_ELEMENT_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_MODEL_ELEMENT__IS_HIDDEN_BY = MODEL_ELEMENT_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_MODEL_ELEMENT__CONFLICTING = MODEL_ELEMENT_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_MODEL_ELEMENT__KIND = MODEL_ELEMENT_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_MODEL_ELEMENT__RIGHT_PARENT = MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_MODEL_ELEMENT__LEFT_ELEMENT = MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The number of structural features of the '<em>Remote Add Model Element</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_MODEL_ELEMENT_FEATURE_COUNT = MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoteAddReferenceValueImpl <em>Remote Add Reference Value</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoteAddReferenceValueImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoteAddReferenceValue()
	 * @generated
	 */
	int REMOTE_ADD_REFERENCE_VALUE = 29;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoteRemoveAttributeImpl <em>Remote Remove Attribute</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoteRemoveAttributeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoteRemoveAttribute()
	 * @generated
	 */
	int REMOTE_REMOVE_ATTRIBUTE = 22;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoteRemoveModelElementImpl <em>Remote Remove Model Element</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoteRemoveModelElementImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoteRemoveModelElement()
	 * @generated
	 */
	int REMOTE_REMOVE_MODEL_ELEMENT = 12;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoteRemoveReferenceValueImpl <em>Remote Remove Reference Value</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoteRemoveReferenceValueImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoteRemoveReferenceValue()
	 * @generated
	 */
	int REMOTE_REMOVE_REFERENCE_VALUE = 31;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoveAttributeImpl <em>Remove Attribute</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoveAttributeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoveAttribute()
	 * @generated
	 */
	int REMOVE_ATTRIBUTE = 21;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoveModelElementImpl <em>Remove Model Element</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoveModelElementImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoveModelElement()
	 * @generated
	 */
	int REMOVE_MODEL_ELEMENT = 11;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS = MODEL_ELEMENT_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_MODEL_ELEMENT__IS_HIDDEN_BY = MODEL_ELEMENT_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_MODEL_ELEMENT__CONFLICTING = MODEL_ELEMENT_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_MODEL_ELEMENT__KIND = MODEL_ELEMENT_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_MODEL_ELEMENT__RIGHT_PARENT = MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_MODEL_ELEMENT__LEFT_ELEMENT = MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The number of structural features of the '<em>Remove Model Element</em>' class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_MODEL_ELEMENT_FEATURE_COUNT = MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS = MODEL_ELEMENT_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_MODEL_ELEMENT__IS_HIDDEN_BY = MODEL_ELEMENT_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_MODEL_ELEMENT__CONFLICTING = MODEL_ELEMENT_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_MODEL_ELEMENT__KIND = MODEL_ELEMENT_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Left Parent</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_MODEL_ELEMENT__LEFT_PARENT = MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_MODEL_ELEMENT__RIGHT_ELEMENT = MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The number of structural features of the '<em>Remote Remove Model Element</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_MODEL_ELEMENT_FEATURE_COUNT = MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoveReferenceValueImpl <em>Remove Reference Value</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoveReferenceValueImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoveReferenceValue()
	 * @generated
	 */
	int REMOVE_REFERENCE_VALUE = 30;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateAttributeImpl <em>Update Attribute</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateAttributeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateAttribute()
	 * @generated
	 */
	int UPDATE_ATTRIBUTE = 23;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateModelElementImpl <em>Update Model Element</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateModelElementImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateModelElement()
	 * @generated
	 */
	int UPDATE_MODEL_ELEMENT = 13;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS = MODEL_ELEMENT_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_MODEL_ELEMENT__IS_HIDDEN_BY = MODEL_ELEMENT_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_MODEL_ELEMENT__LEFT_ELEMENT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Update Model Element</em>' class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_MODEL_ELEMENT_FEATURE_COUNT = MODEL_ELEMENT_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateReferenceImpl <em>Update Reference</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateReferenceImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateReference()
	 * @generated
	 */
	int UPDATE_REFERENCE = 32;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateUniqueReferenceValueImpl <em>Update Unique Reference Value</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateUniqueReferenceValueImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateUniqueReferenceValue()
	 * @generated
	 */
	int UPDATE_UNIQUE_REFERENCE_VALUE = 33;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.MoveModelElementImpl <em>Move Model Element</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.MoveModelElementImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getMoveModelElement()
	 * @generated
	 */
	int MOVE_MODEL_ELEMENT = 14;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS = UPDATE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__IS_HIDDEN_BY = UPDATE_MODEL_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__RIGHT_ELEMENT = UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__LEFT_ELEMENT = UPDATE_MODEL_ELEMENT__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__LEFT_TARGET = UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT__RIGHT_TARGET = UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Move Model Element</em>' class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MOVE_MODEL_ELEMENT_FEATURE_COUNT = UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoteMoveModelElementImpl <em>Remote Move Model Element</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoteMoveModelElementImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoteMoveModelElement()
	 * @generated
	 */
	int REMOTE_MOVE_MODEL_ELEMENT = 15;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_MOVE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS = MOVE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_MOVE_MODEL_ELEMENT__IS_HIDDEN_BY = MOVE_MODEL_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_MOVE_MODEL_ELEMENT__CONFLICTING = MOVE_MODEL_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_MOVE_MODEL_ELEMENT__KIND = MOVE_MODEL_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_MOVE_MODEL_ELEMENT__RIGHT_ELEMENT = MOVE_MODEL_ELEMENT__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_MOVE_MODEL_ELEMENT__LEFT_ELEMENT = MOVE_MODEL_ELEMENT__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_MOVE_MODEL_ELEMENT__LEFT_TARGET = MOVE_MODEL_ELEMENT__LEFT_TARGET;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_MOVE_MODEL_ELEMENT__RIGHT_TARGET = MOVE_MODEL_ELEMENT__RIGHT_TARGET;

	/**
	 * The number of structural features of the '<em>Remote Move Model Element</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_MOVE_MODEL_ELEMENT_FEATURE_COUNT = MOVE_MODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__SUB_DIFF_ELEMENTS = DIFF_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__IS_HIDDEN_BY = DIFF_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Attribute</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__ATTRIBUTE = DIFF_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__LEFT_ELEMENT = DIFF_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__RIGHT_ELEMENT = DIFF_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Attribute Change</em>' class. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = ATTRIBUTE_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Attribute</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_LEFT_TARGET__ATTRIBUTE = ATTRIBUTE_CHANGE__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_ELEMENT = ATTRIBUTE_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_LEFT_TARGET__RIGHT_ELEMENT = ATTRIBUTE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = ATTRIBUTE_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Attribute</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_RIGHT_TARGET__ATTRIBUTE = ATTRIBUTE_CHANGE__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_RIGHT_TARGET__LEFT_ELEMENT = ATTRIBUTE_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = ATTRIBUTE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_ATTRIBUTE__SUB_DIFF_ELEMENTS = ATTRIBUTE_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_ATTRIBUTE__IS_HIDDEN_BY = ATTRIBUTE_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_ATTRIBUTE__CONFLICTING = ATTRIBUTE_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_ATTRIBUTE__KIND = ATTRIBUTE_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_ATTRIBUTE__ATTRIBUTE = ATTRIBUTE_CHANGE_RIGHT_TARGET__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_ATTRIBUTE__LEFT_ELEMENT = ATTRIBUTE_CHANGE_RIGHT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_ATTRIBUTE__RIGHT_ELEMENT = ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_ATTRIBUTE__RIGHT_TARGET = ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_TARGET;

	/**
	 * The number of structural features of the '<em>Add Attribute</em>' class. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_ATTRIBUTE_FEATURE_COUNT = ATTRIBUTE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_ATTRIBUTE__SUB_DIFF_ELEMENTS = ATTRIBUTE_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_ATTRIBUTE__IS_HIDDEN_BY = ATTRIBUTE_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_ATTRIBUTE__CONFLICTING = ATTRIBUTE_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_ATTRIBUTE__KIND = ATTRIBUTE_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_ATTRIBUTE__ATTRIBUTE = ATTRIBUTE_CHANGE_LEFT_TARGET__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_ATTRIBUTE__LEFT_ELEMENT = ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_ATTRIBUTE__RIGHT_ELEMENT = ATTRIBUTE_CHANGE_LEFT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_ATTRIBUTE__LEFT_TARGET = ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_TARGET;

	/**
	 * The number of structural features of the '<em>Remote Add Attribute</em>' class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_ATTRIBUTE_FEATURE_COUNT = ATTRIBUTE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_ATTRIBUTE__SUB_DIFF_ELEMENTS = ATTRIBUTE_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_ATTRIBUTE__IS_HIDDEN_BY = ATTRIBUTE_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_ATTRIBUTE__CONFLICTING = ATTRIBUTE_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_ATTRIBUTE__KIND = ATTRIBUTE_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_ATTRIBUTE__ATTRIBUTE = ATTRIBUTE_CHANGE_LEFT_TARGET__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_ATTRIBUTE__LEFT_ELEMENT = ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_ATTRIBUTE__RIGHT_ELEMENT = ATTRIBUTE_CHANGE_LEFT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_ATTRIBUTE__LEFT_TARGET = ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_TARGET;

	/**
	 * The number of structural features of the '<em>Remove Attribute</em>' class. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_ATTRIBUTE_FEATURE_COUNT = ATTRIBUTE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_ATTRIBUTE__SUB_DIFF_ELEMENTS = ATTRIBUTE_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_ATTRIBUTE__IS_HIDDEN_BY = ATTRIBUTE_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_ATTRIBUTE__CONFLICTING = ATTRIBUTE_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_ATTRIBUTE__KIND = ATTRIBUTE_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_ATTRIBUTE__ATTRIBUTE = ATTRIBUTE_CHANGE_RIGHT_TARGET__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_ATTRIBUTE__LEFT_ELEMENT = ATTRIBUTE_CHANGE_RIGHT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_ATTRIBUTE__RIGHT_ELEMENT = ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_ATTRIBUTE__RIGHT_TARGET = ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_TARGET;

	/**
	 * The number of structural features of the '<em>Remote Remove Attribute</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_ATTRIBUTE_FEATURE_COUNT = ATTRIBUTE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE__SUB_DIFF_ELEMENTS = ATTRIBUTE_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE__IS_HIDDEN_BY = ATTRIBUTE_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Attribute</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE__ATTRIBUTE = ATTRIBUTE_CHANGE__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE__LEFT_ELEMENT = ATTRIBUTE_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE__RIGHT_ELEMENT = ATTRIBUTE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The number of structural features of the '<em>Update Attribute</em>' class. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_ATTRIBUTE_FEATURE_COUNT = ATTRIBUTE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoteUpdateAttributeImpl <em>Remote Update Attribute</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoteUpdateAttributeImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoteUpdateAttribute()
	 * @generated
	 */
	int REMOTE_UPDATE_ATTRIBUTE = 24;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_UPDATE_ATTRIBUTE__SUB_DIFF_ELEMENTS = UPDATE_ATTRIBUTE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_UPDATE_ATTRIBUTE__IS_HIDDEN_BY = UPDATE_ATTRIBUTE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_UPDATE_ATTRIBUTE__CONFLICTING = UPDATE_ATTRIBUTE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_UPDATE_ATTRIBUTE__KIND = UPDATE_ATTRIBUTE__KIND;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_UPDATE_ATTRIBUTE__ATTRIBUTE = UPDATE_ATTRIBUTE__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_UPDATE_ATTRIBUTE__LEFT_ELEMENT = UPDATE_ATTRIBUTE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_UPDATE_ATTRIBUTE__RIGHT_ELEMENT = UPDATE_ATTRIBUTE__RIGHT_ELEMENT;

	/**
	 * The number of structural features of the '<em>Remote Update Attribute</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_UPDATE_ATTRIBUTE_FEATURE_COUNT = UPDATE_ATTRIBUTE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__SUB_DIFF_ELEMENTS = DIFF_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__IS_HIDDEN_BY = DIFF_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Reference</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__REFERENCE = DIFF_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__RIGHT_ELEMENT = DIFF_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__LEFT_ELEMENT = DIFF_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Reference Change</em>' class. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = REFERENCE_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Reference</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_LEFT_TARGET__REFERENCE = REFERENCE_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_LEFT_TARGET__RIGHT_ELEMENT = REFERENCE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_LEFT_TARGET__LEFT_ELEMENT = REFERENCE_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Removed Target</b></em>' reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_LEFT_TARGET__LEFT_REMOVED_TARGET = REFERENCE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Removed Target</b></em>' reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_LEFT_TARGET__RIGHT_REMOVED_TARGET = REFERENCE_CHANGE_FEATURE_COUNT + 1;

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
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = REFERENCE_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Reference</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET__REFERENCE = REFERENCE_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = REFERENCE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET__LEFT_ELEMENT = REFERENCE_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Added Target</b></em>' reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_ADDED_TARGET = REFERENCE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Left Added Target</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_RIGHT_TARGET__LEFT_ADDED_TARGET = REFERENCE_CHANGE_FEATURE_COUNT + 1;

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
	int ADD_REFERENCE_VALUE__SUB_DIFF_ELEMENTS = REFERENCE_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_VALUE__IS_HIDDEN_BY = REFERENCE_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_VALUE__CONFLICTING = REFERENCE_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_VALUE__KIND = REFERENCE_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_VALUE__REFERENCE = REFERENCE_CHANGE_RIGHT_TARGET__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_VALUE__RIGHT_ELEMENT = REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_VALUE__LEFT_ELEMENT = REFERENCE_CHANGE_RIGHT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Added Target</b></em>' reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_VALUE__RIGHT_ADDED_TARGET = REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_ADDED_TARGET;

	/**
	 * The feature id for the '<em><b>Left Added Target</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_VALUE__LEFT_ADDED_TARGET = REFERENCE_CHANGE_RIGHT_TARGET__LEFT_ADDED_TARGET;

	/**
	 * The number of structural features of the '<em>Add Reference Value</em>' class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_REFERENCE_VALUE_FEATURE_COUNT = REFERENCE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_REFERENCE_VALUE__SUB_DIFF_ELEMENTS = REFERENCE_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_REFERENCE_VALUE__IS_HIDDEN_BY = REFERENCE_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_REFERENCE_VALUE__CONFLICTING = REFERENCE_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_REFERENCE_VALUE__KIND = REFERENCE_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_REFERENCE_VALUE__REFERENCE = REFERENCE_CHANGE_LEFT_TARGET__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_REFERENCE_VALUE__RIGHT_ELEMENT = REFERENCE_CHANGE_LEFT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_REFERENCE_VALUE__LEFT_ELEMENT = REFERENCE_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Removed Target</b></em>' reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_REFERENCE_VALUE__LEFT_REMOVED_TARGET = REFERENCE_CHANGE_LEFT_TARGET__LEFT_REMOVED_TARGET;

	/**
	 * The feature id for the '<em><b>Right Removed Target</b></em>' reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_REFERENCE_VALUE__RIGHT_REMOVED_TARGET = REFERENCE_CHANGE_LEFT_TARGET__RIGHT_REMOVED_TARGET;

	/**
	 * The number of structural features of the '<em>Remote Add Reference Value</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_ADD_REFERENCE_VALUE_FEATURE_COUNT = REFERENCE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_VALUE__SUB_DIFF_ELEMENTS = REFERENCE_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_VALUE__IS_HIDDEN_BY = REFERENCE_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_VALUE__CONFLICTING = REFERENCE_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_VALUE__KIND = REFERENCE_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_VALUE__REFERENCE = REFERENCE_CHANGE_LEFT_TARGET__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_VALUE__RIGHT_ELEMENT = REFERENCE_CHANGE_LEFT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_VALUE__LEFT_ELEMENT = REFERENCE_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Removed Target</b></em>' reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_VALUE__LEFT_REMOVED_TARGET = REFERENCE_CHANGE_LEFT_TARGET__LEFT_REMOVED_TARGET;

	/**
	 * The feature id for the '<em><b>Right Removed Target</b></em>' reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_VALUE__RIGHT_REMOVED_TARGET = REFERENCE_CHANGE_LEFT_TARGET__RIGHT_REMOVED_TARGET;

	/**
	 * The number of structural features of the '<em>Remove Reference Value</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOVE_REFERENCE_VALUE_FEATURE_COUNT = REFERENCE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_REFERENCE_VALUE__SUB_DIFF_ELEMENTS = REFERENCE_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_REFERENCE_VALUE__IS_HIDDEN_BY = REFERENCE_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_REFERENCE_VALUE__CONFLICTING = REFERENCE_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_REFERENCE_VALUE__KIND = REFERENCE_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_REFERENCE_VALUE__REFERENCE = REFERENCE_CHANGE_RIGHT_TARGET__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_REFERENCE_VALUE__RIGHT_ELEMENT = REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_REFERENCE_VALUE__LEFT_ELEMENT = REFERENCE_CHANGE_RIGHT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Added Target</b></em>' reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_REFERENCE_VALUE__RIGHT_ADDED_TARGET = REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_ADDED_TARGET;

	/**
	 * The feature id for the '<em><b>Left Added Target</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_REFERENCE_VALUE__LEFT_ADDED_TARGET = REFERENCE_CHANGE_RIGHT_TARGET__LEFT_ADDED_TARGET;

	/**
	 * The number of structural features of the '<em>Remote Remove Reference Value</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_REMOVE_REFERENCE_VALUE_FEATURE_COUNT = REFERENCE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE__SUB_DIFF_ELEMENTS = REFERENCE_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE__IS_HIDDEN_BY = REFERENCE_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The feature id for the '<em><b>Reference</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE__REFERENCE = REFERENCE_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE__RIGHT_ELEMENT = REFERENCE_CHANGE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE__LEFT_ELEMENT = REFERENCE_CHANGE__LEFT_ELEMENT;

	/**
	 * The number of structural features of the '<em>Update Reference</em>' class. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_REFERENCE_FEATURE_COUNT = REFERENCE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_UNIQUE_REFERENCE_VALUE__SUB_DIFF_ELEMENTS = UPDATE_REFERENCE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_UNIQUE_REFERENCE_VALUE__IS_HIDDEN_BY = UPDATE_REFERENCE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_UNIQUE_REFERENCE_VALUE__CONFLICTING = UPDATE_REFERENCE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_UNIQUE_REFERENCE_VALUE__KIND = UPDATE_REFERENCE__KIND;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_UNIQUE_REFERENCE_VALUE__REFERENCE = UPDATE_REFERENCE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_UNIQUE_REFERENCE_VALUE__RIGHT_ELEMENT = UPDATE_REFERENCE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_UNIQUE_REFERENCE_VALUE__LEFT_ELEMENT = UPDATE_REFERENCE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_UNIQUE_REFERENCE_VALUE__LEFT_TARGET = UPDATE_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_UNIQUE_REFERENCE_VALUE__RIGHT_TARGET = UPDATE_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Update Unique Reference Value</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_UNIQUE_REFERENCE_VALUE_FEATURE_COUNT = UPDATE_REFERENCE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoteUpdateUniqueReferenceValueImpl <em>Remote Update Unique Reference Value</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoteUpdateUniqueReferenceValueImpl
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoteUpdateUniqueReferenceValue()
	 * @generated
	 */
	int REMOTE_UPDATE_UNIQUE_REFERENCE_VALUE = 34;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_UPDATE_UNIQUE_REFERENCE_VALUE__SUB_DIFF_ELEMENTS = UPDATE_UNIQUE_REFERENCE_VALUE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_UPDATE_UNIQUE_REFERENCE_VALUE__IS_HIDDEN_BY = UPDATE_UNIQUE_REFERENCE_VALUE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_UPDATE_UNIQUE_REFERENCE_VALUE__CONFLICTING = UPDATE_UNIQUE_REFERENCE_VALUE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_UPDATE_UNIQUE_REFERENCE_VALUE__KIND = UPDATE_UNIQUE_REFERENCE_VALUE__KIND;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_UPDATE_UNIQUE_REFERENCE_VALUE__REFERENCE = UPDATE_UNIQUE_REFERENCE_VALUE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_UPDATE_UNIQUE_REFERENCE_VALUE__RIGHT_ELEMENT = UPDATE_UNIQUE_REFERENCE_VALUE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_UPDATE_UNIQUE_REFERENCE_VALUE__LEFT_ELEMENT = UPDATE_UNIQUE_REFERENCE_VALUE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_UPDATE_UNIQUE_REFERENCE_VALUE__LEFT_TARGET = UPDATE_UNIQUE_REFERENCE_VALUE__LEFT_TARGET;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_UPDATE_UNIQUE_REFERENCE_VALUE__RIGHT_TARGET = UPDATE_UNIQUE_REFERENCE_VALUE__RIGHT_TARGET;

	/**
	 * The number of structural features of the '<em>Remote Update Unique Reference Value</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REMOTE_UPDATE_UNIQUE_REFERENCE_VALUE_FEATURE_COUNT = UPDATE_UNIQUE_REFERENCE_VALUE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * The meta object id for the '{@link org.eclipse.emf.compare.diff.metamodel.DifferenceKind <em>Difference Kind</em>}'
	 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.diff.metamodel.DifferenceKind
	 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDifferenceKind()
	 * @generated
	 */
	int DIFFERENCE_KIND = 36;

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension <em>Abstract Diff Extension</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Abstract Diff Extension</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension
	 * @generated
	 */
	EClass getAbstractDiffExtension();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension#getHideElements <em>Hide Elements</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Hide Elements</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension#getHideElements()
	 * @see #getAbstractDiffExtension()
	 * @generated
	 */
	EReference getAbstractDiffExtension_HideElements();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension#isIsCollapsed <em>Is Collapsed</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Is Collapsed</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension#isIsCollapsed()
	 * @see #getAbstractDiffExtension()
	 * @generated
	 */
	EAttribute getAbstractDiffExtension_IsCollapsed();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.emf.compare.diff.metamodel.DifferenceKind <em>Difference Kind</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Difference Kind</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DifferenceKind
	 * @generated
	 */
	EEnum getDifferenceKind();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.AddAttribute <em>Add Attribute</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Add Attribute</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AddAttribute
	 * @generated
	 */
	EClass getAddAttribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.AddModelElement <em>Add Model Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Add Model Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AddModelElement
	 * @generated
	 */
	EClass getAddModelElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.AddReferenceValue <em>Add Reference Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Add Reference Value</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AddReferenceValue
	 * @generated
	 */
	EClass getAddReferenceValue();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChange <em>Attribute Change</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Attribute Change</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChange
	 * @generated
	 */
	EClass getAttributeChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChange#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Attribute</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChange#getAttribute()
	 * @see #getAttributeChange()
	 * @generated
	 */
	EReference getAttributeChange_Attribute();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChange#getLeftElement <em>Left Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Left Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChange#getLeftElement()
	 * @see #getAttributeChange()
	 * @generated
	 */
	EReference getAttributeChange_LeftElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChange#getRightElement <em>Right Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Right Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChange#getRightElement()
	 * @see #getAttributeChange()
	 * @generated
	 */
	EReference getAttributeChange_RightElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget <em>Attribute Change Left Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Attribute Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget
	 * @generated
	 */
	EClass getAttributeChangeLeftTarget();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget#getLeftTarget <em>Left Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Left Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget#getLeftTarget()
	 * @see #getAttributeChangeLeftTarget()
	 * @generated
	 */
	EAttribute getAttributeChangeLeftTarget_LeftTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget <em>Attribute Change Right Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Attribute Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget
	 * @generated
	 */
	EClass getAttributeChangeRightTarget();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget#getRightTarget <em>Right Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Right Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget#getRightTarget()
	 * @see #getAttributeChangeRightTarget()
	 * @generated
	 */
	EAttribute getAttributeChangeRightTarget_RightTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement <em>Conflicting Diff Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Conflicting Diff Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement
	 * @generated
	 */
	EClass getConflictingDiffElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getLeftParent <em>Left Parent</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Left Parent</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getLeftParent()
	 * @see #getConflictingDiffElement()
	 * @generated
	 */
	EReference getConflictingDiffElement_LeftParent();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getOriginElement <em>Origin Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Origin Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getOriginElement()
	 * @see #getConflictingDiffElement()
	 * @generated
	 */
	EReference getConflictingDiffElement_OriginElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getRightParent <em>Right Parent</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Right Parent</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement#getRightParent()
	 * @see #getConflictingDiffElement()
	 * @generated
	 */
	EReference getConflictingDiffElement_RightParent();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.DiffElement <em>Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffElement
	 * @generated
	 */
	EClass getDiffElement();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.diff.metamodel.DiffElement#getIsHiddenBy <em>Is Hidden By</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Is Hidden By</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffElement#getIsHiddenBy()
	 * @see #getDiffElement()
	 * @generated
	 */
	EReference getDiffElement_IsHiddenBy();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.DiffElement#isConflicting <em>Conflicting</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Conflicting</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffElement#isConflicting()
	 * @see #getDiffElement()
	 * @generated
	 */
	EAttribute getDiffElement_Conflicting();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.DiffElement#getKind <em>Kind</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Kind</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffElement#getKind()
	 * @see #getDiffElement()
	 * @generated
	 */
	EAttribute getDiffElement_Kind();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.diff.metamodel.DiffElement#getSubDiffElements <em>Sub Diff Elements</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
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
	 * 
	 * @return the meta object for class '<em>Group</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffGroup
	 * @generated
	 */
	EClass getDiffGroup();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup#getLeftParent <em>Left Parent</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Left Parent</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffGroup#getLeftParent()
	 * @see #getDiffGroup()
	 * @generated
	 */
	EReference getDiffGroup_LeftParent();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.DiffGroup#getSubchanges <em>Subchanges</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Subchanges</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffGroup#getSubchanges()
	 * @see #getDiffGroup()
	 * @generated
	 */
	EAttribute getDiffGroup_Subchanges();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.DiffModel <em>Model</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Model</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffModel
	 * @generated
	 */
	EClass getDiffModel();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getLeft <em>Left</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Left</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffModel#getLeft()
	 * @see #getDiffModel()
	 * @generated
	 */
	EAttribute getDiffModel_Left();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getOrigin <em>Origin</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Origin</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffModel#getOrigin()
	 * @see #getDiffModel()
	 * @generated
	 */
	EAttribute getDiffModel_Origin();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getOwnedElements <em>Owned Elements</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Owned Elements</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffModel#getOwnedElements()
	 * @see #getDiffModel()
	 * @generated
	 */
	EReference getDiffModel_OwnedElements();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.DiffModel#getRight <em>Right</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Right</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.DiffModel#getRight()
	 * @see #getDiffModel()
	 * @generated
	 */
	EAttribute getDiffModel_Right();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.GenericDiffElement <em>Generic Diff Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Generic Diff Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.GenericDiffElement
	 * @generated
	 */
	EClass getGenericDiffElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.GenericDiffElement#getLeftElement <em>Left Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Left Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.GenericDiffElement#getLeftElement()
	 * @see #getGenericDiffElement()
	 * @generated
	 */
	EReference getGenericDiffElement_LeftElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.GenericDiffElement#getRightElement <em>Right Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Right Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.GenericDiffElement#getRightElement()
	 * @see #getGenericDiffElement()
	 * @generated
	 */
	EReference getGenericDiffElement_RightElement();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.emf.compare.diff.merge.api.IMerger <em>IMerger</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>IMerger</em>'.
	 * @see org.eclipse.emf.compare.diff.merge.api.IMerger
	 * @model instanceClass="org.eclipse.emf.compare.diff.merge.api.IMerger"
	 * @generated
	 */
	EDataType getIMerger();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChange <em>Model Element Change</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Model Element Change</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChange
	 * @generated
	 */
	EClass getModelElementChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget <em>Model Element Change Left Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Model Element Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget
	 * @generated
	 */
	EClass getModelElementChangeLeftTarget();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget#getLeftElement <em>Left Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Left Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget#getLeftElement()
	 * @see #getModelElementChangeLeftTarget()
	 * @generated
	 */
	EReference getModelElementChangeLeftTarget_LeftElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget#getRightParent <em>Right Parent</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Right Parent</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget#getRightParent()
	 * @see #getModelElementChangeLeftTarget()
	 * @generated
	 */
	EReference getModelElementChangeLeftTarget_RightParent();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget <em>Model Element Change Right Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Model Element Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget
	 * @generated
	 */
	EClass getModelElementChangeRightTarget();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget#getLeftParent <em>Left Parent</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Left Parent</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget#getLeftParent()
	 * @see #getModelElementChangeRightTarget()
	 * @generated
	 */
	EReference getModelElementChangeRightTarget_LeftParent();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget#getRightElement <em>Right Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Right Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget#getRightElement()
	 * @see #getModelElementChangeRightTarget()
	 * @generated
	 */
	EReference getModelElementChangeRightTarget_RightElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot <em>Model Input Snapshot</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Model Input Snapshot</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot
	 * @generated
	 */
	EClass getModelInputSnapshot();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot#getDate()
	 * @see #getModelInputSnapshot()
	 * @generated
	 */
	EAttribute getModelInputSnapshot_Date();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot#getDiff <em>Diff</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Diff</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot#getDiff()
	 * @see #getModelInputSnapshot()
	 * @generated
	 */
	EReference getModelInputSnapshot_Diff();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot#getMatch <em>Match</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Match</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot#getMatch()
	 * @see #getModelInputSnapshot()
	 * @generated
	 */
	EReference getModelInputSnapshot_Match();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.MoveModelElement <em>Move Model Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Move Model Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.MoveModelElement
	 * @generated
	 */
	EClass getMoveModelElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.MoveModelElement#getLeftTarget <em>Left Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Left Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.MoveModelElement#getLeftTarget()
	 * @see #getMoveModelElement()
	 * @generated
	 */
	EReference getMoveModelElement_LeftTarget();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.MoveModelElement#getRightTarget <em>Right Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Right Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.MoveModelElement#getRightTarget()
	 * @see #getMoveModelElement()
	 * @generated
	 */
	EReference getMoveModelElement_RightTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChange <em>Reference Change</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Reference Change</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChange
	 * @generated
	 */
	EClass getReferenceChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChange#getLeftElement <em>Left Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Left Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChange#getLeftElement()
	 * @see #getReferenceChange()
	 * @generated
	 */
	EReference getReferenceChange_LeftElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChange#getReference <em>Reference</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Reference</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChange#getReference()
	 * @see #getReferenceChange()
	 * @generated
	 */
	EReference getReferenceChange_Reference();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChange#getRightElement <em>Right Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Right Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChange#getRightElement()
	 * @see #getReferenceChange()
	 * @generated
	 */
	EReference getReferenceChange_RightElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget <em>Reference Change Left Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Reference Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget
	 * @generated
	 */
	EClass getReferenceChangeLeftTarget();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget#getLeftRemovedTarget <em>Left Removed Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Left Removed Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget#getLeftRemovedTarget()
	 * @see #getReferenceChangeLeftTarget()
	 * @generated
	 */
	EReference getReferenceChangeLeftTarget_LeftRemovedTarget();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget#getRightRemovedTarget <em>Right Removed Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Right Removed Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget#getRightRemovedTarget()
	 * @see #getReferenceChangeLeftTarget()
	 * @generated
	 */
	EReference getReferenceChangeLeftTarget_RightRemovedTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget <em>Reference Change Right Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Reference Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget
	 * @generated
	 */
	EClass getReferenceChangeRightTarget();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget#getLeftAddedTarget <em>Left Added Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Left Added Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget#getLeftAddedTarget()
	 * @see #getReferenceChangeRightTarget()
	 * @generated
	 */
	EReference getReferenceChangeRightTarget_LeftAddedTarget();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget#getRightAddedTarget <em>Right Added Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Right Added Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget#getRightAddedTarget()
	 * @see #getReferenceChangeRightTarget()
	 * @generated
	 */
	EReference getReferenceChangeRightTarget_RightAddedTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.RemoteAddAttribute <em>Remote Add Attribute</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Remote Add Attribute</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoteAddAttribute
	 * @generated
	 */
	EClass getRemoteAddAttribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.RemoteAddModelElement <em>Remote Add Model Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Remote Add Model Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoteAddModelElement
	 * @generated
	 */
	EClass getRemoteAddModelElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.RemoteAddReferenceValue <em>Remote Add Reference Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Remote Add Reference Value</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoteAddReferenceValue
	 * @generated
	 */
	EClass getRemoteAddReferenceValue();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.RemoteMoveModelElement <em>Remote Move Model Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Remote Move Model Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoteMoveModelElement
	 * @generated
	 */
	EClass getRemoteMoveModelElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.RemoteRemoveAttribute <em>Remote Remove Attribute</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Remote Remove Attribute</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoteRemoveAttribute
	 * @generated
	 */
	EClass getRemoteRemoveAttribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.RemoteRemoveModelElement <em>Remote Remove Model Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Remote Remove Model Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoteRemoveModelElement
	 * @generated
	 */
	EClass getRemoteRemoveModelElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.RemoteRemoveReferenceValue <em>Remote Remove Reference Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Remote Remove Reference Value</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoteRemoveReferenceValue
	 * @generated
	 */
	EClass getRemoteRemoveReferenceValue();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.RemoteUpdateAttribute <em>Remote Update Attribute</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Remote Update Attribute</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoteUpdateAttribute
	 * @generated
	 */
	EClass getRemoteUpdateAttribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.RemoteUpdateUniqueReferenceValue <em>Remote Update Unique Reference Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Remote Update Unique Reference Value</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoteUpdateUniqueReferenceValue
	 * @generated
	 */
	EClass getRemoteUpdateUniqueReferenceValue();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.RemoveAttribute <em>Remove Attribute</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Remove Attribute</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoveAttribute
	 * @generated
	 */
	EClass getRemoveAttribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.RemoveModelElement <em>Remove Model Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Remove Model Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoveModelElement
	 * @generated
	 */
	EClass getRemoveModelElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue <em>Remove Reference Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Remove Reference Value</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue
	 * @generated
	 */
	EClass getRemoveReferenceValue();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.UpdateAttribute <em>Update Attribute</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Update Attribute</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateAttribute
	 * @generated
	 */
	EClass getUpdateAttribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.UpdateModelElement <em>Update Model Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Update Model Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateModelElement
	 * @generated
	 */
	EClass getUpdateModelElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.UpdateModelElement#getLeftElement <em>Left Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Left Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateModelElement#getLeftElement()
	 * @see #getUpdateModelElement()
	 * @generated
	 */
	EReference getUpdateModelElement_LeftElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.UpdateModelElement#getRightElement <em>Right Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Right Element</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateModelElement#getRightElement()
	 * @see #getUpdateModelElement()
	 * @generated
	 */
	EReference getUpdateModelElement_RightElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.UpdateReference <em>Update Reference</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Update Reference</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateReference
	 * @generated
	 */
	EClass getUpdateReference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue <em>Update Unique Reference Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Update Unique Reference Value</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue
	 * @generated
	 */
	EClass getUpdateUniqueReferenceValue();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue#getLeftTarget <em>Left Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Left Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue#getLeftTarget()
	 * @see #getUpdateUniqueReferenceValue()
	 * @generated
	 */
	EReference getUpdateUniqueReferenceValue_LeftTarget();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue#getRightTarget <em>Right Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Right Target</em>'.
	 * @see org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue#getRightTarget()
	 * @see #getUpdateUniqueReferenceValue()
	 * @generated
	 */
	EReference getUpdateUniqueReferenceValue_RightTarget();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("hiding")
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AbstractDiffExtensionImpl <em>Abstract Diff Extension</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.DifferenceKind <em>Difference Kind</em>}'
		 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.DifferenceKind
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDifferenceKind()
		 * @generated
		 */
		EEnum DIFFERENCE_KIND = eINSTANCE.getDifferenceKind();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AddAttributeImpl <em>Add Attribute</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.AddAttributeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAddAttribute()
		 * @generated
		 */
		EClass ADD_ATTRIBUTE = eINSTANCE.getAddAttribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AddModelElementImpl <em>Add Model Element</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.AddModelElementImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAddModelElement()
		 * @generated
		 */
		EClass ADD_MODEL_ELEMENT = eINSTANCE.getAddModelElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AddReferenceValueImpl <em>Add Reference Value</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.AddReferenceValueImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getAddReferenceValue()
		 * @generated
		 */
		EClass ADD_REFERENCE_VALUE = eINSTANCE.getAddReferenceValue();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeImpl <em>Attribute Change</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeLeftTargetImpl <em>Attribute Change Left Target</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.AttributeChangeRightTargetImpl <em>Attribute Change Right Target</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ConflictingDiffElementImpl <em>Conflicting Diff Element</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffElementImpl <em>Element</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
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
		 * The meta object literal for the '<em><b>Kind</b></em>' attribute feature. <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DIFF_ELEMENT__KIND = eINSTANCE.getDiffElement_Kind();

		/**
		 * The meta object literal for the '<em><b>Sub Diff Elements</b></em>' containment reference list
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIFF_ELEMENT__SUB_DIFF_ELEMENTS = eINSTANCE.getDiffElement_SubDiffElements();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffGroupImpl <em>Group</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffGroupImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDiffGroup()
		 * @generated
		 */
		EClass DIFF_GROUP = eINSTANCE.getDiffGroup();

		/**
		 * The meta object literal for the '<em><b>Left Parent</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIFF_GROUP__LEFT_PARENT = eINSTANCE.getDiffGroup_LeftParent();

		/**
		 * The meta object literal for the '<em><b>Subchanges</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DIFF_GROUP__SUBCHANGES = eINSTANCE.getDiffGroup_Subchanges();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl <em>Model</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffModelImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getDiffModel()
		 * @generated
		 */
		EClass DIFF_MODEL = eINSTANCE.getDiffModel();

		/**
		 * The meta object literal for the '<em><b>Left</b></em>' attribute feature. <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DIFF_MODEL__LEFT = eINSTANCE.getDiffModel_Left();

		/**
		 * The meta object literal for the '<em><b>Origin</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DIFF_MODEL__ORIGIN = eINSTANCE.getDiffModel_Origin();

		/**
		 * The meta object literal for the '<em><b>Owned Elements</b></em>' containment reference list
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIFF_MODEL__OWNED_ELEMENTS = eINSTANCE.getDiffModel_OwnedElements();

		/**
		 * The meta object literal for the '<em><b>Right</b></em>' attribute feature. <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DIFF_MODEL__RIGHT = eINSTANCE.getDiffModel_Right();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.GenericDiffElementImpl <em>Generic Diff Element</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.GenericDiffElementImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getGenericDiffElement()
		 * @generated
		 */
		EClass GENERIC_DIFF_ELEMENT = eINSTANCE.getGenericDiffElement();

		/**
		 * The meta object literal for the '<em><b>Left Element</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference GENERIC_DIFF_ELEMENT__LEFT_ELEMENT = eINSTANCE.getGenericDiffElement_LeftElement();

		/**
		 * The meta object literal for the '<em><b>Right Element</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference GENERIC_DIFF_ELEMENT__RIGHT_ELEMENT = eINSTANCE.getGenericDiffElement_RightElement();

		/**
		 * The meta object literal for the '<em>IMerger</em>' data type. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.merge.api.IMerger
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getIMerger()
		 * @generated
		 */
		EDataType IMERGER = eINSTANCE.getIMerger();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeImpl <em>Model Element Change</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getModelElementChange()
		 * @generated
		 */
		EClass MODEL_ELEMENT_CHANGE = eINSTANCE.getModelElementChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeLeftTargetImpl <em>Model Element Change Left Target</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelElementChangeRightTargetImpl <em>Model Element Change Right Target</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ModelInputSnapshotImpl <em>Model Input Snapshot</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ModelInputSnapshotImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getModelInputSnapshot()
		 * @generated
		 */
		EClass MODEL_INPUT_SNAPSHOT = eINSTANCE.getModelInputSnapshot();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature. <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute MODEL_INPUT_SNAPSHOT__DATE = eINSTANCE.getModelInputSnapshot_Date();

		/**
		 * The meta object literal for the '<em><b>Diff</b></em>' containment reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MODEL_INPUT_SNAPSHOT__DIFF = eINSTANCE.getModelInputSnapshot_Diff();

		/**
		 * The meta object literal for the '<em><b>Match</b></em>' containment reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MODEL_INPUT_SNAPSHOT__MATCH = eINSTANCE.getModelInputSnapshot_Match();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.MoveModelElementImpl <em>Move Model Element</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeImpl <em>Reference Change</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeLeftTargetImpl <em>Reference Change Left Target</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getReferenceChangeLeftTarget()
		 * @generated
		 */
		EClass REFERENCE_CHANGE_LEFT_TARGET = eINSTANCE.getReferenceChangeLeftTarget();

		/**
		 * The meta object literal for the '<em><b>Left Removed Target</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference REFERENCE_CHANGE_LEFT_TARGET__LEFT_REMOVED_TARGET = eINSTANCE
				.getReferenceChangeLeftTarget_LeftRemovedTarget();

		/**
		 * The meta object literal for the '<em><b>Right Removed Target</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference REFERENCE_CHANGE_LEFT_TARGET__RIGHT_REMOVED_TARGET = eINSTANCE
				.getReferenceChangeLeftTarget_RightRemovedTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeRightTargetImpl <em>Reference Change Right Target</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.ReferenceChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getReferenceChangeRightTarget()
		 * @generated
		 */
		EClass REFERENCE_CHANGE_RIGHT_TARGET = eINSTANCE.getReferenceChangeRightTarget();

		/**
		 * The meta object literal for the '<em><b>Left Added Target</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference REFERENCE_CHANGE_RIGHT_TARGET__LEFT_ADDED_TARGET = eINSTANCE
				.getReferenceChangeRightTarget_LeftAddedTarget();

		/**
		 * The meta object literal for the '<em><b>Right Added Target</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_ADDED_TARGET = eINSTANCE
				.getReferenceChangeRightTarget_RightAddedTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoteAddAttributeImpl <em>Remote Add Attribute</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoteAddAttributeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoteAddAttribute()
		 * @generated
		 */
		EClass REMOTE_ADD_ATTRIBUTE = eINSTANCE.getRemoteAddAttribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoteAddModelElementImpl <em>Remote Add Model Element</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoteAddModelElementImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoteAddModelElement()
		 * @generated
		 */
		EClass REMOTE_ADD_MODEL_ELEMENT = eINSTANCE.getRemoteAddModelElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoteAddReferenceValueImpl <em>Remote Add Reference Value</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoteAddReferenceValueImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoteAddReferenceValue()
		 * @generated
		 */
		EClass REMOTE_ADD_REFERENCE_VALUE = eINSTANCE.getRemoteAddReferenceValue();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoteMoveModelElementImpl <em>Remote Move Model Element</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoteMoveModelElementImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoteMoveModelElement()
		 * @generated
		 */
		EClass REMOTE_MOVE_MODEL_ELEMENT = eINSTANCE.getRemoteMoveModelElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoteRemoveAttributeImpl <em>Remote Remove Attribute</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoteRemoveAttributeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoteRemoveAttribute()
		 * @generated
		 */
		EClass REMOTE_REMOVE_ATTRIBUTE = eINSTANCE.getRemoteRemoveAttribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoteRemoveModelElementImpl <em>Remote Remove Model Element</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoteRemoveModelElementImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoteRemoveModelElement()
		 * @generated
		 */
		EClass REMOTE_REMOVE_MODEL_ELEMENT = eINSTANCE.getRemoteRemoveModelElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoteRemoveReferenceValueImpl <em>Remote Remove Reference Value</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoteRemoveReferenceValueImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoteRemoveReferenceValue()
		 * @generated
		 */
		EClass REMOTE_REMOVE_REFERENCE_VALUE = eINSTANCE.getRemoteRemoveReferenceValue();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoteUpdateAttributeImpl <em>Remote Update Attribute</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoteUpdateAttributeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoteUpdateAttribute()
		 * @generated
		 */
		EClass REMOTE_UPDATE_ATTRIBUTE = eINSTANCE.getRemoteUpdateAttribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoteUpdateUniqueReferenceValueImpl <em>Remote Update Unique Reference Value</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoteUpdateUniqueReferenceValueImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoteUpdateUniqueReferenceValue()
		 * @generated
		 */
		EClass REMOTE_UPDATE_UNIQUE_REFERENCE_VALUE = eINSTANCE.getRemoteUpdateUniqueReferenceValue();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoveAttributeImpl <em>Remove Attribute</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoveAttributeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoveAttribute()
		 * @generated
		 */
		EClass REMOVE_ATTRIBUTE = eINSTANCE.getRemoveAttribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoveModelElementImpl <em>Remove Model Element</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoveModelElementImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoveModelElement()
		 * @generated
		 */
		EClass REMOVE_MODEL_ELEMENT = eINSTANCE.getRemoveModelElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.RemoveReferenceValueImpl <em>Remove Reference Value</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.RemoveReferenceValueImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getRemoveReferenceValue()
		 * @generated
		 */
		EClass REMOVE_REFERENCE_VALUE = eINSTANCE.getRemoveReferenceValue();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateAttributeImpl <em>Update Attribute</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateAttributeImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateAttribute()
		 * @generated
		 */
		EClass UPDATE_ATTRIBUTE = eINSTANCE.getUpdateAttribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateModelElementImpl <em>Update Model Element</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateReferenceImpl <em>Update Reference</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateReferenceImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateReference()
		 * @generated
		 */
		EClass UPDATE_REFERENCE = eINSTANCE.getUpdateReference();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.diff.metamodel.impl.UpdateUniqueReferenceValueImpl <em>Update Unique Reference Value</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.UpdateUniqueReferenceValueImpl
		 * @see org.eclipse.emf.compare.diff.metamodel.impl.DiffPackageImpl#getUpdateUniqueReferenceValue()
		 * @generated
		 */
		EClass UPDATE_UNIQUE_REFERENCE_VALUE = eINSTANCE.getUpdateUniqueReferenceValue();

		/**
		 * The meta object literal for the '<em><b>Left Target</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference UPDATE_UNIQUE_REFERENCE_VALUE__LEFT_TARGET = eINSTANCE
				.getUpdateUniqueReferenceValue_LeftTarget();

		/**
		 * The meta object literal for the '<em><b>Right Target</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference UPDATE_UNIQUE_REFERENCE_VALUE__RIGHT_TARGET = eINSTANCE
				.getUpdateUniqueReferenceValue_RightTarget();

	}

} // DiffPackage
