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

import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChange;
import org.eclipse.emf.compare.uml2diff.Uml2diffFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Interval;
import org.eclipse.uml2.uml.IntervalConstraint;
import org.eclipse.uml2.uml.ValueSpecification;

/**
 * Factory for UMLIntervalConstraintChangeLeftTarget.
 */
public class UMLIntervalConstraintChangeFactory extends AbstractDiffExtensionFactory {

	/**
	 * Constructor.
	 */
	public UMLIntervalConstraintChangeFactory() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#handles(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public boolean handles(Diff input) {
		return input instanceof ReferenceChange && ((ReferenceChange)input).getReference().isContainment()
				&& ((ReferenceChange)input).getValue() instanceof IntervalConstraint;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#create(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	public Diff create(Diff input, EcoreUtil.CrossReferencer crossReferencer) {
		final ReferenceChange referenceChange = (ReferenceChange)input;
		final IntervalConstraint intervalConstraint = (IntervalConstraint)referenceChange.getValue();

		final UMLIntervalConstraintChange ret = Uml2diffFactory.eINSTANCE.createUMLIntervalConstraintChange();

		final ValueSpecification valueSpecification = intervalConstraint.getSpecification();

		if (valueSpecification instanceof Interval) {
			final ValueSpecification min = ((Interval)valueSpecification).getMin();
			beRefinedByCrossReferences(min, ComparePackage.Literals.REFERENCE_CHANGE__VALUE, ret,
					crossReferencer);
			final ValueSpecification max = ((Interval)valueSpecification).getMax();
			beRefinedByCrossReferences(max, ComparePackage.Literals.REFERENCE_CHANGE__VALUE, ret,
					crossReferencer);
		}

		ret.getRefinedBy().add(input);

		return ret;
	}
}
