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

import org.eclipse.emf.compare.diff.engine.IMatchManager.MatchSide;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffPackage;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationAddition;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Factory for UMLStereotypeApplicationAddition.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class UMLStereotypeApplicationAdditionFactory extends AbstractUMLApplicationChangeFactory {

	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            UML2DiffEngine
	 */
	public UMLStereotypeApplicationAdditionFactory(UML2DiffEngine engine) {
		super(engine);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#handles(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public boolean handles(DiffElement input) {
		if (input instanceof ModelElementChangeLeftTarget) {
			final EObject leftElement = ((ModelElementChangeLeftTarget)input).getLeftElement();
			final Element leftBase = UMLUtil.getBaseElement(leftElement);
			return leftBase != null;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#create(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	public AbstractDiffExtension create(DiffElement input, EcoreUtil.CrossReferencer crossReferencer) {
		final ModelElementChangeLeftTarget modelElement = (ModelElementChangeLeftTarget)input;
		final EObject leftElement = modelElement.getLeftElement();
		final EObject base = UMLUtil.getBaseElement(leftElement);

		final UMLStereotypeApplicationAddition ret = UML2DiffFactory.eINSTANCE
				.createUMLStereotypeApplicationAddition();

		ret.setRemote(input.isRemote());
		ret.setLeftElement(base);

		ret.setRightElement(getEngine().getMatched(base, MatchSide.RIGHT));
		final Stereotype stereotype = UMLUtil.getStereotype(leftElement);

		ret.setStereotype(stereotype);

		ret.getHideElements().add(input);
		ret.getRequires().add(input);

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory#getParentDiff(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	@Override
	public DiffElement getParentDiff(DiffElement input, EcoreUtil.CrossReferencer crossReferencer) {
		final ModelElementChangeLeftTarget modelElement = (ModelElementChangeLeftTarget)input;
		final DiffModel rootDiffGroup = (DiffModel)EcoreUtil.getRootContainer(input);

		final EObject leftBase = UMLUtil.getBaseElement(modelElement.getLeftElement());
		final EObject rightBase = getEngine().getMatched(leftBase, MatchSide.RIGHT);

		if (rightBase == null) {
			return findOrCreateDiffGroup(rootDiffGroup, leftBase, crossReferencer);
		} else {
			return findOrCreateDiffGroup(rootDiffGroup, rightBase, crossReferencer);
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory#fillRequiredDifferences(org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	@Override
	public void fillRequiredDifferences(AbstractDiffExtension diff, CrossReferencer crossReferencer) {
		final UMLStereotypeApplicationAddition myDiff = (UMLStereotypeApplicationAddition)diff;
		final EObject leftElement = myDiff.getLeftElement();
		final EObject stereotypeApp = ((Element)leftElement).getStereotypeApplication(myDiff.getStereotype());
		final DiffElement applyProfileDiff = getProfileDiff(stereotypeApp, crossReferencer,
				DiffPackage.Literals.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT,
				UML2DiffPackage.Literals.UML_PROFILE_APPLICATION_ADDITION);
		if (applyProfileDiff != null) {
			myDiff.getRequires().add(applyProfileDiff);
		}
	}

}
