/**
 * <copyright>
 * </copyright>
 *
 * $Id: AddressBookImpl.java,v 1.1 2008/04/11 14:56:46 cbrun Exp $
 */
package org.eclipse.emf.compare.examples.addressbook.addressbook.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.examples.addressbook.addressbook.AddressBook;
import org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage;
import org.eclipse.emf.compare.examples.addressbook.addressbook.People;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Address Book</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.AddressBookImpl#getPeoples <em>Peoples</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AddressBookImpl extends EObjectImpl implements AddressBook {
    /**
     * The cached value of the '{@link #getPeoples() <em>Peoples</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPeoples()
     * @generated
     * @ordered
     */
    protected EList<People> peoples;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AddressBookImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return AddressbookPackage.Literals.ADDRESS_BOOK;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<People> getPeoples() {
        if (peoples == null) {
            peoples = new EObjectContainmentEList<People>(People.class, this, AddressbookPackage.ADDRESS_BOOK__PEOPLES);
        }
        return peoples;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case AddressbookPackage.ADDRESS_BOOK__PEOPLES:
                return ((InternalEList<?>)getPeoples()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case AddressbookPackage.ADDRESS_BOOK__PEOPLES:
                return getPeoples();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case AddressbookPackage.ADDRESS_BOOK__PEOPLES:
                getPeoples().clear();
                getPeoples().addAll((Collection<? extends People>)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case AddressbookPackage.ADDRESS_BOOK__PEOPLES:
                getPeoples().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case AddressbookPackage.ADDRESS_BOOK__PEOPLES:
                return peoples != null && !peoples.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //AddressBookImpl
