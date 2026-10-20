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

import static org.eclipse.emf.compare.match.hash.CandidateMatcher.checkCanceled;

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
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.match.eobject.CachingDistance;
import org.eclipse.emf.compare.match.eobject.EditionDistance;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.ProximityEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.WeightProvider;
import org.eclipse.emf.compare.match.hash.CandidateMatcher.CandidateGroupKey;
import org.eclipse.emf.compare.match.hash.CandidateMatcher.MatchingMode;
import org.eclipse.emf.ecore.EObject;
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
public class ContentHashEObjectMatcher implements IEObjectMatcher {

	/** Numeric runs normalized when detecting competing semantic labels. */
	private static final Pattern DIGIT_RUNS = Pattern.compile("[0-9]+"); //$NON-NLS-1$

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
		final Function<EObject, CandidateGroupKey> containmentGroupingFunction = object -> new CandidateGroupKey(
				object.eClass(), object.eContainmentFeature(),
				resourceKeys.key(object.eResource()));
		final Map<EObject, CandidateGroupKey> competingNameGroups = indexCompetingNumberedNames(
				Arrays.asList(leftObjects, rightObjects, originObjects), fingerprintsByObject);
		final EditionDistance editionDistance = new ContentDistance(weights, competingNameGroups,
				fingerprintsByObject);
		final CandidateMatcher candidateMatcher = createCandidateMatcher(comparison, fingerprintsByObject,
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
		candidateMatcher.matchWithinGroups(leftRoots, rightRoots, originRoots, containmentGroupingFunction,
				MatchingMode.ALLOW_SIMILARITY_AND_REPEATS);
		while (!parentsToProcess.isEmpty()) {
			checkCanceled(monitor);
			final Iterator<Match> nextParent = parentsToProcess.iterator();
			final Match parentMatch = nextParent.next();
			nextParent.remove();
			candidateMatcher.matchWithinGroups(
					childrenOfMatchedParent(leftChildrenByOwner, parentMatch.getLeft()),
					childrenOfMatchedParent(rightChildrenByOwner, parentMatch.getRight()),
					childrenOfMatchedParent(originChildrenByOwner, parentMatch.getOrigin()),
					containmentGroupingFunction,
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

	/**
	 * Creates comparison-local candidate matching after fingerprints have been captured. Overrides can
	 * return a specialized matcher or supply a different distance for its candidate passes. The supplied
	 * distance remains the policy used by the final proximity fallback.
	 *
	 * @param comparison
	 *            the comparison to populate
	 * @param fingerprintsByObject
	 *            captured snapshots for this invocation; implementations must not modify this map
	 * @param editionDistance
	 *            the default distance used to validate similarity candidates
	 * @param parentsToProcess
	 *            the shared queue to update when matches are created or combined
	 * @param monitor
	 *            the cancellation monitor for this invocation
	 * @return a new matcher updating the supplied comparison and parent queue
	 */
	protected CandidateMatcher createCandidateMatcher(Comparison comparison,
			Map<EObject, Fingerprint> fingerprintsByObject, EditionDistance editionDistance,
			Set<Match> parentsToProcess, Monitor monitor) {
		return new CandidateMatcher(comparison, fingerprintsByObject, editionDistance, parentsToProcess,
				monitor);
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
		final Set<EObject> scopedObjects = Collections.newSetFromMap(new IdentityHashMap<>());
		scopedObjects.addAll(objects);
		for (final EObject object : objects) {
			EObject parent = object.eContainer();
			if (!scopedObjects.contains(parent)) {
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
			Map<CandidateGroupKey, String> previousNameByFamily = new HashMap<>();
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
					String previousName = previousNameByFamily.put(familyKey, anchor);
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
	}

}
