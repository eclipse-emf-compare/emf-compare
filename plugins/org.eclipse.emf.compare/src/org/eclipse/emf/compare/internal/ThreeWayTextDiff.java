/*******************************************************************************
 * Copyright (c) 2014 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.compare.internal.dmp.LineBasedDiff;
import org.eclipse.emf.compare.internal.dmp.diff_match_patch.Diff;
import org.eclipse.emf.compare.internal.dmp.diff_match_patch.Operation;
import org.eclipse.emf.compare.internal.dmp.diff_match_patch.Patch;

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

	/** Constant for new line String. */
	private static final String NL = "\n"; //$NON-NLS-1$

	/** The line operations of the left-hand side. */
	private final LineOperations leftOperations;

	/** The line operations of the right-hand side. */
	private final LineOperations rightOperations;

	/**
	 * Constructs a {@link ThreeWayTextDiff} for the given {@code origin} of a plain text and the two
	 * potentially modified versions of it, {@code left} and {@code right}.
	 * 
	 * @param origin
	 *            The original version of the plain text.
	 * @param left
	 *            The potentially modified left version of the origin.
	 * @param right
	 *            The potentially modified right version of the origin.
	 */
	public ThreeWayTextDiff(String origin, String left, String right) {
		this.leftOperations = new LineOperations(origin, left);
		this.rightOperations = new LineOperations(origin, right);
	}

	/**
	 * Returns the original version.
	 * 
	 * @return The original version.
	 */
	public String getOrigin() {
		return leftOperations.getOrigin();
	}

	/**
	 * Returns the potentially modified left-hand side version of the origin.
	 * 
	 * @return The left version.
	 */
	public String getLeft() {
		return leftOperations.getRevised();
	}

	/**
	 * Returns the potentially modified right-hand side version of the origin.
	 * 
	 * @return The right version.
	 */
	public String getRight() {
		return rightOperations.getRevised();
	}

	/**
	 * Specifies whether the modification on the left-hand side and the modifications on the right-hand side
	 * are conflicting.
	 * <p>
	 * A conflict occurs if a line is changed on both sides (left and right), a line is changed on one side
	 * and is deleted on the other side, or no total order of lines can be found (e.g., two additions of new
	 * lines at the same original line).
	 * </p>
	 * <p>
	 * When considering each change as a deletion and addition of a line, the conflict detection boils down to
	 * finding additions on both sides that are inserted before or after the same line.
	 * </p>
	 * 
	 * @return <code>true</code> if left and right is in conflict; <code>false</code> otherwise.
	 */
	public boolean isConflicting() {
		boolean isConflicting = false;
		final int numberOfLinesInOrigin = getNumberOfLinesInOrigin();
		for (int currentIndex = 0; currentIndex < numberOfLinesInOrigin; currentIndex++) {
			if (isConflicting(currentIndex)) {
				isConflicting = true;
				break;
			}
		}
		return isConflicting;
	}

	/**
	 * Returns the number of lines of the {@link #origin original text}.
	 * 
	 * @return The number of lines of origin.
	 */
	private int getNumberOfLinesInOrigin() {
		final String[] originLines = nullToEmpty(getOrigin()).split(NL);
		return originLines.length;
	}

	/**
	 * Specifies whether there are conflicting changes in {@link #left} or {@link #right} at the
	 * {@code lineIndex} denoting the line of the {@link #origin}.
	 * 
	 * @param lineIndex
	 *            The line index of {@link #origin}.
	 * @return <code>true</code> if there are conflicting changes, <code>false</code> otherwise.
	 */
	private boolean isConflicting(int lineIndex) {
		return isInsertInsertAt(lineIndex) || isInsertDeleteAt(lineIndex) || isDeleteInsertAt(lineIndex);
	}

	/**
	 * Specifies whether there is an insert-insert conflict at the {@code lineIndex} denoting the line of the
	 * {@link #origin}.
	 * 
	 * @param lineIndex
	 *            The line index of {@link #origin}.
	 * @return <code>true</code> if there is an insert-insert conflict, <code>false</code> otherwise.
	 */
	private boolean isInsertInsertAt(int lineIndex) {
		return leftOperations.isInsertAtOriginLineIndex(lineIndex)
				&& rightOperations.isInsertAtOriginLineIndex(lineIndex);
	}

	/**
	 * Specifies whether there is an insert-delete conflict (left-hand side insert and right-hand side delete)
	 * at the {@code lineIndex} denoting the line of the {@link #origin}.
	 * 
	 * @param lineIndex
	 *            The line index of {@link #origin}.
	 * @return <code>true</code> if there is an insert-delete conflict, <code>false</code> otherwise.
	 */
	private boolean isInsertDeleteAt(int lineIndex) {
		return leftOperations.isChangedAtOriginLineIndex(lineIndex)
				&& rightOperations.isDeleteAtOriginLineIndex(lineIndex);
	}

	/**
	 * Specifies whether there is an delete-insert conflict (left-hand side delete and right-hand side insert)
	 * at the {@code lineIndex} denoting the line of the {@link #origin}.
	 * 
	 * @param lineIndex
	 *            The line index of {@link #origin}.
	 * @return <code>true</code> if there is an delete-insert conflict, <code>false</code> otherwise.
	 */
	private boolean isDeleteInsertAt(int lineIndex) {
		return leftOperations.isDeleteAtOriginLineIndex(lineIndex)
				&& rightOperations.isChangedAtOriginLineIndex(lineIndex);
	}

	/**
	 * Performs and returns a merge of the modifications of the left-hand side and the right-hand side.
	 * <p>
	 * If the left-hand side diff and the right-hand side diff is not conflicting, the merge operation is
	 * symmetric; that is, the merge result will always yield the same result, also when switching the
	 * left-hand side and the right-hand side. If the left-hand and right-hand side diffs are conflicting,
	 * this method will still return a merged version, taking the left-hand side and applying the patches of
	 * the right-hand side. However, in this case, the merge operation might be not be symmetric; that is,
	 * switching left and right might yield different merge results.
	 * </p>
	 * 
	 * @return A merged version of left and right.
	 */
	public String getMerged() {
		final String patchedLeft = rightOperations.apply(getLeft());
		final String merged = nullIfUnset(patchedLeft);
		return merged;
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
		if (mergeResult.length() < 1 && isLeftOrRightUnset()) {
			return null;
		} else {
			return mergeResult;
		}
	}

	/**
	 * Specifies whether {@link #left} or {@link #right} has been unset.
	 * 
	 * @return <code>true</code> if left or right has been unset, <code>false</code> otherwise.
	 */
	private boolean isLeftOrRightUnset() {
		return getOrigin() != null && (getLeft() == null || getRight() == null);
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
	 * The line operations (delete, insert, or equal) of each line of an origin version in the context of a
	 * diff.
	 * 
	 * @author Philip Langer <planger@eclipsesource.com>
	 */
	private class LineOperations {

		/** The {@link LineBasedDiff} instance. */
		private final LineBasedDiff lbDiff = new LineBasedDiff();

		/** The origin version of the text. */
		private String origin;

		/** The revised version of the text. */
		private String revised;

		/** The diffs described in this line operations. */
		private LinkedList<Diff> diffs;

		/** The list of operations. */
		private List<Operation> operations = new ArrayList<Operation>();

		/**
		 * Creates the line operations for the given {@code origin} version of a text and its {@code revised}
		 * version.
		 * 
		 * @param origin
		 *            The origin version of the plain text.
		 * @param revised
		 *            The revised version of the plain text.
		 */
		public LineOperations(String origin, String revised) {
			super();
			this.origin = origin;
			this.revised = revised;
			this.diffs = computeLineBasedDiff(origin, revised);
			initialize();
		}

		/**
		 * Returns the original version of the text.
		 * 
		 * @return The original version.
		 */
		public String getOrigin() {
			return origin;
		}

		/**
		 * Returns the revised version of the text.
		 * 
		 * @return The revised version.
		 */
		public String getRevised() {
			return revised;
		}

		/**
		 * Initializes the list of operations.
		 */
		private void initialize() {
			for (Diff diff : diffs) {
				addLineOperations(diff);
			}
		}

		/**
		 * Computes a line-based diff between {@code original} and {@code revision}.
		 * 
		 * @param original
		 *            The original version of the plain text.
		 * @param revision
		 *            The revised version of the plain text.
		 * @return The line-based differences between {@code origin} and {@code revision}.
		 */
		private LinkedList<Diff> computeLineBasedDiff(String original, String revision) {
			return lbDiff.computeLineBasedDiff(nullToEmpty(original), nullToEmpty(revision));
		}

		/**
		 * Adds the line operations of the given {@code diff}.
		 * 
		 * @param diff
		 *            The diff to add line operations of.
		 */
		private void addLineOperations(Diff diff) {
			final int numberOfLines = getNumberOfLines(diff);
			for (int i = 0; i < numberOfLines; i++) {
				operations.add(diff.operation);
			}
		}

		/**
		 * Returns the number of lines inserted, deleted, or left equal of the given {@code diff}.
		 * 
		 * @param diff
		 *            The diff to get the number of inserted, deleted, or equal lines for.
		 * @return The number of inserted, deleted, or equal lines for {@code diff}.
		 */
		private int getNumberOfLines(Diff diff) {
			return diff.text.split(NL).length;
		}

		/**
		 * Applies the line operations to the given {@code original} text.
		 * 
		 * @param original
		 *            The text to apply the {@code patches} to.
		 * @return The patched version of {@code origin}.
		 */
		public String apply(String original) {
			final LinkedList<Patch> patches = computePatches();
			return (String)lbDiff.patch_apply(patches, nullToEmpty(original))[0];
		}

		/**
		 * Computes and returns patches for this line operations.
		 * 
		 * @return The computed patches.
		 */
		private LinkedList<Patch> computePatches() {
			return lbDiff.patch_make(diffs);
		}

		/**
		 * Specifies whether the line operations contain an insertion at the given {@code originLineIndex}
		 * (i.e., the line index of the origin version).
		 * <p>
		 * Therefore, it first determines the corresponding line in the revised version and checks for an
		 * insertion.
		 * </p>
		 * 
		 * @param originLineIndex
		 *            The line index of the origin version of the text.
		 * @return <code>true</code> if there is an insertion, <code>false</code> otherwise.
		 */
		public boolean isInsertAtOriginLineIndex(int originLineIndex) {
			final int lineIndex = getRevisedLineIndexForOriginLineIndex(originLineIndex);
			return isInsertAt(lineIndex);
		}

		/**
		 * Specifies whether the line operations contain a deletion at the given {@code originLineIndex}
		 * (i.e., the line index of the origin version).
		 * <p>
		 * Therefore, it first determines the corresponding line in the revised version and checks for a
		 * deletion.
		 * </p>
		 * 
		 * @param originLineIndex
		 *            The line index of the origin version of the text.
		 * @return <code>true</code> if there is a deletion, <code>false</code> otherwise.
		 */
		public boolean isDeleteAtOriginLineIndex(int originLineIndex) {
			final int lineIndex = getRevisedLineIndexForOriginLineIndex(originLineIndex);
			return isDeleteAt(lineIndex);
		}

		/**
		 * Specifies whether the line at {@code originLineIndex} (i.e., the line index of the origin version)
		 * is changed.
		 * <p>
		 * Therefore, it first determines the corresponding line in the revised version and checks for a
		 * change. The line is changed if there is a deletion that is followed by an insertion or if there is
		 * an insertion only.
		 * </p>
		 * 
		 * @param originLineIndex
		 *            The line index of the origin version of the text.
		 * @return <code>true</code> if there is a deletion with a subsequent insertion, <code>false</code>
		 *         otherwise.
		 */
		public boolean isChangedAtOriginLineIndex(int originLineIndex) {
			final int lineIndex = getRevisedLineIndexForOriginLineIndex(originLineIndex);
			return isChangedAt(lineIndex);
		}

		/**
		 * Returns the line index of the revised version that corresponds to the given {@code originLineIndex}
		 * (i.e., the line index of the origin version).
		 * 
		 * @param originLineIndex
		 *            The line index of the origin version of the text.
		 * @return The line in the revised version that corresponds to the given line in the origin version.
		 */
		public int getRevisedLineIndexForOriginLineIndex(int originLineIndex) {
			int currentOriginLineIndex = 0;
			int lineIndex = 0;
			while (currentOriginLineIndex < originLineIndex) {
				final Operation operation = getOperation(lineIndex);
				if (!isInsert(operation)) {
					currentOriginLineIndex++;
				}
				lineIndex++;
			}
			return lineIndex;
		}

		/**
		 * Specifies whether there is an insertion at the given {@code lineIndex} (i.e., the line index of the
		 * revised version).
		 * 
		 * @param lineIndex
		 *            The line index of the revised version of the text.
		 * @return <code>true</code> if there is an insertion, <code>false</code> otherwise.
		 */
		private boolean isInsertAt(int lineIndex) {
			return hasIndex(lineIndex) && isInsert(getOperation(lineIndex));
		}

		/**
		 * Specifies whether there is a deletion at the given {@code lineIndex} (i.e., the line index of the
		 * revised version).
		 * 
		 * @param lineIndex
		 *            The line index of the revised version of the text.
		 * @return <code>true</code> if there is a deletion, <code>false</code> otherwise.
		 */
		private boolean isDeleteAt(int lineIndex) {
			return hasIndex(lineIndex) && isDelete(getOperation(lineIndex));
		}

		/**
		 * Specifies whether the line at {@code lineIndex} is changed.
		 * <p>
		 * The line at {@code lineIndex} (i.e., the line index of the revised version) is changed if there is
		 * a deletion that is followed by an insertion or if there is an insertion only.
		 * </p>
		 * 
		 * @param lineIndex
		 *            The line index of the revised version of the text.
		 * @return <code>true</code> if the line is changed, <code>false</code> otherwise.
		 */
		private boolean isChangedAt(int lineIndex) {
			return (isDeleteAt(lineIndex) && isFollowedByInsert(lineIndex)) || isInsertAt(lineIndex);
		}

		/**
		 * Specifies whether the given {@code lineIndex} of the revised version is followed by an insertion.
		 * 
		 * @param lineIndex
		 *            The line index of the revised version of the text.
		 * @return <code>true</code> if the given {@code lineIndex} is followed by an insertion,
		 *         <code>false</code> otherwise.
		 */
		private boolean isFollowedByInsert(int lineIndex) {
			final boolean isFollowedByInsert;
			final int nextLineIndex = lineIndex + 1;
			if (hasIndex(nextLineIndex) && !isEqual(getOperation(nextLineIndex))) {
				final Operation nextOperation = getOperation(nextLineIndex);
				isFollowedByInsert = isInsert(nextOperation) || isFollowedByInsert(nextLineIndex);
			} else {
				isFollowedByInsert = false;
			}
			return isFollowedByInsert;
		}

		/**
		 * Returns the operation at the given {@code index}.
		 * <p>
		 * The index corresponds to the line index of the revised version of the text. If there is no
		 * operation at the given {@code index}, this method throws an {@link IndexOutOfBoundsException}.
		 * </p>
		 * 
		 * @param index
		 *            The index for which the operation is requested.
		 * @return The operation at the given {@code index}.
		 */
		private Operation getOperation(int index) {
			return operations.get(index);
		}

		/**
		 * Specifies whether the list operations has an operation at the given {@code index}.
		 * 
		 * @param index
		 *            The index for which the operation is requested.
		 * @return <code>true</code> if there is an operation at the given {@code index}, <code>false</code>
		 *         otherwise.
		 */
		private boolean hasIndex(int index) {
			return operations.size() > index;
		}

		/**
		 * Specifies whether the given {@code operation} is an insertion.
		 * 
		 * @param operation
		 *            The operation to check.
		 * @return <code>true</code> if it is an insertion, <code>false</code> otherwise.
		 */
		private boolean isInsert(Operation operation) {
			return Operation.INSERT.equals(operation);
		}

		/**
		 * Specifies whether the given {@code operation} is a deletion.
		 * 
		 * @param operation
		 *            The operation to check.
		 * @return <code>true</code> if it is a deletion, <code>false</code> otherwise.
		 */
		private boolean isDelete(final Operation operation) {
			return Operation.DELETE.equals(operation);
		}

		/**
		 * Specifies whether the given {@code operation} denotes an unchanged line (equal).
		 * 
		 * @param operation
		 *            The operation to check.
		 * @return <code>true</code> if the operation denotes an unchanged line, <code>false</code> otherwise.
		 */
		private boolean isEqual(final Operation operation) {
			return Operation.EQUAL.equals(operation);
		}
	}
}
