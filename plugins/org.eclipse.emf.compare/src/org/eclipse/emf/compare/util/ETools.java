/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;

/**
 * This contains general utility methods for ecore browsing support.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public final class ETools {
	/** Externalized here to avoid redondant {@link String} uses. */
	private static final String SEPARATOR = "."; //$NON-NLS-1$

	/**
	 * Utility classes don't need to be initialized.
	 */
	private ETools() {
		// prevents instantiation
	}

	/**
	 * Search a classifier recursively in a package.
	 * <p>
	 * Remarks :
	 * <ul>
	 * <li>Never returns classifier java.resources.Folder if name = "File"</li>
	 * <li>Never returns classifier java.resources.Folder if name = "older"</li>
	 * <li>Returns classifier java.resources.Folder for "Folder" or "resources.Folder"</li>
	 * </ul>
	 * 
	 * @param ePackage
	 *            The package from where we seek the given classifier.
	 * @param classifierName
	 *            The classifier identifier.
	 * @return The classifier or <code>null</code> if it cannot be found.
	 */
	public static EClassifier getEClassifier(EPackage ePackage, String classifierName) {
		final String name = classifierName.trim();
		EClassifier get = null;
		final Iterator classifiers = ePackage.getEClassifiers().iterator();
		while (get == null && classifiers.hasNext()) {
			final EClassifier classifier = (EClassifier)classifiers.next();
			final String instanceClassName = SEPARATOR + getEClassifierPath(classifier);
			final String endsWith = SEPARATOR + name;
			if (instanceClassName.endsWith(endsWith)) {
				get = classifier;
			}
		}
		final Iterator packages = ePackage.getESubpackages().iterator();
		while (get == null && packages.hasNext()) {
			final EClassifier classifier = getEClassifier((EPackage)packages.next(), name);
			if (classifier != null)
				get = classifier;
		}
		return get;
	}

	/**
	 * Returns the full path of the classifier.
	 * <p>
	 * Sample : "java.resources.JavaFile" is the full path for the classifier java.resources.JavaFile
	 * 
	 * @param eClassifier
	 *            The classifier we need the full path of.
	 * @return The full path of <code>eClassifier</code>.
	 */
	public static String getEClassifierPath(EClassifier eClassifier) {
		String fullPath = null;
		if (eClassifier != null) {
			final String instanceClassName = eClassifier.getInstanceClassName();
			String name = eClassifier.getName();
			if (eClassifier.getEPackage() != null) {
				EPackage container = eClassifier.getEPackage();
				if (container != null && instanceClassName != null
						&& instanceClassName.endsWith(container.getName() + SEPARATOR + name)) {
					fullPath = instanceClassName;
				} else {
					while (container != null) {
						name = container.getName() + SEPARATOR + name;
						container = container.getESuperPackage();
					}
					fullPath = name;
				}
			}
		}
		return fullPath;
	}

	/**
	 * Returns a URI for the eObject, i.e., either the eProxyURI, the URI of the eResource with the fragment
	 * produced by the eResource, or the URI consisting of just the fragment that would be produced by a
	 * default Resource with the eObject as its only contents.
	 * 
	 * @param eObject
	 *            The object for which to get the URI.
	 * @return The URI for the object.
	 */
	public static String getURI(EObject eObject) {
		EObject object = eObject;
		if (object.eResource() != null)
			return object.eResource().getURIFragment(object);
		// inspired from EMF sources
		final StringBuffer result = new StringBuffer("//"); //$NON-NLS-1$
		final List<String> uriFragmentPath = new ArrayList<String>();
		for (EObject container = object.eContainer(); container != null; container = object.eContainer()) {
			uriFragmentPath.add(((InternalEObject)container).eURIFragmentSegment(
					object.eContainmentFeature(), object));
			object = container;
		}
		final int size = uriFragmentPath.size();
		if (size > 0) {
			for (int i = size - 1;; --i) {
				result.append(uriFragmentPath.get(i));
				if (i == 0)
					break;
				result.append('/');
			}
		}
		return result.toString();
	}

	/**
	 * Indicates if the type corresponds to the name of the classifier.
	 * 
	 * @param classifier
	 *            The classifier to test.
	 * @param type
	 *            Type to test against <code>classifier</code> name.
	 * @return <code>True</code> if the type corresponds to the name of the classifier, <code>False</code>
	 *         otherwise.
	 */
	public static boolean ofClass(EClassifier classifier, String type) {
		final String path = ETools.getEClassifierPath(classifier);
		return (SEPARATOR + path).endsWith(SEPARATOR + type);
	}

	/**
	 * Indicates if an instance of the class is an instance of the type.
	 * 
	 * @param eClass
	 *            Class to test agaisnt <code>type</code>.
	 * @param type
	 *            Name of the type to test.
	 * @return <code>True</code> if an instance of the class is an instance of the type, <code>False</code>
	 *         otherwise.
	 */
	public static boolean ofType(EClass eClass, String type) {
		boolean isOfType = false;
		if (ofClass(eClass, type))
			isOfType = true;
		final Iterator superTypes = eClass.getESuperTypes().iterator();
		while (superTypes.hasNext() && !isOfType) {
			final EClassifier superType = (EClassifier)superTypes.next();
			if (ofType(superType, type))
				isOfType = true;
		}
		return isOfType;
	}

	/**
	 * Indicates if an instance of the classifier is an instance of the type.
	 * 
	 * @param classifier
	 *            Classifier to test against the instances of <code>type</code>.
	 * @param type
	 *            Type to test.
	 * @return <code>True</code> if an instance of the classifier is an instance of the type,
	 *         <code>False</code> otherwise.
	 */
	public static boolean ofType(EClassifier classifier, String type) {
		if (classifier instanceof EClass)
			return ofType((EClass)classifier, type);
		return ofClass(classifier, type);
	}

}
