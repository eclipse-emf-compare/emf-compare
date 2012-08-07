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

import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.uml2.diff.internal.extension.UMLAbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UMLExtension;
import org.eclipse.emf.ecore.EObject;

/**
 * Factory for UMLStereotypeAttributeChangeLeftTarget.
 */
public class UMLStereotypeReferenceOrderChangeFactory extends UMLAbstractDiffExtensionFactory {

	public Class<? extends UMLExtension> getExtensionKind() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected UMLExtension createExtension() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected EObject getDiscriminantFromDiff(Diff input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<EObject> getPotentialChangedValuesFromDiscriminant(EObject discriminant) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected DifferenceKind getRelatedExtensionKind(Diff input) {
		// TODO Auto-generated method stub
		return null;
	}

}
