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

import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
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
	public AbstractUMLApplicationChangeFactory(UML2DiffEngine engine) {
		super(engine);
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
	 * Get the differences on applications of stereotypes in relation to the profile application.
	 * 
	 * @param profileApplication
	 *            The profile application.
	 * @param crossReferencer
	 *            Cross referencer to retrieve the differences related to stereotypes.
	 * @param diffSide
	 *            Side to look for stereotypes.
	 * @param expectedDiff
	 *            The EClass of the expected difference.
	 * @return The list of differences on stereotypes.
	 */
	protected List<DiffElement> getStereotypeDiffs(ProfileApplication profileApplication,
			EcoreUtil.CrossReferencer crossReferencer, EReference diffSide, EClass expectedDiff) {
		final List<DiffElement> result = new ArrayList<DiffElement>();
		final Iterator<EObject> it = getStereotypeApplications(profileApplication).iterator();
		while (it.hasNext()) {
			final EObject st = it.next();
			final Element elt = UMLUtil.getBaseElement(st);
			final List<DiffElement> findCrossReferences = findCrossReferences(elt, diffSide, crossReferencer);
			for (DiffElement diffElement : findCrossReferences) {
				if (expectedDiff.isInstance(diffElement)) {
					result.add(diffElement);
				}
			}
		}
		return result;
	}

	/**
	 * Get the difference on application of profile in relation to a stereotype application.
	 * 
	 * @param stereotypeApplication
	 *            the stereotype application.
	 * @param crossReferencer
	 *            Cross referencer to retrieve the differences related to the profile.
	 * @param diffSide
	 *            Side to look for the profile.
	 * @param expectedDiff
	 *            The EClass of the expected difference.
	 * @return The difference on profile.
	 */
	protected DiffElement getProfileDiff(EObject stereotypeApplication, CrossReferencer crossReferencer,
			EReference diffSide, EClass expectedDiff) {
		final ProfileApplication profileApplication = getProfileApplication(stereotypeApplication);
		if (profileApplication != null) {
			final List<DiffElement> findCrossReferences = findCrossReferences(profileApplication, diffSide,
					crossReferencer);
			for (DiffElement diffElement : findCrossReferences) {
				if (expectedDiff.isInstance(diffElement)) {
					return diffElement;
				}
			}
		}
		return null;
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
