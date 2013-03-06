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

import static com.google.common.collect.Iterables.addAll;
import static com.google.common.collect.Iterables.concat;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompareMessages;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * This utility class will be used to provide similarity implementations.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
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
		final char[] str1 = first.toCharArray();
		final char[] str2 = second.toCharArray();

		final double coefficient;

		if (Arrays.equals(str1, str2)) {
			coefficient = 1d;
		} else if (str1.length <= 2 || str2.length <= 2) {
			int equalChars = 0;

			for (int i = 0; i < Math.min(str1.length, str2.length); i++) {
				if (str1[i] == str2[i]) {
					equalChars++;
				}
			}

			int union = str1.length + str2.length;
			if (str1.length != str2.length) {
				coefficient = (double)equalChars / union;
			} else {
				coefficient = ((double)equalChars * 2) / union;
			}
		} else {
			Set<String> s1Bigrams = Sets.newHashSet();
			Set<String> s2Bigrams = Sets.newHashSet();

			for (int i = 0; i < str1.length - 1; i++) {
				char[] chars = new char[] {str1[i], str1[i + 1], };
				s1Bigrams.add(String.valueOf(chars));
			}
			for (int i = 0; i < str2.length - 1; i++) {
				char[] chars = new char[] {str2[i], str2[i + 1], };
				s2Bigrams.add(String.valueOf(chars));
			}

			Set<String> intersection = Sets.intersection(s1Bigrams, s2Bigrams);
			coefficient = (2d * intersection.size()) / (s1Bigrams.size() + s2Bigrams.size());
		}

		return coefficient;
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
		// FIXME : merge the two "LCS" methods in one (only one line differs...)
		final List<E> copy1 = Lists.newArrayList(sequence1);
		final List<E> copy2 = Lists.newArrayList(sequence2);

		// Reduce sets
		final List<E> prefix = trimPrefix(comparison, ignoredElements, copy1, copy2);
		final List<E> suffix = trimSuffix(comparison, ignoredElements, copy1, copy2);

		final IEqualityHelper equalityHelper = comparison.getEqualityHelper();
		final int size1 = copy1.size();
		final int size2 = copy2.size();

		final int[][] matrix = new int[size1 + 1][size2 + 1];

		// Compute the LCS matrix
		for (int i = 1; i <= size1; i++) {
			final E first = copy1.get(i - 1);
			for (int j = 1; j <= size2; j++) {
				final E second = copy2.get(j - 1);
				if (equalityHelper.matchingValues(first, second)
						&& !contains(comparison, equalityHelper, ignoredElements, second)) {
					matrix[i][j] = 1 + matrix[i - 1][j - 1];
				} else {
					matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i][j - 1]);
				}
			}
		}

		// Traceback the matrix to create the final LCS
		int current1 = size1;
		int current2 = size2;
		final List<E> result = Lists.newArrayList();

		while (current1 > 0 && current2 > 0) {
			final int currentLength = matrix[current1][current2];
			final int nextLeft = matrix[current1][current2 - 1];
			final int nextUp = matrix[current1 - 1][current2];
			if (currentLength > nextLeft && currentLength > nextUp) {
				result.add(copy1.get(current1 - 1));
				current1--;
				current2--;
			} else if (nextLeft >= nextUp) {
				current2--;
			} else {
				current1--;
			}
		}

		return ImmutableList.copyOf(Iterables.concat(prefix, Lists.reverse(result), suffix));
	}

	/**
	 * Trims and returns the common prefix of the two given sequences. All ignored elements within or after
	 * this common prefix will also be trimmed.
	 * <p>
	 * Note that the two given sequences will be modified in-place.
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
	 * @return The common prefix of the two given sequences, less their ignored elements. As a side note, both
	 *         {@code sequence1} and {@code sequence2} will have been trimmed of their prefix when this
	 *         returns.
	 */
	private static <E> List<E> trimPrefix(Comparison comparison, Iterable<E> ignoredElements,
			List<E> sequence1, List<E> sequence2) {
		final IEqualityHelper equalityHelper = comparison.getEqualityHelper();
		final int size1 = sequence1.size();
		final int size2 = sequence2.size();

		final List<E> prefix = Lists.newArrayList();
		int start1 = 1;
		int start2 = 1;
		boolean matching = true;
		while (start1 <= size1 && start2 <= size2 && matching) {
			final E first = sequence1.get(start1 - 1);
			final E second = sequence2.get(start2 - 1);
			if (equalityHelper.matchingValues(first, second)) {
				prefix.add(first);
				start1++;
				start2++;
			} else {
				boolean ignore1 = contains(comparison, equalityHelper, ignoredElements, first);
				boolean ignore2 = contains(comparison, equalityHelper, ignoredElements, second);
				if (ignore1) {
					start1++;
				}
				if (ignore2) {
					start2++;
				}
				if (!ignore1 && !ignore2) {
					matching = false;
				}
			}
		}
		for (int i = 1; i < start1; i++) {
			sequence1.remove(0);
		}
		for (int i = 1; i < start2; i++) {
			sequence2.remove(0);
		}

		return prefix;
	}

	/**
	 * Trims and returns the common suffix of the two given sequences. All ignored elements within or before
	 * this common suffix will also be trimmed.
	 * <p>
	 * Note that the two given sequences will be modified in-place.
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
	 * @return The common suffix of the two given sequences, less their ignored elements. As a side note, both
	 *         {@code sequence1} and {@code sequence2} will have been trimmed of their suffix when this
	 *         returns.
	 */
	private static <E> List<E> trimSuffix(Comparison comparison, Iterable<E> ignoredElements,
			List<E> sequence1, List<E> sequence2) {
		final IEqualityHelper equalityHelper = comparison.getEqualityHelper();
		final int size1 = sequence1.size();
		final int size2 = sequence2.size();

		final List<E> suffix = Lists.newArrayList();
		int end1 = size1;
		int end2 = size2;
		boolean matching = true;
		while (end1 > 0 && end2 > 0 && matching) {
			final E first = sequence1.get(end1 - 1);
			final E second = sequence2.get(end2 - 1);
			if (equalityHelper.matchingValues(first, second)) {
				suffix.add(first);
				end1--;
				end2--;
			} else {
				boolean ignore1 = contains(comparison, equalityHelper, ignoredElements, first);
				boolean ignore2 = contains(comparison, equalityHelper, ignoredElements, second);
				if (ignore1) {
					end1--;
				}
				if (ignore2) {
					end2--;
				}
				if (!ignore1 && !ignore2) {
					matching = false;
				}
			}
		}
		for (int i = size1; i > end1; i--) {
			sequence1.remove(sequence1.size() - 1);
		}
		for (int i = size2; i > end2; i--) {
			sequence2.remove(sequence2.size() - 1);
		}

		return Lists.reverse(suffix);
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
		return longestCommonSubsequence(comparison, Collections.<E> emptyList(), sequence1, sequence2);
	}

	/*
	 * TODO perf : all "lookups" in source and target could be rewritten by using the lcs elements' matches.
	 * This may or may not help, should be profiled.
	 */
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
		final IEqualityHelper equalityHelper = comparison.getEqualityHelper();

		// We assume that "newElement" is in source but not in the target yet
		final List<E> lcs;
		if (ignoredElements != null) {
			lcs = longestCommonSubsequence(comparison, ignoredElements, source, target);
		} else {
			lcs = longestCommonSubsequence(comparison, source, target);
		}

		E firstLCS = null;
		E lastLCS = null;
		if (lcs.size() > 0) {
			firstLCS = lcs.get(0);
			lastLCS = lcs.listIterator(lcs.size()).previous();
		}

		final int noLCS = -2;
		int currentIndex = -1;
		int firstLCSIndex = -1;
		int lastLCSIndex = -1;
		if (firstLCS == null) {
			// We have no LCS
			firstLCSIndex = noLCS;
			lastLCSIndex = noLCS;
		}

		ListIterator<E> sourceIterator = source.listIterator();
		for (int i = 0; sourceIterator.hasNext() && (currentIndex == -1 || firstLCSIndex == -1); i++) {
			final E sourceElement = sourceIterator.next();
			if (currentIndex == -1 && equalityHelper.matchingValues(sourceElement, newElement)) {
				currentIndex = i;
			}
			if (firstLCSIndex == -1 && equalityHelper.matchingValues(sourceElement, firstLCS)) {
				firstLCSIndex = i;
			}
		}
		// The list may contain duplicates, use a reverse iteration to find the last from LCS.
		final int sourceSize = source.size();
		sourceIterator = source.listIterator(sourceSize);
		for (int i = sourceSize - 1; sourceIterator.hasPrevious() && lastLCSIndex == -1; i--) {
			final E sourceElement = sourceIterator.previous();
			if (lastLCSIndex == -1 && equalityHelper.matchingValues(sourceElement, lastLCS)) {
				lastLCSIndex = i;
			}
		}

		int insertionIndex = -1;
		if (firstLCSIndex == noLCS) {
			// We have no LCS. The two lists have no element in common. Insert at the very end of the target.
			insertionIndex = target.size();
		} else if (currentIndex < firstLCSIndex) {
			// The object we are to insert is before the LCS in source.
			insertionIndex = insertBeforeLCS(target, equalityHelper, firstLCS);
		} else if (currentIndex > lastLCSIndex) {
			// The object we are to insert is after the LCS in source.
			insertionIndex = findInsertionIndexAfterLCS(target, equalityHelper, lastLCS);
		} else {
			// Our object is in-between two elements A and B of the LCS in source
			insertionIndex = findInsertionIndexWithinLCS(source, target, equalityHelper, lcs, currentIndex);
		}

		// We somehow failed to determine the insertion index. Insert at the very end.
		if (insertionIndex == -1) {
			insertionIndex = target.size();
		}

		return insertionIndex;
	}

	/**
	 * This will be called to try and find the insertion index for an element that is located in-between two
	 * elements of the LCS between {@code source} and {@code target}.
	 * 
	 * @param source
	 *            The List from which one element has to be added to the {@code target} list.
	 * @param target
	 *            The List into which one element from {@code source} has to be added.
	 * @param equalityHelper
	 *            The equality helper to use for this computation.
	 * @param lcs
	 *            The lcs between {@code source} and {@code target}.
	 * @param currentIndex
	 *            Current index (in {@code source} of the element we are to insert into {@code target}.
	 * @param <E>
	 *            Type of the sequences content.
	 * @return The index in the target list in which should be inserted that element.
	 */
	private static <E> int findInsertionIndexWithinLCS(List<E> source, List<E> target,
			final IEqualityHelper equalityHelper, final List<E> lcs, int currentIndex) {
		int insertionIndex = -1;
		/*
		 * If any element of the subsequence {<index of A>, <index of B>} from source had been in the same
		 * subsequence in target, it would have been part of the LCS. We thus know none is.
		 */
		// The insertion index will be just after A in target

		// First, find which element of the LCS is "A"
		int lcsIndexOfSubsequenceStart = -1;
		for (int i = 0; i < currentIndex; i++) {
			final E sourceElement = source.get(i);

			boolean isInLCS = false;
			for (int j = lcsIndexOfSubsequenceStart + 1; j < lcs.size() && !isInLCS; j++) {
				final E lcsElement = lcs.get(j);

				if (equalityHelper.matchingValues(sourceElement, lcsElement)) {
					isInLCS = true;
					lcsIndexOfSubsequenceStart++;
				}
			}
		}

		// Do we have duplicates before A in the lcs?
		final Multiset<E> dupesLCS = HashMultiset.create(lcs.subList(0, lcsIndexOfSubsequenceStart + 1));
		final E subsequenceStart = lcs.get(lcsIndexOfSubsequenceStart);
		int duplicatesToGo = dupesLCS.count(subsequenceStart) - 1;

		// Then, find the index of "A" in target
		for (int i = 0; i < target.size() && insertionIndex == -1; i++) {
			final E targetElement = target.get(i);

			if (equalityHelper.matchingValues(targetElement, subsequenceStart)) {
				if (duplicatesToGo > 0) {
					duplicatesToGo--;
				} else {
					insertionIndex = i + 1;
				}
			}
		}

		return insertionIndex;
	}

	/**
	 * This will be called when we are to insert an element after the LCS in the {@code target} list.
	 * 
	 * @param target
	 *            The List into which one element has to be added.
	 * @param equalityHelper
	 *            The equality helper to use for this computation.
	 * @param lastLCS
	 *            The last element of the LCS.
	 * @param <E>
	 *            Type of the sequences content.
	 * @return The index to use for insertion into {@code target} in order to add an element just after the
	 *         LCS.
	 */
	private static <E> int findInsertionIndexAfterLCS(List<E> target, IEqualityHelper equalityHelper,
			E lastLCS) {
		int insertionIndex = -1;
		// The insertion index will be inside the subsequence {<LCS end>, <list.size()>} in target.
		/*
		 * We'll insert it just after the LCS end : there cannot be any common element between the two lists
		 * "after" the LCS since it would be part of the LCS itself.
		 */
		for (int i = target.size() - 1; i >= 0 && insertionIndex == -1; i--) {
			final E targetElement = target.get(i);
			if (equalityHelper.matchingValues(targetElement, lastLCS)) {
				// We've reached the last element of the LCS in target. insert after it.
				insertionIndex = i + 1;
			}
		}
		return insertionIndex;
	}

	/**
	 * This will be called when we are to insert an element before the LCS in the {@code target} list.
	 * 
	 * @param target
	 *            The List into which one element has to be added.
	 * @param equalityHelper
	 *            The equality helper to use for this computation.
	 * @param firstLCS
	 *            The first element of the LCS.
	 * @param <E>
	 *            Type of the sequences content.
	 * @return The index to use for insertion into {@code target} in order to add an element just before the
	 *         LCS.
	 */
	private static <E> int insertBeforeLCS(List<E> target, IEqualityHelper equalityHelper, E firstLCS) {
		int insertionIndex = -1;
		// The insertion index will be inside the subsequence {0, <LCS start>} in target
		/*
		 * We'll insert it just before the LCS start : there cannot be any common element between the two
		 * lists "before" the LCS since it would be part of the LCS itself.
		 */
		for (int i = 0; i < target.size() && insertionIndex == -1; i++) {
			final E targetElement = target.get(i);

			if (equalityHelper.matchingValues(targetElement, firstLCS)) {
				// We've reached the first element from the LCS in target. Insert here
				insertionIndex = i;
			}
		}
		return insertionIndex;
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
		return findInsertionIndex(comparison, null, source, target, newElement);
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
	@SuppressWarnings("unchecked")
	public static int findInsertionIndex(Comparison comparison, Diff diff, boolean rightToLeft) {
		final EStructuralFeature feature;
		final Object value;
		if (diff instanceof AttributeChange) {
			feature = ((AttributeChange)diff).getAttribute();
			value = ((AttributeChange)diff).getValue();
		} else if (diff instanceof ReferenceChange) {
			feature = ((ReferenceChange)diff).getReference();
			value = ((ReferenceChange)diff).getValue();
		} else {
			throw new IllegalArgumentException(EMFCompareMessages.getString(
					"DiffUtil.IllegalDiff", diff.eClass().getName())); //$NON-NLS-1$
		}
		if (!feature.isMany()) {
			throw new IllegalArgumentException(EMFCompareMessages.getString(
					"DiffUtil.IllegalFeature", feature.getName())); //$NON-NLS-1$
		}
		final Match match = diff.getMatch();

		final EObject expectedContainer;
		if (feature instanceof EReference && ((EReference)feature).isContainment()
				&& diff.getKind() == DifferenceKind.MOVE) {
			// The value can only be an EObject, and its match cannot be null.
			// If any of these two assumptions is wrong, something went horribly awry beforehand.
			final Match valueMatch = comparison.getMatch((EObject)value);

			final Match targetContainerMatch;
			// If it exists, use the source side's container as reference
			if (rightToLeft && valueMatch.getRight() != null) {
				targetContainerMatch = comparison.getMatch(valueMatch.getRight().eContainer());
			} else if (!rightToLeft && valueMatch.getLeft() != null) {
				targetContainerMatch = comparison.getMatch(valueMatch.getLeft().eContainer());
			} else {
				// Otherwise, the value we're moving on one side has been removed from its source side.
				targetContainerMatch = comparison.getMatch(valueMatch.getOrigin().eContainer());
			}
			if (rightToLeft) {
				expectedContainer = targetContainerMatch.getLeft();
			} else {
				expectedContainer = targetContainerMatch.getRight();
			}
		} else if (rightToLeft) {
			expectedContainer = match.getLeft();
		} else {
			expectedContainer = match.getRight();
		}

		final List<Object> sourceList = getSourceList(diff, feature, rightToLeft);
		final List<Object> targetList;

		if (expectedContainer != null) {
			final List<Object> temp = (List<Object>)ReferenceUtil.safeEGet(expectedContainer, feature);
			if (feature == EcorePackage.Literals.ECLASS__ESUPER_TYPES
					|| feature == EcorePackage.Literals.EOPERATION__EEXCEPTIONS) {
				// workaround 394286
				targetList = temp;
			} else if (temp instanceof InternalEList<?>) {
				// EMF ignores the "resolve" flag for containment lists...
				targetList = ((InternalEList<Object>)temp).basicList();
			} else {
				targetList = temp;
			}
		} else {
			targetList = ImmutableList.of();
		}

		Iterable<Object> ignoredElements = Iterables.concat(computeIgnoredElements(targetList, diff),
				Collections.singleton(value));
		// We know we'll have to iterate quite a number of times on this one.
		ignoredElements = Lists.newArrayList(ignoredElements);

		return DiffUtil.findInsertionIndex(comparison, ignoredElements, sourceList, targetList, value);
	}

	/**
	 * Retrieves the "source" list of the given {@code diff}. This will be different according to the kind of
	 * change and the direction of the merging.
	 * 
	 * @param diff
	 *            The diff for which merging we need a 'source'.
	 * @param feature
	 *            The feature on which the merging is actually taking place.
	 * @param rightToLeft
	 *            Direction of the merging. {@code true} if the merge is to be done on the left side, making
	 *            'source' the right side, {@code false} otherwise.
	 * @return The list that should be used as a source for this merge. May be empty, but never
	 *         <code>null</code>.
	 */
	@SuppressWarnings("unchecked")
	private static List<Object> getSourceList(Diff diff, EStructuralFeature feature, boolean rightToLeft) {
		final Match match = diff.getMatch();
		final List<Object> sourceList;
		final EObject expectedContainer;

		if (diff.getKind() == DifferenceKind.MOVE) {
			final boolean undoingLeft = rightToLeft && diff.getSource() == DifferenceSource.LEFT;
			final boolean undoingRight = !rightToLeft && diff.getSource() == DifferenceSource.RIGHT;

			if ((undoingLeft || undoingRight) && match.getOrigin() != null) {
				expectedContainer = match.getOrigin();
			} else if (rightToLeft) {
				expectedContainer = match.getRight();
			} else {
				expectedContainer = match.getLeft();
			}

		} else {
			if (match.getOrigin() != null && diff.getKind() == DifferenceKind.DELETE) {
				expectedContainer = match.getOrigin();
			} else if (rightToLeft) {
				expectedContainer = match.getRight();
			} else {
				expectedContainer = match.getLeft();
			}
		}

		if (expectedContainer != null) {
			final List<Object> temp = (List<Object>)ReferenceUtil.safeEGet(expectedContainer, feature);
			if (feature == EcorePackage.Literals.ECLASS__ESUPER_TYPES
					|| feature == EcorePackage.Literals.EOPERATION__EEXCEPTIONS) {
				// workaround 394286
				sourceList = temp;
			} else if (temp instanceof InternalEList<?>) {
				// EMF ignores the "resolve" flag for containment lists...
				sourceList = ((InternalEList<Object>)temp).basicList();
			} else {
				sourceList = temp;
			}
		} else {
			sourceList = ImmutableList.of();
		}

		return sourceList;
	}

	/**
	 * Checks whether the given {@code sequence} contains the given {@code element} according to the semantics
	 * of the given {@code equalityHelper}.
	 * 
	 * @param comparison
	 *            This will be used in order to retrieve the Match for EObjects when comparing them.
	 * @param equalityHelper
	 *            The {@link EqualityHelper} gives us the necessary semantics for Object matching.
	 * @param sequence
	 *            The sequence which elements we need to compare with {@code element}.
	 * @param element
	 *            The element we are seeking in {@code sequence}.
	 * @param <E>
	 *            Type of the sequence content.
	 * @return {@code true} if the given {@code sequence} contains an element matching {@code element},
	 *         {@code false} otherwise.
	 * @see EqualityHelper#matchingValues(Comparison, Object, Object)
	 */
	private static <E> boolean contains(Comparison comparison, IEqualityHelper equalityHelper,
			Iterable<E> sequence, E element) {
		final Iterator<E> iterator = sequence.iterator();
		while (iterator.hasNext()) {
			E candidate = iterator.next();
			if (equalityHelper.matchingValues(candidate, element)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * When computing the insertion index of an element in a list, we need to ignore all elements present in
	 * that list that feature unresolved Diffs on the same feature.
	 * 
	 * @param candidates
	 *            The sequence in which we need to compute an insertion index.
	 * @param diff
	 *            The diff we are computing an insertion index for.
	 * @param <E>
	 *            Type of the list's content.
	 * @return The list of elements that should be ignored when computing the insertion index for a new
	 *         element in {@code candidates}.
	 */
	private static <E> Iterable<E> computeIgnoredElements(Iterable<E> candidates, final Diff diff) {
		final Match match = diff.getMatch();
		final Iterable<? extends Diff> filteredCandidates = Lists.newArrayList(match.getDifferences());
		final EStructuralFeature feature;
		if (diff instanceof AttributeChange) {
			feature = ((AttributeChange)diff).getAttribute();
		} else if (diff instanceof ReferenceChange) {
			feature = ((ReferenceChange)diff).getReference();
		} else {
			return Collections.emptyList();
		}

		final Set<E> ignored = Sets.newLinkedHashSet();
		for (E candidate : candidates) {
			if (candidate instanceof EObject) {
				final Iterable<? extends Diff> differences = match.getComparison().getDifferences(
						(EObject)candidate);
				if (Iterables.any(differences, new UnresolvedDiffMatching(feature, candidate))) {
					ignored.add(candidate);
				}
			} else {
				if (Iterables.any(filteredCandidates, new UnresolvedDiffMatching(feature, candidate))) {
					ignored.add(candidate);
				}
			}
		}
		return ignored;
	}

	/**
	 * When merging a {@link Diff}, returns the sub diffs of this given diff, and all associated diffs (see
	 * {@link DiffUtil#getAssociatedDiffs(Iterable, boolean, Diff)}) of these sub diffs.
	 * <p>
	 * If the diff is an {@link AttributeChange} or a {@link ResourceAttachmentChange}, this method will
	 * return an empty iterable.
	 * </p>
	 * <p>
	 * If the diff is a {@link ReferenceChange} this method will return all differences contained in the match
	 * that contains the value of the reference change, and all associated diffs of these differences.
	 * </p>
	 * 
	 * @param leftToRight
	 *            the direction of merge.
	 * @return an iterable containing the sub diffs of this given diff, and all associated diffs of these sub
	 *         diffs.
	 * @since 3.0
	 */
	public static Function<Diff, Iterable<Diff>> getSubDiffs(final boolean leftToRight) {
		return new Function<Diff, Iterable<Diff>>() {
			public Iterable<Diff> apply(Diff diff) {
				if (diff instanceof ReferenceChange) {
					Match match = diff.getMatch();
					Match matchOfValue = diff.getMatch().getComparison().getMatch(
							((ReferenceChange)diff).getValue());
					if (!match.equals(matchOfValue) && match.getSubmatches().contains(matchOfValue)) {
						final Iterable<Diff> subDiffs = matchOfValue.getAllDifferences();
						final Iterable<Diff> associatedDiffs = getAssociatedDiffs(diff, subDiffs, leftToRight);
						return ImmutableSet.copyOf(concat(subDiffs, associatedDiffs));
					}
				}
				return ImmutableSet.of();
			}
		};
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
	 * @return an iterable containing the associated diffs of these given sub diffs, and all sub diffs of
	 *         these associated diffs.
	 * @since 3.0
	 */
	public static Iterable<Diff> getAssociatedDiffs(final Diff diffRoot, Iterable<Diff> subDiffs,
			boolean leftToRight) {
		Collection<Diff> associatedDiffs = new HashSet<Diff>();
		for (Diff diff : subDiffs) {
			final Collection<Diff> reqs = new HashSet<Diff>();
			if (leftToRight) {
				if (diff.getSource() == DifferenceSource.LEFT) {
					reqs.addAll(diff.getRequires());
				} else {
					reqs.addAll(diff.getRequiredBy());
				}
			} else {
				if (diff.getSource() == DifferenceSource.LEFT) {
					reqs.addAll(diff.getRequiredBy());
				} else {
					reqs.addAll(diff.getRequires());
				}
			}
			reqs.remove(diffRoot);
			associatedDiffs.addAll(reqs);
			for (Diff req : reqs) {
				if (!Iterables.contains(subDiffs, req)) {
					addAll(associatedDiffs, getSubDiffs(leftToRight).apply(req));
				}
			}
		}
		return associatedDiffs;
	}

	/**
	 * This can be used to check whether a given Diff affects a value for which we can find another,
	 * unresolved Diff on a given Feature.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private static class UnresolvedDiffMatching implements Predicate<Diff> {
		/** Feature on which we expect an unresolved diff. */
		private final EStructuralFeature feature;

		/** Element for which we expect an unresolved diff. */
		private final Object element;

		/**
		 * Constructs a predicate that can be used to retrieve all unresolved diffs that apply to the given
		 * {@code feature} and {@code element}.
		 * 
		 * @param feature
		 *            The feature which our diffs must concern.
		 * @param element
		 *            The element which must be our diffs' target.
		 */
		public UnresolvedDiffMatching(EStructuralFeature feature, Object element) {
			this.feature = feature;
			this.element = element;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.google.common.base.Predicate#apply(java.lang.Object)
		 */
		public boolean apply(Diff input) {
			boolean apply = false;
			if (input instanceof AttributeChange) {
				apply = input.getState() == DifferenceState.UNRESOLVED
						&& ((AttributeChange)input).getAttribute() == feature
						&& matchingValues((AttributeChange)input, element);
			} else if (input instanceof ReferenceChange) {
				apply = input.getState() == DifferenceState.UNRESOLVED
						&& ((ReferenceChange)input).getReference() == feature
						&& matchingValues((ReferenceChange)input, element);
			} else {
				apply = false;
			}
			return apply;
		}

		/**
		 * Checks that the value of the given diff matches <code>value</code>, resorting to the equality
		 * helper if needed.
		 * 
		 * @param diff
		 *            The diff which value we need to check.
		 * @param value
		 *            The expected value of <code>diff</code>
		 * @return <code>true</code> if the value matches.
		 */
		private boolean matchingValues(AttributeChange diff, Object value) {
			if (diff.getValue() == value) {
				return true;
			}
			// Only resort to the equality helper as "last resort"
			final IEqualityHelper helper = diff.getMatch().getComparison().getEqualityHelper();
			return helper.matchingAttributeValues(diff.getValue(), value);
		}

		/**
		 * Checks that the value of the given diff matches <code>value</code>, resorting to the equality
		 * helper if needed.
		 * 
		 * @param diff
		 *            The diff which value we need to check.
		 * @param value
		 *            The expected value of <code>diff</code>
		 * @return <code>true</code> if the value matches.
		 */
		private boolean matchingValues(ReferenceChange diff, Object value) {
			if (diff.getValue() == value) {
				return true;
			}
			// Only resort to the equality helper as "last resort"
			final IEqualityHelper helper = diff.getMatch().getComparison().getEqualityHelper();
			return helper.matchingValues(diff.getValue(), value);
		}
	}
}
