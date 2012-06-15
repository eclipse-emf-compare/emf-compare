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

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

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
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

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
	 * helper used to check for equality of values or EObjects.
	 */
	private EqualityHelper helper;

	/**
	 * Create the conflict detector.
	 * 
	 * @param helper
	 *            the same equality helper used to detect the diffs.
	 */
	public DefaultConflictDetector(EqualityHelper helper) {
		this.helper = helper;
	}

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
		/*
		 * DELETE diffs can conflict with every other if on containment references, only with MOVE or other
		 * DELETE otherwise.
		 */
		/*
		 * ADD diffs can only conflict with "DELETE" or "ADD" ones ... Most will be detected on the DELETE.
		 * However, ADD diffs on containment reference can conflict with other ADDs on the same match.
		 */
		// CHANGE diffs can only conflict with other CHANGE or DELETE ... here again detected on the DELETE
		// MOVE diffs can conflict with DELETE ones, detected on the delete, or with other MOVE diffs.
		if (diff instanceof ReferenceChange && ((ReferenceChange)diff).getReference().isContainment()) {
			checkContainmentConflict(comparison, (ReferenceChange)diff, Iterables.filter(candidates,
					ReferenceChange.class));
		} else {
			switch (diff.getKind()) {
				case DELETE:
					checkFeatureDeleteConflict(comparison, diff, candidates);
					break;
				case CHANGE:
					checkFeatureChangeConflict(comparison, diff, candidates);
					break;
				case MOVE:
					checkFeatureMoveConflict(comparison, diff, candidates);
					break;
				case ADD:
					checkFeatureAddConflict(comparison, diff, candidates);
					break;
				default:
					break;
			}
		}
	}

	/**
	 * This will be called once for each ReferenceChange on containment references in the comparison model.
	 * 
	 * @param comparison
	 *            The originating comparison of those diffs.
	 * @param diff
	 *            The reference change for which we are to try and determine conflicts.
	 * @param candidates
	 *            An iterable over the ReferenceChanges that are possible candidates for conflicts.
	 */
	protected void checkContainmentConflict(Comparison comparison, ReferenceChange diff,
			Iterable<ReferenceChange> candidates) {
		final Match valueMatch = comparison.getMatch(diff.getValue());

		for (ReferenceChange candidate : candidates) {
			if (valueMatch.getLeft() == candidate.getValue() || valueMatch.getRight() == candidate.getValue()
					|| valueMatch.getOrigin() == candidate.getValue()) {
				checkContainmentConflict(comparison, diff, candidate);
			}
		}

		// Every Diff "under" a containment deletion conflicts with it.
		if (diff.getKind() == DifferenceKind.DELETE) {
			final Predicate<? super Diff> candidateFilter = new ConflictCandidateFilter(diff);
			for (Diff extendedCandidate : Iterables.filter(valueMatch.getAllDifferences(), candidateFilter)) {
				if (isDeleteOrUnsetDiff(comparison, extendedCandidate)) {
					conflictOn(comparison, diff, extendedCandidate, ConflictKind.PSEUDO);
				} else {
					conflictOn(comparison, diff, extendedCandidate, ConflictKind.REAL);
				}
			}
		}
	}

	/**
	 * For each couple of diffs on the same value in which one is a containment reference change, we will call
	 * this in order to check for possible conflicts.
	 * <p>
	 * Once here, we know that {@code diff} is a containment reference change, and we known that {@code diff}
	 * and {@code candidate} are both pointing to the same value. {@code candidate} can be a containment
	 * reference change, but that is not a given.
	 * </p>
	 * 
	 * @param comparison
	 *            The originating comparison of those diffs.
	 * @param diff
	 *            Containment reference changes for which we need to check possible conflicts.
	 * @param candidate
	 *            A reference change that point to the same value as {@code diff}.
	 */
	protected void checkContainmentConflict(Comparison comparison, ReferenceChange diff,
			ReferenceChange candidate) {
		if (candidate.getReference().isContainment()) {
			// The same value has been changed on both sides in containment references
			// This is a conflict, but is it a pseudo-conflict?
			ConflictKind kind = ConflictKind.REAL;
			final boolean diffIsDelete = isDeleteOrUnsetDiff(comparison, diff);
			final boolean candidateIsDelete = isDeleteOrUnsetDiff(comparison, candidate);
			if (diffIsDelete && candidateIsDelete) {
				kind = ConflictKind.PSEUDO;
			} else if (diff.getMatch() == candidate.getMatch()
					&& diff.getReference() == candidate.getReference()) {
				// Same value added in the same container/reference couple
				if (!diffIsDelete
						&& !candidateIsDelete
						&& matchingIndices(comparison, diff.getMatch(), diff.getReference(), diff.getValue(),
								candidate.getValue())) {
					kind = ConflictKind.PSEUDO;
				}
			}
			conflictOn(comparison, diff, candidate, kind);
		} else if (diff.getKind() == DifferenceKind.DELETE) {
			/*
			 * We removed an element from its containment difference, but it has been used in some way on the
			 * other side.
			 */
			if (candidate.getKind() == DifferenceKind.DELETE) {
				// No conflict here
			} else {
				// Be it added, moved or changed, this is a REAL conflict
				conflictOn(comparison, diff, candidate, ConflictKind.REAL);
			}
		}
	}

	/**
	 * This will be called from {@link #checkConflict(Comparison, Diff, Iterable)} in order to detect
	 * conflicts on a Diff that is of type "CHANGE".
	 * <p>
	 * Those can only conflict with other CHANGE Diffs on the same reference.
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
	protected void checkFeatureChangeConflict(Comparison comparison, Diff diff, Iterable<Diff> candidates) {
		final Object changedValue;
		final EStructuralFeature feature;
		if (diff instanceof ReferenceChange) {
			changedValue = ((ReferenceChange)diff).getValue();
			feature = ((ReferenceChange)diff).getReference();
		} else {
			changedValue = ((AttributeChange)diff).getValue();
			feature = ((AttributeChange)diff).getAttribute();
		}

		final Iterable<Diff> refinedCandidates = Iterables.filter(candidates, new Predicate<Diff>() {
			public boolean apply(Diff input) {
				boolean apply = false;
				if (input.getKind() == DifferenceKind.CHANGE) {
					if (input instanceof ReferenceChange) {
						apply = ((ReferenceChange)input).getReference() == feature;
					} else if (input instanceof AttributeChange) {
						apply = ((AttributeChange)input).getAttribute() == feature;
					}
				}
				return apply;
			}
		});

		for (Diff candidate : refinedCandidates) {
			final Object candidateValue;
			if (candidate instanceof ReferenceChange) {
				candidateValue = ((ReferenceChange)candidate).getValue();
			} else {
				candidateValue = ((AttributeChange)candidate).getValue();
			}

			if (diff.getMatch() == candidate.getMatch()) {
				if (helper.matchingValues(comparison, changedValue, candidateValue)) {
					// Same value added on both side in the same container
					conflictOn(comparison, diff, candidate, ConflictKind.PSEUDO);
				} else {
					conflictOn(comparison, diff, candidate, ConflictKind.REAL);
				}
			}
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
	protected void checkFeatureMoveConflict(Comparison comparison, Diff diff, Iterable<Diff> candidates) {
		final Object changedValue;
		final EStructuralFeature feature;
		if (diff instanceof ReferenceChange) {
			changedValue = ((ReferenceChange)diff).getValue();
			feature = ((ReferenceChange)diff).getReference();
		} else {
			changedValue = ((AttributeChange)diff).getValue();
			feature = ((AttributeChange)diff).getAttribute();
		}

		final Iterable<Diff> refinedCandidates = Iterables.filter(candidates, new Predicate<Diff>() {
			public boolean apply(Diff input) {
				boolean apply = false;
				if (input.getKind() == DifferenceKind.MOVE) {
					if (input instanceof ReferenceChange) {
						apply = ((ReferenceChange)input).getReference() == feature;
					} else if (input instanceof AttributeChange) {
						apply = ((AttributeChange)input).getAttribute() == feature;
					}
				}
				return apply;
			}
		});

		for (Diff candidate : refinedCandidates) {
			final Object candidateValue;
			if (candidate instanceof ReferenceChange) {
				candidateValue = ((ReferenceChange)candidate).getValue();
			} else {
				candidateValue = ((AttributeChange)candidate).getValue();
			}

			if (diff.getMatch() == candidate.getMatch()
					&& helper.matchingValues(comparison, changedValue, candidateValue)) {
				// Same value moved in both side of the same container
				if (matchingIndices(comparison, diff.getMatch(), feature, changedValue, candidateValue)) {
					conflictOn(comparison, diff, candidate, ConflictKind.PSEUDO);
				} else {
					conflictOn(comparison, diff, candidate, ConflictKind.REAL);
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
		final EStructuralFeature feature;
		if (diff instanceof ReferenceChange) {
			final Match valueMatch = comparison.getMatch(((ReferenceChange)diff).getValue());
			if (diff.getSource() == DifferenceSource.LEFT) {
				deletedValue = valueMatch.getRight();
			} else {
				deletedValue = valueMatch.getLeft();
			}
			feature = ((ReferenceChange)diff).getReference();
		} else {
			deletedValue = ((AttributeChange)diff).getValue();
			feature = ((AttributeChange)diff).getAttribute();
		}

		/*
		 * The only potential conflict with the deletion of a feature value is a move or delete concerning
		 * that value on the opposite side (the "feature" cannot be a containment reference, those are handled
		 * through #checkContainmentDeleteConflict).
		 */
		final Iterable<Diff> refinedCandidates = Iterables.filter(candidates, new Predicate<Diff>() {
			public boolean apply(Diff input) {
				boolean apply = false;
				if (input.getKind() == DifferenceKind.MOVE || input.getKind() == DifferenceKind.DELETE) {
					if (input instanceof ReferenceChange) {
						apply = ((ReferenceChange)input).getReference() == feature;
					} else if (input instanceof AttributeChange) {
						apply = ((AttributeChange)input).getAttribute() == feature;
					}
				}
				return apply;
			}
		});

		for (Diff candidate : refinedCandidates) {
			final Object movedValue;
			if (candidate instanceof ReferenceChange) {
				movedValue = ((ReferenceChange)candidate).getValue();
			} else {
				movedValue = ((AttributeChange)candidate).getValue();
			}

			if (helper.matchingValues(comparison, deletedValue, movedValue)) {
				if (candidate.getKind() == DifferenceKind.MOVE) {
					conflictOn(comparison, diff, candidate, ConflictKind.REAL);
				} else {
					conflictOn(comparison, diff, candidate, ConflictKind.PSEUDO);
				}
			}
		}
	}

	/**
	 * This will be called from {@link #checkConflict(Comparison, Diff, Iterable)} in order to detect
	 * conflicts on a Diff that is of type "ADD" and which is <b>not</b> a containment reference change.
	 * <p>
	 * These will conflict with Diffs on the other side on the same reference in the same container, of type
	 * ADD an on the same value.
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
	protected void checkFeatureAddConflict(final Comparison comparison, final Diff diff,
			Iterable<Diff> candidates) {
		final Object addedValue;
		final EStructuralFeature feature;
		if (diff instanceof ReferenceChange) {
			addedValue = ((ReferenceChange)diff).getValue();
			feature = ((ReferenceChange)diff).getReference();
		} else {
			addedValue = ((AttributeChange)diff).getValue();
			feature = ((AttributeChange)diff).getAttribute();
		}

		/*
		 * Can only conflict on Diffs : of type ADD, on the opposite side, in the same container and the same
		 * reference, with the same added value.
		 */
		final Iterable<Diff> refinedCandidates = Iterables.filter(candidates, new Predicate<Diff>() {
			public boolean apply(Diff input) {
				boolean apply = false;
				if (input.getKind() == DifferenceKind.ADD && diff.getMatch() == input.getMatch()) {
					if (input instanceof ReferenceChange) {
						apply = ((ReferenceChange)input).getReference() == feature;
					} else if (input instanceof AttributeChange) {
						apply = ((AttributeChange)input).getAttribute() == feature;
					}
				}
				return apply;
			}
		});

		for (Diff candidate : refinedCandidates) {
			final Object candidateValue;
			if (candidate instanceof ReferenceChange) {
				candidateValue = ((ReferenceChange)candidate).getValue();
			} else {
				candidateValue = ((AttributeChange)candidate).getValue();
			}
			// No diff on non unique features : multiple same values can coexist
			if (feature.isUnique() && helper.matchingValues(comparison, addedValue, candidateValue)) {
				// This is a conflict. Is it real?
				if (matchingIndices(comparison, diff.getMatch(), feature, addedValue, candidateValue)) {
					conflictOn(comparison, diff, candidate, ConflictKind.PSEUDO);
				} else {
					conflictOn(comparison, diff, candidate, ConflictKind.REAL);
				}
			}
		}
	}

	/**
	 * This will be called in order to check whether the given diff represents a DELETE or "CHANGE to default"
	 * diff. This serves the purpose of determining whether we are in th presence of a real or pseudo
	 * conflict.
	 * 
	 * @param comparison
	 *            The originating comparison of this diff.
	 * @param diff
	 *            Diff we are to check.
	 * @return {@code true} if {@code diff} is of "DELETE" kind, or if it is of "CHANGE" kind and the new
	 *         value of the corresponding feature is the default of that feature.
	 */
	private static boolean isDeleteOrUnsetDiff(Comparison comparison, Diff diff) {
		boolean deleteOrUnset = false;
		if (diff.getKind() == DifferenceKind.DELETE) {
			deleteOrUnset = true;
		} else if (diff instanceof ReferenceChange) {
			final EObject value = ((ReferenceChange)diff).getValue();
			final Match valueMatch = comparison.getMatch(value);

			deleteOrUnset = valueMatch != null && valueMatch.getOrigin() == value;
		} else if (diff.getKind() == DifferenceKind.CHANGE && diff instanceof AttributeChange) {
			final EAttribute attribute = ((AttributeChange)diff).getAttribute();
			final EObject expectedContainer;
			if (diff.getSource() == DifferenceSource.LEFT) {
				expectedContainer = diff.getMatch().getLeft();
			} else {
				expectedContainer = diff.getMatch().getRight();
			}

			final Object value = expectedContainer.eGet(attribute);
			// Though not the "default value", we consider that an empty string is an unset attribute.
			final Object defaultValue = attribute.getDefaultValue();
			deleteOrUnset = value == null || value.equals(defaultValue)
					|| (defaultValue == null && "".equals(value)); //$NON-NLS-1$
		}
		return deleteOrUnset;
	}

	/**
	 * This will be used whenever we check for conflictual MOVEs in order to determine whether we have a
	 * pseudo conflict or a real conflict.
	 * <p>
	 * Namely, this will retrieve the value of the given {@code feature} on the right and left sides of the
	 * given {@code match}, then check whether the two given values are on the same index.
	 * </p>
	 * <p>
	 * Note that no sanity checks will be made on either the match's sides or the feature.
	 * </p>
	 * 
	 * @param comparison
	 *            Provides us with the necessary information to match EObjects.
	 * @param match
	 *            Match for which we need to check a feature.
	 * @param feature
	 *            The feature which values we need to check.
	 * @param value1
	 *            First of the two values which index we are to compare.
	 * @param value2
	 *            Second of the two values which index we are to compare.
	 * @return {@code true} if the two given values are located at the same index in the given feature's
	 *         values list, {@code false} otherwise.
	 */
	@SuppressWarnings("unchecked")
	private boolean matchingIndices(Comparison comparison, Match match, EStructuralFeature feature,
			Object value1, Object value2) {
		boolean matching = false;
		if (feature.isMany()) {
			final List<Object> leftValues = (List<Object>)match.getLeft().eGet(feature);
			final List<Object> rightValues = (List<Object>)match.getRight().eGet(feature);

			// Using for loops instead of indexOf to handle non unique lists
			for (int i = 0; i < leftValues.size() && i < rightValues.size() && !matching; i++) {
				final Object left = leftValues.get(i);
				if (helper.matchingValues(comparison, left, value1)) {
					final Object right = rightValues.get(i);
					matching = helper.matchingValues(comparison, right, value2);
				}
			}

			for (int i = 0; i < leftValues.size() && i < rightValues.size() && !matching; i++) {
				final Object right = rightValues.get(i);
				if (helper.matchingValues(comparison, right, value1)) {
					final Object left = leftValues.get(i);
					matching = helper.matchingValues(comparison, left, value2);
				}
			}
		} else {
			matching = true;
		}
		return matching;
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
		Conflict toBeMerged = null;
		if (diff1.getConflict() != null) {
			conflict = diff1.getConflict();
			if (conflict.getKind() == ConflictKind.PSEUDO && conflict.getKind() != kind) {
				conflict.setKind(kind);
			}
			if (diff2.getConflict() != null) {
				// Merge the two
				toBeMerged = diff2.getConflict();
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

		final List<Diff> conflictDiffs = conflict.getDifferences();
		if (toBeMerged != null) {
			// These references are opposite. We can't simply iterate
			for (Diff aDiff : Lists.newArrayList(toBeMerged.getDifferences())) {
				if (!conflictDiffs.contains(aDiff)) {
					conflictDiffs.add(aDiff);
				}
			}
			if (toBeMerged.getKind() == ConflictKind.REAL && conflict.getKind() != ConflictKind.REAL) {
				conflict.setKind(ConflictKind.REAL);
			}
			EcoreUtil.remove(toBeMerged);
			toBeMerged.getDifferences().clear();
		}

		if (!conflict.getDifferences().contains(diff1)) {
			conflict.getDifferences().add(diff1);
		}
		if (!conflict.getDifferences().contains(diff2)) {
			conflict.getDifferences().add(diff2);
		}
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
}
