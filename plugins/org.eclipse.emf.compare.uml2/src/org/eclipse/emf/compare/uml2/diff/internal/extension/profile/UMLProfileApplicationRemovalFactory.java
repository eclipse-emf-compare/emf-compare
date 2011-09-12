/*******************************************************************************
 * Copyright (c) 2011 Obeo.
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

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLProfileApplicationRemoval;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationRemoval;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.ProfileApplication;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.util.UMLUtil;

public class UMLProfileApplicationRemovalFactory extends AbstractDiffExtensionFactory {

	public UMLProfileApplicationRemovalFactory(UML2DiffEngine engine) {
		super(engine);
	}

	public boolean handles(DiffElement input) {
		if (input instanceof ModelElementChangeRightTarget) {
			EObject rightElement = ((ModelElementChangeRightTarget)input).getRightElement();
			return rightElement.eClass().equals(UMLPackage.eINSTANCE.getProfileApplication());
		}
		return false;
	}

	public AbstractDiffExtension create(DiffElement input, EcoreUtil.CrossReferencer crossReferencer) {
		ModelElementChangeRightTarget modelElement = (ModelElementChangeRightTarget)input;
		EObject rightElement = modelElement.getRightElement();

		UMLProfileApplicationRemoval ret = UML2DiffFactory.eINSTANCE.createUMLProfileApplicationRemoval();

		ret.setRemote(input.isRemote());
		ret.setLeftElement(getEngine().getMatched(rightElement, UML2DiffEngine.getLeftSide()));
		ret.setRightElement(rightElement);
		Profile profile = UMLUtil.getProfile(((ProfileApplication)rightElement).getAppliedDefinition());
		ret.setProfile(profile);

		ret.getHideElements().add(input);
		ret.getRequires().add(input);

		return ret;
	}

	@Override
	public DiffElement getParentDiff(DiffElement input, EcoreUtil.CrossReferencer crossReferencer) {
		return (DiffElement)input.eContainer();
	}

	private List<EObject> getStereotypeApplications(ProfileApplication profileApplication) {
		List<EObject> result = new ArrayList<EObject>();
		Package p = profileApplication.getApplyingPackage();
		Iterator<EObject> it = p.eAllContents();
		while (it.hasNext()) {
			EObject elt = it.next();
			if (elt instanceof Element) {
				for (Stereotype stereotype : ((Element)elt).getAppliedStereotypes()) {
					if (stereotype.getProfile().equals(profileApplication.getAppliedProfile())) {
						EObject stereotypeApplication = ((Element)elt).getStereotypeApplication(stereotype);
						result.add(stereotypeApplication);
					}
				}
			}
		}
		return result;
	}

	private List<DiffElement> getUnapplyStereotypeDiffs(ProfileApplication profileApplication,
			EcoreUtil.CrossReferencer crossReferencer) {
		List<DiffElement> result = new ArrayList<DiffElement>();
		Iterator<EObject> it = getStereotypeApplications(profileApplication).iterator();
		while (it.hasNext()) {
			EObject st = it.next();
			Element elt = UMLUtil.getBaseElement(st);
			List<DiffElement> findCrossReferences = findCrossReferences(elt,
					DiffPackage.Literals.UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT, crossReferencer);
			for (DiffElement diffElement : findCrossReferences) {
				if (diffElement instanceof UMLStereotypeApplicationRemoval) {
					result.add(diffElement);
				}
			}
		}
		return result;
	}

	@Override
	public void fillRequiredDifferences(AbstractDiffExtension diff, EcoreUtil.CrossReferencer crossReferencer) {
		UMLProfileApplicationRemoval myDiff = (UMLProfileApplicationRemoval)diff;
		EObject rightElement = myDiff.getRightElement();
		myDiff.getRequires().addAll(
				getUnapplyStereotypeDiffs((ProfileApplication)rightElement, crossReferencer));
	}

}
