/*******************************************************************************
 * Copyright (c) 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Martin Fleck - bug 514415
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import static org.eclipse.emf.compare.ConflictKind.REAL;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.internal.merge.MergeMode;

/**
 * This class computes the diffs to merge for a given diff in the correct order, taking into account the
 * merger's indications about required and consequent diffs.
 * <dl>
 * <dt>Required diffs</dt>
 * <dd>These are the diffs that need to be merged (from a structural point of view) before a given diff for
 * this diff to be mergeable by the merger.</dd>
 * <dt>Consequent diffs</dt>
 * <dd>These are all the diffs that, for some reason, the merger considers necessary to merge along with a
 * given diff, but which need not be merged before this diff for the merger to be able to merge it. This is
 * used to define atomicity of merge operations: Each merger defines the set of diffs that must be merged
 * together, and the engine makes surre all these diffs (required+consequent) are merged together or not at
 * all in one 'transaction'.</dd>
 * </dl>
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 3.5
 */
public class ComputeDiffsToMerge {

	/**
	 * If this is not acting on a MergeMode (i.e. direction is on a diff-by-diff basis), this will define the
	 * global direction of the merge.
	 */
	private final boolean rightToLeft;

	/**
	 * The current merging mode.
	 */
	private final MergeMode mergeMode;

	/** Tells us whether the left side of the comparison we're operating on is editable. */
	private final boolean isLeftEditable;

	/** Tells us whether the right side of the comparison we're operating on is editable. */
	private final boolean isRightEditable;

	/**
	 * The ordered set of diffs to merge.
	 */
	private Set<Diff> result = new LinkedHashSet<Diff>();

	/**
	 * The overall ordered set of diffs to merge as returned by {@link #getAllDiffsToMerge(Iterable)}.
	 */
	private Set<Diff> globalResult;

	/** The unordered set of all diffs used to avoid infinite loops. */
	private Set<Diff> computing = new HashSet<Diff>();

	/**
	 * Whether the discovery of a (real) conflict in the differences needed must cause this to throw an
	 * Exception.
	 */
	private Predicate<? super Conflict> conflictChecker;

	/** The relationship computer used to calculate dependencies and requirements of diffs. */
	private IDiffRelationshipComputer relationshipComputer;

	/**
	 * Constructor.
	 * 
	 * @param mergeMode
	 *            The kind of merging we're about to implement.
	 * @param isLeftEditable
	 *            Whether the left side of the comparison we're operating on is editable.
	 * @param isRightEditable
	 *            Whether the right side of the comparison we're operating on is editable.
	 * @param relationshipComputer
	 *            The relationship computer used to calculate dependencies and requirements of diffs.
	 */
	public ComputeDiffsToMerge(MergeMode mergeMode, boolean isLeftEditable, boolean isRightEditable,
			IDiffRelationshipComputer relationshipComputer) {
		this(true, mergeMode, isLeftEditable, isRightEditable, relationshipComputer);
	}

	/**
	 * Constructor if there is no merge mode for this action.
	 * 
	 * @param rightToLeft
	 *            Direction of the merge.
	 * @param relationshipComputer
	 *            The relationship computer used to calculate dependencies and requirements of diffs.
	 */
	public ComputeDiffsToMerge(boolean rightToLeft, IDiffRelationshipComputer relationshipComputer) {
		this(rightToLeft, null, true, true, relationshipComputer);
	}

	/**
	 * Constructor.
	 * 
	 * @param rightToLeft
	 *            Direction of the merge. Ignored if {@link MergeMode} is not null.
	 * @param mergeMode
	 *            The kind of merging we're about to implement.
	 * @param isLeftEditable
	 *            Whether the left side of the comparison we're operating on is editable.
	 * @param isRightEditable
	 *            Whether the right side of the comparison we're operating on is editable.
	 * @param relationshipComputer
	 *            The relationship computer used to calculate dependencies and requirements of diffs.
	 */
	private ComputeDiffsToMerge(boolean rightToLeft, MergeMode mergeMode, boolean isLeftEditable,
			boolean isRightEditable, IDiffRelationshipComputer relationshipComputer) {
		this.rightToLeft = rightToLeft;
		this.mergeMode = mergeMode;
		this.isLeftEditable = isLeftEditable;
		this.isRightEditable = isRightEditable;
		this.relationshipComputer = relationshipComputer;
	}

