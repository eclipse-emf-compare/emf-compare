/*******************************************************************************
 * Copyright (c) 2014, 2017 EclipseSource Muenchen GmbH, Christian W. Damus, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *     Christian W. Damus - bug 519227
 *******************************************************************************/
package org.eclipse.emf.compare.internal;

import static com.google.common.collect.Iterables.filter;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.compare.internal.dmp.LineBasedDiff;
import org.eclipse.emf.compare.internal.dmp.diff_match_patch.Diff;
import org.eclipse.emf.compare.internal.dmp.diff_match_patch.Operation;

/**
 * Three-way differencing utility for plain text.
 * <p>
 * Taking three versions of a plain text as input (the origin, left, and right version), a three-way diff
 * provides the information on whether the left and right version underwent conflicting changes and, if not,
 * enables to compute a new merged version of the text so that all changes of the left and right version are
 * combined.
 * </p>
 * <p>
 * The differencing, conflict detection, and merging is line-based; that is, a conflict-free merge can only be
 * performed if each single line is changed only on one side (left or right), each changed line on one side
 * has not been deleted on the other side, and a total order of lines can be found (e.g., no additions of new
 * lines at the same line). Otherwise, the modifications of the left version and the right version are in
 * conflict.
 * </p>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class ThreeWayTextDiff {

	/** Specifies whether {@link #left} or {@link #right} has been unset. */
	private final boolean isLeftOrRightUnset;

	/** The computed three-way line differences. */
	private final List<ThreeWayLineDifference> threeWayDifferences;

	/** The conflict state. */
	private ConflictState conflictState = ConflictState.UNKNOWN;

	/** The new line character used in the text. */
	private String lineSeparator = "\n"; //$NON-NLS-1$

	/** The cached merged text. */
	private String merged;

	/**
	 * Constructs a {@link ThreeWayTextDiff} for the given {@code origin} version of a plain text and the two
	 * potentially modified versions of it, {@code left} and {@code right}.
	 * 
	 * @param origin
	 *            The common ancestor version of the plain text.
	 * @param left
	 *            The potentially modified left-hand side version of the common ancestor.
	 * @param right
	 *            The potentially modified right-hand side version of the common ancestor.
	 */
	public ThreeWayTextDiff(String origin, String left, String right) {
		this.lineSeparator = determineLineSeparator(origin);
		this.isLeftOrRightUnset = origin != null && (left == null || right == null);
		this.threeWayDifferences = computeThreeWayDiffs(origin, left, right);
	}

	/**
	 * Determine the line separator used in the given {@code string}.
	 * 
	 * @param string
	 *            The string to get the line separator from.
	 * @return The line separator used in string or &quot;\n&quot; as default.
	 */
	private String determineLineSeparator(String string) {
		try (StringReader stringReader = new StringReader(nullToEmpty(string))) {
			int read = 0;
			while (read != -1) {
				read = stringReader.read();
				final char currentChar = (char)read;
				if (currentChar == '\n' || currentChar == '\r') {
					String separator = String.valueOf(currentChar);
					read = stringReader.read();
					final char nextChar = (char)read;
					if ((read != -1) && (nextChar != currentChar)
							&& ((nextChar == '\r') || (nextChar == '\n'))) {
						separator += nextChar;
					}
					return separator;
				}
			}
		} catch (IOException e) {
			// must never happen as we read strings
		}
		return "\n"; //$NON-NLS-1$
	}

	/**
	 * Computes the three-way line differences among {@code left} and {@code right} and their common ancestor
	 * {@code origin}.
	 * 
	 * @param origin
	 *            The common ancestor version.
	 * @param left
	 *            The left-hand side version.
	 * @param right
	 *            The right-hand side version.
	 * @return The computed three-way line differences.
	 */
	private List<ThreeWayLineDifference> computeThreeWayDiffs(String origin, String left, String right) {
		final TwoWayTextDiff leftDiffs = new TwoWayTextDiff(origin, left);
		final TwoWayTextDiff rightDiffs = new TwoWayTextDiff(origin, right);
		final LinkedList<ThreeWayLineDifference> threeWayDiffs = new LinkedList<ThreeWayLineDifference>();
		final LinkedList<Diff> leftDiffQueue = new LinkedList<Diff>(leftDiffs.getDifferences());
		final LinkedList<Diff> rightDiffQueue = new LinkedList<Diff>(rightDiffs.getDifferences());
		try (BufferedReader originReader = createBufferedReader(nullToEmpty(origin))) {
			String originLine;
			while ((originLine = originReader.readLine()) != null) {
				final List<Diff> leftRange = collectDifferenceRange(originLine, leftDiffQueue);
				final List<Diff> rightRange = collectDifferenceRange(originLine, rightDiffQueue);
				threeWayDiffs.add(new ThreeWayLineDifference(leftRange, rightRange));
			}

			// add all trailing differences (i.e., additions to the end of the origin)
			if (!leftDiffQueue.isEmpty() || !rightDiffQueue.isEmpty()) {
				threeWayDiffs.add(new ThreeWayLineDifference(leftDiffQueue, rightDiffQueue));
			}
		} catch (IOException e) {
			// must never happen as we read strings
		}
		return threeWayDiffs;
	}

	/**
	 * Collects and returns the differences from {@code diffQueue} that belong to the difference range
	 * according to the given {@code originLine}.
	 * <p>
	 * The difference range encompasses all potentially existing inserts <em>before</em> the equal or deletion
	 * of the origin line, the equal or deletion of the origin line itself, as well as the inserts after the
	 * deletion of the origin line (i.e., updates).
	 * </p>
	 * 
	 * @param originLine
	 *            The origin line.
	 * @param diffQueue
	 *            The queue of differences to collect from.
	 * @return The collected list of differences.
	 */
	private List<Diff> collectDifferenceRange(String originLine, LinkedList<Diff> diffQueue) {
		final List<Diff> diffs = new LinkedList<Diff>();

		Diff currentDiff = diffQueue.peek();
		boolean hitEqualsOriginLine = false;
		boolean hitDeleteOriginLine = false;

		// collect differences until we hit equals or deletion of origin line
		while (currentDiff != null && !hitEqualsOriginLine && !hitDeleteOriginLine) {
			hitEqualsOriginLine = isEqualOfLine(currentDiff, originLine);
			hitDeleteOriginLine = isDeleteOfLine(currentDiff, originLine);
			diffs.add(diffQueue.poll());
			currentDiff = diffQueue.peek();
		}

		// if there was a deletion, collect following inserts to complete the update
		// (i.e., delete with subsequent insert diffs)
		while (hitDeleteOriginLine && currentDiff != null && isInsert(currentDiff)) {
			diffs.add(diffQueue.poll());
			currentDiff = diffQueue.peek();
		}

		return diffs;
	}

	/**
	 * Specifies whether {@code diff} is an equal of the given {@code line}.
	 * 
	 * @param diff
	 *            The difference to check.
	 * @param line
	 *            The line to check for equality.
	 * @return <code>true</code> if it is an equal of the given line, <code>false</code> otherwise.
	 */
	private boolean isEqualOfLine(Diff diff, String line) {
		return diff != null && isEqual(diff) && line.equals(diff.text);
	}

	/**
	 * Specifies whether {@code diff} is a deletion of the given {@code line}.
	 * 
	 * @param diff
	 *            The difference to check.
	 * @param line
	 *            The line to check for equality.
	 * @return <code>true</code> if it is a deletion of the given line, <code>false</code> otherwise.
	 */
	private boolean isDeleteOfLine(Diff diff, String line) {
		return diff != null && isDelete(diff) && line.equals(diff.text);
	}

	/**
	 * Specifies whether {@code diff} is an equal (unchanged line).
	 * 
	 * @param diff
	 *            The difference to check.
	 * @return <code>true</code> if it is an equal, <code>false</code> otherwise.
	 */
	private boolean isEqual(Diff diff) {
		return diff != null && Operation.EQUAL.equals(diff.operation);
	}

	/**
	 * Specifies whether {@code diff} is a deletion.
	 * 
	 * @param diff
	 *            The difference to check.
	 * @return <code>true</code> if it is a deletion, <code>false</code> otherwise.
	 */
	private boolean isDelete(Diff diff) {
		return diff != null && Operation.DELETE.equals(diff.operation);
	}

	/**
	 * Specifies whether {@code diff} is a insertion.
	 * 
	 * @param diff
	 *            The difference to check.
	 * @return <code>true</code> if it is a insertion, <code>false</code> otherwise.
	 */
	private boolean isInsert(Diff diff) {
		return diff != null && Operation.INSERT.equals(diff.operation);
	}

	/**
	 * Specifies whether the modification on the left-hand side and the modifications on the right-hand side
	 * are conflicting.
	 * <p>
	 * A conflict occurs if a line is changed on both sides (left and right), a line is changed on one side
	 * and is deleted on the other side, or no total order of lines can be found (e.g., two additions of new
	 * lines at the same original line).
	 * </p>
	 * 
	 * @return <code>true</code> if left and right is in conflict; <code>false</code> otherwise.
	 */
	public boolean isConflicting() {
		if (ConflictState.UNKNOWN.equals(conflictState)) {
			conflictState = computeConflictState();
		}
		return ConflictState.CONFLICTING.equals(conflictState);
	}

	/**
	 * Computes the conflict state based on the conflict state of all {@link #threeWayDifferences}.
	 * 
	 * @return The computed conflict state.
	 */
	private ConflictState computeConflictState() {
		for (ThreeWayLineDifference threeWayDiff : threeWayDifferences) {
			if (threeWayDiff.isConflicting()) {
				return ConflictState.CONFLICTING;
			}
		}
		return ConflictState.NOT_CONFLICTING;
	}

	/**
	 * Returns the merge result of the modifications applied at the left-hand side and the right-hand side.
	 * <p>
	 * If the left-hand side modifications and the right-hand side modifications are not conflicting, the
	 * merge operation is symmetric; that is, the merge result will always yield the same result, also when
	 * switching the left-hand side and the right-hand side. If the left-hand and right-hand side
	 * modifications are conflicting, this method will still return a merged version. However, in this case,
	 * the merge operation might be not be symmetric; that is, switching left and right might yield different
	 * merge results.
	 * </p>
	 * 
	 * @return The result of merging the left-hand side and right-hand side differences.
	 */
	public String getMerged() {
		if (merged == null) {
			merged = computeMerged();
		}
		return merged;
	}

	/**
	 * Computes the merge result based on the {@link #threeWayDifferences}.
	 * 
	 * @return The result of merging all {@link #threeWayDifferences}.
	 */
	private String computeMerged() {
		final StringBuilder mergeBuilder = new StringBuilder();
		for (ThreeWayLineDifference threeWayLineDifference : threeWayDifferences) {
			mergeBuilder.append(threeWayLineDifference.getMerged());
		}
		return stripTrailingNewLine(nullIfUnset(mergeBuilder.toString()));
	}

	/**
	 * Removes the trailing new line character of the given {@code string}.
	 * 
	 * @param string
	 *            The string to remove the trailing new line character from.
	 * @return The string without trailing new line character.
	 */
	private String stripTrailingNewLine(String string) {
		if (string != null && string.endsWith(lineSeparator)) {
			return string.substring(0, string.length() - lineSeparator.length());
		}
		return string;
	}

	/**
	 * Creates a {@link BufferedReader} over the given {@code string}.
	 * 
	 * @param string
	 *            The string to create the reader for.
	 * @return The created reader.
	 */
	private BufferedReader createBufferedReader(String string) {
		final ByteArrayInputStream inputStream = new ByteArrayInputStream(string.getBytes());
		return new BufferedReader(new InputStreamReader(inputStream));
	}

	/**
	 * If {@link #left} or {@link #right} is {@link #isLeftOrRightUnset() unset} and the given
	 * {@code mergeResult} is empty (i.e., the unset constitutes the merge result), this method returns
	 * <code>null</code> to indicate an unset. Otherwise, this method returns {@code mergeResult}.
	 * 
	 * @param mergeResult
	 *            The merge result to check for unset.
	 * @return <code>null</code> if the merge result is an unset; otherwise {@code mergeResult}.
	 */
	private String nullIfUnset(final String mergeResult) {
		if (mergeResult.length() < 1 && isLeftOrRightUnset) {
			return null;
		} else {
			return mergeResult;
		}
	}

	/**
	 * Returns an empty string if the given {@code text} is <code>null</code>, otherwise it returns
	 * {@code text}.
	 * 
	 * @param text
	 *            The string to get safely.
	 * @return An empty string if {@code text} is <code>null</code>, otherwise {@code text}.
	 */
	private String nullToEmpty(String text) {
		if (text == null) {
			return ""; //$NON-NLS-1$
		} else {
			return text;
		}
	}

	/**
	 * The conflict state of a three-way line difference may either be unknown, conflicting, or free of
	 * conflicts.
	 * 
	 * @author Philip Langer <planger@eclipsesource.com>
	 */
	private enum ConflictState {
		/** The conflict state is unknown yet. */
		UNKNOWN,
		/** The conflict state is conflicting. */
		CONFLICTING,
		/** The conflict state is free of conflicts. */
		NOT_CONFLICTING;
	}

	/**
	 * Two-way line differences (delete, insert, or equal) between an origin version and a revised version.
	 * 
	 * @author Philip Langer <planger@eclipsesource.com>
	 */
	private class TwoWayTextDiff {

		/** The {@link LineBasedDiff} instance. */
		private final LineBasedDiff lbDiff = new LineBasedDiff();

		/** The differences between {@link #origin} and {@link #revised}. */
		private final LinkedList<Diff> diffs;

		/**
		 * Creates the line differences for the given {@code origin} version of a text and its {@code revised}
		 * version.
		 * 
		 * @param origin
		 *            The origin version of the plain text.
		 * @param revised
		 *            The revised version of the plain text.
		 */
		TwoWayTextDiff(String origin, String revised) {
			final String safeOrigin = nullToEmpty(origin);
			final String safeRevised = nullToEmpty(revised);
			final LinkedList<Diff> differences = lbDiff.computeLineBasedDiff(safeOrigin, safeRevised);
			this.diffs = flattenDifferences(differences);
		}

		/**
		 * Flattens the given {@code differences}.
		 * <p>
		 * A flattened difference contains an entry for each equal, added, or deleted line. Thus, also if an
		 * addition or deletion spans across multiple lines, the flattened differences contain one addition or
		 * deletion for each line within the added or deleted block of lines.
		 * </p>
		 * 
		 * @param differences
		 *            The differences to be flattened.
		 * @return The flattened differences.
		 */
		private LinkedList<Diff> flattenDifferences(LinkedList<Diff> differences) {
			LinkedList<Diff> flattenedDifferences = new LinkedList<Diff>();
			for (Diff diff : differences) {
				String line;
				try (BufferedReader reader = createBufferedReader(diff.text)) {
					while ((line = reader.readLine()) != null) {
						flattenedDifferences.add(new Diff(diff.operation, line));
					}
				} catch (IOException e) {
					// this must never happen as we are reading strings
				}
			}
			return flattenedDifferences;
		}

		/**
		 * Returns the line-based differences between an origin version and a revised version.
		 * 
		 * @return The line-based differences.
		 */
		public List<Diff> getDifferences() {
			return Collections.unmodifiableList(diffs);
		}
	}

	/**
	 * A single three-way line difference.
	 * <p>
	 * A three-way line difference describes the modifications in a left-hand side and right-hand side version
	 * of a single line in the origin.
	 * </p>
	 * 
	 * @author Philip Langer <planger@eclipsesource.com>
	 */
	private class ThreeWayLineDifference {

		/** The differences of the left-hand side. */
		private final LinkedList<Diff> leftDiffs = new LinkedList<Diff>();

		/** The differences of the left-hand side. */
		private final LinkedList<Diff> rightDiffs = new LinkedList<Diff>();

		/**
		 * Creates a new three-way line difference with the given {@code leftDifferences} and
		 * {@code rightDifferences}.
		 * 
		 * @param leftDifferences
		 *            The left-hand side differences related to a common origin line.
		 * @param rightDifferences
		 *            The right-hand side differences related to a common origin line.
		 */
		ThreeWayLineDifference(List<Diff> leftDifferences, List<Diff> rightDifferences) {
			leftDiffs.addAll(leftDifferences);
			rightDiffs.addAll(rightDifferences);
		}

		/**
		 * Specifies whether the left-hand side and right-hand side modifications of this difference are in
		 * conflict with each other.
		 * 
		 * @return <code>true</code> if the modifications are conflicting; <code>false</code> otherwise.
		 */
		public boolean isConflicting() {
			return isInsertInsertConflict() || isDeleteUpdateConflict() || isUpdateUpdateConflict();
		}

		/**
		 * Specifies whether the left-hand side and right-hand side modifications of this difference both
		 * update the same origin line to a different new value (i.e., update-update conflict).
		 * 
		 * @return <code>true</code> if the modifications cause an update-update conflict, <code>false</code>
		 *         otherwise.
		 */
		private boolean isUpdateUpdateConflict() {
			final List<Diff> leftInsertsAfterDelete = getAllInsertsAfterDelete(leftDiffs);
			final List<Diff> rightsInsertsAfterDelete = getAllInsertsAfterDelete(rightDiffs);
			return containDifferentInserts(leftInsertsAfterDelete, rightsInsertsAfterDelete);
		}

		/**
		 * Specifies whether the left-hand side and right-hand side modifications of this difference both add
		 * different lines to the same location so that no total ordering for the concurrently added lines can
		 * be found (i.e., insert-insert conflict).
		 * 
		 * @return <code>true</code> if the modifications cause an insert-insert conflict, <code>false</code>
		 *         otherwise.
		 */
		private boolean isInsertInsertConflict() {
			final List<Diff> leftInsertsBeforeOriginLine = getAllInsertsBeforeEqualsOrDelete(leftDiffs);
			final List<Diff> rightInsertsBeforeOriginLine = getAllInsertsBeforeEqualsOrDelete(rightDiffs);
			return containDifferentInserts(leftInsertsBeforeOriginLine, rightInsertsBeforeOriginLine);
		}

		/**
		 * Returns a list of all insert differences of the given list of {@code diffs} <em>before</em> the
		 * equal or delete difference of the origin line.
		 * 
		 * @param diffs
		 *            The list to get the inserts from.
		 * @return The list of inserts before the origin line.
		 */
		private List<Diff> getAllInsertsBeforeEqualsOrDelete(LinkedList<Diff> diffs) {
			final List<Diff> inserts = new LinkedList<Diff>();
			for (Diff diff : diffs) {
				if (isEqual(diff) || isDelete(diff)) {
					break;
				} else if (isInsert(diff)) {
					inserts.add(diff);
				}
			}
			return inserts;
		}

		/**
		 * Returns a list of all insert differences of the given list of {@code diffs} <em>before</em> the
		 * equal or delete difference of the origin line.
		 * 
		 * @param diffs
		 *            The list to get the inserts from.
		 * @return The list of inserts before the origin line.
		 */
		private List<Diff> getAllInsertsAfterDelete(LinkedList<Diff> diffs) {
			final List<Diff> inserts = new LinkedList<Diff>();
			boolean hitDeleteOrigin = false;
			for (Diff diff : diffs) {
				if (hitDeleteOrigin && isInsert(diff)) {
					inserts.add(diff);
				}
				if (isDelete(diff)) {
					hitDeleteOrigin = true;
				}
			}
			return inserts;
		}

		/**
		 * Specifies whether the given {@code leftInserts} contain different inserts as the given
		 * {@code rightInserts} with respect to the inserted line. If they are equal but one side contains
		 * more inserts, this method will still return <code>false</code>.
		 * 
		 * @param leftInserts
		 *            The left insert differences.
		 * @param rightInserts
		 *            The right insert differences.
		 * @return <code>true</code> if one side inserts different lines than the other side;
		 *         <code>false</code> otherwise.
		 */
		private boolean containDifferentInserts(final List<Diff> leftInserts, final List<Diff> rightInserts) {
			final Iterator<Diff> leftDiffIterator = leftInserts.iterator();
			final Iterator<Diff> rightDiffIterator = rightInserts.iterator();
			while (leftDiffIterator.hasNext() && rightDiffIterator.hasNext()) {
				final Diff leftDiff = leftDiffIterator.next();
				final Diff rightDiff = rightDiffIterator.next();
				if (!leftDiff.text.equals(rightDiff.text)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Specifies whether the one side modifies the origin line of this difference, whereas the other side
		 * deletes the same origin line (i.e., delete-update conflict).
		 * 
		 * @return <code>true</code> if the modifications cause an delete-update conflict, <code>false</code>
		 *         otherwise.
		 */
		private boolean isDeleteUpdateConflict() {
			if ((leftDiffs.size() > 1 || rightDiffs.size() > 1)
					&& (isDelete(leftDiffs.peek()) && isDelete(rightDiffs.peek()))) {
				final Optional<Diff> leftInsert = getFirstInsert(leftDiffs);
				final Optional<Diff> rightInsert = getFirstInsert(rightDiffs);
				return (leftInsert.isPresent() && !rightInsert.isPresent())
						|| (!leftInsert.isPresent() && rightInsert.isPresent());
			}
			return false;
		}

		/**
		 * Returns the {@link Optional optionally} available first insert diff within the given lift of
		 * {@code diffs}.
		 * 
		 * @param diffs
		 *            The differences to get the first insert diff from.
		 * @return An {@link Optional} diff that is the first insert within {@code diff};
		 *         {@link Optional#absent()} if not available.
		 */
		private Optional<Diff> getFirstInsert(LinkedList<Diff> diffs) {
			for (Diff diff : diffs) {
				if (isInsert(diff)) {
					return Optional.of(diff);
				}
			}
			return Optional.absent();
		}

		/**
		 * Builds and returns the merge result of this three-way line difference.
		 * 
		 * @return The merge result of this three-way line difference.
		 */
		public String getMerged() {
			final StringBuilder stringBuilder = new StringBuilder();
			for (Diff diff : mergeDifferences()) {
				stringBuilder.append(diff.text + lineSeparator);
			}
			return stringBuilder.toString();
		}

		/**
		 * Merges {@link #leftDiffs} and {@link #rightDiffs} and returns the list of merged differences.
		 * <p>
		 * Merging the differences includes finding the correct order of differences, as well as filtering
		 * equivalent or overridden differences.
		 * </p>
		 * 
		 * @return The merged list of differences.
		 */
		private Iterable<Diff> mergeDifferences() {
			final LinkedList<Diff> primaryDiffs;
			final LinkedList<Diff> secondaryDiffs;

			if (isRightPrimaryMergeSide()) {
				primaryDiffs = rightDiffs;
				secondaryDiffs = leftDiffs;
			} else {
				primaryDiffs = leftDiffs;
				secondaryDiffs = rightDiffs;
			}

			final Iterable<Diff> filteredPrimaryDiffs = filter(primaryDiffs,
					isMergedAsPrimaryDiff(secondaryDiffs));
			final Iterable<Diff> filteredSecondaryDiffs = filter(secondaryDiffs,
					isMergedAsSecondaryDiff(primaryDiffs));

			return Iterables.concat(filteredPrimaryDiffs, filteredSecondaryDiffs);
		}

		/**
		 * Specifies whether the right-hand side is the primary merge side.
		 * <p>
		 * The primary merge side is the one with the higher number of insertions before the origin line. We
		 * need to merge this side first, to ensure that all insertions are applied before the origin line is
		 * printed.
		 * </p>
		 * 
		 * @return <code>true</code> if the right-hand side is the primary merge side; <code>false</code>
		 *         otherwise.
		 */
		private boolean isRightPrimaryMergeSide() {
			final int noOfRightInsertions = getAllInsertsBeforeEqualsOrDelete(rightDiffs).size();
			final int noOfLeftInsertaions = getAllInsertsBeforeEqualsOrDelete(leftDiffs).size();
			return noOfRightInsertions > noOfLeftInsertaions;
		}

		/**
		 * Returns a predicate that specifies whether a difference is to be merged as secondary difference
		 * with respected to the given {@code primaryDiffs}.
		 * <p>
		 * A secondary difference has to be merged only if it is not a delete and if the primary differences
		 * do not include the same difference or a difference that overrides it (i.e., an equals is overridden
		 * by a delete and subsequent insert).
		 * </p>
		 * 
		 * @param primaryDiffs
		 *            The primary differences, which will be merged before the secondary differences.
		 * @return <code>true</code> if it is to be merged, <code>false</code> if it has to be filtered.
		 */
		private Predicate<Diff> isMergedAsSecondaryDiff(final LinkedList<Diff> primaryDiffs) {
			return new Predicate<Diff>() {
				public boolean apply(Diff input) {
					return !isDelete(input) && !containsEqualOrOverridingDiff(primaryDiffs, input);
				}
			};
		}

		/**
		 * Returns a predicate that specifies whether a difference is to be merged as primary difference with
		 * respected to the given {@code secondaryDiffs}.
		 * <p>
		 * A primary difference has to be merged only if it is not a delete and if the secondary differences
		 * do not include a difference that overrides it (i.e., an equals is overridden by a delete and
		 * subsequent insert).
		 * </p>
		 * 
		 * @param secondaryDiffs
		 *            The secondary differences, which will be merged after the primary differences.
		 * @return <code>true</code> if it is to be merged, <code>false</code> if it has to be filtered.
		 */
		private Predicate<Diff> isMergedAsPrimaryDiff(final LinkedList<Diff> secondaryDiffs) {
			return new Predicate<Diff>() {
				public boolean apply(Diff input) {
					return !isDelete(input) && !containsOverridingDiff(secondaryDiffs, input);
				}
			};
		}

		/**
		 * Specifies whether {@code diffs} contains a difference that is overriding {@code diff}.
		 * 
		 * @param diffs
		 *            The differences to search for overriding differences in.
		 * @param diff
		 *            The difference to check whether it is being overridden in {@code diffs}.
		 * @return <code>true</code> if {@code diffs} contain a difference that is equal to or overriding
		 *         {@code diff}, <code>false</code> otherwise.
		 */
		private boolean containsOverridingDiff(Iterable<Diff> diffs, Diff diff) {
			for (Diff currentDiff : diffs) {
				if (isOverridingDiff(diff, currentDiff)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Specifies whether {@code diffs} contains a difference that is equal to or overriding {@code diff}.
		 * 
		 * @param diffs
		 *            The differences to search for equal or overriding differences in.
		 * @param diff
		 *            The difference to check against equality or whether it is being overridden in
		 *            {@code diffs}.
		 * @return <code>true</code> if {@code diffs} contain a difference that is equal to or overriding
		 *         {@code diff}, <code>false</code> otherwise.
		 */
		private boolean containsEqualOrOverridingDiff(Iterable<Diff> diffs, Diff diff) {
			for (Diff currentDiff : diffs) {
				if (currentDiff.equals(diff) || isOverridingDiff(diff, currentDiff)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Specifies whether {@code diff} is overridden by {@code overridingDiff}.
		 * <p>
		 * A difference is overridden if it is an equal and the overriding diff is a delete of the same line.
		 * </p>
		 * 
		 * @param diff
		 *            The diff to check whether it is overridden by {@code overridingDiff}.
		 * @param overridingDiff
		 *            The diff to check whether it is overriding {@code diff}.
		 * @return <code>true</code> if {@code diff} is overridden by {@code overridingDiff},
		 *         <code>false</code> otherwise.
		 */
		private boolean isOverridingDiff(Diff diff, Diff overridingDiff) {
			return isEqual(diff) && isDelete(overridingDiff) && overridingDiff.text.equals(diff.text);
		}
	}
}
