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
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffPackage;
import org.eclipse.emf.compare.uml2diff.UMLProfileApplicationAddition;
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
public class UMLProfileApplicationAdditionFactory extends AbstractUMLApplicationChangeFactory {

	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            UML2DiffEngine
	 */
	public UMLProfileApplicationAdditionFactory(UML2DiffEngine engine) {
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
			return leftElement.eClass().equals(UMLPackage.eINSTANCE.getProfileApplication());
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

		final UMLProfileApplicationAddition ret = UML2DiffFactory.eINSTANCE
				.createUMLProfileApplicationAddition();

		ret.setRemote(input.isRemote());
		ret.setLeftElement(leftElement);

		ret.setRightElement(getEngine().getMatched(leftElement, MatchSide.RIGHT));
		final Profile profile = UMLUtil.getProfile(((ProfileApplication)leftElement).getAppliedDefinition());

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
		final UMLProfileApplicationAddition myDiff = (UMLProfileApplicationAddition)diff;
		final EObject leftElement = myDiff.getLeftElement();
		myDiff.getRequires().addAll(
				getStereotypeDiffs((ProfileApplication)leftElement, crossReferencer,
						DiffPackage.Literals.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT,
						UML2DiffPackage.Literals.UML_STEREOTYPE_APPLICATION_ADDITION));
	}
}
