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
package org.eclipse.emf.compare.uml2diff;

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
 * @see org.eclipse.emf.compare.uml2diff.Uml2diffFactory
 * @model kind="package"
 * @generated
 */
public interface Uml2diffPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "uml2diff"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare/diff/uml2/2.0"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "uml2diff"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Uml2diffPackage eINSTANCE = org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLAssociationChangeImpl <em>UML Association Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLAssociationChangeImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLAssociationChange()
	 * @generated
	 */
	int UML_ASSOCIATION_CHANGE = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLGeneralizationSetChangeImpl <em>UML Generalization Set Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLGeneralizationSetChangeImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLGeneralizationSetChange()
	 * @generated
	 */
	int UML_GENERALIZATION_SET_CHANGE = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLDependencyChangeImpl <em>UML Dependency Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLDependencyChangeImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLDependencyChange()
	 * @generated
	 */
	int UML_DEPENDENCY_CHANGE = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLExtendChangeImpl <em>UML Extend Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLExtendChangeImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLExtendChange()
	 * @generated
	 */
	int UML_EXTEND_CHANGE = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLExecutionSpecificationChangeImpl <em>UML Execution Specification Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLExecutionSpecificationChangeImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLExecutionSpecificationChange()
	 * @generated
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE = 6;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLDestructionEventChangeImpl <em>UML Destruction Event Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLDestructionEventChangeImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLDestructionEventChange()
	 * @generated
	 */
	int UML_DESTRUCTION_EVENT_CHANGE = 7;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLIntervalConstraintChangeImpl <em>UML Interval Constraint Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLIntervalConstraintChangeImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLIntervalConstraintChange()
	 * @generated
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE = 8;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLMessageChangeImpl <em>UML Message Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLMessageChangeImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLMessageChange()
	 * @generated
	 */
	int UML_MESSAGE_CHANGE = 9;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypePropertyChangeImpl <em>UML Stereotype Property Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypePropertyChangeImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLStereotypePropertyChange()
	 * @generated
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE = 10;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationChangeImpl <em>UML Stereotype Application Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationChangeImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLStereotypeApplicationChange()
	 * @generated
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE = 11;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeReferenceChangeImpl <em>UML Stereotype Reference Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeReferenceChangeImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLStereotypeReferenceChange()
	 * @generated
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE = 12;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLProfileApplicationChangeImpl <em>UML Profile Application Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLProfileApplicationChangeImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLProfileApplicationChange()
	 * @generated
	 */
	int UML_PROFILE_APPLICATION_CHANGE = 13;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLExtensionImpl <em>UML Extension</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLExtensionImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLExtension()
	 * @generated
	 */
	int UML_EXTENSION = 14;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTENSION__MATCH = ComparePackage.DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTENSION__REQUIRES = ComparePackage.DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTENSION__REQUIRED_BY = ComparePackage.DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTENSION__REFINES = ComparePackage.DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTENSION__REFINED_BY = ComparePackage.DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTENSION__KIND = ComparePackage.DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTENSION__SOURCE = ComparePackage.DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTENSION__STATE = ComparePackage.DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTENSION__EQUIVALENCE = ComparePackage.DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTENSION__CONFLICT = ComparePackage.DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTENSION__DISCRIMINANT = ComparePackage.DIFF_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>UML Extension</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTENSION_FEATURE_COUNT = ComparePackage.DIFF_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__MATCH = UML_EXTENSION__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__REQUIRES = UML_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__REQUIRED_BY = UML_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__REFINES = UML_EXTENSION__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__REFINED_BY = UML_EXTENSION__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__KIND = UML_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__SOURCE = UML_EXTENSION__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__STATE = UML_EXTENSION__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__EQUIVALENCE = UML_EXTENSION__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__CONFLICT = UML_EXTENSION__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__DISCRIMINANT = UML_EXTENSION__DISCRIMINANT;

	/**
	 * The feature id for the '<em><b>Association</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE__ASSOCIATION = UML_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>UML Association Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_ASSOCIATION_CHANGE_FEATURE_COUNT = UML_EXTENSION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__MATCH = UML_EXTENSION__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__REQUIRES = UML_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__REQUIRED_BY = UML_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__REFINES = UML_EXTENSION__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__REFINED_BY = UML_EXTENSION__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__KIND = UML_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__SOURCE = UML_EXTENSION__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__STATE = UML_EXTENSION__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__EQUIVALENCE = UML_EXTENSION__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__CONFLICT = UML_EXTENSION__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__DISCRIMINANT = UML_EXTENSION__DISCRIMINANT;

	/**
	 * The feature id for the '<em><b>Dependency</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE__DEPENDENCY = UML_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>UML Dependency Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DEPENDENCY_CHANGE_FEATURE_COUNT = UML_EXTENSION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLInterfaceRealizationChangeImpl <em>UML Interface Realization Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLInterfaceRealizationChangeImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLInterfaceRealizationChange()
	 * @generated
	 */
	int UML_INTERFACE_REALIZATION_CHANGE = 2;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERFACE_REALIZATION_CHANGE__MATCH = UML_EXTENSION__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERFACE_REALIZATION_CHANGE__REQUIRES = UML_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERFACE_REALIZATION_CHANGE__REQUIRED_BY = UML_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERFACE_REALIZATION_CHANGE__REFINES = UML_EXTENSION__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERFACE_REALIZATION_CHANGE__REFINED_BY = UML_EXTENSION__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERFACE_REALIZATION_CHANGE__KIND = UML_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERFACE_REALIZATION_CHANGE__SOURCE = UML_EXTENSION__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERFACE_REALIZATION_CHANGE__STATE = UML_EXTENSION__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERFACE_REALIZATION_CHANGE__EQUIVALENCE = UML_EXTENSION__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERFACE_REALIZATION_CHANGE__CONFLICT = UML_EXTENSION__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERFACE_REALIZATION_CHANGE__DISCRIMINANT = UML_EXTENSION__DISCRIMINANT;

	/**
	 * The number of structural features of the '<em>UML Interface Realization Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERFACE_REALIZATION_CHANGE_FEATURE_COUNT = UML_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLSubstitutionChangeImpl <em>UML Substitution Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2diff.impl.UMLSubstitutionChangeImpl
	 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLSubstitutionChange()
	 * @generated
	 */
	int UML_SUBSTITUTION_CHANGE = 3;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_SUBSTITUTION_CHANGE__MATCH = UML_EXTENSION__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_SUBSTITUTION_CHANGE__REQUIRES = UML_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_SUBSTITUTION_CHANGE__REQUIRED_BY = UML_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_SUBSTITUTION_CHANGE__REFINES = UML_EXTENSION__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_SUBSTITUTION_CHANGE__REFINED_BY = UML_EXTENSION__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_SUBSTITUTION_CHANGE__KIND = UML_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_SUBSTITUTION_CHANGE__SOURCE = UML_EXTENSION__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_SUBSTITUTION_CHANGE__STATE = UML_EXTENSION__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_SUBSTITUTION_CHANGE__EQUIVALENCE = UML_EXTENSION__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_SUBSTITUTION_CHANGE__CONFLICT = UML_EXTENSION__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_SUBSTITUTION_CHANGE__DISCRIMINANT = UML_EXTENSION__DISCRIMINANT;

	/**
	 * The number of structural features of the '<em>UML Substitution Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_SUBSTITUTION_CHANGE_FEATURE_COUNT = UML_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__MATCH = UML_EXTENSION__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__REQUIRES = UML_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__REQUIRED_BY = UML_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__REFINES = UML_EXTENSION__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__REFINED_BY = UML_EXTENSION__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__KIND = UML_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__SOURCE = UML_EXTENSION__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__STATE = UML_EXTENSION__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__EQUIVALENCE = UML_EXTENSION__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__CONFLICT = UML_EXTENSION__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__DISCRIMINANT = UML_EXTENSION__DISCRIMINANT;

	/**
	 * The feature id for the '<em><b>Extend</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE__EXTEND = UML_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>UML Extend Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXTEND_CHANGE_FEATURE_COUNT = UML_EXTENSION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__MATCH = UML_EXTENSION__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__REQUIRES = UML_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__REQUIRED_BY = UML_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__REFINES = UML_EXTENSION__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__REFINED_BY = UML_EXTENSION__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__KIND = UML_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__SOURCE = UML_EXTENSION__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__STATE = UML_EXTENSION__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__EQUIVALENCE = UML_EXTENSION__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__CONFLICT = UML_EXTENSION__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__DISCRIMINANT = UML_EXTENSION__DISCRIMINANT;

	/**
	 * The feature id for the '<em><b>Generalization Set</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE__GENERALIZATION_SET = UML_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>UML Generalization Set Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_GENERALIZATION_SET_CHANGE_FEATURE_COUNT = UML_EXTENSION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__MATCH = UML_EXTENSION__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__REQUIRES = UML_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__REQUIRED_BY = UML_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__REFINES = UML_EXTENSION__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__REFINED_BY = UML_EXTENSION__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__KIND = UML_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__SOURCE = UML_EXTENSION__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__STATE = UML_EXTENSION__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__EQUIVALENCE = UML_EXTENSION__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__CONFLICT = UML_EXTENSION__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE__DISCRIMINANT = UML_EXTENSION__DISCRIMINANT;

	/**
	 * The number of structural features of the '<em>UML Execution Specification Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_EXECUTION_SPECIFICATION_CHANGE_FEATURE_COUNT = UML_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__MATCH = UML_EXTENSION__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__REQUIRES = UML_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__REQUIRED_BY = UML_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__REFINES = UML_EXTENSION__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__REFINED_BY = UML_EXTENSION__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__KIND = UML_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__SOURCE = UML_EXTENSION__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__STATE = UML_EXTENSION__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__EQUIVALENCE = UML_EXTENSION__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__CONFLICT = UML_EXTENSION__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE__DISCRIMINANT = UML_EXTENSION__DISCRIMINANT;

	/**
	 * The number of structural features of the '<em>UML Destruction Event Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DESTRUCTION_EVENT_CHANGE_FEATURE_COUNT = UML_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__MATCH = UML_EXTENSION__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__REQUIRES = UML_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__REQUIRED_BY = UML_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__REFINES = UML_EXTENSION__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__REFINED_BY = UML_EXTENSION__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__KIND = UML_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__SOURCE = UML_EXTENSION__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__STATE = UML_EXTENSION__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__EQUIVALENCE = UML_EXTENSION__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__CONFLICT = UML_EXTENSION__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE__DISCRIMINANT = UML_EXTENSION__DISCRIMINANT;

	/**
	 * The number of structural features of the '<em>UML Interval Constraint Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_INTERVAL_CONSTRAINT_CHANGE_FEATURE_COUNT = UML_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__MATCH = UML_EXTENSION__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__REQUIRES = UML_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__REQUIRED_BY = UML_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__REFINES = UML_EXTENSION__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__REFINED_BY = UML_EXTENSION__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__KIND = UML_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__SOURCE = UML_EXTENSION__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__STATE = UML_EXTENSION__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__EQUIVALENCE = UML_EXTENSION__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__CONFLICT = UML_EXTENSION__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE__DISCRIMINANT = UML_EXTENSION__DISCRIMINANT;

	/**
	 * The number of structural features of the '<em>UML Message Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_MESSAGE_CHANGE_FEATURE_COUNT = UML_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__MATCH = UML_EXTENSION__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__REQUIRES = UML_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__REQUIRED_BY = UML_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__REFINES = UML_EXTENSION__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__REFINED_BY = UML_EXTENSION__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__KIND = UML_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__SOURCE = UML_EXTENSION__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__STATE = UML_EXTENSION__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__EQUIVALENCE = UML_EXTENSION__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__CONFLICT = UML_EXTENSION__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__DISCRIMINANT = UML_EXTENSION__DISCRIMINANT;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE__STEREOTYPE = UML_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>UML Stereotype Property Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_PROPERTY_CHANGE_FEATURE_COUNT = UML_EXTENSION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__MATCH = ComparePackage.REFERENCE_CHANGE__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__REQUIRES = ComparePackage.REFERENCE_CHANGE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__REQUIRED_BY = ComparePackage.REFERENCE_CHANGE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__REFINES = ComparePackage.REFERENCE_CHANGE__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__REFINED_BY = ComparePackage.REFERENCE_CHANGE__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__KIND = ComparePackage.REFERENCE_CHANGE__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__SOURCE = ComparePackage.REFERENCE_CHANGE__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__STATE = ComparePackage.REFERENCE_CHANGE__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__EQUIVALENCE = ComparePackage.REFERENCE_CHANGE__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__CONFLICT = ComparePackage.REFERENCE_CHANGE__CONFLICT;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__REFERENCE = ComparePackage.REFERENCE_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__VALUE = ComparePackage.REFERENCE_CHANGE__VALUE;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__DISCRIMINANT = ComparePackage.REFERENCE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE = ComparePackage.REFERENCE_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Stereotype Application Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_APPLICATION_CHANGE_FEATURE_COUNT = ComparePackage.REFERENCE_CHANGE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE__MATCH = UML_EXTENSION__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE__REQUIRES = UML_EXTENSION__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE__REQUIRED_BY = UML_EXTENSION__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE__REFINES = UML_EXTENSION__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE__REFINED_BY = UML_EXTENSION__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE__KIND = UML_EXTENSION__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE__SOURCE = UML_EXTENSION__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE__STATE = UML_EXTENSION__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE__EQUIVALENCE = UML_EXTENSION__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE__CONFLICT = UML_EXTENSION__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE__DISCRIMINANT = UML_EXTENSION__DISCRIMINANT;

	/**
	 * The number of structural features of the '<em>UML Stereotype Reference Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_STEREOTYPE_REFERENCE_CHANGE_FEATURE_COUNT = UML_EXTENSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__MATCH = ComparePackage.REFERENCE_CHANGE__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__REQUIRES = ComparePackage.REFERENCE_CHANGE__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__REQUIRED_BY = ComparePackage.REFERENCE_CHANGE__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__REFINES = ComparePackage.REFERENCE_CHANGE__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__REFINED_BY = ComparePackage.REFERENCE_CHANGE__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__KIND = ComparePackage.REFERENCE_CHANGE__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__SOURCE = ComparePackage.REFERENCE_CHANGE__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__STATE = ComparePackage.REFERENCE_CHANGE__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__EQUIVALENCE = ComparePackage.REFERENCE_CHANGE__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__CONFLICT = ComparePackage.REFERENCE_CHANGE__CONFLICT;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__REFERENCE = ComparePackage.REFERENCE_CHANGE__REFERENCE;

	/**
	 * The feature id for the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__VALUE = ComparePackage.REFERENCE_CHANGE__VALUE;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__DISCRIMINANT = ComparePackage.REFERENCE_CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Profile</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE__PROFILE = ComparePackage.REFERENCE_CHANGE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Profile Application Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_PROFILE_APPLICATION_CHANGE_FEATURE_COUNT = ComparePackage.REFERENCE_CHANGE_FEATURE_COUNT + 2;


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
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.uml2diff.UMLAssociationChange#getAssociation <em>Association</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Association</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLAssociationChange#getAssociation()
	 * @see #getUMLAssociationChange()
	 * @generated
	 */
	EReference getUMLAssociationChange_Association();

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
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChange#getGeneralizationSet <em>Generalization Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Generalization Set</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChange#getGeneralizationSet()
	 * @see #getUMLGeneralizationSetChange()
	 * @generated
	 */
	EReference getUMLGeneralizationSetChange_GeneralizationSet();

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
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.uml2diff.UMLDependencyChange#getDependency <em>Dependency</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Dependency</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDependencyChange#getDependency()
	 * @see #getUMLDependencyChange()
	 * @generated
	 */
	EReference getUMLDependencyChange_Dependency();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLInterfaceRealizationChange <em>UML Interface Realization Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Interface Realization Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLInterfaceRealizationChange
	 * @generated
	 */
	EClass getUMLInterfaceRealizationChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLSubstitutionChange <em>UML Substitution Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Substitution Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLSubstitutionChange
	 * @generated
	 */
	EClass getUMLSubstitutionChange();

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
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.uml2diff.UMLExtendChange#getExtend <em>Extend</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Extend</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLExtendChange#getExtend()
	 * @see #getUMLExtendChange()
	 * @generated
	 */
	EReference getUMLExtendChange_Extend();

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
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLDestructionEventChange <em>UML Destruction Event Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Destruction Event Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLDestructionEventChange
	 * @generated
	 */
	EClass getUMLDestructionEventChange();

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
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLMessageChange <em>UML Message Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Message Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLMessageChange
	 * @generated
	 */
	EClass getUMLMessageChange();

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
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChange <em>UML Stereotype Reference Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Stereotype Reference Change</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChange
	 * @generated
	 */
	EClass getUMLStereotypeReferenceChange();

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
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2diff.UMLExtension <em>UML Extension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Extension</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLExtension
	 * @generated
	 */
	EClass getUMLExtension();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.uml2diff.UMLExtension#getDiscriminant <em>Discriminant</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Discriminant</em>'.
	 * @see org.eclipse.emf.compare.uml2diff.UMLExtension#getDiscriminant()
	 * @see #getUMLExtension()
	 * @generated
	 */
	EReference getUMLExtension_Discriminant();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	Uml2diffFactory getUml2diffFactory();

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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLAssociationChangeImpl <em>UML Association Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLAssociationChangeImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLAssociationChange()
		 * @generated
		 */
		EClass UML_ASSOCIATION_CHANGE = eINSTANCE.getUMLAssociationChange();

		/**
		 * The meta object literal for the '<em><b>Association</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UML_ASSOCIATION_CHANGE__ASSOCIATION = eINSTANCE.getUMLAssociationChange_Association();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLGeneralizationSetChangeImpl <em>UML Generalization Set Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLGeneralizationSetChangeImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLGeneralizationSetChange()
		 * @generated
		 */
		EClass UML_GENERALIZATION_SET_CHANGE = eINSTANCE.getUMLGeneralizationSetChange();

		/**
		 * The meta object literal for the '<em><b>Generalization Set</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UML_GENERALIZATION_SET_CHANGE__GENERALIZATION_SET = eINSTANCE.getUMLGeneralizationSetChange_GeneralizationSet();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLDependencyChangeImpl <em>UML Dependency Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLDependencyChangeImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLDependencyChange()
		 * @generated
		 */
		EClass UML_DEPENDENCY_CHANGE = eINSTANCE.getUMLDependencyChange();

		/**
		 * The meta object literal for the '<em><b>Dependency</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UML_DEPENDENCY_CHANGE__DEPENDENCY = eINSTANCE.getUMLDependencyChange_Dependency();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLInterfaceRealizationChangeImpl <em>UML Interface Realization Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLInterfaceRealizationChangeImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLInterfaceRealizationChange()
		 * @generated
		 */
		EClass UML_INTERFACE_REALIZATION_CHANGE = eINSTANCE.getUMLInterfaceRealizationChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLSubstitutionChangeImpl <em>UML Substitution Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLSubstitutionChangeImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLSubstitutionChange()
		 * @generated
		 */
		EClass UML_SUBSTITUTION_CHANGE = eINSTANCE.getUMLSubstitutionChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLExtendChangeImpl <em>UML Extend Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLExtendChangeImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLExtendChange()
		 * @generated
		 */
		EClass UML_EXTEND_CHANGE = eINSTANCE.getUMLExtendChange();

		/**
		 * The meta object literal for the '<em><b>Extend</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UML_EXTEND_CHANGE__EXTEND = eINSTANCE.getUMLExtendChange_Extend();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLExecutionSpecificationChangeImpl <em>UML Execution Specification Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLExecutionSpecificationChangeImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLExecutionSpecificationChange()
		 * @generated
		 */
		EClass UML_EXECUTION_SPECIFICATION_CHANGE = eINSTANCE.getUMLExecutionSpecificationChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLDestructionEventChangeImpl <em>UML Destruction Event Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLDestructionEventChangeImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLDestructionEventChange()
		 * @generated
		 */
		EClass UML_DESTRUCTION_EVENT_CHANGE = eINSTANCE.getUMLDestructionEventChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLIntervalConstraintChangeImpl <em>UML Interval Constraint Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLIntervalConstraintChangeImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLIntervalConstraintChange()
		 * @generated
		 */
		EClass UML_INTERVAL_CONSTRAINT_CHANGE = eINSTANCE.getUMLIntervalConstraintChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLMessageChangeImpl <em>UML Message Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLMessageChangeImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLMessageChange()
		 * @generated
		 */
		EClass UML_MESSAGE_CHANGE = eINSTANCE.getUMLMessageChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypePropertyChangeImpl <em>UML Stereotype Property Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypePropertyChangeImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLStereotypePropertyChange()
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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationChangeImpl <em>UML Stereotype Application Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeApplicationChangeImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLStereotypeApplicationChange()
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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeReferenceChangeImpl <em>UML Stereotype Reference Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLStereotypeReferenceChangeImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLStereotypeReferenceChange()
		 * @generated
		 */
		EClass UML_STEREOTYPE_REFERENCE_CHANGE = eINSTANCE.getUMLStereotypeReferenceChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLProfileApplicationChangeImpl <em>UML Profile Application Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLProfileApplicationChangeImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLProfileApplicationChange()
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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2diff.impl.UMLExtensionImpl <em>UML Extension</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2diff.impl.UMLExtensionImpl
		 * @see org.eclipse.emf.compare.uml2diff.impl.Uml2diffPackageImpl#getUMLExtension()
		 * @generated
		 */
		EClass UML_EXTENSION = eINSTANCE.getUMLExtension();

		/**
		 * The meta object literal for the '<em><b>Discriminant</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UML_EXTENSION__DISCRIMINANT = eINSTANCE.getUMLExtension_Discriminant();

	}

} //Uml2diffPackage
