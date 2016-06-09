/*****************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH
 * *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Alexandra Buzila (EclipseSource) - Initial API and implementation
 *****************************************************************************/

package org.eclipse.emf.compre.uml2.edit.papyrus.internal.decorator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.infra.emf.utils.EMFHelper;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;

/**
 * Utility class for Papyrus Stereotyped Elements.
 * 
 * @author Alexandra Buzila
 */
public final class PapyrusStereotypedElementUtil {

	/** Papyrus profile URI. */
	private static final String PAPYRUS_PROFILE_URI = "http://www.eclipse.org/papyrus.*"; //$NON-NLS-1$

	/** Constructor. */
	private PapyrusStereotypedElementUtil() {
	}

	/**
	 * Returns <code>true</code> if the given object is a Papyrus stereotyped {@link Element}.
	 *
	 * @param object
	 *            the element to check
	 * @return <code>true</code> if the given object is a Papyrus stereotyped {@link Element}
	 */
	public static boolean isPapyrusStereotypedElement(Object object) {
		EObject eObject = EMFHelper.getEObject(object);
		if (!(eObject instanceof Element)) {
			return false;
		}
		Element element = (Element)eObject;
		Package nearestPackage = element.getNearestPackage();
		if (nearestPackage == null) {
			return false;
		}
		for (Profile profile : nearestPackage.getAllAppliedProfiles()) {
			if (profile.getURI() != null && profile.getURI().matches(PAPYRUS_PROFILE_URI)) {
				return true;
			}
		}
		return false;
	}
}
