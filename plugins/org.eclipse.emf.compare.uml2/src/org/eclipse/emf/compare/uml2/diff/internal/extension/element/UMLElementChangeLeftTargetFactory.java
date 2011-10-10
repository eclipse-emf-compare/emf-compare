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
package org.eclipse.emf.compare.uml2.diff.internal.extension.element;

import java.util.List;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffPackage;
import org.eclipse.emf.compare.uml2diff.UMLElementChangeLeftTarget;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
import org.eclipse.uml2.uml.Element;

/**
 * Factory for UMLElementChangeLeftTarget.
 * 
 * @see Bug 351593.
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class UMLElementChangeLeftTargetFactory extends AbstractUMLElementChangeFactory {

	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            UML2DiffEngine
	 */
	public UMLElementChangeLeftTargetFactory(UML2DiffEngine engine) {
		super(engine);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#handles(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public boolean handles(DiffElement input) {
		if (input instanceof ModelElementChangeLeftTarget) {
			final EObject lelt = ((ModelElementChangeLeftTarget)input).getLeftElement();
			return lelt instanceof Element && getAllStereotypeApplications((Element)lelt).size() > 0;
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

		final UMLElementChangeLeftTarget ret = UML2DiffFactory.eINSTANCE.createUMLElementChangeLeftTarget();

		ret.setRemote(input.isRemote());
		ret.setLeftElement(modelElement.getLeftElement());
		ret.setRightParent(modelElement.getRightParent());

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
	public void fillRequiredDifferences(AbstractDiffExtension diff, CrossReferencer crossReferencer) {
		final UMLElementChangeLeftTarget myDiff = (UMLElementChangeLeftTarget)diff;
		final EObject leftElement = myDiff.getLeftElement();
		final List<DiffElement> diffs = getEmbeddedStereotypeApplicationDiffs(leftElement, crossReferencer,
				DiffPackage.Literals.UPDATE_MODEL_ELEMENT__LEFT_ELEMENT,
				UML2DiffPackage.Literals.UML_STEREOTYPE_APPLICATION_ADDITION);
		myDiff.getRequires().addAll(diffs);
		// Hide these extensions
		myDiff.getHideElements().addAll(diffs);
	}

}
