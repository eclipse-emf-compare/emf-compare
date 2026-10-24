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

import com.google.common.hash.Hashing;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.match.eobject.WeightProvider;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * Captures model content once per comparison and derives three kinds of matching evidence:
 * <ul>
 * <li>a semantic anchor, by default the stored, non-empty name of the object;</li>
 * <li>exact content containing normalized feature values, used to verify hash collisions;</li>
 * <li>a weighted 64-bit similarity signature, computed only when matching needs approximate candidates.</li>
 * </ul>
 * <p>
 * This implementation is inspired by MoHash's EObjectSimHasher and WeightedEHasherTable. Text contributes
 * words and overlapping character triples. Containments contribute their captured child content and
 * signatures. References contribute a qualified key for exact matching and local target text for similarity,
 * so moving a target does not discard all evidence of its identity.
 * </p>
 * <p>
 * Model values and reference keys are captured without resolving proxies. Similarity computations use only
 * those captured values, even if the model changes before the signature is requested. Derived, transient and
 * volatile features are excluded by default. Feature selection affects matching; the configured diff engine
 * decides which matched features produce differences. Non-text attribute scalars are captured through their
 * datatype factory's serialization, which must be stable and lossless for custom datatypes.
 * </p>
 * <p>
 * Subclasses can customize {@link #featuresOf(EClass)}, {@link #anchor(EObject)},
 * {@link #getCustomKeyForElement(EObject)} and {@link #contributesCustomReferenceSegment(EObject)}. The matcher
 * calls {@link #initialize(Comparison)} before every comparison to supply resource correspondence and reset caches.
 * Direct users must also call initialize after changing a model before asking for new snapshots.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class ContentHasher implements IContentHasher {

	/** Feature-weight providers for the current matcher. */
	private final WeightProvider.Descriptor.Registry weightProviders;

	/** Selected features, weights and per-feature text caches, indexed by metaclass. */
	private final Map<EClass, List<WeightedFeature>> weightedFeaturesByClass = new HashMap<>();

	/** Hashes reused for common scalar values and feature names. */
	private final Map<String, Long> scalarAndFeatureHashes = new HashMap<>();

	/** Similarity signatures of reference target text, shared by deferred fingerprint computations. */
	private final Map<String, Long> referenceSignaturesByDescriptor = new ConcurrentHashMap<>();

	/** Reference snapshots indexed by target identity; equal names alone do not identify an object. */
	private final Map<EObject, CapturedReference> referenceKeysByTarget = new IdentityHashMap<>();

	/** Captured fingerprints indexed by EObject identity, independent of any domain-specific equals(). */
	private final Map<EObject, Fingerprint> fingerprintsByObject = new IdentityHashMap<>();

	/** Interned exact content: equal feature snapshots share one list after full equality checks. */
	private final Map<List<FeatureValue>, List<FeatureValue>> sharedExactContents = new HashMap<>();

	/** Resource keys derived from the comparison's existing resource matches. */
	private ResourceMatchKeyIndex resourceKeys = ResourceMatchKeyIndex.of(Collections.emptyList());

	/**
	 * Creates a content hasher using the supplied feature weights.
	 *
	 * @param weightProviders
	 *            the feature-weight provider registry
	 */
	public ContentHasher(final WeightProvider.Descriptor.Registry weightProviders) {
		this.weightProviders = weightProviders;
	}

	/**
	 * Starts a new snapshot and resource-key index, discarding caches from the preceding comparison. Already
	 * returned fingerprints retain their captured content and deferred inputs.
	 *
	 * @param comparison
	 *            the new comparison, including its resource correspondence
	 */
	@Override
	public void initialize(Comparison comparison) {
		resourceKeys = ResourceMatchKeyIndex.of(comparison.getMatchedResources());
		weightedFeaturesByClass.clear();
		scalarAndFeatureHashes.clear();
		referenceSignaturesByDescriptor.clear();
		referenceKeysByTarget.clear();
		fingerprintsByObject.clear();
		sharedExactContents.clear();
	}

	/**
	 * Returns the current comparison's snapshot for an object, capturing it only on its first request.
	 * Selected containment children are captured before their owner using an explicit stack, so deeply nested
	 * models do not consume the Java call stack. Similarity itself remains deferred.
	 *
	 * @param object
	 *            the model object whose stored features should contribute to matching
	 * @return its cached or newly captured fingerprint
	 */
	public Fingerprint fingerprint(final EObject object) {
		Fingerprint cachedFingerprint = this.fingerprintsByObject.get(object);
		if (cachedFingerprint == null) {
			// Capture children before building their owner, without recursive containment traversal.
			Deque<PendingFingerprint> pendingSnapshots = new ArrayDeque<>();
			pendingSnapshots.push(captureFeatureValues(object));
			while (!pendingSnapshots.isEmpty()) {
				PendingFingerprint currentSnapshot = pendingSnapshots.peek();
				if (currentSnapshot.nextChildIndex < currentSnapshot.containedObjects.size()) {
					EObject child = currentSnapshot.containedObjects.get(currentSnapshot.nextChildIndex++);
					if (!fingerprintsByObject.containsKey(child)) {
						pendingSnapshots.push(captureFeatureValues(child));
					}
				} else {
					fingerprintsByObject.put(currentSnapshot.object, buildFingerprint(currentSnapshot));
					pendingSnapshots.pop();
				}
			}
			cachedFingerprint = this.fingerprintsByObject.get(object);
		}
		return cachedFingerprint;
	}

	/**
	 * Reads the selected raw feature values and collects the non-proxy containments needed by the owner.
	 * These temporary values are normalized into a fingerprint after the contained objects have been
	 * captured. Reference lists are read through their non-resolving basic view.
	 *
	 * @param object
	 *            the model object being captured
	 * @return the owner's raw feature values and the contained objects still requiring fingerprints
	 */
	private PendingFingerprint captureFeatureValues(EObject object) {
		PendingFingerprint capturedObject = new PendingFingerprint(object,
				weightedFeaturesByClass.computeIfAbsent(object.eClass(), this::weightedFeaturesForClass));
		for (int i = 0; i < capturedObject.weightedFeatures.size(); i++) {
			WeightedFeature weightedFeature = capturedObject.weightedFeatures.get(i);
			Object rawValue = object.eGet(weightedFeature.feature, false);
			if (rawValue instanceof List<?>) {
				List<?> unresolvedValues = (List<?>)rawValue;
				// eGet(..., false) can still return a resolving list; its basic view keeps proxies intact.
				if (rawValue instanceof InternalEList<?>) {
					unresolvedValues = ((InternalEList<?>)rawValue).basicList();
				}
				rawValue = unresolvedValues;
				for (Object item : unresolvedValues) {
					accumulateContainedObjects(weightedFeature.feature, item,
							capturedObject.containedObjects);
				}
			} else {
				accumulateContainedObjects(weightedFeature.feature, rawValue,
						capturedObject.containedObjects);
			}
			capturedObject.featureValues[i] = rawValue;
		}
		return capturedObject;
	}

	/**
	 * Adds a non-proxy containment target to the dependency list, unwrapping feature-map entries first.
	 * References and containment proxies contribute reference keys instead and must not be traversed.
	 *
	 * @param feature
	 *            the feature holding the value, or the outer feature-map feature
	 * @param value
	 *            the raw stored value, possibly a feature-map entry
	 * @param containedObjects
	 *            accumulator of contained objects whose fingerprints are needed before their owner's
	 */
	private static void accumulateContainedObjects(EStructuralFeature feature, Object value,
			List<EObject> containedObjects) {
		EStructuralFeature effectiveFeature = feature;
		Object containedValue = value;
		while (containedValue instanceof FeatureMap.Entry) {
			FeatureMap.Entry entry = (FeatureMap.Entry)containedValue;
			effectiveFeature = entry.getEStructuralFeature();
			containedValue = entry.getValue();
		}
		if (containedValue instanceof EObject && !((EObject)containedValue).eIsProxy()
				&& effectiveFeature instanceof EReference && ((EReference)effectiveFeature).isContainment()) {
			containedObjects.add((EObject)containedValue);
		}
	}

	/**
	 * Captured feature values waiting for their owned fingerprints.
	 *
	 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
	 */
	private static final class PendingFingerprint {
		/** Object associated with the completed snapshot. */
		private final EObject object;

		/** Selected, nonresolving feature values. */
		private final Object[] featureValues;

		/** Cached metadata shared by all objects with this metaclass. */
		private final List<WeightedFeature> weightedFeatures;

		/** Selected owned objects in feature order. */
		private final List<EObject> containedObjects = new ArrayList<>();

		/** Next owned object to capture. */
		private int nextChildIndex;

		/**
		 * Retains one object's raw values while its selected containments are captured.
		 *
		 * @param object
		 *            the model object associated with the eventual fingerprint
		 * @param weightedFeatures
		 *            the ordered selected features and their hashing metadata
		 */
		private PendingFingerprint(EObject object, List<WeightedFeature> weightedFeatures) {
			this.object = object;
			this.weightedFeatures = weightedFeatures;
			this.featureValues = new Object[weightedFeatures.size()];
		}
	}

	/**
	 * Builds exact content and deferred similarity inputs after the selected children have fingerprints.
	 * Lists are copied into normalized values. Equivalent exact content is shared across objects, while the
	 * similarity supplier retains captured feature values and child fingerprints instead of live model reads.
	 *
	 * @param capturedObject
	 *            the owner's raw values with its containment dependencies already captured
	 * @return the fingerprint with exact content and a deferred similarity computation
	 */
	private Fingerprint buildFingerprint(final PendingFingerprint capturedObject) {
		final List<FeatureValue> exactContent = new ArrayList<>();
		final List<CapturedFeature> capturedFeatures = new ArrayList<>();
		final List<Fingerprint> childFingerprints = new ArrayList<>();
		for (int i = 0; i < capturedObject.weightedFeatures.size(); i++) {
			final WeightedFeature weightedFeature = capturedObject.weightedFeatures.get(i);
			final EStructuralFeature feature = weightedFeature.feature;
			final Object rawValue = capturedObject.featureValues[i];
			final List<Object> normalizedValues;
			final Object capturedSimilarityValue;
			if (rawValue instanceof List<?>) {
				List<?> rawValues = (List<?>)rawValue;
				if (rawValues.isEmpty()) {
					normalizedValues = Collections.emptyList();
					capturedSimilarityValue = null;
				} else {
					final List<Object> exactValues = new ArrayList<>(rawValues.size());
					final List<Object> similarityValues = new ArrayList<>(rawValues.size());
					for (Object item : rawValues) {
						exactValues.add(this.captureExactValue(feature, item));
						similarityValues.add(this.captureSimilarityValue(feature, item, childFingerprints));
					}
					normalizedValues = Collections.unmodifiableList(exactValues);
					capturedSimilarityValue = similarityValues;
				}
			} else {
				normalizedValues = Collections.singletonList(this.captureExactValue(feature, rawValue));
				capturedSimilarityValue = this.captureSimilarityValue(feature, rawValue, childFingerprints);
			}
			exactContent.add(new FeatureValue(feature, normalizedValues));
			if (capturedSimilarityValue != null) {
				capturedFeatures.add(new CapturedFeature(weightedFeature, capturedSimilarityValue));
			}
		}
		List<FeatureValue> immutableContent = Collections.unmodifiableList(exactContent);
		// Share equal immutable subtrees so repeated exact checks stop at the shared snapshot.
		final List<FeatureValue> sharedContent = sharedExactContents.putIfAbsent(immutableContent,
				immutableContent);
		final Map<String, Long> referenceSignatureCache = referenceSignaturesByDescriptor;
		if (sharedContent != null) {
			immutableContent = sharedContent;
		}
		return new Fingerprint(anchor(capturedObject.object), immutableContent,
				() -> computeSimilaritySignature(capturedFeatures, referenceSignatureCache),
				childFingerprints);
	}

	/**
	 * Combines captured feature contributions into a weighted 64-bit signature without reading the model.
	 * Each feature receives a fixed vote budget; text length and collection size do not multiply its weight.
	 * Text contributions and reference descriptors are reused when they occur in multiple fingerprints.
	 *
	 * @param capturedFeatures
	 *            captured values together with their feature weights and bit masks
	 * @param referenceSignatureCache
	 *            shared signatures for local reference-target text
	 * @return the signature formed by positive accumulated bit votes
	 */
	private static long computeSimilaritySignature(List<CapturedFeature> capturedFeatures,
			Map<String, Long> referenceSignatureCache) {
		final SimilarityAccumulator accumulator = new SimilarityAccumulator();
		for (CapturedFeature feature : capturedFeatures) {
			final WeightedFeature weightedFeature = feature.weightedFeature;
			if (feature.capturedValue instanceof String) {
				accumulator.addCachedFeatureContribution(weightedFeature.textContributions
						.computeIfAbsent((String)feature.capturedValue, text -> {
							SimilarityAccumulator featureAccumulator = new SimilarityAccumulator();
							featureAccumulator.startFeature(weightedFeature.featureHash);
							featureAccumulator.addTextTokens(text, weightedFeature.featureHash);
							featureAccumulator.finishFeature(weightedFeature.weight);
							return featureAccumulator.finishedFeatureContribution();
						}));
			} else {
				accumulator.startFeature(weightedFeature.featureHash);
				if (feature.capturedValue instanceof List<?>) {
					for (Object value : (List<?>)feature.capturedValue) {
						addCapturedValue(accumulator, weightedFeature.featureHash, value,
								referenceSignatureCache);
					}
				} else {
					addCapturedValue(accumulator, weightedFeature.featureHash, feature.capturedValue,
							referenceSignatureCache);
				}
				accumulator.finishFeature(weightedFeature.weight);
			}
		}
		return accumulator.signature();
	}

	/**
	 * Captures a feature value in the form needed by deferred similarity. Strings remain text, owned objects
	 * become child fingerprints, references become captured reference snapshots, and other scalar values are
	 * hashed immediately. Feature-map entries retain their own feature's hash and recursively captured value.
	 *
	 * @param feature
	 *            the effective structural feature of this value
	 * @param rawValue
	 *            the stored value to capture before returning the owner's fingerprint
	 * @param childFingerprints
	 *            accumulator of owned fingerprints that must be evaluated before the owner's signature
	 * @return a captured text, scalar hash, child fingerprint, reference snapshot or feature-map entry
	 */
	private Object captureSimilarityValue(EStructuralFeature feature, Object rawValue,
			List<Fingerprint> childFingerprints) {
		Object capturedValue;
		if (rawValue instanceof FeatureMap.Entry) {
			FeatureMap.Entry entry = (FeatureMap.Entry)rawValue;
			EStructuralFeature entryFeature = entry.getEStructuralFeature();
			long featureHash = scalarAndFeatureHashes.computeIfAbsent(entryFeature.getName(),
					name -> Hashing.murmur3_128().hashUnencodedChars(name).asLong());
			capturedValue = new CapturedFeatureMapEntry(featureHash,
					captureSimilarityValue(entryFeature, entry.getValue(), childFingerprints));
		} else if (rawValue instanceof EObject) {
			EObject target = (EObject)rawValue;
			if (!target.eIsProxy() && feature instanceof EReference
					&& ((EReference)feature).isContainment()) {
				Fingerprint child = fingerprint(target);
				childFingerprints.add(child);
				capturedValue = child;
			} else {
				capturedValue = referenceKeysByTarget.computeIfAbsent(target, this::captureReference);
			}
		} else if (rawValue instanceof String) {
			capturedValue = rawValue;
		} else {
			capturedValue = hashScalarValue(feature, rawValue);
		}
		return capturedValue;
	}

	/**
	 * Hashes a non-text scalar together with its feature name. Null, boolean and enum values recur
	 * frequently, so their hashes are cached across captured objects.
	 *
	 * @param feature
	 *            the structural feature whose name distinguishes the scalar contribution
	 * @param scalarValue
	 *            the stored scalar value, including null
	 * @return the scalar contribution's hash
	 */
	private Long hashScalarValue(EStructuralFeature feature, Object scalarValue) {
		if (scalarValue instanceof byte[] && feature instanceof EAttribute) {
			// Array identity must not make equal binary attribute values look dissimilar.
			scalarValue = EcoreUtil.convertToString(((EAttribute)feature).getEAttributeType(), scalarValue);
		}
		String scalarText = feature.getName() + ":" + scalarValue; //$NON-NLS-1$
		if (scalarValue == null || scalarValue instanceof Boolean || scalarValue instanceof Enum<?>) {
			return scalarAndFeatureHashes.computeIfAbsent(scalarText,
					key -> Hashing.murmur3_128().hashUnencodedChars(key).asLong());
		}
		return Hashing.murmur3_128().hashUnencodedChars(scalarText).asLong();
	}

	/**
	 * Adds one captured value to the current feature's similarity votes. Child signatures and reference
	 * descriptors are mixed with the referring feature's hash. Feature-map entries instead use the hash of
	 * their own effective feature. No branch reads a live model object.
	 *
	 * @param accumulator
	 *            the vote accumulator for the current object and feature
	 * @param featureHash
	 *            the effective feature-name hash used to distinguish token contributions
	 * @param capturedValue
	 *            a value previously produced by {@link #captureSimilarityValue}
	 * @param referenceSignatureCache
	 *            cached signatures of local reference-target descriptions
	 */
	private static void addCapturedValue(SimilarityAccumulator accumulator, long featureHash,
			Object capturedValue, Map<String, Long> referenceSignatureCache) {
		if (capturedValue instanceof CapturedFeatureMapEntry) {
			CapturedFeatureMapEntry entry = (CapturedFeatureMapEntry)capturedValue;
			addCapturedValue(accumulator, entry.featureHash, entry.capturedValue, referenceSignatureCache);
		} else if (capturedValue instanceof Fingerprint) {
			accumulator.addToken(((Fingerprint)capturedValue).similarity() ^ featureHash);
		} else if (capturedValue instanceof CapturedReference) {
			CapturedReference referenceSnapshot = (CapturedReference)capturedValue;
			long referenceSignature = referenceSignatureCache
					.computeIfAbsent(referenceSnapshot.similarityText, text -> {
						SimilarityAccumulator descriptorAccumulator = new SimilarityAccumulator();
						// Hash target text on all bits, then mix in the referring feature's hash below.
						descriptorAccumulator.startFeature(-1L);
						descriptorAccumulator.addTextTokens(text, 0L);
						descriptorAccumulator.finishFeature(1);
						return descriptorAccumulator.signature();
					});
			accumulator.addToken(referenceSignature ^ featureHash);
		} else if (capturedValue instanceof String) {
			accumulator.addTextTokens((String)capturedValue, featureHash);
		} else {
			accumulator.addToken((Long)capturedValue);
		}
	}

	/**
	 * One selected feature's hashing metadata and captured input for deferred similarity.
	 *
	 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
	 */
	private static final class CapturedFeature {
		/** Captured feature weight and signature mask. */
		private final WeightedFeature weightedFeature;

		/** Captured value used without rereading the model. */
		private final Object capturedValue;

		/**
		 * Retains a feature's hashing metadata alongside its captured similarity input.
		 *
		 * @param weightedFeature
		 *            the structural feature, weight, mask and reusable text cache
		 * @param capturedValue
		 *            the captured value or list of values consumed when similarity is requested
		 */
		private CapturedFeature(WeightedFeature weightedFeature, Object capturedValue) {
			this.weightedFeature = weightedFeature;
			this.capturedValue = capturedValue;
		}
	}

	/**
	 * The effective feature hash and captured value of one feature-map entry.
	 *
	 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
	 */
	private static final class CapturedFeatureMapEntry {
		/** Effective feature-name hash mixed into the entry value's token contributions. */
		private final long featureHash;

		/** Captured value used without rereading the model. */
		private final Object capturedValue;

		/**
		 * Retains the effective feature's hash when its value is stored in a feature map.
		 *
		 * @param featureHash
		 *            the hash of the entry's effective feature name
		 * @param capturedValue
		 *            the entry value already captured for deferred similarity
		 */
		private CapturedFeatureMapEntry(long featureHash, Object capturedValue) {
			this.featureHash = featureHash;
			this.capturedValue = capturedValue;
		}
	}

	/**
	 * Captures the selected features' weights, feature-name hashes and reusable text caches. Included
	 * containments retain a positive vote budget even when their weight provider returns zero.
	 *
	 * @param type
	 *            the metaclass whose selected features are being prepared
	 * @return the ordered hashing metadata used by every captured instance of this class
	 */
	private List<WeightedFeature> weightedFeaturesForClass(final EClass type) {
		final WeightProvider provider = weightProviders.getHighestRankingWeightProvider(type.getEPackage());
		return featuresOf(type).stream()
				.map(feature -> new WeightedFeature(feature, Math.max(1, provider.getWeight(feature))))
				.collect(Collectors.toList());
	}

	/**
	 * Per-metaclass feature metadata and reusable normalized text contributions.
	 *
	 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
	 */
	private static final class WeightedFeature {
		/** Containment or stored structural feature. */
		private final EStructuralFeature feature;

		/** Captured feature weight. */
		private final int weight;

		/** Feature-name hash used as both a token salt and the mask selecting its voting bits. */
		private final long featureHash;

		/** Immutable text vote vectors reused across deferred fingerprints. */
		private final Map<String, double[]> textContributions = new ConcurrentHashMap<>();

		/**
		 * Retains one feature's positive weight, feature-name hash and text contribution cache.
		 *
		 * @param feature
		 *            the selected structural feature
		 * @param weight
		 *            its positive vote budget in an object's similarity signature
		 */
		private WeightedFeature(EStructuralFeature feature, int weight) {
			this.feature = feature;
			this.weight = weight;
			// Each feature votes on its own subset of bits, so a strong name cannot silence every other
			// feature.
			this.featureHash = Hashing.murmur3_128().hashUnencodedChars(feature.getName()).asLong();
		}
	}

	/**
	 * Selects the stored features that contribute to exact content and similarity. The default skips derived,
	 * transient, volatile and container references, then retains positive-weight features and containments.
	 * Subclasses can change this selection; values are still read without proxy resolution by the capture
	 * code.
	 *
	 * @param type
	 *            the metaclass whose feature selection can be customized
	 * @return the ordered features to capture for instances of this class
	 */
	protected List<EStructuralFeature> featuresOf(final EClass type) {
		final WeightProvider provider = this.weightProviders
				.getHighestRankingWeightProvider(type.getEPackage());
		return type.getEAllStructuralFeatures().stream()
				.filter(feature -> !feature.isDerived() && !feature.isTransient() && !feature.isVolatile())
				.filter(feature -> !(feature instanceof EReference && ((EReference)feature).isContainer()))
				.filter(feature -> provider.getWeight(feature) > 0
						|| feature instanceof EReference && ((EReference)feature).isContainment())
				.collect(Collectors.toList());
	}

	/**
	 * Normalizes one value for exact-content equality without resolving references. Containments use the
	 * already captured child's exact content. References use qualified resource and semantic keys.
	 * Feature-map entries keep their effective feature alongside their normalized value. Attribute scalars
	 * use immutable datatype keys; custom datatype factories must provide stable, lossless serialization.
	 *
	 * @param feature
	 *            the effective feature holding this value
	 * @param rawValue
	 *            the stored scalar, object or feature-map entry
	 * @return the value used in the owner's exact feature snapshot
	 */
	private Object captureExactValue(final EStructuralFeature feature, final Object rawValue) {
		Object capturedValue = rawValue;
		if (rawValue instanceof FeatureMap.Entry) {
			final FeatureMap.Entry entry = (FeatureMap.Entry)rawValue;
			capturedValue = new FeatureValue(entry.getEStructuralFeature(), Collections
					.singletonList(this.captureExactValue(entry.getEStructuralFeature(), entry.getValue())));
		} else if (rawValue instanceof EObject) {
			final EObject target = (EObject)rawValue;
			if (!target.eIsProxy() && feature instanceof EReference
					&& ((EReference)feature).isContainment()) {
				capturedValue = this.fingerprint(target).content();
			} else {
				capturedValue = this.referenceKeysByTarget.computeIfAbsent(target,
						this::captureReference).qualifiedKey;
			}
		} else {
			capturedValue = FeatureValue.captureScalarValue(feature, rawValue);
		}
		return capturedValue;
	}

	/**
	 * Captures both the qualified exact key and local similarity text of a referenced object. The exact key
	 * includes resource correspondence and selected container name segments. Similarity uses the target's own
	 * semantic segment, or its metaclass when unnamed, so container moves do not destroy all similarity
	 * evidence. A proxy contributes its normalized URI; proxy containers terminate the walk without
	 * resolution.
	 *
	 * @param target
	 *            the referenced object or unresolved proxy
	 * @return its exact key and local description, captured without future model reads
	 */
	private CapturedReference captureReference(final EObject target) {
		final List<Object> qualifiedKey = new ArrayList<>();
		Object localDescriptor = target.eClass().getName();
		if (target.eIsProxy()) {
			localDescriptor = resourceKeys.key(((InternalEObject)target).eProxyURI());
			qualifiedKey.add(localDescriptor);
		} else {
			if (target.eResource() != null) {
				qualifiedKey.add(resourceKeys.key(target.eResource()));
			}
			for (InternalEObject containerOrTarget = (InternalEObject)target; containerOrTarget != null; containerOrTarget = containerOrTarget
					.eInternalContainer()) {
				if (containerOrTarget.eIsProxy()) {
					qualifiedKey.add(resourceKeys.key(containerOrTarget.eProxyURI()));
					break;
				}
				qualifiedKey.add(containerOrTarget.eClass());
				if (contributesCustomReferenceSegment(containerOrTarget)) {
					String semanticSegment = getCustomKeyForElement(containerOrTarget);
					qualifiedKey.add(semanticSegment);
					if (containerOrTarget == target && semanticSegment != null) {
						localDescriptor = semanticSegment;
					}
				}
			}
		}
		return new CapturedReference(Collections.unmodifiableList(qualifiedKey),
				String.valueOf(localDescriptor));
	}

	/**
	 * A qualified exact reference key and the local text retained for edit-tolerant similarity.
	 *
	 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
	 */
	private static final class CapturedReference {
		/** Resource, metaclass and semantic segments used to distinguish exact reference targets. */
		private final List<Object> qualifiedKey;

		/** Local target text used for similarity, deliberately excluding named-container paths. */
		private final String similarityText;

		/**
		 * Retains the two representations needed to compare a reference target.
		 *
		 * @param qualifiedKey
		 *            the immutable resource, type and semantic-path segments for exact equality
		 * @param similarityText
		 *            the target's own semantic text, metaclass name or normalized proxy key
		 */
		private CapturedReference(List<Object> qualifiedKey, String similarityText) {
			this.qualifiedKey = qualifiedKey;
			this.similarityText = similarityText;
		}
	}

	/**
	 * Supplies a semantic path segment for a referenced object or one of its containers. The default uses
	 * {@link #anchor(EObject)}. The target's own non-null segment also becomes its local similarity text;
	 * container segments contribute only to the qualified exact reference key.
	 *
	 * @param object
	 *            the referenced object or container selected by
	 *            {@link #contributesCustomReferenceSegment(EObject)}
	 * @return the custom semantic segment, or null when no segment is available
	 */
	protected String getCustomKeyForElement(EObject object) {
		return anchor(object);
	}

	/**
	 * Selects which referenced objects and containers contribute custom semantic path segments. The default
	 * accepts any object with a non-empty anchor, regardless of its EPackage. Subclasses can restrict this
	 * hook to their domain metamodel; accepted objects are passed to
	 * {@link #getCustomKeyForElement(EObject)}.
	 *
	 * @param object
	 *            the referenced object or container currently visited in the qualified-key walk
	 * @return whether its custom segment should be included in the exact reference key
	 */
	protected boolean contributesCustomReferenceSegment(EObject object) {
		return anchor(object) != null;
	}

	/**
	 * Reads an object's stored, non-empty name as its semantic anchor, without resolving references. The
	 * default accepts a single-valued string EAttribute named name, including an ID attribute. Derived,
	 * transient and volatile name features are ignored. Subclasses may supply another semantic identity.
	 *
	 * @param object
	 *            the model object whose anchor is requested
	 * @return the semantic anchor, or null when no usable stored name is available
	 */
	protected String anchor(final EObject object) {
		final EStructuralFeature name = object.eClass().getEStructuralFeature("name"); //$NON-NLS-1$
		if (name == null || name.isMany() || !(name instanceof EAttribute)) {
			return null;
		}
		if (!name.isDerived() && !name.isTransient() && !name.isVolatile()) {
			final Object value = object.eGet(name, false);
			if (value instanceof String && !((String)value).isEmpty()) {
				return (String)value;
			}
		}
		return null;
	}

	/**
	 * Accumulates bounded feature contributions while preserving overlap in text and child signatures.
	 *
	 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
	 */
	private static final class SimilarityAccumulator {

		/** Accumulated normalized object votes. */
		private final double[] objectVotes = new double[Long.SIZE];

		/** Weighted token votes for the current feature. */
		private final long[] featureVotes = new long[Long.SIZE];

		/** Bits on which the current feature contributes. */
		private long featureBitMask;

		/** Total token weight of the current feature. */
		private long totalTokenWeight;

		/** Lone token retained until a second token requires per-bit feature voting. */
		private long singleTokenHash;

		/** Creates an accumulator for an object or cached feature contribution. */
		private SimilarityAccumulator() {
		}

		/**
		 * Starts accumulating a new feature's tokens on its assigned subset of signature bits. The previous
		 * feature must have been finished before starting another.
		 *
		 * @param bitMask
		 *            the bit positions on which this feature is allowed to vote
		 */
		private void startFeature(long bitMask) {
			featureBitMask = bitMask;
			totalTokenWeight = 0;
		}

		/**
		 * Adds one token with unit relative weight to the current feature.
		 *
		 * @param tokenHash
		 *            the token's 64-bit hash
		 */
		private void addToken(long tokenHash) {
			addToken(tokenHash, 1);
		}

		/**
		 * Adds a weighted token to the current feature, delaying per-bit staging for a lone scalar token.
		 *
		 * @param tokenHash
		 *            the token's 64-bit hash
		 * @param tokenWeight
		 *            the token's relative weight within this feature, before feature-wide normalization
		 */
		private void addToken(long tokenHash, int tokenWeight) {
			// Most stored features are scalar; avoid staging a vector for a single token.
			if (totalTokenWeight == 0 && tokenWeight == 1) {
				singleTokenHash = tokenHash;
				totalTokenWeight = 1;
				return;
			}
			if (totalTokenWeight == 0 || totalTokenWeight == 1) {
				Arrays.fill(featureVotes, 0);
				if (totalTokenWeight == 1) {
					accumulateTokenVotes(singleTokenHash, 1);
				}
			}
			accumulateTokenVotes(tokenHash, tokenWeight);
			totalTokenWeight += tokenWeight;
		}

		/**
		 * Adds weighted positive or negative votes on the current feature's active bit positions.
		 *
		 * @param tokenHash
		 *            the token's 64-bit hash
		 * @param tokenWeight
		 *            its relative vote weight within the current feature
		 */
		private void accumulateTokenVotes(long tokenHash, int tokenWeight) {
			for (long bits = featureBitMask; bits != 0; bits &= bits - 1) {
				int bit = Long.numberOfTrailingZeros(bits);
				int contribution = -tokenWeight;
				if ((tokenHash & (1L << bit)) != 0) {
					contribution = tokenWeight;
				}
				featureVotes[bit] += contribution;
			}
		}

		/**
		 * Adds words, identifier parts and overlapping character triples as evidence for one text value.
		 * Words split at punctuation, digit transitions, camel case and acronym boundaries. Short text uses
		 * its whole value when there are no character triples.
		 *
		 * @param text
		 *            the captured text to tokenize
		 * @param featureHash
		 *            the feature-name hash mixed into word and character-triple hashes
		 */
		private void addTextTokens(String text, long featureHash) {
			int wordStart = -1;
			for (int characterIndex = 0; characterIndex <= text.length(); characterIndex++) {
				if (characterIndex == text.length()
						|| !Character.isLetterOrDigit(text.charAt(characterIndex))) {
					if (wordStart >= 0) {
						addWord(text, wordStart, characterIndex, featureHash);
						wordStart = -1;
					}
				} else if (wordStart < 0) {
					wordStart = characterIndex;
				} else if (isWordBoundary(text, characterIndex)) {
					addWord(text, wordStart, characterIndex, featureHash);
					wordStart = characterIndex;
				}
			}
			if (text.length() < 3) {
				addToken(Hashing.murmur3_128().hashUnencodedChars(text).asLong() ^ featureHash);
			} else {
				for (int characterIndex = 0; characterIndex < text.length() - 2; characterIndex++) {
					long characterTriple = (long)text.charAt(characterIndex) << Integer.SIZE
							| (long)text.charAt(characterIndex + 1) << Character.SIZE
							| text.charAt(characterIndex + 2);
					addToken(Hashing.murmur3_128().hashLong(characterTriple ^ featureHash).asLong());
				}
			}
		}

		/**
		 * Detects digit, camel-case and acronym boundaries within an identifier.
		 *
		 * @param text
		 *            the captured text
		 * @param index
		 *            the character after a possible boundary
		 * @return whether a new identifier word starts at the index
		 */
		private static boolean isWordBoundary(String text, int index) {
			char previous = text.charAt(index - 1);
			char current = text.charAt(index);
			if (Character.isDigit(previous) != Character.isDigit(current)) {
				return true;
			}
			if (Character.isLowerCase(previous) && Character.isUpperCase(current)) {
				return true;
			}
			return Character.isUpperCase(previous) && Character.isUpperCase(current)
					&& index + 1 < text.length() && Character.isLowerCase(text.charAt(index + 1));
		}

		/**
		 * Adds a word with a weight balanced against all character triples in the containing text.
		 *
		 * @param text
		 *            the captured text containing the word
		 * @param wordStart
		 *            the inclusive character index of the word
		 * @param wordEnd
		 *            the exclusive character index of the word
		 * @param featureHash
		 *            the feature-name hash mixed into the word's hash
		 */
		private void addWord(String text, int wordStart, int wordEnd, long featureHash) {
			// Each word or identifier part carries as much evidence as all character fragments together.
			addToken(Hashing.murmur3_128().hashUnencodedChars(text.substring(wordStart, wordEnd)).asLong()
					^ featureHash, Math.max(1, text.length() - 2));
		}

		/**
		 * Normalizes the feature's token votes to its fixed weight and adds them to the object's votes. This
		 * keeps long text and large collections from overwhelming the contributions of other features.
		 *
		 * @param featureWeight
		 *            the feature's overall vote budget, independent of token count
		 */
		private void finishFeature(int featureWeight) {
			if (totalTokenWeight == 0) {
				return;
			}
			// A feature has a fixed vote budget, regardless of text length or collection size.
			double normalizationScale = (double)featureWeight / totalTokenWeight;
			for (long bits = featureBitMask; bits != 0; bits &= bits - 1) {
				int bit = Long.numberOfTrailingZeros(bits);
				double contribution;
				if (totalTokenWeight == 1) {
					contribution = -featureWeight;
					if ((singleTokenHash & (1L << bit)) != 0) {
						contribution = featureWeight;
					}
				} else {
					contribution = normalizationScale * featureVotes[bit];
				}
				objectVotes[bit] += contribution;
			}
		}

		/**
		 * Returns the completed feature vote vector for caching without copying it. The caller must finish
		 * the feature first and never modify this accumulator after sharing its vector.
		 *
		 * @return the normalized per-bit contribution, which callers must keep immutable
		 */
		private double[] finishedFeatureContribution() {
			return objectVotes;
		}

		/**
		 * Adds a previously normalized, immutable feature contribution to the object's accumulated votes.
		 *
		 * @param contribution
		 *            the cached per-bit votes for one feature value
		 */
		private void addCachedFeatureContribution(double[] contribution) {
			for (int bit = 0; bit < Long.SIZE; bit++) {
				objectVotes[bit] += contribution[bit];
			}
		}

		/**
		 * Builds the signature from bits whose accumulated votes are positive.
		 *
		 * @return the 64-bit signature
		 */
		private long signature() {
			long signatureBits = 0;
			for (int bit = 0; bit < Long.SIZE; bit++) {
				if (objectVotes[bit] > 0) {
					signatureBits |= 1L << bit;
				}
			}
			return signatureBits;
		}
	}

}
