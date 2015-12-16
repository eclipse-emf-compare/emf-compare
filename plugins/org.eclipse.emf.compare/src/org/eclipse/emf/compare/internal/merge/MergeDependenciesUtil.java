/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 462884
 *******************************************************************************/
package org.eclipse.emf.compare.internal.merge;

import static com.google.common.collect.Iterables.addAll;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.graph.IGraph;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.internal.utils.Graph;
import org.eclipse.emf.compare.merge.AbstractMerger;
import org.eclipse.emf.compare.merge.IMergeOptionAware;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger2;

/**
 * Factorizes utilities used throughout EMF Compare to explore merge dependencies.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class MergeDependenciesUtil {
	/** Hides default constructor. */
	private MergeDependenciesUtil() {
		// Hides default constructor
	}

	/**
	 * This will map all the differences from the given comparison in a dependency graph, enabling EMF Compare
	 * to differentiate what can be safely merged from what cannot.
	 * 
	 * @param comparison
	 *            The comparison which differences are to be mapped into a dependency graph.
	 * @param mergerRegistry
	 *            The {@link IMerger.Registry merger registry} currently in use.
	 * @param mergeRightToLeft
	 *            The direction in which we're preparing a merge.
	 * @param mergeMode
	 *            The merge mode. If MergeMode is null, then no differences will be filtered.
	 * @return The dependency graph of this comparison's differences.
	 * @see #mapDifferences(Collection, org.eclipse.emf.compare.merge.IMerger.Registry, boolean, MergeMode)
	 */
	public static IGraph<Diff> mapDifferences(Comparison comparison, IMerger.Registry mergerRegistry,
			boolean mergeRightToLeft, MergeMode mergeMode) {
		return mapDifferences(comparison.getDifferences(), mergerRegistry, mergeRightToLeft, mergeMode);
	}

	/**
	 * This will map the given differences in a dependency graph, enabling EMF Compare to differentiate what
	 * can be safely merged from what cannot.
	 * <p>
	 * Typically, all differences that are not in a real conflict with another and that do not depend,
	 * directly or indirectly, on a conflicting difference can be pre-merged without corrupting the models.
	 * For example, if adding a reference "ref1" to an added class "C1" depends on the addition of a package
	 * "P1" (i.e. three additions in a row), but "C1" has also been added in another place in the other model
	 * (a conflict between two "same" elements added into different containers), then we can safely pre-merge
	 * the addition of P1, but not the addition of its contained class C1, nor the addition of ref1.
	 * </p>
	 * 
	 * @param differences
	 *            The differences to be mapped into a dependency graph.
	 * @param mergerRegistry
	 *            The {@link IMerger.Registry merger registry} currently in use.
	 * @param mergeRightToLeft
	 *            The direction in which we're preparing a merge.
	 * @param mergeMode
	 *            The merge mode. If MergeMode is null, then no differences will be filtered.
	 * @return The dependency graph of this comparison's differences.
	 */
	public static IGraph<Diff> mapDifferences(Collection<Diff> differences, IMerger.Registry mergerRegistry,
			boolean mergeRightToLeft, MergeMode mergeMode) {
		IGraph<Diff> differencesGraph = new Graph<Diff>();
		final Predicate<? super Diff> filter;
		if (mergeMode == MergeMode.RIGHT_TO_LEFT) {
			filter = fromSide(DifferenceSource.RIGHT);
		} else if (mergeMode == MergeMode.LEFT_TO_RIGHT) {
			filter = fromSide(DifferenceSource.LEFT);
		} else {
			filter = Predicates.alwaysTrue();
		}
		for (Diff diff : Iterables.filter(differences, filter)) {
			final IMerger merger = mergerRegistry.getHighestRankingMerger(diff);
			final Set<Diff> directParents;
			if (merger instanceof IMerger2) {
				directParents = ((IMerger2)merger).getDirectMergeDependencies(diff, mergeRightToLeft);
			} else {
				directParents = Collections.emptySet();
			}
			if (directParents.isEmpty()) {
				differencesGraph.add(diff);
			} else {
				for (Diff parent : directParents) {
					differencesGraph.addChildren(parent, Collections.singleton(diff));
				}
			}
		}
		return differencesGraph;
	}

	/**
	 * Retrieves the set of all diffs related to the given <code>diff</code> when merging in the given
	 * direction.
	 * <p>
	 * This is expected to return the set of all differences that will be need to merged along when a user
	 * wishes to merge <code>diff</code>, either because they are required by it or because they are implied
	 * by it one way or another.
	 * </p>
	 * <p>
	 * Note that <code>diff</code> will be included in the returned set.
	 * </p>
	 * 
	 * @param diff
	 *            The difference for which we seek all related ones.
	 * @param mergerRegistry
	 *            The {@link IMerger.Registry merger registry} currently in use.
	 * @param mergeRightToLeft
	 *            The direction in which we're considering a merge.
	 * @return The set of all diffs related to the given <code>diff</code> when merging in the given
	 *         direction.
	 */
	public static Set<Diff> getAllResultingMerges(Diff diff, IMerger.Registry mergerRegistry,
			boolean mergeRightToLeft) {
		final Set<Diff> resultingMerges = new LinkedHashSet<Diff>();
		resultingMerges.add(diff);

		Set<Diff> relations = internalGetResultingMerges(diff, mergerRegistry, mergeRightToLeft, diff
				.getSource());
		Set<Diff> difference = Sets.difference(relations, resultingMerges);
		while (!difference.isEmpty()) {
			final Set<Diff> newRelations = new LinkedHashSet<Diff>(difference);
			resultingMerges.addAll(newRelations);
			relations = new LinkedHashSet<Diff>();
			for (Diff newRelation : newRelations) {
				relations.addAll(internalGetResultingMerges(newRelation, mergerRegistry, mergeRightToLeft,
						diff.getSource()));
			}
			difference = Sets.difference(relations, resultingMerges);
		}

		return resultingMerges;
	}

	/**
	 * Returns the set of all differences <b>directly</b> related to the given one, either as dependencies or
	 * as implications.
	 * 
	 * @param diff
	 *            The difference for which we seek all directly related others.
	 * @param mergerRegistry
	 *            The {@link IMerger.Registry merger registry} currently in use.
	 * @param mergeRightToLeft
	 *            The direction in which we're considering a merge.
	 * @param originalSource
	 *            The original side of the diff the dependencies of which we are computing
	 * @return The set of all differences <b>directly</b> related to the given one.
	 */
	private static Set<Diff> internalGetResultingMerges(Diff diff, IMerger.Registry mergerRegistry,
			boolean mergeRightToLeft, DifferenceSource originalSource) {
		final IMerger merger = mergerRegistry.getHighestRankingMerger(diff);
		// If a (pseudo-)conflict makes use merge diffs from the other side,
		// we must then look for the consequences of these diffs
		// as if they had been merged the other way around.
		final boolean direction;
		if (diff.getSource() == originalSource) {
			direction = mergeRightToLeft;
		} else {
			direction = !mergeRightToLeft;
		}
		final Set<Diff> directParents;
		final Set<Diff> directImplications;
		if (merger instanceof IMerger2) {
			directParents = ((IMerger2)merger).getDirectMergeDependencies(diff, direction);
			directImplications = ((IMerger2)merger).getDirectResultingMerges(diff, direction);
		} else {
			directParents = Collections.emptySet();
			directImplications = Collections.emptySet();
		}

		// FIXME [PERF] Useless copy
		final LinkedHashSet<Diff> directRelated = Sets.newLinkedHashSet(Sets.union(directParents,
				directImplications));

		if (merger instanceof IMergeOptionAware) {
			Object subDiffs = ((IMergeOptionAware)merger).getMergeOptions().get(
					AbstractMerger.SUB_DIFF_AWARE_OPTION);
			if (subDiffs == Boolean.TRUE) {
				addAll(directRelated, ComparisonUtil.getSubDiffs(!direction).apply(diff));
			}
		}

		return directRelated;
	}

	/**
	 * Retrieves the set of all diffs that will be rejected if the given <code>diff</code> is merged, either
	 * because of unresolveable conflicts or because of unreachable requirements.
	 * 
	 * @param diff
	 *            The difference for which we seek all opposite ones.
	 * @param mergerRegistry
	 *            The {@link IMerger.Registry merger registry} currently in use.
	 * @param mergeRightToLeft
	 *            The direction in which we're considering a merge.
	 * @return The set of all diffs that will be rejected if the given <code>diff</code> is merged in the
	 *         given direction.
	 */
	public static Set<Diff> getAllResultingRejections(Diff diff, IMerger.Registry mergerRegistry,
			boolean mergeRightToLeft) {
		final Set<Diff> resultingRejections = new LinkedHashSet<Diff>();

		final Set<Diff> allResultingMerges = getAllResultingMerges(diff, mergerRegistry, mergeRightToLeft);
		for (Diff resulting : allResultingMerges) {
			Set<Diff> rejections = internalGetResultingRejections(resulting, mergerRegistry,
					mergeRightToLeft, diff.getSource());
			Set<Diff> difference = Sets.difference(rejections, resultingRejections);
			while (!difference.isEmpty()) {
				final Set<Diff> newRejections = new LinkedHashSet<Diff>(difference);
				resultingRejections.addAll(newRejections);
				rejections = new LinkedHashSet<Diff>();
				for (Diff rejected : newRejections) {
					final IMerger merger = mergerRegistry.getHighestRankingMerger(rejected);
					if (merger instanceof IMerger2) {
						rejections.addAll(((IMerger2)merger).getDirectMergeDependencies(rejected,
								mergeRightToLeft));
						rejections.addAll(((IMerger2)merger).getDirectResultingMerges(rejected,
								mergeRightToLeft));
					}
				}
				difference = Sets.difference(rejections, resultingRejections);
			}
		}

		return resultingRejections;
	}

	/**
	 * Returns the set of differences directly related to <code>diff</code> that will be rejected if it is
	 * merged.
	 * 
	 * @param diff
	 *            The difference for which we seek all opposite ones.
	 * @param mergerRegistry
	 *            The {@link IMerger.Registry merger registry} currently in use.
	 * @param mergeRightToLeft
	 *            The direction in which we're considering a merge.
	 * @param originalSource
	 *            The original side of the diff the dependencies of which we are computing
	 * @return The set of all directly related differences that will be rejected if <code>diff</code> is
	 *         merged in the given direction.
	 */
	private static Set<Diff> internalGetResultingRejections(Diff diff, IMerger.Registry mergerRegistry,
			boolean mergeRightToLeft, DifferenceSource originalSource) {
		final boolean direction;
		if (diff.getSource() == originalSource) {
			direction = mergeRightToLeft;
		} else {
			direction = !mergeRightToLeft;
		}
		final IMerger merger = mergerRegistry.getHighestRankingMerger(diff);
		if (merger instanceof IMerger2) {
			return ((IMerger2)merger).getDirectResultingRejections(diff, direction);
		}
		return Collections.emptySet();
	}
}
