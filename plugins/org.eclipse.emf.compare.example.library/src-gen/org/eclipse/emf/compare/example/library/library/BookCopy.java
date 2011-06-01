/**
 *  Copyright (c) 2011 Obeo.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *      Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.example.library.library;

import org.eclipse.emf.compare.example.library.books.Book;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Book Copy</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.example.library.library.BookCopy#getBook <em>Book</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.example.library.library.BookCopy#getCopies <em>Copies</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.example.library.library.LibraryPackage#getBookCopy()
 * @model
 * @generated
 */
public interface BookCopy extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = " Copyright (c) 2011 Obeo.\r\n All rights reserved. This program and the accompanying materials\r\n are made available under the terms of the Eclipse Public License v1.0\r\n which accompanies this distribution, and is available at\r\n http://www.eclipse.org/legal/epl-v10.html\r\n \r\n Contributors:\r\n     Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Book</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Book</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Book</em>' reference.
	 * @see #setBook(Book)
	 * @see org.eclipse.emf.compare.example.library.library.LibraryPackage#getBookCopy_Book()
	 * @model
	 * @generated
	 */
	Book getBook();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.example.library.library.BookCopy#getBook <em>Book</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Book</em>' reference.
	 * @see #getBook()
	 * @generated
	 */
	void setBook(Book value);

	/**
	 * Returns the value of the '<em><b>Copies</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Copies</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Copies</em>' attribute.
	 * @see #setCopies(int)
	 * @see org.eclipse.emf.compare.example.library.library.LibraryPackage#getBookCopy_Copies()
	 * @model
	 * @generated
	 */
	int getCopies();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.example.library.library.BookCopy#getCopies <em>Copies</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Copies</em>' attribute.
	 * @see #getCopies()
	 * @generated
	 */
	void setCopies(int value);

} // BookCopy
