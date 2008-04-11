/**
 * <copyright>
 * </copyright>
 *
 * $Id: Repository.java,v 1.1 2008/04/11 14:56:48 cbrun Exp $
 */
package org.eclipse.emf.compare.examples.addressbook.addressbook;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Repository</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.examples.addressbook.addressbook.Repository#getHead <em>Head</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.examples.addressbook.addressbook.Repository#getHistory <em>History</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage#getRepository()
 * @model
 * @generated
 */
public interface Repository extends EObject {
    /**
     * Returns the value of the '<em><b>Head</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Head</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Head</em>' containment reference.
     * @see #setHead(AddressBook)
     * @see org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage#getRepository_Head()
     * @model containment="true" required="true"
     * @generated
     */
    AddressBook getHead();

    /**
     * Sets the value of the '{@link org.eclipse.emf.compare.examples.addressbook.addressbook.Repository#getHead <em>Head</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Head</em>' containment reference.
     * @see #getHead()
     * @generated
     */
    void setHead(AddressBook value);

    /**
     * Returns the value of the '<em><b>History</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.emf.compare.examples.addressbook.addressbook.BookVersion}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>History</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>History</em>' containment reference list.
     * @see org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage#getRepository_History()
     * @model containment="true"
     * @generated
     */
    EList<BookVersion> getHistory();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model idRequired="true"
     * @generated
     */
    AddressBook checkout(int id);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    void checkin();

} // Repository
