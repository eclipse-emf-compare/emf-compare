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
package org.eclipse.emf.compare.examples.export.library.impl;

import org.eclipse.emf.compare.examples.export.library.Book;
import org.eclipse.emf.compare.examples.export.library.Library;
import org.eclipse.emf.compare.examples.export.library.LibraryFactory;
import org.eclipse.emf.compare.examples.export.library.LibraryPackage;
import org.eclipse.emf.compare.examples.export.library.Member;
import org.eclipse.emf.compare.examples.export.library.Writer;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class LibraryFactoryImpl extends EFactoryImpl implements LibraryFactory {
	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static LibraryFactory init() {
		try {
			final LibraryFactory theLibraryFactory = (LibraryFactory)EPackage.Registry.INSTANCE
					.getEFactory("library"); //$NON-NLS-1$ 
			if (theLibraryFactory != null)
				return theLibraryFactory;
		} catch (final Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new LibraryFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LibraryFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case LibraryPackage.LIBRARY:
				return createLibrary();
			case LibraryPackage.BOOK:
				return createBook();
			case LibraryPackage.WRITER:
				return createWriter();
			case LibraryPackage.MEMBER:
				return createMember();
			default:
				throw new IllegalArgumentException(
						"The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Library createLibrary() {
		final LibraryImpl library = new LibraryImpl();
		return library;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Book createBook() {
		final BookImpl book = new BookImpl();
		return book;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Writer createWriter() {
		final WriterImpl writer = new WriterImpl();
		return writer;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Member createMember() {
		final MemberImpl member = new MemberImpl();
		return member;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LibraryPackage getLibraryPackage() {
		return (LibraryPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static LibraryPackage getPackage() {
		return LibraryPackage.eINSTANCE;
	}

} // LibraryFactoryImpl
