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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Predicates.and;
import static org.eclipse.emf.compare.ConflictKind.PSEUDO;
import static org.eclipse.emf.compare.ConflictKind.REAL;
import static org.eclipse.emf.compare.DifferenceKind.DELETE;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onFeature;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.valueIs;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
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
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.internal.ThreeWayTextDiff;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Class in charge of finding conflicting diffs for a given diff of type T.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @param <T>
 *            The type of diff for which conflict are researched
 */
public abstract class AbstractConflictSearch<T extends Diff> {

	/** The difference, never <code>null</code>. */
	protected final T diff;

	/** The comparison that contains diff. */
	protected final Comparison comparison;

	/** The index of the comparison. */
	protected final ComparisonIndex index;

	/** The monitor to report progress to. */
	protected final Monitor monitor;

	/**
	 * Constructor.
	 * 
	 * @param diff
	 *            The diff to search conflicts with, must not be <code>null</code> and have a non-null match
	 *            that belongs to a non-null comparison. It must also have a non-null {@link DifferenceKind}
	 *            and {@link DifferenceSource}.
	 * @param index
	 *            Comparison index, must not be null
	 * @param monitor
	 *            the monitor to report progress to, must not be null
	 */
	public AbstractConflictSearch(T diff, ComparisonIndex index, Monitor monitor) {
		checkNotNull(diff);
		if (diff.getMatch() == null || diff.getMatch().getComparison() == null) {
			throw new IllegalArgumentException();
		}
		comparison = diff.getMatch().getComparison();
		checkArgument(diff.getKind() != null && diff.getSource() != null);
		this.diff = diff;
		this.index = checkNotNull(index);
		this.monitor = checkNotNull(monitor);
	}

	/**
	 * Detect conflicts with {@link AbstractConflictSearch#diff} in its comparison. This will add or update
	 * conflicts in <code>diff</code>'s comparison.
	 */
	public abstract void detectConflicts();

