/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor;

import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isAddOrSetDiff;
import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isDeleteOrUnsetDiff;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.internal.postprocessor.factories.IChangeFactory;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.compare.uml2.internal.postprocessor.extension.UMLExtensionFactoryRegistry;
import org.eclipse.emf.compare.uml2.internal.postprocessor.util.UMLCompareUtil;
import org.eclipse.emf.ecore.EReference;

/**
 * Post-processor to create the UML difference extensions.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class UMLPostProcessor implements IPostProcessor {

	/** UML2 extensions factories. */
	private Set<IChangeFactory> uml2ExtensionFactories;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postMatch(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postMatch(Comparison comparison, Monitor monitor) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postDiff(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postDiff(Comparison comparison, Monitor monitor) {
		// Not needed here.
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postRequirements(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postRequirements(Comparison comparison, Monitor monitor) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postEquivalences(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postEquivalences(Comparison comparison, Monitor monitor) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postConflicts(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postConflicts(Comparison comparison, Monitor monitor) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postComparison(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postComparison(Comparison comparison, Monitor monitor) {

		final Map<Class<? extends Diff>, IChangeFactory> mapUml2ExtensionFactories = UMLExtensionFactoryRegistry
				.createExtensionFactories();
		uml2ExtensionFactories = new HashSet<IChangeFactory>(mapUml2ExtensionFactories.values());

		// Creation of the UML difference extensions
		for (Diff diff : comparison.getDifferences()) {
			applyManagedTypes(diff);
		}

		// Filling of the requirements link of the UML difference extensions
		for (Diff umlDiff : comparison.getDifferences()) {
			if (umlDiff instanceof UMLDiff) {
				final Class<?> classDiffElement = umlDiff.eClass().getInstanceClass();
				final IChangeFactory diffFactory = mapUml2ExtensionFactories.get(classDiffElement);
				if (diffFactory != null) {
					diffFactory.fillRequiredDifferences(comparison, (UMLDiff)umlDiff);
				}
			}
		}

		// Filling implications with subsets
		for (Diff diff : comparison.getDifferences()) {
			if (diff instanceof ReferenceChange) {
				fillImplicationsWithUMLSubsets((ReferenceChange)diff);
			}
		}

	}

	/**
	 * Creates the difference extensions in relation to the existing {@link DiffElement}s.
	 * 
	 * @param element
	 *            The input {@link DiffElement}.
	 */
	private void applyManagedTypes(Diff element) {
		for (IChangeFactory factory : uml2ExtensionFactories) {
			if (factory.handles(element)) {
				Diff extension = factory.create(element);
				// FIXME: Instantiation of UML extensions (intersections of predicates)
				if (!extension.getRefinedBy().isEmpty()) {
					final Match match = factory.getParentMatch(element);
					// FIXME: why the match may be null ? (see AddAssociation2Test.testMergeLtRA30UseCase)
					if (match != null) {
						match.getDifferences().add(extension);
					}
				} else {
					extension = null;
				}

			}
		}
	}

	/**
	 * Fill the implication links ({@link Diff#getImplies()}, {@link Diff#getImpliedBy()}) on the given
	 * reference change.
	 * 
	 * @param diff
	 *            The reference change.
	 */
	private void fillImplicationsWithUMLSubsets(ReferenceChange diff) {
		EReference reference = diff.getReference();
		// ADD implies ADD on non union supersets
		// DELETE is implied by DEL on non union supersets
		for (EReference superSet : UMLCompareUtil.getNonUnionSupersetReferences(reference)) {
			Comparison comparison = diff.getMatch().getComparison();
			for (Diff superSetDiff : comparison.getDifferences(superSet)) {
				// Only keep diffs on the same ref and value where parent matches
				if (superSetDiff instanceof ReferenceChange
						&& ((ReferenceChange)superSetDiff).getReference() == superSet
						&& ((ReferenceChange)superSetDiff).getValue() == diff.getValue()
						&& ((ReferenceChange)superSetDiff).getMatch() == diff.getMatch()) {
					if (isAddOrSetDiff(diff) && isAddOrSetDiff(superSetDiff)) {
						diff.getImplies().add(superSetDiff);
					} else if (isDeleteOrUnsetDiff(diff) && isDeleteOrUnsetDiff(superSetDiff)) {
						diff.getImpliedBy().add(superSetDiff);
					}
				}
			}
		}
	}
}
