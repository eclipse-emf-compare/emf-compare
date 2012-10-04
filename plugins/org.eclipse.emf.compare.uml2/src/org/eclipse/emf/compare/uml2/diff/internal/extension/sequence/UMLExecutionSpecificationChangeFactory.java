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
package org.eclipse.emf.compare.uml2.diff.internal.extension.sequence;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.ExecutionSpecificationChange;
import org.eclipse.emf.compare.uml2.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.UMLDiff;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.ExecutionOccurrenceSpecification;
import org.eclipse.uml2.uml.ExecutionSpecification;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Factory for UMLExecutionSpecificationChangeLeft.
 */
public class UMLExecutionSpecificationChangeFactory extends AbstractDiffExtensionFactory {

	public Class<? extends UMLDiff> getExtensionKind() {
		return ExecutionSpecificationChange.class;
	}

	@Override
	protected UMLDiff createExtension() {
		return UMLCompareFactory.eINSTANCE.createExecutionSpecificationChange();
	}

	@Override
	protected EObject getDiscriminantFromDiff(Diff input) {
		EObject result = null;
		final DifferenceKind kind = getRelatedExtensionKind(input);
		if (kind == DifferenceKind.ADD || kind == DifferenceKind.DELETE) {
			result = ((ReferenceChange)input).getValue();
		} else if (kind == DifferenceKind.CHANGE) {
			final EObject container = MatchUtil.getContainer(input);
			if (container instanceof ExecutionSpecification) {
				result = container;
			} else if (container instanceof ExecutionOccurrenceSpecification) {
				result = ((ExecutionOccurrenceSpecification)container).getExecution();
			}
		}
		return result;
	}

	@Override
	protected List<EObject> getPotentialChangedValuesFromDiscriminant(EObject discriminant) {
		List<EObject> result = new ArrayList<EObject>();
		if (discriminant instanceof ExecutionSpecification) {
			result.add(((ExecutionSpecification)discriminant).getStart());
			result.add(((ExecutionSpecification)discriminant).getFinish());
			result.addAll(((ExecutionSpecification)discriminant).getCovereds());
		}
		return result;
	}

	protected List<EClass> getManagedConcreteDiscriminantKind() {
		final List<EClass> result = new ArrayList<EClass>();
		result.add(UMLPackage.Literals.ACTION_EXECUTION_SPECIFICATION);
		result.add(UMLPackage.Literals.BEHAVIOR_EXECUTION_SPECIFICATION);
		result.add(UMLPackage.Literals.EXECUTION_OCCURRENCE_SPECIFICATION);
		return result;
	}

	@Override
	protected boolean isRelatedToAnExtensionChange(ReferenceChange input) {
		return ((input.getReference().equals(UMLPackage.Literals.EXECUTION_SPECIFICATION__START)
				|| input.getReference().equals(UMLPackage.Literals.EXECUTION_SPECIFICATION__FINISH) || input
				.getReference().equals(UMLPackage.Literals.INTERACTION_FRAGMENT__COVERED)) && getManagedConcreteDiscriminantKind()
				.contains(MatchUtil.getContainer(input).eClass()));
	}

	@Override
	protected boolean isRelatedToAnExtensionAdd(ReferenceChange input) {
		return (input.getReference().isContainment() && input.getKind().equals(DifferenceKind.ADD)
				&& input.getValue() instanceof ExecutionSpecification
				&& ((ExecutionSpecification)input.getValue()).getStart() != null
				&& ((ExecutionSpecification)input.getValue()).getFinish() != null
				&& ((ExecutionSpecification)input.getValue()).getCovereds() != null
				&& !((ExecutionSpecification)input.getValue()).getCovereds().isEmpty() && getManagedConcreteDiscriminantKind()
				.contains(input.getValue().eClass()));
	}

	@Override
	protected boolean isRelatedToAnExtensionDelete(ReferenceChange input) {
		return input.getReference().isContainment() && input.getKind().equals(DifferenceKind.DELETE)
				&& input.getValue() instanceof ExecutionSpecification
				&& getManagedConcreteDiscriminantKind().contains(input.getValue().eClass());
	}
}
