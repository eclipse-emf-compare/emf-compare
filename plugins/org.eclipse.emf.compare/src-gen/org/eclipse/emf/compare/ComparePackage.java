/**
 * Copyright (c) 2013, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare;

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
 * @see org.eclipse.emf.compare.CompareFactory
 * @model kind="package"
 * @generated
 */
public interface ComparePackage extends EPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The package name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "compare"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "compare"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	ComparePackage eINSTANCE = org.eclipse.emf.compare.impl.ComparePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.impl.ComparisonImpl <em>Comparison</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.impl.ComparisonImpl
	 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getComparison()
	 * @generated
	 */
	int COMPARISON = 0;

	/**
	 * The feature id for the '<em><b>Matched Resources</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPARISON__MATCHED_RESOURCES = 0;

	/**
	 * The feature id for the '<em><b>Matches</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPARISON__MATCHES = 1;

	/**
	 * The feature id for the '<em><b>Conflicts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPARISON__CONFLICTS = 2;

	/**
	 * The feature id for the '<em><b>Equivalences</b></em>' containment reference list.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPARISON__EQUIVALENCES = 3;

	/**
	 * The feature id for the '<em><b>Three Way</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPARISON__THREE_WAY = 4;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPARISON__DIAGNOSTIC = 5;

	/**
	 * The number of structural features of the '<em>Comparison</em>' class.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPARISON_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.impl.MatchResourceImpl <em>Match Resource</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.impl.MatchResourceImpl
	 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getMatchResource()
	 * @generated
	 */
	int MATCH_RESOURCE = 1;

	/**
	 * The feature id for the '<em><b>Left URI</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MATCH_RESOURCE__LEFT_URI = 0;

	/**
	 * The feature id for the '<em><b>Right URI</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MATCH_RESOURCE__RIGHT_URI = 1;

	/**
	 * The feature id for the '<em><b>Origin URI</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_RESOURCE__ORIGIN_URI = 2;

	/**
	 * The feature id for the '<em><b>Left</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_RESOURCE__LEFT = 3;

	/**
	 * The feature id for the '<em><b>Right</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_RESOURCE__RIGHT = 4;

	/**
	 * The feature id for the '<em><b>Origin</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MATCH_RESOURCE__ORIGIN = 5;

	/**
	 * The feature id for the '<em><b>Comparison</b></em>' container reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_RESOURCE__COMPARISON = 6;

	/**
	 * The feature id for the '<em><b>Location Changes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_RESOURCE__LOCATION_CHANGES = 7;

	/**
	 * The number of structural features of the '<em>Match Resource</em>' class.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_RESOURCE_FEATURE_COUNT = 8;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.impl.MatchImpl <em>Match</em>}' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.impl.MatchImpl
	 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getMatch()
	 * @generated
	 */
	int MATCH = 2;

	/**
	 * The feature id for the '<em><b>Submatches</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH__SUBMATCHES = 0;

	/**
	 * The feature id for the '<em><b>Differences</b></em>' containment reference list.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH__DIFFERENCES = 1;

	/**
	 * The feature id for the '<em><b>Left</b></em>' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH__LEFT = 2;

	/**
	 * The feature id for the '<em><b>Right</b></em>' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH__RIGHT = 3;

	/**
	 * The feature id for the '<em><b>Origin</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MATCH__ORIGIN = 4;

	/**
	 * The number of structural features of the '<em>Match</em>' class.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.impl.DiffImpl <em>Diff</em>}' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl
	 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getDiff()
	 * @generated
	 */
	int DIFF = 3;

	/**
	 * The feature id for the '<em><b>Match</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF__MATCH = 0;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF__REQUIRES = 1;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF__REQUIRED_BY = 2;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF__IMPLIES = 3;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF__IMPLIED_BY = 4;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF__REFINES = 5;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF__REFINED_BY = 6;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF__PRIME_REFINING = 7;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF__KIND = 8;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFF__SOURCE = 9;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF__STATE = 10;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF__EQUIVALENCE = 11;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIFF__CONFLICT = 12;

	/**
	 * The number of structural features of the '<em>Diff</em>' class.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_FEATURE_COUNT = 13;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.impl.ResourceAttachmentChangeImpl <em>Resource Attachment Change</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.impl.ResourceAttachmentChangeImpl
	 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getResourceAttachmentChange()
	 * @generated
	 */
	int RESOURCE_ATTACHMENT_CHANGE = 4;

	/**
	 * The feature id for the '<em><b>Match</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ATTACHMENT_CHANGE__MATCH = DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ATTACHMENT_CHANGE__REQUIRES = DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ATTACHMENT_CHANGE__REQUIRED_BY = DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ATTACHMENT_CHANGE__IMPLIES = DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ATTACHMENT_CHANGE__IMPLIED_BY = DIFF__IMPLIED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ATTACHMENT_CHANGE__REFINES = DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ATTACHMENT_CHANGE__REFINED_BY = DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ATTACHMENT_CHANGE__PRIME_REFINING = DIFF__PRIME_REFINING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ATTACHMENT_CHANGE__KIND = DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ATTACHMENT_CHANGE__SOURCE = DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ATTACHMENT_CHANGE__STATE = DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ATTACHMENT_CHANGE__EQUIVALENCE = DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ATTACHMENT_CHANGE__CONFLICT = DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Resource URI</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ATTACHMENT_CHANGE__RESOURCE_URI = DIFF_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Resource Attachment Change</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RESOURCE_ATTACHMENT_CHANGE_FEATURE_COUNT = DIFF_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.impl.ResourceLocationChangeImpl <em>Resource Location Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.impl.ResourceLocationChangeImpl
	 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getResourceLocationChange()
	 * @generated
	 */
	int RESOURCE_LOCATION_CHANGE = 5;

	/**
	 * The feature id for the '<em><b>Match</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION_CHANGE__MATCH = DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION_CHANGE__REQUIRES = DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION_CHANGE__REQUIRED_BY = DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION_CHANGE__IMPLIES = DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION_CHANGE__IMPLIED_BY = DIFF__IMPLIED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION_CHANGE__REFINES = DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION_CHANGE__REFINED_BY = DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION_CHANGE__PRIME_REFINING = DIFF__PRIME_REFINING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION_CHANGE__KIND = DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION_CHANGE__SOURCE = DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION_CHANGE__STATE = DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION_CHANGE__EQUIVALENCE = DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION_CHANGE__CONFLICT = DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Base Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION_CHANGE__BASE_LOCATION = DIFF_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Changed Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION_CHANGE__CHANGED_LOCATION = DIFF_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Resource Location Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_LOCATION_CHANGE_FEATURE_COUNT = DIFF_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.impl.ReferenceChangeImpl <em>Reference Change</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.impl.ReferenceChangeImpl
	 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getReferenceChange()
	 * @generated
	 */
	int REFERENCE_CHANGE = 6;

	/**
	 * The feature id for the '<em><b>Match</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__MATCH = DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__REQUIRES = DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__REQUIRED_BY = DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__IMPLIES = DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__IMPLIED_BY = DIFF__IMPLIED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__REFINES = DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__REFINED_BY = DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__PRIME_REFINING = DIFF__PRIME_REFINING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__KIND = DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__SOURCE = DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__STATE = DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__EQUIVALENCE = DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__CONFLICT = DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__REFERENCE = DIFF_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE__VALUE = DIFF_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Reference Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_CHANGE_FEATURE_COUNT = DIFF_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.impl.AttributeChangeImpl <em>Attribute Change</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.impl.AttributeChangeImpl
	 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getAttributeChange()
	 * @generated
	 */
	int ATTRIBUTE_CHANGE = 7;

	/**
	 * The feature id for the '<em><b>Match</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__MATCH = DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__REQUIRES = DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__REQUIRED_BY = DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__IMPLIES = DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__IMPLIED_BY = DIFF__IMPLIED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__REFINES = DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__REFINED_BY = DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__PRIME_REFINING = DIFF__PRIME_REFINING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__KIND = DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__SOURCE = DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__STATE = DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__EQUIVALENCE = DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__CONFLICT = DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__ATTRIBUTE = DIFF_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE__VALUE = DIFF_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Attribute Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_CHANGE_FEATURE_COUNT = DIFF_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.impl.FeatureMapChangeImpl <em>Feature Map Change</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.impl.FeatureMapChangeImpl
	 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getFeatureMapChange()
	 * @generated
	 */
	int FEATURE_MAP_CHANGE = 8;

	/**
	 * The feature id for the '<em><b>Match</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MAP_CHANGE__MATCH = DIFF__MATCH;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MAP_CHANGE__REQUIRES = DIFF__REQUIRES;

	/**
	 * The feature id for the '<em><b>Required By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MAP_CHANGE__REQUIRED_BY = DIFF__REQUIRED_BY;

	/**
	 * The feature id for the '<em><b>Implies</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MAP_CHANGE__IMPLIES = DIFF__IMPLIES;

	/**
	 * The feature id for the '<em><b>Implied By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MAP_CHANGE__IMPLIED_BY = DIFF__IMPLIED_BY;

	/**
	 * The feature id for the '<em><b>Refines</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MAP_CHANGE__REFINES = DIFF__REFINES;

	/**
	 * The feature id for the '<em><b>Refined By</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MAP_CHANGE__REFINED_BY = DIFF__REFINED_BY;

	/**
	 * The feature id for the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MAP_CHANGE__PRIME_REFINING = DIFF__PRIME_REFINING;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MAP_CHANGE__KIND = DIFF__KIND;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE_MAP_CHANGE__SOURCE = DIFF__SOURCE;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MAP_CHANGE__STATE = DIFF__STATE;

	/**
	 * The feature id for the '<em><b>Equivalence</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MAP_CHANGE__EQUIVALENCE = DIFF__EQUIVALENCE;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE_MAP_CHANGE__CONFLICT = DIFF__CONFLICT;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE_MAP_CHANGE__ATTRIBUTE = DIFF_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MAP_CHANGE__VALUE = DIFF_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Feature Map Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MAP_CHANGE_FEATURE_COUNT = DIFF_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.impl.ConflictImpl <em>Conflict</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.impl.ConflictImpl
	 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getConflict()
	 * @generated
	 */
	int CONFLICT = 9;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFLICT__KIND = 0;

	/**
	 * The feature id for the '<em><b>Differences</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFLICT__DIFFERENCES = 1;

	/**
	 * The number of structural features of the '<em>Conflict</em>' class.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFLICT_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.impl.EquivalenceImpl <em>Equivalence</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.impl.EquivalenceImpl
	 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getEquivalence()
	 * @generated
	 */
	int EQUIVALENCE = 10;

	/**
	 * The feature id for the '<em><b>Differences</b></em>' reference list.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EQUIVALENCE__DIFFERENCES = 0;

	/**
	 * The number of structural features of the '<em>Equivalence</em>' class.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EQUIVALENCE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.DifferenceKind <em>Difference Kind</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.DifferenceKind
	 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getDifferenceKind()
	 * @generated
	 */
	int DIFFERENCE_KIND = 11;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.DifferenceSource <em>Difference Source</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.DifferenceSource
	 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getDifferenceSource()
	 * @generated
	 */
	int DIFFERENCE_SOURCE = 12;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.DifferenceState <em>Difference State</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.DifferenceState
	 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getDifferenceState()
	 * @generated
	 */
	int DIFFERENCE_STATE = 13;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.ConflictKind <em>Conflict Kind</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.ConflictKind
	 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getConflictKind()
	 * @generated
	 */
	int CONFLICT_KIND = 14;

	/**
	 * The meta object id for the '<em>EIterable</em>' data type. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see java.lang.Iterable
	 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getEIterable()
	 * @generated
	 */
	int EITERABLE = 15;

	/**
	 * The meta object id for the '<em>IEquality Helper</em>' data type.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see org.eclipse.emf.compare.utils.IEqualityHelper
	 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getIEqualityHelper()
	 * @generated
	 */
	int IEQUALITY_HELPER = 16;

	/**
	 * The meta object id for the '<em>Diagnostic</em>' data type. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see org.eclipse.emf.common.util.Diagnostic
	 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getDiagnostic()
	 * @generated
	 * @since 3.1
	 */
	int DIAGNOSTIC = 17;

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.Comparison <em>Comparison</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Comparison</em>'.
	 * @see org.eclipse.emf.compare.Comparison
	 * @generated
	 */
	EClass getComparison();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.compare.Comparison#getMatchedResources <em>Matched Resources</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Matched Resources</em>'.
	 * @see org.eclipse.emf.compare.Comparison#getMatchedResources()
	 * @see #getComparison()
	 * @generated
	 */
	EReference getComparison_MatchedResources();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.Comparison#getMatches <em>Matches</em>}'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Matches</em>'.
	 * @see org.eclipse.emf.compare.Comparison#getMatches()
	 * @see #getComparison()
	 * @generated
	 */
	EReference getComparison_Matches();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.Comparison#getConflicts <em>Conflicts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Conflicts</em>'.
	 * @see org.eclipse.emf.compare.Comparison#getConflicts()
	 * @see #getComparison()
	 * @generated
	 */
	EReference getComparison_Conflicts();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.Comparison#getEquivalences <em>Equivalences</em>}'.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Equivalences</em>'.
	 * @see org.eclipse.emf.compare.Comparison#getEquivalences()
	 * @see #getComparison()
	 * @generated
	 */
	EReference getComparison_Equivalences();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.Comparison#isThreeWay <em>Three Way</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Three Way</em>'.
	 * @see org.eclipse.emf.compare.Comparison#isThreeWay()
	 * @see #getComparison()
	 * @generated
	 */
	EAttribute getComparison_ThreeWay();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.Comparison#getDiagnostic <em>Diagnostic</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Diagnostic</em>'.
	 * @see org.eclipse.emf.compare.Comparison#getDiagnostic()
	 * @see #getComparison()
	 * @generated
	 */
	EAttribute getComparison_Diagnostic();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.MatchResource <em>Match Resource</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Match Resource</em>'.
	 * @see org.eclipse.emf.compare.MatchResource
	 * @generated
	 */
	EClass getMatchResource();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.MatchResource#getLeftURI <em>Left URI</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Left URI</em>'.
	 * @see org.eclipse.emf.compare.MatchResource#getLeftURI()
	 * @see #getMatchResource()
	 * @generated
	 */
	EAttribute getMatchResource_LeftURI();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.MatchResource#getRightURI <em>Right URI</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Right URI</em>'.
	 * @see org.eclipse.emf.compare.MatchResource#getRightURI()
	 * @see #getMatchResource()
	 * @generated
	 */
	EAttribute getMatchResource_RightURI();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.MatchResource#getOriginURI <em>Origin URI</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Origin URI</em>'.
	 * @see org.eclipse.emf.compare.MatchResource#getOriginURI()
	 * @see #getMatchResource()
	 * @generated
	 */
	EAttribute getMatchResource_OriginURI();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.MatchResource#getLeft <em>Left</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Left</em>'.
	 * @see org.eclipse.emf.compare.MatchResource#getLeft()
	 * @see #getMatchResource()
	 * @generated
	 */
	EAttribute getMatchResource_Left();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.MatchResource#getRight <em>Right</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Right</em>'.
	 * @see org.eclipse.emf.compare.MatchResource#getRight()
	 * @see #getMatchResource()
	 * @generated
	 */
	EAttribute getMatchResource_Right();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.MatchResource#getOrigin <em>Origin</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Origin</em>'.
	 * @see org.eclipse.emf.compare.MatchResource#getOrigin()
	 * @see #getMatchResource()
	 * @generated
	 */
	EAttribute getMatchResource_Origin();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.emf.compare.MatchResource#getComparison <em>Comparison</em>}'.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Comparison</em>'.
	 * @see org.eclipse.emf.compare.MatchResource#getComparison()
	 * @see #getMatchResource()
	 * @generated
	 */
	EReference getMatchResource_Comparison();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.MatchResource#getLocationChanges <em>Location Changes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Location Changes</em>'.
	 * @see org.eclipse.emf.compare.MatchResource#getLocationChanges()
	 * @see #getMatchResource()
	 * @generated
	 */
	EReference getMatchResource_LocationChanges();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.Match <em>Match</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Match</em>'.
	 * @see org.eclipse.emf.compare.Match
	 * @generated
	 */
	EClass getMatch();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.Match#getSubmatches <em>Submatches</em>}'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Submatches</em>'.
	 * @see org.eclipse.emf.compare.Match#getSubmatches()
	 * @see #getMatch()
	 * @generated
	 */
	EReference getMatch_Submatches();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.Match#getDifferences <em>Differences</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Differences</em>'.
	 * @see org.eclipse.emf.compare.Match#getDifferences()
	 * @see #getMatch()
	 * @generated
	 */
	EReference getMatch_Differences();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.Match#getLeft <em>Left</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left</em>'.
	 * @see org.eclipse.emf.compare.Match#getLeft()
	 * @see #getMatch()
	 * @generated
	 */
	EReference getMatch_Left();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.Match#getRight <em>Right</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right</em>'.
	 * @see org.eclipse.emf.compare.Match#getRight()
	 * @see #getMatch()
	 * @generated
	 */
	EReference getMatch_Right();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.Match#getOrigin <em>Origin</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Origin</em>'.
	 * @see org.eclipse.emf.compare.Match#getOrigin()
	 * @see #getMatch()
	 * @generated
	 */
	EReference getMatch_Origin();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.Diff <em>Diff</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Diff</em>'.
	 * @see org.eclipse.emf.compare.Diff
	 * @generated
	 */
	EClass getDiff();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.Diff#getMatch <em>Match</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Match</em>'.
	 * @see org.eclipse.emf.compare.Diff#getMatch()
	 * @see #getDiff()
	 * @generated
	 */
	EReference getDiff_Match();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.Diff#getRequires <em>Requires</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Requires</em>'.
	 * @see org.eclipse.emf.compare.Diff#getRequires()
	 * @see #getDiff()
	 * @generated
	 */
	EReference getDiff_Requires();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.Diff#getRequiredBy <em>Required By</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Required By</em>'.
	 * @see org.eclipse.emf.compare.Diff#getRequiredBy()
	 * @see #getDiff()
	 * @generated
	 */
	EReference getDiff_RequiredBy();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.Diff#getImplies <em>Implies</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Implies</em>'.
	 * @see org.eclipse.emf.compare.Diff#getImplies()
	 * @see #getDiff()
	 * @generated
	 */
	EReference getDiff_Implies();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.Diff#getImpliedBy <em>Implied By</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Implied By</em>'.
	 * @see org.eclipse.emf.compare.Diff#getImpliedBy()
	 * @see #getDiff()
	 * @generated
	 */
	EReference getDiff_ImpliedBy();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.Diff#getRefines <em>Refines</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Refines</em>'.
	 * @see org.eclipse.emf.compare.Diff#getRefines()
	 * @see #getDiff()
	 * @generated
	 */
	EReference getDiff_Refines();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.Diff#getRefinedBy <em>Refined By</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Refined By</em>'.
	 * @see org.eclipse.emf.compare.Diff#getRefinedBy()
	 * @see #getDiff()
	 * @generated
	 */
	EReference getDiff_RefinedBy();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.Diff#getPrimeRefining <em>Prime Refining</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Prime Refining</em>'.
	 * @see org.eclipse.emf.compare.Diff#getPrimeRefining()
	 * @see #getDiff()
	 * @generated
	 */
	EReference getDiff_PrimeRefining();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.Diff#getKind <em>Kind</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Kind</em>'.
	 * @see org.eclipse.emf.compare.Diff#getKind()
	 * @see #getDiff()
	 * @generated
	 */
	EAttribute getDiff_Kind();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.Diff#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source</em>'.
	 * @see org.eclipse.emf.compare.Diff#getSource()
	 * @see #getDiff()
	 * @generated
	 */
	EAttribute getDiff_Source();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.Diff#getState <em>State</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>State</em>'.
	 * @see org.eclipse.emf.compare.Diff#getState()
	 * @see #getDiff()
	 * @generated
	 */
	EAttribute getDiff_State();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.Diff#getEquivalence <em>Equivalence</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Equivalence</em>'.
	 * @see org.eclipse.emf.compare.Diff#getEquivalence()
	 * @see #getDiff()
	 * @generated
	 */
	EReference getDiff_Equivalence();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.Diff#getConflict <em>Conflict</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Conflict</em>'.
	 * @see org.eclipse.emf.compare.Diff#getConflict()
	 * @see #getDiff()
	 * @generated
	 */
	EReference getDiff_Conflict();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.ResourceAttachmentChange <em>Resource Attachment Change</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource Attachment Change</em>'.
	 * @see org.eclipse.emf.compare.ResourceAttachmentChange
	 * @generated
	 */
	EClass getResourceAttachmentChange();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.emf.compare.ResourceAttachmentChange#getResourceURI <em>Resource URI</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Resource URI</em>'.
	 * @see org.eclipse.emf.compare.ResourceAttachmentChange#getResourceURI()
	 * @see #getResourceAttachmentChange()
	 * @generated
	 */
	EAttribute getResourceAttachmentChange_ResourceURI();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.ResourceLocationChange <em>Resource Location Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource Location Change</em>'.
	 * @see org.eclipse.emf.compare.ResourceLocationChange
	 * @generated
	 */
	EClass getResourceLocationChange();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.ResourceLocationChange#getBaseLocation <em>Base Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Location</em>'.
	 * @see org.eclipse.emf.compare.ResourceLocationChange#getBaseLocation()
	 * @see #getResourceLocationChange()
	 * @generated
	 */
	EAttribute getResourceLocationChange_BaseLocation();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.ResourceLocationChange#getChangedLocation <em>Changed Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Changed Location</em>'.
	 * @see org.eclipse.emf.compare.ResourceLocationChange#getChangedLocation()
	 * @see #getResourceLocationChange()
	 * @generated
	 */
	EAttribute getResourceLocationChange_ChangedLocation();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.ReferenceChange <em>Reference Change</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Reference Change</em>'.
	 * @see org.eclipse.emf.compare.ReferenceChange
	 * @generated
	 */
	EClass getReferenceChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.ReferenceChange#getReference <em>Reference</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Reference</em>'.
	 * @see org.eclipse.emf.compare.ReferenceChange#getReference()
	 * @see #getReferenceChange()
	 * @generated
	 */
	EReference getReferenceChange_Reference();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.ReferenceChange#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Value</em>'.
	 * @see org.eclipse.emf.compare.ReferenceChange#getValue()
	 * @see #getReferenceChange()
	 * @generated
	 */
	EReference getReferenceChange_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.AttributeChange <em>Attribute Change</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute Change</em>'.
	 * @see org.eclipse.emf.compare.AttributeChange
	 * @generated
	 */
	EClass getAttributeChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.AttributeChange#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Attribute</em>'.
	 * @see org.eclipse.emf.compare.AttributeChange#getAttribute()
	 * @see #getAttributeChange()
	 * @generated
	 */
	EReference getAttributeChange_Attribute();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.AttributeChange#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.emf.compare.AttributeChange#getValue()
	 * @see #getAttributeChange()
	 * @generated
	 */
	EAttribute getAttributeChange_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.FeatureMapChange <em>Feature Map Change</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Feature Map Change</em>'.
	 * @see org.eclipse.emf.compare.FeatureMapChange
	 * @generated
	 */
	EClass getFeatureMapChange();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.FeatureMapChange#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Attribute</em>'.
	 * @see org.eclipse.emf.compare.FeatureMapChange#getAttribute()
	 * @see #getFeatureMapChange()
	 * @generated
	 */
	EReference getFeatureMapChange_Attribute();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.FeatureMapChange#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.emf.compare.FeatureMapChange#getValue()
	 * @see #getFeatureMapChange()
	 * @generated
	 */
	EAttribute getFeatureMapChange_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.Conflict <em>Conflict</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Conflict</em>'.
	 * @see org.eclipse.emf.compare.Conflict
	 * @generated
	 */
	EClass getConflict();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.Conflict#getKind <em>Kind</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Kind</em>'.
	 * @see org.eclipse.emf.compare.Conflict#getKind()
	 * @see #getConflict()
	 * @generated
	 */
	EAttribute getConflict_Kind();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.Conflict#getDifferences <em>Differences</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Differences</em>'.
	 * @see org.eclipse.emf.compare.Conflict#getDifferences()
	 * @see #getConflict()
	 * @generated
	 */
	EReference getConflict_Differences();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.Equivalence <em>Equivalence</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Equivalence</em>'.
	 * @see org.eclipse.emf.compare.Equivalence
	 * @generated
	 */
	EClass getEquivalence();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.Equivalence#getDifferences <em>Differences</em>}'.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Differences</em>'.
	 * @see org.eclipse.emf.compare.Equivalence#getDifferences()
	 * @see #getEquivalence()
	 * @generated
	 */
	EReference getEquivalence_Differences();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.emf.compare.DifferenceKind <em>Difference Kind</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Difference Kind</em>'.
	 * @see org.eclipse.emf.compare.DifferenceKind
	 * @generated
	 */
	EEnum getDifferenceKind();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.emf.compare.DifferenceSource <em>Difference Source</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Difference Source</em>'.
	 * @see org.eclipse.emf.compare.DifferenceSource
	 * @generated
	 */
	EEnum getDifferenceSource();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.emf.compare.DifferenceState <em>Difference State</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Difference State</em>'.
	 * @see org.eclipse.emf.compare.DifferenceState
	 * @generated
	 */
	EEnum getDifferenceState();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.emf.compare.ConflictKind <em>Conflict Kind</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Conflict Kind</em>'.
	 * @see org.eclipse.emf.compare.ConflictKind
	 * @generated
	 */
	EEnum getConflictKind();

	/**
	 * Returns the meta object for data type '{@link java.lang.Iterable <em>EIterable</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>EIterable</em>'.
	 * @see java.lang.Iterable
	 * @model instanceClass="java.lang.Iterable" typeParameters="T"
	 * @generated
	 */
	EDataType getEIterable();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.emf.compare.utils.IEqualityHelper <em>IEquality Helper</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>IEquality Helper</em>'.
	 * @see org.eclipse.emf.compare.utils.IEqualityHelper
	 * @model instanceClass="org.eclipse.emf.compare.utils.IEqualityHelper" serializeable="false"
	 * @generated
	 */
	EDataType getIEqualityHelper();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.emf.common.util.Diagnostic <em>Diagnostic</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Diagnostic</em>'.
	 * @see org.eclipse.emf.common.util.Diagnostic
	 * @model instanceClass="org.eclipse.emf.common.util.Diagnostic" serializeable="false"
	 * @generated
	 */
	EDataType getDiagnostic();

	/**
	 * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CompareFactory getCompareFactory();

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
	// generated code, removing warnings
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.impl.ComparisonImpl <em>Comparison</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.impl.ComparisonImpl
		 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getComparison()
		 * @generated
		 */
		EClass COMPARISON = eINSTANCE.getComparison();

		/**
		 * The meta object literal for the '<em><b>Matched Resources</b></em>' containment reference list feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPARISON__MATCHED_RESOURCES = eINSTANCE.getComparison_MatchedResources();

		/**
		 * The meta object literal for the '<em><b>Matches</b></em>' containment reference list feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference COMPARISON__MATCHES = eINSTANCE.getComparison_Matches();

		/**
		 * The meta object literal for the '<em><b>Conflicts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPARISON__CONFLICTS = eINSTANCE.getComparison_Conflicts();

		/**
		 * The meta object literal for the '<em><b>Equivalences</b></em>' containment reference list feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPARISON__EQUIVALENCES = eINSTANCE.getComparison_Equivalences();

		/**
		 * The meta object literal for the '<em><b>Three Way</b></em>' attribute feature.
		 * <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPARISON__THREE_WAY = eINSTANCE.getComparison_ThreeWay();

		/**
		 * The meta object literal for the '<em><b>Diagnostic</b></em>' attribute feature.
		 * <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPARISON__DIAGNOSTIC = eINSTANCE.getComparison_Diagnostic();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.impl.MatchResourceImpl <em>Match Resource</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.impl.MatchResourceImpl
		 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getMatchResource()
		 * @generated
		 */
		EClass MATCH_RESOURCE = eINSTANCE.getMatchResource();

		/**
		 * The meta object literal for the '<em><b>Left URI</b></em>' attribute feature.
		 * <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MATCH_RESOURCE__LEFT_URI = eINSTANCE.getMatchResource_LeftURI();

		/**
		 * The meta object literal for the '<em><b>Right URI</b></em>' attribute feature.
		 * <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MATCH_RESOURCE__RIGHT_URI = eINSTANCE.getMatchResource_RightURI();

		/**
		 * The meta object literal for the '<em><b>Origin URI</b></em>' attribute feature.
		 * <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MATCH_RESOURCE__ORIGIN_URI = eINSTANCE.getMatchResource_OriginURI();

		/**
		 * The meta object literal for the '<em><b>Left</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MATCH_RESOURCE__LEFT = eINSTANCE.getMatchResource_Left();

		/**
		 * The meta object literal for the '<em><b>Right</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MATCH_RESOURCE__RIGHT = eINSTANCE.getMatchResource_Right();

		/**
		 * The meta object literal for the '<em><b>Origin</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MATCH_RESOURCE__ORIGIN = eINSTANCE.getMatchResource_Origin();

		/**
		 * The meta object literal for the '<em><b>Comparison</b></em>' container reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MATCH_RESOURCE__COMPARISON = eINSTANCE.getMatchResource_Comparison();

		/**
		 * The meta object literal for the '<em><b>Location Changes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MATCH_RESOURCE__LOCATION_CHANGES = eINSTANCE.getMatchResource_LocationChanges();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.impl.MatchImpl <em>Match</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.impl.MatchImpl
		 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getMatch()
		 * @generated
		 */
		EClass MATCH = eINSTANCE.getMatch();

		/**
		 * The meta object literal for the '<em><b>Submatches</b></em>' containment reference list feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @generated
		 */
		EReference MATCH__SUBMATCHES = eINSTANCE.getMatch_Submatches();

		/**
		 * The meta object literal for the '<em><b>Differences</b></em>' containment reference list feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @generated
		 */
		EReference MATCH__DIFFERENCES = eINSTANCE.getMatch_Differences();

		/**
		 * The meta object literal for the '<em><b>Left</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MATCH__LEFT = eINSTANCE.getMatch_Left();

		/**
		 * The meta object literal for the '<em><b>Right</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MATCH__RIGHT = eINSTANCE.getMatch_Right();

		/**
		 * The meta object literal for the '<em><b>Origin</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MATCH__ORIGIN = eINSTANCE.getMatch_Origin();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.impl.DiffImpl <em>Diff</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.impl.DiffImpl
		 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getDiff()
		 * @generated
		 */
		EClass DIFF = eINSTANCE.getDiff();

		/**
		 * The meta object literal for the '<em><b>Match</b></em>' container reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIFF__MATCH = eINSTANCE.getDiff_Match();

		/**
		 * The meta object literal for the '<em><b>Requires</b></em>' reference list feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIFF__REQUIRES = eINSTANCE.getDiff_Requires();

		/**
		 * The meta object literal for the '<em><b>Required By</b></em>' reference list feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIFF__REQUIRED_BY = eINSTANCE.getDiff_RequiredBy();

		/**
		 * The meta object literal for the '<em><b>Implies</b></em>' reference list feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 * @since 3.1
		 */
		EReference DIFF__IMPLIES = eINSTANCE.getDiff_Implies();

		/**
		 * The meta object literal for the '<em><b>Implied By</b></em>' reference list feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 * @since 3.1
		 */
		EReference DIFF__IMPLIED_BY = eINSTANCE.getDiff_ImpliedBy();

		/**
		 * The meta object literal for the '<em><b>Refines</b></em>' reference list feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIFF__REFINES = eINSTANCE.getDiff_Refines();

		/**
		 * The meta object literal for the '<em><b>Refined By</b></em>' reference list feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIFF__REFINED_BY = eINSTANCE.getDiff_RefinedBy();

		/**
		 * The meta object literal for the '<em><b>Prime Refining</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 * @since 3.1
		 */
		EReference DIFF__PRIME_REFINING = eINSTANCE.getDiff_PrimeRefining();

		/**
		 * The meta object literal for the '<em><b>Kind</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIFF__KIND = eINSTANCE.getDiff_Kind();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIFF__SOURCE = eINSTANCE.getDiff_Source();

		/**
		 * The meta object literal for the '<em><b>State</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIFF__STATE = eINSTANCE.getDiff_State();

		/**
		 * The meta object literal for the '<em><b>Equivalence</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DIFF__EQUIVALENCE = eINSTANCE.getDiff_Equivalence();

		/**
		 * The meta object literal for the '<em><b>Conflict</b></em>' reference feature.
		 * <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIFF__CONFLICT = eINSTANCE.getDiff_Conflict();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.impl.ResourceAttachmentChangeImpl <em>Resource Attachment Change</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.impl.ResourceAttachmentChangeImpl
		 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getResourceAttachmentChange()
		 * @generated
		 */
		EClass RESOURCE_ATTACHMENT_CHANGE = eINSTANCE.getResourceAttachmentChange();

		/**
		 * The meta object literal for the '<em><b>Resource URI</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute RESOURCE_ATTACHMENT_CHANGE__RESOURCE_URI = eINSTANCE
				.getResourceAttachmentChange_ResourceURI();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.impl.ResourceLocationChangeImpl <em>Resource Location Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.impl.ResourceLocationChangeImpl
		 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getResourceLocationChange()
		 * @generated
		 */
		EClass RESOURCE_LOCATION_CHANGE = eINSTANCE.getResourceLocationChange();

		/**
		 * The meta object literal for the '<em><b>Base Location</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESOURCE_LOCATION_CHANGE__BASE_LOCATION = eINSTANCE
				.getResourceLocationChange_BaseLocation();

		/**
		 * The meta object literal for the '<em><b>Changed Location</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESOURCE_LOCATION_CHANGE__CHANGED_LOCATION = eINSTANCE
				.getResourceLocationChange_ChangedLocation();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.impl.ReferenceChangeImpl <em>Reference Change</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.impl.ReferenceChangeImpl
		 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getReferenceChange()
		 * @generated
		 */
		EClass REFERENCE_CHANGE = eINSTANCE.getReferenceChange();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' reference feature.
		 * <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * @generated
		 */
		EReference REFERENCE_CHANGE__REFERENCE = eINSTANCE.getReferenceChange_Reference();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REFERENCE_CHANGE__VALUE = eINSTANCE.getReferenceChange_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.impl.AttributeChangeImpl <em>Attribute Change</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.impl.AttributeChangeImpl
		 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getAttributeChange()
		 * @generated
		 */
		EClass ATTRIBUTE_CHANGE = eINSTANCE.getAttributeChange();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' reference feature.
		 * <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * @generated
		 */
		EReference ATTRIBUTE_CHANGE__ATTRIBUTE = eINSTANCE.getAttributeChange_Attribute();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE_CHANGE__VALUE = eINSTANCE.getAttributeChange_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.impl.FeatureMapChangeImpl <em>Feature Map Change</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.impl.FeatureMapChangeImpl
		 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getFeatureMapChange()
		 * @generated
		 */
		EClass FEATURE_MAP_CHANGE = eINSTANCE.getFeatureMapChange();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' reference feature.
		 * <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * @generated
		 */
		EReference FEATURE_MAP_CHANGE__ATTRIBUTE = eINSTANCE.getFeatureMapChange_Attribute();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FEATURE_MAP_CHANGE__VALUE = eINSTANCE.getFeatureMapChange_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.impl.ConflictImpl <em>Conflict</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.impl.ConflictImpl
		 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getConflict()
		 * @generated
		 */
		EClass CONFLICT = eINSTANCE.getConflict();

		/**
		 * The meta object literal for the '<em><b>Kind</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONFLICT__KIND = eINSTANCE.getConflict_Kind();

		/**
		 * The meta object literal for the '<em><b>Differences</b></em>' reference list feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CONFLICT__DIFFERENCES = eINSTANCE.getConflict_Differences();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.impl.EquivalenceImpl <em>Equivalence</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.impl.EquivalenceImpl
		 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getEquivalence()
		 * @generated
		 */
		EClass EQUIVALENCE = eINSTANCE.getEquivalence();

		/**
		 * The meta object literal for the '<em><b>Differences</b></em>' reference list feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference EQUIVALENCE__DIFFERENCES = eINSTANCE.getEquivalence_Differences();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.DifferenceKind <em>Difference Kind</em>}' enum.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.DifferenceKind
		 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getDifferenceKind()
		 * @generated
		 */
		EEnum DIFFERENCE_KIND = eINSTANCE.getDifferenceKind();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.DifferenceSource <em>Difference Source</em>}' enum.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.DifferenceSource
		 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getDifferenceSource()
		 * @generated
		 */
		EEnum DIFFERENCE_SOURCE = eINSTANCE.getDifferenceSource();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.DifferenceState <em>Difference State</em>}' enum.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.DifferenceState
		 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getDifferenceState()
		 * @generated
		 */
		EEnum DIFFERENCE_STATE = eINSTANCE.getDifferenceState();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.ConflictKind <em>Conflict Kind</em>}' enum.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.ConflictKind
		 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getConflictKind()
		 * @generated
		 */
		EEnum CONFLICT_KIND = eINSTANCE.getConflictKind();

		/**
		 * The meta object literal for the '<em>EIterable</em>' data type.
		 * <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * @see java.lang.Iterable
		 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getEIterable()
		 * @generated
		 */
		EDataType EITERABLE = eINSTANCE.getEIterable();

		/**
		 * The meta object literal for the '<em>IEquality Helper</em>' data type.
		 * <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * @see org.eclipse.emf.compare.utils.IEqualityHelper
		 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getIEqualityHelper()
		 * @generated
		 */
		EDataType IEQUALITY_HELPER = eINSTANCE.getIEqualityHelper();

		/**
		 * The meta object literal for the '<em>Diagnostic</em>' data type.
		 * <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * @see org.eclipse.emf.common.util.Diagnostic
		 * @see org.eclipse.emf.compare.impl.ComparePackageImpl#getDiagnostic()
		 * @generated
		 */
		EDataType DIAGNOSTIC = eINSTANCE.getDiagnostic();

	}

} // ComparePackage
