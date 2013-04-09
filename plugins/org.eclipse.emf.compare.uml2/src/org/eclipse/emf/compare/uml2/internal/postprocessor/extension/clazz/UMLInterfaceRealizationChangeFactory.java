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
package org.eclipse.emf.compare.uml2.internal.postprocessor.extension.clazz;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.internal.InterfaceRealizationChange;
import org.eclipse.emf.compare.uml2.internal.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.UMLPackage;

public class UMLInterfaceRealizationChangeFactory extends UMLDependencyChangeFactory {

	@Override
	public Class<? extends UMLDiff> getExtensionKind() {
		return InterfaceRealizationChange.class;
	}

	@Override
	public UMLDiff createExtension() {
		return UMLCompareFactory.eINSTANCE.createInterfaceRealizationChange();
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
	protected boolean isRelatedToAnExtensionChange(ReferenceChange input) {
		return super.isRelatedToAnExtensionChange(input)
				|| input.getReference().equals(UMLPackage.Literals.INTERFACE_REALIZATION__CONTRACT);
	}

	@Override
	protected boolean isRelatedToAnExtensionAdd(ReferenceChange input) {
		return super.isRelatedToAnExtensionAdd(input)
				&& ((InterfaceRealization)input.getValue()).getContract() != null;
	}

	@Override
	protected List<EClass> getManagedConcreteDiscriminantKind() {
		final List<EClass> result = new ArrayList<EClass>();
		result.add(UMLPackage.Literals.INTERFACE_REALIZATION);
		return result;
	}
}
