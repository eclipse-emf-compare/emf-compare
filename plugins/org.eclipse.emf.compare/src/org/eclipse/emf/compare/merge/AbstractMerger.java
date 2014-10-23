/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - bug 441172
 *     Alexandra Buzila - Fixes for Bug 446252
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import static com.google.common.base.Predicates.in;
import static com.google.common.collect.Iterables.any;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.isDiffOnEOppositeOf;

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.FeatureMapChange;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.utils.EMFCompareCopier;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * Abstract implementation of an {@link IMerger}. This can be used as a base implementation to avoid
 * re-implementing the whole contract.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 3.0
 */
public abstract class AbstractMerger implements IMerger2 {
	/** Ranking of this merger. */
	private int ranking;

	/** Registry from which this merger has been created.. */
	private Registry registry;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#getRanking()
	 */
	public int getRanking() {
		return ranking;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#setRanking(int)
	 */
	public void setRanking(int r) {
		ranking = r;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#getRegistry()
	 */
	public Registry getRegistry() {
		return registry;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#setRegistry(org.eclipse.emf.compare.merge.IMerger.Registry)
	 */
	public void setRegistry(Registry registry) {
		if (this.registry != null && registry != null) {
			throw new IllegalStateException("The registry has to be set only once."); //$NON-NLS-1$
		}
		this.registry = registry;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	public Set<Diff> getDirectMergeDependencies(Diff diff, boolean mergeRightToLeft) {
		final Set<Diff> dependencies = new LinkedHashSet<Diff>();
		if (mergeRightToLeft) {
			if (DifferenceSource.LEFT == diff.getSource()) {
				dependencies.addAll(diff.getImplies());
				dependencies.addAll(diff.getRequiredBy());
			} else {
				dependencies.addAll(diff.getImpliedBy());
				dependencies.addAll(diff.getRequires());
			}
		} else {
			if (DifferenceSource.LEFT == diff.getSource()) {
				dependencies.addAll(diff.getImpliedBy());
				dependencies.addAll(diff.getRequires());
			} else {
				dependencies.addAll(diff.getImplies());
				dependencies.addAll(diff.getRequiredBy());
			}
		}
		dependencies.addAll(diff.getRefinedBy());
		if (diff.getEquivalence() != null) {
			final Diff masterEquivalence = findMasterEquivalence(diff, mergeRightToLeft);
			if (masterEquivalence != null && masterEquivalence != diff) {
				dependencies.add(masterEquivalence);
			}
		}
		return dependencies;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	public Set<Diff> getDirectResultingMerges(Diff target, boolean mergeRightToLeft) {
		final Set<Diff> resulting = new LinkedHashSet<Diff>();
		if (mergeRightToLeft) {
			if (DifferenceSource.LEFT == target.getSource()) {
				resulting.addAll(target.getImpliedBy());
			} else {
				resulting.addAll(target.getImplies());
			}
		} else {
			if (DifferenceSource.LEFT == target.getSource()) {
				resulting.addAll(target.getImplies());
			} else {
				resulting.addAll(target.getImpliedBy());
			}
		}
		if (target.getEquivalence() != null) {
			resulting.addAll(target.getEquivalence().getDifferences());
			resulting.remove(target);
		}
		if (target.getConflict() != null && target.getConflict().getKind() == ConflictKind.PSEUDO) {
			resulting.addAll(target.getConflict().getDifferences());
			resulting.remove(target);
		}
		return resulting;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	public Set<Diff> getDirectResultingRejections(Diff target, boolean mergeRightToLeft) {
		final Set<Diff> directlyImpliedRejections = new LinkedHashSet<Diff>();
		final Conflict conflict = target.getConflict();
		if (conflict != null && conflict.getKind() == ConflictKind.REAL) {
			if (mergeRightToLeft && target.getSource() == DifferenceSource.RIGHT) {
				Iterables.addAll(directlyImpliedRejections, Iterables.filter(conflict.getDifferences(),
						fromSide(DifferenceSource.LEFT)));
			} else if (!mergeRightToLeft && target.getSource() == DifferenceSource.LEFT) {
				Iterables.addAll(directlyImpliedRejections, Iterables.filter(conflict.getDifferences(),
						fromSide(DifferenceSource.RIGHT)));
			}
		}
		return directlyImpliedRejections;
	}

	/**
	 * Even within 'equivalent' differences, there might be one that we need to consider as the "master", one
	 * part of the equivalence that should take precedence over the others when merging.
	 * <p>
	 * There are four main cases in which this happens :
	 * <ol>
	 * <li>Equivalent differences regarding two "eOpposite" sides, with one side being a single-valued
	 * reference while the other side is a multi-valued reference (one-to-many). In such a case, we need the
	 * 'many' side of that equivalence to be merged over the 'single' side, so as to avoid potential ordering
	 * issues. Additionally, to avoid losing information, equivalent differences with
	 * {@link DifferenceKind.ADD} instead of {@link DifferenceKind.REMOVE} must be merged first.</li>
	 * <li>Equivalent differences regarding two "eOpposite" sides, with both sides being a single-valued
	 * reference (one-to-one). In such a case, we need to merge the difference that results in setting a
	 * feature value over the difference unsetting a feature. This is needed to prevent information loss.</li>
	 * <li>Equivalent differences with conflicts: basically, if one of the diffs of an equivalence relation is
	 * in conflict while the others are not, then none of the equivalent differences can be automatically
	 * merged. We need to consider the conflict to be taking precedence over the others to make sure that the
	 * conflict is resolved before even trying to merge anything.</li>
	 * <li>Equivalent {@link ReferenceChange} and {@link FeatureMapChange} differences: in this case the
	 * {@link FeatureMapChange} difference will take precedence over the {@link ReferenceChange}. This happens
	 * in order to prevent special cases in which the {@link ReferenceChangeMerger} cannot ensure the correct	 
	 * order of the feature map attribute.</li>
	 * </ol>
	 * </p>
	 * 
	 * @param diff
	 *            The diff we need to check the equivalence for a 'master' difference.
	 * @param mergeRightToLeft
	 *            Direction of the merge operation.
	 * @return The master difference of this equivalence relation. May be <code>null</code> if there are none.
	 */
	private Diff findMasterEquivalence(Diff diff, boolean mergeRightToLeft) {
		final Diff masterDiff;
		final List<Diff> equivalentDiffs = diff.getEquivalence().getDifferences();
		final Optional<Diff> firstConflicting = Iterables.tryFind(equivalentDiffs,
				hasConflict(ConflictKind.REAL));
		if (firstConflicting.isPresent()) {
			masterDiff = firstConflicting.get();
		} else if (diff instanceof ReferenceChange) {
			final ReferenceChange referenceChange = (ReferenceChange)diff;
			masterDiff = getMasterEquivalenceForReferenceChange(referenceChange, mergeRightToLeft);
		} else {
			masterDiff = null;
		}
		return masterDiff;
	}

	/**
	 * Returns the master equivalence for a {@link ReferenceChange}.
	 * 
	 * @see AbstractMerger#findMasterEquivalence(Diff, boolean)
	 * @param diff
	 *            The {@link Diff} we need to check the equivalence for a 'master' difference.
	 * @param mergeRightToLeft
	 *            Direction of the current merging.
	 * @return The master difference of {@code diff} and its equivalent diffs. This method may return <code>null</code> if there is no master diff.
	 */
	private Diff getMasterEquivalenceForReferenceChange(ReferenceChange diff, boolean mergeRightToLeft) {
		Diff masterDiff = getMasterEquivalenceOnEOpposite(diff, mergeRightToLeft);
		if (masterDiff == null) {
			masterDiff = getMasterEquivalenceFeatureMap(diff);
		}
		return masterDiff;
	}

	/**
	 * Returns the master equivalence for a {@link ReferenceChange} from among its {@code eOpposites}.
	 * 
	 * @see AbstractMerger#findMasterEquivalence(Diff, boolean)
	 * @param diff
	 *            The {@link Diff} we need to check the equivalence for a 'master' difference.
	 * @param mergeRightToLeft
	 *            Direction of the current merging.
	 * @return The master difference of {@code diff} and its equivalent diffs. This method may return
	 *         <code>null</code> if there is no master diff.
	 */
	private Diff getMasterEquivalenceOnEOpposite(ReferenceChange diff, boolean mergeRightToLeft) {
		Diff masterDiff = null;
		final Iterator<Diff> equivalentDiffs = diff.getEquivalence().getDifferences().iterator();
		final Iterator<Diff> eOppositeDiffs = Iterators.filter(equivalentDiffs, isDiffOnEOppositeOf(diff));
		while (masterDiff == null && eOppositeDiffs.hasNext()) {
			final ReferenceChange candidate = (ReferenceChange)eOppositeDiffs.next();
			if (isOneToManyAndAdd(diff, candidate, mergeRightToLeft)) {
				masterDiff = candidate;
			} else if (!isSet(diff, mergeRightToLeft) && isOneToOneAndSet(diff, candidate, mergeRightToLeft)) {
				masterDiff = candidate;
			}
		}
		return masterDiff;
	}

	/**
	 * Returns the master equivalence of type {@link FeatureMapChange}, for a {@link ReferenceChange}.
	 * 
	 * @see AbstractMerger#findMasterEquivalence(Diff, boolean)
	 * @param diff
	 *            The {@link Diff} we need to check the equivalence for a 'master' difference.
	 * @return The master difference of {@code diff} and its equivalent diffs. This method may return
	 *         <code>null</code> if there is no master diff.
	 */
	private Diff getMasterEquivalenceFeatureMap(ReferenceChange diff) {
		return Iterators.tryFind(diff.getEquivalence().getDifferences().iterator(),
				Predicates.instanceOf(FeatureMapChange.class)).orNull();
	}

	/**
	 * Specifies whether the given reference changes, {@code diff} and {@code equivalent}, affect references
	 * constituting an one-to-many relationship and whether {@code equivalent} is an addition in the current
	 * merging.
	 *
	 * @param diff
	 *            The difference to check. One-side of the relation.
	 * @param equivalent
	 *            The equivalent to the {@code diff}. Many-side of the relation.
	 * @param mergeRightToLeft
	 *            Direction of the merge.
	 * @return <code>true</code> if {@code diff} and {@code equivalent} are one-to-many eOpposites with
	 *         {@code equivalent} resulting in an Add-operation, <code>false</code> otherwise.
	 */
	private boolean isOneToManyAndAdd(ReferenceChange diff, ReferenceChange equivalent,
			boolean mergeRightToLeft) {
		return !diff.getReference().isMany() && equivalent.getReference().isMany()
				&& isAdd(equivalent, mergeRightToLeft);
	}

	/**
	 * Specifies whether the given reference changes, {@code diff} and {@code equivalent}, affect references
	 * constituting an one-to-one relationship and whether {@code equivalent} is a set in the current merging.
	 *
	 * @param diff
	 *            The difference to check.
	 * @param equivalent
	 *            The equivalent to the {@code diff}.
	 * @param mergeRightToLeft
	 *            Direction of the merge.
	 * @return <code>true</code> if {@code diff} and {@code equivalent} are one-to-many eOpposites with
	 *         {@code equivalent} resulting in an Add-operation, <code>false</code> otherwise.
	 */
	private boolean isOneToOneAndSet(ReferenceChange diff, ReferenceChange equivalent,
			boolean mergeRightToLeft) {
		return !diff.getReference().isMany() && !equivalent.getReference().isMany()
				&& isSet(equivalent, mergeRightToLeft);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#copyLeftToRight(org.eclipse.emf.compare.Diff,
	 *      org.eclipse.emf.common.util.Monitor)
	 * @since 3.1
	 */
	public void copyLeftToRight(Diff target, Monitor monitor) {
		// Don't merge an already merged (or discarded) diff
		if (target.getState() != DifferenceState.UNRESOLVED) {
			return;
		}

		// Change the diff's state before we actually merge it : this allows us to avoid requirement cycles.
		target.setState(DifferenceState.MERGED);

		final Set<Diff> dependencies = getDirectMergeDependencies(target, false);

		// We'll redo some of the work from getDirectMergeDependencies here in order to ensure we haven't been
		// merged by another diff (equivalence or implication).
		// requiresMerging must be executed before actually merging the dependencies because
		// findMasterEquivalence may return a different result after merging.
		boolean requiresMerging = requiresMerging(target, false);

		for (Diff mergeMe : dependencies) {
			mergeDiff(mergeMe, false, monitor);
		}

		for (Diff transitiveMerge : getDirectResultingMerges(target, false)) {
			transitiveMerge.setState(DifferenceState.MERGED);
		}

		if (requiresMerging) {
			if (target.getSource() == DifferenceSource.LEFT) {
				accept(target, false);
			} else {
				reject(target, false);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#copyRightToLeft(org.eclipse.emf.compare.Diff,
	 *      org.eclipse.emf.common.util.Monitor)
	 * @since 3.1
	 */
	public void copyRightToLeft(Diff target, Monitor monitor) {
		// Don't merge an already merged (or discarded) diff
		if (target.getState() != DifferenceState.UNRESOLVED) {
			return;
		}

		// Change the diff's state before we actually merge it : this allows us to avoid requirement cycles.
		target.setState(DifferenceState.MERGED);

		final Set<Diff> dependencies = getDirectMergeDependencies(target, true);

		// We'll redo some of the work from getDirectMergeDependencies here in order to ensure we haven't been
		// merged by another diff (equivalence or implication).
		// requiresMerging must be executed before actually merging the dependencies because
		// findMasterEquivalence may return a different result after merging.
		boolean requiresMerging = requiresMerging(target, true);

		for (Diff mergeMe : dependencies) {
			mergeDiff(mergeMe, true, monitor);
		}

		for (Diff transitiveMerge : getDirectResultingMerges(target, true)) {
			transitiveMerge.setState(DifferenceState.MERGED);
		}

		if (requiresMerging) {
			if (target.getSource() == DifferenceSource.LEFT) {
				reject(target, true);
			} else {
				accept(target, true);
			}
		}
	}

	/**
	 * Checks whether the given diff still needs to be merged or if it has been merged because of an
	 * implication or 'master' equivalence.
	 * 
	 * @param target
	 *            The difference we are considering merging.
	 * @param mergeRightToLeft
	 *            The direction in which we're considering a merge.
	 * @return <code>true</code> if the <code>target</code> diff hasn't been merged yet and requires handling
	 *         of its own.
	 */
	private boolean requiresMerging(Diff target, boolean mergeRightToLeft) {
		boolean requiresMerging = true;
		if (isImpliedMerge(target, mergeRightToLeft)) {
			// first, if we are implied by something, then we're already merged
			requiresMerging = false;
		} else if (target.getEquivalence() != null) {
			final Diff masterEquivalence = findMasterEquivalence(target, mergeRightToLeft);
			if (masterEquivalence != null && masterEquivalence != target) {
				// If we have a "master" equivalence (see doc on findMasterEquivalence) then we've been merged
				// along with it
				requiresMerging = false;
			} else {
				// We also need to check for implications on our equivalence (dependency loops)
				requiresMerging = !hasTransitiveImplicationBeenMerged(target, mergeRightToLeft);
			}
		}
		return requiresMerging;
	}

	/**
	 * Checks if the given diff is implied by another in the given merge direction, which means that it
	 * doesn't need to be merged individually.
	 * 
	 * @param target
	 *            The diff we're considering merging.
	 * @param mergeRightToLeft
	 *            The direction in which we're currently merging.
	 * @return <code>true</code> if the given diff will be implicitely merged by another in that direction.
	 */
	private boolean isImpliedMerge(Diff target, boolean mergeRightToLeft) {
		final boolean isImpliedForDirection;
		if (mergeRightToLeft) {
			if (DifferenceSource.LEFT == target.getSource()) {
				isImpliedForDirection = !target.getImplies().isEmpty();
			} else {
				isImpliedForDirection = !target.getImpliedBy().isEmpty();
			}
		} else {
			if (DifferenceSource.LEFT == target.getSource()) {
				isImpliedForDirection = !target.getImpliedBy().isEmpty();
			} else {
				isImpliedForDirection = !target.getImplies().isEmpty();
			}
		}
		return isImpliedForDirection;
	}

	// FIXME find a use case and check whether this is still required.
	/**
	 * Checks whether the given diff has been merged through a dependency cycle on its equivalence relations
	 * (this diff requires the merging of a diff that implies one of its equivalences).
	 * <p>
	 * This should only be called on differences that have equivalences.
	 * </p>
	 * 
	 * @param target
	 *            The difference we are considering merging.
	 * @param mergeRightToLeft
	 *            The direction in which we're considering a merge.
	 * @return <code>true</code> if the <code>target</code> diff has already been merged.
	 */
	private boolean hasTransitiveImplicationBeenMerged(Diff target, boolean mergeRightToLeft) {
		boolean mergedThroughEquivalentImplication = false;
		final Iterator<Diff> equivalenceIterator = target.getEquivalence().getDifferences().iterator();
		while (!mergedThroughEquivalentImplication && equivalenceIterator.hasNext()) {
			final Diff equivalent = equivalenceIterator.next();
			if (equivalent != target && mergeRightToLeft) {
				if (target.getSource() == DifferenceSource.LEFT) {
					mergedThroughEquivalentImplication = any(equivalent.getImplies(), in(target
							.getRequiredBy()));
				} else {
					mergedThroughEquivalentImplication = any(equivalent.getImpliedBy(), in(target
							.getRequires()));
				}
			} else if (equivalent != target) {
				if (target.getSource() == DifferenceSource.LEFT) {
					mergedThroughEquivalentImplication = any(equivalent.getImpliedBy(), in(target
							.getRequires()));
				} else {
					mergedThroughEquivalentImplication = any(equivalent.getImplies(), in(target
							.getRequiredBy()));
				}
			}
		}
		return mergedThroughEquivalentImplication;
	}

	/**
	 * Accept the given difference. This may be overridden by clients.
	 * 
	 * @param diff
	 *            the difference to merge
	 * @param rightToLeft
	 *            the direction of the merge
	 * @since 3.1
	 */
	protected void accept(final Diff diff, boolean rightToLeft) {
		// Empty default implementation
	}

	/**
	 * Reject the given difference. This may be overridden by clients.
	 * 
	 * @param diff
	 *            the difference to merge
	 * @param rightToLeft
	 *            the direction of the merge
	 * @since 3.1
	 */
	protected void reject(final Diff diff, boolean rightToLeft) {
		// Empty default implementation
	}

	/**
	 * This will merge all {@link Diff#getRequiredBy() differences that require} {@code diff} in the given
	 * direction.
	 * 
	 * @param diff
	 *            We need to merge all differences that require this one (see {@link Diff#getRequiredBy()}.
	 * @param rightToLeft
	 *            If {@code true}, {@link #copyRightToLeft(Diff, Monitor) apply} all differences that require
	 *            {@code diff}. Otherwise, {@link #copyLeftToRight(Diff, Monitor) revert} them.
	 * @param monitor
	 *            The monitor we should use to report progress.
	 * @Deprecated
	 */
	@Deprecated
	protected void mergeRequiredBy(Diff diff, boolean rightToLeft, Monitor monitor) {
		// TODO log back to the user what we will merge along?
		for (Diff dependency : diff.getRequiredBy()) {
			// TODO: what to do when state = Discarded but is required?
			mergeDiff(dependency, rightToLeft, monitor);
		}
	}

	/**
	 * Mark as {@link DifferenceState#MERGED merged} all the implied differences recursively from the given
	 * one.
	 * 
	 * @param diff
	 *            The difference from which the implications have to be marked.
	 * @param rightToLeft
	 *            The direction of the merge.
	 * @param monitor
	 *            Monitor.
	 * @since 3.1
	 * @deprecated
	 */
	@Deprecated
	protected void handleImplies(Diff diff, boolean rightToLeft, Monitor monitor) {
		for (Diff implied : diff.getImplies()) {
			implied.setState(DifferenceState.MERGED);
			handleImplies(implied, rightToLeft, monitor);
		}
	}

	/**
	 * Mark as {@link DifferenceState#MERGED merged} all the implying differences recursively from the given
	 * one.
	 * 
	 * @param diff
	 *            The difference from which the implications have to be marked.
	 * @param rightToLeft
	 *            The direction of the merge.
	 * @param monitor
	 *            Monitor.
	 * @since 3.1
	 * @deprecated
	 */
	@Deprecated
	protected void handleImpliedBy(Diff diff, boolean rightToLeft, Monitor monitor) {
		// Do nothing, this was an implementation error
	}

	/**
	 * This will merge all {@link Diff#getRequires() differences required by} {@code diff} in the given
	 * direction.
	 * 
	 * @param diff
	 *            The difference which requirements we need to merge.
	 * @param rightToLeft
	 *            If {@code true}, {@link #copyRightToLeft(Diff, Monitor) apply} all required differences.
	 *            Otherwise, {@link #copyLeftToRight(Diff, Monitor) revert} them.
	 * @param monitor
	 *            The monitor we should use to report progress.
	 * @deprecated
	 */
	@Deprecated
	protected void mergeRequires(Diff diff, boolean rightToLeft, Monitor monitor) {
		// TODO log back to the user what we will merge along?
		for (Diff dependency : diff.getRequires()) {
			// TODO: what to do when state = Discarded but is required?
			mergeDiff(dependency, rightToLeft, monitor);
		}
	}

	/**
	 * This can be used by mergers to merge another (required, equivalent...) difference using the right
	 * merger for that diff.
	 * 
	 * @param diff
	 *            The diff we need to merge.
	 * @param rightToLeft
	 *            Direction of that merge.
	 * @param monitor
	 *            The monitor we should use to report progress.
	 */
	protected void mergeDiff(Diff diff, boolean rightToLeft, Monitor monitor) {
		if (rightToLeft) {
			final IMerger delegate = getRegistry().getHighestRankingMerger(diff);
			delegate.copyRightToLeft(diff, monitor);
		} else {
			final IMerger delegate = getRegistry().getHighestRankingMerger(diff);
			delegate.copyLeftToRight(diff, monitor);
		}
	}

	/**
	 * Handles the equivalences of this difference.
	 * <p>
	 * Note that in certain cases, we'll merge our opposite instead of merging this diff. This is done to
	 * avoid merge orders where merging differences of kind {@link DifferenceKind.REMOVE} or
	 * {@link DifferenceKind.CHANGE} (resulting in an unset) first can lead to information loss. Additionally
	 * in case of one-to-many eOpposites this allows us not to worry about the order of the references on the
	 * 'many' side.
	 * </p>
	 * <p>
	 * This is called before the merge of <code>this</code>. In short, if this returns <code>false</code>, we
	 * won't carry on merging <code>this</code> after returning.
	 * </p>
	 * 
	 * @param diff
	 *            The diff we are currently merging.
	 * @param rightToLeft
	 *            Direction of the merge.
	 * @param monitor
	 *            The monitor to use in order to report progress information.
	 * @return <code>true</code> if the current difference should still be merged after handling its
	 *         equivalences, <code>false</code> if it should be considered "already merged".
	 * @since 3.1
	 * @Deprecated
	 */
	@Deprecated
	protected boolean handleEquivalences(Diff diff, boolean rightToLeft, Monitor monitor) {
		boolean continueMerge = true;
		for (Diff equivalent : diff.getEquivalence().getDifferences()) {

			if (diff instanceof ReferenceChange && equivalent instanceof ReferenceChange) {

				final ReferenceChange diffRC = (ReferenceChange)diff;
				final ReferenceChange equivalentRC = (ReferenceChange)equivalent;

				if (diffRC.getReference().getEOpposite() == equivalentRC.getReference()
						&& equivalent.getState() == DifferenceState.UNRESOLVED) {

					// This equivalence is on our eOpposite. Should we merge it instead of 'this'?
					final boolean mergeEquivalence = isOneToManyAndAdd(diffRC, equivalentRC, rightToLeft)
							|| isOneToOneAndSet(diffRC, equivalentRC, rightToLeft);

					if (mergeEquivalence) {
						mergeDiff(equivalent, rightToLeft, monitor);
						continueMerge = false;
					}
				}
			}

			// in the case of a ReferenceChange-FeatureMapChange equivalence, the FeatureMapChange is
			// preferred to the ReferenceChange - cf. Bug 446252
			if (diff instanceof ReferenceChange && equivalent instanceof FeatureMapChange) {
				mergeDiff(equivalent, rightToLeft, monitor);
				continueMerge = false;
			}

			/*
			 * If one of the equivalent differences is implied or implying (depending on the merge direction)
			 * a merged diff, then we have a dependency loop : the "current" difference has already been
			 * merged because of this implication. This will allow us to break out of that loop.
			 */
			if (rightToLeft) {
				if (diff.getSource() == DifferenceSource.LEFT) {
					continueMerge = continueMerge && !any(equivalent.getImplies(), in(diff.getRequiredBy()));
				} else {
					continueMerge = continueMerge && !any(equivalent.getImpliedBy(), in(diff.getRequires()));
				}
			} else {
				if (diff.getSource() == DifferenceSource.LEFT) {
					continueMerge = continueMerge && !any(equivalent.getImpliedBy(), in(diff.getRequires()));
				} else {
					continueMerge = continueMerge && !any(equivalent.getImplies(), in(diff.getRequiredBy()));
				}
			}

			equivalent.setState(DifferenceState.MERGED);
		}
		return continueMerge;
	}

	/**
	 * Specifies whether the given {@code diff} will add a value in the target model for the current merging.
	 * <p>
	 * To check whether the {@code diff} is an addition, we have to check the direction of the merge,
	 * specified in {@code rightToLeft} and the {@link Diff#getSource() source of the diff}. Therefore, this
	 * method delegates to {@link #isLeftAddOrRightDelete(ReferenceChange)} and
	 * {@link #isLeftDeleteOrRightAdd(ReferenceChange)}.
	 * </p>
	 * 
	 * @param diff
	 *            The difference to check.
	 * @param rightToLeft
	 *            Direction of the merge.
	 * @return <code>true</code> if {@code diff} will add a value with this merge, <code>false</code>
	 *         otherwise.
	 * @since 3.1
	 */
	protected boolean isAdd(ReferenceChange diff, boolean rightToLeft) {
		if (rightToLeft) {
			return isLeftDeleteOrRightAdd(diff);
		} else {
			return isLeftAddOrRightDelete(diff);
		}
	}

	/**
	 * Specifies whether the given {@code diff} is either an addition on the left-hand side or a deletion on
	 * the right-hand side.
	 * 
	 * @param diff
	 *            The difference to check.
	 * @return <code>true</code> if it is a left addition or a right deletion.
	 */
	private boolean isLeftAddOrRightDelete(ReferenceChange diff) {
		if (diff.getSource() == DifferenceSource.LEFT) {
			return diff.getKind() == DifferenceKind.ADD;
		} else {
			return diff.getKind() == DifferenceKind.DELETE;
		}
	}

	/**
	 * Specifies whether the given {@code diff} is either a deletion on the left-hand side or an addition on
	 * the right-hand side.
	 * 
	 * @param diff
	 *            The difference to check.
	 * @return <code>true</code> if it is a left deletion or a right addition.
	 */
	private boolean isLeftDeleteOrRightAdd(ReferenceChange diff) {
		if (diff.getSource() == DifferenceSource.LEFT) {
			return diff.getKind() == DifferenceKind.DELETE;
		} else {
			return diff.getKind() == DifferenceKind.ADD;
		}
	}

	/**
	 * Specifies whether the given {@code diff} will set a value in the target model for the current merging.
	 * <p>
	 * To check whether the {@code diff} is a set, we have to check the direction of the merge, specified in
	 * {@code rightToLeft} and the {@link Diff#getSource() source of the diff}. Therefore, this method
	 * delegates to {@link #isLeftSetOrRightUnset(ReferenceChange)} and
	 * {@link #isLeftUnsetOrRightSet(ReferenceChange)}.
	 * </p>
	 *
	 * @param diff
	 *            The difference to check.
	 * @param mergeRightToLeft
	 *            Direction of the merge.
	 * @return <code>true</code> if {@code diff} will set a value with this merge, <code>false</code> if it
	 *         will unset.
	 */
	private boolean isSet(ReferenceChange diff, boolean mergeRightToLeft) {
		if (diff.getKind() != DifferenceKind.CHANGE) {
			return false;
		}

		boolean isSet = false;
		final Match match = diff.getMatch();
		final EObject container;
		if (diff.getSource() == DifferenceSource.LEFT) {
			container = match.getLeft();
		} else {
			container = match.getRight();
		}

		if (container == null) {
			// This is an unset diff. However, if we're merging towards the source, we're actually "rejecting"
			// the unset, and the merge operation will be a "set"
			isSet = isRejecting(diff, mergeRightToLeft);
		} else {
			if (!ReferenceUtil.safeEIsSet(container, diff.getReference())) {
				// No value on the source side, this is an unset
				// Same case as above, if we are rejecting the diff, it is a "set" operation
				isSet = isRejecting(diff, mergeRightToLeft);
			} else {
				// The feature is set on the source side. If we're merging towards the other side, it can only
				// be a "set" operation.
				// Otherwise we're going to reset this reference to its previous value. That will end as an
				// "unset" if the "previous value" is unset itself.
				if (isRejecting(diff, mergeRightToLeft)) {
					final EObject originContainer;
					if (match.getComparison().isThreeWay()) {
						originContainer = match.getOrigin();
					} else if (mergeRightToLeft) {
						originContainer = match.getRight();
					} else {
						originContainer = match.getLeft();
					}

					isSet = originContainer != null
							&& ReferenceUtil.safeEIsSet(originContainer, diff.getReference());
				} else {
					isSet = true;
				}
			}
		}

		return isSet;
	}

	/**
	 * Checks whether the given merge direction will result in rejecting this difference.
	 *
	 * @param diff
	 *            The difference we're merging.
	 * @param mergeRightToLeft
	 *            Direction of the merge operation.
	 * @return <code>true</code> if we're rejecting this diff.
	 */
	private boolean isRejecting(Diff diff, boolean mergeRightToLeft) {
		if (diff.getSource() == DifferenceSource.LEFT) {
			return mergeRightToLeft;
		} else {
			return !mergeRightToLeft;
		}
	}

	/**
	 * This will create a copy of the given EObject that can be used as the target of an addition (or the
	 * reverting of a deletion).
	 * <p>
	 * The target will be self-contained and will have no reference towards any other EObject set (neither
	 * containment nor "classic" references). All of its attributes' values will match the given
	 * {@code referenceObject}'s.
	 * </p>
	 * 
	 * @param referenceObject
	 *            The EObject for which we'll create a copy.
	 * @return A self-contained copy of {@code referenceObject}.
	 * @see EMFCompareCopier#copy(EObject)
	 */
	protected EObject createCopy(EObject referenceObject) {
		/*
		 * We can't simply use EcoreUtil.copy. References will have their own diffs and will thus be merged
		 * later on.
		 */
		final EcoreUtil.Copier copier = new EMFCompareCopier();
		return copier.copy(referenceObject);
	}

	/**
	 * Adds the given {@code value} into the given {@code list} at the given {@code index}. An {@code index}
	 * under than zero or above the list's size will mean that the value should be appended at the end of the
	 * list.
	 * 
	 * @param list
	 *            The list into which {@code value} should be added.
	 * @param value
	 *            The value we need to add to {@code list}.
	 * @param <E>
	 *            Type of objects contained in the list.
	 * @param insertionIndex
	 *            The index at which {@code value} should be inserted into {@code list}. {@code -1} if it
	 *            should be appended at the end of the list.
	 */
	@SuppressWarnings("unchecked")
	protected <E> void addAt(List<E> list, E value, int insertionIndex) {
		if (list instanceof InternalEList<?>) {
			if (insertionIndex < 0 || insertionIndex > list.size()) {
				((InternalEList<Object>)list).addUnique(value);
			} else {
				((InternalEList<Object>)list).addUnique(insertionIndex, value);
			}
		} else {
			if (insertionIndex < 0 || insertionIndex > list.size()) {
				list.add(value);
			} else {
				list.add(insertionIndex, value);
			}
		}
	}
}
