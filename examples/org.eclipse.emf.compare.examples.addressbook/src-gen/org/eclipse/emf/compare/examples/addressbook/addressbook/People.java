/**
 * <copyright>
 * </copyright>
 *
 * $Id: People.java,v 1.1 2008/04/11 14:56:48 cbrun Exp $
 */
package org.eclipse.emf.compare.examples.addressbook.addressbook;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>People</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.examples.addressbook.addressbook.People#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.examples.addressbook.addressbook.People#getContacts <em>Contacts</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage#getPeople()
 * @model
 * @generated
 */
public interface People extends EObject {
    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage#getPeople_Name()
     * @model required="true"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.eclipse.emf.compare.examples.addressbook.addressbook.People#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Contacts</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.emf.compare.examples.addressbook.addressbook.Contact}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Contacts</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Contacts</em>' containment reference list.
     * @see org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage#getPeople_Contacts()
     * @model containment="true"
     * @generated
     */
    EList<Contact> getContacts();

} // People
