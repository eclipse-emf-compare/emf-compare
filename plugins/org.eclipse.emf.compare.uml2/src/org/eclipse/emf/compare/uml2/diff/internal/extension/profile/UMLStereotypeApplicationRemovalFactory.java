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
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffPackage;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationRemoval;
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
public class UMLStereotypeApplicationRemovalFactory extends AbstractUMLApplicationChangeFactory {

	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            UML2DiffEngine
	 */
	public UMLStereotypeApplicationRemovalFactory(UML2DiffEngine engine) {
		super(engine);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#handles(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public boolean handles(DiffElement input) {
		if (input instanceof ModelElementChangeRightTarget) {
			final EObject rightElement = ((ModelElementChangeRightTarget)input).getRightElement();
			final Element rightBase = UMLUtil.getBaseElement(rightElement);
			return rightBase != null;
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
		final ModelElementChangeRightTarget modelElement = (ModelElementChangeRightTarget)input;
		final EObject rightElement = modelElement.getRightElement();
		final EObject base = UMLUtil.getBaseElement(rightElement);

		final UMLStereotypeApplicationRemoval ret = UML2DiffFactory.eINSTANCE
				.createUMLStereotypeApplicationRemoval();

		ret.setRemote(input.isRemote());
		ret.setLeftElement(getEngine().getMatched(base, MatchSide.LEFT));
		ret.setRightElement(base);
		ret.setStereotype(UMLUtil.getStereotype(rightElement));

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
		final ModelElementChangeRightTarget modelElement = (ModelElementChangeRightTarget)input;
		final DiffModel rootDiffGroup = (DiffModel)EcoreUtil.getRootContainer(input);

		final EObject rightBase = UMLUtil.getBaseElement(modelElement.getRightElement());

		return findOrCreateDiffGroup(rootDiffGroup, rightBase, crossReferencer);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory#fillRequiredDifferences(org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	@Override
	public void fillRequiredDifferences(AbstractDiffExtension diff, CrossReferencer crossReferencer) {
		final UMLStereotypeApplicationRemoval myDiff = (UMLStereotypeApplicationRemoval)diff;
		final EObject rightElement = myDiff.getRightElement();
		final EObject stereotypeApp = ((Element)rightElement)
				.getStereotypeApplication(myDiff.getStereotype());
		final DiffElement applyProfileDiff = getProfileDiff(stereotypeApp, crossReferencer,
				DiffPackage.Literals.UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT,
				UML2DiffPackage.Literals.UML_PROFILE_APPLICATION_REMOVAL);
		if (applyProfileDiff != null) {
			myDiff.getRequires().add(applyProfileDiff);
		}
	}

}
