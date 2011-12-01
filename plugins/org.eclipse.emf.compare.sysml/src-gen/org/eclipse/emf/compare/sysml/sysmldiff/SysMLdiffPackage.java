/**
 *  Copyright (c) 2011 Atos Origin.
 *  
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *  Atos Origin - Initial API and implementation
 * 
 */
package org.eclipse.emf.compare.sysml.sysmldiff;

import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.uml2diff.UML2DiffPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

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
 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLdiffFactory
 * @model kind="package"
 * @generated
 */
public interface SysMLdiffPackage extends EPackage {
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "sysmldiff"; //$NON-NLS-1$

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare/diff/SysML/1.0"; //$NON-NLS-1$

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "SysMLdiff"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	SysMLdiffPackage eINSTANCE = org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLDiffExtension
	 * <em>Sys ML Diff Extension</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLDiffExtension
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffPackageImpl#getSysMLDiffExtension()
	 * @generated
	 */
	int SYS_ML_DIFF_EXTENSION = 0;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_DIFF_EXTENSION__SUB_DIFF_ELEMENTS = DiffPackage.DIFF_ELEMENT__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_DIFF_EXTENSION__IS_HIDDEN_BY = DiffPackage.DIFF_ELEMENT__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_DIFF_EXTENSION__CONFLICTING = DiffPackage.DIFF_ELEMENT__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_DIFF_EXTENSION__KIND = DiffPackage.DIFF_ELEMENT__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_DIFF_EXTENSION__REMOTE = DiffPackage.DIFF_ELEMENT__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_DIFF_EXTENSION__REQUIRES = DiffPackage.DIFF_ELEMENT__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_DIFF_EXTENSION__REQUIRED_BY = DiffPackage.DIFF_ELEMENT__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_DIFF_EXTENSION__HIDE_ELEMENTS = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_DIFF_EXTENSION__IS_COLLAPSED = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Sys ML Diff Extension</em>' class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_DIFF_EXTENSION_FEATURE_COUNT = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeAttributeChange
	 * <em>Sys ML Stereotype Attribute Change</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeAttributeChange
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffPackageImpl#getSysMLStereotypeAttributeChange()
	 * @generated
	 */
	int SYS_ML_STEREOTYPE_ATTRIBUTE_CHANGE = 1;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_ATTRIBUTE_CHANGE__SUB_DIFF_ELEMENTS = UML2DiffPackage.UML_STEREOTYPE_PROPERTY_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_ATTRIBUTE_CHANGE__IS_HIDDEN_BY = UML2DiffPackage.UML_STEREOTYPE_PROPERTY_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_ATTRIBUTE_CHANGE__CONFLICTING = UML2DiffPackage.UML_STEREOTYPE_PROPERTY_CHANGE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_ATTRIBUTE_CHANGE__KIND = UML2DiffPackage.UML_STEREOTYPE_PROPERTY_CHANGE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_ATTRIBUTE_CHANGE__REMOTE = UML2DiffPackage.UML_STEREOTYPE_PROPERTY_CHANGE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_ATTRIBUTE_CHANGE__REQUIRES = UML2DiffPackage.UML_STEREOTYPE_PROPERTY_CHANGE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_ATTRIBUTE_CHANGE__REQUIRED_BY = UML2DiffPackage.UML_STEREOTYPE_PROPERTY_CHANGE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_ATTRIBUTE_CHANGE__HIDE_ELEMENTS = UML2DiffPackage.UML_STEREOTYPE_PROPERTY_CHANGE__HIDE_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_ATTRIBUTE_CHANGE__IS_COLLAPSED = UML2DiffPackage.UML_STEREOTYPE_PROPERTY_CHANGE__IS_COLLAPSED;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_ATTRIBUTE_CHANGE__STEREOTYPE = UML2DiffPackage.UML_STEREOTYPE_PROPERTY_CHANGE__STEREOTYPE;

