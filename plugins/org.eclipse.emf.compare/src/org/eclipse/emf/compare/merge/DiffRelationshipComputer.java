/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;
import static org.eclipse.emf.compare.ConflictKind.PSEUDO;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.sameSideAs;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.merge.IMerger.Registry2;

/**
 * A computer implementation to calculate the relationship of differences in EMF Compare.
 * 
 * @since 3.5
 * @author Martin Fleck <mfleck@eclipsesource.com>
 * @see IMerger2
 */
public class DiffRelationshipComputer implements IDiffRelationshipComputer {

	/** Merger registry used to retrieve the correct merger. */
	protected Registry registry;

	/** Merge criterion used to retrieve the correct merger. */
	protected IMergeCriterion criterion;

	/**
	 * Creates a new relationship computer.
	 * 
	 * @param registry
	 *            merger registry
	 */
	public DiffRelationshipComputer(IMerger.Registry registry) {
		this(registry, IMergeCriterion.NONE);
	}

	/**
	 * Creates a new relationship computer.
	 * 
	 * @param registry
	 *            merger registry
	 * @param criterion
	 *            merge criterion used to get the merger from the registry, use {@link IMergeCriterion#NONE}
	 *            if no special criterion should be set.
	 */
	public DiffRelationshipComputer(IMerger.Registry registry, IMergeCriterion criterion) {
		this.registry = registry;
		this.criterion = criterion;
	}

	@Override
	public Registry getMergerRegistry() {
		return registry;
	}

	@Override
	public void setMergerRegistry(Registry mergerRegistry) {
		this.registry = mergerRegistry;
	}

	@Override
	public IMergeCriterion getMergeCriterion() {
		return criterion;
	}

	@Override
	public void setMergeCriterion(IMergeCriterion mergeCriterion) {
		this.criterion = mergeCriterion;
	}

	/**
	 * Returns the {@link #getMergerRegistry() merger registry} as {@link Registry2}, if possible. If the
	 * merger registry is not an instance of {@link Registry2}, null is returned.
	 * 
	 * @return {@link Registry2} instance or null.
	 */
	protected Registry2 getMergerRegistry2() {
		if (registry instanceof Registry2) {
			return (Registry2)registry;
		}
		return null;
	}

	@Override
	public IMerger2 getMerger(Diff diff) {
		if (getMergerRegistry2() == null) {
			return null;
		}

		DelegatingMerger mergerDelegate = AbstractMerger.getMergerDelegate(diff, getMergerRegistry2(),
				getMergeCriterion());
		IMerger merger = mergerDelegate.getMerger();
		if (!(merger instanceof IMerger2)) {
			return null;
		}

		return (IMerger2)merger;
	}

	@Override
	public boolean hasMerger(Diff diff) {
		return getMerger(diff) != null;
	}

	@Override
	public Set<Diff> getDirectMergeDependencies(Diff diff, boolean mergeRightToLeft) {
		IMerger2 merger = getMerger(diff);
		if (merger != null) {
			return merger.getDirectMergeDependencies(diff, mergeRightToLeft);
		}
		return Sets.newHashSet();
	}

	@Override
	public Set<Diff> getDirectResultingMerges(Diff diff, boolean mergeRightToLeft) {
		IMerger2 merger = getMerger(diff);
		if (merger != null) {
			return merger.getDirectResultingMerges(diff, mergeRightToLeft);
		}
		return Sets.newHashSet();
	}

	@Override
	public Set<Diff> getDirectResultingRejections(Diff diff, boolean mergeRightToLeft) {
		IMerger2 merger = getMerger(diff);
		if (merger != null) {
			return merger.getDirectResultingRejections(diff, mergeRightToLeft);
		} else {
			return Sets.newHashSet();
		}
	}

