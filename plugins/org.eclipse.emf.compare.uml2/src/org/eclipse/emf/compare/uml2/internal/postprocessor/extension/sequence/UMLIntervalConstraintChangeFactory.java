/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor.extension.sequence;

import static com.google.common.base.Predicates.instanceOf;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.uml2.internal.IntervalConstraintChange;
import org.eclipse.emf.compare.uml2.internal.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.uml2.uml.Interval;
import org.eclipse.uml2.uml.IntervalConstraint;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValueSpecification;

/**
 * Factory for UMLIntervalConstraintChangeLeftTarget.
 */
/**
 * Factory for interval constraint changes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class UMLIntervalConstraintChangeFactory extends AbstractUMLChangeFactory {

	/**
	 * Discriminants getter for the interval constraint change.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	private class IntervalConstraintGetter extends DiscriminantsGetter {
		/**
		 * {@inheritDoc}<br>
		 * Discriminants are the interval constraint and the min/max values of the specification interval.
		 * 
		 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseIntervalConstraint(org.eclipse.uml2.uml.IntervalConstraint)
		 */
		@Override
		public Set<EObject> caseIntervalConstraint(IntervalConstraint object) {
			Set<EObject> result = new LinkedHashSet<EObject>();
			result.add(object);
			ValueSpecification value = object.getSpecification();
			if (value instanceof Interval) {
				ValueSpecification min = ((Interval)value).getMin();
				if (min != null) {
					result.add(min);
				}
				ValueSpecification max = ((Interval)value).getMax();
				if (max != null) {
					result.add(max);
				}
			}
			return result;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseInterval(org.eclipse.uml2.uml.Interval)
		 */
		@Override
		public Set<EObject> caseInterval(Interval object) {
			Set<EObject> result = new LinkedHashSet<EObject>();
			if (object.eContainer() instanceof IntervalConstraint) {
				result.addAll(caseIntervalConstraint((IntervalConstraint)object.eContainer()));
			}
			return result;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseValueSpecification(org.eclipse.uml2.uml.ValueSpecification)
		 */
		@Override
		public Set<EObject> caseValueSpecification(ValueSpecification object) {
			Set<EObject> result = new LinkedHashSet<EObject>();
			EObject container = object.eContainer();
			if (container instanceof IntervalConstraint || container instanceof ValueSpecification) {
				result.addAll(doSwitch(object.eContainer()));
			} else {
				final Setting setting = getInverseReferences(object,
						new Predicate<EStructuralFeature.Setting>() {
							public boolean apply(EStructuralFeature.Setting input) {
								return ((input.getEStructuralFeature() == UMLPackage.Literals.INTERVAL__MIN
										|| input.getEStructuralFeature() == UMLPackage.Literals.INTERVAL__MAX))
										&& input.getEObject().eContainer() instanceof IntervalConstraint;
							}
						});
				if (setting != null) {
					IntervalConstraint intervalConstraint = (IntervalConstraint)((Interval)setting
							.getEObject()).eContainer();
					result.addAll(caseIntervalConstraint(intervalConstraint));
				}
			}
			return result;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#getExtensionKind()
	 */
	@Override
	public Class<? extends UMLDiff> getExtensionKind() {
		return IntervalConstraintChange.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#createExtension()
	 */
	@Override
	public UMLDiff createExtension() {
		return UMLCompareFactory.eINSTANCE.createIntervalConstraintChange();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#getDiscriminant(org.eclipse.emf.compare.Diff)
	 */
	@Override
	protected EObject getDiscriminant(Diff input) {
		return Iterables.find(getDiscriminants(input), instanceOf(IntervalConstraint.class), null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.postprocessor.AbstractUMLChangeFactory#getDiscriminantsGetter()
	 */
	@Override
	protected Switch<Set<EObject>> getDiscriminantsGetter() {
		return new IntervalConstraintGetter();
	}

}