	/**
	 * The number of structural features of the '<em>Sys ML Stereotype Attribute Change</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_ATTRIBUTE_CHANGE_FEATURE_COUNT = UML2DiffPackage.UML_STEREOTYPE_PROPERTY_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypePropertyChangeLeftTargetImpl
	 * <em>Sys ML Stereotype Property Change Left Target</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypePropertyChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffPackageImpl#getSysMLStereotypePropertyChangeLeftTarget()
	 * @generated
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET = 2;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET__CONFLICTING = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET__KIND = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET__REMOTE = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET__REQUIRES = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET__REQUIRED_BY = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET__ATTRIBUTE = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET__LEFT_ELEMENT = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET__RIGHT_ELEMENT = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET__LEFT_TARGET = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET__LEFT_TARGET;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET__HIDE_ELEMENTS = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET__IS_COLLAPSED = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET__STEREOTYPE = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Sys ML Stereotype Property Change Left Target</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET_FEATURE_COUNT = DiffPackage.ATTRIBUTE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypePropertyChangeRightTargetImpl
	 * <em>Sys ML Stereotype Property Change Right Target</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypePropertyChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffPackageImpl#getSysMLStereotypePropertyChangeRightTarget()
	 * @generated
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET = 3;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET__CONFLICTING = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET__KIND = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET__REMOTE = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET__REQUIRES = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET__REQUIRED_BY = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET__ATTRIBUTE = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET__LEFT_ELEMENT = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET__RIGHT_TARGET = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET__RIGHT_TARGET;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET__IS_COLLAPSED = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET__STEREOTYPE = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Sys ML Stereotype Property Change Right Target</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET_FEATURE_COUNT = DiffPackage.ATTRIBUTE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeReferenceChangeLeftTargetImpl
	 * <em>Sys ML Stereotype Reference Change Left Target</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeReferenceChangeLeftTargetImpl
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffPackageImpl#getSysMLStereotypeReferenceChangeLeftTarget()
	 * @generated
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET = 4;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__IS_HIDDEN_BY = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__CONFLICTING = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__KIND = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__REMOTE = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__REQUIRES = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__REQUIRED_BY = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__REFERENCE = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__RIGHT_ELEMENT = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__LEFT_ELEMENT = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__LEFT_TARGET = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__LEFT_TARGET;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__RIGHT_TARGET = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET__RIGHT_TARGET;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__HIDE_ELEMENTS = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__IS_COLLAPSED = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET__STEREOTYPE = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Sys ML Stereotype Reference Change Left Target</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET_FEATURE_COUNT = DiffPackage.REFERENCE_CHANGE_LEFT_TARGET_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeReferenceChangeRightTargetImpl
	 * <em>Sys ML Stereotype Reference Change Right Target</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeReferenceChangeRightTargetImpl
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffPackageImpl#getSysMLStereotypeReferenceChangeRightTarget()
	 * @generated
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET = 5;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__CONFLICTING = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__KIND = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__REMOTE = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__REQUIRES = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__REQUIRED_BY = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__REFERENCE = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__LEFT_ELEMENT = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_TARGET = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__RIGHT_TARGET;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__LEFT_TARGET = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET__LEFT_TARGET;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__HIDE_ELEMENTS = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__IS_COLLAPSED = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET__STEREOTYPE = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Sys ML Stereotype Reference Change Right Target</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET_FEATURE_COUNT = DiffPackage.REFERENCE_CHANGE_RIGHT_TARGET_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeReferenceOrderChangeImpl
	 * <em>Sys ML Stereotype Reference Order Change</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeReferenceOrderChangeImpl
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffPackageImpl#getSysMLStereotypeReferenceOrderChange()
	 * @generated
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE = 6;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE__SUB_DIFF_ELEMENTS = DiffPackage.REFERENCE_ORDER_CHANGE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE__IS_HIDDEN_BY = DiffPackage.REFERENCE_ORDER_CHANGE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE__CONFLICTING = DiffPackage.REFERENCE_ORDER_CHANGE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE__KIND = DiffPackage.REFERENCE_ORDER_CHANGE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE__REMOTE = DiffPackage.REFERENCE_ORDER_CHANGE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE__REQUIRES = DiffPackage.REFERENCE_ORDER_CHANGE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE__REQUIRED_BY = DiffPackage.REFERENCE_ORDER_CHANGE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE__REFERENCE = DiffPackage.REFERENCE_ORDER_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE__RIGHT_ELEMENT = DiffPackage.REFERENCE_ORDER_CHANGE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE__LEFT_ELEMENT = DiffPackage.REFERENCE_ORDER_CHANGE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE__LEFT_TARGET = DiffPackage.REFERENCE_ORDER_CHANGE__LEFT_TARGET;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE__RIGHT_TARGET = DiffPackage.REFERENCE_ORDER_CHANGE__RIGHT_TARGET;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE__HIDE_ELEMENTS = DiffPackage.REFERENCE_ORDER_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE__IS_COLLAPSED = DiffPackage.REFERENCE_ORDER_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE__STEREOTYPE = DiffPackage.REFERENCE_ORDER_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Sys ML Stereotype Reference Order Change</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE_FEATURE_COUNT = DiffPackage.REFERENCE_ORDER_CHANGE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeUpdateAttributeImpl
	 * <em>Sys ML Stereotype Update Attribute</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeUpdateAttributeImpl
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffPackageImpl#getSysMLStereotypeUpdateAttribute()
	 * @generated
	 */
	int SYS_ML_STEREOTYPE_UPDATE_ATTRIBUTE = 7;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_ATTRIBUTE__SUB_DIFF_ELEMENTS = DiffPackage.UPDATE_ATTRIBUTE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_ATTRIBUTE__IS_HIDDEN_BY = DiffPackage.UPDATE_ATTRIBUTE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_ATTRIBUTE__CONFLICTING = DiffPackage.UPDATE_ATTRIBUTE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_ATTRIBUTE__KIND = DiffPackage.UPDATE_ATTRIBUTE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_ATTRIBUTE__REMOTE = DiffPackage.UPDATE_ATTRIBUTE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_ATTRIBUTE__REQUIRES = DiffPackage.UPDATE_ATTRIBUTE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_ATTRIBUTE__REQUIRED_BY = DiffPackage.UPDATE_ATTRIBUTE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_ATTRIBUTE__ATTRIBUTE = DiffPackage.UPDATE_ATTRIBUTE__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_ATTRIBUTE__LEFT_ELEMENT = DiffPackage.UPDATE_ATTRIBUTE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_ATTRIBUTE__RIGHT_ELEMENT = DiffPackage.UPDATE_ATTRIBUTE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_ATTRIBUTE__HIDE_ELEMENTS = DiffPackage.UPDATE_ATTRIBUTE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_ATTRIBUTE__IS_COLLAPSED = DiffPackage.UPDATE_ATTRIBUTE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_ATTRIBUTE__STEREOTYPE = DiffPackage.UPDATE_ATTRIBUTE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Sys ML Stereotype Update Attribute</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_ATTRIBUTE_FEATURE_COUNT = DiffPackage.UPDATE_ATTRIBUTE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeUpdateReferenceImpl
	 * <em>Sys ML Stereotype Update Reference</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeUpdateReferenceImpl
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffPackageImpl#getSysMLStereotypeUpdateReference()
	 * @generated
	 */
	int SYS_ML_STEREOTYPE_UPDATE_REFERENCE = 8;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_REFERENCE__SUB_DIFF_ELEMENTS = DiffPackage.UPDATE_REFERENCE__SUB_DIFF_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_REFERENCE__IS_HIDDEN_BY = DiffPackage.UPDATE_REFERENCE__IS_HIDDEN_BY;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_REFERENCE__CONFLICTING = DiffPackage.UPDATE_REFERENCE__CONFLICTING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_REFERENCE__KIND = DiffPackage.UPDATE_REFERENCE__KIND;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_REFERENCE__REMOTE = DiffPackage.UPDATE_REFERENCE__REMOTE;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_REFERENCE__REQUIRES = DiffPackage.UPDATE_REFERENCE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_REFERENCE__REQUIRED_BY = DiffPackage.UPDATE_REFERENCE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_REFERENCE__REFERENCE = DiffPackage.UPDATE_REFERENCE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_REFERENCE__RIGHT_ELEMENT = DiffPackage.UPDATE_REFERENCE__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_REFERENCE__LEFT_ELEMENT = DiffPackage.UPDATE_REFERENCE__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Left Target</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_REFERENCE__LEFT_TARGET = DiffPackage.UPDATE_REFERENCE__LEFT_TARGET;

