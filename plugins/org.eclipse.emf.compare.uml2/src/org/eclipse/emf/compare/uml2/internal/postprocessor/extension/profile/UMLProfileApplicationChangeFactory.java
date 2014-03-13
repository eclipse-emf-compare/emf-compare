/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor.extension.profile;

import static com.google.common.base.Predicates.instanceOf;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.uml2.internal.ProfileApplicationChange;
import org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange;
import org.eclipse.emf.compare.uml2.internal.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ProfileApplication;
import org.eclipse.uml2.uml.Stereotype;

/**
 * Factory for Profile Application changes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class UMLProfileApplicationChangeFactory extends AbstractUMLChangeFactory {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#getExtensionKind()
	 */
	@Override
	public Class<? extends UMLDiff> getExtensionKind() {
		return ProfileApplicationChange.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#createExtension()
	 */
	@Override
	public UMLDiff createExtension() {
		return UMLCompareFactory.eINSTANCE.createProfileApplicationChange();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#getDiscriminant(org.eclipse.emf.compare.Diff)
	 */
	@Override
	protected EObject getDiscriminant(Diff input) {
		return Iterables.find(getDiscriminants(input), instanceOf(ProfileApplication.class), null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#getDiscriminantsGetter()
	 */
	@Override
	protected Switch<Set<EObject>> getDiscriminantsGetter() {
		return new DiscriminantsGetter() {

			@Override
			public Set<EObject> caseProfileApplication(ProfileApplication object) {
				Set<EObject> result = new HashSet<EObject>();
				result.add(object);
				return result;
			}

		};
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#fillRequiredDifferences(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.compare.Diff)
	 */
	@Override
	public void fillRequiredDifferences(Comparison comparison, Diff extension) {
		super.fillRequiredDifferences(comparison, extension);
		if (extension instanceof ProfileApplicationChange) {
			final ProfileApplicationChange profileApplicationChange = (ProfileApplicationChange)extension;
			if (profileApplicationChange.getKind() == DifferenceKind.DELETE) {
				final Iterator<EObject> stereotypeApplications = getStereotypeApplications(
						(ProfileApplication)profileApplicationChange.getDiscriminant()).iterator();
				while (stereotypeApplications.hasNext()) {
					final EObject eObject = stereotypeApplications.next();
					for (Diff diff : comparison.getDifferences(eObject)) {
						if (diff instanceof StereotypeApplicationChange
								&& diff.getKind() == DifferenceKind.DELETE) {
							profileApplicationChange.getRequires().add(diff);
						}
					}
				}
			}
		}
	}

	/**
	 * Get all the applied stereotypes from the given profile application.
	 * 
	 * @param profileApplication
	 *            The given profile applciation.
	 * @return The list of applied stereotypes.
	 */
	private List<EObject> getStereotypeApplications(ProfileApplication profileApplication) {
		final List<EObject> result = new ArrayList<EObject>();
		final org.eclipse.uml2.uml.Package p = profileApplication.getApplyingPackage();
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
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#getParentMatch(org.eclipse.emf.compare.Diff)
	 */
	@Override
	public Match getParentMatch(Diff input) {
		final EObject discriminant = getDiscriminant(input);
		if (discriminant instanceof ProfileApplication) {
			final org.eclipse.uml2.uml.Package p = ((ProfileApplication)discriminant).getApplyingPackage();
			final Match match = input.getMatch().getComparison().getMatch(p);
			if (match != null) {
				return match;
			}
		}
		return super.getParentMatch(input);
	}

}
