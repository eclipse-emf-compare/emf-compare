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
package org.eclipse.emf.compare.tests.match;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.compare.match.hash.FeatureValue;
import org.eclipse.emf.compare.match.hash.Fingerprint;
import org.eclipse.emf.ecore.EcorePackage;
import org.junit.Test;

/**
 * Verifies {@link Fingerprint} as a value object used to retain matching evidence. Tests construct its
 * content and similarity inputs directly so failures identify a snapshot contract rather than a matching
 * decision.
 * <p>
 * The eager constructor must copy the supplied content list and expose it as unmodifiable. Equality must
 * include the anchor, exact content and similarity signature; equal values must have equal hash codes, and
 * signature bits above bit 31 must contribute to the hash. The known String collision FB/Ea checks that equal
 * hashes do not make different content equal.
 * </p>
 * <p>
 * An incrementing supplier checks that deferred similarity is evaluated on first access and reused on the
 * next access. This is a single-threaded lazy-evaluation check. Capturing live model values and selecting
 * object partners are tested separately by the hasher and matcher tests.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@SuppressWarnings("nls")
public class FingerprintTest {
	/** High signature bit needed to detect truncation to the low integer word. */
	private static final int HIGH_BIT_POSITION = 40;

	/** Clearing the caller's content list must not empty an already constructed fingerprint. */
	@Test
	public void constructorCopiesTheContentList() {
		FeatureValue name = nameValue("original");
		List<FeatureValue> content = new ArrayList<>(Collections.singletonList(name));
		Fingerprint snapshot = new Fingerprint(null, content, 0L);

		content.clear();

		assertEquals(Collections.singletonList(name), snapshot.content());
	}

	/** Equal content and similarity must produce equal values and hash codes. */
	@Test
	public void equalFingerprintsHaveEqualHashCodes() {
		Fingerprint first = new Fingerprint("anchor", Collections.singletonList(nameValue("name")), 1L);
		Fingerprint second = new Fingerprint("anchor", Collections.singletonList(nameValue("name")), 1L);
		assertEquals(first, second);
		assertEquals(first.hashCode(), second.hashCode());
	}

	/** FB and Ea have the same String hash; exact content must still distinguish them. */
	@Test
	public void hashCollisionsDoNotMakeDifferentContentEqual() {
		Fingerprint first = new Fingerprint(null, Collections.singletonList(nameValue("FB")), 0L);
		Fingerprint second = new Fingerprint(null, Collections.singletonList(nameValue("Ea")), 0L);
		assertEquals(first.hashCode(), second.hashCode());
		assertNotEquals(first, second);
	}

	/** A semantic anchor is part of fingerprint equality even when exact content is equal. */
	@Test
	public void differentAnchorsMakeFingerprintsUnequal() {
		assertNotEquals(new Fingerprint("first", Collections.emptyList(), 0L),
				new Fingerprint("second", Collections.emptyList(), 0L));
	}

	/** Similarity is part of fingerprint equality even when anchor and content are equal. */
	@Test
	public void differentSimilaritiesMakeFingerprintsUnequal() {
		assertNotEquals(new Fingerprint(null, Collections.emptyList(), 0L),
				new Fingerprint(null, Collections.emptyList(), 1L));
	}

	/** Changing a bit above bit 31 must remain visible in the fingerprint's hash code. */
	@Test
	public void hashCodeIncludesTheHighSimilarityBits() {
		Fingerprint zero = new Fingerprint(null, Collections.emptyList(), 0L);
		Fingerprint highBit = new Fingerprint(null, Collections.emptyList(), 1L << HIGH_BIT_POSITION);
		assertNotEquals(zero.hashCode(), highBit.hashCode());
	}

	/** The signature supplier must run only on the first access to similarity. */
	@Test
	public void similarityIsComputedOnFirstAccessAndThenReused() {
		AtomicInteger computations = new AtomicInteger();
		Fingerprint snapshot = new Fingerprint(null, Collections.emptyList(), computations::incrementAndGet,
				Collections.emptyList());
		assertEquals(0, computations.get());
		assertEquals(1L, snapshot.similarity());
		assertEquals(1L, snapshot.similarity());
		// A cached return value alone would not reveal a redundant supplier invocation.
		assertEquals(1, computations.get());
	}

	/** Fingerprint equality must reject null and values of other types. */
	@Test
	public void fingerprintsAreNotEqualToNullOrOtherTypes() {
		Fingerprint snapshot = new Fingerprint(null, Collections.emptyList(), 0L);
		assertNotEquals(snapshot, null);
		assertNotEquals(snapshot, "other type");
	}

	/** The content accessor must not allow callers to change a captured fingerprint. */
	@Test(expected = UnsupportedOperationException.class)
	public void capturedContentCannotBeModified() {
		Fingerprint snapshot = new Fingerprint(null, Collections.singletonList(nameValue("stored")), 0L);
		snapshot.content().clear();
	}

	/**
	 * Creates immutable name content for the value-equality tests.
	 *
	 * @param name
	 *            the captured name
	 * @return the name feature snapshot
	 */
	private static FeatureValue nameValue(String name) {
		return FeatureValue.of(EcorePackage.Literals.ENAMED_ELEMENT__NAME, Collections.singletonList(name));
	}
}