	/**
	 * Get the diffs in the same {@link Match} as diff.
	 * 
	 * @return A never-null EList of differences in the same {@link Match} as diff, including diff.
	 */
	protected EList<Diff> getDiffsInSameMatch() {
		return diff.getMatch().getDifferences();
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
	protected boolean isFeatureMapChangeOrMergeableStringAttributeChange(Diff diff1, Diff diff2) {
		return isFeatureMapChange(diff1) || areMergeableStringAttributeChanges(diff1, diff2);
	}

	/**
	 * Specifies whether the given {@code diff} is a {@link FeatureMapChange}.
	 * 
	 * @param toCheck
	 *            The diff to check.
	 * @return <code>true</code> if it is a {@link FeatureMapChange}, <code>false</code> otherwise.
	 */
	protected boolean isFeatureMapChange(Diff toCheck) {
		return toCheck instanceof FeatureMapChange;
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
	protected boolean areMergeableStringAttributeChanges(Diff diff1, Diff diff2) {
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
	 * @param toCheck
	 *            The diff to check.
	 * @return <code>true</code> if it is a {@link AttributeChange} of a String attribute, <code>false</code>
	 *         otherwise.
	 */
	protected boolean isStringAttributeChange(Diff toCheck) {
		return toCheck instanceof AttributeChange
				&& ((AttributeChange)toCheck).getAttribute().getEAttributeType().getInstanceClass() == String.class;
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
	protected boolean isMergeable(final AttributeChange diff1, final AttributeChange diff2) {
		final String changedValue1 = getChangedValue(diff1);
		final String changedValue2 = getChangedValue(diff2);
		final EObject originalContainer = diff1.getMatch().getOrigin();
		final EAttribute changedAttribute = diff1.getAttribute();
		final String originalValue = (String)originalContainer.eGet(changedAttribute);
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
	 * @param attributeChange
	 *            The attribute change for which the changed value is requested.
	 * @return The changed attribute value.
	 */
	protected String getChangedValue(final AttributeChange attributeChange) {
		final String changedValue;
		Match match = attributeChange.getMatch();
		if (DifferenceSource.LEFT.equals(attributeChange.getSource())) {
			changedValue = (String)match.getLeft().eGet(attributeChange.getAttribute());
		} else if (DifferenceSource.RIGHT.equals(attributeChange.getSource())) {
			changedValue = (String)match.getRight().eGet(attributeChange.getAttribute());
		} else {
			changedValue = (String)attributeChange.getValue();
		}
		return changedValue;
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
	protected boolean matchingIndices(Match match, EStructuralFeature feature, Object value1, Object value2) {
		boolean matching = false;
		if (feature.isMany()) {
			@SuppressWarnings("unchecked")
			final List<Object> leftValues = (List<Object>)ReferenceUtil.safeEGet(match.getLeft(), feature);
			@SuppressWarnings("unchecked")
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
	protected boolean hasDiff(Match match, EStructuralFeature feature, Object value) {
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
	protected boolean hasDeleteDiff(Match match, EStructuralFeature feature, Object value) {
		checkArgument(match.getComparison() == comparison);
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
				valueIs(expectedValue), ofKind(DELETE)));
	}

	/**
	 * This will be called whenever we detect a new conflict in order to create (or update) the actual
	 * association.
	 * 
	 * @param other
	 *            Second of the two differences for which we detected a conflict.
	 * @param kind
	 *            Kind of this conflict.
	 */
	protected void conflict(Diff other, ConflictKind kind) {
		// Pre-condition: diff and other are not already part of the same conflict
		if (diff.getConflict() != null && diff.getConflict().getDifferences().contains(other)) {
			return;
		}

		Conflict conflict = null;
		Conflict toBeMerged = null;
		if (diff.getConflict() != null) {
			conflict = diff.getConflict();
			if (conflict.getKind() == PSEUDO && conflict.getKind() != kind) {
				conflict.setKind(kind);
			}
			if (other.getConflict() != null) {
				// Merge the two
				toBeMerged = other.getConflict();
			}
		} else if (other.getConflict() != null) {
			conflict = other.getConflict();
			if (conflict.getKind() == PSEUDO && conflict.getKind() != kind) {
				conflict.setKind(kind);
			}
		} else if (diff.getEquivalence() != null) {
			Equivalence equivalence = diff.getEquivalence();
			for (Diff equ : equivalence.getDifferences()) {
				if (equ.getConflict() != null) {
					conflict = equ.getConflict();
					if (other.getConflict() == conflict) {
						// See initial pre-condition
						return;
					}
					if (conflict.getKind() == PSEUDO && conflict.getKind() != kind) {
						conflict.setKind(kind);
					}
					if (other.getConflict() != null) {
						// Merge the two
						toBeMerged = other.getConflict();
					}
					break;
				}
			}
		} else if (other.getEquivalence() != null) {
			Equivalence equivalence = other.getEquivalence();
			for (Diff equ : equivalence.getDifferences()) {
				if (equ.getConflict() != null) {
					conflict = equ.getConflict();
					if (conflict.getKind() == PSEUDO && conflict.getKind() != kind) {
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

		final EList<Diff> conflictDiffs = conflict.getDifferences();
		if (toBeMerged != null) {
			// These references are opposite. We can't simply iterate
			for (Diff aDiff : Lists.newArrayList(toBeMerged.getDifferences())) {
				conflictDiffs.add(aDiff);
			}
			if (toBeMerged.getKind() == REAL && conflict.getKind() != REAL) {
				conflict.setKind(REAL);
			}
			EcoreUtil.remove(toBeMerged);
			toBeMerged.getDifferences().clear();
		}

		conflict.getDifferences().add(diff);
		conflict.getDifferences().add(other);
	}

	/**
	 * Returns the MatchResource corresponding to the given <code>resource</code>.
	 * 
	 * @param resource
	 *            Resource for which we need a MatchResource.
	 * @return The MatchResource corresponding to the given <code>resource</code>.
	 */
	protected MatchResource getMatchResource(Resource resource) {
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
		checkState(soughtMatch != null, EMFCompareMessages.getString(
				"ResourceAttachmentChangeSpec.MissingMatch", resource.getURI().lastSegment())); //$NON-NLS-1$
		return soughtMatch;
	}

	/**
	 * Provide the model element the given diff applies to.
	 * 
	 * @param rac
	 *            The change
	 * @return The model element of the given diff, or null if it cannot be found.
	 */
	protected EObject getRelatedModelElement(ResourceAttachmentChange rac) {
		Match m = rac.getMatch();
		EObject o;
		switch (rac.getSource()) {
			case LEFT:
				o = m.getLeft(); // null if DELETE
				break;
			case RIGHT:
				o = m.getRight(); // null if DELETE
				break;
			default:
				o = null;
		}
		return o;
	}

	/**
	 * Provide the non-null model element the given diff applies to.
	 * 
	 * @param rac
	 *            The change
	 * @return The model element of the given diff, cannot be null.
	 */
	protected EObject getValue(ResourceAttachmentChange rac) {
		Match m = rac.getMatch();
		EObject o;
		switch (rac.getKind()) {
			case ADD:
				// Voluntary pass-through
			case CHANGE:
				// Voluntary pass-through
			case MOVE:
				switch (rac.getSource()) {
					case LEFT:
						o = m.getLeft();
						break;
					case RIGHT:
						o = m.getRight();
						break;
					default:
						o = null;
				}
				break;
			case DELETE:
				o = m.getOrigin();
				break;
			default:
				throw new IllegalStateException();
		}
		checkState(o != null);
		return o;
	}

	// FIXME Move this elsewhere
	/**
	 * This predicate will be <code>true</code> for any Match which represents a containment deletion.
	 * 
	 * @return A Predicate that will be met by containment deletions.
	 */
	protected Predicate<? super Match> isContainmentDelete() {
		return new Predicate<Match>() {
			public boolean apply(Match input) {
				return input.getOrigin() != null && (input.getLeft() == null || input.getRight() == null);
			}
		};
	}
}
