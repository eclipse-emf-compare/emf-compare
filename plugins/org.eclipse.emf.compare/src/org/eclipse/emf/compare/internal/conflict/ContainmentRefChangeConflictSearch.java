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

import static org.eclipse.emf.compare.ConflictKind.PSEUDO;
import static org.eclipse.emf.compare.ConflictKind.REAL;
import static org.eclipse.emf.compare.DifferenceKind.ADD;
import static org.eclipse.emf.compare.DifferenceKind.CHANGE;
import static org.eclipse.emf.compare.DifferenceKind.MOVE;
import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isAddOrSetDiff;
import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isDeleteOrUnsetDiff;
import static org.eclipse.emf.compare.utils.EMFCompareJavaPredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFCompareJavaPredicates.onFeature;
import static org.eclipse.emf.compare.utils.EMFCompareJavaPredicates.possiblyConflictingWith;
import static org.eclipse.emf.compare.utils.EMFCompareJavaPredicates.valueMatches;
import static org.eclipse.emf.compare.utils.MatchUtil.matchingIndices;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diff.FeatureFilter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * Search conflicts for containment {@link ReferenceChange}s.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ContainmentRefChangeConflictSearch {

	/**
	 * Search conflicts for containment {@link ReferenceChange} of kind {@link DifferenceKind#ADD}.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class Add extends AbstractConflictSearch<ReferenceChange> {

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
		public Add(ReferenceChange diff, ComparisonIndex index, Monitor monitor) {
			super(diff, index, monitor);
		}

		@Override
		public void detectConflicts() {
			EObject value = diff.getValue();
			EReference feature = diff.getReference();

			// First let's see if non-containment diffs point to the EObject added
			Collection<ReferenceChange> refChanges = index.getReferenceChangesByValue(value);
			Iterable<ReferenceChange> candidates = refChanges.stream()
					.filter(possiblyConflictingWith(diff).and(ofKind(ADD, CHANGE)))::iterator;
			for (ReferenceChange candidate : candidates) {
				if (candidate.getReference().isContainment()) {
					if (candidate.getReference() == feature && candidate.getMatch() == diff.getMatch()) {
						// added in the same feature in the same match.
						// Might be a real conflict if order is significant and not at the same index
						FeatureFilter featureFilter = getFeatureFilter(comparison);
						if (featureFilter == null || featureFilter.checkForOrderingChanges(feature)) {
							if (matchingIndices(diff.getMatch(), feature, value, candidate.getValue())) {
								conflict(candidate, PSEUDO);
							} else {
								conflict(candidate, REAL);
							}
						} else {
							// order doesn't matter
							conflict(candidate, PSEUDO);
						}
					} else {
						// added in a different feature or another match
						conflict(candidate, REAL);
					}
				}
			}

			// Can conflict with other ADD or SET if isMany() == false
			EList<Diff> diffsInSameMatch = diff.getMatch().getDifferences();
			if (!feature.isMany()) {
				Iterable<Diff> additionalCandidates = diffsInSameMatch.stream()
						.filter(possiblyConflictingWith(diff).and(ReferenceChange.class::isInstance)
								.and(onFeature(feature)).and(ofKind(ADD, CHANGE)))::iterator;
				for (Diff candidate : additionalCandidates) {
					if (comparison.getEqualityHelper().matchingValues(((ReferenceChange)candidate).getValue(),
							diff.getValue())) {
						conflict(candidate, PSEUDO);
					} else {
						conflict(candidate, REAL);
					}
				}
			}
		}
	}

	/**
	 * Search conflicts for containment {@link ReferenceChange} of kind {@link DifferenceKind#CHANGE}.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class Change extends AbstractConflictSearch<ReferenceChange> {

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
		public Change(ReferenceChange diff, ComparisonIndex index, Monitor monitor) {
			super(diff, index, monitor);
		}

		@Override
		public void detectConflicts() {
			EObject value = diff.getValue();
			EReference feature = diff.getReference();

			// First let's see if non-containment diffs point to the EObject added
			Collection<ReferenceChange> refChanges = index.getReferenceChangesByValue(value);
			Iterable<ReferenceChange> candidates = refChanges.stream()
					.filter(possiblyConflictingWith(diff).and(ofKind(ADD, CHANGE)))::iterator;
			for (ReferenceChange candidate : candidates) {
				if (candidate.getReference().isContainment()) {
					if (candidate.getReference() == feature && candidate.getMatch() == diff.getMatch()) {
						conflict(candidate, PSEUDO);
					} else {
						conflict(candidate, REAL);
					}
				}
			}

			// Can conflict with other ADD or SET if isMany() == false
			EList<Diff> diffsInSameMatch = diff.getMatch().getDifferences();
			if (!feature.isMany() && isAddOrSetDiff(diff)) {
				Iterable<Diff> additionalCandidates = diffsInSameMatch.stream()
						.filter(possiblyConflictingWith(diff).and(ReferenceChange.class::isInstance)
								.and(onFeature(feature)))::iterator;
				for (Diff candidate : additionalCandidates) {
					if (comparison.getEqualityHelper().matchingValues(((ReferenceChange)candidate).getValue(),
							diff.getValue())) {
						conflict(candidate, PSEUDO);
					} else {
						conflict(candidate, REAL);
					}
				}
			} else if (!isDeleteOrUnsetDiff(diff)) {
				Iterable<Diff> additionalCandidates = diffsInSameMatch.stream()
						.filter(possiblyConflictingWith(diff).and(ReferenceChange.class::isInstance)
								.and(onFeature(feature)))::iterator;
				for (Diff candidate : additionalCandidates) {
					if (!isDeleteOrUnsetDiff(candidate)
							&& diff.getReference() == ((ReferenceChange)candidate).getReference()) {
						// Same value added in the same container/reference couple
						if (matchingIndices(diff.getMatch(), diff.getReference(), value,
								((ReferenceChange)candidate).getValue())) {
							conflict(candidate, PSEUDO);
						}
					}
				}
			}
		}
	}

	/**
	 * Search conflicts for containment {@link ReferenceChange} of kind {@link DifferenceKind#DELETE}.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class Delete extends AbstractConflictSearch<ReferenceChange> {

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
		public Delete(ReferenceChange diff, ComparisonIndex index, Monitor monitor) {
			super(diff, index, monitor);
		}

		@Override
		public void detectConflicts() {
			EObject value = diff.getValue();

			// First let's see if non-containment diffs point to the EObject deleted from its parent
			Collection<ReferenceChange> refChanges = index.getReferenceChangesByValue(value);
			refChanges.stream().filter(possiblyConflictingWith(diff)).filter(d -> !isDeleteOrUnsetDiff(d))
					.forEach(d -> conflict(d, REAL));

			// Now let's look for conflits with containment ReferenceChanges
			EList<Diff> diffsInSameMatch = diff.getMatch().getDifferences();
			diffsInSameMatch.stream()
					.filter(possiblyConflictingWith(diff).and(ReferenceChange.class::isInstance)
							.and(valueMatches(comparison.getEqualityHelper(), value)))
					.forEach(candidate -> {
						if (isDeleteOrUnsetDiff(candidate)) {
							conflict(candidate, PSEUDO);
						} else {
							conflict(candidate, REAL);
						}
					});

			// [381143] Every Diff "under" a containment deletion conflicts with it.
			final DiffTreeIterator diffIterator = new DiffTreeIterator(comparison.getMatch(value));
			diffIterator.setFilter(possiblyConflictingWith(diff));
			diffIterator.setPruningFilter(isContainmentDelete());

			while (diffIterator.hasNext()) {
				Diff extendedCandidate = diffIterator.next();
				if (isDeleteOrUnsetDiff(extendedCandidate)) {
					// We do not want to create a pseudo conflict between a deleted container and its
					// deleted content, since that would prevent us from merging the container deletion
					// altogether (since pseudo conflicts usually mean that no action is needed).
					// conflict(extendedCandidate, PSEUDO);
				} else {
					conflict(extendedCandidate, REAL);
				}
			}

		}
	}

	/**
	 * Search conflicts for containment {@link ReferenceChange} of kind {@link DifferenceKind#MOVE}.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class Move extends AbstractConflictSearch<ReferenceChange> {

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
		public Move(ReferenceChange diff, ComparisonIndex index, Monitor monitor) {
			super(diff, index, monitor);
		}

		@Override
		public void detectConflicts() {
			EObject value = diff.getValue();
			EReference feature = diff.getReference();

			// First let's see if non-containment diffs point to the EObject added
			Collection<ReferenceChange> refChanges = index.getReferenceChangesByValue(value);
			Iterable<ReferenceChange> candidates = refChanges.stream()
					.filter(possiblyConflictingWith(diff).and(ofKind(MOVE)))
					.filter(d -> d.getReference().isContainment())::iterator;
			for (ReferenceChange candidate : candidates) {
				// This is a containment move. It is the only case in which we could have a "move"
				// difference in a reference where order is not significant
				if (candidate.getReference() == feature && candidate.getMatch() == diff.getMatch()) {
					// Same element has been moved into the same container on both sides. If we care about
					// ordering in this new containment reference, check the indices to detect potential
					// real conflicts.
					FeatureFilter featureFilter = getFeatureFilter(comparison);
					if (featureFilter == null || featureFilter.checkForOrderingChanges(feature)) {
						if (matchingIndices(diff.getMatch(), feature, value, candidate.getValue())) {
							conflict(candidate, PSEUDO);
						} else {
							conflict(candidate, REAL);
						}
					} else {
						conflict(candidate, PSEUDO);
					}
				} else {
					conflict(candidate, REAL);
				}
			}

			EList<Diff> diffsInSameMatch = diff.getMatch().getDifferences();
			diffsInSameMatch.stream()
					.filter(possiblyConflictingWith(diff)
							.and(valueMatches(comparison.getEqualityHelper(), value))
							.and(ReferenceChange.class::isInstance).and(onFeature(feature)))
					.forEach(candidate -> {
						if (matchingIndices(diff.getMatch(), diff.getReference(), value,
								((ReferenceChange)candidate).getValue())) {
							conflict(candidate, PSEUDO);
						} else {
							conflict(candidate, REAL);
						}
					});
		}
	}
}
