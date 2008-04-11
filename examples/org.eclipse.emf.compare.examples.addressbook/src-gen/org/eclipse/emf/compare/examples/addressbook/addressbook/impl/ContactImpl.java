/**
 * <copyright>
 * </copyright>
 *
 * $Id: ContactImpl.java,v 1.1 2008/04/11 14:56:46 cbrun Exp $
 */
package org.eclipse.emf.compare.examples.addressbook.addressbook.impl;

import org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage;
import org.eclipse.emf.compare.examples.addressbook.addressbook.Contact;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Contact</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public abstract class ContactImpl extends EObjectImpl implements Contact {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ContactImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return AddressbookPackage.Literals.CONTACT;
    }

} //ContactImpl
