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
package org.eclipse.emf.compare.match.metamodel;

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
 * @see org.eclipse.emf.compare.match.metamodel.MatchFactory
 * @model kind="package"
 * @generated
 */
@SuppressWarnings("hiding")
public interface MatchPackage extends EPackage {
	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	MatchPackage eINSTANCE = org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl.init();

	/**
	 * The package name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "match"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "match"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare/match/1.1"; //$NON-NLS-1$

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.match.metamodel.impl.MatchElementImpl <em>Element</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchElementImpl
	 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getMatchElement()
	 * @generated
	 */
	int MATCH_ELEMENT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.match.metamodel.impl.MatchModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchModelImpl
	 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getMatchModel()
	 * @generated
	 */
	int MATCH_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Left Model</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_MODEL__LEFT_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Right Model</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_MODEL__RIGHT_MODEL = 1;

	/**
	 * The feature id for the '<em><b>Origin Model</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_MODEL__ORIGIN_MODEL = 2;

	/**
	 * The feature id for the '<em><b>Matched Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MATCH_MODEL__MATCHED_ELEMENTS = 3;

	/**
	 * The feature id for the '<em><b>Unmatched Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_MODEL__UNMATCHED_ELEMENTS = 4;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_MODEL_FEATURE_COUNT = 5;

	/**
	 * The feature id for the '<em><b>Similarity</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_ELEMENT__SIMILARITY = 0;

	/**
	 * The feature id for the '<em><b>Sub Match Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MATCH_ELEMENT__SUB_MATCH_ELEMENTS = 1;

	/**
	 * The number of structural features of the '<em>Element</em>' class.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_ELEMENT_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.match.metamodel.impl.Match2ElementsImpl <em>Match2 Elements</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.match.metamodel.impl.Match2ElementsImpl
	 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getMatch2Elements()
	 * @generated
	 */
	int MATCH2_ELEMENTS = 2;

	/**
	 * The feature id for the '<em><b>Similarity</b></em>' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH2_ELEMENTS__SIMILARITY = MATCH_ELEMENT__SIMILARITY;

	/**
	 * The feature id for the '<em><b>Sub Match Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MATCH2_ELEMENTS__SUB_MATCH_ELEMENTS = MATCH_ELEMENT__SUB_MATCH_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH2_ELEMENTS__LEFT_ELEMENT = MATCH_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH2_ELEMENTS__RIGHT_ELEMENT = MATCH_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Match2 Elements</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH2_ELEMENTS_FEATURE_COUNT = MATCH_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.match.metamodel.impl.Match3ElementsImpl <em>Match3 Elements</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.match.metamodel.impl.Match3ElementsImpl
	 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getMatch3Elements()
	 * @generated
	 */
	int MATCH3_ELEMENTS = 3;

	/**
	 * The feature id for the '<em><b>Similarity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH3_ELEMENTS__SIMILARITY = MATCH2_ELEMENTS__SIMILARITY;

	/**
	 * The feature id for the '<em><b>Sub Match Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH3_ELEMENTS__SUB_MATCH_ELEMENTS = MATCH2_ELEMENTS__SUB_MATCH_ELEMENTS;

	/**
	 * The feature id for the '<em><b>Left Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH3_ELEMENTS__LEFT_ELEMENT = MATCH2_ELEMENTS__LEFT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH3_ELEMENTS__RIGHT_ELEMENT = MATCH2_ELEMENTS__RIGHT_ELEMENT;

	/**
	 * The feature id for the '<em><b>Origin Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH3_ELEMENTS__ORIGIN_ELEMENT = MATCH2_ELEMENTS_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Match3 Elements</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH3_ELEMENTS_FEATURE_COUNT = MATCH2_ELEMENTS_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.match.metamodel.impl.UnmatchElementImpl <em>Unmatch Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.match.metamodel.impl.UnmatchElementImpl
	 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getUnmatchElement()
	 * @generated
	 */
	int UNMATCH_ELEMENT = 4;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNMATCH_ELEMENT__ELEMENT = 0;

