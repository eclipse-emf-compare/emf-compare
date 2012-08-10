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
package org.eclipse.emf.compare.uml2.diff.internal.extension;

import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.uml2.UMLDiff;
import org.eclipse.emf.ecore.EObject;

/**
 * Factory for the difference extensions.
 */
public abstract class AbstractDiffExtensionFactory implements IDiffExtensionFactory {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#getParentDiff(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public Match getParentMatch(Diff input) {
		return input.getMatch();
	}

	/**
	 * Find the cross references of the given model object, through the specified feature, with a cross
	 * referencer and a predicate.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @param lookup
	 *            The model object.
	 * @param inFeature
	 *            The feature.
	 * @param p
	 *            The predicate.
	 * @param crossReferencer
	 *            The cross referencer.
	 * @return The cross references.
	 */
	protected final List<Diff> findCrossReferences(Comparison comparison, EObject lookup, Predicate<Diff> p) {
		final List<Diff> result = new ArrayList<Diff>();
		for (Diff diff : comparison.getDifferences(lookup)) {
			if (p.apply(diff)) {
				result.add(diff);
			}
		}
		return result;
	}

	/**
	 * Hide the difference elements from the given extension, from the specified model object, the feature and
	 * cross referencer, with a predicate.
	 * 
	 * @param lookup
	 *            The model object.
	 * @param inFeature
	 *            The feature.
	 * @param hiddingExtension
	 *            The extension
	 * @param p
	 *            The predicate
	 * @param crossReferencer
	 *            The cross referencer.
	 */
	protected final void beRefinedByCrossReferences(Comparison comparison, EObject lookup,
			UMLDiff refinedExtension, Predicate<Diff> p) {
		for (Diff diffElement : findCrossReferences(comparison, lookup, p)) {
			refinedExtension.getRefinedBy().add(diffElement);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#fillRequiredDifferences(org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension)
	 */
	public void fillRequiredDifferences(Comparison comparison, UMLDiff extension) {
		// Default behavior
	}

}
