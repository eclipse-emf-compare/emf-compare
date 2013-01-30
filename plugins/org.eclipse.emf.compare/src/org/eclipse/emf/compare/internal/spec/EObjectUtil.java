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
package org.eclipse.emf.compare.internal.spec;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * Static utilities to create human readable EObject.toString() methods.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public final class EObjectUtil {

	/**
	 * Private constructor to avoid instantiation.
	 */
	private EObjectUtil() {
	}

	/**
	 * Returns a String label for the given EObject given the following rules :
	 * <ul>
	 * <li>if the given {@code object} is {@code null}, returns the String {@code "<null>"}</li>
	 * <li>if not null, return the name of the {@link EClass} of the given {@code object} and the hex string
	 * of the object hashcode. It is followed by the value of the {@link EStructuralFeature} name if the given
	 * {@code object} has one or the first EAttribute with an instance class equals to String. (same rule as
	 * in the ReflectiveItemProvider)</li>
	 * </ul>
	 * 
	 * @param object
	 *            the object to get the label from.
	 * @return the label.
	 */
	public static String getLabel(EObject object) {
		String ret = null;
		if (object == null) {
			ret = "<null>"; //$NON-NLS-1$
		} else {
			EObject eObject = object;
			EClass eClass = eObject.eClass();
			ret = eClass.getName() + "@" + Integer.toHexString(object.hashCode()); //$NON-NLS-1$

			EStructuralFeature feature = getLabelFeature(eClass);
			if (feature != null) {
				Object value = eObject.eGet(feature);
				if (value != null) {
					ret += " " + value.toString(); //$NON-NLS-1$
				}
			}
		}
		return ret;
	}

	/**
	 * Returns either the {@link EAttribute} with name {@code name} or the first {@link EAttribute} with
	 * instance class of String otherwise.
	 * 
	 * @param eClass
	 *            the eClass to browse EAttribute.
	 * @return the label feature.
	 */
	private static EStructuralFeature getLabelFeature(EClass eClass) {
		if (eClass == EcorePackage.Literals.ENAMED_ELEMENT) {
			return EcorePackage.Literals.ENAMED_ELEMENT__NAME;
		}

		EAttribute result = null;
		for (EAttribute eAttribute : eClass.getEAllAttributes()) {
			if (!eAttribute.isMany() && eAttribute.getEType().getInstanceClass() != FeatureMap.Entry.class) {
				if ("name".equalsIgnoreCase(eAttribute.getName())) { //$NON-NLS-1$
					result = eAttribute;
					break;
				} else if (result == null) {
					result = eAttribute;
				} else if (eAttribute.getEAttributeType().getInstanceClass() == String.class
						&& result.getEAttributeType().getInstanceClass() != String.class) {
					result = eAttribute;
				}
			}
		}
		return result;
	}

}
