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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.function.LongSupplier;

/**
 * Immutable content snapshot. Exact values verify hash collisions; similarity may be computed lazily from
 * captured values and remains stable after model edits.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class Fingerprint {

	/** Combines the anchor, exact content and similarity hashes. */
	private static final int HASH_PRIME = 31;

	/** Semantic anchor supplied by the hasher, or null. */
	private final String anchor;

	/** Immutable exact content used for collision checks. */
	private final List<FeatureValue> content;

	/** Computed similarity signature. */
	private long similarity;

	/** Deferred signature computation, cleared after successful evaluation. */
	private volatile LongSupplier similarityComputation;

	/** Child fingerprints whose signatures must be computed before this one's. */
	private final List<Fingerprint> similarityDependencies;

	/**
	 * Creates a snapshot with an already computed similarity signature. The content list is copied; its
	 * feature values must already be immutable snapshots.
	 *
	 * @param anchor
	 *            the semantic name, or null
	 * @param content
	 *            the normalized exact content, constructed using {@link FeatureValue#of}
	 * @param similarity
	 *            the similarity signature
	 */
	public Fingerprint(String anchor, List<FeatureValue> content, long similarity) {
		this.anchor = anchor;
		this.content = Collections.unmodifiableList(new ArrayList<>(content));
		this.similarity = similarity;
		this.similarityComputation = null;
		this.similarityDependencies = Collections.emptyList();
	}

	/**
	 * Creates a snapshot whose signature uses captured values only, retaining canonical content without
	 * copying. The content, its nested values and the child dependency list must remain immutable after
	 * construction. The computation must use captured values and only the listed child fingerprints.
	 *
	 * @param anchor
	 *            the semantic name, or null
	 * @param content
	 *            the normalized exact content
	 * @param similarityComputation
	 *            the computation using captured values only
	 * @param children
	 *            the captured owned fingerprints required by the computation
	 */
	public Fingerprint(String anchor, List<FeatureValue> content, LongSupplier similarityComputation,
			List<Fingerprint> children) {
		this.anchor = anchor;
		this.content = content;
		this.similarityComputation = similarityComputation;
		this.similarityDependencies = children;
	}

	/**
	 * Returns the semantic anchor supplied by the hasher.
	 *
	 * @return the semantic anchor, or null
	 */
	public String anchor() {
		return anchor;
	}

	/**
	 * Returns the exact normalized content retained for collision checks.
	 *
	 * @return the exact content snapshot
	 */
	public List<FeatureValue> content() {
		return content;
	}

	/**
	 * Computes the weighted signature from captured values without rereading the model.
	 *
	 * @return the 64-bit similarity signature
	 */
	public long similarity() {
		if (similarityComputation != null) {
			if (similarityDependencies.isEmpty()) {
				computeSignatureOnce();
			} else {
				computeDependencySignatures();
			}
		}
		return similarity;
	}

	/** Evaluates children before their owner using an explicit stack, so deep models cannot overflow it. */
	private void computeDependencySignatures() {
		Deque<Fingerprint> pendingFingerprints = new ArrayDeque<>();
		pendingFingerprints.push(this);
		while (!pendingFingerprints.isEmpty()) {
			Fingerprint currentFingerprint = pendingFingerprints.peek();
			boolean dependenciesComputed = true;
			for (Fingerprint child : currentFingerprint.similarityDependencies) {
				if (child.similarityComputation != null) {
					pendingFingerprints.push(child);
					dependenciesComputed = false;
				}
			}
			if (dependenciesComputed) {
				currentFingerprint.computeSignatureOnce();
				pendingFingerprints.pop();
			}
		}
	}

	/**
	 * Publishes one computed signature without holding child locks. Keeping the supplier until it succeeds
	 * also allows a failed computation to be retried.
	 */
	private synchronized void computeSignatureOnce() {
		if (similarityComputation != null) {
			similarity = similarityComputation.getAsLong();
			similarityComputation = null;
		}
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Fingerprint)) {
			return false;
		}
		Fingerprint other = (Fingerprint)object;
		return similarity() == other.similarity() && Objects.equals(anchor, other.anchor)
				&& Objects.equals(content, other.content);
	}

	@Override
	public int hashCode() {
		return HASH_PRIME * (HASH_PRIME * Objects.hashCode(anchor) + Objects.hashCode(content))
				+ Long.hashCode(similarity());
	}

	@Override
	public String toString() {
		return "Fingerprint[anchor=" + anchor + ", content=" + content + ", similarity=" + similarity() + ']'; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
