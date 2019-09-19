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
import static org.eclipse.emf.compare.DifferenceKind.DELETE;
import static org.eclipse.emf.compare.DifferenceKind.MOVE;
import static org.eclipse.emf.compare.utils.EMFCompareJavaPredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFCompareJavaPredicates.onFeature;
import static org.eclipse.emf.compare.utils.EMFCompareJavaPredicates.possiblyConflictingWith;
import static org.eclipse.emf.compare.utils.MatchUtil.matchingIndices;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diff.FeatureFilter;
import org.eclipse.emf.ecore.EReference;

/**
 * Search conflicts for non-containment {@link ReferenceChange}s.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class NonContainmentRefChangeConflictSearch {

	/**
	 * Search conflicts for non-containment {@link ReferenceChange} of kind {@link DifferenceKind#ADD}.
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
			EReference feature = diff.getReference();
			// Only unique features can conflict
			if (feature.isUnique()) {
				Object value = diff.getValue();
				EList<Diff> diffsInSameMatch = diff.getMatch().getDifferences();
				Iterable<Diff> candidates = diffsInSameMatch.stream()
						.filter(possiblyConflictingWith(diff).and(ReferenceChange.class::isInstance)
								.and(onFeature(feature)).and(ofKind(ADD)))::iterator;
				for (Diff candidate : candidates) {
					Object candidateValue = ((ReferenceChange)candidate).getValue();
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
			}
		}
	}

	/**
	 * Search conflicts for non-containment {@link ReferenceChange} of kind {@link DifferenceKind#CHANGE}.
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
			Object value = diff.getValue();
			EReference feature = diff.getReference();
			EList<Diff> diffsInSameMatch = diff.getMatch().getDifferences();
			Iterable<Diff> candidates = diffsInSameMatch.stream()
					.filter(possiblyConflictingWith(diff).and(ReferenceChange.class::isInstance)
							.and(onFeature(feature)).and(ofKind(CHANGE)))::iterator;
			for (Diff candidate : candidates) {
				Object candidateValue = ((ReferenceChange)candidate).getValue();
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
	 * Search conflicts for non-containment {@link ReferenceChange} of kind {@link DifferenceKind#DELETE}.
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
			Object value = diff.getValue();
			EReference feature = diff.getReference();
			EList<Diff> diffsInSameMatch = diff.getMatch().getDifferences();
			Iterable<Diff> candidates = diffsInSameMatch.stream()
					.filter(possiblyConflictingWith(diff).and(ReferenceChange.class::isInstance)
							.and(onFeature(feature)).and(ofKind(MOVE, DELETE)))::iterator;
			for (Diff candidate : candidates) {
				Object candidateValue = ((ReferenceChange)candidate).getValue();
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
	 * Search conflicts for non-containment {@link ReferenceChange} of kind {@link DifferenceKind#MOVE}.
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
			Object value = diff.getValue();
			EReference feature = diff.getReference();
			EList<Diff> diffsInSameMatch = diff.getMatch().getDifferences();
			Iterable<Diff> candidates = diffsInSameMatch.stream()
					.filter(possiblyConflictingWith(diff).and(ReferenceChange.class::isInstance)
							.and(onFeature(feature)).and(ofKind(MOVE)))::iterator;
			for (Diff candidate : candidates) {
				Object candidateValue = ((ReferenceChange)candidate).getValue();

				// This can only be a conflict if the value moved is the same
				if (comparison.getEqualityHelper().matchingValues(value, candidateValue)) {
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
