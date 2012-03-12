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
package org.eclipse.emf.compare.examples.addressbook.addressbook;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Office</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.examples.addressbook.addressbook.Office#getCompany <em>Company</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage#getOffice()
 * @model
 * @generated
 */
public interface Office extends Contact {
	/**
	 * Returns the value of the '<em><b>Company</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Company</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Company</em>' attribute.
	 * @see #setCompany(String)
	 * @see org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage#getOffice_Company()
	 * @model
	 * @generated
	 */
	String getCompany();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.emf.compare.examples.addressbook.addressbook.Office#getCompany <em>Company</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Company</em>' attribute.
	 * @see #getCompany()
	 * @generated
	 */
	void setCompany(String value);

} // Office
