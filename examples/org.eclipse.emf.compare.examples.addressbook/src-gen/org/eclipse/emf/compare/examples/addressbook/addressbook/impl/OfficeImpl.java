/**
 * <copyright>
 * </copyright>
 *
 * $Id: OfficeImpl.java,v 1.1 2008/04/11 14:56:46 cbrun Exp $
 */
package org.eclipse.emf.compare.examples.addressbook.addressbook.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage;
import org.eclipse.emf.compare.examples.addressbook.addressbook.Office;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Office</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.OfficeImpl#getCompany <em>Company</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OfficeImpl extends ContactImpl implements Office {
    /**
     * The default value of the '{@link #getCompany() <em>Company</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCompany()
     * @generated
     * @ordered
     */
    protected static final String COMPANY_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCompany() <em>Company</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCompany()
     * @generated
     * @ordered
     */
    protected String company = COMPANY_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected OfficeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return AddressbookPackage.Literals.OFFICE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCompany() {
        return company;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCompany(String newCompany) {
        String oldCompany = company;
        company = newCompany;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AddressbookPackage.OFFICE__COMPANY, oldCompany, company));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case AddressbookPackage.OFFICE__COMPANY:
                return getCompany();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case AddressbookPackage.OFFICE__COMPANY:
                setCompany((String)newValue);
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
            case AddressbookPackage.OFFICE__COMPANY:
                setCompany(COMPANY_EDEFAULT);
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
            case AddressbookPackage.OFFICE__COMPANY:
                return COMPANY_EDEFAULT == null ? company != null : !COMPANY_EDEFAULT.equals(company);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (company: ");
        result.append(company);
        result.append(')');
        return result.toString();
    }

} //OfficeImpl
