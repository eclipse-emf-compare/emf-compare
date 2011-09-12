/**
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2diff;

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
 * @see org.eclipse.emf.compare.uml2diff.UML2DiffFactory
 * @model kind="package"
 * @generated
 */
public interface UML2DiffPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "uml2diff";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare/diff/uml2/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "uml2diff";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	UML2DiffPackage eINSTANCE = org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.UMLDiffExtension <em>UML Diff Extension</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.UMLDiffExtension
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDiffExtension()
	 * @generated
	 */
	int UML_DIFF_EXTENSION = 0;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF_EXTENSION__SUB_DIFF_ELEMENTS = DiffPackage.DIFF_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF_EXTENSION__IS_HIDDEN_BY = DiffPackage.DIFF_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF_EXTENSION__CONFLICTING = DiffPackage.DIFF_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF_EXTENSION__KIND = DiffPackage.DIFF_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF_EXTENSION__REMOTE = DiffPackage.DIFF_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF_EXTENSION__REQUIRES = DiffPackage.DIFF_ELEMENT__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF_EXTENSION__REQUIRED_BY = DiffPackage.DIFF_ELEMENT__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF_EXTENSION__HIDE_ELEMENTS = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF_EXTENSION__IS_COLLAPSED = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Diff Extension</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF_EXTENSION_FEATURE_COUNT = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.UMLAssociationChange <em>UML Association Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.UMLAssociationChange
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLAssociationChange()
	 * @generated
	 */
	int UML_ASSOCIATION_CHANGE = 1;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__SUB_DIFF_ELEMENTS = UML_DIFF_EXTENSION__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__IS_HIDDEN_BY = UML_DIFF_EXTENSION__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__CONFLICTING = UML_DIFF_EXTENSION__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__KIND = UML_DIFF_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__REMOTE = UML_DIFF_EXTENSION__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__REQUIRES = UML_DIFF_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__REQUIRED_BY = UML_DIFF_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__HIDE_ELEMENTS = UML_DIFF_EXTENSION__HIDE_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__IS_COLLAPSED = UML_DIFF_EXTENSION__IS_COLLAPSED;

	/**
	 * The number of structural features of the '<em>UML Association Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_FEATURE_COUNT = UML_DIFF_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLAssociationChangeLeftTargetImpl <em>UML Association Change Left Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLAssociationChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLAssociationChangeLeftTarget()
	 * @generated
	 */
	int UML_ASSOCIATION_CHANGE_LEFT_TARGET = 2;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_LEFT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_LEFT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_LEFT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_LEFT_TARGET__REQUIRES = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_LEFT_TARGET__REQUIRED_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_LEFT_TARGET__RIGHT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_LEFT_TARGET__LEFT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_LEFT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_LEFT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Association Change Left Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_LEFT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLAssociationChangeRightTargetImpl <em>UML Association Change Right Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLAssociationChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLAssociationChangeRightTarget()
	 * @generated
	 */
	int UML_ASSOCIATION_CHANGE_RIGHT_TARGET = 3;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_RIGHT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_RIGHT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_RIGHT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_RIGHT_TARGET__REQUIRES = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_RIGHT_TARGET__REQUIRED_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Left Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_RIGHT_TARGET__LEFT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_RIGHT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Association Change Right Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_RIGHT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChange <em>UML Association Branch Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChange
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLAssociationBranchChange()
	 * @generated
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE = 4;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE__SUB_DIFF_ELEMENTS = UML_DIFF_EXTENSION__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE__IS_HIDDEN_BY = UML_DIFF_EXTENSION__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE__CONFLICTING = UML_DIFF_EXTENSION__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE__KIND = UML_DIFF_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE__REMOTE = UML_DIFF_EXTENSION__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE__REQUIRES = UML_DIFF_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE__REQUIRED_BY = UML_DIFF_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE__HIDE_ELEMENTS = UML_DIFF_EXTENSION__HIDE_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE__IS_COLLAPSED = UML_DIFF_EXTENSION__IS_COLLAPSED;

	/**
	 * The number of structural features of the '<em>UML Association Branch Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_FEATURE_COUNT = UML_DIFF_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLAssociationBranchChangeLeftTargetImpl <em>UML Association Branch Change Left Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLAssociationBranchChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLAssociationBranchChangeLeftTarget()
	 * @generated
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_LEFT_TARGET = 5;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_LEFT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_LEFT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_LEFT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_LEFT_TARGET__REQUIRES = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_LEFT_TARGET__REQUIRED_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_LEFT_TARGET__RIGHT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_LEFT_TARGET__LEFT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_LEFT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_LEFT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Association Branch Change Left Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_LEFT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLAssociationBranchChangeRightTargetImpl <em>UML Association Branch Change Right Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLAssociationBranchChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLAssociationBranchChangeRightTarget()
	 * @generated
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_RIGHT_TARGET = 6;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_RIGHT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_RIGHT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_RIGHT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_RIGHT_TARGET__REQUIRES = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_RIGHT_TARGET__REQUIRED_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Left Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_RIGHT_TARGET__LEFT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_RIGHT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Association Branch Change Right Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_BRANCH_CHANGE_RIGHT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChange <em>UML Dependency Branch Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChange
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDependencyBranchChange()
	 * @generated
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE = 7;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE__SUB_DIFF_ELEMENTS = UML_DIFF_EXTENSION__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE__IS_HIDDEN_BY = UML_DIFF_EXTENSION__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE__CONFLICTING = UML_DIFF_EXTENSION__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE__KIND = UML_DIFF_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE__REMOTE = UML_DIFF_EXTENSION__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE__REQUIRES = UML_DIFF_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE__REQUIRED_BY = UML_DIFF_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE__HIDE_ELEMENTS = UML_DIFF_EXTENSION__HIDE_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE__IS_COLLAPSED = UML_DIFF_EXTENSION__IS_COLLAPSED;

	/**
	 * The number of structural features of the '<em>UML Dependency Branch Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_FEATURE_COUNT = UML_DIFF_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLDependencyBranchChangeLeftTargetImpl <em>UML Dependency Branch Change Left Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLDependencyBranchChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDependencyBranchChangeLeftTarget()
	 * @generated
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET = 8;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET__CONFLICTING = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET__KIND = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET__REMOTE = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET__REQUIRES = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET__REQUIRED_BY = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET__REFERENCE = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET__RIGHT_ELEMENT = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET__LEFT_ELEMENT = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET__LEFT_TARGET = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__LEFT_TARGET;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET__RIGHT_TARGET = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__RIGHT_TARGET;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET__HIDE_ELEMENTS = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET__IS_COLLAPSED = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Dependency Branch Change Left Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET_FEATURE_COUNT = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLDependencyBranchChangeRightTargetImpl <em>UML Dependency Branch Change Right Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLDependencyBranchChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDependencyBranchChangeRightTarget()
	 * @generated
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET = 9;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET__CONFLICTING = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET__KIND = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET__REMOTE = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET__REQUIRES = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET__REQUIRED_BY = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET__REFERENCE = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET__LEFT_ELEMENT = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET__RIGHT_TARGET = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_TARGET;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET__LEFT_TARGET = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__LEFT_TARGET;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET__IS_COLLAPSED = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Dependency Branch Change Right Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET_FEATURE_COUNT = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChange <em>UML Generalization Set Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChange
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLGeneralizationSetChange()
	 * @generated
	 */
	int UML_GENERALIZATION_SET_CHANGE = 10;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__SUB_DIFF_ELEMENTS = UML_DIFF_EXTENSION__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__IS_HIDDEN_BY = UML_DIFF_EXTENSION__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__CONFLICTING = UML_DIFF_EXTENSION__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__KIND = UML_DIFF_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__REMOTE = UML_DIFF_EXTENSION__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__REQUIRES = UML_DIFF_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__REQUIRED_BY = UML_DIFF_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__HIDE_ELEMENTS = UML_DIFF_EXTENSION__HIDE_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__IS_COLLAPSED = UML_DIFF_EXTENSION__IS_COLLAPSED;

	/**
	 * The number of structural features of the '<em>UML Generalization Set Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_FEATURE_COUNT = UML_DIFF_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLGeneralizationSetChangeLeftTargetImpl <em>UML Generalization Set Change Left Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLGeneralizationSetChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLGeneralizationSetChangeLeftTarget()
	 * @generated
	 */
	int UML_GENERALIZATION_SET_CHANGE_LEFT_TARGET = 11;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_LEFT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_LEFT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_LEFT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_LEFT_TARGET__REQUIRES = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_LEFT_TARGET__REQUIRED_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_LEFT_TARGET__RIGHT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_LEFT_TARGET__LEFT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_LEFT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_LEFT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Generalization Set Change Left Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_LEFT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLGeneralizationSetChangeRightTargetImpl <em>UML Generalization Set Change Right Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLGeneralizationSetChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLGeneralizationSetChangeRightTarget()
	 * @generated
	 */
	int UML_GENERALIZATION_SET_CHANGE_RIGHT_TARGET = 12;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_RIGHT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_RIGHT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_RIGHT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_RIGHT_TARGET__REQUIRES = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_RIGHT_TARGET__REQUIRED_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Left Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_RIGHT_TARGET__LEFT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_RIGHT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Generalization Set Change Right Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_RIGHT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.UMLDependencyChange <em>UML Dependency Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.UMLDependencyChange
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDependencyChange()
	 * @generated
	 */
	int UML_DEPENDENCY_CHANGE = 13;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__SUB_DIFF_ELEMENTS = UML_DIFF_EXTENSION__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__IS_HIDDEN_BY = UML_DIFF_EXTENSION__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__CONFLICTING = UML_DIFF_EXTENSION__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__KIND = UML_DIFF_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__REMOTE = UML_DIFF_EXTENSION__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__REQUIRES = UML_DIFF_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__REQUIRED_BY = UML_DIFF_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__HIDE_ELEMENTS = UML_DIFF_EXTENSION__HIDE_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__IS_COLLAPSED = UML_DIFF_EXTENSION__IS_COLLAPSED;

	/**
	 * The number of structural features of the '<em>UML Dependency Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_FEATURE_COUNT = UML_DIFF_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLDependencyChangeLeftTargetImpl <em>UML Dependency Change Left Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLDependencyChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDependencyChangeLeftTarget()
	 * @generated
	 */
	int UML_DEPENDENCY_CHANGE_LEFT_TARGET = 14;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_LEFT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_LEFT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_LEFT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_LEFT_TARGET__REQUIRES = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_LEFT_TARGET__REQUIRED_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_LEFT_TARGET__RIGHT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_LEFT_TARGET__LEFT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_LEFT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_LEFT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Dependency Change Left Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_LEFT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLDependencyChangeRightTargetImpl <em>UML Dependency Change Right Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLDependencyChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDependencyChangeRightTarget()
	 * @generated
	 */
	int UML_DEPENDENCY_CHANGE_RIGHT_TARGET = 15;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_RIGHT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_RIGHT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_RIGHT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_RIGHT_TARGET__REQUIRES = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_RIGHT_TARGET__REQUIRED_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Left Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_RIGHT_TARGET__LEFT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_RIGHT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Dependency Change Right Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_RIGHT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.UMLExtendChange <em>UML Extend Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.UMLExtendChange
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLExtendChange()
	 * @generated
	 */
	int UML_EXTEND_CHANGE = 16;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__SUB_DIFF_ELEMENTS = UML_DIFF_EXTENSION__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__IS_HIDDEN_BY = UML_DIFF_EXTENSION__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__CONFLICTING = UML_DIFF_EXTENSION__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__KIND = UML_DIFF_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__REMOTE = UML_DIFF_EXTENSION__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__REQUIRES = UML_DIFF_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__REQUIRED_BY = UML_DIFF_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__HIDE_ELEMENTS = UML_DIFF_EXTENSION__HIDE_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__IS_COLLAPSED = UML_DIFF_EXTENSION__IS_COLLAPSED;

	/**
	 * The number of structural features of the '<em>UML Extend Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_FEATURE_COUNT = UML_DIFF_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLExtendChangeLeftTargetImpl <em>UML Extend Change Left Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLExtendChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLExtendChangeLeftTarget()
	 * @generated
	 */
	int UML_EXTEND_CHANGE_LEFT_TARGET = 17;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_LEFT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_LEFT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_LEFT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_LEFT_TARGET__REQUIRES = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_LEFT_TARGET__REQUIRED_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_LEFT_TARGET__RIGHT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_LEFT_TARGET__LEFT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_LEFT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_LEFT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Extend Change Left Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_LEFT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLExtendChangeRightTargetImpl <em>UML Extend Change Right Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLExtendChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLExtendChangeRightTarget()
	 * @generated
	 */
	int UML_EXTEND_CHANGE_RIGHT_TARGET = 18;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_RIGHT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_RIGHT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_RIGHT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_RIGHT_TARGET__REQUIRES = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_RIGHT_TARGET__REQUIRED_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Left Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_RIGHT_TARGET__LEFT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_RIGHT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Extend Change Right Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_RIGHT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChange <em>UML Execution Specification Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChange
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLExecutionSpecificationChange()
	 * @generated
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE = 19;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__SUB_DIFF_ELEMENTS = UML_DIFF_EXTENSION__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__IS_HIDDEN_BY = UML_DIFF_EXTENSION__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__CONFLICTING = UML_DIFF_EXTENSION__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__KIND = UML_DIFF_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__REMOTE = UML_DIFF_EXTENSION__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__REQUIRES = UML_DIFF_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__REQUIRED_BY = UML_DIFF_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__HIDE_ELEMENTS = UML_DIFF_EXTENSION__HIDE_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__IS_COLLAPSED = UML_DIFF_EXTENSION__IS_COLLAPSED;

	/**
	 * The number of structural features of the '<em>UML Execution Specification Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_FEATURE_COUNT = UML_DIFF_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLExecutionSpecificationChangeLeftTargetImpl <em>UML Execution Specification Change Left Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLExecutionSpecificationChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLExecutionSpecificationChangeLeftTarget()
	 * @generated
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_LEFT_TARGET = 20;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_LEFT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_LEFT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_LEFT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_LEFT_TARGET__REQUIRES = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_LEFT_TARGET__REQUIRED_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_LEFT_TARGET__RIGHT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_LEFT_TARGET__LEFT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_LEFT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_LEFT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Execution Specification Change Left Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_LEFT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLExecutionSpecificationChangeRightTargetImpl <em>UML Execution Specification Change Right Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLExecutionSpecificationChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLExecutionSpecificationChangeRightTarget()
	 * @generated
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET = 21;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__REQUIRES = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__REQUIRED_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Left Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__LEFT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Execution Specification Change Right Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.UMLDestructionEventChange <em>UML Destruction Event Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.UMLDestructionEventChange
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDestructionEventChange()
	 * @generated
	 */
	int UML_DESTRUCTION_EVENT_CHANGE = 22;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__SUB_DIFF_ELEMENTS = UML_DIFF_EXTENSION__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__IS_HIDDEN_BY = UML_DIFF_EXTENSION__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__CONFLICTING = UML_DIFF_EXTENSION__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__KIND = UML_DIFF_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__REMOTE = UML_DIFF_EXTENSION__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__REQUIRES = UML_DIFF_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__REQUIRED_BY = UML_DIFF_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__HIDE_ELEMENTS = UML_DIFF_EXTENSION__HIDE_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__IS_COLLAPSED = UML_DIFF_EXTENSION__IS_COLLAPSED;

	/**
	 * The number of structural features of the '<em>UML Destruction Event Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_FEATURE_COUNT = UML_DIFF_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLDestructionEventChangeLeftTargetImpl <em>UML Destruction Event Change Left Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLDestructionEventChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDestructionEventChangeLeftTarget()
	 * @generated
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_LEFT_TARGET = 23;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_LEFT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_LEFT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_LEFT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_LEFT_TARGET__REQUIRES = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_LEFT_TARGET__REQUIRED_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_LEFT_TARGET__RIGHT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_LEFT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_LEFT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Destruction Event Change Left Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_LEFT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLDestructionEventChangeRightTargetImpl <em>UML Destruction Event Change Right Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLDestructionEventChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDestructionEventChangeRightTarget()
	 * @generated
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_RIGHT_TARGET = 24;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_RIGHT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_RIGHT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_RIGHT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_RIGHT_TARGET__REQUIRES = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_RIGHT_TARGET__REQUIRED_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Left Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_RIGHT_TARGET__LEFT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_RIGHT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Destruction Event Change Right Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChange <em>UML Interval Constraint Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChange
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLIntervalConstraintChange()
	 * @generated
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE = 25;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__SUB_DIFF_ELEMENTS = UML_DIFF_EXTENSION__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__IS_HIDDEN_BY = UML_DIFF_EXTENSION__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__CONFLICTING = UML_DIFF_EXTENSION__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__KIND = UML_DIFF_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__REMOTE = UML_DIFF_EXTENSION__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__REQUIRES = UML_DIFF_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__REQUIRED_BY = UML_DIFF_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__HIDE_ELEMENTS = UML_DIFF_EXTENSION__HIDE_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__IS_COLLAPSED = UML_DIFF_EXTENSION__IS_COLLAPSED;

	/**
	 * The number of structural features of the '<em>UML Interval Constraint Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_FEATURE_COUNT = UML_DIFF_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLIntervalConstraintChangeLeftTargetImpl <em>UML Interval Constraint Change Left Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLIntervalConstraintChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLIntervalConstraintChangeLeftTarget()
	 * @generated
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_LEFT_TARGET = 26;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_LEFT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_LEFT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_LEFT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_LEFT_TARGET__REQUIRES = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_LEFT_TARGET__REQUIRED_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_LEFT_TARGET__RIGHT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_LEFT_TARGET__LEFT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_LEFT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_LEFT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Interval Constraint Change Left Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_LEFT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLIntervalConstraintChangeRightTargetImpl <em>UML Interval Constraint Change Right Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLIntervalConstraintChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLIntervalConstraintChangeRightTarget()
	 * @generated
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_RIGHT_TARGET = 27;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_RIGHT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_RIGHT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_RIGHT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_RIGHT_TARGET__REQUIRES = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_RIGHT_TARGET__REQUIRED_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Left Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_RIGHT_TARGET__LEFT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_RIGHT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Interval Constraint Change Right Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_RIGHT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.UMLMessageChange <em>UML Message Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.UMLMessageChange
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLMessageChange()
	 * @generated
	 */
	int UML_MESSAGE_CHANGE = 28;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__SUB_DIFF_ELEMENTS = UML_DIFF_EXTENSION__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__IS_HIDDEN_BY = UML_DIFF_EXTENSION__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__CONFLICTING = UML_DIFF_EXTENSION__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__KIND = UML_DIFF_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__REMOTE = UML_DIFF_EXTENSION__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__REQUIRES = UML_DIFF_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__REQUIRED_BY = UML_DIFF_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__HIDE_ELEMENTS = UML_DIFF_EXTENSION__HIDE_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__IS_COLLAPSED = UML_DIFF_EXTENSION__IS_COLLAPSED;

	/**
	 * The number of structural features of the '<em>UML Message Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_FEATURE_COUNT = UML_DIFF_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLMessageChangeLeftTargetImpl <em>UML Message Change Left Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLMessageChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLMessageChangeLeftTarget()
	 * @generated
	 */
	int UML_MESSAGE_CHANGE_LEFT_TARGET = 29;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_LEFT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_LEFT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_LEFT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_LEFT_TARGET__REQUIRES = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_LEFT_TARGET__REQUIRED_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_LEFT_TARGET__RIGHT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_LEFT_TARGET__LEFT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_LEFT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_LEFT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Message Change Left Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_LEFT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLMessageChangeRightTargetImpl <em>UML Message Change Right Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLMessageChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLMessageChangeRightTarget()
	 * @generated
	 */
	int UML_MESSAGE_CHANGE_RIGHT_TARGET = 30;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_RIGHT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_RIGHT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_RIGHT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_RIGHT_TARGET__REQUIRES = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_RIGHT_TARGET__REQUIRED_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Left Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_RIGHT_TARGET__LEFT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_RIGHT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Message Change Right Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_RIGHT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange <em>UML Stereotype Property Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypePropertyChange()
	 * @generated
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE = 31;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__SUB_DIFF_ELEMENTS = UML_DIFF_EXTENSION__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__IS_HIDDEN_BY = UML_DIFF_EXTENSION__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__CONFLICTING = UML_DIFF_EXTENSION__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__KIND = UML_DIFF_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__REMOTE = UML_DIFF_EXTENSION__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__REQUIRES = UML_DIFF_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__REQUIRED_BY = UML_DIFF_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__HIDE_ELEMENTS = UML_DIFF_EXTENSION__HIDE_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__IS_COLLAPSED = UML_DIFF_EXTENSION__IS_COLLAPSED;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__STEREOTYPE = UML_DIFF_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>UML Stereotype Property Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE_FEATURE_COUNT = UML_DIFF_EXTENSION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeAttributeChangeLeftTargetImpl <em>UML Stereotype Attribute Change Left Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeAttributeChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeAttributeChangeLeftTarget()
	 * @generated
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET = 32;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET__CONFLICTING = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET__KIND = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET__REMOTE = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET__REQUIRES = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET__REQUIRED_BY = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET__ATTRIBUTE = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_ELEMENT = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET__RIGHT_ELEMENT = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_TARGET = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_TARGET;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET__HIDE_ELEMENTS = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET__IS_COLLAPSED = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET__STEREOTYPE = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>UML Stereotype Attribute Change Left Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET_FEATURE_COUNT = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeAttributeChangeRightTargetImpl <em>UML Stereotype Attribute Change Right Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeAttributeChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeAttributeChangeRightTarget()
	 * @generated
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET = 33;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET__CONFLICTING = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET__KIND = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET__REMOTE = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET__REQUIRES = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET__REQUIRED_BY = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET__ATTRIBUTE = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET__LEFT_ELEMENT = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_TARGET = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_TARGET;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET__IS_COLLAPSED = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET__STEREOTYPE = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>UML Stereotype Attribute Change Right Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET_FEATURE_COUNT = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeUpdateAttributeImpl <em>UML Stereotype Update Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeUpdateAttributeImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeUpdateAttribute()
	 * @generated
	 */
	int UML_STEREOTYPE_UPDATE_ATTRIBUTE = 34;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_ATTRIBUTE__SUB_DIFF_ELEMENTS = DiffPackage.UPDATE_ATTRIBUTE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_ATTRIBUTE__IS_HIDDEN_BY = DiffPackage.UPDATE_ATTRIBUTE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_ATTRIBUTE__CONFLICTING = DiffPackage.UPDATE_ATTRIBUTE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_ATTRIBUTE__KIND = DiffPackage.UPDATE_ATTRIBUTE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_ATTRIBUTE__REMOTE = DiffPackage.UPDATE_ATTRIBUTE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_ATTRIBUTE__REQUIRES = DiffPackage.UPDATE_ATTRIBUTE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_ATTRIBUTE__REQUIRED_BY = DiffPackage.UPDATE_ATTRIBUTE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_ATTRIBUTE__ATTRIBUTE = DiffPackage.UPDATE_ATTRIBUTE__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_ATTRIBUTE__LEFT_ELEMENT = DiffPackage.UPDATE_ATTRIBUTE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_ATTRIBUTE__RIGHT_ELEMENT = DiffPackage.UPDATE_ATTRIBUTE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_ATTRIBUTE__HIDE_ELEMENTS = DiffPackage.UPDATE_ATTRIBUTE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_ATTRIBUTE__IS_COLLAPSED = DiffPackage.UPDATE_ATTRIBUTE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_ATTRIBUTE__STEREOTYPE = DiffPackage.UPDATE_ATTRIBUTE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>UML Stereotype Update Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_ATTRIBUTE_FEATURE_COUNT = DiffPackage.UPDATE_ATTRIBUTE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationChange <em>UML Stereotype Application Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationChange
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeApplicationChange()
	 * @generated
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE = 35;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__SUB_DIFF_ELEMENTS = UML_DIFF_EXTENSION__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__IS_HIDDEN_BY = UML_DIFF_EXTENSION__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__CONFLICTING = UML_DIFF_EXTENSION__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__KIND = UML_DIFF_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__REMOTE = UML_DIFF_EXTENSION__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__REQUIRES = UML_DIFF_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__REQUIRED_BY = UML_DIFF_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__HIDE_ELEMENTS = UML_DIFF_EXTENSION__HIDE_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__IS_COLLAPSED = UML_DIFF_EXTENSION__IS_COLLAPSED;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE = UML_DIFF_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>UML Stereotype Application Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE_FEATURE_COUNT = UML_DIFF_EXTENSION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationAdditionImpl <em>UML Stereotype Application Addition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationAdditionImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeApplicationAddition()
	 * @generated
	 */
	int UML_STEREOTYPE_APPLICATION_ADDITION = 36;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_ADDITION__SUB_DIFF_ELEMENTS = DiffPackage.UPDATE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_ADDITION__IS_HIDDEN_BY = DiffPackage.UPDATE_MODEL_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_ADDITION__CONFLICTING = DiffPackage.UPDATE_MODEL_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_ADDITION__KIND = DiffPackage.UPDATE_MODEL_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_ADDITION__REMOTE = DiffPackage.UPDATE_MODEL_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_ADDITION__REQUIRES = DiffPackage.UPDATE_MODEL_ELEMENT__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_ADDITION__REQUIRED_BY = DiffPackage.UPDATE_MODEL_ELEMENT__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_ADDITION__RIGHT_ELEMENT = DiffPackage.UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_ADDITION__LEFT_ELEMENT = DiffPackage.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_ADDITION__HIDE_ELEMENTS = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_ADDITION__IS_COLLAPSED = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_ADDITION__STEREOTYPE = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>UML Stereotype Application Addition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_ADDITION_FEATURE_COUNT = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationRemovalImpl <em>UML Stereotype Application Removal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationRemovalImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeApplicationRemoval()
	 * @generated
	 */
	int UML_STEREOTYPE_APPLICATION_REMOVAL = 37;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_REMOVAL__SUB_DIFF_ELEMENTS = DiffPackage.UPDATE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_REMOVAL__IS_HIDDEN_BY = DiffPackage.UPDATE_MODEL_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_REMOVAL__CONFLICTING = DiffPackage.UPDATE_MODEL_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_REMOVAL__KIND = DiffPackage.UPDATE_MODEL_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_REMOVAL__REMOTE = DiffPackage.UPDATE_MODEL_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_REMOVAL__REQUIRES = DiffPackage.UPDATE_MODEL_ELEMENT__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_REMOVAL__REQUIRED_BY = DiffPackage.UPDATE_MODEL_ELEMENT__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_REMOVAL__RIGHT_ELEMENT = DiffPackage.UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_REMOVAL__LEFT_ELEMENT = DiffPackage.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_REMOVAL__HIDE_ELEMENTS = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_REMOVAL__IS_COLLAPSED = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_REMOVAL__STEREOTYPE = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>UML Stereotype Application Removal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_REMOVAL_FEATURE_COUNT = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 3;


	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeReferenceChangeLeftTargetImpl <em>UML Stereotype Reference Change Left Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeReferenceChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeReferenceChangeLeftTarget()
	 * @generated
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET = 38;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__CONFLICTING = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__KIND = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__REMOTE = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__REQUIRES = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__REQUIRED_BY = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__REFERENCE = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__RIGHT_ELEMENT = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__LEFT_ELEMENT = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__LEFT_TARGET = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__LEFT_TARGET;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__RIGHT_TARGET = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__RIGHT_TARGET;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__HIDE_ELEMENTS = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__IS_COLLAPSED = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__STEREOTYPE = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>UML Stereotype Reference Change Left Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET_FEATURE_COUNT = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeReferenceChangeRightTargetImpl <em>UML Stereotype Reference Change Right Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeReferenceChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeReferenceChangeRightTarget()
	 * @generated
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET = 39;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__CONFLICTING = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__KIND = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__REMOTE = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__REQUIRES = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__REQUIRED_BY = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__REFERENCE = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__LEFT_ELEMENT = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_TARGET = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_TARGET;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__LEFT_TARGET = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__LEFT_TARGET;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__IS_COLLAPSED = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__STEREOTYPE = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>UML Stereotype Reference Change Right Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET_FEATURE_COUNT = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 3;


	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeUpdateReferenceImpl <em>UML Stereotype Update Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeUpdateReferenceImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeUpdateReference()
	 * @generated
	 */
	int UML_STEREOTYPE_UPDATE_REFERENCE = 40;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_REFERENCE__SUB_DIFF_ELEMENTS = DiffPackage.UPDATE_REFERENCE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_REFERENCE__IS_HIDDEN_BY = DiffPackage.UPDATE_REFERENCE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_REFERENCE__CONFLICTING = DiffPackage.UPDATE_REFERENCE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_REFERENCE__KIND = DiffPackage.UPDATE_REFERENCE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_REFERENCE__REMOTE = DiffPackage.UPDATE_REFERENCE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_REFERENCE__REQUIRES = DiffPackage.UPDATE_REFERENCE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_REFERENCE__REQUIRED_BY = DiffPackage.UPDATE_REFERENCE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_REFERENCE__REFERENCE = DiffPackage.UPDATE_REFERENCE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_REFERENCE__RIGHT_ELEMENT = DiffPackage.UPDATE_REFERENCE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_REFERENCE__LEFT_ELEMENT = DiffPackage.UPDATE_REFERENCE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_REFERENCE__LEFT_TARGET = DiffPackage.UPDATE_REFERENCE__LEFT_TARGET;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_REFERENCE__RIGHT_TARGET = DiffPackage.UPDATE_REFERENCE__RIGHT_TARGET;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_REFERENCE__HIDE_ELEMENTS = DiffPackage.UPDATE_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_REFERENCE__IS_COLLAPSED = DiffPackage.UPDATE_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_REFERENCE__STEREOTYPE = DiffPackage.UPDATE_REFERENCE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>UML Stereotype Update Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_REFERENCE_FEATURE_COUNT = DiffPackage.UPDATE_REFERENCE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeReferenceOrderChangeImpl <em>UML Stereotype Reference Order Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeReferenceOrderChangeImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeReferenceOrderChange()
	 * @generated
	 */
	int UML_STEREOTYPE_REFERENCE_ORDER_CHANGE = 41;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_ORDER_CHANGE__SUB_DIFF_ELEMENTS = DiffPackage.REFERENCE_ORDER_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_ORDER_CHANGE__IS_HIDDEN_BY = DiffPackage.REFERENCE_ORDER_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_ORDER_CHANGE__CONFLICTING = DiffPackage.REFERENCE_ORDER_CHANGE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_ORDER_CHANGE__KIND = DiffPackage.REFERENCE_ORDER_CHANGE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_ORDER_CHANGE__REMOTE = DiffPackage.REFERENCE_ORDER_CHANGE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_ORDER_CHANGE__REQUIRES = DiffPackage.REFERENCE_ORDER_CHANGE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_ORDER_CHANGE__REQUIRED_BY = DiffPackage.REFERENCE_ORDER_CHANGE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_ORDER_CHANGE__REFERENCE = DiffPackage.REFERENCE_ORDER_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_ORDER_CHANGE__RIGHT_ELEMENT = DiffPackage.REFERENCE_ORDER_CHANGE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_ORDER_CHANGE__LEFT_ELEMENT = DiffPackage.REFERENCE_ORDER_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_ORDER_CHANGE__LEFT_TARGET = DiffPackage.REFERENCE_ORDER_CHANGE__LEFT_TARGET;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_ORDER_CHANGE__RIGHT_TARGET = DiffPackage.REFERENCE_ORDER_CHANGE__RIGHT_TARGET;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_ORDER_CHANGE__HIDE_ELEMENTS = DiffPackage.REFERENCE_ORDER_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_ORDER_CHANGE__IS_COLLAPSED = DiffPackage.REFERENCE_ORDER_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_ORDER_CHANGE__STEREOTYPE = DiffPackage.REFERENCE_ORDER_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>UML Stereotype Reference Order Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_ORDER_CHANGE_FEATURE_COUNT = DiffPackage.REFERENCE_ORDER_CHANGE_FEATURE_COUNT + 3;


	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.UMLProfileApplicationChange <em>UML Profile Application Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.UMLProfileApplicationChange
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLProfileApplicationChange()
	 * @generated
	 */
	int UML_PROFILE_APPLICATION_CHANGE = 42;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__SUB_DIFF_ELEMENTS = UML_DIFF_EXTENSION__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__IS_HIDDEN_BY = UML_DIFF_EXTENSION__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__CONFLICTING = UML_DIFF_EXTENSION__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__KIND = UML_DIFF_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__REMOTE = UML_DIFF_EXTENSION__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__REQUIRES = UML_DIFF_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__REQUIRED_BY = UML_DIFF_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__HIDE_ELEMENTS = UML_DIFF_EXTENSION__HIDE_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__IS_COLLAPSED = UML_DIFF_EXTENSION__IS_COLLAPSED;

	/**
	 * The feature id for the '<em><b>Profile</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__PROFILE = UML_DIFF_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>UML Profile Application Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE_FEATURE_COUNT = UML_DIFF_EXTENSION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLProfileApplicationAdditionImpl <em>UML Profile Application Addition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLProfileApplicationAdditionImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLProfileApplicationAddition()
	 * @generated
	 */
	int UML_PROFILE_APPLICATION_ADDITION = 43;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_ADDITION__SUB_DIFF_ELEMENTS = DiffPackage.UPDATE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_ADDITION__IS_HIDDEN_BY = DiffPackage.UPDATE_MODEL_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_ADDITION__CONFLICTING = DiffPackage.UPDATE_MODEL_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_ADDITION__KIND = DiffPackage.UPDATE_MODEL_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_ADDITION__REMOTE = DiffPackage.UPDATE_MODEL_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_ADDITION__REQUIRES = DiffPackage.UPDATE_MODEL_ELEMENT__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_ADDITION__REQUIRED_BY = DiffPackage.UPDATE_MODEL_ELEMENT__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_ADDITION__RIGHT_ELEMENT = DiffPackage.UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_ADDITION__LEFT_ELEMENT = DiffPackage.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_ADDITION__HIDE_ELEMENTS = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_ADDITION__IS_COLLAPSED = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Profile</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_ADDITION__PROFILE = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>UML Profile Application Addition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_ADDITION_FEATURE_COUNT = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLProfileApplicationRemovalImpl <em>UML Profile Application Removal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLProfileApplicationRemovalImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLProfileApplicationRemoval()
	 * @generated
	 */
	int UML_PROFILE_APPLICATION_REMOVAL = 44;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_REMOVAL__SUB_DIFF_ELEMENTS = DiffPackage.UPDATE_MODEL_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_REMOVAL__IS_HIDDEN_BY = DiffPackage.UPDATE_MODEL_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_REMOVAL__CONFLICTING = DiffPackage.UPDATE_MODEL_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_REMOVAL__KIND = DiffPackage.UPDATE_MODEL_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_REMOVAL__REMOTE = DiffPackage.UPDATE_MODEL_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_REMOVAL__REQUIRES = DiffPackage.UPDATE_MODEL_ELEMENT__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_REMOVAL__REQUIRED_BY = DiffPackage.UPDATE_MODEL_ELEMENT__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_REMOVAL__RIGHT_ELEMENT = DiffPackage.UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_REMOVAL__LEFT_ELEMENT = DiffPackage.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_REMOVAL__HIDE_ELEMENTS = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_REMOVAL__IS_COLLAPSED = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Profile</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_REMOVAL__PROFILE = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>UML Profile Application Removal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_REMOVAL_FEATURE_COUNT = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 3;


	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLDiffExtension <em>UML Diff Extension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Diff Extension</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDiffExtension
	 * @generated
	 */
	EClass getUMLDiffExtension();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLAssociationChange <em>UML Association Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Association Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLAssociationChange
	 * @generated
	 */
	EClass getUMLAssociationChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLAssociationChangeLeftTarget <em>UML Association Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Association Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLAssociationChangeLeftTarget
	 * @generated
	 */
	EClass getUMLAssociationChangeLeftTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLAssociationChangeRightTarget <em>UML Association Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Association Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLAssociationChangeRightTarget
	 * @generated
	 */
	EClass getUMLAssociationChangeRightTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChange <em>UML Association Branch Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Association Branch Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChange
	 * @generated
	 */
	EClass getUMLAssociationBranchChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeLeftTarget <em>UML Association Branch Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Association Branch Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeLeftTarget
	 * @generated
	 */
	EClass getUMLAssociationBranchChangeLeftTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeRightTarget <em>UML Association Branch Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Association Branch Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeRightTarget
	 * @generated
	 */
	EClass getUMLAssociationBranchChangeRightTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChange <em>UML Dependency Branch Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Dependency Branch Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChange
	 * @generated
	 */
	EClass getUMLDependencyBranchChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeLeftTarget <em>UML Dependency Branch Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Dependency Branch Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeLeftTarget
	 * @generated
	 */
	EClass getUMLDependencyBranchChangeLeftTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeRightTarget <em>UML Dependency Branch Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Dependency Branch Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeRightTarget
	 * @generated
	 */
	EClass getUMLDependencyBranchChangeRightTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChange <em>UML Generalization Set Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Generalization Set Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChange
	 * @generated
	 */
	EClass getUMLGeneralizationSetChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeLeftTarget <em>UML Generalization Set Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Generalization Set Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeLeftTarget
	 * @generated
	 */
	EClass getUMLGeneralizationSetChangeLeftTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeRightTarget <em>UML Generalization Set Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Generalization Set Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeRightTarget
	 * @generated
	 */
	EClass getUMLGeneralizationSetChangeRightTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLDependencyChange <em>UML Dependency Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Dependency Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDependencyChange
	 * @generated
	 */
	EClass getUMLDependencyChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLDependencyChangeLeftTarget <em>UML Dependency Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Dependency Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDependencyChangeLeftTarget
	 * @generated
	 */
	EClass getUMLDependencyChangeLeftTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLDependencyChangeRightTarget <em>UML Dependency Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Dependency Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDependencyChangeRightTarget
	 * @generated
	 */
	EClass getUMLDependencyChangeRightTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLExtendChange <em>UML Extend Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Extend Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLExtendChange
	 * @generated
	 */
	EClass getUMLExtendChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLExtendChangeLeftTarget <em>UML Extend Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Extend Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLExtendChangeLeftTarget
	 * @generated
	 */
	EClass getUMLExtendChangeLeftTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLExtendChangeRightTarget <em>UML Extend Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Extend Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLExtendChangeRightTarget
	 * @generated
	 */
	EClass getUMLExtendChangeRightTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChange <em>UML Execution Specification Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Execution Specification Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChange
	 * @generated
	 */
	EClass getUMLExecutionSpecificationChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeLeftTarget <em>UML Execution Specification Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Execution Specification Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeLeftTarget
	 * @generated
	 */
	EClass getUMLExecutionSpecificationChangeLeftTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeRightTarget <em>UML Execution Specification Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Execution Specification Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeRightTarget
	 * @generated
	 */
	EClass getUMLExecutionSpecificationChangeRightTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLDestructionEventChange <em>UML Destruction Event Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Destruction Event Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDestructionEventChange
	 * @generated
	 */
	EClass getUMLDestructionEventChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLDestructionEventChangeLeftTarget <em>UML Destruction Event Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Destruction Event Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDestructionEventChangeLeftTarget
	 * @generated
	 */
	EClass getUMLDestructionEventChangeLeftTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLDestructionEventChangeRightTarget <em>UML Destruction Event Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Destruction Event Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDestructionEventChangeRightTarget
	 * @generated
	 */
	EClass getUMLDestructionEventChangeRightTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChange <em>UML Interval Constraint Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Interval Constraint Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChange
	 * @generated
	 */
	EClass getUMLIntervalConstraintChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeLeftTarget <em>UML Interval Constraint Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Interval Constraint Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeLeftTarget
	 * @generated
	 */
	EClass getUMLIntervalConstraintChangeLeftTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeRightTarget <em>UML Interval Constraint Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Interval Constraint Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeRightTarget
	 * @generated
	 */
	EClass getUMLIntervalConstraintChangeRightTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLMessageChange <em>UML Message Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Message Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLMessageChange
	 * @generated
	 */
	EClass getUMLMessageChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLMessageChangeLeftTarget <em>UML Message Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Message Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLMessageChangeLeftTarget
	 * @generated
	 */
	EClass getUMLMessageChangeLeftTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLMessageChangeRightTarget <em>UML Message Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Message Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLMessageChangeRightTarget
	 * @generated
	 */
	EClass getUMLMessageChangeRightTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange <em>UML Stereotype Property Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Stereotype Property Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange
	 * @generated
	 */
	EClass getUMLStereotypePropertyChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange#getStereotype <em>Stereotype</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Stereotype</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange#getStereotype()
	 * @see #getUMLStereotypePropertyChange()
	 * @generated
	 */
	EReference getUMLStereotypePropertyChange_Stereotype();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeLeftTarget <em>UML Stereotype Attribute Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Stereotype Attribute Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeLeftTarget
	 * @generated
	 */
	EClass getUMLStereotypeAttributeChangeLeftTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeRightTarget <em>UML Stereotype Attribute Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Stereotype Attribute Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeRightTarget
	 * @generated
	 */
	EClass getUMLStereotypeAttributeChangeRightTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateAttribute <em>UML Stereotype Update Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Stereotype Update Attribute</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateAttribute
	 * @generated
	 */
	EClass getUMLStereotypeUpdateAttribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationChange <em>UML Stereotype Application Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Stereotype Application Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationChange
	 * @generated
	 */
	EClass getUMLStereotypeApplicationChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationChange#getStereotype <em>Stereotype</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Stereotype</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationChange#getStereotype()
	 * @see #getUMLStereotypeApplicationChange()
	 * @generated
	 */
	EReference getUMLStereotypeApplicationChange_Stereotype();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationAddition <em>UML Stereotype Application Addition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Stereotype Application Addition</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationAddition
	 * @generated
	 */
	EClass getUMLStereotypeApplicationAddition();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationRemoval <em>UML Stereotype Application Removal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Stereotype Application Removal</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationRemoval
	 * @generated
	 */
	EClass getUMLStereotypeApplicationRemoval();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeLeftTarget <em>UML Stereotype Reference Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Stereotype Reference Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeLeftTarget
	 * @generated
	 */
	EClass getUMLStereotypeReferenceChangeLeftTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeRightTarget <em>UML Stereotype Reference Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Stereotype Reference Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeRightTarget
	 * @generated
	 */
	EClass getUMLStereotypeReferenceChangeRightTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateReference <em>UML Stereotype Update Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Stereotype Update Reference</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateReference
	 * @generated
	 */
	EClass getUMLStereotypeUpdateReference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceOrderChange <em>UML Stereotype Reference Order Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Stereotype Reference Order Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceOrderChange
	 * @generated
	 */
	EClass getUMLStereotypeReferenceOrderChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLProfileApplicationChange <em>UML Profile Application Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Profile Application Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLProfileApplicationChange
	 * @generated
	 */
	EClass getUMLProfileApplicationChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.uml2diff.UMLProfileApplicationChange#getProfile <em>Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Profile</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLProfileApplicationChange#getProfile()
	 * @see #getUMLProfileApplicationChange()
	 * @generated
	 */
	EReference getUMLProfileApplicationChange_Profile();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLProfileApplicationAddition <em>UML Profile Application Addition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Profile Application Addition</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLProfileApplicationAddition
	 * @generated
	 */
	EClass getUMLProfileApplicationAddition();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLProfileApplicationRemoval <em>UML Profile Application Removal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Profile Application Removal</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLProfileApplicationRemoval
	 * @generated
	 */
	EClass getUMLProfileApplicationRemoval();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	UML2DiffFactory getUML2DiffFactory();

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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.UMLDiffExtension <em>UML Diff Extension</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.UMLDiffExtension
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDiffExtension()
		 * @generated
		 */
		EClass UML_DIFF_EXTENSION = eINSTANCE.getUMLDiffExtension();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.UMLAssociationChange <em>UML Association Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.UMLAssociationChange
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLAssociationChange()
		 * @generated
		 */
		EClass UML_ASSOCIATION_CHANGE = eINSTANCE.getUMLAssociationChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLAssociationChangeLeftTargetImpl <em>UML Association Change Left Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLAssociationChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLAssociationChangeLeftTarget()
		 * @generated
		 */
		EClass UML_ASSOCIATION_CHANGE_LEFT_TARGET = eINSTANCE.getUMLAssociationChangeLeftTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLAssociationChangeRightTargetImpl <em>UML Association Change Right Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLAssociationChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLAssociationChangeRightTarget()
		 * @generated
		 */
		EClass UML_ASSOCIATION_CHANGE_RIGHT_TARGET = eINSTANCE.getUMLAssociationChangeRightTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChange <em>UML Association Branch Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChange
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLAssociationBranchChange()
		 * @generated
		 */
		EClass UML_ASSOCIATION_BRANCH_CHANGE = eINSTANCE.getUMLAssociationBranchChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLAssociationBranchChangeLeftTargetImpl <em>UML Association Branch Change Left Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLAssociationBranchChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLAssociationBranchChangeLeftTarget()
		 * @generated
		 */
		EClass UML_ASSOCIATION_BRANCH_CHANGE_LEFT_TARGET = eINSTANCE.getUMLAssociationBranchChangeLeftTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLAssociationBranchChangeRightTargetImpl <em>UML Association Branch Change Right Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLAssociationBranchChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLAssociationBranchChangeRightTarget()
		 * @generated
		 */
		EClass UML_ASSOCIATION_BRANCH_CHANGE_RIGHT_TARGET = eINSTANCE.getUMLAssociationBranchChangeRightTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChange <em>UML Dependency Branch Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChange
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDependencyBranchChange()
		 * @generated
		 */
		EClass UML_DEPENDENCY_BRANCH_CHANGE = eINSTANCE.getUMLDependencyBranchChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLDependencyBranchChangeLeftTargetImpl <em>UML Dependency Branch Change Left Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLDependencyBranchChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDependencyBranchChangeLeftTarget()
		 * @generated
		 */
		EClass UML_DEPENDENCY_BRANCH_CHANGE_LEFT_TARGET = eINSTANCE.getUMLDependencyBranchChangeLeftTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLDependencyBranchChangeRightTargetImpl <em>UML Dependency Branch Change Right Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLDependencyBranchChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDependencyBranchChangeRightTarget()
		 * @generated
		 */
		EClass UML_DEPENDENCY_BRANCH_CHANGE_RIGHT_TARGET = eINSTANCE.getUMLDependencyBranchChangeRightTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChange <em>UML Generalization Set Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChange
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLGeneralizationSetChange()
		 * @generated
		 */
		EClass UML_GENERALIZATION_SET_CHANGE = eINSTANCE.getUMLGeneralizationSetChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLGeneralizationSetChangeLeftTargetImpl <em>UML Generalization Set Change Left Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLGeneralizationSetChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLGeneralizationSetChangeLeftTarget()
		 * @generated
		 */
		EClass UML_GENERALIZATION_SET_CHANGE_LEFT_TARGET = eINSTANCE.getUMLGeneralizationSetChangeLeftTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLGeneralizationSetChangeRightTargetImpl <em>UML Generalization Set Change Right Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLGeneralizationSetChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLGeneralizationSetChangeRightTarget()
		 * @generated
		 */
		EClass UML_GENERALIZATION_SET_CHANGE_RIGHT_TARGET = eINSTANCE.getUMLGeneralizationSetChangeRightTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.UMLDependencyChange <em>UML Dependency Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.UMLDependencyChange
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDependencyChange()
		 * @generated
		 */
		EClass UML_DEPENDENCY_CHANGE = eINSTANCE.getUMLDependencyChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLDependencyChangeLeftTargetImpl <em>UML Dependency Change Left Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLDependencyChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDependencyChangeLeftTarget()
		 * @generated
		 */
		EClass UML_DEPENDENCY_CHANGE_LEFT_TARGET = eINSTANCE.getUMLDependencyChangeLeftTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLDependencyChangeRightTargetImpl <em>UML Dependency Change Right Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLDependencyChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDependencyChangeRightTarget()
		 * @generated
		 */
		EClass UML_DEPENDENCY_CHANGE_RIGHT_TARGET = eINSTANCE.getUMLDependencyChangeRightTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.UMLExtendChange <em>UML Extend Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.UMLExtendChange
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLExtendChange()
		 * @generated
		 */
		EClass UML_EXTEND_CHANGE = eINSTANCE.getUMLExtendChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLExtendChangeLeftTargetImpl <em>UML Extend Change Left Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLExtendChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLExtendChangeLeftTarget()
		 * @generated
		 */
		EClass UML_EXTEND_CHANGE_LEFT_TARGET = eINSTANCE.getUMLExtendChangeLeftTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLExtendChangeRightTargetImpl <em>UML Extend Change Right Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLExtendChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLExtendChangeRightTarget()
		 * @generated
		 */
		EClass UML_EXTEND_CHANGE_RIGHT_TARGET = eINSTANCE.getUMLExtendChangeRightTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChange <em>UML Execution Specification Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChange
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLExecutionSpecificationChange()
		 * @generated
		 */
		EClass UML_EXECUTION_SPECIFICATION_CHANGE = eINSTANCE.getUMLExecutionSpecificationChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLExecutionSpecificationChangeLeftTargetImpl <em>UML Execution Specification Change Left Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLExecutionSpecificationChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLExecutionSpecificationChangeLeftTarget()
		 * @generated
		 */
		EClass UML_EXECUTION_SPECIFICATION_CHANGE_LEFT_TARGET = eINSTANCE.getUMLExecutionSpecificationChangeLeftTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLExecutionSpecificationChangeRightTargetImpl <em>UML Execution Specification Change Right Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLExecutionSpecificationChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLExecutionSpecificationChangeRightTarget()
		 * @generated
		 */
		EClass UML_EXECUTION_SPECIFICATION_CHANGE_RIGHT_TARGET = eINSTANCE.getUMLExecutionSpecificationChangeRightTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.UMLDestructionEventChange <em>UML Destruction Event Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.UMLDestructionEventChange
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDestructionEventChange()
		 * @generated
		 */
		EClass UML_DESTRUCTION_EVENT_CHANGE = eINSTANCE.getUMLDestructionEventChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLDestructionEventChangeLeftTargetImpl <em>UML Destruction Event Change Left Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLDestructionEventChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDestructionEventChangeLeftTarget()
		 * @generated
		 */
		EClass UML_DESTRUCTION_EVENT_CHANGE_LEFT_TARGET = eINSTANCE.getUMLDestructionEventChangeLeftTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLDestructionEventChangeRightTargetImpl <em>UML Destruction Event Change Right Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLDestructionEventChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLDestructionEventChangeRightTarget()
		 * @generated
		 */
		EClass UML_DESTRUCTION_EVENT_CHANGE_RIGHT_TARGET = eINSTANCE.getUMLDestructionEventChangeRightTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChange <em>UML Interval Constraint Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChange
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLIntervalConstraintChange()
		 * @generated
		 */
		EClass UML_INTERVAL_CONSTRAINT_CHANGE = eINSTANCE.getUMLIntervalConstraintChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLIntervalConstraintChangeLeftTargetImpl <em>UML Interval Constraint Change Left Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLIntervalConstraintChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLIntervalConstraintChangeLeftTarget()
		 * @generated
		 */
		EClass UML_INTERVAL_CONSTRAINT_CHANGE_LEFT_TARGET = eINSTANCE.getUMLIntervalConstraintChangeLeftTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLIntervalConstraintChangeRightTargetImpl <em>UML Interval Constraint Change Right Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLIntervalConstraintChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLIntervalConstraintChangeRightTarget()
		 * @generated
		 */
		EClass UML_INTERVAL_CONSTRAINT_CHANGE_RIGHT_TARGET = eINSTANCE.getUMLIntervalConstraintChangeRightTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.UMLMessageChange <em>UML Message Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.UMLMessageChange
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLMessageChange()
		 * @generated
		 */
		EClass UML_MESSAGE_CHANGE = eINSTANCE.getUMLMessageChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLMessageChangeLeftTargetImpl <em>UML Message Change Left Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLMessageChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLMessageChangeLeftTarget()
		 * @generated
		 */
		EClass UML_MESSAGE_CHANGE_LEFT_TARGET = eINSTANCE.getUMLMessageChangeLeftTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLMessageChangeRightTargetImpl <em>UML Message Change Right Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLMessageChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLMessageChangeRightTarget()
		 * @generated
		 */
		EClass UML_MESSAGE_CHANGE_RIGHT_TARGET = eINSTANCE.getUMLMessageChangeRightTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange <em>UML Stereotype Property Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypePropertyChange()
		 * @generated
		 */
		EClass UML_STEREOTYPE_PROPERTY_CHANGE = eINSTANCE.getUMLStereotypePropertyChange();

		/**
		 * The meta object literal for the '<em><b>Stereotype</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UML_STEREOTYPE_PROPERTY_CHANGE__STEREOTYPE = eINSTANCE.getUMLStereotypePropertyChange_Stereotype();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeAttributeChangeLeftTargetImpl <em>UML Stereotype Attribute Change Left Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeAttributeChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeAttributeChangeLeftTarget()
		 * @generated
		 */
		EClass UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET = eINSTANCE.getUMLStereotypeAttributeChangeLeftTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeAttributeChangeRightTargetImpl <em>UML Stereotype Attribute Change Right Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeAttributeChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeAttributeChangeRightTarget()
		 * @generated
		 */
		EClass UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET = eINSTANCE.getUMLStereotypeAttributeChangeRightTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeUpdateAttributeImpl <em>UML Stereotype Update Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeUpdateAttributeImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeUpdateAttribute()
		 * @generated
		 */
		EClass UML_STEREOTYPE_UPDATE_ATTRIBUTE = eINSTANCE.getUMLStereotypeUpdateAttribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationChange <em>UML Stereotype Application Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationChange
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeApplicationChange()
		 * @generated
		 */
		EClass UML_STEREOTYPE_APPLICATION_CHANGE = eINSTANCE.getUMLStereotypeApplicationChange();

		/**
		 * The meta object literal for the '<em><b>Stereotype</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UML_STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE = eINSTANCE.getUMLStereotypeApplicationChange_Stereotype();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationAdditionImpl <em>UML Stereotype Application Addition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationAdditionImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeApplicationAddition()
		 * @generated
		 */
		EClass UML_STEREOTYPE_APPLICATION_ADDITION = eINSTANCE.getUMLStereotypeApplicationAddition();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationRemovalImpl <em>UML Stereotype Application Removal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationRemovalImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeApplicationRemoval()
		 * @generated
		 */
		EClass UML_STEREOTYPE_APPLICATION_REMOVAL = eINSTANCE.getUMLStereotypeApplicationRemoval();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeReferenceChangeLeftTargetImpl <em>UML Stereotype Reference Change Left Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeReferenceChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeReferenceChangeLeftTarget()
		 * @generated
		 */
		EClass UML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET = eINSTANCE.getUMLStereotypeReferenceChangeLeftTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeReferenceChangeRightTargetImpl <em>UML Stereotype Reference Change Right Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeReferenceChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeReferenceChangeRightTarget()
		 * @generated
		 */
		EClass UML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET = eINSTANCE.getUMLStereotypeReferenceChangeRightTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeUpdateReferenceImpl <em>UML Stereotype Update Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeUpdateReferenceImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeUpdateReference()
		 * @generated
		 */
		EClass UML_STEREOTYPE_UPDATE_REFERENCE = eINSTANCE.getUMLStereotypeUpdateReference();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeReferenceOrderChangeImpl <em>UML Stereotype Reference Order Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeReferenceOrderChangeImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeReferenceOrderChange()
		 * @generated
		 */
		EClass UML_STEREOTYPE_REFERENCE_ORDER_CHANGE = eINSTANCE.getUMLStereotypeReferenceOrderChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.UMLProfileApplicationChange <em>UML Profile Application Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.UMLProfileApplicationChange
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLProfileApplicationChange()
		 * @generated
		 */
		EClass UML_PROFILE_APPLICATION_CHANGE = eINSTANCE.getUMLProfileApplicationChange();

		/**
		 * The meta object literal for the '<em><b>Profile</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UML_PROFILE_APPLICATION_CHANGE__PROFILE = eINSTANCE.getUMLProfileApplicationChange_Profile();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLProfileApplicationAdditionImpl <em>UML Profile Application Addition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLProfileApplicationAdditionImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLProfileApplicationAddition()
		 * @generated
		 */
		EClass UML_PROFILE_APPLICATION_ADDITION = eINSTANCE.getUMLProfileApplicationAddition();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLProfileApplicationRemovalImpl <em>UML Profile Application Removal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLProfileApplicationRemovalImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLProfileApplicationRemoval()
		 * @generated
		 */
		EClass UML_PROFILE_APPLICATION_REMOVAL = eINSTANCE.getUMLProfileApplicationRemoval();

	}

} //UML2DiffPackage
