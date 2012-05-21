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
package org.eclipse.emf.compare.diff;

import static com.google.common.base.Predicates.not;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.utils.EqualityHelper;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

// TODO this probably doesn't handle feature maps. Test with an XSD-based metamodel
// TODO does not handle proxies yet (see fixmes)
/**
 * The diff engine is in charge of actually computing the differences between the objects mapped by a
 * {@link Match} object.
 * <p>
 * This default implementation aims at being generic enough to be used for any model, whatever the metamodel.
 * However, specific differences, refinements of differences or even higher level differences might be
 * necessary.
 * </p>
 * TODO document available extension possibilities.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class DefaultDiffEngine implements IDiffEngine {
	/**
	 * We'll use this "placeholder" to differentiate the unmatched elements from the "null" values that
	 * attributes can legitimately use.
	 */
	protected static final Object UNMATCHED_VALUE = new Object();

	/**
	 * The comparison for which we are detecting differences. Should only be accessed through
	 * {@link #getComparison()}.
	 */
	private Comparison currentComparison;

	/**
	 * The diff processor that will be used by this engine. Should only be instantiated through
	 * {@link #createDiffProcessor()} and accessed by {@link #getDiffProcessor()}.
	 */
	private IDiffProcessor diffProcessor;

	/**
	 * This predicate can be used to check whether a given element is contained within the given iterable
	 * according to the semantics of {@link EqualityHelper#matchingValues(Comparison, Object, Object)} before
	 * returning it.
	 * 
	 * @param comparison
	 *            This will be used in order to retrieve the Match for EObjects when comparing them.
	 * @param iterable
	 *            Iterable which content we are to check.
	 * @param <E>
	 *            Type of the reference iterable's content.
	 * @return The useable predicate.
	 */
	protected static <E> Predicate<E> containedIn(final Comparison comparison, final Iterable<E> iterable) {
		return new Predicate<E>() {
			public boolean apply(E input) {
				return contains(comparison, iterable, input);
			}
		};
	}

	/**
	 * Checks whether the given {@code iterable} contains the given {@code element} according to the semantics
	 * of {@link EqualityHelper#matchingValues(Comparison, Object, Object)}.
	 * 
	 * @param comparison
	 *            This will be used in order to retrieve the Match for EObjects when comparing them.
	 * @param iterable
	 *            Iterable which content we are to check.
	 * @param element
	 *            The element we expect to be contained in {@code  iterable}.
	 * @param <E>
	 *            Type of the input iterable's content.
	 * @return {@code true} if the given {@code iterable} contains {@code element}, {@code false} otherwise.
	 */
	protected static <E> boolean contains(Comparison comparison, Iterable<E> iterable, E element) {
		for (E candidate : iterable) {
			if (EqualityHelper.matchingValues(comparison, candidate, element)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This utility simply allows us to retrieve the value of a given feature as a List.
	 * 
	 * @param object
	 *            The object for which feature we need a value.
	 * @param feature
	 *            The actual feature of which we need the value.
	 * @return The value of the given <code>feature</code> for the given <code>object</code> as a list. An
	 *         empty list if this object has no value for that feature or if the object is <code>null</code>.
	 */
	@SuppressWarnings("unchecked")
	protected static List<Object> getAsList(EObject object, EStructuralFeature feature) {
		if (object != null) {
			Object value = object.eGet(feature, false);
			final List<Object> asList;
			if (value instanceof List) {
				asList = (List<Object>)value;
			} else if (value instanceof Iterable) {
				asList = ImmutableList.copyOf((Iterable<Object>)value);
			} else if (value != null) {
				asList = ImmutableList.of(value);
			} else {
				asList = Collections.emptyList();
			}
			return asList;
		}
		return Collections.emptyList();
	}

	// TODO unit test this with a number of known input
	/**
	 * This will compute the longest common subsequence between the two given Lists. We will use
	 * {@link EqualityHelper#matchingValues(Comparison, Object, Object)} in order to try and match the values
	 * from both lists two-by-two. This can thus be used both for reference values or attribute values. If
	 * there are two subsequences of the same "longest" length, the first (according to the second argument)
	 * will be returned.
	 * <p>
	 * For example, it the two given sequence are, in this order, {"a", "b", "c", "d", "e"} and {"c", "z",
	 * "d", "a", "b"}, there are two "longest" subsequences : {"a", "b"} and {"c", "d"}. The first of those
	 * two subsequences in the second list is {"c", "d"}. On the other hand, the LCS of {"a", "b", "c", "d",
	 * "e"} and {"y", "c", "d", "e", "b"} is {"c", "d", "e"}.
	 * </p>
	 * <p>
	 * The following algorithm has been inferred from the wikipedia article on the Longest Common Subsequence,
	 * http://en.wikipedia.org/wiki/Longest_common_subsequence_problem at the time of writing. It is
	 * decomposed in two : we first compute the LCS matrix, then we backtrack through the input to determine
	 * the LCS. Evaluation will be shortcut after the first part if the LCS is one of the two input sequences.
	 * </p>
	 * <p>
	 * Note : we are not using Iterables as input in order to make use of the random access cost of
	 * ArrayLists. This might also be converted to directly use arrays. This implementation will not play well
	 * with LinkedLists or any List which needs to iterate over the values for each call to
	 * {@link List#get(int)}, i.e any list which is not instanceof RandomAccess or does not satisfy its
	 * contract.
	 * </p>
	 * 
	 * @param comparison
	 *            This will be used in order to retrieve the Match for EObjects when comparing them.
	 * @param sequence1
	 *            First of the two sequences to consider.
	 * @param sequence2
	 *            Second of the two sequences to consider.
	 * @param <E>
	 *            Type of the sequences content.
	 * @return The LCS of the two given sequences. Will never be the same instance as one of the input
	 *         sequences.
	 */
	protected static <E> List<E> longestCommonSubsequence(Comparison comparison, List<E> sequence1,
			List<E> sequence2) {
		final int size1 = sequence1.size();
		final int size2 = sequence2.size();
		final int[][] matrix = new int[size1 + 1][size2 + 1];

		// Compute the LCS matrix
		for (int i = 1; i <= size1; i++) {
			for (int j = 1; j <= size2; j++) {
				final E first = sequence1.get(i - 1);
				final E second = sequence2.get(j - 1);
				if (EqualityHelper.matchingValues(comparison, first, second)) {
					matrix[i][j] = 1 + matrix[i - 1][j - 1];
				} else {
					matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i][j - 1]);
				}
			}
		}

		// Shortcut evaluation if the lcs is the whole sequence
		final boolean lcsIs1 = matrix[size1][size2] == size1;
		final boolean lcsIs2 = matrix[size1][size2] == size2;
		if (lcsIs1 || lcsIs2) {
			final List<E> shortcut;
			if (lcsIs1) {
				shortcut = ImmutableList.copyOf(sequence1);
			} else {
				shortcut = ImmutableList.copyOf(sequence2);
			}
			return shortcut;
		}

		int current1 = size1;
		int current2 = size2;
		final List<E> result = Lists.newArrayList();

		while (current1 > 0 && current2 > 0) {
			final E first = sequence1.get(current1 - 1);
			final E second = sequence2.get(current2 - 1);
			if (EqualityHelper.matchingValues(comparison, first, second)) {
				result.add(first);
				current1--;
				current2--;
			} else if (matrix[current1][current2 - 1] >= matrix[current1 - 1][current2]) {
				current2--;
			} else {
				current1--;
			}
		}
		return Lists.reverse(result);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.IDiffEngine#diff(org.eclipse.emf.compare.Comparison)
	 */
	public void diff(Comparison comparison) {
		this.currentComparison = comparison;
		diffProcessor = createDiffProcessor();

		for (Match rootMatch : comparison.getMatches()) {
			checkForDifferences(rootMatch);
		}
	}

	/**
	 * Checks the given {@link Match}'s sides for potential differences. Will recursively check for
	 * differences on submatches.
	 * 
	 * @param match
	 *            The match that is to be checked.
	 */
	protected void checkForDifferences(Match match) {
		final FeatureFilter featureFilter = createFeatureFilter();

		final Iterator<EReference> references = featureFilter.getReferencesToCheck(match);
		while (references.hasNext()) {
			final EReference reference = references.next();
			final boolean considerOrdering = featureFilter.checkForOrderingChanges(reference);
			computeDifferences(match, reference, considerOrdering);
		}

		final Iterator<EAttribute> attributes = featureFilter.getAttributesToCheck(match);
		while (attributes.hasNext()) {
			final EAttribute attribute = attributes.next();
			final boolean considerOrdering = featureFilter.checkForOrderingChanges(attribute);
			computeDifferences(match, attribute, considerOrdering);
		}

		for (Match submatch : match.getSubmatches()) {
			checkForDifferences(submatch);
		}
	}

	/**
	 * Computes the difference between the sides of the given {@code match} for the given containment
	 * {@code reference}.
	 * <p>
	 * This is only meant for three-way comparisons.
	 * </p>
	 * 
	 * @param match
	 *            The match which sides we need to check for potential differences.
	 * @param reference
	 *            The containment reference which values are to be checked.
	 * @param checkOrdering
	 *            {@code true} if we should consider ordering changes on this reference, {@code false}
	 *            otherwise.
	 */
	protected void computeContainmentDifferencesThreeWay(Match match, EReference reference,
			boolean checkOrdering) {
		// We won't use iterables here since we need random access collections for fast LCS.
		final List<Object> leftValues = getAsList(match.getLeft(), reference);
		final List<Object> rightValues = getAsList(match.getRight(), reference);
		final List<Object> originValues = getAsList(match.getOrigin(), reference);

		final List<Object> lcsOriginLeft = longestCommonSubsequence(getComparison(), originValues, leftValues);
		final List<Object> lcsOriginRight = longestCommonSubsequence(getComparison(), originValues,
				rightValues);

		// TODO Can we shortcut in any way?

		// Which values have "changed" in any way?
		final Iterable<Object> changedLeft = Iterables.filter(leftValues, not(containedIn(getComparison(),
				lcsOriginLeft)));
		final Iterable<Object> changedRight = Iterables.filter(rightValues, not(containedIn(getComparison(),
				lcsOriginRight)));

		// Added or moved in left
		for (Object diffCandidate : changedLeft) {
			/*
			 * This value is not in the LCS between origin and left, we thus know it has changed. We also know
			 * that this is a containment reference.
			 */
			final Match candidateMatch = getComparison().getMatch((EObject)diffCandidate);

			if (contains(getComparison(), originValues, diffCandidate)) {
				// This object is contained in both the left and origin lists. It can only have moved
				if (checkOrdering) {
					featureChange(match, reference, diffCandidate, DifferenceKind.MOVE, DifferenceSource.LEFT);
				}
			} else {
				// This object is not contained in the origin list.
				// If single-valued, this is either a CHANGE or a MOVE (moved from another container)
				// If multi-valued references, ADD or MOVE
				if (candidateMatch != null && candidateMatch.getOrigin() != null) {
					featureChange(match, reference, diffCandidate, DifferenceKind.MOVE, DifferenceSource.LEFT);
				} else {
					featureChange(match, reference, diffCandidate, DifferenceKind.ADD, DifferenceSource.LEFT);
				}
			}
		}

		// Added or moved in right
		for (Object diffCandidate : changedRight) {
			/*
			 * This value is not in the LCS between origin and right, we thus know it has changed. We also
			 * know that this is a containment reference.
			 */
			final Match candidateMatch = getComparison().getMatch((EObject)diffCandidate);

			if (contains(getComparison(), originValues, diffCandidate)) {
				// This object is contained in both the right and origin lists. It can only have moved
				if (checkOrdering) {
					featureChange(match, reference, diffCandidate, DifferenceKind.MOVE,
							DifferenceSource.RIGHT);
				}
			} else {
				// This object is not contained in the origin list.
				// If single-valued, this is either a CHANGE or a MOVE (moved from another container)
				// If multi-valued references, ADD or MOVE
				if (candidateMatch != null && candidateMatch.getOrigin() != null) {
					featureChange(match, reference, diffCandidate, DifferenceKind.MOVE,
							DifferenceSource.RIGHT);
				} else {
					featureChange(match, reference, diffCandidate, DifferenceKind.ADD, DifferenceSource.RIGHT);
				}
			}
		}

		// deleted from either side
		for (Object diffCandidate : originValues) {
			/*
			 * A value that is in the origin but not in the left/right either has been deleted or is a moved
			 * element which previously was in this reference. We'll detect the move on its new reference.
			 */
			final Match candidateMatch = getComparison().getMatch((EObject)diffCandidate);
			if (candidateMatch == null) {
				// out of scope
			} else {
				if (candidateMatch.getLeft() == null) {
					featureChange(match, reference, diffCandidate, DifferenceKind.DELETE,
							DifferenceSource.LEFT);
				}
				if (candidateMatch.getRight() == null) {
					featureChange(match, reference, diffCandidate, DifferenceKind.DELETE,
							DifferenceSource.RIGHT);
				}
			}
		}
	}

	/**
	 * Computes the difference between the sides of the given {@code match} for the given containment
	 * {@code reference}.
	 * <p>
	 * This is only meant for two-way comparisons.
	 * </p>
	 * 
	 * @param match
	 *            The match which sides we need to check for potential differences.
	 * @param reference
	 *            The containment reference which values are to be checked.
	 * @param checkOrdering
	 *            {@code true} if we should consider ordering changes on this reference, {@code false}
	 *            otherwise.
	 */
	protected void computeContainmentDifferencesTwoWay(Match match, EReference reference,
			boolean checkOrdering) {
		final List<Object> leftValues = getAsList(match.getLeft(), reference);
		final List<Object> rightValues = getAsList(match.getRight(), reference);

		final List<Object> lcs = longestCommonSubsequence(getComparison(), rightValues, leftValues);

		// TODO Can we shortcut in any way?

		// Which values have "changed" in any way?
		final Iterable<Object> changed = Iterables.filter(leftValues, not(containedIn(getComparison(), lcs)));

		// Added or moved
		for (Object diffCandidate : changed) {
			/*
			 * This value is not in the LCS between right and left, we thus know it has changed. We also know
			 * that this is a containment reference.
			 */
			final Match candidateMatch = getComparison().getMatch((EObject)diffCandidate);

			if (contains(getComparison(), rightValues, diffCandidate)) {
				// This object is contained in both the left and right lists. It can only have moved
				if (checkOrdering) {
					featureChange(match, reference, diffCandidate, DifferenceKind.MOVE, DifferenceSource.LEFT);
				}
			} else {
				// This object is not contained in the right list.
				// If single-valued, this is either a CHANGE or a MOVE (moved from another container)
				// If multi-valued references, ADD or MOVE
				if (candidateMatch != null && candidateMatch.getRight() != null) {
					featureChange(match, reference, diffCandidate, DifferenceKind.MOVE, DifferenceSource.LEFT);
				} else {
					featureChange(match, reference, diffCandidate, DifferenceKind.ADD, DifferenceSource.LEFT);
				}
			}
		}

		// deleted
		for (Object diffCandidate : rightValues) {
			/*
			 * A value that is in the right but not in the left either has been deleted or is a moved element
			 * which previously was in this reference. We'll detect the move on its new reference.
			 */
			final Match candidateMatch = getComparison().getMatch((EObject)diffCandidate);
			if (candidateMatch == null) {
				// out of scope
			} else if (candidateMatch.getLeft() == null) {
				featureChange(match, reference, diffCandidate, DifferenceKind.DELETE, DifferenceSource.LEFT);
			}
		}
	}

	/**
	 * Computes the difference between the sides of the given <code>match</code> for the given
	 * <code>attribute</code>.
	 * 
	 * @param match
	 *            The match which sides we need to check for potential differences.
	 * @param attribute
	 *            The attribute which values are to be checked.
	 * @param checkOrdering
	 *            <code>true</code> if we should consider ordering changes on this attribute,
	 *            <code>false</code> otherwise.
	 */
	protected void computeDifferences(Match match, EAttribute attribute, boolean checkOrdering) {
		// This default implementation does not care about "attribute" changes on unmatched elements
		if (match.getOrigin() == null && (match.getLeft() == null || match.getRight() == null)) {
			return;
		}

		if (attribute.isMany()) {
			if (getComparison().isThreeWay()) {
				computeMultiValuedFeatureDifferencesThreeWay(match, attribute, checkOrdering);
			} else {
				computeMultiValuedFeatureDifferencesTwoWay(match, attribute, checkOrdering);
			}
		} else {
			computeSingleValuedAttributeDifferences(match, attribute);
		}
	}

	/**
	 * Computes the difference between the sides of the given <code>match</code> for the given
	 * <code>reference</code>.
	 * 
	 * @param match
	 *            The match which sides we need to check for potential differences.
	 * @param reference
	 *            The reference which values are to be checked.
	 * @param checkOrdering
	 *            <code>true</code> if we should consider ordering changes on this reference,
	 *            <code>false</code> otherwise.
	 */
	protected void computeDifferences(Match match, EReference reference, boolean checkOrdering) {
		if (reference.isContainment()) {
			if (getComparison().isThreeWay()) {
				computeContainmentDifferencesThreeWay(match, reference, checkOrdering);
			} else {
				computeContainmentDifferencesTwoWay(match, reference, checkOrdering);
			}
		} else if (reference.isMany()) {
			if (getComparison().isThreeWay()) {
				computeMultiValuedFeatureDifferencesThreeWay(match, reference, checkOrdering);
			} else {
				computeMultiValuedFeatureDifferencesTwoWay(match, reference, checkOrdering);
			}
		} else {
			if (getComparison().isThreeWay()) {
				computeSingleValuedReferenceDifferencesThreeWay(match, reference);
			} else {
				computeSingleValuedReferenceDifferencesTwoWay(match, reference);
			}
		}
	}

	/**
	 * Computes the difference between the sides of the given {@code match} for the given multi-valued
	 * {@code feature}.
	 * <p>
	 * The given {@code feature} cannot be a containment reference.
	 * </p>
	 * <p>
	 * This is only meant for three-way comparisons.
	 * </p>
	 * 
	 * @param match
	 *            The match which sides we need to check for potential differences.
	 * @param feature
	 *            The feature which values are to be checked.
	 * @param checkOrdering
	 *            {@code true} if we should consider ordering changes on this feature, {@code false}
	 *            otherwise.
	 */
	protected void computeMultiValuedFeatureDifferencesThreeWay(Match match, EStructuralFeature feature,
			boolean checkOrdering) {
		// We won't use iterables here since we need random access collections for fast LCS.
		final List<Object> leftValues = getAsList(match.getLeft(), feature);
		final List<Object> rightValues = getAsList(match.getRight(), feature);
		final List<Object> originValues = getAsList(match.getOrigin(), feature);

		final List<Object> lcsOriginLeft = longestCommonSubsequence(getComparison(), originValues, leftValues);
		final List<Object> lcsOriginRight = longestCommonSubsequence(getComparison(), originValues,
				rightValues);

		// TODO Can we shortcut in any way?

		// Which values have "changed" in any way?
		final Iterable<Object> changedLeft = Iterables.filter(leftValues, not(containedIn(getComparison(),
				lcsOriginLeft)));
		final Iterable<Object> changedRight = Iterables.filter(rightValues, not(containedIn(getComparison(),
				lcsOriginRight)));

		// Added or moved in the left
		for (Object diffCandidate : changedLeft) {
			/*
			 * This value is not in the LCS between origin and left, we thus know it has changed. If it is
			 * present in the origin, this is a move. Otherwise, it has been added in the left list.
			 */
			if (contains(getComparison(), originValues, diffCandidate)) {
				if (checkOrdering) {
					featureChange(match, feature, diffCandidate, DifferenceKind.MOVE, DifferenceSource.LEFT);
				}
			} else {
				featureChange(match, feature, diffCandidate, DifferenceKind.ADD, DifferenceSource.LEFT);
			}
		}

		// Added or moved in the right
		for (Object diffCandidate : changedRight) {
			/*
			 * This value is not in the LCS between origin and right, we thus know it has changed. If it is
			 * present in the origin, this is a move. Otherwise, it has been added in the right list.
			 */
			if (contains(getComparison(), originValues, diffCandidate)) {
				if (checkOrdering) {
					featureChange(match, feature, diffCandidate, DifferenceKind.MOVE, DifferenceSource.RIGHT);
				}
			} else {
				featureChange(match, feature, diffCandidate, DifferenceKind.ADD, DifferenceSource.RIGHT);
			}
		}

		// Removed from either side
		for (Object diffCandidate : originValues) {
			// A value that is in the origin but not in one of the side has been deleted.
			// However, we do not want attribute changes on removed elements.
			if (!contains(getComparison(), leftValues, diffCandidate)) {
				if (feature instanceof EReference || match.getLeft() != null) {
					featureChange(match, feature, diffCandidate, DifferenceKind.DELETE, DifferenceSource.LEFT);
				}
			}
			if (!contains(getComparison(), rightValues, diffCandidate)) {
				if (feature instanceof EReference || match.getRight() != null) {
					featureChange(match, feature, diffCandidate, DifferenceKind.DELETE,
							DifferenceSource.RIGHT);
				}
			}
		}
	}

	/**
	 * Computes the difference between the sides of the given {@code match} for the given multi-valued
	 * {@code feature}.
	 * <p>
	 * The given {@code feature} cannot be a containment reference.
	 * </p>
	 * <p>
	 * This is only meant for two-way comparisons.
	 * </p>
	 * 
	 * @param match
	 *            The match which sides we need to check for potential differences.
	 * @param feature
	 *            The feature which values are to be checked.
	 * @param checkOrdering
	 *            {@code true} if we should consider ordering changes on this feature, {@code false}
	 *            otherwise.
	 */
	protected void computeMultiValuedFeatureDifferencesTwoWay(Match match, EStructuralFeature feature,
			boolean checkOrdering) {
		// We won't use iterables here since we need random access collections for fast LCS.
		final List<Object> leftValues = getAsList(match.getLeft(), feature);
		final List<Object> rightValues = getAsList(match.getRight(), feature);

		final List<Object> lcs = longestCommonSubsequence(getComparison(), rightValues, leftValues);

		// TODO Can we shortcut in any way?

		// Which values have "changed" in any way?
		final Iterable<Object> changed = Iterables.filter(leftValues, not(containedIn(getComparison(), lcs)));

		// Added or moved
		for (Object diffCandidate : changed) {
			/*
			 * This value is not in the LCS between right and left, we thus know it has changed. If it is
			 * present in the right, this is a move. Otherwise, it has been added in the left list.
			 */
			if (contains(getComparison(), rightValues, diffCandidate)) {
				if (checkOrdering) {
					featureChange(match, feature, diffCandidate, DifferenceKind.MOVE, DifferenceSource.LEFT);
				}
			} else {
				featureChange(match, feature, diffCandidate, DifferenceKind.ADD, DifferenceSource.LEFT);
			}
		}

		// deleted
		for (Object diffCandidate : rightValues) {
			// A value that is in the right but not in the left has been deleted.
			// However, we do not want attribute changes on removed elements.
			if (!contains(getComparison(), leftValues, diffCandidate)) {
				if (feature instanceof EReference || match.getLeft() != null) {
					featureChange(match, feature, diffCandidate, DifferenceKind.DELETE, DifferenceSource.LEFT);
				}
			}
		}
	}

	/**
	 * Computes the difference between the sides of the given <code>match</code> for the given single-valued
	 * <code>attribute</code>.
	 * 
	 * @param match
	 *            The match which sides we need to check for potential differences.
	 * @param attribute
	 *            The attribute which values are to be checked.
	 */
	protected void computeSingleValuedAttributeDifferences(Match match, EAttribute attribute) {
		Object leftValue = UNMATCHED_VALUE;
		if (match.getLeft() != null) {
			leftValue = match.getLeft().eGet(attribute);
		}
		Object rightValue = UNMATCHED_VALUE;
		if (match.getRight() != null) {
			rightValue = match.getRight().eGet(attribute);
		}

		if (EqualityHelper.matchingValues(getComparison(), leftValue, rightValue)) {
			// Identical values in left and right. The only problematic case is if they do not match the
			// origin (and left and right are defined, i.e don't detect attribute change on unmatched)
			if (leftValue != UNMATCHED_VALUE && match.getOrigin() != null) {
				final Object originValue = match.getOrigin().eGet(attribute);

				if (!EqualityHelper.matchingValues(getComparison(), leftValue, originValue)) {
					// The same change has been made on both side. This is actually a pseudo-conflict
					getDiffProcessor().attributeChange(match, attribute, originValue, DifferenceKind.CHANGE,
							DifferenceSource.LEFT);
					getDiffProcessor().attributeChange(match, attribute, originValue, DifferenceKind.CHANGE,
							DifferenceSource.RIGHT);
				}
			}
		} else if (match.getOrigin() != null) {
			final Object originValue = match.getOrigin().eGet(attribute);

			if (EqualityHelper.matchingValues(getComparison(), leftValue, originValue)) {
				if (rightValue != UNMATCHED_VALUE) {
					// Value is in left and origin, but not in the right
					getDiffProcessor().attributeChange(match, attribute, rightValue, DifferenceKind.CHANGE,
							DifferenceSource.RIGHT);
				} else {
					// Right is unmatched, left is the same as in the origin. No diff here : the diff is on
					// the match itself, not on one of its attributes.
				}
			} else if (EqualityHelper.matchingValues(getComparison(), rightValue, originValue)) {
				if (leftValue != UNMATCHED_VALUE) {
					// Value is in right and origin, but not in left
					getDiffProcessor().attributeChange(match, attribute, leftValue, DifferenceKind.CHANGE,
							DifferenceSource.LEFT);
				} else {
					// Left is unmatched, right is the same as in the origin. No diff here : the diff is on
					// the match itself, not on one of its attributes.
				}
			} else {
				/*
				 * Left and right are different. None match what's in the origin. Those of the two that are
				 * not unmatched are thus a "change" difference, with a possible conflict.
				 */
				if (leftValue != UNMATCHED_VALUE) {
					getDiffProcessor().attributeChange(match, attribute, leftValue, DifferenceKind.CHANGE,
							DifferenceSource.LEFT);
				}
				if (rightValue != UNMATCHED_VALUE) {
					getDiffProcessor().attributeChange(match, attribute, rightValue, DifferenceKind.CHANGE,
							DifferenceSource.RIGHT);
				}
			}
		} else {
			// Left and right values are different, and we have no origin.
			if (leftValue != UNMATCHED_VALUE) {
				getDiffProcessor().attributeChange(match, attribute, leftValue, DifferenceKind.CHANGE,
						DifferenceSource.LEFT);
			}
			if (getComparison().isThreeWay() && rightValue != UNMATCHED_VALUE) {
				getDiffProcessor().attributeChange(match, attribute, rightValue, DifferenceKind.CHANGE,
						DifferenceSource.RIGHT);
			}
		}
	}

	/**
	 * Computes the difference between the sides of the given <code>match</code> for the given single-valued
	 * <code>reference</code>.
	 * <p>
	 * The given {@code reference} cannot be a containment reference.
	 * </p>
	 * <p>
	 * This is only meant for three-way comparisons.
	 * </p>
	 * 
	 * @param match
	 *            The match which sides we need to check for potential differences.
	 * @param reference
	 *            The reference which values are to be checked.
	 */
	protected void computeSingleValuedReferenceDifferencesThreeWay(Match match, EReference reference) {
		Object leftValue = UNMATCHED_VALUE;
		if (match.getLeft() != null) {
			leftValue = match.getLeft().eGet(reference);
		}
		Object rightValue = UNMATCHED_VALUE;
		if (match.getRight() != null) {
			rightValue = match.getRight().eGet(reference);
		}
		Object originValue = UNMATCHED_VALUE;
		if (match.getOrigin() != null) {
			originValue = match.getOrigin().eGet(reference);
		}

		final Match leftValueMatch;
		if (leftValue instanceof EObject) {
			leftValueMatch = getComparison().getMatch((EObject)leftValue);
		} else {
			leftValueMatch = null;
		}

		final boolean distinctValueLO;
		if (leftValueMatch != null) {
			distinctValueLO = originValue == null || leftValueMatch.getOrigin() != originValue;
		} else {
			distinctValueLO = originValue instanceof EObject;
		}

		if (distinctValueLO) {
			final boolean leftOutOfScope = leftValue instanceof EObject && leftValueMatch == null;
			final boolean originOutOfScope = originValue instanceof EObject
					&& getComparison().getMatch((EObject)originValue) == null;

			// Left and origin are distinct
			if (leftValueMatch != null && !leftOutOfScope) {
				// Left has been set to a new value, or left has been added altogether
				getDiffProcessor().referenceChange(match, reference, (EObject)leftValue,
						DifferenceKind.CHANGE, DifferenceSource.LEFT);
			} else if (originValue != UNMATCHED_VALUE && !originOutOfScope) {
				// left value is unset, or left has been removed
				getDiffProcessor().referenceChange(match, reference, (EObject)originValue,
						DifferenceKind.CHANGE, DifferenceSource.LEFT);
			} else {
				// left has been added. This reference is either unset or set to an out of scope value
			}
		}

		final Match rightValueMatch;
		if (rightValue instanceof EObject) {
			rightValueMatch = getComparison().getMatch((EObject)rightValue);
		} else {
			rightValueMatch = null;
		}

		final boolean distinctValueRO;
		if (rightValueMatch != null) {
			distinctValueRO = originValue == null || rightValueMatch.getOrigin() != originValue;
		} else {
			distinctValueRO = originValue instanceof EObject;
		}

		if (distinctValueRO) {
			final boolean rightOutOfScope = rightValue instanceof EObject && rightValueMatch == null;
			final boolean originOutOfScope = originValue instanceof EObject
					&& getComparison().getMatch((EObject)originValue) == null;

			// Right and origin are distinct
			if (rightValueMatch != null && !rightOutOfScope) {
				// Right has been set to a new value, or right has been added altogether
				getDiffProcessor().referenceChange(match, reference, (EObject)rightValue,
						DifferenceKind.CHANGE, DifferenceSource.RIGHT);
			} else if (originValue != UNMATCHED_VALUE && !originOutOfScope) {
				// right value is unset, or right has been removed
				getDiffProcessor().referenceChange(match, reference, (EObject)originValue,
						DifferenceKind.CHANGE, DifferenceSource.RIGHT);
			} else {
				// right has been added. This reference is either unset or set to an out of scope value
			}
		}
	}

	/**
	 * Computes the difference between the sides of the given <code>match</code> for the given single-valued
	 * <code>reference</code>.
	 * <p>
	 * The given {@code reference} cannot be a containment reference.
	 * </p>
	 * <p>
	 * This is only meant for two-way comparisons.
	 * </p>
	 * 
	 * @param match
	 *            The match which sides we need to check for potential differences.
	 * @param reference
	 *            The reference which values are to be checked.
	 */
	protected void computeSingleValuedReferenceDifferencesTwoWay(Match match, EReference reference) {
		Object leftValue = UNMATCHED_VALUE;
		if (match.getLeft() != null) {
			leftValue = match.getLeft().eGet(reference);
		}
		Object rightValue = UNMATCHED_VALUE;
		if (match.getRight() != null) {
			rightValue = match.getRight().eGet(reference);
		}

		final Match leftValueMatch;
		if (leftValue instanceof EObject) {
			leftValueMatch = getComparison().getMatch((EObject)leftValue);
		} else {
			leftValueMatch = null;
		}

		final boolean distinctValue;
		if (leftValueMatch != null) {
			distinctValue = rightValue == null || leftValueMatch.getRight() != rightValue;
		} else {
			distinctValue = rightValue instanceof EObject;
		}

		if (distinctValue) {
			final boolean leftOutOfScope = leftValue instanceof EObject && leftValueMatch == null;
			final boolean rightOutOfScope = rightValue instanceof EObject
					&& getComparison().getMatch((EObject)rightValue) == null;

			/*
			 * TODO should probably detect diffs even for out of scope values. What if I changed the type of a
			 * reference from "EInt" to "EString" ? Holds true for three way too.
			 */

			if (leftValueMatch != null && !leftOutOfScope) {
				// Left has been set to a new value, or left has been added altogether
				getDiffProcessor().referenceChange(match, reference, (EObject)leftValue,
						DifferenceKind.CHANGE, DifferenceSource.LEFT);
			} else if (rightValue != UNMATCHED_VALUE && !rightOutOfScope) {
				// left value is unset, or left has been removed
				getDiffProcessor().referenceChange(match, reference, (EObject)rightValue,
						DifferenceKind.CHANGE, DifferenceSource.LEFT);
			} else {
				// left has been added. This reference is either unset or set to an out of scope value
			}
		}
	}

	/**
	 * This will be used in order to create the diff processor that is to be used by this diff engine.
	 * 
	 * @return The diff processor of this diff engine. Will only be called once per call to
	 *         {@link #diff(Comparison)}.
	 */
	protected IDiffProcessor createDiffProcessor() {
		return new DiffBuilder();
	}

	/**
	 * This will be used in order to create the {@link FeatureFilter} that should be used by this engine to
	 * determine the structural features on which it is to try and detect differences.
	 * 
	 * @return The newly created feature filter.
	 */
	protected FeatureFilter createFeatureFilter() {
		return new FeatureFilter();
	}

	/**
	 * Delegates to the diff processor to create the specified feature change.
	 * 
	 * @param match
	 *            The match on which values we detected a diff.
	 * @param feature
	 *            The exact feature on which a diff was detected.
	 * @param value
	 *            The value for which we detected a changed.
	 * @param kind
	 *            The kind of difference to create.
	 * @param source
	 *            The source from which originates that diff.
	 */
	protected void featureChange(Match match, EStructuralFeature feature, Object value, DifferenceKind kind,
			DifferenceSource source) {
		if (feature instanceof EAttribute) {
			getDiffProcessor().attributeChange(match, (EAttribute)feature, value, kind, source);
		} else if (value instanceof EObject) {
			getDiffProcessor().referenceChange(match, (EReference)feature, (EObject)value, kind, source);
		}
	}

	/**
	 * Returns the comparison for which we are currently detecting differences.
	 * 
	 * @return The comparison for which we are currently detecting differences.
	 */
	protected Comparison getComparison() {
		return currentComparison;
	}

	/**
	 * This will return the diff processor that has been created through {@link #createDiffProcessor()} for
	 * this differencing process.
	 * 
	 * @return The diff processor to notify of difference detections.
	 */
	protected final IDiffProcessor getDiffProcessor() {
		return diffProcessor;
	}
}
