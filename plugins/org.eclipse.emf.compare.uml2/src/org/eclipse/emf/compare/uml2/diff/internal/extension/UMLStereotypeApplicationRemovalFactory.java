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
package org.eclipse.emf.compare.uml2.diff.internal.extension;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationRemoval;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.util.UMLUtil;

public class UMLStereotypeApplicationRemovalFactory extends AbstractDiffExtensionFactory {

	public UMLStereotypeApplicationRemovalFactory(UML2DiffEngine engine) {
		super(engine);
	}

	public boolean handles(DiffElement input) {
		boolean b = input instanceof ModelElementChangeRightTarget;
		boolean c = false;
		if (b) {
			c = UMLUtil.getBaseElement(((ModelElementChangeRightTarget)input).getRightElement()) != null;
		}
		return b && c;
	}

	public AbstractDiffExtension create(DiffElement input) {
		ModelElementChangeRightTarget modelElement = (ModelElementChangeRightTarget) input;
		EObject base = UMLUtil.getBaseElement(modelElement.getRightElement());
		
		UMLStereotypeApplicationRemoval ret = UML2DiffFactory.eINSTANCE.createUMLStereotypeApplicationRemoval();
		
		ret.setLeftElement(getEngine().getMatched(base, UML2DiffEngine.getLeftSide()));
		ret.setRightElement(base);
		
		ret.getHideElements().add(input);
		
		return ret;
	}
	
	@Override
	public DiffElement getParentDiff(DiffElement input) {
		ModelElementChangeRightTarget modelElement = (ModelElementChangeRightTarget) input;
		DiffGroup rootDiffGroup = (DiffGroup) EcoreUtil.getRootContainer(input);
		
		EObject rightBase = UMLUtil.getBaseElement(modelElement.getRightElement());
		EObject leftBase = getEngine().getMatched(rightBase, UML2DiffEngine.getLeftSide());
		
		return findOrCreateDiffElementFor(rootDiffGroup, rightBase, leftBase);
	}

}