	/**
	 * The feature id for the '<em><b>Right Target</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_REFERENCE__RIGHT_TARGET = DiffPackage.UPDATE_REFERENCE__RIGHT_TARGET;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_REFERENCE__HIDE_ELEMENTS = DiffPackage.UPDATE_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_REFERENCE__IS_COLLAPSED = DiffPackage.UPDATE_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_REFERENCE__STEREOTYPE = DiffPackage.UPDATE_REFERENCE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Sys ML Stereotype Update Reference</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SYS_ML_STEREOTYPE_UPDATE_REFERENCE_FEATURE_COUNT = DiffPackage.UPDATE_REFERENCE_FEATURE_COUNT + 3;

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLDiffExtension
	 * <em>Sys ML Diff Extension</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Sys ML Diff Extension</em>'.
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLDiffExtension
	 * @generated
	 */
	EClass getSysMLDiffExtension();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeAttributeChange
	 * <em>Sys ML Stereotype Attribute Change</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Sys ML Stereotype Attribute Change</em>'.
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeAttributeChange
	 * @generated
	 */
	EClass getSysMLStereotypeAttributeChange();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypePropertyChangeLeftTarget
	 * <em>Sys ML Stereotype Property Change Left Target</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Sys ML Stereotype Property Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypePropertyChangeLeftTarget
	 * @generated
	 */
	EClass getSysMLStereotypePropertyChangeLeftTarget();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypePropertyChangeRightTarget
	 * <em>Sys ML Stereotype Property Change Right Target</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for class '<em>Sys ML Stereotype Property Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypePropertyChangeRightTarget
	 * @generated
	 */
	EClass getSysMLStereotypePropertyChangeRightTarget();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceChangeLeftTarget
	 * <em>Sys ML Stereotype Reference Change Left Target</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for class '<em>Sys ML Stereotype Reference Change Left Target</em>'.
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceChangeLeftTarget
	 * @generated
	 */
	EClass getSysMLStereotypeReferenceChangeLeftTarget();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceChangeRightTarget
	 * <em>Sys ML Stereotype Reference Change Right Target</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for class '<em>Sys ML Stereotype Reference Change Right Target</em>'.
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceChangeRightTarget
	 * @generated
	 */
	EClass getSysMLStereotypeReferenceChangeRightTarget();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceOrderChange
	 * <em>Sys ML Stereotype Reference Order Change</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Sys ML Stereotype Reference Order Change</em>'.
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeReferenceOrderChange
	 * @generated
	 */
	EClass getSysMLStereotypeReferenceOrderChange();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeUpdateAttribute
	 * <em>Sys ML Stereotype Update Attribute</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Sys ML Stereotype Update Attribute</em>'.
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeUpdateAttribute
	 * @generated
	 */
	EClass getSysMLStereotypeUpdateAttribute();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeUpdateReference
	 * <em>Sys ML Stereotype Update Reference</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Sys ML Stereotype Update Reference</em>'.
	 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeUpdateReference
	 * @generated
	 */
	EClass getSysMLStereotypeUpdateReference();

