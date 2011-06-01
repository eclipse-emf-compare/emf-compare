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
package org.eclipse.emf.compare.example.library.books;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.compare.example.library.writers.Writer;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Book</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.example.library.books.Book#getIsbn <em>Isbn</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.example.library.books.Book#getTitle <em>Title</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.example.library.books.Book#getPages <em>Pages</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.example.library.books.Book#getAuthors <em>Authors</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.example.library.books.BooksPackage#getBook()
 * @model
 * @generated
 */
public interface Book extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = " Copyright (c) 2011 Obeo.\r\n All rights reserved. This program and the accompanying materials\r\n are made available under the terms of the Eclipse Public License v1.0\r\n which accompanies this distribution, and is available at\r\n http://www.eclipse.org/legal/epl-v10.html\r\n \r\n Contributors:\r\n     Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Isbn</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Isbn</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Isbn</em>' attribute.
	 * @see #setIsbn(String)
	 * @see org.eclipse.emf.compare.example.library.books.BooksPackage#getBook_Isbn()
	 * @model id="true" required="true"
	 * @generated
	 */
	String getIsbn();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.example.library.books.Book#getIsbn <em>Isbn</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Isbn</em>' attribute.
	 * @see #getIsbn()
	 * @generated
	 */
	void setIsbn(String value);

	/**
	 * Returns the value of the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Title</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Title</em>' attribute.
	 * @see #setTitle(String)
	 * @see org.eclipse.emf.compare.example.library.books.BooksPackage#getBook_Title()
	 * @model required="true"
	 * @generated
	 */
	String getTitle();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.example.library.books.Book#getTitle <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
	void setTitle(String value);

	/**
	 * Returns the value of the '<em><b>Pages</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pages</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pages</em>' attribute.
	 * @see #setPages(int)
	 * @see org.eclipse.emf.compare.example.library.books.BooksPackage#getBook_Pages()
	 * @model
	 * @generated
	 */
	int getPages();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.example.library.books.Book#getPages <em>Pages</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pages</em>' attribute.
	 * @see #getPages()
	 * @generated
	 */
	void setPages(int value);

	/**
	 * Returns the value of the '<em><b>Authors</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.compare.example.library.writers.Writer}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.compare.example.library.writers.Writer#getBooks <em>Books</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Authors</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Authors</em>' reference list.
	 * @see org.eclipse.emf.compare.example.library.books.BooksPackage#getBook_Authors()
	 * @see org.eclipse.emf.compare.example.library.writers.Writer#getBooks
	 * @model opposite="books"
	 * @generated
	 */
	EList<Writer> getAuthors();

} // Book
