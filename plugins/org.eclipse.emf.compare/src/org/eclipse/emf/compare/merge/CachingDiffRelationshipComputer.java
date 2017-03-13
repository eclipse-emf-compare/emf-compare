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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.merge.IMerger.Registry;

/**
 * A computer implementation to cache the relationship of diffs.
 * 
 * @since 3.5
 * @author Martin Fleck <mfleck@eclipsesource.com>
 * @see IMerger2
 */
public class CachingDiffRelationshipComputer implements IDiffRelationshipComputer {

	/** A computer instance to calculate the relationships between diffs. */
	protected IDiffRelationshipComputer computer;

	/** Direct merge dependencies: right to left. */
	protected Map<Diff, Set<Diff>> directMergeDependenciesR2L = new ConcurrentHashMap<>();

	/** Direct merge dependencies: left to right. */
	protected Map<Diff, Set<Diff>> directMergeDependenciesL2R = new ConcurrentHashMap<>();

	/** Direct resulting merges: right to left. */
	protected Map<Diff, Set<Diff>> directResultingMergesR2L = new ConcurrentHashMap<>();

	/** Direct resulting merges: left to right. */
	protected Map<Diff, Set<Diff>> directResultingMergesL2R = new ConcurrentHashMap<>();

	/** Direct resulting rejections: right to left. */
	protected Map<Diff, Set<Diff>> directResultingRejectionsR2L = new ConcurrentHashMap<>();

	/** Direct resulting rejections: left to right. */
	protected Map<Diff, Set<Diff>> directResultingRejectionsL2R = new ConcurrentHashMap<>();

	/** All resulting merges: right to left. */
	protected Map<Diff, Set<Diff>> allResultingMergesR2L = new ConcurrentHashMap<>();

	/** All resulting merges: left to right. */
	protected Map<Diff, Set<Diff>> allResultingMergesL2R = new ConcurrentHashMap<>();

	/** All resulting rejections: right to left. */
	protected Map<Diff, Set<Diff>> allResultingRejectionsR2L = new ConcurrentHashMap<>();

	/** All resulting rejections: left to right. */
	protected Map<Diff, Set<Diff>> allResultingRejectionsL2R = new ConcurrentHashMap<>();

	/**
	 * Creates a new computer with the given registry.
	 * 
	 * @param registry
	 *            merger registry
	 */
	public CachingDiffRelationshipComputer(IMerger.Registry registry) {
		this(registry, IMergeCriterion.NONE);
	}

	/**
	 * Creates a new computer with the given registry and merge criterion.
	 * 
	 * @param registry
	 *            merger registry
	 * @param criterion
	 *            merge criterion used to get the merger from the registry, use {@link IMergeCriterion#NONE}
	 *            if no special criterion should be set.
	 */
	public CachingDiffRelationshipComputer(IMerger.Registry registry, IMergeCriterion criterion) {
		this(new DiffRelationshipComputer(registry, criterion));
	}

	/**
	 * Creates a new computer by wrapping the given instance. Any computing calls are delegated to this
	 * instance and cached.
	 * 
	 * @param computer
	 *            computer instance used for calculating diff relationships.
	 */
	public CachingDiffRelationshipComputer(IDiffRelationshipComputer computer) {
		this.computer = computer;
	}

	/**
	 * Returns the internal computer instance used to compute the diff relationships.
	 * 
	 * @return internal computer instance.
	 */
	protected IDiffRelationshipComputer getComputer() {
		return computer;
	}

	@Override
	public Registry getMergerRegistry() {
		return getComputer().getMergerRegistry();
	}

	/**
	 * {@inheritDoc} WARNING: Setting the merger registry invalidates previously cached results, if another
	 * registry was set previously!
	 */
	public void setMergerRegistry(Registry mergerRegistry) {
		if (getMergerRegistry() != mergerRegistry) {
			getComputer().setMergerRegistry(mergerRegistry);
			invalidate();
		}
	}

	@Override
	public IMergeCriterion getMergeCriterion() {
		return getComputer().getMergeCriterion();
	}

	@Override
	public IMerger2 getMerger(Diff diff) {
		return getComputer().getMerger(diff);
	}

	@Override
	public boolean hasMerger(Diff diff) {
		return getComputer().hasMerger(diff);
	}