	/**
	 * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	SysMLdiffFactory getSysMLdiffFactory();

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
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLDiffExtension
		 * <em>Sys ML Diff Extension</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLDiffExtension
		 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffPackageImpl#getSysMLDiffExtension()
		 * @generated
		 */
		EClass SYS_ML_DIFF_EXTENSION = eINSTANCE.getSysMLDiffExtension();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeAttributeChange
		 * <em>Sys ML Stereotype Attribute Change</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypeAttributeChange
		 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffPackageImpl#getSysMLStereotypeAttributeChange()
		 * @generated
		 */
		EClass SYS_ML_STEREOTYPE_ATTRIBUTE_CHANGE = eINSTANCE.getSysMLStereotypeAttributeChange();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypePropertyChangeLeftTargetImpl
		 * <em>Sys ML Stereotype Property Change Left Target</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypePropertyChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffPackageImpl#getSysMLStereotypePropertyChangeLeftTarget()
		 * @generated
		 */
		EClass SYS_ML_STEREOTYPE_PROPERTY_CHANGE_LEFT_TARGET = eINSTANCE
				.getSysMLStereotypePropertyChangeLeftTarget();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypePropertyChangeRightTargetImpl
		 * <em>Sys ML Stereotype Property Change Right Target</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypePropertyChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffPackageImpl#getSysMLStereotypePropertyChangeRightTarget()
		 * @generated
		 */
		EClass SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET = eINSTANCE
				.getSysMLStereotypePropertyChangeRightTarget();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeReferenceChangeLeftTargetImpl
		 * <em>Sys ML Stereotype Reference Change Left Target</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeReferenceChangeLeftTargetImpl
		 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffPackageImpl#getSysMLStereotypeReferenceChangeLeftTarget()
		 * @generated
		 */
		EClass SYS_ML_STEREOTYPE_REFERENCE_CHANGE_LEFT_TARGET = eINSTANCE
				.getSysMLStereotypeReferenceChangeLeftTarget();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeReferenceChangeRightTargetImpl
		 * <em>Sys ML Stereotype Reference Change Right Target</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeReferenceChangeRightTargetImpl
		 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffPackageImpl#getSysMLStereotypeReferenceChangeRightTarget()
		 * @generated
		 */
		EClass SYS_ML_STEREOTYPE_REFERENCE_CHANGE_RIGHT_TARGET = eINSTANCE
				.getSysMLStereotypeReferenceChangeRightTarget();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeReferenceOrderChangeImpl
		 * <em>Sys ML Stereotype Reference Order Change</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeReferenceOrderChangeImpl
		 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffPackageImpl#getSysMLStereotypeReferenceOrderChange()
		 * @generated
		 */
		EClass SYS_ML_STEREOTYPE_REFERENCE_ORDER_CHANGE = eINSTANCE.getSysMLStereotypeReferenceOrderChange();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeUpdateAttributeImpl
		 * <em>Sys ML Stereotype Update Attribute</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeUpdateAttributeImpl
		 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffPackageImpl#getSysMLStereotypeUpdateAttribute()
		 * @generated
		 */
		EClass SYS_ML_STEREOTYPE_UPDATE_ATTRIBUTE = eINSTANCE.getSysMLStereotypeUpdateAttribute();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeUpdateReferenceImpl
		 * <em>Sys ML Stereotype Update Reference</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLStereotypeUpdateReferenceImpl
		 * @see org.eclipse.emf.compare.sysml.sysmldiff.impl.SysMLdiffPackageImpl#getSysMLStereotypeUpdateReference()
		 * @generated
		 */
		EClass SYS_ML_STEREOTYPE_UPDATE_REFERENCE = eINSTANCE.getSysMLStereotypeUpdateReference();

	}

} // SysMLdiffPackage
