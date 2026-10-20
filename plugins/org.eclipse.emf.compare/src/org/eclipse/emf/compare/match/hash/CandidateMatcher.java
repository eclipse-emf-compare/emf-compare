/*******************************************************************************
 * Copyright (c) 2026 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.hash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ComparisonCanceledException;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.match.eobject.EObjectIndex.Side;
import org.eclipse.emf.compare.match.eobject.EditionDistance;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * Applies candidate grouping and evidence phases to one comparison and its child-processing queue.
 * The supplied comparison and parent queue are updated directly; candidate lists retain their scope order
 * and are not modified. Fingerprints are read from the supplied comparison-local map.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class CandidateMatcher {

	/** Fixed phase order, reused without allocating an enum array for every candidate group. */
	private static final MatchingPhase[] MATCHING_PHASES = MatchingPhase.values();

	/** Comparison being populated. */
	private final Comparison comparison;

	/** Fingerprints captured once for the scoped objects. */
	private final Map<EObject, Fingerprint> fingerprintsByObject;

	/** Distance validation applied to approximate candidates. */
	private final EditionDistance editionDistance;

	/** Matches whose scoped children still need processing. */
	private final Set<Match> parentsToProcess;

	/** Cancellation monitor shared by matching passes. */
	private final Monitor monitor;

	/**
	 * Captures the state shared by candidate passes for one comparison.
	 *
	 * @param comparison
	 *            the comparison in progress
	 * @param fingerprintsByObject
	 *            the captured object fingerprints
	 * @param editionDistance
	 *            the distance validation for candidate pairs
	 * @param parentsToProcess
	 *            the matches awaiting child processing
	 * @param monitor
	 *            the cancellation monitor
	 */
	public CandidateMatcher(Comparison comparison, Map<EObject, Fingerprint> fingerprintsByObject,
			EditionDistance editionDistance, Set<Match> parentsToProcess, Monitor monitor) {
		this.comparison = comparison;
		this.fingerprintsByObject = fingerprintsByObject;
		this.editionDistance = editionDistance;
		this.parentsToProcess = parentsToProcess;
		this.monitor = monitor;
	}

	/**
	 * Matches objects only against candidates with the same grouping key. Within each group, anchors
	 * precede exact content and optional similarity. Each phase processes origin/left, origin/right, then
	 * left/right, so ancestor agreements seed later branch matching.
	 *
	 * @param leftObjects
	 *            the scoped objects on the left
	 * @param rightObjects
	 *            the scoped objects on the right
	 * @param originObjects
	 *            the scoped ancestor objects, or an empty list for a two-way comparison
	 * @param groupingFunction
	 *            the function defining the metaclass, containment and context constraints for this pass
	 * @param mode
	 *            whether this pass accepts only unique exact keys or also ordered repeats and similarity
	 */
	public void matchWithinGroups(final List<EObject> leftObjects, final List<EObject> rightObjects,
			final List<EObject> originObjects,
			final Function<EObject, CandidateGroupKey> groupingFunction, final MatchingMode mode) {
		if (allObjectsHaveAllCounterparts(comparison, leftObjects)
				&& allObjectsHaveAllCounterparts(comparison, rightObjects)
				&& allObjectsHaveAllCounterparts(comparison, originObjects)) {
			return;
		}
		final Map<CandidateGroupKey, List<EObject>> leftGroups = groupObjectsByKey(leftObjects,
				groupingFunction);
		final Map<CandidateGroupKey, List<EObject>> rightGroups = groupObjectsByKey(rightObjects,
				groupingFunction);
		final Map<CandidateGroupKey, List<EObject>> originGroups = groupObjectsByKey(originObjects,
				groupingFunction);
		final Set<CandidateGroupKey> allGroupKeys = new LinkedHashSet<>(leftGroups.keySet());
		allGroupKeys.addAll(rightGroups.keySet());
		allGroupKeys.addAll(originGroups.keySet());
		for (final CandidateGroupKey groupKey : allGroupKeys) {
			checkCanceled(monitor);
			final List<EObject> leftCandidates = leftGroups.getOrDefault(groupKey,
					Collections.emptyList());
			final List<EObject> rightCandidates = rightGroups.getOrDefault(groupKey,
					Collections.emptyList());
			final List<EObject> originCandidates = originGroups.getOrDefault(groupKey,
					Collections.emptyList());
			if (allObjectsHaveAllCounterparts(comparison, leftCandidates)
					&& allObjectsHaveAllCounterparts(comparison, rightCandidates)
					&& allObjectsHaveAllCounterparts(comparison, originCandidates)) {
				continue;
			}
			boolean allowOrderedRepeats = mode == MatchingMode.ALLOW_SIMILARITY_AND_REPEATS;
			for (MatchingPhase phase : MATCHING_PHASES) {
				if (phase == MatchingPhase.SIMILARITY && mode == MatchingMode.UNIQUE_KEYS_ONLY) {
					continue;
				}
				// Establish ancestor agreements before completing left/right pairs in each phase.
				matchSidePair(originCandidates, leftCandidates, Side.ORIGIN, Side.LEFT, phase,
						allowOrderedRepeats);
				matchSidePair(originCandidates, rightCandidates, Side.ORIGIN, Side.RIGHT, phase,
						allowOrderedRepeats);
				matchSidePair(leftCandidates, rightCandidates, Side.LEFT, Side.RIGHT, phase,
						allowOrderedRepeats);
			}
		}
	}

	/**
	 * Applies one evidence phase between two comparison sides without overwriting existing counterparts.
	 * Similarity uses the full candidate lists, including already claimed objects, when checking
	 * uniqueness. Those objects can block an ambiguous pair even though they cannot themselves be claimed
	 * again.
	 *
	 * @param firstObjects
	 *            all candidates on the first comparison side
	 * @param secondObjects
	 *            all candidates on the second comparison side
	 * @param firstSide
	 *            the comparison side of the first candidate list
	 * @param secondSide
	 *            the comparison side of the second candidate list
	 * @param phase
	 *            the evidence to apply: anchor, exact content or reciprocal nearest similarity
	 * @param allowOrderedRepeats
	 *            whether equal repeated content may retain scope order during the exact-content phase
	 */
	private void matchSidePair(final List<EObject> firstObjects, final List<EObject> secondObjects,
			final Side firstSide, final Side secondSide, final MatchingPhase phase,
			final boolean allowOrderedRepeats) {
		if (firstObjects.isEmpty() || secondObjects.isEmpty()) {
			return;
		}
		final List<EObject> firstAvailableObjects = objectsWithoutCounterpart(comparison, firstObjects,
				secondSide);
		if (firstAvailableObjects.isEmpty()) {
			return;
		}
		final List<EObject> secondAvailableObjects = objectsWithoutCounterpart(comparison, secondObjects,
				firstSide);
		if (secondAvailableObjects.isEmpty()) {
			return;
		}
		final BiConsumer<EObject, EObject> recordMatch = (first, second) -> linkObjects(comparison, first,
				second, firstSide, secondSide, parentsToProcess);
		if (phase != MatchingPhase.SIMILARITY) {
			Function<EObject, Object> keyFunction = object -> fingerprintsByObject.get(object).content();
			if (phase == MatchingPhase.ANCHOR) {
				keyFunction = object -> fingerprintsByObject.get(object).anchor();
			}
			matchEqualKeys(firstAvailableObjects, secondAvailableObjects, keyFunction, recordMatch,
					phase == MatchingPhase.EXACT_CONTENT && allowOrderedRepeats);
		} else {
			// Keep claimed candidates in both arrays: they still establish ambiguity.
			final long[] secondSignatures = collectSimilaritySignatures(secondObjects,
					fingerprintsByObject);
			long[] firstSignatures = null;
			for (final EObject object : firstAvailableObjects) {
				checkCanceled(monitor);
				if (objectOnSide(comparison.getMatch(object), secondSide) != null) {
					continue;
				}
				final int candidateIndex = indexOfUniqueNearestSignature(
						fingerprintsByObject.get(object).similarity(), secondSignatures);
				if (candidateIndex < 0) {
					continue;
				}
				final EObject candidate = secondObjects.get(candidateIndex);
				if (objectOnSide(comparison.getMatch(candidate), firstSide) != null
						|| !canCombineMatches(comparison, object, candidate)) {
					continue;
				}
				if (firstSignatures == null) {
					firstSignatures = collectSimilaritySignatures(firstObjects, fingerprintsByObject);
				}
				final int reverseIndex = indexOfUniqueNearestSignature(secondSignatures[candidateIndex],
						firstSignatures);
				if (reverseIndex >= 0 && firstObjects.get(reverseIndex) == object
						&& editionDistance.distance(comparison, object, candidate) < Double.MAX_VALUE) {
					recordMatch.accept(object, candidate);
				}
			}
		}
	}

	/**
	 * Checks whether every object already has all counterparts required by the comparison.
	 *
	 * @param comparison
	 *            the comparison in progress
	 * @param objects
	 *            the scoped objects in iteration order
	 * @return whether all objects are fully matched
	 */
	private static boolean allObjectsHaveAllCounterparts(Comparison comparison, List<EObject> objects) {
		for (EObject object : objects) {
			Match match = comparison.getMatch(object);
			if (match == null || match.getLeft() == null || match.getRight() == null) {
				return false;
			}
			if (comparison.isThreeWay() && match.getOrigin() == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Selects objects whose current match has no object on the requested counterpart side.
	 *
	 * @param comparison
	 *            the comparison containing partial matches
	 * @param objects
	 *            the candidate objects in scope order
	 * @param counterpartSide
	 *            the side that each candidate still needs to fill
	 * @return candidates that can still receive a counterpart on that side, in scope order
	 */
	private static List<EObject> objectsWithoutCounterpart(Comparison comparison, List<EObject> objects,
			Side counterpartSide) {
		return objects.stream()
				.filter(object -> objectOnSide(comparison.getMatch(object), counterpartSide) == null)
				.collect(Collectors.toList());
	}

	/**
	 * Returns the object on a requested match side, or null when it is absent.
	 *
	 * @param match
	 *            the partial object match
	 * @param side
	 *            the requested comparison side
	 * @return the counterpart, or null
	 */
	private static EObject objectOnSide(Match match, Side side) {
		if (match == null) {
			return null;
		}
		if (side == Side.LEFT) {
			return match.getLeft();
		}
		if (side == Side.RIGHT) {
			return match.getRight();
		}
		return match.getOrigin();
	}

	/**
	 * Checks whether the proposed objects can share a match without replacing an existing counterpart. Two
	 * different partial matches are compatible only when they do not both occupy the same side.
	 *
	 * @param comparison
	 *            the comparison containing the proposed objects' existing matches
	 * @param firstObject
	 *            the first proposed partner
	 * @param secondObject
	 *            the second proposed partner
	 * @return whether their matches are absent, already shared, or occupy disjoint comparison sides
	 */
	private static boolean canCombineMatches(Comparison comparison, EObject firstObject,
			EObject secondObject) {
		Match firstMatch = comparison.getMatch(firstObject);
		Match secondMatch = comparison.getMatch(secondObject);
		if (firstMatch == null || secondMatch == null || firstMatch == secondMatch) {
			return true;
		}
		if (firstMatch.getLeft() != null && secondMatch.getLeft() != null) {
			return false;
		}
		if (firstMatch.getRight() != null && secondMatch.getRight() != null) {
			return false;
		}
		return firstMatch.getOrigin() == null || secondMatch.getOrigin() == null;
	}

	/**
	 * Records a pair in a new or existing match and schedules that match's children for processing. If both
	 * objects already belong to different compatible matches, their sides and submatches are merged and the
	 * redundant match is removed from the comparison and the work queue.
	 *
	 * @param comparison
	 *            the comparison to update
	 * @param firstObject
	 *            the first accepted partner
	 * @param secondObject
	 *            the second accepted partner
	 * @param firstSide
	 *            the side occupied by the first object
	 * @param secondSide
	 *            the side occupied by the second object
	 * @param parentsToProcess
	 *            accumulator of matches whose children need processing after this pair is recorded
	 */
	private static void linkObjects(Comparison comparison, EObject firstObject, EObject secondObject,
			Side firstSide, Side secondSide, Set<Match> parentsToProcess) {
		if (!canCombineMatches(comparison, firstObject, secondObject)) {
			return;
		}
		Match combinedMatch = comparison.getMatch(firstObject);
		Match otherMatch = comparison.getMatch(secondObject);
		if (combinedMatch == null && otherMatch == null) {
			combinedMatch = CompareFactory.eINSTANCE.createMatch();
			((InternalEList<Match>)comparison.getMatches()).addUnique(combinedMatch);
		} else if (combinedMatch == null) {
			combinedMatch = otherMatch;
		} else if (otherMatch != null && combinedMatch != otherMatch) {
			if (otherMatch.getLeft() != null) {
				combinedMatch.setLeft(otherMatch.getLeft());
			}
			if (otherMatch.getRight() != null) {
				combinedMatch.setRight(otherMatch.getRight());
			}
			if (otherMatch.getOrigin() != null) {
				combinedMatch.setOrigin(otherMatch.getOrigin());
			}
			// Reparenting removes children from the old match, so iterate a copy of that list.
			combinedMatch.getSubmatches().addAll(new ArrayList<>(otherMatch.getSubmatches()));
			EcoreUtil.remove(otherMatch);
			parentsToProcess.remove(otherMatch);
		}
		setObjectOnSide(combinedMatch, firstSide, firstObject);
		setObjectOnSide(combinedMatch, secondSide, secondObject);
		parentsToProcess.add(combinedMatch);
	}

	/**
	 * Sets the object on the requested side of a match.
	 *
	 * @param match
	 *            the partial object match
	 * @param side
	 *            the requested comparison side
	 * @param object
	 *            the model object
	 */
	private static void setObjectOnSide(Match match, Side side, EObject object) {
		if (side == Side.LEFT) {
			match.setLeft(object);
		} else if (side == Side.RIGHT) {
			match.setRight(object);
		} else {
			match.setOrigin(object);
		}
	}

	/**
	 * Groups objects with non-null keys, preserving their scope order inside each bucket.
	 *
	 * @param <K>
	 *            the key type used for grouping
	 * @param objects
	 *            the candidate objects in scope order
	 * @param keyFunction
	 *            the function selecting each object's group or exact-match key
	 * @return the objects grouped by key; objects with a null key are omitted
	 */
	private static <K> Map<K, List<EObject>> groupObjectsByKey(final List<EObject> objects,
			final Function<EObject, K> keyFunction) {
		final Map<K, List<EObject>> objectsByKey = new HashMap<>();
		for (final EObject object : objects) {
			final K groupKey = keyFunction.apply(object);
			if (groupKey != null) {
				objectsByKey.computeIfAbsent(groupKey, ignored -> new ArrayList<>()).add(object);
			}
		}
		return objectsByKey;
	}

	/**
	 * Pairs objects with equal non-null keys, comparing actual keys as well as their hash codes. Without
	 * ordered repeats, only keys unique on both sides can establish a pair. When ordered repeats are allowed,
	 * equal keys with equal multiplicity are paired in their original scope order.
	 *
	 * @param <K>
	 *            the anchor or exact-content key type
	 * @param firstObjects
	 *            the available objects on the first comparison side
	 * @param secondObjects
	 *            the available objects on the second comparison side
	 * @param keyFunction
	 *            the function selecting an anchor or exact-content snapshot
	 * @param recordMatch
	 *            the callback that records each accepted pair in the comparison
	 * @param allowOrderedRepeats
	 *            whether repeated equal keys may be paired in scope order when multiplicities agree
	 */
	private static <K> void matchEqualKeys(final List<EObject> firstObjects,
			final List<EObject> secondObjects, final Function<EObject, K> keyFunction,
			final BiConsumer<EObject, EObject> recordMatch, final boolean allowOrderedRepeats) {
		if (!allowOrderedRepeats) {
			Map<K, EObject> firstUniqueObjects = indexObjectsWithUniqueKeys(firstObjects, keyFunction);
			Map<K, EObject> secondUniqueObjects = indexObjectsWithUniqueKeys(secondObjects, keyFunction);
			for (Map.Entry<K, EObject> entry : firstUniqueObjects.entrySet()) {
				EObject counterpartObject = secondUniqueObjects.get(entry.getKey());
				if (entry.getValue() != null && counterpartObject != null) {
					recordMatch.accept(entry.getValue(), counterpartObject);
				}
			}
			return;
		}
		final Map<K, List<EObject>> secondObjectsByKey = groupObjectsByKey(secondObjects, keyFunction);
		for (final Map.Entry<K, List<EObject>> entry : groupObjectsByKey(firstObjects, keyFunction)
				.entrySet()) {
			final List<EObject> counterpartObjects = secondObjectsByKey.getOrDefault(entry.getKey(),
					Collections.emptyList());
			if (entry.getValue().size() == counterpartObjects.size()) {
				for (int i = 0; i < counterpartObjects.size(); i++) {
					recordMatch.accept(entry.getValue().get(i), counterpartObjects.get(i));
				}
			}
		}
	}

	/**
	 * Indexes each non-null key by its unique object, retaining null as the value for duplicate keys. Keeping
	 * duplicate keys visible prevents a later object with that key from being treated as unique.
	 *
	 * @param <K>
	 *            the anchor or content key type
	 * @param objects
	 *            the candidate objects
	 * @param keyFunction
	 *            the function selecting each object's key
	 * @return an index containing one object for unique keys and null for duplicate keys
	 */
	private static <K> Map<K, EObject> indexObjectsWithUniqueKeys(List<EObject> objects,
			Function<EObject, K> keyFunction) {
		Map<K, EObject> uniqueObjectByKey = new HashMap<>();
		for (EObject object : objects) {
			K groupKey = keyFunction.apply(object);
			if (groupKey != null) {
				EObject uniqueObject = object;
				if (uniqueObjectByKey.containsKey(groupKey)) {
					uniqueObject = null;
				}
				uniqueObjectByKey.put(groupKey, uniqueObject);
			}
		}
		return uniqueObjectByKey;
	}

	/**
	 * Captures candidate signatures once for repeated nearest-neighbor scans.
	 *
	 * @param objects
	 *            the scoped objects in iteration order
	 * @param fingerprintsByObject
	 *            the captured object fingerprints
	 * @return the signatures in candidate order
	 */
	private static long[] collectSimilaritySignatures(List<EObject> objects,
			Map<EObject, Fingerprint> fingerprintsByObject) {
		long[] signatures = new long[objects.size()];
		for (int i = 0; i < signatures.length; i++) {
			signatures[i] = fingerprintsByObject.get(objects.get(i)).similarity();
		}
		return signatures;
	}

	/**
	 * Finds the candidate with the fewest differing signature bits, rejecting equal-distance ties.
	 *
	 * @param signature
	 *            the signature whose nearest candidate is needed
	 * @param candidateSignatures
	 *            the signatures in candidate-list order
	 * @return the nearest candidate's list index, or -1 if the list is empty or the best distance is tied
	 */
	private static int indexOfUniqueNearestSignature(long signature, long[] candidateSignatures) {
		int nearestIndex = -1;
		int fewestDifferingBits = Integer.MAX_VALUE;
		// shortcut: scan changed siblings; add a hash tree if wide edited sibling groups dominate.
		for (int i = 0; i < candidateSignatures.length; i++) {
			final int differingBits = Long.bitCount(signature ^ candidateSignatures[i]);
			if (differingBits < fewestDifferingBits) {
				fewestDifferingBits = differingBits;
				nearestIndex = i;
			} else if (differingBits == fewestDifferingBits) {
				nearestIndex = -1;
			}
		}
		return nearestIndex;
	}

	/**
	 * Stops matching when the monitor reports cancellation.
	 *
	 * @param monitor
	 *            the cancellation monitor
	 */
	public static void checkCanceled(final Monitor monitor) {
		if (monitor.isCanceled()) {
			throw new ComparisonCanceledException();
		}
	}

	/** Global move discovery uses unique keys; local and completion passes may use weaker evidence. */
	public enum MatchingMode {
		/** Pair only unique anchors and exact-content keys. */
		UNIQUE_KEYS_ONLY,
		/** Also allow ordered equal-content repeats and distance-checked similarity candidates. */
		ALLOW_SIMILARITY_AND_REPEATS
	}

	/** Evidence is applied from the strongest key to the weakest candidate search. */
	private enum MatchingPhase {
		/** A unique semantic name. */
		ANCHOR,
		/** Equal normalized content, checked in addition to its hash code. */
		EXACT_CONTENT,
		/** A reciprocal nearest signature accepted by edition distance. */
		SIMILARITY
	}

	/** Candidate type, optional containment feature and optional resource or semantic-name context. */
	public static final class CandidateGroupKey {
		/** Multiplier used to combine the grouping-key components. */
		private static final int HASH_PRIME = 31;

		/** Candidate metaclass. */
		private final EClass metaclass;

		/** Required containment feature, or null when containment is ignored. */
		private final EStructuralFeature containmentFeature;

		/**
		 * Group discriminator: a resource-group number or URI, a normalized numbered-name family, or null
		 * when the pass intentionally ignores that context.
		 */
		private final Object context;

		/**
		 * Combines an EClass, optional containment feature and optional context into a candidate-group key.
		 * The context is a resource key for resource-restricted matching, a name family for numeric-label
		 * competition, or null when matching across resources.
		 *
		 * @param metaclass
		 *            the metaclass required of every candidate in the group
		 * @param containmentFeature
		 *            the required containment feature, or null when containment is ignored
		 * @param context
		 *            the resource or numbered-name discriminator, or null when context is ignored
		 */
		public CandidateGroupKey(EClass metaclass, EStructuralFeature containmentFeature, Object context) {
			this.metaclass = metaclass;
			this.containmentFeature = containmentFeature;
			this.context = context;
		}

		@Override
		public boolean equals(Object object) {
			if (!(object instanceof CandidateGroupKey)) {
				return false;
			}
			CandidateGroupKey other = (CandidateGroupKey)object;
			return Objects.equals(metaclass, other.metaclass)
					&& Objects.equals(containmentFeature, other.containmentFeature)
					&& Objects.equals(context, other.context);
		}

		@Override
		public int hashCode() {
			return HASH_PRIME
					* (HASH_PRIME * Objects.hashCode(metaclass) + Objects.hashCode(containmentFeature))
					+ Objects.hashCode(context);
		}
	}
}