	@Override
	public Set<Diff> getAllResultingMerges(Diff diff, boolean rightToLeft) {
		final Set<Diff> resultingMerges = new LinkedHashSet<Diff>();
		resultingMerges.add(diff);

		Set<Diff> relations = internalGetAllResultingMerges(diff, rightToLeft);
		// We don't want to take in account pseudo conflicts since there is nothing to do with them
		// and their dependencies may cause incorrect merge dependencies computation.
		Set<Diff> difference = Sets.filter(Sets.difference(relations, resultingMerges),
				not(hasConflict(PSEUDO)));
		while (!difference.isEmpty()) {
			final Set<Diff> newRelations = new LinkedHashSet<Diff>(difference);
			resultingMerges.addAll(newRelations);
			relations = new LinkedHashSet<Diff>();
			for (Diff newRelation : newRelations) {
				Set<Diff> internalResultingMerges = internalGetAllResultingMerges(newRelation, rightToLeft);
				// We don't want to take in account pseudo conflicts since there is nothing to do with them
				// and there dependencies may cause incorrect merge dependencies computation.
				relations.addAll(Sets.filter(internalResultingMerges, not(hasConflict(PSEUDO))));
			}
			difference = Sets.difference(relations, resultingMerges);
		}

		// If a pseudo conflict is directly selected, we want to display other diffs of the pseudo conflict as
		// resulting merge for the user
		if (diff.getConflict() != null && diff.getConflict().getKind() == PSEUDO) {
			resultingMerges.addAll(diff.getConflict().getDifferences());
		}

		return resultingMerges;
	}

	/**
	 * Returns the set of all differences <b>directly</b> related to the given one, either as dependencies or
	 * as implications.
	 * 
	 * @param diff
	 *            The difference for which we seek all directly related others.
	 * @param rightToLeft
	 *            The direction in which we're considering a merge.
	 * @return The set of all differences <b>directly</b> related to the given one.
	 */
	protected Set<Diff> internalGetAllResultingMerges(Diff diff, boolean rightToLeft) {
		final Set<Diff> directParents = getDirectMergeDependencies(diff, rightToLeft);
		final Set<Diff> directImplications = getDirectResultingMerges(diff, rightToLeft);
		final SetView<Diff> directRelated = Sets.union(directParents, directImplications);
		return directRelated;
	}

	@Override
	public Set<Diff> getAllResultingRejections(Diff diff, boolean mergeRightToLeft) {
		final Set<Diff> resultingRejections = new LinkedHashSet<Diff>();
		final Set<Diff> allResultingMerges = getAllResultingMerges(diff, mergeRightToLeft);
		resultingRejections.addAll(
				Sets.filter(allResultingMerges, and(not(hasConflict(PSEUDO)), not(sameSideAs(diff)))));
		// Only search rejections caused by diffs on the same side
		for (Diff resulting : Sets.filter(allResultingMerges, sameSideAs(diff))) {
			Set<Diff> rejections = getDirectResultingRejections(resulting, mergeRightToLeft);
			// We don't want to take in account pseudo conflicts since there is nothing to do with them
			// and their dependencies may cause incorrect merge dependencies computation.
			Set<Diff> difference = Sets.filter(rejections, not(hasConflict(PSEUDO)));
			while (!difference.isEmpty()) {
				final Set<Diff> newRejections = new LinkedHashSet<Diff>(difference);
				resultingRejections.addAll(newRejections);
				rejections = new LinkedHashSet<Diff>();
				for (Diff rejected : newRejections) {
					Set<Diff> directMergeDependencies = getDirectMergeDependencies(rejected,
							mergeRightToLeft);
					// We don't want to take in account pseudo conflicts since there is nothing to do with
					// them and their dependencies may cause incorrect merge dependencies computation.
					// We also don't want to consider diffs on the same side for rejections
					rejections.addAll(Sets.filter(directMergeDependencies,
							and(not(hasConflict(PSEUDO)), not(sameSideAs(diff)))));
					Set<Diff> directResultingMerges = getDirectResultingMerges(rejected, mergeRightToLeft);
					rejections.addAll(Sets.filter(directResultingMerges,
							and(not(hasConflict(PSEUDO)), not(sameSideAs(diff)))));
				}
				difference = Sets.difference(rejections, resultingRejections);
			}
		}
		return resultingRejections;
	}

}
