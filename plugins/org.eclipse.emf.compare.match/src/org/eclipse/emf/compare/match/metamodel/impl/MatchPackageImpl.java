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
package org.eclipse.emf.compare.match.metamodel.impl;

import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.Match3Element;
import org.eclipse.emf.compare.match.metamodel.MatchElement;
import org.eclipse.emf.compare.match.metamodel.MatchFactory;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.metamodel.MatchPackage;
import org.eclipse.emf.compare.match.metamodel.RemoteUnMatchElement;
import org.eclipse.emf.compare.match.metamodel.UnMatchElement;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * @generated
 */
public class MatchPackageImpl extends EPackageImpl implements MatchPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass match2ElementsEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass match3ElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass matchElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass matchModelEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass remoteUnMatchElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass unMatchElementEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.emf.compare.match.metamodel.MatchPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MatchPackageImpl() {
		super(eNS_URI, MatchFactory.eINSTANCE);
	}

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which
	 * it depends. Simple dependencies are satisfied by calling this method on all dependent packages before
	 * doing anything else. This method drives initialization for interdependent packages directly, in
	 * parallel with this package, itself.
	 * <p>
	 * Of this package and its interdependencies, all packages which have not yet been registered by their URI
	 * values are first created and registered. The packages are then initialized in two steps: meta-model
	 * objects for all of the packages are created before any are initialized, since one package's meta-model
	 * objects may refer to those of another.
	 * <p>
	 * Invocation of this method will not affect any packages that have already been initialized. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static MatchPackage init() {
		if (isInited)
			return (MatchPackage)EPackage.Registry.INSTANCE.getEPackage(MatchPackage.eNS_URI);

		// Obtain or create and register package
		MatchPackageImpl theMatchPackage = (MatchPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof MatchPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(eNS_URI)
				: new MatchPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theMatchPackage.createPackageContents();

		// Initialize created meta-data
		theMatchPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theMatchPackage.freeze();

		return theMatchPackage;
	}

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		matchModelEClass = createEClass(MATCH_MODEL);
		createEAttribute(matchModelEClass, MATCH_MODEL__LEFT_MODEL);
		createEAttribute(matchModelEClass, MATCH_MODEL__RIGHT_MODEL);
		createEAttribute(matchModelEClass, MATCH_MODEL__ORIGIN_MODEL);
		createEReference(matchModelEClass, MATCH_MODEL__MATCHED_ELEMENTS);
		createEReference(matchModelEClass, MATCH_MODEL__UN_MATCHED_ELEMENTS);

		matchElementEClass = createEClass(MATCH_ELEMENT);
		createEAttribute(matchElementEClass, MATCH_ELEMENT__SIMILARITY);
		createEReference(matchElementEClass, MATCH_ELEMENT__SUB_MATCH_ELEMENTS);

		match2ElementsEClass = createEClass(MATCH2_ELEMENTS);
		createEReference(match2ElementsEClass, MATCH2_ELEMENTS__LEFT_ELEMENT);
		createEReference(match2ElementsEClass, MATCH2_ELEMENTS__RIGHT_ELEMENT);

		match3ElementEClass = createEClass(MATCH3_ELEMENT);
		createEReference(match3ElementEClass, MATCH3_ELEMENT__ORIGIN_ELEMENT);

		unMatchElementEClass = createEClass(UN_MATCH_ELEMENT);
		createEReference(unMatchElementEClass, UN_MATCH_ELEMENT__ELEMENT);

		remoteUnMatchElementEClass = createEClass(REMOTE_UN_MATCH_ELEMENT);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMatch2Elements() {
		return match2ElementsEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMatch2Elements_LeftElement() {
		return (EReference)match2ElementsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMatch2Elements_RightElement() {
		return (EReference)match2ElementsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMatch3Element() {
		return match3ElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMatch3Element_OriginElement() {
		return (EReference)match3ElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMatchElement() {
		return matchElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMatchElement_Similarity() {
		return (EAttribute)matchElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMatchElement_SubMatchElements() {
		return (EReference)matchElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public MatchFactory getMatchFactory() {
		return (MatchFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMatchModel() {
		return matchModelEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMatchModel_LeftModel() {
		return (EAttribute)matchModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMatchModel_MatchedElements() {
		return (EReference)matchModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMatchModel_OriginModel() {
		return (EAttribute)matchModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMatchModel_RightModel() {
		return (EAttribute)matchModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMatchModel_UnMatchedElements() {
		return (EReference)matchModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoteUnMatchElement() {
		return remoteUnMatchElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUnMatchElement() {
		return unMatchElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUnMatchElement_Element() {
		return (EReference)unMatchElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		match2ElementsEClass.getESuperTypes().add(this.getMatchElement());
		match3ElementEClass.getESuperTypes().add(this.getMatch2Elements());
		remoteUnMatchElementEClass.getESuperTypes().add(this.getUnMatchElement());

		// Initialize classes and features; add operations and parameters
		initEClass(matchModelEClass, MatchModel.class,
				"MatchModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(
				getMatchModel_LeftModel(),
				ecorePackage.getEString(),
				"leftModel", null, 1, 1, MatchModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, !IS_ORDERED); //$NON-NLS-1$
		initEAttribute(
				getMatchModel_RightModel(),
				ecorePackage.getEString(),
				"rightModel", null, 1, 1, MatchModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, !IS_ORDERED); //$NON-NLS-1$
		initEAttribute(
				getMatchModel_OriginModel(),
				ecorePackage.getEString(),
				"originModel", null, 1, 1, MatchModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, !IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getMatchModel_MatchedElements(),
				this.getMatchElement(),
				null,
				"matchedElements", null, 0, -1, MatchModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getMatchModel_UnMatchedElements(),
				this.getUnMatchElement(),
				null,
				"unMatchedElements", null, 0, -1, MatchModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(matchElementEClass, MatchElement.class,
				"MatchElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(
				getMatchElement_Similarity(),
				ecorePackage.getEDouble(),
				"similarity", null, 1, 1, MatchElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, !IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getMatchElement_SubMatchElements(),
				this.getMatchElement(),
				null,
				"subMatchElements", null, 0, -1, MatchElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(match2ElementsEClass, Match2Elements.class,
				"Match2Elements", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getMatch2Elements_LeftElement(),
				ecorePackage.getEObject(),
				null,
				"leftElement", null, 1, 1, Match2Elements.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getMatch2Elements_RightElement(),
				ecorePackage.getEObject(),
				null,
				"rightElement", null, 1, 1, Match2Elements.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED); //$NON-NLS-1$

		initEClass(match3ElementEClass, Match3Element.class,
				"Match3Element", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getMatch3Element_OriginElement(),
				ecorePackage.getEObject(),
				null,
				"originElement", null, 1, 1, Match3Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED); //$NON-NLS-1$

		initEClass(unMatchElementEClass, UnMatchElement.class,
				"UnMatchElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getUnMatchElement_Element(),
				ecorePackage.getEObject(),
				null,
				"element", null, 1, 1, UnMatchElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED); //$NON-NLS-1$

		initEClass(remoteUnMatchElementEClass, RemoteUnMatchElement.class,
				"RemoteUnMatchElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		// Create resource
		createResource(eNS_URI);
	}

} // MatchPackageImpl
