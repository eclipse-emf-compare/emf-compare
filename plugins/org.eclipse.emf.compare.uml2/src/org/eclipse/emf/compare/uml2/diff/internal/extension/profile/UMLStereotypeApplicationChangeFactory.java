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

import java.util.Set;

import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2diff.UMLExtension;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationChange;
import org.eclipse.emf.compare.uml2diff.Uml2diffFactory;
import org.eclipse.emf.compare.uml2diff.Uml2diffPackage;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Factory for UMLStereotypeApplicationRemoval.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class UMLStereotypeApplicationChangeFactory extends AbstractUMLApplicationChangeFactory {

	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            UML2DiffEngine
	 */
	public UMLStereotypeApplicationChangeFactory() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#handles(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public boolean handles(Diff input) {
		if (input instanceof ReferenceChange && ((ReferenceChange)input).getReference().isContainment()) {
			final EObject value = ((ReferenceChange)input).getValue();
			final Element base = UMLUtil.getBaseElement(value);
			return base != null;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#create(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	public Diff create(Diff input, EcoreUtil.CrossReferencer crossReferencer) {
		final ReferenceChange referenceChange = (ReferenceChange)input;
		final EObject value = referenceChange.getValue();

		final UMLStereotypeApplicationChange ret = Uml2diffFactory.eINSTANCE
				.createUMLStereotypeApplicationChange();

		ret.setStereotype(UMLUtil.getStereotype(value));

		ret.getRefinedBy().add(input);
		final Element base = UMLUtil.getBaseElement(value);
		ret.setValue(base);
		ret.setKind(referenceChange.getKind());

		registerUMLExtension(crossReferencer, ret, ComparePackage.Literals.REFERENCE_CHANGE__VALUE, base);

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory#getParentDiff(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	@Override
	public Match getParentMatch(Diff input, EcoreUtil.CrossReferencer crossReferencer) {
		final ReferenceChange referenceChange = (ReferenceChange)input;

		final EObject rightBase = UMLUtil.getBaseElement(referenceChange.getValue());

		Set<Match> matchs = ReferenceUtil.getCrossReferences(crossReferencer, rightBase,
				ComparePackage.Literals.MATCH__RIGHT, Match.class);
		matchs.addAll(ReferenceUtil.getCrossReferences(crossReferencer, rightBase,
				ComparePackage.Literals.MATCH__ORIGIN, Match.class));

		if (matchs.iterator().hasNext()) {
			return matchs.iterator().next();
		}

		return super.getParentMatch(input, crossReferencer);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory#fillRequiredDifferences(org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	@Override
	public void fillRequiredDifferences(UMLExtension diff, CrossReferencer crossReferencer) {
		final UMLStereotypeApplicationChange myDiff = (UMLStereotypeApplicationChange)diff;
		final EObject rightElement = ((ReferenceChange)myDiff.getRefinedBy().get(0)).getValue();
		final EObject stereotypeApp = ((Element)rightElement)
				.getStereotypeApplication(myDiff.getStereotype());

		final Diff applyProfileDiff = getProfileDiff(stereotypeApp, crossReferencer,
				ComparePackage.Literals.REFERENCE_CHANGE__VALUE,
				Uml2diffPackage.Literals.UML_PROFILE_APPLICATION_CHANGE);
		if (applyProfileDiff != null && myDiff.getKind().equals(DifferenceKind.ADD)
				&& applyProfileDiff.getKind().equals(DifferenceKind.ADD)) {
			myDiff.getRequires().add(applyProfileDiff);
		}
	}

}
