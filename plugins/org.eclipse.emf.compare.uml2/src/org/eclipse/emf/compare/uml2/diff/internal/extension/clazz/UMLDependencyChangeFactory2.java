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
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.diff.internal.extension.UMLAbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UMLDependencyChange;
import org.eclipse.emf.compare.uml2diff.UMLExtension;
import org.eclipse.emf.compare.uml2diff.Uml2diffFactory;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.UMLPackage;

public class UMLDependencyChangeFactory2 extends UMLAbstractDiffExtensionFactory {

	public Class<? extends UMLExtension> getExtensionKind() {
		return UMLDependencyChange.class;
	}

	@Override
	protected UMLExtension createExtension() {
		return Uml2diffFactory.eINSTANCE.createUMLDependencyChange();
	}

	@Override
	protected List<EObject> getPotentialChangedValuesFromDiscriminant(EObject discriminant) {
		List<EObject> result = new ArrayList<EObject>();
		if (discriminant instanceof Dependency) {
			result.addAll(((Dependency)discriminant).getClients());
			result.addAll(((Dependency)discriminant).getSuppliers());
		}
		return result;
	}

	@Override
	protected boolean isRelatedToAnExtensionChange(Diff input) {
		return input instanceof ReferenceChange
				&& (((ReferenceChange)input).getReference().equals(UMLPackage.Literals.DEPENDENCY__CLIENT) || ((ReferenceChange)input)
						.getReference().equals(UMLPackage.Literals.DEPENDENCY__SUPPLIER))
				&& getManagedConcreteDiscriminantKind().contains(
						MatchUtil.getContainer(MatchUtil.getComparison(input), input).eClass());
	}

	@Override
	protected boolean isRelatedToAnExtensionAdd(Diff input) {
		return input instanceof ReferenceChange
				&& ((ReferenceChange)input).getReference().isContainment()
				&& input.getKind().equals(DifferenceKind.ADD)
				&& ((ReferenceChange)input).getValue() instanceof Dependency
				&& ((Dependency)((ReferenceChange)input).getValue()).getClients() != null
				&& !((Dependency)((ReferenceChange)input).getValue()).getClients().isEmpty()
				&& getManagedConcreteDiscriminantKind()
						.contains(((ReferenceChange)input).getValue().eClass());
	}

	@Override
	protected boolean isRelatedToAnExtensionDelete(Diff input) {
		return input instanceof ReferenceChange
				&& ((ReferenceChange)input).getReference().isContainment()
				&& input.getKind().equals(DifferenceKind.DELETE)
				&& ((ReferenceChange)input).getValue() instanceof Dependency
				&& getManagedConcreteDiscriminantKind()
						.contains(((ReferenceChange)input).getValue().eClass());
	}

	@Override
	protected EObject getDiscriminantFromDiff(Diff input) {
		EObject result = null;
		if (isRelatedToAnExtensionAdd(input) || isRelatedToAnExtensionDelete(input)) {
			result = ((ReferenceChange)input).getValue();
		} else if (isRelatedToAnExtensionChange(input)) {
			final EObject container = MatchUtil.getContainer(MatchUtil.getComparison(input), input);
			if (container instanceof Dependency) {
				result = container;
			}
		}
		return result;
	}

	protected List<EClass> getManagedConcreteDiscriminantKind() {
		final List<EClass> result = new ArrayList<EClass>();
		result.add(UMLPackage.Literals.DEPENDENCY);
		result.add(UMLPackage.Literals.ABSTRACTION);
		result.add(UMLPackage.Literals.USAGE);
		result.add(UMLPackage.Literals.REALIZATION);
		return result;
	}

}
