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
package org.eclipse.emf.compare.examples.addressbook.addressbook;

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
 * 
 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookFactory
 * @model kind="package"
 * @generated
 */
public interface AddressbookPackage extends EPackage {
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "addressbook";

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare/examples/addressbook";

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "addressbook";

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	AddressbookPackage eINSTANCE = org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressbookPackageImpl
			.init();

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.ContactImpl <em>Contact</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.ContactImpl
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressbookPackageImpl#getContact()
	 * @generated
	 */
	int CONTACT = 0;

	/**
	 * The number of structural features of the '<em>Contact</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTACT_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.ElectronicImpl
	 * <em>Electronic</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.ElectronicImpl
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressbookPackageImpl#getElectronic()
	 * @generated
	 */
	int ELECTRONIC = 1;

	/**
	 * The feature id for the '<em><b>Email</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ELECTRONIC__EMAIL = CONTACT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Website</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ELECTRONIC__WEBSITE = CONTACT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Electronic</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ELECTRONIC_FEATURE_COUNT = CONTACT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.OfficeImpl <em>Office</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.OfficeImpl
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressbookPackageImpl#getOffice()
	 * @generated
	 */
	int OFFICE = 2;

	/**
	 * The feature id for the '<em><b>Company</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int OFFICE__COMPANY = CONTACT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Office</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int OFFICE_FEATURE_COUNT = CONTACT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.PeopleImpl <em>People</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.PeopleImpl
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressbookPackageImpl#getPeople()
	 * @generated
	 */
	int PEOPLE = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PEOPLE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Contacts</b></em>' containment reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PEOPLE__CONTACTS = 1;

	/**
	 * The number of structural features of the '<em>People</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PEOPLE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressBookImpl
	 * <em>Address Book</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressBookImpl
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressbookPackageImpl#getAddressBook()
	 * @generated
	 */
	int ADDRESS_BOOK = 4;

	/**
	 * The feature id for the '<em><b>Peoples</b></em>' containment reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADDRESS_BOOK__PEOPLES = 0;

	/**
	 * The number of structural features of the '<em>Address Book</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ADDRESS_BOOK_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.RepositoryImpl
	 * <em>Repository</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.RepositoryImpl
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressbookPackageImpl#getRepository()
	 * @generated
	 */
	int REPOSITORY = 5;

	/**
	 * The feature id for the '<em><b>Head</b></em>' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REPOSITORY__HEAD = 0;

	/**
	 * The feature id for the '<em><b>History</b></em>' containment reference list. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REPOSITORY__HISTORY = 1;

	/**
	 * The number of structural features of the '<em>Repository</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REPOSITORY_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.BookVersionImpl
	 * <em>Book Version</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.BookVersionImpl
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressbookPackageImpl#getBookVersion()
	 * @generated
	 */
	int BOOK_VERSION = 6;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BOOK_VERSION__ID = 0;

	/**
	 * The feature id for the '<em><b>Book</b></em>' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BOOK_VERSION__BOOK = 1;

	/**
	 * The number of structural features of the '<em>Book Version</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BOOK_VERSION_FEATURE_COUNT = 2;

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.Contact <em>Contact</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Contact</em>'.
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.Contact
	 * @generated
	 */
	EClass getContact();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.Electronic <em>Electronic</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Electronic</em>'.
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.Electronic
	 * @generated
	 */
	EClass getElectronic();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.Electronic#getEmail <em>Email</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Email</em>'.
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.Electronic#getEmail()
	 * @see #getElectronic()
	 * @generated
	 */
	EAttribute getElectronic_Email();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.Electronic#getWebsite <em>Website</em>}
	 * '. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Website</em>'.
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.Electronic#getWebsite()
	 * @see #getElectronic()
	 * @generated
	 */
	EAttribute getElectronic_Website();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.Office <em>Office</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Office</em>'.
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.Office
	 * @generated
	 */
	EClass getOffice();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.Office#getCompany <em>Company</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Company</em>'.
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.Office#getCompany()
	 * @see #getOffice()
	 * @generated
	 */
	EAttribute getOffice_Company();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.People <em>People</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>People</em>'.
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.People
	 * @generated
	 */
	EClass getPeople();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.People#getName <em>Name</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.People#getName()
	 * @see #getPeople()
	 * @generated
	 */
	EAttribute getPeople_Name();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.People#getContacts <em>Contacts</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Contacts</em>'.
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.People#getContacts()
	 * @see #getPeople()
	 * @generated
	 */
	EReference getPeople_Contacts();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.AddressBook <em>Address Book</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Address Book</em>'.
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.AddressBook
	 * @generated
	 */
	EClass getAddressBook();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.AddressBook#getPeoples
	 * <em>Peoples</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Peoples</em>'.
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.AddressBook#getPeoples()
	 * @see #getAddressBook()
	 * @generated
	 */
	EReference getAddressBook_Peoples();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.Repository <em>Repository</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Repository</em>'.
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.Repository
	 * @generated
	 */
	EClass getRepository();

