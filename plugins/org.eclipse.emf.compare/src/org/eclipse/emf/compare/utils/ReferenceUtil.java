/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.utils;

import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * This utility class holds methods that will be used by the diff and merge processes. TODO: Maybe useless.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public final class ReferenceUtil {
	/**
	 * Utility classes don't need a default constructor.
	 */
	private ReferenceUtil() {
		// Hides default constructor
	}

	/**
	 * This utility simply allows us to retrieve the value of a given feature as a List.
	 * 
	 * @param object
	 *            The object for which feature we need a value.
	 * @param feature
	 *            The actual feature of which we need the value.
	 * @return The value of the given <code>feature</code> for the given <code>object</code> as a list. An
	 *         empty list if this object has no value for that feature or if the object is <code>null</code>.
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> getAsList(EObject object, EStructuralFeature feature) {
		if (object != null) {
			Object value = safeEGet(object, feature);
			final List<Object> asList;
			if (value instanceof InternalEList<?>) {
				// EMF ignores the "resolve" flag for containment lists...
				asList = ((InternalEList<Object>)value).basicList();
			} else if (value instanceof List) {
				asList = (List<Object>)value;
			} else if (value instanceof Iterable) {
				asList = ImmutableList.copyOf((Iterable<Object>)value);
			} else if (value != null) {
				asList = ImmutableList.of(value);
			} else {
				asList = Collections.emptyList();
			}
			return asList;
		}
		return Collections.emptyList();
	}

	/**
	 * In case of dynamic EObjects, the EClasses of both sides might be different, making "eget" fail in
	 * "unknown feature". We assume that even if the EClasses are distinct instances, they are the same
	 * nonetheless, and thus we can use the feature name in order to retrieve the feature's value.
	 * 
	 * @param object
	 *            The object for which feature we need a value.
	 * @param feature
	 *            The actual feature of which we need the value.
	 * @return The value of the given {@code feature} for the given {@code object}.
	 */
	public static Object safeEGet(EObject object, EStructuralFeature feature) {
		final EClass clazz = object.eClass();
		// TODO profile. This "if" might be counter productive : accessing both packages is probably as long
		// as a direct lookup to the clazz.eGetEStructuralFeature...
		if (clazz.getEPackage() == feature.getEContainingClass().getEPackage()) {
			return object.eGet(feature, false);
		}
		// Assumes that the containing package is the same, let it fail otherwise
		return object.eGet(clazz.getEStructuralFeature(feature.getName()), false);
	}
}
