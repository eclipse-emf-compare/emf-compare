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
import java.util.StringTokenizer;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EcorePackageImpl;
import org.eclipse.emf.ecore.util.Diagnostician;

/**
 * This contains general utility methods for ecore browsing support.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public final class ETools {
	private static final String SEPARATOR = "."; //$NON-NLS-1$

	private ETools() {
		// prevents instantiation
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
		if (object.eResource() != null) {
			return object.eResource().getURIFragment(object);
		} else {
			// inspired from EMF sources
			final StringBuffer result = new StringBuffer("//"); //$NON-NLS-1$
			final List<String> uriFragmentPath = new ArrayList<String>();
			for (EObject container = object.eContainer(); container != null; container = object.eContainer()) {
				uriFragmentPath.add(((InternalEObject)container).eURIFragmentSegment(object
						.eContainmentFeature(), object));
				object = container;
			}
			final int size = uriFragmentPath.size();
			if (size > 0) {
				for (int i = size - 1;; --i) {
					result.append((String)uriFragmentPath.get(i));
					if (i == 0) {
						break;
					} else {
						result.append('/');
					}
				}
			}
			return result.toString();
		}
	}

	/**
	 * Validates the given EMF object.
	 * <p>
	 * An error is put in the log if the validation failed.
	 * <p>
	 * <ul>
	 * <li><code>!blocker</code> || {@link Diagnostic#OK} => <code>root</code></li>
	 * <li><code>blocker</code> && !{@link Diagnostic#OK} => <code>null</code></li>
	 * <li><code>root</code> == <code>null</code> => <code>null</code></li>
	 * </ul>
	 * 
	 * @param root
	 *            The object to validate.
	 * @param blocker
	 *            Indicates if the result must be {@link Diagnostic#OK}.
	 * @param message
	 *            The error message to put in the log.
	 * @return The given object or <code>null</code>.
	 */
	public static EObject validate(EObject root, boolean blocker, String message) {
		if (root != null && !(root.getClass().getName().startsWith("org.eclipse.uml2"))) { //$NON-NLS-1$
			if (Diagnostician.INSTANCE.validate(root).getSeverity() != Diagnostic.OK) {
				EMFComparePlugin.getDefault().log(message, false);
				if (blocker) {
					return null;
				}
			}
		}
		return root;
	}

	/**
	 * Search all the classifiers recursively in a package.
	 * 
	 * @param ePackage
	 *            The package from which we need the classifiers.
	 * @return Array of classifiers.
	 */
	@SuppressWarnings("unchecked")
	public static EClassifier[] computeAllClassifiers(EPackage ePackage) {
		final List classifiers = computeAllClassifiersList(ePackage);
		return (EClassifier[])classifiers.toArray(new EClassifier[] {});
	}

	/**
	 * Search all the classifiers recursively in a package.
	 * 
	 * @param ePackage
	 *            The package from which we need the classifiers.
	 * @return List of all the package's classifiers.
	 */
	public static List computeAllClassifiersList(EPackage ePackage) {
		return computeAllClassifiersList(ePackage, false);
	}

	/**
	 * Search all the classifiers recursively in a package.
	 * 
	 * @param ePackage
	 *            The package from which we need the classifiers.
	 * @param classOnly
	 *            Indicates that only the classes are kept.
	 * @return List of classifiers.
	 */
	public static List computeAllClassifiersList(EPackage ePackage, boolean classOnly) {
		final List classifiers = new BasicEList();
		if (ePackage != null)
			computeAllClassifiersList(ePackage, classifiers, classOnly);
		return classifiers;
	}

	@SuppressWarnings("unchecked")
	private static void computeAllClassifiersList(EPackage ePackage, List all, boolean classOnly) {
		final Iterator classifiers = ePackage.getEClassifiers().iterator();
		while (classifiers.hasNext()) {
			final EClassifier classifier = (EClassifier)classifiers.next();
			if (!classOnly) {
				all.add(classifier);
			} else {
				if (classifier instanceof EClass && !((EClass)classifier).isAbstract()
						&& !((EClass)classifier).isInterface()) {
					all.add(classifier);
				}
			}
		}
		final Iterator packages = ePackage.getESubpackages().iterator();
		while (packages.hasNext()) {
			computeAllClassifiersList((EPackage)packages.next(), all, classOnly);
		}
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
	 * Returns the feature <code>name</code> from the given {@link EClassifier}.
	 * 
	 * @param currentEClassifier
	 *            The classifier to get the feature <code>name</code> from.
	 * @param name
	 *            Name of the feature we seek.
	 * @return The feature resolved with the given name.
	 */
	public static EStructuralFeature getEStructuralFeature(EClassifier currentEClassifier, String name) {
		if (currentEClassifier != null && currentEClassifier instanceof EClass) {
			return getEStructuralFeature((EClass)currentEClassifier, name);
		} else {
			return null;
		}
	}

	/**
	 * Returns the feature <code>name</code> from the given {@link EClass}.
	 * 
	 * @param currentEClass
	 *            The class to get the feature <code>name</code> from.
	 * @param featureName
	 *            Name of the feature we seek.
	 * @return The feature resolved with the given name.
	 */
	public static EStructuralFeature getEStructuralFeature(EClass currentEClass, String featureName) {
		final String name = featureName.trim();
		if (currentEClass != null) {
			return currentEClass.getEStructuralFeature(name);
		} else {
			return null;
		}
	}

	/**
	 * Returns a factory name for a classifier. There is a factory by package. The factory is used to create
	 * instances of classifiers.
	 * <p>
	 * Sample : "ResourcesFactory" is the name of the factory java.resources.ResourcesFactory
	 * 
	 * @param eClassifier
	 *            The classifier from which we need the factory's name.
	 * @return The factory name.
	 */
	public static String getEClassifierFactoryName(EClassifier eClassifier) {
		return getEClassifierFactoryShortName(eClassifier) + "Factory"; //$NON-NLS-1$
	}

	/**
	 * Returns a factory short name for a classifier. There is a factory by package. The factory is used to
	 * create instances of classifiers.
	 * <p>
	 * Sample : "Resources" is the short name of the factory java.resources.ResourcesFactory
	 * 
	 * @param eClassifier
	 *            The classifier from which we need the factory's short name.
	 * @return The factory short name.
	 */
	public static String getEClassifierFactoryShortName(EClassifier eClassifier) {
		if (eClassifier != null) {
			final EPackage p = eClassifier.getEPackage();
			final String name = p.getName();
			if (name != null && name.length() > 0) {
				return name.substring(0, 1).toUpperCase() + name.substring(1);
			}
		}
		return new String();
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
	 * Returns the short path of the classifier.
	 * <p>
	 * Sample : "resources.JavaFile" is the short path for the classifier java.resources.JavaFile
	 * 
	 * @param eClassifier
	 *            The classifier we need the short path of.
	 * @return Short path of <code>eClassifier</code>.
	 */
	public static String getEClassifierShortPath(EClassifier eClassifier) {
		String name = eClassifier.getName();
		if (eClassifier.getEPackage() != null)
			name = eClassifier.getEPackage().getName() + SEPARATOR + name;
		return name;
	}

	/**
	 * Creates a package and his children in an ecore model. The children are separated by '.'
	 * <p>
	 * Sample : "a.b.c" is a package full path,
	 * <p>
	 * "a", "b", and "c" packages are created, the root package "a" is returned.
	 * 
	 * @param path
	 *            The package full path.
	 * @return The root package.
	 */
	@SuppressWarnings("unchecked")
	public static EPackage createPackageHierarchy(String path) {
		EPackage ePackage = null;
		if (path != null && path.length() > 0) {
			final EcorePackage p = EcorePackageImpl.init();
			final EcoreFactory factory = p.getEcoreFactory();
			EPackage parent = null;
			final StringTokenizer st = new StringTokenizer(path, SEPARATOR);
			while (st.hasMoreTokens()) {
				final String name = st.nextToken();
				final EPackage child = factory.createEPackage();
				child.setName(name);
				if (parent != null)
					parent.getESubpackages().add(child);
				else
					ePackage = child;
				parent = child;
			}
		}
		return ePackage;
	}

	/**
	 * Gets a package in a parent package.
	 * <p>
	 * Sample : "a.b" is a parent package and "c.d" is a relative path,
	 * <p>
	 * "a.b.c.d" package is returned.
	 * 
	 * @param parent
	 *            The parent package.
	 * @param path
	 *            Relative path of the desired package.
	 * @return The desired package, <code>null</code> if it cannot be found.
	 */
	public static EPackage getEPackage(EPackage parent, String path) {
		EPackage ePackage = parent;
		if (path != null && path.length() > 0) {
			final StringTokenizer st = new StringTokenizer(path, SEPARATOR);
			while (st.hasMoreTokens()) {
				final String name = st.nextToken();
				boolean found = false;
				final Iterator subPackages = ePackage.getESubpackages().iterator();
				while (!found && subPackages.hasNext()) {
					final EPackage subPackage = (EPackage)subPackages.next();
					if (subPackage.getName().equals(name)) {
						found = true;
						ePackage = subPackage;
					}
				}
				if (!found)
					return null;
			}
		}
		return ePackage;
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
		if (classifier instanceof EClass) {
			return ofType((EClass)classifier, type);
		} else {
			return ofClass(classifier, type);
		}
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

}
