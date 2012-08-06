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

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2diff.UMLExtension;
import org.eclipse.emf.compare.uml2diff.UMLInterfaceRealizationChange;
import org.eclipse.emf.compare.uml2diff.Uml2diffFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.UMLPackage;

public class UMLInterfaceRealizationChangeFactory2 extends UMLDependencyChangeFactory2 {

	@Override
	public Class<? extends UMLExtension> getExtensionKind() {
		return UMLInterfaceRealizationChange.class;
	}

	@Override
	protected UMLExtension createExtension() {
		return Uml2diffFactory.eINSTANCE.createUMLInterfaceRealizationChange();
	}

	@Override
	protected List<EObject> getPotentialChangedValuesFromDiscriminant(EObject discriminant) {
		List<EObject> result = super.getPotentialChangedValuesFromDiscriminant(discriminant);
		if (discriminant instanceof InterfaceRealization) {
			result.add(((InterfaceRealization)discriminant).getContract());
		}
		return result;
	}

	@Override
	protected boolean isRelatedToAnExtensionChange(Diff input) {
		return super.isRelatedToAnExtensionChange(input)
				|| ((ReferenceChange)input).getReference().equals(
						UMLPackage.Literals.INTERFACE_REALIZATION__CONTRACT);
	}

	@Override
	protected boolean isRelatedToAnExtensionAdd(Diff input) {
		return super.isRelatedToAnExtensionAdd(input)
				&& ((InterfaceRealization)((ReferenceChange)input).getValue()).getContract() != null;
	}

	@Override
	protected List<EClass> getManagedConcreteDiscriminantKind() {
		final List<EClass> result = new ArrayList<EClass>();
		result.add(UMLPackage.Literals.INTERFACE_REALIZATION);
		return result;
	}
}
