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

import org.eclipse.emf.compare.diff.engine.IMatchManager.MatchSide;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffPackage;
import org.eclipse.emf.compare.uml2diff.UMLProfileApplicationRemoval;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.ProfileApplication;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Factory for UMLProfileApplicationRemoval.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
// CHECKSTYLE:OFF
public class UMLProfileApplicationRemovalFactory extends AbstractUMLApplicationChangeFactory { // CHECKSTYLE:ON

	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            UML2DiffEngine
	 */
	public UMLProfileApplicationRemovalFactory(UML2DiffEngine engine) {
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
			return rightElement.eClass().equals(UMLPackage.eINSTANCE.getProfileApplication());
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

		final UMLProfileApplicationRemoval ret = UML2DiffFactory.eINSTANCE
				.createUMLProfileApplicationRemoval();

		ret.setRemote(input.isRemote());
		ret.setLeftElement(getEngine().getMatched(rightElement, MatchSide.LEFT));
		ret.setRightElement(rightElement);
		final Profile profile = UMLUtil.getProfile(((ProfileApplication)rightElement).getAppliedDefinition());
		ret.setProfile(profile);

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
		return (DiffElement)input.eContainer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory#fillRequiredDifferences(org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	@Override
	public void fillRequiredDifferences(AbstractDiffExtension diff, EcoreUtil.CrossReferencer crossReferencer) {
		final UMLProfileApplicationRemoval myDiff = (UMLProfileApplicationRemoval)diff;
		final EObject rightElement = myDiff.getRightElement();
		myDiff.getRequires().addAll(
				getStereotypeDiffs((ProfileApplication)rightElement, crossReferencer,
						DiffPackage.Literals.UPDATE_MODEL_ELEMENT__RIGHT_ELEMENT,
						UML2DiffPackage.Literals.UML_STEREOTYPE_APPLICATION_REMOVAL));
	}

}
