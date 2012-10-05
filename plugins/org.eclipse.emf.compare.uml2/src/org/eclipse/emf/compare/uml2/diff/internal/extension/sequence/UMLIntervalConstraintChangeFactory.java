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

import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.IntervalConstraintChange;
import org.eclipse.emf.compare.uml2.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.UMLDiff;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.uml2.uml.Interval;
import org.eclipse.uml2.uml.IntervalConstraint;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValueSpecification;

/**
 * Factory for UMLIntervalConstraintChangeLeftTarget.
 */
public class UMLIntervalConstraintChangeFactory extends AbstractDiffExtensionFactory {

	public Class<? extends UMLDiff> getExtensionKind() {
		return IntervalConstraintChange.class;
	}

	@Override
	protected UMLDiff createExtension() {
		return UMLCompareFactory.eINSTANCE.createIntervalConstraintChange();
	}

	@Override
	protected EObject getDiscriminantFromDiff(Diff input) {
		EObject result = null;
		final DifferenceKind kind = getRelatedExtensionKind(input);
		if (kind == DifferenceKind.ADD || kind == DifferenceKind.DELETE) {
			result = ((ReferenceChange)input).getValue();
		} else if (kind == DifferenceKind.CHANGE) {
			final EObject container = MatchUtil.getContainer(input.getMatch().getComparison(), input);
			result = getIntervalContraint(container);
		}
		return result;
	}

	private IntervalConstraint getIntervalContraint(EObject object) {
		if (object instanceof IntervalConstraint) {
			return (IntervalConstraint)object;
		} else if (object instanceof ValueSpecification && object.eContainer() instanceof IntervalConstraint) {
			return (IntervalConstraint)object.eContainer();
		} else if (object instanceof ValueSpecification && object.eContainer() instanceof ValueSpecification) {
			return getIntervalContraint(object.eContainer());
		} else if (object instanceof ValueSpecification) {
			final Setting setting = getInverseReferences(object, new Predicate<EStructuralFeature.Setting>() {
				public boolean apply(EStructuralFeature.Setting input) {
					return ((input.getEStructuralFeature() == UMLPackage.Literals.INTERVAL__MIN || input
							.getEStructuralFeature() == UMLPackage.Literals.INTERVAL__MAX))
							&& input.getEObject().eContainer() instanceof IntervalConstraint;
				}
			});
			return (IntervalConstraint)((Interval)setting.getEObject()).eContainer();
		}
		return null;
	}

	@Override
	protected List<EObject> getPotentialChangedValuesFromDiscriminant(EObject discriminant) {
		List<EObject> result = new ArrayList<EObject>();
		if (discriminant instanceof IntervalConstraint) {
			result.addAll(((IntervalConstraint)discriminant).getConstrainedElements());
			ValueSpecification valueSpecification = ((IntervalConstraint)discriminant).getSpecification();
			result.add(valueSpecification);
			if (valueSpecification instanceof Interval) {
				final ValueSpecification min = ((Interval)valueSpecification).getMin();
				final ValueSpecification max = ((Interval)valueSpecification).getMax();
				result.add(min);
				result.add(max);
				result.addAll(min.eContents());
				result.addAll(max.eContents());
			}

		}
		return result;
	}

	@Override
	protected boolean isRelatedToAnExtensionChange(ReferenceChange input) {
		return (input.getReference().equals(UMLPackage.Literals.CONSTRAINT__CONSTRAINED_ELEMENT)
				|| input.getReference().equals(UMLPackage.Literals.CONSTRAINT__SPECIFICATION)
				|| input.getReference().equals(UMLPackage.Literals.INTERVAL__MIN)
				|| input.getReference().equals(UMLPackage.Literals.INTERVAL__MAX) || input.getReference()
				.equals(UMLPackage.Literals.TIME_EXPRESSION__EXPR))
				&& getManagedConcreteDiscriminantKind().contains(
						MatchUtil.getContainer(input.getMatch().getComparison(), input).eClass());
	}

	@Override
	protected boolean isRelatedToAnExtensionAdd(ReferenceChange input) {
		return (input.getReference().isContainment() && input.getKind().equals(DifferenceKind.ADD)
				&& input.getValue() instanceof IntervalConstraint
				&& ((IntervalConstraint)input.getValue()).getConstrainedElements() != null
				&& !((IntervalConstraint)input.getValue()).getConstrainedElements().isEmpty() && getManagedConcreteDiscriminantKind()
				.contains(input.getValue().eClass()));
	}

	@Override
	protected boolean isRelatedToAnExtensionDelete(ReferenceChange input) {
		return input.getReference().isContainment() && input.getKind().equals(DifferenceKind.DELETE)
				&& input.getValue() instanceof IntervalConstraint
				&& getManagedConcreteDiscriminantKind().contains(input.getValue().eClass());
	}

	protected List<EClass> getManagedConcreteDiscriminantKind() {
		final List<EClass> result = new ArrayList<EClass>();
		result.add(UMLPackage.Literals.INTERVAL_CONSTRAINT);
		result.add(UMLPackage.Literals.TIME_CONSTRAINT);
		result.add(UMLPackage.Literals.INTERVAL);
		result.add(UMLPackage.Literals.TIME_INTERVAL);
		result.add(UMLPackage.Literals.VALUE_SPECIFICATION);
		result.add(UMLPackage.Literals.TIME_EXPRESSION);
		return result;
	}

}
