/*******************************************************************************
 * Copyright (c) 2008, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.examples.addressbook.addressbook.impl;

import org.eclipse.emf.compare.examples.addressbook.addressbook.AddressBook;
import org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookFactory;
import org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage;
import org.eclipse.emf.compare.examples.addressbook.addressbook.BookVersion;
import org.eclipse.emf.compare.examples.addressbook.addressbook.Contact;
import org.eclipse.emf.compare.examples.addressbook.addressbook.Electronic;
import org.eclipse.emf.compare.examples.addressbook.addressbook.Office;
import org.eclipse.emf.compare.examples.addressbook.addressbook.People;
import org.eclipse.emf.compare.examples.addressbook.addressbook.Repository;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class AddressbookPackageImpl extends EPackageImpl implements AddressbookPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass contactEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass electronicEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass officeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass peopleEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass addressBookEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass repositoryEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass bookVersionEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory method {@link #init init()},
	 * which also performs initialization of the package, or returns the registered package, if one already
	 * exists. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private AddressbookPackageImpl() {
		super(eNS_URI, AddressbookFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it
	 * depends. Simple dependencies are satisfied by calling this method on all dependent packages before
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
	public static AddressbookPackage init() {
		if (isInited)
			return (AddressbookPackage)EPackage.Registry.INSTANCE.getEPackage(AddressbookPackage.eNS_URI);

		// Obtain or create and register package
		final AddressbookPackageImpl theAddressbookPackage = (AddressbookPackageImpl)(EPackage.Registry.INSTANCE
				.getEPackage(eNS_URI) instanceof AddressbookPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(eNS_URI) : new AddressbookPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theAddressbookPackage.createPackageContents();

		// Initialize created meta-data
		theAddressbookPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theAddressbookPackage.freeze();

		return theAddressbookPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getContact() {
		return contactEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getElectronic() {
		return electronicEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getElectronic_Email() {
		return (EAttribute)electronicEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getElectronic_Website() {
		return (EAttribute)electronicEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getOffice() {
		return officeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getOffice_Company() {
		return (EAttribute)officeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getPeople() {
		return peopleEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPeople_Name() {
		return (EAttribute)peopleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getPeople_Contacts() {
		return (EReference)peopleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getAddressBook() {
		return addressBookEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAddressBook_Peoples() {
		return (EReference)addressBookEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getRepository() {
		return repositoryEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getRepository_Head() {
		return (EReference)repositoryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getRepository_History() {
		return (EReference)repositoryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getBookVersion() {
		return bookVersionEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getBookVersion_Id() {
		return (EAttribute)bookVersionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getBookVersion_Book() {
		return (EReference)bookVersionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AddressbookFactory getAddressbookFactory() {
		return (AddressbookFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is guarded to have no affect on any
	 * invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		contactEClass = createEClass(CONTACT);

		electronicEClass = createEClass(ELECTRONIC);
		createEAttribute(electronicEClass, ELECTRONIC__EMAIL);
		createEAttribute(electronicEClass, ELECTRONIC__WEBSITE);

		officeEClass = createEClass(OFFICE);
		createEAttribute(officeEClass, OFFICE__COMPANY);

		peopleEClass = createEClass(PEOPLE);
		createEAttribute(peopleEClass, PEOPLE__NAME);
		createEReference(peopleEClass, PEOPLE__CONTACTS);

		addressBookEClass = createEClass(ADDRESS_BOOK);
		createEReference(addressBookEClass, ADDRESS_BOOK__PEOPLES);

		repositoryEClass = createEClass(REPOSITORY);
		createEReference(repositoryEClass, REPOSITORY__HEAD);
		createEReference(repositoryEClass, REPOSITORY__HISTORY);

		bookVersionEClass = createEClass(BOOK_VERSION);
		createEAttribute(bookVersionEClass, BOOK_VERSION__ID);
		createEReference(bookVersionEClass, BOOK_VERSION__BOOK);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This method is guarded to have no affect
	 * on any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
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
		electronicEClass.getESuperTypes().add(this.getContact());
		officeEClass.getESuperTypes().add(this.getContact());

		// Initialize classes and features; add operations and parameters
		initEClass(contactEClass, Contact.class, "Contact", IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);

		initEClass(electronicEClass, Electronic.class, "Electronic", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getElectronic_Email(), ecorePackage.getEString(), "email", null, 0, 1,
				Electronic.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElectronic_Website(), ecorePackage.getEString(), "website", null, 0, 1,
				Electronic.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(officeEClass, Office.class, "Office", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getOffice_Company(), ecorePackage.getEString(), "company", null, 0, 1, Office.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);

		initEClass(peopleEClass, People.class, "People", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPeople_Name(), ecorePackage.getEString(), "name", null, 1, 1, People.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getPeople_Contacts(), this.getContact(), null, "contacts", null, 0, -1, People.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(addressBookEClass, AddressBook.class, "AddressBook", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAddressBook_Peoples(), this.getPeople(), null, "peoples", null, 0, -1,
				AddressBook.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(repositoryEClass, Repository.class, "Repository", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRepository_Head(), this.getAddressBook(), null, "head", null, 1, 1,
				Repository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRepository_History(), this.getBookVersion(), null, "history", null, 0, -1,
				Repository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		final EOperation op = addEOperation(repositoryEClass, this.getAddressBook(), "checkout", 0, 1,
				IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEInt(), "id", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(repositoryEClass, null, "checkin", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(bookVersionEClass, BookVersion.class, "BookVersion", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBookVersion_Id(), ecorePackage.getEInt(), "id", null, 1, 1, BookVersion.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getBookVersion_Book(), this.getAddressBook(), null, "book", null, 1, 1,
				BookVersion.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} // AddressbookPackageImpl
