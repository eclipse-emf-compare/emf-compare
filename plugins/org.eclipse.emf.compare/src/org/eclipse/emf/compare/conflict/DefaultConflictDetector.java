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
package org.eclipse.emf.compare.conflict;

import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.utils.EqualityHelper;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * The conflict detector is in charge of refining the Comparison model with all detected Conflict between its
 * differences.
 * <p>
 * This default implementation of {@link IConflictDetector} should detect most generic cases, but is not aimed
 * at detecting conflicts at "business" level. For example, adding two enum literals of the same value but
 * distinct IDs might be seen as a conflict... but that is not the "generic" case.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class DefaultConflictDetector implements IConflictDetector {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.conflict.IConflictDetector#detect(org.eclipse.emf.compare.Comparison)
	 */
	public void detect(Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();
		final int diffCount = differences.size();

		for (int i = 0; i < diffCount; i++) {
			final Diff diff = differences.get(i);

			final Predicate<? super Diff> candidateFilter = new ConflictCandidateFilter(diff);
			checkConflict(comparison, diff, Iterables.filter(differences, candidateFilter));
		}
	}

	/**
	 * This will be called once for each difference in the comparison model.
	 * 
	 * @param comparison
	 *            The originating comparison of those diffs.
	 * @param diff
	 *            Diff for which we are to try and determine conflicts.
	 * @param candidates
	 *            An iterable over the Diffs that possible candidates for conflicts.
	 */
	protected void checkConflict(Comparison comparison, Diff diff, Iterable<Diff> candidates) {
		// DELETE diffs can conflict with every other if on containment references, only with MOVE otherwise
		// ADD diffs can only conflict with "DELETE" ones ... These will be detected on the DELETE
		// CHANGE diffs can only conflict with other CHANGE or DELETE ... here again detected on the DELETE
		// MOVE diffs can conflict with DELETE ones, detected on the delete, or with other MOVE diffs.
		if (diff.getKind() == DifferenceKind.DELETE) {
			if (diff instanceof ReferenceChange && ((ReferenceChange)diff).getReference().isContainment()) {
				checkContainmentDeleteConflict(comparison, (ReferenceChange)diff, candidates);
			} else {
				// attribute or reference, the only potential conflict is a "move" on the other side
				checkFeatureDeleteConflict(comparison, diff, candidates);
			}
		} else if (diff.getKind() == DifferenceKind.CHANGE) {
			checkFeatureChangeOrMoveConflict(comparison, diff, candidates);
		} else if (diff.getKind() == DifferenceKind.MOVE) {
			checkFeatureChangeOrMoveConflict(comparison, diff, candidates);
		} else {
			// no possible conflict on a "ADD" diff that wouldn't be deleted by the other paths
		}
	}

	/**
	 * This will be called from {@link #checkConflict(Comparison, Diff, Iterable)} in order to detect
	 * conflicts on a Diff that is of type "CHANGE" or "MOVE".
	 * <p>
	 * Those can only conflict with other Diffs of the same type on the same reference.
	 * </p>
	 * 
	 * @param comparison
	 *            The originating comparison of those diffs.
	 * @param diff
	 *            The diff which we are to check for conflicts.
	 * @param candidates
	 *            The list of candidates for a conflict. This list only contains Diff from the side opposite
	 *            to {@code diff}.
	 */
	protected void checkFeatureChangeOrMoveConflict(Comparison comparison, Diff diff,
			Iterable<Diff> candidates) {
		// The only possible conflict for a "change" diff is another "change" on the same feature
		final Object changedValue;
		final EStructuralFeature feature;
		if (diff instanceof ReferenceChange) {
			changedValue = ((ReferenceChange)diff).getValue();
			feature = ((ReferenceChange)diff).getReference();
		} else {
			changedValue = ((AttributeChange)diff).getValue();
			feature = ((AttributeChange)diff).getAttribute();
		}

		final Iterable<Diff> changeCandidates = Iterables.filter(candidates, ofKind(diff.getKind()));
		for (Diff candidate : changeCandidates) {
			if (candidate instanceof ReferenceChange) {
				final EStructuralFeature candidateFeature = ((ReferenceChange)candidate).getReference();
				final Object candidateChanged = ((ReferenceChange)candidate).getValue();
				if (candidateFeature == feature
						&& !EqualityHelper.matchingValues(comparison, changedValue, candidateChanged)) {
					conflictOn(comparison, diff, candidate, ConflictKind.REAL);
				} else if (candidateFeature == feature) {
					conflictOn(comparison, diff, candidate, ConflictKind.PSEUDO);
				}
			} else if (candidate instanceof AttributeChange) {
				final EStructuralFeature candidateFeature = ((AttributeChange)candidate).getAttribute();
				final Object candidateChanged = ((AttributeChange)candidate).getValue();
				if (candidateFeature == feature
						&& !EqualityHelper.matchingValues(comparison, changedValue, candidateChanged)) {
					conflictOn(comparison, diff, candidate, ConflictKind.REAL);
				} else if (candidateFeature == feature) {
					conflictOn(comparison, diff, candidate, ConflictKind.PSEUDO);
				}
			}
		}
	}

	/**
	 * This will be called from {@link #checkConflict(Comparison, Diff, Iterable)} in order to detect
	 * conflicts on a Diff that is of type "DELETE" and which is <b>not</b> a containment reference change.
	 * <p>
	 * The only potential conflict for such a diff is a "MOVE" of that same value on the opposite side.
	 * </p>
	 * 
	 * @param comparison
	 *            The originating comparison of those diffs.
	 * @param diff
	 *            The diff which we are to check for conflicts.
	 * @param candidates
	 *            The list of candidates for a conflict. This list only contains Diff from the side opposite
	 *            to {@code diff}.
	 */
	protected void checkFeatureDeleteConflict(Comparison comparison, Diff diff, Iterable<Diff> candidates) {
		final Object deletedValue;
		if (diff instanceof ReferenceChange) {
			final Match valueMatch = comparison.getMatch(((ReferenceChange)diff).getValue());
			if (diff.getSource() == DifferenceSource.LEFT) {
				deletedValue = valueMatch.getRight();
			} else {
				deletedValue = valueMatch.getLeft();
			}
		} else {
			deletedValue = ((AttributeChange)diff).getValue();
		}

		/*
		 * The only potential conflict with the deletion of a feature value is a move concerning that value on
		 * the opposite side (the "feature" cannot be a containment reference, those are handled through
		 * #checkContainmentDeleteConflict)
		 */
		for (Diff candidate : candidates) {
			if (candidate.getKind() == DifferenceKind.MOVE) {
				final Object movedValue;
				if (candidate instanceof ReferenceChange) {
					movedValue = ((ReferenceChange)candidate).getValue();
				} else {
					movedValue = ((AttributeChange)candidate).getValue();
				}

				if (EqualityHelper.matchingValues(comparison, deletedValue, movedValue)) {
					conflictOn(comparison, diff, candidate, ConflictKind.REAL);
				}
			}
		}
	}

	/**
	 * This will be called from {@link #checkConflict(Comparison, Diff, Iterable)} in order to detect
	 * conflicts on a Diff that is of type "DELETE" on a containment reference.
	 * <p>
	 * Such diffs can conflict with most others, notably, <b>all</b> differences of the opposite side located
	 * under the deleted element are conflicts.
	 * </p>
	 * 
	 * @param comparison
	 *            The originating comparison of those diffs.
	 * @param diff
	 *            The diff which we are to check for conflicts.
	 * @param candidates
	 *            The list of candidates for a conflict. This list only contains Diff from the side opposite
	 *            to {@code diff}.
	 */
	protected void checkContainmentDeleteConflict(Comparison comparison, ReferenceChange diff,
			Iterable<Diff> candidates) {
		final Match deletedMatch = comparison.getMatch(diff.getValue());

		// If we only have an origin, we can only have a pseudo conflict with an identical diff on the other
		// side.
		if (deletedMatch.getLeft() == null && deletedMatch.getRight() == null) {
			final Predicate<? super Diff> oppositeDiffPredicate = new OppositeSideDiff(diff);

			final Diff oppositeDiff = getFirst(candidates, oppositeDiffPredicate);
			if (oppositeDiff != null) {
				conflictOn(comparison, diff, oppositeDiff, ConflictKind.PSEUDO);
			}
		} else {
			final EObject deletedValue;
			// The list of candidates only sports diffs of the opposite side. The "deleted value" is located
			// on that "opposite" side.
			if (diff.getSource() == DifferenceSource.LEFT) {
				deletedValue = deletedMatch.getRight();
			} else {
				deletedValue = deletedMatch.getLeft();
			}

			for (Diff candidate : candidates) {
				if (candidate.getMatch() == deletedMatch) {
					// "candidate" is a diff on an Object that has been deleted on the other side
					boolean pseudoConflict = false;
					if (candidate instanceof ReferenceChange) {
						final EObject value = ((ReferenceChange)candidate).getValue();
						final Match valueMatch = comparison.getMatch(value);

						pseudoConflict = valueMatch != null && valueMatch.getOrigin() == value;
					}
					final ConflictKind kind;
					if (pseudoConflict) {
						kind = ConflictKind.PSEUDO;
					} else {
						kind = ConflictKind.REAL;
					}
					conflictOn(comparison, diff, candidate, kind);
				} else if (candidate instanceof ReferenceChange) {
					/*
					 * The only potential conflict here is if the candidate is a ReferenceChange of either
					 * "ADD" or "CHANGE" kind which value is the deleted element.
					 */
					if (candidate.getKind() == DifferenceKind.ADD
							|| candidate.getKind() == DifferenceKind.CHANGE
							&& ((ReferenceChange)candidate).getValue() == deletedValue) {
						conflictOn(comparison, diff, candidate, ConflictKind.REAL);
					}
				}
			}
		}
	}

	/**
	 * This will be called whenever we detect a new conflict in order to create (or update) the actual
	 * association.
	 * 
	 * @param comparison
	 *            The originating comparison of those diffs.
	 * @param diff1
	 *            First of the two differences for which we detected a conflict.
	 * @param diff2
	 *            Second of the two differences for which we detected a conflict.
	 * @param kind
	 *            Kind of this conflict.
	 */
	protected void conflictOn(Comparison comparison, Diff diff1, Diff diff2, ConflictKind kind) {
		final Conflict conflict;
		if (diff1.getConflict() != null) {
			conflict = diff1.getConflict();
			if (conflict.getKind() == ConflictKind.PSEUDO && conflict.getKind() != kind) {
				conflict.setKind(kind);
			}
		} else if (diff2.getConflict() != null) {
			conflict = diff2.getConflict();
			if (conflict.getKind() == ConflictKind.PSEUDO && conflict.getKind() != kind) {
				conflict.setKind(kind);
			}
		} else {
			conflict = CompareFactory.eINSTANCE.createConflict();
			conflict.setKind(kind);
			comparison.getConflicts().add(conflict);
		}

		if (!conflict.getDifferences().contains(diff1)) {
			conflict.getDifferences().add(diff1);
		}
		if (!conflict.getDifferences().contains(diff2)) {
			conflict.getDifferences().add(diff2);
		}
	}

	/**
	 * Returns the first element returned by {@code iterable.iterator()} that satisfies the given predicate.
	 * 
	 * @param iterable
	 *            Iterable over which elements we are to iterate.
	 * @param predicate
	 *            The predicate which needs to be satisified.
	 * @param <T>
	 *            type of the elements we'll iterate over.
	 * @return The first element of {@code iterator} that satisfies {@code predicate}, {@code null} if none.
	 */
	private static <T> T getFirst(Iterable<T> iterable, Predicate<? super T> predicate) {
		final Iterator<T> iterator = iterable.iterator();
		while (iterator.hasNext()) {
			T element = iterator.next();
			if (predicate.apply(element)) {
				return element;
			}
		}
		return null;
	}

	/**
	 * This will be used to filter out the list of potential candidates for conflict with a given Diff.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private final class ConflictCandidateFilter implements Predicate<Diff> {
		/** The Diff for which we seek conflict candidates. */
		private final Diff reference;

		/**
		 * Instantiates our filtering Predicate given the reference Diff for which to seek potential
		 * conflicts.
		 * 
		 * @param reference
		 *            The Diff for which we seek conflict candidates.
		 */
		public ConflictCandidateFilter(Diff reference) {
			this.reference = reference;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.google.common.base.Predicate#apply(java.lang.Object)
		 */
		public boolean apply(Diff input) {
			return canConflictWith(reference, input);
		}

		/**
		 * Checks if the given {@link Diff diff1} can be in conflict with the given {@link Diff diff2}.
		 * Notably, we don't need to try and detect a conflict between two diffs if they one and the same or
		 * if they have already been detected as a conflicting couple. Likewise, there can be no conflict if
		 * the two diffs originate from the same side.
		 * 
		 * @param diff1
		 *            First of the two differences to consider for conflict detection.
		 * @param diff2
		 *            Second of the two differences to consider for conflict detection.
		 * @return {@code true} if the two given diffs can conflict, {@code false} otherwise.
		 */
		private boolean canConflictWith(Diff diff1, Diff diff2) {
			if (diff1 == diff2 || diff1.getSource() == diff2.getSource()) {
				return false;
			}
			final Conflict conflict = diff1.getConflict();

			return conflict == null || !conflict.getDifferences().contains(diff2);
		}
	}

	/**
	 * This will be used to try and find a Diff matching the given reference, but originating from the
	 * opposite side.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private final class OppositeSideDiff implements Predicate<Diff> {
		/** The reference diff for which we need a match. */
		private final ReferenceChange reference;

		/**
		 * Instantiates our predicate given the reference diff to try and match.
		 * 
		 * @param reference
		 *            The reference diff.
		 */
		public OppositeSideDiff(ReferenceChange reference) {
			this.reference = reference;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.google.common.base.Predicate#apply(java.lang.Object)
		 */
		public boolean apply(Diff input) {
			if (input instanceof ReferenceChange && input.getMatch() == reference.getMatch()
					&& ((ReferenceChange)input).getReference() == reference.getReference()
					&& input.getSource() != reference.getSource()) {
				// reference change on the same reference from the opposite side. We only care
				return input.getKind() == reference.getKind();
			}
			return false;
		}
	}
}
