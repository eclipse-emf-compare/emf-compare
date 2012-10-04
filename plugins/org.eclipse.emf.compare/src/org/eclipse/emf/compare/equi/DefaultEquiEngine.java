/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.equi;

import static com.google.common.collect.Iterables.filter;

import com.google.common.base.Predicate;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * The requirements engine is in charge of actually computing the equivalences between the differences.
 * <p>
 * This default implementation aims at being generic enough to be used for any model, whatever the metamodel.
 * However, specific requirements might be necessary.
 * </p>
 * TODO document available extension possibilities.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DefaultEquiEngine implements IEquiEngine {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.equi.IEquiEngine#computeEquivalences(org.eclipse.emf.compare.Comparison)
	 */
	public void computeEquivalences(Comparison comparison) {
		for (Diff difference : comparison.getDifferences()) {
			checkForEquivalences(comparison, difference);
		}
	}

	/**
	 * Checks the potential equivalence from the given <code>difference</code>.
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 * @param difference
	 *            The difference that is to be checked
	 */
	protected void checkForEquivalences(Comparison comparison, Diff difference) {
		if (difference instanceof ReferenceChange) {
			ReferenceChange referenceChange = (ReferenceChange)difference;
			EReference reference = referenceChange.getReference();
			EReference eOpposite = reference.getEOpposite();

			// If reference change on an opposite reference
			if (eOpposite != null && !eOpposite.isContainer() && !eOpposite.isDerived()) {
				checkForEquivalences(comparison, referenceChange);
			}
		}
	}

	/**
	 * Checks the potential equivalence from the given <code>difference</code>.
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 * @param referenceChange
	 *            The difference that is to be checked
	 */
	protected void checkForEquivalences(final Comparison comparison, final ReferenceChange referenceChange) {
		Equivalence equivalence = referenceChange.getEquivalence();
		if (equivalence == null) {
			// If no equivalence, create one
			equivalence = CompareFactory.eINSTANCE.createEquivalence();
			comparison.getEquivalences().add(equivalence);

			// Add the current difference to the equivalence
			equivalence.getDifferences().add(referenceChange);

			/*
			 * Add the difference where the value is the object containing the current difference, which is
			 * contained by the value of the current difference, where the reference is linked to the opposite
			 * one
			 */
			final Match valueMatch = comparison.getMatch(referenceChange.getValue());
			final EReference eOpposite = referenceChange.getReference().getEOpposite();
			if (eOpposite != null && valueMatch != null) {
				final Predicate<? super Diff> candidateFilter = new Predicate<Diff>() {
					public boolean apply(Diff input) {
						if (input instanceof ReferenceChange
								&& ((ReferenceChange)input).getReference() == eOpposite) {
							final Match candidateMatch = comparison.getMatch(((ReferenceChange)input)
									.getValue());
							return candidateMatch == referenceChange.getMatch();
						}
						return false;
					}
				};
				final Iterable<Diff> candidates = filter(valueMatch.getDifferences(), candidateFilter);

				for (Diff candidate : candidates) {
					equivalence.getDifferences().add(candidate);
				}

				addChangesFromOrigin(comparison, referenceChange, equivalence);
			}
		}
	}

	/**
	 * Add to the given <code>equivalence</code> potential changes based on the same reference as the given
	 * <code>diff</code>.
	 * 
	 * @param comparison
	 *            The comparison which enable to know from which side come the business model objects owning
	 *            the differences.
	 * @param diff
	 *            The current difference.
	 * @param equivalence
	 *            The current equivalence attached to the difference.
	 */
	private void addChangesFromOrigin(Comparison comparison, ReferenceChange diff, Equivalence equivalence) {
		if (!diff.getReference().isMany()) {
			final EObject originContainer = MatchUtil.getOriginContainer(diff);
			if (originContainer != null) {
				for (Diff referenceChange : comparison.getDifferences(originContainer)) {
					if (referenceChange instanceof ReferenceChange
							/*
							 * && MatchUtil.getContainer(comparison, referenceChange).equals(
							 * ReferenceUtil.safeEGet(originContainer, diff.getReference()))
							 */
							&& diff.getReference().equals(
									((ReferenceChange)referenceChange).getReference().getEOpposite())) {
						equivalence.getDifferences().add(referenceChange);
					}
				}
			}
		}
	}

}
