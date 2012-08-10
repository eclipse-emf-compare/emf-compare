/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.diff.internal.extension.profile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.ProfileApplication;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Common factory for stereotypes and profiles.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public abstract class AbstractUMLApplicationChangeFactory extends AbstractDiffExtensionFactory {

	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            UML2DiffEngine
	 */
	public AbstractUMLApplicationChangeFactory() {
	}

	/**
	 * Get the stereotype applications in relation to the profile application.
	 * 
	 * @param profileApplication
	 *            profile application.
	 * @return The list of stereotype applications.
	 */
	private List<EObject> getStereotypeApplications(ProfileApplication profileApplication) {
		final List<EObject> result = new ArrayList<EObject>();
		final Package p = profileApplication.getApplyingPackage();
		final Iterator<EObject> it = p.eAllContents();
		while (it.hasNext()) {
			final EObject elt = it.next();
			if (elt instanceof Element) {
				for (Stereotype stereotype : ((Element)elt).getAppliedStereotypes()) {
					if (stereotype.getProfile().equals(profileApplication.getAppliedProfile())) {
						final EObject stereotypeApplication = ((Element)elt)
								.getStereotypeApplication(stereotype);
						result.add(stereotypeApplication);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Get the profile application in relation to the stereotype application.
	 * 
	 * @param stereotypeApplication
	 *            The stereotype application.
	 * @return The profile application.
	 */
	private ProfileApplication getProfileApplication(EObject stereotypeApplication) {
		final EObject base = UMLUtil.getBaseElement(stereotypeApplication);
		for (org.eclipse.uml2.uml.Package myPackage : ancestor(base)) {
			final ProfileApplication profileApplication = myPackage.getProfileApplication(UMLUtil
					.getStereotype(stereotypeApplication).getProfile());
			if (profileApplication != null) {
				return profileApplication;
			}
		}
		return null;
	}

	/**
	 * Get the list of the package ancestors of the given object.
	 * 
	 * @param obj
	 *            The object to look for its ancestors.
	 * @return The package ancestors.
	 */
	private List<org.eclipse.uml2.uml.Package> ancestor(EObject obj) {
		final List<org.eclipse.uml2.uml.Package> result = new ArrayList<org.eclipse.uml2.uml.Package>();
		final EObject container = obj.eContainer();
		if (container instanceof org.eclipse.uml2.uml.Package) {
			result.add((org.eclipse.uml2.uml.Package)container);
			result.addAll(ancestor(container));
		}
		return result;
	}

}
