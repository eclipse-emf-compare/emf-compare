/**
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.diagram.internal.extensions.impl;

import org.eclipse.emf.compare.ComparePackage;

import org.eclipse.emf.compare.diagram.internal.extensions.CoordinatesChange;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.diagram.internal.extensions.EdgeChange;
import org.eclipse.emf.compare.diagram.internal.extensions.ExtensionsFactory;
import org.eclipse.emf.compare.diagram.internal.extensions.ExtensionsPackage;
import org.eclipse.emf.compare.diagram.internal.extensions.Hide;
import org.eclipse.emf.compare.diagram.internal.extensions.NodeChange;
import org.eclipse.emf.compare.diagram.internal.extensions.Show;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ExtensionsPackageImpl extends EPackageImpl implements ExtensionsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass showEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass hideEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nodeChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass coordinatesChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass edgeChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diagramDiffEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.ExtensionsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ExtensionsPackageImpl() {
		super(eNS_URI, ExtensionsFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link ExtensionsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ExtensionsPackage init() {
		if (isInited) return (ExtensionsPackage)EPackage.Registry.INSTANCE.getEPackage(ExtensionsPackage.eNS_URI);

		// Obtain or create and register package
		ExtensionsPackageImpl theExtensionsPackage = (ExtensionsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ExtensionsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ExtensionsPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		ComparePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theExtensionsPackage.createPackageContents();

		// Initialize created meta-data
		theExtensionsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theExtensionsPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ExtensionsPackage.eNS_URI, theExtensionsPackage);
		return theExtensionsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getShow() {
		return showEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getHide() {
		return hideEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNodeChange() {
		return nodeChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCoordinatesChange() {
		return coordinatesChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEdgeChange() {
		return edgeChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiagramDiff() {
		return diagramDiffEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiagramDiff_SemanticDiff() {
		return (EReference)diagramDiffEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiagramDiff_View() {
		return (EReference)diagramDiffEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExtensionsFactory getExtensionsFactory() {
		return (ExtensionsFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		showEClass = createEClass(SHOW);

		hideEClass = createEClass(HIDE);

		nodeChangeEClass = createEClass(NODE_CHANGE);

		coordinatesChangeEClass = createEClass(COORDINATES_CHANGE);

		edgeChangeEClass = createEClass(EDGE_CHANGE);

		diagramDiffEClass = createEClass(DIAGRAM_DIFF);
		createEReference(diagramDiffEClass, DIAGRAM_DIFF__SEMANTIC_DIFF);
		createEReference(diagramDiffEClass, DIAGRAM_DIFF__VIEW);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		ComparePackage theComparePackage = (ComparePackage)EPackage.Registry.INSTANCE.getEPackage(ComparePackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		showEClass.getESuperTypes().add(this.getDiagramDiff());
		hideEClass.getESuperTypes().add(this.getDiagramDiff());
		nodeChangeEClass.getESuperTypes().add(this.getDiagramDiff());
		coordinatesChangeEClass.getESuperTypes().add(this.getNodeChange());
		edgeChangeEClass.getESuperTypes().add(this.getDiagramDiff());
		diagramDiffEClass.getESuperTypes().add(theComparePackage.getDiff());

		// Initialize classes and features; add operations and parameters
		initEClass(showEClass, Show.class, "Show", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(hideEClass, Hide.class, "Hide", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(nodeChangeEClass, NodeChange.class, "NodeChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(coordinatesChangeEClass, CoordinatesChange.class, "CoordinatesChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(edgeChangeEClass, EdgeChange.class, "EdgeChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(diagramDiffEClass, DiagramDiff.class, "DiagramDiff", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDiagramDiff_SemanticDiff(), theComparePackage.getDiff(), null, "semanticDiff", null, 0, 1, DiagramDiff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDiagramDiff_View(), theEcorePackage.getEObject(), null, "view", null, 0, 1, DiagramDiff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //ExtensionsPackageImpl
