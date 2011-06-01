/**
 *  Copyright (c) 2011 Obeo.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *      Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.example.library.books.impl;

import org.eclipse.emf.compare.example.library.books.Book;
import org.eclipse.emf.compare.example.library.books.BooksFactory;
import org.eclipse.emf.compare.example.library.books.BooksPackage;
import org.eclipse.emf.compare.example.library.books.Catalog;

import org.eclipse.emf.compare.example.library.library.LibraryPackage;

import org.eclipse.emf.compare.example.library.library.impl.LibraryPackageImpl;

import org.eclipse.emf.compare.example.library.writers.WritersPackage;

import org.eclipse.emf.compare.example.library.writers.impl.WritersPackageImpl;

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
public class BooksPackageImpl extends EPackageImpl implements BooksPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = " Copyright (c) 2011 Obeo.\r\n All rights reserved. This program and the accompanying materials\r\n are made available under the terms of the Eclipse Public License v1.0\r\n which accompanies this distribution, and is available at\r\n http://www.eclipse.org/legal/epl-v10.html\r\n \r\n Contributors:\r\n     Obeo - initial API and implementation"; //$NON-NLS-1$

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
	private EClass bookEClass = null;

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
	 * @see org.eclipse.emf.compare.example.library.books.BooksPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private BooksPackageImpl() {
		super(eNS_URI, BooksFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link BooksPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static BooksPackage init() {
		if (isInited)
			return (BooksPackage)EPackage.Registry.INSTANCE.getEPackage(BooksPackage.eNS_URI);

		// Obtain or create and register package
		BooksPackageImpl theBooksPackage = (BooksPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof BooksPackageImpl ? EPackage.Registry.INSTANCE
				.get(eNS_URI) : new BooksPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		LibraryPackageImpl theLibraryPackage = (LibraryPackageImpl)(EPackage.Registry.INSTANCE
				.getEPackage(LibraryPackage.eNS_URI) instanceof LibraryPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(LibraryPackage.eNS_URI) : LibraryPackage.eINSTANCE);
		WritersPackageImpl theWritersPackage = (WritersPackageImpl)(EPackage.Registry.INSTANCE
				.getEPackage(WritersPackage.eNS_URI) instanceof WritersPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(WritersPackage.eNS_URI) : WritersPackage.eINSTANCE);

		// Create package meta-data objects
		theBooksPackage.createPackageContents();
		theLibraryPackage.createPackageContents();
		theWritersPackage.createPackageContents();

		// Initialize created meta-data
		theBooksPackage.initializePackageContents();
		theLibraryPackage.initializePackageContents();
		theWritersPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theBooksPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(BooksPackage.eNS_URI, theBooksPackage);
		return theBooksPackage;
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
	public EReference getCatalog_Books() {
		return (EReference)catalogEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBook() {
		return bookEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBook_Isbn() {
		return (EAttribute)bookEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBook_Title() {
		return (EAttribute)bookEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBook_Pages() {
		return (EAttribute)bookEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBook_Authors() {
		return (EReference)bookEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BooksFactory getBooksFactory() {
		return (BooksFactory)getEFactoryInstance();
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
		createEReference(catalogEClass, CATALOG__BOOKS);

		bookEClass = createEClass(BOOK);
		createEAttribute(bookEClass, BOOK__ISBN);
		createEAttribute(bookEClass, BOOK__TITLE);
		createEAttribute(bookEClass, BOOK__PAGES);
		createEReference(bookEClass, BOOK__AUTHORS);
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
		WritersPackage theWritersPackage = (WritersPackage)EPackage.Registry.INSTANCE
				.getEPackage(WritersPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(catalogEClass, Catalog.class,
				"Catalog", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(
				getCatalog_Books(),
				this.getBook(),
				null,
				"books", null, 0, -1, Catalog.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(bookEClass, Book.class, "Book", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(
				getBook_Isbn(),
				ecorePackage.getEString(),
				"isbn", null, 1, 1, Book.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(
				getBook_Title(),
				ecorePackage.getEString(),
				"title", null, 1, 1, Book.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(
				getBook_Pages(),
				ecorePackage.getEInt(),
				"pages", null, 0, 1, Book.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(
				getBook_Authors(),
				theWritersPackage.getWriter(),
				theWritersPackage.getWriter_Books(),
				"authors", null, 0, -1, Book.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		// Create resource
		createResource(eNS_URI);
	}

} //BooksPackageImpl