	/**
	 * The number of structural features of the '<em>Unmatch Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNMATCH_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.match.metamodel.impl.RemoteUnmatchElementImpl <em>Remote Unmatch Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.match.metamodel.impl.RemoteUnmatchElementImpl
	 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getRemoteUnmatchElement()
	 * @generated
	 */
	int REMOTE_UNMATCH_ELEMENT = 5;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOTE_UNMATCH_ELEMENT__ELEMENT = UNMATCH_ELEMENT__ELEMENT;

	/**
	 * The number of structural features of the '<em>Remote Unmatch Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOTE_UNMATCH_ELEMENT_FEATURE_COUNT = UNMATCH_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.match.metamodel.Match2Elements <em>Match2 Elements</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Match2 Elements</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.Match2Elements
	 * @generated
	 */
	EClass getMatch2Elements();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.match.metamodel.Match2Elements#getLeftElement <em>Left Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left Element</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.Match2Elements#getLeftElement()
	 * @see #getMatch2Elements()
	 * @generated
	 */
	EReference getMatch2Elements_LeftElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.match.metamodel.Match2Elements#getRightElement <em>Right Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right Element</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.Match2Elements#getRightElement()
	 * @see #getMatch2Elements()
	 * @generated
	 */
	EReference getMatch2Elements_RightElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.match.metamodel.Match3Elements <em>Match3 Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Match3 Elements</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.Match3Elements
	 * @generated
	 */
	EClass getMatch3Elements();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.match.metamodel.Match3Elements#getOriginElement <em>Origin Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Origin Element</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.Match3Elements#getOriginElement()
	 * @see #getMatch3Elements()
	 * @generated
	 */
	EReference getMatch3Elements_OriginElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.match.metamodel.UnmatchElement <em>Unmatch Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Unmatch Element</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.UnmatchElement
	 * @generated
	 */
	EClass getUnmatchElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.compare.match.metamodel.UnmatchElement#getElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Element</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.UnmatchElement#getElement()
	 * @see #getUnmatchElement()
	 * @generated
	 */
	EReference getUnmatchElement_Element();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.match.metamodel.RemoteUnmatchElement <em>Remote Unmatch Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Remote Unmatch Element</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.RemoteUnmatchElement
	 * @generated
	 */
	EClass getRemoteUnmatchElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.match.metamodel.MatchElement <em>Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchElement
	 * @generated
	 */
	EClass getMatchElement();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.match.metamodel.MatchElement#getSimilarity <em>Similarity</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Similarity</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchElement#getSimilarity()
	 * @see #getMatchElement()
	 * @generated
	 */
	EAttribute getMatchElement_Similarity();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.match.metamodel.MatchElement#getSubMatchElements <em>Sub Match Elements</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sub Match Elements</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchElement#getSubMatchElements()
	 * @see #getMatchElement()
	 * @generated
	 */
	EReference getMatchElement_SubMatchElements();

	/**
	 * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MatchFactory getMatchFactory();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.match.metamodel.MatchModel <em>Model</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchModel
	 * @generated
	 */
	EClass getMatchModel();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getLeftModel <em>Left Model</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Left Model</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchModel#getLeftModel()
	 * @see #getMatchModel()
	 * @generated
	 */
	EAttribute getMatchModel_LeftModel();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getMatchedElements <em>Matched Elements</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Matched Elements</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchModel#getMatchedElements()
	 * @see #getMatchModel()
	 * @generated
	 */
	EReference getMatchModel_MatchedElements();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getUnmatchedElements <em>Unmatched Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Unmatched Elements</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchModel#getUnmatchedElements()
	 * @see #getMatchModel()
	 * @generated
	 */
	EReference getMatchModel_UnmatchedElements();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getOriginModel <em>Origin Model</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Origin Model</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchModel#getOriginModel()
	 * @see #getMatchModel()
	 * @generated
	 */
	EAttribute getMatchModel_OriginModel();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getRightModel <em>Right Model</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Right Model</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchModel#getRightModel()
	 * @see #getMatchModel()
	 * @generated
	 */
	EAttribute getMatchModel_RightModel();

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
		 * The meta object literal for the '{@link org.eclipse.emf.compare.match.metamodel.impl.MatchElementImpl <em>Element</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchElementImpl
		 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getMatchElement()
		 * @generated
		 */
		EClass MATCH_ELEMENT = eINSTANCE.getMatchElement();

