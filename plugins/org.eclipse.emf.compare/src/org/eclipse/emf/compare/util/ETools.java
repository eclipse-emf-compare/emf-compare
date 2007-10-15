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
import java.util.List;

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
	 * Returns the full path of the classifier.
	 * <p>
	 * Sample : "java.resources.JavaFile" is the full path for the classifier java.resources.JavaFile.
	 * </p>
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
		final StringBuilder result = new StringBuilder("//"); //$NON-NLS-1$
		final List<String> uriFragmentPath = new ArrayList<String>();
		for (EObject container = object.eContainer(); container != null; container = object.eContainer()) {
			uriFragmentPath.add(((InternalEObject)container).eURIFragmentSegment(
					object.eContainmentFeature(), object));
			object = container;
		}
		final int size = uriFragmentPath.size();
		if (size > 0) {
			for (int i = size - 1; i >= 0; --i) {
				result.append(uriFragmentPath.get(i));
				if (i > 0)
					result.append('/');
			}
		}
		return result.toString();
	}
}
