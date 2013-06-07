/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.utils;

import com.google.common.base.Function;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;

/**
 * This utility class will be used to provide similarity implementations.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @deprecated Not intendended to be used by clients
 */
@Deprecated
public final class DiffUtil {
	/** This utility class does not need to be instantiated. */
	private DiffUtil() {
		// Hides default constructor
	}

	/**
	 * Computes the dice coefficient between the two given String's bigrams.
	 * <p>
	 * This implementation is case sensitive.
	 * </p>
	 * 
	 * @param first
	 *            First of the two Strings to compare.
	 * @param second
	 *            Second of the two Strings to compare.
	 * @return The dice coefficient of the two given String's bigrams, ranging from 0 to 1.
	 */
	public static double diceCoefficient(String first, String second) {
		return org.eclipse.emf.compare.internal.utils.DiffUtil.diceCoefficient(first, second);
	}

	/**
	 * This will compute the longest common subsequence between the two given Lists, ignoring any object that
	 * is included in {@code ignoredElements}. We will use
	 * {@link EqualityHelper#matchingValues(Comparison, Object, Object)} in order to try and match the values
	 * from both lists two-by-two. This can thus be used both for reference values or attribute values. If
	 * there are two subsequences of the same "longest" length, the first (according to the second argument)
	 * will be returned.
	 * <p>
	 * Take note that this might be slower than
	 * {@link #longestCommonSubsequence(Comparison, EqualityHelper, List, List)} and should only be used when
	 * elements should be removed from the potential LCS. This is mainly aimed at merge operations during
	 * three-way comparisons as some objects might be in conflict and thus shifting the computed insertion
	 * indices.
	 * </p>
	 * <p>
	 * Please see {@link #longestCommonSubsequence(Comparison, EqualityHelper, List, List)} for a more
	 * complete description.
	 * </p>
	 * 
	 * @param comparison
	 *            This will be used in order to retrieve the Match for EObjects when comparing them.
	 * @param ignoredElements
	 *            Specifies elements that should be excluded from the subsequences.
	 * @param sequence1
	 *            First of the two sequences to consider.
	 * @param sequence2
	 *            Second of the two sequences to consider.
	 * @param <E>
	 *            Type of the sequences content.
	 * @return The LCS of the two given sequences. Will never be the same instance as one of the input
	 *         sequences.
	 * @see #longestCommonSubsequence(Comparison, EqualityHelper, List, List).
	 */
	public static <E> List<E> longestCommonSubsequence(Comparison comparison, Iterable<E> ignoredElements,
			List<E> sequence1, List<E> sequence2) {
		return org.eclipse.emf.compare.internal.utils.DiffUtil.longestCommonSubsequence(comparison,
				ignoredElements, sequence1, sequence2);
	}

	/**
	 * This will compute the longest common subsequence between the two given Lists. We will use
	 * {@link EqualityHelper#matchingValues(Comparison, Object, Object)} in order to try and match the values
	 * from both lists two-by-two. This can thus be used both for reference values or attribute values. If
	 * there are two subsequences of the same "longest" length, the first (according to the second argument)
	 * will be returned.
	 * <p>
	 * For example, it the two given sequence are, in this order, <code>{"a", "b", "c", "d", "e"}</code> and
	 * <code>{"c", "z", "d", "a", "b"}</code>, there are two "longest" subsequences : <code>{"a", "b"}</code>
	 * and <code>{"c", "d"}</code>. The first of those two subsequences in the second list is
	 * <code>{"c", "d"}</code>. On the other hand, the LCS of <code>{"a", "b", "c", "d", "e"}</code> and
	 * <code>{"y", "c", "d", "e", "b"}</code> is <code>{"c", "d", "e"}</code>.
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
	public static <E> List<E> longestCommonSubsequence(Comparison comparison, List<E> sequence1,
			List<E> sequence2) {
		return org.eclipse.emf.compare.internal.utils.DiffUtil.longestCommonSubsequence(comparison,
				sequence1, sequence2);
	}

	/**
	 * This will try and determine the index at which a given element from the {@code source} list should be
	 * inserted in the {@code target} list. We expect {@code newElement} to be an element from the
	 * {@code source} or to have a Match that allows us to map it to one of the {@code source} list's
	 * elements.
	 * <p>
	 * The expected insertion index will always be relative to the Longest Common Subsequence (LCS) between
	 * the two given lists, ignoring all elements from that LCS that have changed between the target list and
	 * the common origin of the two. If there are more than one "longest" subsequence between the two lists,
	 * the insertion index will be relative to the first that comes in the {@code target} list.
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
	 * @param ignoredElements
	 *            If there are elements from {@code target} that should be ignored when searching for an
	 *            insertion index, set them here. Can be {@code null} or an empty list.
	 * @param source
	 *            The List from which one element has to be added to the {@code target} list.
	 * @param target
	 *            The List into which one element from {@code source} has to be added.
	 * @param newElement
	 *            The element from {@code source} that needs to be added into {@code target}.
	 * @param <E>
	 *            Type of the sequences content.
	 * @return The index at which {@code newElement} should be inserted in {@code target}.
	 * @see #longestCommonSubsequence(Comparison, List, List)
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public static <E> int findInsertionIndex(Comparison comparison, Iterable<E> ignoredElements,
			List<E> source, List<E> target, E newElement) {
		return org.eclipse.emf.compare.internal.utils.DiffUtil.findInsertionIndex(comparison,
				ignoredElements, source, target, newElement);
	}

	/**
	 * This will try and determine the index at which a given element from the {@code source} list should be
	 * inserted in the {@code target} list. We expect {@code newElement} to be an element from the
	 * {@code source} or to have a Match that allows us to map it to one of the {@code source} list's
	 * elements.
	 * <p>
	 * The expected insertion index will always be relative to the Longest Common Subsequence (LCS) between
	 * the two given lists. If there are more than one "longest" subsequence between the two lists, the
	 * insertion index will be relative to the first that comes in the {@code target} list.
	 * </p>
	 * <p>
	 * For example, assume {@code source} is <code>{"1", "2", "4", "6", "8", "3", "0", "7", "5"}</code> and
	 * {@code target} is <code>{"8", "1", "2", "9", "3", "4", "7"}</code>; I try to merge the addition of
	 * {@code "0"} in the right list. The returned "insertion index" will be {@code 5} : just after
	 * {@code "3"}. There are two subsequence of the same "longest" length 4 :
	 * <code>{"1", "2", "3", "7"}</code> and <code>{"1", "2", "4", "7"}</code>. However, the first of those
	 * two in {@code target} is <code>{"1", "2", "3", "7"}</code>. The closest element before {@code "0"} in
	 * this LCS in {@code source} is {@code "3"}.
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
	 * @param source
	 *            The List from which one element has to be added to the {@code target} list.
	 * @param target
	 *            The List into which one element from {@code source} has to be added.
	 * @param newElement
	 *            The element from {@code source} that needs to be added into {@code target}.
	 * @param <E>
	 *            Type of the sequences content.
	 * @return The index at which {@code newElement} should be inserted in {@code target}.
	 * @see #longestCommonSubsequence(Comparison, List, List)
	 */
	public static <E> int findInsertionIndex(Comparison comparison, List<E> source, List<E> target,
			E newElement) {
		return org.eclipse.emf.compare.internal.utils.DiffUtil.findInsertionIndex(comparison, source, target,
				newElement);
	}

