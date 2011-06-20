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
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeLeftTarget;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.util.UMLUtil;

public class UMLStereotypeAttributeChangeLeftTargetFactory extends AbstractDiffExtensionFactory {

	public UMLStereotypeAttributeChangeLeftTargetFactory(UML2DiffEngine engine,
			EcoreUtil.CrossReferencer crossReferencer) {
		super(engine, crossReferencer);
	}

	public boolean handles(DiffElement input) {
		if (input instanceof AttributeChangeLeftTarget) {
			EObject left = ((AttributeChangeLeftTarget)input).getLeftElement();
			EObject right = ((AttributeChangeLeftTarget)input).getRightElement();
			EObject leftBase = UMLUtil.getBaseElement(left);
			EObject rightBase = UMLUtil.getBaseElement(right);
			return leftBase != null && rightBase != null;
		}
		return false;
	}

	public AbstractDiffExtension create(DiffElement input) {
		AttributeChangeLeftTarget attributeChangeLeftTarget = (AttributeChangeLeftTarget)input;
		EObject leftElement = attributeChangeLeftTarget.getLeftElement();
		EObject rightElement = attributeChangeLeftTarget.getRightElement();
		EObject leftBase = UMLUtil.getBaseElement(leftElement);
		EObject rightBase = UMLUtil.getBaseElement(rightElement);

		UMLStereotypeAttributeChangeLeftTarget ret = UML2DiffFactory.eINSTANCE
				.createUMLStereotypeAttributeChangeLeftTarget();

		ret.setStereotype(UMLUtil.getStereotype(leftElement));
		ret.setRemote(input.isRemote());

		ret.setAttribute(attributeChangeLeftTarget.getAttribute());
		ret.setLeftElement(leftBase);
		ret.setRightElement(rightBase);
		ret.setLeftTarget(attributeChangeLeftTarget.getLeftTarget());

		ret.getHideElements().add(input);

		return ret;
	}

	@Override
	public DiffElement getParentDiff(DiffElement input) {
		EObject right = ((AttributeChangeLeftTarget)input).getRightElement();
		EObject rightBase = UMLUtil.getBaseElement(right);

		DiffModel rootDiffGroup = (DiffModel)EcoreUtil.getRootContainer(input);

		return findOrCreateDiffGroup(rootDiffGroup, rightBase);
	}
}
