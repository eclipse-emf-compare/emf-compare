/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.any;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.HashSet;
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
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.utils.EMFCompareCopier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
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
	public Set<Diff> getResultingMerges(Diff diff, boolean mergeLeftToRight, Set<Diff> knownImplications) {
		final Set<Diff> relations = new LinkedHashSet<Diff>();
		relations.add(diff);
		if (mergeLeftToRight) {
			if (DifferenceSource.LEFT == diff.getSource()) {
				relations.addAll(getImpliedDiffsOf(diff.getRequires(), mergeLeftToRight, Sets.union(
						relations, knownImplications)));
				relations.addAll(getRecursiveImplies(diff));
			} else {
				relations.addAll(getImpliedDiffsOf(diff.getRequiredBy(), mergeLeftToRight, Sets.union(
						relations, knownImplications)));
				relations.addAll(getRecursiveImpliedBy(diff));
			}
		} else {
			if (DifferenceSource.LEFT == diff.getSource()) {
				relations.addAll(getImpliedDiffsOf(diff.getRequiredBy(), mergeLeftToRight, Sets.union(
						relations, knownImplications)));
				relations.addAll(getRecursiveImpliedBy(diff));
			} else {
				relations.addAll(getImpliedDiffsOf(diff.getRequires(), mergeLeftToRight, Sets.union(
						relations, knownImplications)));
				relations.addAll(getRecursiveImplies(diff));
			}
		}
		relations.addAll(getImpliedDiffsOf(diff.getRefinedBy(), mergeLeftToRight, Sets.union(relations,
				knownImplications)));
		if (diff.getEquivalence() != null) {
			relations.addAll(diff.getEquivalence().getDifferences());
		}
		if (diff.getConflict() != null && diff.getConflict().getKind() == ConflictKind.PSEUDO) {
			relations.addAll(diff.getConflict().getDifferences());
		}
		return relations;
	}

	/**
	 * Prompt the merger of each of the given diffs to tell us what will be implied by their merge. This will
	 * recursively call {@link #getImpliedDiffs(Diff, boolean)}.
	 * <p>
	 * Note that the diffs themselves are included in the returned set (i.e. a diff is considered to be
	 * implying itself as far as the merge is concerned).
	 * </p>
	 * 
	 * @param subDiffs
	 *            The diffs for which merges we need the implications.
	 * @param mergeLeftToRight
	 *            The direction in which we're considering a merge.
	 * @param knownImplications
	 *            The set of Diffs already known as being implied by our starting point. Since there may be
	 *            implication cycles, this can be used to break free. This method is not supposed to add
	 *            anything to this set.
	 * @return All of the recursive implications of merging the given differences in the given direction.
	 */
	private Set<Diff> getImpliedDiffsOf(Iterable<Diff> subDiffs, boolean mergeLeftToRight,
			Set<Diff> knownImplications) {
		final Set<Diff> relations = new LinkedHashSet<Diff>();
		for (Diff dependency : Iterables.filter(subDiffs, not(in(knownImplications)))) {
			IMerger dependencyMerger = getRegistry().getHighestRankingMerger(dependency);
			if (dependencyMerger instanceof IMerger2) {
				relations.addAll(((IMerger2)dependencyMerger).getResultingMerges(dependency, mergeLeftToRight,
						Sets.union(relations, knownImplications)));
			} else {
				relations.add(dependency);
			}
		}
		return relations;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	public Set<Diff> getResultingRejections(Diff diff, boolean mergeLeftToRight, Set<Diff> knownRejections) {
		final Set<Diff> impliedRejections = new LinkedHashSet<Diff>();

		/*
		 * Consider that we are merging "diff" from left to right. For all the diffs implied by this merge
		 * (including "diff"), we need to look at possible conflicts. For any of these diffs that does have a
		 * real conflict, we are in essence rejecting the diffs from the opposite side (say we are merging a
		 * diff detected on the left side, all diffs from the right side conflicting with it won't be
		 * mergeable once all is said and done). Transitively, a diff that was requiring one of the rejected
		 * ones will also be rejected. We do not need to check for conflicts on the rejected ones though :
		 * "rejecting" a diff in conflict means nothing for the other diffs of said conflict, except that they
		 * can now be merged on demand.
		 */
		/*
		 * "accepting" a diff is merging a diff from the DifferenceSource#LEFT side from left to right, or a
		 * diff from the DifferenceSource#RIGHT side from right to left. Rejecting a diff is ... the reverse
		 * of that.
		 */

		final Set<Diff> impliedDiffs = getResultingMerges(diff, mergeLeftToRight, Collections.<Diff> emptySet());
		for (Diff implied : impliedDiffs) {
			final Conflict conflict = implied.getConflict();
			if (conflict != null && conflict.getKind() == ConflictKind.REAL) {
				final Iterable<Diff> directlyImpliedRejections;
				if (mergeLeftToRight && implied.getSource() == DifferenceSource.LEFT) {
					directlyImpliedRejections = Iterables.filter(conflict.getDifferences(),
							fromSide(DifferenceSource.RIGHT));
				} else if (!mergeLeftToRight && implied.getSource() == DifferenceSource.RIGHT) {
					directlyImpliedRejections = Iterables.filter(conflict.getDifferences(),
							fromSide(DifferenceSource.LEFT));
				} else {
					directlyImpliedRejections = new HashSet<Diff>();
				}
				// And the diffs requiring our rejections are, in turn, rejected.
				Iterables.addAll(impliedRejections, getImpliedDiffsOf(directlyImpliedRejections,
						mergeLeftToRight, knownRejections));
			}
		}

		return impliedRejections;
	}

	/**
	 * Returns all of the differences "implied" as in {@link Diff#getImplies()} differences of the given
	 * starting point, recursively.
	 * 
	 * @param diff
	 *            The diff for which we need all recursive implications.
	 * @return all of the differences implied by the given starting point.
	 */
	private Set<Diff> getRecursiveImplies(Diff diff) {
		final Set<Diff> implies = new LinkedHashSet<Diff>();
		implies.addAll(diff.getImplies());
		for (Diff implied : diff.getImplies()) {
			implies.addAll(getRecursiveImplies(implied));
		}
		return implies;
	}

	/**
	 * Returns all of the differences "impliedBy" as in {@link Diff#getImpliedBY()} differences of the given
	 * starting point, recursively.
	 * 
	 * @param diff
	 *            The diff for which we need all recursive implying diffs.
	 * @return all of the differences implying the given starting point.
	 */
	private Set<Diff> getRecursiveImpliedBy(Diff diff) {
		final Set<Diff> implying = new LinkedHashSet<Diff>();
		implying.addAll(diff.getImpliedBy());
		for (Diff impliedBy : diff.getImpliedBy()) {
			implying.addAll(getRecursiveImpliedBy(impliedBy));
		}
		return implying;
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

		if (target.getSource() == DifferenceSource.LEFT) {
			// merge all "requires" diffs
			mergeRequires(target, false, monitor);
			handleImplies(target, false, monitor);
		} else {
			// merge all "required by" diffs
			mergeRequiredBy(target, false, monitor);
			handleImpliedBy(target, false, monitor);
		}

		for (Diff refining : target.getRefinedBy()) {
			mergeDiff(refining, false, monitor);
		}

		boolean hasToBeMerged = true;
		if (target.getEquivalence() != null) {
			hasToBeMerged = handleEquivalences(target, false, monitor);
		}

		if (hasToBeMerged) {
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

		if (target.getSource() == DifferenceSource.LEFT) {
			// merge all "required by" diffs
			mergeRequiredBy(target, true, monitor);
			handleImpliedBy(target, true, monitor);
		} else {
			// merge all "requires" diffs
			mergeRequires(target, true, monitor);
			handleImplies(target, true, monitor);
		}

		for (Diff refining : target.getRefinedBy()) {
			mergeDiff(refining, true, monitor);
		}

		boolean hasToBeMerged = true;
		if (target.getEquivalence() != null) {
			hasToBeMerged = handleEquivalences(target, true, monitor);
		}

		if (hasToBeMerged) {
			if (target.getSource() == DifferenceSource.LEFT) {
				reject(target, true);
			} else {
				accept(target, true);
			}
		}
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
	 */
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
	 */
	protected void handleImplies(Diff diff, boolean rightToLeft, Monitor monitor) {
		for (Diff implied : getRecursiveImplies(diff)) {
			implied.setState(DifferenceState.MERGED);
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
	 */
	protected void handleImpliedBy(Diff diff, boolean rightToLeft, Monitor monitor) {
		for (Diff impliedBy : getRecursiveImpliedBy(diff)) {
			impliedBy.setState(DifferenceState.MERGED);
		}
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
	 */
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
	 * Note that in certain cases, we'll merge our opposite instead of merging this diff. Specifically, we'll
	 * do that for one-to-many eOpposites : we'll merge the 'many' side instead of the 'unique' one. This
	 * allows us not to worry about the order of the references on that 'many' side.
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
	 */
	protected boolean handleEquivalences(Diff diff, boolean rightToLeft, Monitor monitor) {
		boolean continueMerge = true;
		for (Diff equivalent : diff.getEquivalence().getDifferences()) {
			// For 1..*, merge diff on many-valued to preserve ordering
			if (diff instanceof ReferenceChange && equivalent instanceof ReferenceChange) {
				final EReference reference = ((ReferenceChange)diff).getReference();
				final EReference equivalentReference = ((ReferenceChange)equivalent).getReference();

				if (reference.getEOpposite() == equivalentReference
						&& equivalent.getState() == DifferenceState.UNRESOLVED) {
					// This equivalence is on our eOpposite. Should we merge it instead of 'this'?
					final boolean mergeEquivalence = !reference.isMany()
							&& ((ReferenceChange)equivalent).getReference().isMany()
							&& isAdd((ReferenceChange)equivalent, rightToLeft);
					if (mergeEquivalence) {
						mergeDiff(equivalent, rightToLeft, monitor);
						continueMerge = false;
					}
				}
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