	/**
	 * {@inheritDoc} WARNING: Setting the merge criterion invalidates previously cached results, if another
	 * criterion was set previously.
	 */
	public void setMergeCriterion(IMergeCriterion mergeCriterion) {
		if (getMergeCriterion() != mergeCriterion) {
			getComputer().setMergeCriterion(mergeCriterion);
			invalidate();
		}
	}

	/**
	 * Caches the given direct merge dependencies.
	 * 
	 * @param diff
	 *            diff
	 * @param mergeRightToLeft
	 *            merge direction
	 * @param directMergeDependencies
	 *            direct merge dependencies of diff
	 */
	protected void setCachedDirectMergeDependencies(Diff diff, boolean mergeRightToLeft,
			Set<Diff> directMergeDependencies) {
		if (mergeRightToLeft) {
			directMergeDependenciesR2L.put(diff, directMergeDependencies);
		} else {
			directMergeDependenciesL2R.put(diff, directMergeDependencies);
		}
	}

	/**
	 * Returns the cached direct merge dependencies.
	 * 
	 * @param diff
	 *            diff
	 * @param mergeRightToLeft
	 *            merge direction
	 * @return cached direct merge dependencies
	 */
	protected Set<Diff> getCachedDirectMergeDependencies(Diff diff, boolean mergeRightToLeft) {
		if (mergeRightToLeft) {
			return directMergeDependenciesR2L.get(diff);
		} else {
			return directMergeDependenciesL2R.get(diff);
		}
	}

	/**
	 * Computes direct merge dependencies for the given diff.
	 * 
	 * @param diff
	 *            diff
	 * @param mergeRightToLeft
	 *            merge direction
	 * @return a non-null set of direct merge dependencies
	 */
	protected Set<Diff> computeDirectMergeDependencies(Diff diff, boolean mergeRightToLeft) {
		return getComputer().getDirectMergeDependencies(diff, mergeRightToLeft);
	}

	/**
	 * Returns the cached direct merge dependencies, if present. Otherwise, the direct merge dependencies are
	 * retrieved and cached using the given merger.
	 * 
	 * @param diff
	 *            diff
	 * @param mergeRightToLeft
	 *            merge direction
	 * @return cached direct merge dependencies
	 * @see IMerger2#getDirectMergeDependencies(Diff, boolean)
	 */
	@Override
	public Set<Diff> getDirectMergeDependencies(Diff diff, boolean mergeRightToLeft) {
		Set<Diff> directMergeDependencies = getCachedDirectMergeDependencies(diff, mergeRightToLeft);
		if (directMergeDependencies == null) {
			directMergeDependencies = computeDirectMergeDependencies(diff, mergeRightToLeft);
			setCachedDirectMergeDependencies(diff, mergeRightToLeft, directMergeDependencies);
		}
		return directMergeDependencies;
	}

	/**
	 * Caches the given direct resulting merges.
	 * 
	 * @param diff
	 *            diff
	 * @param mergeRightToLeft
	 *            merge direction
	 * @param directResultingMerges
	 *            direct resulting merges
	 */
	protected void setCachedDirectResultingMerges(Diff diff, boolean mergeRightToLeft,
			Set<Diff> directResultingMerges) {
		if (mergeRightToLeft) {
			directResultingMergesR2L.put(diff, directResultingMerges);
		} else {
			directResultingMergesL2R.put(diff, directResultingMerges);
		}
	}

	/**
	 * Returns the cached direct resulting merges.
	 * 
	 * @param diff
	 *            diff
	 * @param mergeRightToLeft
	 *            merge direction
	 * @return cached direct resulting merges
	 */
	protected Set<Diff> getCachedDirectResultingMerges(Diff diff, boolean mergeRightToLeft) {
		if (mergeRightToLeft) {
			return directResultingMergesR2L.get(diff);
		} else {
			return directResultingMergesL2R.get(diff);
		}
	}

	/**
	 * Computes direct resulting merges for the given diff.
	 * 
	 * @param diff
	 *            diff
	 * @param mergeRightToLeft
	 *            merge direction
	 * @return a non-null set of all resulting merges
	 */
	protected Set<Diff> computeDirectResultingMerges(Diff diff, boolean mergeRightToLeft) {
		return getComputer().getDirectResultingMerges(diff, mergeRightToLeft);
	}

