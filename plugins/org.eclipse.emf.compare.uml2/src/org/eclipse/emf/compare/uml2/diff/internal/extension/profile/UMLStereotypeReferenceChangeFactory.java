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

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChange;
import org.eclipse.emf.compare.uml2diff.Uml2diffFactory;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Factory for UMLStereotypeAttributeChangeLeftTarget.
 */
public class UMLStereotypeReferenceChangeFactory extends AbstractDiffExtensionFactory {

	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            The UML2 difference engine.
	 */
	public UMLStereotypeReferenceChangeFactory() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#handles(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public boolean handles(Diff input) {
		if (input instanceof ReferenceChange && !((ReferenceChange)input).getReference().isContainment()) {
			final EObject left = ((ReferenceChange)input).getMatch().getLeft();
			final EObject right = ((ReferenceChange)input).getMatch().getRight();
			final EObject leftBase = UMLUtil.getBaseElement(left);
			final EObject rightBase = UMLUtil.getBaseElement(right);
			return leftBase != null && rightBase != null;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#create(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	public Diff create(Diff input, EcoreUtil.CrossReferencer crossReferencer) {

		final UMLStereotypeReferenceChange ret = Uml2diffFactory.eINSTANCE
				.createUMLStereotypeReferenceChange();

		ret.getRefinedBy().add(input);

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory#getParentDiff(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	@Override
	public Match getParentMatch(Diff input, EcoreUtil.CrossReferencer crossReferencer) {
		final EObject right = ((AttributeChange)input).getMatch().getRight();
		final EObject rightBase = UMLUtil.getBaseElement(right);

		Comparison comparison = MatchUtil.getComparison(input);
		return comparison.getMatch(rightBase);
	}
}
