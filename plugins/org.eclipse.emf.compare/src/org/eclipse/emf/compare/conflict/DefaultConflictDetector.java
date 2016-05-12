/*******************************************************************************
( * Copyright (c) 2012, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bugs 446947, 479449
 *     Martin Fleck - bug 493527
 *******************************************************************************/
package org.eclipse.emf.compare.conflict;

import static com.google.common.base.Predicates.and;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.isEmpty;
import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isAddOrSetDiff;
import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isDeleteOrUnsetDiff;
import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isFeatureMapContainment;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.CONTAINMENT_REFERENCE_CHANGE;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onFeature;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.possiblyConflictingWith;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.valueIs;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ComparisonCanceledException;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompareMessages;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.compare.FeatureMapChange;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.internal.ThreeWayTextDiff;
import org.eclipse.emf.compare.internal.conflict.DiffTreeIterator;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.utils.IEqualityHelper;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;

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

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(DefaultConflictDetector.class);

	/**
	 * This can be used to check whether a given conflict involves add containment reference changes.
	 */
	private static final Predicate<? super Conflict> IS_REAL_CONTAINMENT_ADD_CONFLICT = new Predicate<Conflict>() {
		public boolean apply(Conflict input) {
			boolean isRealAddContainmentConflict = false;
			if (input != null && input.getKind() == ConflictKind.REAL) {
				Iterable<Diff> containmentRefs = filter(input.getDifferences(), CONTAINMENT_REFERENCE_CHANGE);
				if (!isEmpty(containmentRefs)) {
					for (Diff diff : containmentRefs) {
						if (diff.getKind() != DifferenceKind.ADD) {
							return false;
						}
					}
					isRealAddContainmentConflict = true;
				}
			}
			return isRealAddContainmentConflict;
		}
	};

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.conflict.IConflictDetector#detect(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void detect(Comparison comparison, Monitor monitor) {
		long start = System.currentTimeMillis();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("detect conflicts - START"); //$NON-NLS-1$
		}
		final List<Diff> differences = comparison.getDifferences();
		final int diffCount = differences.size();

		for (int i = 0; i < diffCount; i++) {
			if (i % 100 == 0) {
				monitor.subTask(EMFCompareMessages.getString("DefaultConflictDetector.monitor.detect", //$NON-NLS-1$
						Integer.valueOf(i + 1), Integer.valueOf(diffCount)));
			}
			if (monitor.isCanceled()) {
				throw new ComparisonCanceledException();
			}
			final Diff diff = differences.get(i);

			checkConflict(comparison, diff, Iterables.filter(differences, possiblyConflictingWith(diff)));
		}

		handlePseudoUnderRealAdd(comparison);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format(
					"detect conflicts - END - Took %d ms", Long.valueOf(System.currentTimeMillis() - start))); //$NON-NLS-1$
		}
	}

	/**
	 * If a real add conflict contains pseudo conflicts, these pseudo conflicts must be changed to real
	 * conflicts.
	 * 
	 * @param comparison
	 *            The originating comparison of those diffs.
	 */
	private void handlePseudoUnderRealAdd(Comparison comparison) {
		for (Conflict realContainmentAdd : filter(comparison.getConflicts(), IS_REAL_CONTAINMENT_ADD_CONFLICT)) {
			changeKindOfPseudoConflictsUnder(realContainmentAdd);
		}
	}

	/**
	 * Change all pseudo conflicts under the given real conflict to real conflicts.
	 * 
	 * @param conflict
	 *            the given conflict.
	 */
	private void changeKindOfPseudoConflictsUnder(Conflict conflict) {
		for (Diff diff : conflict.getDifferences()) {
			final Match realConflictMatch = diff.getMatch();
			for (Match subMatch : realConflictMatch.getSubmatches()) {
				for (Diff conflictDiffUnder : filter(subMatch.getDifferences(),
						hasConflict(ConflictKind.PSEUDO))) {
					Conflict conflictUnder = conflictDiffUnder.getConflict();
					conflictUnder.setKind(ConflictKind.REAL);
					changeKindOfPseudoConflictsUnder(conflictUnder);
				}
			}
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
		// DELETE diffs can conflict with every other if on containment references, only with MOVE or other
		// DELETE otherwise.
		// ADD diffs can only conflict with "DELETE" or "ADD" ones ... Most will be detected on the DELETE.
		// However, ADD diffs on containment reference can conflict with other ADDs on the same match.
		//
		// CHANGE diffs can only conflict with other CHANGE or DELETE ... here again detected on the DELETE
		// MOVE diffs can conflict with DELETE ones, detected on the delete, or with other MOVE diffs.
		if (diff instanceof ReferenceChange && ((ReferenceChange)diff).getReference().isContainment()) {
			checkContainmentConflict(comparison, (ReferenceChange)diff, Iterables.filter(candidates,
					ReferenceChange.class));
		} else if (diff instanceof ResourceAttachmentChange) {
			// These will be handled about the same way as containment deletions,
			// Though they can also conflict with themselves
			checkResourceAttachmentConflict(comparison, (ResourceAttachmentChange)diff, candidates);
		} else if (isFeatureMapContainment(diff)) {
			checkContainmentFeatureMapConflict(comparison, (FeatureMapChange)diff, Iterables.filter(
					candidates, FeatureMapChange.class));
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
		for (ReferenceChange candidate : candidates) {
			if (isMatchingValues(comparison, diff, candidate)) {
				checkContainmentConflict(comparison, diff, candidate);
			} else if (isConflictingAdditionToSingleValuedReference(diff, candidate)) {
				if (comparison.getEqualityHelper().matchingValues(candidate.getValue(), diff.getValue())) {
					conflictOn(comparison, diff, candidate, ConflictKind.PSEUDO);
				} else {
					conflictOn(comparison, diff, candidate, ConflictKind.REAL);
				}
			}
		}

		// [381143] Every Diff "under" a containment deletion conflicts with it.
		if (diff.getKind() == DifferenceKind.DELETE) {
			final DiffTreeIterator diffIterator = new DiffTreeIterator(comparison.getMatch(diff.getValue()));
			diffIterator.setFilter(possiblyConflictingWith(diff));
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
	 * Specifies whether the given differences <code>diff</code> and <code>candidate</code> are conflicting
	 * additions to single-valued references.
	 * <p>
	 * They are, if the changed reference is single-valued and both are additions to the same object.
	 * </p>
	 * 
	 * @param diff
	 *            The reference diff to check.
	 * @param candidate
	 *            The candidate diff to check.
	 * @return <code>true</code> if they are conflicting additions to single-valued references,
	 *         <code>false</code> otherwise.
	 */
	private boolean isConflictingAdditionToSingleValuedReference(ReferenceChange diff,
			ReferenceChange candidate) {
		return (diff.getReference() == candidate.getReference() && !diff.getReference().isMany())
				&& (isAddOrSetDiff(diff) && isAddOrSetDiff(candidate))
				&& diff.getMatch() == candidate.getMatch();
	}

	/**
	 * Specifies whether the values of the given differences <code>diff</code> and <code>candidate</code>
	 * match.
	 * 
	 * @param comparison
	 *            The originating comparison of those diffs.
	 * @param diff
	 *            The one difference.
	 * @param candidate
	 *            The other difference.
	 * @return <code>true</code> if the values of the differences match, <code>false</code> otherwise.
	 */
	private boolean isMatchingValues(Comparison comparison, ReferenceChange diff, ReferenceChange candidate) {
		final Match valueMatch = comparison.getMatch(diff.getValue());
		final EObject candidateValue = candidate.getValue();
		return valueMatch.getLeft() == candidateValue || valueMatch.getRight() == candidateValue
				|| valueMatch.getOrigin() == candidateValue;
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
		final boolean candidateIsDelete = isDeleteOrUnsetDiff(candidate);
		if (candidate.getReference().isContainment()) {
			// The same value has been changed on both sides in containment references
			// This is a conflict, but is it a pseudo-conflict?
			ConflictKind kind = ConflictKind.REAL;
			final boolean diffIsDelete = isDeleteOrUnsetDiff(diff);
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
			if (candidateIsDelete) {
				// No conflict here
			} else {
				// Be it added, moved or changed, this is a REAL conflict
				conflictOn(comparison, diff, candidate, ConflictKind.REAL);
			}
		}
	}

	/**
	 * This will be called once for each FeatureMapChange on containment values in the comparison model.
	 * 
	 * @param comparison
	 *            The originating comparison of those diffs.
	 * @param diff
	 *            The feature map change for which we are to try and determine conflicts.
	 * @param candidates
	 *            An iterable over the FeatureMapChanges that are possible candidates for conflicts.
	 * @since 3.2
	 */
	protected void checkContainmentFeatureMapConflict(Comparison comparison, FeatureMapChange diff,
			Iterable<FeatureMapChange> candidates) {
		final FeatureMap.Entry entry = (FeatureMap.Entry)diff.getValue();
		final Object value = entry.getValue();
		final Match valueMatch;
		if (value instanceof EObject) {
			valueMatch = comparison.getMatch((EObject)value);
		} else {
			valueMatch = diff.getMatch();
		}

		for (FeatureMapChange candidate : candidates) {
			FeatureMap.Entry candidateEntry = (FeatureMap.Entry)candidate.getValue();
			Object candidateValue = candidateEntry.getValue();
			if (valueMatch.getLeft() == candidateValue || valueMatch.getRight() == candidateValue
					|| valueMatch.getOrigin() == candidateValue) {
				checkContainmentFeatureMapConflict(comparison, diff, candidate);
			}
		}

		// [381143] Every Diff "under" a containment deletion conflicts with it.
		if (diff.getKind() == DifferenceKind.DELETE) {
			final DiffTreeIterator diffIterator = new DiffTreeIterator(valueMatch);
			diffIterator.setFilter(possiblyConflictingWith(diff));
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
	 * For each couple of diffs on the same value in which one is a containment feature map change, we will
	 * call this in order to check for possible conflicts.
	 * <p>
	 * Once here, we know that {@code diff} is a containment feature map change, and we known that
	 * {@code diff} and {@code candidate} are both pointing to the same value. {@code candidate} can be a
	 * containment feature map change, but that is not a given.
	 * </p>
	 * 
	 * @param comparison
	 *            The originating comparison of those diffs.
	 * @param diff
	 *            Containment feature map changes for which we need to check possible conflicts.
	 * @param candidate
	 *            A feature map change that point to the same value as {@code diff}.
	 * @since 3.2
	 */
	protected void checkContainmentFeatureMapConflict(Comparison comparison, FeatureMapChange diff,
			FeatureMapChange candidate) {
		final boolean candidateIsDelete = isDeleteOrUnsetDiff(candidate);
		if (isFeatureMapContainment(candidate)) {
			// The same value has been changed on both sides in containment references
			// This is a conflict, but is it a pseudo-conflict?
			ConflictKind kind = ConflictKind.REAL;
			final boolean diffIsDelete = isDeleteOrUnsetDiff(diff);
			if (diffIsDelete && candidateIsDelete) {
				kind = ConflictKind.PSEUDO;
			} else if (diff.getMatch() == candidate.getMatch()
					&& diff.getAttribute() == candidate.getAttribute()) {
				// Same value added in the same container/reference couple with the same key
				if (!diffIsDelete
						&& !candidateIsDelete
						&& matchingIndices(comparison, diff.getMatch(), diff.getAttribute(), diff.getValue(),
								candidate.getValue()) && haveSameKey(diff, candidate)) {
					kind = ConflictKind.PSEUDO;
				}
			}
			conflictOn(comparison, diff, candidate, kind);
		} else if (diff.getKind() == DifferenceKind.DELETE) {
			/*
			 * We removed an element from its containment difference, but it has been used in some way on the
			 * other side.
			 */
			if (candidateIsDelete) {
				// No conflict here
			} else {
				// Be it added, moved or changed, this is a REAL conflict
				conflictOn(comparison, diff, candidate, ConflictKind.REAL);
			}
		}
	}

	/**
	 * Check if both feature map changes (hosting FeatureMap entries) have entries with same key.
	 * 
	 * @param left
	 *            the left candidate.
	 * @param right
	 *            the right candiadte.
	 * @return true if both feature map changes have entries with same key, false otherwise.
	 */
	private boolean haveSameKey(FeatureMapChange left, FeatureMapChange right) {
		FeatureMap.Entry leftEntry = (FeatureMap.Entry)left.getValue();
		FeatureMap.Entry rightEntry = (FeatureMap.Entry)right.getValue();
		return leftEntry.getEStructuralFeature().equals(rightEntry.getEStructuralFeature());
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
		} else if (diff instanceof FeatureMapChange) {
			changedValue = ((FeatureMap.Entry)((FeatureMapChange)diff).getValue()).getValue();
			feature = ((FeatureMapChange)diff).getAttribute();
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
					} else if (input instanceof FeatureMapChange) {
						apply = ((FeatureMapChange)input).getAttribute() == feature;
					}
				}
				return apply;
			}
		});

		final IEqualityHelper equalityHelper = comparison.getEqualityHelper();

		for (Diff candidate : refinedCandidates) {
			final Object candidateValue;
			if (candidate instanceof ReferenceChange) {
				candidateValue = ((ReferenceChange)candidate).getValue();
			} else if (candidate instanceof AttributeChange) {
				candidateValue = ((AttributeChange)candidate).getValue();
			} else if (candidate instanceof FeatureMapChange) {
				candidateValue = ((FeatureMap.Entry)((FeatureMapChange)candidate).getValue()).getValue();
			} else {
				candidateValue = null;
			}

			if (diff.getMatch() == candidate.getMatch()) {
				if (equalityHelper.matchingValues(changedValue, candidateValue)) {
					// Same value added on both side in the same container
					conflictOn(comparison, diff, candidate, ConflictKind.PSEUDO);
				} else if (!isFeatureMapChangeOrMergeableStringAttributeChange(diff, candidate)) {
					conflictOn(comparison, diff, candidate, ConflictKind.REAL);
				}
			}
		}
	}

	/**
	 * Specifies whether the given {@code diff1} and {@code diff2} are either {@link FeatureMapChange feature
	 * map changes} or mergeable {@link AttributeChange attribute changes} of String attributes.
	 * 
	 * @param diff1
	 *            One of the diffs to check.
	 * @param diff2
	 *            The other diff to check.
	 * @return <code>true</code> if it is a {@link FeatureMapChange} or a mergeable {@link AttributeChange},
	 *         <code>false</code> otherwise.
	 */
	private boolean isFeatureMapChangeOrMergeableStringAttributeChange(Diff diff1, Diff diff2) {
		return isFeatureMapChange(diff1) || areMergeableStringAttributeChanges(diff1, diff2);
	}

	/**
	 * Specifies whether the given {@code diff} is a {@link FeatureMapChange}.
	 * 
	 * @param diff
	 *            The diff to check.
	 * @return <code>true</code> if it is a {@link FeatureMapChange}, <code>false</code> otherwise.
	 */
	private boolean isFeatureMapChange(Diff diff) {
		return diff instanceof FeatureMapChange;
	}

	/**
	 * Specifies whether the two given diffs, {@code diff1} and {@code diff2}, are both
	 * {@link AttributeChange attribute changes} of String attributes and can be merged with a line-based
	 * three-way merge.
	 * 
	 * @see org.eclipse.emf.compare.internal.ThreeWayTextDiff
	 * @param diff1
	 *            One of the diffs to check.
	 * @param diff2
	 *            The other diff to check.
	 * @return <code>true</code> if the diffs are mergeable changes of a string attribute, <code>false</code>
	 *         otherwise.
	 */
	private boolean areMergeableStringAttributeChanges(Diff diff1, Diff diff2) {
		final boolean mergeableStringAttributeChange;
		if (isStringAttributeChange(diff1)) {
			final AttributeChange attributeChange1 = (AttributeChange)diff1;
			final AttributeChange attributeChange2 = (AttributeChange)diff2;
			mergeableStringAttributeChange = isMergeable(attributeChange1, attributeChange2);
		} else {
			mergeableStringAttributeChange = false;
		}
		return mergeableStringAttributeChange;
	}

	/**
	 * Specifies whether the given {@code diff} is a {@link AttributeChange} of a String attribute.
	 * 
	 * @param diff
	 *            The diff to check.
	 * @return <code>true</code> if it is a {@link AttributeChange} of a String attribute, <code>false</code>
	 *         otherwise.
	 */
	private boolean isStringAttributeChange(Diff diff) {
		return diff instanceof AttributeChange
				&& ((AttributeChange)diff).getAttribute().getEAttributeType().getInstanceClass() == String.class;
	}

	/**
	 * Specifies whether the two given attribute changes, {@code diff1} and {@code diff2}, can be merged with
	 * a line-based three-way merge.
	 * 
	 * @see org.eclipse.emf.compare.internal.ThreeWayTextDiff
	 * @param diff1
	 *            One of the attribute changes to check.
	 * @param diff2
	 *            The other attribute change to check.
	 * @return <code>true</code> if the attribute changes are mergeable, <code>false</code> otherwise.
	 */
	private boolean isMergeable(final AttributeChange diff1, final AttributeChange diff2) {
		final String changedValue1 = getChangedValue(diff1);
		final String changedValue2 = getChangedValue(diff2);
		final EObject originalContainer = diff1.getMatch().getOrigin();
		final EAttribute changedAttribute = diff1.getAttribute();
		final String originalValue = (String)ReferenceUtil.safeEGet(originalContainer, changedAttribute);
		return isMergeableText(changedValue1, changedValue2, originalValue);
	}

	/**
	 * Specifies whether the given three versions of a text {@code left}, {@code right}, and {@code origin}
	 * are mergeable with a line-based three-way merge.
	 * 
	 * @param left
	 *            The left version.
	 * @param right
	 *            The right version.
	 * @param origin
	 *            The original version.
	 * @return <code>true</code> if they are mergeable, false otherwise.
	 * @since 3.2
	 */
	protected boolean isMergeableText(final String left, final String right, final String origin) {
		ThreeWayTextDiff textDiff = new ThreeWayTextDiff(origin, left, right);
		return !textDiff.isConflicting();
	}

	/**
	 * Returns the changed attribute value denoted by the given {@code diff}.
	 * 
	 * @param diff
	 *            The attribute change for which the changed value is requested.
	 * @return The changed attribute value.
	 */
	private String getChangedValue(final AttributeChange diff) {
		final String changedValue;
		Match match = diff.getMatch();
		if (DifferenceSource.LEFT.equals(diff.getSource())) {
			changedValue = (String)ReferenceUtil.safeEGet(match.getLeft(), diff.getAttribute());
		} else if (DifferenceSource.RIGHT.equals(diff.getSource())) {
			changedValue = (String)ReferenceUtil.safeEGet(match.getRight(), diff.getAttribute());
		} else {
			changedValue = (String)diff.getValue();
		}
		return changedValue;
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
		} else if (diff instanceof FeatureMapChange) {
			changedValue = ((FeatureMap.Entry)((FeatureMapChange)diff).getValue()).getValue();
			feature = ((FeatureMapChange)diff).getAttribute();
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
					} else if (input instanceof FeatureMapChange) {
						apply = ((FeatureMapChange)input).getAttribute() == feature;
					}
				}
				return apply;
			}
		});

		for (Diff candidate : refinedCandidates) {
			final Object candidateValue;
			if (candidate instanceof ReferenceChange) {
				candidateValue = ((ReferenceChange)candidate).getValue();
			} else if (candidate instanceof AttributeChange) {
				candidateValue = ((AttributeChange)candidate).getValue();
			} else if (candidate instanceof FeatureMapChange) {
				candidateValue = ((FeatureMap.Entry)((FeatureMapChange)candidate).getValue()).getValue();
			} else {
				candidateValue = null;
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
		} else if (diff instanceof FeatureMapChange) {
			deletedValue = ((FeatureMap.Entry)((FeatureMapChange)diff).getValue()).getValue();
			feature = ((FeatureMapChange)diff).getAttribute();
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
					} else if (input instanceof FeatureMapChange) {
						apply = ((FeatureMapChange)input).getAttribute() == feature;
					}
				}
				return apply;
			}
		});

		for (Diff candidate : refinedCandidates) {
			final Object movedValue;
			if (candidate instanceof ReferenceChange) {
				movedValue = ((ReferenceChange)candidate).getValue();
			} else if (candidate instanceof AttributeChange) {
				movedValue = ((AttributeChange)candidate).getValue();
			} else if (candidate instanceof FeatureMapChange) {
				movedValue = ((FeatureMap.Entry)((FeatureMapChange)candidate).getValue()).getValue();
			} else {
				movedValue = null;
			}

			if (comparison.getEqualityHelper().matchingValues(deletedValue, movedValue)) {
				if (candidate.getKind() == DifferenceKind.MOVE) {
					conflictOn(comparison, diff, candidate, ConflictKind.REAL);
				} else if (diff.getMatch() == candidate.getMatch()) {
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
		} else if (diff instanceof FeatureMapChange) {
			addedValue = ((FeatureMap.Entry)((FeatureMapChange)diff).getValue()).getValue();
			feature = ((FeatureMapChange)diff).getAttribute();
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
					} else if (input instanceof FeatureMapChange) {
						apply = ((FeatureMapChange)input).getAttribute() == feature;
					}
				}
				return apply;
			}
		});

		for (Diff candidate : refinedCandidates) {
			final Object candidateValue;
			if (candidate instanceof ReferenceChange) {
				candidateValue = ((ReferenceChange)candidate).getValue();
			} else if (candidate instanceof AttributeChange) {
				candidateValue = ((AttributeChange)candidate).getValue();
			} else if (candidate instanceof FeatureMapChange) {
				candidateValue = ((FeatureMap.Entry)((FeatureMapChange)candidate).getValue()).getValue();
			} else {
				candidateValue = null;
			}
			// No diff on non unique features : multiple same values can coexist
			if (feature.isUnique()
					&& comparison.getEqualityHelper().matchingValues(addedValue, candidateValue)) {
				// This is a conflict. Is it real?
				if (diff instanceof FeatureMapChange) {

					// If the key changed, this is a real conflict
					EStructuralFeature key1 = ((FeatureMap.Entry)((FeatureMapChange)diff).getValue())
							.getEStructuralFeature();
					EStructuralFeature key2 = ((FeatureMap.Entry)((FeatureMapChange)candidate).getValue())
							.getEStructuralFeature();
					if (key1.equals(key2)) {
						conflictOn(comparison, diff, candidate, ConflictKind.PSEUDO);
					} else if (isFeatureMapContainment(diff)) { // If the feature map is non-containment, the
																// same value can appear twice.
						conflictOn(comparison, diff, candidate, ConflictKind.REAL);
					}
				} else if (matchingIndices(comparison, diff.getMatch(), feature, addedValue, candidateValue)) {
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
				if (diff.getKind() == DifferenceKind.DELETE && match == candidate.getMatch()
						&& getRelatedModelElement(diff) == null) {
					if (candidate.getKind() != DifferenceKind.DELETE) {
						if (!ComparisonUtil.isDeleteOrUnsetDiff(candidate)) {
							// The EObject that owns the changed EReference has been deleted on the other side
							// [493527] deleted or unset references do not conflict with deleted element
							conflictOn(comparison, diff, candidate, ConflictKind.REAL);
						}
					}
				} else {
					// Any ReferenceChange that references the affected root is a possible conflict
					final EObject candidateValue = ((ReferenceChange)candidate).getValue();
					if (candidateValue == leftVal || candidateValue == rightVal
							|| candidateValue == originVal) {
						checkResourceAttachmentConflict(comparison, diff, (ReferenceChange)candidate);
					}
				}
			} else if (candidate instanceof AttributeChange) {
				// The change of an attribute on an EObject that has been removed from a root on the other
				// side is a conflict
				if (diff.getKind() == DifferenceKind.DELETE && match == candidate.getMatch()
						&& getRelatedModelElement(diff) == null) {
					if (ComparisonUtil.isDeleteOrUnsetDiff(candidate)) {
						conflictOn(comparison, diff, candidate, ConflictKind.PSEUDO);
					} else {
						conflictOn(comparison, diff, candidate, ConflictKind.REAL);
					}
				}
			} else if (candidate instanceof FeatureMapChange) {
				// The change of a FM on an EObject that has been removed from a root on the other side
				// is a conflict
				if (diff.getKind() == DifferenceKind.DELETE && match == candidate.getMatch()
						&& getRelatedModelElement(diff) == null) {
					if (ComparisonUtil.isDeleteOrUnsetDiff(candidate)) {
						conflictOn(comparison, diff, candidate, ConflictKind.PSEUDO);
					} else {
						conflictOn(comparison, diff, candidate, ConflictKind.REAL);
					}
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
				} else if (candidate.getKind() == DifferenceKind.MOVE
						&& diff.getKind() == DifferenceKind.MOVE) {
					String lhsURI = diff.getResourceURI();
					String rhsURI = ((ResourceAttachmentChange)candidate).getResourceURI();
					if (lhsURI.equals(rhsURI)) {
						kind = ConflictKind.PSEUDO;
					}
				}
				conflictOn(comparison, diff, candidate, kind);
			}
		}

		// [381143] Every Diff "under" a root deletion conflicts with it.
		if (diff.getKind() == DifferenceKind.DELETE) {
			// [477607] DELETE does not necessarily mean that the element is removed from the model
			EObject o = getRelatedModelElement(diff);
			if (o != null && o.eContainer() == null) {
				for (Diff extendedCandidate : Iterables.filter(match.getAllDifferences(),
						possiblyConflictingWith(diff))) {
					if (isDeleteOrUnsetDiff(extendedCandidate)) {
						conflictOn(comparison, diff, extendedCandidate, ConflictKind.PSEUDO);
					} else {
						conflictOn(comparison, diff, extendedCandidate, ConflictKind.REAL);
					}
				}
			}
		}
	}

	/**
	 * Provide the model element the given diff applies to.
	 * 
	 * @param diff
	 *            The change
	 * @return The model element of the given diff, or null if it cannot be found.
	 */
	private EObject getRelatedModelElement(ResourceAttachmentChange diff) {
		Match m = diff.getMatch();
		EObject o;
		switch (diff.getSource()) {
			case LEFT:
				o = m.getLeft();
				break;
			case RIGHT:
				o = m.getRight();
				break;
			default:
				o = null;
		}
		return o;
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
			// [477607] DELETE does not necessarily mean that the element is removed from the model
			EObject o = getRelatedModelElement(diff);
			if (o != null && o.eContainer() == null) {
				// The root has been deleted.
				// Anything other than a delete of this value in a reference is a conflict.
				if (candidate.getKind() == DifferenceKind.DELETE) {
					// No conflict here
				} else {
					conflictOn(comparison, diff, candidate, ConflictKind.REAL);
				}
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
		Conflict conflict = null;
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
		} else if (diff1.getEquivalence() != null) {
			Equivalence equivalence = diff1.getEquivalence();
			for (Diff equ : equivalence.getDifferences()) {
				if (equ.getConflict() != null) {
					conflict = equ.getConflict();
					if (conflict.getKind() == ConflictKind.PSEUDO && conflict.getKind() != kind) {
						conflict.setKind(kind);
					}
					if (diff2.getConflict() != null) {
						// Merge the two
						toBeMerged = diff2.getConflict();
					}
					break;
				}
			}
		} else if (diff2.getEquivalence() != null) {
			Equivalence equivalence = diff2.getEquivalence();
			for (Diff equ : equivalence.getDifferences()) {
				if (equ.getConflict() != null) {
					conflict = equ.getConflict();
					if (conflict.getKind() == ConflictKind.PSEUDO && conflict.getKind() != kind) {
						conflict.setKind(kind);
					}
					break;
				}
			}
		}

		if (conflict == null) {
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

		// This diff may have equivalences. These equivalences
	}
}
