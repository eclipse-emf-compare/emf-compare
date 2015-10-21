/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - bugs 441172, 452147 and 454579
 *     Alexandra Buzila - Fixes for Bug 446252
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Iterables.addAll;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.transform;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasSameReferenceAs;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.isDiffOnEOppositeOf;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.FeatureMapChange;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.utils.EMFCompareCopier;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * Abstract implementation of an {@link IMerger}. This can be used as a base implementation to avoid
 * re-implementing the whole contract.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 3.0
 */
public abstract class AbstractMerger implements IMerger2, IMergeOptionAware {

	/** The key of the merge option that allows to the mergers to consider sub-diffs of a diff as a whole. */
	public static final String SUB_DIFF_AWARE_OPTION = "subDiffAwareOption"; //$NON-NLS-1$

	/** Ranking of this merger. */
	private int ranking;

	/** Registry from which this merger has been created. */
	private Registry registry;

	/** The map of all merge options that this merger should be aware of. */
	private Map<Object, Object> mergeOptions;

	/**
	 * Default constructor.
	 */
	public AbstractMerger() {
		this.mergeOptions = Maps.newHashMap();
	}

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
	 * @see org.eclipse.emf.compare.merge.IMergeOptionAware#getMergeOptions()
	 */
	public Map<Object, Object> getMergeOptions() {
		return this.mergeOptions;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMergeOptionAware#setMergeOptions(java.util.Map)
	 * @since 3.3
	 */
	public void setMergeOptions(Map<Object, Object> options) {
		this.mergeOptions = options;
	}

	/**
	 * Check the SUB_DIFF_AWARE_OPTION state.
	 * 
	 * @return true if the SUB_DIFF_AWARE_OPTION of the merge options is set to true, false otherwise.
	 */
	private boolean isHandleSubDiffs() {
		if (this.mergeOptions != null) {
			Object subDiffs = this.mergeOptions.get(SUB_DIFF_AWARE_OPTION);
			return subDiffs == Boolean.TRUE;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
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

		if (isHandleSubDiffs()) {
			final Set<Diff> dependenciesToMerge = Sets.newHashSet(dependencies);
			Iterable<Diff> subDiffs = concat(transform(dependenciesToMerge, ComparisonUtil
					.getSubDiffs(!mergeRightToLeft)));
			addAll(dependencies, subDiffs);
		}
		return dependencies;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
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

		// If a diff refines another, we have to check if the "macro" diff has to be merged with it. It is the
		// case when the unresolved diffs that refine the "macro" diff are all contained by the set
		// (target + resulting) (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=458961)
		for (Diff refine : target.getRefines()) {
			Set<Diff> tmp = Sets.newHashSet(resulting);
			tmp.add(target);
			Collection<Diff> unresolvedRefinedDiffs = Collections2.filter(refine.getRefinedBy(),
					EMFComparePredicates.hasState(DifferenceState.UNRESOLVED));
			if (tmp.containsAll(unresolvedRefinedDiffs)) {
				resulting.add(refine);
			}
		}

		// Bug 452147:
		// Add interlocked differences to the resulting merges to avoid merging redundant differences with
		// undefined consequences.
		if (target instanceof ReferenceChange) {
			final ReferenceChange refTarget = (ReferenceChange)target;
			if (isOneToOneAndChange(refTarget)) {
				resulting.addAll(findInterlockedOneToOneDiffs(refTarget, mergeRightToLeft));
			}
		}

		if (isHandleSubDiffs()) {
			final Set<Diff> resultingToMerge = Sets.newHashSet(resulting);
			Iterable<Diff> subDiffs = concat(transform(resultingToMerge, ComparisonUtil
					.getSubDiffs(!mergeRightToLeft)));
			addAll(resulting, subDiffs);
		}
		return resulting;

	}

	/**
	 * Interlocked differences only occur in special cases: When both ends of a one-to-one feature have the
	 * same type and are actually set to the container object in an instance model.
	 * <p>
	 * For each end of the feature usually two differences are determined: Setting the feature in object A and
	 * in object B. Each pair of differences is equivalent. But when the value of the feature is set to its
	 * containing object, those differences may ALL act as equivalent depending on the merge direction.
	 * <p>
	 * These interlocked differences are therefore indirectly equivalent and need special treatment to avoid
	 * merging the same effects twice. These differences are determined by this method.
	 * 
	 * @see <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=452147">Bugzilla #452147</a> for more
	 *      information.
	 * @param referenceChange
	 *            The diff for which interlocked differences are determined.
	 * @param mergeRightToLeft
	 *            The direction in which we're considering a merge.
	 * @return All interlocked differences in regards to the given {@code referenceChange} and
	 *         {@code mergeDirection}.
	 */
	private Collection<? extends Diff> findInterlockedOneToOneDiffs(ReferenceChange referenceChange,
			boolean mergeRightToLeft) {
		final boolean sanityChecks = referenceChange.getKind() != DifferenceKind.CHANGE
				|| referenceChange.getReference().isMany()
				|| referenceChange.getReference().getEOpposite().isMany();

		// check if value to be set is the container itself
		final EObject sourceContainer = ComparisonUtil.getExpectedSide(referenceChange.getMatch(),
				referenceChange.getSource(), mergeRightToLeft);

		if (!sanityChecks && sourceContainer != null) {
			final Object sourceValue = ReferenceUtil
					.safeEGet(sourceContainer, referenceChange.getReference());

			if (sourceValue == sourceContainer) {
				// collect all diffs which might be "equal"
				final Match match = referenceChange.getMatch();
				final Set<Diff> candidates = new LinkedHashSet<Diff>();
				for (Diff diff : match.getDifferences()) {
					candidates.add(diff);
					if (diff.getEquivalence() != null) {
						candidates.addAll(diff.getEquivalence().getDifferences());
					}
				}

				// special case - check for interlocked diffs and return them as result
				return filterInterlockedOneToOneDiffs(candidates, referenceChange, mergeRightToLeft);
			}
		}

		return Collections.emptyList();
	}

	/**
	 * Checks for interlocked differences from a list of candidates. See
	 * {@link #findInterlockedOneToOneDiffs(ReferenceChange, boolean)} for more information.
	 *
	 * @param diffsToCheck
	 *            The differences to be checked for indirect equivalence.
	 * @param referenceChange
	 *            The diff to which the determined differences are indirectly equivalent.
	 * @param mergeRightToLeft
	 *            The direction in which we're considering a merge.
	 * @return All differences (and their equivalents) from {@code diffsToCheck} which are indirectly
	 *         equivalent to {@code referenceChange}. Does not modify the given collection.
	 */
	private Collection<? extends Diff> filterInterlockedOneToOneDiffs(
			Collection<? extends Diff> diffsToCheck, ReferenceChange referenceChange, boolean mergeRightToLeft) {

		final Object sourceContainer = ComparisonUtil.getExpectedSide(referenceChange.getMatch(),
				referenceChange.getSource(), mergeRightToLeft);
		final EReference sourceReference = referenceChange.getReference();

		final Set<Diff> result = new LinkedHashSet<Diff>();

		for (Diff candidate : diffsToCheck) {
			if (candidate instanceof ReferenceChange) {
				// check if container & reference(-opposite) are the same as from the given referenceChange
				final Object candidateContainer = ComparisonUtil.getExpectedSide(candidate.getMatch(),
						candidate.getSource(), mergeRightToLeft);
				final EReference candidateReference = ((ReferenceChange)candidate).getReference();

				if (sourceContainer == candidateContainer
						&& sourceReference.getEOpposite() == candidateReference) {
					result.add(candidate);
					if (candidate.getEquivalence() != null) {
						result.addAll(candidate.getEquivalence().getDifferences());
					}
				}
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
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

		if (isHandleSubDiffs()) {
			final Set<Diff> directlyImpliedToReject = Sets.newHashSet(directlyImpliedRejections);
			Iterable<Diff> subDiffs = concat(transform(directlyImpliedToReject, ComparisonUtil
					.getSubDiffs(!mergeRightToLeft)));
			addAll(directlyImpliedRejections, subDiffs);
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
	 * {@link FeatureMapChange} difference will take precedence over the {@link ReferenceChange} when the the
	 * resulting operation actively modifies a FeatureMap. The {@link ReferenceChange} will take precedence
	 * when a FeatureMap is only modified implicitly. This happens in order to prevent special cases in which
	 * the {@link ReferenceChangeMerger} cannot ensure the correct order of the feature map attribute.</li>
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
		final List<Diff> equivalentDiffs = diff.getEquivalence().getDifferences();
		final Optional<Diff> firstConflicting = Iterables.tryFind(equivalentDiffs,
				hasConflict(ConflictKind.REAL));

		final Diff idealMasterDiff;

		if (diff instanceof ReferenceChange) {
			final ReferenceChange referenceChange = (ReferenceChange)diff;
			idealMasterDiff = getMasterEquivalenceForReferenceChange(referenceChange, mergeRightToLeft);
		} else if (diff instanceof FeatureMapChange) {
			final FeatureMapChange featureMapChange = (FeatureMapChange)diff;
			idealMasterDiff = getMasterEquivalenceForFeatureMapChange(featureMapChange, mergeRightToLeft);
		} else {
			idealMasterDiff = null;
		}

		final Diff masterDiff;
		// conflicting equivalents take precedence over the ideal master equivalence
		if (firstConflicting.isPresent() && !hasRealConflict(idealMasterDiff)) {
			if (hasRealConflict(diff)) {
				masterDiff = null;
			} else {
				masterDiff = firstConflicting.get();
			}
		} else {
			masterDiff = idealMasterDiff;
		}

		return masterDiff;
	}

	/**
	 * Determines if the given {@link Diff} has a conflict of kind {@link ConflictKind#REAL}.
	 *
	 * @param diff
	 *            The {@link Diff} to check.
	 * @return {@code true} if the diff exists and has a conflict of kind {@link ConflictKind#REAL},
	 *         {@code false} otherwise.
	 */
	private boolean hasRealConflict(Diff diff) {
		return diff != null && diff.getConflict() != null
				&& diff.getConflict().getKind() == ConflictKind.REAL;
	}

	/**
	 * Returns the master equivalence for a {@link FeatureMapChange}.
	 * 
	 * @see AbstractMerger#findMasterEquivalence(Diff, boolean)
	 * @param diff
	 *            The {@link Diff} we need to check the equivalence for a 'master' difference.
	 * @param mergeRightToLeft
	 *            Direction of the current merging.
	 * @return The master difference of {@code diff} and its equivalent diffs. This method may return
	 *         <code>null</code> if there is no master diff.
	 */
	private Diff getMasterEquivalenceForFeatureMapChange(FeatureMapChange diff, boolean mergeRightToLeft) {
		if (diff.getKind() == DifferenceKind.MOVE) {
			final Comparison comparison = diff.getMatch().getComparison();
			final FeatureMap.Entry entry = (FeatureMap.Entry)diff.getValue();

			if (entry.getValue() instanceof EObject) {
				final Match valueMatch = comparison.getMatch((EObject)entry.getValue());

				final EObject expectedValue = ComparisonUtil.getExpectedSide(valueMatch, diff.getSource(),
						mergeRightToLeft);

				// Try to find the ReferenceChange-MasterEquivalence when the expected value will not be
				// contained in a FeatureMap
				if (!ComparisonUtil.isContainedInFeatureMap(expectedValue)) {
					return Iterators.tryFind(diff.getEquivalence().getDifferences().iterator(),
							Predicates.instanceOf(ReferenceChange.class)).orNull();
				}
			}

		}
		return null;
	}

	/**
	 * Returns the master equivalence for a {@link ReferenceChange}.
	 * 
	 * @see AbstractMerger#findMasterEquivalence(Diff, boolean)
	 * @param diff
	 *            The {@link Diff} we need to check the equivalence for a 'master' difference.
	 * @param mergeRightToLeft
	 *            Direction of the current merging.
	 * @return The master difference of {@code diff} and its equivalent diffs. This method may return
	 *         <code>null</code> if there is no master diff.
	 */
	private Diff getMasterEquivalenceForReferenceChange(ReferenceChange diff, boolean mergeRightToLeft) {
		Diff masterDiff = getMasterEquivalenceOnReference(diff, mergeRightToLeft);
		if (masterDiff == null) {
			masterDiff = getMasterEquivalenceOnFeatureMap(diff, mergeRightToLeft);
		}
		return masterDiff;
	}

	/**
	 * Returns the master equivalence for a {@link ReferenceChange} from among its equivalents with the same
	 * or {@code eOpposite} reference.
	 * 
	 * @see AbstractMerger#findMasterEquivalence(Diff, boolean)
	 * @param diff
	 *            The {@link Diff} we need to check the equivalence for a 'master' difference.
	 * @param mergeRightToLeft
	 *            Direction of the current merging.
	 * @return The master difference of {@code diff} and its equivalent diffs. This method may return
	 *         <code>null</code> if there is no master diff.
	 */
	private Diff getMasterEquivalenceOnReference(ReferenceChange diff, final boolean mergeRightToLeft) {
		Diff masterDiff = null;
		/*
		 * For the following, we'll only consider diffs that are either on the same reference as "diff", or on
		 * its eopposite.
		 */
		final Predicate<Diff> candidateFilter = or(isDiffOnEOppositeOf(diff), hasSameReferenceAs(diff));
		final List<Diff> equivalentDiffs = diff.getEquivalence().getDifferences();

		// We need to lookup the first multi-valued addition
		final Optional<Diff> multiValuedAddition = Iterators.tryFind(Iterators.filter(equivalentDiffs
				.iterator(), candidateFilter), new Predicate<Diff>() {
			public boolean apply(Diff input) {
				return input instanceof ReferenceChange && ((ReferenceChange)input).getReference().isMany()
						&& isAdd((ReferenceChange)input, mergeRightToLeft);
			}
		});

		final Iterator<Diff> candidateDiffs = Iterators.filter(equivalentDiffs.iterator(), candidateFilter);
		if (multiValuedAddition.isPresent()) {
			// We have at least one multi-valued addition. It will take precedence if there is any
			// single-valued reference change or multi-valued deletion
			while (masterDiff == null && candidateDiffs.hasNext()) {
				final ReferenceChange next = (ReferenceChange)candidateDiffs.next();
				if (!next.getReference().isMany() || !isAdd(next, mergeRightToLeft)) {
					masterDiff = multiValuedAddition.get();
				}
			}
		} else {
			// The only diff that could take precedence is a single-valued set, _if_ there is any multi-valued
			// deletion or single-valued unset in the list.
			ReferenceChange candidate = null;
			if (candidateDiffs.hasNext()) {
				candidate = (ReferenceChange)candidateDiffs.next();
			}
			while (masterDiff == null && candidateDiffs.hasNext()) {
				assert candidate != null;
				final ReferenceChange next = (ReferenceChange)candidateDiffs.next();
				if (candidate.getReference().isMany() || isUnset(candidate, mergeRightToLeft)) {
					// candidate is a multi-valued deletion or an unset. Do we have a single-valued set in the
					// list?
					if (!next.getReference().isMany() && isSet(next, mergeRightToLeft)) {
						masterDiff = next;
					}
				} else if (isSet(candidate, mergeRightToLeft)) {
					// candidate is a set. Is it our master diff?
					if (next.getReference().isMany() || isUnset(next, mergeRightToLeft)) {
						masterDiff = candidate;
					}
				} else {
					// candidate is a change on a single-valued reference. This has no influence over the
					// 'master' lookup. Go on to the next.
					candidate = next;
				}
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
	 * @param mergeRightToLeft
	 *            Direction of the current merging.
	 * @return The master difference of {@code diff} and its equivalent diffs. This method may return
	 *         <code>null</code> if there is no master diff.
	 */
	private Diff getMasterEquivalenceOnFeatureMap(ReferenceChange diff, boolean mergeRightToLeft) {
		if (diff.getKind() == DifferenceKind.MOVE) {

			Comparison comparison = diff.getMatch().getComparison();
			Match valueMatch = comparison.getMatch(diff.getValue());

			EObject sourceValue = ComparisonUtil.getExpectedSide(valueMatch, diff.getSource(),
					mergeRightToLeft);

			// No FeatureMap-MasterEquivalence when the resulting destination is not a FeatureMap
			if (!ComparisonUtil.isContainedInFeatureMap(sourceValue)) {
				return null;
			}
		}

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
	 * @deprecated
	 */
	@Deprecated
	private boolean isOneToManyAndAdd(ReferenceChange diff, ReferenceChange equivalent,
			boolean mergeRightToLeft) {
		return !diff.getReference().isMany() && equivalent.getReference().isMany()
				&& isAdd(equivalent, mergeRightToLeft);
	}

	/**
	 * Specifies whether the given reference changes, {@code diff} and {@code equivalent}, affect references
	 * constituting a one-to-one relationship and whether {@code equivalent} is a set in the current merging.
	 *
	 * @param diff
	 *            The difference to check.
	 * @param equivalent
	 *            The equivalent to the {@code diff}.
	 * @param mergeRightToLeft
	 *            Direction of the merge.
	 * @return <code>true</code> if {@code diff} and {@code equivalent} are one-to-many eOpposites with
	 *         {@code equivalent} resulting in an Add-operation, <code>false</code> otherwise.
	 * @deprecated
	 */
	@Deprecated
	private boolean isOneToOneAndSet(ReferenceChange diff, ReferenceChange equivalent,
			boolean mergeRightToLeft) {
		return !diff.getReference().isMany() && !equivalent.getReference().isMany()
				&& isSet(equivalent, mergeRightToLeft);
	}

	/**
	 * Checks whether the given {@code diff} is of kind {@link DifferenceKind#CHANGE} and its reference is
	 * one-to-one.
	 * 
	 * @param diff
	 *            The ReferenceChange to check.
	 * @return {@code true} if the given {@code diff} is of kind {@link DifferenceKind#CHANGE} and describes a
	 *         one-to-one reference, {@code false} otherwise.
	 */
	private boolean isOneToOneAndChange(ReferenceChange diff) {
		final boolean oppositeReferenceExists = diff.getReference() != null
				&& diff.getReference().getEOpposite() != null;
		return diff.getKind() == DifferenceKind.CHANGE && oppositeReferenceExists
				&& !diff.getReference().isMany() && !diff.getReference().getEOpposite().isMany();
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

		if (isHandleSubDiffs()) {
			addAll(dependencies, ComparisonUtil.getSubDiffs(true).apply(target));
		}

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

		if (isHandleSubDiffs()) {
			addAll(dependencies, ComparisonUtil.getSubDiffs(false).apply(target));
		}

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
		final IMerger delegate = getRegistry().getHighestRankingMerger(diff);
		if (rightToLeft) {
			delegate.copyRightToLeft(diff, monitor);
		} else {
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
	 * @since 3.2
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
	 * Checks whether the given diff will result in the unsetting of a reference in the given merge direction.
	 *
	 * @param diff
	 *            The difference to check.
	 * @param mergeRightToLeft
	 *            Direction of the merge.
	 * @return <code>true</code> if {@code diff} will unset a value with this merge, <code>false</code> if
	 *         this will either "set" or "change" values... or if the given diff is affecting a multi-valued
	 *         reference.
	 */
	private boolean isUnset(ReferenceChange diff, boolean mergeRightToLeft) {
		if (diff.getKind() != DifferenceKind.CHANGE) {
			return false;
		}

		boolean isUnset = false;
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
			isUnset = !isRejecting(diff, mergeRightToLeft);
		} else {
			if (!ReferenceUtil.safeEIsSet(container, diff.getReference())) {
				// No value on the source side, this is an unset
				// Same case as above, if we are rejecting the diff, it is a "set" operation
				isUnset = !isRejecting(diff, mergeRightToLeft);
			} else {
				// The feature is set on the source side. If we're merging towards the other side, this cannot
				// be an unset.
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

					isUnset = originContainer == null
							|| !ReferenceUtil.safeEIsSet(originContainer, diff.getReference());
				}
			}
		}

		return isUnset;
	}

	/**
	 * Checks whether the given diff will result in the setting of a reference in the given merge direction.
	 *
	 * @param diff
	 *            The difference to check.
	 * @param mergeRightToLeft
	 *            Direction of the merge.
	 * @return <code>true</code> if {@code diff} will set a value with this merge, <code>false</code> if this
	 *         will either "unset" or "change" values... or if the given diff is affecting a multi-valued
	 *         reference.
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
				// The feature is set on the source side. If we're merging towards the other side, this is a
				// "set" operation if the feature is not set on the target side.
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
					final EObject targetContainer;
					if (mergeRightToLeft) {
						targetContainer = match.getLeft();
					} else {
						targetContainer = match.getRight();
					}

					isSet = targetContainer == null
							|| !ReferenceUtil.safeEIsSet(targetContainer, diff.getReference());
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
