/**
 * <copyright>
 * </copyright>
 *
 * $Id: Book.java,v 1.1 2008/04/21 09:43:18 lgoubet Exp $
 */
package org.eclipse.emf.compare.examples.export.library;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Book</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.examples.export.library.Book#getTitle <em>Title</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.examples.export.library.Book#getPages <em>Pages</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.examples.export.library.Book#getAuthor <em>Author</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.examples.export.library.LibraryPackage#getBook()
 * @model
 * @generated
 */
public interface Book extends EObject {
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
	 * @see org.eclipse.emf.compare.examples.export.library.LibraryPackage#getBook_Title()
	 * @model
	 * @generated
	 */
	String getTitle();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.examples.export.library.Book#getTitle <em>Title</em>}' attribute.
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
	 * @see org.eclipse.emf.compare.examples.export.library.LibraryPackage#getBook_Pages()
	 * @model
	 * @generated
	 */
	int getPages();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.examples.export.library.Book#getPages <em>Pages</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pages</em>' attribute.
	 * @see #getPages()
	 * @generated
	 */
	void setPages(int value);

	/**
	 * Returns the value of the '<em><b>Author</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.compare.examples.export.library.Writer#getBooks <em>Books</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Author</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Author</em>' reference.
	 * @see #setAuthor(Writer)
	 * @see org.eclipse.emf.compare.examples.export.library.LibraryPackage#getBook_Author()
	 * @see org.eclipse.emf.compare.examples.export.library.Writer#getBooks
	 * @model opposite="books"
	 * @generated
	 */
	Writer getAuthor();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.examples.export.library.Book#getAuthor <em>Author</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Author</em>' reference.
	 * @see #getAuthor()
	 * @generated
	 */
	void setAuthor(Writer value);

} // Book
