/*******************************************************************************
 * Copyright (c) 2007, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.examples.export.library;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Member</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.examples.export.library.Member#getName <em>Name</em>}</li>
 * <li>{@link org.eclipse.emf.compare.examples.export.library.Member#getId <em>Id</em>}</li>
 * <li>{@link org.eclipse.emf.compare.examples.export.library.Member#getBorrowedBooks <em>Borrowed Books</em>}
 * </li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.compare.examples.export.library.LibraryPackage#getMember()
 * @model
 * @generated
 */
public interface Member extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.emf.compare.examples.export.library.LibraryPackage#getMember_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.examples.export.library.Member#getName
	 * <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(int)
	 * @see org.eclipse.emf.compare.examples.export.library.LibraryPackage#getMember_Id()
	 * @model
	 * @generated
	 */
	int getId();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.examples.export.library.Member#getId <em>Id</em>}
	 * ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(int value);

	/**
	 * Returns the value of the '<em><b>Borrowed Books</b></em>' reference list. The list contents are of type
	 * {@link org.eclipse.emf.compare.examples.export.library.Book}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Borrowed Books</em>' reference list isn't clear, there really should be more
	 * of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Borrowed Books</em>' reference list.
	 * @see org.eclipse.emf.compare.examples.export.library.LibraryPackage#getMember_BorrowedBooks()
	 * @model
	 * @generated
	 */
	EList<Book> getBorrowedBooks();

} // Member
