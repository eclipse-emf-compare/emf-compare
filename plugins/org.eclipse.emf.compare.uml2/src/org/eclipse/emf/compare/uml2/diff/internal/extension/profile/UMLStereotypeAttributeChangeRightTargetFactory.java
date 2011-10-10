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

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeRightTarget;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Factory for UMLStereotypeAttributeChangeLeftTarget.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UMLStereotypeAttributeChangeRightTargetFactory extends AbstractDiffExtensionFactory {

	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            The UML2 difference engine.
	 */
	public UMLStereotypeAttributeChangeRightTargetFactory(UML2DiffEngine engine) {
		super(engine);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#handles(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public boolean handles(DiffElement input) {
		if (input instanceof AttributeChangeRightTarget) {
			final EObject left = ((AttributeChangeRightTarget)input).getLeftElement();
			final EObject right = ((AttributeChangeRightTarget)input).getRightElement();
			final EObject leftBase = UMLUtil.getBaseElement(left);
			final EObject rightBase = UMLUtil.getBaseElement(right);
			return leftBase != null && rightBase != null;
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
		final AttributeChangeRightTarget attributeChangeRightTarget = (AttributeChangeRightTarget)input;
		final EObject leftElement = attributeChangeRightTarget.getLeftElement();
		final EObject rightElement = attributeChangeRightTarget.getRightElement();
		final EObject leftBase = UMLUtil.getBaseElement(leftElement);
		final EObject rightBase = UMLUtil.getBaseElement(rightElement);

		final UMLStereotypeAttributeChangeRightTarget ret = UML2DiffFactory.eINSTANCE
				.createUMLStereotypeAttributeChangeRightTarget();

		ret.setStereotype(UMLUtil.getStereotype(rightElement));
		ret.setRemote(input.isRemote());

		ret.setAttribute(attributeChangeRightTarget.getAttribute());
		ret.setLeftElement(leftBase);
		ret.setRightElement(rightBase);
		ret.setRightTarget(attributeChangeRightTarget.getRightTarget());

		ret.getHideElements().add(input);
		ret.getRequires().add(input);

		return ret;
	}

	@Override
	public DiffElement getParentDiff(DiffElement input, EcoreUtil.CrossReferencer crossReferencer) {
		final EObject right = ((AttributeChangeRightTarget)input).getRightElement();
		final EObject rightBase = UMLUtil.getBaseElement(right);

		final DiffModel rootDiffGroup = (DiffModel)EcoreUtil.getRootContainer(input);

		return findOrCreateDiffGroup(rootDiffGroup, rightBase, crossReferencer);
	}
}
