/*******************************************************************************
 * Copyright (c) 2019 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.utils;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isDeleteOrUnsetDiff;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.FeatureMapChange;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceLocationChange;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * This class will provide a number of Predicates that can be used to retrieve particular {@link Diff}s from
 * an iterable.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class EMFCompareJavaPredicates {

	/**
	 * Predicate builder for diffs that can conflict with the given diff.
	 * 
	 * @param diff
	 *            The diff
	 * @return A predicate that accepts diffs that might conflict with the given diff.
	 */
	public static Predicate<Diff> possiblyConflictingWith(Diff diff) {
		return new ConflictCandidateFilter(diff);
	}

	/**
	 * This predicate can be used to check whether a particular diff is of the given {@code kind}. This is
	 * mainly used to differentiate additions from deletions.
	 * 
	 * @param kind
	 *            The kind we expect this diff to have.
	 * @return The created predicate.
	 */
	public static Predicate<? super Diff> ofKind(DifferenceKind... kind) {
		return new Predicate<Diff>() {
			public boolean test(Diff input) {
				if (input != null) {
					return Arrays.asList(kind).contains(input.getKind());
				}
				return false;
			}
		};
	}

	/**
	 * Accept only diffs that inherit either AttributeChange, ReferenceChange, or FeatureMapChange that
	 * concern the given feature.
	 * 
	 * @param feature
	 *            Feature to deal with
	 * @return a new predicate that accepts diffs that concern the given feature.
	 */
	public static Predicate<Diff> onFeature(EStructuralFeature feature) {
		return new OnFeature(feature);
	}

	/**
	 * Accept only diffs whose value matches the given value.
	 * 
	 * @param helper
	 *            The helper to match values
	 * @param value
	 *            The value to match
	 * @return The created predicate.
	 */
	public static Predicate<Diff> valueMatches(final IEqualityHelper helper, final Object value) {
		return new Predicate<Diff>() {
			public boolean test(Diff input) {
				if (input instanceof ReferenceChange) {
					return helper.matchingValues(value, ((ReferenceChange)input).getValue());
				} else if (input instanceof AttributeChange) {
					return helper.matchingValues(value, ((AttributeChange)input).getValue());
				} else if (input instanceof FeatureMapChange) {
					return helper.matchingValues(value,
							((FeatureMap.Entry)((FeatureMapChange)input).getValue()).getValue());
				}
				return false;
			}
		};
	}

	/**
	 * This will be used to filter out the list of potential candidates for conflict with a given Diff.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private static final class ConflictCandidateFilter implements Predicate<Diff> {
		/** The Diff for which we seek conflict candidates. */
		private final Diff diff;

		/**
		 * Instantiates our filtering Predicate given the reference Diff for which to seek potential
		 * conflicts.
		 * 
		 * @param diff
		 *            The Diff for which we seek conflict candidates, must not be null.
		 */
		ConflictCandidateFilter(Diff diff) {
			this.diff = Objects.requireNonNull(diff);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.google.common.base.Predicate#apply(java.lang.Object)
		 */
		public boolean test(Diff input) {
			return !(input instanceof ResourceLocationChange) && canConflictWith(input);
		}

		/**
		 * Checks if the given {@link Diff diff1} can be in conflict with the given {@link Diff diff2}.
		 * <p>
		 * Notably, we don't need to try and detect a conflict between two diffs if they're one and the same
		 * or if they have already been detected as a conflicting couple. Likewise, there can be no conflict
		 * if the two diffs originate from the same side.
		 * </p>
		 * <p>
		 * bug 381143 : we'll also remove any containment deletion diff on other Matches from here.
		 * </p>
		 * 
		 * @param other
		 *            candidate difference to consider for conflict detection.
		 * @return {@code true} if the two given diffs can conflict, {@code false} otherwise.
		 */
		private boolean canConflictWith(Diff other) {
			if (diff == other || diff.getSource() == other.getSource()) {
				return false;
			}
			Conflict conflict = diff.getConflict();
			boolean canConflict = false;
			if (conflict == null || !conflict.getDifferences().contains(other)) {
				if (diff.getMatch() != other.getMatch() && other instanceof ReferenceChange
						&& ((ReferenceChange)other).getReference().isContainment()) {
					canConflict = !isDeleteOrUnsetDiff(other);
				} else {
					canConflict = true;
				}
			}
			return canConflict;
		}
	}

	/**
	 * Predicate for diffs taht concern a given feature.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private static class OnFeature implements Predicate<Diff> {
		/** The feature. */
		private final EStructuralFeature feature;

		/**
		 * Constructor.
		 * 
		 * @param feature
		 *            the feature
		 */
		OnFeature(EStructuralFeature feature) {
			this.feature = checkNotNull(feature);
		}

		/**
		 * Apply the predicate.
		 * 
		 * @param input
		 *            The diff to filter.
		 * @return true if and only if input concerns the given feature.
		 */
		public boolean test(Diff input) {
			if (input == null) {
				return false;
			}
			boolean apply = false;
			if (input instanceof ReferenceChange) {
				apply = ((ReferenceChange)input).getReference() == feature;
			} else if (input instanceof AttributeChange) {
				apply = ((AttributeChange)input).getAttribute() == feature;
			} else if (input instanceof FeatureMapChange) {
				apply = ((FeatureMapChange)input).getAttribute() == feature;
			}
			return apply;
		}
	}
}
