/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.util;

import java.util.Random;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

/**
 * This class is a convenience class for creating ecore models for the purpose of testing.
 * <p>
 * Keep in mind that this class has been intended for the puposes of testing and does not performs any check
 * to validate its method parameters.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("nls")
public final class EcoreModelUtils {
	/** Keeps a reference to the book class of the metamodel created via {@link #createMetaModel()}. */
	private static EClass bookClass;

	/** The factory for creating ecore related objects. */
	private static final EcoreFactory EFACTORY = EcorePackage.eINSTANCE.getEcoreFactory();

	/** The package for getting ecore related data types. */
	private static final EcorePackage EPACKAGE = EcorePackage.eINSTANCE;

	/** Keeps a reference to the library class of the metamodel created via {@link #createMetaModel()}. */
	private static EClass libraryClass;

	/** Keeps a reference towards the created metamodel. */
	private static EPackage metaModel;

	/** Keeps a reference to the visibility enum of the metamodel created via {@link #createMetaModel()}. */
	private static EEnum visibilityEnum;

	/** Keeps a reference to the writer class of the metamodel created via {@link #createMetaModel()}. */
	private static EClass writerClass;

	/**
	 * Utility classes don't need to (and shouldn't) be instantiated.
	 */
	private EcoreModelUtils() {
		// prevents instantiation.
	}

	/**
	 * Creates and returns a containment {@link EReference} of the given name, with the given type, under the
	 * given containing class.
	 * 
	 * @param containingClass
	 *            The class that is to be added a new reference.
	 * @param name
	 *            Name to give the created reference.
	 * @param type
	 *            EType of the created reference.
	 * @return The created EReference.
	 */
	public static EReference createContainmentEReference(EClass containingClass, String name, EClassifier type) {
		final EReference reference = EFACTORY.createEReference();
		reference.setName(name);
		reference.setEType(type);
		reference.setContainment(true);
		containingClass.getEStructuralFeatures().add(reference);
		return reference;
	}

	/**
	 * Creates and returns an {@link EAttribute} of the given name, with the given type, under the given
	 * containing class.
	 * 
	 * @param containingClass
	 *            The class that will be the container of our new attribute.
	 * @param name
	 *            Name to give the created attribute.
	 * @param type
	 *            EType of the created attribute.
	 * @return The created EAttribute.
	 */
	public static EAttribute createEAttribute(EClass containingClass, String name, EClassifier type) {
		final EAttribute attribute = EFACTORY.createEAttribute();
		attribute.setName(name);
		attribute.setEType(type);
		containingClass.getEStructuralFeatures().add(attribute);
		return attribute;
	}

	/**
	 * Creates and returns an {@link EClass} of the given name under the given container.
	 * 
	 * @param container
	 *            The package under which the class has to be created.
	 * @param name
	 *            Name of the created EClass.
	 * @return The created EClass.
	 */
	public static EClass createEClass(EPackage container, String name) {
		final EClass clazz = EFACTORY.createEClass();
		clazz.setName(name);
		container.getEClassifiers().add(clazz);
		return clazz;
	}

	/**
	 * Creates and returns an {@link EEnum} of the given name under the given package. The enumeration will
	 * have literals in number and names such as defined by <code>literals</code>.
	 * 
	 * @param container
	 *            The package under which will be created the enumeration.
	 * @param name
	 *            Name of the new enumeration.
	 * @param literals
	 *            Literals to create under this enumeration.
	 * @return The created EEnum.
	 */
	public static EEnum createEEnum(EPackage container, String name, String... literals) {
		final EEnum eenum = EFACTORY.createEEnum();
		eenum.setName(name);
		for (int i = 0; i < literals.length; i++) {
			final EEnumLiteral literal = EFACTORY.createEEnumLiteral();
			literal.setName(literals[i]);
			literal.setValue(i);
			eenum.getELiterals().add(literal);
		}
		container.getEClassifiers().add(eenum);
		return eenum;
	}

	/**
	 * Creates and returns an {@link EPackage} of the given name under the given container.
	 * 
	 * @param container
	 *            Container for the created package.
	 * @param name
	 *            Name of the EPackage to create.
	 * @return The created EPackage.
	 */
	public static EPackage createEPackage(EPackage container, String name) {
		final EPackage packaje = EFACTORY.createEPackage();
		packaje.setName(name);
		container.getESubpackages().add(packaje);
		return packaje;
	}

	/**
	 * Creates and returns an {@link EPackage} of the given name under the given resource.
	 * 
	 * @param resource
	 *            The resource under which the package has to be crated.
	 * @param name
	 *            Name of the EPackage to create.
	 * @return The created EPackage.
	 */
	public static EPackage createEPackage(Resource resource, String name) {
		final EPackage packaje = EFACTORY.createEPackage();
		packaje.setName(name);
		resource.getContents().add(packaje);
		return packaje;
	}

	/**
	 * Creates and returns an {@link EReference} of the given name, with the given type, under the given
	 * containing class.
	 * 
	 * @param containingClass
	 *            The class that is to be added a new reference.
	 * @param name
	 *            Name to give the created reference.
	 * @param type
	 *            EType of the created reference.
	 * @return The created EReference.
	 */
	public static EReference createEReference(EClass containingClass, String name, EClassifier type) {
		final EReference reference = EFACTORY.createEReference();
		reference.setName(name);
		reference.setEType(type);
		containingClass.getEStructuralFeatures().add(reference);
		return reference;
	}

	/**
	 * This is a convenience method fully equivalent to <code>createMetaModel(false)</code>.
	 * 
	 * @return The root of the created metamodel.
	 */
	public static EPackage createMetaModel() {
		return createMetaModel(false);
	}

	/**
	 * This will generate an ecore metamodel.
	 * <p>
	 * Generated metamodel will be of the form :
	 * 
	 * <pre>
	 * EPackage -root-
	 * |
	 * |---EPackage -library-
	 *     |
	 *     |---EClass -Library-
	 *     |   |
	 *     |   |---EAttribute -name-    : EString
	 *     |   |---EReference -books-   : Book
	 *     |   |---EReference -authors- : Writer
	 *     |
	 *     |---EClass -Book-
	 *     |   |
	 *     |   |---EAttribute -title-      : EString
	 *     |   |---EAttribute -pages-      : EInt
	 *     |   |---EAttribute -visibility- : visibility
	 *     |   |---EReference -author-     : Writer
	 *     |
	 *     |---EClass -Writer-
	 *     |   |
	 *     |   |---EAttribute -name-         : EString
	 *     |   |---EAttribute -visibility-   : visibility
	 *     |   |---EReference -writtenBooks- : Book
	 *     |
	 *     |---EEnum -visibility-
	 *         |
	 *         |---EEnumLiteral -private-   = 0
	 *         |---EEnumLiteral -package-   = 1
	 *         |---EEnumLiteral -protected- = 2
	 *         |---EEnumLiteral -public-    = 3
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param forceCreate
	 *            If set to <code>True</code>, the metamodel will be created anew even if it has already
	 *            been cached.
	 * @return The root of the created metamodel.
	 */
	public static EPackage createMetaModel(boolean forceCreate) {
		if (metaModel == null || forceCreate) {
			final String eenumName = "visibility";
			final String nameFeatureName = "name";
			final Resource resource = new ResourceImpl();
			// First creates the structure (classes, packages, datatypes)
			metaModel = createEPackage(resource, "root");
			final EPackage libraryPackage = createEPackage(metaModel, "library");
			libraryClass = createEClass(libraryPackage, "Library");
			bookClass = createEClass(libraryPackage, "Book");
			writerClass = createEClass(libraryPackage, "Writer");
			visibilityEnum = createEEnum(libraryPackage, eenumName, "private", "package", "protected",
					"public");

			// Then creates structural features.
			// Library features
			createEAttribute(libraryClass, nameFeatureName, EPACKAGE.getEString());
			final EReference libraryBooksReference = createContainmentEReference(libraryClass, "books",
					bookClass);
			final EReference libraryWritersReference = createContainmentEReference(libraryClass, "authors",
					writerClass);
			// Book features
			createEAttribute(bookClass, "title", EPACKAGE.getEString());
			createEAttribute(bookClass, "pages", EPACKAGE.getEInt());
			createEAttribute(bookClass, eenumName, visibilityEnum);
			final EReference bookAuthorReference = createEReference(bookClass, "author", writerClass);
			// Writer features
			createEAttribute(writerClass, nameFeatureName, EPACKAGE.getEString());
			createEAttribute(writerClass, eenumName, visibilityEnum);
			final EReference writerBooksReference = createEReference(writerClass, "writtenBooks", bookClass);

			// Sets multiplicity and oppposites of the references
			libraryBooksReference.setUpperBound(-1);
			libraryWritersReference.setUpperBound(-1);
			bookAuthorReference.setEOpposite(writerBooksReference);
			writerBooksReference.setUpperBound(-1);
		}
		return metaModel;
	}

	/**
	 * This will create an ecore model with the metamodel defined by {@link #createMetaModel()}.
	 * <p>
	 * <code>writerCount</code> and <code>bookPerWriterCount</code> allows us to create either small or
	 * huge models.
	 * </p>
	 * 
	 * @param writerCount
	 *            Total number of writers to create in the model.
	 * @param bookPerWriterCount
	 *            Maximum number of books to create for each author. Actual number will be comprised between 0
	 *            and this value.
	 * @return The root of the created model.
	 * @throws FactoryException
	 *             Thrown if an error occurs when trying to set one of the model content's features via
	 *             {@link EFactory#eSet(EObject, String, Object)}.
	 */
	public static EObject createModel(int writerCount, int bookPerWriterCount) throws FactoryException {
		return createModel(writerCount, bookPerWriterCount, System.nanoTime(), false, false);
	}

	/**
	 * This will create an ecore model with the metamodel defined by {@link #createMetaModel()}.
	 * <p>
	 * <code>writerCount</code> and <code>bookPerWriterCount</code> allows us to create either small or
	 * huge models.
	 * </p>
	 * <p>
	 * <code>seed</code> will be used for the random number generator throughout the creation. calling this
	 * method twice with the same given <code>seed</code> will create equal models.
	 * </p>
	 * 
	 * @param writerCount
	 *            Total number of writers to create in the model.
	 * @param bookPerWriterCount
	 *            Maximum number of books to create for each author. Actual number will be comprised between 0
	 *            and this value.
	 * @param seed
	 *            <code>seed</code> to be used for the pseudo-random number generator.
	 * @return The root of the created model.
	 * @throws FactoryException
	 *             Thrown if an error occurs when trying to set one of the model content's features via
	 *             {@link EFactory#eSet(EObject, String, Object)}.
	 * @see Random
	 */
	public static EObject createModel(int writerCount, int bookPerWriterCount, long seed)
			throws FactoryException {
		return createModel(writerCount, bookPerWriterCount, seed, false, false);
	}

	/**
	 * This will create an ecore model with the metamodel defined by {@link #createMetaModel()}.
	 * <p>
	 * <code>writerCount</code> and <code>bookPerWriterCount</code> allows us to create either small or
	 * huge models.
	 * </p>
	 * <p>
	 * <code>seed</code> will be used for the random number generator throughout the creation. calling this
	 * method twice with the same given <code>seed</code> will create equal models.
	 * </p>
	 * 
	 * @param writerCount
	 *            Total number of writers to create in the model.
	 * @param bookPerWriterCount
	 *            Maximum number of books to create for each author. Actual number will be comprised between 0
	 *            and this value.
	 * @param seed
	 *            <code>seed</code> to be used for the pseudo-random number generator.
	 * @param setXMIID
	 *            If set to <code>True</code>, this will set a an auto incremented number as the XMI ID of
	 *            each created object.
	 * @return The root of the created model.
	 * @throws FactoryException
	 *             Thrown if an error occurs when trying to set one of the model content's features via
	 *             {@link EFactory#eSet(EObject, String, Object)}.
	 * @see Random
	 */
	public static EObject createModel(int writerCount, int bookPerWriterCount, long seed, boolean setXMIID)
			throws FactoryException {
		return createModel(writerCount, bookPerWriterCount, seed, setXMIID, false);
	}
	
	/**
	 * This will create an ecore model with the metamodel defined by {@link #createMetaModel()}.
	 * <p>
	 * <code>writerCount</code> and <code>bookPerWriterCount</code> allows us to create either small or
	 * huge models.
	 * </p>
	 * <p>
	 * <code>seed</code> will be used for the random number generator throughout the creation. calling this
	 * method twice with the same given <code>seed</code> will create equal models.
	 * </p>
	 * 
	 * @param writerCount
	 *            Total number of writers to create in the model.
	 * @param bookPerWriterCount
	 *            Maximum number of books to create for each author. Actual number will be comprised between 0
	 *            and this value.
	 * @param seed
	 *            <code>seed</code> to be used for the pseudo-random number generator.
	 * @param setXMIID
	 *            If set to <code>True</code>, this will set a an auto incremented number as the XMI ID of
	 *            each created object.
	 * @param forceMetaModelCreation
	 * Forces the creation of a new meta model even if one has already been cached.
	 * @return The root of the created model.
	 * @throws FactoryException
	 *             Thrown if an error occurs when trying to set one of the model content's features via
	 *             {@link EFactory#eSet(EObject, String, Object)}.
	 * @see Random
	 */
	public static EObject createModel(int writerCount, int bookPerWriterCount, long seed, boolean setXMIID, boolean forceMetaModelCreation)
			throws FactoryException {
		int xmiID = 0;
		final XMIResource resource = new XMIResourceImpl();
		final String eenumName = "visibility";
		final String nameFeatureName = "name";
		final Random randomGenerator = new Random(seed);
		final EPackage libraryPackage = (EPackage)createMetaModel(forceMetaModelCreation).eContents().get(0);
		final org.eclipse.emf.ecore.EFactory libraryFactory = libraryPackage.getEFactoryInstance();

		// Creates the library itself
		final EObject library = libraryFactory.create(libraryClass);
		EFactory.eSet(library, nameFeatureName, "Library");
		resource.getContents().add(library);
		if (setXMIID)
			resource.setID(library, new Integer(++xmiID).toString());

		// Creates each writer of the library
		for (int writerNum = 0; writerNum < writerCount; writerNum++) {
			final EObject writer = libraryFactory.create(writerClass);
			EFactory.eSet(writer, nameFeatureName, "writer" + writerNum);
			EFactory.eSet(writer, eenumName, visibilityEnum.getELiterals().get(
					Double.valueOf(randomGenerator.nextDouble() * visibilityEnum.getELiterals().size())
							.intValue()).getLiteral());
			EFactory.eAdd(library, "authors", writer);
			if (setXMIID)
				resource.setID(writer, new Integer(++xmiID).toString());

			// Creates a random number of book for each writer
			for (int bookNum = 0; bookNum < (randomGenerator.nextDouble() * bookPerWriterCount) + 1; bookNum++) {
				final EObject book = libraryFactory.create(bookClass);
				EFactory
						.eSet(book, "title", "book" + Integer.toString(writerNum) + Integer.toString(bookNum));
				EFactory.eSet(book, "pages", Double.valueOf(randomGenerator.nextDouble() * 1000).intValue());
				EFactory.eSet(book, eenumName, visibilityEnum.getELiterals().get(
						Double.valueOf(randomGenerator.nextDouble() * visibilityEnum.getELiterals().size())
								.intValue()).getLiteral());
				EFactory.eSet(book, "author", writer);
				EFactory.eAdd(library, "books", book);
				if (setXMIID)
					resource.setID(book, new Integer(++xmiID).toString());
			}
		}

		return library;
	}
}
