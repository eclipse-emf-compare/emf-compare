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
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationRemoval;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.util.UMLUtil;

public class UMLStereotypeApplicationRemovalFactory extends AbstractDiffExtensionFactory {

	public UMLStereotypeApplicationRemovalFactory(UML2DiffEngine engine,
			EcoreUtil.CrossReferencer crossReferencer) {
		super(engine, crossReferencer);
	}

	public boolean handles(DiffElement input) {
		if (input instanceof ModelElementChangeRightTarget) {
			EObject rightElement = ((ModelElementChangeRightTarget)input).getRightElement();
			Element rightBase = UMLUtil.getBaseElement(rightElement);
			return rightBase != null;
		}
		return false;
	}

	public AbstractDiffExtension create(DiffElement input) {
		ModelElementChangeRightTarget modelElement = (ModelElementChangeRightTarget)input;
		EObject rightElement = modelElement.getRightElement();
		EObject base = UMLUtil.getBaseElement(rightElement);

		UMLStereotypeApplicationRemoval ret = UML2DiffFactory.eINSTANCE
				.createUMLStereotypeApplicationRemoval();

		ret.setRemote(input.isRemote());
		ret.setLeftElement(getEngine().getMatched(base, UML2DiffEngine.getLeftSide()));
		ret.setRightElement(base);
		ret.setStereotype(UMLUtil.getStereotype(rightElement));

		ret.getHideElements().add(input);

		return ret;
	}

	@Override
	public DiffElement getParentDiff(DiffElement input) {
		ModelElementChangeRightTarget modelElement = (ModelElementChangeRightTarget)input;
		DiffModel rootDiffGroup = (DiffModel)EcoreUtil.getRootContainer(input);

		EObject rightBase = UMLUtil.getBaseElement(modelElement.getRightElement());

		return findOrCreateDiffGroup(rootDiffGroup, rightBase);
	}

}