	/**
	 * Set the failOnConflict flag.
	 * 
	 * @param predicate
	 *            Predicate that will be used to check whether an exception must be thrown when encountering a
	 *            diff that's involved in a conflict. This predicate will be applied to the conflict, and if
	 *            it returns <code>false</code> then an exception will be thrown.
	 * @return This for a fluent API.
	 */
	public ComputeDiffsToMerge failOnRealConflictUnless(Predicate<? super Conflict> predicate) {
		conflictChecker = predicate;
		return this;
	}

	/**
	 * Compute the ordered set of diffs to merge for the given diffs.
	 * 
	 * @param diffs
	 *            The diffs to merge, along with its required diffs and its consequent diffs.
	 * @return An ordered Set that contains all the diffs to merge, in the correct order based on required
	 *         diffs and consequent diffs.
	 */
	public Set<Diff> getAllDiffsToMerge(Iterable<? extends Diff> diffs) {
		try {
			globalResult = Sets.newLinkedHashSet();
			Set<Diff> globalIgnoredDiffs = Sets.newHashSet();
			Set<Diff> diffPath = Sets.newLinkedHashSet();
			for (Diff diff : diffs) {
				if (!globalIgnoredDiffs.contains(diff) && !globalResult.contains(diff)) {
					try {
						result.clear();
						computing.clear();
						diffPath.clear();
						addDiffs(Collections.singleton(diff), diffPath);
						globalResult.addAll(result);
					} catch (MergeBlockedByConflictException ex) {
						globalIgnoredDiffs.addAll(ex.getConflictingDiffs());
					}
				}
			}
			return globalResult;
		} finally {
			globalResult = null;
		}
	}

	/**
	 * Compute the ordered set of diffs to merge for the given diff.
	 * 
	 * @param diff
	 *            The diff to merge, along with its required diffs and its consequent diffs.
	 * @return An ordered Set that contains all the diffs to merge, in the correct order.
	 */
	public Set<Diff> getAllDiffsToMerge(Diff diff) {
		result.clear();
		computing.clear();
		addDiff(diff);
		return result;
	}

	/**
	 * Compute the ordered set of diffs for the given diff.
	 * 
	 * @param diff
	 *            The diff to merge
	 */
	protected void addDiff(Diff diff) {
		addDiffs(Collections.singleton(diff), Sets.<Diff> newLinkedHashSet());
	}

	/**
	 * Recursively add the given diffs to the result Set, starting by their requirements, then themselves,
	 * then their consequences, in the right order.
	 * 
	 * @param diffs
	 *            The diffs to merge at the current step of the computation
	 * @param diffPath
	 *            The path that lead to a diff
	 */
	protected void addDiffs(Collection<Diff> diffs, Set<Diff> diffPath) {
		if (diffs.isEmpty()) {
			return;
		}
		Set<Diff> consequences = new LinkedHashSet<Diff>();
		for (Diff diff : diffs) {
			addDiff(diff, consequences, diffPath);
		}
		addDiffs(consequences, diffPath);
	}

	/**
	 * Recursively add the required diffs, then the diff itself, to the result. Also add the consequent diffs
	 * of all the required diffs and the provided one to the given set of consequences.
	 * 
	 * @param diff
	 *            The diff to add
	 * @param consequences
	 *            The set of diffs that must be merged at the next step.
	 * @param diffPath
	 *            The path that lead to the diff to add
	 */
	protected void addDiff(Diff diff, Set<Diff> consequences, Set<Diff> diffPath) {
		if (!result.contains(diff) && (globalResult == null || !globalResult.contains(diff))
				&& computing.add(diff)) {
			boolean addedToPath = diffPath.add(diff);
			if (conflictChecker != null) {
				Conflict conflict = diff.getConflict();
				if (conflict != null && conflict.getKind() == REAL && !conflictChecker.apply(conflict)) {
					throw new MergeBlockedByConflictException(diffPath);
				}
			}

			boolean mergeRightToLeft = rightToLeft;
			if (mergeMode != null) {
				mergeRightToLeft = !mergeMode.isLeftToRight(diff, isLeftEditable, isRightEditable);
			}
			Set<Diff> dependencies = relationshipComputer.getDirectMergeDependencies(diff, mergeRightToLeft);
			for (Diff required : dependencies) {
				addDiff(required, consequences, diffPath);
			}

			result.add(diff);
			computing.remove(diff);

			final Set<Diff> directResultingMerges = relationshipComputer.getDirectResultingMerges(diff,
					mergeRightToLeft);
			consequences.addAll(directResultingMerges);

			if (addedToPath) {
				diffPath.remove(diff);
			}
		}
	}
}
