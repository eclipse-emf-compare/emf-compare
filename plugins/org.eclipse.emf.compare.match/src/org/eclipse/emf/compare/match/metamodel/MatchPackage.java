/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
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
	int MATCH_ELEMENT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.match.metamodel.impl.MatchModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchModelImpl
	 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getMatchModel()
	 * @generated
	 */
	int MATCH_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Matched Elements</b></em>' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MATCH_MODEL__MATCHED_ELEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Unmatched Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_MODEL__UNMATCHED_ELEMENTS = 1;

	/**
	 * The feature id for the '<em><b>Left Roots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_MODEL__LEFT_ROOTS = 2;

	/**
	 * The feature id for the '<em><b>Right Roots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_MODEL__RIGHT_ROOTS = 3;

	/**
	 * The feature id for the '<em><b>Ancestor Roots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_MODEL__ANCESTOR_ROOTS = 4;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_MODEL_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.match.metamodel.impl.UnmatchModelImpl <em>Unmatch Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.match.metamodel.impl.UnmatchModelImpl
	 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getUnmatchModel()
	 * @generated
	 */
	int UNMATCH_MODEL = 1;

	/**
	 * The feature id for the '<em><b>Roots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNMATCH_MODEL__ROOTS = 0;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNMATCH_MODEL__REMOTE = 1;

	/**
	 * The feature id for the '<em><b>Side</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNMATCH_MODEL__SIDE = 2;

	/**
	 * The number of structural features of the '<em>Unmatch Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNMATCH_MODEL_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.match.metamodel.impl.Match2ElementsImpl <em>Match2 Elements</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.match.metamodel.impl.Match2ElementsImpl
	 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getMatch2Elements()
	 * @generated
	 */
	int MATCH2_ELEMENTS = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.match.metamodel.impl.Match3ElementsImpl <em>Match3 Elements</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.match.metamodel.impl.Match3ElementsImpl
	 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getMatch3Elements()
	 * @generated
	 */
	int MATCH3_ELEMENTS = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.match.metamodel.impl.UnmatchElementImpl <em>Unmatch Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.match.metamodel.impl.UnmatchElementImpl
	 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getUnmatchElement()
	 * @generated
	 */
	int UNMATCH_ELEMENT = 6;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.match.metamodel.impl.MatchResourceSetImpl <em>Resource Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchResourceSetImpl
	 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getMatchResourceSet()
	 * @generated
	 */
	int MATCH_RESOURCE_SET = 2;

	/**
	 * The feature id for the '<em><b>Match Models</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_RESOURCE_SET__MATCH_MODELS = 0;

	/**
	 * The feature id for the '<em><b>Unmatched Models</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_RESOURCE_SET__UNMATCHED_MODELS = 1;

	/**
	 * The number of structural features of the '<em>Resource Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MATCH_RESOURCE_SET_FEATURE_COUNT = 2;

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
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNMATCH_ELEMENT__ELEMENT = 0;

	/**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNMATCH_ELEMENT__CONFLICTING = 1;

	/**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNMATCH_ELEMENT__REMOTE = 2;

	/**
	 * The feature id for the '<em><b>Side</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNMATCH_ELEMENT__SIDE = 3;

	/**
	 * The number of structural features of the '<em>Unmatch Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNMATCH_ELEMENT_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.match.metamodel.Side <em>Side</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.compare.match.metamodel.Side
	 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getSide()
	 * @generated
	 */
	int SIDE = 7;

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
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.match.metamodel.UnmatchElement#isConflicting <em>Conflicting</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Conflicting</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.UnmatchElement#isConflicting()
	 * @see #getUnmatchElement()
	 * @generated
	 */
	EAttribute getUnmatchElement_Conflicting();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.match.metamodel.UnmatchElement#isRemote <em>Remote</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Remote</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.UnmatchElement#isRemote()
	 * @see #getUnmatchElement()
	 * @generated
	 */
	EAttribute getUnmatchElement_Remote();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.match.metamodel.UnmatchElement#getSide <em>Side</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Side</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.UnmatchElement#getSide()
	 * @see #getUnmatchElement()
	 * @generated
	 */
	EAttribute getUnmatchElement_Side();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.match.metamodel.MatchResourceSet <em>Resource Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource Set</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchResourceSet
	 * @generated
	 */
	EClass getMatchResourceSet();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.match.metamodel.MatchResourceSet#getMatchModels <em>Match Models</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Match Models</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchResourceSet#getMatchModels()
	 * @see #getMatchResourceSet()
	 * @generated
	 */
	EReference getMatchResourceSet_MatchModels();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.compare.match.metamodel.MatchResourceSet#getUnmatchedModels <em>Unmatched Models</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Unmatched Models</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchResourceSet#getUnmatchedModels()
	 * @see #getMatchResourceSet()
	 * @generated
	 */
	EReference getMatchResourceSet_UnmatchedModels();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.emf.compare.match.metamodel.Side <em>Side</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Side</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.Side
	 * @generated
	 */
	EEnum getSide();

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
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getLeftRoots <em>Left Roots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Left Roots</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchModel#getLeftRoots()
	 * @see #getMatchModel()
	 * @generated
	 */
	EReference getMatchModel_LeftRoots();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getRightRoots <em>Right Roots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Right Roots</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchModel#getRightRoots()
	 * @see #getMatchModel()
	 * @generated
	 */
	EReference getMatchModel_RightRoots();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.match.metamodel.MatchModel#getAncestorRoots <em>Ancestor Roots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Ancestor Roots</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.MatchModel#getAncestorRoots()
	 * @see #getMatchModel()
	 * @generated
	 */
	EReference getMatchModel_AncestorRoots();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.match.metamodel.UnmatchModel <em>Unmatch Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Unmatch Model</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.UnmatchModel
	 * @generated
	 */
	EClass getUnmatchModel();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.compare.match.metamodel.UnmatchModel#getRoots <em>Roots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Roots</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.UnmatchModel#getRoots()
	 * @see #getUnmatchModel()
	 * @generated
	 */
	EReference getUnmatchModel_Roots();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.match.metamodel.UnmatchModel#isRemote <em>Remote</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Remote</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.UnmatchModel#isRemote()
	 * @see #getUnmatchModel()
	 * @generated
	 */
	EAttribute getUnmatchModel_Remote();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.compare.match.metamodel.UnmatchModel#getSide <em>Side</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Side</em>'.
	 * @see org.eclipse.emf.compare.match.metamodel.UnmatchModel#getSide()
	 * @see #getUnmatchModel()
	 * @generated
	 */
	EAttribute getUnmatchModel_Side();

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
		 * The meta object literal for the '<em><b>Left Roots</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MATCH_MODEL__LEFT_ROOTS = eINSTANCE.getMatchModel_LeftRoots();

		/**
		 * The meta object literal for the '<em><b>Right Roots</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MATCH_MODEL__RIGHT_ROOTS = eINSTANCE.getMatchModel_RightRoots();

		/**
		 * The meta object literal for the '<em><b>Ancestor Roots</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MATCH_MODEL__ANCESTOR_ROOTS = eINSTANCE.getMatchModel_AncestorRoots();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.match.metamodel.impl.UnmatchModelImpl <em>Unmatch Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.match.metamodel.impl.UnmatchModelImpl
		 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getUnmatchModel()
		 * @generated
		 */
		EClass UNMATCH_MODEL = eINSTANCE.getUnmatchModel();

		/**
		 * The meta object literal for the '<em><b>Roots</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UNMATCH_MODEL__ROOTS = eINSTANCE.getUnmatchModel_Roots();

		/**
		 * The meta object literal for the '<em><b>Remote</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNMATCH_MODEL__REMOTE = eINSTANCE.getUnmatchModel_Remote();

		/**
		 * The meta object literal for the '<em><b>Side</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNMATCH_MODEL__SIDE = eINSTANCE.getUnmatchModel_Side();

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
		 * The meta object literal for the '<em><b>Conflicting</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNMATCH_ELEMENT__CONFLICTING = eINSTANCE.getUnmatchElement_Conflicting();

		/**
		 * The meta object literal for the '<em><b>Remote</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNMATCH_ELEMENT__REMOTE = eINSTANCE.getUnmatchElement_Remote();

		/**
		 * The meta object literal for the '<em><b>Side</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNMATCH_ELEMENT__SIDE = eINSTANCE.getUnmatchElement_Side();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.match.metamodel.impl.MatchResourceSetImpl <em>Resource Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchResourceSetImpl
		 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getMatchResourceSet()
		 * @generated
		 */
		EClass MATCH_RESOURCE_SET = eINSTANCE.getMatchResourceSet();

		/**
		 * The meta object literal for the '<em><b>Match Models</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MATCH_RESOURCE_SET__MATCH_MODELS = eINSTANCE.getMatchResourceSet_MatchModels();

		/**
		 * The meta object literal for the '<em><b>Unmatched Models</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MATCH_RESOURCE_SET__UNMATCHED_MODELS = eINSTANCE.getMatchResourceSet_UnmatchedModels();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.match.metamodel.Side <em>Side</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.compare.match.metamodel.Side
		 * @see org.eclipse.emf.compare.match.metamodel.impl.MatchPackageImpl#getSide()
		 * @generated
		 */
		EEnum SIDE = eINSTANCE.getSide();

	}

} // MatchPackage
