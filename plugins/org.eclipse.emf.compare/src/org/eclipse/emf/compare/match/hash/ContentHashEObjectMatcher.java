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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ComparisonCanceledException;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.match.eobject.CachingDistance;
import org.eclipse.emf.compare.match.eobject.EObjectIndex.Side;
import org.eclipse.emf.compare.match.eobject.EditionDistance;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.ProximityEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.WeightProvider;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * Pairs EObjects using semantic anchors, exact content and edit-tolerant similarity signatures, inspired by
 * MoHash's hash-first approach. An anchor is a semantic name supplied by the hasher. Exact content retains
 * normalized feature values as well as their hash, so hash collisions do not establish correspondence.
 * <p>
 * Matching first searches for unique anchors and equal content across owners, using the comparison's existing
 * resource correspondence. It then processes roots and children of matched parents, where containment narrows
 * the candidates. Repeated equal content can retain scope order when both sides have the same number of
 * candidates. Similarity is used only after exact keys: the nearest candidate must be unique in both
 * directions and pass {@link EditionDistance}. Competing numbered names, such as Type1 and Type2, require
 * shared non-default content before they can be treated as a rename.
 * </p>
 * <p>
 * Existing matches, including those supplied by an identifier matcher, are retained. Three-way matching
 * combines partial matches only when their occupied sides do not conflict. A final pass searches across
 * owners for moved and edited objects. Objects that still have no match are delegated to proximity with the
 * same distance policy. Callers can wrap this matcher in an identifier matcher when identifiers should take
 * precedence over content.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ContentHashEObjectMatcher implements IEObjectMatcher {

	/** Multiplier used to combine the candidate grouping-key components. */
	private static final int HASH_PRIME = 31;

	/** Numeric runs normalized when detecting competing semantic labels. */
	private static final Pattern DIGIT_RUNS = Pattern.compile("[0-9]+"); //$NON-NLS-1$

	/** Fixed phase order, reused without allocating an enum array for every candidate group. */
	private static final MatchingPhase[] MATCHING_PHASES = MatchingPhase.values();

	/** Feature-weight providers for the current matcher. */
	private final WeightProvider.Descriptor.Registry weights;

	/** Content snapshots supplied to the candidate passes. */
	private final IContentHasher hasher;

	/**
	 * Creates a matcher using the supplied content snapshots and feature weights.
	 *
	 * @param weights
	 *            the feature-weight provider registry
	 * @param hasher
	 *            the content snapshot implementation
	 */
	public ContentHashEObjectMatcher(final WeightProvider.Descriptor.Registry weights,
			IContentHasher hasher) {
		this.weights = weights;
		this.hasher = hasher;
	}

	@Override
	public void createMatches(final Comparison comparison, final Iterator<? extends EObject> left,
			final Iterator<? extends EObject> right, final Iterator<? extends EObject> origin,
			final Monitor monitor) {
		// DefaultMatchEngine has already matched resources; reuse that correspondence for content keys.
		hasher.initialize(comparison);
		final ResourceMatchKeyIndex resourceKeys = ResourceMatchKeyIndex
				.of(comparison.getMatchedResources());

		// Capture each side once. Matching passes below reuse these fingerprints and scope-order lists.
		final Map<EObject, Fingerprint> fingerprintsByObject = new IdentityHashMap<>();
		final List<EObject> leftObjects = accumulateFingerprints(left, hasher, fingerprintsByObject, monitor);
		final List<EObject> rightObjects = accumulateFingerprints(right, hasher, fingerprintsByObject,
				monitor);
		final List<EObject> originObjects = accumulateFingerprints(origin, hasher, fingerprintsByObject,
				monitor);

		// Index direct children, including children whose owners were matched earlier by identifiers.
		final Set<Match> parentsToProcess = new LinkedHashSet<>();
		final Map<EObject, List<EObject>> leftChildrenByOwner = indexScopedChildrenByOwner(leftObjects,
				comparison, parentsToProcess);
		final Map<EObject, List<EObject>> rightChildrenByOwner = indexScopedChildrenByOwner(rightObjects,
				comparison, parentsToProcess);
		final Map<EObject, List<EObject>> originChildrenByOwner = indexScopedChildrenByOwner(originObjects,
				comparison, parentsToProcess);
		final Function<EObject, CandidateGroupKey> groupingFunction = object -> new CandidateGroupKey(
				object.eClass(), object.eContainmentFeature(),
				resourceKeys.key(object.eResource()));
		final Map<EObject, CandidateGroupKey> competingNameGroups = indexCompetingNumberedNames(
				Arrays.asList(leftObjects, rightObjects, originObjects), fingerprintsByObject);
		final EditionDistance editionDistance = new ContentDistance(weights, competingNameGroups,
				fingerprintsByObject);
		final CandidateMatcher candidateMatcher = new CandidateMatcher(comparison, fingerprintsByObject,
				editionDistance, parentsToProcess, monitor);

		// Find unique anchors and content across owners before accepting weaker local candidates.
		final Function<EObject, CandidateGroupKey> resourceGroupingFunction = object -> new CandidateGroupKey(
				object.eClass(), null, resourceKeys.key(object.eResource()));
		candidateMatcher.matchWithinGroups(leftObjects, rightObjects, originObjects, resourceGroupingFunction,
				MatchingMode.UNIQUE_KEYS_ONLY);
		if (spansMultipleResourceGroups(Arrays.asList(leftObjects, rightObjects, originObjects),
				resourceKeys)) {
			candidateMatcher.matchWithinGroups(leftObjects, rightObjects, originObjects,
					object -> new CandidateGroupKey(object.eClass(), null, null),
					MatchingMode.UNIQUE_KEYS_ONLY);
		}

		// The null index key denotes scope roots; a missing matched parent has no children instead.
		final List<EObject> leftRoots = leftChildrenByOwner.getOrDefault(null, Collections.emptyList());
		final List<EObject> rightRoots = rightChildrenByOwner.getOrDefault(null, Collections.emptyList());
		final List<EObject> originRoots = originChildrenByOwner.getOrDefault(null, Collections.emptyList());
		candidateMatcher.matchWithinGroups(leftRoots, rightRoots, originRoots, groupingFunction,
				MatchingMode.ALLOW_SIMILARITY_AND_REPEATS);
		while (!parentsToProcess.isEmpty()) {
			checkCanceled(monitor);
			final Iterator<Match> nextParent = parentsToProcess.iterator();
			final Match parentMatch = nextParent.next();
			nextParent.remove();
			candidateMatcher.matchWithinGroups(
					childrenOfMatchedParent(leftChildrenByOwner, parentMatch.getLeft()),
					childrenOfMatchedParent(rightChildrenByOwner, parentMatch.getRight()),
					childrenOfMatchedParent(originChildrenByOwner, parentMatch.getOrigin()), groupingFunction,
					MatchingMode.ALLOW_SIMILARITY_AND_REPEATS);
		}

		// Complete partial matches even when a branch both moves and edits an object.
		candidateMatcher.matchWithinGroups(leftObjects, rightObjects, originObjects,
				object -> new CandidateGroupKey(object.eClass(), null, null),
				MatchingMode.ALLOW_SIMILARITY_AND_REPEATS);
		final List<EObject> remainingLeft = objectsWithoutMatches(comparison, leftObjects);
		final List<EObject> remainingRight = objectsWithoutMatches(comparison, rightObjects);
		final List<EObject> remainingOrigin = objectsWithoutMatches(comparison, originObjects);
		if (remainingLeft.isEmpty() && remainingRight.isEmpty() && remainingOrigin.isEmpty()) {
			attachMatchesToParents(comparison, monitor);
		} else {
			// Proximity also nests existing matches, so leave the final nesting to that delegate.
			new ProximityEObjectMatcher(new CachingDistance(editionDistance)).createMatches(comparison,
					remainingLeft.iterator(), remainingRight.iterator(), remainingOrigin.iterator(), monitor);
		}
	}

	/** Validates content candidates while keeping competing numbered names distinct. */
	private static final class ContentDistance extends EditionDistance {
		/** Competing semantic names captured for this comparison. */
		private final Map<EObject, CandidateGroupKey> competingNameGroups;

		/** Captured content used to disambiguate those names. */
		private final Map<EObject, Fingerprint> fingerprintsByObject;

		/**
		 * Creates the comparison-local distance policy.
		 *
		 * @param weights
		 *            the feature weights
		 * @param competingNameGroups
		 *            the competing numeric-name groups
		 * @param fingerprintsByObject
		 *            the captured object fingerprints
		 */
		private ContentDistance(WeightProvider.Descriptor.Registry weights,
				Map<EObject, CandidateGroupKey> competingNameGroups,
				Map<EObject, Fingerprint> fingerprintsByObject) {
			super(weights);
			this.competingNameGroups = competingNameGroups;
			this.fingerprintsByObject = fingerprintsByObject;
		}

		@Override
		public double distance(Comparison comparison, EObject left, EObject right) {
			if (lacksEvidenceForNumberedRename(left, right, competingNameGroups, fingerprintsByObject)) {
				return Double.MAX_VALUE;
			}
			return super.distance(comparison, left, right);
		}

		@Override
		public boolean areIdentic(Comparison comparison, EObject left, EObject right) {
			return !lacksEvidenceForNumberedRename(left, right, competingNameGroups, fingerprintsByObject)
					&& super.areIdentic(comparison, left, right);
		}
	}

	/**
	 * Returns the direct scoped children of a matched parent, or an empty list for a missing counterpart.
	 * <p>
	 * The null key in the index stores scope roots. A null parent here instead means that this comparison
	 * side has no parent object, so its roots must not be used as children of that missing object.
	 * </p>
	 *
	 * @param childrenByOwner
	 *            the index returned by {@link #indexScopedChildrenByOwner}
	 * @param parent
	 *            the parent on one match side, or null when that side is absent
	 * @return the parent's direct scoped children; never the root bucket for a null parent
	 */
	private static List<EObject> childrenOfMatchedParent(Map<EObject, List<EObject>> childrenByOwner,
			EObject parent) {
		if (parent == null) {
			return Collections.emptyList();
		}
		return childrenByOwner.getOrDefault(parent, Collections.emptyList());
	}

	/**
	 * Computes each object's fingerprint and accumulates it in the supplied map.
	 * <p>
	 * The returned list contains the objects consumed from the iterator, in the same order. The objects are
	 * not copied; the list allows later matching passes to reuse the original iteration order.
	 * </p>
	 *
	 * @param objects
	 *            the scoped objects to consume
	 * @param hasher
	 *            the hasher that captures each object's content
	 * @param fingerprintsByObject
	 *            accumulator for the fingerprints captured for each object
	 * @param monitor
	 *            the cancellation monitor checked before capturing each object
	 * @return a list containing all objects from the input iterator, in their original order
	 */
	private static List<EObject> accumulateFingerprints(final Iterator<? extends EObject> objects,
			final IContentHasher hasher, final Map<EObject, Fingerprint> fingerprintsByObject,
			final Monitor monitor) {
		final List<EObject> scopedObjects = new ArrayList<>();
		while (objects.hasNext()) {
			checkCanceled(monitor);
			final EObject object = objects.next();
			scopedObjects.add(object);
			fingerprintsByObject.put(object, hasher.fingerprint(object));
		}
		return scopedObjects;
	}

	/**
	 * Indexes every supplied object under its direct container, without recursively traversing children.
	 * <p>
	 * Objects with no scoped or already matched container are indexed under null as scope roots. If a
	 * container is outside this object list but already has a match, its children remain indexed under that
	 * container and its match is added to {@code parentsToProcess}. This allows children without identifiers
	 * to be processed under parents already paired by an identifier matcher.
	 * </p>
	 *
	 * @param objects
	 *            the objects on one comparison side
	 * @param comparison
	 *            the comparison containing any matches established before content matching
	 * @param parentsToProcess
	 *            accumulator of existing parent matches whose scoped children still need matching
	 * @return an identity-based map of direct scoped children by owner, with null denoting scope roots
	 */
	private static Map<EObject, List<EObject>> indexScopedChildrenByOwner(final List<EObject> objects,
			Comparison comparison, Set<Match> parentsToProcess) {
		final Map<EObject, List<EObject>> childrenByOwner = new IdentityHashMap<>();
		// Domain EObjects can override equals(); scope membership must use object identity instead.
		// The Boolean values are unused: this identity map acts as a membership set.
		final Map<EObject, Boolean> scopedObjectIdentities = new IdentityHashMap<>();
		objects.forEach(object -> scopedObjectIdentities.put(object, Boolean.TRUE));
		for (final EObject object : objects) {
			EObject parent = object.eContainer();
			if (!scopedObjectIdentities.containsKey(parent)) {
				Match ownerMatch = comparison.getMatch(parent);
				if (ownerMatch == null) {
					parent = null;
				} else {
					parentsToProcess.add(ownerMatch);
				}
			}
			childrenByOwner.computeIfAbsent(parent, ignored -> new ArrayList<>()).add(object);
		}
		return childrenByOwner;
	}

	/**
	 * Checks whether the scope spans more than one resource correspondence group.
	 *
	 * @param sides
	 *            the left, right and ancestor object lists
	 * @param resourceKeys
	 *            the keys derived from the comparison's existing resource matches
	 * @return whether multiple resource groups occur
	 */
	private static boolean spansMultipleResourceGroups(List<List<EObject>> sides,
			ResourceMatchKeyIndex resourceKeys) {
		Set<Object> keys = new HashSet<>();
		for (List<EObject> side : sides) {
			for (EObject object : side) {
				keys.add(resourceKeys.key(object.eResource()));
				if (keys.size() > 1) {
					return true;
				}
			}
		}
		return false;
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
	 * Indexes objects whose numbered-name family has competing labels on at least one comparison side.
	 * <p>
	 * For example, Type1 and Type2 compete when both occur on the same side. Type1 on one side and Type2
	 * alone on the other do not compete. The family key combines the EClass with a name whose digit runs have
	 * been replaced by #; only families with a same-side competitor remain in the returned index.
	 * </p>
	 *
	 * @param sides
	 *            the left, right and ancestor object lists, kept separate to detect same-side competition
	 * @param fingerprintsByObject
	 *            the captured anchors used to identify numbered names
	 * @return the objects belonging to competing numbered-name families, indexed by object identity
	 */
	private static Map<EObject, CandidateGroupKey> indexCompetingNumberedNames(List<List<EObject>> sides,
			Map<EObject, Fingerprint> fingerprintsByObject) {
		Map<EObject, CandidateGroupKey> groupsByObject = new IdentityHashMap<>();
		Set<CandidateGroupKey> competingFamilies = new HashSet<>();
		Map<String, String> normalizedNameCache = new HashMap<>();
		for (List<EObject> side : sides) {
			Map<CandidateGroupKey, String> firstNameByFamily = new HashMap<>();
			for (EObject object : side) {
				String anchor = fingerprintsByObject.get(object).anchor();
				if (anchor != null) {
					String family = normalizedNameCache.computeIfAbsent(anchor, name -> {
						Matcher matcher = DIGIT_RUNS.matcher(name);
						if (matcher.find()) {
							return matcher.replaceAll("#");
						}
						return name; // $NON-NLS-1$
					});
					if (family.equals(anchor)) {
						continue;
					}
					CandidateGroupKey familyKey = new CandidateGroupKey(object.eClass(), null, family);
					groupsByObject.put(object, familyKey);
					String previousName = firstNameByFamily.put(familyKey, anchor);
					if (previousName != null && !previousName.equals(anchor)) {
						competingFamilies.add(familyKey);
					}
				}
			}
		}
		groupsByObject.values().removeIf(familyKey -> !competingFamilies.contains(familyKey));
		return groupsByObject;
	}

	/**
	 * Checks whether a proposed rename between competing numeric labels lacks shared content evidence.
	 * <p>
	 * A common prefix alone must not pair unrelated additions and deletions such as Type1 and Type2. A shared
	 * non-default feature other than name can justify that rename; {@link FeatureValue#isDiscriminating}
	 * identifies such evidence.
	 * </p>
	 *
	 * @param firstObject
	 *            the first proposed partner
	 * @param secondObject
	 *            the second proposed partner
	 * @param competingNameGroups
	 *            the numbered-name families that contain same-side competitors
	 * @param fingerprintsByObject
	 *            the captured content of the proposed partners
	 * @return whether the pair must be rejected for lack of evidence beyond the numbered name
	 */
	private static boolean lacksEvidenceForNumberedRename(EObject firstObject, EObject secondObject,
			Map<EObject, CandidateGroupKey> competingNameGroups,
			Map<EObject, Fingerprint> fingerprintsByObject) {
		CandidateGroupKey family = competingNameGroups.get(firstObject);
		Fingerprint firstFingerprint = fingerprintsByObject.get(firstObject);
		Fingerprint secondFingerprint = fingerprintsByObject.get(secondObject);
		if (family == null || !family.equals(competingNameGroups.get(secondObject))
				|| Objects.equals(firstFingerprint.anchor(), secondFingerprint.anchor())) {
			return false;
		}
		// Competing numeric labels need evidence beyond a shared word prefix to establish a rename.
		for (FeatureValue featureValue : firstFingerprint.content()) {
			if (featureValue.isDiscriminating() && secondFingerprint.content().contains(featureValue)) {
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
	 * Selects objects that have no match yet.
	 *
	 * @param comparison
	 *            the comparison in progress
	 * @param objects
	 *            the scoped objects in iteration order
	 * @return the unmatched objects in scope order
	 */
	private static List<EObject> objectsWithoutMatches(final Comparison comparison,
			final List<EObject> objects) {
		return objects.stream().filter(object -> comparison.getMatch(object) == null)
				.collect(Collectors.toList());
	}

	/**
	 * Nests matches under their matched container after all candidate passes have completed. Containment on
	 * the left is preferred, then right, then origin, so a moved object's match has one consistent
	 * presentation parent even when its containers differ across sides.
	 *
	 * @param comparison
	 *            the comparison whose flat or partially nested matches need final placement
	 * @param monitor
	 *            the cancellation monitor
	 */
	private static void attachMatchesToParents(final Comparison comparison, final Monitor monitor) {
		final List<Match> matches = new ArrayList<>();
		comparison.eAllContents().forEachRemaining(object -> {
			if (object instanceof Match) {
				matches.add((Match)object);
			}
		});
		for (final Match match : matches) {
			checkCanceled(monitor);
			EObject object = match.getLeft();
			if (object == null) {
				object = match.getRight();
			}
			if (object == null) {
				object = match.getOrigin();
			}
			final Match parent = comparison.getMatch(object.eContainer());
			if (parent != null && match.eContainer() != parent) {
				((InternalEList<Match>)parent.getSubmatches()).addUnique(match);
			}
		}
	}

	/**
	 * Stops matching when the monitor reports cancellation.
	 *
	 * @param monitor
	 *            the cancellation monitor
	 */
	private static void checkCanceled(final Monitor monitor) {
		if (monitor.isCanceled()) {
			throw new ComparisonCanceledException();
		}
	}

	/** Global move discovery uses unique keys; local and completion passes may use weaker evidence. */
	private enum MatchingMode {
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

	/** Applies candidate grouping and evidence phases to one comparison and its child-processing queue. */
	private static final class CandidateMatcher {
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
		private CandidateMatcher(Comparison comparison, Map<EObject, Fingerprint> fingerprintsByObject,
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
		private void matchWithinGroups(final List<EObject> leftObjects, final List<EObject> rightObjects,
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
	}

	/** Candidate type, optional containment feature and optional resource or semantic-name context. */
	private static final class CandidateGroupKey {
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
		private CandidateGroupKey(EClass metaclass, EStructuralFeature containmentFeature, Object context) {
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
