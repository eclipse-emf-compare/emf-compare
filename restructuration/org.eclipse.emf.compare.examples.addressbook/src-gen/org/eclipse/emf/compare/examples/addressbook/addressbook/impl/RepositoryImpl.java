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
package org.eclipse.emf.compare.examples.addressbook.addressbook.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.examples.addressbook.addressbook.AddressBook;
import org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage;
import org.eclipse.emf.compare.examples.addressbook.addressbook.BookVersion;
import org.eclipse.emf.compare.examples.addressbook.addressbook.Repository;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Repository</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.RepositoryImpl#getHead <em>Head
 * </em>}</li>
 * <li>{@link org.eclipse.emf.compare.examples.addressbook.addressbook.impl.RepositoryImpl#getHistory <em>
 * History</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class RepositoryImpl extends EObjectImpl implements Repository {
	/**
	 * The cached value of the '{@link #getHead() <em>Head</em>}' containment reference. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #getHead()
	 * @generated
	 * @ordered
	 */
	protected AddressBook head;

	/**
	 * The cached value of the '{@link #getHistory() <em>History</em>}' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getHistory()
	 * @generated
	 * @ordered
	 */
	protected EList<BookVersion> history;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected RepositoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AddressbookPackage.Literals.REPOSITORY;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AddressBook getHead() {
		return head;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetHead(AddressBook newHead, NotificationChain msgs) {
		final AddressBook oldHead = head;
		head = newHead;
		if (eNotificationRequired()) {
			final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					AddressbookPackage.REPOSITORY__HEAD, oldHead, newHead);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setHead(AddressBook newHead) {
		if (newHead != head) {
			NotificationChain msgs = null;
			if (head != null) {
				msgs = ((InternalEObject)head).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
						- AddressbookPackage.REPOSITORY__HEAD, null, msgs);
			}
			if (newHead != null) {
				msgs = ((InternalEObject)newHead).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
						- AddressbookPackage.REPOSITORY__HEAD, null, msgs);
			}
			msgs = basicSetHead(newHead, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AddressbookPackage.REPOSITORY__HEAD,
					newHead, newHead));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<BookVersion> getHistory() {
		if (history == null) {
			history = new EObjectContainmentEList<BookVersion>(BookVersion.class, this,
					AddressbookPackage.REPOSITORY__HISTORY);
		}
		return history;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AddressBook checkout(int id) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void checkin() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AddressbookPackage.REPOSITORY__HEAD:
				return basicSetHead(null, msgs);
			case AddressbookPackage.REPOSITORY__HISTORY:
				return ((InternalEList<?>)getHistory()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AddressbookPackage.REPOSITORY__HEAD:
				return getHead();
			case AddressbookPackage.REPOSITORY__HISTORY:
				return getHistory();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case AddressbookPackage.REPOSITORY__HEAD:
				setHead((AddressBook)newValue);
				return;
			case AddressbookPackage.REPOSITORY__HISTORY:
				getHistory().clear();
				getHistory().addAll((Collection<? extends BookVersion>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case AddressbookPackage.REPOSITORY__HEAD:
				setHead((AddressBook)null);
				return;
			case AddressbookPackage.REPOSITORY__HISTORY:
				getHistory().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case AddressbookPackage.REPOSITORY__HEAD:
				return head != null;
			case AddressbookPackage.REPOSITORY__HISTORY:
				return history != null && !history.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // RepositoryImpl
