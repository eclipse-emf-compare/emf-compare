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
import static org.eclipse.emf.compare.DifferenceKind.DELETE;
import static org.eclipse.emf.compare.DifferenceKind.MOVE;
import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isDeleteOrUnsetDiff;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.possiblyConflictingWith;

import com.google.common.collect.Iterables;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Search for {@link ResourceAttachmentChange} conflicts.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ResourceAttachmentChangeConflictSearch {

	/**
	 * Search conflicts for {@link ResourceAttachmentChange} of kind {@link DifferenceKind#ADD}.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class Add extends AbstractConflictSearch<ResourceAttachmentChange> {

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
		public Add(ResourceAttachmentChange diff, ComparisonIndex index, Monitor monitor) {
			super(diff, index, monitor);
		}

		@Override
		public void detectConflicts() {
			// Can conflict with any containment reference change that concerns the same object
			Match match = diff.getMatch();
			EObject value = getValue(diff);

			// First let's see if ReferenceChanges point to the EObject moved
			Collection<ReferenceChange> refChanges = index.getReferenceChangesByValue(value);
			for (ReferenceChange candidate : Iterables.filter(refChanges, possiblyConflictingWith(diff))) {
				if (candidate.getReference().isContainment()) {
					// The element is a new root on one side, but it has been moved to an EObject
					// container on the other
					conflict(candidate, REAL);
				} else {
					// [477607] DELETE does not necessarily mean that the element is removed from the
					// model
					if (value.eContainer() == null) {
						// The root has been deleted.
						// Anything other than a delete of this value in a reference is a conflict.
						if (candidate.getKind() == DELETE) {
							// No conflict here
						} else {
							conflict(candidate, REAL);
						}
					}
				}
			}

			// Then let's see if there's a conflict with another ResourceAttachmentChange
			EList<Diff> diffsInSameMatch = diff.getMatch().getDifferences();
			for (Diff candidate : Iterables.filter(diffsInSameMatch, and(possiblyConflictingWith(diff),
					instanceOf(ResourceAttachmentChange.class)))) {
				ConflictKind kind = REAL;
				if (candidate.getKind() == ADD) {
					final Resource diffRes;
					final Resource candidateRes;
					if (diff.getSource() == DifferenceSource.LEFT) {
						diffRes = match.getLeft().eResource();
						candidateRes = match.getRight().eResource();
					} else {
						diffRes = match.getRight().eResource();
						candidateRes = match.getLeft().eResource();
					}
					if (getMatchResource(diffRes) == getMatchResource(candidateRes)) {
						kind = PSEUDO;
					}
				}
				conflict(candidate, kind);
			}
		}
	}

	/**
	 * Search conflicts for {@link ResourceAttachmentChange} of kind {@link DifferenceKind#CHANGE}.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class Change extends AbstractConflictSearch<ResourceAttachmentChange> {

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
		public Change(ResourceAttachmentChange diff, ComparisonIndex index, Monitor monitor) {
			super(diff, index, monitor);
		}

		@Override
		public void detectConflicts() {
			throw new IllegalStateException("ResourceAttachmentChanges of type CHANGE should not exist."); //$NON-NLS-1$
		}
	}

	/**
	 * Search conflicts for {@link ResourceAttachmentChange} of kind {@link DifferenceKind#DELETE}.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class Delete extends AbstractConflictSearch<ResourceAttachmentChange> {

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
		public Delete(ResourceAttachmentChange diff, ComparisonIndex index, Monitor monitor) {
			super(diff, index, monitor);
		}

		@Override
		public void detectConflicts() {
			Match match = diff.getMatch();
			EObject value = getRelatedModelElement(diff);

			// First let's see if ReferenceChanges point to the EObject moved
			if (value != null) {
				Collection<ReferenceChange> refChanges = index.getReferenceChangesByValue(value);
				for (ReferenceChange candidate : Iterables.filter(refChanges, possiblyConflictingWith(diff))) {
					if (candidate.getReference().isContainment()) {
						// The element is a new root on one side, but it has been moved to an EObject
						// container on the other
						conflict(candidate, REAL);
					} else {
						// [477607] DELETE does not necessarily mean that the element is removed from the
						// model
						if (value.eContainer() == null) {
							// The root has been deleted.
							// Anything other than a delete of this value in a reference is a conflict.
							if (candidate.getKind() != DELETE) {
								conflict(candidate, REAL);
							}
						}
					}
				}
			}

			// Then let's see if there's a conflict with another ResourceAttachmentChange
			EList<Diff> diffsInSameMatch = diff.getMatch().getDifferences();
			for (Diff candidate : Iterables.filter(diffsInSameMatch, and(possiblyConflictingWith(diff),
					instanceOf(ResourceAttachmentChange.class)))) {
				ConflictKind kind = REAL;
				if (candidate.getKind() == DELETE) {
					final Resource diffRes;
					final Resource candidateRes;
					diffRes = match.getOrigin().eResource();
					candidateRes = match.getOrigin().eResource();
					if (getMatchResource(diffRes) == getMatchResource(candidateRes)) {
						kind = PSEUDO;
					}
				}
				conflict(candidate, kind);
			}

			// [381143] Every Diff "under" a root deletion conflicts with it.
			// [477607] DELETE does not necessarily mean that the element is removed from the model
			// Each element under a pseudo-conflicting diff should have its own conflict and not be just a
			// dependence of the existing conflict
			EObject o = getRelatedModelElement(diff);
			if ((o == null || o.eContainer() == null)
					&& (diff.getConflict() == null || diff.getConflict().getKind() != PSEUDO)) {
				for (Diff extendedCandidate : Iterables.filter(match.getAllDifferences(),
						possiblyConflictingWith(diff))) {
					if (isDeleteOrUnsetDiff(extendedCandidate)) {
						conflict(extendedCandidate, PSEUDO);
					} else {
						conflict(extendedCandidate, REAL);
					}
				}
			}
		}
	}

	/**
	 * Search conflicts for {@link ResourceAttachmentChange} of kind {@link DifferenceKind#MOVE}.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class Move extends AbstractConflictSearch<ResourceAttachmentChange> {

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
		public Move(ResourceAttachmentChange diff, ComparisonIndex index, Monitor monitor) {
			super(diff, index, monitor);
		}

		@Override
		public void detectConflicts() {
			EObject value = getRelatedModelElement(diff);

			// First let's see if ReferenceChanges point to the EObject moved
			Collection<ReferenceChange> refChanges = index.getReferenceChangesByValue(value);
			for (ReferenceChange candidate : Iterables.filter(refChanges, possiblyConflictingWith(diff))) {
				if (candidate.getReference().isContainment()) {
					// The element is a new root on one side, but it has been moved to an EObject container on
					// the other
					conflict(candidate, REAL);
				}
			}

			EList<Diff> diffsInSameMatch = diff.getMatch().getDifferences();
			for (Diff candidate : Iterables.filter(diffsInSameMatch, and(possiblyConflictingWith(diff),
					instanceOf(ResourceAttachmentChange.class)))) {
				ConflictKind kind = REAL;
				if (candidate.getKind() == MOVE) {
					String lhsURI = diff.getResourceURI();
					String rhsURI = ((ResourceAttachmentChange)candidate).getResourceURI();
					if (lhsURI.equals(rhsURI)) {
						kind = ConflictKind.PSEUDO;
					}
				}
				conflict(candidate, kind);
			}
		}
	}
}