	/**
	 * Returns the meta object for the containment reference '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.Repository#getHead <em>Head</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Head</em>'.
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.Repository#getHead()
	 * @see #getRepository()
	 * @generated
	 */
	EReference getRepository_Head();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.Repository#getHistory <em>History</em>}
	 * '. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>History</em>'.
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.Repository#getHistory()
	 * @see #getRepository()
	 * @generated
	 */
	EReference getRepository_History();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.BookVersion <em>Book Version</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Book Version</em>'.
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.BookVersion
	 * @generated
	 */
	EClass getBookVersion();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.BookVersion#getId <em>Id</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.BookVersion#getId()
	 * @see #getBookVersion()
	 * @generated
	 */
	EAttribute getBookVersion_Id();

	/**
	 * Returns the meta object for the containment reference '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.BookVersion#getBook <em>Book</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Book</em>'.
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.BookVersion#getBook()
	 * @see #getBookVersion()
	 * @generated
	 */
	EReference getBookVersion_Book();

	/**
	 * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AddressbookFactory getAddressbookFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.ContactImpl <em>Contact</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.ContactImpl
		 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressbookPackageImpl#getContact()
		 * @generated
		 */
		EClass CONTACT = eINSTANCE.getContact();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.ElectronicImpl
		 * <em>Electronic</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.ElectronicImpl
		 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressbookPackageImpl#getElectronic()
		 * @generated
		 */
		EClass ELECTRONIC = eINSTANCE.getElectronic();

		/**
		 * The meta object literal for the '<em><b>Email</b></em>' attribute feature. <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ELECTRONIC__EMAIL = eINSTANCE.getElectronic_Email();

		/**
		 * The meta object literal for the '<em><b>Website</b></em>' attribute feature. <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ELECTRONIC__WEBSITE = eINSTANCE.getElectronic_Website();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.OfficeImpl <em>Office</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.OfficeImpl
		 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressbookPackageImpl#getOffice()
		 * @generated
		 */
		EClass OFFICE = eINSTANCE.getOffice();

		/**
		 * The meta object literal for the '<em><b>Company</b></em>' attribute feature. <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute OFFICE__COMPANY = eINSTANCE.getOffice_Company();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.PeopleImpl <em>People</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.PeopleImpl
		 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressbookPackageImpl#getPeople()
		 * @generated
		 */
		EClass PEOPLE = eINSTANCE.getPeople();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature. <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PEOPLE__NAME = eINSTANCE.getPeople_Name();

		/**
		 * The meta object literal for the '<em><b>Contacts</b></em>' containment reference list feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PEOPLE__CONTACTS = eINSTANCE.getPeople_Contacts();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressBookImpl
		 * <em>Address Book</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressBookImpl
		 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressbookPackageImpl#getAddressBook()
		 * @generated
		 */
		EClass ADDRESS_BOOK = eINSTANCE.getAddressBook();

		/**
		 * The meta object literal for the '<em><b>Peoples</b></em>' containment reference list feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ADDRESS_BOOK__PEOPLES = eINSTANCE.getAddressBook_Peoples();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.RepositoryImpl
		 * <em>Repository</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.RepositoryImpl
		 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressbookPackageImpl#getRepository()
		 * @generated
		 */
		EClass REPOSITORY = eINSTANCE.getRepository();

		/**
		 * The meta object literal for the '<em><b>Head</b></em>' containment reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference REPOSITORY__HEAD = eINSTANCE.getRepository_Head();

		/**
		 * The meta object literal for the '<em><b>History</b></em>' containment reference list feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference REPOSITORY__HISTORY = eINSTANCE.getRepository_History();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.BookVersionImpl
		 * <em>Book Version</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.BookVersionImpl
		 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressbookPackageImpl#getBookVersion()
		 * @generated
		 */
		EClass BOOK_VERSION = eINSTANCE.getBookVersion();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature. <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute BOOK_VERSION__ID = eINSTANCE.getBookVersion_Id();

		/**
		 * The meta object literal for the '<em><b>Book</b></em>' containment reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference BOOK_VERSION__BOOK = eINSTANCE.getBookVersion_Book();

	}

} // AddressbookPackage
