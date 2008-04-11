/**
 * <copyright>
 * </copyright>
 *
 * $Id: AddressbookFactoryImpl.java,v 1.1 2008/04/11 14:56:46 cbrun Exp $
 */
package org.eclipse.emf.compare.examples.addressbook.addressbook.impl;

import org.eclipse.emf.compare.examples.addressbook.addressbook.*;
import org.eclipse.emf.compare.examples.addressbook.addressbook.AddressBook;
import org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookFactory;
import org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage;
import org.eclipse.emf.compare.examples.addressbook.addressbook.BookVersion;
import org.eclipse.emf.compare.examples.addressbook.addressbook.Electronic;
import org.eclipse.emf.compare.examples.addressbook.addressbook.Office;
import org.eclipse.emf.compare.examples.addressbook.addressbook.People;
import org.eclipse.emf.compare.examples.addressbook.addressbook.Repository;
import org.eclipse.emf.compare.examples.addressbook.addressbook.spec.RepositorySpec;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AddressbookFactoryImpl extends EFactoryImpl implements AddressbookFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static AddressbookFactory init() {
        try {
            AddressbookFactory theAddressbookFactory = (AddressbookFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/emf/compare/examples/addressbook"); 
            if (theAddressbookFactory != null) {
                return theAddressbookFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new AddressbookFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AddressbookFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case AddressbookPackage.ELECTRONIC: return createElectronic();
            case AddressbookPackage.OFFICE: return createOffice();
            case AddressbookPackage.PEOPLE: return createPeople();
            case AddressbookPackage.ADDRESS_BOOK: return createAddressBook();
            case AddressbookPackage.REPOSITORY: return createRepository();
            case AddressbookPackage.BOOK_VERSION: return createBookVersion();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Electronic createElectronic() {
        ElectronicImpl electronic = new ElectronicImpl();
        return electronic;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Office createOffice() {
        OfficeImpl office = new OfficeImpl();
        return office;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public People createPeople() {
        PeopleImpl people = new PeopleImpl();
        return people;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AddressBook createAddressBook() {
        AddressBookImpl addressBook = new AddressBookImpl();
        return addressBook;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @not-generated
     */
    public Repository createRepository() {
        RepositoryImpl repository = new RepositorySpec();
        return repository;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BookVersion createBookVersion() {
        BookVersionImpl bookVersion = new BookVersionImpl();
        return bookVersion;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AddressbookPackage getAddressbookPackage() {
        return (AddressbookPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static AddressbookPackage getPackage() {
        return AddressbookPackage.eINSTANCE;
    }

} //AddressbookFactoryImpl
