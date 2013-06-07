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

import static com.google.common.base.Predicates.and;
import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isDeleteOrUnsetDiff;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onFeature;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.valueIs;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompareMessages;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.internal.SubMatchIterator;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
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
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.conflict.IConflictDetector#detect(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void detect(Comparison comparison, Monitor monitor) {
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
		} else if (diff instanceof ResourceAttachmentChange) {
			// These will be handled about the same way as containment deletions,
			// Though they can also conflict with themselves
			checkResourceAttachmentConflict(comparison, (ResourceAttachmentChange)diff, candidates);
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

		// [381143] Every Diff "under" a containment deletion conflicts with it.
		if (diff.getKind() == DifferenceKind.DELETE) {
			final Predicate<? super Diff> candidateFilter = new ConflictCandidateFilter(diff);
			final DiffTreeIterator diffIterator = new DiffTreeIterator(valueMatch);
			diffIterator.setFilter(candidateFilter);
			diffIterator.setPruningFilter(isContainmentDelete());

			while (diffIterator.hasNext()) {
				Diff extendedCandidate = diffIterator.next();
				if (isDeleteOrUnsetDiff(extendedCandidate)) {
					conflictOn(comparison, diff, extendedCandidate, ConflictKind.PSEUDO);
				} else {
					conflictOn(comparison, diff, extendedCandidate, ConflictKind.REAL);
				}
			}
		}
	}

	/**
	 * This predicate will be <code>true</code> for any Match which represents a containment deletion.
	 * 
	 * @return A Predicate that will be met by containment deletions.
	 */
	private Predicate<? super Match> isContainmentDelete() {
		return new Predicate<Match>() {
			public boolean apply(Match input) {
				return input.getOrigin() != null && (input.getLeft() == null || input.getRight() == null);
			}
		};
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
			final boolean diffIsDelete = isDeleteOrUnsetDiff(diff);
			final boolean candidateIsDelete = isDeleteOrUnsetDiff(candidate);
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
		} else if (diff instanceof AttributeChange) {
			changedValue = ((AttributeChange)diff).getValue();
			feature = ((AttributeChange)diff).getAttribute();
		} else {
			return;
		}

		final Iterable<Diff> refinedCandidates = Iterables.filter(candidates, new Predicate<Diff>() {
			public boolean apply(Diff input) {
				boolean apply = false;
				if (input != null && input.getKind() == DifferenceKind.CHANGE) {
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
				if (comparison.getEqualityHelper().matchingValues(changedValue, candidateValue)) {
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
		} else if (diff instanceof AttributeChange) {
			changedValue = ((AttributeChange)diff).getValue();
			feature = ((AttributeChange)diff).getAttribute();
		} else {
			return;
		}

		final Iterable<Diff> refinedCandidates = Iterables.filter(candidates, new Predicate<Diff>() {
			public boolean apply(Diff input) {
				boolean apply = false;
				if (input != null && input.getKind() == DifferenceKind.MOVE) {
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
					&& comparison.getEqualityHelper().matchingValues(changedValue, candidateValue)) {
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
			deletedValue = ((ReferenceChange)diff).getValue();
			feature = ((ReferenceChange)diff).getReference();
		} else if (diff instanceof AttributeChange) {
			deletedValue = ((AttributeChange)diff).getValue();
			feature = ((AttributeChange)diff).getAttribute();
		} else {
			return;
		}

		/*
		 * The only potential conflict with the deletion of a feature value is a move or delete concerning
		 * that value on the opposite side (the "feature" cannot be a containment reference, those are handled
		 * through #checkContainmentDeleteConflict).
		 */
		final Iterable<Diff> refinedCandidates = Iterables.filter(candidates, new Predicate<Diff>() {
			public boolean apply(Diff input) {
				boolean apply = false;
				if (input != null
						&& (input.getKind() == DifferenceKind.MOVE || input.getKind() == DifferenceKind.DELETE)) {
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

			if (comparison.getEqualityHelper().matchingValues(deletedValue, movedValue)) {
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
		} else if (diff instanceof AttributeChange) {
			addedValue = ((AttributeChange)diff).getValue();
			feature = ((AttributeChange)diff).getAttribute();
		} else {
			return;
		}

		/*
		 * Can only conflict on Diffs : of type ADD, on the opposite side, in the same container and the same
		 * reference, with the same added value.
		 */
		final Iterable<Diff> refinedCandidates = Iterables.filter(candidates, new Predicate<Diff>() {
			public boolean apply(Diff input) {
				boolean apply = false;
				if (input != null
						&& (input.getKind() == DifferenceKind.ADD && diff.getMatch() == input.getMatch())) {
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
			if (feature.isUnique()
					&& comparison.getEqualityHelper().matchingValues(addedValue, candidateValue)) {
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
	 * This will be called once for each ResourceAttachmentChange in the comparison model.
	 * 
	 * @param comparison
	 *            The originating comparison of those diffs.
	 * @param diff
	 *            The "root" difference for which we are to try and determine conflicts.
	 * @param candidates
	 *            An iterable over the Diffs that are possible candidates for conflicts.
	 */
	protected void checkResourceAttachmentConflict(Comparison comparison, ResourceAttachmentChange diff,
			Iterable<Diff> candidates) {
		final Match match = diff.getMatch();
		final EObject leftVal = match.getLeft();
		final EObject rightVal = match.getRight();
		final EObject originVal = match.getOrigin();
		for (Diff candidate : candidates) {
			if (candidate instanceof ReferenceChange) {
				// Any ReferenceChange that references the affected root is a possible conflict
				final EObject candidateValue = ((ReferenceChange)candidate).getValue();
				if (candidateValue == leftVal || candidateValue == rightVal || candidateValue == originVal) {
					checkResourceAttachmentConflict(comparison, diff, (ReferenceChange)candidate);
				}
			} else if (candidate instanceof ResourceAttachmentChange && match == candidate.getMatch()) {
				// This can only be a conflict. All we need to know is its kind.
				ConflictKind kind = ConflictKind.REAL;
				if (candidate.getKind() == DifferenceKind.DELETE && diff.getKind() == DifferenceKind.DELETE) {
					kind = ConflictKind.PSEUDO;
				} else if (candidate.getKind() == DifferenceKind.ADD && diff.getKind() == DifferenceKind.ADD) {
					final Resource diffRes;
					final Resource candidateRes;
					if (diff.getSource() == DifferenceSource.LEFT) {
						diffRes = match.getLeft().eResource();
						candidateRes = match.getRight().eResource();
					} else {
						diffRes = match.getRight().eResource();
						candidateRes = match.getLeft().eResource();
					}
					if (getMatchResource(comparison, diffRes) == getMatchResource(comparison, candidateRes)) {
						kind = ConflictKind.PSEUDO;
					}
				}
				conflictOn(comparison, diff, candidate, kind);
			}
		}

		// [381143] Every Diff "under" a root deletion conflicts with it.
		if (diff.getKind() == DifferenceKind.DELETE) {
			final Predicate<? super Diff> candidateFilter = new ConflictCandidateFilter(diff);
			for (Diff extendedCandidate : Iterables.filter(match.getAllDifferences(), candidateFilter)) {
				if (isDeleteOrUnsetDiff(extendedCandidate)) {
					conflictOn(comparison, diff, extendedCandidate, ConflictKind.PSEUDO);
				} else {
					conflictOn(comparison, diff, extendedCandidate, ConflictKind.REAL);
				}
			}
		}
	}

	/**
	 * Returns the MatchResource corresponding to the given <code>resource</code>.
	 * 
	 * @param comparison
	 *            the comparison to search for a MatchResource.
	 * @param resource
	 *            Resource for which we need a MatchResource.
	 * @return The MatchResource corresponding to the given <code>resource</code>.
	 */
	protected MatchResource getMatchResource(Comparison comparison, Resource resource) {
		final List<MatchResource> matchedResources = comparison.getMatchedResources();
		final int size = matchedResources.size();
		MatchResource soughtMatch = null;
		for (int i = 0; i < size && soughtMatch == null; i++) {
			final MatchResource matchRes = matchedResources.get(i);
			if (matchRes.getRight() == resource || matchRes.getLeft() == resource
					|| matchRes.getOrigin() == resource) {
				soughtMatch = matchRes;
			}
		}

		if (soughtMatch == null) {
			// This should never happen
			throw new RuntimeException(EMFCompareMessages.getString(
					"ResourceAttachmentChangeSpec.MissingMatch", resource.getURI().lastSegment())); //$NON-NLS-1$
		}

		return soughtMatch;
	}

	/**
	 * This will be called from
	 * {@link #checkResourceAttachmentConflict(Comparison, ResourceAttachmentChange, Iterable)} for each
	 * ReferenceChange in the comparison model that is on the other side and that impacts the changed root.
	 * 
	 * @param comparison
	 *            The originating comparison of those diffs.
	 * @param diff
	 *            Resource attachment change for which we need to check possible conflicts.
	 * @param candidate
	 *            A reference change that point to the same value as {@code diff}.
	 */
	protected void checkResourceAttachmentConflict(Comparison comparison, ResourceAttachmentChange diff,
			ReferenceChange candidate) {
		if (candidate.getReference().isContainment()) {
			// The element is a new root on one side, but it has been moved to an EObject container on the
			// other
			conflictOn(comparison, diff, candidate, ConflictKind.REAL);
		} else if (diff.getKind() == DifferenceKind.DELETE) {
			// The root has been deleted.
			// Anything other than a delete of this value in a reference is a conflict.
			if (candidate.getKind() == DifferenceKind.DELETE) {
				// No conflict here
			} else {
				conflictOn(comparison, diff, candidate, ConflictKind.REAL);
			}
		}
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
			final List<Object> leftValues = (List<Object>)ReferenceUtil.safeEGet(match.getLeft(), feature);
			final List<Object> rightValues = (List<Object>)ReferenceUtil.safeEGet(match.getRight(), feature);

			// FIXME the detection _will_ fail for non-unique lists with multiple identical values...
			int leftIndex = -1;
			int rightIndex = -1;
			for (int i = 0; i < leftValues.size(); i++) {
				final Object left = leftValues.get(i);
				if (comparison.getEqualityHelper().matchingValues(left, value1)) {
					break;
				} else if (hasDiff(match, feature, left) || hasDeleteDiff(match, feature, left)) {
					// Do not increment.
				} else {
					leftIndex++;
				}
			}
			for (int i = 0; i < rightValues.size(); i++) {
				final Object right = rightValues.get(i);
				if (comparison.getEqualityHelper().matchingValues(right, value2)) {
					break;
				} else if (hasDiff(match, feature, right) || hasDeleteDiff(match, feature, right)) {
					// Do not increment.
				} else {
					rightIndex++;
				}
			}
			matching = leftIndex == rightIndex;
		} else {
			matching = true;
		}
		return matching;
	}

	/**
	 * Checks whether the given {@code match} presents a difference of any kind on the given {@code feature}'s
	 * {@code value}.
	 * 
	 * @param match
	 *            The match which differences we'll check.
	 * @param feature
	 *            The feature on which we expect a difference.
	 * @param value
	 *            The value we expect to have changed inside {@code feature}.
	 * @return <code>true</code> if there is such a Diff on {@code match}, <code>false</code> otherwise.
	 */
	private boolean hasDiff(Match match, EStructuralFeature feature, Object value) {
		return Iterables.any(match.getDifferences(), and(onFeature(feature.getName()), valueIs(value)));
	}

	/**
	 * Checks whether the given {@code value} has been deleted from the given {@code feature} of {@code match}
	 * .
	 * 
	 * @param match
	 *            The match which differences we'll check.
	 * @param feature
	 *            The feature on which we expect a difference.
	 * @param value
	 *            The value we expect to have been removed from {@code feature}.
	 * @return <code>true</code> if there is such a Diff on {@code match}, <code>false</code> otherwise.
	 */
	@SuppressWarnings("unchecked")
	private boolean hasDeleteDiff(Match match, EStructuralFeature feature, Object value) {
		final Comparison comparison = match.getComparison();
		final Object expectedValue;
		if (value instanceof EObject && comparison.isThreeWay()) {
			final Match valueMatch = comparison.getMatch((EObject)value);
			if (valueMatch != null) {
				expectedValue = valueMatch.getOrigin();
			} else {
				expectedValue = value;
			}
		} else {
			expectedValue = value;
		}
		return Iterables.any(match.getDifferences(), and(onFeature(feature.getName()),
				valueIs(expectedValue), ofKind(DifferenceKind.DELETE)));
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
	private static final class ConflictCandidateFilter implements Predicate<Diff> {
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
		 * <p>
		 * Notably, we don't need to try and detect a conflict between two diffs if they're one and the same
		 * or if they have already been detected as a conflicting couple. Likewise, there can be no conflict
		 * if the two diffs originate from the same side.
		 * </p>
		 * <p>
		 * bug 381143 : we'll also remove any containment deletion diff on other Matches from here.
		 * </p>
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

			boolean canConflict = false;
			if (conflict == null || !conflict.getDifferences().contains(diff2)) {
				if (diff1.getMatch() != diff2.getMatch() && diff2 instanceof ReferenceChange
						&& ((ReferenceChange)diff2).getReference().isContainment()) {
					canConflict = !isDeleteOrUnsetDiff(diff2);
				} else {
					canConflict = true;
				}
			}
			return canConflict;
		}
	}

	/**
	 * A custom iterator that will walk a Match->submatch tree, and allow iteration over the Diffs of these
	 * Matches.
	 * <p>
	 * Since we're walking over Matches but returning Diffs, this is not a good candidate for guava's filters.
	 * We're providing the custom {@link DiffTreeIterator#setFilter(Predicate)} and
	 * {@link DiffTreeIterator#setPruningFilter(Predicate)} to allow for filtering or pruning the the
	 * iteration.
	 * </p>
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private static class DiffTreeIterator implements Iterator<Diff> {
		/**
		 * The tree iterator that will walk over our Match tree. Some of the paths can be pruned through the
		 * use of a {@link #pruningFilter}.
		 */
		private final TreeIterator<Match> subMatchIterator;

		/** An iterator over the differences of the current Match. */
		private Iterator<Diff> diffIterator;

		/** Current match. */
		private Match current;

		/** The Diff that will be returned by the next call to {@link #next()}. */
		private Diff nextDiff;

		/** Only Diffs that meet this criterion will be returned by this iterator. */
		private Predicate<? super Diff> filter = Predicates.alwaysTrue();

		/**
		 * This particular filter can be used in order to prune a given Match and all of its differences and
		 * sub-differences.
		 */
		private Predicate<? super Match> pruningFilter = Predicates.alwaysFalse();

		/**
		 * Constructs our iterator given the root of the Match tree to iterate over.
		 * 
		 * @param start
		 *            Starting match of the tree we'll iterate over.
		 */
		public DiffTreeIterator(Match start) {
			this.current = start;
			this.subMatchIterator = new SubMatchIterator(start);
			this.diffIterator = start.getDifferences().iterator();
		}

		/**
		 * Sets the criterion that Diffs must meet to be returned by this iterator.
		 * 
		 * @param filter
		 *            The filter differences must meet.
		 */
		public void setFilter(Predicate<? super Diff> filter) {
			this.filter = filter;
		}

		/**
		 * Sets the pruning filter for this iterator. Any Match that meets this criterion will be pruned along
		 * with all of its differences and sub-differences.
		 * 
		 * @param pruningFilter
		 *            The pruning filter for this iterator.
		 */
		public void setPruningFilter(Predicate<? super Match> pruningFilter) {
			this.pruningFilter = pruningFilter;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext() {
			if (nextDiff != null) {
				return true;
			}
			if (!diffIterator.hasNext()) {
				computeNextMatch();
			}
			while (nextDiff == null && diffIterator.hasNext()) {
				final Diff next = diffIterator.next();
				if (filter.apply(next)) {
					nextDiff = next;
				}
			}
			return nextDiff != null;
		}

		/**
		 * Computes the next match within the sub-match tree, pruning those that may meet
		 * {@link #pruningFilter}.
		 */
		private void computeNextMatch() {
			final Match old = current;
			while (current == old && subMatchIterator.hasNext()) {
				final Match next = subMatchIterator.next();
				if (pruningFilter.apply(next)) {
					subMatchIterator.prune();
				} else {
					current = next;
					diffIterator = current.getDifferences().iterator();
				}
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Iterator#next()
		 */
		public Diff next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			final Diff next = nextDiff;
			nextDiff = null;
			return next;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Iterator#remove()
		 */
		public void remove() {
			diffIterator.remove();
		}
	}
}
