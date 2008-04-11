/**
 * <copyright>
 * </copyright>
 *
 * $Id: AddressBook.java,v 1.1 2008/04/11 14:56:48 cbrun Exp $
 */
package org.eclipse.emf.compare.examples.addressbook.addressbook;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Address Book</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.examples.addressbook.addressbook.AddressBook#getPeoples <em>Peoples</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage#getAddressBook()
 * @model
 * @generated
 */
public interface AddressBook extends EObject {
    /**
     * Returns the value of the '<em><b>Peoples</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.emf.compare.examples.addressbook.addressbook.People}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Peoples</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Peoples</em>' containment reference list.
     * @see org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage#getAddressBook_Peoples()
     * @model containment="true"
     * @generated
     */
    EList<People> getPeoples();

} // AddressBook
