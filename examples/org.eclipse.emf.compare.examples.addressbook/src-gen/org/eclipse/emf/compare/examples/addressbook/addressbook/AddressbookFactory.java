/**
 * <copyright>
 * </copyright>
 *
 * $Id: AddressbookFactory.java,v 1.1 2008/04/11 14:56:48 cbrun Exp $
 */
package org.eclipse.emf.compare.examples.addressbook.addressbook;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage
 * @generated
 */
public interface AddressbookFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    AddressbookFactory eINSTANCE = org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressbookFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Electronic</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Electronic</em>'.
     * @generated
     */
    Electronic createElectronic();

    /**
     * Returns a new object of class '<em>Office</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Office</em>'.
     * @generated
     */
    Office createOffice();

    /**
     * Returns a new object of class '<em>People</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>People</em>'.
     * @generated
     */
    People createPeople();

    /**
     * Returns a new object of class '<em>Address Book</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Address Book</em>'.
     * @generated
     */
    AddressBook createAddressBook();

    /**
     * Returns a new object of class '<em>Repository</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Repository</em>'.
     * @generated
     */
    Repository createRepository();

    /**
     * Returns a new object of class '<em>Book Version</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Book Version</em>'.
     * @generated
     */
    BookVersion createBookVersion();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    AddressbookPackage getAddressbookPackage();

} //AddressbookFactory
