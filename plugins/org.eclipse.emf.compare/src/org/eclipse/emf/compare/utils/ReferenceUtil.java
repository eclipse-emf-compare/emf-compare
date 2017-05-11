/*******************************************************************************
 * Copyright (c) 2012, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 516524
 *******************************************************************************/
package org.eclipse.emf.compare.utils;

import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
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
		if (object != null && feature != null) {
			Object value = safeEGet(object, feature);
			final List<Object> asList;
			if (feature == EcorePackage.Literals.ECLASS__ESUPER_TYPES
					|| feature == EcorePackage.Literals.EOPERATION__EEXCEPTIONS) {
				// workaround 394286. Use the normal list, resolution is not much of a problem on these.
				asList = (List<Object>)value;
			} else if (value instanceof InternalEList<?>) {
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
	 *            The object for which feature we need a value, must not be <code>null</code>.
	 * @param feature
	 *            The actual feature of which we need the value, must not be <code>null</code>.
	 * @return The value of the given {@code feature} for the given {@code object}.
	 */
	public static Object safeEGet(EObject object, EStructuralFeature feature) {
		final int featureID = getFeatureID(feature, object.eClass());
		return ((InternalEObject)object).eGet(featureID, false, true);
	}

	/**
	 * In case of dynamic EObjects, the EClasses of both sides might be different, making "isset" fail in
	 * "unknown feature". We assume that even if the EClasses are distinct instances, they are the same
	 * nonetheless, and thus we can use the feature name in order to retrieve the feature's value.
	 * 
	 * @param object
	 *            The object for which feature we need a value, must not be <code>null</code>.
	 * @param feature
	 *            The actual feature of which we need the value, must not be <code>null</code>.
	 * @return whether the {@code feature} for the given {@code object} is set.
	 */
	public static boolean safeEIsSet(EObject object, EStructuralFeature feature) {
		int featureID = getFeatureID(feature, object.eClass());
		return ((InternalEObject)object).eIsSet(featureID);
	}

	/**
	 * In case of dynamic EObjects, the EClasses of both sides might be different, making "isset" fail in
	 * "unknown feature". We assume that even if the EClasses are distinct instances, they are the same
	 * nonetheless, and thus we can use the feature name in order to retrieve the feature's value.
	 * 
	 * @param object
	 *            The object for which feature we'll set the value, must not be <code>null</code>.
	 * @param feature
	 *            The actual feature of which we'll set the value, must not be <code>null</code>.
	 * @param newValue
	 *            The value to set, can be <code>null</code>.
	 */
	public static void safeESet(EObject object, EStructuralFeature feature, Object newValue) {
		int featureID = getFeatureID(feature, object.eClass());
		((InternalEObject)object).eSet(featureID, newValue);
	}

	/**
	 * Returns the ID of the given <code>feature</code> relative to the given <code>eClass</code>.
	 * <p>
	 * If the feature ID could not be found in <code>eClass</code> directly, this method will try find a
	 * feature in <code>eClass</code> with the same name as the given <code>feature</code> and return its
	 * feature ID. Otherwise, this method returns -1. , or -1 if the feature is not in this class.
	 * </p>
	 * 
	 * @param feature
	 *            The feature.
	 * @param eClass
	 *            The class.
	 * @return The ID of the <code>feature</code> relative to <code>class</code>, or -1 if the feature or an
	 *         equally named feature is not in <code>clazz</code>.
	 */
	private static int getFeatureID(EStructuralFeature feature, final EClass eClass) {
		int featureID = eClass.getFeatureID(feature);
		if (featureID == -1) {
			// We may have a different but equivalent EClass, so try find the feature with the same name and
			// compute the feature ID for that.
			featureID = eClass.getFeatureID(eClass.getEStructuralFeature(feature.getName()));
		}
		return featureID;
	}

	/**
	 * Checks if the given reference is a FeatureMap-derived feature.
	 * 
	 * @param reference
	 *            the given EReference.
	 * @return true if the given reference is a FeatureMap-derived feature, false otherwise.
	 * @since 3.2
	 */
	public static boolean isFeatureMapDerivedFeature(EReference reference) {
		if (reference.isDerived() && reference.isTransient() && reference.isVolatile()) {
			String annotation = EcoreUtil.getAnnotation(reference, ExtendedMetaData.ANNOTATION_URI, "group"); //$NON-NLS-1$
			if (annotation != null) {
				if (annotation.startsWith("#")) { //$NON-NLS-1$
					annotation = annotation.substring(1); // deletes the '#' character
				}
				EClass container = reference.getEContainingClass();
				for (EAttribute content : container.getEAttributes()) {
					if (FeatureMapUtil.isFeatureMap(content)
							&& annotation.toLowerCase().startsWith(content.getName().toLowerCase())) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
