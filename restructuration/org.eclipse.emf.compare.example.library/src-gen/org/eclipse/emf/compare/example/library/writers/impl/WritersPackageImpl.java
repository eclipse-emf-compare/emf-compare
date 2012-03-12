/**
 *  Copyright (c) 2011, 2012 Obeo.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *      Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.example.library.writers.impl;

import org.eclipse.emf.compare.example.library.books.BooksPackage;

import org.eclipse.emf.compare.example.library.books.impl.BooksPackageImpl;

import org.eclipse.emf.compare.example.library.library.LibraryPackage;

import org.eclipse.emf.compare.example.library.library.impl.LibraryPackageImpl;

import org.eclipse.emf.compare.example.library.writers.Catalog;
import org.eclipse.emf.compare.example.library.writers.Writer;
import org.eclipse.emf.compare.example.library.writers.WritersFactory;
import org.eclipse.emf.compare.example.library.writers.WritersPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class WritersPackageImpl extends EPackageImpl implements WritersPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = " Copyright (c) 2011, 2012 Obeo.\r\n All rights reserved. This program and the accompanying materials\r\n are made available under the terms of the Eclipse Public License v1.0\r\n which accompanies this distribution, and is available at\r\n http://www.eclipse.org/legal/epl-v10.html\r\n \r\n Contributors:\r\n     Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass catalogEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass writerEClass = null;

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
	 * @see org.eclipse.emf.compare.example.library.writers.WritersPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private WritersPackageImpl() {
		super(eNS_URI, WritersFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link WritersPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static WritersPackage init() {
		if (isInited)
			return (WritersPackage)EPackage.Registry.INSTANCE.getEPackage(WritersPackage.eNS_URI);

		// Obtain or create and register package
		WritersPackageImpl theWritersPackage = (WritersPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof WritersPackageImpl ? EPackage.Registry.INSTANCE
				.get(eNS_URI) : new WritersPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		LibraryPackageImpl theLibraryPackage = (LibraryPackageImpl)(EPackage.Registry.INSTANCE
				.getEPackage(LibraryPackage.eNS_URI) instanceof LibraryPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(LibraryPackage.eNS_URI) : LibraryPackage.eINSTANCE);
		BooksPackageImpl theBooksPackage = (BooksPackageImpl)(EPackage.Registry.INSTANCE
				.getEPackage(BooksPackage.eNS_URI) instanceof BooksPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(BooksPackage.eNS_URI) : BooksPackage.eINSTANCE);

		// Create package meta-data objects
		theWritersPackage.createPackageContents();
		theLibraryPackage.createPackageContents();
		theBooksPackage.createPackageContents();

		// Initialize created meta-data
		theWritersPackage.initializePackageContents();
		theLibraryPackage.initializePackageContents();
		theBooksPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theWritersPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(WritersPackage.eNS_URI, theWritersPackage);
		return theWritersPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCatalog() {
		return catalogEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCatalog_Writers() {
		return (EReference)catalogEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getWriter() {
		return writerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWriter_Name() {
		return (EAttribute)writerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getWriter_Books() {
		return (EReference)writerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WritersFactory getWritersFactory() {
		return (WritersFactory)getEFactoryInstance();
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
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		catalogEClass = createEClass(CATALOG);
		createEReference(catalogEClass, CATALOG__WRITERS);

		writerEClass = createEClass(WRITER);
		createEAttribute(writerEClass, WRITER__NAME);
		createEReference(writerEClass, WRITER__BOOKS);
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
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		BooksPackage theBooksPackage = (BooksPackage)EPackage.Registry.INSTANCE
				.getEPackage(BooksPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(catalogEClass, Catalog.class,
				"Catalog", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getCatalog_Writers(),
				this.getWriter(),
				null,
				"writers", null, 0, -1, Catalog.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(writerEClass, Writer.class,
				"Writer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(
				getWriter_Name(),
				ecorePackage.getEString(),
				"name", null, 1, 1, Writer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getWriter_Books(),
				theBooksPackage.getBook(),
				theBooksPackage.getBook_Authors(),
				"books", null, 0, -1, Writer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		// Create resource
		createResource(eNS_URI);
	}

} //WritersPackageImpl
