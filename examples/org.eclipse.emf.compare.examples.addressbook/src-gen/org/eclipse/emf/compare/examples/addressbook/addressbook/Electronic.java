/**
 * <copyright>
 * </copyright>
 *
 * $Id: Electronic.java,v 1.1 2008/04/11 14:56:48 cbrun Exp $
 */
package org.eclipse.emf.compare.examples.addressbook.addressbook;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Electronic</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.examples.addressbook.addressbook.Electronic#getEmail <em>Email</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.examples.addressbook.addressbook.Electronic#getWebsite <em>Website</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage#getElectronic()
 * @model
 * @generated
 */
public interface Electronic extends Contact {
    /**
     * Returns the value of the '<em><b>Email</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Email</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Email</em>' attribute.
     * @see #setEmail(String)
     * @see org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage#getElectronic_Email()
     * @model
     * @generated
     */
    String getEmail();

    /**
     * Sets the value of the '{@link org.eclipse.emf.compare.examples.addressbook.addressbook.Electronic#getEmail <em>Email</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Email</em>' attribute.
     * @see #getEmail()
     * @generated
     */
    void setEmail(String value);

    /**
     * Returns the value of the '<em><b>Website</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Website</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Website</em>' attribute.
     * @see #setWebsite(String)
     * @see org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage#getElectronic_Website()
     * @model
     * @generated
     */
    String getWebsite();

    /**
     * Sets the value of the '{@link org.eclipse.emf.compare.examples.addressbook.addressbook.Electronic#getWebsite <em>Website</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Website</em>' attribute.
     * @see #getWebsite()
     * @generated
     */
    void setWebsite(String value);

} // Electronic
