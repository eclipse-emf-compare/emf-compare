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
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.UMLAbstractionChange <em>UML Abstraction Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.UMLAbstractionChange
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLAbstractionChange()
	 * @generated
	 */
	int UML_ABSTRACTION_CHANGE = 1;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE__SUB_DIFF_ELEMENTS = UML_DIFF_EXTENSION__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE__IS_HIDDEN_BY = UML_DIFF_EXTENSION__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE__CONFLICTING = UML_DIFF_EXTENSION__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE__KIND = UML_DIFF_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE__REMOTE = UML_DIFF_EXTENSION__REMOTE;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE__HIDE_ELEMENTS = UML_DIFF_EXTENSION__HIDE_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE__IS_COLLAPSED = UML_DIFF_EXTENSION__IS_COLLAPSED;

	/**
	 * The number of structural features of the '<em>UML Abstraction Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_FEATURE_COUNT = UML_DIFF_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLAbstractionChangeLeftTargetImpl <em>UML Abstraction Change Left Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLAbstractionChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLAbstractionChangeLeftTarget()
	 * @generated
	 */
	int UML_ABSTRACTION_CHANGE_LEFT_TARGET = 2;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_LEFT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_LEFT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_LEFT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_LEFT_TARGET__RIGHT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__RIGHT_PARENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_LEFT_TARGET__LEFT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_LEFT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_LEFT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Abstraction Change Left Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_LEFT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_LEFT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLAbstractionChangeRightTargetImpl <em>UML Abstraction Change Right Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLAbstractionChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLAbstractionChangeRightTarget()
	 * @generated
	 */
	int UML_ABSTRACTION_CHANGE_RIGHT_TARGET = 3;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_RIGHT_TARGET__CONFLICTING = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_RIGHT_TARGET__KIND = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_RIGHT_TARGET__REMOTE = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Left Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_RIGHT_TARGET__LEFT_PARENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__LEFT_PARENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_RIGHT_TARGET__IS_COLLAPSED = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Abstraction Change Right Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ABSTRACTION_CHANGE_RIGHT_TARGET_FEATURE_COUNT = DiffPackage.MODEL_ELEMENT_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.UMLAssociationChange <em>UML Association Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.UMLAssociationChange
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLAssociationChange()
	 * @generated
	 */
	int UML_ASSOCIATION_CHANGE = 4;

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
	int UML_ASSOCIATION_CHANGE_LEFT_TARGET = 5;

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
	int UML_ASSOCIATION_CHANGE_RIGHT_TARGET = 6;

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
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange <em>UML Stereotype Property Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypePropertyChange()
	 * @generated
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE = 7;

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
	 * The feature id for the '<em><b>Stereotype Applications</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__STEREOTYPE_APPLICATIONS = UML_DIFF_EXTENSION_FEATURE_COUNT + 0;

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
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET = 8;

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
	 * The feature id for the '<em><b>Stereotype Applications</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET__STEREOTYPE_APPLICATIONS = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 2;

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
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET = 9;

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
	 * The feature id for the '<em><b>Stereotype Applications</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_ATTRIBUTE_CHANGE_RIGHT_TARGET__STEREOTYPE_APPLICATIONS = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 2;

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
	int UML_STEREOTYPE_UPDATE_ATTRIBUTE = 10;

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
	 * The feature id for the '<em><b>Stereotype Applications</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_UPDATE_ATTRIBUTE__STEREOTYPE_APPLICATIONS = DiffPackage.UPDATE_ATTRIBUTE_FEATURE_COUNT + 2;

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
	int UML_STEREOTYPE_APPLICATION_CHANGE = 11;

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
	 * The number of structural features of the '<em>UML Stereotype Application Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE_FEATURE_COUNT = UML_DIFF_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationAdditionImpl <em>UML Stereotype Application Addition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationAdditionImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeApplicationAddition()
	 * @generated
	 */
	int UML_STEREOTYPE_APPLICATION_ADDITION = 12;

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
	 * The number of structural features of the '<em>UML Stereotype Application Addition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_ADDITION_FEATURE_COUNT = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationRemovalImpl <em>UML Stereotype Application Removal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationRemovalImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypeApplicationRemoval()
	 * @generated
	 */
	int UML_STEREOTYPE_APPLICATION_REMOVAL = 13;

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
	 * The number of structural features of the '<em>UML Stereotype Application Removal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_REMOVAL_FEATURE_COUNT = DiffPackage.UPDATE_MODEL_ELEMENT_FEATURE_COUNT + 2;


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
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLAbstractionChange <em>UML Abstraction Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Abstraction Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLAbstractionChange
	 * @generated
	 */
	EClass getUMLAbstractionChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLAbstractionChangeLeftTarget <em>UML Abstraction Change Left Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Abstraction Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLAbstractionChangeLeftTarget
	 * @generated
	 */
	EClass getUMLAbstractionChangeLeftTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLAbstractionChangeRightTarget <em>UML Abstraction Change Right Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Abstraction Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLAbstractionChangeRightTarget
	 * @generated
	 */
	EClass getUMLAbstractionChangeRightTarget();

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
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange <em>UML Stereotype Property Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Stereotype Property Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange
	 * @generated
	 */
	EClass getUMLStereotypePropertyChange();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange#getStereotypeApplications <em>Stereotype Applications</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Stereotype Applications</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange#getStereotypeApplications()
	 * @see #getUMLStereotypePropertyChange()
	 * @generated
	 */
	EReference getUMLStereotypePropertyChange_StereotypeApplications();

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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.UMLAbstractionChange <em>UML Abstraction Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.UMLAbstractionChange
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLAbstractionChange()
		 * @generated
		 */
		EClass UML_ABSTRACTION_CHANGE = eINSTANCE.getUMLAbstractionChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLAbstractionChangeLeftTargetImpl <em>UML Abstraction Change Left Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLAbstractionChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLAbstractionChangeLeftTarget()
		 * @generated
		 */
		EClass UML_ABSTRACTION_CHANGE_LEFT_TARGET = eINSTANCE.getUMLAbstractionChangeLeftTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLAbstractionChangeRightTargetImpl <em>UML Abstraction Change Right Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLAbstractionChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLAbstractionChangeRightTarget()
		 * @generated
		 */
		EClass UML_ABSTRACTION_CHANGE_RIGHT_TARGET = eINSTANCE.getUMLAbstractionChangeRightTarget();

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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange <em>UML Stereotype Property Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypePropertyChange
		 * @see org.eclipse.emf.compare.uml2diff.impl.UML2DiffPackageImpl#getUMLStereotypePropertyChange()
		 * @generated
		 */
		EClass UML_STEREOTYPE_PROPERTY_CHANGE = eINSTANCE.getUMLStereotypePropertyChange();

		/**
		 * The meta object literal for the '<em><b>Stereotype Applications</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UML_STEREOTYPE_PROPERTY_CHANGE__STEREOTYPE_APPLICATIONS = eINSTANCE.getUMLStereotypePropertyChange_StereotypeApplications();

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

	}

} //UML2DiffPackage
