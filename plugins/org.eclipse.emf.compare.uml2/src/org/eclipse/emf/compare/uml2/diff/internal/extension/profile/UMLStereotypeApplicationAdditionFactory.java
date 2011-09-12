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
import java.util.List;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLProfileApplicationAddition;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationAddition;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ProfileApplication;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.util.UMLUtil;

public class UMLStereotypeApplicationAdditionFactory extends AbstractDiffExtensionFactory {

	public UMLStereotypeApplicationAdditionFactory(UML2DiffEngine engine) {
		super(engine);
	}

	public boolean handles(DiffElement input) {
		if (input instanceof ModelElementChangeLeftTarget) {
			EObject leftElement = ((ModelElementChangeLeftTarget)input).getLeftElement();
			Element leftBase = UMLUtil.getBaseElement(leftElement);
			return leftBase != null;
		}
		return false;
	}

	public AbstractDiffExtension create(DiffElement input, EcoreUtil.CrossReferencer crossReferencer) {
		ModelElementChangeLeftTarget modelElement = (ModelElementChangeLeftTarget)input;
		EObject leftElement = modelElement.getLeftElement();
		EObject base = UMLUtil.getBaseElement(leftElement);

		UMLStereotypeApplicationAddition ret = UML2DiffFactory.eINSTANCE
				.createUMLStereotypeApplicationAddition();

		ret.setRemote(input.isRemote());
		ret.setLeftElement(base);
		ret.setRightElement(getEngine().getMatched(base, UML2DiffEngine.getRightSide()));
		Stereotype stereotype = UMLUtil.getStereotype(leftElement);
		ret.setStereotype(stereotype);

		ret.getHideElements().add(input);
		ret.getRequires().add(input);

		return ret;
	}

	@Override
	public DiffElement getParentDiff(DiffElement input, EcoreUtil.CrossReferencer crossReferencer) {
		ModelElementChangeLeftTarget modelElement = (ModelElementChangeLeftTarget)input;
		DiffModel rootDiffGroup = (DiffModel)EcoreUtil.getRootContainer(input);

		EObject leftBase = UMLUtil.getBaseElement(modelElement.getLeftElement());
		EObject rightBase = getEngine().getMatched(leftBase, UML2DiffEngine.getRightSide());

		return findOrCreateDiffGroup(rootDiffGroup, rightBase, crossReferencer);
	}

	@Override
	public void fillRequiredDifferences(AbstractDiffExtension diff, CrossReferencer crossReferencer) {
		UMLStereotypeApplicationAddition myDiff = (UMLStereotypeApplicationAddition)diff;
		EObject leftElement = myDiff.getLeftElement();
		EObject stereotypeApp = ((Element)leftElement).getStereotypeApplication(myDiff.getStereotype());
		DiffElement applyProfileDiff = getApplyProfileDiff(stereotypeApp, crossReferencer);
		if (applyProfileDiff != null) {
			myDiff.getRequires().add(applyProfileDiff);
		}
	}

	private DiffElement getApplyProfileDiff(EObject stereotypeApplication, CrossReferencer crossReferencer) {
		ProfileApplication profileApplication = getProfileApplication(stereotypeApplication);
		if (profileApplication != null) {
			List<DiffElement> findCrossReferences = findCrossReferences(profileApplication,
					DiffPackage.Literals.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT, crossReferencer);
			for (DiffElement diffElement : findCrossReferences) {
				if (diffElement instanceof UMLProfileApplicationAddition) {
					return diffElement;
				}
			}
		}
		return null;
	}

	private ProfileApplication getProfileApplication(EObject stereotypeApplication) {
		EObject base = UMLUtil.getBaseElement(stereotypeApplication);
		for (org.eclipse.uml2.uml.Package myPackage : ancestor(base)) {
			ProfileApplication profileApplication = myPackage.getProfileApplication(UMLUtil.getStereotype(
					stereotypeApplication).getProfile());
			if (profileApplication != null) {
				return profileApplication;
			}
		}
		return null;
	}

	private List<org.eclipse.uml2.uml.Package> ancestor(EObject obj) {
		List<org.eclipse.uml2.uml.Package> result = new ArrayList<org.eclipse.uml2.uml.Package>();
		EObject container = obj.eContainer();
		if (container instanceof org.eclipse.uml2.uml.Package) {
			result.add((org.eclipse.uml2.uml.Package)container);
			result.addAll(ancestor(container));
		}
		return result;
	}

}
