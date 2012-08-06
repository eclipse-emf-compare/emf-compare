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

import java.util.List;

import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2diff.UMLExtension;
import org.eclipse.emf.compare.uml2diff.UMLProfileApplicationChange;
import org.eclipse.emf.compare.uml2diff.Uml2diffFactory;
import org.eclipse.emf.compare.uml2diff.Uml2diffPackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.ProfileApplication;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Factory for UMLProfileApplicationAddition.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class UMLProfileApplicationChangeFactory extends AbstractUMLApplicationChangeFactory {

	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            UML2DiffEngine
	 */
	public UMLProfileApplicationChangeFactory() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#handles(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public boolean handles(Diff input) {
		if (input instanceof ReferenceChange && ((ReferenceChange)input).getReference().isContainment()) {
			final EObject value = ((ReferenceChange)input).getValue();
			return value.eClass().equals(UMLPackage.eINSTANCE.getProfileApplication());
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
		final ProfileApplication value = (ProfileApplication)referenceChange.getValue();

		final UMLProfileApplicationChange ret = Uml2diffFactory.eINSTANCE.createUMLProfileApplicationChange();

		final Profile profile = UMLUtil.getProfile(((ProfileApplication)value).getAppliedDefinition());
		ret.setProfile(profile);

		ret.getRefinedBy().add(input);
		ret.setValue(value.getOwner());
		ret.setKind(referenceChange.getKind());

		registerUMLExtension(crossReferencer, ret, ComparePackage.Literals.REFERENCE_CHANGE__VALUE, value
				.getOwner());

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory#fillRequiredDifferences(org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	@Override
	public void fillRequiredDifferences(UMLExtension diff, EcoreUtil.CrossReferencer crossReferencer) {
		final UMLProfileApplicationChange myDiff = (UMLProfileApplicationChange)diff;
		final EObject value = ((ReferenceChange)myDiff.getRefinedBy().get(0)).getValue();

		List<Diff> diffs = getStereotypeDiffs((ProfileApplication)value, crossReferencer,
				ComparePackage.Literals.REFERENCE_CHANGE__VALUE,
				Uml2diffPackage.Literals.UML_STEREOTYPE_APPLICATION_CHANGE);

		if (myDiff.getKind().equals(DifferenceKind.DELETE)) {
			for (Diff stereoAppliChange : diffs) {
				if (stereoAppliChange.getKind().equals(DifferenceKind.DELETE)) {
					myDiff.getRequires().add(stereoAppliChange);
				}
			}
		}

	}
}
