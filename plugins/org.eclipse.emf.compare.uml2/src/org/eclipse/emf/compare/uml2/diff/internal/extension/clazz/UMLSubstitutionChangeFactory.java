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
package org.eclipse.emf.compare.uml2.diff.internal.extension.clazz;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.SubstitutionChange;
import org.eclipse.emf.compare.uml2.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.UMLDiff;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Substitution;
import org.eclipse.uml2.uml.UMLPackage;

public class UMLSubstitutionChangeFactory extends UMLDependencyChangeFactory {

	@Override
	public Class<? extends UMLDiff> getExtensionKind() {
		return SubstitutionChange.class;
	}

	@Override
	protected UMLDiff createExtension() {
		return UMLCompareFactory.eINSTANCE.createSubstitutionChange();
	}

	@Override
	protected List<EObject> getPotentialChangedValuesFromDiscriminant(EObject discriminant) {
		List<EObject> result = super.getPotentialChangedValuesFromDiscriminant(discriminant);
		if (discriminant instanceof Substitution) {
			result.add(((Substitution)discriminant).getContract());
		}
		return result;
	}

	@Override
	protected boolean isRelatedToAnExtensionChange(ReferenceChange input) {
		return super.isRelatedToAnExtensionChange(input)
				|| input.getReference().equals(UMLPackage.Literals.SUBSTITUTION__CONTRACT);
	}

	@Override
	protected boolean isRelatedToAnExtensionAdd(ReferenceChange input) {
		return super.isRelatedToAnExtensionAdd(input)
				&& ((Substitution)input.getValue()).getContract() != null;
	}

	@Override
	protected List<EClass> getManagedConcreteDiscriminantKind() {
		final List<EClass> result = new ArrayList<EClass>();
		result.add(UMLPackage.Literals.SUBSTITUTION);
		return result;
	}
}
