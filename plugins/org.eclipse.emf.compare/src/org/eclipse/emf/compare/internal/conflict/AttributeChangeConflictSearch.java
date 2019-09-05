/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.conflict;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static org.eclipse.emf.compare.ConflictKind.PSEUDO;
import static org.eclipse.emf.compare.ConflictKind.REAL;
import static org.eclipse.emf.compare.DifferenceKind.ADD;
import static org.eclipse.emf.compare.DifferenceKind.CHANGE;
import static org.eclipse.emf.compare.DifferenceKind.DELETE;
import static org.eclipse.emf.compare.DifferenceKind.MOVE;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onFeature;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.possiblyConflictingWith;
import static org.eclipse.emf.compare.utils.MatchUtil.matchingIndices;

import com.google.common.collect.Iterables;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.diff.FeatureFilter;
import org.eclipse.emf.ecore.EAttribute;

/**
 * Search conflicts for {@link AttributeChange}s.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class AttributeChangeConflictSearch {

	/**
	 * Search conflicts for {@link AttributeChange} of kind {@link DifferenceKind#ADD}.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class Add extends AbstractConflictSearch<AttributeChange> {

		/**
		 * Constructor.
		 * 
		 * @param diff
		 *            The diff to search for conflicts
		 * @param index
		 *            Comparison index, must not be null
		 * @param monitor
		 *            the monitor to report progress to, must not be null
		 */
		public Add(AttributeChange diff, ComparisonIndex index, Monitor monitor) {
			super(diff, index, monitor);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void detectConflicts() {
			EAttribute feature = diff.getAttribute();
			// Only unique features can have real conflicts
			Object value = diff.getValue();
			EList<Diff> diffsInSameMatch = diff.getMatch().getDifferences();
			Iterable<Diff> conflictCandidates = Iterables.filter(diffsInSameMatch,
					and(possiblyConflictingWith(diff), instanceOf(AttributeChange.class), onFeature(feature),
							ofKind(ADD)));
			if (feature.isUnique()) {
				for (Diff candidate : conflictCandidates) {
					Object candidateValue = ((AttributeChange)candidate).getValue();
					if (comparison.getEqualityHelper().matchingValues(value, candidateValue)) {
						// This is a conflict. Is it real?
						FeatureFilter featureFilter = getFeatureFilter(comparison);
						if (featureFilter == null || featureFilter.checkForOrderingChanges(feature)) {
							if (matchingIndices(diff.getMatch(), feature, value, candidateValue)) {
								conflict(candidate, PSEUDO);
							} else {
								conflict(candidate, REAL);
							}
						} else {
							conflict(candidate, PSEUDO);
						}
					}
				}
			} else {
				/*
				 * multiple same values can coexist on non-unique features, so we won't detect real conflicts
				 * in such cases. However, if a value is not present in the origin but added in both left and
				 * right, we'll consider it a pseudo conflict to avoid "noise" for the user. If the same value
				 * has been added multiple times on the side(s), we'll only detect pseudo conflict on pairs of
				 * additions and none if there is no longer a pair (i.e. the same value has been added one
				 * more times on one side than in the other).
				 */
				for (Diff candidate : conflictCandidates) {
					Object candidateValue = ((AttributeChange)candidate).getValue();
					if (comparison.getEqualityHelper().matchingValues(value, candidateValue)) {
						// potential pseudo-conflict
						// is this candidate already paired in a conflict?
						if (candidate.getConflict() != null
								&& candidate.getConflict().getKind() == ConflictKind.PSEUDO) {
							if (candidate.getConflict().getDifferences().stream()
									.filter(AttributeChange.class::isInstance)
									.anyMatch(conflictingWith -> matchingConflictingDiff(diff,
											(AttributeChange)conflictingWith))) {
								// continue to next candidate
								continue;
							}
						}
						conflict(candidate, PSEUDO);
						// break the loop to prevent further matching add conflicts
						break;
					}
				}
			}
		}

		private boolean matchingConflictingDiff(AttributeChange reference, AttributeChange candidate) {
			if (reference == candidate) {
				return false;
			}
			if (reference.getMatch() == candidate.getMatch() && reference.getKind() == candidate.getKind()) {
				Object referenceValue = reference.getValue();
				Object candidateValue = candidate.getValue();
				return comparison.getEqualityHelper().matchingValues(referenceValue, candidateValue);
			}
			return false;
		}
	}

	/**
	 * Search conflicts for {@link AttributeChange} of kind {@link DifferenceKind#CHANGE}.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class Change extends AbstractConflictSearch<AttributeChange> {

		/**
		 * Constructor.
		 * 
		 * @param diff
		 *            The diff to search for conflicts
		 * @param index
		 *            Comparison index, must not be null
		 * @param monitor
		 *            the monitor to report progress to, must not be null
		 */
		public Change(AttributeChange diff, ComparisonIndex index, Monitor monitor) {
			super(diff, index, monitor);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void detectConflicts() {
			Object value = diff.getValue();
			EAttribute feature = diff.getAttribute();
			EList<Diff> diffsInSameMatch = diff.getMatch().getDifferences();
			for (Diff candidate : Iterables.filter(diffsInSameMatch, and(possiblyConflictingWith(diff),
					instanceOf(AttributeChange.class), onFeature(feature), ofKind(CHANGE)))) {
				Object candidateValue = ((AttributeChange)candidate).getValue();
				if (comparison.getEqualityHelper().matchingValues(value, candidateValue)) {
					// Same value added on both side in the same container
					conflict(candidate, PSEUDO);
				} else if (!isFeatureMapChangeOrMergeableStringAttributeChange(diff, candidate)) {
					conflict(candidate, REAL);
				}
			}
		}
	}

	/**
	 * Search conflicts for {@link AttributeChange} of kind {@link DifferenceKind#DELETE}.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class Delete extends AbstractConflictSearch<AttributeChange> {

		/**
		 * Constructor.
		 * 
		 * @param diff
		 *            The diff to search for conflicts
		 * @param index
		 *            Comparison index, must not be null
		 * @param monitor
		 *            the monitor to report progress to, must not be null
		 */
		public Delete(AttributeChange diff, ComparisonIndex index, Monitor monitor) {
			super(diff, index, monitor);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void detectConflicts() {
			Object value = diff.getValue();
			EAttribute feature = diff.getAttribute();
			EList<Diff> diffsInSameMatch = diff.getMatch().getDifferences();
			for (Diff candidate : Iterables.filter(diffsInSameMatch, and(possiblyConflictingWith(diff),
					instanceOf(AttributeChange.class), onFeature(feature), ofKind(MOVE, DELETE)))) {
				Object candidateValue = ((AttributeChange)candidate).getValue();
				if (comparison.getEqualityHelper().matchingValues(value, candidateValue)) {
					if (candidate.getKind() == MOVE) {
						conflict(candidate, REAL);
					} else if (diff.getMatch() == candidate.getMatch()) {
						conflict(candidate, PSEUDO);
					}
				}
			}
		}
	}

	/**
	 * Search conflicts for {@link AttributeChange} of kind {@link DifferenceKind#MOVE}.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class Move extends AbstractConflictSearch<AttributeChange> {

		/**
		 * Constructor.
		 * 
		 * @param diff
		 *            The diff to search for conflicts
		 * @param index
		 *            Comparison index, must not be null
		 * @param monitor
		 *            the monitor to report progress to, must not be null
		 */
		public Move(AttributeChange diff, ComparisonIndex index, Monitor monitor) {
			super(diff, index, monitor);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void detectConflicts() {
			Object value = diff.getValue();
			EAttribute feature = diff.getAttribute();
			EList<Diff> diffsInSameMatch = diff.getMatch().getDifferences();
			for (Diff candidate : Iterables.filter(diffsInSameMatch, and(possiblyConflictingWith(diff),
					instanceOf(AttributeChange.class), onFeature(feature), ofKind(MOVE)))) {
				Object candidateValue = ((AttributeChange)candidate).getValue();

				// This can only be a conflict if the value moved is the same
				if (comparison.getEqualityHelper().matchingAttributeValues(value, candidateValue)) {
					if (matchingIndices(diff.getMatch(), feature, value, candidateValue)) {
						conflict(candidate, PSEUDO);
					} else {
						conflict(candidate, REAL);
					}
				}
			}
		}
	}
}
