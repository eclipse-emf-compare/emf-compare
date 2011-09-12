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
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLProfileApplicationAddition;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.ProfileApplication;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.util.UMLUtil;

public class UMLProfileApplicationAdditionFactory extends AbstractDiffExtensionFactory {

	public UMLProfileApplicationAdditionFactory(UML2DiffEngine engine) {
		super(engine);
	}

	public boolean handles(DiffElement input) {
		if (input instanceof ModelElementChangeLeftTarget) {
			EObject leftElement = ((ModelElementChangeLeftTarget)input).getLeftElement();
			return leftElement.eClass().equals(UMLPackage.eINSTANCE.getProfileApplication());
		}
		return false;
	}

	public AbstractDiffExtension create(DiffElement input, EcoreUtil.CrossReferencer crossReferencer) {
		ModelElementChangeLeftTarget modelElement = (ModelElementChangeLeftTarget)input;
		EObject leftElement = modelElement.getLeftElement();

		UMLProfileApplicationAddition ret = UML2DiffFactory.eINSTANCE.createUMLProfileApplicationAddition();

		ret.setRemote(input.isRemote());
		ret.setLeftElement(leftElement);
		ret.setRightElement(getEngine().getMatched(leftElement, UML2DiffEngine.getRightSide()));
		Profile profile = UMLUtil.getProfile(((ProfileApplication)leftElement).getAppliedDefinition());
		ret.setProfile(profile);

		ret.getHideElements().add(input);
		ret.getRequires().add(input);

		return ret;
	}

	@Override
	public DiffElement getParentDiff(DiffElement input, EcoreUtil.CrossReferencer crossReferencer) {
		return (DiffElement)input.eContainer();
	}

}
