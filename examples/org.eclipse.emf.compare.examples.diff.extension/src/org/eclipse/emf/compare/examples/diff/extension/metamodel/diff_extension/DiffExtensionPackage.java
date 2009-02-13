/**
 * 
 *  Copyright (c) 2006, 2009 Obeo.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *      Obeo - initial API and implementation
 * 
 *
 * $Id: DiffExtensionPackage.java,v 1.7 2009/02/13 11:33:07 lgoubet Exp $
 */
package org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension;

import org.eclipse.emf.compare.diff.metamodel.DiffPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.DiffExtensionFactory
 * @model kind="package"
 * @generated
 */
public interface DiffExtensionPackage extends EPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "\n Copyright (c) 2006, 2009 Obeo.\n All rights reserved. This program and the accompanying materials\n are made available under the terms of the Eclipse Public License v1.0\n which accompanies this distribution, and is available at\n http://www.eclipse.org/legal/epl-v10.html\n \n Contributors:\n     Obeo - initial API and implementation\n";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	DiffExtensionPackage eINSTANCE = org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.DiffExtensionPackageImpl.init();

	/**
	 * The package name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "diff_extension";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "diff_extension";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare/sample/diff_extension/1.0";

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.UMLAssociationDiffImpl <em>UML Association Diff</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.UMLAssociationDiffImpl
	 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.DiffExtensionPackageImpl#getUMLAssociationDiff()
	 * @generated
	 */
	int UML_ASSOCIATION_DIFF = 1;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_DIFF__HIDE_ELEMENTS = DiffPackage.ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_DIFF__IS_COLLAPSED = DiffPackage.ABSTRACT_DIFF_EXTENSION__IS_COLLAPSED;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_DIFF__PROPERTIES = DiffPackage.ABSTRACT_DIFF_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Navigable</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_DIFF__IS_NAVIGABLE = DiffPackage.ABSTRACT_DIFF_EXTENSION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Container Package</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_DIFF__CONTAINER_PACKAGE = DiffPackage.ABSTRACT_DIFF_EXTENSION_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>UML Association Diff</em>' class.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_DIFF_FEATURE_COUNT = DiffPackage.ABSTRACT_DIFF_EXTENSION_FEATURE_COUNT + 3;
	
	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.AddUMLAssociationImpl <em>Add UML Association</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.AddUMLAssociationImpl
	 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.DiffExtensionPackageImpl#getAddUMLAssociation()
	 * @generated
	 */
	int ADD_UML_ASSOCIATION = 0;

	/**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_UML_ASSOCIATION__HIDE_ELEMENTS = UML_ASSOCIATION_DIFF__HIDE_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_UML_ASSOCIATION__IS_COLLAPSED = UML_ASSOCIATION_DIFF__IS_COLLAPSED;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_UML_ASSOCIATION__PROPERTIES = UML_ASSOCIATION_DIFF__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Is Navigable</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_UML_ASSOCIATION__IS_NAVIGABLE = UML_ASSOCIATION_DIFF__IS_NAVIGABLE;

	/**
	 * The feature id for the '<em><b>Container Package</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_UML_ASSOCIATION__CONTAINER_PACKAGE = UML_ASSOCIATION_DIFF__CONTAINER_PACKAGE;

	/**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADD_UML_ASSOCIATION__SUB_DIFF_ELEMENTS = UML_ASSOCIATION_DIFF_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_UML_ASSOCIATION__IS_HIDDEN_BY = UML_ASSOCIATION_DIFF_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_UML_ASSOCIATION__CONFLICTING = UML_ASSOCIATION_DIFF_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_UML_ASSOCIATION__KIND = UML_ASSOCIATION_DIFF_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_UML_ASSOCIATION__REMOTE = UML_ASSOCIATION_DIFF_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Right Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_UML_ASSOCIATION__RIGHT_PARENT = UML_ASSOCIATION_DIFF_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_UML_ASSOCIATION__LEFT_ELEMENT = UML_ASSOCIATION_DIFF_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Add UML Association</em>' class.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_UML_ASSOCIATION_FEATURE_COUNT = UML_ASSOCIATION_DIFF_FEATURE_COUNT + 7;

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.AddUMLAssociation <em>Add UML Association</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Add UML Association</em>'.
	 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.AddUMLAssociation
	 * @generated
	 */
	EClass getAddUMLAssociation();

	/**
	 * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DiffExtensionFactory getDiffExtensionFactory();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.UMLAssociationDiff <em>UML Association Diff</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Association Diff</em>'.
	 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.UMLAssociationDiff
	 * @generated
	 */
	EClass getUMLAssociationDiff();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.UMLAssociationDiff#getContainerPackage <em>Container Package</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Container Package</em>'.
	 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.UMLAssociationDiff#getContainerPackage()
	 * @see #getUMLAssociationDiff()
	 * @generated
	 */
	EReference getUMLAssociationDiff_ContainerPackage();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.UMLAssociationDiff#isIsNavigable <em>Is Navigable</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Navigable</em>'.
	 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.UMLAssociationDiff#isIsNavigable()
	 * @see #getUMLAssociationDiff()
	 * @generated
	 */
	EAttribute getUMLAssociationDiff_IsNavigable();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.UMLAssociationDiff#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Properties</em>'.
	 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.UMLAssociationDiff#getProperties()
	 * @see #getUMLAssociationDiff()
	 * @generated
	 */
	EReference getUMLAssociationDiff_Properties();

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
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.AddUMLAssociationImpl <em>Add UML Association</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.AddUMLAssociationImpl
		 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.DiffExtensionPackageImpl#getAddUMLAssociation()
		 * @generated
		 */
		EClass ADD_UML_ASSOCIATION = eINSTANCE.getAddUMLAssociation();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.UMLAssociationDiffImpl <em>UML Association Diff</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.UMLAssociationDiffImpl
		 * @see org.eclipse.emf.compare.examples.diff.extension.metamodel.diff_extension.impl.DiffExtensionPackageImpl#getUMLAssociationDiff()
		 * @generated
		 */
		EClass UML_ASSOCIATION_DIFF = eINSTANCE.getUMLAssociationDiff();

		/**
		 * The meta object literal for the '<em><b>Container Package</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference UML_ASSOCIATION_DIFF__CONTAINER_PACKAGE = eINSTANCE.getUMLAssociationDiff_ContainerPackage();

		/**
		 * The meta object literal for the '<em><b>Is Navigable</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute UML_ASSOCIATION_DIFF__IS_NAVIGABLE = eINSTANCE.getUMLAssociationDiff_IsNavigable();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>' reference list feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference UML_ASSOCIATION_DIFF__PROPERTIES = eINSTANCE.getUMLAssociationDiff_Properties();

	}

} // DiffExtensionPackage