	/**
	 * Returns the cached direct resulting merges, if present. Otherwise, the direct resulting merges are
	 * retrieved and cached using the given merger.
	 * 
	 * @param diff
	 *            diff
	 * @param mergeRightToLeft
	 *            merge direction
	 * @return cached direct resulting merges
	 * @see IMerger2#getDirectResultingMerges(Diff, boolean)
	 */
	@Override
	public Set<Diff> getDirectResultingMerges(Diff diff, boolean mergeRightToLeft) {
		Set<Diff> directResultingMerges = getCachedDirectResultingMerges(diff, mergeRightToLeft);
		if (directResultingMerges == null) {
			directResultingMerges = computeDirectResultingMerges(diff, mergeRightToLeft);
			setCachedDirectResultingMerges(diff, mergeRightToLeft, directResultingMerges);
		}
		return directResultingMerges;
	}

	/**
	 * Caches the given direct resulting rejections.
	 * 
	 * @param diff
	 *            diff
	 * @param mergeRightToLeft
	 *            merge direction
	 * @param directResultingRejections
	 *            direct resulting rejections
	 */
	protected void setCachedDirectResultingRejections(Diff diff, boolean mergeRightToLeft,
			Set<Diff> directResultingRejections) {
		if (mergeRightToLeft) {
			directResultingRejectionsR2L.put(diff, directResultingRejections);
		} else {
			directResultingRejectionsL2R.put(diff, directResultingRejections);
		}
	}

	/**
	 * Returns the cached direct resulting rejections.
	 * 
	 * @param diff
	 *            diff
	 * @param mergeRightToLeft
	 *            merge direction
	 * @return cached direct resulting rejections
	 */
	protected Set<Diff> getCachedDirectResultingRejections(Diff diff, boolean mergeRightToLeft) {
		if (mergeRightToLeft) {
			return directResultingRejectionsR2L.get(diff);
		} else {
			return directResultingRejectionsL2R.get(diff);
		}
	}

	/**
	 * Computes the direct resulting rejections.
	 * 
	 * @param diff
	 *            diff
	 * @param mergeRightToLeft
	 *            merge direction
	 * @return a non-null set of direct resulting rejections
	 */
	protected Set<Diff> computeDirectResultingRejections(Diff diff, boolean mergeRightToLeft) {
		return getComputer().getDirectResultingRejections(diff, mergeRightToLeft);
	}

	@Override
	public Set<Diff> getDirectResultingRejections(Diff diff, boolean mergeRightToLeft) {
		Set<Diff> directResultingRejections = getCachedDirectResultingRejections(diff, mergeRightToLeft);
		if (directResultingRejections == null) {
			directResultingRejections = computeDirectResultingRejections(diff, mergeRightToLeft);
			setCachedDirectResultingRejections(diff, mergeRightToLeft, directResultingRejections);
		}
		return directResultingRejections;
	}

	/**
	 * Caches the given all resulting rejections.
	 * 
	 * @param diff
	 *            diff
	 * @param mergeRightToLeft
	 *            merge direction
	 * @param allResultingRejections
	 *            all resulting rejections
	 */
	protected void setCachedAllResultingRejections(Diff diff, boolean mergeRightToLeft,
			Set<Diff> allResultingRejections) {
		if (mergeRightToLeft) {
			allResultingRejectionsR2L.put(diff, allResultingRejections);
		} else {
			allResultingRejectionsL2R.put(diff, allResultingRejections);
		}
	}

	/**
	 * Returns the cached all resulting rejections.
	 * 
	 * @param diff
	 *            diff
	 * @param mergeRightToLeft
	 *            merge direction
	 * @return cached all resulting rejections
	 */
	protected Set<Diff> getCachedAllResultingRejections(Diff diff, boolean mergeRightToLeft) {
		if (mergeRightToLeft) {
			return allResultingRejectionsR2L.get(diff);
		} else {
			return allResultingRejectionsL2R.get(diff);
		}
	}

	/**
	 * Computes all resulting rejections for the given diff.
	 * 
	 * @param diff
	 *            diff
	 * @param mergeRightToLeft
	 *            merge direction
	 * @return a non-null set of all resulting rejections
	 */
	protected Set<Diff> computeAllResultingRejections(Diff diff, boolean mergeRightToLeft) {
		return getComputer().getAllResultingRejections(diff, mergeRightToLeft);
	}

