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
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
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
			// Only unique features can conflict
			if (feature.isUnique()) {
				Object value = diff.getValue();
				EList<Diff> diffsInSameMatch = diff.getMatch().getDifferences();
				for (Diff candidate : Iterables.filter(diffsInSameMatch, and(possiblyConflictingWith(diff),
						instanceOf(AttributeChange.class), onFeature(feature), ofKind(ADD)))) {
					Object candidateValue = ((AttributeChange)candidate).getValue();
					if (comparison.getEqualityHelper().matchingValues(value, candidateValue)) {
						// This is a conflict. Is it real?
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
				if (matchingIndices(diff.getMatch(), feature, value, candidateValue)) {
					conflict(candidate, PSEUDO);
				} else {
					conflict(candidate, REAL);
				}
			}
		}
	}
}
