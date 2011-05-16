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
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationAddition;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.util.UMLUtil;

public class UMLStereotypeApplicationAdditionFactory extends AbstractDiffExtensionFactory {

	public UMLStereotypeApplicationAdditionFactory(UML2DiffEngine engine) {
		super(engine);
	}

	public boolean handles(DiffElement input) {
		boolean b = input instanceof ModelElementChangeLeftTarget;
		boolean c = false;
		if (b) {
			Element baseElement = UMLUtil.getBaseElement(((ModelElementChangeLeftTarget)input).getLeftElement());
			c = baseElement != null;
		}
		return b && c;
	}

	public AbstractDiffExtension create(DiffElement input) {
		ModelElementChangeLeftTarget modelElement = (ModelElementChangeLeftTarget) input;
		EObject base = UMLUtil.getBaseElement(modelElement.getLeftElement());
		
		UMLStereotypeApplicationAddition ret = UML2DiffFactory.eINSTANCE.createUMLStereotypeApplicationAddition();
		
		ret.setLeftElement(base);
		ret.setRightElement(getEngine().getMatched(base, UML2DiffEngine.getRightSide()));
		
		ret.getHideElements().add(input);
		
		return ret;
	}
	
	@Override
	public DiffElement getParentDiff(DiffElement input) {
		ModelElementChangeLeftTarget modelElement = (ModelElementChangeLeftTarget) input;
		DiffGroup rootDiffGroup = (DiffGroup) EcoreUtil.getRootContainer(input);
		
		EObject leftBase = UMLUtil.getBaseElement(modelElement.getLeftElement());
		EObject rightBase = getEngine().getMatched(leftBase, UML2DiffEngine.getRightSide());
		
		return findOrCreateDiffElementFor(rootDiffGroup, leftBase, rightBase);
	}

}