	@Override
	public Set<Diff> getAllResultingRejections(Diff diff, boolean mergeRightToLeft) {
		Set<Diff> allResultingRejections = getCachedAllResultingRejections(diff, mergeRightToLeft);
		if (allResultingRejections == null) {
			allResultingRejections = computeAllResultingRejections(diff, mergeRightToLeft);
			setCachedAllResultingRejections(diff, mergeRightToLeft, allResultingRejections);
		}
		return allResultingRejections;
	}

	/**
	 * Caches the given all resulting merges.
	 * 
	 * @param diff
	 *            diff
	 * @param mergeRightToLeft
	 *            merge direction
	 * @param allResultingMerges
	 *            all resulting merges
	 */
	protected void setCachedAllResultingMerges(Diff diff, boolean mergeRightToLeft,
			Set<Diff> allResultingMerges) {
		if (mergeRightToLeft) {
			allResultingMergesR2L.put(diff, allResultingMerges);
		} else {
			allResultingMergesL2R.put(diff, allResultingMerges);
		}
	}

	/**
	 * Returns the cached all resulting merges.
	 * 
	 * @param diff
	 *            diff
	 * @param mergeRightToLeft
	 *            merge direction
	 * @return cached all resulting merges
	 */
	protected Set<Diff> getCachedAllResultingMerges(Diff diff, boolean mergeRightToLeft) {
		if (mergeRightToLeft) {
			return allResultingMergesR2L.get(diff);
		} else {
			return allResultingMergesL2R.get(diff);
		}
	}

	/**
	 * Computes all resulting merges for the given diff.
	 * 
	 * @param diff
	 *            diff
	 * @param mergeRightToLeft
	 *            merge direction
	 * @return a non-null set of all resulting merges
	 */
	protected Set<Diff> computeAllResultingMerges(Diff diff, boolean mergeRightToLeft) {
		return getComputer().getAllResultingMerges(diff, mergeRightToLeft);
	}

	/**
	 * Returns the cached direct resulting merges, if present. Otherwise, the direct resulting merges are
	 * retrieved and cached using the given merger.
	 * 
	 * @param diff
	 *            diff
	 * @param mergeRightToLeft
	 *            merge direction
	 * @return cached direct resulting merges
	 * @see IMerger2#getDirectResultingMerges(Diff, boolean)
	 */
	@Override
	public Set<Diff> getAllResultingMerges(Diff diff, boolean mergeRightToLeft) {
		Set<Diff> allResultingMerges = getCachedAllResultingMerges(diff, mergeRightToLeft);
		if (allResultingMerges == null) {
			allResultingMerges = computeAllResultingMerges(diff, mergeRightToLeft);
			setCachedAllResultingMerges(diff, mergeRightToLeft, allResultingMerges);
		}
		return allResultingMerges;
	}

	/**
	 * Invalidates the cache for the given diff, so that relationships will be re-calculated for this diff the
	 * next time a respective method is called.
	 * 
	 * @param diff
	 *            diff for which the cache should be invalidated.
	 */
	public void invalidate(Diff diff) {
		directMergeDependenciesR2L.remove(diff);
		directMergeDependenciesL2R.remove(diff);
		directResultingRejectionsR2L.remove(diff);
		directResultingRejectionsL2R.remove(diff);
		directResultingMergesR2L.remove(diff);
		directResultingMergesL2R.remove(diff);
		allResultingMergesL2R.remove(diff);
		allResultingMergesR2L.remove(diff);
		allResultingRejectionsL2R.remove(diff);
		allResultingRejectionsR2L.remove(diff);
	}

	/**
	 * Invalidates the cache for all given diffs, so that relationships will be re-calculated for each diff
	 * the next time a respective method is called.
	 * 
	 * @param diffs
	 *            diffs for which the cache should be invalidated.
	 */
	public void invalidate(Iterable<Diff> diffs) {
		for (Diff diff : diffs) {
			invalidate(diff);
		}
	}

	/**
	 * Invalidates the complete cache, so that relationships will be re-calculated any diff the next time a
	 * respective method is called.
	 */
	public void invalidate() {
		directMergeDependenciesR2L.clear();
		directMergeDependenciesL2R.clear();
		directResultingRejectionsR2L.clear();
		directResultingRejectionsL2R.clear();
		directResultingMergesR2L.clear();
		directResultingMergesL2R.clear();
		allResultingMergesL2R.clear();
		allResultingMergesR2L.clear();
		allResultingRejectionsL2R.clear();
		allResultingRejectionsR2L.clear();
	}
}
