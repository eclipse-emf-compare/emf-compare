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
package org.eclipse.emf.compare.uml2.diff.internal.util;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Extension;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Some utility methods that may be tweaked to allow EMFCompare to scale.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UMLCompareUtil {
	/**
	 * Retrieves the base element for the specified stereotype application, i.e. the element to which the
	 * stereotype is applied.
	 * <p>
	 * It first calls {@link UMLUtil#getBaseElement(EObject)}. If it returns null, it then tries to find a
	 * {@link EStructuralFeature} with a name starting with {@link Extension#METACLASS_ROLE_PREFIX}. It
	 * <em>does not</em> verify if the the given {@code stereotypeApplication}'s eClass is defined as a
	 * Stereotype within a Profile because it may lead to load the resource of the Profile.
	 * 
	 * @param stereotypeApplication
	 *            The stereotype application.
	 * @return The base element.
	 */
	public static Element getBaseElement(EObject stereotypeApplication) {
		Element baseElement = UMLUtil.getBaseElement(stereotypeApplication);
		if (baseElement == null) {
			if (stereotypeApplication != null) {
				EClass eClass = stereotypeApplication.eClass();
				for (EStructuralFeature eStructuralFeature : eClass.getEAllStructuralFeatures()) {

					if (eStructuralFeature.getName().startsWith(Extension.METACLASS_ROLE_PREFIX)) {

						Object value = stereotypeApplication.eGet(eStructuralFeature);

						if (value instanceof Element) {
							return (Element)value;
						}
					}
				}
			}
		}

		return baseElement;
	}

}
