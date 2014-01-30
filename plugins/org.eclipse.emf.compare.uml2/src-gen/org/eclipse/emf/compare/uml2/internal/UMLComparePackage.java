/**
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2.internal;

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
 * @see org.eclipse.emf.compare.uml2.internal.UMLCompareFactory
 * @model kind="package"
 * @generated
 */
public interface UMLComparePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "uml2"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare/uml2/2.0"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "umlcompare"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	UMLComparePackage eINSTANCE = org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2.internal.impl.UMLDiffImpl <em>UML Diff</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLDiffImpl
	 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getUMLDiff()
	 * @generated
	 */
	int UML_DIFF = 11;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF__MATCH = ComparePackage.DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF__REQUIRES = ComparePackage.DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF__REQUIRED_BY = ComparePackage.DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF__IMPLIES = ComparePackage.DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF__IMPLIED_BY = ComparePackage.DIFF__IMPLIED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF__REFINES = ComparePackage.DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF__REFINED_BY = ComparePackage.DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF__PRIME_REFINING = ComparePackage.DIFF__PRIME_REFINING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF__KIND = ComparePackage.DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF__SOURCE = ComparePackage.DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF__STATE = ComparePackage.DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF__EQUIVALENCE = ComparePackage.DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF__CONFLICT = ComparePackage.DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF__DISCRIMINANT = ComparePackage.DIFF_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>EReference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF__EREFERENCE = ComparePackage.DIFF_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>UML Diff</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UML_DIFF_FEATURE_COUNT = ComparePackage.DIFF_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2.internal.impl.AssociationChangeImpl <em>Association Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2.internal.impl.AssociationChangeImpl
	 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getAssociationChange()
	 * @generated
	 */
	int ASSOCIATION_CHANGE = 0;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CHANGE__MATCH = UML_DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CHANGE__REQUIRES = UML_DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CHANGE__REQUIRED_BY = UML_DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CHANGE__IMPLIES = UML_DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CHANGE__IMPLIED_BY = UML_DIFF__IMPLIED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CHANGE__REFINES = UML_DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CHANGE__REFINED_BY = UML_DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CHANGE__PRIME_REFINING = UML_DIFF__PRIME_REFINING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CHANGE__KIND = UML_DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CHANGE__SOURCE = UML_DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CHANGE__STATE = UML_DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CHANGE__EQUIVALENCE = UML_DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CHANGE__CONFLICT = UML_DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CHANGE__DISCRIMINANT = UML_DIFF__DISCRIMINANT;

	/**
	 * The feature id for the '<em><b>EReference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CHANGE__EREFERENCE = UML_DIFF__EREFERENCE;

	/**
	 * The number of structural features of the '<em>Association Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CHANGE_FEATURE_COUNT = UML_DIFF_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2.internal.impl.ExtendChangeImpl <em>Extend Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2.internal.impl.ExtendChangeImpl
	 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getExtendChange()
	 * @generated
	 */
	int EXTEND_CHANGE = 1;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTEND_CHANGE__MATCH = UML_DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTEND_CHANGE__REQUIRES = UML_DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTEND_CHANGE__REQUIRED_BY = UML_DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTEND_CHANGE__IMPLIES = UML_DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTEND_CHANGE__IMPLIED_BY = UML_DIFF__IMPLIED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTEND_CHANGE__REFINES = UML_DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTEND_CHANGE__REFINED_BY = UML_DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTEND_CHANGE__PRIME_REFINING = UML_DIFF__PRIME_REFINING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTEND_CHANGE__KIND = UML_DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTEND_CHANGE__SOURCE = UML_DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTEND_CHANGE__STATE = UML_DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTEND_CHANGE__EQUIVALENCE = UML_DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTEND_CHANGE__CONFLICT = UML_DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTEND_CHANGE__DISCRIMINANT = UML_DIFF__DISCRIMINANT;

	/**
	 * The feature id for the '<em><b>EReference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTEND_CHANGE__EREFERENCE = UML_DIFF__EREFERENCE;

	/**
	 * The number of structural features of the '<em>Extend Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTEND_CHANGE_FEATURE_COUNT = UML_DIFF_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2.internal.impl.GeneralizationSetChangeImpl <em>Generalization Set Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2.internal.impl.GeneralizationSetChangeImpl
	 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getGeneralizationSetChange()
	 * @generated
	 */
	int GENERALIZATION_SET_CHANGE = 2;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERALIZATION_SET_CHANGE__MATCH = UML_DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERALIZATION_SET_CHANGE__REQUIRES = UML_DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERALIZATION_SET_CHANGE__REQUIRED_BY = UML_DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERALIZATION_SET_CHANGE__IMPLIES = UML_DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERALIZATION_SET_CHANGE__IMPLIED_BY = UML_DIFF__IMPLIED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERALIZATION_SET_CHANGE__REFINES = UML_DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERALIZATION_SET_CHANGE__REFINED_BY = UML_DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERALIZATION_SET_CHANGE__PRIME_REFINING = UML_DIFF__PRIME_REFINING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERALIZATION_SET_CHANGE__KIND = UML_DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERALIZATION_SET_CHANGE__SOURCE = UML_DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERALIZATION_SET_CHANGE__STATE = UML_DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERALIZATION_SET_CHANGE__EQUIVALENCE = UML_DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERALIZATION_SET_CHANGE__CONFLICT = UML_DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERALIZATION_SET_CHANGE__DISCRIMINANT = UML_DIFF__DISCRIMINANT;

	/**
	 * The feature id for the '<em><b>EReference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERALIZATION_SET_CHANGE__EREFERENCE = UML_DIFF__EREFERENCE;

	/**
	 * The number of structural features of the '<em>Generalization Set Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERALIZATION_SET_CHANGE_FEATURE_COUNT = UML_DIFF_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2.internal.impl.ExecutionSpecificationChangeImpl <em>Execution Specification Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2.internal.impl.ExecutionSpecificationChangeImpl
	 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getExecutionSpecificationChange()
	 * @generated
	 */
	int EXECUTION_SPECIFICATION_CHANGE = 3;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_SPECIFICATION_CHANGE__MATCH = UML_DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_SPECIFICATION_CHANGE__REQUIRES = UML_DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_SPECIFICATION_CHANGE__REQUIRED_BY = UML_DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_SPECIFICATION_CHANGE__IMPLIES = UML_DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_SPECIFICATION_CHANGE__IMPLIED_BY = UML_DIFF__IMPLIED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_SPECIFICATION_CHANGE__REFINES = UML_DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_SPECIFICATION_CHANGE__REFINED_BY = UML_DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_SPECIFICATION_CHANGE__PRIME_REFINING = UML_DIFF__PRIME_REFINING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_SPECIFICATION_CHANGE__KIND = UML_DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_SPECIFICATION_CHANGE__SOURCE = UML_DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_SPECIFICATION_CHANGE__STATE = UML_DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_SPECIFICATION_CHANGE__EQUIVALENCE = UML_DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_SPECIFICATION_CHANGE__CONFLICT = UML_DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_SPECIFICATION_CHANGE__DISCRIMINANT = UML_DIFF__DISCRIMINANT;

	/**
	 * The feature id for the '<em><b>EReference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_SPECIFICATION_CHANGE__EREFERENCE = UML_DIFF__EREFERENCE;

	/**
	 * The number of structural features of the '<em>Execution Specification Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_SPECIFICATION_CHANGE_FEATURE_COUNT = UML_DIFF_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2.internal.impl.IntervalConstraintChangeImpl <em>Interval Constraint Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2.internal.impl.IntervalConstraintChangeImpl
	 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getIntervalConstraintChange()
	 * @generated
	 */
	int INTERVAL_CONSTRAINT_CHANGE = 4;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERVAL_CONSTRAINT_CHANGE__MATCH = UML_DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERVAL_CONSTRAINT_CHANGE__REQUIRES = UML_DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERVAL_CONSTRAINT_CHANGE__REQUIRED_BY = UML_DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERVAL_CONSTRAINT_CHANGE__IMPLIES = UML_DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERVAL_CONSTRAINT_CHANGE__IMPLIED_BY = UML_DIFF__IMPLIED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERVAL_CONSTRAINT_CHANGE__REFINES = UML_DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERVAL_CONSTRAINT_CHANGE__REFINED_BY = UML_DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERVAL_CONSTRAINT_CHANGE__PRIME_REFINING = UML_DIFF__PRIME_REFINING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERVAL_CONSTRAINT_CHANGE__KIND = UML_DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERVAL_CONSTRAINT_CHANGE__SOURCE = UML_DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERVAL_CONSTRAINT_CHANGE__STATE = UML_DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERVAL_CONSTRAINT_CHANGE__EQUIVALENCE = UML_DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERVAL_CONSTRAINT_CHANGE__CONFLICT = UML_DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERVAL_CONSTRAINT_CHANGE__DISCRIMINANT = UML_DIFF__DISCRIMINANT;

	/**
	 * The feature id for the '<em><b>EReference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERVAL_CONSTRAINT_CHANGE__EREFERENCE = UML_DIFF__EREFERENCE;

	/**
	 * The number of structural features of the '<em>Interval Constraint Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERVAL_CONSTRAINT_CHANGE_FEATURE_COUNT = UML_DIFF_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2.internal.impl.MessageChangeImpl <em>Message Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2.internal.impl.MessageChangeImpl
	 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getMessageChange()
	 * @generated
	 */
	int MESSAGE_CHANGE = 5;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_CHANGE__MATCH = UML_DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_CHANGE__REQUIRES = UML_DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_CHANGE__REQUIRED_BY = UML_DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_CHANGE__IMPLIES = UML_DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_CHANGE__IMPLIED_BY = UML_DIFF__IMPLIED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_CHANGE__REFINES = UML_DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_CHANGE__REFINED_BY = UML_DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_CHANGE__PRIME_REFINING = UML_DIFF__PRIME_REFINING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_CHANGE__KIND = UML_DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_CHANGE__SOURCE = UML_DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_CHANGE__STATE = UML_DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_CHANGE__EQUIVALENCE = UML_DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_CHANGE__CONFLICT = UML_DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_CHANGE__DISCRIMINANT = UML_DIFF__DISCRIMINANT;

	/**
	 * The feature id for the '<em><b>EReference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_CHANGE__EREFERENCE = UML_DIFF__EREFERENCE;

	/**
	 * The number of structural features of the '<em>Message Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_CHANGE_FEATURE_COUNT = UML_DIFF_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2.internal.impl.StereotypePropertyChangeImpl <em>Stereotype Property Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2.internal.impl.StereotypePropertyChangeImpl
	 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getStereotypePropertyChange()
	 * @generated
	 */
	int STEREOTYPE_PROPERTY_CHANGE = 6;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_PROPERTY_CHANGE__MATCH = UML_DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_PROPERTY_CHANGE__REQUIRES = UML_DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_PROPERTY_CHANGE__REQUIRED_BY = UML_DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_PROPERTY_CHANGE__IMPLIES = UML_DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_PROPERTY_CHANGE__IMPLIED_BY = UML_DIFF__IMPLIED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_PROPERTY_CHANGE__REFINES = UML_DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_PROPERTY_CHANGE__REFINED_BY = UML_DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_PROPERTY_CHANGE__PRIME_REFINING = UML_DIFF__PRIME_REFINING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_PROPERTY_CHANGE__KIND = UML_DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_PROPERTY_CHANGE__SOURCE = UML_DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_PROPERTY_CHANGE__STATE = UML_DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_PROPERTY_CHANGE__EQUIVALENCE = UML_DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_PROPERTY_CHANGE__CONFLICT = UML_DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_PROPERTY_CHANGE__DISCRIMINANT = UML_DIFF__DISCRIMINANT;

	/**
	 * The feature id for the '<em><b>EReference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_PROPERTY_CHANGE__EREFERENCE = UML_DIFF__EREFERENCE;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_PROPERTY_CHANGE__STEREOTYPE = UML_DIFF_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Stereotype Property Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_PROPERTY_CHANGE_FEATURE_COUNT = UML_DIFF_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2.internal.impl.StereotypeApplicationChangeImpl <em>Stereotype Application Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2.internal.impl.StereotypeApplicationChangeImpl
	 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getStereotypeApplicationChange()
	 * @generated
	 */
	int STEREOTYPE_APPLICATION_CHANGE = 7;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_APPLICATION_CHANGE__MATCH = UML_DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_APPLICATION_CHANGE__REQUIRES = UML_DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_APPLICATION_CHANGE__REQUIRED_BY = UML_DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_APPLICATION_CHANGE__IMPLIES = UML_DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_APPLICATION_CHANGE__IMPLIED_BY = UML_DIFF__IMPLIED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_APPLICATION_CHANGE__REFINES = UML_DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_APPLICATION_CHANGE__REFINED_BY = UML_DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_APPLICATION_CHANGE__PRIME_REFINING = UML_DIFF__PRIME_REFINING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_APPLICATION_CHANGE__KIND = UML_DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_APPLICATION_CHANGE__SOURCE = UML_DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_APPLICATION_CHANGE__STATE = UML_DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_APPLICATION_CHANGE__EQUIVALENCE = UML_DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_APPLICATION_CHANGE__CONFLICT = UML_DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_APPLICATION_CHANGE__DISCRIMINANT = UML_DIFF__DISCRIMINANT;

	/**
	 * The feature id for the '<em><b>EReference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_APPLICATION_CHANGE__EREFERENCE = UML_DIFF__EREFERENCE;

	/**
	 * The feature id for the '<em><b>Stereotype</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE = UML_DIFF_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Stereotype Application Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_APPLICATION_CHANGE_FEATURE_COUNT = UML_DIFF_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2.internal.impl.StereotypeReferenceChangeImpl <em>Stereotype Reference Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2.internal.impl.StereotypeReferenceChangeImpl
	 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getStereotypeReferenceChange()
	 * @generated
	 */
	int STEREOTYPE_REFERENCE_CHANGE = 8;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_REFERENCE_CHANGE__MATCH = UML_DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_REFERENCE_CHANGE__REQUIRES = UML_DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_REFERENCE_CHANGE__REQUIRED_BY = UML_DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_REFERENCE_CHANGE__IMPLIES = UML_DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_REFERENCE_CHANGE__IMPLIED_BY = UML_DIFF__IMPLIED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_REFERENCE_CHANGE__REFINES = UML_DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_REFERENCE_CHANGE__REFINED_BY = UML_DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_REFERENCE_CHANGE__PRIME_REFINING = UML_DIFF__PRIME_REFINING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_REFERENCE_CHANGE__KIND = UML_DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_REFERENCE_CHANGE__SOURCE = UML_DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_REFERENCE_CHANGE__STATE = UML_DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_REFERENCE_CHANGE__EQUIVALENCE = UML_DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_REFERENCE_CHANGE__CONFLICT = UML_DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_REFERENCE_CHANGE__DISCRIMINANT = UML_DIFF__DISCRIMINANT;

	/**
	 * The feature id for the '<em><b>EReference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_REFERENCE_CHANGE__EREFERENCE = UML_DIFF__EREFERENCE;

	/**
	 * The number of structural features of the '<em>Stereotype Reference Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREOTYPE_REFERENCE_CHANGE_FEATURE_COUNT = UML_DIFF_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2.internal.impl.ProfileApplicationChangeImpl <em>Profile Application Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2.internal.impl.ProfileApplicationChangeImpl
	 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getProfileApplicationChange()
	 * @generated
	 */
	int PROFILE_APPLICATION_CHANGE = 9;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_APPLICATION_CHANGE__MATCH = UML_DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_APPLICATION_CHANGE__REQUIRES = UML_DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_APPLICATION_CHANGE__REQUIRED_BY = UML_DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_APPLICATION_CHANGE__IMPLIES = UML_DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_APPLICATION_CHANGE__IMPLIED_BY = UML_DIFF__IMPLIED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_APPLICATION_CHANGE__REFINES = UML_DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_APPLICATION_CHANGE__REFINED_BY = UML_DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_APPLICATION_CHANGE__PRIME_REFINING = UML_DIFF__PRIME_REFINING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_APPLICATION_CHANGE__KIND = UML_DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_APPLICATION_CHANGE__SOURCE = UML_DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_APPLICATION_CHANGE__STATE = UML_DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_APPLICATION_CHANGE__EQUIVALENCE = UML_DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_APPLICATION_CHANGE__CONFLICT = UML_DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_APPLICATION_CHANGE__DISCRIMINANT = UML_DIFF__DISCRIMINANT;

	/**
	 * The feature id for the '<em><b>EReference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_APPLICATION_CHANGE__EREFERENCE = UML_DIFF__EREFERENCE;

	/**
	 * The feature id for the '<em><b>Profile</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_APPLICATION_CHANGE__PROFILE = UML_DIFF_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Profile Application Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_APPLICATION_CHANGE_FEATURE_COUNT = UML_DIFF_FEATURE_COUNT + 1;


	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.uml2.internal.impl.DirectedRelationshipChangeImpl <em>Directed Relationship Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.uml2.internal.impl.DirectedRelationshipChangeImpl
	 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getDirectedRelationshipChange()
	 * @generated
	 */
	int DIRECTED_RELATIONSHIP_CHANGE = 10;

	/**
	 * The feature id for the '<em><b>Match</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECTED_RELATIONSHIP_CHANGE__MATCH = UML_DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECTED_RELATIONSHIP_CHANGE__REQUIRES = UML_DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECTED_RELATIONSHIP_CHANGE__REQUIRED_BY = UML_DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECTED_RELATIONSHIP_CHANGE__IMPLIES = UML_DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECTED_RELATIONSHIP_CHANGE__IMPLIED_BY = UML_DIFF__IMPLIED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECTED_RELATIONSHIP_CHANGE__REFINES = UML_DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECTED_RELATIONSHIP_CHANGE__REFINED_BY = UML_DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECTED_RELATIONSHIP_CHANGE__PRIME_REFINING = UML_DIFF__PRIME_REFINING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECTED_RELATIONSHIP_CHANGE__KIND = UML_DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECTED_RELATIONSHIP_CHANGE__SOURCE = UML_DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECTED_RELATIONSHIP_CHANGE__STATE = UML_DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECTED_RELATIONSHIP_CHANGE__EQUIVALENCE = UML_DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECTED_RELATIONSHIP_CHANGE__CONFLICT = UML_DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Discriminant</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECTED_RELATIONSHIP_CHANGE__DISCRIMINANT = UML_DIFF__DISCRIMINANT;

	/**
	 * The feature id for the '<em><b>EReference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECTED_RELATIONSHIP_CHANGE__EREFERENCE = UML_DIFF__EREFERENCE;

	/**
	 * The number of structural features of the '<em>Directed Relationship Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECTED_RELATIONSHIP_CHANGE_FEATURE_COUNT = UML_DIFF_FEATURE_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2.internal.AssociationChange <em>Association Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Association Change</em>'.
	 * @see org.eclipse.emf.compare.uml2.internal.AssociationChange
	 * @generated
	 */
	EClass getAssociationChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2.internal.ExtendChange <em>Extend Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Extend Change</em>'.
	 * @see org.eclipse.emf.compare.uml2.internal.ExtendChange
	 * @generated
	 */
	EClass getExtendChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2.internal.GeneralizationSetChange <em>Generalization Set Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Generalization Set Change</em>'.
	 * @see org.eclipse.emf.compare.uml2.internal.GeneralizationSetChange
	 * @generated
	 */
	EClass getGeneralizationSetChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2.internal.ExecutionSpecificationChange <em>Execution Specification Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Execution Specification Change</em>'.
	 * @see org.eclipse.emf.compare.uml2.internal.ExecutionSpecificationChange
	 * @generated
	 */
	EClass getExecutionSpecificationChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2.internal.IntervalConstraintChange <em>Interval Constraint Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Interval Constraint Change</em>'.
	 * @see org.eclipse.emf.compare.uml2.internal.IntervalConstraintChange
	 * @generated
	 */
	EClass getIntervalConstraintChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2.internal.MessageChange <em>Message Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Message Change</em>'.
	 * @see org.eclipse.emf.compare.uml2.internal.MessageChange
	 * @generated
	 */
	EClass getMessageChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2.internal.StereotypePropertyChange <em>Stereotype Property Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Stereotype Property Change</em>'.
	 * @see org.eclipse.emf.compare.uml2.internal.StereotypePropertyChange
	 * @generated
	 */
	EClass getStereotypePropertyChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.uml2.internal.StereotypePropertyChange#getStereotype <em>Stereotype</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Stereotype</em>'.
	 * @see org.eclipse.emf.compare.uml2.internal.StereotypePropertyChange#getStereotype()
	 * @see #getStereotypePropertyChange()
	 * @generated
	 */
	EReference getStereotypePropertyChange_Stereotype();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange <em>Stereotype Application Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Stereotype Application Change</em>'.
	 * @see org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange
	 * @generated
	 */
	EClass getStereotypeApplicationChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange#getStereotype <em>Stereotype</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Stereotype</em>'.
	 * @see org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange#getStereotype()
	 * @see #getStereotypeApplicationChange()
	 * @generated
	 */
	EReference getStereotypeApplicationChange_Stereotype();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2.internal.StereotypeReferenceChange <em>Stereotype Reference Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Stereotype Reference Change</em>'.
	 * @see org.eclipse.emf.compare.uml2.internal.StereotypeReferenceChange
	 * @generated
	 */
	EClass getStereotypeReferenceChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2.internal.ProfileApplicationChange <em>Profile Application Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Profile Application Change</em>'.
	 * @see org.eclipse.emf.compare.uml2.internal.ProfileApplicationChange
	 * @generated
	 */
	EClass getProfileApplicationChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.uml2.internal.ProfileApplicationChange#getProfile <em>Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Profile</em>'.
	 * @see org.eclipse.emf.compare.uml2.internal.ProfileApplicationChange#getProfile()
	 * @see #getProfileApplicationChange()
	 * @generated
	 */
	EReference getProfileApplicationChange_Profile();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2.internal.DirectedRelationshipChange <em>Directed Relationship Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Directed Relationship Change</em>'.
	 * @see org.eclipse.emf.compare.uml2.internal.DirectedRelationshipChange
	 * @generated
	 */
	EClass getDirectedRelationshipChange();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.uml2.internal.UMLDiff <em>UML Diff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UML Diff</em>'.
	 * @see org.eclipse.emf.compare.uml2.internal.UMLDiff
	 * @generated
	 */
	EClass getUMLDiff();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.uml2.internal.UMLDiff#getDiscriminant <em>Discriminant</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Discriminant</em>'.
	 * @see org.eclipse.emf.compare.uml2.internal.UMLDiff#getDiscriminant()
	 * @see #getUMLDiff()
	 * @generated
	 */
	EReference getUMLDiff_Discriminant();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.uml2.internal.UMLDiff#getEReference <em>EReference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>EReference</em>'.
	 * @see org.eclipse.emf.compare.uml2.internal.UMLDiff#getEReference()
	 * @see #getUMLDiff()
	 * @generated
	 */
	EReference getUMLDiff_EReference();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	UMLCompareFactory getUMLCompareFactory();

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
	@SuppressWarnings("hiding")
	// generated code, removing warnings
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2.internal.impl.AssociationChangeImpl <em>Association Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2.internal.impl.AssociationChangeImpl
		 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getAssociationChange()
		 * @generated
		 */
		EClass ASSOCIATION_CHANGE = eINSTANCE.getAssociationChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2.internal.impl.ExtendChangeImpl <em>Extend Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2.internal.impl.ExtendChangeImpl
		 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getExtendChange()
		 * @generated
		 */
		EClass EXTEND_CHANGE = eINSTANCE.getExtendChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2.internal.impl.GeneralizationSetChangeImpl <em>Generalization Set Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2.internal.impl.GeneralizationSetChangeImpl
		 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getGeneralizationSetChange()
		 * @generated
		 */
		EClass GENERALIZATION_SET_CHANGE = eINSTANCE.getGeneralizationSetChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2.internal.impl.ExecutionSpecificationChangeImpl <em>Execution Specification Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2.internal.impl.ExecutionSpecificationChangeImpl
		 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getExecutionSpecificationChange()
		 * @generated
		 */
		EClass EXECUTION_SPECIFICATION_CHANGE = eINSTANCE.getExecutionSpecificationChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2.internal.impl.IntervalConstraintChangeImpl <em>Interval Constraint Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2.internal.impl.IntervalConstraintChangeImpl
		 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getIntervalConstraintChange()
		 * @generated
		 */
		EClass INTERVAL_CONSTRAINT_CHANGE = eINSTANCE.getIntervalConstraintChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2.internal.impl.MessageChangeImpl <em>Message Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2.internal.impl.MessageChangeImpl
		 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getMessageChange()
		 * @generated
		 */
		EClass MESSAGE_CHANGE = eINSTANCE.getMessageChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2.internal.impl.StereotypePropertyChangeImpl <em>Stereotype Property Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2.internal.impl.StereotypePropertyChangeImpl
		 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getStereotypePropertyChange()
		 * @generated
		 */
		EClass STEREOTYPE_PROPERTY_CHANGE = eINSTANCE.getStereotypePropertyChange();

		/**
		 * The meta object literal for the '<em><b>Stereotype</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STEREOTYPE_PROPERTY_CHANGE__STEREOTYPE = eINSTANCE.getStereotypePropertyChange_Stereotype();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2.internal.impl.StereotypeApplicationChangeImpl <em>Stereotype Application Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2.internal.impl.StereotypeApplicationChangeImpl
		 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getStereotypeApplicationChange()
		 * @generated
		 */
		EClass STEREOTYPE_APPLICATION_CHANGE = eINSTANCE.getStereotypeApplicationChange();

		/**
		 * The meta object literal for the '<em><b>Stereotype</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STEREOTYPE_APPLICATION_CHANGE__STEREOTYPE = eINSTANCE.getStereotypeApplicationChange_Stereotype();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2.internal.impl.StereotypeReferenceChangeImpl <em>Stereotype Reference Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2.internal.impl.StereotypeReferenceChangeImpl
		 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getStereotypeReferenceChange()
		 * @generated
		 */
		EClass STEREOTYPE_REFERENCE_CHANGE = eINSTANCE.getStereotypeReferenceChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2.internal.impl.ProfileApplicationChangeImpl <em>Profile Application Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2.internal.impl.ProfileApplicationChangeImpl
		 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getProfileApplicationChange()
		 * @generated
		 */
		EClass PROFILE_APPLICATION_CHANGE = eINSTANCE.getProfileApplicationChange();

		/**
		 * The meta object literal for the '<em><b>Profile</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROFILE_APPLICATION_CHANGE__PROFILE = eINSTANCE.getProfileApplicationChange_Profile();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2.internal.impl.DirectedRelationshipChangeImpl <em>Directed Relationship Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2.internal.impl.DirectedRelationshipChangeImpl
		 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getDirectedRelationshipChange()
		 * @generated
		 */
		EClass DIRECTED_RELATIONSHIP_CHANGE = eINSTANCE.getDirectedRelationshipChange();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.uml2.internal.impl.UMLDiffImpl <em>UML Diff</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLDiffImpl
		 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLComparePackageImpl#getUMLDiff()
		 * @generated
		 */
		EClass UML_DIFF = eINSTANCE.getUMLDiff();

		/**
		 * The meta object literal for the '<em><b>Discriminant</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UML_DIFF__DISCRIMINANT = eINSTANCE.getUMLDiff_Discriminant();

		/**
		 * The meta object literal for the '<em><b>EReference</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UML_DIFF__EREFERENCE = eINSTANCE.getUMLDiff_EReference();

	}

} //UMLComparePackage