	/**
	 * This is the main entry point for
	 * {@link #findInsertionIndex(Comparison, EqualityHelper, Iterable, List, List, Object)}. It will use
	 * default algorithms to determine the source and target lists as well as the list of elements that should
	 * be ignored when computing the insertion index.
	 * 
	 * @param comparison
	 *            This will be used in order to retrieve the Match for EObjects when comparing them.
	 * @param diff
	 *            The diff which merging will trigger the need for an insertion index in its target list.
	 * @param rightToLeft
	 *            {@code true} if the merging will be done into the left list, so that we should consider the
	 *            right model as the source and the left as the target.
	 * @return The index at which this {@code diff}'s value should be inserted into the 'target' list, as
	 *         inferred from {@code rightToLeft}.
	 * @see #findInsertionIndex(Comparison, Iterable, List, List, Object)
	 */
	public static int findInsertionIndex(Comparison comparison, Diff diff, boolean rightToLeft) {
		return org.eclipse.emf.compare.internal.utils.DiffUtil.findInsertionIndex(comparison, diff,
				rightToLeft);
	}

	/**
	 * When merging a {@link Diff}, returns the sub diffs of this given diff, and all associated diffs (see
	 * {@link DiffUtil#getAssociatedDiffs(Iterable, boolean, Diff)}) of these sub diffs.
	 * <p>
	 * If the diff is an {@link org.eclipse.emf.compare.AttributeChange} or a @{code
	 * ResourceAttachmentChange}, this method will return an empty iterable.
	 * </p>
	 * <p>
	 * If the diff is a {@link org.eclipse.emf.compare.ReferenceChange} this method will return all
	 * differences contained in the match that contains the value of the reference change, and all associated
	 * diffs of these differences.
	 * </p>
	 * 
	 * @param leftToRight
	 *            the direction of merge.
	 * @return an iterable containing the sub diffs of this given diff, and all associated diffs of these sub
	 *         diffs.
	 * @since 3.0
	 */
	public static Function<Diff, Iterable<Diff>> getSubDiffs(final boolean leftToRight) {
		return ComparisonUtil.getSubDiffs(leftToRight);
	}

	/**
	 * When merging a {@link Diff}, returns the associated diffs of the sub diffs of the diff, and all sub
	 * diffs (see {@link DiffUtil#getSubDiffs(boolean)}) of these associated diffs.
	 * <p>
	 * The associated diffs of a diff are :
	 * <p>
	 * - {@link Diff#getRequiredBy()} if the source of the diff is the left side and the direction of the
	 * merge is right to left.
	 * </p>
	 * <p>
	 * - {@link Diff#getRequiredBy()} if the source of the diff is the right side and the direction of the
	 * merge is left to right.
	 * </p>
	 * <p>
	 * - {@link Diff#getRequires()} if the source of the diff is the left side and the direction of the merge
	 * is left to right.
	 * </p>
	 * <p>
	 * - {@link Diff#getRequires()} if the source of the diff is the right side and the direction of the merge
	 * is right to left.
	 * </p>
	 * </p>
	 * 
	 * @param diffRoot
	 *            the given diff.
	 * @param subDiffs
	 *            the iterable of sub diffs for which we want the associated diffs.
	 * @param leftToRight
	 *            the direction of merge.
	 * @return an empty list.
	 * @since 3.0
	 * @deprecated
	 */
	@Deprecated
	public static Iterable<Diff> getAssociatedDiffs(final Diff diffRoot, Iterable<Diff> subDiffs,
			boolean leftToRight) {
		return Collections.emptyList();
	}
}