		/**
		 * The meta object literal for the '<em><b>Similarity</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute MATCH_ELEMENT__SIMILARITY = eINSTANCE.getMatchElement_Similarity();

		/**
		 * The meta object literal for the '<em><b>Sub Match Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @generated
		 */
		EReference MATCH_ELEMENT__SUB_MATCH_ELEMENTS = eINSTANCE.getMatchElement_SubMatchElements();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.match.metamodel.impl.MatchModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchModelImpl
		 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getMatchModel()
		 * @generated
		 */
		EClass MATCH_MODEL = eINSTANCE.getMatchModel();

		/**
		 * The meta object literal for the '<em><b>Left Model</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute MATCH_MODEL__LEFT_MODEL = eINSTANCE.getMatchModel_LeftModel();

		/**
		 * The meta object literal for the '<em><b>Matched Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @generated
		 */
		EReference MATCH_MODEL__MATCHED_ELEMENTS = eINSTANCE.getMatchModel_MatchedElements();

		/**
		 * The meta object literal for the '<em><b>Unmatched Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MATCH_MODEL__UNMATCHED_ELEMENTS = eINSTANCE.getMatchModel_UnmatchedElements();

		/**
		 * The meta object literal for the '<em><b>Origin Model</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute MATCH_MODEL__ORIGIN_MODEL = eINSTANCE.getMatchModel_OriginModel();

		/**
		 * The meta object literal for the '<em><b>Right Model</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute MATCH_MODEL__RIGHT_MODEL = eINSTANCE.getMatchModel_RightModel();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.match.metamodel.impl.Match2ElementsImpl <em>Match2 Elements</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.match.metamodel.impl.Match2ElementsImpl
		 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getMatch2Elements()
		 * @generated
		 */
		EClass MATCH2_ELEMENTS = eINSTANCE.getMatch2Elements();

		/**
		 * The meta object literal for the '<em><b>Left Element</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MATCH2_ELEMENTS__LEFT_ELEMENT = eINSTANCE.getMatch2Elements_LeftElement();

		/**
		 * The meta object literal for the '<em><b>Right Element</b></em>' reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MATCH2_ELEMENTS__RIGHT_ELEMENT = eINSTANCE.getMatch2Elements_RightElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.match.metamodel.impl.Match3ElementsImpl <em>Match3 Elements</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.match.metamodel.impl.Match3ElementsImpl
		 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getMatch3Elements()
		 * @generated
		 */
		EClass MATCH3_ELEMENTS = eINSTANCE.getMatch3Elements();

		/**
		 * The meta object literal for the '<em><b>Origin Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MATCH3_ELEMENTS__ORIGIN_ELEMENT = eINSTANCE.getMatch3Elements_OriginElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.match.metamodel.impl.UnmatchElementImpl <em>Unmatch Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.match.metamodel.impl.UnmatchElementImpl
		 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getUnmatchElement()
		 * @generated
		 */
		EClass UNMATCH_ELEMENT = eINSTANCE.getUnmatchElement();

		/**
		 * The meta object literal for the '<em><b>Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UNMATCH_ELEMENT__ELEMENT = eINSTANCE.getUnmatchElement_Element();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.match.metamodel.impl.RemoteUnmatchElementImpl <em>Remote Unmatch Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.match.metamodel.impl.RemoteUnmatchElementImpl
		 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getRemoteUnmatchElement()
		 * @generated
		 */
		EClass REMOTE_UNMATCH_ELEMENT = eINSTANCE.getRemoteUnmatchElement();

	}

} // MatchPackage
