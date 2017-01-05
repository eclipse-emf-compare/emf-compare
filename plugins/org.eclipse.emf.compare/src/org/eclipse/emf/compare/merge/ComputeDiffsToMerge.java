/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.eclipse.emf.compare.ConflictKind.REAL;
import static org.eclipse.emf.compare.merge.IMergeCriterion.NONE;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;

/**
 * This class computes the diffs to merge for a given diff in the correct order, taking into account the
 * merger's indications about required and consequent diffs.
 * <dl>
 * <dt>Required diffs</dt>
 * <dd>These are the diffs that need to be merged (from a structural point of view) before a given diff for
 * this diff to be mergeabl by th merger.</dd>
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
	 * The direction of the merge, <code>true</code> for right to left.
	 */
	private final boolean rightToLeft;

	/**
	 * The merge criterion to use, can be <code>null</code>.
	 */
	private IMergeCriterion criterion;

	/**
	 * The merger registry to use.
	 */
	private IMerger.Registry2 registry;

	/**
	 * The ordered set of diffs to merge.
	 */
	private Set<Diff> result = new LinkedHashSet<Diff>();

	/** The unordered set of all diffs used to avoid infinite loops. */
	private Set<Diff> computing = new HashSet<Diff>();

	/**
	 * Whether the discovery of a (real) conflict in the differences needed must cause this to throw an
	 * Exception.
	 */
	private Predicate<? super Conflict> conflictChecker;

	/**
	 * Constructor.
	 * 
	 * @param rightToLeft
	 *            The merge direction
	 * @param registry
	 *            The Registry to use.
	 */
	public ComputeDiffsToMerge(boolean rightToLeft, IMerger.Registry2 registry) {
		this(rightToLeft, registry, NONE);
	}

	/**
	 * Constructor.
	 * 
	 * @param rightToLeft
	 *            The merge direction
	 * @param registry
	 *            The Registry to use.
	 * @param criterion
	 *            The merge criterion, must not be <code>null</code>
	 */
	public ComputeDiffsToMerge(boolean rightToLeft, IMerger.Registry2 registry, IMergeCriterion criterion) {
		this.rightToLeft = rightToLeft;
		this.registry = registry;
		this.criterion = checkNotNull(criterion);
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
		addDiffs(Collections.singleton(diff));
	}

	/**
	 * Recursively add the given diffs to the result Set, starting by their requirements, then themselves,
	 * then their consequences, in the right order.
	 * 
	 * @param diffs
	 *            The diffs to merge at the current step of the computation
	 */
	protected void addDiffs(Collection<Diff> diffs) {
		if (diffs.isEmpty()) {
			return;
		}
		Set<Diff> consequences = new LinkedHashSet<Diff>();
		for (Diff diff : diffs) {
			addDiff(diff, consequences);
		}
		addDiffs(Sets.difference(consequences, result));
	}

	/**
	 * Recursively add the required diffs, then the diff itself, to the result. Also add the consequent diffs
	 * of all the required diffs and the provided one to the given set of consequences.
	 * 
	 * @param diff
	 *            The diff to add
	 * @param consequences
	 *            The set of diffs that must be merged at the next step.
	 */
	protected void addDiff(Diff diff, Set<Diff> consequences) {
		if (!result.contains(diff) && computing.add(diff)) {
			Conflict conflict = diff.getConflict();
			if (conflictChecker != null && conflict != null && !conflictChecker.apply(conflict)
					&& diff.getConflict().getKind() == REAL) {
				List<Diff> diffsThatLedToConflict = new ArrayList<Diff>(result);
				diffsThatLedToConflict.add(diff);
				throw new MergeBlockedByConflictException(diffsThatLedToConflict);
			}
			DelegatingMerger mergerDelegate = AbstractMerger.getMergerDelegate(diff, registry, criterion);
			IMerger merger = mergerDelegate.getMerger();
			if (merger instanceof IMerger2) {
				Set<Diff> dependencies = ((IMerger2)merger).getDirectMergeDependencies(diff, rightToLeft);
				for (Diff required : dependencies) {
					addDiff(required, consequences);
				}
				result.add(diff);
				computing.remove(diff);
				consequences.addAll(((IMerger2)merger).getDirectResultingMerges(diff, rightToLeft));
			} else {
				result.add(diff);
			}
		}
	}
}
